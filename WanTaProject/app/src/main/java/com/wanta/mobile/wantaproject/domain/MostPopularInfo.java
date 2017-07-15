package com.wanta.mobile.wantaproject.domain;

import android.widget.ImageView;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class MostPopularInfo {
    private  int icnfid;//首页icon的id
    private  String title ;//首页标题
    private  String content ;//首页内容
    private  int likenum ;//点赞数
    private  int storenum ;//收藏数
    private  int userid ;//用户id
    private  String username ;//用户名字
    private  String useravatar ;//用户图标
    private  String favourstate ;//是否已被该用户点赞
    private  String storedstate ;//是否已被该用户收藏
    private  String lng ;//经度
    private  String lat ;//纬度
    private  String address ;//地点
    private  String createdat ;//创建时间
    private  String images ;//图片集合
    private String weight;//体重
    private String bust;//胸围
    private String uaddress;//常居住
    private String height;//身高
    private String followstate;//是否关注该用户
    private String bra;//罩杯
    private String topics;//获取话题信息
    private String browsenum;//浏览数
    private String title_cn;//标题的汉语
    private String content_cn;//内容的汉语

    public String getTitle_cn() {
        return title_cn;
    }

    public void setTitle_cn(String title_cn) {
        this.title_cn = title_cn;
    }

    public String getContent_cn() {
        return content_cn;
    }

    public void setContent_cn(String content_cn) {
        this.content_cn = content_cn;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getBrowsenum() {
        return browsenum;
    }

    public void setBrowsenum(String browsenum) {
        this.browsenum = browsenum;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBust() {
        return bust;
    }

    public void setBust(String bust) {
        this.bust = bust;
    }

    public String getUaddress() {
        return uaddress;
    }

    public void setUaddress(String uaddress) {
        this.uaddress = uaddress;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getFollowstate() {
        return followstate;
    }

    public void setFollowstate(String followstate) {
        this.followstate = followstate;
    }

    public String getBra() {
        return bra;
    }

    public void setBra(String bra) {
        this.bra = bra;
    }

    private String picUrl;//每张图片的地址

    public MostPopularInfo(int likenum, String favourstate) {
        this.likenum = likenum;
        this.favourstate = favourstate;
    }

    public int getCmtnum() {
        return cmtnum;
    }

    public void setCmtnum(int cmtnum) {
        this.cmtnum = cmtnum;
    }

    private int picWidth;//图片的宽度
    private int picHeight;//图片的高度
    private int cmtnum ;//评论数目

    public MostPopularInfo(String picUrl, int picWidth, int picHeight) {
        this.picUrl = picUrl;
        this.picWidth = picWidth;
        this.picHeight = picHeight;
    }

    public MostPopularInfo() {

    }

    public MostPopularInfo(int icnfid, String title, String content, int likenum, int storenum,
                           int userid, String username, String useravatar,
                           String favourstate, String storedstate, String lng, String lat, String address,
                           String createdat, String images, String weight, String bust, String uaddress,
                           String height, String followstate, String bra,
                           int cmtnum,String topics,String browsenum,String title_cn,String content_cn ) {
        this.icnfid = icnfid;
        this.title = title;
        this.content = content;
        this.likenum = likenum;
        this.storenum = storenum;
        this.userid = userid;
        this.username = username;
        this.useravatar = useravatar;
        this.favourstate = favourstate;
        this.storedstate = storedstate;
        this.lng = lng;
        this.lat = lat;
        this.address = address;
        this.createdat = createdat;
        this.images = images;
        this.weight = weight;
        this.bust = bust;
        this.uaddress = uaddress;
        this.height = height;
        this.followstate = followstate;
        this.bra = bra;
        this.cmtnum = cmtnum;
        this.topics = topics;
        this.browsenum = browsenum;
        this.title_cn = title_cn;
        this.content_cn = content_cn;
    }
    //获取其他人用户信息的mordul
    public MostPopularInfo(int icnfid, String title, String content, int likenum, int storenum,
                           String favourstate, String storedstate, String lng, String lat, String address,
                           String createdat, String images, String followstate,
                           String topics,String browsenum,String title_cn,String content_cn ) {
        this.icnfid = icnfid;
        this.title = title;
        this.content = content;
        this.likenum = likenum;
        this.storenum = storenum;
        this.favourstate = favourstate;
        this.storedstate = storedstate;
        this.lng = lng;
        this.lat = lat;
        this.address = address;
        this.createdat = createdat;
        this.images = images;
        this.followstate = followstate;
        this.topics = topics;
        this.browsenum = browsenum;
        this.title_cn = title_cn;
        this.content_cn = content_cn;
    }
    //获取自己用户信息的mordul
    public MostPopularInfo(int userid,int icnfid, String title,String title_cn, String content,String content_cn, int likenum ,String browsenum, int storenum,
                           String favourstate, String storedstate, String lng, String lat, String address,
                           String createdat, String images,
                           String topics ) {
        this.userid = userid;
        this.icnfid = icnfid;
        this.title = title;
        this.title_cn = title_cn;
        this.content = content;
        this.content_cn = content_cn;
        this.likenum = likenum;
        this.browsenum = browsenum;
        this.storenum = storenum;
        this.favourstate = favourstate;
        this.storedstate = storedstate;
        this.lng = lng;
        this.lat = lat;
        this.address = address;
        this.createdat = createdat;
        this.images = images;
        this.topics = topics;
    }

    public MostPopularInfo(int icnfid, String title, String content, int likenum, String username, String useravatar, String images) {
        this.icnfid = icnfid;
        this.title = title;
        this.content = content;
        this.likenum = likenum;
        this.username = username;
        this.useravatar = useravatar;
        this.images = images;
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

    public int getIcnfid() {
        return icnfid;
    }

    public void setIcnfid(int icnfid) {
        this.icnfid = icnfid;
    }

    public int getLikenum() {
        return likenum;
    }

    public void setLikenum(int likenum) {
        this.likenum = likenum;
    }

    public int getStorenum() {
        return storenum;
    }

    public void setStorenum(int storenum) {
        this.storenum = storenum;
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

    public String getUseravatar() {
        return useravatar;
    }

    public void setUseravatar(String useravatar) {
        this.useravatar = useravatar;
    }

    public String getFavourstate() {
        return favourstate;
    }

    public void setFavourstate(String favourstate) {
        this.favourstate = favourstate;
    }

    public String getStoredstate() {
        return storedstate;
    }

    public void setStoredstate(String storedstate) {
        this.storedstate = storedstate;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getPicWidth() {
        return picWidth;
    }

    public void setPicWidth(int picWidth) {
        this.picWidth = picWidth;
    }

    public int getPicHeight() {
        return picHeight;
    }

    public void setPicHeight(int picHeight) {
        this.picHeight = picHeight;
    }

    @Override
    public String toString() {
        return "MostPopularInfo{" +
                "icnfid=" + icnfid +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", likenum=" + likenum +
                ", storenum=" + storenum +
                ", userid=" + userid +
                ", username='" + username + '\'' +
                ", useravatar='" + useravatar + '\'' +
                ", favourstate='" + favourstate + '\'' +
                ", storedstate='" + storedstate + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", address='" + address + '\'' +
                ", createdat='" + createdat + '\'' +
                ", images='" + images + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", picWidth=" + picWidth +
                ", picHeight=" + picHeight +
                ", cmtnum=" + cmtnum +
                '}';
    }
}
