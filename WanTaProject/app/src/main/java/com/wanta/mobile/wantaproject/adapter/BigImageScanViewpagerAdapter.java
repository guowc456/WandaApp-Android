package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Map;

/**
 * Created by WangYongqiang on 2016/11/16.
 */
public class BigImageScanViewpagerAdapter extends PagerAdapter {
    private Map<Integer, SimpleDraweeView> map;

    public BigImageScanViewpagerAdapter(Context context, Map<Integer, SimpleDraweeView> map){
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
        container.addView(map.get(position));
        return map.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
//        container.removeView(map.get(position));
    }
}
