package com.wanta.mobile.wantaproject.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.MainActivity;
import com.wanta.mobile.wantaproject.activity.WeixinSuccessActivity;
import com.wanta.mobile.wantaproject.uploadimage.DemoApplication;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.weixinutils.HttpCallBackListener;
import com.wanta.mobile.wantaproject.weixinutils.HttpUtil;
import com.wanta.mobile.wantaproject.weixinutils.PrefParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by asus on 2016/1/16.
 */
public class weixinLoginFragment extends Fragment {

    public static final String TAG = "weixinLoginFragment";

    private LinearLayout mLoginWeChat;
    private IWXAPI api;
    private ReceiveBroadCast receiveBroadCast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_login, container, false);
        rootView.setTag(TAG);
        mLoginWeChat = (LinearLayout) rootView.findViewById(R.id.layout_login_wx);
        mLoginWeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weChatAuth();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("authlogin");
        getActivity().registerReceiver(receiveBroadCast, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiveBroadCast);
    }

    private void weChatAuth() {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(getActivity(), DemoApplication.WX_APPID, true);
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wx_login_duzun";
        api.sendReq(req);
    }

    public void getAccessToken() {
        SharedPreferences WxSp = getActivity().getApplicationContext()
                .getSharedPreferences(PrefParams.spName, Context.MODE_PRIVATE);
        String code = WxSp.getString(PrefParams.CODE, "");
        final SharedPreferences.Editor WxSpEditor = WxSp.edit();
        Log.d(TAG, "-----获取到的code----" + code);
        LogUtils.showVerbose("weixinLoginFragment","获到的code是："+code);
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + DemoApplication.WX_APPID
                + "&secret="
                + DemoApplication.WX_APPSecret
                + "&code="
                + code
                + "&grant_type=authorization_code";
        Log.d(TAG, "--------即将获取到的access_token的地址--------");
        LogUtils.showVerbose("weixinLoginFragment","获到的access_token的地址是："+url);
        HttpUtil.sendHttpRequest(url, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {

                //解析以及存储获取到的信息
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d(TAG, "-----获取到的json数据1-----" + jsonObject.toString());
                    String access_token = jsonObject.getString("access_token");
                    Log.d(TAG, "--------获取到的access_token的地址--------" + access_token);
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
                Toast.makeText(getActivity(), "通过code获取数据没有成功", Toast.LENGTH_SHORT).show();
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
                    Log.d(TAG, "------获取到的个人信息------" + jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getActivity(), "通过openid获取数据没有成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class ReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            getAccessToken();
            Intent intent1 = new Intent(getActivity(), WeixinSuccessActivity.class);
            startActivity(intent1);
        }
    }
}
