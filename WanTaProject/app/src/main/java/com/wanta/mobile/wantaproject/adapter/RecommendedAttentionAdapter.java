package com.wanta.mobile.wantaproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.ItemMostpopularActivity;
import com.wanta.mobile.wantaproject.activity.RecommendedAttentionActivity;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.CacheDataUtils;
import com.wanta.mobile.wantaproject.domain.IconsInfo;
import com.wanta.mobile.wantaproject.domain.RecommendCareInfo;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.SimpleDraweeControlUtils;
import com.wanta.mobile.wantaproject.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public class RecommendedAttentionAdapter extends RecyclerView.Adapter {

    private Context context;
    private int flag ;//表示显示推荐关注的还是显示已关注的
    private List<RecommendCareInfo> recommendList;
    private OnRecommendedAttentionCareListener mOnRecommendedAttentionCareListener = null;

    public RecommendedAttentionAdapter(Context context,int flag) {
        this.context = context;
        this.flag = flag;
    }

    public void setRecommendList(List<RecommendCareInfo> recommendList){
        this.recommendList = recommendList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_recommended_attention_layout,null);
        RecommendedAttentionViewHolder holder = new RecommendedAttentionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final RecommendCareInfo recommendCareInfo = recommendList.get(position);
        ((RecommendedAttentionViewHolder)holder).adapter_recommended_attention_care_icon.setSize(Constants.PHONE_WIDTH/7,Constants.PHONE_WIDTH/16);
        if (flag==1){
            //推荐关注的
            ((RecommendedAttentionViewHolder)holder).adapter_recommended_attention_recycleview.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            ((RecommendedAttentionViewHolder)holder).adapter_recommended_attention_recycleview.setLayoutManager(linearLayoutManager);
            ItemRecommendedAttentionAdapter attentionAdapter = new ItemRecommendedAttentionAdapter(context);
            attentionAdapter.setOnClickRecommendedAttentionImageListener(new ItemRecommendedAttentionAdapter.OnClickRecommendedAttentionImageListener() {
                @Override
                public void onClick(int position, IconsInfo iconsInfo) {
                    CacheDataUtils.setCurrentNeedToSaveData(iconsInfo.getIcnfid(),iconsInfo.getTitle(),iconsInfo.getContent(),iconsInfo.getLikenum(),iconsInfo.getStorenum(),
                            iconsInfo.getUserid(),recommendCareInfo.getUsername(),recommendCareInfo.getAvatar(),iconsInfo.getFavourstate(),iconsInfo.getStoredstate(),
                            iconsInfo.getLng(),iconsInfo.getLat(),iconsInfo.getAddress(),iconsInfo.getAddress(),iconsInfo.getCreatedat(),iconsInfo.getImages(),
                            "null",recommendCareInfo.getHeight()+"",recommendCareInfo.getWeight(),recommendCareInfo.getUnderbustgirth()+"",recommendCareInfo.getBra(),
                            iconsInfo.getCmtnum(),iconsInfo.getTopics(),iconsInfo.getBrowsenum(),iconsInfo.getTitle_cn(),iconsInfo.getContent_cn());
                    ActivityColection.addActivity((RecommendedAttentionActivity) context);
                    Intent intent = new Intent(context, ItemMostpopularActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    ((RecommendedAttentionActivity) context).finish();
                }
            });
            attentionAdapter.setItemRecommendList(recommendCareInfo.getIcons(),1);
            ((RecommendedAttentionViewHolder)holder).adapter_recommended_attention_recycleview.setAdapter(attentionAdapter);
        }else if (flag==2){
            //已经关注的
            ((RecommendedAttentionViewHolder)holder).adapter_recommended_attention_recycleview.setVisibility(View.GONE);
        }
        if ("false".equals(recommendCareInfo.getIsCare())){
            ((RecommendedAttentionViewHolder)holder).adapter_recommended_attention_care_icon.setSelected(true);
        }else {
            ((RecommendedAttentionViewHolder)holder).adapter_recommended_attention_care_icon.setSelected(false);
        }
        ((RecommendedAttentionViewHolder)holder).adapter_recommended_attention_care_icon_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRecommendedAttentionCareListener.onItemClick(((RecommendedAttentionViewHolder)holder).itemView,recommendCareInfo, ((RecommendedAttentionViewHolder)holder).adapter_recommended_attention_care_icon);
            }
        });
        ((RecommendedAttentionViewHolder)holder).adapter_recommended_attention_author_icon.setWidth(Constants.PHONE_WIDTH/10);
        ((RecommendedAttentionViewHolder)holder).adapter_recommended_attention_author_icon.setHeight(Constants.PHONE_WIDTH/10);
        SimpleDraweeControlUtils.getNetRingSimpleDraweeControl(context,((RecommendedAttentionViewHolder)holder).adapter_recommended_attention_author_icon,
                Constants.FIRST_PAGE_IMAGE_URL+recommendCareInfo.getAvatar());

        ((RecommendedAttentionViewHolder)holder).recommende_attention_author_name.setText(recommendCareInfo.getUsername());
        if ("null".equals(recommendCareInfo.getAddress())){
            ((RecommendedAttentionViewHolder)holder).recommende_attention_author_location.setText("");
        }else {
            ((RecommendedAttentionViewHolder)holder).recommende_attention_author_location.setText(recommendCareInfo.getAddress());
        }
        ((RecommendedAttentionViewHolder)holder).recommende_attention_author_body_msg.setText(
                recommendCareInfo.getHeight()+"cm/"+recommendCareInfo.getWeight()+"kg"+"   "+recommendCareInfo.getBra()
        );
        ((RecommendedAttentionViewHolder)holder).recommende_attention_author_fensi_number.setText(recommendCareInfo.getFunsnum()+"人关注");
    }

    @Override
    public int getItemCount() {
        return recommendList.size();
    }
    class RecommendedAttentionViewHolder extends RecyclerView.ViewHolder {

        private final CustomSimpleDraweeView adapter_recommended_attention_author_icon;
        private final MyImageView adapter_recommended_attention_care_icon;
        private final RecyclerView adapter_recommended_attention_recycleview;
        private final TextView recommende_attention_author_name;
        private final TextView recommende_attention_author_location;
        private final TextView recommende_attention_author_body_msg;
        private final TextView recommende_attention_author_fensi_number;
        private final LinearLayout adapter_recommended_attention_care_icon_layout;

        public RecommendedAttentionViewHolder(View itemView) {
            super(itemView);
            adapter_recommended_attention_author_icon = (CustomSimpleDraweeView) itemView.findViewById(R.id.adapter_recommended_attention_author_icon);
            adapter_recommended_attention_care_icon = (MyImageView) itemView.findViewById(R.id.adapter_recommended_attention_care_icon);
            adapter_recommended_attention_recycleview = (RecyclerView) itemView.findViewById(R.id.adapter_recommended_attention_recycleview);
            recommende_attention_author_name = (TextView) itemView.findViewById(R.id.recommende_attention_author_name);
            recommende_attention_author_location = (TextView) itemView.findViewById(R.id.recommende_attention_author_location);
            recommende_attention_author_body_msg = (TextView) itemView.findViewById(R.id.recommende_attention_author_body_msg);
            recommende_attention_author_fensi_number = (TextView) itemView.findViewById(R.id.recommende_attention_author_fensi_number);
            adapter_recommended_attention_care_icon_layout = (LinearLayout) itemView.findViewById(R.id.adapter_recommended_attention_care_icon_layout);
        }
    }

    public interface OnRecommendedAttentionCareListener{
        void onItemClick(View view, RecommendCareInfo recommendCareInfo, MyImageView imageview);
    }
    public void setOnRecommendedAttentionCareListener(OnRecommendedAttentionCareListener mOnRecommendedAttentionCareListener){
        this.mOnRecommendedAttentionCareListener = mOnRecommendedAttentionCareListener;
    }

}
