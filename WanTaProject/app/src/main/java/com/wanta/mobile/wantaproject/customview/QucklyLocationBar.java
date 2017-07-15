package com.wanta.mobile.wantaproject.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.wanta.mobile.wantaproject.utils.LogUtils;

/**
 * Created by WangYongqiang on 2016/12/18.
 */
public class QucklyLocationBar extends View {

    private int width;
    private int height;
    private boolean isup = true;//默认是弹起的
    private String[] msg = {
            "当前","历史","热门","A","B","C","D","E","F","G","H","J","K","L","M","N","P","Q","R","S","T","W","X","Y","Z"
    };
//    private String[] msg = {
//            "当前","历史","热门","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
//    };
    private Paint mPaint;
    private QuicklyLocationBarListener mQuicklyLocationBarListener = null ;

    public QucklyLocationBar(Context context) {
        super(context,null);
        init();
    }

    public QucklyLocationBar(Context context, AttributeSet attrs) {
        super(context, attrs,0);
        init();
    }

    public QucklyLocationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    //进行初始化
    public void init(){
        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        //获取每个字母的宽度所占的宽度
        int eachWidht = height/(msg.length+4);
        //获取每个应该画的位置
        for (int i=0;i<msg.length;i++){
            mPaint.setColor(Color.BLACK);
            mPaint.setTextSize(20);
            if (i>=0&&i<3){
                canvas.drawText(msg[i],width/4,eachWidht/2+eachWidht*i,mPaint);
            }else {
                canvas.drawText(msg[i],width/2,eachWidht/2+eachWidht*i,mPaint);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eachWidht = height/(msg.length+4);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                LogUtils.showVerbose("QucklyLocationBar","我按下了");
                //首先获取当前的高度
                float y = event.getY();//相对于滑棒的y值
                for (int i=0;i<msg.length;i++){
                    if (y<eachWidht){
                        LogUtils.showVerbose("QucklyLocationBar","点击了"+msg[0]);
                        mQuicklyLocationBarListener.setItemListener(msg[0],false);
                    }else if (y>(i-1)*eachWidht&&y<i*eachWidht){
                        LogUtils.showVerbose("QucklyLocationBar","点击了"+msg[i]);
                        mQuicklyLocationBarListener.setItemListener(msg[i],false);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.showVerbose("QucklyLocationBar","我移动了");
                float y1 = event.getY();//获取滑动后的位置
                for (int i=0;i<msg.length;i++){
                    if (y1<eachWidht){
                        LogUtils.showVerbose("QucklyLocationBar","点击了"+msg[0]);
                        mQuicklyLocationBarListener.setItemListener(msg[0],false);
                    }else if (y1>(i)*eachWidht&&y1<(i+1)*eachWidht){
                        LogUtils.showVerbose("QucklyLocationBar","点击了"+msg[i]);
                        mQuicklyLocationBarListener.setItemListener(msg[i],false);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.showVerbose("QucklyLocationBar","我松开了");
                mQuicklyLocationBarListener.setItemListener("",true);
                break;
        }
        return true;
    }

    public void setBarWidth(int width){
        this.width = width;
    }
    public void setBarHeight(int height){
        this.height = height;
    }
    public interface QuicklyLocationBarListener {
        void setItemListener(String currentStr,boolean isup);
    }
    public void setQuicklyLocationBarListener(QuicklyLocationBarListener mQuicklyLocationBarListener){
        this.mQuicklyLocationBarListener = mQuicklyLocationBarListener;
    }
}
