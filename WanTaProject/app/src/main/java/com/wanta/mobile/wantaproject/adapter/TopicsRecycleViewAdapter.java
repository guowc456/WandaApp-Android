package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.MostPopularInfo;
import com.wanta.mobile.wantaproject.domain.TopicsInfo;
import com.wanta.mobile.wantaproject.uploadimage.DraweeUtils;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.JsonParseUtils;
import com.wanta.mobile.wantaproject.utils.SimpleDraweeControlUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/4/13.
 */

public class TopicsRecycleViewAdapter extends RecyclerView.Adapter {

    private List<TopicsInfo> topicsInfoList;
    private Context context;
    private OnTopicsItemHeartListener mOnTopicsItemHeartListener = null;
    private OnTopicsItemImageListener monTopicsItemImageListener = null;

    public TopicsRecycleViewAdapter( Context context,List<TopicsInfo> topicsInfoList) {
        this.topicsInfoList = topicsInfoList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_topics_layout,null);
        TopicsViewHolder holder = new TopicsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final TopicsInfo info = topicsInfoList.get(position);
        ((TopicsViewHolder)holder).item_topics_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ((TopicsViewHolder)holder).item_topics_image.setAdjustViewBounds(true);
        ((TopicsViewHolder)holder).item_topics_image.setMaxWidth(Constants.PHONE_WIDTH/2);
        ((TopicsViewHolder)holder).item_topics_image.setMaxHeight((int) (info.getHeight() * (((Constants.PHONE_WIDTH / 2)*1.00) / info.getWidth())));
        ((TopicsViewHolder)holder).item_topics_image.setAspectRatio(info.getWidth()/info.getHeight());
        ((TopicsViewHolder)holder).item_topics_image.setWidth(Constants.PHONE_WIDTH/2);
        ((TopicsViewHolder)holder).item_topics_image.setHeight((int) (info.getHeight()*(((Constants.PHONE_WIDTH/2)*1.00)/info.getWidth())));
        DraweeUtils.resizepic(Uri.parse(Constants.FIRST_PAGE_IMAGE_URL + info.getImgpath()),((TopicsViewHolder)holder).item_topics_image);
        ((TopicsViewHolder)holder).item_topics_author_icon.setWidth(Constants.PHONE_WIDTH/12);
        ((TopicsViewHolder)holder).item_topics_author_icon.setHeight(Constants.PHONE_WIDTH/12);
        SimpleDraweeControlUtils.getNetRingSimpleDraweeControl(context,((TopicsViewHolder)holder).item_topics_author_icon,Constants.FIRST_PAGE_IMAGE_URL +info.getImgpath());
        ((TopicsViewHolder)holder).item_topics_heart.setSize(Constants.PHONE_WIDTH/12,Constants.PHONE_WIDTH/12);
        if (!"null".equals(info.getFavourstate())){
            ((TopicsViewHolder)holder).item_topics_heart.setImageResource(R.mipmap.heart_press);
        }else {
            ((TopicsViewHolder)holder).item_topics_heart.setImageResource(R.mipmap.heart_no_press);
        }
        ((TopicsViewHolder)holder).item_topics_title.setText(info.getTitle());
        ((TopicsViewHolder)holder).item_topics_content.setText(info.getContent()+"");
        ((TopicsViewHolder)holder).item_topics_heartconnt.setText(info.getBeliked()+"");
        ((TopicsViewHolder)holder).item_mostpopular_body_msg_tv.setText("话题日记 "+info.getIconnum()+"  |  浏览量  "+info.getBebrowsed());

        ((TopicsViewHolder)holder).item_topics_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnTopicsItemHeartListener.onItemHeartClick(position,info,((TopicsViewHolder)holder).item_topics_heartconnt,((TopicsViewHolder)holder).item_topics_heart);
            }
        });

        ((TopicsViewHolder)holder).item_topics_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monTopicsItemImageListener.onItemImageClick(info);
            }
        });

    }


    @Override
    public int getItemCount() {
        return topicsInfoList.size();
    }

    class TopicsViewHolder extends RecyclerView.ViewHolder {

        private final CustomSimpleDraweeView item_topics_image;
        private final CustomSimpleDraweeView item_topics_author_icon;
        private final TextView item_topics_title;
        private final TextView item_topics_content;
        private final TextView item_topics_heartconnt;
        private final MyImageView item_topics_heart;
        private final TextView item_mostpopular_body_msg_tv;

        public TopicsViewHolder(View itemView) {
            super(itemView);
            item_topics_image = (CustomSimpleDraweeView) itemView.findViewById(R.id.item_topics_image);
            item_topics_author_icon = (CustomSimpleDraweeView) itemView.findViewById(R.id.item_topics_author_icon);
            item_topics_title = (TextView) itemView.findViewById(R.id.item_topics_title);
            item_topics_content = (TextView) itemView.findViewById(R.id.item_topics_content);
            item_topics_heartconnt = (TextView) itemView.findViewById(R.id.item_topics_heartconnt);
            item_topics_heart = (MyImageView) itemView.findViewById(R.id.item_topics_heart);
            item_mostpopular_body_msg_tv = (TextView) itemView.findViewById(R.id.item_mostpopular_body_msg_tv);
        }
    }

    public interface OnTopicsItemHeartListener{
        void onItemHeartClick(int position, TopicsInfo info, TextView heartconnt_tv, MyImageView topics_heart_imag);
    }
    public void setOnTopicsItemHeartListener(OnTopicsItemHeartListener mOnTopicsItemHeartListener){
        this.mOnTopicsItemHeartListener = mOnTopicsItemHeartListener;
    }

    public interface OnTopicsItemImageListener{
        void onItemImageClick(TopicsInfo info);
    }
    public void setOnTopicsItemImageListener(OnTopicsItemImageListener monTopicsItemImageListener){
        this.monTopicsItemImageListener = monTopicsItemImageListener;
    }

}
