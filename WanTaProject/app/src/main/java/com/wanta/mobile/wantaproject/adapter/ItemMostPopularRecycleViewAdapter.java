package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.domain.CommentsInfo;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.SimpleDraweeControlUtils;

import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class ItemMostPopularRecycleViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<CommentsInfo> mList;
    private OnAgreeClickListener mOnAgreeClickListener = null;
    private OnAuthorMessageClickListener mOnAuthorMessageClickListener = null;
    private OnResponseMessageClickListener mOnResponseMessageClickListener = null;

    public ItemMostPopularRecycleViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setShowNum(List<CommentsInfo> mList){
        this.mList = mList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mostpopular_recycleview_adapter,parent,false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final CommentsInfo commentInfo = mList.get(position);
        ((ItemViewHolder)holder).mItem_mostpopular_recycleview_author.setText(commentInfo.getUsername());
        ((ItemViewHolder)holder).mItem_mostpopular_recycleview_date.setText(commentInfo.getCreatedat());
        ((ItemViewHolder)holder).mItem_mostpopular_recycleview_response_num.setText("当前回复了"+commentInfo.getRpnum()+"条");
        ((ItemViewHolder)holder).mItem_mostpopular_recycleview_replay_content.setText(commentInfo.getComment());
        ((ItemViewHolder)holder).mItem_mostpopular_recycleview_author_icon.setWidth(Constants.PHONE_WIDTH / 12);
        ((ItemViewHolder)holder).mItem_mostpopular_recycleview_author_icon.setHeight(Constants.PHONE_WIDTH / 12);
        if ("null".equals(commentInfo.getAvatar())){
            ((ItemViewHolder)holder).mItem_mostpopular_recycleview_author_icon.setImageResource(R.mipmap.icon);
        }else {
            SimpleDraweeControlUtils.getNetRingSimpleDraweeControl(mContext,((ItemViewHolder)holder).mItem_mostpopular_recycleview_author_icon,Constants.FIRST_PAGE_IMAGE_URL + commentInfo.getAvatar());
        }
        ((ItemViewHolder)holder).mItem_mostpopular_recycleview_agree_click.setWidth(Constants.PHONE_WIDTH/18);
        ((ItemViewHolder)holder).mItem_mostpopular_recycleview_agree_click.setHeight(Constants.PHONE_WIDTH/18);
        if ("null".equals(commentInfo.getCmtfavour())){
            //当前没有点赞
            ((ItemViewHolder)holder).mItem_mostpopular_recycleview_agree_click.setImageResource(R.mipmap.agree_click);
        }else {
            //当前已经点赞了
            ((ItemViewHolder)holder).mItem_mostpopular_recycleview_agree_click.setImageResource(R.mipmap.agree_click_press);
        }
        ((ItemViewHolder)holder).mItem_mostpopular_recycleview_like_num.setText(commentInfo.getLikednum()+"");
        ((ItemViewHolder)holder).mAuthor_msg_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnAuthorMessageClickListener.authorClick();
            }
        });
        ((ItemViewHolder)holder).mResponse_msg_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnResponseMessageClickListener.responseClick(commentInfo);
            }
        });
        ((ItemViewHolder)holder).mAgree_click_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnAgreeClickListener.agreeclick(((ItemViewHolder)holder).itemView,((ItemViewHolder)holder).mItem_mostpopular_recycleview_like_num,((ItemViewHolder)holder).mItem_mostpopular_recycleview_agree_click);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        private final CustomSimpleDraweeView mItem_mostpopular_recycleview_author_icon;
        private final TextView mItem_mostpopular_recycleview_author;
        private final TextView mItem_mostpopular_recycleview_date;
        private final CustomSimpleDraweeView mItem_mostpopular_recycleview_agree_click;
        private final TextView mItem_mostpopular_recycleview_replay_content;
        private final TextView mItem_mostpopular_recycleview_response_num;
        private final TextView mItem_mostpopular_recycleview_like_num;
        private final LinearLayout mAuthor_msg_layout;
        private final LinearLayout mResponse_msg_layout;
        private final LinearLayout mAgree_click_layout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mItem_mostpopular_recycleview_author_icon = (CustomSimpleDraweeView) itemView.findViewById(R.id.item_mostpopular_recycleview_author_icon);
            mItem_mostpopular_recycleview_author = (TextView) itemView.findViewById(R.id.item_mostpopular_recycleview_author);
            mItem_mostpopular_recycleview_date = (TextView) itemView.findViewById(R.id.item_mostpopular_recycleview_date);
            mItem_mostpopular_recycleview_agree_click = (CustomSimpleDraweeView) itemView.findViewById(R.id.item_mostpopular_recycleview_agree_click);
            mItem_mostpopular_recycleview_replay_content = (TextView) itemView.findViewById(R.id.item_mostpopular_recycleview_replay_content);
            mItem_mostpopular_recycleview_response_num = (TextView) itemView.findViewById(R.id.item_mostpopular_recycleview_response_num);
            mItem_mostpopular_recycleview_like_num = (TextView) itemView.findViewById(R.id.item_mostpopular_recycleview_like_num);
            mAuthor_msg_layout = (LinearLayout) itemView.findViewById(R.id.author_msg_layout);
            mResponse_msg_layout = (LinearLayout) itemView.findViewById(R.id.response_msg_layout);
            mAgree_click_layout = (LinearLayout) itemView.findViewById(R.id.agree_click_layout);

        }
    }
    public interface OnAgreeClickListener{
        void agreeclick(View itemView, TextView like_num, CustomSimpleDraweeView draweeView);
    }
    public void setOnAgreeClickListener(OnAgreeClickListener mOnAgreeClickListener){
        this.mOnAgreeClickListener = mOnAgreeClickListener;
    }

    public interface OnAuthorMessageClickListener{
        void authorClick();
    }
    public void setOnAuthorMessageClickListener(OnAuthorMessageClickListener mOnAuthorMessageClickListener){
        this.mOnAuthorMessageClickListener = mOnAuthorMessageClickListener;
    }

    public interface OnResponseMessageClickListener{
        void responseClick(CommentsInfo commentInfo);
    }
    public void setOnResponseMessageClickListener(OnResponseMessageClickListener mOnResponseMessageClickListener){
        this.mOnResponseMessageClickListener = mOnResponseMessageClickListener;
    }
}
