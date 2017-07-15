package com.wanta.mobile.wantaproject.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.generic.RoundingParams;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;

/**
 * Created by WangYongqiang on 2016/11/23.
 */
public class SystemUtils {
    private LinearLayout system_pop_window_ok_layout;

    public static void showOrHide(Context context) {
        //如果没显示就打开，如果打开了就关闭
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果没开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
    }

    public static void hideInput(Context context, Activity className, View submitBt) {
//        //如果没显示就打开，如果打开了就关闭
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        // 得到InputMethodManager的实例
//        if (imm.isActive()) {
//            // 如果没开启
//            imm.toggleSoftInput(0,
//                    InputMethodManager.HIDE_NOT_ALWAYS);
//            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
//        }
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(className.getCurrentFocus().getWindowToken()
                , InputMethodManager.HIDE_NOT_ALWAYS);

//接受软键盘输入的编辑文本或其它视图
        inputMethodManager.showSoftInput(submitBt, InputMethodManager.SHOW_FORCED);
    }

    //默认弹出框弹出框
    public static void showSystemPopWindow(Context context, View showbottom,String title,String content) {
        View view = LayoutInflater.from(context).inflate(R.layout.system_pop_window_layout, null);
        final PopupWindow popupWindow = new PopupWindow(view, Constants.PHONE_WIDTH, Constants.PHONE_HEIGHT, false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.showAsDropDown(showbottom);
        View system_pop_window_view = popupWindow.getContentView();
        CustomSimpleDraweeView system_pop_window_drawee = (CustomSimpleDraweeView) system_pop_window_view.findViewById(R.id.system_pop_window_drawee);
//        system_pop_window_drawee.setSize((float) ((Constants.PHONE_WIDTH*1.00/6)*3),(int) (534*(((float) ((Constants.PHONE_WIDTH*1.00/6)*3))/911)));
        system_pop_window_drawee.setWidth((int) ((Constants.PHONE_WIDTH * 1.00 / 7) * 5));//911,534
//        system_pop_window_drawee.setAspectRatio((float) (534*1.00/911));
        system_pop_window_drawee.setHeight((int) (534 * (((float) ((Constants.PHONE_WIDTH * 1.00 / 7) * 5)) / 911)));
        system_pop_window_drawee.setScaleType(ImageView.ScaleType.FIT_CENTER);
        SimpleDraweeControlUtils.setAssetsImageTopRangle(context, system_pop_window_drawee, 12, "system_pop_window_icon.png");

        LinearLayout system_pop_window_message_layout = (LinearLayout) system_pop_window_view.findViewById(R.id.system_pop_window_message_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) ((Constants.PHONE_WIDTH * 1.00 / 7) * 5),
                Constants.PHONE_HEIGHT / 4);
        system_pop_window_message_layout.setLayoutParams(params);

//        system_pop_window_drawee.setImageResource(R.mipmap.system_pop_window_icon);
        TextView system_pop_window_title = (TextView) system_pop_window_view.findViewById(R.id.system_pop_window_title);
        system_pop_window_title.setText(title);
        TextView system_pop_window_content = (TextView) system_pop_window_view.findViewById(R.id.system_pop_window_content);
        system_pop_window_content.setText(content);
        TextView system_pop_window_ok = (TextView) system_pop_window_view.findViewById(R.id.system_pop_window_ok);
        system_pop_window_ok.setText("知道了");
        LinearLayout system_pop_window_ok_layout = (LinearLayout) system_pop_window_view.findViewById(R.id.system_pop_window_ok_layout);
        system_pop_window_ok_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

}
