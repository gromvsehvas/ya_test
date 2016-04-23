package gromvsehvas.ya_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by gromvsehvas on 4/21/2016.
 */
public class ActivityFillList extends AppCompatActivity {

    public TitleArr[] clTitleArr;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_list);

        // создадим свой тулбар для смены цвета текста и стрелки назад во второй активити
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle(R.string.title);
        setSupportActionBar(toolbar);

        // получим с первой активити список тайтлов с описанием
        Intent intent = getIntent();
        ReadJSONFile(intent.getStringExtra("strJson"));

        // Создадим адаптер для плавного заполнения списка тайтлов
        ListView ListView = (ListView) findViewById(R.id.listView);
        FillListView mAdapter = new FillListView(ActivityFillList.this, clTitleArr);
        ListView.setAdapter(mAdapter);


        // Ловим клик по таблице, дял открытия активити с описанием по тайтлу
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View itemView, int index, long id) {
                Intent intent = new Intent(ActivityFillList.this, ActivitySecond.class);

                // описываем для передачи нужные поля
                intent.putExtra("Title", clTitleArr[index].strTitle);
                intent.putExtra("Albums", clTitleArr[index].strAlbums);
                intent.putExtra("Tracks", clTitleArr[index].strTracks);
                intent.putExtra("LinkTitle", clTitleArr[index].strLinkTitle);
                intent.putExtra("Description", clTitleArr[index].strDescription);
                intent.putExtra("Genres", clTitleArr[index].strGenres);
                intent.putExtra("CoverBig", clTitleArr[index].strCoverBig);

                // запускаем активити для просмотра информации о тайтле
                startActivity(intent);
            }
        });
    }

    private void ReadJSONFile(String strJson)
    {
        try
        {
            // Строку в массив и после проходим по массиву в цикле
            JSONArray arrJSON = new JSONArray(strJson);
            // Сразу задаем размер clTitleArr
            clTitleArr = new TitleArr[arrJSON.length()];

            String strGenres;
            for (int i=0; i<arrJSON.length(); i++)
            {
                JSONObject post = arrJSON.getJSONObject(i);

                TitleArr mTitleArr = new TitleArr();
                mTitleArr.strTitle = post.optString("name");
                // Заполняем кол-во альбомов и склоняем текст
                mTitleArr.strAlbums = NounsDeclension(post.optInt("albums"), "альбом", "альбома", "альбомов");
                // Заполняем кол-во песен и склоняем текст
                mTitleArr.strTracks = NounsDeclension(post.optInt("tracks"), "песня", "песни", "песен");
                mTitleArr.strLinkTitle = post.optString("link");
                mTitleArr.strDescription = post.optString("description");
                // Получаем массив жанров
                JSONArray jsonArray = post.getJSONArray("genres");
                strGenres = "";
                // В строку вписываем жаны через запятую
                for (int j = 0; j < jsonArray.length(); j++) {
                    strGenres += jsonArray.optString(j);
                    // После последней запятую не ставим
                    if (jsonArray.length() != j + 1)
                        strGenres += ", ";
                }
                mTitleArr.strGenres = strGenres;
                // Тут без массива, всегда 2 вида обложки
                JSONObject jCover = post.getJSONObject("cover");
                mTitleArr.strCoverSmall = jCover.optString("small");
                mTitleArr.strCoverBig = jCover.optString("big");

                // Меняем значения в clTitleArr
                clTitleArr[i] = mTitleArr;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Хитрая схема со склонением слов (альбом, альбома, альбомов и песня, песни, песен)
    private String NounsDeclension(int nCount, String text1, String text2, String text3) {
        String strEnd = "";
        int n = nCount % 100;
        if (n > 10 && n < 20)
            strEnd = text3;
        else {
            n = n % 10;
            switch (n) {
                case 1: {
                    strEnd = text1;
                    break;
                }
                case 2:
                case 3:
                case 4: {
                    strEnd = text2;
                    break;
                }
                default:{
                    strEnd = text3;
                    break;
                }
            }
        }
        return String.format("%d %s", nCount, strEnd);
    }
}