package com.wanta.mobile.wantaproject.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.ByteArrayBody;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.ImagePublishtAdapter;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.uploadimage.ImagesSelectorActivity;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.ImageCompressUtil;
import com.wanta.mobile.wantaproject.utils.ImageUtils;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/11/17.
 */
public class ImagesPublishActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mPublish_recycleview;
    private MyImageView mPublish_back;
    private LinearLayout mIamge_save_publish;
    private EditText mPublish_title;
    private TextView mPublish_title_length;
    private MyImageView mPublish_share_google;
    private MyImageView mPublish_share_xinlang;
    private MyImageView mPublish_share_kongjian;
    private EditText mPublish_content;
    private ProgressDialog mProgressDialog;
    private MyImageView mPublish_location_icon;
    private LinearLayout mPublish_back_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_publish);
        ActivityColection.addActivity(this);
        init();
    }

    private void init() {
        mPublish_recycleview = (RecyclerView) this.findViewById(R.id.publish_recycleview);
        mPublish_back = (MyImageView) this.findViewById(R.id.publish_back);
        mPublish_back.setSize(Constants.PHONE_WIDTH / 14, Constants.PHONE_WIDTH / 14);
//        mPublish_back.setOnClickListener(this);
        mPublish_back_layout = (LinearLayout) this.findViewById(R.id.publish_back_layout);
        mPublish_back_layout.setOnClickListener(this);
        mIamge_save_publish = (LinearLayout) this.findViewById(R.id.iamge_save_publish);
        mIamge_save_publish.setOnClickListener(this);
        mPublish_title = (EditText) this.findViewById(R.id.publish_title);
        mPublish_title_length = (TextView) this.findViewById(R.id.publish_title_length);
        mPublish_share_google = (MyImageView) this.findViewById(R.id.publish_share_google);
        mPublish_share_google.setSize(Constants.PHONE_WIDTH / 16, Constants.PHONE_WIDTH / 16);
        mPublish_share_xinlang = (MyImageView) this.findViewById(R.id.publish_share_xinlang);
        mPublish_share_xinlang.setSize(Constants.PHONE_WIDTH / 16, Constants.PHONE_WIDTH / 16);
        mPublish_share_kongjian = (MyImageView) this.findViewById(R.id.publish_share_kongjian);
        mPublish_share_kongjian.setSize(Constants.PHONE_WIDTH / 16, Constants.PHONE_WIDTH / 16);
        mPublish_content = (EditText) this.findViewById(R.id.publish_content);
        mPublish_location_icon = (MyImageView) this.findViewById(R.id.publish_location_icon);
        mPublish_location_icon.setSize(Constants.PHONE_WIDTH/10,Constants.PHONE_WIDTH/10);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mPublish_recycleview.setLayoutManager(linearLayoutManager);
        ImagePublishtAdapter adapter = new ImagePublishtAdapter(this,Constants.upload_images_lrucache);
        mPublish_recycleview.setAdapter(adapter);
        adapter.setOnImagePublishItemListener(new ImagePublishtAdapter.OnImagePublishItemListener() {
            @Override
            public void onImagePublishItemClick(View view) {
                int click_position = mPublish_recycleview.getChildAdapterPosition(view);
                if (click_position == Constants.IMAGE_URL.size()) {
//                    ToastUtil.showShort(ImagesPublishActivity.this,"点我了");
                    //将选中的图片的信息清空
                    Constants.IMAGE_URL.clear();
                    Constants.modify_bitmap_list.clear();
                    Constants.display_images.clear();
                    Constants.upload_images.clear();
                    Intent intent = new Intent(ImagesPublishActivity.this, ImagesSelectorActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    for (int i=0;i<Constants.upload_images.size();i++){
                        Constants.modify_bitmap_list.set(i,Constants.upload_images.get(i));
                    }
                    Intent intent = new Intent(ImagesPublishActivity.this, CameraActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("image_publish_edit", click_position);
                    startActivity(intent);
                }
            }
        });
        mPublish_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mPublish_title_length.setText("30");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LogUtils.showVerbose("ImagesPublishActivity", "长度:" + s.length());
                mPublish_title_length.setText((30 - s.length()) + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iamge_save_publish:
                //将数据发送到服务器
//                new Thread(){
//                    @Override
//                    public void run() {
//                        uploadImagesAndMessage();
//                    }
//                }.start();
//                final List<String> list = new ArrayList<>();
//                list.add("/storage/emulated/0/Download/pic.png");
//                list.add("/storage/emulated/0/Download/pic.png");
//                list.add("/storage/emulated/0/Download/pic.png");
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("图片正在上传,请稍等......");
                mProgressDialog.show();
                new Thread() {
                    public void run() {
                        postMethod(getPublishMsg(),null);
                    }
                }.start();

                break;
            case R.id.publish_back_layout:
                Intent intent = new Intent(ImagesPublishActivity.this, CameraActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (ActivityColection.isContains(this)) {
                ActivityColection.removeActivity(this);
                Intent intent = new Intent(ImagesPublishActivity.this, CameraActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public byte[] bitmapToBytes(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    /**
     * 上传图片的信息
     *
     * @return
     */
    public String getPublishMsg() {
        //拼接每个图片的信息
        JSONObject firstObject = null;
        JSONObject mTwoObject = null;
        try {
            JSONArray firstArray = new JSONArray();
            for (int j = 0; j < Constants.upload_images_url.size(); j++) {
                //选中的图片的个数
                JSONArray secondArray = new JSONArray();
                for (int i = 0; i < 1; i++) {
                    //每个图片上打标签的个数
                    JSONObject fiveObject = new JSONObject();
                    fiveObject.put("pos_x", 0);
                    fiveObject.put("pos_y", 0);
                    fiveObject.put("itemtype", "trousers");
                    fiveObject.put("itemid", "32");
                    fiveObject.put("itemlink", "link2");
                    secondArray.put(fiveObject);
                }

                JSONObject fourObject = new JSONObject();
                fourObject.put("imgFile", "imagefile" + j);
                fourObject.put("tag", secondArray);
                firstArray.put(fourObject);
            }
            JSONObject threeObject = new JSONObject();
            threeObject.put("userid", 9);
            threeObject.put("description", mPublish_content.getText());
            threeObject.put("title", mPublish_title.getText());
            threeObject.put("fashion_style", "fashion2");
            threeObject.put("lng", 120);
            threeObject.put("lat", 30);
            threeObject.put("address", "test address");

            mTwoObject = new JSONObject();
            mTwoObject.put("icon", threeObject);
            mTwoObject.put("image", firstArray);
//
            firstObject = new JSONObject(mTwoObject.toString());
//            firstObject.put("iconInfo", mTwoObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return firstObject.toString();
    }
    private synchronized void postMethod(String text,
                                         List<String> imageUrlList) {
        // 链接超时，请求超时设置
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);
        HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000);

        // 请求参数设置
        HttpClient client = new DefaultHttpClient(httpParams);
        HttpPost post = new HttpPost(
                "http://1zou.me/apisq/addIcon");
        MultipartEntity entity = new MultipartEntity();
        try {
            entity.addPart("iconInfo",
                    new StringBody(text, Charset.forName("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //上传文件
        for (int i = 0; i < Constants.SAVE_PHONE_PICTURES.size(); i++) {
//            entity.addPart("imagefile" + i, new FileBody(new File("/storage/emulated/0/Download/IMG_20161128_134943.jpg"), "image/*"));
            String picName = Constants.IMAGE_URL.get(i).substring(Constants.IMAGE_URL.get(i).lastIndexOf("/")+1,Constants.IMAGE_URL.get(i).length());
//            LogUtils.showVerbose("ImagesPublishActivity","picName="+Constants.upload_images_url.get(i));
            try {
                entity.addPart("imagefile" + i,new ByteArrayBody(bitmapToBytes(Constants.upload_images_lrucache.get(Constants.upload_images_url.get(i))),picName));
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
            LogUtils.showVerbose("MainActivity", "Response:" + EntityUtils.toString(resp.getEntity()));
            LogUtils.showVerbose("MainActivity", "errorcode:" + EntityUtils.getContentCharSet(resp.getEntity()));

          if (resp.getStatusLine().getStatusCode()==200){
              mProgressDialog.dismiss();
              Constants.IMAGE_URL.clear();
              Constants.modify_bitmap_list.clear();
              Constants.upload_images.clear();
              Constants.upload_images_lrucache.evictAll();
              Constants.upload_images_url.clear();
              Constants.modify_bitmap_list_lrucache.evictAll();
              Constants.modify_bitmap_list_url.clear();
              //跳转到首页
              Intent intent_tomain = new Intent(ImagesPublishActivity.this, MainActivity.class);
              intent_tomain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(intent_tomain);
              finish();
          }

        } catch (Exception e) {
            e.printStackTrace();
        }
//        LogUtils.showVerbose("MainActivity", "StatusCode:" + resp.getStatusLine().getStatusCode());
    }
}

