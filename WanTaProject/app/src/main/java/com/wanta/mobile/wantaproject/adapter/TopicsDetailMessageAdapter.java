package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.TopicsDetailMessageInfo;
import com.wanta.mobile.wantaproject.domain.TopicsInfo;
import com.wanta.mobile.wantaproject.uploadimage.DraweeUtils;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.JsonParseUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.SimpleDraweeControlUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/4/13.
 */

public class TopicsDetailMessageAdapter extends RecyclerView.Adapter {

    private List<TopicsDetailMessageInfo> topicsInfoList;
    private Context context;
    private OnTopicsDetailMessageListener mOnTopicsDetailMessageListener = null;

    public TopicsDetailMessageAdapter(Context context) {
        this.context = context;
    }

    public void setTopicsNumber(List<TopicsDetailMessageInfo> topicsInfoList){
        this.topicsInfoList = topicsInfoList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_topics_detail_msg_layout,null);
        TopicsDetailMsgViewHolder holder = new TopicsDetailMsgViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final TopicsDetailMessageInfo topicsDetailMessageInfo = topicsInfoList.get(position);
        if (!TextUtils.isEmpty(topicsDetailMessageInfo.getImages())){
            int[] firstPicSize = JsonParseUtils.getFirstPicSize(topicsDetailMessageInfo.getImages());
            final int width = firstPicSize[0];
            final int height = firstPicSize[1];
            ((TopicsDetailMsgViewHolder)holder).item_topics_detail_msg_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ((TopicsDetailMsgViewHolder)holder).item_topics_detail_msg_image.setAdjustViewBounds(true);
            ((TopicsDetailMsgViewHolder)holder).item_topics_detail_msg_image.setMaxWidth(Constants.PHONE_WIDTH/2);
            ((TopicsDetailMsgViewHolder)holder).item_topics_detail_msg_image.setMaxHeight((int) (height * (((Constants.PHONE_WIDTH / 2)*1.00) / width)));
            ((TopicsDetailMsgViewHolder)holder).item_topics_detail_msg_image.setAspectRatio(width/height);
            ((TopicsDetailMsgViewHolder)holder).item_topics_detail_msg_image.setWidth((int) (Constants.PHONE_WIDTH*1.00/2.2));
            ((TopicsDetailMsgViewHolder)holder).item_topics_detail_msg_image.setHeight((int) (height*(((Constants.PHONE_WIDTH*1.00/2.2)*1.00)/width)));
            SimpleDraweeControlUtils.getNetFourCornersSimpleDraweeControl(context,((TopicsDetailMsgViewHolder)holder).item_topics_detail_msg_image,Uri.parse(Constants.FIRST_PAGE_IMAGE_URL +JsonParseUtils.getTopicsFirstPicUrl(topicsDetailMessageInfo.getImages())));
            LogUtils.showVerbose("TopicsDetailMessageAdapter","话题具体信息的图片的地址："+Constants.FIRST_PAGE_IMAGE_URL +JsonParseUtils.getTopicsFirstPicUrl(topicsDetailMessageInfo.getImages()));
       }
        ((TopicsDetailMsgViewHolder)holder).item_topics_detail_msg_tv.setText(topicsDetailMessageInfo.getTitle());
        ((TopicsDetailMsgViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnTopicsDetailMessageListener.itemClick(topicsDetailMessageInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topicsInfoList.size();
    }

    class TopicsDetailMsgViewHolder extends RecyclerView.ViewHolder {

        private final CustomSimpleDraweeView item_topics_detail_msg_image;
        private final TextView item_topics_detail_msg_tv;

        public TopicsDetailMsgViewHolder(View itemView) {
            super(itemView);
            item_topics_detail_msg_image = (CustomSimpleDraweeView) itemView.findViewById(R.id.item_topics_detail_msg_image);
            item_topics_detail_msg_tv = (TextView) itemView.findViewById(R.id.item_topics_detail_msg_tv);
        }
    }

    public interface OnTopicsDetailMessageListener{
        void itemClick(TopicsDetailMessageInfo topicsDetailMessageInfo);
    }
    public void setOnTopicsDetailMessageListener(OnTopicsDetailMessageListener mOnTopicsDetailMessageListener){
        this.mOnTopicsDetailMessageListener = mOnTopicsDetailMessageListener;
    }
}
