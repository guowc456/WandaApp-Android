package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.FirstItem;
import com.wanta.mobile.wantaproject.domain.SecondItem;
import com.wanta.mobile.wantaproject.domain.ThirdItem;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;

import java.util.List;

/**
 * Created by WangYongqiang on 2016/11/14.
 */
public class ExpandAdapter2 extends BaseExpandableListAdapter {

    private Context context;
    private List<FirstItem> firstList;

    private int cpostion;
    private int gposition;
    private ExpandableListView treeView;
    private ExpandableListView.OnChildClickListener stvClickEvent;//外部回调函数
    private int secondlength;
    private OnFirstAddClickListener mOnFirstAddClickListener = null;
    private OnThridItemClickListener mOnThridItemClickListener = null;
    private int second_item_height = Constants.PHONE_HEIGHT/13;
    private int thrid_item_height = Constants.PHONE_HEIGHT/13;


    public ExpandAdapter2(Context context, List<FirstItem> firstList,
                          int cposition, int gposition, ExpandableListView treeView,ExpandableListView.OnChildClickListener stvClickEvent) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.firstList = firstList;
        this.cpostion = cposition;
        this.gposition = gposition;
        this.treeView = treeView;
        this.stvClickEvent=stvClickEvent;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return firstList.get(gposition).getSecondItems().get(groupPosition)
                .getThirdItems().get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ThirdViewHolder childViewHolder = null;
        if (convertView == null) {
            childViewHolder = new ThirdViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.third_lay, null);
            childViewHolder.third_name = (TextView) convertView
                    .findViewById(R.id.third_name);
            childViewHolder.third_image = (MyImageView) convertView
                    .findViewById(R.id.third_image);
            childViewHolder.third_layout = (LinearLayout) convertView.findViewById(R.id.third_layout);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ThirdViewHolder) convertView.getTag();
        }
        ThirdItem thirdItem = firstList.get(gposition).getSecondItems()
                .get(groupPosition).getThirdItems().get(childPosition);
        childViewHolder.third_name.setText(thirdItem.getName());
        childViewHolder.third_name.setPadding(0,thrid_item_height/4,0,thrid_item_height/4);
        childViewHolder.third_image.setImageResource(R.mipmap.arrows_down);
        childViewHolder.third_image.setPadding(10,0,0,0);
        childViewHolder.third_image.setSize(Constants.PHONE_WIDTH/24,Constants.PHONE_WIDTH/24);
        //添加布局的高度
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(Constants.PHONE_WIDTH,thrid_item_height);
        childViewHolder.third_layout.setLayoutParams(params);
        //获取选中的内容
        treeView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView arg0, View arg1, int groupPosition,
                                        int childPosition, long arg4) {
                mOnThridItemClickListener.clickThridItemListener(gposition,groupPosition,childPosition);
                return false;
            }
        });
        treeView.setPadding(140, 0, 0, 0);//设置三级菜单中字体距离左边框的距离
        return convertView;
    }

    @Override
    public int getChildrenCount(int position) {
        // TODO Auto-generated method stub
        return firstList.get(gposition).getSecondItems().get(position)
                .getThirdItems().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return firstList.get(gposition).getSecondItems().get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return firstList.get(gposition).getSecondItems().size();
    }

    @Override
    public long getGroupId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getGroupView(int position, boolean arg1, View convertView,
                             ViewGroup arg3) {
        SecondViewHolder childViewHolder = null;
        if (convertView == null) {
            childViewHolder = new SecondViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.second_lay, null);
            childViewHolder.second_title = (TextView) convertView
                    .findViewById(R.id.second_title);
            childViewHolder.second_add = (MyImageView) convertView.findViewById(R.id.second_add);
            childViewHolder.second_add_layout =  (LinearLayout) convertView.findViewById(R.id.second_add_layout);
            childViewHolder.second_layout = (LinearLayout) convertView.findViewById(R.id.second_layout);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (SecondViewHolder) convertView.getTag();
        }
        final SecondItem secondItem = firstList.get(gposition).getSecondItems()
                .get(position);
        childViewHolder.second_title.setText(secondItem.getTitle());
        childViewHolder.second_add.setSize(Constants.PHONE_WIDTH/16,Constants.PHONE_WIDTH/16);
//        AbsListView.LayoutParams params = new AbsListView.LayoutParams(Constants.PHONE_WIDTH,second_item_height);
//        childViewHolder.second_layout.setLayoutParams(params);
        //设置添加的点击事件
        childViewHolder.second_add_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFirstAddClickListener.clickFirstAddListener(v);
            }
        });
        /***
         * 展开监听
         */
        treeView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int position) {
                // TODO Auto-generated method stub
                LogUtils.showVerbose("ExpandAdapter2","自己的大小"+treeView.getChildCount());
//                if (treeView.getChildCount() == firstList.get(gposition)
//                        .getSecondItems().size()) {
                    if (secondlength > 0) {
                        //当第二次点击二级菜单张开和收缩的时候会执行
                        secondlength += firstList.get(gposition)
                                .getSecondItems().get(position).getThirdItems()
                                .size()
                                * thrid_item_height;
                        LogUtils.showVerbose("ExpandAdapter2","111");
                    } else {
                        //当第一次点击二级菜单的时候会执行
                        secondlength += firstList.get(gposition)
                                .getSecondItems().size()
                                * second_item_height
                                + firstList.get(gposition).getSecondItems()
                                .get(position).getThirdItems().size()
                                * thrid_item_height;
                        LogUtils.showVerbose("ExpandAdapter2","222");
                    }
                treeView.setDividerHeight(0);
                treeView.setDivider(null);
//                } else {
//                    LogUtils.showVerbose("ExpandAdapter2","333");
//                    secondlength += firstList.get(gposition).getSecondItems()
//                            .get(position).getThirdItems().size()
//                            * Constants.THIRD_ITEM_HEIGHT;
//                }
//                if(secondlength>0){
//
//                }
//                secondlength = secondlength + (firstList.get(gposition)
//                        .getSecondItems().size())
//                        * Constants.SECOND_ITEM_HEIGHT
//                        + (firstList.get(gposition).getSecondItems()
//                        .get(position).getThirdItems().size())
//                        * Constants.THIRD_ITEM_HEIGHT;
                LogUtils.showVerbose("ExpandAdapter2","总的大小"+secondlength);
                for (int i=0;i<firstList.get(gposition).getSecondItems().size();i++){
                    LogUtils.showVerbose("ExpandAdapter2","二级目录:"+ second_item_height);
                }
                for (int i=0;i<firstList.get(gposition).getSecondItems().get(position).getThirdItems().size();i++){
                    LogUtils.showVerbose("ExpandAdapter2","三级目录:"+firstList.get(gposition).getSecondItems().get(position).getThirdItems().size()*thrid_item_height);
                }

                LogUtils.showVerbose("ExpandAdapter2","secondlength:"+(secondlength)+" 二级高度："+firstList.get(gposition)
                        .getSecondItems().toString()+"三级高度："+firstList.get(gposition).getSecondItems()
                        .get(position).getThirdItems().toString());
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, secondlength);
                treeView.setLayoutParams(lp);
            }
        });
        /***
         * 缩放监听
         */
        treeView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int position) {
                // TODO Auto-generated method stub

                secondlength -= firstList.get(gposition).getSecondItems()
                        .get(position).getThirdItems().size()
                        * thrid_item_height;
                LogUtils.showVerbose("ExpandAdapter2","缩放secondlength"+secondlength+"第三个长度："+firstList.get(gposition).getSecondItems()
                        .get(position).getThirdItems().size()
                        * thrid_item_height);
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, secondlength);
                treeView.setLayoutParams(lp);
            }
        });
        treeView.setPadding(140, 0, 0, 0);//设置二级菜单中字体距离左边框的距离
        treeView.setGroupIndicator(null);//设置向下的箭头为空
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return true;
    }

    class ThirdViewHolder {
        MyImageView third_image;
        TextView third_name;
        LinearLayout third_layout ;
    }

    class SecondViewHolder {
        TextView second_title;
        MyImageView second_add;
        LinearLayout second_layout ;
        LinearLayout second_add_layout;
    }
    public interface OnFirstAddClickListener{
        void clickFirstAddListener(View view);
    }
    public void setOnFirstAddClickListener(OnFirstAddClickListener mOnFirstAddClickListener){
        this.mOnFirstAddClickListener = mOnFirstAddClickListener;
    }
    public interface OnThridItemClickListener{
        void clickThridItemListener(int firstItem,int secondItem,int thridItem);
    }
    public void setOnThridItemClickListener(OnThridItemClickListener mOnThridItemClickListener){
        this.mOnThridItemClickListener = mOnThridItemClickListener;
    }
}
