package com.wanta.mobile.wantaproject.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wanta.mobile.wantaproject.calendar.CalendarDateUtils;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;

/**
 * Created by WangYongqiang on 2017/1/6.
 */
public class CustomCalendarView extends View {

    private int year = 0;
    private int month = 0;
    private Paint mMPaint;
    private float oldX = 0;
    private float oldY = 0;
    private float newX = 0;
    private int allGroup = 0;
    private int currentGroup = 0;
    private int mMonthDays;

    public CustomCalendarView(Context context) {
        super(context);
        init();
    }

    public CustomCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    //进行初始化
    private void init() {
        mMPaint = new Paint();
        mMPaint.setColor(Color.GRAY);
        allGroup = 0;
        currentGroup = 0;
        //获取当前需要的天数
        mMonthDays = CalendarDateUtils.getMonthDays(year, month);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        for (int i=1;i<=mMonthDays;i++){
            //设置字体的大小和颜色
            mMPaint.setTextSize(30);
            mMPaint.setStrokeWidth(6);
            canvas.drawText(i+"",(i-1)*(Constants.PHONE_WIDTH/7)+Constants.PHONE_WIDTH/14,Constants.PHONE_HEIGHT/24,mMPaint);
        }
    }

    public void setYearAndMonth(int year, int month){
        this.year = year;
        this.month = month;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                oldX = event.getX();
                oldY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                newX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                //设定只在自定义view的区间才可以滑动有效
                if (oldY<Constants.PHONE_HEIGHT/12){
                    if (newX<oldX){
                        LogUtils.showVerbose("CustomCalendarView","向左滑动了");
//                        //获取当前需要的天数
//                        int mMonthDays = CalendarDateUtils.getmMonthDays(year, month);
                        //获取当前总共有几个分组
                        allGroup = getAllGroup(mMonthDays);
                        //获取不同数组的天数
                        getEveryGroupDays(currentGroup);
                    }else {
                        LogUtils.showVerbose("CustomCalendarView","向右滑动了");
                    }
                }
                break;
        }
        return true ;
    }


    private void getEveryGroupDays(int curgroup) {
        int[] days = new int[7];

    }

    //获取所有天数的分组
    private int getAllGroup(int monthDays) {
        int numGroup = 0;
        int day1 = monthDays/7;
        int day2 = monthDays%7;
        if (day2>0){
            numGroup = day1 + 1;
        }else {
            numGroup = day1 ;
        }
        return numGroup;
    }
}
