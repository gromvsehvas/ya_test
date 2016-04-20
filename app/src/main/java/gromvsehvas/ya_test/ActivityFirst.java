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

        DownloadTitleFile AsyncTask = new DownloadTitleFile();
        AsyncTask.execute();


    }
    class DownloadTitleFile extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            TextView tView = (TextView) findViewById(R.id.tView);
            tView.setText("Подождите,\nвыполняется загрузка");
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                
                URL url = new URL("http://download.cdn.yandex.net/mobilization-2016/artists.json");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer strBuffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    strBuffer.append(line);
                }
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
                //передадим данные в ActivityFillList для заполнения таблицы
                intent.putExtra("strJson", strJson);

                //запускаем активити
                startActivity(intent);
                ActivityFirst.this.finish();
            }
        }
    }

}
