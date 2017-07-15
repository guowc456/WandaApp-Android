package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.FirstItem;
import com.wanta.mobile.wantaproject.utils.Constants;

import java.util.List;

/**
 * Created by WangYongqiang on 2016/11/14.
 */
public class ExpandAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<FirstItem> firstList;
    private ExpandableListView.OnChildClickListener stvClickEvent;//外部回调函数
    private OnAddClickListener mOnAddClickListener = null ;
    private OnItemClickListener mOnItemClickListener = null ;
    private OnFirstItemClickListener mOnFirstItemClickListener = null ;
    // private List<SecondItem> secondList;

    public ExpandAdapter(Context context, List<FirstItem> firstList, ExpandableListView.OnChildClickListener stvClickEvent) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.firstList = firstList;
        this.stvClickEvent=stvClickEvent;
        // this.secondList = secondList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return firstList.get(groupPosition).getSecondItems();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ExpandableListView treeView = getExpandableListView(firstList.get(groupPosition).getSecondItems().size());
        ExpandAdapter2 expandAdapter2 = new ExpandAdapter2(context, firstList,
                childPosition, groupPosition,treeView,stvClickEvent);
        treeView.setAdapter(expandAdapter2);
        treeView.setPadding(140, 0, 0, 0);//设置二级菜单中字体距离左边框的距离
        //设置添加的点击事件
        expandAdapter2.setOnFirstAddClickListener(new ExpandAdapter2.OnFirstAddClickListener() {
            @Override
            public void clickFirstAddListener(View view) {
                mOnAddClickListener.clickAddListener(view);
            }
        });
        //设置条目的点击事件
        expandAdapter2.setOnThridItemClickListener(new ExpandAdapter2.OnThridItemClickListener() {
            @Override
            public void clickThridItemListener(int firstItem, int secondItem, int thridItem) {
                mOnItemClickListener.clickItemListener(firstItem,secondItem,thridItem);
            }
        });
//        treeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mOnFirstItemClickListener.clickFirstItemListener(groupPosition);
//            }
//        });
        return treeView;
    }

    /**
     * //设置二级界面的显示的宽度
     * @param position 某个一级界面中包含的二级条目的个数
     * @return
     */
    public ExpandableListView getExpandableListView(final int position) {
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, Constants.PHONE_HEIGHT/13*position);
        ExpandableListView superTreeView = new ExpandableListView(context);
        superTreeView.setLayoutParams(params);
        superTreeView.setDivider(null);
        superTreeView.setDividerHeight(0);
        return superTreeView;
    }

    @Override
    public int getChildrenCount(int position) {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public Object getGroup(int position) {
        // TODO Auto-generated method stub
        return firstList.get(position);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return firstList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public View getGroupView(final int position, boolean flag, View convertView,
                             ViewGroup group) {
        // TODO Auto-generated method stub
        FirstHolder holder = null;
        if (convertView == null) {
            holder = new FirstHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.first_lay, null);
            holder.first_arrow = (MyImageView) convertView
                    .findViewById(R.id.first_arrow);
            holder.first_image = (MyImageView) convertView
                    .findViewById(R.id.first_image);
            holder.first_title = (TextView) convertView
                    .findViewById(R.id.first_title);
            holder.first_sum = (TextView) convertView.findViewById(R.id.first_sum);
            convertView.setTag(holder);
        } else {
            holder = (FirstHolder) convertView.getTag();
        }
        FirstItem firstItem = firstList.get(position);
        holder.first_image.setSize(Constants.PHONE_WIDTH/7,Constants.PHONE_WIDTH/7);
        holder.first_image.setPadding(5,5,5,5);
        holder.first_image.setImageResource(firstItem.getImage());
        holder.first_title.setText(firstItem.getTitle());
        holder.first_title.setTextSize(18);
        holder.first_sum.setText(firstItem.getNumber());
        holder.first_sum.setTextSize(10);
        holder.first_arrow.setSize(Constants.PHONE_WIDTH/24,Constants.PHONE_WIDTH/24);
        if (flag){
            holder.first_arrow.setImageResource(R.mipmap.arrows_down);
        }else {
            holder.first_arrow.setImageResource(R.mipmap.arrows_up);
        }

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
    public interface OnAddClickListener{
        void clickAddListener(View view);
    }
    public void setOnAddClickListener(OnAddClickListener mOnAddClickListener){
        this.mOnAddClickListener = mOnAddClickListener;
    }
    public interface OnItemClickListener{
        void clickItemListener(int firstItem,int secondItem,int thridItem);
    }
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
    public interface OnFirstItemClickListener{
        void clickFirstItemListener(int firstItem);
    }
    public void setOnFirstItemClickListener(OnFirstItemClickListener mOnFirstItemClickListener){
        this.mOnFirstItemClickListener = mOnFirstItemClickListener;
    }
}

class FirstHolder {
    TextView first_title;
    TextView first_sum;
    MyImageView first_image;
    MyImageView first_arrow;
}

class SecondHolder {
    TextView second_title;
    ImageView second_arrow;
}

