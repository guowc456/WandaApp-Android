package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.graphics.Color;
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

public class NewPublishTopicsSelectAdapter extends RecyclerView.Adapter {

    private Context context;
    private int tagNames[] ;
    private OnTopicsLinkRecycleviewListener mOnTopicsLinkRecycleviewListener = null;
    private int selectFlag[] = new int[]{
            0,0,0,0,0,0,0,0,0,0,0,0,0,0
    };

    public NewPublishTopicsSelectAdapter(Context context, int tagNames[]) {
        this.context = context;
        this.tagNames = tagNames;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_new_publish_topics_recycle,null);
        TopicsLinkRecycleviewViewHolder holder = new TopicsLinkRecycleviewViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (selectFlag[position]==0){
            ((TopicsLinkRecycleviewViewHolder)holder).item_new_publish_topics_tv.setText("#"+ Constants.topics_name[tagNames[position]-1]+"#");
            ((TopicsLinkRecycleviewViewHolder)holder).item_new_publish_topics_tv.setSelected(false);
            ((TopicsLinkRecycleviewViewHolder)holder).item_new_publish_topics_tv.setTextColor(Color.BLACK);
        }else {
            ((TopicsLinkRecycleviewViewHolder)holder).item_new_publish_topics_tv.setText("#"+ Constants.topics_name[tagNames[position]-1]+"#");
            ((TopicsLinkRecycleviewViewHolder)holder).item_new_publish_topics_tv.setSelected(true);
            ((TopicsLinkRecycleviewViewHolder)holder).item_new_publish_topics_tv.setTextColor(Color.WHITE);
        }
        ((TopicsLinkRecycleviewViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectFlag[position]==0){
                    selectFlag[position] = 1;
                }else {
                    selectFlag[position] = 0;
                }

                mOnTopicsLinkRecycleviewListener.onItemClick(v,selectFlag);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tagNames.length;
    }

    class TopicsLinkRecycleviewViewHolder extends RecyclerView.ViewHolder {

        private final TextView item_new_publish_topics_tv;

        public TopicsLinkRecycleviewViewHolder(View itemView) {
            super(itemView);
            item_new_publish_topics_tv = (TextView) itemView.findViewById(R.id.item_new_publish_topics_tv);
        }
    }

    public interface OnTopicsLinkRecycleviewListener{
        void onItemClick(View view, int[] selectFlag);
    }

    public void setOnTopicsLinkRecycleviewListener(OnTopicsLinkRecycleviewListener mOnTopicsLinkRecycleviewListener){
        this.mOnTopicsLinkRecycleviewListener = mOnTopicsLinkRecycleviewListener;
    }
}
