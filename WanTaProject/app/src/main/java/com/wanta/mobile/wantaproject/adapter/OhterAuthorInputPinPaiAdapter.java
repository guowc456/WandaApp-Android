package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/12.
 */

public class OhterAuthorInputPinPaiAdapter extends RecyclerView.Adapter {
    private List<String> list;
    private Context context;
    private OnOhterAuthorInputPinPaiListener mOnOhterAuthorInputPinPaiListener = null;
    private ArrayList<String> mUnfilteredData;
    private ArrayFilter mFilter;
    private int pos = 10000;

    public OhterAuthorInputPinPaiAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
    }

    //设置选中的位置
    public void setSelectPositioni(int pos){
        this.pos = pos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_other_auhtor_input_pinpai, null);
        OtherAuthorPinPaiViewHolder holder = new OtherAuthorPinPaiViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((OtherAuthorPinPaiViewHolder) holder).item_other_author_pinpai_content.setText(list.get(position));
        if (position==pos){
            ((OtherAuthorPinPaiViewHolder) holder).item_other_author_pinpai_content.setTextColor(context.getResources().getColor(R.color.head_bg_color));
        }else{
            ((OtherAuthorPinPaiViewHolder) holder).item_other_author_pinpai_content.setTextColor(context.getResources().getColor(R.color.pinpai_font_color));
        }
        ((OtherAuthorPinPaiViewHolder) holder).item_other_author_pinpai_content_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnOhterAuthorInputPinPaiListener.onItemClick(position,list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class OtherAuthorPinPaiViewHolder extends RecyclerView.ViewHolder {

        private final TextView item_other_author_pinpai_content;
        private final LinearLayout item_other_author_pinpai_content_layout;

        public OtherAuthorPinPaiViewHolder(View itemView) {
            super(itemView);
            item_other_author_pinpai_content = (TextView) itemView.findViewById(R.id.item_other_author_pinpai_content);
            item_other_author_pinpai_content_layout = (LinearLayout) itemView.findViewById(R.id.item_other_author_pinpai_content_layout);
        }
    }

    public interface OnOhterAuthorInputPinPaiListener {
        void onItemClick(int pos, String text);
    }

    public void setOnOhterAuthorInputPinPaiListener(OnOhterAuthorInputPinPaiListener mOnOhterAuthorInputPinPaiListener) {
        this.mOnOhterAuthorInputPinPaiListener = mOnOhterAuthorInputPinPaiListener;
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
                mUnfilteredData = new ArrayList<String>(list);
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

                        if (pi != null && pi.startsWith(prefixString)
                                || pi != null && pi.startsWith(prefixString)) {
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
            list = (List<String>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            }
        }

    }
}
