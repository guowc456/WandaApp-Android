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
public class PersonDesignRecycleViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<String> mContentList;
    private String  mNamesList[];
    private OnPersonDesignItemListener mOnPersonDesignItemListener = null;

    public PersonDesignRecycleViewAdapter(Context mContext, List<String> mContentList) {
        this.mContext = mContext;
        this.mContentList = mContentList;
        mNamesList = mContext.getResources().getStringArray(R.array.person_design_tags);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.person_design_recycleview_adapter,parent,false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (position==0){
//            ((ItemViewHolder)holder).mPerson_design_author_icon.setVisibility(View.VISIBLE);
//            ((ItemViewHolder)holder).mPerson_design_author_icon.setSize(Constants.PHONE_WIDTH/8,Constants.PHONE_WIDTH/8);
//            ((ItemViewHolder)holder).mPerson_design_catogry_name.setVisibility(View.GONE);
//            ((ItemViewHolder)holder).mPerson_design_catogry_content.setText(mContentList.get(position));
//        }else {
//
//        }
        ((ItemViewHolder)holder).mPerson_design_catogry_name.setVisibility(View.VISIBLE);
        ((ItemViewHolder)holder).mPerson_design_catogry_name.setText(mNamesList[position]);
        ((ItemViewHolder)holder).mPerson_design_catogry_content.setText(mContentList.get(position));
        ((ItemViewHolder)holder).mPerson_design_next_icon.setSize(Constants.PHONE_WIDTH/20,Constants.PHONE_WIDTH/20);
//        ((ItemViewHolder)holder).mPerson_design_catogry_content.setTextSize(Constants.PHONE_WIDTH/55);
//        ((ItemViewHolder)holder).mPerson_design_catogry_name.setTextSize(Constants.PHONE_WIDTH/50);
    }

    @Override
    public int getItemCount() {
        return mContentList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        private final TextView mPerson_design_catogry_name;
        private final TextView mPerson_design_catogry_content;
        private final MyImageView mPerson_design_next_icon;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mPerson_design_catogry_name = (TextView) itemView.findViewById(R.id.person_design_catogry_name);
            mPerson_design_catogry_content = (TextView) itemView.findViewById(R.id.person_design_catogry_content);
            mPerson_design_next_icon = (MyImageView) itemView.findViewById(R.id.person_design_next_icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPersonDesignItemListener.setItemClick(v);
                }
            });
        }
    }
    public interface OnPersonDesignItemListener{
        void setItemClick(View view);
    }
    public void setOnPersonDesignItemListener(OnPersonDesignItemListener mOnPersonDesignItemListener){
        this.mOnPersonDesignItemListener = mOnPersonDesignItemListener;
    }

}
