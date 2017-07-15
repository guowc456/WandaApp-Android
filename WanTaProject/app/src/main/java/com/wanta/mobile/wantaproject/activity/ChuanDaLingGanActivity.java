package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.ChuanDaLingGanRecycviewAdapter;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.ChuanDaLingGanInfo;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.DealReturnLogicUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/23.
 */

public class ChuanDaLingGanActivity extends BaseActivity implements View.OnClickListener {

    private MyImageView chuandalinggan_back_icon;
    private LinearLayout chuandalinggan_back_layout;
    private String mUrl;
    private String clothCatogry;
    private String clothId;
    private List<ChuanDaLingGanInfo> chuanDaLingGanInfoList = new ArrayList<>();
    private RecyclerView chuandalinggan_recycview;
    private MyImageView chuandalinggan_hand_image;
    private MyImageView chuandalinggan_line_image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuandaligngan_layout);
//        ActivityColection.addActivity(this);
//        mUrl = getIntent().getStringExtra("url");
//        clothCatogry = getIntent().getStringExtra("clothCatogry");
//        clothId = getIntent().getStringExtra("clothId");
        mUrl = Constants.single_activity_url;
        clothCatogry = Constants.single_activity_clothCatogry;
        clothId = Constants.single_activity_clothId;
        LogUtils.showVerbose("ChuanDaLingGanActivity","mesg=:"+"id="+Constants.USER_ID+"url="+mUrl+"和"+clothCatogry+"和"+clothId);
        initId();
        initNetMessage();
    }

    private void initNetMessage() {
        MyHttpUtils.getNetMessage(this, "http://1zou.me/api/iconfromitem/" + clothCatogry + "/" + clothId, new MyHttpUtils.Callback() {
            @Override
            public void getResponseMsg(String response) {
                LogUtils.showVerbose("ChuanDaLingGanActivity","穿衣搭配信息:"+response);
                try {
                    JSONArray array = new JSONArray(response);
                    if (array.length()!=0){
                        chuanDaLingGanInfoList.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            chuanDaLingGanInfoList.add(new ChuanDaLingGanInfo(
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("image_path"),
                                    jsonObject.getString("type")
                            ));
                        }
                        handler.sendEmptyMessage(1);
                    }

                } catch (JSONException e) {

                }
            }
        }, new MyHttpUtils.CallbackError() {
            @Override
            public void getResponseMsg(String error) {

            }
        });
    }

    private void initId() {
        chuandalinggan_back_icon = (MyImageView) this.findViewById(R.id.chuandalinggan_back_icon);
        chuandalinggan_back_icon.setSize(Constants.PHONE_WIDTH / 10, Constants.PHONE_WIDTH / 10);
        chuandalinggan_back_layout = (LinearLayout) this.findViewById(R.id.chuandalinggan_back_layout);
        chuandalinggan_back_layout.setOnClickListener(this);
        chuandalinggan_recycview = (RecyclerView) this.findViewById(R.id.chuandalinggan_recycview);
        chuandalinggan_hand_image = (MyImageView) this.findViewById(R.id.chuandalinggan_hand_image);
        chuandalinggan_hand_image.setSize(Constants.PHONE_WIDTH / 15, Constants.PHONE_WIDTH / 15);
        chuandalinggan_line_image = (MyImageView) this.findViewById(R.id.chuandalinggan_line_image);
        chuandalinggan_line_image.setSize(Constants.PHONE_WIDTH / 200, Constants.PHONE_WIDTH / 10);
    }

    private void initData() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chuandalinggan_recycview.setLayoutManager(gridLayoutManager);
        ChuanDaLingGanRecycviewAdapter adapter = new ChuanDaLingGanRecycviewAdapter(this, chuanDaLingGanInfoList);
        chuandalinggan_recycview.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chuandalinggan_back_layout:
//                jumpToWardoreSingleActivity();
                DealReturnLogicUtils.dealReturnLogic(this);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
//            jumpToWardoreSingleActivity();
            DealReturnLogicUtils.dealReturnLogic(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void jumpToWardoreSingleActivity(){
        Intent intent = new Intent(ChuanDaLingGanActivity.this, WardoreSingleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("url", mUrl);
        intent.putExtra("clothCatogry", clothCatogry);
        intent.putExtra("clothId", clothId);
        startActivity(intent);
        finish();
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                initData();
            }
        }
    };


}
