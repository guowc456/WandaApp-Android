package com.wanta.mobile.wantaproject.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/8/11.
 */
public class ActivityColection  {

    public static List<Activity> mActivities = new ArrayList<>();
    public static List<Activity> currentActivities = new ArrayList<>();

    public static void addActivity(Activity activity){
        mActivities.add(activity);
    }
    public static void removeActivity(Activity activity){
        mActivities.remove(mActivities.get(mActivities.size()-1));
    }
    public static boolean isContains(Activity activity){
        if (!mActivities.isEmpty()){
//            if (mActivities.contains(activity)){
//                return true;
//            }
            if (mActivities.get(mActivities.size()-1).equals(activity)){
                return true;
            }
        }
       return false;
    }

    /**
     * 在获取对应的activity的时候也把mActivities中的大小逐渐减小防止跳转错误，
     * @param currentActivityPosition  当前要跳转大小的位置
     * @return
     */
    public static Activity getActivity(int currentActivityPosition){
        if (mActivities.size()>0){
//            currentActivities.clear();
//            for (int i=0;i<=currentActivityPosition-1;i++){
//                currentActivities.add(mActivities.get(i));
//            }
//            mActivities.clear();
//            for (int i=0;i<=currentActivityPosition-1;i++){
//                mActivities.add(currentActivities.get(i));
//            }
            return mActivities.get(currentActivityPosition-1);
        }
        return null;
    }

    public static void clearAllActivity(){
        mActivities.clear();
    }

    public static void finshAll(){

        for (Activity activity:mActivities){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
        System.exit(0);
    }

    public static void removeAll(){
        for (Activity activity:mActivities){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
