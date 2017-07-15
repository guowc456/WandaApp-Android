package com.wanta.mobile.wantaproject.phonecamera;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.ByteArrayBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.BaseActivity;
import com.wanta.mobile.wantaproject.activity.MainActivity;
import com.wanta.mobile.wantaproject.activity.WardrobeCalendarDetailActivity;
import com.wanta.mobile.wantaproject.domain.ImageListContent;
import com.wanta.mobile.wantaproject.phonepics.PhonePicsSelectActivity;
import com.wanta.mobile.wantaproject.uploadimage.FileUtils;
import com.wanta.mobile.wantaproject.uploadimage.ImagesSelectorActivity;
import com.wanta.mobile.wantaproject.uploadimage.SelectorSettings;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.ImageCompressUtil;
import com.wanta.mobile.wantaproject.utils.LogUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by Administrator on 2017/4/10.
 */

public class LunchCameraActivity extends BaseActivity {

    private final int IMAGE_RESULT_CODE = 2;// 表示打开照相机
    private Uri tempUri;
    private int cameraCatogry = 0;//是哪个界面调用了相机
    private ProgressDialog progressDialog;
    private String currentPicName = "";
    private int newSelectDay;
    private int newSelectYear;
    private int newSelectMonth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ("wardrobe_camera".equals(getIntent().getStringExtra("wardrobe_camera"))) {
            cameraCatogry = 1;//当期调用的是衣橱中弹出框的相机
        }
        if ("calendar_detail".equals(getIntent().getStringExtra("calendar_detail"))) {
            cameraCatogry = 2;//是从日历中跳过来的
//            Bundle extras = getIntent().getExtras();
//            newSelectDay = extras.getInt("newSelectDay");
//            newSelectYear = extras.getInt("newSelectYear");
//            newSelectMonth = extras.getInt("newSelectMonth");
        }
        launchCamera();
    }

    private void launchCamera() {
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        currentPicName = getRandomPicName();
        tempUri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera"), currentPicName));
        // 指定照片保存路径（SD卡）
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, IMAGE_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_RESULT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, tempUri));
                    LogUtils.showVerbose("LunchCameraActivity", "tempUri==" + tempUri);
                    if (cameraCatogry == 1) {
                        progressDialog = new ProgressDialog(this);
                        progressDialog.setMessage("正在添加图片，请稍等...");
                        progressDialog.show();
                        //把照的图片上传到衣橱中
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                postMethod(getPostPicsJsonBody(), Constants.USER_ID + "", Constants.currentClothType);
                            }
                        }).start();
                    }else if (cameraCatogry==2){
                        Intent intent = new Intent(LunchCameraActivity.this, ImagesSelectorActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //这个标志位主要是为了解决中间拍完照片后，不再跳回LunchCameraActivity而是直接跳回WardrobeCalendarDetailActivity
//                        intent.putExtra("calendarDetailCameraImageSelector","calendarDetailCameraImageSelector");
//                        Bundle bundle = new Bundle();
//                        bundle.putInt("newSelectDay",newSelectDay);
//                        bundle.putInt("newSelectYear",newSelectYear);
//                        bundle.putInt("newSelectMonth",newSelectMonth);
//                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                }else if (resultCode==0){
                    if (cameraCatogry==1){
                        Intent intent_tomain = new Intent(LunchCameraActivity.this, MainActivity.class);
                        intent_tomain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent_tomain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent_tomain.putExtra("wardrobe_detail", "wardrobe_detail");
                        startActivity(intent_tomain);
                        finish();
                    }else if (cameraCatogry==2){
                        //当前没有拍照，直接返回
                        Intent intent = new Intent(LunchCameraActivity.this, WardrobeCalendarDetailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //这个标志位主要是为了解决中间拍完照片后，不再跳回LunchCameraActivity而是直接跳回WardrobeCalendarDetailActivity
//                        intent.putExtra("calendarDetailCameraImageSelector","calendarDetailCameraImageSelector");
//                        Bundle bundle = new Bundle();
//                        bundle.putInt("newSelectDay",newSelectDay);
//                        bundle.putInt("newSelectYear",newSelectYear);
//                        bundle.putInt("newSelectMonth",newSelectMonth);
//                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                }
        }
    }

    public String getRandomPicName() {
        String name = "wanda_" + Math.random() + "imag.jpg";
        return name;
    }

    private synchronized void postMethod(String jsonImages, String userid, String type) {
        // 链接超时，请求超时设置
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);
        HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000);

        // 请求参数设置
        HttpClient client = new DefaultHttpClient(httpParams);
        HttpPost post = new HttpPost(
                "http://1zou.me/api/itemupload");
        MultipartEntity entity = new MultipartEntity();
        try {
            entity.addPart("images",
                    new StringBody(jsonImages, Charset.forName("UTF-8")));
            entity.addPart("userid",
                    new StringBody(userid, Charset.forName("UTF-8")));
            entity.addPart("type",
                    new StringBody(type, Charset.forName("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //上传文件
        for (int i = 0; i < 1; i++) {
            String picName = currentPicName;
            try {
                entity.addPart("file" + i, new ByteArrayBody(bitmapToBytes(ImageCompressUtil.compressBySize(tempUri.getPath(), 600, 800)), picName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        post.setEntity(entity);
        HttpResponse resp = null;
        try {
            resp = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            LogUtils.showVerbose("LunchCameraActivity", "Response:" + EntityUtils.toString(resp.getEntity()));
            LogUtils.showVerbose("LunchCameraActivity", "errorcode:" + EntityUtils.getContentCharSet(resp.getEntity()));

            if (resp.getStatusLine().getStatusCode() == 200) {
                progressDialog.dismiss();
                //跳转到首页
                Intent intent_tomain = new Intent(LunchCameraActivity.this, MainActivity.class);
                intent_tomain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent_tomain.putExtra("wardrobe_detail", "wardrobe_detail");
                startActivity(intent_tomain);
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPostPicsJsonBody() {
        JSONArray array = new JSONArray();
        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("imgFile", "file" + i);
                array.put(jsonObject);
            }
        } catch (JSONException e) {
            LogUtils.showVerbose("LunchCameraActivity", "json数据拼接错误");
        }
        return array.toString();
    }

    public byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
