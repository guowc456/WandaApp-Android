package com.wanta.mobile.wantaproject.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.uploadimage.DemoApplication;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;
import com.wanta.mobile.wantaproject.utils.PhoneUtils;
import com.wanta.mobile.wantaproject.weixinutils.HttpCallBackListener;
import com.wanta.mobile.wantaproject.weixinutils.HttpUtil;
import com.wanta.mobile.wantaproject.weixinutils.PrefParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by WangYongqiang on 2016/12/28.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button mLogin_btn_regist;
    private Button mLogin_btn_login;
    private EditText mLogin_username;
    private EditText mLogin_password;
    private ProgressDialog mProgressDialog;
    private MyImageView mLogin_image_name_icon;
    private MyImageView mLogin_image_password_icon;
    private MyImageView mLogin_image_weixin;
    private MyImageView mLogin_image_xinlang;
    private MyImageView mLogin_image_qq;
    private String mShare_name;
    private String mShare_password;
    private LinearLayout mLogin_weixin_layout;
    private IWXAPI api;
    private ReceiveBroadCast receiveBroadCast;
    private String currentWeixinPersonMsg ;
    private LinearLayout mLogin_back_icon_layout;
    private MyImageView mLogin_back_icon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        ActivityColection.addActivity(this);
        if ("hasReigist".equals(getIntent().getStringExtra("hasReigist"))){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("正在登陆,请稍等......");
            mProgressDialog.show();
            SharedPreferences sharedPreferences = getSharedPreferences("saveloginmsg", Context.MODE_PRIVATE);

            //判断一下当前保存的信息
            postLoginMsg(sharedPreferences.getString("name",""),sharedPreferences.getString("password",""));
        }else {
            initId();
        }

    }

    private void initId() {
        mLogin_back_icon_layout = (LinearLayout) this.findViewById(R.id.login_back_icon_layout);
        mLogin_back_icon_layout.setOnClickListener(this);
        mLogin_back_icon = (MyImageView) this.findViewById(R.id.login_back_icon);
        mLogin_back_icon.setSize(Constants.PHONE_WIDTH/12,Constants.PHONE_WIDTH/12);
        mLogin_username = (EditText) this.findViewById(R.id.login_username);
        mLogin_password = (EditText) this.findViewById(R.id.login_password);
        mLogin_btn_regist = (Button) this.findViewById(R.id.login_btn_regist);
        mLogin_btn_regist.setOnClickListener(this);
        mLogin_btn_login = (Button) this.findViewById(R.id.login_btn_login);
        mLogin_btn_login.setOnClickListener(this);

        mLogin_image_name_icon = (MyImageView) this.findViewById(R.id.login_image_name_icon);
        mLogin_image_name_icon.setSize(Constants.PHONE_WIDTH/10,Constants.PHONE_WIDTH/10);
        mLogin_image_password_icon = (MyImageView) this.findViewById(R.id.login_image_password_icon);
        mLogin_image_password_icon.setSize(Constants.PHONE_WIDTH/10,Constants.PHONE_WIDTH/10);
        mLogin_image_weixin = (MyImageView) this.findViewById(R.id.login_image_weixin);
        mLogin_image_weixin.setSize(Constants.PHONE_WIDTH/8,Constants.PHONE_WIDTH/8);
        mLogin_image_xinlang = (MyImageView) this.findViewById(R.id.login_image_xinlang);
        mLogin_image_xinlang.setSize(Constants.PHONE_WIDTH/8,Constants.PHONE_WIDTH/8);
        mLogin_image_qq = (MyImageView) this.findViewById(R.id.login_image_qq);
        mLogin_image_qq.setSize(Constants.PHONE_WIDTH/8,Constants.PHONE_WIDTH/8);

        mLogin_weixin_layout = (LinearLayout) this.findViewById(R.id.login_weixin_layout);
        mLogin_weixin_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn_regist:
                if ("new_publish_to_login".equals(getIntent().getStringExtra("new_publish_to_login"))){
                    Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("new_publish_to_login","new_publish_to_login");
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

                break;
            case R.id.login_btn_login:
                if (TextUtils.isEmpty(mLogin_username.getText().toString())||TextUtils.isEmpty(mLogin_password.getText().toString())){
                    Toast.makeText(LoginActivity.this,"用户名或者密码不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    //登陆成功，然后进行跳转
                    mProgressDialog = new ProgressDialog(this);
                    mProgressDialog.setMessage("正在登陆,请稍等......");
                    mProgressDialog.show();
                    //判断一下当前保存的信息
                    postLoginMsg(mLogin_username.getText().toString(),mLogin_password.getText().toString());
                }
                break;
            case R.id.login_weixin_layout:
                //微信第三方登陆
                weChatAuth();
                break;
            case R.id.login_back_icon_layout:
                jumpMainActivity();
                break;
        }
    }
    public void jumpMainActivity(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (ActivityColection.isContains(this)){
                ActivityColection.removeActivity(this);
                jumpMainActivity();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("authlogin");
        registerReceiver(receiveBroadCast, filter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiveBroadCast);
    }

    private void weChatAuth() {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(this, DemoApplication.WX_APPID, true);
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wx_login_duzun";
        api.sendReq(req);
    }

    public void getAccessToken() {
        SharedPreferences WxSp = this.getApplicationContext()
                .getSharedPreferences(PrefParams.spName, Context.MODE_PRIVATE);
        String code = WxSp.getString(PrefParams.CODE, "");
        final SharedPreferences.Editor WxSpEditor = WxSp.edit();
        LogUtils.showVerbose("weixinLoginFragment","获到的code是："+code);
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + DemoApplication.WX_APPID
                + "&secret="
                + DemoApplication.WX_APPSecret
                + "&code="
                + code
                + "&grant_type=authorization_code";
        LogUtils.showVerbose("weixinLoginFragment","获到的access_token的地址是："+url);
        HttpUtil.sendHttpRequest(url, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {

                //解析以及存储获取到的信息
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String access_token = jsonObject.getString("access_token");
                    String openid = jsonObject.getString("openid");
                    String refresh_token = jsonObject.getString("refresh_token");
                    if (!access_token.equals("")) {
                        WxSpEditor.putString(PrefParams.ACCESS_TOKEN, access_token);
                        WxSpEditor.apply();
                    }
                    if (!refresh_token.equals("")) {
                        WxSpEditor.putString(PrefParams.REFRESH_TOKEN, refresh_token);
                        WxSpEditor.apply();
                    }
                    if (!openid.equals("")) {
                        WxSpEditor.putString(PrefParams.WXOPENID, openid);
                        WxSpEditor.apply();
                        getPersonMessage(access_token, openid);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(LoginActivity.this, "通过code获取数据没有成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPersonMessage(String access_token, String openid) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + access_token
                + "&openid="
                + openid;
        HttpUtil.sendHttpRequest(url, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    LogUtils.showVerbose("weixinLoginFragment","获取到的个人信息=："+jsonObject.toString());
                    currentWeixinPersonMsg = jsonObject.toString();
                    String openid = jsonObject.getString("openid");
                    String nickname = jsonObject.getString("nickname");
                    String avatr = jsonObject.getString("headimgurl");
//                    nickname = new String(nickname.toString().getBytes("UTF-8"));
                    nickname = URLEncoder.encode(nickname, "utf-8");
//                    LogUtils.showVerbose("weixinLoginFragment","openid=："+openid+" nickname="+nickname);
                    //然后进行注册
                    weiXinToRegist(openid,nickname,avatr);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    LogUtils.showVerbose("weixinLoginFragment","字符串转换utf-8错误");
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(LoginActivity.this, "通过openid获取数据没有成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class ReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //授权成功之后获取微信用户的信息，然后进行登陆
            getAccessToken();
            LogUtils.showVerbose("weixinLoginFragment","收到信息");
//            Intent weixin_login_success_intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(weixin_login_success_intent);
            //把个人的额信息保存在本地并且上传到服务器
//            个人信息的格式
//            {
//                "sex":1,
//                    "nickname":"神游侠小强",
//                    "unionid":"oXKp91KhCjJhbjIUmBg2iGIZU88Y",
//                    "privilege":[],
//                "province":"Zhejiang",
//                    "openid":"og-4LwWTvzoA1q5hhTgQab--GSkQ",
//                    "language":"zh_CN",
//                    "headimgurl":
//                "http://wx.qlogo.cn/mmopen/V62KVl82xeQiaYVex16bjMF4Lho6b8kuv3gzpqiaicBicQHMl6lqZ6byJHaS8FqQ3C45WISyho9mwpxseTOz22d0lQ/0",
//                        "country":"CN",
//                    "city":"Hangzhou"
//            }
//            if (!TextUtils.isEmpty(currentWeixinPersonMsg)){
////                mProgressDialog.show();
//                //进行登陆申请
//                try {
//                    JSONObject jsonObject = new JSONObject(currentWeixinPersonMsg);
//                    LogUtils.showVerbose("LoginActivity","返回的信息="+currentWeixinPersonMsg);
//                    String openid = jsonObject.getString("openid");
//                    String nickname = jsonObject.getString("nickname");
//                    //然后进行注册
//                    weiXinToRegist(openid,nickname);
//                } catch (JSONException e) {
//                    //json解析异常，但还是跳转到主界面
//                    Intent weixin_login_success_intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(weixin_login_success_intent);
//                }
//
//            }else {
//                Toast.makeText(LoginActivity.this,"微信登陆失败",Toast.LENGTH_SHORT).show();
//            }
        }
    }

    private void weiXinToRegist(final String openid, final String nickname,String avatr) {
        MyHttpUtils.getNetMessage(this,
                "http://1zou.me/api/userregister?openid="+openid+"&avatar="+avatr+"&username="+nickname+"&password=&deviceid="+ PhoneUtils.getUniqueMsg(this)+"",
                new MyHttpUtils.Callback() {
            @Override
            public void getResponseMsg(String response) {
//                {"errCode":0,"userId":7921,"userName":"test","errInfo":""}。
                LogUtils.showVerbose("weixinLoginFragment","注册的个人信息=："+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    LogUtils.showVerbose("weixinLoginFragment","注册的个人信息=："+jsonObject.getString("userName"));
                    int errCode = jsonObject.getInt("errCode");
                    if (errCode==0){
                        //注册成功
                        Constants.USER_ID = jsonObject.getInt("userId");
                        LogUtils.showVerbose("LoginActivity","userid="+Constants.USER_ID);
                        Constants.STATUS = "reg_log";
                        Constants.AVATAR = jsonObject.getString("avatar");
                        Constants.USER_NAME = nickname;

                        SharedPreferences sharedPreferences = getSharedPreferences("saveloginmsg", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString("name", nickname);
                        edit.putString("password", openid);
                        edit.putString("state","reg_log");
                        edit.putInt("userId",jsonObject.getInt("userId"));
                        edit.putString("atatar",jsonObject.getString("avatar"));
                        edit.commit();
//                        mProgressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "恭喜您，注册成功", Toast.LENGTH_SHORT).show();
                        if ("new_publish_to_login".equals(getIntent().getStringExtra("new_publish_to_login"))){
                            Intent intent = new Intent(LoginActivity.this, NewPublishActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }

                    }else if (errCode==2){
                        Toast.makeText(LoginActivity.this,"这个账号已经注册过了",Toast.LENGTH_SHORT).show();
                        Constants.USER_ID = jsonObject.getInt("userId");
                        Constants.STATUS = "reg_log";
                        SharedPreferences sharedPreferences = getSharedPreferences("saveloginmsg", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString("state","reg_log");
                        edit.putInt("userId",jsonObject.getInt("userId"));
                        edit.commit();
                        if ("new_publish_to_login".equals(getIntent().getStringExtra("new_publish_to_login"))){
                            Intent intent = new Intent(LoginActivity.this, NewPublishActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(intent);
                            finish();
                        }else {
                            Intent weixin_login_success_intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(weixin_login_success_intent);
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                    Intent weixin_login_success_intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(weixin_login_success_intent);
                }
            }
        }, new MyHttpUtils.CallbackError() {
            @Override
            public void getResponseMsg(String error) {
                LogUtils.showVerbose("weixinLoginFragment","注册的错误息=："+error);
            }
        });
    }

    private void postLoginMsg(final String name, final String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://1zou.me/api/login?username="+name+"&password="+password,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String string) {
                        LogUtils.showVerbose("LoginActivity","登陆信息="+string);
//                        {"errCode":0,"userId":7926,"errInfo":""}
                        try {
                            JSONObject jsonObject = new JSONObject(string);
                            boolean isLogin = jsonObject.getBoolean("isLogin");
                            if (isLogin){
                                //登陆成功
                                JSONObject userInfo = jsonObject.getJSONObject("userInfo");

                                SharedPreferences sharedPreferences = getSharedPreferences("saveloginmsg", Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = sharedPreferences.edit();
                                edit.putString("name", userInfo.getString("userName"));
                                edit.putString("password", password);
                                edit.putString("state","reg_log");
                                edit.putInt("userId",userInfo.getInt("userId"));
                                edit.commit();

                                Constants.USER_ID = userInfo.getInt("userId");
                                Constants.USER_NAME = userInfo.getString("userName");
                                Constants.AVATAR = userInfo.getString("avatar");
                                mProgressDialog.dismiss();
                                Constants.STATUS = "reg_log";
                                LogUtils.showVerbose("LoginActivity","用户id="+Constants.USER_ID);

                                Toast.makeText(LoginActivity.this,"恭喜您，登陆成功",Toast.LENGTH_SHORT).show();
                                if ("new_publish_to_login".equals(getIntent().getStringExtra("new_publish_to_login"))){
                                    Intent intent = new Intent(LoginActivity.this, NewPublishActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Intent weixin_login_success_intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(weixin_login_success_intent);
                                }
                            }else {
                                mProgressDialog.dismiss();
                                Toast.makeText(LoginActivity.this,"账号或者密码错误",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this,"登陆失败，请重新登陆",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.showVerbose("RegisterActivity","错误信息="+volleyError);
            }
        });
        requestQueue.add(stringRequest);
    }
}
