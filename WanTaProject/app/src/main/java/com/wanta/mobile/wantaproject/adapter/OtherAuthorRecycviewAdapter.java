package com.wanta.mobile.wantaproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.ItemMostpopularActivity;
import com.wanta.mobile.wantaproject.activity.OtherAuthorActivity;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.CacheDataUtils;
import com.wanta.mobile.wantaproject.domain.MostPopularInfo;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.ToastUtil;

import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class OtherAuthorRecycviewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<MostPopularInfo> otherAuthorList ;
    private int flag = 0;//1是其他人员调用的，2是自己信息调用的


    public OtherAuthorRecycviewAdapter(Context context) {
        mContext = context;
    }

    public void setOtherAuthorListMsg(List<MostPopularInfo> otherAuthorList,int flag){
        this.otherAuthorList = otherAuthorList;
        this.flag = flag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view_other_author = LayoutInflater.from(mContext).inflate(R.layout.item_other_author_recycleview_adapter,null);
        ItemFindViewHolder viewHolder = new ItemFindViewHolder(view_other_author);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MostPopularInfo mostPopularInfo = otherAuthorList.get(position);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        ((ItemFindViewHolder)holder).item_other_author_recycleview.setLayoutManager(linearLayoutManager);
        ItemRecommendedAttentionAdapter attentionAdapter = new ItemRecommendedAttentionAdapter(mContext);
        attentionAdapter.setItemRecommendList(mostPopularInfo.getImages(),2);
        attentionAdapter.setOnClickRecommendedAttentionListener(new ItemRecommendedAttentionAdapter.OnClickRecommendedAttentionListener() {
            @Override
            public void onClick(int position) {
                LogUtils.showVerbose("OtherAuthorRecycviewAdapter","position=="+position);

                Intent intent = new Intent(mContext, ItemMostpopularActivity.class);
                if (flag==1){
                    intent.putExtra("otherAuthorToItemMost","otherAuthorToItemMost");
                    ActivityColection.addActivity((OtherAuthorActivity) mContext);
                }else if (flag==2){
                    intent.putExtra("ownAuthorToItemMost","ownAuthorToItemMost");
                    ActivityColection.addActivity((Activity)mContext);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                CacheDataUtils.setCurrentNeedToSaveData( mostPopularInfo.getIcnfid(), mostPopularInfo.getTitle(), mostPopularInfo.getContent(), mostPopularInfo.getLikenum(), mostPopularInfo.getStorenum(),
                        mostPopularInfo.getUserid(), mostPopularInfo.getUsername(), mostPopularInfo.getUseravatar(), mostPopularInfo.getFavourstate(), mostPopularInfo.getStoredstate(),
                        mostPopularInfo.getLng(), mostPopularInfo.getLat(), mostPopularInfo.getAddress(), mostPopularInfo.getUaddress(), mostPopularInfo.getCreatedat(), mostPopularInfo.getImages(),
                        mostPopularInfo.getFollowstate(), mostPopularInfo.getHeight(), mostPopularInfo.getWeight(), mostPopularInfo.getBust(), mostPopularInfo.getBra(), mostPopularInfo.getCmtnum()
                        ,mostPopularInfo.getTopics(),mostPopularInfo.getBrowsenum(),mostPopularInfo.getTitle_cn(),mostPopularInfo.getContent_cn());
                mContext.startActivity(intent);
                ((Activity)mContext).finish();
            }
        });
        ((ItemFindViewHolder)holder).item_other_author_recycleview.setAdapter(attentionAdapter);

        ((ItemFindViewHolder)holder).item_other_author_recycleview_adapter_title.setText(mostPopularInfo.getTitle());
        ((ItemFindViewHolder)holder).item_other_author_recycleview_adapter_createDate.setText(mostPopularInfo.getCreatedat());
        ((ItemFindViewHolder)holder).item_other_author_recycleview_adapter_browsenum.setText(mostPopularInfo.getBrowsenum()+"");
        ((ItemFindViewHolder)holder).item_other_author_recycleview_adapter_likenum.setText(mostPopularInfo.getLikenum()+"");
        ((ItemFindViewHolder)holder).item_other_author_recycleview_adapter_commentNum.setText(mostPopularInfo.getCmtnum()+"");
        ((ItemFindViewHolder)holder).item_other_author_recycleview_adapter_storenum.setText(mostPopularInfo.getStorenum()+"");
    }

    @Override
    public int getItemCount() {
        return otherAuthorList.size();
    }

    class ItemFindViewHolder extends RecyclerView.ViewHolder{

        private final RecyclerView item_other_author_recycleview;
        private final MyImageView ohter_author_eye_icon;
        private final MyImageView other_author_heart_icon;
        private final MyImageView ohter_author_message_icon;
        private final MyImageView ohter_author_store_icon;
        private final TextView item_other_author_recycleview_adapter_title;
        private final TextView item_other_author_recycleview_adapter_createDate;
        private final TextView item_other_author_recycleview_adapter_browsenum;
        private final TextView item_other_author_recycleview_adapter_likenum;
        private final TextView item_other_author_recycleview_adapter_commentNum;
        private final TextView item_other_author_recycleview_adapter_storenum;

        public ItemFindViewHolder(View itemView) {
            super(itemView);
            item_other_author_recycleview = (RecyclerView) itemView.findViewById(R.id.item_other_author_recycleview);
            ohter_author_eye_icon = (MyImageView) itemView.findViewById(R.id.ohter_author_eye_icon);
            ohter_author_eye_icon.setSize(Constants.PHONE_WIDTH/18,Constants.PHONE_WIDTH/18);
            other_author_heart_icon = (MyImageView) itemView.findViewById(R.id.other_author_heart_icon);
            other_author_heart_icon.setSize(Constants.PHONE_WIDTH/18,Constants.PHONE_WIDTH/18);
            ohter_author_message_icon = (MyImageView) itemView.findViewById(R.id.ohter_author_message_icon);
            ohter_author_message_icon.setSize(Constants.PHONE_WIDTH/18,Constants.PHONE_WIDTH/18);
            ohter_author_store_icon = (MyImageView) itemView.findViewById(R.id.ohter_author_store_icon);
            ohter_author_store_icon.setSize(Constants.PHONE_WIDTH/18,Constants.PHONE_WIDTH/18);
            item_other_author_recycleview_adapter_title = (TextView) itemView.findViewById(R.id.item_other_author_recycleview_adapter_title);
            item_other_author_recycleview_adapter_createDate = (TextView) itemView.findViewById(R.id.item_other_author_recycleview_adapter_createDate);
            item_other_author_recycleview_adapter_browsenum = (TextView) itemView.findViewById(R.id.item_other_author_recycleview_adapter_browsenum);
            item_other_author_recycleview_adapter_likenum = (TextView) itemView.findViewById(R.id.item_other_author_recycleview_adapter_likenum);
            item_other_author_recycleview_adapter_commentNum = (TextView) itemView.findViewById(R.id.item_other_author_recycleview_adapter_commentNum);
            item_other_author_recycleview_adapter_storenum = (TextView) itemView.findViewById(R.id.item_other_author_recycleview_adapter_storenum);
        }
    }

}
