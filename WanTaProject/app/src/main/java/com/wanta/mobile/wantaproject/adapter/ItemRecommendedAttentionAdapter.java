package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.domain.IconsInfo;
import com.wanta.mobile.wantaproject.uploadimage.DraweeUtils;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.JsonParseUtils;
import com.wanta.mobile.wantaproject.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public class ItemRecommendedAttentionAdapter extends RecyclerView.Adapter {

    private Context context;
    private String iconInfo;
    private List<String> list = new ArrayList<>();
    private List<String> recommendImages = new ArrayList<>();
    private List<IconsInfo> recommendIconInfo = new ArrayList<>();
    private String currentDate="";
    private int flag = 0;
    private OnClickRecommendedAttentionListener mOnClickRecommendedAttentionListener = null;
    private OnClickRecommendedAttentionImageListener mOnClickRecommendedAttentionImageListener = null;

    public ItemRecommendedAttentionAdapter(Context context) {
        this.context = context;
    }

    public void setItemRecommendList(String iconInfo,int flag){
        this.iconInfo = iconInfo;
        this.flag = flag;
        if (flag==1){
            //当前是推荐关注的
            try {
                JSONArray jsonArray = new JSONArray(iconInfo);
                if (jsonArray.length()!=0){
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int cmtnum = 0;
                        if (jsonObject.has("cmtnum")) {
                            cmtnum = jsonObject.getInt("cmtnum");
                        } else {
                            cmtnum = 0;
                        }
                        recommendIconInfo.add(new IconsInfo(jsonObject.getInt("icnfid"),jsonObject.getString("title"),jsonObject.getString("content"),
                                jsonObject.getInt("likenum"),jsonObject.getInt("storenum"),jsonObject.getInt("userid"),jsonObject.getString("favourstate"),
                                jsonObject.getString("storedstate"),jsonObject.getString("lng"),jsonObject.getString("lat"),jsonObject.getString("address"),
                                jsonObject.getString("createdat"),jsonObject.getString("images"),jsonObject.getString("topics"),jsonObject.getString("browsenum"),
                                jsonObject.getString("title_cn"),jsonObject.getString("content_cn"),cmtnum));
                    }
                }
            } catch (JSONException e) {

            }
        }else if (flag==2){
            //当前是其他个人信息页面的信息
            try {
                JSONArray jsonArray = new JSONArray(iconInfo);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(jsonObject.getString("imglink"));
                }
                recommendImages = list;
            } catch (JSONException e) {

            }
        }

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adapter_recommended_attention_layout,null);
        RecommendedAttentionViewHolder holder = new RecommendedAttentionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (flag==1){
            DraweeUtils.loadTagsPics(Uri.parse(Constants.FIRST_PAGE_IMAGE_URL+ "icon"+JsonParseUtils.getFirstPicUrl(recommendIconInfo.get(position).getImages())),((RecommendedAttentionViewHolder)holder).item_adapter_recommend_attention_drawee);
//            ((RecommendedAttentionViewHolder)holder).item_adapter_recommend_attention_drawee.setImageURI(Uri.parse(Constants.FIRST_PAGE_IMAGE_URL+ "icon"+JsonParseUtils.getFirstPicUrl(recommendIconInfo.get(position).getImages())));
            ((RecommendedAttentionViewHolder)holder).item_adapter_recommend_attention_drawee.setWidth(Constants.PHONE_WIDTH/4);
            ((RecommendedAttentionViewHolder)holder).item_adapter_recommend_attention_drawee.setHeight(Constants.PHONE_WIDTH/4);
            ((RecommendedAttentionViewHolder)holder).item_adapter_recommend_attentio_create_date_tv.setVisibility(View.VISIBLE);
            ((RecommendedAttentionViewHolder)holder).item_adapter_recommend_attentio_create_date_tv.setText(recommendIconInfo.get(position).getCreatedat());
            ((RecommendedAttentionViewHolder)holder).item_adapter_recommend_attention_drawee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickRecommendedAttentionImageListener.onClick(position,recommendIconInfo.get(position));
                }
            });
        }else if (flag==2){
            DraweeUtils.loadTagsPics(Uri.parse(Constants.FIRST_PAGE_IMAGE_URL+recommendImages.get(position)),((RecommendedAttentionViewHolder)holder).item_adapter_recommend_attention_drawee);
            ((RecommendedAttentionViewHolder)holder).item_adapter_recommend_attention_drawee.setImageURI(Uri.parse(Constants.FIRST_PAGE_IMAGE_URL+recommendImages.get(position)));
            ((RecommendedAttentionViewHolder)holder).item_adapter_recommend_attention_drawee.setWidth(Constants.PHONE_WIDTH/4);
            ((RecommendedAttentionViewHolder)holder).item_adapter_recommend_attention_drawee.setHeight(Constants.PHONE_WIDTH/4);
            ((RecommendedAttentionViewHolder)holder).item_adapter_recommend_attentio_create_date_tv.setVisibility(View.GONE);
            ((RecommendedAttentionViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickRecommendedAttentionListener.onClick(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (flag==1){
            return recommendIconInfo.size();
        }else if (flag==2){
            return recommendImages.size();
        }
        return 0;
    }
    class RecommendedAttentionViewHolder extends RecyclerView.ViewHolder {

        private final CustomSimpleDraweeView item_adapter_recommend_attention_drawee;
        private final TextView item_adapter_recommend_attentio_create_date_tv;

        public RecommendedAttentionViewHolder(View itemView) {
            super(itemView);
            item_adapter_recommend_attention_drawee = (CustomSimpleDraweeView) itemView.findViewById(R.id.item_adapter_recommend_attention_drawee);
            item_adapter_recommend_attentio_create_date_tv = (TextView) itemView.findViewById(R.id.item_adapter_recommend_attentio_create_date_tv);
        }
    }

    //设置item的点击事件
    public interface OnClickRecommendedAttentionListener{
        void onClick(int position);
    }

    public void setOnClickRecommendedAttentionListener(OnClickRecommendedAttentionListener mOnClickRecommendedAttentionListener){
        this.mOnClickRecommendedAttentionListener = mOnClickRecommendedAttentionListener;
    }

    //设置图片的点击事件
    public interface OnClickRecommendedAttentionImageListener{
        void onClick(int position, IconsInfo iconsInfo);
    }

    public void setOnClickRecommendedAttentionImageListener(OnClickRecommendedAttentionImageListener mOnClickRecommendedAttentionImageListener){
        this.mOnClickRecommendedAttentionImageListener = mOnClickRecommendedAttentionImageListener;
    }
}
