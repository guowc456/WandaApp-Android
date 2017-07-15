package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.TopicsRecycleViewAdapter;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.MostPopularInfo;
import com.wanta.mobile.wantaproject.domain.TopicsInfo;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.JsonParseUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/13.
 */

public class TopicsActivity extends BaseActivity implements View.OnClickListener {

    private MyImageView topics_back_icon;
    private LinearLayout topics_back_layout;
    private int icnfid;
    private String title;
    private String content;
    private int likenum;
    private int storenum;
    private int userid;
    private String username;
    private String useravatar;
    private String favourstate;
    private String storedstate;
    private String lng;
    private String lat;
    private String address;
    private String uaddress;
    private String createdat;
    private String mImages;
    private String followstate;
    private String height;
    private String weight;
    private String bust;
    private String bra;
    private int cmtnum;
    private RecyclerView topics_recycview;
    private List<TopicsInfo> topics_all_message = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics_layout);
        Bundle bundle = getIntent().getExtras();
        icnfid = bundle.getInt("icnfid");
        title = bundle.getString("title");
        content = bundle.getString("content");
        likenum = bundle.getInt("likenum");
        storenum = bundle.getInt("storenum");
        userid = bundle.getInt("userid");
        username = bundle.getString("username");
        useravatar = bundle.getString("useravatar");
        favourstate = bundle.getString("favourstate");
        storedstate = bundle.getString("storedstate");
        lng = bundle.getString("lng");
        lat = bundle.getString("lat");
        address = bundle.getString("address");
        uaddress = bundle.getString("uaddress");
        createdat = bundle.getString("createdat");
        mImages = bundle.getString("images");
        followstate = bundle.getString("followstate");
        height = bundle.getString("height");
        weight = bundle.getString("weight");
        bust = bundle.getString("bust");
        bra = bundle.getString("bra");
        cmtnum = bundle.getInt("cmtnum");
        initNetMessage();
        initId();
//        initData();
    }

    private void initNetMessage() {
        MyHttpUtils.getNetMessage(this, "http://1zou.me/apisq/loadTpc/"+Constants.USER_ID, new MyHttpUtils.Callback() {
            @Override
            public void getResponseMsg(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int errorCode = jsonObject.getInt("errorCode");
                    if (errorCode==0){
                        jsonParse(response);
                    }
                } catch (JSONException e) {
                    LogUtils.showVerbose("TopicsActivity","话题信息解析错误");
                }
            }
        }, new MyHttpUtils.CallbackError() {
            @Override
            public void getResponseMsg(String error) {

            }
        });
    }

    private void initData() {

    }

    private void initId() {
        List<TopicsInfo> list = new ArrayList<>();
        for (int i=0;i<9;i++){
            TopicsInfo info = new TopicsInfo();
            list.add(info);
        }
        topics_back_icon = (MyImageView) this.findViewById(R.id.topics_back_icon);
        topics_back_icon.setSize(Constants.PHONE_WIDTH/12,Constants.PHONE_WIDTH/12);
        topics_back_layout = (LinearLayout) this.findViewById(R.id.topics_back_layout);
        topics_back_layout.setOnClickListener(this);
        topics_recycview = (RecyclerView) this.findViewById(R.id.topics_recycview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        topics_recycview.setLayoutManager(gridLayoutManager);
        TopicsRecycleViewAdapter adapter = new TopicsRecycleViewAdapter(this,list);
        topics_recycview.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.topics_back_layout:
                Intent intent_to_item_mostpopular = new Intent(TopicsActivity.this,ItemMostpopularActivity.class);
                intent_to_item_mostpopular.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent_to_item_mostpopular.putExtras(putBundleExtra());
                startActivity(intent_to_item_mostpopular);
                finish();
                break;
        }
    }
    //bundle添加的参数
    public Bundle putBundleExtra(){
        Bundle bundle = new Bundle();
        bundle.putInt("icnfid", icnfid);
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putInt("likenum", likenum);
        bundle.putInt("storenum", storenum);
        bundle.putInt("userid", userid);
        bundle.putString("username", username);
        bundle.putString("useravatar", useravatar);
        bundle.putString("favourstate", favourstate);
        bundle.putString("storedstate", storedstate);
        bundle.putString("lng", lng);
        bundle.putString("lat", lat);
        bundle.putString("address", address);
        bundle.putString("uaddress", uaddress);
        bundle.putString("createdat", createdat);
        bundle.putString("followstate", followstate);
        bundle.putString("height", height);
        bundle.putString("weight", weight);
        bundle.putString("bust", bust);
        bundle.putString("bra", bra);
        bundle.putString("images", mImages);
        bundle.putInt("cmtnum", cmtnum);
        return bundle;
    }

    private void jsonParse(String string) {
        try {
            JSONObject object = new JSONObject(string);
            JSONArray array = object.getJSONArray("datas");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                topics_all_message.add(new TopicsInfo(
                        jsonObject.getInt("fid"),jsonObject.getString("title"),
                        jsonObject.getString("content"),jsonObject.getString("imgpath"),
                        jsonObject.getInt("bebrowsed"),jsonObject.getInt("beliked"),
                        jsonObject.getInt("iconnum"),jsonObject.getInt("width"),
                        jsonObject.getInt("height"),jsonObject.getString("favourstate")));

                LogUtils.showVerbose("MostPopularFragment", "获取的信息=：" + jsonObject.toString());
            }
            handler.sendEmptyMessage(1);
        } catch (JSONException e) {
            LogUtils.showVerbose("MostPopularFragment", "第一级数组解析错误");
        }
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                initData();
            }
        }
    };
}
