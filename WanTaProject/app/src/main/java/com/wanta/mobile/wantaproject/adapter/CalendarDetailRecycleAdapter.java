package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.utils.Constants;

import java.util.List;

/**
 * Created by WangYongqiang on 2017/1/5.
 */
public class CalendarDetailRecycleAdapter extends RecyclerView.Adapter {

    private int[] mList ;
    private Context mContext;

    public CalendarDetailRecycleAdapter(Context mContext,int[] mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_calendar_detail,null);
        CalendarDetailHolder holder = new CalendarDetailHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //设置条目的宽和高
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH/7,Constants.PHONE_HEIGHT/8);
        ((CalendarDetailHolder)holder).mItem_adapter_calendar_detail_content.setLayoutParams(layoutParams);
        ((CalendarDetailHolder)holder).mItem_adapter_calendar_detail_content.setText(mList[position]+"");
    }

    @Override
    public int getItemCount() {
        return mList.length;
    }

    public class CalendarDetailHolder extends RecyclerView.ViewHolder {

        private final TextView mItem_adapter_calendar_detail_content;

        public CalendarDetailHolder(View itemView) {
            super(itemView);
            mItem_adapter_calendar_detail_content = (TextView) itemView.findViewById(R.id.item_adapter_calendar_detail_content);
        }
    }
}
