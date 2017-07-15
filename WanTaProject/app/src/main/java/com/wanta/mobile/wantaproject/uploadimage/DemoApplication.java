package com.wanta.mobile.wantaproject.uploadimage;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


/**
 * Created by zfdang on 2016-4-15.
 */
public class DemoApplication extends Application
{

    private static RequestQueue mRequestQueue;
    public static final String WX_APPID = "wx4af982f0e478f37e";
    public static final String WX_APPSecret = "d75e8fe7ed12d8c717d24bd8fabce335";


    private IWXAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        //使用simpledrawee的全局配置
        // the following line is important
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(getApplicationContext())
                // other setters
                .setDownsampleEnabled(true)
                .build();//添加这个是为了适配更多格式的图片
        Fresco.initialize(getApplicationContext(),config);

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        //微信第三方登录
        api = WXAPIFactory.createWXAPI(this, WX_APPID, true);
        api.registerApp(WX_APPID);

        //内存泄漏测试
//        LeakCanary.install(this);
    }

    public static RequestQueue getRequesstInstance(){
        return mRequestQueue;
    }
}
