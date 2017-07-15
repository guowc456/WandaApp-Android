package com.wanta.mobile.wantaproject.domain;

/**
 * Created by Administrator on 2017/4/13.
 */

public class TopicsInfo {
   private int fid;//话题id（唯一）
   private String title;//标题：话题名称
   private String content;//描述：话题描述;
   private String imgpath;//话题图片
   private int bebrowsed;//话题的浏览数
   private int beliked;//话题的收藏数
   private int iconnum;//话题所包含图标总数
   private int width;//图片的宽
   private int height;//图片的高
   private String favourstate;//表示34用户的点赞状态，非空表示已经点赞

    public TopicsInfo() {

    }

    public TopicsInfo(int beliked, String favourstate) {
        this.beliked = beliked;
        this.favourstate = favourstate;
    }

    public TopicsInfo(int fid, String title, String content, String imgpath, int bebrowsed, int beliked, int iconnum, int width, int height, String favourstate) {
        this.fid = fid;
        this.title = title;
        this.content = content;
        this.imgpath = imgpath;
        this.bebrowsed = bebrowsed;
        this.beliked = beliked;
        this.iconnum = iconnum;
        this.width = width;
        this.height = height;
        this.favourstate = favourstate;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public int getBebrowsed() {
        return bebrowsed;
    }

    public void setBebrowsed(int bebrowsed) {
        this.bebrowsed = bebrowsed;
    }

    public int getBeliked() {
        return beliked;
    }

    public void setBeliked(int beliked) {
        this.beliked = beliked;
    }

    public int getIconnum() {
        return iconnum;
    }

    public void setIconnum(int iconnum) {
        this.iconnum = iconnum;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getFavourstate() {
        return favourstate;
    }

    public void setFavourstate(String favourstate) {
        this.favourstate = favourstate;
    }
}
