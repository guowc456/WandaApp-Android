package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class LocationBaiduMapAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<String> mContentList;
    private String  mNamesList[];
    private OnLocationBaiduItemListener mOnLocationBaiduItemListener = null;
    private ArrayList<String> mUnfilteredData;
    private ArrayFilter mFilter;

    public LocationBaiduMapAdapter(Context mContext, List<String> mContentList) {
        this.mContext = mContext;
        this.mContentList = mContentList;
        mNamesList = mContext.getResources().getStringArray(R.array.person_design_tags);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.location_baidu_map_item_layout,parent,false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder)holder).location_baidu_map_item_txt.setText(mContentList.get(position));
    }

    @Override
    public int getItemCount() {
        return mContentList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        private final TextView location_baidu_map_item_txt;

        public ItemViewHolder(View itemView) {
            super(itemView);
            location_baidu_map_item_txt = (TextView) itemView.findViewById(R.id.location_baidu_map_item_txt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnLocationBaiduItemListener.setItemClick(v);
                }
            });
        }
    }
    public interface OnLocationBaiduItemListener{
        void setItemClick(View view);
    }
    public void setOnLocationBaiduItemListener(OnLocationBaiduItemListener mOnLocationBaiduItemListener){
        this.mOnLocationBaiduItemListener = mOnLocationBaiduItemListener;
    }

    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    public class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mUnfilteredData == null) {
                mUnfilteredData = new ArrayList<String>(mContentList);
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<String> list = mUnfilteredData;
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();
                ArrayList<String> unfilteredValues = mUnfilteredData;
                int count = unfilteredValues.size();

                ArrayList<String> newValues = new ArrayList<String>(count);

                for (int i = 0; i < count; i++) {
                    String pi = unfilteredValues.get(i);
                    if (pi != null) {

                        if(pi!=null && pi.startsWith(prefixString)
                                ||pi!=null && pi.startsWith(prefixString)){
                            newValues.add(pi);
                        }
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            //noinspection unchecked
            mContentList = (List<String>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            }
        }

    }
}
