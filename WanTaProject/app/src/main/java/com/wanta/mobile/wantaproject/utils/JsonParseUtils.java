package com.wanta.mobile.wantaproject.utils;

import android.net.Uri;

import com.wanta.mobile.wantaproject.domain.MostPopularInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/12/24.
 */
public class JsonParseUtils {
    //获取图片中的第一张图片
    public static String getFirstPicUrl(String string){
//        LogUtils.showVerbose("JsonParseUtils","str=="+string);
        JSONArray array = null;
        JSONObject jsonObject = null;
        String str = "";
        try {
            array =  new JSONArray(string);
            jsonObject = array.getJSONObject(0);
            str = jsonObject.getString("imglink");
            str = str.substring(4,str.length());
            LogUtils.showVerbose("JsonParseUtils","str=="+str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }
    //获取图片中的第一张图片
    public static String getTopicsFirstPicUrl(String string){
        JSONArray array = null;
        JSONObject jsonObject = null;
        String str = "";
        try {
            array =  new JSONArray(string);
            jsonObject = array.getJSONObject(0);
            str = jsonObject.getString("imglink");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static int[] getFirstPicSize(String string){
        int[] arr = new int[2];
        JSONArray array = null;
        JSONObject jsonObject = null;
        int str1 = 0;
        int str2 = 0;
        try {
            array =  new JSONArray(string);
            jsonObject = array.getJSONObject(0);
            str1 = jsonObject.getInt("imgwd");
            str2 = jsonObject.getInt("imght");
            arr[0] = str1;
            arr[1] = str2;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arr;
    }
    public static List<MostPopularInfo> getAllPicUrl(String string) throws JSONException {
        List<MostPopularInfo> list = new ArrayList<>();
        JSONArray array = new JSONArray(string);
        for (int i=0;i<array.length();i++){
            JSONObject jsonObject = array.getJSONObject(i);
            MostPopularInfo mostPopularInfo = new MostPopularInfo(jsonObject.getString("imglink"),jsonObject.getInt("imgwd"),jsonObject.getInt("imght"));
            list.add(mostPopularInfo);
        }
        return list;
    }

//    //获取数组中的最小值
    public static int getSmallConfid(List<Integer> iconfid){
        int minfid = iconfid.get(0);
        for (int i = 0;i<iconfid.size();i++){
            if (iconfid.get(i)<minfid){
                minfid = iconfid.get(i);
            }
        }
        return minfid;
    }


    //将当前的信息转化成字符串
    public static void getToString(List<String> strings){
        String str = "";
        if (strings.size()!=0){
            for (int i=0;i<strings.size();i++){
                if (i==strings.size()-1){
                    str = str + strings.get(i);
                }else {
                    str = str + strings.get(i)+";";
                }
            }
            Constants.imagesOfEachTag.set(Constants.selectTagsAttribute,str);
        }else {
            Constants.imagesOfEachTag.set(Constants.selectTagsAttribute,"");
        }

    }

    public static int[] getTopicNumber(String string){
        JSONArray jsonArray = null;
        int[] number = null;
        try {
            jsonArray = new JSONArray(string);
            number = new int[jsonArray.length()/2];
            for (int j=0;j<jsonArray.length();j++){
                if (j%2==0){
                    number[j/2] = (int) jsonArray.get(j);
                }
            }
        } catch (JSONException e) {
            LogUtils.showVerbose("ItemMostpopularActivity","数组解析错误");
        }

        return number;
    }
}
