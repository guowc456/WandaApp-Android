package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.WardoreSingleRecycleViewAdapter;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.domain.CacheDataHelper;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.DealReturnLogicUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by WangYongqiang on 2017/1/10.
 */
public class WardoreSingleActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mWardrobe_single_back_layout;
    private CustomSimpleDraweeView mWardrobe_single_back;
    private String mUrl;
    private CustomSimpleDraweeView mWardrobe_single_draweeview;
    private RecyclerView mWardrobe_single_recycleview;
    private CustomSimpleDraweeView mWardrobe_single_log_draweeview;
    private CustomSimpleDraweeView mWardrobe_single_meihua_draweeview;
    private CustomSimpleDraweeView mWardrobe_single_share_draweeview;
    private CustomSimpleDraweeView mWardrobe_single_delete_draweeview;
    private ScrollView mWardrobe_single_scrollview;
    private LinearLayout mWardrobe_single_head_layout;
    private String clothCatogry;
    private String clothId;
    private String contents[] = new String[10];
    private PopupWindow popupWindow;
    private TextView chuandalinggan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wandore_single_layout);
//        ActivityColection.addActivity(this);
//        mUrl = getIntent().getStringExtra("url");
//        clothCatogry = getIntent().getStringExtra("clothCatogry");
//        clothId = getIntent().getStringExtra("clothId");
        mUrl = Constants.single_activity_url;
        clothCatogry = Constants.single_activity_clothCatogry;
        clothId = Constants.single_activity_clothId;
//        LogUtils.showVerbose("WardoreSingleActivity","mUrl=="+mUrl+"  clothCatogry=="+clothCatogry+"  clothId=="+clothId);
        initClothMsg();
        initId();
    }

    private void initClothMsg() {
        MyHttpUtils.getNetMessage(this, "http://1zou.me/api/itemdetails/"+clothCatogry+"/"+clothId, new MyHttpUtils.Callback() {
            @Override
            public void getResponseMsg(String response) {
//                LogUtils.showVerbose("WardoreSingleActivity","衣服单品信息="+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    contents[0] = jsonObject.getString("type");
                    contents[1] = jsonObject.getString("color");
                    contents[2] = jsonObject.getString("band");
                    contents[3] = jsonObject.getString("size");
                    contents[4] = jsonObject.getString("price");
                    contents[5] = jsonObject.getString("buyloc");
                    contents[6] = jsonObject.getString("story");
                    contents[7] = jsonObject.getString("perdarling");
                    contents[8] = jsonObject.getString("link");
                    contents[9] = jsonObject.getString("weartimes");
                    handler.sendEmptyMessageDelayed(1,200);
                } catch (JSONException e) {
                    LogUtils.showVerbose("WardoreSingleActivity","json数据解析错误");
                }
            }
        }, new MyHttpUtils.CallbackError() {
            @Override
            public void getResponseMsg(String error) {

            }
        });
    }

    private void initId() {
        mWardrobe_single_head_layout = (LinearLayout) this.findViewById(R.id.wardrobe_single_head_layout);
        mWardrobe_single_back_layout = (LinearLayout) this.findViewById(R.id.wardrobe_single_back_layout);
        mWardrobe_single_back_layout.setOnClickListener(this);
        mWardrobe_single_back = (CustomSimpleDraweeView) this.findViewById(R.id.wardrobe_single_back);
        mWardrobe_single_back.setWidth(Constants.PHONE_WIDTH/16);
        mWardrobe_single_back.setHeight(Constants.PHONE_WIDTH/16);
        mWardrobe_single_back.setImageResource(R.mipmap.pre_arrows);
        mWardrobe_single_draweeview = (CustomSimpleDraweeView) this.findViewById(R.id.wardrobe_single_draweeview);
        mWardrobe_single_draweeview.setWidth(Constants.PHONE_WIDTH);
        mWardrobe_single_draweeview.setHeight(Constants.PHONE_HEIGHT/5*4);
        mWardrobe_single_draweeview.setImageURI(Uri.parse("http://1zou.me/images/"+mUrl));
        mWardrobe_single_draweeview.setFocusable(true);
        mWardrobe_single_draweeview.setFocusableInTouchMode(true);
        mWardrobe_single_draweeview.requestFocus();
        mWardrobe_single_log_draweeview = (CustomSimpleDraweeView) this.findViewById(R.id.wardrobe_single_log_draweeview);
        mWardrobe_single_log_draweeview.setWidth(Constants.PHONE_WIDTH/8);
        mWardrobe_single_log_draweeview.setHeight(Constants.PHONE_WIDTH/8);
        mWardrobe_single_log_draweeview.setImageResource(R.mipmap.log);
        mWardrobe_single_meihua_draweeview = (CustomSimpleDraweeView) this.findViewById(R.id.wardrobe_single_meihua_draweeview);
        mWardrobe_single_meihua_draweeview.setWidth(Constants.PHONE_WIDTH/8);
        mWardrobe_single_meihua_draweeview.setHeight(Constants.PHONE_WIDTH/8);
        mWardrobe_single_meihua_draweeview.setImageResource(R.mipmap.meihua);
        mWardrobe_single_share_draweeview = (CustomSimpleDraweeView) this.findViewById(R.id.wardrobe_single_share_draweeview);
        mWardrobe_single_share_draweeview.setWidth(Constants.PHONE_WIDTH/8);
        mWardrobe_single_share_draweeview.setHeight(Constants.PHONE_WIDTH/8);
        mWardrobe_single_share_draweeview.setImageResource(R.mipmap.share);
        mWardrobe_single_delete_draweeview = (CustomSimpleDraweeView) this.findViewById(R.id.wardrobe_single_delete_draweeview);
        mWardrobe_single_delete_draweeview.setWidth(Constants.PHONE_WIDTH/8);
        mWardrobe_single_delete_draweeview.setHeight(Constants.PHONE_WIDTH/8);
        mWardrobe_single_delete_draweeview.setImageResource(R.mipmap.delete);
        mWardrobe_single_delete_draweeview.setOnClickListener(this);
        chuandalinggan = (TextView) this.findViewById(R.id.chuandalinggan);
        chuandalinggan.setOnClickListener(this);
    }

    private void initData() {
        mWardrobe_single_recycleview = (RecyclerView) this.findViewById(R.id.wardrobe_single_recycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mWardrobe_single_recycleview.setLayoutManager(linearLayoutManager);
        WardoreSingleRecycleViewAdapter adapter = new WardoreSingleRecycleViewAdapter(this,contents);
        mWardrobe_single_recycleview.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wardrobe_single_back_layout:
//                jumpToWardrobeDetailActivity();
                DealReturnLogicUtils.dealReturnLogic(this);
                break;
            case R.id.wardrobe_single_delete_draweeview:
                View view =  LayoutInflater.from(WardoreSingleActivity.this).inflate(R.layout.pop_window_single,null);
                popupWindow = new PopupWindow(view,
                        ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,false);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.setTouchable(true);
                popupWindow.showAsDropDown(mWardrobe_single_head_layout);
                View contentView = popupWindow.getContentView();
                LinearLayout wardrobe_single_pop_window_delete = (LinearLayout) contentView.findViewById(R.id.wardrobe_single_pop_window_delete);
                LinearLayout wardrobe_single_pop_window_cancel = (LinearLayout) contentView.findViewById(R.id.wardrobe_single_pop_window_cancel);
                wardrobe_single_pop_window_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除单品
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                deleteSingleCloth(clothCatogry,clothId);
                            }
                        }).start();
                    }
                });
                wardrobe_single_pop_window_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.id.chuandalinggan:
                //跳到穿搭灵感
                CacheDataHelper.addNullArgumentsMethod();
                ActivityColection.addActivity(this);
                Intent intent = new Intent(WardoreSingleActivity.this,ChuanDaLingGanActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("url",mUrl);
//                intent.putExtra("clothCatogry",clothCatogry);
//                intent.putExtra("clothId",clothId);
                startActivity(intent);
                finish();
                break;
        }
    }

    //删除单品衣服
    private void deleteSingleCloth(String type,String itemid) {
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);
        HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000);

        // 请求参数设置
        HttpClient client = new DefaultHttpClient(httpParams);
        HttpPost post = new HttpPost("http://1zou.me/api/itemdelete");
        MultipartEntity entity = new MultipartEntity();
        try {
            entity.addPart("type", new StringBody(type, Charset.forName("UTF-8")));
            entity.addPart("itemid", new StringBody(itemid, Charset.forName("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setEntity(entity);
        HttpResponse resp = null;
        try {
            resp = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject = new JSONObject(EntityUtils.toString(resp.getEntity())+"");
            LogUtils.showVerbose("MyHttpUtils","返回的信息:"+jsonObject.toString());
            String errcode = jsonObject.getString("errcode");
            int code = resp.getStatusLine().getStatusCode();
            if ("0".equals(errcode)&&code==200) {
                //删除成功
                popupWindow.dismiss();
                jumpToWardrobeDetailActivity();
            }else {

            }

        } catch (Exception e) {

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
//            if (ActivityColection.isContains(this)){
//                ActivityColection.removeActivity(this);
//                jumpToWardrobeDetailActivity();
//            }
            DealReturnLogicUtils.dealReturnLogic(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void jumpToWardrobeDetailActivity() {
        Intent intent = new Intent(WardoreSingleActivity.this,WardrobeDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("wardoresingle","wardoresingle");
        startActivity(intent);
        finish();
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
