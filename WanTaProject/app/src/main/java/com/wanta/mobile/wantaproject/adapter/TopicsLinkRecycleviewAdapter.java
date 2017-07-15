package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.utils.Constants;

/**
 * Created by Administrator on 2017/4/27.
 */

public class TopicsLinkRecycleviewAdapter extends RecyclerView.Adapter {

    private Context context;
    private int tagNames[] ;
    private OnTopicsLinkRecycleviewListener mOnTopicsLinkRecycleviewListener = null;

    public TopicsLinkRecycleviewAdapter(Context context,int tagNames[]) {
        this.context = context;
        this.tagNames = tagNames;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_topics_link_recycle,null);
        TopicsLinkRecycleviewViewHolder holder = new TopicsLinkRecycleviewViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TopicsLinkRecycleviewViewHolder)holder).item_topics_link_tv.setText("#"+ Constants.topics_name[tagNames[position]-1]+"#");
    }

    @Override
    public int getItemCount() {
        return tagNames.length;
    }

    class TopicsLinkRecycleviewViewHolder extends RecyclerView.ViewHolder {

        private final TextView item_topics_link_tv;

        public TopicsLinkRecycleviewViewHolder(View itemView) {
            super(itemView);
            item_topics_link_tv = (TextView) itemView.findViewById(R.id.item_topics_link_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnTopicsLinkRecycleviewListener.onItemClick(v);
                }
            });
        }
    }

    public interface OnTopicsLinkRecycleviewListener{
        void onItemClick(View view);
    }

    public void setOnTopicsLinkRecycleviewListener(OnTopicsLinkRecycleviewListener mOnTopicsLinkRecycleviewListener){
        this.mOnTopicsLinkRecycleviewListener = mOnTopicsLinkRecycleviewListener;
    }
}
