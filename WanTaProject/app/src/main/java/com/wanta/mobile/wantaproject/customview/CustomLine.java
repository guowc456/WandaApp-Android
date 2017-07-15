package com.wanta.mobile.wantaproject.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;

/**
 * Created by WangYongqiang on 2016/12/6.
 */
public class CustomLine extends View {

    private int lineNumber = 1;//定义需要划分的线的个数
    private int selectLine = 1;//定义选中的线段
    private int lineHeight = 10;//定义线距离顶部的高度
    private int lineWidth = 10;//定义每段线的长度

    private Paint mPaint = new Paint();

    public CustomLine(Context context) {
        super(context);
    }

    public CustomLine(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //先画出来选中的部分
        mPaint.setStrokeWidth(Constants.PHONE_WIDTH/60);
//        for (int i=1;i<=lineNumber;i++){
//            if (i==selectLine){
//                mPaint.setColor(getResources().getColor(R.color.head_bg_color));
//                canvas.drawLine((i-1)*lineWidth,lineHeight,i*lineWidth,lineHeight,mPaint);
//            }else {
//                mPaint.setColor(getResources().getColor(R.color.line_color));
//                canvas.drawLine((i-1)*lineWidth,lineHeight,i*lineWidth,lineHeight,mPaint);
//            }
//        }
        mPaint.setColor(getResources().getColor(R.color.head_bg_color));
        canvas.drawLine((selectLine-1)*lineWidth,lineHeight,selectLine*lineWidth,lineHeight,mPaint);
//        mPaint.setColor(getResources().getColor(R.color.head_bg_color));
//        canvas.drawLine(0,lineHeight,lineWidth,lineHeight,mPaint);
//        LogUtils.showVerbose("CustomLine","lineHeight="+lineHeight+" lineWidth="+lineWidth+"  lineHeight="+lineHeight);
//        invalidate();
    }
    public void setLineNumber(int lineNumber){
        this.lineNumber = lineNumber;
//        invalidate();
    }

    public void setSelectLine(int selectLine){
        this.selectLine = selectLine;
        invalidate();
    }
    public void setLineHeight(int lineWidth,int lineHeight){
        this.lineHeight = lineHeight;
        this.lineWidth = lineWidth;
//        invalidate();
    }
}
