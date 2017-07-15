package com.wanta.mobile.wantaproject.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.mordel.DataCatogryHelper;
import com.wanta.mobile.wantaproject.mordel.DomesticDbHelper;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;
import com.wanta.mobile.wantaproject.utils.PhoneUtils;
import com.wanta.mobile.wantaproject.utils.SaveMsgUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Date;

public class WelcomeActivity extends BaseActivity {

    private LocationClient locClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplication());
        setContentView(R.layout.activity_welcome);
        initDate();
        initLocationDatas();
        getWindowLength();
        getLocation();
        clearCacheData();
        //检测用户信息
//        checkUserMsg();
        new MyHandler(this).sendEmptyMessageDelayed(1,3000);
    }

    private void initLocationDatas() {
        new MyThread(this).start();
    }

    private static class MyThread extends Thread {
        WeakReference<WelcomeActivity> mThreadActivityRef;

        public MyThread(WelcomeActivity activity) {
            mThreadActivityRef = new WeakReference<WelcomeActivity>(
                    activity);
        }

        @Override
        public void run() {
            super.run();
            if (mThreadActivityRef == null)
                return;
            if (mThreadActivityRef.get() != null){
                SharedPreferences sharedPreferences = mThreadActivityRef.get().getSharedPreferences("isCopyDataIntoDB",Context.MODE_PRIVATE);
                boolean isCopy = sharedPreferences.getBoolean("isCopy", false);
                if (isCopy==false){
                    SharedPreferences shares = mThreadActivityRef.get().getSharedPreferences("isCopyDataIntoDB",Context.MODE_PRIVATE);
                    shares.edit().putBoolean("isCopy",true).commit();
                    DataCatogryHelper.initDomestic(mThreadActivityRef.get());
                }
            }

        }
    }
    static class MyHandler extends Handler {
        WeakReference<Activity > mActivityReference;
        MyHandler(Activity activity) {
            mActivityReference= new WeakReference<Activity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mActivityReference.get();
            if (activity != null) {
                goToMain(activity);
            }
        }
    }

    //获取当前的时间
    private void initDate() {
        final Date date = new Date(System.currentTimeMillis());
        LogUtils.showVerbose("WelcomeActivity", "当前的时间:" + date.getTime());
        //一个月的时间默认为30天 （2592000000毫秒）
        SharedPreferences timeShare = getSharedPreferences("isMoreThanThirtyDays", Context.MODE_PRIVATE);
        boolean isFirstLogin = timeShare.getBoolean("isFirstLogin", true);// true表示第一次登陆，是匿名用户
        if (isFirstLogin == true) {
            //匿名信息注册
            MyHttpUtils.getNetMessage(this, "http://1zou.me/api/userregister?openid=" + "" + "&username=m_wdanonymous&password=" + "" + "&deviceid=" + PhoneUtils.getUniqueMsg(this) + "", new MyHttpUtils.Callback() {
                @Override
                public void getResponseMsg(String response) {
                    LogUtils.showVerbose("WelcomeActivity", "是否是匿名登陆的返回的信息:" + response);
//               返回的格式     {"errCode":0,"userId":8419,"userName":"m_wdanonymous","errInfo":""}
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        int errCode = jsonObject.getInt("errCode");
                        if (errCode == 0) {

                            SharedPreferences timeShare = getSharedPreferences("isMoreThanThirtyDays", Context.MODE_PRIVATE);
                            timeShare.edit().putBoolean("isFirstLogin", false).commit();

                            SharedPreferences isMoreThanThrithDayShare = getSharedPreferences("isMoreThanThrithDay", Context.MODE_PRIVATE);
                            isMoreThanThrithDayShare.edit().putLong("isMoreThan", date.getTime()).commit();

                            //说明匿名注册成功
                            SharedPreferences sharedPreferences = getSharedPreferences("saveloginmsg", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sharedPreferences.edit();
                            edit.putString("name", jsonObject.getString("userName"));
                            edit.putString("password", "");
                            edit.putString("state", "reg");
                            edit.putInt("userId", jsonObject.getInt("userId"));
                            edit.commit();
                        }
                    } catch (JSONException e) {
                        SharedPreferences sharedPreferences = getSharedPreferences("saveloginmsg", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString("name", "");
                        edit.putString("password", "");
                        edit.putString("state", "reg");
                        edit.putInt("userId", 0);
                        edit.commit();
                    }
                }
            }, new MyHttpUtils.CallbackError() {
                @Override
                public void getResponseMsg(String error) {

                }
            });
        } else {
            //判断当前的时间是否超过30天
            SharedPreferences isMoreThanThrithDayShare = getSharedPreferences("isMoreThanThrithDay", Context.MODE_PRIVATE);
            long isMoreThan = isMoreThanThrithDayShare.getLong("isMoreThan", 0);// true表示第一次登陆，是匿名用户
            if ((date.getTime() - isMoreThan) / 1000 >= 2592000) {
                //超过一个月了
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void getWindowLength() {
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        defaultDisplay.getMetrics(metrics);
        Constants.PHONE_WIDTH = metrics.widthPixels;
        Constants.PHONE_HEIGHT = metrics.heightPixels;
        Constants.PHONE_DENSITY = metrics.density;
        LogUtils.showVerbose("MainActivity", "width=" + Constants.PHONE_WIDTH + "  height=" + Constants.PHONE_HEIGHT);
    }

    /**
     * 跳转到主界面
     */
    private static void goToMain(Activity activity) {
        if (SaveMsgUtils.getCurrentBooleanState(activity, "questionState", "saveState")) {
            //说明已经设置过文件调查了
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        } else {
            Intent intent = new Intent(activity, AskQuestionActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
//        Intent intent = new Intent(WelcomeActivity.this,RecommendedAttentionActivity.class);
//        startActivity(intent);
//        finish();
    }

    //获取手机当前所在位置的经纬度
    public void getLocation() {
        //实例化位置客户端
        locClient = new LocationClient(getApplicationContext());
        //创建一个位置option对象
        LocationClientOption locOption = new LocationClientOption();
        //设置option的属性
        locOption.setCoorType("bd09II");  // bd09II表示返回的结果是百度的经纬度
        locOption.setOpenGps(false);
        locOption.setScanSpan(5000); //每5秒发起一次定位请求
        //确保获取省市的信息
        locOption.setIsNeedAddress(true);

        //将封装的参数设置到位置客户端
        locClient.setLocOption(locOption);

        locClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Constants.CURRENT_ADDRESS = bdLocation.getAddress().address;
                Constants.CURRENT_CITY = bdLocation.getCity();
            }
        });

        //启动位置客户端
        locClient.start();

    }



    private void clearCacheData() {
        ActivityColection.clearAllActivity();
        Constants.saveCacheDataList.clear();
    }

}
