package com.wanta.mobile.wantaproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.TagsDetailActivity;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/16.
 */
public class CameraModifyLinkFragment extends Fragment implements View.OnClickListener{

    private View view_link;
    private TextView mLink_bag;
    private TextView mLink_belt;
    private TextView mLink_hat;
    private TextView mLink_other;
    private TextView mLink_scarf;
    private TextView mLink_shoes;
    private TextView mLink_skirt;
    private TextView mLink_trousers;
    private TextView mLink_upper;
    private List<String> linkList = new ArrayList<>();
    private TextView mLink_confirm;
    private TextView mLink_cancle;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_link = inflater.inflate(R.layout.fragment_camera_modify_link,container,false);
        init();
        return view_link;
    }
    private void init() {
        mLink_bag = (TextView) view_link.findViewById(R.id.link_bag);
        mLink_belt = (TextView) view_link.findViewById(R.id.link_belt);
        mLink_hat = (TextView) view_link.findViewById(R.id.link_hat);
        mLink_other = (TextView) view_link.findViewById(R.id.link_other);
        mLink_scarf = (TextView) view_link.findViewById(R.id.link_scarf);
        mLink_shoes = (TextView) view_link.findViewById(R.id.link_shoes);
        mLink_skirt = (TextView) view_link.findViewById(R.id.link_skirt);
        mLink_trousers = (TextView) view_link.findViewById(R.id.link_trousers);
        mLink_upper = (TextView) view_link.findViewById(R.id.link_upper);
        mLink_confirm = (TextView) view_link.findViewById(R.id.link_confirm);
        mLink_cancle = (TextView) view_link.findViewById(R.id.link_cancle);
        mLink_bag.setOnClickListener(this);
        mLink_belt.setOnClickListener(this);
        mLink_hat.setOnClickListener(this);
        mLink_other.setOnClickListener(this);
        mLink_scarf.setOnClickListener(this);
        mLink_shoes.setOnClickListener(this);
        mLink_skirt.setOnClickListener(this);
        mLink_trousers.setOnClickListener(this);
        mLink_upper.setOnClickListener(this);
        mLink_confirm.setOnClickListener(this);
        mLink_cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.link_bag:
                setSelectTextView(mLink_bag);
                break;
            case R.id.link_belt:
                setSelectTextView(mLink_belt);
                break;
            case R.id.link_hat:
                setSelectTextView(mLink_hat);
                break;
            case R.id.link_other:
                setSelectTextView(mLink_other);
                break;
            case R.id.link_scarf:
                setSelectTextView(mLink_scarf);
                break;
            case R.id.link_shoes:
                setSelectTextView(mLink_shoes);
                break;
            case R.id.link_skirt:
                setSelectTextView(mLink_skirt);
                break;
            case R.id.link_trousers:
                setSelectTextView(mLink_trousers);
                break;
            case R.id.link_upper:
                setSelectTextView(mLink_upper);
                break;
            case R.id.link_confirm:
                setConfirmOperation();
                break;
            case R.id.link_cancle:
                setCancleOperation();
                break;
            default:
                break;
        }
    }
    public void setSelectTextView(TextView textView){
        if (textView.isSelected()){
            textView.setSelected(false);
            textView.setTextColor(getResources().getColor(R.color.main_btn_color));
            LogUtils.showVerbose("CameraModifyLinkFragment","没有被选中"+textView.getText());
            linkList.remove(textView.getText());
        }else {
            textView.setSelected(true);
            textView.setTextColor(getResources().getColor(R.color.white));
            LogUtils.showVerbose("CameraModifyLinkFragment","被选中了"+textView.getText());
            linkList.add(textView.getText()+"");
        }
    }
    public void setConfirmOperation(){
        Constants.LINK_LIST.clear();
        LogUtils.showVerbose("CameraModifyLinkFragment","确定");
        Constants.LINK_LIST = linkList;
    }
    public void setCancleOperation(){
        mLink_bag.setSelected(false);
        mLink_bag.setTextColor(getResources().getColor(R.color.main_btn_color));
        mLink_upper.setSelected(false);
        mLink_upper.setTextColor(getResources().getColor(R.color.main_btn_color));
        mLink_trousers.setSelected(false);
        mLink_trousers.setTextColor(getResources().getColor(R.color.main_btn_color));
        mLink_belt.setSelected(false);
        mLink_belt.setTextColor(getResources().getColor(R.color.main_btn_color));
        mLink_hat.setSelected(false);
        mLink_hat.setTextColor(getResources().getColor(R.color.main_btn_color));
        mLink_other.setSelected(false);
        mLink_other.setTextColor(getResources().getColor(R.color.main_btn_color));
        mLink_scarf.setSelected(false);
        mLink_scarf.setTextColor(getResources().getColor(R.color.main_btn_color));
        mLink_shoes.setSelected(false);
        mLink_shoes.setTextColor(getResources().getColor(R.color.main_btn_color));
        mLink_skirt.setSelected(false);
        mLink_skirt.setTextColor(getResources().getColor(R.color.main_btn_color));
        linkList.clear();
    }
}
