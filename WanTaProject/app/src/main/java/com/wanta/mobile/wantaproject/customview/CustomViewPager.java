package com.wanta.mobile.wantaproject.customview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.wanta.mobile.wantaproject.utils.Constants;

/**
 * Created by WangYongqiang on 2016/7/7.
 */
public class CustomViewPager extends ViewPager {
    private int width = 1 ;
    private int height = 1 ;
    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    public void setWidth(int width){
        this.width = width;
    }
    public void setHeight(int height){
        this.height = height;
    }
}
