package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.LocationBaiduMapAdapter;
import com.wanta.mobile.wantaproject.adapter.OhterAuthorInputPinPaiAdapter;
import com.wanta.mobile.wantaproject.customview.KeyboardListenRelativeLayout;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;
import com.wanta.mobile.wantaproject.utils.SystemUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/29.
 */

public class OtherAuthorInputPinPaiActivity extends BaseActivity implements View.OnClickListener {

    private MyImageView other_author_input_description_back_icon;
    private EditText other_author_input_description_msg_tv;
    private KeyboardListenRelativeLayout other_author_input_description_keyboardlistener;
    private LinearLayout other_author_input_description_ok;
    private int currentPosition;
    private int image_publish_edit;
    private MyImageView tag_pinpai_add_icon;
    private RecyclerView tag_pinpai_add_recycleview;
    private List<String> pinpaiName;
    private LinearLayout other_author_input_pinpai_back_layout;
    private MyImageView other_author_input_pinpai_back_icon;
    private EditText other_author_input_pinpai_msg_tv;
    private LinearLayout other_author_input_pinpai_ok_layout;
    private PopupWindow init_set_pop;
    private RecyclerView mLocation_baidu_map_recycleview;
    private boolean isFilter  = false;
    private KeyboardListenRelativeLayout other_author_input_pinapi_keyboardlistener;
    private LocationBaiduMapAdapter adapter;
    private EditText location_baidu_map_msg_tv;
    private KeyboardListenRelativeLayout other_author_input_pinapi_keyboardlistener1;
    private LinearLayout tag_pinpai_add_layout;
    private LinearLayout other_author_head_title_layout;
    private TextView fragment1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_author_input_pinpai_layout);
        ActivityColection.addActivity(this);
        Bundle extras = getIntent().getExtras();
        currentPosition = extras.getInt("currentPosition");
        image_publish_edit = extras.getInt("image_publish_edit");
        initPinPai();
        initId();
    }

    private void initPinPai() {
        MyHttpUtils.getNetMessage(this, "http://1zou.me/api/getbrands", new MyHttpUtils.Callback() {

            @Override
            public void getResponseMsg(String response) {
                try {
//                    LogUtils.showVerbose("OtherAuthorInputPinPaiActivity", "返回信息" + response);
                    JSONArray jsonArray = new JSONArray(response);
                    pinpaiName = new ArrayList<String>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String str = (String) jsonArray.get(i);
                        pinpaiName.add(str);
                    }
                    handler.sendEmptyMessage(1);
                } catch (JSONException e) {
                    LogUtils.showVerbose("OtherAuthorInputPinPaiActivity", "品牌信息解析错误");
                }
            }
        }, new MyHttpUtils.CallbackError() {
            @Override
            public void getResponseMsg(String error) {

            }
        });
    }

    private void initId() {
        tag_pinpai_add_icon = (MyImageView) this.findViewById(R.id.tag_pinpai_add_icon);
        tag_pinpai_add_icon.setSize(Constants.PHONE_WIDTH / 10, Constants.PHONE_WIDTH / 10);
        other_author_input_pinpai_back_layout = (LinearLayout) this.findViewById(R.id.other_author_input_pinpai_back_layout);
        other_author_input_pinpai_back_layout.setOnClickListener(this);
        other_author_input_pinpai_back_icon = (MyImageView) this.findViewById(R.id.other_author_input_pinpai_back_icon);
        other_author_input_pinpai_back_icon.setSize(Constants.PHONE_WIDTH / 10, Constants.PHONE_WIDTH / 10);
        tag_pinpai_add_recycleview = (RecyclerView) this.findViewById(R.id.tag_pinpai_add_recycleview);
        other_author_input_pinpai_msg_tv = (EditText) this.findViewById(R.id.other_author_input_pinpai_msg_tv);
        other_author_input_pinpai_msg_tv.setOnClickListener(this);
        other_author_input_pinpai_ok_layout = (LinearLayout) this.findViewById(R.id.other_author_input_pinpai_ok_layout);
        other_author_input_pinpai_ok_layout.setOnClickListener(this);
        tag_pinpai_add_layout = (LinearLayout) this.findViewById(R.id.tag_pinpai_add_layout);
        tag_pinpai_add_layout.setOnClickListener(this);
        fragment1 = (TextView) this.findViewById(R.id.fragment1);
    }

    private void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tag_pinpai_add_recycleview.setLayoutManager(linearLayoutManager);
        final OhterAuthorInputPinPaiAdapter authorInputPinPaiAdapter = new OhterAuthorInputPinPaiAdapter(this, pinpaiName);
        tag_pinpai_add_recycleview.setAdapter(authorInputPinPaiAdapter);
        authorInputPinPaiAdapter.setOnOhterAuthorInputPinPaiListener(new OhterAuthorInputPinPaiAdapter.OnOhterAuthorInputPinPaiListener() {
            @Override
            public void onItemClick(int pos, String text) {
//                textView.setTextColor(getResources().getColor(R.color.head_bg_color));
                authorInputPinPaiAdapter.setSelectPositioni(pos);
                authorInputPinPaiAdapter.notifyDataSetChanged();
                Constants.other_author_pop_window_pinpai = text;
            }
        });
        other_author_input_pinapi_keyboardlistener1 = (KeyboardListenRelativeLayout) this.findViewById(R.id.other_author_input_pinapi_keyboardlistener);
        other_author_input_pinapi_keyboardlistener1.setOnKeyboardStateChangedListener(new KeyboardListenRelativeLayout.IOnKeyboardStateChangedListener() {
            @Override
            public void onKeyboardStateChanged(int state) {
                switch (state) {
                    case KeyboardListenRelativeLayout.KEYBOARD_STATE_HIDE://软键盘隐藏

                        break;
                    case KeyboardListenRelativeLayout.KEYBOARD_STATE_SHOW://软键盘显示

                        break;
                    default:
                        break;
                }
            }
        });

        other_author_input_pinpai_msg_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    authorInputPinPaiAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (ActivityColection.isContains(this)) {
                ActivityColection.removeActivity(this);
                jumpToCameraActivity();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void jumpToCameraActivity() {
        Intent intent = new Intent(OtherAuthorInputPinPaiActivity.this, CameraActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("currentPosition", currentPosition);
        intent.putExtra("jump_flag", 1);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.other_author_input_pinpai_back_layout:
                Constants.other_author_pop_window_pinpai = "";
                jumpToCameraActivity();
                break;
            case R.id.other_author_input_pinpai_ok_layout:
                if (TextUtils.isEmpty(Constants.other_author_pop_window_pinpai)){
                    Toast.makeText(OtherAuthorInputPinPaiActivity.this,"当前输入的价格信息不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    SystemUtils.hideInput(this,OtherAuthorInputPinPaiActivity.this,other_author_input_pinpai_ok_layout);//隐藏输入法
                    jumpToCameraActivity();
                }
                break;
            case R.id.other_author_input_pinpai_msg_tv:
                popwindows_selectCity();
                break;
            case R.id.tag_pinpai_add_layout:
                //弹出添加信息的弹出框
                SystemUtils.showOrHide(this);
                showAddPinpaiDialog();
                break;
        }
    }

    private void showAddPinpaiDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.other_author_input_pinpai_add_dialog_layout,null);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(fragment1);
//        popupWindow.showAsDropDown(other_author_input_pinpai_back_icon,Constants.PHONE_WIDTH/8,Constants.PHONE_HEIGHT/4);
        View contentView = popupWindow.getContentView();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) ((Constants.PHONE_WIDTH*1.00/4)*3), ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(Constants.PHONE_WIDTH/8,Constants.PHONE_HEIGHT/4,0,0);
        LinearLayout other_author_input_pinapi_add_dialog_layout = (LinearLayout) contentView.findViewById(R.id.other_author_input_pinapi_add_dialog_layout);
        other_author_input_pinapi_add_dialog_layout.setLayoutParams(params);
        final EditText other_author_input_pinapi_add_dialog_edittext = (EditText) contentView.findViewById(R.id.other_author_input_pinapi_add_dialog_edittext);
        KeyboardListenRelativeLayout other_author_input_pinapi_add_dialog_keyboardlistener = (KeyboardListenRelativeLayout) contentView.findViewById(R.id.other_author_input_pinapi_add_dialog_keyboardlistener);
        other_author_input_pinapi_add_dialog_keyboardlistener.setOnKeyboardStateChangedListener(new KeyboardListenRelativeLayout.IOnKeyboardStateChangedListener() {
            @Override
            public void onKeyboardStateChanged(int state) {
                switch (state) {
                    case KeyboardListenRelativeLayout.KEYBOARD_STATE_HIDE://软键盘隐藏

                        break;
                    case KeyboardListenRelativeLayout.KEYBOARD_STATE_SHOW://软键盘显示

                        break;
                    default:
                        break;
                }
            }
        });
        LinearLayout other_author_input_pinapi_add_dialog_cancel_layout = (LinearLayout) contentView.findViewById(R.id.other_author_input_pinapi_add_dialog_cancel_layout);
        other_author_input_pinapi_add_dialog_cancel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        LinearLayout other_author_input_pinapi_add_dialog_ok_layout = (LinearLayout) contentView.findViewById(R.id.other_author_input_pinapi_add_dialog_ok_layout);
        other_author_input_pinapi_add_dialog_ok_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                other_author_input_pinpai_msg_tv.setText(other_author_input_pinapi_add_dialog_edittext.getText());
                popupWindow.dismiss();
            }
        });
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

    private void popwindows_selectCity() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_windows_baidu_map_layout, null);
        init_set_pop = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);

        init_set_pop.setBackgroundDrawable(new BitmapDrawable());
        init_set_pop.setOutsideTouchable(true);
        init_set_pop.setFocusable(true);
        init_set_pop.setTouchable(true);
        //	set_pop.setAnimationStyle(R.style.Anim_popWindow_in);
        //set_pop.showAtLocation(frame,Gravity.TOP, 0, 0);
        init_set_pop.showAsDropDown(other_author_input_pinpai_msg_tv);
        View my_init_pop = init_set_pop.getContentView();
        mLocation_baidu_map_recycleview = (RecyclerView) my_init_pop.findViewById(R.id.location_baidu_map_recycleview);
//        location_baidu_map_msg_tv = (EditText) my_init_pop.findViewById(R.id.location_baidu_map_msg_tv);
        try {

            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            mLocation_baidu_map_recycleview.setLayoutManager(manager);
            adapter = new LocationBaiduMapAdapter(this,pinpaiName);
            mLocation_baidu_map_recycleview.setAdapter(adapter);
            adapter.setOnLocationBaiduItemListener(new LocationBaiduMapAdapter.OnLocationBaiduItemListener() {
                @Override
                public void setItemClick(View view) {

                    int position = mLocation_baidu_map_recycleview.getChildAdapterPosition(view);
                    other_author_input_pinpai_msg_tv.setText(pinpaiName.get(position));
                    init_set_pop.dismiss();
                }
            });
//            location_baidu_map_msg_tv.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    if (s.length()>0){
//                        adapter.getFilter().filter(s);
//                    }
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });
        } catch (Exception e) {
            LogUtils.showVerbose("LocationBaiduMapActivity","生成json数组错误");
        }
    }
}
