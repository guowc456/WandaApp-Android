package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

/**
 * Created by WangYongqiang on 2016/11/16.
 */
public class FindViewpagerAdapter extends PagerAdapter {
    private static final int MAX_VALUE = 10000;
    private Map<Integer, View> map;

    public FindViewpagerAdapter(Context context, Map<Integer, View> map){
        this.map=map;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        container.addView(map.get(position%map.size()));
        return map.get(position%map.size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
