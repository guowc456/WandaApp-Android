package com.wanta.mobile.wantaproject.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;
import com.wanta.mobile.wantaproject.utils.PhoneUtils;
import com.wanta.mobile.wantaproject.utils.SystemUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

/**
 * Created by WangYongqiang on 2016/12/29.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText mRegist_btn_name;
    private EditText mRegist_btn_password;
    private Button mRegist_btn_ok;
    private ProgressDialog mProgressDialog;
    private MyImageView mRegist_btn_back;
    private MyImageView mRegist_imag_name;
    private MyImageView mRegist_imag_password;
    private MyImageView mRegister_image_qq;
    private MyImageView mRegister_image_weixin;
    private MyImageView mRegister_image_xinlang;
    private Button mRegist_login_ok;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initId();
    }

    private void initId() {
        mRegist_btn_name = (EditText) this.findViewById(R.id.regist_btn_name);
        mRegist_btn_name.setOnClickListener(this);
        mRegist_btn_password = (EditText) this.findViewById(R.id.regist_btn_password);
        mRegist_btn_password.setOnClickListener(this);
        mRegist_btn_ok = (Button) this.findViewById(R.id.regist_btn_ok);
        mRegist_btn_ok.setOnClickListener(this);
        mRegist_btn_back = (MyImageView) this.findViewById(R.id.regist_btn_back);
        mRegist_btn_back.setSize(Constants.PHONE_WIDTH / 10, Constants.PHONE_WIDTH / 10);
        mRegist_btn_back.setOnClickListener(this);
        mRegist_imag_name = (MyImageView) this.findViewById(R.id.regist_imag_name);
        mRegist_imag_name.setSize(Constants.PHONE_WIDTH / 20, Constants.PHONE_WIDTH / 20);
        mRegist_imag_password = (MyImageView) this.findViewById(R.id.regist_imag_password);
        mRegist_imag_password.setSize(Constants.PHONE_WIDTH / 20, Constants.PHONE_WIDTH / 20);
        mRegister_image_qq = (MyImageView) this.findViewById(R.id.register_image_qq);
        mRegister_image_qq.setSize(Constants.PHONE_WIDTH / 10, Constants.PHONE_WIDTH / 10);
        mRegister_image_weixin = (MyImageView) this.findViewById(R.id.register_image_weixin);
        mRegister_image_weixin.setSize(Constants.PHONE_WIDTH / 10, Constants.PHONE_WIDTH / 10);
        mRegister_image_xinlang = (MyImageView) this.findViewById(R.id.register_image_xinlang);
        mRegister_image_xinlang.setSize(Constants.PHONE_WIDTH / 10, Constants.PHONE_WIDTH / 10);
        mRegist_login_ok = (Button) this.findViewById(R.id.regist_login_ok);
        mRegist_login_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regist_btn_ok:
                LogUtils.showVerbose("RegisterActivity", "注册信息=");
                if (TextUtils.isEmpty(mRegist_btn_name.getText().toString()) || TextUtils.isEmpty(mRegist_btn_password.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "手机号或者密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    //可以进行上传了
                    mProgressDialog = new ProgressDialog(this);
                    mProgressDialog.setMessage("正在进行注册,请稍等......");
                    mProgressDialog.show();
                    postRegisterMsg(mRegist_btn_name.getText().toString(), mRegist_btn_password.getText().toString(), PhoneUtils.getUniqueMsg(this));
                    LogUtils.showVerbose("RegisterActivity", "1111");
                }
                break;
            case R.id.regist_btn_back:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.regist_login_ok:
                //可以进行登录了
                if (TextUtils.isEmpty(mRegist_btn_name.getText().toString()) || TextUtils.isEmpty(mRegist_btn_password.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "手机号或者密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    //可以进行上传了
                    mProgressDialog = new ProgressDialog(this);
                    mProgressDialog.setMessage("正在登陆,请稍等......");
                    mProgressDialog.show();
                    postLoginMsg(mRegist_btn_name.getText().toString(),mRegist_btn_password.getText().toString());
                    LogUtils.showVerbose("RegisterActivity", "1111");
                }
                break;
        }
    }

    private void postRegisterMsg(final String name, final String password, String deviced) {
        MyHttpUtils.getNetMessage(this, "http://1zou.me/api/userregister?username=" + name + "&password=" + password + "&deviceid=" + deviced, new MyHttpUtils.Callback() {
            @Override
            public void getResponseMsg(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int errCode = jsonObject.getInt("errCode");
                    if (errCode == 0) {
                        //注册成功
                        Constants.USER_ID = jsonObject.getInt("userId");
                        Constants.STATUS = "reg_log";
                        Constants.USER_NAME = name;

                        SharedPreferences sharedPreferences = getSharedPreferences("saveloginmsg", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString("name", name);
                        edit.putString("password", password);
                        edit.putString("state","reg_log");
                        edit.putInt("userId",jsonObject.getInt("userId"));
                        edit.commit();

                        mProgressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "恭喜您，注册成功", Toast.LENGTH_SHORT).show();
                        if ("new_publish_to_login".equals(getIntent().getStringExtra("new_publish_to_login"))){
                            Intent intent = new Intent(RegisterActivity.this, NewPublishActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra("hasReigist","hasReigist");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }

                    } else if (errCode == 1) {
                        mProgressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "该账户已经注册，请用改账号直接登录", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(RegisterActivity.this, "注册账号失败，请重新注册", Toast.LENGTH_SHORT).show();
                }
            }
        }, new MyHttpUtils.CallbackError() {
            @Override
            public void getResponseMsg(String error) {

            }
        });
    }
    private void postLoginMsg(final String name, final String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://1zou.me/api/login?username="+name+"&password="+password,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String string) {
                        LogUtils.showVerbose("RegisterActivity","注册信息="+string);
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

                                Toast.makeText(RegisterActivity.this,"恭喜您，登陆成功",Toast.LENGTH_SHORT).show();
                                if ("new_publish_to_login".equals(getIntent().getStringExtra("new_publish_to_login"))){
                                    Intent intent = new Intent(RegisterActivity.this, NewPublishActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }

                            }else {
                                mProgressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this,"账号或者密码错误",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(RegisterActivity.this,"登陆失败，请重新登陆",Toast.LENGTH_SHORT).show();
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
