package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.MostPopularInfo;
import com.wanta.mobile.wantaproject.uploadimage.DraweeUtils;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.ImageCompressUtil;
import com.wanta.mobile.wantaproject.utils.ImageDealUtils;
import com.wanta.mobile.wantaproject.utils.ImageUtils;
import com.wanta.mobile.wantaproject.utils.JsonParseUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.SimpleDraweeControlUtils;

import java.io.File;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class MostPopularFragmentAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<MostPopularInfo> mList;
    private onMostPopularItemImageListener mOnMostPopularItemImageListener = null;
    private onMostPopularItemTitleListener mOnMostPopularItemTitleListener = null ;
    private onMostPopularItemContentListener mOnMostPopularItemContentListener = null;
    private onMostPopularItemIconListener mOnMostPopularItemIconListener = null;
    private onMostPopularItemNameListener mOnMostPopularItemNameListener = null ;
    private onMostPopularItemHeartListener mOnMostPopularItemHeartListener = null;

    public MostPopularFragmentAdapter(Context mContext,List<MostPopularInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }
    public void setShowNumber(List<MostPopularInfo> mList){
        this.mList = mList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mostpopular_adapter,parent,false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final MostPopularInfo mostPopularInfo = mList.get(position);
        if (!"null".equals(mostPopularInfo.getTitle())){
            ((ItemViewHolder)holder).mItem_mostpopular_title.setText(mostPopularInfo.getTitle());
        }else {
            ((ItemViewHolder)holder).mItem_mostpopular_title.setText("");
        }
        if (!"null".equals(mostPopularInfo.getContent())){
            ((ItemViewHolder)holder).mItem_mostpopular_content.setText(mostPopularInfo.getContent());
        }else {
            ((ItemViewHolder)holder).mItem_mostpopular_content.setText("");
        }
        if (!"null".equals(mostPopularInfo.getUsername())){
            ((ItemViewHolder)holder).mItem_mostpopular_tv_name.setText(mostPopularInfo.getUsername());
        }else {
            ((ItemViewHolder)holder).mItem_mostpopular_tv_name.setText("游客");
        }
        ((ItemViewHolder)holder).mItem_mostpopular_heartconnt.setText(mostPopularInfo.getLikenum()+"");
        if (!"null".equals(mostPopularInfo.getFavourstate())){
            ((ItemViewHolder)holder).mItem_mostpopular_heart.setImageBitmap(ImageDealUtils.readBitMap(mContext,R.mipmap.heart_press));
        }else {
            ((ItemViewHolder)holder).mItem_mostpopular_heart.setImageBitmap(ImageDealUtils.readBitMap(mContext,R.mipmap.heart_no_press));
        }
        //设置显示的宽度
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH/4,Constants.PHONE_WIDTH/10);
        ((ItemViewHolder)holder).author_layout.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH/5,Constants.PHONE_WIDTH/10);
        ((ItemViewHolder)holder).heart_layout.setLayoutParams(layoutParams1);

        ((ItemViewHolder)holder).item_mostpopular_body_msg_tv.setText(mostPopularInfo.getHeight()+"cm/"+mostPopularInfo.getWeight()+"kg   "+mostPopularInfo.getBust()+mostPopularInfo.getBra());

        if (!TextUtils.isEmpty(mostPopularInfo.getImages())){
            int[] firstPicSize = JsonParseUtils.getFirstPicSize(mostPopularInfo.getImages());
            final int width = firstPicSize[0];
            final int height = firstPicSize[1];
            ((ItemViewHolder)holder).mItem_mostpopular_img.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ((ItemViewHolder)holder).mItem_mostpopular_img.setAdjustViewBounds(true);
            ((ItemViewHolder)holder).mItem_mostpopular_img.setMaxWidth(Constants.PHONE_WIDTH/2);
            ((ItemViewHolder)holder).mItem_mostpopular_img.setMaxHeight((int) (height * (((Constants.PHONE_WIDTH / 2)*1.00) / width)));
            ((ItemViewHolder)holder).mItem_mostpopular_img.setAspectRatio(width/height);
            ((ItemViewHolder)holder).mItem_mostpopular_img.setWidth(Constants.PHONE_WIDTH/2);
            ((ItemViewHolder)holder).mItem_mostpopular_img.setHeight((int) (height*(((Constants.PHONE_WIDTH/2)*1.00)/width)));
            LogUtils.showVerbose("MostPopularFragmentAdapter","路径="+Constants.FIRST_PAGE_IMAGE_URL +"thumbicon"+ JsonParseUtils.getFirstPicUrl(mostPopularInfo.getImages()));
            DraweeUtils.resizepic(Uri.parse(Constants.FIRST_PAGE_IMAGE_URL +"thumbicon"+ JsonParseUtils.getFirstPicUrl(mostPopularInfo.getImages())),((ItemViewHolder)holder).mItem_mostpopular_img);
        }

        ((ItemViewHolder)holder).mItem_mostpopular_icon.setWidth(Constants.PHONE_WIDTH/12);
        ((ItemViewHolder)holder).mItem_mostpopular_icon.setHeight(Constants.PHONE_WIDTH/12);
        SimpleDraweeControlUtils.getNetRingSimpleDraweeControl(mContext,((ItemViewHolder)holder).mItem_mostpopular_icon,Constants.FIRST_PAGE_IMAGE_URL +"/"+mostPopularInfo.getUseravatar());
//        ((ItemViewHolder)holder).mItem_mostpopular_heart.setSize(Constants.PHONE_WIDTH/12,Constants.PHONE_WIDTH/12);
        ((ItemViewHolder)holder).mItem_mostpopular_heart.setWidth(Constants.PHONE_WIDTH/12);
        ((ItemViewHolder)holder).mItem_mostpopular_heart.setHeight(Constants.PHONE_WIDTH/12);

        //添加事件
        ((ItemViewHolder)holder).mItem_mostpopular_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnMostPopularItemImageListener.onItemImageClick(((ItemViewHolder)holder).itemView,mostPopularInfo);
            }
        });
        ((ItemViewHolder)holder).mItem_mostpopular_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnMostPopularItemTitleListener.onItemTitlsClick(((ItemViewHolder)holder).itemView,mostPopularInfo);
            }
        });
        ((ItemViewHolder)holder).mItem_mostpopular_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnMostPopularItemContentListener.onItemContentClick(((ItemViewHolder)holder).itemView,mostPopularInfo);
            }
        });
        ((ItemViewHolder)holder).mItem_mostpopular_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnMostPopularItemIconListener.onItemIconClick(((ItemViewHolder)holder).itemView,mostPopularInfo);
            }
        });
        ((ItemViewHolder)holder).mItem_mostpopular_tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnMostPopularItemNameListener.onItemNameClick(((ItemViewHolder)holder).itemView,mostPopularInfo);
            }
        });
        ((ItemViewHolder)holder).mItem_mostpopular_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnMostPopularItemHeartListener.onItemHeartClick(((ItemViewHolder)holder).itemView,((ItemViewHolder)holder).mItem_mostpopular_heart,mostPopularInfo,((ItemViewHolder)holder).mItem_mostpopular_heartconnt);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        private final CustomSimpleDraweeView mItem_mostpopular_img;
        private final TextView mItem_mostpopular_title;
        private final TextView mItem_mostpopular_content;
        private final CustomSimpleDraweeView mItem_mostpopular_icon;
        private final TextView mItem_mostpopular_tv_name;
        private final CustomSimpleDraweeView mItem_mostpopular_heart;
        private final TextView mItem_mostpopular_heartconnt;
        private final TextView item_mostpopular_body_msg_tv;
        private final LinearLayout author_layout;
        private final LinearLayout heart_layout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mItem_mostpopular_img = (CustomSimpleDraweeView) itemView.findViewById(R.id.item_mostpopular_img);
            mItem_mostpopular_title = (TextView) itemView.findViewById(R.id.item_mostpopular_title);
            mItem_mostpopular_content = (TextView) itemView.findViewById(R.id.item_mostpopular_content);
            mItem_mostpopular_icon = (CustomSimpleDraweeView) itemView.findViewById(R.id.item_mostpopular_icon);
            mItem_mostpopular_tv_name = (TextView) itemView.findViewById(R.id.item_mostpopular_tv_name);
            mItem_mostpopular_heart = (CustomSimpleDraweeView) itemView.findViewById(R.id.item_mostpopular_heart);
            mItem_mostpopular_heartconnt = (TextView) itemView.findViewById(R.id.item_mostpopular_heartconnt);
            item_mostpopular_body_msg_tv = (TextView) itemView.findViewById(R.id.item_mostpopular_body_msg_tv);
            author_layout = (LinearLayout) itemView.findViewById(R.id.author_layout);
            heart_layout = (LinearLayout) itemView.findViewById(R.id.heart_layout);
        }
    }
    public interface onMostPopularItemImageListener{
        void onItemImageClick(View view, MostPopularInfo mostPopularInfo);
    }
    public void setOnMostPopularItemImageListener(onMostPopularItemImageListener mOnMostPopularItemImageListener){
        this.mOnMostPopularItemImageListener = mOnMostPopularItemImageListener;
    }

    public interface onMostPopularItemTitleListener{
        void onItemTitlsClick(View view, MostPopularInfo mostPopularInfo);
    }
    public void setOnMostPopularItemTitleListener(onMostPopularItemTitleListener mOnMostPopularItemTitleListener){
        this.mOnMostPopularItemTitleListener = mOnMostPopularItemTitleListener;
    }

    public interface onMostPopularItemContentListener{
        void onItemContentClick(View view, MostPopularInfo mostPopularInfo);
    }
    public void setOnMostPopularItemContentListener(onMostPopularItemContentListener mOnMostPopularItemContentListener){
        this.mOnMostPopularItemContentListener = mOnMostPopularItemContentListener;
    }

    public interface onMostPopularItemIconListener{
        void onItemIconClick(View view, MostPopularInfo mostPopularInfo);
    }
    public void setOnMostPopularItemIconListener(onMostPopularItemIconListener mOnMostPopularItemIconListener){
        this.mOnMostPopularItemIconListener = mOnMostPopularItemIconListener;
    }

    public interface onMostPopularItemNameListener{
        void onItemNameClick(View view, MostPopularInfo mostPopularInfo);
    }
    public void setOnMostPopularItemNameListener(onMostPopularItemNameListener mOnMostPopularItemNameListener){
        this.mOnMostPopularItemNameListener = mOnMostPopularItemNameListener;
    }

    public interface onMostPopularItemHeartListener{
        void onItemHeartClick(View view, CustomSimpleDraweeView item_mostpopular_heart, MostPopularInfo mostPopularInfo, TextView textView);
    }
    public void setOnMostPopularItemHeartListener(onMostPopularItemHeartListener mOnMostPopularItemHeartListener){
        this.mOnMostPopularItemHeartListener = mOnMostPopularItemHeartListener;
    }


}
