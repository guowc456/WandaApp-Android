package com.wanta.mobile.wantaproject.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;

/**
 * Created by WangYongqiang on 2017/1/19.
 */
public class FilterSimpleDraweeView extends SimpleDraweeView {

    private Paint myPaint = null;
    private Bitmap bitmap = null;
    private ColorMatrix myColorMatrix = null;
    private float[] colorArray = {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0};
    private int imageWidth = 0;
    private int imageHeight = 0;
    private int paddingLeft = 0;
    private Bitmap mNewbitmap;
    private int imgR = 0;
    private int imgG = 0;
    private int imgB = 0;
    private int width = 0;
    private int height = 0;
    private int distance = 0;
    private int oldWidth = 0;
    private int oldHeight = 0;
    private int lefDistance = 0;

    public FilterSimpleDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public FilterSimpleDraweeView(Context context) {
        super(context);
    }

    public FilterSimpleDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        invalidate();
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

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        myPaint = new Paint();
        mNewbitmap = Bitmap.createBitmap(this.bitmap.getWidth(), this.bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas newcanvas = new Canvas(mNewbitmap);
        newcanvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.ANTI_ALIAS_FLAG)); //转为可修改

        int[] pixelSize = new int[mNewbitmap.getWidth()*mNewbitmap.getHeight()];
        int[] dstPixelSize = new int[mNewbitmap.getWidth()*mNewbitmap.getHeight()];

        mNewbitmap.getPixels(pixelSize,0, mNewbitmap.getWidth(),0,0,mNewbitmap.getWidth(),mNewbitmap.getHeight());

        for(int i=0;i<mNewbitmap.getWidth()*mNewbitmap.getHeight();i++)
        {
            int color = pixelSize[i];

            int r= Color.red(color);
            int g= Color.green(color);
            int b= Color.blue(color);
            int alpha = Color.alpha(color);
            if (imgR!=0){
                r = imgR;
            }
            if (imgG!=0){
                g = imgG;
            }
            if (imgB!=0){
                b = imgB;
            }
            dstPixelSize[i] = Color.argb(alpha,getTranslateValue(r),getTranslateValue(g),getTranslateValue(b));
        }
        mNewbitmap.setPixels(dstPixelSize,0, mNewbitmap.getWidth(),0,0,mNewbitmap.getWidth(),mNewbitmap.getHeight());
        canvas.drawBitmap(mNewbitmap, Constants.PHONE_WIDTH / 2 - mNewbitmap.getWidth() / 2, (Constants.PHONE_HEIGHT / 4 - mNewbitmap.getHeight() / 2), myPaint);
        invalidate();

    }

    //设置颜色数值
    public void setColorArray(float[] colorArray) {
        this.colorArray = colorArray;
    }

    //设置图片
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    //处理后的图片
    public Bitmap getBitmap() {
        LogUtils.showVerbose("FilterColorView", "111");
        return mNewbitmap;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    //设置距离左边的距离
    public void setPaddingLeftDistance(int left) {
        this.paddingLeft = left;
    }
    public void setPicTranslateMax(int max){
        Constants.picStretchMax = max;
    }
    public void setPicTranslateMin(int min){
        Constants.picStretchMin = min;
    }
    public void setPicR(int r){
        imgR = r;
    }
    public void setPicG(int g){
        imgR = g;
    }
    public void setPicB(int b){
        imgR = b;
    }

    //得到变换后的灰度值
    public int getTranslateValue(int value){
        int currentValue = 0;
        if (value<Constants.picStretchMin){
            currentValue = 0;
        }else if (value>Constants.picStretchMax){
            currentValue = 255;
        }else {
            currentValue = (int) (((value-Constants.picStretchMin)*255*1.00)/(Constants.picStretchMax-Constants.picStretchMin));
        }
        return currentValue;
    }
    public void setWidth(int width){
        this.width = width;
    }
    public void setHeight(int height){
        this.height = height;
    }
}
