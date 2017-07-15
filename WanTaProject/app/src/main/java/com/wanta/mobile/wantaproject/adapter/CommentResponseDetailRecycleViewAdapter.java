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
import com.wanta.mobile.wantaproject.domain.ResponseInfo;
import com.wanta.mobile.wantaproject.utils.Constants;

import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class CommentResponseDetailRecycleViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<ResponseInfo> mList;
    private OnAuthorMessageClickListener mOnAuthorMessageClickListener = null;
    private OnResponseMessageClickListener mOnResponseMessageClickListener = null;

    public CommentResponseDetailRecycleViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setShowNum(List<ResponseInfo> mList){
        this.mList = mList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comments_response_detail_recycleview_adapter,parent,false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ResponseInfo responseInfo = mList.get(position);
        ((ItemViewHolder)holder).response_detail__author.setText(responseInfo.getRp_username());
        ((ItemViewHolder)holder).response_detail__date.setText(responseInfo.getCreatedat());
        ((ItemViewHolder)holder).response_detail__replay_content.setText("回复了"+responseInfo.getBe_rp_username()+": "+responseInfo.getRpcontent());
        ((ItemViewHolder)holder).response_detail_author_icon.setWidth(Constants.PHONE_WIDTH / 12);
        ((ItemViewHolder)holder).response_detail_author_icon.setHeight(Constants.PHONE_WIDTH / 12);
        if ("null".equals(responseInfo.getRp_avatar())){
            ((ItemViewHolder)holder).response_detail_author_icon.setImageResource(R.mipmap.icon);
        }else {
            ((ItemViewHolder)holder).response_detail_author_icon.setImageURI(Uri.parse(Constants.FIRST_PAGE_IMAGE_URL + responseInfo.getRp_avatar()));
        }
        ((ItemViewHolder)holder).response_detail_author_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnAuthorMessageClickListener.authorClick();
            }
        });
        ((ItemViewHolder)holder).response_detail_response_msg_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnResponseMessageClickListener.responseClick(responseInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        private final CustomSimpleDraweeView response_detail_author_icon;
        private final TextView response_detail__author;
        private final TextView response_detail__date;
        private final TextView response_detail__replay_content;
        private final LinearLayout response_detail_author_layout;
        private final LinearLayout response_detail_response_msg_layout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            response_detail_author_icon = (CustomSimpleDraweeView) itemView.findViewById(R.id.response_detail_author_icon);
            response_detail__author = (TextView) itemView.findViewById(R.id.response_detail__author);
            response_detail__date = (TextView) itemView.findViewById(R.id.response_detail__date);
            response_detail__replay_content = (TextView) itemView.findViewById(R.id.response_detail__replay_content);
            response_detail_author_layout = (LinearLayout) itemView.findViewById(R.id.response_detail_author_layout);
            response_detail_response_msg_layout = (LinearLayout) itemView.findViewById(R.id.response_detail_response_msg_layout);

        }
    }

    public interface OnAuthorMessageClickListener{
        void authorClick();
    }
    public void setOnAuthorMessageClickListener(OnAuthorMessageClickListener mOnAuthorMessageClickListener){
        this.mOnAuthorMessageClickListener = mOnAuthorMessageClickListener;
    }

    public interface OnResponseMessageClickListener{
        void responseClick(ResponseInfo responseInfo);
    }
    public void setOnResponseMessageClickListener(OnResponseMessageClickListener mOnResponseMessageClickListener){
        this.mOnResponseMessageClickListener = mOnResponseMessageClickListener;
    }
}
