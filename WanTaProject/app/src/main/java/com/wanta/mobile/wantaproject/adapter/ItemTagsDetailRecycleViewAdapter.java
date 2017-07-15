package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.uploadimage.DraweeUtils;
import com.wanta.mobile.wantaproject.utils.Constants;

import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class ItemTagsDetailRecycleViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<String> mList;
    private OnItemClickListener mOnItemClickListener = null;

    public ItemTagsDetailRecycleViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setShowNumber(List<String> mList1){
        this.mList = mList1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tags_detail_recycleview_adapter, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ItemViewHolder) holder).item_tags_detail_image.setWidth(Constants.PHONE_WIDTH/4);
        ((ItemViewHolder) holder).item_tags_detail_image.setHeight(Constants.PHONE_WIDTH/4);
        DraweeUtils.loadTagsPics(Uri.parse("http://1zou.me/images/" + mList.get(position)),((ItemViewHolder) holder).item_tags_detail_image);
        ((ItemViewHolder) holder).mTags_detail_image.setSize(Constants.PHONE_WIDTH/20,Constants.PHONE_WIDTH/20);
        ((ItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(((ItemViewHolder) holder).itemView,((ItemViewHolder) holder).mTags_detail_image);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {


        private final CustomSimpleDraweeView item_tags_detail_image;
        private final MyImageView mTags_detail_image;

        public ItemViewHolder(View itemView) {
            super(itemView);
            item_tags_detail_image = (CustomSimpleDraweeView) itemView.findViewById(R.id.item_tags_detail_image);
            mTags_detail_image = (MyImageView) itemView.findViewById(R.id.tags_detail_image);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View v, MyImageView tags_detail_image);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
