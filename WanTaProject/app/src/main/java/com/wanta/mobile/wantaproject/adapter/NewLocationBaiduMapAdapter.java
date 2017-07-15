package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.wanta.mobile.wantaproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class NewLocationBaiduMapAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<PoiInfo> mContentList;
    private List<String> cityName;
    private OnNewLocationBaiduItemListener mOnNewLocationBaiduItemListener = null;
    private OnNewLocationCityItemListener mOnNewLocationCityItemListener = null;
    private int catogry = 1;

    public void setPoiInfoMessage(Context mContext, List<PoiInfo> mContentList,int catogry) {
        this.mContext = mContext;
        this.mContentList = mContentList;
        this.catogry = catogry;
    }
    public void setCityNameMessage(Context mContext, List<String> cityName,int catogry) {
        this.mContext = mContext;
        this.cityName = cityName;
        this.catogry = catogry;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.new_location_baidu_map_item_layout,parent,false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (catogry==1){
            ((ItemViewHolder)holder).new_location_baidu_map_item_name.setText(mContentList.get(position).name);
            ((ItemViewHolder)holder).new_location_baidu_map_item_address.setVisibility(View.VISIBLE);
            ((ItemViewHolder)holder).new_location_baidu_map_item_address.setText(mContentList.get(position).address);
            ((ItemViewHolder)holder).new_location_baidu_map_item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnNewLocationBaiduItemListener.setItemClick(mContentList.get(position));
                }
            });
        }else {
            ((ItemViewHolder)holder).new_location_baidu_map_item_name.setText(cityName.get(position));
            ((ItemViewHolder)holder).new_location_baidu_map_item_address.setVisibility(View.GONE);
            ((ItemViewHolder)holder).new_location_baidu_map_item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnNewLocationCityItemListener.setItemClick(cityName.get(position));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (catogry==1){
            //当前是地址信息
            return mContentList.size();
        }else if (catogry==2){
            //当前是城市信息
            return cityName.size();
        }
       return 0;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        private final TextView new_location_baidu_map_item_name;
        private final TextView new_location_baidu_map_item_address;
        private final LinearLayout new_location_baidu_map_item_layout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            new_location_baidu_map_item_name = (TextView) itemView.findViewById(R.id.new_location_baidu_map_item_name);
            new_location_baidu_map_item_address = (TextView) itemView.findViewById(R.id.new_location_baidu_map_item_address);
            new_location_baidu_map_item_layout = (LinearLayout) itemView.findViewById(R.id.new_location_baidu_map_item_layout);

        }
    }
    public interface OnNewLocationBaiduItemListener{
        void setItemClick(PoiInfo poiInfo);
    }
    public void setOnNewLocationBaiduItemListener(OnNewLocationBaiduItemListener mOnNewLocationBaiduItemListener){
        this.mOnNewLocationBaiduItemListener = mOnNewLocationBaiduItemListener;
    }

    public interface OnNewLocationCityItemListener{
        void setItemClick(String cityName);
    }
    public void setOnNewLocationCityItemListener(OnNewLocationCityItemListener mOnNewLocationCityItemListener){
        this.mOnNewLocationCityItemListener = mOnNewLocationCityItemListener;
    }

}
