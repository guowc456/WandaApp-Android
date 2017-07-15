package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.OtherAuthorRecycviewAdapter;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.CacheDataHelper;
import com.wanta.mobile.wantaproject.domain.CacheDataUtils;
import com.wanta.mobile.wantaproject.domain.MostPopularInfo;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.DealReturnLogicUtils;
import com.wanta.mobile.wantaproject.utils.JsonParseUtils;
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
 * Created by Administrator on 2017/3/28.
 */

public class OtherAuthorActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout other_author_back_layout;
    private MyImageView other_author_back_icon;
    private CustomSimpleDraweeView other_author_head_icon;
    private MyImageView other_author_body_icon;
    private MyImageView other_author_location_icon;
    private RecyclerView other_author_recycleview1;
    private MyImageView other_author_bg_icon1;
    private MyImageView other_author_bg_icon2;
    private MyImageView other_author_bg_icon3;
    private LinearLayout other_author_body_msg_layout;
    private LinearLayout other_author_recommend_layout;
    private TextView other_author_endorsement_number;
    private TextView other_author_fensi_number;
    private TextView other_author_like_number;
    private TextView other_author_store_number;
    private LinearLayout other_author_body_detail_msg_layout;
    private TextView other_author_name_tv;
    private TextView other_author_location;
    private TextView other_author_height;
    private TextView other_author_sign;
    private List<MostPopularInfo> all_message = new ArrayList<>();
    private int num = 0;
    private OtherAuthorRecycviewAdapter authorRecycviewAdapter;
    private TextView other_author_dapei_number;
    private TextView other_author_shouchang_number;
    private TextView other_author_yichu_number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_author_layout);

//        if ("mostpopular".equals(getIntent().getStringExtra("mostpopular"))) {
//            //从主界面跳转过来的
//            Constants.jumpFlag = 1;
//        } else if ("itemmostpopular".equals(getIntent().getStringExtra("itemmostpopular"))) {
//            //从子界面的主界面跳转过来
//            Constants.jumpFlag = 2;
//        }
        initId();
        initNetMsg();
    }

    private void initNetMsg() {
        if (NetUtils.checkNet(this) == true) {
            MyHttpUtils.getNetMessage(this, "http://1zou.me/apisq/userStatic/" + CacheDataUtils.getCurrentNeedToSaveData().getUserid(), new MyHttpUtils.Callback() {
                @Override
                public void getResponseMsg(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int errorCode = jsonObject.getInt("errorCode");
                        if (errorCode == 0) {
                            //数据获取正确
                            JSONArray datas = jsonObject.getJSONArray("datas");
                            JSONObject object = datas.getJSONObject(0);
                            if ("null".equals(object.getString("icnbrowsenum"))) {
                                other_author_endorsement_number.setText("0");
                            } else {
                                other_author_endorsement_number.setText(object.getString("icnbrowsenum"));
                            }
                            other_author_fensi_number.setText(object.get("fansnum") + "");
                            if ("null".equals(object.getString("icnbelikednum"))) {
                                other_author_like_number.setText("0");
                            } else {
                                other_author_like_number.setText(object.getString("icnbrowsenum"));
                            }
                            if ("null".equals(object.getString("icnbestorednum"))) {
                                other_author_store_number.setText("0");
                            } else {
                                other_author_store_number.setText(object.getString("icnbrowsenum"));
                            }
                            handler.sendEmptyMessage(1);
                        }
                    } catch (JSONException e) {
                        LogUtils.showVerbose("OtherAuthorActivity", "数据解析错误");
                    }
                }
            }, new MyHttpUtils.CallbackError() {
                @Override
                public void getResponseMsg(String error) {

                }
            });
        } else {
            NetUtils.showNoNetDialog(this);
        }

    }

    private void initId() {
        other_author_back_layout = (LinearLayout) this.findViewById(R.id.other_author_back_layout);
        other_author_back_layout.setOnClickListener(this);
        other_author_back_icon = (MyImageView) this.findViewById(R.id.other_author_back_icon);
        other_author_back_icon.setSize(Constants.PHONE_WIDTH / 18, Constants.PHONE_WIDTH / 18);
        other_author_head_icon = (CustomSimpleDraweeView) this.findViewById(R.id.other_author_head_icon);
        other_author_head_icon.setWidth(Constants.PHONE_WIDTH / 8);
        other_author_head_icon.setHeight(Constants.PHONE_WIDTH / 8);
        SimpleDraweeControlUtils.getNetRingSimpleDraweeControl(this, other_author_head_icon, Constants.FIRST_PAGE_IMAGE_URL + CacheDataUtils.getCurrentNeedToSaveData().getUseravatar());
        other_author_endorsement_number = (TextView) this.findViewById(R.id.other_author_endorsement_number);
        other_author_fensi_number = (TextView) this.findViewById(R.id.other_author_fensi_number);
        other_author_like_number = (TextView) this.findViewById(R.id.other_author_like_number);
        other_author_store_number = (TextView) this.findViewById(R.id.other_author_store_number);
        other_author_body_icon = (MyImageView) this.findViewById(R.id.other_author_body_icon);
        other_author_body_icon.setSize(Constants.PHONE_WIDTH / 20, Constants.PHONE_WIDTH / 20);
        other_author_location_icon = (MyImageView) this.findViewById(R.id.other_author_location_icon);
        other_author_location_icon.setSize(Constants.PHONE_WIDTH / 20, Constants.PHONE_WIDTH / 20);
        other_author_bg_icon1 = (MyImageView) this.findViewById(R.id.other_author_bg_icon1);
        other_author_bg_icon1.setSize(Constants.PHONE_WIDTH / 10, Constants.PHONE_WIDTH / 10);
        other_author_bg_icon2 = (MyImageView) this.findViewById(R.id.other_author_bg_icon2);
        other_author_bg_icon2.setSize(Constants.PHONE_WIDTH / 10, Constants.PHONE_WIDTH / 10);
        other_author_bg_icon3 = (MyImageView) this.findViewById(R.id.other_author_bg_icon3);
        other_author_bg_icon3.setSize(Constants.PHONE_WIDTH / 10, Constants.PHONE_WIDTH / 10);
        other_author_recommend_layout = (LinearLayout) this.findViewById(R.id.other_author_recommend_layout);
        other_author_recommend_layout.setOnClickListener(this);

        other_author_recycleview1 = (RecyclerView) this.findViewById(R.id.other_author_recycleview1);

        other_author_body_msg_layout = (LinearLayout) this.findViewById(R.id.other_author_body_msg_layout);
        other_author_body_msg_layout.setOnClickListener(this);

        other_author_body_detail_msg_layout = (LinearLayout) this.findViewById(R.id.other_author_body_detail_msg_layout);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams((int) ((Constants.PHONE_WIDTH * 1.00 / 4) * 2.5), ViewGroup.LayoutParams.WRAP_CONTENT);
        other_author_body_detail_msg_layout.setLayoutParams(params1);
        other_author_name_tv = (TextView) this.findViewById(R.id.other_author_name_tv);
        other_author_location = (TextView) this.findViewById(R.id.other_author_location);
        other_author_height = (TextView) this.findViewById(R.id.other_author_height);
        other_author_sign = (TextView) this.findViewById(R.id.other_author_sign);

        other_author_dapei_number = (TextView) this.findViewById(R.id.other_author_dapei_number);
        other_author_shouchang_number = (TextView) this.findViewById(R.id.other_author_shouchang_number);
        other_author_yichu_number = (TextView) this.findViewById(R.id.other_author_yichu_number);
    }

    private void initData() {
        other_author_name_tv.setText(CacheDataUtils.getCurrentNeedToSaveData().getUsername());
        other_author_location.setText(CacheDataUtils.getCurrentNeedToSaveData().getAddress());
//        other_author_height.setText("   "+CacheDataUtils.getCurrentNeedToSaveData().getHeight()+"cm/"+CacheDataUtils.getCurrentNeedToSaveData().getWeight()+"kg"+"   ");
////        other_author_sign.setText();

        //接下来获取自己的信息
        if (NetUtils.checkNet(this) == true) {
            MyHttpUtils.getNetMessage(this, "http://1zou.me/apisq/loadUserIconInfo/" + CacheDataUtils.getCurrentNeedToSaveData().getUserid() + "/" + Constants.USER_ID + "/10/0", new MyHttpUtils.Callback() {
                @Override
                public void getResponseMsg(String response) {
                    initJsonParse(response);
//                    LogUtils.showVerbose("OtherAuthorActivity",response);
                }
            }, new MyHttpUtils.CallbackError() {
                @Override
                public void getResponseMsg(String error) {

                }
            });
        } else {
            NetUtils.showNoNetDialog(this);
        }

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

    //初始化的数据的解析
    private void initJsonParse(String string) {
        num = 0;
        try {
            JSONObject object = new JSONObject(string);
            JSONObject datas_array = object.getJSONObject("datas");
            //获取对应的工作和签名信息
            String carrer = "";
            if (!"null".equals(datas_array.getString("career"))){
                carrer = datas_array.getString("career");
            }else {
                carrer = "";
            }
            other_author_height.setText("   " + CacheDataUtils.getCurrentNeedToSaveData().getHeight() + "cm/" + CacheDataUtils.getCurrentNeedToSaveData().getWeight() + "kg" + "   "+carrer);
            other_author_sign.setText("个性签名:"+datas_array.getString("sign"));
            if ("null".equals(datas_array.getString("storednum"))){
                other_author_shouchang_number.setText("0");
            }else {
                other_author_shouchang_number.setText(datas_array.getString("storednum"));
            }
            other_author_yichu_number.setText(datas_array.getInt("wardrobe")+"");
            JSONArray array = datas_array.getJSONArray("icons");
            other_author_dapei_number.setText(array.length()+"");
            all_message.clear();
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                int cmtnum = 0;
                if (jsonObject.has("cmtnum")) {
                    cmtnum = jsonObject.getInt("cmtnum");
                } else {
                    cmtnum = 0;
                }
                all_message.add(new MostPopularInfo(jsonObject.getInt("icnfid"),
                        jsonObject.getString("title"),
                        jsonObject.getString("content"),
                        jsonObject.getInt("likenum"),
                        jsonObject.getInt("storenum"),
                        CacheDataUtils.getCurrentNeedToSaveData().getUserid(),
                        CacheDataUtils.getCurrentNeedToSaveData().getUsername(),
                        CacheDataUtils.getCurrentNeedToSaveData().getUseravatar(),
                        jsonObject.getString("favourstate"),
                        jsonObject.getString("storedstate"),
                        jsonObject.getString("lng"),
                        jsonObject.getString("lat"),
                        jsonObject.getString("address"),
                        jsonObject.getString("createdat"),
                        jsonObject.getString("images"),
                        CacheDataUtils.getCurrentNeedToSaveData().getWeight(),
                        CacheDataUtils.getCurrentNeedToSaveData().getBust(), CacheDataUtils.getCurrentNeedToSaveData().getUaddress(),
                        CacheDataUtils.getCurrentNeedToSaveData().getHeight(), jsonObject.getString("followstate"),
                        CacheDataUtils.getCurrentNeedToSaveData().getBra(),
                        cmtnum, jsonObject.getString("topics"), jsonObject.getString("browsenum")
                        ,jsonObject.getString("title_cn"),jsonObject.getString("content_cn")));
            }
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            other_author_recycleview1.setLayoutManager(linearLayoutManager);
            authorRecycviewAdapter = new OtherAuthorRecycviewAdapter(this);
            authorRecycviewAdapter.setOtherAuthorListMsg(all_message,1);
            other_author_recycleview1.setAdapter(authorRecycviewAdapter);
            other_author_recycleview1.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    LogUtils.showVerbose("MostPopularFragment", "111");
                    }
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
//                LogUtils.showVerbose("MostPopularFragment", "是否滑动到底=" + isSlideToBottom(recyclerView));
                    if (isSlideToBottom(recyclerView)) {
                        num = num + 1;
                        if (NetUtils.checkNet(OtherAuthorActivity.this) == true) {
                            MyHttpUtils.getNetMessage(OtherAuthorActivity.this, "http://1zou.me/apisq/loadOthersIcons/" + CacheDataUtils.getCurrentNeedToSaveData().getUserid() + "/" + Constants.USER_ID + "/10/" + num * 10, new MyHttpUtils.Callback() {
                                @Override
                                public void getResponseMsg(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        int errorCode = jsonObject.getInt("errorCode");
                                        if (errorCode == 0) {
                                            //获取信息正确
                                            jsonParse(response);
                                        } else {
                                            LogUtils.showVerbose("MostPopularFragment", "获取的数据错误");
                                        }
                                    } catch (JSONException e) {
//                                e.printStackTrace();
                                    }

                                }
                            }, new MyHttpUtils.CallbackError() {
                                @Override
                                public void getResponseMsg(String error) {
                                }
                            });
                        } else {
                            NetUtils.showNoNetDialog(OtherAuthorActivity.this);
                        }

                    } else {
//                    LogUtils.showVerbose("MostPopularFragment", "当前的number=" + currentFid);
                    }
                }
            });
        } catch (JSONException e) {
            LogUtils.showVerbose("MostPopularFragment", "第一级数组解析错误");

        }
    }

    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    private void jsonParse(String string) {
        try {
            JSONObject object = new JSONObject(string);
            JSONArray array = object.getJSONArray("datas");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                int cmtnum = 0;
                if (jsonObject.has("cmtnum")) {
                    cmtnum = jsonObject.getInt("cmtnum");
                } else {
                    cmtnum = 0;
                }
                all_message.add(new MostPopularInfo(jsonObject.getInt("icnfid"),
                        jsonObject.getString("title"),
                        jsonObject.getString("content"),
                        jsonObject.getInt("likenum"),
                        jsonObject.getInt("storenum"),
                        CacheDataUtils.getCurrentNeedToSaveData().getUserid(),
                        CacheDataUtils.getCurrentNeedToSaveData().getUsername(),
                        CacheDataUtils.getCurrentNeedToSaveData().getUseravatar(),
                        jsonObject.getString("favourstate"),
                        jsonObject.getString("storedstate"),
                        jsonObject.getString("lng"),
                        jsonObject.getString("lat"),
                        jsonObject.getString("address"),
                        jsonObject.getString("createdat"),
                        jsonObject.getString("images"),
                        CacheDataUtils.getCurrentNeedToSaveData().getWeight(),
                        CacheDataUtils.getCurrentNeedToSaveData().getBust(), CacheDataUtils.getCurrentNeedToSaveData().getUaddress(),
                        CacheDataUtils.getCurrentNeedToSaveData().getHeight(), jsonObject.getString("followstate"),
                        CacheDataUtils.getCurrentNeedToSaveData().getBra(),
                        cmtnum, jsonObject.getString("topics"), jsonObject.getString("browsenum")
                        ,jsonObject.getString("title_cn"),jsonObject.getString("content_cn")));

                LogUtils.showVerbose("MostPopularFragment", "获取的信息=：" + jsonObject.toString());
            }

            authorRecycviewAdapter.setOtherAuthorListMsg(all_message,1);
            authorRecycviewAdapter.notifyItemRangeInserted(authorRecycviewAdapter.getItemCount(), authorRecycviewAdapter.getItemCount() + 10);
            authorRecycviewAdapter.notifyItemRangeChanged(authorRecycviewAdapter.getItemCount(), authorRecycviewAdapter.getItemCount() + 10);
        } catch (JSONException e) {
            LogUtils.showVerbose("MostPopularFragment", "第一级数组解析错误");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (ActivityColection.isContains(this)) {
//                ActivityColection.removeActivity(this);
//                if (Constants.jumpFlag == 1) {
//                    Intent intent = new Intent(OtherAuthorActivity.this, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    finish();
//                } else if (Constants.jumpFlag == 2) {
//                    Intent intent_to_itemmostpopular = new Intent(OtherAuthorActivity.this, ItemMostpopularActivity.class);
//                    intent_to_itemmostpopular.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent_to_itemmostpopular);
//                    finish();
//                }
//            }
            DealReturnLogicUtils.dealReturnLogic(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.other_author_body_msg_layout:
                CacheDataHelper.addHasArgumentsMethod();
                ActivityColection.addActivity(this);
                Intent intent_to_body_msg = new Intent(OtherAuthorActivity.this, BodyDetailMessageActivity.class);
                intent_to_body_msg.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent_to_body_msg.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent_to_body_msg.putExtra("other_author_msg", "other_author_msg");
                startActivity(intent_to_body_msg);
                finish();
                break;
            case R.id.other_author_back_layout:
//                if (Constants.jumpFlag == 1) {
//                    Intent intent = new Intent(OtherAuthorActivity.this, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    finish();
//                } else if (Constants.jumpFlag == 2) {
//                    Intent intent_to_itemmostpopular = new Intent(OtherAuthorActivity.this, ItemMostpopularActivity.class);
//                    intent_to_itemmostpopular.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent_to_itemmostpopular);
//                    finish();
//                }
                DealReturnLogicUtils.dealReturnLogic(this);
                break;
            case R.id.other_author_recommend_layout:
                CacheDataHelper.addHasArgumentsMethod();
                ActivityColection.addActivity(this);
                Intent intent1 = new Intent(OtherAuthorActivity.this, RecommendedAttentionActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                finish();
                break;
        }
    }
}
