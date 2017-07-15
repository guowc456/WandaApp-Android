package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.utils.Constants;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class FindFragmentAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private int width;
    private int height;
    private int picType;
    private int mList[] ;
    private onFindItemListener mOnFindItemListener = null;
    public FindFragmentAdapter(Context mContext,int mList[],int picType) {
        this.mContext = mContext;
        this.mList = mList;
        this.picType = picType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_find_adapter,parent,false);
        ItemFindViewHolder viewHolder = new ItemFindViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (picType==1){
            ((ItemFindViewHolder)holder).mItem_img.setImageResource(mList[position]);
            ((ItemFindViewHolder)holder).mItem_img.setSize(Constants.PHONE_WIDTH/4,Constants.PHONE_WIDTH/4);
        }else if (picType==2){
            ((ItemFindViewHolder)holder).mItem_img.setImageResource(mList[position]);
            ((ItemFindViewHolder)holder).mItem_img.setSize(Constants.PHONE_WIDTH/4,Constants.PHONE_WIDTH/4);
        }else  if (picType==3){
            ((ItemFindViewHolder)holder).mItem_img.setImageResource(mList[position]);
            ((ItemFindViewHolder)holder).mItem_img.setSize(Constants.PHONE_WIDTH/4,Constants.PHONE_WIDTH/4);
        }else if (picType==4){
            ((ItemFindViewHolder)holder).mItem_img.setImageResource(mList[position]);
            ((ItemFindViewHolder)holder).mItem_img.setSize(Constants.PHONE_WIDTH/4,Constants.PHONE_WIDTH/6);
        }else if (picType==5){
            ((ItemFindViewHolder)holder).mItem_img.setImageResource(mList[position]);
            ((ItemFindViewHolder)holder).mItem_img.setSize(Constants.PHONE_WIDTH/4,Constants.PHONE_WIDTH/4);
        }else if (picType==6){
            ((ItemFindViewHolder)holder).mItem_img.setImageResource(mList[position]);
            ((ItemFindViewHolder)holder).mItem_img.setSize(Constants.PHONE_WIDTH/4,Constants.PHONE_WIDTH/4);
        }

    }

    @Override
    public int getItemCount() {
        return mList.length;
    }

    class ItemFindViewHolder extends RecyclerView.ViewHolder{

        private final MyImageView mItem_img;

        public ItemFindViewHolder(View itemView) {
            super(itemView);
            mItem_img = (MyImageView) itemView.findViewById(R.id.item_img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnFindItemListener.onItemClick(v);
                }
            });
        }
    }
    public interface onFindItemListener{
        void onItemClick(View view);
    }
    public void setOnFindItemListener(onFindItemListener mOnFindItemListener){
        this.mOnFindItemListener = mOnFindItemListener;
    }
}
