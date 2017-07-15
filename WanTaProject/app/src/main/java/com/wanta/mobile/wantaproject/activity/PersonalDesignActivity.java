package com.wanta.mobile.wantaproject.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.ByteArrayBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.PersonDesignRecycleViewAdapter;
import com.wanta.mobile.wantaproject.customview.DividerItemDecoration;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.customview.SingleWheelView;
import com.wanta.mobile.wantaproject.domain.JudgeDate;
import com.wanta.mobile.wantaproject.domain.PersonMessageSerializable;
import com.wanta.mobile.wantaproject.domain.ScreenInfo;
import com.wanta.mobile.wantaproject.domain.WheelMain;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.CutPictureUtils;
import com.wanta.mobile.wantaproject.utils.DateUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WangYongqiang on 2016/11/25.
 */
public class PersonalDesignActivity extends BaseActivity implements View.OnClickListener {

    private MyImageView mPerson_design_back;
    private TextView mPerson_design_title;
    private RecyclerView mPerson_design_recycleview;
    private String mContentList[] = {
            "未填写", "未选择", "未选择", "未填写", "未填写", "未填写", "未选择", "未填写"
    };
    private final String[] BODY_SEX = new String[]{"男", "女"};
    private final String[] BODY_Height = null;
    private PersonDesignRecycleViewAdapter mPersonDesignRecycleViewAdapter;
    private TextView mPerson_design_waring_msg;
    private PopupWindow person_desigin_pop_window;
    private LinearLayout mPerson_design_head_layout;
    private SingleWheelView mMain_wv;
    private TextView mPopwindow_person_design_cancel;
    private TextView mPopwindow_person_design_ok;
    private WheelMain wheelMainDate;
    private java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    protected static final int CHOOSE_PICTURE = 12;
    protected static final int CHOOSE_PICTURE_CODE = 0;
    protected static final int TAKE_PICTURE = 13;
    protected static final int TAKE_PICTURE_CODE = 1;
    private static final int CROP_SMALL_PICTURE = 14;
    private static final int REMEMBER_SIGN = 7;
    private static final int REMEMBER_OTHER_NAME = 0;
    private Uri tempUri;
    private String imagePath;
    private MyImageView mPerson_design_author_icon;
    private LinearLayout mPerson_design_author_layout;
    private MyImageView mPerson_design_next_icon1;
    private CutPictureUtils mPictureUtils = new CutPictureUtils(this);
    private Uri imageUri;
    private TextView mPerson_design_ok;
    private ProgressDialog mProgressDialog;
    private boolean isSaveModify = false;//默认是没有保存修改的

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_design);
        ActivityColection.addActivity(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在保存,请稍等......");
        initId();
    }

    private void initId() {
        if (Constants.PERSON_DESIGN_MESSAGE.size() == 0) {
            for (int i = 0; i < mContentList.length; i++) {
                Constants.PERSON_DESIGN_MESSAGE.add(mContentList[i]);
            }
        }
        mPerson_design_author_layout = (LinearLayout) this.findViewById(R.id.person_design_author_layout);
        mPerson_design_author_layout.setOnClickListener(this);
        mPerson_design_author_icon = (MyImageView) this.findViewById(R.id.person_design_author_icon);
        mPerson_design_author_icon.setSize(Constants.PHONE_WIDTH / 8, Constants.PHONE_WIDTH / 8);
        mPerson_design_next_icon1 = (MyImageView) this.findViewById(R.id.person_design_next_icon1);
        mPerson_design_next_icon1.setSize(Constants.PHONE_WIDTH / 20, Constants.PHONE_WIDTH / 20);
        mPerson_design_back = (MyImageView) this.findViewById(R.id.person_design_back);
        mPerson_design_back.setSize(Constants.PHONE_WIDTH / 15, Constants.PHONE_WIDTH / 15);
        mPerson_design_back.setOnClickListener(this);
        mPerson_design_ok = (TextView) this.findViewById(R.id.person_design_ok);
        mPerson_design_ok.setOnClickListener(this);
        mPerson_design_ok.setVisibility(View.INVISIBLE);
        mPerson_design_title = (TextView) this.findViewById(R.id.person_design_title);
//        mPerson_design_title.setTextSize(Constants.PHONE_WIDTH/40);
        mPerson_design_waring_msg = (TextView) this.findViewById(R.id.person_design_waring_msg);
//        mPerson_design_waring_msg.setTextSize(Constants.PHONE_WIDTH/55);
        mPerson_design_recycleview = (RecyclerView) this.findViewById(R.id.person_design_recycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mPerson_design_head_layout = (LinearLayout) this.findViewById(R.id.person_design_head_layout);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mPerson_design_recycleview.setLayoutManager(linearLayoutManager);
        mPersonDesignRecycleViewAdapter = new PersonDesignRecycleViewAdapter(
                this, Constants.PERSON_DESIGN_MESSAGE
        );
        mPerson_design_recycleview.setAdapter(mPersonDesignRecycleViewAdapter);
        mPerson_design_recycleview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mPersonDesignRecycleViewAdapter.setOnPersonDesignItemListener(new PersonDesignRecycleViewAdapter.OnPersonDesignItemListener() {
            @Override
            public void setItemClick(View view) {
                int position = mPerson_design_recycleview.getChildAdapterPosition(view);
                itemOperation(position, view);
                mPerson_design_ok.setVisibility(View.VISIBLE);
                isSaveModify = true;
            }
        });

        //获取职业和居住地的信息
        String person_design_profession = getIntent().getStringExtra("person_design_profession");
        String person_design_location = getIntent().getStringExtra("person_design_location");
        if (!TextUtils.isEmpty(person_design_profession)) {
            mPerson_design_ok.setVisibility(View.VISIBLE);
            isSaveModify = true;
            Constants.PERSON_DESIGN_MESSAGE.set(5, person_design_profession);
            mPersonDesignRecycleViewAdapter.notifyDataSetChanged();
        }
        if (!TextUtils.isEmpty(person_design_location)) {
            mPerson_design_ok.setVisibility(View.VISIBLE);
            isSaveModify = true;
            Constants.PERSON_DESIGN_MESSAGE.set(2, person_design_location);
            mPersonDesignRecycleViewAdapter.notifyDataSetChanged();
        }

        if (Constants.authorIcon!=null){
            mPerson_design_author_icon.setImageBitmap(Constants.authorIcon);
        }
    }

    private void itemOperation(int position, View view) {
        switch (position) {

            case 0:
                jumpToItemMessage(0);
                break;
            case 1:
                showSexDialog(1, BODY_SEX);
                break;
            case 2:
//                LogUtils.showVerbose("PersonalDesignActivity","111");
                Intent intent = new Intent(PersonalDesignActivity.this, PersonDesignLocationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
//                LogUtils.showVerbose("PersonalDesignActivity","222");
                break;
            case 3:
                showSexDialog(3, getResources().getStringArray(R.array.person_design_body_height));
                break;
            case 4:
                showSexDialog(4, getResources().getStringArray(R.array.person_design_body_weight));
                break;
            case 5:
                Intent professioin_intent = new Intent(PersonalDesignActivity.this, PersonDesignProfessionActivity.class);
                professioin_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(professioin_intent);
                finish();
                break;
            case 6:
                showBottoPopupWindow(6);
                break;
            case 7:
                jumpToItemMessage(7);
                break;
            default:
                break;
        }
    }

    private void showSexDialog(final int position, String[] strings) {
        final String[] itemMessage = new String[1];
        if (position == 1) {
            itemMessage[0] = "男";
        } else if (position == 3) {
            itemMessage[0] = "150cm";
        } else if (position == 4) {
            itemMessage[0] = "40kg";
        }
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_windows_person_design_layout, null);
        person_desigin_pop_window = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);

        person_desigin_pop_window.setBackgroundDrawable(new BitmapDrawable());
        person_desigin_pop_window.setOutsideTouchable(true);
        person_desigin_pop_window.setFocusable(true);
        person_desigin_pop_window.setTouchable(true);
        //	set_pop.setAnimationStyle(R.style.Anim_popWindow_in);
        //set_pop.showAtLocation(frame,Gravity.TOP, 0, 0);
        person_desigin_pop_window.showAsDropDown(mPerson_design_head_layout, 0, Constants.PHONE_HEIGHT / 5 * 3);
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
//        mPopwindow_person_design_cancel.setTextSize(Constants.PHONE_WIDTH/50);
//        mPopwindow_person_design_ok.setTextSize(Constants.PHONE_WIDTH/50);
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
                mPopwindow_person_design_ok.setTextColor(getResources().getColor(R.color.head_bg_color));
                Constants.PERSON_DESIGN_MESSAGE.set(position, itemMessage[0]);
                mPersonDesignRecycleViewAdapter.notifyDataSetChanged();
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
        mPopupWindow.showAtLocation(mPerson_design_head_layout, Gravity.BOTTOM, 0, 0);
        TextView tv_cancle = (TextView) menuView.findViewById(R.id.tv_cancle);
        final TextView tv_ensure = (TextView) menuView.findViewById(R.id.tv_ensure);
        tv_ensure.setTextColor(getResources().getColor(R.color.black));
//        tv_cancle.setTextSize(Constants.PHONE_WIDTH/50);
//        tv_ensure.setTextSize(Constants.PHONE_WIDTH/50);
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
                Constants.PERSON_DESIGN_MESSAGE.set(position, DateUtils.formateString(beginTime, DateUtils.yyyyMMdd));
                mPersonDesignRecycleViewAdapter.notifyDataSetChanged();
                tv_ensure.setTextColor(getResources().getColor(R.color.head_bg_color));
                mPopupWindow.dismiss();
            }
        });
    }

    public void jumpToItemMessage(int position) {
        Intent intent = new Intent(PersonalDesignActivity.this, ItemPersonDesigenMessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("click_position", position);
        intent.putExtras(bundle);
        startActivityForResult(intent, position);
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
                        Bitmap bitmap = mPictureUtils.decodeUriAsBitmap(imageUri);
                        if (bitmap != null) {
                            //bitmap类型图片使用
                            Constants.authorIcon = Utils.toRecttangleBitmap(bitmap);//保存当前显示的头像
                            Constants.authorIconUrl = Utils.savePhoto(bitmap, Environment
                                    .getExternalStorageDirectory().getAbsolutePath(), String
                                    .valueOf(System.currentTimeMillis()));//保存当前头像的路径
//                            LogUtils.showVerbose("PersonalDesignActivity","当前头像的路径："+Constants.authorIconUrl);
                            mPerson_design_author_icon.setImageBitmap(Constants.authorIcon); // 这个时候的图片已经被处理成圆形的了
                            uploadToService(Constants.authorIcon, Constants.authorIconUrl);
                        }
                    }
                    break;
                case REMEMBER_OTHER_NAME:
                    if (Constants.authorIcon != null) {
                        mPerson_design_author_icon.setImageBitmap(Constants.authorIcon);
                    }
                    PersonMessageSerializable other_name_info = (PersonMessageSerializable) data.getSerializableExtra("input_message");
                    Constants.PERSON_DESIGN_MESSAGE.set(0, other_name_info.getInputMessage());
                    mPersonDesignRecycleViewAdapter.notifyDataSetChanged();
                    break;
                case REMEMBER_SIGN:
                    if (Constants.authorIcon != null) {
                        mPerson_design_author_icon.setImageBitmap(Constants.authorIcon);
                    }
                    PersonMessageSerializable sign_info = (PersonMessageSerializable) data.getSerializableExtra("input_message");
                    Constants.PERSON_DESIGN_MESSAGE.set(7, sign_info.getInputMessage());
                    mPersonDesignRecycleViewAdapter.notifyDataSetChanged();
                    break;
            }

        }
    }

    private void uploadToService(final Bitmap authorIcon, final String authorIconUrl) {
        mProgressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                postMethod(getpublishMessage(),authorIcon,authorIconUrl);
            }
        }).start();
    }

    private synchronized void postMethod(String text, Bitmap bitmap,String iconpath) {
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
        String picName = iconpath.substring(iconpath.lastIndexOf("/")+1,iconpath.length());
        entity.addPart("imageFile1", new ByteArrayBody(bitmapToBytes(bitmap),picName));
        post.setEntity(entity);
        HttpResponse resp = null;
        try {
            resp = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            LogUtils.showVerbose("PersonalDesignActivity", "Response:" + EntityUtils.toString(resp.getEntity()));
            LogUtils.showVerbose("PersonalDesignActivity", "StatusCode:" + resp.getStatusLine().getStatusCode());
            if (resp.getStatusLine().getStatusCode() == 200) {
                mProgressDialog.dismiss();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getpublishMessage(){
        JSONObject jsonObject = new JSONObject();
        try {
//            {"id":"id1","avatar":"imgPath1","nname":"nickname","gender":"nan","address":"address1",
//                    "height":181,"weight":80,"career":"carear1","birthd":"20161225"
//            }

            jsonObject.put("id",Constants.USER_ID);
            jsonObject.put("avatar","imageFile1");
            jsonObject.put("nname",Constants.PERSON_DESIGN_MESSAGE.get(0));
            jsonObject.put("gender",Constants.PERSON_DESIGN_MESSAGE.get(1));
            jsonObject.put("address",Constants.PERSON_DESIGN_MESSAGE.get(2));
            jsonObject.put("height",Integer.getInteger(Constants.PERSON_DESIGN_MESSAGE.get(3).substring(0,2)));
            jsonObject.put("weight",Integer.getInteger(Constants.PERSON_DESIGN_MESSAGE.get(4).substring(0,1)));
            jsonObject.put("career",Constants.PERSON_DESIGN_MESSAGE.get(5));
            jsonObject.put("birthd",Constants.PERSON_DESIGN_MESSAGE.get(6));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (ActivityColection.isContains(this)) {
                ActivityColection.removeActivity(this);
                Intent intent = new Intent(PersonalDesignActivity.this, MainActivity.class);
                intent.putExtra("person_design", "person_design");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.person_design_back:
                if (isSaveModify){
                    new AlertDialog.Builder(PersonalDesignActivity.this)
                            .setTitle("修改提示")
                            .setMessage("是否保存当前保存的信息")
                            .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    postMethod1(getpublishMessage1());
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(PersonalDesignActivity.this, MainActivity.class);
                                    intent.putExtra("person_design", "person_design");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .show();
                }else {
                    Intent intent = new Intent(PersonalDesignActivity.this, MainActivity.class);
                    intent.putExtra("person_design", "person_design");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

                break;
            case R.id.person_design_author_layout:

                if (Constants.authorIcon != null) {
                    mPerson_design_author_icon.setImageBitmap(Constants.authorIcon);
                }
                showChoosePicDialog();
                break;
            case R.id.person_design_ok:
                //提交修改的信息
                uploadDesignMessage();
                isSaveModify = false;//当前已经保存过了
                LogUtils.showVerbose("PersonalDesignActivity","保存的信息"+Constants.PERSON_DESIGN_MESSAGE.toString());
                break;
            default:
                break;
        }
    }

    private void uploadDesignMessage() {
        mProgressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                postMethod1(getpublishMessage1());
            }
        }).start();
    }
    //保存当前的设置的信息
    private synchronized void postMethod1(String text) {
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
            LogUtils.showVerbose("PersonalDesignActivity", "Response:" + EntityUtils.toString(resp.getEntity()));
//            LogUtils.showVerbose("PersonalDesignActivity", "errorcode:" + EntityUtils.getContentCharSet(resp.getEntity()));
            LogUtils.showVerbose("PersonalDesignActivity", "StatusCode:" + resp.getStatusLine().getStatusCode());
            if (resp.getStatusLine().getStatusCode() == 200) {
                mProgressDialog.dismiss();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //保存的信息[哈哈哈, 男, 安达, 150cm, 40kg, 通信硬件, 2017-01-15, 哈哈哈哈哈哈哈哈哈哈]
    public String getpublishMessage1(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",Constants.USER_ID);
            jsonObject.put("nname",Constants.PERSON_DESIGN_MESSAGE.get(0));
            jsonObject.put("gender",Constants.PERSON_DESIGN_MESSAGE.get(1));
            jsonObject.put("address",Constants.PERSON_DESIGN_MESSAGE.get(2));
            jsonObject.put("height",Integer.getInteger(Constants.PERSON_DESIGN_MESSAGE.get(3).substring(0,2)));
            jsonObject.put("weight",Integer.getInteger(Constants.PERSON_DESIGN_MESSAGE.get(4).substring(0,1)));
            jsonObject.put("career",Constants.PERSON_DESIGN_MESSAGE.get(5));
            jsonObject.put("birthd",Constants.PERSON_DESIGN_MESSAGE.get(6));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
