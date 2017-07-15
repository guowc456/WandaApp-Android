package com.wanta.mobile.wantaproject.phonepics;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.domain.ImageItem;
import com.wanta.mobile.wantaproject.utils.Constants;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */

public class PhonePicsSelectAdapter extends RecyclerView.Adapter{

    private Context mContext;
    private List<ImageItem> mList ;
    private int picWidth ,picHeight;
    private int iconWidth ,iconHeight;
    private OnItemPhonePicsSelectIconLayout mOnItemPhonePicsSelectIconLayout = null;

    public PhonePicsSelectAdapter(Context mContext, List<ImageItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }
    //设置图片的大小
    public void setPhonePicSize(int picWidth,int picHeight){
        this.picWidth = picWidth;
        this.picHeight = picHeight;
    }
    //设置图片上选择框的大小
    public void setPhoneIconSize(int iconWidth,int iconHeight){
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_phone_pics_select,null);
        PhonePicsViewHolder viewHolder = new PhonePicsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((PhonePicsViewHolder)holder).item_phone_pics_select.setWidth(picWidth);
        ((PhonePicsViewHolder)holder).item_phone_pics_select.setHeight(picHeight);
        showThumb(Uri.fromFile(new File(mList.get(position).getPath())),((PhonePicsViewHolder)holder).item_phone_pics_select);
        ((PhonePicsViewHolder)holder).item_phone_pics_select_icon.setWidth(iconWidth);
        ((PhonePicsViewHolder)holder).item_phone_pics_select_icon.setHeight(iconHeight);
        if (Constants.SELECT_PIC_URL.contains(mList.get(position).getPath())){
            ((PhonePicsViewHolder)holder).item_phone_pics_select_icon.setImageResource(R.mipmap.image_selected);
        }else {
            ((PhonePicsViewHolder)holder).item_phone_pics_select_icon.setImageResource(R.mipmap.image_unselected);
        }
//        ((PhonePicsViewHolder)holder).item_phone_pics_select_icon.setImageResource(R.mipmap.image_unselected);
        ((PhonePicsViewHolder)holder).item_phone_pics_select_icon_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemPhonePicsSelectIconLayout.onItemClick(position,((PhonePicsViewHolder)holder).item_phone_pics_select_icon,mList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class PhonePicsViewHolder extends RecyclerView.ViewHolder {

        private final CustomSimpleDraweeView item_phone_pics_select;
        private final CustomSimpleDraweeView item_phone_pics_select_icon;
        private final LinearLayout item_phone_pics_select_icon_layout;

        public PhonePicsViewHolder(View itemView) {
            super(itemView);
            item_phone_pics_select = (CustomSimpleDraweeView) itemView.findViewById(R.id.item_phone_pics_select);
            item_phone_pics_select_icon = (CustomSimpleDraweeView) itemView.findViewById(R.id.item_phone_pics_select_icon);
            item_phone_pics_select_icon_layout = (LinearLayout) itemView.findViewById(R.id.item_phone_pics_select_icon_layout);
        }
    }
    public static void showThumb(Uri uri, SimpleDraweeView draweeView){
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setAutoRotateEnabled(true)
                .setResizeOptions(new ResizeOptions(150, 150))
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .setOldController(draweeView.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        draweeView.setController(controller);
    }
    //设置选择图标的接口
    interface OnItemPhonePicsSelectIconLayout{
        void onItemClick(int position, CustomSimpleDraweeView draweeView, ImageItem imageItem);
    }
    public void setOnItemPhonePicsSelectIconLayout(OnItemPhonePicsSelectIconLayout mOnItemPhonePicsSelectIconLayout){
        this.mOnItemPhonePicsSelectIconLayout = mOnItemPhonePicsSelectIconLayout;
    }
}
