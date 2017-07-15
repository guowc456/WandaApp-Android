package com.wanta.mobile.wantaproject.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;

/**
 * Created by WangYongqiang on 2016/11/13.
 */
public class CustormLineLayout extends LinearLayout {

    private MyImageView mImageView;
    private TextView mTextView;
    private MyImageView mImageViewArrow;
    private TextView mCustom_linelayout_bottom_sum;
    private TextView mCustom_linelayout_center_sum;
    private MyImageView mCustom_linelayout_add;

    public CustormLineLayout(Context context) {
        super(context);
        init(context);
    }

    public CustormLineLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_linelayout_layout,this);
        mImageView = (MyImageView) this.findViewById(R.id.custom_linelayout_image);
        mTextView = (TextView) this.findViewById(R.id.custom_linelayout_text);
        mImageViewArrow = (MyImageView) this.findViewById(R.id.custom_linelayout_arrows);
        mCustom_linelayout_bottom_sum = (TextView) this.findViewById(R.id.custom_linelayout_bottom_sum);
        mCustom_linelayout_center_sum = (TextView) this.findViewById(R.id.custom_linelayout_center_sum);
        mCustom_linelayout_add = (MyImageView) this.findViewById(R.id.custom_linelayout_add);
    }

    /**
     * 设置1级界面的信息
     * @param imageId  设置衣服的图片
     * @param str  设置衣服的种类
     * @param bottomSum 设置衣服下面显示的衣服的件数
     * @param arrowIcon 设置箭头的图标
     */
    public void setFirstGrade(int imageId,String str,String bottomSum,int arrowIcon){
        mImageView.setVisibility(VISIBLE);
        mTextView.setVisibility(VISIBLE);
        mCustom_linelayout_bottom_sum.setVisibility(VISIBLE);
        mImageViewArrow.setVisibility(VISIBLE);
        mCustom_linelayout_center_sum.setVisibility(GONE);
        mCustom_linelayout_add.setVisibility(GONE);

        mImageView.setImageResource(imageId);
        mImageView.setSize(100,100);
        mTextView.setText(str);
        mCustom_linelayout_bottom_sum.setText(bottomSum);
        mImageViewArrow.setImageResource(arrowIcon);
        mImageViewArrow.setSize(30,30);

    }

    /**
     * 设置二级菜单
     * @param str 设置衣服的类别
     * @param centerSum 设置衣服类别后面的数量
     * @param addIcon 设置添加的按钮
     */
    public void setSecondGrade(String str,String centerSum,int addIcon){
        mImageView.setVisibility(GONE);
        mTextView.setVisibility(VISIBLE);
        mCustom_linelayout_bottom_sum.setVisibility(GONE);
        mImageViewArrow.setVisibility(GONE);
        mCustom_linelayout_center_sum.setVisibility(VISIBLE);
        mCustom_linelayout_add.setVisibility(VISIBLE);

        mTextView.setText(str);
        mTextView.setTextSize(16);
        mCustom_linelayout_center_sum.setText(centerSum);
        mCustom_linelayout_center_sum.setTextSize(16);
        mCustom_linelayout_add.setImageResource(addIcon);
        mCustom_linelayout_add.setSize(30,30);
    }

    /**
     * 设置三级界面
     * @param str 设置具体的衣服的类别
     * @param arrowIcon 设置下拉的箭头
     */
    public void setThreeGrade(String str,int arrowIcon){
        mImageView.setVisibility(GONE);
        mTextView.setVisibility(VISIBLE);
        mCustom_linelayout_bottom_sum.setVisibility(GONE);
        mImageViewArrow.setVisibility(VISIBLE);
        mCustom_linelayout_center_sum.setVisibility(GONE);
        mCustom_linelayout_add.setVisibility(GONE);

        mTextView.setText(str);
        mImageViewArrow.setImageResource(arrowIcon);
        mImageViewArrow.setSize(30,30);
    }
    /**
     * 设置图片资源
     */
    public void setImageReasource(int resId){
        mImageView.setImageResource(resId);
        mImageView.setSize(100,100);
    }
    /**
     * 设置衣服的类别
     */
    public void setTextViewText(String text){
        mTextView.setText(text);
    }
    /**
     * 设置衣服的类别下面的总件数
     */
    public void setTextBottomSum(String text){
        mCustom_linelayout_bottom_sum.setText(text);
    }
    /**
     * 设置衣服的类别后面的总件数
     */
    public void setTextCenterSum(String text){
        mCustom_linelayout_center_sum.setText(text);
    }
    /**
     * 设置箭头图标
     */
    public void setImageViewArrow(int resId){
        mImageView.setSize(50,50);
        mImageViewArrow.setImageResource(resId);
    }
    /**
     * 设置增加的图标
     */
    public void setImageViewAdd(int resId){
        mCustom_linelayout_add.setSize(50,50);
        mCustom_linelayout_add.setImageResource(resId);
    }
}
