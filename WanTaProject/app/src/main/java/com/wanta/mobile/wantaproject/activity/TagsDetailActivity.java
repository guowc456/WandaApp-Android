package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.CustomLine;
import com.wanta.mobile.wantaproject.fragment.ItemFragment1;
import com.wanta.mobile.wantaproject.fragment.ItemFragment10;
import com.wanta.mobile.wantaproject.fragment.ItemFragment11;
import com.wanta.mobile.wantaproject.fragment.ItemFragment12;
import com.wanta.mobile.wantaproject.fragment.ItemFragment13;
import com.wanta.mobile.wantaproject.fragment.ItemFragment14;
import com.wanta.mobile.wantaproject.fragment.ItemFragment15;
import com.wanta.mobile.wantaproject.fragment.ItemFragment2;
import com.wanta.mobile.wantaproject.fragment.ItemFragment3;
import com.wanta.mobile.wantaproject.fragment.ItemFragment4;
import com.wanta.mobile.wantaproject.fragment.ItemFragment5;
import com.wanta.mobile.wantaproject.fragment.ItemFragment6;
import com.wanta.mobile.wantaproject.fragment.ItemFragment7;
import com.wanta.mobile.wantaproject.fragment.ItemFragment8;
import com.wanta.mobile.wantaproject.fragment.ItemFragment9;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2017/1/6.
 */
public class TagsDetailActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mTags_detail_linelayout;
    private CustomLine mTags_detail_custom_line;
    private FrameLayout mTags_detail_framelayout;
    private ItemFragment7 mItemFragment7;
    private ItemFragment8 mItemFragment8;
    private ItemFragment9 mItemFragment9;
    private ItemFragment10 mItemFragment10;
    private ItemFragment11 mItemFragment11;
    private ItemFragment12 mItemFragment12;
    private ItemFragment13 mItemFragment13;
    private String[] mUrls;
    private ItemFragment14 mItemFragment14;
    private ItemFragment15 mItemFragment15;
    private TextView mTags_detail_cancel;
    private TextView mTags_detail_ok;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags_details);
        ActivityColection.addActivity(this);
        Constants.TAGS_SELECT_IAMGE_URL.clear();//清空选择的图片的URL
        initId();
        initData();
        setTagListener();
        getCorrespondMsg(0);
    }

    private void initId() {
        mTags_detail_linelayout = (LinearLayout) this.findViewById(R.id.tags_detail_linelayout);
        mTags_detail_custom_line = (CustomLine) this.findViewById(R.id.tags_detail_custom_line);
        mTags_detail_framelayout = (FrameLayout) this.findViewById(R.id.tags_detail_framelayout);
        mTags_detail_cancel = (TextView) this.findViewById(R.id.tags_detail_cancel);
        mTags_detail_ok = (TextView) this.findViewById(R.id.tags_detail_ok);
        mTags_detail_cancel.setOnClickListener(this);
        mTags_detail_ok.setOnClickListener(this);
        Constants.selectTagsAttribute = 0;
        Constants.imagesOfEachTag.clear();
        Constants.imagesOfEachTag = new ArrayList<>(Constants.TAGS_LIST);

        Constants.TAGS_SELECT_IAMGE_URL1.clear();
        Constants.TAGS_SELECT_IAMGE_URL2.clear();
        Constants.TAGS_SELECT_IAMGE_URL3.clear();
        Constants.TAGS_SELECT_IAMGE_URL4.clear();
        Constants.TAGS_SELECT_IAMGE_URL5.clear();
        Constants.TAGS_SELECT_IAMGE_URL6.clear();
        Constants.TAGS_SELECT_IAMGE_URL7.clear();
        Constants.TAGS_SELECT_IAMGE_URL8.clear();
        Constants.TAGS_SELECT_IAMGE_URL9.clear();
    }
    //初始化标签数据
    private void initData() {
        for (int i=0;i<Constants.TAGS_LIST.size();i++){
            TextView textView = new TextView(this);
            textView.setWidth(Constants.PHONE_WIDTH/Constants.TAGS_LIST.size());
            textView.setHeight(Constants.PHONE_HEIGHT/20);
            textView.setText(Constants.TAGS_LIST.get(i));
            textView.setTextColor(getResources().getColor(R.color.text_color));
            textView.setGravity(Gravity.CENTER);
            mTags_detail_linelayout.addView(textView);
        }
        getAllUrl(Constants.TAGS_LIST);
    }

    //获取对应的标签的链接
    public void getAllUrl(List<String> list){
        mUrls = new String[list.size()];
        for (int i = 0; i< list.size();i++){
            //获取相应的链接
            if ("上衣".equals(list.get(i))){
                mUrls[i] = "http://1zou.me/api/sq/useritems/1/upper";
            }else if ("裤子".equals(list.get(i))){
                mUrls[i] = "http://1zou.me/api/sq/useritems/1/trouser";
            }else if ("裙装".equals(list.get(i))){
                mUrls[i] = "http://1zou.me/api/sq/useritems/1/skirt";
            }else if ("鞋子".equals(list.get(i))){
                mUrls[i] = "http://1zou.me/api/sq/useritems/1/shoe";
            }else if ("帽子".equals(list.get(i))){
                mUrls[i] = "http://1zou.me/api/sq/useritems/1/hat";
            }else if ("围巾".equals(list.get(i))){
                mUrls[i] = "http://1zou.me/api/sq/useritems/1/scarf";
            }else if ("腰带".equals(list.get(i))){
                mUrls[i] = "http://1zou.me/api/sq/useritems/1/waist";
            }else if ("包包".equals(list.get(i))){
                mUrls[i] = "http://1zou.me/api/sq/useritems/1/bag";
            }else if ("其他".equals(list.get(i))){
                mUrls[i] = "http://1zou.me/api/sq/test/1/upper";
            }
        }
    }

    //设置对应的tag的点击事件
    private void setTagListener() {
        for (int i =0;i<mUrls.length;i++){
            final int finalI = i;
            ((TextView)mTags_detail_linelayout.getChildAt(i)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    LogUtils.showVerbose("TagsDetailActivity","当前的位置:"+ finalI);
                    getCorrespondMsg(finalI);
                }
            });
        }
    }

    //获取对应fragment的数据
    public void getCorrespondMsg(final int position){
        //清楚当前标签的图片的URL
        Constants.TAGS_IAMGE_URL.clear();
        final String url = mUrls[position];
        MyHttpUtils.getNetMessage(this, url, new MyHttpUtils.Callback() {
            @Override
            public void getResponseMsg(String response) {
                jsonParseMsg(response);
                //设置默认tag下线的颜色
                mTags_detail_custom_line.setLineNumber(Constants.TAGS_LIST.size());
                mTags_detail_custom_line.setLineHeight(Constants.PHONE_WIDTH / Constants.TAGS_LIST.size(), 0);
                mTags_detail_custom_line.setSelectLine(position + 1);
                //设置字体的颜色
                for (int j = 0; j < mUrls.length; j++) {
                    if (j == position) {
                        ((TextView) mTags_detail_linelayout.getChildAt(j)).setTextColor(getResources().getColor(R.color.head_bg_color));
                    } else {
                        ((TextView) mTags_detail_linelayout.getChildAt(j)).setTextColor(getResources().getColor(R.color.tablayout_normal_color));
                    }
                }
                //选择相应的fragment
                setTabSelection(position);
            }
        }, new MyHttpUtils.CallbackError() {
            @Override
            public void getResponseMsg(String error) {

            }
        });
    }
    //解析获取过来的数据
    private void jsonParseMsg(String response) {
        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0;i<array.length();i++){
                JSONObject jsonObject = array.getJSONObject(i);
                String image_path = jsonObject.getString("image_path");
                Constants.TAGS_IAMGE_URL.add(image_path);
            }
        } catch (JSONException e) {
            LogUtils.showVerbose("TagsDetailActivity","解析单品数据错误");
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     */
    private void setTabSelection(int index) {
        Constants.selectTagsAttribute = index;
        // 开启一个Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        removeFragments(transaction);
        switch (index) {
            case 0:
                mItemFragment7 = new ItemFragment7();
                transaction.replace(R.id.tags_detail_framelayout, mItemFragment7);
                break;
            case 1:
                mItemFragment8 = new ItemFragment8();
                transaction.replace(R.id.tags_detail_framelayout, mItemFragment8);
                break;
            case 2:
                mItemFragment9 = new ItemFragment9();
                transaction.replace(R.id.tags_detail_framelayout, mItemFragment9);
                break;
            case 3:
                mItemFragment10 = new ItemFragment10();
                transaction.replace(R.id.tags_detail_framelayout, mItemFragment10);
                break;
            case 4:
                mItemFragment11 = new ItemFragment11();
                transaction.replace(R.id.tags_detail_framelayout, mItemFragment11);
                break;
            case 5:
                mItemFragment12 = new ItemFragment12();
                transaction.replace(R.id.tags_detail_framelayout, mItemFragment12);
                break;
            case 6:
                mItemFragment13 = new ItemFragment13();
                transaction.replace(R.id.tags_detail_framelayout, mItemFragment13);
                break;
            case 7:
                mItemFragment14 = new ItemFragment14();
                transaction.replace(R.id.tags_detail_framelayout, mItemFragment14);
                break;
            case 8:
                mItemFragment15 = new ItemFragment15();
                transaction.replace(R.id.tags_detail_framelayout, mItemFragment15);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void removeFragments(FragmentTransaction transaction) {
        if (mItemFragment7 != null) {
            transaction.remove(mItemFragment7);
        }
        if (mItemFragment8 != null) {
            transaction.remove(mItemFragment8);
        }
        if (mItemFragment9 != null) {
            transaction.remove(mItemFragment9);
        }
        if (mItemFragment10 != null) {
            transaction.remove(mItemFragment10);
        }
        if (mItemFragment11 != null) {
            transaction.remove(mItemFragment11);
        }
        if (mItemFragment12 != null) {
            transaction.remove(mItemFragment12);
        }
        if (mItemFragment13 != null) {
            transaction.remove(mItemFragment13);
        }
        if (mItemFragment14 != null) {
            transaction.remove(mItemFragment14);
        }
        if (mItemFragment15 != null) {
            transaction.remove(mItemFragment15);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tags_detail_cancel:
                //取消
                jumpToCameraActivity();
                Constants.TAGS_SELECT_IAMGE_URL.clear();
                break;
            case R.id.tags_detail_ok:
                //确定按钮
                jumpToCameraActivity();
                break;
        }
    }
    public void jumpToCameraActivity(){
        Intent intent = new Intent(TagsDetailActivity.this,CameraActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("tags_detail","tags_detail");
        startActivity(intent);
        finish();
    }
}
