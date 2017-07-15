package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.wanta.mobile.wantaproject.customview.FilterColorView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WangYongqiang on 2016/11/16.
 */
public class CameraViewpagerAdapter extends PagerAdapter {
    private HashMap<Integer, FilterColorView> map;
    private OnItemViewpagerListener mOnItemViewpagerListener = null;

    public CameraViewpagerAdapter(Context context, HashMap<Integer, FilterColorView> map){
        this.map=map;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
//        return MAX_VALUE;
        return map.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        map.get(position).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemViewpagerListener.setItemViewPagerClick(v,position,map.get(position));
            }
        });
        container.addView(map.get(position));
        return map.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    public interface OnItemViewpagerListener{
        void setItemViewPagerClick(View view, int position, FilterColorView filterColorView);
    }
    public void setOnItemViewpagerListener(OnItemViewpagerListener mOnItemViewpagerListener){
        this.mOnItemViewpagerListener = mOnItemViewpagerListener;
    }
}
