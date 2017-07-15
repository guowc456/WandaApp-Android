package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.domain.CustomGroupInfo;
import com.wanta.mobile.wantaproject.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/23.
 */

public class CustomRecycleviewAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<CustomGroupInfo> groupList = new ArrayList<>();//层次数据
    private int TYPE_EVEN_NUMBER = 0;//为偶数
    private int TYPE_ODD_NUMBER = 1;//为奇数
    private int currentPosition = 0;//记录当前的带你极爱位置
    private OnCustomGroupItemClickListener mOnCustomGroupItemClickListener = null;
    private OnCustomArrowsClickListener mOnCustomArrowsClickListener = null;
    private OnCustomClothCatogryClickListener mOnCustomClothCatogryClickListener = null;
    private OnCustomAddIconClickListener mOnCustomAddIconClickListener = null;
    private OnCustomClothAttributeClickListener mOnCustomClothAttributeClickListener = null;
    private TextView custom_child_upper_color;
    private TextView custom_child_upper_length;
    private TextView custom_child_upper_temper;
    private TextView custom_child_trouser_color;
    private TextView custom_child_trouser_lianshenku;
    private TextView custom_child_trouser_kuzhuang;
    private TextView custom_child_trouser_length;
    private TextView custom_child_trouser_kuxing;
    private TextView custom_child_skirts_color;
    private TextView custom_child_skirts_banshenku;
    private TextView custom_child_skirts_lianyiqun;
    private TextView custom_child_skirts_length;
    private TextView custom_child_skirts_temper;
    private TextView custom_child_hat_color;
    private TextView custom_child_shoe_color;
    private TextView custom_child_shoe_xiegen;
    private TextView custom_child_shoe_qingbian;
    private TextView custom_child_shoe_yichuan;
    private TextView custom_child_scarf_color;
    private TextView custom_child_scarf_size;
    private TextView custom_child_scarf_temper;
    private TextView custom_child_waist_color;
    private TextView custom_child_waist_kuanzai;
    private TextView custom_child_bag_color;
    private TextView custom_child_bag_size;
    private TextView custom_child_bag_xingbian;
    private TextView custom_child_upper_all_number;
    private TextView custom_child_trouser_all_number;
    private TextView custom_child_skirts_all_number;
    private TextView custom_child_hat_all_number;
    private TextView custom_child_shoe_all_number;
    private TextView custom_child_scarf_all_number;
    private TextView custom_child_waist_all_number;
    private TextView custom_child_bag_all_number;

    public CustomRecycleviewAdapter(Context context,List<CustomGroupInfo> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TYPE_EVEN_NUMBER){
            //为偶数,父目录
            return new CutomGroupRecycleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_custom_group_recycleview,parent,false));
        }else if (viewType==TYPE_ODD_NUMBER){
            return new CutomChildRecycleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_custom_child_recycleview,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CutomGroupRecycleViewHolder){
            //设置箭头的点击事件
            ((CutomGroupRecycleViewHolder)holder).new_wardrobe_group_arrows_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    groupList.get(position).setGroupExpend(!groupList.get(position).isGroupExpend());
                    currentPosition = position;
                    mOnCustomGroupItemClickListener.onItemClick(v,groupList.get(position),currentPosition);
                    mOnCustomArrowsClickListener.onItemClick(v,position);
                    notifyDataSetChanged();
                }
            });
            //设置衣服类别的点击事件
            ((CutomGroupRecycleViewHolder)holder).new_wardrobe_group_head_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnCustomClothCatogryClickListener.onItemClick(v,position);
                }
            });
            ((CutomGroupRecycleViewHolder)holder).new_wardrobe_group_cloth_catory.setText(groupList.get(position).getClothName());
            ((CutomGroupRecycleViewHolder)holder).new_wardrobe_group_cloth_number.setText(groupList.get(position).getClothNumber());
            ((CutomGroupRecycleViewHolder)holder).new_wardrobe_group_icon.setImageResource(groupList.get(position).getIconUrl());
            if (position==0){
                ((CutomGroupRecycleViewHolder)holder).new_wardrobe_group_cloth_number.setVisibility(View.GONE);
                ((CutomGroupRecycleViewHolder)holder).new_wardrobe_group_arrows.setVisibility(View.GONE);
            }else {
                ((CutomGroupRecycleViewHolder)holder).new_wardrobe_group_cloth_number.setVisibility(View.VISIBLE);
                ((CutomGroupRecycleViewHolder)holder).new_wardrobe_group_arrows.setVisibility(View.VISIBLE);
            }
            if (groupList.get(position).isGroupExpend()==true){
                ((CutomGroupRecycleViewHolder)holder).new_wardrobe_group_arrows.setImageResource(R.mipmap.arrows_up);
            }else {
                ((CutomGroupRecycleViewHolder)holder).new_wardrobe_group_arrows.setImageResource(R.mipmap.arrows_down);
            }
        }else if (holder instanceof CutomChildRecycleViewHolder){
            if (position-1==0){
                //确保万搭日历没有下拉界面
                ((CutomChildRecycleViewHolder)holder).item_custom_child_recycle_layout.setVisibility(View.GONE);
            }else {
                if (groupList.get(position-1).isGroupExpend()==false){
                    ((CutomChildRecycleViewHolder)holder).item_custom_child_recycle_layout.setVisibility(View.GONE);
                }else {
                    ((CutomChildRecycleViewHolder)holder).item_custom_child_recycle_layout.removeAllViews();
                    ((CutomChildRecycleViewHolder)holder).item_custom_child_recycle_layout.setVisibility(View.VISIBLE);
                    if (position-1==2){
                        //添加的是上衣
                        showUpperMsg(((CutomChildRecycleViewHolder)holder));
                    }else if (position-1==4){
                        //添加的是裤子
                        showTrouserMsg(((CutomChildRecycleViewHolder)holder));
                    }else if (position-1==6){
                        showSkirtsMsg(((CutomChildRecycleViewHolder)holder));
                    }else if (position-1==8){
                        showHatMsg(((CutomChildRecycleViewHolder)holder));
                    }else if (position-1==10){
                        showShoeMsg(((CutomChildRecycleViewHolder)holder));
                    }else if (position-1==12){
                        showScarfMsg(((CutomChildRecycleViewHolder)holder));
                    }else if (position-1==14){
                        showWaistMsg(((CutomChildRecycleViewHolder)holder));
                    }else if (position-1==16){
                        showBagMsg(((CutomChildRecycleViewHolder)holder));
                    }

                }
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position%2==0){
            return TYPE_EVEN_NUMBER;
        }else {
            return TYPE_ODD_NUMBER;
        }
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class CutomGroupRecycleViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout item_custom_group_recycle_layout;
        private final CustomSimpleDraweeView new_wardrobe_group_icon;
        private final CustomSimpleDraweeView new_wardrobe_group_arrows;
        private final TextView item_custom_group_recycle_tv;
        private final LinearLayout new_wardrobe_group_arrows_layout;
        private final LinearLayout new_wardrobe_group_head_layout;
        private final TextView new_wardrobe_group_cloth_catory;
        private final TextView new_wardrobe_group_cloth_number;

        public CutomGroupRecycleViewHolder(View itemView) {
            super(itemView);
            item_custom_group_recycle_tv = (TextView) itemView.findViewById(R.id.item_custom_group_recycle_tv);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH,Constants.PHONE_HEIGHT/12);
            item_custom_group_recycle_layout = (LinearLayout) itemView.findViewById(R.id.item_custom_group_recycle_layout);
            item_custom_group_recycle_layout.setLayoutParams(params);
            new_wardrobe_group_icon = (CustomSimpleDraweeView) itemView.findViewById(R.id.new_wardrobe_group_icon);
            new_wardrobe_group_icon.setWidth(Constants.PHONE_WIDTH/8);
            new_wardrobe_group_icon.setHeight(Constants.PHONE_WIDTH/8);
            new_wardrobe_group_arrows = (CustomSimpleDraweeView) itemView.findViewById(R.id.new_wardrobe_group_arrows);
            new_wardrobe_group_arrows.setWidth(Constants.PHONE_WIDTH/14);
            new_wardrobe_group_arrows.setHeight(Constants.PHONE_WIDTH/14);
            new_wardrobe_group_arrows_layout = (LinearLayout) itemView.findViewById(R.id.new_wardrobe_group_arrows_layout);
            new_wardrobe_group_head_layout = (LinearLayout) itemView.findViewById(R.id.new_wardrobe_group_head_layout);
            new_wardrobe_group_cloth_catory = (TextView) itemView.findViewById(R.id.new_wardrobe_group_cloth_catory);
            new_wardrobe_group_cloth_number = (TextView) itemView.findViewById(R.id.new_wardrobe_group_cloth_number);
        }
    }

    public class CutomChildRecycleViewHolder extends RecyclerView.ViewHolder {

        private final TextView custom_child_recycle_tv;
        private final LinearLayout item_custom_child_recycle_layout;

        public CutomChildRecycleViewHolder(View itemView) {
            super(itemView);
            custom_child_recycle_tv = (TextView) itemView.findViewById(R.id.custom_child_recycle_tv);
            item_custom_child_recycle_layout = (LinearLayout) itemView.findViewById(R.id.item_custom_child_recycle_layout);
        }
    }

    public interface OnCustomGroupItemClickListener {
        void onItemClick(View groupView, CustomGroupInfo groupInfo, int groupPosition);
    }

    public void setOnCustomGroupItemClickListener(OnCustomGroupItemClickListener mOnCustomGroupItemClickListener){
        this.mOnCustomGroupItemClickListener = mOnCustomGroupItemClickListener;
    }
    //箭头的接口
    public interface OnCustomArrowsClickListener {
        void onItemClick(View view, int groupPosition);
    }

    public void setOnCustomArrowsClickListener(OnCustomArrowsClickListener mOnCustomArrowsClickListener){
        this.mOnCustomArrowsClickListener = mOnCustomArrowsClickListener;
    }
    //衣服类别的接口
    public interface OnCustomClothCatogryClickListener {
        void onItemClick(View view, int groupPosition);
    }

    public void setOnCustomClothCatogryClickListener(OnCustomClothCatogryClickListener mOnCustomClothCatogryClickListener){
        this.mOnCustomClothCatogryClickListener = mOnCustomClothCatogryClickListener;
    }
    //衣服上衣添加按钮的接口
    public interface OnCustomAddIconClickListener {
        void onItemClick(int position);
    }

    public void setOnCustomAddIconClickListener(OnCustomAddIconClickListener mOnCustomAddIconClickListener){
        this.mOnCustomAddIconClickListener = mOnCustomAddIconClickListener;
    }
    //衣服上衣品类的接口
    public interface OnCustomClothAttributeClickListener {
        void onItemClick(int groupPosition, int childPosition);
    }

    public void setOnCustomClothAttributeClickListener(OnCustomClothAttributeClickListener  mOnCustomClothAttributeClickListener){
        this.mOnCustomClothAttributeClickListener = mOnCustomClothAttributeClickListener;
    }
    //设置上衣的相关事件
    public void showUpperMsg(CutomChildRecycleViewHolder holder){
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_custom_child_item_upper_layout,null);
        holder.item_custom_child_recycle_layout.addView(view);
        CustomSimpleDraweeView custom_child_upper_add_icon = (CustomSimpleDraweeView) (holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_upper_add_icon));
        custom_child_upper_add_icon.setWidth(Constants.PHONE_WIDTH/12);
        custom_child_upper_add_icon.setHeight(Constants.PHONE_WIDTH/12);
        custom_child_upper_all_number = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_upper_all_number);
        custom_child_upper_all_number.setText("全部上衣"+Constants.Cloth_catogry_number[0]+"件");
        custom_child_upper_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomAddIconClickListener.onItemClick(2);
            }
        });
        custom_child_upper_color = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_upper_color);
        custom_child_upper_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(2,0);
            }
        });
        custom_child_upper_length = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_upper_length);
        custom_child_upper_length.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(2,1);
            }
        });
        custom_child_upper_temper = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_upper_temper);
        custom_child_upper_temper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(2, 2);
            }
        });
    }
    //设置裤子的相关事件
    public void showTrouserMsg(CutomChildRecycleViewHolder holder){
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_custom_child_item_trouser_layout,null);
        holder.item_custom_child_recycle_layout.addView(view);
        CustomSimpleDraweeView custom_child_trouser_add_icon = (CustomSimpleDraweeView) (holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_trouser_add_icon));
        custom_child_trouser_add_icon.setWidth(Constants.PHONE_WIDTH/12);
        custom_child_trouser_add_icon.setHeight(Constants.PHONE_WIDTH/12);
        custom_child_trouser_all_number = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_trouser_all_number);
        custom_child_trouser_all_number.setText("全部裤子"+Constants.Cloth_catogry_number[1]+"件");
        custom_child_trouser_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomAddIconClickListener.onItemClick(4);
            }
        });
        custom_child_trouser_color = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_trouser_color);
        custom_child_trouser_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(4, 0);
            }
        });
        custom_child_trouser_lianshenku = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_trouser_lianshenku);
        custom_child_trouser_lianshenku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(4, 1);
            }
        });
        custom_child_trouser_kuzhuang = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_trouser_kuzhuang);
        custom_child_trouser_kuzhuang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(4, 2);
            }
        });
        custom_child_trouser_length = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_trouser_length);
        custom_child_trouser_length.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(4, 3);
            }
        });
        custom_child_trouser_kuxing = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_trouser_kuxing);
        custom_child_trouser_kuxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(4, 4);
            }
        });
    }
    //设置裙子的相关事件
    public void showSkirtsMsg(CutomChildRecycleViewHolder holder){
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_custom_child_item_skirts_layout,null);
        holder.item_custom_child_recycle_layout.addView(view);
        CustomSimpleDraweeView custom_child_skirts_add_icon = (CustomSimpleDraweeView) (holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_skirts_add_icon));
        custom_child_skirts_add_icon.setWidth(Constants.PHONE_WIDTH/12);
        custom_child_skirts_add_icon.setHeight(Constants.PHONE_WIDTH/12);
        custom_child_skirts_all_number = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_skirts_all_number);
        custom_child_skirts_all_number.setText("全部裙子"+Constants.Cloth_catogry_number[2]+"件");
        custom_child_skirts_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomAddIconClickListener.onItemClick(6);
            }
        });
        custom_child_skirts_color = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_skirts_color);
        custom_child_skirts_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(6, 0);
            }
        });
        custom_child_skirts_banshenku = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_skirts_banshenku);
        custom_child_skirts_banshenku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(6, 1);
            }
        });
        custom_child_skirts_lianyiqun = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_skirts_lianyiqun);
        custom_child_skirts_lianyiqun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(6, 2);
            }
        });
        custom_child_skirts_length = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_skirts_length);
        custom_child_skirts_length.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(6, 3);
            }
        });
        custom_child_skirts_temper = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_skirts_temper);
        custom_child_skirts_temper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(6, 4);
            }
        });
    }
    //设置帽子的相关事件
    public void showHatMsg(CutomChildRecycleViewHolder holder){
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_custom_child_item_hat_layout,null);
        holder.item_custom_child_recycle_layout.addView(view);
        CustomSimpleDraweeView custom_child_hat_add_icon = (CustomSimpleDraweeView) (holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_hat_add_icon));
        custom_child_hat_add_icon.setWidth(Constants.PHONE_WIDTH/12);
        custom_child_hat_add_icon.setHeight(Constants.PHONE_WIDTH/12);
        custom_child_hat_all_number = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_hat_all_number);
        custom_child_hat_all_number.setText("全部帽子"+Constants.Cloth_catogry_number[3]+"件");
        custom_child_hat_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomAddIconClickListener.onItemClick(8);
            }
        });
        custom_child_hat_color = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_hat_color);
        custom_child_hat_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(8, 0);
            }
        });

    }
    //设置鞋子的相关事件
    public void showShoeMsg(CutomChildRecycleViewHolder holder){
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_custom_child_item_shoe_layout,null);
        holder.item_custom_child_recycle_layout.addView(view);
        CustomSimpleDraweeView custom_child_shoe_add_icon = (CustomSimpleDraweeView) (holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_shoe_add_icon));
        custom_child_shoe_add_icon.setWidth(Constants.PHONE_WIDTH/12);
        custom_child_shoe_add_icon.setHeight(Constants.PHONE_WIDTH/13);
        custom_child_shoe_all_number = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_shoe_all_number);
        custom_child_shoe_all_number.setText("全部鞋子"+Constants.Cloth_catogry_number[4]+"件");
        custom_child_shoe_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomAddIconClickListener.onItemClick(10);
            }
        });
        custom_child_shoe_color = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_shoe_color);
        custom_child_shoe_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(10, 0);
            }
        });
        custom_child_shoe_xiegen = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_shoe_xiegen);
        custom_child_shoe_xiegen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(10, 1);
            }
        });
        custom_child_shoe_qingbian = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_shoe_qingbian);
        custom_child_shoe_qingbian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(10, 2);
            }
        });
        custom_child_shoe_yichuan = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_shoe_yichuan);
        custom_child_shoe_yichuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(10, 3);
            }
        });
    }
    //设置围巾的相关事件
    public void showScarfMsg(CutomChildRecycleViewHolder holder){
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_custom_child_item_scarf_layout,null);
        holder.item_custom_child_recycle_layout.addView(view);
        CustomSimpleDraweeView custom_child_scarf_add_icon = (CustomSimpleDraweeView) (holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_scarf_add_icon));
        custom_child_scarf_add_icon.setWidth(Constants.PHONE_WIDTH/12);
        custom_child_scarf_add_icon.setHeight(Constants.PHONE_WIDTH/12);
        custom_child_scarf_all_number = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_scarf_all_number);
        custom_child_scarf_all_number.setText("全部围巾"+Constants.Cloth_catogry_number[5]+"件");
        custom_child_scarf_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomAddIconClickListener.onItemClick(12);
            }
        });
        custom_child_scarf_color = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_scarf_color);
        custom_child_scarf_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(12, 0);
            }
        });
        custom_child_scarf_size = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_scarf_size);
        custom_child_scarf_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(12, 1);
            }
        });
        custom_child_scarf_temper = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_scarf_temper);
        custom_child_scarf_temper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(12, 2);
            }
        });
    }
    //设置腰带的相关事件
    public void showWaistMsg(CutomChildRecycleViewHolder holder){
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_custom_child_item_waist_layout,null);
        holder.item_custom_child_recycle_layout.addView(view);
        CustomSimpleDraweeView custom_child_waist_add_icon = (CustomSimpleDraweeView) (holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_waist_add_icon));
        custom_child_waist_add_icon.setWidth(Constants.PHONE_WIDTH/12);
        custom_child_waist_add_icon.setHeight(Constants.PHONE_WIDTH/12);
        custom_child_waist_all_number = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_waist_all_number);
        custom_child_waist_all_number.setText("全部腰带"+Constants.Cloth_catogry_number[6]+"件");
        custom_child_waist_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomAddIconClickListener.onItemClick(14);
            }
        });
        custom_child_waist_color = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_waist_color);
        custom_child_waist_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(14, 0);
            }
        });
        custom_child_waist_kuanzai = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_waist_kuanzai);
        custom_child_waist_kuanzai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(14, 1);
            }
        });
    }
    //设置包的相关事件
    public void showBagMsg(CutomChildRecycleViewHolder holder){
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_custom_child_item_bag_layout,null);
        holder.item_custom_child_recycle_layout.addView(view);
        CustomSimpleDraweeView custom_child_bag_add_icon = (CustomSimpleDraweeView) (holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_bag_add_icon));
        custom_child_bag_add_icon.setWidth(Constants.PHONE_WIDTH/12);
        custom_child_bag_add_icon.setHeight(Constants.PHONE_WIDTH/12);
        custom_child_bag_all_number = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_bag_all_number);
        custom_child_bag_all_number.setText("全部包"+Constants.Cloth_catogry_number[7]+"件");
        custom_child_bag_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomAddIconClickListener.onItemClick(16);
            }
        });
        custom_child_bag_color = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_bag_color);
        custom_child_bag_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(16, 0);
            }
        });
        custom_child_bag_size = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_bag_size);
        custom_child_bag_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(16, 1);
            }
        });
        custom_child_bag_xingbian = (TextView) holder.item_custom_child_recycle_layout.findViewById(R.id.custom_child_bag_xingbian);
        custom_child_bag_xingbian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomClothAttributeClickListener.onItemClick(16, 2);
            }
        });
    }
}
