package com.wanta.mobile.wantaproject.domain;

/**
 * Created by Administrator on 2017/5/7.
 */

public class RecommendCareInfo {
    private int userid;
    private String username;
    private String avatar;
    private int height;
    private String weight;
    private String address;
    private String bra;
    private int underbustgirth;
    private int funsnum;
    private String icons;
    private String isCare;
    private String sign;

    public String getIsCare() {
        return isCare;
    }

    public void setIsCare(String isCare) {
        this.isCare = isCare;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBra() {
        return bra;
    }

    public void setBra(String bra) {
        this.bra = bra;
    }

    public int getUnderbustgirth() {
        return underbustgirth;
    }

    public void setUnderbustgirth(int underbustgirth) {
        this.underbustgirth = underbustgirth;
    }

    public int getFunsnum() {
        return funsnum;
    }

    public void setFunsnum(int funsnum) {
        this.funsnum = funsnum;
    }

    public String getIcons() {
        return icons;
    }

    public void setIcons(String icons) {
        this.icons = icons;
    }

    public RecommendCareInfo(int userid, String username, String avatar, int height, String weight, String address, String bra, int underbustgirth, int funsnum, String icons, String isCare) {
        this.userid = userid;
        this.username = username;
        this.avatar = avatar;
        this.height = height;
        this.weight = weight;
        this.address = address;
        this.bra = bra;
        this.underbustgirth = underbustgirth;
        this.funsnum = funsnum;
        this.icons = icons;
        this.isCare = isCare;
    }

    public RecommendCareInfo(String isCare) {
        this.isCare = isCare;
    }

    public RecommendCareInfo(int userid, String username, String avatar, int height, String weight, String address, String bra, int funsnum, String sign, String isCare) {
        this.userid = userid;
        this.username = username;
        this.avatar = avatar;
        this.height = height;
        this.weight = weight;
        this.address = address;
        this.bra = bra;
        this.funsnum = funsnum;
        this.sign = sign;
        this.isCare = isCare;
    }
}
