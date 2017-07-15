package com.wanta.mobile.wantaproject.domain;

/**
 * Created by Administrator on 2017/3/23.
 */

public class CustomGroupInfo {
    private boolean isGroupExpend;//设置是否展开
    private String clothName ;//衣服的名字
    private String clothNumber ;//衣服的数目
    private CustomChildInfo childInfo;
    private int iconUrl ;//头像地址

    public int getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(int iconUrl) {
        this.iconUrl = iconUrl;
    }

    public boolean isGroupExpend() {
        return isGroupExpend;
    }

    public void setGroupExpend(boolean groupExpend) {
        isGroupExpend = groupExpend;
    }

    public String getClothName() {
        return clothName;
    }

    public void setClothName(String clothName) {
        this.clothName = clothName;
    }

    public String getClothNumber() {
        return clothNumber;
    }

    public void setClothNumber(String clothNumber) {
        this.clothNumber = clothNumber;
    }

    public CustomChildInfo getChildInfo() {
        return childInfo;
    }

    public void setChildInfo(CustomChildInfo childInfo) {
        this.childInfo = childInfo;
    }
}
