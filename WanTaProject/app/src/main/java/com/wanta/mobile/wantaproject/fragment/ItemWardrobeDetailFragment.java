package com.wanta.mobile.wantaproject.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.WardrobeCalendarActivity;
import com.wanta.mobile.wantaproject.activity.WardrobeDetailActivity;
import com.wanta.mobile.wantaproject.adapter.ExpandAdapter;
import com.wanta.mobile.wantaproject.adapter.ItemWardrobeDetailRecycleViewAdapter;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.FirstItem;
import com.wanta.mobile.wantaproject.domain.SecondItem;
import com.wanta.mobile.wantaproject.domain.ThirdItem;
import com.wanta.mobile.wantaproject.uploadimage.FileUtils;
import com.wanta.mobile.wantaproject.uploadimage.ImagesSelectorActivity;
import com.wanta.mobile.wantaproject.uploadimage.SelectorSettings;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/16.
 */
public class ItemWardrobeDetailFragment extends Fragment {

    private View mView_wardrobe_detail;
    private RecyclerView mWardrobe_detail_recycleview;
    private List<String> mStringList = new ArrayList<>(10);
    private int itemNumber = 0;
    private ItemWardrobeDetailRecycleViewAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView_wardrobe_detail = inflater.inflate(R.layout.fragment_item_wardrobe_detail,container,false);
        initId();
        return mView_wardrobe_detail;
    }

    private void initId() {
        mWardrobe_detail_recycleview = (RecyclerView) mView_wardrobe_detail.findViewById(R.id.wardrobe_detail_recycleview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mWardrobe_detail_recycleview.setLayoutManager(gridLayoutManager);
        LogUtils.showVerbose("ItemWardrobeDetailFragment","大小1："+Constants.detail_images.size());
        LogUtils.showVerbose("ItemWardrobeDetailFragment","大小2："+Constants.Wardrobe_detail_imags_url.size());
        mAdapter = new ItemWardrobeDetailRecycleViewAdapter(getActivity());
        mStringList.clear();
        if (Constants.Wardrobe_detail_imags_url.size()>=10){
            itemNumber = 1;
            for (int i=0;i<10;i++){
                mStringList.add(Constants.Wardrobe_detail_imags_url.get(i));
            }
            mAdapter.setShowNumber(mStringList);
        }else {
            for (int i=0;i<Constants.Wardrobe_detail_imags_url.size();i++){
                mStringList.add(Constants.Wardrobe_detail_imags_url.get(i));
            }
            mAdapter.setShowNumber(mStringList);
        }
        mWardrobe_detail_recycleview.setAdapter(mAdapter);
        mWardrobe_detail_recycleview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            //用来标记是否正在向最后一个滑动
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LogUtils.showVerbose("ItemWardrobeDetailFragment","newState"+newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    //大于0表示正在向下滚动
                    isSlidingToLast = true;
                    LogUtils.showVerbose("ItemWardrobeDetailFragment","向下滑动");
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    //屏幕中最后一个可见子项的position
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    //屏幕中第一个可见子项的position
                    int firstVisibleItemPosition =  layoutManager.findFirstCompletelyVisibleItemPosition();
                    //当前屏幕所看到的子项个数
                    int visibleItemCount = layoutManager.getChildCount();
                    //当前RecyclerView的所有子项个数
                    int totalItemCount = layoutManager.getItemCount();
                    //RecyclerView的滑动状态
                    int state = recyclerView.getScrollState();
                    if(visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 ){
                        //加载更多
                        LogUtils.showVerbose("ItemWardrobeDetailFragment","加载更多"+"visibleItemCount="+
                                visibleItemCount+" lastVisibleItemPosition="+lastVisibleItemPosition+" totalItemCount="+totalItemCount+"  state=="+state);
//                        mStringList.clear();
                        if (Constants.Wardrobe_detail_imags_url.size()>(itemNumber+1)*10){
                            for (int i=itemNumber*10;i<(itemNumber+1)*10;i++){
                                mStringList.add(Constants.Wardrobe_detail_imags_url.get(i));

                            }
                            mAdapter.setShowNumber(mStringList);
                            mAdapter.notifyItemChanged(itemNumber*10,(itemNumber+1)*10);
                            itemNumber++;
//                            mAdapter.notifyDataSetChanged();
                        }else if (Constants.Wardrobe_detail_imags_url.size()<(itemNumber+1)*10&&Constants.Wardrobe_detail_imags_url.size()>=itemNumber*10){
                            for (int i=itemNumber*10;i<Constants.Wardrobe_detail_imags_url.size();i++){
                                mStringList.add(Constants.Wardrobe_detail_imags_url.get(i));
                            }
                            mAdapter.setShowNumber(mStringList);
                            mAdapter.notifyItemChanged(itemNumber*10,Constants.Wardrobe_detail_imags_url.size()-1);
//                            mAdapter.notifyDataSetChanged();
                            itemNumber++;
                        }
                    }else {
                        //继续加载
                        LogUtils.showVerbose("ItemWardrobeDetailFragment","继续加载"+"visibleItemCount="+
                                visibleItemCount+" lastVisibleItemPosition="+lastVisibleItemPosition+" totalItemCount="+totalItemCount+"  state=="+state);
                    }

                } else {
                    //小于等于0表示停止或向左滚动
                    isSlidingToLast = false;
                    LogUtils.showVerbose("ItemWardrobeDetailFragment","向上滑动");
                }
            }
        });
    }
}
