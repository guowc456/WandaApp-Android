package com.wanta.mobile.wantaproject.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.wanta.mobile.wantaproject.R;

/**
 * Created by WangYongqiang on 2016/10/24.
 */
public class AnimationView extends View {

    private float radidus;
    private Paint mPaint;
    private float mRad_x;
    private float mRad_y;

    public AnimationView(Context context) {
        this(context,null);
    }

    public AnimationView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public AnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
        //根据属性名称获取对应的值，属性名称的格式为类名_属性名
        radidus = typedArray.getFloat(R.styleable.CustomView_radidus, 10f);
        mRad_x = typedArray.getFloat(R.styleable.CustomView_rad_x, 0f);
        mRad_y = typedArray.getFloat(R.styleable.CustomView_rad_y, 0f);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(mRad_x,mRad_y,radidus,mPaint);
    }
    public void setRadidus( float radidus){
        this.radidus = radidus;
        requestLayout();
        invalidate();
    }
    public void setLocation(float mRad_x,float mRad_y){
        this.mRad_x = mRad_x;
        this.mRad_y = mRad_y;
    }
}
