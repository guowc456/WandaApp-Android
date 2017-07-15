package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.utils.Constants;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class AskQuestionRecycleViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private String  mList[];
    private int flag = 0;//判断当前的适配器的类型
    private AskQuestionRecycleViewClickListener mAskQuestionRecycleViewClickListener= null;

    public AskQuestionRecycleViewAdapter(Context mContext, String  mList[],int flag) {
        this.mContext = mContext;
        this.mList = mList;
        this.flag = flag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_askquestion_adapter,parent,false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (flag==1){
            //只有文本框
            ((ItemViewHolder)holder).mItem_askquestion_image_face.setVisibility(View.GONE);
            ((ItemViewHolder)holder).mItem_askquestion_image_draw.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH/4,Constants.PHONE_HEIGHT/22);
            ((ItemViewHolder)holder).mItem_askquestion_content_layout.setLayoutParams(params);
            ((ItemViewHolder)holder).mItem_askquestion_content.setText(mList[position]);
        }else if (flag==2){
            //文本框前有笑脸选择框
            ((ItemViewHolder)holder).mItem_askquestion_image_draw.setVisibility(View.GONE);
            ((ItemViewHolder)holder).mItem_askquestion_image_face.setVisibility(View.VISIBLE);
            ((ItemViewHolder)holder).mItem_askquestion_image_face.setSize(Constants.PHONE_WIDTH/20,Constants.PHONE_WIDTH/20);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (Constants.PHONE_WIDTH/4.5),Constants.PHONE_HEIGHT/22);
            ((ItemViewHolder)holder).mItem_askquestion_content_layout.setLayoutParams(params);
            ((ItemViewHolder)holder).mItem_askquestion_content.setText(mList[position]);
        }else if (flag==3){
            //文本框后有打钩选择框
            ((ItemViewHolder)holder).mItem_askquestion_image_face.setVisibility(View.GONE);
            ((ItemViewHolder)holder).mItem_askquestion_image_draw.setVisibility(View.VISIBLE);
            ((ItemViewHolder)holder).mItem_askquestion_image_draw.setSize(Constants.PHONE_WIDTH/20,Constants.PHONE_WIDTH/20);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH/4,Constants.PHONE_HEIGHT/22);
            ((ItemViewHolder)holder).mItem_askquestion_content_layout.setLayoutParams(params);
            ((ItemViewHolder)holder).mItem_askquestion_content.setText(mList[position]);
        }else if (flag==4){
            //看别人风格文本框
            ((ItemViewHolder)holder).mItem_askquestion_image_face.setVisibility(View.GONE);
            ((ItemViewHolder)holder).mItem_askquestion_image_draw.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH/3,Constants.PHONE_HEIGHT/22);
            ((ItemViewHolder)holder).mItem_askquestion_content_layout.setLayoutParams(params);
            ((ItemViewHolder)holder).mItem_askquestion_content.setText(mList[position]);
        }

    }

    @Override
    public int getItemCount() {
        return mList.length;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        private final TextView mItem_askquestion_content;
        private final LinearLayout mItem_askquestion_content_layout;
        private final MyImageView mItem_askquestion_image_face;
        private final MyImageView mItem_askquestion_image_draw;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mItem_askquestion_content = (TextView) itemView.findViewById(R.id.item_askquestion_content);
            mItem_askquestion_content_layout = (LinearLayout) itemView.findViewById(R.id.item_askquestion_content_layout);
            mItem_askquestion_image_face = (MyImageView) itemView.findViewById(R.id.item_askquestion_image_face);
            mItem_askquestion_image_draw = (MyImageView) itemView.findViewById(R.id.item_askquestion_image_draw);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAskQuestionRecycleViewClickListener.setOnItemClick(v);
                }
            });
        }
    }
    public interface AskQuestionRecycleViewClickListener{
        void setOnItemClick(View view);
    }
    public void setAskQuestionRecycleViewClickListener(AskQuestionRecycleViewClickListener mAskQuestionRecycleViewClickListener){
        this.mAskQuestionRecycleViewClickListener = mAskQuestionRecycleViewClickListener;
    }
}
