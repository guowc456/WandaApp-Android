package com.wanta.mobile.wantaproject.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.wanta.mobile.wantaproject.uploadimage.DemoApplication;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by WangYongqiang on 2016/12/26.
 */
public class MyHttpUtils {

    private Callback mCallback;
    private CallbackError callbackError;
    private static String sStr;

    public static void getClickIconHeart(Context context, int careFlag, int icnfid , final Callback mCallback, final CallbackError callbackError){
        String flag = null;
        if (careFlag==1){
            //已经点赞了
            flag = "/iconMinFavour/"+icnfid+"/"+Constants.USER_ID;
        }else if (careFlag==0){
            //没有点赞
            flag = "/iconPlusFavour/"+icnfid+"/"+Constants.USER_ID;
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.SERVICE_URL + flag, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                mCallback.getResponseMsg(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callbackError.getResponseMsg(volleyError.getMessage());
            }
        });
        requestQueue.add(stringRequest);

    }
    //话题点赞
    public static void getClickTopicsHeart(Context context, int careFlag, int fid ,final Callback mCallback, final CallbackError callbackError){
        String flag = null;
        if (careFlag==1){
            //已经点赞了
            flag = "/minTcpFavour/"+Constants.USER_ID+"/"+fid;
        }else if (careFlag==0){
            //没有点赞
            flag = "/plusTcpFavour/"+Constants.USER_ID+"/"+fid;
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.SERVICE_URL + flag, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                mCallback.getResponseMsg(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callbackError.getResponseMsg(volleyError.getMessage());
            }
        });
        requestQueue.add(stringRequest);

    }
    //图片点赞
    public static boolean getTakeCare(Context context,int careFlag,int icnfid) {
        final int[] returnCode = new int[1];
        String flag = null;
        if (careFlag==1){
            //已经点赞了
            flag = "/iconMinFavour/"+icnfid+"/"+Constants.USER_ID;
        }else if (careFlag==0){
            //没有点赞
            flag = "/iconPlusFavour/"+icnfid+"/"+Constants.USER_ID;
        }
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.SERVICE_URL + flag, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    int errorCode = jsonObject.getInt("errorCode");
                    LogUtils.showVerbose("MyHttpUtils","response="+string);
                    returnCode[0] = errorCode;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
       mRequestQueue.add(stringRequest);
        if (returnCode[0]==0){
            return true;
        }else {
            return false;
        }
    }
    //评论点赞
    public static void getHandCommentClick(Context context, int careFlag, int commentfid , final Callback mCallback, final CallbackError callbackError){
        String flag = null;
        if (careFlag==1){
            //已经点赞了
            flag = "/cmtMinFavour/"+commentfid+"/"+Constants.USER_ID;
        }else if (careFlag==0){
            //没有点赞
            flag = "/cmtPlusFavour/"+commentfid+"/"+Constants.USER_ID;
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.SERVICE_URL + flag, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                mCallback.getResponseMsg(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callbackError.getResponseMsg(volleyError.getMessage());
            }
        });
        requestQueue.add(stringRequest);

    }

    //图片点赞
    public static void getStore(Context context,int careFlag,int icnfid , final Callback callback, final CallbackError callbackError) {
        String flag = null;
        if (careFlag==1){
            //已经点赞了
            flag = "/iconMinStore/"+icnfid+"/"+Constants.USER_ID;
        }else if (careFlag==0){
            //没有点赞
            flag = "/iconPlusStore/"+icnfid+"/"+Constants.USER_ID;
        }
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.SERVICE_URL + flag, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    int errorCode = jsonObject.getInt("errorCode");
                    callback.getResponseMsg(errorCode+"");
                } catch (JSONException e) {
                    LogUtils.showVerbose("MyHttpUtils","获取的收藏的json数据解析错误");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callbackError.getResponseMsg(volleyError.getMessage().toString());
            }
        });
        mRequestQueue.add(stringRequest);
    }

    public static void postJsonData(String url, String jsonKey, String jsonValue, final Callback callback, final CallbackError callbackError){
// 链接超时，请求超时设置
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);
        HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000);

        // 请求参数设置
        HttpClient client = new DefaultHttpClient(httpParams);
        HttpPost post = new HttpPost(url);
        MultipartEntity entity = new MultipartEntity();
        try {
            entity.addPart(jsonKey, new StringBody(jsonValue, Charset.forName("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setEntity(entity);
        HttpResponse resp = null;
        try {
            resp = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject = new JSONObject(EntityUtils.toString(resp.getEntity())+"");
            LogUtils.showVerbose("MyHttpUtils","返回的信息:"+jsonObject.toString());
            int errorCode = jsonObject.getInt("errorCode");
            int code = resp.getStatusLine().getStatusCode();
            if (code==200&&errorCode==0) {
                callback.getResponseMsg(""+errorCode);
            }else {
                callback.getResponseMsg(""+errorCode);
            }

        } catch (Exception e) {
            callbackError.getResponseMsg("error");
        }
    }

    //获取网络数据
    public static void getNetMessage(Context context, String url, final Callback callback, final CallbackError callbackError){
        final String[] responseMsg = {""};
        RequestQueue requestQueue = DemoApplication.getRequesstInstance();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
//                LogUtils.showVerbose("MyHttpUtils", "网络返回的信息："+string);
                callback.getResponseMsg(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callbackError.getResponseMsg(volleyError.getMessage());
            }
        });
        requestQueue.add(stringRequest);
    }


    public interface Callback{
        void getResponseMsg(String response);
    }
    public interface CallbackError{
        void getResponseMsg(String error);
    }

}
