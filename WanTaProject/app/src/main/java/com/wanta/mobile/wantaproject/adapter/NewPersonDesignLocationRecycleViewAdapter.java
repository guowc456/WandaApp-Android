package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.mordel.DomesticDbHelper;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class NewPersonDesignLocationRecycleViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<String> mContentList;
    private OnPersonDesignLocationItemListener mOnPersonDesignLocationItemListener = null;
    private String[] mStrings3 = {
            "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "w", "x", "y", "z"
    };
    private String[] mStrings4 = {
            "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "W", "X", "Y", "Z"
    };
    private final DomesticDbHelper mHelper;
    private int num = 0;
    private int current_position = 0;
    private List<Integer> letterData = new ArrayList<>();
    public static final int TYPE_TYPE1 = 1;
    public static final int TYPE_TYPE3 = 3;


    public NewPersonDesignLocationRecycleViewAdapter(Context mContext, List<String> mContentList ,List<Integer> letterData) {
        this.mContext = mContext;
        this.mContentList = mContentList;
        this.letterData = letterData;
        mHelper = new DomesticDbHelper(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_TYPE1:
                //显示一行
                return new ItemLetterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_person_design_location_adapter, parent, false));
            case TYPE_TYPE3:
                return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.person_design_location_recycleview_adapter, parent, false));
            default:
                return null;
        }
//        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH/3,Constants.PHONE_HEIGHT/18);
            ((ItemViewHolder)holder).mItem_location_content_layout.setLayoutParams(params);
            ((ItemViewHolder)holder).mItem_location_content.setText(mContentList.get(position));
        }else if (holder instanceof ItemLetterViewHolder){
            ((ItemLetterViewHolder)holder).item_location_letter_label.setText(mContentList.get(position));
        }
    }

    /**
     * spansize 描述的是每一个item的大小
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemViewType = getItemViewType(position);
                    switch (itemViewType){
                        case TYPE_TYPE1:
                            return 3;
                        case TYPE_TYPE3:
                            return 1;
                        default:
                            return 1;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(letterData.contains(position)||position==0){
            return TYPE_TYPE1;
        }else {
            return TYPE_TYPE3;
        }
    }

    @Override
    public int getItemCount() {
        return mContentList.size();
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
    class ItemLetterViewHolder extends RecyclerView.ViewHolder {

        private final TextView item_location_letter_label;

        public ItemLetterViewHolder(View itemView) {
            super(itemView);
            item_location_letter_label = (TextView) itemView.findViewById(R.id.item_location_letter_label);
        }
    }
    public interface OnPersonDesignLocationItemListener{
        void setItemClick(View view);
    }
    public void setOnPersonDesignLocationItemListener(OnPersonDesignLocationItemListener mOnPersonDesignLocationItemListener){
        this.mOnPersonDesignLocationItemListener = mOnPersonDesignLocationItemListener;
    }


}
