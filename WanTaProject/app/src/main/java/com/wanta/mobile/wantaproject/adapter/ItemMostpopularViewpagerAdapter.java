package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

/**
 * Created by WangYongqiang on 2016/11/16.
 */
public class ItemMostpopularViewpagerAdapter extends PagerAdapter {
    private Map<Integer, View> map;
    private OnItemViewpagerListener mOnItemViewpagerListener = null;

    public ItemMostpopularViewpagerAdapter(Context context, Map<Integer, View> map){
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
                mOnItemViewpagerListener.setItemViewPagerClick(v,position);
            }
        });
        container.addView(map.get(position%map.size()));
        return map.get(position%map.size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface OnItemViewpagerListener{
        void setItemViewPagerClick(View view, int position);
    }
    public void setOnItemViewpagerListener(OnItemViewpagerListener mOnItemViewpagerListener){
        this.mOnItemViewpagerListener = mOnItemViewpagerListener;
    }
}
