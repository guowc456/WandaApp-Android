package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.utils.Constants;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class PersonDesignLocationRecycleViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private String[] mContentList;
    private OnPersonDesignLocationItemListener mOnPersonDesignLocationItemListener = null;

    public PersonDesignLocationRecycleViewAdapter(Context mContext, String[] mContentList) {
        this.mContext = mContext;
        this.mContentList = mContentList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.person_design_location_recycleview_adapter,parent,false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH/3,Constants.PHONE_HEIGHT/18);
        ((ItemViewHolder)holder).mItem_location_content_layout.setLayoutParams(params);
        ((ItemViewHolder)holder).mItem_location_content.setText(mContentList[position]);
    }

    @Override
    public int getItemCount() {
        return mContentList.length;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        private final TextView mItem_location_content;
        private final LinearLayout mItem_location_content_layout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mItem_location_content = (TextView) itemView.findViewById(R.id.item_location_content);
            mItem_location_content_layout = (LinearLayout) itemView.findViewById(R.id.item_location_content_layout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPersonDesignLocationItemListener.setItemClick(v);
                }
            });
        }
    }
    public interface OnPersonDesignLocationItemListener{
        void setItemClick(View view);
    }
    public void setOnPersonDesignLocationItemListener(OnPersonDesignLocationItemListener mOnPersonDesignLocationItemListener){
        this.mOnPersonDesignLocationItemListener = mOnPersonDesignLocationItemListener;
    }
}
