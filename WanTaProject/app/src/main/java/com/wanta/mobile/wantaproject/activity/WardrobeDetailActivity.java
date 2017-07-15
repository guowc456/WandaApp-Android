package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.CustomLine;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.fragment.CommunityFragment;
import com.wanta.mobile.wantaproject.fragment.FindFragment;
import com.wanta.mobile.wantaproject.fragment.ItemFragment1;
import com.wanta.mobile.wantaproject.fragment.ItemFragment2;
import com.wanta.mobile.wantaproject.fragment.ItemFragment3;
import com.wanta.mobile.wantaproject.fragment.ItemFragment4;
import com.wanta.mobile.wantaproject.fragment.ItemFragment5;
import com.wanta.mobile.wantaproject.fragment.ItemFragment6;
import com.wanta.mobile.wantaproject.fragment.ItemWardrobeDetailFragment;
import com.wanta.mobile.wantaproject.fragment.SelfFragment;
import com.wanta.mobile.wantaproject.fragment.WardrobeFragment;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.DealReturnLogicUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.ShowFragment;
import com.wanta.mobile.wantaproject.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by WangYongqiang on 2016/11/26.
 */
public class WardrobeDetailActivity extends BaseActivity implements View.OnClickListener {

    private int mFirstItem;
    private int mThridItem;
    private TabLayout mWardrobe_detail_tablayout;
    private ViewPager mWardrobe_detail_viewpager;
    private MyImageView mWardrobe_detail_back;
    private TextView mWardrobe_detail_title;
    public static String[][] thrid_item = { {"按颜色","按长短","按气温"},
            {"按颜色","连身裤","裤装","按长度","按裤型"},
            {"按颜色","半身裙","连衣裙","按长度","按气温"},
            {"按颜色"},
            {"按颜色","按鞋跟","轻便","易穿"},
            {"按颜色","按大小","按气温"},{"按颜色","按宽窄"},{"按颜色","按大小","按形变"}};
    private ImageView mImage_test;
    private LinearLayout mLinelayout;
    private FrameLayout mWardrobe_detail_framelayout;
    private ItemFragment1 mItemFragment1;
    private ItemFragment2 mItemFragment2;
    private ItemFragment3 mItemFragment3;
    private ItemFragment4 mItemFragment4;
    private ItemFragment5 mItemFragment5;
    private ItemFragment6 mItemFragment6;
    private CustomLine mWardrobe_detail_custom_line;
    private LinearLayout mWardrobe_detail_back_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardrobe_detail);
        if ("wardoresingle".equals(getIntent().getStringExtra("wardoresingle"))){
//            Constants.firstItem = mFirstItem;
//            Constants.thridItem = mThridItem;
//            LogUtils.showVerbose("WardrobeDetailActivity","一级="+Constants.firstItem+" 二级="+Constants.thridItem);
        }else {
//            mFirstItem = getIntent().getExtras().getInt("firstItem");
//            mThridItem = getIntent().getExtras().getInt("thridItem");
            mFirstItem = Constants.selectImageFirstItem;
            mThridItem = Constants.selectImageThridItem;
            Constants.current_number = mFirstItem;
            Constants.firstItem = mFirstItem;
            Constants.thridItem = mThridItem;
        }
        //进行数据的传递
        initId();
        getOtherFragment(Constants.thridItem);
        mWardrobe_detail_custom_line.setSelectLine(Constants.thridItem+1);
    }

    private void initId() {
        mWardrobe_detail_back = (MyImageView) this.findViewById(R.id.wardrobe_detail_back);
        mWardrobe_detail_back.setSize(Constants.PHONE_WIDTH/15,Constants.PHONE_WIDTH/15);
        mWardrobe_detail_back_layout = (LinearLayout) this.findViewById(R.id.wardrobe_detail_back_layout);
        mWardrobe_detail_back_layout.setOnClickListener(this);
        mWardrobe_detail_title = (TextView) this.findViewById(R.id.wardrobe_detail_title);
        mWardrobe_detail_title.setTextSize(Constants.PHONE_WIDTH/50);
        mWardrobe_detail_custom_line = (CustomLine) this.findViewById(R.id.wardrobe_detail_custom_line);
        mWardrobe_detail_custom_line.setLineNumber(thrid_item[Constants.firstItem].length);
        mWardrobe_detail_custom_line.setLineHeight(Constants.PHONE_WIDTH/thrid_item[Constants.firstItem].length,0);

        mLinelayout = (LinearLayout) this.findViewById(R.id.linelayout);
        for (int i=0;i<thrid_item[Constants.firstItem].length;i++){
            TextView textView = new TextView(this);
            textView.setWidth(Constants.PHONE_WIDTH/thrid_item[Constants.firstItem].length);
            textView.setHeight(Constants.PHONE_HEIGHT/20);
            textView.setText(thrid_item[Constants.firstItem][i]);
            textView.setTextColor(getResources().getColor(R.color.text_color));
            textView.setGravity(Gravity.CENTER);
            mLinelayout.addView(textView);
        }
        //点击的默认显示的标签的颜色
        ((TextView)mLinelayout.getChildAt(Constants.thridItem)).setTextColor(getResources().getColor(R.color.head_bg_color));
        mWardrobe_detail_framelayout = (FrameLayout) this.findViewById(R.id.wardrobe_detail_framelayout);
        for (int i=0;i<thrid_item[Constants.firstItem].length;i++){
            final int finalI = i;
            mLinelayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWardrobe_detail_custom_line.setSelectLine(finalI+1);
//                    ToastUtil.showShort(WardrobeDetailActivity.this,thrid_item[Constants.firstItem][finalI]);
                    for (int j=0;j<thrid_item[Constants.firstItem].length;j++){
                        if (j==finalI){
                            ((TextView)mLinelayout.getChildAt(j)).setTextColor(getResources().getColor(R.color.head_bg_color));
                        }else {
                            ((TextView)mLinelayout.getChildAt(j)).setTextColor(getResources().getColor(R.color.text_color));
                        }
                    }
                    getOtherFragment(finalI);
                }
            });
        }
    }

    private void getOtherFragment(final int position) {
        String clothCatogry = getClothCatogry(thrid_item[Constants.firstItem][position]);
//        String url = "http://1zou.me/api/sq/"+clothCatogry+"/1/"+Constants.cloth_catogry_english[Constants.firstItem];
        String url = null;
        if ("null".equals(Constants.cloth_catogry_thrid_url[Constants.firstItem][position])){
            url = "http://1zou.me/api/sq/test/1/upper";
        }else {
            url = Constants.cloth_catogry_thrid_url[Constants.firstItem][position]+Constants.USER_ID+Constants.cloth_catogry_thrid_url1[Constants.firstItem][position];
        }

        LogUtils.showVerbose("WardrobeDetailActivity","url="+url);
        Constants.Wardrobe_detail_imags_url.clear();
        Constants.detail_images.clear();
        Constants.Wardrobe_detail_imags_id.clear();
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtils.showVerbose("WardrobeDetailActivity","response="+response);
                      if (!TextUtils.isEmpty(response)){
                          try {
                              JSONArray array = new JSONArray(response);
                              for (int i=0;i<array.length();i++){
                                  JSONObject object = array.getJSONObject(i);
                                  Constants.Wardrobe_detail_imags_url.add(object.getString("image_path"));
                                  Constants.Wardrobe_detail_imags_id.add(object.getString("id"));
//                                  LogUtils.showVerbose("WardrobeDetailActivity","wardrobe_url="+Constants.Wardrobe_detail_imags_url.get(i));
                                  LogUtils.showVerbose("WardrobeDetailActivity","单品的对象="+object.toString());
                              }
                              setTabSelection(position);
                          } catch (JSONException e) {
                              e.printStackTrace();
                          }

                      }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setTabSelection(position);
            }
        });
        mQueue.add(stringRequest);
    }

    public String getClothCatogry(String tabName){
        String clothName = "test";
//        String strName = thrid_item[Constants.firstItem][Constants.thridItem];
        String strName = tabName;
        if (!TextUtils.isEmpty(strName)){
            if ("颜色".equals(strName)||"款式".equals(strName)||"风格".equals(strName)){
                clothName = "useritems";
            }else if ("长度".equals(strName)){
                clothName = "useritemslen";
            }else if ("外套".equals(strName)){
                clothName = "useritemsout";
            }else if ("内搭".equals(strName)){
                clothName = "useritemsinner";
            }else if ("连衣裙".equals(strName)){
                clothName = "userdresses";
            }else if ("半身裙".equals(strName)){
                clothName = "userskirts";
            }else {
                clothName = "test";
            }
        }
        return clothName;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
//            if (ActivityColection.isContains(this)){
//                ActivityColection.removeActivity(this);
//                Intent intent = new Intent(WardrobeDetailActivity.this,MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("wardrobe_detail","wardrobe_detail");
//                startActivity(intent);
//                finish();
//            }
            DealReturnLogicUtils.dealReturnLogic(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wardrobe_detail_back_layout:
//                Intent intent = new Intent(WardrobeDetailActivity.this,MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("wardrobe_detail","wardrobe_detail");
//                startActivity(intent);
//                finish();
                DealReturnLogicUtils.dealReturnLogic(this);
                break;
            default:
                break;
        }
    }
    /**
     * 根据传入的index参数来设置选中的tab页。
     */
    private void setTabSelection(int index) {
        Bundle bundle = new Bundle();
        bundle.putString("clothCatogry",Constants.cloth_catogry_english[Constants.firstItem]);
        // 开启一个Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        removeFragments(transaction);
        switch (index) {
            case 0:
                mItemFragment1 = new ItemFragment1();
                mItemFragment1.setArguments(bundle);
                transaction.replace(R.id.wardrobe_detail_framelayout, mItemFragment1);
                break;
            case 1:
                mItemFragment2 = new ItemFragment2();
                mItemFragment2.setArguments(bundle);
                transaction.replace(R.id.wardrobe_detail_framelayout, mItemFragment2);
                break;
            case 2:
                mItemFragment3 = new ItemFragment3();
                mItemFragment3.setArguments(bundle);
                transaction.replace(R.id.wardrobe_detail_framelayout, mItemFragment3);
                break;
            case 3:
                mItemFragment4 = new ItemFragment4();
                mItemFragment4.setArguments(bundle);
                transaction.replace(R.id.wardrobe_detail_framelayout, mItemFragment4);
                break;
            case 4:
                mItemFragment5 = new ItemFragment5();
                mItemFragment5.setArguments(bundle);
                transaction.replace(R.id.wardrobe_detail_framelayout, mItemFragment5);
                break;
            case 5:
                mItemFragment6 = new ItemFragment6();
                mItemFragment6.setArguments(bundle);
                transaction.replace(R.id.wardrobe_detail_framelayout, mItemFragment6);
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
        if (mItemFragment1 != null) {
            transaction.remove(mItemFragment1);
        }
        if (mItemFragment2 != null) {
            transaction.remove(mItemFragment2);
        }
        if (mItemFragment3 != null) {
            transaction.remove(mItemFragment3);
        }
        if (mItemFragment4 != null) {
            transaction.remove(mItemFragment4);
        }
        if (mItemFragment5 != null) {
            transaction.remove(mItemFragment5);
        }
        if (mItemFragment6 != null) {
            transaction.remove(mItemFragment6);
        }
    }

}
