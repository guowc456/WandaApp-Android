package com.wanta.mobile.wantaproject.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.ByteArrayBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.ImagePublishtAdapter;
import com.wanta.mobile.wantaproject.adapter.NewPublishTopicsSelectAdapter;
import com.wanta.mobile.wantaproject.customview.ExStaggeredGridLayoutManager;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.TagMsgInfo;
import com.wanta.mobile.wantaproject.uploadimage.ImagesSelectorActivity;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.DealReturnLogicUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.NetUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by WangYongqiang on 2017/2/15.
 */
public class NewPublishActivity extends BaseActivity implements View.OnClickListener {

    private MyImageView mNew_publish_back;
    private LinearLayout new_publish_back_layout;
    private LinearLayout new_publish_ok_layout;
    private RecyclerView new_publish_recycleview;
    private EditText new_publish_title;
    private TextView new_publish_title_length;
    private MyImageView tuwen_swithc_img;
    private MyImageView new_public_weibo_icon;
    private ProgressDialog mProgressDialog;
    private EditText new_publish_content;
    private MyImageView new_public_qq_icon;
    private MyImageView new_publish_stare_icon;
    private RecyclerView new_public_add_topics;
    private int[] topicsArray = new int[]{
            1,2,3,4,5,6,7,8,9,10,11,12,13,14
    };
    private int currentSelectTopics[] = new int[14];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_publish);
        initId();
        initData();
    }

    private void initId() {
        mNew_publish_back = (MyImageView) this.findViewById(R.id.new_publish_back);
        mNew_publish_back.setSize(Constants.PHONE_WIDTH/16,Constants.PHONE_WIDTH/16);
        new_publish_back_layout = (LinearLayout) this.findViewById(R.id.new_publish_back_layout);
        new_publish_back_layout.setOnClickListener(this);
        new_publish_ok_layout = (LinearLayout) this.findViewById(R.id.new_publish_ok_layout);
        new_publish_ok_layout.setOnClickListener(this);
        new_publish_recycleview = (RecyclerView) this.findViewById(R.id.new_publish_recycleview);
        new_publish_title = (EditText) this.findViewById(R.id.new_publish_title);
        new_publish_title_length = (TextView) this.findViewById(R.id.new_publish_title_length);
        new_publish_content = (EditText) this.findViewById(R.id.new_publish_content);
        tuwen_swithc_img = (MyImageView) this.findViewById(R.id.tuwen_swithc_img);
        tuwen_swithc_img.setSize(Constants.PHONE_WIDTH/8,Constants.PHONE_HEIGHT/27);
        new_public_weibo_icon = (MyImageView) this.findViewById(R.id.new_public_weibo_icon);
        new_public_weibo_icon.setSize(Constants.PHONE_WIDTH/10,Constants.PHONE_WIDTH/10);
        new_public_qq_icon = (MyImageView) this.findViewById(R.id.new_public_qq_icon);
        new_public_qq_icon.setSize(Constants.PHONE_WIDTH/12,Constants.PHONE_WIDTH/12);
        new_publish_stare_icon = (MyImageView) this.findViewById(R.id.new_publish_stare_icon);
        new_publish_stare_icon.setSize(Constants.PHONE_WIDTH/12,Constants.PHONE_WIDTH/12);
        new_public_add_topics = (RecyclerView) this.findViewById(R.id.new_public_add_topics);
    }

    private void initData() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        new_publish_recycleview.setLayoutManager(gridLayoutManager);
        ImagePublishtAdapter adapter = new ImagePublishtAdapter(this,Constants.upload_images_lrucache);
        new_publish_recycleview.setAdapter(adapter);
        adapter.setOnImagePublishItemListener(new ImagePublishtAdapter.OnImagePublishItemListener() {
            @Override
            public void onImagePublishItemClick(View view) {
                int click_position = new_publish_recycleview.getChildAdapterPosition(view);
                if (click_position == Constants.IMAGE_URL.size()) {
//                    ToastUtil.showShort(ImagesPublishActivity.this,"点我了");
                    //将选中的图片的信息清空
                    Constants.IMAGE_URL.clear();
                    Constants.modify_bitmap_list.clear();
                    Constants.display_images.clear();
                    Constants.upload_images.clear();
                    Intent intent = new Intent(NewPublishActivity.this, ImagesSelectorActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    for (int i=0;i<Constants.upload_images.size();i++){
                        Constants.modify_bitmap_list.set(i,Constants.upload_images.get(i));
                    }
                    Intent intent = new Intent(NewPublishActivity.this, CameraActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("image_publish_edit", click_position);
                    startActivity(intent);
                }
            }
        });

        new_publish_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                new_publish_title_length.setText("30");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new_publish_title_length.setText((30 - s.length()) + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //设置选择话题的信息
        ExStaggeredGridLayoutManager staggeredGridLayoutManager = new ExStaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setOrientation(StaggeredGridLayoutManager.VERTICAL);
        new_public_add_topics.setLayoutManager(staggeredGridLayoutManager);
        final NewPublishTopicsSelectAdapter topicsLinkRecycleviewAdapter = new NewPublishTopicsSelectAdapter(this,topicsArray);
        new_public_add_topics.setAdapter(topicsLinkRecycleviewAdapter);
        topicsLinkRecycleviewAdapter.setOnTopicsLinkRecycleviewListener(new NewPublishTopicsSelectAdapter.OnTopicsLinkRecycleviewListener() {
            @Override
            public void onItemClick(View view, int[] selectFlag) {
                    topicsLinkRecycleviewAdapter.notifyDataSetChanged();
                for (int i=0;i<selectFlag.length;i++){
                    currentSelectTopics[i] = selectFlag[i];
                }
                for (int j=0;j<currentSelectTopics.length;j++){
                    if (currentSelectTopics[j]==1){
                        LogUtils.showVerbose("NewPublishActivity","当前的位子="+j);
                    }
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_publish_back_layout:
//                Intent intent = new Intent(NewPublishActivity.this, CameraActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
                DealReturnLogicUtils.dealReturnLogic(this);
                break;
            case R.id.new_publish_ok_layout:
                if (Constants.STATUS.equals("reg_log")) {
                    if (NetUtils.checkNet(this)==true){
                        mProgressDialog = new ProgressDialog(this);
                        mProgressDialog.setMessage("图片正在上传,请稍等......");
                        mProgressDialog.show();
                        new Thread() {
                            public void run() {
                                postMethod(getPublishMsg(),null);
                            }
                        }.start();
                    }else {
                        NetUtils.showNoNetDialog(this);
                    }
                }else {
                    Intent register_intent = new Intent(NewPublishActivity.this, LoginActivity.class);
                    register_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    register_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    register_intent.putExtra("new_publish_to_login","new_publish_to_login");
                    startActivity(register_intent);
                    finish();
                }


                break;
        }
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
        for (int i = 0; i < Constants.upload_images_lrucache.size(); i++) {
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
                Constants.saveTagMsgMap.clear();
                //跳转到首页

                Intent intent_tomain = new Intent(NewPublishActivity.this, MainActivity.class);
                intent_tomain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_tomain);
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
                if (Constants.saveTagMsgMap.containsKey(j)){
                    for (Map.Entry<Integer, List<TagMsgInfo>> entry : Constants.saveTagMsgMap.entrySet()) {
                        LogUtils.showVerbose("NewPublishActivity","当前的位置"+entry.getKey()+"和"+entry.getValue().size());
                        if (entry.getKey()==j){
                            for (int i = 0; i < entry.getValue().size(); i++) {
                                TagMsgInfo tagMsgInfo = entry.getValue().get(i);
                                //每个图片上打标签的个数
                                JSONObject fiveObject = new JSONObject();
                                fiveObject.put("pos_x", 0);
                                fiveObject.put("pos_y", 0);
                                fiveObject.put("itemtype", "");
                                fiveObject.put("itemid", "32");
                                fiveObject.put("itemlink", "link2");

                                if ("".equals(tagMsgInfo.getPrice())){
                                    fiveObject.put("price", 0);
                                }else {
                                    fiveObject.put("price", Integer.parseInt(tagMsgInfo.getPrice()));
                                }
                                fiveObject.put("currency", "人民币");
                                fiveObject.put("region", tagMsgInfo.getRegion());
                                fiveObject.put("brand", tagMsgInfo.getBrand());
                                fiveObject.put("description", tagMsgInfo.getDescription());
                                secondArray.put(fiveObject);
                            }
                        }
                    }
                }

                JSONObject fourObject = new JSONObject();
                fourObject.put("imgFile", "imagefile" + j);
                fourObject.put("tag", secondArray);
                firstArray.put(fourObject);
            }
            JSONObject threeObject = new JSONObject();
            threeObject.put("userid", Constants.USER_ID);
            threeObject.put("description", new_publish_content.getText());
            threeObject.put("title", new_publish_title.getText());
            threeObject.put("fashion_style", "fashion2");
            threeObject.put("lng", 120);
            threeObject.put("lat", 30);
            threeObject.put("address", "test address");

            mTwoObject = new JSONObject();
            mTwoObject.put("icon", threeObject);
            mTwoObject.put("image", firstArray);
            //加入icon的信息
            JSONArray topicsArray = new JSONArray();
            for (int i=0;i<currentSelectTopics.length;i++){
                if (currentSelectTopics[i]==1){
                    topicsArray.put(i+1);
                }
            }
            mTwoObject.put("topic",topicsArray);
//
            firstObject = new JSONObject(mTwoObject.toString());
//            firstObject.put("iconInfo", mTwoObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return firstObject.toString();
    }
    public byte[] bitmapToBytes(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (ActivityColection.isContains(this)) {
//                ActivityColection.removeActivity(this);
//                Intent intent = new Intent(NewPublishActivity.this, CameraActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//            }
            DealReturnLogicUtils.dealReturnLogic(this);
        }
        return super.onKeyDown(keyCode, event);
    }
}
