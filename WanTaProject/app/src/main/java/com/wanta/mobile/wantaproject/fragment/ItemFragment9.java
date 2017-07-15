package com.wanta.mobile.wantaproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.ItemTagsDetailRecycleViewAdapter;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.JsonParseUtils;

/**
 * Created by WangYongqiang on 2016/10/16.
 */
public class ItemFragment9 extends Fragment {

    private View mView;
    private RecyclerView mTags_detail_recycleview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_item_tags_detail,null);
        initId();
        initData();
        return mView;
    }
    private void initId() {
        mTags_detail_recycleview = (RecyclerView) mView.findViewById(R.id.tags_detail_recycleview);
    }

    private void initData() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTags_detail_recycleview.setLayoutManager(gridLayoutManager);

        final ItemTagsDetailRecycleViewAdapter adapter = new ItemTagsDetailRecycleViewAdapter(getActivity());
        adapter.setShowNumber(Constants.TAGS_IAMGE_URL);
        mTags_detail_recycleview.setAdapter(adapter);

        adapter.setOnItemClickListener(new ItemTagsDetailRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, MyImageView tags_detail_image) {
                int position = mTags_detail_recycleview.getChildAdapterPosition(v);
                if (Constants.TAGS_SELECT_IAMGE_URL3.contains(Constants.TAGS_IAMGE_URL.get(position))){
                    //点击之后取消
                    Constants.TAGS_SELECT_IAMGE_URL3.remove(Constants.TAGS_IAMGE_URL.get(position));
                    tags_detail_image.setVisibility(View.GONE);
                    JsonParseUtils.getToString(Constants.TAGS_SELECT_IAMGE_URL3);
                }else {
                    Constants.TAGS_SELECT_IAMGE_URL3.add(Constants.TAGS_IAMGE_URL.get(position));
                    tags_detail_image.setVisibility(View.VISIBLE);
                    JsonParseUtils.getToString(Constants.TAGS_SELECT_IAMGE_URL3);
                }
            }
        });
    }
}
