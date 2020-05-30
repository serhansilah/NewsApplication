package edu.sabanciuniv.osmanserhansilahyureklihomework3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PostActivity extends AppCompatActivity {

    EditText name;
    EditText comm;
    int selectedNewsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        setTitle("Post Comments");
        Intent k = getIntent();
        selectedNewsId = k.getIntExtra("specific_id",2);
        ActionBar currentBar = getSupportActionBar();
        currentBar.setHomeButtonEnabled(true);
        currentBar.setDisplayHomeAsUpEnabled(true);
        currentBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24px);
         name = findViewById(R.id.nameedit);
         comm = findViewById(R.id.messageedit);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
    class JSONClassifier extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            String urlString = strings[0];
            JSONObject object = new JSONObject();
            try {
                object.put("name", name.getText().toString());
                object.put("text", comm.getText().toString());
                object.put("news_id",selectedNewsId);

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.connect();

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(object.toString());

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String line = "";

                    while ( (line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                }
                else {
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  stringBuilder.toString();
        }
    }
    public void buttonactivate(View v){
        JSONClassifier j = new JSONClassifier();
        j.execute("http://94.138.207.51:8080/NewsApp/service/news/savecomment");
        finish();
    }
}
