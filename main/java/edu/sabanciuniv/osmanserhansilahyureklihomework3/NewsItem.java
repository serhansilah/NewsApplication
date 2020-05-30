package edu.sabanciuniv.osmanserhansilahyureklihomework3;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by atanaltay on 28/03/2017.
 */

public class NewsItem implements Serializable{

    private String title;
    private String text;

    //name of image in Drawable folder
    private String imagePath ;
    private Date newsDate;
    private int id;
    private transient Bitmap bitmap;
    private String categoryName;

    public NewsItem() {
    }

    public NewsItem(int id, String title, String text, String imagePath, Date newsDate,String categoryName) {
        this.title = title;
        this.text = text;
        this.imagePath = imagePath;
        this.newsDate = newsDate;
        this.id = id;
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImageImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    public Date getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(Date newsDate) {
        this.newsDate = newsDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
