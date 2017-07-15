package com.wanta.mobile.wantaproject.domain;

import java.util.List;

/**
 * Created by WangYongqiang on 2016/11/14.
 */
public class FirstItem {
    private int id;
    private String title;
    private int image;
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    private List<SecondItem> secondItems;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public List<SecondItem> getSecondItems() {
        return secondItems;
    }

    public void setSecondItems(List<SecondItem> secondItems) {
        this.secondItems = secondItems;
    }

}
