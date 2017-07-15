package com.wanta.mobile.wantaproject.phonepics;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.ByteArrayBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.BaseActivity;
import com.wanta.mobile.wantaproject.activity.ImagesPublishActivity;
import com.wanta.mobile.wantaproject.activity.MainActivity;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.ImageItem;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.DealReturnLogicUtils;
import com.wanta.mobile.wantaproject.utils.ImageCompressUtil;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.ToastUtil;

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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/7.
 */

public class PhonePicsSelectActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView photo_recycleview;
    private final String[] projections = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media._ID};
    private PhonePicsSelectAdapter adapter;
    private int currentSize;
    private MyImageView phone_pics_select_back_icon;
    private LinearLayout phone_pics_select_back_layout;
    private LinearLayout phone_pics_select_ok_layout;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_pics_select);
//        ActivityColection.addActivity(this);
        initId();
        initData();
        LogUtils.showVerbose("PhonePicsSelectActivity","当前的类型是"+Constants.currentClothType);
    }

    private void initId() {
        photo_recycleview = (RecyclerView) this.findViewById(R.id.photo_recycleview);
        //设置当前要选择的图片的个数
        currentSize = getIntent().getIntExtra("currentSize", -1);
        phone_pics_select_back_icon = (MyImageView) this.findViewById(R.id.phone_pics_select_back_icon);
        phone_pics_select_back_icon.setSize(Constants.PHONE_WIDTH / 12, Constants.PHONE_WIDTH / 12);
        phone_pics_select_back_layout = (LinearLayout) this.findViewById(R.id.phone_pics_select_back_layout);
        phone_pics_select_back_layout.setOnClickListener(this);
        phone_pics_select_ok_layout = (LinearLayout) this.findViewById(R.id.phone_pics_select_ok_layout);
        phone_pics_select_ok_layout.setOnClickListener(this);
    }

    private void initData() {
        Constants.SELECT_PIC_URL.clear();
        //获取手机中图片的信息
        getPicMsgOfPhone();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        photo_recycleview.setLayoutManager(gridLayoutManager);
        adapter = new PhonePicsSelectAdapter(this, Constants.SAVE_PHONE_PICTURES);
        adapter.setPhonePicSize(Constants.PHONE_WIDTH / 3, Constants.PHONE_WIDTH / 3);
        adapter.setPhoneIconSize(Constants.PHONE_WIDTH / 12, Constants.PHONE_WIDTH / 12);
        photo_recycleview.setAdapter(adapter);
        adapter.setOnItemPhonePicsSelectIconLayout(new PhonePicsSelectAdapter.OnItemPhonePicsSelectIconLayout() {
            @Override
            public void onItemClick(int position, CustomSimpleDraweeView draweeView, ImageItem imageItem) {

                if (Constants.SELECT_PIC_URL.contains(imageItem.getPath())) {
                    Constants.SELECT_PIC_URL.remove(imageItem.getPath());
                    draweeView.setImageResource(R.mipmap.image_unselected);
                } else {
                    if (Constants.SELECT_PIC_URL.size() < 9) {
                        Constants.SELECT_PIC_URL.add(imageItem.getPath());
                        draweeView.setImageResource(R.mipmap.image_selected);
                    }

                }
            }
        });
    }

    private void getPicMsgOfPhone() {
        Observable.just("").flatMap(new Func1<String, Observable<ImageItem>>() {

            private ContentResolver contentResolver;

            @Override
            public Observable<ImageItem> call(String s) {
                Constants.SAVE_PHONE_PICTURES.clear();
                List<ImageItem> results = new ArrayList<>();

                Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                String where = MediaStore.Images.Media.SIZE + " > " + 50000;
//                        String where = MediaStore.Images.Media.SIZE + " > " + 0;
                String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

                contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(contentUri, projections, where, null, sortOrder);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        int pathCol = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        String path = cursor.getString(pathCol);
                        ImageItem imageItem = new ImageItem(path);
                        results.add(imageItem);
                        Constants.SAVE_PHONE_PICTURES.add(imageItem);
//                        LogUtils.showVerbose("PhonePicsSelectActivity","当前的路径="+path);
                    }
                    cursor.close();
                }
                return Observable.from(results);
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Subscriber<ImageItem>() {
            @Override
            public void onCompleted() {
                Log.v("MainActivity", "完成了");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ImageItem imageItem) {
                Log.v("MainActivity", "iamge==" + imageItem.getPath());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone_pics_select_back_layout:
//                Intent intent_tomain = new Intent(PhonePicsSelectActivity.this, MainActivity.class);
//                intent_tomain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent_tomain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent_tomain.putExtra("wardrobe_detail","wardrobe_detail");
//                startActivity(intent_tomain);
//                finish();
                DealReturnLogicUtils.dealReturnLogic(this);
                break;
            case R.id.phone_pics_select_ok_layout:
                //将图片信息上传上去，然后跳转到图片的详细信息中
                if (Constants.SELECT_PIC_URL.size() == 0) {
                    ToastUtil.show(PhonePicsSelectActivity.this, "选择的图片个数不能为空", Toast.LENGTH_SHORT);
                } else {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("正在上传图片，请稍等...");
                    progressDialog.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.showVerbose("PhonePicsSelectActivity","传递的信息"+"图片信息:"+getPostPicsJsonBody()
                            +"  userid"+Constants.USER_ID+"图片的个数:"+Constants.SELECT_PIC_URL.size());
                            postMethod(getPostPicsJsonBody(),Constants.USER_ID+"", Constants.currentClothType);
                        }
                    }).start();
                }
                break;
        }
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
        for (int i = 0; i < Constants.SELECT_PIC_URL.size(); i++) {
            String picName = Constants.SELECT_PIC_URL.get(i).substring(Constants.SELECT_PIC_URL.get(i).lastIndexOf("/") + 1, Constants.SELECT_PIC_URL.get(i).length());
            try {
                entity.addPart("file" + i, new ByteArrayBody(bitmapToBytes(ImageCompressUtil.compressBySize(Constants.SELECT_PIC_URL.get(i),600,800)), picName));
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
            LogUtils.showVerbose("PhonePicsSelectActivity", "Response:" + EntityUtils.toString(resp.getEntity()));
            LogUtils.showVerbose("PhonePicsSelectActivity", "errorcode:" + EntityUtils.getContentCharSet(resp.getEntity()));

            if (resp.getStatusLine().getStatusCode() == 200) {
                progressDialog.dismiss();
                //跳转到首页
                Intent intent_tomain = new Intent(PhonePicsSelectActivity.this, MainActivity.class);
                intent_tomain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent_tomain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent_tomain.putExtra("wardrobe_detail","wardrobe_detail");
                startActivity(intent_tomain);
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//        LogUtils.showVerbose("MainActivity", "StatusCode:" + resp.getStatusLine().getStatusCode());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            DealReturnLogicUtils.dealReturnLogic(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    public String getPostPicsJsonBody() {
        JSONArray array = new JSONArray();
        try {
            for (int i = 0; i < Constants.SELECT_PIC_URL.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("imgFile", "file" + i);
                array.put(jsonObject);
            }
        } catch (JSONException e) {
            LogUtils.showVerbose("PhonePicsSelectActivity", "json数据拼接错误");
        }
        return array.toString();
    }

    public byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
