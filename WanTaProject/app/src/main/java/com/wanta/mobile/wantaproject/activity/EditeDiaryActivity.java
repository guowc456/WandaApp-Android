package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.AskQuestionRecycleViewAdapter;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.ImageUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.SaveMsgUtils;
import com.wanta.mobile.wantaproject.utils.SimpleDraweeControlUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by WangYongqiang on 2016/12/8.
 */
public class EditeDiaryActivity extends BaseActivity implements View.OnClickListener {

    private MyImageView mEdit_diary_back;
    private LinearLayout mEdit_diary_publish_layout;
    private LinearLayout mEdit_diary_content;
    private ScrollView mEdit_diary_scrollview;
    private LinearLayout mEdit_diary_back_layout;
    private LinearLayout mEdit_diary_save_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_diary);
        initId();
    }

    private void initId() {
        mEdit_diary_back = (MyImageView) this.findViewById(R.id.edit_diary_back);
        mEdit_diary_back.setSize(Constants.PHONE_WIDTH / 16, Constants.PHONE_WIDTH / 16);
        mEdit_diary_back_layout = (LinearLayout) this.findViewById(R.id.edit_diary_back_layout);
        mEdit_diary_back_layout.setOnClickListener(this);
        mEdit_diary_save_layout = (LinearLayout) this.findViewById(R.id.edit_diary_save_layout);
        mEdit_diary_save_layout.setOnClickListener(this);
        mEdit_diary_publish_layout = (LinearLayout) this.findViewById(R.id.edit_diary_publish_layout);
        mEdit_diary_publish_layout.setOnClickListener(this);
//        设置滑动界面的背景图片
        mEdit_diary_scrollview = (ScrollView) this.findViewById(R.id.edit_diary_scrollview);
//        mEdit_diary_scrollview.setBackground(getResources().getDrawable(R.mipmap.blue_sea));
        mEdit_diary_scrollview.setBackgroundResource(R.mipmap.blue_sea);
        mEdit_diary_content = (LinearLayout) this.findViewById(R.id.edit_diary_content);
        mEdit_diary_content.addView(getOnePicLayoutOfB());
        mEdit_diary_content.addView(getOnePicLayoutOfA());
        mEdit_diary_content.addView(getTwoPicsLayoutOfAA());
        mEdit_diary_content.addView(getTwoPicsLayoutOfAB());
        mEdit_diary_content.addView(getTwoPicsLayoutOfBA());
        mEdit_diary_content.addView(getTwoPicsLayoutOfBB());
        mEdit_diary_content.addView(getTwoPicsLayoutOfABO());
        mEdit_diary_content.addView(getTwoPicsLayoutOfBBO());
        showSelectMethod();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_diary_back_layout:
                Intent back_intent = new Intent(EditeDiaryActivity.this, CameraActivity.class);
                back_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(back_intent);
                break;
            case R.id.edit_diary_publish_layout:
                Intent publish_intent = new Intent(EditeDiaryActivity.this, NewPublishActivity.class);
                publish_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(publish_intent);
                break;
            case R.id.edit_diary_save_layout:
                break;
        }
    }

    //获取应该使用什么样的顺序进行显示
    public void showSelectMethod() {
        String str = "";
        //首先获取过来选中图片的个数
        int selectPicsNums = 9;
//        LogUtils.showVerbose("EditeDiaryActivity", "随机数是=" + ((int) (Math.random() * 3) + 1));
        //首先进行一个循环，然后得出排列的数列
        while (true) {
            int randomNum = ((int) (Math.random() * 3) + 1);
            int remainNums = selectPicsNums - randomNum;
            if (remainNums>0){
                str = str + randomNum;
            }else {
                str = str + selectPicsNums ;
                break;
            }
            selectPicsNums = remainNums;
        }
        //把字符串进行解析
        int[] arrNums = new int[str.length()];
        for (int i=0;i<str.length();i++){
            arrNums[i] = Integer.parseInt(str.charAt(i)+"");
            LogUtils.showVerbose("EditeDiaryActivity", "生成的数列是=" + str+"  数列的长度是："+str.length()+"当前的数列是："+arrNums[i]);
        }
    }

    //一张图片的布局,获取到的是B类型
    public LinearLayout getOnePicLayoutOfB() {
        LinearLayout linearLayout = getVerticalLinearLayoutSubject();
        linearLayout.addView(getTextViewSubject());
        linearLayout.addView(getSimpleDraweeSubject("/storage/emulated/0/DCIM/IMG-400509543.jpg"));
        return linearLayout;
    }

    //一张图片的布局,获取到的是A类型
    public LinearLayout getOnePicLayoutOfA() {
        LinearLayout linearLayout = getVerticalLinearLayoutSubject();
        linearLayout.addView(getTextViewSubject());
        linearLayout.addView(getSimpleDraweeSubject("/storage/emulated/0/XHS/14846383376127/c1148463833839353.jpg"));
        return linearLayout;
    }

    //两张图片的布局,获取到的是AA类型
    public LinearLayout getTwoPicsLayoutOfAA() {
        LinearLayout linearLayout = getVerticalLinearLayoutSubject();
        linearLayout.addView(getTextViewSubject());
        linearLayout.addView(getSimpleDraweeSubject("/storage/emulated/0/XHS/14846383376127/c1148463833839353.jpg"));

        linearLayout.addView(getTextViewSubject());
        linearLayout.addView(getSimpleDraweeSubject("/storage/emulated/0/XHS/14846383376127/c1148463833839353.jpg"));
        return linearLayout;
    }

    //两张图片的布局，获取到的是AB类型
    public LinearLayout getTwoPicsLayoutOfAB() {
        LinearLayout verticalLinearLayoutSubject = getVerticalLinearLayoutSubject();
        verticalLinearLayoutSubject.addView(getSimpleDraweeSubject("/storage/emulated/0/XHS/14846383376127/c1148463833839353.jpg"));
        LinearLayout horizontalLinearLayoutSubject = getHorizontalLinearLayoutSubject();
        horizontalLinearLayoutSubject.addView(getTextViewSubject());
        horizontalLinearLayoutSubject.addView(getSimpleDraweeSubject("/storage/emulated/0/DCIM/IMG-400509543.jpg"));
        horizontalLinearLayoutSubject.setPadding(0, 10, 0, 0);
        verticalLinearLayoutSubject.addView(horizontalLinearLayoutSubject);
        return verticalLinearLayoutSubject;
    }

    //两张图片的布局，获取到的是BA类型
    public LinearLayout getTwoPicsLayoutOfBA() {
        LinearLayout verticalLinearLayoutSubject = getVerticalLinearLayoutSubject();
        LinearLayout horizontalLinearLayoutSubject = getHorizontalLinearLayoutSubject();
        horizontalLinearLayoutSubject.addView(getSimpleDraweeSubject("/storage/emulated/0/DCIM/IMG-400509543.jpg"));
        horizontalLinearLayoutSubject.setPadding(0, 10, 0, 0);
        horizontalLinearLayoutSubject.addView(getTextViewSubject());
        verticalLinearLayoutSubject.addView(horizontalLinearLayoutSubject);
        verticalLinearLayoutSubject.addView(getSimpleDraweeSubject("/storage/emulated/0/XHS/14846383376127/c1148463833839353.jpg"));
        return verticalLinearLayoutSubject;
    }

    //两张图片的布局，获取到的是BB类型
    public LinearLayout getTwoPicsLayoutOfBB() {
        LinearLayout verticalLinearLayoutSubject = getVerticalLinearLayoutSubject();
        LinearLayout horizontalLinearLayoutSubject = getHorizontalLinearLayoutSubject();
        horizontalLinearLayoutSubject.addView(getSimpleDraweeSubject("/storage/emulated/0/DCIM/IMG-400509543.jpg"));
        horizontalLinearLayoutSubject.setPadding(0, 10, 0, 0);
        horizontalLinearLayoutSubject.addView(getTextViewSubject());
        verticalLinearLayoutSubject.addView(horizontalLinearLayoutSubject);
        LinearLayout horizontalLinearLayoutSubject1 = getHorizontalLinearLayoutSubject();
        horizontalLinearLayoutSubject1.addView(getTextViewSubject());
        horizontalLinearLayoutSubject1.addView(getSimpleDraweeSubject("/storage/emulated/0/DCIM/IMG-400509543.jpg"));
        horizontalLinearLayoutSubject1.setPadding(0, 10, 0, 0);
        verticalLinearLayoutSubject.addView(horizontalLinearLayoutSubject1);
        return verticalLinearLayoutSubject;
    }

    //三张图片的布局，获取到的是ABO类型
    public LinearLayout getTwoPicsLayoutOfABO() {
        LinearLayout verticalLinearLayoutSubject = getVerticalLinearLayoutSubject();
        verticalLinearLayoutSubject.addView(getTextViewSubject());
        verticalLinearLayoutSubject.addView(getSimpleDraweeSubject("/storage/emulated/0/XHS/14846383376127/c1148463833839353.jpg"));
        LinearLayout horizontalLinearLayoutSubject = getHorizontalLinearLayoutSubject();
        horizontalLinearLayoutSubject.addView(getTextViewSubject());
        horizontalLinearLayoutSubject.addView(getSimpleDraweeSubject("/storage/emulated/0/DCIM/IMG-400509543.jpg"));
        horizontalLinearLayoutSubject.setPadding(0, 10, 0, 0);
        verticalLinearLayoutSubject.addView(horizontalLinearLayoutSubject);

        LinearLayout horizontalLinearLayoutSubject1 = getHorizontalLinearLayoutSubject();
        horizontalLinearLayoutSubject1.addView(getRingSimpleDraweeSubject("/storage/emulated/0/DCIM/IMG-400509543.jpg"));//获取一个圆形的图片
        horizontalLinearLayoutSubject1.setPadding(0, 10, 0, 0);
        horizontalLinearLayoutSubject1.addView(getTextViewSubject());
        verticalLinearLayoutSubject.addView(horizontalLinearLayoutSubject1);
        return verticalLinearLayoutSubject;
    }

    //三张图片的布局，获取到的是BBO类型
    public LinearLayout getTwoPicsLayoutOfBBO() {
        LinearLayout verticalLinearLayoutSubject = getVerticalLinearLayoutSubject();
        LinearLayout horizontalLinearLayoutSubject = getHorizontalLinearLayoutSubject();
        horizontalLinearLayoutSubject.addView(getTextViewSubject());
        horizontalLinearLayoutSubject.addView(getSimpleDraweeSubject("/storage/emulated/0/DCIM/IMG-400509543.jpg"));
        horizontalLinearLayoutSubject.setPadding(0, 10, 0, 0);
        verticalLinearLayoutSubject.addView(horizontalLinearLayoutSubject);

        LinearLayout horizontalLinearLayoutSubject1 = getHorizontalLinearLayoutSubject();
        horizontalLinearLayoutSubject1.addView(getSimpleDraweeSubject("/storage/emulated/0/DCIM/IMG-400509543.jpg"));
        horizontalLinearLayoutSubject1.setPadding(0, 10, 0, 0);
        horizontalLinearLayoutSubject1.addView(getTextViewSubject());
        verticalLinearLayoutSubject.addView(horizontalLinearLayoutSubject1);

        LinearLayout horizontalLinearLayoutSubject2 = getHorizontalLinearLayoutSubject();
        horizontalLinearLayoutSubject2.addView(getTextViewSubject());
        horizontalLinearLayoutSubject2.addView(getRingSimpleDraweeSubject("/storage/emulated/0/DCIM/IMG-400509543.jpg"));//获取一个圆形的图片
        horizontalLinearLayoutSubject2.setPadding(0, 10, 0, 0);
        verticalLinearLayoutSubject.addView(horizontalLinearLayoutSubject2);
        return verticalLinearLayoutSubject;
    }

    //获取一个水平排布Linelarlayout对象
    public LinearLayout getHorizontalLinearLayoutSubject() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH - 30, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 0, 15, 15);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(params);
        //设置linearlayout的布局方向
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        return linearLayout;
    }

    //获取一个垂直排布Linelarlayout对象
    public LinearLayout getVerticalLinearLayoutSubject() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH - 30, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 0, 15, 15);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(params);
        //设置linearlayout的布局方向
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        return linearLayout;
    }

    //获取一个TextViewdui对象
    public TextView getTextViewSubject() {
        TextView textView = new TextView(this);
        textView.setText("第一张图片");
        textView.setPadding(0, 20, 0, 20);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    //获取到一个四个角是圆角simpledrawee对象
    public CustomSimpleDraweeView getSimpleDraweeSubject(String picUrl) {
        CustomSimpleDraweeView simpleDraweeView = new CustomSimpleDraweeView(this);
        int[] imageWidthHeight = ImageUtils.getImageWidthHeight(picUrl);
        if (imageWidthHeight[0] != 0 && imageWidthHeight[1] != 0) {
            if (imageWidthHeight[0] / imageWidthHeight[1] >= 1) {
                //当前的宽大于高
                simpleDraweeView.setWidth(Constants.PHONE_WIDTH - 30);
                simpleDraweeView.setHeight((int) ((((Constants.PHONE_WIDTH - 30) * 1.00) / imageWidthHeight[0]) * imageWidthHeight[1]));
            } else {
                //当前的宽小于高
                simpleDraweeView.setWidth((int) (((int) ((Constants.PHONE_HEIGHT * 5 * 1.00) / 9) / (imageWidthHeight[1] * 1.00)) * imageWidthHeight[0]));
                simpleDraweeView.setHeight((int) ((Constants.PHONE_HEIGHT * 5 * 1.00) / 9));
            }
            simpleDraweeView.setImageURI(Uri.fromFile(new File(picUrl)));
            SimpleDraweeControlUtils.getSimpleDraweeControl(this,simpleDraweeView, picUrl);
        }
        return simpleDraweeView;
    }

    //获取到一个圆形的simpledrawee对象
    public CustomSimpleDraweeView getRingSimpleDraweeSubject(String picUrl) {
        CustomSimpleDraweeView simpleDraweeView = new CustomSimpleDraweeView(this);
        int[] imageWidthHeight = ImageUtils.getImageWidthHeight(picUrl);
        if (imageWidthHeight[0] != 0 && imageWidthHeight[1] != 0) {
            simpleDraweeView.setWidth(Constants.PHONE_WIDTH / 2);
            simpleDraweeView.setHeight(Constants.PHONE_WIDTH / 2);

            simpleDraweeView.setImageURI(Uri.fromFile(new File(picUrl)));
            SimpleDraweeControlUtils.getRingSimpleDraweeControl(this,simpleDraweeView, picUrl);
        }
        return simpleDraweeView;
    }

//    //设置simpledrawee圆角的功能
//    public void getSimpleDraweeControl(CustomSimpleDraweeView simpleDraweeView, String picUrl) {
//        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(getResources())
//                //设置圆形圆角参数
//                //.setRoundingParams(rp)
//                //设置圆角半径
//                .setRoundingParams(RoundingParams.fromCornersRadius(25))
//                //设置圆形圆角参数；RoundingParams.asCircle()是将图像设置成圆形
//                //.setRoundingParams(RoundingParams.asCircle())
//                //设置淡入淡出动画持续时间(单位：毫秒ms)
////                .setFadeDuration(5000)
//                //构建
//                .build();
//
//        //设置Hierarchy
//        simpleDraweeView.setHierarchy(hierarchy);
//        //构建Controller
//        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                //设置需要下载的图片地址
//                .setUri(Uri.fromFile(new File(picUrl)))
//                //构建
//                .build();
//
//        //设置Controller
//        simpleDraweeView.setController(controller);
//
//    }

//    //设置simpledrawee圆行的功能
//    public void getRingSimpleDraweeControl(CustomSimpleDraweeView simpleDraweeView, String picUrl) {
//        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(getResources())
//                //设置圆形圆角参数
//                //.setRoundingParams(rp)
//                //设置圆角半径
////                .setRoundingParams(RoundingParams.fromCornersRadius(25))
////                设置圆形圆角参数；RoundingParams.asCircle()是将图像设置成圆形
//                .setRoundingParams(RoundingParams.asCircle())
//                //设置淡入淡出动画持续时间(单位：毫秒ms)
////                .setFadeDuration(5000)
//                //构建
//                .build();
//
//        //设置Hierarchy
//        simpleDraweeView.setHierarchy(hierarchy);
//        //构建Controller
//        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                //设置需要下载的图片地址
//                .setUri(Uri.fromFile(new File(picUrl)))
//                //构建
//                .build();
//
//        //设置Controller
//        simpleDraweeView.setController(controller);
//
//    }
}
