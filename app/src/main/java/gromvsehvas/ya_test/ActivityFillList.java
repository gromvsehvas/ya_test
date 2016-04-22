package gromvsehvas.ya_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle(R.string.title);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        ReadJSONFile(intent.getStringExtra("strJson"));


        ListView list = (ListView) findViewById(R.id.listView);
        FillTable mAdapter = new FillTable(ActivityFillList.this, clTitleArr);
        list.setAdapter(mAdapter);
    }

    private  void ReadJSONFile(String Jsonstr)
    {
        try
        {
            JSONArray entries = new JSONArray(Jsonstr);

            clTitleArr = new TitleArr[entries.length()];

            String strGenres;
            for (int i=0; i<entries.length(); i++)
            {
                JSONObject post = entries.getJSONObject(i);

                TitleArr lElement = new TitleArr();

                lElement.strTitle = post.optString("name");
                lElement.nAlbums = post.optInt("albums");
                lElement.nTracks = post.optInt("tracks");
                lElement.strLinkSinger = post.optString("link");
                lElement.strDescription = post.optString("description");

                JSONArray jsonArray = post.getJSONArray("genres");
                strGenres = "";
                for (int j = 0; j < jsonArray.length(); j++) {
                    strGenres += jsonArray.optString(j);

                    if (jsonArray.length() != j +1)
                        strGenres += ", ";
                }
                lElement.strGenres = strGenres;

                JSONObject jCover = post.getJSONObject("cover");
                lElement.strCoverSmall = jCover.optString("small");
                lElement.strCoverBig = jCover.optString("big");

                clTitleArr[i] = lElement;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}