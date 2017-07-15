package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.MostPopularInfo;
import com.wanta.mobile.wantaproject.utils.Constants;

import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class WardrobeFragmentAdapter extends RecyclerView.Adapter {

    private List<String> mList;
    private Context mContext;
    public WardrobeFragmentAdapter(Context mContext,List<String> mList) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_wardrobe_adapter,null);
        ItemWardrobeViewHolder viewHolder = new ItemWardrobeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ItemWardrobeViewHolder extends RecyclerView.ViewHolder{

        private final MyImageView mWardrobe_image;
        private final TextView mWardrobe_tv_name;
        private final MyImageView mWardrobe_more;
        private final TextView mWardrobe_tv_sum;

        public ItemWardrobeViewHolder(View itemView) {
            super(itemView);
            mWardrobe_image = (MyImageView) itemView.findViewById(R.id.wardrobe_image);
            mWardrobe_tv_name = (TextView) itemView.findViewById(R.id.wardrobe_tv_name);
            mWardrobe_more = (MyImageView) itemView.findViewById(R.id.wardrobe_more);
            mWardrobe_tv_sum = (TextView) itemView.findViewById(R.id.wardrobe_tv_sum);
        }
    }
}
