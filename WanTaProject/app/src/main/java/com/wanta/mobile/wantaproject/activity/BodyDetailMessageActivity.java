package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.MostPopularFragmentAdapter;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.CacheDataUtils;
import com.wanta.mobile.wantaproject.uploadimage.BigImageScanActivity;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.DealReturnLogicUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;
import com.wanta.mobile.wantaproject.utils.NetUtils;
import com.wanta.mobile.wantaproject.utils.SimpleDraweeControlUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2017/3/9.
 */
public class BodyDetailMessageActivity extends BaseActivity implements View.OnClickListener {

    private MyImageView mItem_body_detail_msg_back;
    private LinearLayout mItem_body_detail_msg_back_layout;
    private CustomSimpleDraweeView mItem_body_detail_msg_author_icon;
    private MyImageView mItem_body_detail_msg_care;
    private RelativeLayout mItem_body_detail_msg_relative_layout;
    private String mSelf_msg;
    private int body_msg_flag = 0;
    private List<String> body_detail_msg = new ArrayList<>();
    private String currentUserId = "";
    private String currentUserName = "";
    private String currentUserAvatarUrl = "";
    private TextView body_detail_head_title;
    private int CARE_SUCCESS = 1;
    private int CARE_FAILE = 2;
    private boolean isCareFlag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_detail_msg);
//        if ("self_msg".equals(getIntent().getStringExtra("self_msg"))){
//            body_msg_flag = 1;
//            currentUserId = Constants.USER_ID+"";
//            currentUserName = Constants.USER_NAME;
//            currentUserAvatarUrl = Constants.AVATAR;
//        }else if ("other_author_msg".equals(getIntent().getStringExtra("other_author_msg"))){
//            body_msg_flag = 2;
//            currentUserId = CacheDataUtils.getCurrentNeedToSaveData().getUserid()+"";
//            currentUserName = CacheDataUtils.getCurrentNeedToSaveData().getUsername();
//            currentUserAvatarUrl = CacheDataUtils.getCurrentNeedToSaveData().getUseravatar();
//        }else {
//            body_msg_flag = 3;
//            currentUserId = CacheDataUtils.getCurrentNeedToSaveData().getUserid()+"";
//            currentUserName = CacheDataUtils.getCurrentNeedToSaveData().getUsername();
//            currentUserAvatarUrl = CacheDataUtils.getCurrentNeedToSaveData().getUseravatar();
//        }
        if ("self_msg".equals(getIntent().getStringExtra("self_msg"))){
            body_msg_flag = 1;
            currentUserId = Constants.USER_ID+"";
            currentUserName = Constants.USER_NAME;
            currentUserAvatarUrl = Constants.AVATAR;
        }else{
            body_msg_flag = 2;
            currentUserId = CacheDataUtils.getCurrentNeedToSaveData().getUserid()+"";
            currentUserName = CacheDataUtils.getCurrentNeedToSaveData().getUsername();
            currentUserAvatarUrl = CacheDataUtils.getCurrentNeedToSaveData().getUseravatar();
        }
//        ActivityColection.addActivity(this);
        initNetMsg();
        initId();
    }

    private void initId() {
        mItem_body_detail_msg_back = (MyImageView) this.findViewById(R.id.item_body_detail_msg_back);
        mItem_body_detail_msg_back.setSize(Constants.PHONE_WIDTH/12,Constants.PHONE_WIDTH/12);
        mItem_body_detail_msg_back_layout = (LinearLayout) this.findViewById(R.id.item_body_detail_msg_back_layout);
        mItem_body_detail_msg_back_layout.setOnClickListener(this);
        mItem_body_detail_msg_author_icon = (CustomSimpleDraweeView) this.findViewById(R.id.item_body_detail_msg_author_icon);
        mItem_body_detail_msg_author_icon.setWidth(Constants.PHONE_WIDTH/8);
        mItem_body_detail_msg_author_icon.setHeight(Constants.PHONE_WIDTH/8);
        mItem_body_detail_msg_care = (MyImageView) this.findViewById(R.id.item_body_detail_msg_care);
        mItem_body_detail_msg_care.setSize(Constants.PHONE_WIDTH/6,Constants.PHONE_WIDTH/12);
        mItem_body_detail_msg_care.setOnClickListener(this);
        //如果是个人，那么就不需要显示关注的图标
        if ("self_msg".equals(getIntent().getStringExtra("self_msg"))){
            mItem_body_detail_msg_care.setVisibility(View.GONE);
        }else {
            mItem_body_detail_msg_care.setVisibility(View.VISIBLE);
            //判断当前关注的状态
            if ("null".equals(CacheDataUtils.getCurrentNeedToSaveData().getFollowstate())){
                mItem_body_detail_msg_care.setSelected(true);
                isCareFlag = false;
            }else {
                mItem_body_detail_msg_care.setSelected(false);
                isCareFlag = true;
            }
        }
        mItem_body_detail_msg_relative_layout = (RelativeLayout) this.findViewById(R.id.item_body_detail_msg_relative_layout);
        body_detail_head_title = (TextView) this.findViewById(R.id.body_detail_head_title);
        if (isCareFlag==false){
            mItem_body_detail_msg_care.setSelected(true);
        }else {
            mItem_body_detail_msg_care.setSelected(false);
        }
    }

    private void initNetMsg() {
        if (NetUtils.checkNet(this)==true){
            body_detail_msg.clear();
            MyHttpUtils.getNetMessage(this, "http://1zou.me/apisq/userInfo/"+currentUserId, new MyHttpUtils.Callback() {
                @Override
                public void getResponseMsg(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int errorCode = jsonObject.getInt("errorCode");
                        if (errorCode==0){
                            parseJson(jsonObject.getJSONArray("datas").toString());
                        }
                    } catch (JSONException e) {
                        LogUtils.showVerbose("BodyDetailMessageActivity","解析身体数据错误");
                    }
                }
            }, new MyHttpUtils.CallbackError() {
                @Override
                public void getResponseMsg(String error) {

                }
            });
        }else {
            NetUtils.showNoNetDialog(this);
        }

    }

    private void parseJson(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            body_detail_msg.add(jsonObject.getString("headgirth"));//添加头围
            body_detail_msg.add(jsonObject.getString("neckcir"));//添加颈围
            body_detail_msg.add("bust");//添加乳点围/胸围
            body_detail_msg.add(jsonObject.getString("realunderbustgirth"));//添加胸底围
            body_detail_msg.add(jsonObject.getString("navelcrotch"));//添加上裆
            body_detail_msg.add(jsonObject.getString("armlength"));//添加臂长
            body_detail_msg.add(jsonObject.getString("navelankle"));//添加腰腿长
            body_detail_msg.add("null");//添加前腰节高
            body_detail_msg.add(jsonObject.getString("armpitgirth"));//添加肩腋围
            body_detail_msg.add(jsonObject.getString("upperarm"));//添加手臂围
            body_detail_msg.add(jsonObject.getString("shoulderbreadth"));//添加肩宽
            body_detail_msg.add(jsonObject.getString("acrosswidth"));//添加背宽
            body_detail_msg.add(jsonObject.getString("waistline"));//添加腰围
            body_detail_msg.add(jsonObject.getString("hipline"));//添加臀围
            body_detail_msg.add(jsonObject.getString("thighcir"));//添加大腿围
            body_detail_msg.add(jsonObject.getString("bra"));//添加罩杯
            body_detail_msg.add(jsonObject.getString("height"));//添加身高
            body_detail_msg.add(jsonObject.getString("weight"));//添加体重
            initData();
        } catch (JSONException e) {
            LogUtils.showVerbose("BodyDetailMessageActivity","解析datas数据错误");
        }
    }

    private void initData() {
        body_detail_head_title.setText(currentUserName+"的身材数据");
        body_detail_head_title.setTextSize(18);
        body_detail_head_title.setTextColor(Color.WHITE);
        if (!TextUtils.isEmpty(currentUserAvatarUrl)){
            if (body_msg_flag==1){
                SimpleDraweeControlUtils.getNetRingSimpleDraweeControl(this,mItem_body_detail_msg_author_icon,Constants.AVATAR);
            }else {
                SimpleDraweeControlUtils.getNetRingSimpleDraweeControl(this,mItem_body_detail_msg_author_icon,Constants.FIRST_PAGE_IMAGE_URL +"/"+currentUserAvatarUrl);
            }

        }else {
            mItem_body_detail_msg_author_icon.setImageResource(R.mipmap.self_liter_head_icon);
        }
        //设置胸围的信息
        setBodySingleTag((int) ((Constants.PHONE_WIDTH*1.00/100)*53),(int) ((Constants.PHONE_HEIGHT*1.00/100)*11),Constants.PHONE_WIDTH/10,0,"头围"+getBodySingleMessage(0)+"cm");
        //设置颈围的信息
        setBodySingleTag((int) ((Constants.PHONE_WIDTH*1.00/100)*53),(int) ((Constants.PHONE_HEIGHT*1.00/100)*14),Constants.PHONE_WIDTH/10,0,"颈围"+getBodySingleMessage(1)+"cm");
        //设置乳点围/胸围
        setBodyLeftAndRightStareleTag((int) ((Constants.PHONE_WIDTH*1.00/100)*53), (int) ((Constants.PHONE_HEIGHT*1.00/100)*18),Constants.PHONE_WIDTH/10,0,"乳点围/胸围"+getBodySingleMessage(2)+"cm",false);
        //设置胸底围
        setBodyLeftAndRightStareleTag((int) ((Constants.PHONE_WIDTH*1.00/100)*60),(int) ((Constants.PHONE_HEIGHT*1.00/100)*21),Constants.PHONE_WIDTH/10,0,"胸底围"+getBodySingleMessage(3)+"cm",false);
        //设置标签
        setBodyTextTag((int) ((Constants.PHONE_WIDTH*1.00/100)*60),(int) ((Constants.PHONE_HEIGHT*1.00/100)*23),Constants.PHONE_WIDTH/10,0,"RF底部的一圈");
        //设置上裆的信息
        setBodySingleTag((int) ((Constants.PHONE_WIDTH*1.00/100)*53),(int) ((Constants.PHONE_HEIGHT*1.00/100)*26),Constants.PHONE_WIDTH/10,0,"上裆"+getBodySingleMessage(4)+"cm");
        //设置标签
        setBodyTextTag((int) ((Constants.PHONE_WIDTH*1.00/100)*53),(int) ((Constants.PHONE_HEIGHT*1.00/100)*28),Constants.PHONE_WIDTH/10,0,"肚脐到大腿分叉");
        //设置臂长的信息
        setBodySingleTag((int) ((Constants.PHONE_WIDTH*1.00/100)*53),Constants.PHONE_HEIGHT/3,Constants.PHONE_WIDTH/10,0,"臂长"+getBodySingleMessage(5)+"cm");
        //设置腰腿长的信息
        setBodySingleTag((int) ((Constants.PHONE_WIDTH*1.00/100)*42),(int) ((Constants.PHONE_HEIGHT*1.00/100)*38),Constants.PHONE_WIDTH/10,0,"腰腿长"+getBodySingleMessage(6)+"cm");
        //设置标签
        setBodyTextTag((int) ((Constants.PHONE_WIDTH*1.00/100)*60),(int) ((Constants.PHONE_HEIGHT*1.00/100)*38),0,0,"站直，肚脐到脚踝(脚踝骨头尖)");
        //设置前腰节高的信息
        setBodySingleTag(Constants.PHONE_WIDTH/3,(int) ((Constants.PHONE_HEIGHT*1.00/100)*44),Constants.PHONE_WIDTH/10,0,"前腰节高"+getBodySingleMessage(7)+"cm");
        //设置标签
        setBodyTextTag(Constants.PHONE_WIDTH/3,(int) ((Constants.PHONE_HEIGHT*1.00/100)*46),Constants.PHONE_WIDTH/3,0,"锁骨-乳头-胸底围-肚脐(一条折线哟)");
        //设置肩腋围的信息
        setBodySingleTag((int) ((Constants.PHONE_WIDTH*1.00/100)*53),Constants.PHONE_HEIGHT/2,Constants.PHONE_WIDTH/10,0,"肩腋围"+getBodySingleMessage(8)+"cm");
        //设置手臂围的信息
        setBodyLeftAndRightStareleTag((int) ((Constants.PHONE_WIDTH*1.00/100)*34), (int) ((Constants.PHONE_HEIGHT*1.00/100)*53),Constants.PHONE_WIDTH/10,0,"手臂围"+getBodySingleMessage(9)+"cm",true);
        //设置肩宽的信息
        setBodyLeftAndRightStareleTag((int) ((Constants.PHONE_WIDTH*1.00/100)*40),(int) ((Constants.PHONE_HEIGHT*1.00/100)*57),Constants.PHONE_WIDTH/10,0,"肩宽"+getBodySingleMessage(10)+"cm",false);
        //设置标签
        setBodyTextTag((int) ((Constants.PHONE_WIDTH*1.00/100)*38),(int) ((Constants.PHONE_HEIGHT*1.00/100)*60),Constants.PHONE_WIDTH/10,0,"颈椎点  低头摸到的最大半圆形骨头");
        //设置背宽的信息
        setBodySingleTag((int) ((Constants.PHONE_WIDTH*1.00/100)*56),(int) ((Constants.PHONE_HEIGHT*1.00/100)*62),Constants.PHONE_WIDTH/10,0,"背宽"+getBodySingleMessage(11)+"cm");
        //设置腰围的信息
        setBodySingleTag((int) ((Constants.PHONE_WIDTH*1.00/100)*42),(int) ((Constants.PHONE_HEIGHT*1.00/100)*65),Constants.PHONE_WIDTH/10,0,"腰围"+getBodySingleMessage(12)+"cm");
        //设置臀围的信息
        setBodySingleTag((int) ((Constants.PHONE_WIDTH*1.00/100)*56),(int) ((Constants.PHONE_HEIGHT*1.00/100)*68),Constants.PHONE_WIDTH/10,0,"臀围"+getBodySingleMessage(13)+"cm");
        //设置大腿围的信息
        setBodySingleTag((int) ((Constants.PHONE_WIDTH*1.00/100)*42),(int) ((Constants.PHONE_HEIGHT*1.00/100)*73),Constants.PHONE_WIDTH/10,0,"大腿围"+getBodySingleMessage(14)+"cm");
        //设置胸围罩杯
        setBodyRingTag(Constants.PHONE_WIDTH/27,Constants.PHONE_HEIGHT/7,Constants.PHONE_WIDTH/10,0,"罩杯"+getBodySingleMessage(15)+"C");
        //设置身高
        setBodyReactancleTag(Constants.PHONE_WIDTH/27,Constants.PHONE_HEIGHT/4,Constants.PHONE_WIDTH/10,0,"身高"+getBodySingleMessage(16)+"cm");
        //设置体重
        setBodySquareTag(Constants.PHONE_WIDTH/27,(int) ((Constants.PHONE_HEIGHT*1.00/100)*40),Constants.PHONE_WIDTH/10,0,"体重"+getBodySingleMessage(17)+"kg");
    }

    public void setBodySingleTag(int marginLeft,int marginTop,int marginRight,int marginBottom,String textMsg){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(marginLeft,marginTop,marginRight,marginBottom);
        TextView textView = new TextView(this);
        textView.setBackground(this.getResources().getDrawable(R.drawable.body_tag1_bg_color));
        textView.setPadding(10,2,10,2);
        textView.setText(textMsg);
        textView.setTextSize(11);
        mItem_body_detail_msg_relative_layout.addView(textView,params);
    }
    public void setBodyLeftAndRightStareleTag(int marginLeft,int marginTop,int marginRight,int marginBottom,String textMsg,boolean isleft){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(marginLeft,marginTop,marginRight,marginBottom);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView textView = new TextView(this);
        textView.setBackground(this.getResources().getDrawable(R.drawable.body_tag2_bg_color));
        textView.setPadding(10,2,10,2);
        textView.setText(textMsg);
        textView.setTextSize(11);
        MyImageView myImageView = new MyImageView(this,null);
        myImageView.setSize(Constants.PHONE_WIDTH/35,Constants.PHONE_WIDTH/35);
        myImageView.setImageResource(R.mipmap.must_select);
        if (isleft==true){
            linearLayout.addView(myImageView);
            linearLayout.addView(textView);
        }else {
            linearLayout.addView(textView);
            linearLayout.addView(myImageView);
        }

        mItem_body_detail_msg_relative_layout.addView(linearLayout,params);
    }
    public void setBodyTextTag(int marginLeft,int marginTop,int marginRight,int marginBottom,String textMsg){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(marginLeft,marginTop,marginRight,marginBottom);
        TextView textView = new TextView(this);
//        textView.setBackground(this.getResources().getDrawable(R.drawable.body_tag1_bg_color));
        textView.setPadding(10,2,10,2);
        textView.setText(textMsg);
        textView.setTextSize(10);
        textView.setTextColor(Color.BLUE);
        mItem_body_detail_msg_relative_layout.addView(textView,params);
    }
    public void setBodyRingTag(int marginLeft,int marginTop,int marginRight,int marginBottom,String textMsg){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Constants.PHONE_WIDTH/8, Constants.PHONE_WIDTH/8);
        params.setMargins(marginLeft,marginTop,marginRight,marginBottom);
        TextView textView = new TextView(this);
        textView.setBackground(this.getResources().getDrawable(R.drawable.body_tag3_bg_color));
        textView.setText(textMsg);
        textView.setPadding(6,Constants.PHONE_WIDTH/44,0,0);
        textView.setTextSize(12);
        textView.setTextColor(Color.BLUE);
        mItem_body_detail_msg_relative_layout.addView(textView,params);
    }
    public void setBodyReactancleTag(int marginLeft,int marginTop,int marginRight,int marginBottom,String textMsg){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Constants.PHONE_WIDTH/10, Constants.PHONE_WIDTH/5);
        params.setMargins(marginLeft,marginTop,marginRight,marginBottom);
        TextView textView = new TextView(this);
        textView.setBackground(this.getResources().getDrawable(R.drawable.body_tag2_bg_color));
        textView.setText(textMsg);
        textView.setPadding(0,Constants.PHONE_WIDTH/22,0,0);
        textView.setTextSize(13);
        textView.setTextColor(Color.BLUE);
        mItem_body_detail_msg_relative_layout.addView(textView,params);
    }
    public void setBodySquareTag(int marginLeft,int marginTop,int marginRight,int marginBottom,String textMsg){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Constants.PHONE_WIDTH/7-20, Constants.PHONE_WIDTH/10);
        params.setMargins(marginLeft,marginTop,marginRight,marginBottom);
        TextView textView = new TextView(this);
        textView.setBackground(this.getResources().getDrawable(R.drawable.body_tag4_bg_color));
        textView.setText(textMsg);
        textView.setPadding(0,0,0,0);
        textView.setTextSize(12);
        textView.setTextColor(Color.BLUE);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        mItem_body_detail_msg_relative_layout.addView(textView,params);
    }

    //判断当前的返回的信息是否为空 总共18个属性
    public String getBodySingleMessage(int position){
        if ("null".equals(body_detail_msg.get(position))){
            return "--";
        }else {
            return body_detail_msg.get(position);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_body_detail_msg_back_layout:
//                LogUtils.showVerbose("BodyDetailMessageActivity","点击返回");
//                toItemMostPopular();
                DealReturnLogicUtils.dealReturnLogic(this);
                break;
            case R.id.item_body_detail_msg_care:
                if (NetUtils.checkNet(this)==true){
                    if (Constants.STATUS.equals("reg_log")) {
                        if (isCareFlag==true) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    MyHttpUtils.postJsonData("http://1zou.me/apisq/removeFollow", "userInfo", getCancelCareJsonMsg(),
                                            new MyHttpUtils.Callback() {
                                                @Override
                                                public void getResponseMsg(String response) {
                                                    if ("0".equals(response)) {
                                                        mHandler.sendEmptyMessage(CARE_FAILE);
                                                    }
                                                }
                                            }, new MyHttpUtils.CallbackError() {
                                                @Override
                                                public void getResponseMsg(String error) {

                                                }
                                            });
//                                if (isSuccess == true) {
//
                                    LogUtils.showVerbose("ItemMostpopularActivity", "111");
//                                }
                                }
                            }).start();
                        } else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    MyHttpUtils.postJsonData("http://1zou.me/apisq/addFollow", "userInfo", getAddCareJsonMsg(),
                                            new MyHttpUtils.Callback() {
                                                @Override
                                                public void getResponseMsg(String response) {
                                                    LogUtils.showVerbose("ItemMostpopularActivity", "response=" + response);
                                                    if ("0".equals(response)) {
                                                        mHandler.sendEmptyMessage(CARE_SUCCESS);
                                                    }
                                                }
                                            }, new MyHttpUtils.CallbackError() {
                                                @Override
                                                public void getResponseMsg(String error) {
                                                    LogUtils.showVerbose("ItemMostpopularActivity", "error=" + error);
                                                }
                                            });
//                                if (isSuccess == true) {
//
//                                    LogUtils.showVerbose("ItemMostpopularActivity", "222");
//                                }
                                }
                            }).start();
                        }
                    } else {
                        Intent register_intent = new Intent(BodyDetailMessageActivity.this, LoginActivity.class);
                        register_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(register_intent);
                        finish();
                    }
                }else {
                    NetUtils.showNoNetDialog(this);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
//            if (ActivityColection.isContains(this)){
//                ActivityColection.removeActivity(this);
//                toItemMostPopular();
//            }
            DealReturnLogicUtils.dealReturnLogic(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void toItemMostPopular(){
        if (body_msg_flag==1){
            Intent intent = new Intent(BodyDetailMessageActivity.this, MainActivity.class);
            intent.putExtra("person_design", "person_design");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else if (body_msg_flag==2){
            Intent intent = new Intent(BodyDetailMessageActivity.this, OtherAuthorActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("itemmostpopular","itemmostpopular");
            startActivity(intent);
            finish();
        }else if (body_msg_flag==3){
            Intent intent = new Intent(BodyDetailMessageActivity.this,ItemMostpopularActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == CARE_FAILE) {
                isCareFlag = false;
                mItem_body_detail_msg_care.setSelected(true);
                CacheDataUtils.getCurrentNeedToSaveData().setFollowstate("null");
                //取消关注
                Toast.makeText(BodyDetailMessageActivity.this, "取消关注", Toast.LENGTH_SHORT).show();
            } else if (msg.what == CARE_SUCCESS) {
                isCareFlag = true;
                mItem_body_detail_msg_care.setSelected(false);
                CacheDataUtils.getCurrentNeedToSaveData().setFollowstate(Constants.USER_ID+"");
                Toast.makeText(BodyDetailMessageActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
            }
        }
    };
    //获取取消关注的参数
    public String getCancelCareJsonMsg() {
        JSONObject jsonObject = new JSONObject();
//        {'userInfo':{'userid':1,'fansid':2}}
        try {
            jsonObject.put("userid", Constants.USER_ID);
//            jsonObject.put("userid", 7927);
            jsonObject.put("followid", CacheDataUtils.getCurrentNeedToSaveData().getUserid());//添加被关注者的id
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    //获取添加关注的参数
    public String getAddCareJsonMsg() {
        JSONObject jsonObject = new JSONObject();
//       {'userInfo':{'userid':1,'followid':2}}
        try {
            jsonObject.put("userid", Constants.USER_ID);
//            jsonObject.put("userid", 7927);
//            jsonObject.put("userid", 1);
            jsonObject.put("followid", CacheDataUtils.getCurrentNeedToSaveData().getUserid());//添加被关注者的id
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}
