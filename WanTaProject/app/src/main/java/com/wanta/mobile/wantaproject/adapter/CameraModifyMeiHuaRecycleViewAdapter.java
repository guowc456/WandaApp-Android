package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.CameraViewpagerItemListener;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.ReplyCommentInfo;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;

import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class CameraModifyMeiHuaRecycleViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private String  mList[];
    private MeihuaRecycleViewClickListener mMeihuaRecycleViewClickListener = null;

    public CameraModifyMeiHuaRecycleViewAdapter(Context mContext, String  mList[]) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.camera_modify_meihua_recycleview_adapter,parent,false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder)holder).mItem_camera_modify_meihua_content.setText(mList[position]);
//        ((ItemViewHolder)holder).mItem_camera_modify_meihua_content.setTextSize(Constants.PHONE_WIDTH/60);
        ((ItemViewHolder)holder).mItem_camera_modify_meihua_img.setImageResource(R.mipmap.meihua_icon);
        ((ItemViewHolder)holder).mItem_camera_modify_meihua_img.setWidth(Constants.PHONE_WIDTH/4);
        ((ItemViewHolder)holder).mItem_camera_modify_meihua_img.setHeight(Constants.PHONE_WIDTH/4);
        ((ItemViewHolder)holder).mItem_camera_modify_meihua_img_bg_color.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        LogUtils.showVerbose("CameraModifyMeiHuaRecycleViewAdapter","111");
        ((ItemViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMeihuaRecycleViewClickListener.setOnItemClick(((ItemViewHolder)holder).itemView,((ItemViewHolder)holder).mItem_camera_modify_meihua_img_bg_color);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.length;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{


        private final CustomSimpleDraweeView mItem_camera_modify_meihua_img;
        private final TextView mItem_camera_modify_meihua_content;
        private final LinearLayout mItem_camera_modify_meihua_img_bg_color;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mItem_camera_modify_meihua_img = (CustomSimpleDraweeView) itemView.findViewById(R.id.item_camera_modify_meihua_img);
            mItem_camera_modify_meihua_content = (TextView) itemView.findViewById(R.id.item_camera_modify_meihua_content);
            mItem_camera_modify_meihua_img_bg_color = (LinearLayout) itemView.findViewById(R.id.item_camera_modify_meihua_img_bg_color);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mMeihuaRecycleViewClickListener.setOnItemClick(v,mItem_camera_modify_meihua_img_bg_color);
//                }
//            });
        }
    }
    public interface MeihuaRecycleViewClickListener{
        void setOnItemClick(View v, LinearLayout image_bg_color);
    }
    public void setMeihuaRecycleViewClickListener(MeihuaRecycleViewClickListener mMeihuaRecycleViewClickListener){
        this.mMeihuaRecycleViewClickListener = mMeihuaRecycleViewClickListener;
    }
}
