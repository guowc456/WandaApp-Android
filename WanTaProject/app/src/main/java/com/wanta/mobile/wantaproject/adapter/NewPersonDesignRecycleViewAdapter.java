package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.utils.Constants;

import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class NewPersonDesignRecycleViewAdapter extends RecyclerView.Adapter {

    private Context mContext ;
    private String[] nameList;
    private int catogry;
    private String[] body_description = new String[]{
            "","头顶到脚底","裸体体重","平时穿的内衣尺码","平时穿的内衣型号","胸部最粗处一圈","RF底部的一圈","大臀最粗的一圈","站直挺胸两肩头连线"
    };
    private String[] waist_description = new String[]{
            "锁骨到脚面","腋下到肩头的一圈","锁骨-乳头-胸底围-肚脐","伸直手臂，肩头到手腕(不是从手下开始哦)","肚脐到大腿分叉","站直，肚脐到脚踝(脚踝骨头尖)",
    };
    private String[] head_description = new String[]{
            "头部最粗一圈","颈根部四点连线(最粗的位置)","肚脐一圈","臀部最粗一圈","大腿最粗一圈",
    };
    private OnNewPersonDesignClcik mOnNewPersonDesignClcik = null;

    public NewPersonDesignRecycleViewAdapter(Context mContext ,String[] nameList,int catogry) {
        this.mContext = mContext;
        this.nameList = nameList;
        this.catogry = catogry;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.new_person_design_recycleview_adapter,parent,false);
        PersonDisignViewHolder viewHolder = new PersonDisignViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (catogry==1){
            ((PersonDisignViewHolder)holder).mNew_person_design_content_description.setVisibility(View.VISIBLE);
            if ("1900-1-1".equals(Constants.often_description[position])){
                ((PersonDisignViewHolder)holder).mNew_person_design_content_description.setText("");
            }else {
                ((PersonDisignViewHolder)holder).mNew_person_design_content_description.setText(Constants.often_description[position]);
            }
            ((PersonDisignViewHolder)holder).mNew_person_design_content_name.setText(Constants.often_array[position]);
            if (position==1||position==2||position==3){
                ((PersonDisignViewHolder)holder).mNew_person_design_must_select.setVisibility(View.VISIBLE);
            }else {
                ((PersonDisignViewHolder)holder).mNew_person_design_must_select.setVisibility(View.GONE);
            }
        }else if (catogry==2){
            ((PersonDisignViewHolder)holder).mNew_person_design_content_description.setVisibility(View.VISIBLE);
            ((PersonDisignViewHolder)holder).mNew_person_design_content_description.setText(Constants.body_description[position]);
            ((PersonDisignViewHolder)holder).mNew_person_design_content_name.setText(Constants.body_array[position]);
        }else if (catogry==3){
            ((PersonDisignViewHolder)holder).mNew_person_design_must_select.setVisibility(View.GONE);
            ((PersonDisignViewHolder)holder).mNew_person_design_content_description.setVisibility(View.VISIBLE);
            ((PersonDisignViewHolder)holder).mNew_person_design_content_description.setText(Constants.waist_description[position]);
            ((PersonDisignViewHolder)holder).mNew_person_design_content_name.setText(Constants.waist_array[position]);
        }else if (catogry==4){
            ((PersonDisignViewHolder)holder).mNew_person_design_must_select.setVisibility(View.GONE);
            ((PersonDisignViewHolder)holder).mNew_person_design_content_description.setVisibility(View.VISIBLE);
            ((PersonDisignViewHolder)holder).mNew_person_design_content_description.setText(Constants.head_description[position]);
            ((PersonDisignViewHolder)holder).mNew_person_design_content_name.setText(Constants.head_array[position]);
        }else if (catogry==5){
            ((PersonDisignViewHolder)holder).mNew_person_design_must_select.setVisibility(View.GONE);
            ((PersonDisignViewHolder)holder).mNew_person_design_content_description.setVisibility(View.VISIBLE);
            ((PersonDisignViewHolder)holder).mNew_person_design_content_description.setText(Constants.back_description[position]);
            ((PersonDisignViewHolder)holder).mNew_person_design_content_name.setText(Constants.back_array[position]);
        }
        if (position==0&&catogry==2){
            ((PersonDisignViewHolder)holder).mNew_person_design_must_select.setImageResource(R.mipmap.body_msg_icon1);
            ((PersonDisignViewHolder)holder).mNew_person_design_must_select.setSize(Constants.PHONE_WIDTH/18,Constants.PHONE_WIDTH/18);
        }else {
            ((PersonDisignViewHolder)holder).mNew_person_design_must_select.setImageResource(R.mipmap.must_select);
            ((PersonDisignViewHolder)holder).mNew_person_design_must_select.setSize(Constants.PHONE_WIDTH/50,Constants.PHONE_WIDTH/50);
        }
//        ((PersonDisignViewHolder)holder).mNew_person_design_content_name.setText(nameList[position]);
//        ((PersonDisignViewHolder)holder).mNew_person_design_content_description.setText(nameList[position]);
        ((PersonDisignViewHolder)holder).mNew_person_design_next_icon.setSize(Constants.PHONE_WIDTH/16,Constants.PHONE_WIDTH/16);
    }

    @Override
    public int getItemCount() {
        return nameList.length;
    }

    class PersonDisignViewHolder extends RecyclerView.ViewHolder{

        private final TextView mNew_person_design_content_name;
        private final MyImageView mNew_person_design_must_select;
        private final TextView mNew_person_design_content_description;
        private final MyImageView mNew_person_design_next_icon;

        public PersonDisignViewHolder(View itemView) {
            super(itemView);
            mNew_person_design_content_name = (TextView) itemView.findViewById(R.id.new_person_design_content_name);
            mNew_person_design_must_select = (MyImageView) itemView.findViewById(R.id.new_person_design_must_select);
            mNew_person_design_content_description = (TextView) itemView.findViewById(R.id.new_person_design_content_description);
            mNew_person_design_next_icon = (MyImageView) itemView.findViewById(R.id.new_person_design_next_icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnNewPersonDesignClcik.onItemClick(v);
                }
            });
        }
    }

    public interface OnNewPersonDesignClcik{
        void onItemClick(View view);
    }

    public void setOnNewPersonDesignClcik(OnNewPersonDesignClcik mOnNewPersonDesignClcik){
        this.mOnNewPersonDesignClcik = mOnNewPersonDesignClcik;
    }

}
