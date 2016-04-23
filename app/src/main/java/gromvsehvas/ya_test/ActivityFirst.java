package gromvsehvas.ya_test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivityFirst extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        // Скачиваем список с тайтлами
        DownloadTitleFile AsyncTask = new DownloadTitleFile();
        AsyncTask.execute();
    }

    class DownloadTitleFile extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            TextView tView = (TextView) findViewById(R.id.tView);
            tView.setText("Выполняется загрузка");
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
            if(!strJson.isEmpty()){
                Intent intent = new Intent(ActivityFirst.this, ActivityFillList.class);
                // Передадим данные в ActivityFillList для заполнения таблицы
                intent.putExtra("strJson", strJson);
                // Запускаем активити в котором заполним список тайтлов
                startActivity(intent);
                // Закрываем текущий активити
                ActivityFirst.this.finish();
            }
        }
    }
}
