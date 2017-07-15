package com.wanta.mobile.wantaproject.wxapi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wanta.mobile.wantaproject.uploadimage.DemoApplication;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.weixinutils.PrefParams;

/**
 * Created by asus on 2016/1/16.
 */
public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, DemoApplication.WX_APPID, false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        LogUtils.showVerbose("WXEntryActivity","baseResp="+baseResp.errCode);
        String result = "";
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = ((SendAuth.Resp) baseResp).code;
                SharedPreferences WxSp = getApplicationContext().getSharedPreferences(PrefParams.spName, Context.MODE_PRIVATE);
                SharedPreferences.Editor WxSpEditor = WxSp.edit();
                WxSpEditor.putString(PrefParams.CODE,code);
                WxSpEditor.apply();
                Intent intent = new Intent();
                intent.setAction("authlogin");
                WXEntryActivity.this.sendBroadcast(intent);
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
            default:
                result = "发送返回";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
        }
    }
}
