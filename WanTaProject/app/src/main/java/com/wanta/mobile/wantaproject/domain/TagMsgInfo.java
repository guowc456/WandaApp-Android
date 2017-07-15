package com.wanta.mobile.wantaproject.domain;

/**
 * Created by Administrator on 2017/5/25.
 */

public class TagMsgInfo {
    private int pos_x;//当前的横坐标
    private int pos_y;//当前的纵坐标
    private String itemtype;//图片的类型
    private String itemid;//图片的id
    private String itemlink;//图片的链接
    private String price;//图片中信息的价格
    private String currency;//价格的单位
    private String region;//购买地点
    private String brand;//衣服的品牌
    private String description;//衣服的描述

    public TagMsgInfo(String description, String brand, String region, String price) {
        this.description = description;
        this.brand = brand;
        this.region = region;
        this.price = price;
    }

    public int getPos_x() {
        return pos_x;
    }

    public void setPos_x(int pos_x) {
        this.pos_x = pos_x;
    }

    public int getPos_y() {
        return pos_y;
    }

    public void setPos_y(int pos_y) {
        this.pos_y = pos_y;
    }

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItemlink() {
        return itemlink;
    }

    public void setItemlink(String itemlink) {
        this.itemlink = itemlink;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
