package com.wanta.mobile.wantaproject.utils;

import android.util.Log;

/**
 * Created by WangYongqiang on 2016/6/2.
 */
public class LogUtils {
    private static final int  Verbose = 0;
    private static final int  Debug = 1;
    private static final int  Info = 2;
    private static final int  Warn = 3;
    private static final int  Error = 4;
    private static final int  Nothing = 5;
    private static int currentLog =Verbose;

    public static void showVerbose(String tag,String message){
        if (currentLog<=Verbose){
            Log.v(tag, message);
        }
    }
    public static void showDebug(String tag,String message){
        if (currentLog<=Debug){
            Log.d(tag, message);
        }
    }
    public static void showInfo(String tag,String message){
        if (currentLog<=Info){
            Log.i(tag, message);
        }
    }public static void showWarn(String tag,String message){
        if (currentLog<=Warn){
            Log.w(tag, message);
        }
    }public static void showError(String tag,String message){
        if (currentLog<=Error){
            Log.e(tag, message);
        }
    }
}
