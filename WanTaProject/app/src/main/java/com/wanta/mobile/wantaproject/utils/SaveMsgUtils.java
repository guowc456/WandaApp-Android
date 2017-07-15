package com.wanta.mobile.wantaproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by WangYongqiang on 2017/1/4.
 */
public class SaveMsgUtils {
    /**
     *  //保存当前布尔类型的状态
     * @param context
     * @param fileName  当前要缓存的文件的名字
     * @param key  当前要缓存信息的键名
     * @param value   当前要缓存信息的键值
     */
    public static void setCurrentBooleanState(Context context,String fileName,String key,Boolean value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(key,value);
        edit.commit();
    }

    /**
     * 获取当前文件的boolean类型
     * @param context
     * @param fileName  当前要缓存的文件的名字
     * @param key  当前要缓存信息的键名
     * @return  返回当前键名的键值  ,默认状态时返回false
     */
    public static boolean getCurrentBooleanState(Context context, String fileName, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        boolean aBoolean = sharedPreferences.getBoolean(key,false);
        return aBoolean;
    }
}
