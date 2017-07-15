package com.wanta.mobile.wantaproject.domain;

/**
 * Created by WangYongqiang on 2016/11/22.
 */
public class ReplyCommentInfo {
    private String author_icon ;//评论人的头像
    private String author ;//评论人的名字
    private String reply_date;//评论的日期
    private String reply_comment;//评论的内容
    private String cmtfavour ;//是否被点赞了

    public String getCmtfavour() {
        return cmtfavour;
    }

    public void setCmtfavour(String cmtfavour) {
        this.cmtfavour = cmtfavour;
    }

    public String getAuthor_icon() {
        return author_icon;
    }

    public void setAuthor_icon(String author_icon) {
        this.author_icon = author_icon;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReply_date() {
        return reply_date;
    }

    public void setReply_date(String reply_date) {
        this.reply_date = reply_date;
    }

    public String getReply_comment() {
        return reply_comment;
    }

    public void setReply_comment(String reply_comment) {
        this.reply_comment = reply_comment;
    }
}
