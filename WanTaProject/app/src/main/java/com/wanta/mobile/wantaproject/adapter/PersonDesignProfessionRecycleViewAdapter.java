package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.utils.Constants;

import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class PersonDesignProfessionRecycleViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private String[] mContentList;
    private OnPersonDesignProfessionItemListener mOnPersonDesignProfessionItemListener = null;

    public PersonDesignProfessionRecycleViewAdapter(Context mContext, String[] mContentList) {
        this.mContext = mContext;
        this.mContentList = mContentList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.person_design_profession_recycleview_adapter,parent,false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder)holder).mItem_profession.setText(mContentList[position]);
    }

    @Override
    public int getItemCount() {
        return mContentList.length;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        private final TextView mItem_profession;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mItem_profession = (TextView) itemView.findViewById(R.id.item_profession);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPersonDesignProfessionItemListener.setItemClick(v);
                }
            });
        }
    }
    public interface OnPersonDesignProfessionItemListener{
        void setItemClick(View view);
    }
    public void setOnPersonDesignItemListener(OnPersonDesignProfessionItemListener mOnPersonDesignProfessionItemListener){
        this.mOnPersonDesignProfessionItemListener = mOnPersonDesignProfessionItemListener;
    }
}
