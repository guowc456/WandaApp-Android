package com.wanta.mobile.wantaproject.webview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.MainActivity;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.DealReturnLogicUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView webview;
    private String find_pic_url;
    private LinearLayout webview_back_layout;
    private MyImageView webview_back_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ActivityColection.addActivity(this);
        initId();
    }

    private void initId() {
//        find_pic_url = getIntent().getStringExtra("find_pic_url");
//        LogUtils.showVerbose("WebViewActivity","find_pic_url="+find_pic_url);
        webview_back_layout = (LinearLayout) this.findViewById(R.id.webview_back_layout);
        webview_back_layout.setOnClickListener(this);
        webview_back_icon = (MyImageView) this.findViewById(R.id.webview_back_icon);
        webview_back_icon.setSize(Constants.PHONE_WIDTH/12,Constants.PHONE_WIDTH/12);
        webview = (WebView) this.findViewById(R.id.webview);
        WebSettings settings = webview.getSettings();
        settings.setBuiltInZoomControls(true);//可以进行大小的缩放
        settings.setJavaScriptEnabled(true);//支持js
        webview.loadUrl(Constants.findImageLindUrl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.webview_back_layout:
//                jumpTOMainActivity();
                DealReturnLogicUtils.dealReturnLogic(this);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            DealReturnLogicUtils.dealReturnLogic(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void jumpTOMainActivity(){
        Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("webview_to_find","webview_to_find");
        startActivity(intent);
        finish();
    }
}
