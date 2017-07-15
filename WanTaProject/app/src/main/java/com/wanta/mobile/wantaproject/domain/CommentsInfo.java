package com.wanta.mobile.wantaproject.domain;

/**
 * Created by WangYongqiang on 2016/12/29.
 */
public class CommentsInfo {

    private int cmtid ;//comment的id
    private String comment ;//评论内容
    private String createdat ;//评论创建时间
    private int rpnum ;//回复数
    private int userid ;//用户id
    private String username ;//用户名
    private String avatar ;//头像
    private String cmtfavour ;//标记该评论是否被检索的user点赞
    private int likednum ;//点赞的数目

    public CommentsInfo(int cmtid, String comment, String createdat, int rpnum, int userid, String username, String avatar, String cmtfavour, int likednum) {
        this.cmtid = cmtid;
        this.comment = comment;
        this.createdat = createdat;
        this.rpnum = rpnum;
        this.userid = userid;
        this.username = username;
        this.avatar = avatar;
        this.cmtfavour = cmtfavour;
        this.likednum = likednum;
    }

    public CommentsInfo(int cmtid, int likednum, String cmtfavour) {
        this.cmtid = cmtid;
        this.likednum = likednum;
        this.cmtfavour = cmtfavour;
    }

    public int getCmtid() {
        return cmtid;
    }

    public void setCmtid(int cmtid) {
        this.cmtid = cmtid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

    public int getRpnum() {
        return rpnum;
    }

    public void setRpnum(int rpnum) {
        this.rpnum = rpnum;
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

    public String getCmtfavour() {
        return cmtfavour;
    }

    public void setCmtfavour(String cmtfavour) {
        this.cmtfavour = cmtfavour;
    }

    public int getLikednum() {
        return likednum;
    }

    public void setLikednum(int likednum) {
        this.likednum = likednum;
    }
}
