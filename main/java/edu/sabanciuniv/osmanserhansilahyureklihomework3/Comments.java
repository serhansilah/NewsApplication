package edu.sabanciuniv.osmanserhansilahyureklihomework3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Comments extends AppCompatActivity {
    RecyclerView comRecView;
    List<CommentItem> comData;
    ProgressDialog prgDialog;
    CommentAdapter adp;
    CommentTask tsk;
    int specid;
    boolean isCreated = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        setTitle("Comments");
        comData = new ArrayList<>();
        comRecView = findViewById(R.id.comrec);
        adp = new CommentAdapter(comData,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        specid = (Integer) getIntent().getIntExtra("specific_id", 2);
        comRecView.setLayoutManager(linearLayoutManager);
        comRecView.setAdapter(adp);
        tsk = new CommentTask();
        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getcommentsbynewsid/"+specid);


        ActionBar currentBar = getSupportActionBar();
        currentBar.setHomeButtonEnabled(true);
        currentBar.setDisplayHomeAsUpEnabled(true);
        currentBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24px);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isCreated)
        {
            CommentTask k = new CommentTask();
            String url = "http://94.138.207.51:8080/NewsApp/service/news/getcommentsbynewsid/" + specid;
            k.execute(url);
        }
        isCreated = true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comment_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        else if(item.getItemId()==R.id.mn_add)
        {
            Intent i = new Intent(this,PostActivity.class);
            int idSpecific = (Integer) getIntent().getIntExtra("specific_id", 0);

            i.putExtra("specific_id",idSpecific);
            startActivity(i);
        }
        return true;
    }



    class CommentTask extends AsyncTask<String,Void,String> {
    @Override
    protected void onPreExecute(){
        prgDialog = new ProgressDialog(Comments.this);
        prgDialog.setTitle("Loading");
        prgDialog.setMessage("Please wait...");
        prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prgDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String urlStr = strings[0];
        StringBuilder buffer= new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line= "";
            while((line = reader.readLine()) != null){

                buffer.append(line);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        comData.clear();
        try {
            JSONObject obj = new JSONObject(s);

            if(obj.getInt("serviceMessageCode")==1)
            {
                JSONArray arr = obj.getJSONArray("items");
                for (int i = 0;i < arr.length();i++)
                {
                    JSONObject current = (JSONObject) arr.get(i);
                    CommentItem item = new CommentItem(current.getInt("news_id"),
                            current.getString("text"),
                            current.getString("name"));
                    comData.add(item);
                }
            }
            else{
                System.out.println("Service has got a problem!");
            }
            Log.i("DEV",String.valueOf(comData.size()));
            adp.notifyDataSetChanged();
            prgDialog.dismiss();

        } catch (JSONException e) {
            Log.e("DEV",e.getMessage());
        }
        }

    }
}

