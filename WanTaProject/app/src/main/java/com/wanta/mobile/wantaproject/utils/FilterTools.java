package com.wanta.mobile.wantaproject.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by WangYongqiang on 2017/3/5.
 */
public class FilterTools {
    //获取修改后的图片
    public static Bitmap getModifyPic(Bitmap bitmap, int[] pixelSize){
        Paint myPaint = new Paint();
        Bitmap mNewbitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas newcanvas = new Canvas(mNewbitmap);
        newcanvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.ANTI_ALIAS_FLAG)); //转为可修改

        mNewbitmap.setPixels(pixelSize, 0, mNewbitmap.getWidth(), 0, 0, mNewbitmap.getWidth(), mNewbitmap.getHeight());
//        newcanvas.drawBitmap(mNewbitmap, Constants.PHONE_WIDTH / 2 - mNewbitmap.getWidth() / 2, (Constants.PHONE_HEIGHT / 4 - mNewbitmap.getHeight() / 2), myPaint);
        newcanvas.drawBitmap(mNewbitmap, 0,0, myPaint);//保证了显示的图片没有叠加的效果
        return mNewbitmap;
    }
    //获取修改后的像素点数组
    public static int[] getPixelArray(Bitmap newbitmap, int flag_color, double minLevel, double maxLevel){
        int[] mPixelSize = new int[newbitmap.getWidth() * newbitmap.getHeight()];
        int[] mDstPixelSize = new int[newbitmap.getWidth() * newbitmap.getHeight()];
//        int[] mAllSize = new int[newbitmap.getWidth() * newbitmap.getHeight()];//获取一个空的直方图
        int[] mAllSize = new int[256];//获取一个空的直方图
        newbitmap.getPixels(mPixelSize, 0, newbitmap.getWidth(), 0, 0, newbitmap.getWidth(), newbitmap.getHeight());
        getHistogram(newbitmap,mPixelSize,flag_color,mAllSize);
        setMaxLevel(maxLevel,newbitmap,mAllSize);
        setMinLevel(minLevel,newbitmap,mAllSize);
        for (int i = 0; i < newbitmap.getWidth() * newbitmap.getHeight(); i++) {
            int color = mPixelSize[i];

            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);
            int alpha = Color.alpha(color);
            switch (flag_color){
                case 1:
                    //红色通道
                    mDstPixelSize[i] = Color.argb(alpha, getTranslateValue(r), g, b);
                    break;
                case 2:
                    //绿色通道
                    mDstPixelSize[i] = Color.argb(alpha, r, getTranslateValue(g), b);
                    break;
                case 3:
                    //蓝色通道
                    mDstPixelSize[i] = Color.argb(alpha, r, g, getTranslateValue(b));
                    break;
                case 4:
                    //原图
                    mDstPixelSize[i] = Color.argb(alpha, r, g, b);
                    break;
            }

        }
        return mDstPixelSize;
    }
    //先把图片的像素点统计出来形成直方图，当直方图中累计的和达到相应的标准的时候，然后停止累加，然后把累加的这部分中的直方图的最大值和最小值作为里面的最大值和最小值
    //获取直方图
    public static void getHistogram(Bitmap newbitmap, int[] mPixelSize, int flag_color, int[] mAllSize){
        for (int i = 0; i < newbitmap.getWidth() * newbitmap.getHeight(); i++) {
            int color = mPixelSize[i];

            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);
            switch (flag_color){
                case 1:
                    //红色通道
                    mAllSize[r] = mAllSize[r] + 1;
                    break;
                case 2:
                    //绿色通道
                    mAllSize[g] = mAllSize[g] + 1;
                    break;
                case 3:
                    //蓝色通道
                    mAllSize[b] = mAllSize[b] + 1;
                    break;
            }
        }
    }
    //获得需要的最大值和最小值
    /*
    * percent  表示需要的灰度比是多少
    * */
    public static void setMaxLevel(double percent, Bitmap newbitmap, int[] mAllSize){
        Constants.picStretchMax = getMaxValue(percent,newbitmap,mAllSize);
    }
    public static void setMinLevel(double percent, Bitmap newbitmap, int[] mAllSize){
        Constants.picStretchMin = getMinValue(percent,newbitmap,mAllSize);
    }

    public static int getMaxValue(double percent, Bitmap newbitmap, int[] mAllSize){
        //对直方图进行排序
        int number = (int) (percent*newbitmap.getHeight()*newbitmap.getWidth());
//        LogUtils.showVerbose("CameraModifyMeiHuaFragment","最大值number="+number);
        int max = mAllSize[0];
        for (int i=0;i<mAllSize.length;i++){
            if (max<=number){
                max = max +mAllSize[i];
            }else {
                max = i;
                return max;
            }
        }
        return max;
    }
    public static int getMinValue(double percent, Bitmap newbitmap, int[] mAllSize){
        //对直方图进行排序
        int number = (int) (percent*newbitmap.getHeight()*newbitmap.getWidth());
//        LogUtils.showVerbose("CameraModifyMeiHuaFragment","最小值number="+number);
        int min = mAllSize[0];
        for (int i=0;i<mAllSize.length;i++){
            if (min<=number){
                min = min +mAllSize[i];
            }else {
                min = i;
                return min;
            }
        }
        return min;
    }

    //得到变换后的拉伸值
    public static int getTranslateValue(int value) {
        int currentValue = 0;
        if (value < Constants.picStretchMin) {
            currentValue = 0;
        } else if (value > Constants.picStretchMax) {
            currentValue = 255;
        } else {
            currentValue = (int) (((value - Constants.picStretchMin) * 255 * 1.00) / (Constants.picStretchMax - Constants.picStretchMin));
        }
        return currentValue;
    }
}
