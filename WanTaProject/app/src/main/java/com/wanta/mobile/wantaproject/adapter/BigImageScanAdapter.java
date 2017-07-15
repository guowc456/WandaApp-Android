package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.uploadimage.BigImageScanActivity;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.ImageUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by WangYongqiang on 2016/11/16.
 */
public class BigImageScanAdapter extends BaseAdapter {

    private List<String> mList;
    private Context mContext;
//    private GetCurrentItemPositionListener mGetCurrentItemPosition = null;
    private CustomSimpleDraweeView mBig_image_scan_gallery_simpledraweeview;
//    private int currentPosition = 0;//主要是为了解决页面还没有跳转过去，但是position已经加1的情况

    public BigImageScanAdapter(Context mContext,List<String> mList) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
//        LogUtils.showVerbose("BigImageScanAdapter","11111");
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
//        LogUtils.showVerbose("BigImageScanAdapter","2222");
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
//        LogUtils.showVerbose("BigImageScanAdapter","当前的id是："+position);
//        LogUtils.showVerbose("BigImageScanAdapter","33333");
//        currentPosition=position;
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        GalleryViewHolder holder;
//        View view ;
//        if (convertView==null){
//            view = LayoutInflater.from(mContext).inflate(R.layout.big_image_scan_adapter,null);
//            holder = new GalleryViewHolder();
//            holder.big_image_scan_gallery_simpledraweeview = (CustomSimpleDraweeView) view.findViewById(R.id.big_image_scan_gallery_simpledraweeview);
//            view.setTag(holder);
//        }else {
//            view = convertView;
//            holder = (GalleryViewHolder) view.getTag();
//        }
//        int[] imageWidthHeight = ImageUtils.getImageWidthHeight(mList.get(position));
//        int width = imageWidthHeight[0];
//        int height = imageWidthHeight[1];
//        LogUtils.showVerbose("BigImageScanAdapter","width="+width+" height="+height);
//        holder.big_image_scan_gallery_simpledraweeview.setImageURI(Uri.fromFile(new File(mList.get(position))));
//        holder.big_image_scan_gallery_simpledraweeview.setAspectRatio(width/height);
//        if (width<20){
//            holder.big_image_scan_gallery_simpledraweeview.setWidth(Constants.PHONE_WIDTH);
//            holder.big_image_scan_gallery_simpledraweeview.setHeight(height*(Constants.PHONE_WIDTH/width));
//        }else if (width>=20&&width<200){
//            holder.big_image_scan_gallery_simpledraweeview.setWidth(Constants.PHONE_WIDTH);
//            holder.big_image_scan_gallery_simpledraweeview.setHeight(height*(Constants.PHONE_WIDTH/width));
//        }else if (width>730){
//            holder.big_image_scan_gallery_simpledraweeview.setWidth(Constants.PHONE_WIDTH);
//            holder.big_image_scan_gallery_simpledraweeview.setHeight(height*(Constants.PHONE_WIDTH/width)+Constants.PHONE_WIDTH/5);
//        }else {
//            if (width>height){
//                holder.big_image_scan_gallery_simpledraweeview.setWidth(Constants.PHONE_WIDTH);
//                holder.big_image_scan_gallery_simpledraweeview.setHeight(height*(Constants.PHONE_WIDTH/width));
//            }else {
//                holder.big_image_scan_gallery_simpledraweeview.setWidth((int) (width*(Constants.PHONE_DENSITY+0.4)));
//                holder.big_image_scan_gallery_simpledraweeview.setHeight((int) (height*(Constants.PHONE_DENSITY+0.4)));
//            }
//        }

//        GalleryViewHolder holder;
//        LogUtils.showVerbose("BigImageScanAdapter","44444");
//        LogUtils.showVerbose("BigImageScanAdapter","Postion=："+position);
//        View view = null;
        if (convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.big_image_scan_adapter,null);
            mBig_image_scan_gallery_simpledraweeview = (CustomSimpleDraweeView) convertView.findViewById(R.id.big_image_scan_gallery_simpledraweeview);
        }else {
            mBig_image_scan_gallery_simpledraweeview = (CustomSimpleDraweeView) convertView.findViewById(R.id.big_image_scan_gallery_simpledraweeview);
        }


        int[] imageWidthHeight = ImageUtils.getImageWidthHeight(mList.get(position));
        int width = imageWidthHeight[0];
        int height = imageWidthHeight[1];
//        LogUtils.showVerbose("BigImageScanAdapter","width="+width+" height="+height);
        mBig_image_scan_gallery_simpledraweeview.setImageURI(Uri.fromFile(new File(mList.get(position))));
        mBig_image_scan_gallery_simpledraweeview.setAspectRatio(width/height);
        if (width<20){
            mBig_image_scan_gallery_simpledraweeview.setWidth(Constants.PHONE_WIDTH);
            mBig_image_scan_gallery_simpledraweeview.setHeight((int) (height*(Constants.PHONE_WIDTH*1.00/width)));
        }else if (width>=20&&width<200){
            mBig_image_scan_gallery_simpledraweeview.setWidth(Constants.PHONE_WIDTH);
            mBig_image_scan_gallery_simpledraweeview.setHeight((int) (height*(Constants.PHONE_WIDTH*1.00/width)));
        }else if (width>730){
            mBig_image_scan_gallery_simpledraweeview.setWidth(Constants.PHONE_WIDTH);
            mBig_image_scan_gallery_simpledraweeview.setHeight((int) (height*(Constants.PHONE_WIDTH*1.00/width)+Constants.PHONE_WIDTH/5));
        }else {
            if (width>height){
                mBig_image_scan_gallery_simpledraweeview.setWidth(Constants.PHONE_WIDTH);
                mBig_image_scan_gallery_simpledraweeview.setHeight((int) (height*(Constants.PHONE_WIDTH*1.00/width)));
            }else {
                mBig_image_scan_gallery_simpledraweeview.setWidth((int) (width*(Constants.PHONE_DENSITY+0.4)));
                mBig_image_scan_gallery_simpledraweeview.setHeight((int) (height*(Constants.PHONE_DENSITY+0.4)));
            }
        }
//        mGetCurrentItemPosition.setCurrentItemPosition(position);
//        mBig_image_scan_gallery_simpledraweeview.setWidth((int) (width*(Constants.PHONE_DENSITY+0.4)));
//        mBig_image_scan_gallery_simpledraweeview.setHeight((int) (height*(Constants.PHONE_DENSITY+0.4)));
        return convertView;
    }

//    public class GalleryViewHolder{
//        CustomSimpleDraweeView big_image_scan_gallery_simpledraweeview ;
//    }
//
//    public interface GetCurrentItemPositionListener{
//        void setCurrentItemPosition(int position);
//    }
//    public void setGetCurrentItemPositionListener(GetCurrentItemPositionListener mGetCurrentItemPosition){
//        this.mGetCurrentItemPosition = mGetCurrentItemPosition;
//    }
//    public int getCurrentPosition(){
//        return currentPosition;
//    }
}
