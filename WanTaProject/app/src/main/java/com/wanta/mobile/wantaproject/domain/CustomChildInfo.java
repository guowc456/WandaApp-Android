package com.wanta.mobile.wantaproject.domain;

/**
 * Created by Administrator on 2017/3/23.
 */

public class CustomChildInfo {
    private String childContent;//设置显示的内容
    private boolean isChildExpend;//设置是否展开

    public String getChildContent() {
        return childContent;
    }

    public void setChildContent(String childContent) {
        this.childContent = childContent;
    }

    public boolean isChildExpend() {
        return isChildExpend;
    }

    public void setChildExpend(boolean childExpend) {
        isChildExpend = childExpend;
    }
}
