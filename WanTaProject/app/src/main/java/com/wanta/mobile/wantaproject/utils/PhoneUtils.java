package com.wanta.mobile.wantaproject.utils;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.Method;

/**
 * Created by WangYongqiang on 2016/12/29.
 */
public class PhoneUtils {
    public static String getUniqueMsg(Activity context){
        //判断当前的imei是否为空
        StringBuilder deviceId = new StringBuilder();
//		deviceId.append(context.getCurrentVersion()+"+");
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        if (!TextUtils.isEmpty(imei)){
            deviceId.append(imei);
//			return deviceId.toString();
        }
        //判断当前的
        //wifi mac地址
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String wifiMac = info.getMacAddress();
        if (!TextUtils.isEmpty(wifiMac)){
            deviceId.append(wifiMac);
//			return deviceId.toString();
        }
        //获取当前的序列号
        String simSerialNumber = tm.getSimSerialNumber();
        if (!TextUtils.isEmpty(simSerialNumber)){
            deviceId.append(simSerialNumber);
//			return deviceId.toString();
        }
        return "wd00000000"+deviceId.toString()+getSerialNumber();
//		return getSerialNumber();
    }
    public static String getSerialNumber(){
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }
}
