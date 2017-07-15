package com.wanta.mobile.wantaproject.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;

public class FilterColorView extends ImageView {

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
    private int flag = 0;
    private int flag1 = 0;
    private int[] mNewlSize;
    private int[] mAllSize;
    private int[] mDstPixelSize;
    private int[] mPixelSize;
    private double greyLevel = 0;
    private int[] pixelSize = new int[1];
    private boolean is_modify_pic = false;//false，显示原来的图片，true显示修改过的图片

    public FilterColorView(Context context) {
        super(context);
        invalidate();
    }


    public FilterColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
//		bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon);

        invalidate();
    }

//	@Override
//	public void layout(int l, int t, int r, int b) {
//		super.layout(l+paddingLeft, t, r+paddingLeft, b);
//	}

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //新建画笔对象
//        myPaint = new Paint();
//        //描画（原始图片）
////		canvas.drawBitmap(bitmap,0, 0, myPaint);
////		Canvas newCanvas = new Canvas(Bitmap.createBitmap(Constants.PHONE_WIDTH/8, src.getHeight(), Bitmap.Config.ARGB_8888);)
//        //新建颜色矩阵对象
//        myColorMatrix = new ColorMatrix();
//
////		设置颜色矩阵的值
//        myColorMatrix.set(colorArray);
//        //设置画笔颜色过滤器
//        myPaint.setColorFilter(new ColorMatrixColorFilter(myColorMatrix));
//        //描画（处理后的图片）
//        if (Constants.PHONE_WIDTH / 2 - bitmap.getWidth() / 2 >= 0) {
//            canvas.drawBitmap(bitmap, Constants.PHONE_WIDTH / 2 - bitmap.getWidth() / 2, (Constants.PHONE_HEIGHT / 4 - bitmap.getHeight() / 2), myPaint);
//        } else {
//            canvas.drawBitmap(bitmap, 0, (Constants.PHONE_HEIGHT / 4 - bitmap.getHeight() / 2), myPaint);
//        }
//        LogUtils.showVerbose("FilterColorView", "222");
//        invalidate();
//        myPaint = new Paint();
//        mNewbitmap = Bitmap.createBitmap(this.bitmap.getWidth(), this.bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        for (int i = 0; i < mNewbitmap.getHeight(); i++) {
//            for (int j = 0; j < mNewbitmap.getHeight(); j++) {
//                int color = bitmap.getPixel(i, j);
//                LogUtils.showVerbose("FilterColorView", "R="+Color.red(color)+"g="+Color.green(color)+"b="+Color.blue(color));
//                mNewbitmap.setPixel(i, j, Color.rgb(200, 200, 200));
//            }
//        }
//        if (Constants.PHONE_WIDTH / 2 - mNewbitmap.getWidth() / 2 >= 0) {
//            canvas.drawBitmap(bitmap, Constants.PHONE_WIDTH / 2 - mNewbitmap.getWidth() / 2, (Constants.PHONE_HEIGHT / 4 - mNewbitmap.getHeight() / 2), myPaint);
//        } else {
//            canvas.drawBitmap(bitmap, 0, (Constants.PHONE_HEIGHT / 4 - mNewbitmap.getHeight() / 2), myPaint);
//        }
//        invalidate();
//        myPaint = new Paint();
//        mNewbitmap = Bitmap.createBitmap(this.bitmap.getWidth(), this.bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas newcanvas = new Canvas(mNewbitmap);
//        newcanvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.ANTI_ALIAS_FLAG)); //转为可修改
//
//        mPixelSize = new int[mNewbitmap.getWidth() * mNewbitmap.getHeight()];
//        mDstPixelSize = new int[mNewbitmap.getWidth() * mNewbitmap.getHeight()];
//        if (flag1 == 0) {
//            mNewlSize = new int[mNewbitmap.getWidth() * mNewbitmap.getHeight()];
//        }
//        mAllSize = new int[mNewbitmap.getWidth() * mNewbitmap.getHeight()];//获取一个空的直方图
////        LogUtils.showVerbose("FilterColorView", "像素点大小="+mNewbitmap.getWidth() * mNewbitmap.getHeight());
//        mNewbitmap.getPixels(mPixelSize, 0, mNewbitmap.getWidth(), 0, 0, mNewbitmap.getWidth(), mNewbitmap.getHeight());
//        getHistogram(mNewbitmap);
//        setMaxAndMin(greyLevel);
//
//        for (int i = 0; i < mNewbitmap.getWidth() * mNewbitmap.getHeight(); i++) {
//            int color = mPixelSize[i];
//
//            int r = Color.red(color);
//            int g = Color.green(color);
//            int b = Color.blue(color);
//            int alpha = Color.alpha(color);
//            if (imgR != 0) {
//                mDstPixelSize[i] = Color.argb(alpha, getTranslateValue(r), g, b);
//                imgG = 0;
//                imgB = 0;
//            }
//            if (imgG != 0) {
//                mDstPixelSize[i] = Color.argb(alpha, r, getTranslateValue(g), b);
//                imgR = 0;
//                imgB = 0;
//            }
//            if (imgB != 0) {
//                mDstPixelSize[i] = Color.argb(alpha, r, g, getTranslateValue(b));
//                imgR = 0;
//                imgG = 0;
//            }
//            if (imgR == 0 && imgB == 0 && imgR == 0) {
//                mDstPixelSize[i] = Color.argb(alpha, r, g, b);
//            }
//            if (flag == 0) {
//                mNewlSize[i] = Color.argb(alpha, r, g, b);
//                LogUtils.showVerbose("FilterColorView", "hhh");
//            }
//            //先把图片的像素点统计出来形成直方图，当直方图中累计的和达到相应的标准的时候，然后停止累加，然后把累加的这部分中的直方图的最大值和最小值作为里面的最大值和最小值
//            // 获取直方图
//        }
//        if (flag == 0) {
//            mNewbitmap.setPixels(mNewlSize, 0, mNewbitmap.getWidth(), 0, 0, mNewbitmap.getWidth(), mNewbitmap.getHeight());
//            LogUtils.showVerbose("FilterColorView", "iiii");
//        } else {
//            mNewbitmap.setPixels(mDstPixelSize, 0, mNewbitmap.getWidth(), 0, 0, mNewbitmap.getWidth(), mNewbitmap.getHeight());
//            LogUtils.showVerbose("FilterColorView", "mmm");
//        }
//        flag = flag + 1;
//        flag1 = flag1 + 1;
//        canvas.drawBitmap(mNewbitmap, Constants.PHONE_WIDTH / 2 - mNewbitmap.getWidth() / 2, (Constants.PHONE_HEIGHT / 4 - mNewbitmap.getHeight() / 2), myPaint);
//        invalidate();
        myPaint = new Paint();
        mNewbitmap = Bitmap.createBitmap(this.bitmap.getWidth(), this.bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas newcanvas = new Canvas(mNewbitmap);
        newcanvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.ANTI_ALIAS_FLAG)); //转为可修改
//
        if (is_modify_pic==true){
//            mNewbitmap.getPixels(pixelSize, 0, mNewbitmap.getWidth(), 0, 0, mNewbitmap.getWidth(), mNewbitmap.getHeight());
            mNewbitmap.setPixels(pixelSize, 0, mNewbitmap.getWidth(), 0, 0, mNewbitmap.getWidth(), mNewbitmap.getHeight());
//            LogUtils.showVerbose("FilterColorView","产生");
        }else {
            mNewlSize = new int[mNewbitmap.getWidth() * mNewbitmap.getHeight()];
            mNewbitmap.getPixels(mNewlSize, 0, mNewbitmap.getWidth(), 0, 0, mNewbitmap.getWidth(), mNewbitmap.getHeight());
            mNewbitmap.setPixels(mNewlSize, 0, mNewbitmap.getWidth(), 0, 0, mNewbitmap.getWidth(), mNewbitmap.getHeight());
//            LogUtils.showVerbose("FilterColorView","默认的");
        }

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
//        LogUtils.showVerbose("FilterColorView", "111");
//        return mNewbitmap;
        return bitmap;
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

    public void setPicTranslateMax(int max) {
        Constants.picStretchMax = max;
    }

    public void setPicTranslateMin(int min) {
        Constants.picStretchMin = min;
    }

    public void setPicR(int r) {
        imgR = r;
    }

    public void setPicG(int g) {
        imgR = g;
    }

    public void setPicB(int b) {
        imgR = b;
    }

    public void fresh(int flag) {
        this.flag = flag;
        invalidate();
    }
    //输入对应的灰度值
    public void setGreyLevel(double greyLevel){
        this.greyLevel = greyLevel;
    }
    public void setPixelSize(int[] pixelSize){
        this.pixelSize = pixelSize;
    }
    public void setIsModifyPic(boolean is_modify_pic){
        this.is_modify_pic = is_modify_pic;
    }

    //得到变换后的灰度值
    public int getTranslateValue(int value) {
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
    //先把图片的像素点统计出来形成直方图，当直方图中累计的和达到相应的标准的时候，然后停止累加，然后把累加的这部分中的直方图的最大值和最小值作为里面的最大值和最小值
    //获取直方图
    public void getHistogram(Bitmap newbitmap){
        for (int i = 0; i < newbitmap.getWidth() * newbitmap.getHeight(); i++) {
            int color = mPixelSize[i];

            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);
            int alpha = Color.alpha(color);
            if (imgR != 0) {
                imgG = 0;
                imgB = 0;
                mAllSize[r] = mAllSize[r] + 1;
            }
            if (imgG != 0) {
                imgR = 0;
                imgB = 0;
                mAllSize[g] = mAllSize[g] + 1;
            }
            if (imgB != 0) {
                imgR = 0;
                imgG = 0;
                mAllSize[b] = mAllSize[b]+1;
            }
        }
    }
    //获得需要的最大值和最小值
    /*
    * percent  表示需要的灰度比是多少
    * */
    public void setMaxAndMin(double percent){
        Constants.picStretchMin = getMinValue(percent);
        Constants.picStretchMax = getMaxValue(percent);
    }
    public void setMaxLevel(double percent){
//        Constants.picStretchMin = getMinValue(percent,newbitmap,mAllSize);
        Constants.picStretchMax = getMaxValue(percent);
    }
    public void setMinLevel(double percent){
        Constants.picStretchMin = getMinValue(percent);
//        Constants.picStretchMax = getMaxValue(percent,newbitmap,mAllSize);
    }
    public int getMaxValue(double percent){
        //对直方图进行排序
        int arr[] = new int[mNewbitmap.getHeight()*mNewbitmap.getWidth()];
        int number = (int) (percent*mNewbitmap.getHeight()*mNewbitmap.getWidth());
        int max = mAllSize[0];
        int allNumber = 0;
        for (int i=0;i<mNewbitmap.getWidth()*mNewbitmap.getHeight();i++){
            if (max<=mAllSize[i]){
                max = mAllSize[i];
            }
            //判断当前的值有没有超过对应的比例

            if (allNumber<number){
                allNumber = allNumber + mAllSize[i];
            }else {
                break;
            }
        }
        return max;
    }
    public int getMinValue(double  percent){
        //对直方图进行排序
        int number = (int) (percent*mNewbitmap.getHeight()*mNewbitmap.getWidth());
        int min = mAllSize[0];
        int allNumber = 0;
        for (int i=0;i<mNewbitmap.getWidth()*mNewbitmap.getHeight();i++){
            if (min>=mAllSize[i]){
                min = mAllSize[i];
            }
            //判断当前的值有没有超过对应的比例

            if (allNumber<number){
                allNumber = allNumber + mAllSize[i];
            }else {
                break;
            }
        }
        return min;
    }
}
