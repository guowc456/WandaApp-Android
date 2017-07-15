package com.wanta.mobile.wantaproject.domain;

import java.util.List;

/**
 * Created by WangYongqiang on 2016/11/14.
 */
public class SecondItem {
    private int id;
    private String title;
    private List<ThirdItem> thirdItems;

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

    public List<ThirdItem> getThirdItems() {
        return thirdItems;
    }

    public void setThirdItems(List<ThirdItem> thirdItems) {
        this.thirdItems = thirdItems;
    }


}
