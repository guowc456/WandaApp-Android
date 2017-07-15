package com.wanta.mobile.wantaproject.utils;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by WangYongqiang on 2016/11/26.
 */
public class NetUtils {
//    帽子----hat                 useritems---颜色 ，款式，风格
//    围巾----scarf               useritemslen--长度
//    上衣----upper               useritemsout--外套
//    包包----bag                 useritemsinner--内搭
//    裙装----skirt               userdresses--连衣裙
//    裤装----trousers            userskirts--半身裙
//    鞋子----shoes
//    腰带----waist
    private String responseMessage = null;
    /**
     * 获取不同类型的衣服的数目
     */
    public static void getClothCatogryNumber(final Context context) {
        if (checkNet(context) == true) {
//            RequestQueue mQueue = Volley.newRequestQueue(context);
//            for (int i = 0; i < Constants.cloth_catogry_english.length; i++) {
//                final int finalPosition = i;
//                StringRequest stringRequest = new StringRequest(Constants.INTERNET_URL + "listtypenum/1/" + Constants.cloth_catogry_english[i],
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Constants.Cloth_catogry_number[finalPosition] = response;
////                                LogUtils.showVerbose("NetUtils", "response=" + Constants.Cloth_catogry_number.toString());
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("TAG", error.getMessage(), error);
//                    }
//                });
//                mQueue.add(stringRequest);
                RequestQueue mQueue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Constants.INTERNET_URL + "typenums/1",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                for (int i=0;i<Constants.cloth_catogry_english.length;i++){
                                    Constants.Cloth_catogry_number[i] = object.getString(Constants.cloth_catogry_english[i]);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);
                }
            });
            mQueue.add(stringRequest);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle("请设置网络")
                    .setMessage("是否进行设置网络")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setNet(context);
                        }
                    })
                    .setNegativeButton("取消", null);
            builder.show();
        }

    }

    /**
     * 获取不同类型的衣服的数目
     */
    public static void getClothUrl(final Context context,String url) {
//        Constants.Wardrobe_detail_imags_url.clear();
//        Constants.detail_images.clear();
        if (checkNet(context) == true) {
            RequestQueue mQueue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            LogUtils.showVerbose("NetUtils", "response=" + response);
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i=0;i<array.length();i++){
                                   JSONObject object = (JSONObject) array.get(i);
                                    Constants.Wardrobe_detail_imags_url.add(object.getString("image_path"));
                                    LogUtils.showVerbose("NetUtils", "response11=" + Constants.Wardrobe_detail_imags_url.get(i));
//                                    getClothPictures(context,Constants.Wardrobe_detail_imags_url.get(i));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);
                }
            });
            mQueue.add(stringRequest);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle("请设置网络")
                    .setMessage("是否进行设置网络")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setNet(context);
                        }
                    })
                    .setNegativeButton("取消", null);
            builder.show();
        }

    }
    public static void getClothPictures(final Context context, String strurl) {

        if (checkNet(context) == true) {
            RequestQueue mQueue = Volley.newRequestQueue(context);
            ImageRequest imageRequest = new ImageRequest(
                    "http://1zou.me/images/"+strurl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
//                            Constants.detail_images.add(response);
                            LogUtils.showVerbose("NetUtils", "response22=" + Constants.detail_images.size());
                        }
                    }, 100, 100, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            mQueue.add(imageRequest);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle("请设置网络")
                    .setMessage("是否进行设置网络")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setNet(context);
                        }
                    })
                    .setNegativeButton("取消", null);
            builder.show();
        }

    }
    /*
    * 检查网络但不弹出对话框
    * @param context
    * @return
    */
    public static Boolean checkNet(Context context) {// 检查网络
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
// TODO: handle exception
            Log.v("error", e.toString());
        }
        return false;
    }

    /*
     * 打开网络设置页面
     * @param context
     */
    public static void setNet(Context context) {
        Intent intent = null;
        // 判断手机系统的版本 即API大于10 就是3.0或以上版本
        if (android.os.Build.VERSION.SDK_INT > 10) {
            intent = new Intent(
                    android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings",
                    "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        context.startActivity(intent);
    }

    //弹出网络连不上的弹出框
    public static void showNoNetDialog(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("请设置网络")
                .setMessage("是否进行设置网络")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setNet(context);
                    }
                })
                .setNegativeButton("取消", null);
        builder.show();
    }
}
