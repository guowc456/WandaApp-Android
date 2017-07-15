package com.wanta.mobile.wantaproject.customview;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by WangYongqiang on 2016/12/10.
 */
public class CustomSimpleDraweeView extends SimpleDraweeView {

    private int width = 0;
    private int height = 0;
    private int distance = 0;
    private int oldWidth = 0;
    private int oldHeight = 0;
    private int lefDistance = 0;

    public CustomSimpleDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public CustomSimpleDraweeView(Context context) {
        super(context);
    }

    public CustomSimpleDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSimpleDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        oldWidth = MeasureSpec.getSize(widthMeasureSpec);
        oldHeight = MeasureSpec.getSize(heightMeasureSpec);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l+lefDistance, t+distance, r+lefDistance, b+distance);
    }

    public void setWidth(int width){
        this.width = width;
    }
    public void setHeight(int height){
        this.height = height;
    }
    //设置距离顶部的距离
    public void setPaddingTop(int distance){
        this.distance = distance;
    }

    public void setPaddingLeft(int lefDistance){
        this.lefDistance = lefDistance;
    }

}
