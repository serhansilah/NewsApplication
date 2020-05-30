package edu.sabanciuniv.osmanserhansilahyureklihomework3;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class CategoryItem implements Serializable {
    private int id;
    private String name;

    public CategoryItem() {
    }

    public CategoryItem(String name,int id) {
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
