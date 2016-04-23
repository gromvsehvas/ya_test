package gromvsehvas.ya_test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivityFirst extends AppCompatActivity {

    int nAnimCount = 0;
    ImageView imageView;
    TextView tvTextProgress;
    FrameLayout flProgressBar;
    ProgressBar ProcessBar;
    private Animation mFadeInAnimation, mFadeOutAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        // Делаем видимым ProcessBar
        ProcessBar =  (ProgressBar) findViewById(R.id.ProcessBar);
        assert ProcessBar != null;
        ProcessBar.setVisibility(View.VISIBLE);

        // Скачиваем файл, смена подписи и анимации
        ProcessDownloadFile();
    }

    private void ProcessDownloadFile(){
        flProgressBar = (FrameLayout) findViewById(R.id.flProgressBar);
        assert flProgressBar != null;
        flProgressBar.removeAllViews();
        flProgressBar.addView(ProcessBar);

        // Добавим подпись под загрузку
        tvTextProgress = (TextView) findViewById(R.id.tvTextProgress);
        assert tvTextProgress != null;
        tvTextProgress.setText("Выполняется загрузка");

        // Скачиваем список с тайтлами
        DownloadTitleFile AsyncTask = new DownloadTitleFile();
        AsyncTask.execute();
    }

    class DownloadTitleFile extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvTextProgress = (TextView) findViewById(R.id.tvTextProgress);
            assert tvTextProgress != null;
            tvTextProgress.setText("Выполняется загрузка");
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                // Ссылка на файл с тайтлами
                URL url = new URL(getResources().getString(R.string.URLTitleList));
                // Конектимся и скачиваем
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // парсим полученые данные
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer strBuffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    strBuffer.append(line);
                }
                // Вернем строку со всеми тайтлами для заполнения таблицы
                return strBuffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            if(strJson.isEmpty())
            {
                // Файл не скачался, включаем простую анимацию с миганием иконки
                tvTextProgress = (TextView) findViewById(R.id.tvTextProgress);
                assert tvTextProgress != null;
                tvTextProgress.setText("Неудалось выполнить\nзагрузку");


                ProcessBar = (ProgressBar) findViewById(R.id.ProcessBar);
                assert ProcessBar != null;
                ProcessBar.setVisibility(View.INVISIBLE);

                imageView = new ImageView(ActivityFirst.this);
                imageView.setImageResource(R.drawable.ic_loading_problem);
                imageView.setClickable(true);
                imageView.setAlpha((float) 0.3);

                flProgressBar = (FrameLayout) findViewById(R.id.flProgressBar);
                assert flProgressBar != null;
                flProgressBar.addView(imageView);


                mFadeInAnimation = AnimationUtils.loadAnimation(ActivityFirst.this, R.anim.icon_fadein);
                mFadeOutAnimation = AnimationUtils.loadAnimation(ActivityFirst.this, R.anim.icon_fadeout);
                mFadeInAnimation.setAnimationListener(animationFadeInListener);
                mFadeOutAnimation.setAnimationListener(animationFadeOutListener);

                // При запуске начинаем с анимации исчезновения
                imageView.startAnimation(mFadeOutAnimation);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nAnimCount = 0;
                        // Зачистим старую анимацию
                        imageView.clearAnimation();
                        // Повторим попытку скачать файл
                        ProcessDownloadFile();
                    }
                });

            }else{
                Intent intent = new Intent(ActivityFirst.this, ActivityFillList.class);
                // Передадим данные в ActivityFillList для заполнения таблицы
                intent.putExtra("strJson", strJson);
                // Запускаем активити в котором заполним список тайтлов
                startActivity(intent);
                // Поменяем анимацию перехода между активити
                overridePendingTransition(R.anim.anim_move_right, R.anim.anim_change_alpha);
                // Закрываем текущий активити
                ActivityFirst.this.finish();
            }
        }
    }

    Animation.AnimationListener animationFadeInListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            if(nAnimCount < 3)
                imageView.startAnimation(mFadeOutAnimation);
            else
                tvTextProgress.setText("Повторить загрузку?");
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationStart(Animation animation) {}
    };

    Animation.AnimationListener animationFadeOutListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation animation) {
            ++nAnimCount;
            imageView.startAnimation(mFadeInAnimation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationStart(Animation animation) {}
    };

}
