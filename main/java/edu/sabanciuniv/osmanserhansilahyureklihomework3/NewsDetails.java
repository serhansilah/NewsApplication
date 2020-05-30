package edu.sabanciuniv.osmanserhansilahyureklihomework3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

public class NewsDetails extends AppCompatActivity {
    NewsItem selectedNews;
    ImageView newsDetImg;
    TextView newSDetTitle;
    TextView newSDetDate;
    TextView newsDetText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        setTitle("News Details");
        selectedNews = (NewsItem)getIntent().getSerializableExtra("selectednews");
        newsDetImg = findViewById(R.id.newsdetimg);
        newsDetText= findViewById(R.id.newsdettext);
        newSDetDate= findViewById(R.id.newsdetdate);
        newSDetTitle= findViewById(R.id.newsdettitle);
        if(selectedNews.getBitmap() == null){
            new ImageDownloadTask(newsDetImg).execute(selectedNews);
        }
        else{
            newsDetImg.setImageBitmap(selectedNews.getBitmap());
        }
        newsDetText.setText(selectedNews.getText());
        newSDetDate.setText(new SimpleDateFormat("dd/MM/yyy").format(selectedNews.getNewsDate()));
        newSDetTitle.setText(selectedNews.getTitle());


        ActionBar currentBar = getSupportActionBar();
        currentBar.setHomeButtonEnabled(true);
        currentBar.setDisplayHomeAsUpEnabled(true);
        currentBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24px);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        else if(item.getItemId() == R.id.mn_com){
            Intent i = new Intent(this,Comments.class);
            i.putExtra("specific_id",selectedNews.getId());
            startActivity(i);
        }
        return true;
    }
}
