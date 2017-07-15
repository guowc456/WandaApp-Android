package com.wanta.mobile.wantaproject.domain;

/**
 * Created by Administrator on 2017/6/5.
 */

public class IconsInfo {

    private int icnfid;//首页icon的id
    private String title;//首页标题
    private String content;//首页内容
    private int likenum;//点赞数
    private int storenum;//收藏数
    private int userid;//用户i
    private String favourstate;//是否已被该用户点赞
    private String storedstate;//是否已被该用户收藏
    private String lng;//经度
    private String lat;//纬度
    private String address;//地点
    private String createdat;//创建时间
    private String images;//图片集合
    private String topics;//获取话题信息
    private String browsenum;//浏览数
    private String title_cn;//标题的汉语
    private String content_cn;//内容的汉语
    private int cmtnum ;//评论数目

    public IconsInfo(int icnfid, String title, String content, int likenum, int storenum, int userid, String favourstate, String storedstate, String lng, String lat, String address, String createdat, String images, String topics, String browsenum, String title_cn, String content_cn, int cmtnum) {
        this.icnfid = icnfid;
        this.title = title;
        this.content = content;
        this.likenum = likenum;
        this.storenum = storenum;
        this.userid = userid;
        this.favourstate = favourstate;
        this.storedstate = storedstate;
        this.lng = lng;
        this.lat = lat;
        this.address = address;
        this.createdat = createdat;
        this.images = images;
        this.topics = topics;
        this.browsenum = browsenum;
        this.title_cn = title_cn;
        this.content_cn = content_cn;
        this.cmtnum = cmtnum;
    }

    public int getIcnfid() {
        return icnfid;
    }

    public void setIcnfid(int icnfid) {
        this.icnfid = icnfid;
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

    public int getCmtnum() {
        return cmtnum;
    }

    public void setCmtnum(int cmtnum) {
        this.cmtnum = cmtnum;
    }
}
