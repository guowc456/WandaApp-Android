package com.wanta.mobile.wantaproject.domain;

/**
 * Created by WangYongqiang on 2017/1/10.
 */
public class ResponseInfo {
    private int rpid;//reply的id
    private String rpcontent;//回复内容
    private int rp_userid ;//回复者id
    private int be_rp_userid;//被回复者id
    private String createdat;//创建时间
    private String rp_username;//回复者的名字
    private String rp_avatar;//回复者的头像
    private String be_rp_username;//被回复者的名字
    private String be_rp_avatar;//被回复者的头像

    public ResponseInfo(int rpid, String rpcontent, int rp_userid, int be_rp_userid, String createdat, String rp_username, String rp_avatar, String be_rp_username, String be_rp_avatar) {
        this.rpid = rpid;
        this.rpcontent = rpcontent;
        this.rp_userid = rp_userid;
        this.be_rp_userid = be_rp_userid;
        this.createdat = createdat;
        this.rp_username = rp_username;
        this.rp_avatar = rp_avatar;
        this.be_rp_username = be_rp_username;
        this.be_rp_avatar = be_rp_avatar;
    }

    public int getRpid() {
        return rpid;
    }

    public void setRpid(int rpid) {
        this.rpid = rpid;
    }

    public String getRpcontent() {
        return rpcontent;
    }

    public void setRpcontent(String rpcontent) {
        this.rpcontent = rpcontent;
    }

    public int getRp_userid() {
        return rp_userid;
    }

    public void setRp_userid(int rp_userid) {
        this.rp_userid = rp_userid;
    }

    public int getBe_rp_userid() {
        return be_rp_userid;
    }

    public void setBe_rp_userid(int be_rp_userid) {
        this.be_rp_userid = be_rp_userid;
    }

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

    public String getRp_username() {
        return rp_username;
    }

    public void setRp_username(String rp_username) {
        this.rp_username = rp_username;
    }

    public String getRp_avatar() {
        return rp_avatar;
    }

    public void setRp_avatar(String rp_avatar) {
        this.rp_avatar = rp_avatar;
    }

    public String getBe_rp_username() {
        return be_rp_username;
    }

    public void setBe_rp_username(String be_rp_username) {
        this.be_rp_username = be_rp_username;
    }

    public String getBe_rp_avatar() {
        return be_rp_avatar;
    }

    public void setBe_rp_avatar(String be_rp_avatar) {
        this.be_rp_avatar = be_rp_avatar;
    }
}
