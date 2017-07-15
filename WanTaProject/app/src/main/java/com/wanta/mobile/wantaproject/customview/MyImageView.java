package com.wanta.mobile.wantaproject.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wanta.mobile.wantaproject.R;

/**
 * Created by WangYongqiang on 2016/10/23.
 */
public class MyImageView extends ImageView {
    //宽高比，由我们自己设定
    private float ratio;
    private float mImgWidth;
    private float mImgHeight;


    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyImageView);
        //根据属性名称获取对应的值，属性名称的格式为类名_属性名
        ratio = typedArray.getFloat(R.styleable.MyImageView_ratio, 0.1f);
        mImgWidth = typedArray.getFloat(R.styleable.MyImageView_imgWidth,10f);
        mImgHeight = typedArray.getFloat(R.styleable.MyImageView_imgHeight,10f);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int) mImgWidth,(int) mImgHeight);
    }

    public void setSize(float mImgWidth,float mImgHeight){
        this.mImgWidth = mImgWidth;
        this.mImgHeight = mImgHeight;
    }
}
