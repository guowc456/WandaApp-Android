package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.TopicsDetailMessageAdapter;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.CacheDataHelper;
import com.wanta.mobile.wantaproject.domain.CacheDataUtils;
import com.wanta.mobile.wantaproject.domain.TopicsDetailMessageInfo;
import com.wanta.mobile.wantaproject.uploadimage.ImagesSelectorActivity;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.DealReturnLogicUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;
import com.wanta.mobile.wantaproject.utils.SimpleDraweeControlUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/13.
 */

public class TopicsDetailMessageActivity extends BaseActivity implements View.OnClickListener {

    private MyImageView topics_detail_msg_back_icon;
    private LinearLayout topics_detail_msg_back_layout;
    private RecyclerView topics_detail_msg_recycview;
    private CustomSimpleDraweeView topics_detail_msg_maoboli_image;
    private CustomSimpleDraweeView topics_detail_msg_auhtor_icon;
    private LinearLayout topics_detail_msg_content_layout;
    private int click_location;
    private List<String> topicsMsg = new ArrayList<>();
    private TextView topics_detail_msg_icon_number_tv;
    private TextView topics_detail_msg_bebrowsed_number_tv;
    private TextView topics_detail_msg_beliked_number_tv;
    private TextView topics_detail_msg_name;
    private TextView topics_detail_msg_title;
    private TextView topics_detail_msg_content;
    private List<TopicsDetailMessageInfo> topics_all_message = new ArrayList<>();
    private TopicsDetailMessageAdapter adapter;
    private  int number = 0;
    private int currentAllIcons = 0;
    private LinearLayout topics_detail_msg_head_layout;
    private LinearLayout topics_detail_add_icon_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics_detail_msg_layout);
        initId();
        initNetMsg();
    }

    private void initData() {
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//防止item来回的变换
        topics_detail_msg_recycview.setLayoutManager(staggeredGridLayoutManager);
        topics_detail_msg_recycview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                staggeredGridLayoutManager.invalidateSpanAssignments();//防止出现空白的部分
            }
        });
        adapter = new TopicsDetailMessageAdapter(this);
        adapter.setTopicsNumber(topics_all_message);
        topics_detail_msg_recycview.setAdapter(adapter);

        topics_detail_msg_maoboli_image.setHeight((int) (Constants.PHONE_HEIGHT*1.00/2.5));
        topics_detail_msg_maoboli_image.setWidth(Constants.PHONE_WIDTH);
        topics_detail_msg_maoboli_image.setScaleType(ImageView.ScaleType.FIT_XY);
        topics_detail_msg_maoboli_image.setImageURI(Uri.parse(Constants.FIRST_PAGE_IMAGE_URL +"bg_"+topicsMsg.get(4)));

        topics_detail_msg_auhtor_icon.setWidth(Constants.PHONE_WIDTH/4);
        topics_detail_msg_auhtor_icon.setHeight(Constants.PHONE_WIDTH/4);
        topics_detail_msg_auhtor_icon.setScaleType(ImageView.ScaleType.FIT_XY);
        SimpleDraweeControlUtils.getNetRingSimpleDraweeControl(this,topics_detail_msg_auhtor_icon,Constants.FIRST_PAGE_IMAGE_URL +"avatar_"+topicsMsg.get(4));
//        LogUtils.showVerbose("TopicsDetailMessageActivity","头像的链接"+Constants.FIRST_PAGE_IMAGE_URL +"avatar_"+topicsMsg.get(4));
        topics_detail_msg_name.setText(topicsMsg.get(0));
        topics_detail_msg_title.setText(topicsMsg.get(1));
        topics_detail_msg_content.setText(topicsMsg.get(2));
        topics_detail_msg_icon_number_tv.setText(topicsMsg.get(7));
        topics_detail_msg_bebrowsed_number_tv.setText(topicsMsg.get(5));
        if ("null".equals(topicsMsg.get(6))){
            topics_detail_msg_beliked_number_tv.setText("0");
        }else {
            topics_detail_msg_beliked_number_tv.setText(topicsMsg.get(6));
        }

        topics_detail_msg_recycview.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                LogUtils.showVerbose("MostPopularFragment", "是否滑动到底=" + isSlideToBottom(recyclerView));
                if (isSlideToBottom(recyclerView)) {
                    number = number + 1;
//                    if (currentAllIcons<(number+1)*10)
                    MyHttpUtils.getNetMessage(TopicsDetailMessageActivity.this, "http://1zou.me/apisq/loadTpcInfo/"+Constants.USER_ID+"/"+Constants.whichTopicsPosition+"/10/"+(number*10), new MyHttpUtils.Callback() {
                        @Override
                        public void getResponseMsg(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int errorCode = jsonObject.getInt("errorCode");
                                if (errorCode == 0) {
                                    //获取信息正确
                                    parseJsonMsg(response);
                                } else {
                                    LogUtils.showVerbose("MostPopularFragment", "获取的数据错误");
                                }
                            } catch (JSONException e) {
                            }

                        }
                    }, new MyHttpUtils.CallbackError() {
                        @Override
                        public void getResponseMsg(String error) {
                        }
                    });

                } else {
//                    LogUtils.showVerbose("MostPopularFragment", "当前的number=" + currentFid);
                }
            }
        });

        //设置条目的点击事件
        adapter.setOnTopicsDetailMessageListener(new TopicsDetailMessageAdapter.OnTopicsDetailMessageListener() {
            @Override
            public void itemClick(TopicsDetailMessageInfo topicsDetailMessageInfo) {
                CacheDataUtils.setCurrentNeedToSaveData(
                        topicsDetailMessageInfo.getIcnfid(),topicsDetailMessageInfo.getTitle(),topicsDetailMessageInfo.getContent(),topicsDetailMessageInfo.getLikenum(),
                        topicsDetailMessageInfo.getStorenum(),topicsDetailMessageInfo.getUserid(),topicsDetailMessageInfo.getUsername(),topicsDetailMessageInfo.getUseravatar(),
                        topicsDetailMessageInfo.getFavourstate(),topicsDetailMessageInfo.getStoredstate(),topicsDetailMessageInfo.getLng(),topicsDetailMessageInfo.getLat(),
                        topicsDetailMessageInfo.getAddress(),topicsDetailMessageInfo.getUaddress(),topicsDetailMessageInfo.getCreatedat(),
                        topicsDetailMessageInfo.getImages(),topicsDetailMessageInfo.getFollowstate(),topicsDetailMessageInfo.getHeight(),topicsDetailMessageInfo.getWeight(),
                        topicsDetailMessageInfo.getBust(),topicsDetailMessageInfo.getBra(),topicsDetailMessageInfo.getCmtnum(),topicsDetailMessageInfo.getTopics(),topicsDetailMessageInfo.getBrowsenum(),
                        topicsDetailMessageInfo.getTitle_cn(),topicsDetailMessageInfo.getContent_cn()
                );
                ActivityColection.addActivity(TopicsDetailMessageActivity.this);
                Intent intent = new Intent(TopicsDetailMessageActivity.this,ItemMostpopularActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    //获取话题的具体的信息
    private void initNetMsg() {
        MyHttpUtils.getNetMessage(this, "http://1zou.me/apisq/loadTpcInfo/"+Constants.USER_ID+"/"+Constants.whichTopicsPosition+"/10/0", new MyHttpUtils.Callback() {
            @Override
            public void getResponseMsg(String response) {
                LogUtils.showVerbose("TopicsDetailMessageActivity","话题的具体的信息"+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int errorCode = jsonObject.getInt("errorCode");
                    if (errorCode==0){
                        initParseJsonMsg(response);
                    }
                } catch (JSONException e) {
                    LogUtils.showVerbose("TopicsDetailMessageActivity","话题具体信息解析失败");
                }
//                parseJsonMsg(response);
            }
        }, new MyHttpUtils.CallbackError() {
            @Override
            public void getResponseMsg(String error) {

            }
        });
    }

    private void initId() {
//        topics_detail_show_pop_window = (TextView) this.findViewById(R.id.topics_detail_show_pop_window);
        topics_detail_msg_head_layout = (LinearLayout) this.findViewById(R.id.topics_detail_msg_head_layout);
        topics_detail_msg_back_icon = (MyImageView) this.findViewById(R.id.topics_detail_msg_back_icon);
        topics_detail_msg_back_icon.setSize(Constants.PHONE_WIDTH/12,Constants.PHONE_WIDTH/12);
        topics_detail_msg_back_layout = (LinearLayout) this.findViewById(R.id.topics_detail_msg_back_layout);
        topics_detail_msg_back_layout.setOnClickListener(this);
        topics_detail_msg_recycview = (RecyclerView) this.findViewById(R.id.topics_detail_msg_recycview);


        //设置话题显示的宽度
        topics_detail_msg_content_layout = (LinearLayout) this.findViewById(R.id.topics_detail_msg_content_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (Constants.PHONE_WIDTH*1.00/1.5), ViewGroup.LayoutParams.WRAP_CONTENT);
        topics_detail_msg_content_layout.setLayoutParams(params);

        topics_detail_msg_maoboli_image = (CustomSimpleDraweeView) this.findViewById(R.id.topics_detail_msg_maoboli_image);
        topics_detail_msg_auhtor_icon = (CustomSimpleDraweeView) this.findViewById(R.id.topics_detail_msg_auhtor_icon);

        topics_detail_msg_name = (TextView) this.findViewById(R.id.topics_detail_msg_name);
        topics_detail_msg_title = (TextView) this.findViewById(R.id.topics_detail_msg_title);
        topics_detail_msg_content = (TextView) this.findViewById(R.id.topics_detail_msg_content);
        topics_detail_msg_icon_number_tv = (TextView) this.findViewById(R.id.topics_detail_msg_icon_number_tv);
        topics_detail_msg_bebrowsed_number_tv = (TextView) this.findViewById(R.id.topics_detail_msg_bebrowsed_number_tv);
        topics_detail_msg_beliked_number_tv = (TextView) this.findViewById(R.id.topics_detail_msg_beliked_number_tv);

        topics_detail_add_icon_layout = (LinearLayout) this.findViewById(R.id.topics_detail_add_icon_layout);
        topics_detail_add_icon_layout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.topics_detail_msg_back_layout:
//               jumpToItemMostpopularActivity();
                DealReturnLogicUtils.dealReturnLogic(this);
                break;
            case R.id.topics_detail_add_icon_layout:
                CacheDataHelper.addNullArgumentsMethod();
                ActivityColection.addActivity(this);
//                CacheDataUtils.setWhichTopics();
                Intent intent = new Intent(TopicsDetailMessageActivity.this, ImagesSelectorActivity.class);
                intent.putExtra("topics_detail_to_imageselector","topics_detail_to_imageselector");
//                intent.putExtra("click_location", click_location);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
//            if (ActivityColection.isContains(this)){
////                ActivityColection.removeActivity(this);
//                jumpToItemMostpopularActivity();
//            }
            DealReturnLogicUtils.dealReturnLogic(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void jumpToItemMostpopularActivity(){
//        if ("topics".equals(getIntent().getStringExtra("topics"))){
//            //从话题页跳转过来的
//            Intent intent = new Intent(TopicsDetailMessageActivity.this,MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("webview_to_find","webview_to_find");
//            startActivity(intent);
//            finish();
//        }else {
//            Intent intent_to_item_mostpopular = new Intent(TopicsDetailMessageActivity.this,ItemMostpopularActivity.class);
//            intent_to_item_mostpopular.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////            intent_to_item_mostpopular.putExtras(putBundleExtra());
//            startActivity(intent_to_item_mostpopular);
//            finish();
//        }

        if (ActivityColection.isContains(this)){
            ActivityColection.removeActivity(this);
            Intent intent = new Intent(TopicsDetailMessageActivity.this, ActivityColection.getActivity(Constants.saveCacheDataList.size()).getClass());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            if (Constants.saveCacheDataList.size()>1){
                CacheDataUtils.removeCurrentCacheDate();
            }
        }

    }

    private void initParseJsonMsg(String response) {
        topicsMsg.clear();
        topics_all_message.clear();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject datas = jsonObject.getJSONObject("datas");
            topicsMsg.add(datas.getString("title"));
            topicsMsg.add(datas.getString("title_en"));
            topicsMsg.add(datas.getString("content"));
            topicsMsg.add(datas.getString("des_cn"));
            topicsMsg.add(datas.getString("imgpath"));
            topicsMsg.add(datas.getString("bebrowsed"));
            topicsMsg.add(datas.getString("bestored"));
            topicsMsg.add(datas.getString("iconnum"));
            currentAllIcons = Integer.parseInt(datas.getString("iconnum"));
            JSONArray icons = datas.getJSONArray("icons");
            for (int i = 0; i < icons.length(); i++) {
                JSONObject json_object = icons.getJSONObject(i);
                int cmtnum = 0;
                if (jsonObject.has("cmtnum")) {
                    cmtnum = json_object.getInt("cmtnum");
                } else {
                    cmtnum = 0;
                }
                topics_all_message.add(new TopicsDetailMessageInfo(json_object.getInt("icnfid"),
                        json_object.getString("title"),
                        json_object.getString("content"),
                        json_object.getInt("likenum"),
                        json_object.getInt("storenum"),
                        json_object.getInt("userid"),
                        json_object.getString("username"),
                        json_object.getString("useravatar"),
                        json_object.getString("favourstate"),
                        json_object.getString("storedstate"),
                        json_object.getString("lng"),
                        json_object.getString("lat"),
                        json_object.getString("address"),
                        json_object.getString("createdat"),
                        json_object.getString("images"),
                        json_object.getString("weight"),
                        json_object.getString("bust"), json_object.getString("uaddress"),
                        json_object.getString("height"), json_object.getString("followstate"),
                        json_object.getString("bra"),
                        cmtnum, json_object.getString("topics"), json_object.getString("browsenum")
                        ,json_object.getString("title_cn"),json_object.getString("content_cn")));

                LogUtils.showVerbose("MostPopularFragment", "获取的信息=：" + json_object.toString());
            }
            handler.sendEmptyMessage(1);
        } catch (JSONException e) {
            LogUtils.showVerbose("TopicsDetailMessageActivity","话题具体信息解析失败");
        }
    }
    private void parseJsonMsg(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject datas = jsonObject.getJSONObject("datas");
            JSONArray icons = datas.getJSONArray("icons");
            for (int i = 0; i < icons.length(); i++) {
                JSONObject json_object = icons.getJSONObject(i);
                int cmtnum = 0;
                if (jsonObject.has("cmtnum")) {
                    cmtnum = json_object.getInt("cmtnum");
                } else {
                    cmtnum = 0;
                }
                topics_all_message.add(new TopicsDetailMessageInfo(json_object.getInt("icnfid"),
                        json_object.getString("title"),
                        json_object.getString("content"),
                        json_object.getInt("likenum"),
                        json_object.getInt("storenum"),
                        json_object.getInt("userid"),
                        json_object.getString("username"),
                        json_object.getString("useravatar"),
                        json_object.getString("favourstate"),
                        json_object.getString("storedstate"),
                        json_object.getString("lng"),
                        json_object.getString("lat"),
                        json_object.getString("address"),
                        json_object.getString("createdat"),
                        json_object.getString("images"),
                        json_object.getString("weight"),
                        json_object.getString("bust"), json_object.getString("uaddress"),
                        json_object.getString("height"), json_object.getString("followstate"),
                        json_object.getString("bra"),
                        cmtnum, json_object.getString("topics"), json_object.getString("browsenum")
                        ,json_object.getString("title_cn"),json_object.getString("content_cn")));

                LogUtils.showVerbose("MostPopularFragment", "获取的信息=：" + json_object.toString());
            }
            adapter.setTopicsNumber(topics_all_message);
            adapter.notifyItemRangeInserted(adapter.getItemCount(), adapter.getItemCount() + 10);
            adapter.notifyItemRangeChanged(adapter.getItemCount(), adapter.getItemCount() + 10);
        } catch (JSONException e) {
            LogUtils.showVerbose("TopicsDetailMessageActivity","话题具体信息解析失败");
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

    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

}
