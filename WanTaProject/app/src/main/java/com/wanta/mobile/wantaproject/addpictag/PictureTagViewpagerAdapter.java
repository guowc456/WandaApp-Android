package com.wanta.mobile.wantaproject.addpictag;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.HashMap;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/11/16.
 */
public class PictureTagViewpagerAdapter extends PagerAdapter {
    private List<PictureTagLayout> map;
    private OnPictureTagItemViewpagerListener mOnPictureTagItemViewpagerListener = null;

    public PictureTagViewpagerAdapter(Context context, List<PictureTagLayout> map){
        this.map=map;
    }
    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        map.get(position).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnPictureTagItemViewpagerListener.setItemViewPagerClick(v,position,map.get(position));
            }
        });
        ViewGroup parent = (ViewGroup)map.get(position).getParent();
        if (parent!=null){
            parent.removeAllViews();
        }
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

    public interface OnPictureTagItemViewpagerListener{
        void setItemViewPagerClick(View view, int position, PictureTagLayout pictureTagLayout);
    }
    public void setOnPictureTagItemViewpagerListener(OnPictureTagItemViewpagerListener mOnPictureTagItemViewpagerListener){
        this.mOnPictureTagItemViewpagerListener = mOnPictureTagItemViewpagerListener;
    }
}
