package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.utils.Constants;

import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class WardoreSingleRecycleViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private String[] names = {
            "鞋子 > ","颜色 > ","品牌 > ","尺码：","价格：","购于：","日志/故事：","S/搭：","链接：","穿过"
    };
//    private String[] contents = {
//            "平底鞋  芭蕾鞋","green yellow","Dior","36","￥5300","香港 海港城","本来是买包包的","￥2850/搭：","",""
//    };
    private String[] contents;

    public WardoreSingleRecycleViewAdapter(Context mContext,String[] contents) {
        this.mContext = mContext;
        this.contents = contents;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.wardore_single_recycleview_adapter,parent,false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ItemViewHolder)holder).item_wardrobe_single_draweeview.setImageResource(R.mipmap.pink_line);
        ((ItemViewHolder)holder).item_wardrobe_single_draweeview.setWidth(Constants.PHONE_WIDTH/150);
        ((ItemViewHolder)holder).item_wardrobe_single_draweeview.setHeight(Constants.PHONE_WIDTH/10);
        ((ItemViewHolder)holder).item_wardrobe_single_name.setText(names[position]);
        if(position==9){
            ((ItemViewHolder)holder).item_wardrobe_single_content.setText(contents[position]+"次");
        }else {
            ((ItemViewHolder)holder).item_wardrobe_single_content.setText(contents[position]);
        }
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        private final TextView item_wardrobe_single_name;
        private final TextView item_wardrobe_single_content;
        private final CustomSimpleDraweeView item_wardrobe_single_draweeview;

        public ItemViewHolder(View itemView) {
            super(itemView);
            item_wardrobe_single_name = (TextView) itemView.findViewById(R.id.item_wardrobe_single_name);
            item_wardrobe_single_content = (TextView) itemView.findViewById(R.id.item_wardrobe_single_content);
            item_wardrobe_single_draweeview = (CustomSimpleDraweeView) itemView.findViewById(R.id.item_wardrobe_single_draweeview);
        }
    }

}
