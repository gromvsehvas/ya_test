package gromvsehvas.ya_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

/**
 * Created by grom on 4/23/2016.
 */
public class ActivitySecond extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Выводим тулбар с кнопкой назад и тайтлем
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        // Забираем данные с предыдушего активити
        Intent intent = getIntent();
        // Выводим в тулбале тайтла
        toolbar.setTitle(intent.getStringExtra("Title"));
        // Активируем тулбар
        setSupportActionBar(toolbar);
        // Добавим кнопку назад
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Добавим анимацию на нажатие
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // Ловим нажатие на тулбаре
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Закрываем текущий активити, открываем предыдущий
                onBackPressed();
            }
        });

        // Заполняем поля на активити
        // Обложку заполним через Picasso
        final ImageView ivCoverBig = (ImageView) findViewById(R.id.ivCoverBig);
        Picasso.with(this)
                .load(intent.getStringExtra("CoverBig"))
                .placeholder(R.drawable.ic_download_cover)
                .error(R.drawable.ic_error_download)
                .into(ivCoverBig);

        // Жанры
        final TextView tvGenres = (TextView) findViewById(R.id.tvGenres_about);
        assert tvGenres != null;
        tvGenres.setText(intent.getStringExtra("Genres"));

        // Выводим кол-во альбомов и кол-во песен через точку в центре строки
        final TextView tvAlbumsAndTracks = (TextView) findViewById(R.id.tvAlbumsAndTracks_about);
        assert tvAlbumsAndTracks != null;
        String strSong = String.format("%s \u2219 %s",
                intent.getStringExtra("Albums"),
                intent.getStringExtra("Tracks"));
        tvAlbumsAndTracks.setText(strSong);

        // Описание тайтла
        final TextView tvDescription = (TextView) findViewById(R.id.tvDescription);
        assert tvDescription != null;
        tvDescription.setText(intent.getStringExtra("Description"));
    }
}