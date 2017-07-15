package com.wanta.mobile.wantaproject.customview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by WangYongqiang on 2016/12/16.
 */
public class CustomGridLayoutManager extends GridLayoutManager {

    private boolean isScrollEnabled = true;

    public CustomGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled&&super.canScrollHorizontally();
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled&&super.canScrollVertically();
    }
    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }
}
