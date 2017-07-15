package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.ImageUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class FindFragmentRecycviewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private int picIds[] = new int[]{
//            R.mipmap.find_pic1,R.mipmap.find_pic2,R.mipmap.find_pic3,
//            R.mipmap.find_pic5,R.mipmap.find_pic6
            R.mipmap.find_pic1,R.mipmap.find_pic2,R.mipmap.find_pic3,
            R.mipmap.find_pic5,R.mipmap.find_pic6

    };
    private FindItemClickListener mFindItemClickListener = null;

    private String lables[] = new String[]{
            "",
            "澳洲13日,碧海蓝天,最想移民的国家没有之一",
            "胸大的妹子不能穿衬衣?那是因为你没有GET技能!",
            "我是跑着我自豪!,附带运动Bra挑选经验",
            "最快的大胸妹手臂塑性,腰腹减脂真人示范",
    };

    public FindFragmentRecycviewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_find_recycleview_adapter,parent,false);
        ItemFindViewHolder viewHolder = new ItemFindViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), picIds[position]);

        if (position==0){
            ((ItemFindViewHolder)holder).mItem_find_text_content.setVisibility(View.GONE);
        }else {
            ((ItemFindViewHolder)holder).mItem_find_text_content.setVisibility(View.VISIBLE);
        }
        ((ItemFindViewHolder)holder).mItem_find_recycleview_simpledrawee.setWidth(Constants.PHONE_WIDTH);
        ((ItemFindViewHolder)holder).mItem_find_recycleview_simpledrawee.setHeight((int) (((Constants.PHONE_WIDTH*1.00)/bitmap.getWidth())*bitmap.getHeight()));
        ((ItemFindViewHolder)holder).mItem_find_text_content.setText(lables[position]);
        ((ItemFindViewHolder)holder).mItem_find_text_content.setGravity(Gravity.CENTER);
        ((ItemFindViewHolder)holder).mItem_find_recycleview_simpledrawee.setImageResource(picIds[position]);
    }

    @Override
    public int getItemCount() {
        return picIds.length;
    }

    class ItemFindViewHolder extends RecyclerView.ViewHolder{

        private final CustomSimpleDraweeView mItem_find_recycleview_simpledrawee;
        private final TextView mItem_find_text_content;

        public ItemFindViewHolder(View itemView) {
            super(itemView);
            mItem_find_recycleview_simpledrawee = (CustomSimpleDraweeView) itemView.findViewById(R.id.item_find_recycleview_simpledrawee);
            mItem_find_text_content = (TextView) itemView.findViewById(R.id.item_find_text_content);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFindItemClickListener.onItemClick(v);
                }
            });
        }
    }

    public interface FindItemClickListener{
        void onItemClick(View view);
    }
    public void setFindItemClickListener(FindItemClickListener mFindItemClickListener){
        this.mFindItemClickListener = mFindItemClickListener;
    }

}
