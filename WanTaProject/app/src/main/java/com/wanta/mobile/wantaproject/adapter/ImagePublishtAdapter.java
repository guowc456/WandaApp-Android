package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.ImageUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class ImagePublishtAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private LruCache<String,Bitmap> urlList ;
    private OnImagePublishItemListener mOnImagePublishItemListener = null;
    public ImagePublishtAdapter(Context mContext, LruCache<String,Bitmap> urlList) {
        this.mContext = mContext;
        this.urlList = urlList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image_publish_adapter,parent,false);
        ItemImagePublishViewHolder viewHolder = new ItemImagePublishViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position==urlList.size()){
            ((ItemImagePublishViewHolder)holder).item_publish_image.setImageResource(R.mipmap.more_iamge);
        }else {
            try {
                ((ItemImagePublishViewHolder)holder).item_publish_image.setImageBitmap(urlList.get(Constants.upload_images_url.get(position)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ((ItemImagePublishViewHolder)holder).item_publish_image.setSize(Constants.PHONE_WIDTH/4,Constants.PHONE_WIDTH/4);
        ((ItemImagePublishViewHolder)holder).item_publish_image.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public int getItemCount() {
        return urlList.size()+1;
    }

    class ItemImagePublishViewHolder extends RecyclerView.ViewHolder{

        private final MyImageView item_publish_image;

        public ItemImagePublishViewHolder(View itemView) {
            super(itemView);
            item_publish_image = (MyImageView) itemView.findViewById(R.id.item_publish_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnImagePublishItemListener.onImagePublishItemClick(v);
                }
            });
        }
    }
    public interface OnImagePublishItemListener{
        void onImagePublishItemClick(View view);
    }
    public void setOnImagePublishItemListener(OnImagePublishItemListener mOnImagePublishItemListener){
        this.mOnImagePublishItemListener = mOnImagePublishItemListener;
    }
}
