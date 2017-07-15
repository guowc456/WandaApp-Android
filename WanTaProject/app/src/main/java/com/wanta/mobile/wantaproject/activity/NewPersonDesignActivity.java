package com.wanta.mobile.wantaproject.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.EntityIterator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.ByteArrayBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.NewPersonDesignRecycleViewAdapter;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.customview.SingleWheelView;
import com.wanta.mobile.wantaproject.domain.JudgeDate;
import com.wanta.mobile.wantaproject.domain.PersonMessageSerializable;
import com.wanta.mobile.wantaproject.domain.ScreenInfo;
import com.wanta.mobile.wantaproject.domain.WheelMain;
import com.wanta.mobile.wantaproject.uploadimage.ImagesSelectorActivity;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.CutPictureUtils;
import com.wanta.mobile.wantaproject.utils.DateUtils;
import com.wanta.mobile.wantaproject.utils.DealReturnLogicUtils;
import com.wanta.mobile.wantaproject.utils.ImageUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.NetUtils;
import com.wanta.mobile.wantaproject.utils.SimpleDraweeControlUtils;
import com.wanta.mobile.wantaproject.utils.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static com.wanta.mobile.wantaproject.utils.Constants.authorIconUrl;

/**
 * Created by WangYongqiang on 2017/2/22.
 */
public class NewPersonDesignActivity extends BaseActivity implements View.OnClickListener {

    private MyImageView mNew_person_design_back;
    private RecyclerView mNew_person_design_recycleview_often;
    private RecyclerView mNew_person_design_recycleview_body;
    private RecyclerView mNew_person_design_recycleview_waist;
    private RecyclerView mNew_person_design_recycleview_head;
    private RecyclerView mNew_person_design_recycleview_back;
    private MyImageView mNew_person_design_author_next_icon;
    private CustomSimpleDraweeView mNew_person_design_author_icon;
    private LinearLayout mNew_person_design_back_layout;
    private LinearLayout mNew_person_design_author_icon_layout;
    protected static final int CHOOSE_PICTURE = 12;
    protected static final int CHOOSE_PICTURE_CODE = 0;
    protected static final int TAKE_PICTURE = 13;
    protected static final int TAKE_PICTURE_CODE = 1;
    private static final int REMEMBER_SIGN = 7;
    private static final int REMEMBER_OTHER_NAME = 0;
    private Uri tempUri;
    private CutPictureUtils mPictureUtils = new CutPictureUtils(this);
    private Uri imageUri;
    private ProgressDialog mProgressDialog;
    private NewPersonDesignRecycleViewAdapter mOften_adapter;
    private NewPersonDesignRecycleViewAdapter mBody_adapter;
    private NewPersonDesignRecycleViewAdapter mWaist_adapter;
    private NewPersonDesignRecycleViewAdapter mHead_adapter;
    private NewPersonDesignRecycleViewAdapter mBack_adapter;
    private final int NIKE_NAME = 0;//昵称
    private final int PERSON_SIGNATURE = 5;//个性签名
    private final int PERSON_PROFESSION = 4;//职业
    private final int PERSON_LOCATION = 2;//zhiye
    private PopupWindow person_desigin_pop_window;
    private SingleWheelView mMain_wv;
    private TextView mPopwindow_person_design_cancel;
    private TextView mPopwindow_person_design_ok;
    private WheelMain wheelMainDate;
    private LinearLayout new_person_design_ok_layout;
    private String[] bra = new String[]{//罩杯信息
            "A", "B", "C", "D", "DD", "E", "F", "G", "H", "I", "J", "K"
    };
    private boolean isUpdateAuthorIcon = false;//是否更新头像和身体信息
    private boolean isOnlyUpdateAuthorIcon = false;//是否更新头像信息
    private boolean isModifyAuthorMessage = false;//是否更新用户信息了
    private TextView new_person_design_head_tv;
    private LinearLayout system_cancel_select_pop_window_ok_layout;
    private TextView system_cancel_select_pop_window_message_content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_person_design_layout);
        ActivityColection.addActivity(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在保存,请稍等......");
        initId();
        initRecycleview();
    }

    private void initId() {
        new_person_design_head_tv = (TextView) this.findViewById(R.id.new_person_design_head_tv);
        mNew_person_design_back = (MyImageView) this.findViewById(R.id.new_person_design_back);
        mNew_person_design_back.setSize(Constants.PHONE_WIDTH / 16, Constants.PHONE_WIDTH / 16);
        mNew_person_design_back_layout = (LinearLayout) this.findViewById(R.id.new_person_design_back_layout);
        mNew_person_design_back_layout.setOnClickListener(this);
        new_person_design_ok_layout = (LinearLayout) this.findViewById(R.id.new_person_design_ok_layout);
        new_person_design_ok_layout.setOnClickListener(this);
        new_person_design_ok_layout.setVisibility(View.GONE);
        mNew_person_design_author_next_icon = (MyImageView) this.findViewById(R.id.new_person_design_author_next_icon);
        mNew_person_design_author_next_icon.setSize(Constants.PHONE_WIDTH / 16, Constants.PHONE_WIDTH / 16);
        mNew_person_design_author_icon = (CustomSimpleDraweeView) this.findViewById(R.id.new_person_design_author_icon);
        mNew_person_design_author_icon.setWidth(Constants.PHONE_WIDTH / 7);
        mNew_person_design_author_icon.setHeight(Constants.PHONE_WIDTH / 7);
        if (!TextUtils.isEmpty(Constants.AVATAR)) {
            SimpleDraweeControlUtils.getNetRingSimpleDraweeControl(this, mNew_person_design_author_icon, Constants.FIRST_PAGE_IMAGE_URL + Constants.AVATAR);
        } else {
            mNew_person_design_author_icon.setImageResource(R.mipmap.self_head_icon);
        }
        mNew_person_design_author_icon_layout = (LinearLayout) this.findViewById(R.id.new_person_design_author_icon_layout);
        mNew_person_design_author_icon_layout.setOnClickListener(this);
        mNew_person_design_recycleview_often = (RecyclerView) this.findViewById(R.id.new_person_design_recycleview_often);
        mNew_person_design_recycleview_body = (RecyclerView) this.findViewById(R.id.new_person_design_recycleview_body);
        mNew_person_design_recycleview_waist = (RecyclerView) this.findViewById(R.id.new_person_design_recycleview_waist);
        mNew_person_design_recycleview_head = (RecyclerView) this.findViewById(R.id.new_person_design_recycleview_head);
        mNew_person_design_recycleview_back = (RecyclerView) this.findViewById(R.id.new_person_design_recycleview_back);
    }

    private void initRecycleview() {
        //常用的信息展示
        LinearLayoutManager often_layoutManager = new LinearLayoutManager(this);
        often_layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mNew_person_design_recycleview_often.setLayoutManager(often_layoutManager);
        mOften_adapter = new NewPersonDesignRecycleViewAdapter(this, Constants.often_description, 1);
        mNew_person_design_recycleview_often.setAdapter(mOften_adapter);
        //身体的信息展示
        LinearLayoutManager body_layoutManager = new LinearLayoutManager(this);
        body_layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mNew_person_design_recycleview_body.setLayoutManager(body_layoutManager);
        mBody_adapter = new NewPersonDesignRecycleViewAdapter(this, Constants.body_description, 2);
        mNew_person_design_recycleview_body.setAdapter(mBody_adapter);
        //腰部信息的展示
        LinearLayoutManager waist_layoutManager = new LinearLayoutManager(this);
        waist_layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mNew_person_design_recycleview_waist.setLayoutManager(waist_layoutManager);
        mWaist_adapter = new NewPersonDesignRecycleViewAdapter(this, Constants.waist_description, 3);
        mNew_person_design_recycleview_waist.setAdapter(mWaist_adapter);
        //头部信息的展示
        LinearLayoutManager head_layoutManager = new LinearLayoutManager(this);
        head_layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mNew_person_design_recycleview_head.setLayoutManager(head_layoutManager);
        mHead_adapter = new NewPersonDesignRecycleViewAdapter(this, Constants.head_description, 4);
        mNew_person_design_recycleview_head.setAdapter(mHead_adapter);
        //背部信息展示
        LinearLayoutManager back_layoutManager = new LinearLayoutManager(this);
        back_layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mNew_person_design_recycleview_back.setLayoutManager(back_layoutManager);
        mBack_adapter = new NewPersonDesignRecycleViewAdapter(this, Constants.back_description, 5);
        mNew_person_design_recycleview_back.setAdapter(mBack_adapter);

//        //初始化头像
//        if (!TextUtils.isEmpty(authorIconUrl)) {
//            mNew_person_design_author_icon.setImageURI(Uri.fromFile(new File(authorIconUrl)));
//            SimpleDraweeControlUtils.getRingSimpleDraweeControl(this, mNew_person_design_author_icon, authorIconUrl);
//        }

        initData();
    }

    private void initData() {
        //常用信息的点击事件
        mOften_adapter.setOnNewPersonDesignClcik(new NewPersonDesignRecycleViewAdapter.OnNewPersonDesignClcik() {
            @Override
            public void onItemClick(View view) {
                isOnlyUpdateAuthorIcon = false;
                isModifyAuthorMessage = true;
                new_person_design_ok_layout.setVisibility(View.VISIBLE);
                processItemClick(1, mNew_person_design_recycleview_often.getChildAdapterPosition(view));
            }
        });
        //身体的信息点击事件
        mBody_adapter.setOnNewPersonDesignClcik(new NewPersonDesignRecycleViewAdapter.OnNewPersonDesignClcik() {
            @Override
            public void onItemClick(View view) {
                isOnlyUpdateAuthorIcon = false;
                isModifyAuthorMessage = true;
                new_person_design_ok_layout.setVisibility(View.VISIBLE);
                processItemClick(2, mNew_person_design_recycleview_body.getChildAdapterPosition(view));
            }
        });
        //腰部的信息点击事件
        mWaist_adapter.setOnNewPersonDesignClcik(new NewPersonDesignRecycleViewAdapter.OnNewPersonDesignClcik() {
            @Override
            public void onItemClick(View view) {
                isOnlyUpdateAuthorIcon = false;
                isModifyAuthorMessage = true;
                new_person_design_ok_layout.setVisibility(View.VISIBLE);
                processItemClick(3, mNew_person_design_recycleview_waist.getChildAdapterPosition(view));
            }
        });
        //头部信息的点击事件
        mHead_adapter.setOnNewPersonDesignClcik(new NewPersonDesignRecycleViewAdapter.OnNewPersonDesignClcik() {
            @Override
            public void onItemClick(View view) {
                isOnlyUpdateAuthorIcon = false;
                isModifyAuthorMessage = true;
                new_person_design_ok_layout.setVisibility(View.VISIBLE);
                processItemClick(4, mNew_person_design_recycleview_back.getChildAdapterPosition(view));
            }
        });
        //背部信息的点击事件
        mBack_adapter.setOnNewPersonDesignClcik(new NewPersonDesignRecycleViewAdapter.OnNewPersonDesignClcik() {
            @Override
            public void onItemClick(View view) {
                isOnlyUpdateAuthorIcon = false;
                isModifyAuthorMessage = true;
                new_person_design_ok_layout.setVisibility(View.VISIBLE);
                processItemClick(5, mNew_person_design_recycleview_back.getChildAdapterPosition(view));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_person_design_back_layout:
                if (isModifyAuthorMessage == true) {
                    //弹出对话款
                    showCancelSelectDialog();
                } else {
//                    jumpToMainActivity();
                    DealReturnLogicUtils.dealReturnLogic(this);
                }
                break;
            case R.id.new_person_design_author_icon_layout:
                //设置头像的功能
//                LogUtils.showVerbose("NewPersonDesignActivity","头像点击");
                showChoosePicDialog();
                break;
            case R.id.new_person_design_ok_layout:
//                if (isOnlyUpdateAuthorIcon = false){
//                    if (isUpdate()==true){
//                        uploadToService();
//                        LogUtils.showVerbose("NewPersonDesignActivity","必须的信息填写完整");
//                    }else {
////                    LogUtils.showVerbose("NewPersonDesignActivity","必须的信息没有填写完整");
//                        showAlertDialog();
//                    }
//                }else {
//
//                }
                uploadToService();
                new_person_design_ok_layout.setVisibility(View.GONE);

                break;
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("信息填写不完善");
        builder.setMessage("当前标有星号*的信息必须要填写完整");
        builder.setPositiveButton("确定", null);
        builder.show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (ActivityColection.isContains(this)) {
                if (isModifyAuthorMessage == true) {
                    showCancelSelectDialog();
                } else {
//                    ActivityColection.removeActivity(this);
//                    jumpToMainActivity();
                    DealReturnLogicUtils.dealReturnLogic(this);
                }

            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void jumpToMainActivity() {
        Intent intent = new Intent(NewPersonDesignActivity.this, MainActivity.class);
        intent.putExtra("person_design", "person_design");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * 显示修改头像的对话框
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE_CODE: // 选择本地照片
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setDataAndType(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                "image/*");
                        startActivityForResult(intent, CHOOSE_PICTURE);// //适用于4.4及以上android版本

                        break;
                    case TAKE_PICTURE_CODE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的

            switch (requestCode) {
                case TAKE_PICTURE:
                    imageUri = mPictureUtils.crop(tempUri);
                    break;
                case CHOOSE_PICTURE:

                    if (data != null) {
                        Uri uri = data.getData();
                        imageUri = mPictureUtils.crop(uri);
                    }
                    break;
                case Constants.CROP_ACTIVITY_RESULT:
                    if (imageUri != null) {
                        mNew_person_design_author_icon.setImageURI(imageUri);
                        SimpleDraweeControlUtils.getRingSimpleDraweeControl(this, mNew_person_design_author_icon, imageUri.getPath());
                        authorIconUrl = imageUri.getPath();
                        isUpdateAuthorIcon = true;
                        isOnlyUpdateAuthorIcon = true;
                        isModifyAuthorMessage = true;
                        new_person_design_ok_layout.setVisibility(View.VISIBLE);
//                        try {
//                            Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri));
//                            uploadToService(bitmap, Constants.authorIconUrl);
//                        } catch (FileNotFoundException e) {
//                            LogUtils.showVerbose("NewPersonDesignActivity", "产生bitmap对象异常");
//                        }
//                        uploadToService();//上传头像到服务器
                    }
                    break;
                case NIKE_NAME:
                    PersonMessageSerializable nike_name = (PersonMessageSerializable) data.getSerializableExtra("input_message");
                    Constants.often_description[0] = nike_name.getInputMessage();
                    mOften_adapter.notifyDataSetChanged();
                    break;
                case PERSON_SIGNATURE:
                    PersonMessageSerializable person_signature = (PersonMessageSerializable) data.getSerializableExtra("input_message");
                    Constants.often_description[5] = person_signature.getInputMessage();
                    mOften_adapter.notifyDataSetChanged();
                    break;
                case PERSON_PROFESSION:
                    PersonMessageSerializable person_profession = (PersonMessageSerializable) data.getSerializableExtra("person_design_profession");
                    if (!TextUtils.isEmpty(person_profession.getInputMessage())) {
                        Constants.often_description[4] = person_profession.getInputMessage();
                    } else {
                        Constants.often_description[4] = "职业";
                    }
                    mOften_adapter.notifyDataSetChanged();
                    break;
                case PERSON_LOCATION:
                    PersonMessageSerializable person_location = (PersonMessageSerializable) data.getSerializableExtra("person_design_location");
                    if (!TextUtils.isEmpty(person_location.getInputMessage())) {
                        Constants.often_description[2] = person_location.getInputMessage();
                    } else {
                        Constants.often_description[2] = "";
                    }
                    mOften_adapter.notifyDataSetChanged();
                    break;
            }

        }
    }

    //把图片和json信息上传到服务器
    private void uploadToService() {
        if (NetUtils.checkNet(this) == true) {
            mProgressDialog.show();
            if (isOnlyUpdateAuthorIcon == true) {
                //只上传头像信息
                try {
                    final Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            postAuthorIconMethod(getpublishAuthorIconMessage(), bitmap, Constants.authorIconUrl);
                        }
                    }).start();
                } catch (FileNotFoundException e) {
                }


            } else {
                if (isUpdateAuthorIcon == true) {
                    //上传头像和身体信息
                    try {
                        final Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri));
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                postMethod(getpublishMessage(), bitmap, Constants.authorIconUrl);
                            }
                        }).start();
                    } catch (FileNotFoundException e) {
                        LogUtils.showVerbose("NewPersonDesignActivity", "产生bitmap对象异常");
                    }

                } else {
                    //只上传身体信息
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            postDetailMsgMethod(getpublishMessage1());
                        }
                    }).start();

                }
            }

        } else {
            NetUtils.showNoNetDialog(this);
        }

    }

    private synchronized void postAuthorIconMethod(String text, Bitmap bitmap, String iconpath) {
        // 链接超时，请求超时设置
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);
        HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000);

        // 请求参数设置
        HttpClient client = new DefaultHttpClient(httpParams);
        HttpPost post = new HttpPost(
                "http://1zou.me/apisq/userupdate");
        MultipartEntity entity = new MultipartEntity();
        try {
            entity.addPart("userInfo",
                    new StringBody(text, Charset.forName("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //上传头像
        String picName = iconpath.substring(iconpath.lastIndexOf("/") + 1, iconpath.length());
        entity.addPart("imageFile1", new ByteArrayBody(bitmapToBytes(bitmap), picName));
        post.setEntity(entity);
        HttpResponse resp = null;
        try {
            resp = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
//            LogUtils.showVerbose("NewPersonDesignActivity", "Response:" + EntityUtils.toString(resp.getEntity()));
            String response = EntityUtils.toString(resp.getEntity());
            LogUtils.showVerbose("NewPersonDesignActivity", "StatusCode:" + resp.getStatusLine().getStatusCode());
            if (resp.getStatusLine().getStatusCode() == 200) {
                mProgressDialog.dismiss();
                isOnlyUpdateAuthorIcon = false;
                isUpdateAuthorIcon = false;
                isModifyAuthorMessage = false;
                JSONObject jsonObject = new JSONObject(response);
                JSONObject datas = jsonObject.getJSONObject("datas");
                String newAvatar = datas.getString("newAvatar");
                Constants.AVATAR = newAvatar;
                saveAuthorIconMsg();
                setSomeBodyMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private synchronized void postMethod(String text, Bitmap bitmap, String iconpath) {
        // 链接超时，请求超时设置
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);
        HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000);

        // 请求参数设置
        HttpClient client = new DefaultHttpClient(httpParams);
        HttpPost post = new HttpPost(
                "http://1zou.me/apisq/userupdate");
        MultipartEntity entity = new MultipartEntity();
        try {
            entity.addPart("userInfo",
                    new StringBody(text, Charset.forName("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //上传头像
        String picName = iconpath.substring(iconpath.lastIndexOf("/") + 1, iconpath.length());
        entity.addPart("imageFile1", new ByteArrayBody(bitmapToBytes(bitmap), picName));
        post.setEntity(entity);
        HttpResponse resp = null;
        try {
            resp = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            LogUtils.showVerbose("NewPersonDesignActivity", "Response:" + EntityUtils.toString(resp.getEntity()));
            LogUtils.showVerbose("NewPersonDesignActivity", "StatusCode:" + resp.getStatusLine().getStatusCode());
            if (resp.getStatusLine().getStatusCode() == 200) {
                mProgressDialog.dismiss();
                isUpdateAuthorIcon = false;
                isOnlyUpdateAuthorIcon = false;
                isModifyAuthorMessage = false;
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONObject datas = jsonObject.getJSONObject("datas");
                String newAvatar = datas.getString("newAvatar");
                Constants.AVATAR = newAvatar;
                setSomeBodyMessage();
                saveAuthorIconMsg();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public String getpublishAuthorIconMessage() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", Constants.USER_ID);
            jsonObject.put("avatar", "imageFile1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String getpublishMessage() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", Constants.USER_ID);
            jsonObject.put("avatar", "imageFile1");
            jsonObject.put("nname", Constants.often_description[0]);
            jsonObject.put("name", Constants.often_description[0]);
            jsonObject.put("gender", Constants.often_description[1]);
            jsonObject.put("address", Constants.often_description[2]);
            jsonObject.put("birthd", Constants.often_description[3]);
            jsonObject.put("career", Constants.often_description[4]);
            jsonObject.put("sign", Constants.often_description[5]);

//            jsonObject.put("height",Integer.getInteger(Constants.body_description[1]));
            jsonObject.put("height", getIntegerData(Constants.body_description[1]));
            jsonObject.put("weight", getIntegerData(Constants.body_description[2]));
            jsonObject.put("underbustgirth", getIntegerData(Constants.body_description[3]));
            jsonObject.put("bra", Constants.body_description[4]);
            jsonObject.put("waistline", getIntegerData(Constants.body_description[5]));
            jsonObject.put("hipline", getIntegerData(Constants.body_description[6]));


            jsonObject.put("bodylength", getIntegerData(Constants.waist_description[0]));
            jsonObject.put("armpitgirth", getIntegerData(Constants.waist_description[1]));
            jsonObject.put("clanipnav", getIntegerData(Constants.waist_description[1]));
            jsonObject.put("armlength", getIntegerData(Constants.waist_description[3]));
            jsonObject.put("navelcrotch", getIntegerData(Constants.waist_description[4]));
            jsonObject.put("navelankle", getIntegerData(Constants.waist_description[5]));

            jsonObject.put("headgirth", getIntegerData(Constants.head_description[0]));
            jsonObject.put("neckcir", getIntegerData(Constants.head_description[1]));
            jsonObject.put("bust", getIntegerData(Constants.head_description[2]));
            jsonObject.put("realunderbustgirth", getIntegerData(Constants.head_description[3]));
            jsonObject.put("upperarm", getIntegerData(Constants.head_description[4]));
            jsonObject.put("shoulderbreadth", getIntegerData(Constants.head_description[5]));
            jsonObject.put("thighcir", getIntegerData(Constants.head_description[6]));

            jsonObject.put("acrosswidth", getIntegerData(Constants.back_description[0]));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String getpublishMessage1() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", Constants.USER_ID);
//            jsonObject.put("avatar", "imageFile1");
            jsonObject.put("nname", Constants.often_description[0]);
            jsonObject.put("name", Constants.often_description[0]);
            jsonObject.put("gender", Constants.often_description[1]);
            jsonObject.put("address", Constants.often_description[2]);
            jsonObject.put("birthd", Constants.often_description[3]);
            jsonObject.put("career", Constants.often_description[4]);
            jsonObject.put("sign", Constants.often_description[5]);

//            jsonObject.put("height",Integer.getInteger(Constants.body_description[1]));
            jsonObject.put("height", getIntegerData(Constants.body_description[1]));
            jsonObject.put("weight", getIntegerData(Constants.body_description[2]));
            jsonObject.put("underbustgirth", getIntegerData(Constants.body_description[3]));
            jsonObject.put("bra", Constants.body_description[4]);
            jsonObject.put("waistline", getIntegerData(Constants.body_description[5]));
            jsonObject.put("hipline", getIntegerData(Constants.body_description[6]));


            jsonObject.put("bodylength", getIntegerData(Constants.waist_description[0]));
            jsonObject.put("armpitgirth", getIntegerData(Constants.waist_description[1]));
            jsonObject.put("clanipnav", getIntegerData(Constants.waist_description[1]));
            jsonObject.put("armlength", getIntegerData(Constants.waist_description[3]));
            jsonObject.put("navelcrotch", getIntegerData(Constants.waist_description[4]));
            jsonObject.put("navelankle", getIntegerData(Constants.waist_description[5]));

            jsonObject.put("headgirth", getIntegerData(Constants.head_description[0]));
            jsonObject.put("neckcir", getIntegerData(Constants.head_description[1]));
            jsonObject.put("bust", getIntegerData(Constants.head_description[2]));
            jsonObject.put("realunderbustgirth", getIntegerData(Constants.head_description[3]));
            jsonObject.put("upperarm", getIntegerData(Constants.head_description[4]));
            jsonObject.put("shoulderbreadth", getIntegerData(Constants.head_description[5]));
            jsonObject.put("thighcir", getIntegerData(Constants.head_description[6]));

            jsonObject.put("acrosswidth", getIntegerData(Constants.back_description[0]));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    //上传身体的具体信息
    private synchronized void postDetailMsgMethod(String text) {
        // 链接超时，请求超时设置
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);
        HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000);

        // 请求参数设置
        HttpClient client = new DefaultHttpClient(httpParams);
        HttpPost post = new HttpPost(
                "http://1zou.me/apisq/userupdateP");
        MultipartEntity entity = new MultipartEntity();
        try {
            entity.addPart("userInfo",
                    new StringBody(text, Charset.forName("UTF-8")));
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
            LogUtils.showVerbose("NewPersonDesignActivity", "Response:" + EntityUtils.toString(resp.getEntity()));
//            LogUtils.showVerbose("PersonalDesignActivity", "errorcode:" + EntityUtils.getContentCharSet(resp.getEntity()));
            LogUtils.showVerbose("NewPersonDesignActivity", "StatusCode:" + resp.getStatusLine().getStatusCode());
            if (resp.getStatusLine().getStatusCode() == 200) {
                mProgressDialog.dismiss();
                isModifyAuthorMessage = false;
                setSomeBodyMessage();
                saveAuthorIconMsg();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置自己的部分信息
     */
    public void setSomeBodyMessage() {
        if ("体重".equals(Constants.body_array[2])) {
            Constants.WEIGHT = "0";
        } else {
            Constants.WEIGHT = Constants.body_array[2];
        }
        if ("身高".equals(Constants.body_array[1])) {
            Constants.HEIGHT = "0";
        } else {
            Constants.HEIGHT = Constants.body_array[1];
        }
        if ("罩杯".equals(Constants.body_array[4])) {
            Constants.BRA = "";
        } else {
            Constants.BRA = Constants.body_array[4];
        }
        if ("乳点围/胸围".equals(Constants.head_array[2])) {
            Constants.BUST = "";
        } else {
            Constants.BUST = Constants.head_array[2];
        }
        if ("常居地".equals(Constants.often_array[2])) {
            Constants.UADRESS = "";
        } else {
            Constants.UADRESS = Constants.often_array[2];
        }

    }

    /**
     * //分别处理不同条目的点击事件
     *
     * @param type     1表示常用的信息展示，2表示身体的信息展示，3表示腰部信息的展示，
     *                 4表示头部信息的展示，5表示背部信息展示，
     * @param position 表示当前点击的位置
     */
    public void processItemClick(int type, int position) {
        switch (position) {
            case 0:
                if (type == 1) {
                    //修改昵称
                    jumpToItemMessage(0);
                } else if (type == 2) {
                    //测量标准
                    Intent intent = new Intent(NewPersonDesignActivity.this, CeLiangBiaoZhunActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if (type == 3) {
                    //体长
                    showSingleLineDialog(3, 0, getDataArray(140, 180));
                } else if (type == 4) {
                    //头围
                    showSingleLineDialog(4, 0, getDataArray(46, 60));
                } else if (type == 5) {
                    //背宽
                    showSingleLineDialog(5, 0, getDataArray(25, 50));
                }
//                LogUtils.showVerbose("NewPersonDesignActivity","type="+type+"  position="+position);
                break;
            case 1:
                if (type == 1) {

                    showSingleLineDialog(1, 1, new String[]{"男", "女"});
                } else if (type == 2) {
                    showSingleLineDialog(2, 1, getDataArray(145, 200));
                } else if (type == 3) {
                    showSingleLineDialog(3, 1, getDataArray(25, 50));
                } else if (type == 4) {
                    showSingleLineDialog(4, 1, getDataArray(35, 60));
                } else if (type == 5) {

                }
                LogUtils.showVerbose("NewPersonDesignActivity", "type=" + type + "  position=" + position);
                break;
            case 2:
                if (type == 1) {
                    //常居地的信息
                    jumpToPersonDesignLocation();
                } else if (type == 2) {
                    showSingleLineDialog(2, 2, getDataArray(35, 100));
                } else if (type == 3) {
                    showSingleLineDialog(3, 2, getDataArray(30, 50));
                } else if (type == 4) {
                    showSingleLineDialog(4, 2, getDataArray(55, 100));
                } else if (type == 5) {

                }
                LogUtils.showVerbose("NewPersonDesignActivity", "type=" + type + "  position=" + position);
                break;
            case 3:
                if (type == 1) {
                    showBottoPopupWindow(3);
                } else if (type == 2) {
                    showSingleLineDialog(2, 3, getDataArray(65, 100));
                } else if (type == 3) {
                    showSingleLineDialog(3, 3, getDataArray(50, 70));
                } else if (type == 4) {
                    showSingleLineDialog(4, 3, getDataArray(75, 120));
                } else if (type == 5) {

                }
                LogUtils.showVerbose("NewPersonDesignActivity", "type=" + type + "  position=" + position);
                break;
            case 4:
                if (type == 1) {
                    jumpToPersonDesignProfession();
                } else if (type == 2) {
                    showSingleLineDialog(2, 4, bra);
                } else if (type == 3) {
                    showSingleLineDialog(3, 4, getDataArray(15, 30));
                } else if (type == 4) {
                    showSingleLineDialog(4, 4, getDataArray(40, 80));
                } else if (type == 5) {

                }
                LogUtils.showVerbose("NewPersonDesignActivity", "type=" + type + "  position=" + position);
                break;
            case 5:
                if (type == 1) {
                    //修改个性签名
                    jumpToItemMessage(5);
                } else if (type == 2) {
                    showSingleLineDialog(2, 5, getDataArray(70, 105));
                } else if (type == 3) {
                    showSingleLineDialog(3, 5, getDataArray(80, 120));
                } else if (type == 4) {

                } else if (type == 5) {

                }
                LogUtils.showVerbose("NewPersonDesignActivity", "type=" + type + "  position=" + position);
                break;
            case 6:
                if (type == 1) {

                } else if (type == 2) {
                    showSingleLineDialog(2, 6, getDataArray(60, 90));
                } else if (type == 3) {

                } else if (type == 4) {

                } else if (type == 5) {

                }
                LogUtils.showVerbose("NewPersonDesignActivity", "type=" + type + "  position=" + position);
                break;
            case 7:
                if (type == 1) {

                } else if (type == 2) {
                    showSingleLineDialog(2, 7, getDataArray(25, 60));
                } else if (type == 3) {

                } else if (type == 4) {

                } else if (type == 5) {

                }
                LogUtils.showVerbose("NewPersonDesignActivity", "type=" + type + "  position=" + position);
                break;
            case 8:
                if (type == 1) {

                } else if (type == 2) {
                    showSingleLineDialog(2, 8, getDataArray(30, 60));
                } else if (type == 3) {

                } else if (type == 4) {

                } else if (type == 5) {

                }
                LogUtils.showVerbose("NewPersonDesignActivity", "type=" + type + "  position=" + position);
                break;
        }
    }

    //修改昵称和个性签名
    public void jumpToItemMessage(int position) {
        Intent intent = new Intent(NewPersonDesignActivity.this, ItemPersonDesigenMessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("click_position", position);
        intent.putExtras(bundle);
        startActivityForResult(intent, position);
    }

    public void jumpToPersonDesignProfession() {
        Intent intent = new Intent(NewPersonDesignActivity.this, PersonDesignProfessionActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivityForResult(intent, 4);
    }

    public void jumpToPersonDesignLocation() {
        Intent intent = new Intent(NewPersonDesignActivity.this, NewPersonDesignLocationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 2);
    }

    private void showSingleLineDialog(final int type, final int position, String[] strings) {
        final String[] itemMessage = new String[1];
        itemMessage[0] = strings[0];

        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_windows_person_design_layout, null);
        person_desigin_pop_window = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);

        person_desigin_pop_window.setBackgroundDrawable(new BitmapDrawable());
        person_desigin_pop_window.setOutsideTouchable(true);
        person_desigin_pop_window.setFocusable(true);
        person_desigin_pop_window.setTouchable(true);
        //	set_pop.setAnimationStyle(R.style.Anim_popWindow_in);
        //set_pop.showAtLocation(frame,Gravity.TOP, 0, 0);
//        person_desigin_pop_window.showAsDropDown(mNew_person_design_back, 0, Constants.PHONE_HEIGHT / 5 * 3);
        person_desigin_pop_window.showAtLocation(mNew_person_design_back, Gravity.BOTTOM, 0, 0);
        View my_person_design_pop = person_desigin_pop_window.getContentView();
        mMain_wv = (SingleWheelView) my_person_design_pop.findViewById(R.id.main_wv);
        mMain_wv.setOffset(1);
        mMain_wv.setItems(Arrays.asList(strings));
        mMain_wv.setOnWheelViewListener(new SingleWheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                itemMessage[0] = item;
                mPopwindow_person_design_ok.setTextColor(getResources().getColor(R.color.black));
            }
        });
        mPopwindow_person_design_cancel = (TextView) my_person_design_pop.findViewById(R.id.popwindow_person_design_cancel);
        mPopwindow_person_design_ok = (TextView) my_person_design_pop.findViewById(R.id.popwindow_person_design_ok);
        mPopwindow_person_design_ok.setTextColor(getResources().getColor(R.color.black));
        mPopwindow_person_design_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemMessage[0] = null;
                person_desigin_pop_window.dismiss();
            }
        });
        mPopwindow_person_design_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unit = " cm";
                mPopwindow_person_design_ok.setTextColor(getResources().getColor(R.color.head_bg_color));
                if (type == 1) {
                    Constants.often_description[position] = itemMessage[0];
                    mOften_adapter.notifyDataSetChanged();
                } else if (type == 2) {
                    if (position == 0 || position == 4) {
                        unit = "";
                    } else if (position == 2) {
                        unit = " kg";
                    }
                    Constants.body_description[position] = itemMessage[0] + unit;
                    mBody_adapter.notifyDataSetChanged();
                } else if (type == 3) {
                    Constants.waist_description[position] = itemMessage[0] + unit;
                    mWaist_adapter.notifyDataSetChanged();
                } else if (type == 4) {
                    Constants.head_description[position] = itemMessage[0] + unit;
                    mHead_adapter.notifyDataSetChanged();
                } else if (type == 5) {
                    Constants.back_description[position] = itemMessage[0] + unit;
                    mBack_adapter.notifyDataSetChanged();
                }
                person_desigin_pop_window.dismiss();
            }
        });
    }

    public void showBottoPopupWindow(final int position) {
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        View menuView = LayoutInflater.from(this).inflate(R.layout.show_popup_window, null);
        final PopupWindow mPopupWindow = new PopupWindow(menuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ScreenInfo screenInfoDate = new ScreenInfo(this);
        wheelMainDate = new WheelMain(menuView, true);
        wheelMainDate.screenheight = screenInfoDate.getHeight();
        String time = DateUtils.currentMonth().toString();
        Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-DD")) {
            try {
                calendar.setTime(new Date(time));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMainDate.initDateTimePicker(year, month, day);
        final String currentTime = wheelMainDate.getTime().toString();
        mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAtLocation(mNew_person_design_back, Gravity.BOTTOM, 0, 0);
        TextView tv_cancle = (TextView) menuView.findViewById(R.id.tv_cancle);
        final TextView tv_ensure = (TextView) menuView.findViewById(R.id.tv_ensure);
        tv_ensure.setTextColor(getResources().getColor(R.color.black));
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mPopupWindow.dismiss();
            }
        });
        tv_ensure.setOnClickListener(new View.OnClickListener() {

            private String beginTime;

            @Override
            public void onClick(View arg0) {
                beginTime = wheelMainDate.getTime().toString();
                Constants.often_description[position] = DateUtils.formateString(beginTime, DateUtils.yyyyMMdd);
                mOften_adapter.notifyDataSetChanged();
                tv_ensure.setTextColor(getResources().getColor(R.color.head_bg_color));
                mPopupWindow.dismiss();
            }
        });
    }

    public String[] getDataArray(int start, int end) {
        String[] array = new String[end - start + 1];
        for (int i = start; i <= end; i++) {
            array[i - start] = i + "";
        }
        return array;
    }

    public int getIntegerData(String str) {
        str = str.trim();
        String str2 = "";
        int integer = 0;
        if (str != null && !"".equals(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    str2 += str.charAt(i);
                }
            }
        }
        if (!TextUtils.isEmpty(str2)) {
            LogUtils.showVerbose("NewPersonDesignActivity", "str==" + str2);
            integer = Integer.parseInt(str2);
        } else {
            integer = 0;
        }
        return integer;
    }

    public void showCancelSelectDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.system_cancel_select_pop_window_layout, null);
        final PopupWindow popupWindow = new PopupWindow(view, Constants.PHONE_WIDTH, Constants.PHONE_HEIGHT, false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.showAsDropDown(new_person_design_head_tv);
        View contentView = popupWindow.getContentView();
        LinearLayout system_cancel_select_pop_window_message_layout = (LinearLayout) contentView.findViewById(R.id.system_cancel_select_pop_window_message_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) ((Constants.PHONE_WIDTH * 1.00 / 7) * 5),
                Constants.PHONE_HEIGHT / 4);
        system_cancel_select_pop_window_message_layout.setLayoutParams(params);
        system_cancel_select_pop_window_message_content = (TextView) contentView.findViewById(R.id.system_cancel_select_pop_window_message_content);
        system_cancel_select_pop_window_message_content.setText("真的要放弃对个人信息的编辑吗?");
        LinearLayout system_cancel_select_pop_window_cancel_layout = (LinearLayout) contentView.findViewById(R.id.system_cancel_select_pop_window_cancel_layout);
        system_cancel_select_pop_window_cancel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
//               jumpToMainActivity();
                DealReturnLogicUtils.dealReturnLogic(NewPersonDesignActivity.this);
            }
        });
        system_cancel_select_pop_window_ok_layout = (LinearLayout) contentView.findViewById(R.id.system_cancel_select_pop_window_ok_layout);
        system_cancel_select_pop_window_ok_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
//
//    public boolean isUpdate(){
//        if (TextUtils.isEmpty(Constants.often_description[1])||TextUtils.isEmpty(Constants.often_description[2])
//                ||Constants.often_description[3].equals("1990-1-1")||
//                getIntegerData(Constants.body_description[1])==0||getIntegerData(Constants.body_description[2])==0
//                ||getIntegerData(Constants.body_description[3])==0||isSelectBar(Constants.body_description[4])==false
//                ||getIntegerData(Constants.body_description[5])==0||getIntegerData(Constants.body_description[6])==0
//                ){
//            return false;
//        }else {
//            return true;
//        }
//    }
//    //判断罩杯是否进行选择了
//    public boolean isSelectBar(String str){
//        for (int i=0;i<bra.length;i++){
//            if (str.equals(bra[i])){
//                return true;
//            }
//        }
//        if (getIntegerData(str)==0){
//            return false;
//        }
//        return false;
//    }

    //保存当前的头像的信息
    public void saveAuthorIconMsg() {
        SharedPreferences sharedPreferences = getSharedPreferences("saveloginmsg", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("atatar", Constants.AVATAR);
        edit.commit();
    }
}
