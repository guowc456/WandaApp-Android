package com.wanta.mobile.wantaproject.domain;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by WangYongqiang on 2016/12/20.
 */
public class ProfessionInfo {
    private LinearLayout mLinearLayout;
    private int position ;
    private TextView mTextView;

    public LinearLayout getLinearLayout() {
        return mLinearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        mLinearLayout = linearLayout;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public void setTextView(TextView textView) {
        mTextView = textView;
    }
}
