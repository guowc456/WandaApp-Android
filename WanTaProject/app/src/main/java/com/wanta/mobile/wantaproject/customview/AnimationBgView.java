package com.wanta.mobile.wantaproject.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/24.
 */
public class AnimationBgView extends View {

    private Paint mPaint;
    private float bg_rad_x;
    private float bg_rad_y;
    private float bg_radidus;
    private List<String> mList;
    private int downX;
    private int downY;
    private OnReturnMessage mOnReturnMessage ;
    private int distance;//行间距
    private int line_length;//线的长度

    public AnimationBgView(Context context) {
        this(context,null);
    }

    public AnimationBgView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public AnimationBgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        distance = Constants.PHONE_WIDTH/13;
        line_length = Constants.PHONE_WIDTH/4;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomBgView);
        //根据属性名称获取对应的值，属性名称的格式为类名_属性名
        bg_rad_x = typedArray.getFloat(R.styleable.CustomBgView_bg_rad_x, 0f);
        bg_rad_y = typedArray.getFloat(R.styleable.CustomBgView_bg_rad_y, 0f);
        bg_radidus = typedArray.getFloat(R.styleable.CustomBgView_bg_radidus, 0f);
        mPaint = new Paint();
        mList = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(getResources().getColor(R.color.animation_bg_color));
        mPaint.setAlpha(100);
        canvas.drawCircle(bg_rad_x,bg_rad_y,bg_radidus,mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(bg_rad_x,bg_rad_y,bg_radidus-5,mPaint);
        drawLineAndText(mPaint,canvas,mList);
    }

    private void drawLineAndText(Paint mpaint,Canvas canvas,List<String> mList) {
        mpaint.setColor(Color.WHITE);
        mpaint.setTextSize(20);//设置字体的大小
        mpaint.setTypeface(Typeface.DEFAULT);
        mpaint.setStrokeWidth(2);//设置划线的宽度
        mPaint.setAlpha(255);//设置划线的透明度，255不透明，0完全透明
        mPaint.setStrokeWidth((float) 2.8);
        mPaint.setTextSize(Constants.PHONE_WIDTH/30);
        if (mList.size()%2==0){
            //如果当前的个数为偶数,就不从中心点开始画
            for (int i=1;i<=mList.size()/2;i++){
                //画分段的线和添加字体
                if (i==1){
                    canvas.drawLine(bg_rad_x,bg_rad_y-i*(distance/2),bg_rad_x-mList.get(mList.size()/2-i).length()*20-line_length,bg_rad_y-i*(distance/2),mPaint);
                    canvas.drawCircle(bg_rad_x-mList.get(mList.size()/2-i).length()*20-line_length,bg_rad_y-i*(distance/2),5,mPaint);
                    canvas.drawText(mList.get(mList.size()/2-i),bg_rad_x-20-line_length,bg_rad_y-i*(distance/2)-10,mPaint);
                    canvas.drawLine(bg_rad_x,bg_rad_y+i*(distance/2),bg_rad_x-mList.get(i+1).length()*20-line_length,bg_rad_y+i*(distance/2),mPaint);
                    canvas.drawCircle(bg_rad_x-mList.get(i+1).length()*20-line_length,bg_rad_y+i*(distance/2),5,mPaint);
                    canvas.drawText(mList.get(mList.size()/2+i-1),bg_rad_x-20-line_length,bg_rad_y+i*(distance/2)-10,mPaint);
                }else {
                    canvas.drawLine(bg_rad_x,bg_rad_y-i*distance+(distance/2),bg_rad_x-mList.get(mList.size()/2-i).length()*20-line_length,bg_rad_y-i*distance+(distance/2),mPaint);
                    canvas.drawCircle(bg_rad_x-mList.get(mList.size()/2-i).length()*20-line_length,bg_rad_y-i*distance+(distance/2),5,mPaint);
                    canvas.drawText(mList.get(mList.size()/2-i),bg_rad_x-200,bg_rad_y-i*distance-10+(distance/2),mPaint);
                    canvas.drawLine(bg_rad_x,bg_rad_y+i*distance-(distance/2),bg_rad_x-mList.get(i+1).length()*20-line_length,bg_rad_y+i*distance-(distance/2),mPaint);
                    canvas.drawCircle(bg_rad_x-mList.get(i+1).length()*20-line_length,bg_rad_y+i*distance-(distance/2),5,mPaint);
                    canvas.drawText(mList.get(mList.size()/2+i-1),bg_rad_x-20-line_length,bg_rad_y+i*distance-10-(distance/2),mPaint);
                }
                //连接分段的线
                canvas.drawLine(bg_rad_x,bg_rad_y,bg_rad_x,bg_rad_y-mList.size()/2*distance+(distance/2),mPaint);
                canvas.drawLine(bg_rad_x,bg_rad_y,bg_rad_x,bg_rad_y+mList.size()/2*distance-(distance/2),mPaint);
            }

        }else {
            //是奇数，要从中间开始画
            canvas.drawLine(bg_rad_x,bg_rad_y,bg_rad_x-mList.get((mList.size()-1)/2).length()*20-line_length,bg_rad_y,mPaint);
            canvas.drawCircle(bg_rad_x-mList.get((mList.size()-1)/2).length()*20-line_length,bg_rad_y,5,mPaint);
            canvas.drawText(mList.get((mList.size()-1)/2),bg_rad_x-20-line_length,bg_rad_y-10,mPaint);
            for (int i=1;i<=(mList.size()-1)/2;i++){
                canvas.drawLine(bg_rad_x,bg_rad_y-i*distance,bg_rad_x-mList.get((mList.size()-1)/2-i).length()*20-line_length,bg_rad_y-i*distance,mPaint);
                canvas.drawCircle(bg_rad_x-mList.get((mList.size()-1)/2-i).length()*20-line_length,bg_rad_y-i*distance,5,mPaint);
                canvas.drawText(mList.get((mList.size()-1)/2-i),bg_rad_x-20-line_length,bg_rad_y-i*distance-10,mPaint);
                canvas.drawLine(bg_rad_x,bg_rad_y+i*distance,bg_rad_x-mList.get(i+1).length()*20-line_length,bg_rad_y+i*distance,mPaint);
                canvas.drawCircle(bg_rad_x-mList.get(i+1).length()*20-line_length,bg_rad_y+i*distance,5,mPaint);
                canvas.drawText(mList.get(i+3),bg_rad_x-20-line_length,bg_rad_y+i*distance-10,mPaint);
            }
            //连接分段的线
            canvas.drawLine(bg_rad_x,bg_rad_y,bg_rad_x,bg_rad_y-(mList.size()-1)/2*distance,mPaint);
            canvas.drawLine(bg_rad_x,bg_rad_y,bg_rad_x,bg_rad_y+(mList.size()-1)/2*distance,mPaint);
        }
    }

    public void setBgRadidus( float bg_radidus){
        this.bg_radidus = bg_radidus;
        requestLayout();
        invalidate();
    }

    public void setLocation(float bg_rad_x,float bg_rad_y){
        this.bg_rad_x = bg_rad_x;
        this.bg_rad_y = bg_rad_y;
    }

    public void setLineText(List<String> mList){
        this.mList = mList;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventCode=  event.getAction();
        switch(eventCode){
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if(Math.abs(upX- downX) < 10 && Math.abs(upY - downY) < 10){//点击事件
                    performClick();
                    doClickAction((upX + downX)/2,(upY + downY)/2);
//                    LogUtils.showVerbose("AnimationBgView","x=="+(upX + downX)/2+"  y=="+(upY + downY)/2);
                }
                break;
        }
        return true;
    }

    public void doClickAction(int x, int y) {
        if (mList.size()%2==0){
            if (y<=bg_rad_y){
                //在上半截
                for (int i=1;i<=mList.size()/2;i++){
                    if (i==mList.size()/2){
                        if (y<bg_rad_y-((mList.size()/2)+1-i)*(distance/2)&&y>bg_rad_y-((mList.size()/2)+1-i)*distance-(distance/2)&&x<bg_rad_x&&x>bg_rad_x-mList.get(i-1).length()*20-line_length){
                            LogUtils.showVerbose("AnimationBgView","第"+i+"个");
                            mOnReturnMessage.onClickMessage(mList.get(i-1));
                        }
                    }else {
                        if (y>bg_rad_y-((mList.size()/2)+1-i)*distance-(distance/2)&&y<bg_rad_y-((mList.size()/2)-i)*distance-(distance/2)&&x<bg_rad_x&&x>bg_rad_x-mList.get(i-1).length()*20-line_length){
                            LogUtils.showVerbose("AnimationBgView","第"+i+"个");
                            mOnReturnMessage.onClickMessage(mList.get(i-1));
                        }
                    }

                }

            }else {
                //在下半截
                for (int i=1;i<=mList.size()/2;i++){
                    if (i==1){
                        if (y<bg_rad_y+i*(distance/2)&&y>bg_rad_y&&x<bg_rad_x&&x>bg_rad_x-mList.get((mList.size()/2+i)-1).length()*20-line_length){
                            LogUtils.showVerbose("AnimationBgView","第"+(mList.size()/2+i)+"个");
                            mOnReturnMessage.onClickMessage(mList.get((mList.size()/2+i)-1));
                        }
                    }else {
                        if (y<bg_rad_y+(i-1)*distance+(distance/2)&&y>bg_rad_y+(i-1)*distance-(distance/2)&&x<bg_rad_x&&x>bg_rad_x-mList.get((mList.size()/2+i)-1).length()*20-line_length){
                            LogUtils.showVerbose("AnimationBgView","第"+(mList.size()/2+i)+"个");
                            mOnReturnMessage.onClickMessage(mList.get((mList.size()/2+i)-1));
                        }
                    }

                }
            }

        }else {
            //是奇数条
            if (y<bg_rad_y){
                //在上半截
                for (int i=1;i<=(mList.size()+1)/2;i++){
                    if (y<bg_rad_y-((mList.size()+1)/2-i)*distance&&y>bg_rad_y-((mList.size()+1)/2-i)*distance-distance&&x<bg_rad_x&&x>bg_rad_x-mList.get(i-1).length()*20-line_length){
                        LogUtils.showVerbose("AnimationBgView","第"+i+"个");
                        mOnReturnMessage.onClickMessage(mList.get(i-1));
                    }
                }

            }else if (y>bg_rad_y){
                //在下半截
                for (int i=1;i<=(mList.size()-1)/2;i++){
                    if (y<bg_rad_y+i*distance&&y>bg_rad_y+i*distance-distance&&x<bg_rad_x&&x>bg_rad_x-mList.get((mList.size()/2+i)).length()*20-line_length){
                        LogUtils.showVerbose("AnimationBgView","第"+((mList.size()-1)/2+i+1)+"个");
                        mOnReturnMessage.onClickMessage(mList.get((mList.size()/2+i)));
                    }
                }
            }
        }
    }

    public interface OnReturnMessage{
        public void  onClickMessage(String returnMsg);
    }
    public void setOnReturnMessage(OnReturnMessage mOnReturnMessage){
        this.mOnReturnMessage = mOnReturnMessage;
    }
}
