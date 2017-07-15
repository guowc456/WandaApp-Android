package com.wanta.mobile.wantaproject.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.wanta.mobile.wantaproject.activity.ItemMostpopularActivity;
import com.wanta.mobile.wantaproject.activity.MainActivity;
import com.wanta.mobile.wantaproject.domain.CacheDataUtils;

/**
 * Created by Administrator on 2017/5/27.
 */

public class DealReturnLogicUtils {
    /**
     * 处理返回的逻辑问题
     */
    public static void dealReturnLogic(Activity activity){
//        if (ActivityColection.isContains(activity)){
//            ActivityColection.removeActivity(activity);
//            Intent intent = new Intent(activity, ActivityColection.getActivity(Constants.saveCacheDataList.size()).getClass());
//            if (Constants.saveCacheDataList.size()>1){
//                CacheDataUtils.removeCurrentCacheDate();
//            }else {
//                Constants.saveCacheDataList.clear();
//            }
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            activity.startActivity(intent);
//            activity.finish();
//        }
        if (Constants.saveCacheDataList.size()==0){
            Intent intent = new Intent(activity, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.finish();
        }else {
            Intent intent = new Intent(activity, ActivityColection.getActivity(Constants.saveCacheDataList.size()).getClass());
            ActivityColection.removeActivity(activity);
            if (Constants.saveCacheDataList.size()>1){
                CacheDataUtils.removeCurrentCacheDate();
            }else {
                Constants.saveCacheDataList.clear();
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.finish();
        }

    }
}
