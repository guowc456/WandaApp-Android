package com.wanta.mobile.wantaproject.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;

/**
 * Created by WangYongqiang on 2016/12/10.
 */
public class AdapterUtils {
    //调整textview的大小
    public static void setTextViewSize(Activity context, int tv_id, int size){
        TextView textView = (TextView) context.findViewById(tv_id);
        textView.setTextSize(changeTextSize(context,size));
    }
    public static int changeTextSize(Activity context,int size) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                size, r.getDisplayMetrics());
    }
}
