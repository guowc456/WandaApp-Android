package com.wanta.mobile.wantaproject.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by WangYongqiang on 2016/12/27.
 */
//**
//        * 屏蔽 滑动事件
//        * Created by fc on 2015/7/16.
//        */
public class MyScrollview extends ScrollView {
    private int downX;
    private int downY;
    private int mTouchSlop;
    private getBottomListener bottomListener = null;

    public MyScrollview(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyScrollview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        View view = (View)getChildAt(getChildCount()-1);
        int d = view.getBottom();
        d -= (getHeight()+getScrollY());
        if(d==0) {
            //已经滑动到底部了
            bottomListener.getBottomPosition(d);
        } else{
            super.onScrollChanged(l,t,oldl,oldt);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }

    public interface getBottomListener{
        void getBottomPosition(int d);
    }

    public void setBottomListener(getBottomListener bottomListener){
        this.bottomListener = bottomListener;
    }
}