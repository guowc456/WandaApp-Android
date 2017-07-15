package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.ReplyCommentInfo;
import com.wanta.mobile.wantaproject.utils.Constants;

import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class SameStyleTagChooseRecycleViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private String[] same_style_choose;
    private OnStyleChooseItemListener mOnStyleChooseItemListener ;
    private int selectPosition = -1;

    public SameStyleTagChooseRecycleViewAdapter(Context mContext, String[] same_style_choose) {
        this.mContext = mContext;
        this.same_style_choose = same_style_choose;
    }

    public void setSelectPosition(int selectPosition){
        this.selectPosition = selectPosition;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.same_style_tag_choose_recycleview_adapter,parent,false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        ((ItemViewHolder)holder).mStyle_tags_content.setTextSize(Constants.PHONE_WIDTH/60);
        ((ItemViewHolder)holder).mStyle_tags_content.setText(same_style_choose[position]);
        if (selectPosition==-1){
            ((ItemViewHolder)holder).mStyle_tags_content.setTextColor(Color.WHITE);
        }else {
            if (selectPosition==position){
                ((ItemViewHolder)holder).mStyle_tags_content.setTextColor(Color.BLACK);
            }else {
                ((ItemViewHolder)holder).mStyle_tags_content.setTextColor(Color.WHITE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return same_style_choose.length;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{


        private final TextView mStyle_tags_content;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mStyle_tags_content = (TextView) itemView.findViewById(R.id.style_tags_content);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnStyleChooseItemListener.setStyleChooseItemClick(v);
                }
            });
        }
    }

    public interface OnStyleChooseItemListener{
        void setStyleChooseItemClick(View view);
    }
    public void setOnStyleChooseItemListener(OnStyleChooseItemListener mOnStyleChooseItemListener){
        this.mOnStyleChooseItemListener = mOnStyleChooseItemListener;
    }
}
