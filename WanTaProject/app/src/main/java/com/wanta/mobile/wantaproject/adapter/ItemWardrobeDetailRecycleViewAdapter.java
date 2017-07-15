package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.ReplyCommentInfo;
import com.wanta.mobile.wantaproject.uploadimage.DraweeUtils;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;

import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class ItemWardrobeDetailRecycleViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<String> mList;
    private OnItemClickListener mOnItemClickListener = null ;

    public ItemWardrobeDetailRecycleViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setShowNumber(List<String> mList1){
        this.mList = mList1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_wardrobe_detail_recycleview_adapter, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ItemViewHolder) holder).mItem_wardrobe_detail_image.setWidth(Constants.PHONE_WIDTH/3);
        ((ItemViewHolder) holder).mItem_wardrobe_detail_image.setHeight(Constants.PHONE_HEIGHT/3);
//        ((ItemViewHolder) holder).mItem_wardrobe_detail_image.setImageURI(Uri.parse("http://1zou.me/images/" + mList.get(position)));
        DraweeUtils.loadWardrobePics(Uri.parse("http://1zou.me/images/" + mList.get(position)),((ItemViewHolder) holder).mItem_wardrobe_detail_image);
        ((ItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.itemClick(mList.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final CustomSimpleDraweeView mItem_wardrobe_detail_image;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mItem_wardrobe_detail_image = (CustomSimpleDraweeView) itemView.findViewById(R.id.item_wardrobe_detail_image);
        }
    }

    public interface OnItemClickListener{
        void itemClick(String string, int position);
    }
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
