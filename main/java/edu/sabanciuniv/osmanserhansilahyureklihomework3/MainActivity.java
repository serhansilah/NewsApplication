package edu.sabanciuniv.osmanserhansilahyureklihomework3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NewsAdapter.NewsItemClickListener{
    RecyclerView newsRecView;
    List<NewsItem> data;
    ProgressDialog prgDialog;
    ProgressDialog prgDialogc;
    NewsAdapter adp;
    Spinner spin;
    CatTask ctask;
    List<String> cat;
    ArrayAdapter<String> sp;
    SelectedCatId selectedCatId;
    List<NewsItem>  selectedCatData;
    CategoryItem cate;
    List<CategoryItem> holdCategory;
    NewsTask tsk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("News");
        spin = findViewById(R.id.spinid);
        cat = new ArrayList<>();
        selectedCatData = new ArrayList<>();
        holdCategory = new ArrayList<>();

        ctask = new CatTask();
        ctask.execute("http://94.138.207.51:8080/NewsApp/service/news/getallnewscategories");
        sp = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,cat);

        spin.setAdapter(sp);
        data = new ArrayList<>();

        newsRecView = findViewById(R.id.newsrec);
        adp = new NewsAdapter(data,this,this);
        newsRecView.setLayoutManager(new LinearLayoutManager(this));
        newsRecView.setAdapter(adp);

        cate = new CategoryItem();
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if( parent.getItemAtPosition(position).toString() == "All"){
                    tsk = new NewsTask();
                    tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getall");
                }
                else {
                    for (int i = 0; i < holdCategory.size(); i++) {
                        if (holdCategory.get(i).getName() == parent.getItemAtPosition(position).toString()) {
                            selectedCatId = new SelectedCatId();
                            cate.setName(holdCategory.get(i).getName());
                            selectedCatId.execute("http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid/" + holdCategory.get(i).getId());
                        } else {
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void newItemClicked(NewsItem selectedNews){
        Intent i = new Intent(this,NewsDetails.class);
        i.putExtra("selectednews",selectedNews);
        startActivity(i);
    }


    class SelectedCatId extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            prgDialog = new ProgressDialog(MainActivity.this);
            prgDialog.setTitle("Loading");
            prgDialog.setMessage("Please wait...");
            prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String urlStr = strings[0];
            StringBuilder buffer = new StringBuilder();
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line = "";
                while ((line = reader.readLine()) != null) {

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
            if(data != null){data.clear();}
            try {
                JSONObject obj = new JSONObject(s);

                if (obj.getInt("serviceMessageCode") == 1) {
                    JSONArray arr = obj.getJSONArray("items");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject current = (JSONObject) arr.get(i);
                        long date = current.getLong("date");
                        Date objDate = new Date(date);
                        NewsItem nitem = new NewsItem(current.getInt("id"),
                                current.getString("title"),
                                current.getString("text"),
                                current.getString("image"),
                                objDate,current.getString("categoryName"));
                        if (nitem.getCategoryName().equals(cate.getName())) {
                            data.add(nitem);
                        }
                        else{}


                    }
                } else {
                    System.out.println("Service has got a problem!");
                }
                adp.notifyDataSetChanged();
                sp.notifyDataSetChanged();
                prgDialog.dismiss();

            } catch (JSONException e) {
                Log.e("DEV", e.getMessage());
            }
        }
    }
    class CatTask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute(){
            prgDialogc = new ProgressDialog(MainActivity.this);
            prgDialogc.setTitle("Loading");
            prgDialogc.setMessage("Please wait...");
            prgDialogc.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgDialogc.show();
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
            cat.clear();
            cat.add("All");
            holdCategory.clear();
            try {
                JSONObject obj = new JSONObject(s);

                if(obj.getInt("serviceMessageCode")==1)
                {
                    JSONArray arr = obj.getJSONArray("items");
                    for (int i = 0;i < arr.length();i++)
                    {
                        JSONObject current = (JSONObject) arr.get(i);
                        CategoryItem citem = new CategoryItem(current.getString("name"),
                                current.getInt("id"));
                        holdCategory.add(citem);
                        cat.add(citem.getName());
                    }
                }
                else{
                    System.out.println("Service has got a problem!");
                }
                Log.i("DEV",String.valueOf(cat.size()));
                sp.notifyDataSetChanged();
                prgDialogc.dismiss();

            } catch (JSONException e) {
                Log.e("DEV",e.getMessage());
            }
        }

    }

    class NewsTask extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute(){
            prgDialog = new ProgressDialog(MainActivity.this);
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
            data.clear();
            try {
                JSONObject obj = new JSONObject(s);

                if(obj.getInt("serviceMessageCode")==1)
                {
                    JSONArray arr = obj.getJSONArray("items");
                    for (int i = 0;i < arr.length();i++)
                    {
                        JSONObject current = (JSONObject) arr.get(i);
                        long date = current.getLong("date");
                        Date objDate = new Date(date);
                        NewsItem item = new NewsItem(current.getInt("id"),
                                current.getString("title"),
                                current.getString("text"),
                                current.getString("image"),
                                objDate,current.getString("categoryName"));
                        data.add(item);
                    }
                }
                else{
                    System.out.println("Service has got a problem!");
                }
                Log.i("DEV",String.valueOf(data.size()));
                adp.notifyDataSetChanged();
                prgDialog.dismiss();

            } catch (JSONException e) {
                Log.e("DEV",e.getMessage());
            }
        }

    }

}
