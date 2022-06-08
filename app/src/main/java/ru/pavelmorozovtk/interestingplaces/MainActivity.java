package ru.pavelmorozovtk.interestingplaces;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yandex.mapkit.MapKitFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class InitMapKit{
    static boolean flag = false;
    public static void init(){
        if (!flag)
        {
            MapKitFactory.setApiKey("2f564278-8e90-4f29-8816-fa121bdd751c");
            flag = true;
        }
    }
}

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_Weather;
    String url="https://api.openweathermap.org/data/2.5/weather" +
            "?lat=55.992837&lon=92.804242" +
            "&units=metric" +
            "&appid=b5292049df11667ba0d0dab8f1966e32";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitMapKit.init();
        DatabaseHandler databaseHandler;
        Button btn_create;
        TextView title;
        TextView text;

        LinearLayout contentList;
        contentList = findViewById(R.id.content_list);
        btn_create = findViewById(R.id.btn_CreateNote);
        btn_create.setOnClickListener(this);

        databaseHandler = new DatabaseHandler(this);
        for (Note note: databaseHandler.getAllContacts()) {
            final View view = getLayoutInflater().inflate(R.layout.layout_card, null);
            title = view.findViewById(R.id.tv_title);
            text = view.findViewById(R.id.tv_text);

            view.setId(note.getid());
            title.setText(note.getTitle()+ "  " +note.getPoint());
            text.setText(note.getDescription());
            view.setOnClickListener(this);

            contentList.addView(view);
        }

        tv_Weather = findViewById(R.id.tv_Weather);
        new UrlData().execute(url);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_CreateNote)
            startActivity(new Intent(this, CreateActivity.class));
        else
            startActivity(new Intent(this, CardActivity.class)
                    .putExtra("note", view.getId()));
    }

    private class UrlData extends AsyncTask<String, String, String>{

        protected  void onPreExecute(){
            super.onPreExecute();
            tv_Weather.setText("Ожидайте...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null){
                    buffer.append(line).append("\n");
                }

                return buffer.toString();

            } catch (Exception e) {

                e.printStackTrace();
                e.printStackTrace();

            } finally {
                if (connection != null) connection.disconnect();
                if (reader != null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                tv_Weather.setText("Температура: "
                        + jsonObject.getJSONObject("main").getDouble("temp")
                        + "°C");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}