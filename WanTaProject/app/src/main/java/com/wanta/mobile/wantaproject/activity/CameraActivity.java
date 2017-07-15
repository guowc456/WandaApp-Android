package com.wanta.mobile.wantaproject.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.addpictag.PictureTagLayout;
import com.wanta.mobile.wantaproject.addpictag.PictureTagViewpagerAdapter;
import com.wanta.mobile.wantaproject.customview.CustomViewPager;
import com.wanta.mobile.wantaproject.customview.FilterColorView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.CacheDataHelper;
import com.wanta.mobile.wantaproject.domain.TagMsgInfo;
import com.wanta.mobile.wantaproject.fragment.CameraModifyLinkFragment;
import com.wanta.mobile.wantaproject.fragment.CameraModifyLocationFragment;
import com.wanta.mobile.wantaproject.fragment.CameraModifyMeiHuaFragment;
import com.wanta.mobile.wantaproject.fragment.CameraModifyTagsFragment;
import com.wanta.mobile.wantaproject.uploadimage.ImagesSelectorActivity;
import com.wanta.mobile.wantaproject.uploadimage.SelectorSettings;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.DealReturnLogicUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/11/15.
 */
public class CameraActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

    private LinearLayout mDot_layout;
    private CustomViewPager mViewpager;
    private MyImageView mCamera_modify_back;
    private TextView mCamera_modify_title;
    private TextView mCamera_modify_continue;
    private CameraModifyMeiHuaFragment mMeiHuaFragment;
    private CameraModifyTagsFragment mTagsFragment;
    private CameraModifyLinkFragment mLinkFragment;
    private CameraModifyLocationFragment mLocationFragment;
    private TextView mCamera_meihua;
    private TextView mCamera_tags;
    private TextView mCamera_link;
    private TextView mCamera_locatioin;
    private PictureTagViewpagerAdapter mAdapter;
    private FrameLayout mCamera_modify_fragment;
    private ArrayList<String> mStringArrayListExtra;
    private LinearLayout mPublsh_layout;
    private HorizontalScrollView mCamera_modify_horizontal_scrollow;
    private LinearLayout mCamera_modify_horizontal_layout;
    private MyImageView mCamera_modify_select_btn;
    private LinearLayout mCamera_modify_back_layout;
    private LinearLayout mCamera_modify_continue_layout;
    private int jump_flag = 0;//设置是否弹出详细信息框
    private String price;
    private PopupWindow popupWindow;
    private TextView head_tv;
    private TextView pop_window_cancle_btn;
    private TextView pop_window_ok_btn;
    private LinearLayout tag_jiage_content_layout;
    private TextView other_author_price_tv;
    private LinearLayout tag_miaosu_content_layout;
    private TextView other_author_description_tv;
    private TextView other_author_pinpai_tv;
    private TextView other_author_gouyu_tv;
    private MyImageView other_author_pinpai_image;
    private MyImageView other_author_price_image;
    private MyImageView other_author_gouyu_image;
    private MyImageView other_author_description_image;
    private LinearLayout tag_pinpai_head_layout;
    private LinearLayout tag_pinpai_content_layout;
    private LinearLayout tag_jiage_head_layout;
    private LinearLayout tag_gouyu_head_layout;
    private LinearLayout tag_gouyu_content_layout;
    private LinearLayout tag_miaosu_head_content;
    private boolean isClickPic = false;
    private RelativeLayout viewpager_layout;
    private int currentPostion = 0;
    private LinearLayout camera_head_title_layout;
    private LinearLayout system_cancel_select_pop_window_ok_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        init();
        initpagerviews();
        initDot();
    }

    private void init() {
        head_tv = (TextView) this.findViewById(R.id.head_tv);
        camera_head_title_layout = (LinearLayout) this.findViewById(R.id.camera_head_title_layout);
        viewpager_layout = (RelativeLayout) this.findViewById(R.id.viewpager_layout);
        mStringArrayListExtra = getIntent().getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
        mViewpager = (CustomViewPager) this.findViewById(R.id.viewpager);
        mViewpager.setHeight(Constants.PHONE_HEIGHT / 2);
        mViewpager.setWidth(Constants.PHONE_WIDTH);
        mDot_layout = (LinearLayout) this.findViewById(R.id.dot_layout);
        mCamera_modify_back = (MyImageView) this.findViewById(R.id.camera_modify_back);
        mCamera_modify_back.setSize(Constants.PHONE_WIDTH / 14, Constants.PHONE_WIDTH / 14);
        mCamera_modify_back_layout = (LinearLayout) this.findViewById(R.id.camera_modify_back_layout);
        mCamera_modify_continue_layout = (LinearLayout) this.findViewById(R.id.camera_modify_continue_layout);
        mCamera_modify_select_btn = (MyImageView) this.findViewById(R.id.camera_modify_select_btn);
        mCamera_modify_select_btn.setSize(Constants.PHONE_WIDTH / 16, Constants.PHONE_WIDTH / 16);
        mCamera_modify_title = (TextView) this.findViewById(R.id.camera_modify_title);
        mCamera_modify_title.setText("编辑图片(1/" + Constants.IMAGE_URL.size() + ")");
        mCamera_modify_continue = (TextView) this.findViewById(R.id.camera_modify_continue);
        mCamera_modify_fragment = (FrameLayout) this.findViewById(R.id.camera_modify_fragment);
        mCamera_meihua = (TextView) this.findViewById(R.id.camera_meihua);
        mCamera_tags = (TextView) this.findViewById(R.id.camera_tags);
        mCamera_link = (TextView) this.findViewById(R.id.camera_link);
        mCamera_locatioin = (TextView) this.findViewById(R.id.camera_locatioin);
        mPublsh_layout = (LinearLayout) this.findViewById(R.id.publsh_layout);
        mCamera_modify_horizontal_scrollow = (HorizontalScrollView) this.findViewById(R.id.camera_modify_horizontal_scrollow);
        mCamera_modify_horizontal_layout = (LinearLayout) this.findViewById(R.id.camera_modify_horizontal_layout);
        mPublsh_layout.setOnClickListener(this);
        mCamera_meihua.setOnClickListener(this);
        mCamera_tags.setOnClickListener(this);
//        mCamera_link.setOnClickListener(this);
        mCamera_locatioin.setOnClickListener(this);
        mCamera_modify_continue_layout.setOnClickListener(this);
        mCamera_modify_back_layout.setOnClickListener(this);
        if ("tags_detail".equals(getIntent().getStringExtra("tags_detail"))) {
            LogUtils.showVerbose("CameraActivity", "我是tags_detail");
            setTabSelection(2);
            btn_press(2);

            //获取当前各个标签中选择的图片的标识
        } else if ("baidumap".equals(getIntent().getStringExtra("baidumap"))) {
            setTabSelection(4);
            btn_press(4);
        } else {
            setTabSelection(1);
            btn_press(1);
        }
        if (getIntent().getIntExtra("jump_flag", 100) == 1) {
            isClickPic = true;
            jump_flag = 1;
            mHandler.sendEmptyMessageDelayed(1, 100);
        } else {
            jump_flag = 0;
        }
        if (getIntent().getIntExtra("currentPosition",100)!=100){
            currentPostion = getIntent().getIntExtra("currentPosition",100);
        }
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                showPopWindows();
                setTabSelection(2);
                btn_press(2);
            }
        }
    };

    public void initpagerviews() {
        Constants.display_images.clear();
        if (getIntent().getIntExtra("image_publish_edit", 100) == 100&&getIntent().getIntExtra("currentPosition",100)==100){
            Constants.all_picture_tag_view.clear();//把标签的图片的个数清空
        }
        for (int j = 0; j < Constants.modify_bitmap_list_url.size(); j++) {
            if (getIntent().getIntExtra("image_publish_edit", 100) != 100||getIntent().getIntExtra("currentPosition",100)!=100) {

                Bitmap bitmapFromMemCache = Constants.upload_images_lrucache.get(Constants.modify_bitmap_list_url.get(j));
                if (bitmapFromMemCache != null) {
                    FilterColorView filterColorView = new FilterColorView(this);
                    filterColorView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    filterColorView.setBitmap(bitmapFromMemCache);
                    filterColorView.setImageBitmap(bitmapFromMemCache);
                    Constants.display_images.put(j, filterColorView);

                }
            } else {
                Bitmap bitmapFromMemCache = Constants.modify_bitmap_list_lrucache.get(Constants.modify_bitmap_list_url.get(j));
                if (bitmapFromMemCache != null) {
                    FilterColorView filterColorView = new FilterColorView(this);
                    filterColorView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    filterColorView.setBitmap(bitmapFromMemCache);
                    Constants.display_images.put(j, filterColorView);
                    if (bitmapFromMemCache.getHeight()>bitmapFromMemCache.getWidth()){
                        //设置显示的大小
                        int currentHeight = bitmapFromMemCache.getHeight();
                        int currentWidth = (int) (bitmapFromMemCache.getWidth()*((Constants.PHONE_HEIGHT/2)*1.00/currentHeight));

                        PictureTagLayout pictureTagLayout = new PictureTagLayout(this);
                        pictureTagLayout.setWidthAndHeight(currentWidth,Constants.PHONE_HEIGHT/2);
                        pictureTagLayout.setX((Constants.PHONE_WIDTH-currentWidth)/2);
                        pictureTagLayout.setY(camera_head_title_layout.getHeight());
                        pictureTagLayout.setBackground(new BitmapDrawable(getResources(),bitmapFromMemCache));
                        Constants.all_picture_tag_view.add(pictureTagLayout);
                        LogUtils.showVerbose("CameraActivity","图片的个数"+Constants.all_picture_tag_view.size());
                    }else {
                        //设置显示的大小
                        int currentWidth = bitmapFromMemCache.getWidth();
                        int currentHeight =(int) (bitmapFromMemCache.getHeight()*((Constants.PHONE_WIDTH)*1.00/currentWidth));

                        PictureTagLayout pictureTagLayout = new PictureTagLayout(this);
                        pictureTagLayout.setWidthAndHeight(Constants.PHONE_WIDTH,currentHeight);
                        pictureTagLayout.setX(0);
                        pictureTagLayout.setY(camera_head_title_layout.getHeight()+(Constants.PHONE_HEIGHT/2-currentHeight)/2);
                        pictureTagLayout.setBackground(new BitmapDrawable(getResources(),bitmapFromMemCache));
                        Constants.all_picture_tag_view.add(pictureTagLayout);
                        LogUtils.showVerbose("CameraActivity","图片的个数"+Constants.all_picture_tag_view.size());
                    }

                }
            }
        }
        mAdapter = new PictureTagViewpagerAdapter(this, Constants.all_picture_tag_view);
        mAdapter.setOnPictureTagItemViewpagerListener(new PictureTagViewpagerAdapter.OnPictureTagItemViewpagerListener() {
            @Override
            public void setItemViewPagerClick(View view, int position, PictureTagLayout pictureTagLayout) {
                if (isClickPic == true&&Constants.isContainThePoint==false) {
                    //弹出标签框
                    showPopWindows();
                }
            }
        });
        mViewpager.setAdapter(mAdapter);
        if (getIntent().getIntExtra("image_publish_edit", 100) != 100) {
            mViewpager.setCurrentItem(getIntent().getIntExtra("image_publish_edit", 100));
        }
        if (getIntent().getIntExtra("currentPosition",100)!=100){
            mViewpager.setCurrentItem(currentPostion);
        }
        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setDot();
                mCamera_modify_title.setText("编辑图片(" + (position + 1) + "/" + Constants.IMAGE_URL.size() + ")");
                mViewpager.setTag(position);
                isClickPic = false;
                setTabSelection(1);
                btn_press(1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化dot
     */
    private void initDot() {
        for (int i = 0; i < Constants.display_images.size(); i++) {
            View view = new View(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            layoutParams.leftMargin = 5;
            view.setLayoutParams(layoutParams);
            view.setBackgroundResource(R.drawable.dot_selector);
            mDot_layout.addView(view);
            if (getIntent().getIntExtra("image_publish_edit", 100) != 100||getIntent().getIntExtra("currentPosition", 100)!=100) {
                if (i == getIntent().getIntExtra("image_publish_edit", 100)||i==getIntent().getIntExtra("currentPosition", 100)) {
                    mDot_layout.getChildAt(i).setEnabled(true);
                } else {
                    mDot_layout.getChildAt(i).setEnabled(false);
                }
            } else {
                if (i == 0) {
                    mDot_layout.getChildAt(i).setEnabled(true);
                } else {
                    mDot_layout.getChildAt(i).setEnabled(false);
                }
            }
        }
    }

    /**
     * 设置dot
     */
    private void setDot() {
        int currentItem = mViewpager.getCurrentItem() % Constants.display_images.size();
        for (int k = 0; k < mDot_layout.getChildCount(); k++) {
            mDot_layout.getChildAt(k).setEnabled(k == currentItem);
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     */
    private void setTabSelection(int index) {
        //记录当前传递的信息
        Bundle bundle = new Bundle();
        bundle.putInt("currentPosition", mViewpager.getCurrentItem());
        bundle.putInt("image_publish_edit", 90);
        bundle.putInt("jump_flag", jump_flag);
        // 开启一个Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        removeFragments(transaction);
        switch (index) {
            case 1:
                jump_flag = 0;
                mMeiHuaFragment = new CameraModifyMeiHuaFragment();
                transaction.replace(R.id.camera_modify_fragment, mMeiHuaFragment);
                break;
            case 2:
                mTagsFragment = new CameraModifyTagsFragment();
                mTagsFragment.setArguments(bundle);
                transaction.replace(R.id.camera_modify_fragment, mTagsFragment);
                break;
            case 3:
                jump_flag = 0;
                mLinkFragment = new CameraModifyLinkFragment();
                mLinkFragment.setArguments(bundle);
                transaction.replace(R.id.camera_modify_fragment, mLinkFragment);
                break;
//            case 4:
//            default:
//                mLocationFragment = new CameraModifyLocationFragment();
//                transaction.replace(R.id.camera_modify_fragment, mLocationFragment);
//                break;
        }
        transaction.commit();
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void removeFragments(FragmentTransaction transaction) {
        if (mMeiHuaFragment != null) {
            transaction.remove(mMeiHuaFragment);
        }
        if (mTagsFragment != null) {
            transaction.remove(mTagsFragment);
        }
        if (mLinkFragment != null) {
            transaction.remove(mLinkFragment);
        }
        if (mLocationFragment != null) {
            transaction.remove(mLocationFragment);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_meihua:
                isClickPic = false;
                setTabSelection(1);
                btn_press(1);
                break;
            case R.id.camera_tags:
                isClickPic = true;
                setTabSelection(2);
                btn_press(2);
                break;
            case R.id.camera_link:
                setTabSelection(3);
                btn_press(3);
                break;
            case R.id.camera_locatioin:
                setTabSelection(4);
                btn_press(4);
                break;
            case R.id.camera_modify_continue_layout:
//                Intent intent_continue = new Intent(CameraActivity.this, ImagesPublishActivity.class);
//                intent_continue.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent_continue);
                break;
            case R.id.camera_modify_back_layout:
                //清空图片地址的信息
//                Constants.IMAGE_URL.clear();
//                Constants.modify_bitmap_list.clear();
//                Constants.display_images.clear();
//                Constants.all_picture_tag_view.clear();
//                Intent intent_cancel = new Intent(CameraActivity.this, ImagesSelectorActivity.class);
//                intent_cancel.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent_cancel);
//                finish();
                showCancelSelectDialog();
                break;
            case R.id.publsh_layout:
                CacheDataHelper.addNullArgumentsMethod();
                ActivityColection.addActivity(this);
                Intent intent_publish = new Intent(CameraActivity.this, NewPublishActivity.class);
                intent_publish.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent_publish.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_publish);
//                Intent intent_edit_diary = new Intent(CameraActivity.this,EditeDiaryActivity.class);
//                intent_edit_diary.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent_edit_diary);
                finish();
                break;
            case R.id.tag_jiage_content_layout:
                Intent intent = new Intent(this, OtherAuthorInputPriceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                Bundle bundle = new Bundle();
//                bundle.putInt("currentPosition", mViewpager.getCurrentItem());
//                bundle.putInt("image_publish_edit", 90);
                intent.putExtras(getBundleMsg());
                startActivity(intent);
                this.finish();
                break;
            case R.id.tag_miaosu_content_layout:
                Intent intent_to_description = new Intent(this, OtherAuthorInputDescriptionActivity.class);
                intent_to_description.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                Bundle bundle_to_description = new Bundle();
//                bundle_to_description.putInt("currentPosition", mViewpager.getCurrentItem());
//                bundle_to_description.putInt("image_publish_edit", 90);
                intent_to_description.putExtras(getBundleMsg());
                startActivity(intent_to_description);
                finish();
                break;
            case R.id.tag_pinpai_content_layout:
                Intent intent_to_pinpai = new Intent(this, OtherAuthorInputPinPaiActivity.class);
                intent_to_pinpai.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent_to_pinpai.putExtras(getBundleMsg());
                startActivity(intent_to_pinpai);
                finish();
                break;
            case R.id.tag_gouyu_content_layout:
                Intent intent_to_gouyu = new Intent(CameraActivity.this, NewLocationBaiduMapActivity.class);
                intent_to_gouyu.putExtras(getBundleMsg());
                startActivity(intent_to_gouyu);
                finish();
                break;
            case R.id.pop_window_cancle_btn:
                popupWindow.dismiss();
                other_author_price_tv.setText("");
                other_author_description_tv.setText("");
                other_author_gouyu_tv.setText("");
                other_author_pinpai_tv.setText("");
                break;

            default:
                break;
        }
    }

    public Bundle getBundleMsg(){
        Bundle bundle = new Bundle();
        bundle.putInt("currentPosition", mViewpager.getCurrentItem());
        bundle.putInt("image_publish_edit", 90);
        return bundle;
    }

    private void btn_press(int pos) {
        switch (pos) {
            case 1:
                //选中的是美化
                mCamera_meihua.setTextColor(getResources().getColor(R.color.main_btn_color));
                mCamera_tags.setTextColor(Color.BLACK);
                mCamera_link.setTextColor(Color.BLACK);
                mCamera_locatioin.setTextColor(Color.BLACK);
                break;
            case 2:
                //选中的是标签
                mCamera_meihua.setTextColor(Color.BLACK);
                mCamera_tags.setTextColor(getResources().getColor(R.color.main_btn_color));
                mCamera_link.setTextColor(Color.BLACK);
                mCamera_locatioin.setTextColor(Color.BLACK);
                break;
            case 3:
                //选中的是链接
                mCamera_meihua.setTextColor(Color.BLACK);
                mCamera_tags.setTextColor(Color.BLACK);
                mCamera_link.setTextColor(getResources().getColor(R.color.main_btn_color));
                mCamera_locatioin.setTextColor(Color.BLACK);
                break;
            case 4:
                //选中的是地点
                mCamera_meihua.setTextColor(Color.BLACK);
                mCamera_tags.setTextColor(Color.BLACK);
                mCamera_link.setTextColor(Color.BLACK);
                mCamera_locatioin.setTextColor(getResources().getColor(R.color.main_btn_color));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showCancelSelectDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    public PictureTagViewpagerAdapter getAdapter() {
        return mAdapter;
    }

    private void showPopWindows() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_window_add_flags_layout, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(head_tv);
        View contentView = popupWindow.getContentView();
        MyImageView pop_window_select_icon = (MyImageView) contentView.findViewById(R.id.pop_window_select_icon);
        pop_window_select_icon.setSize(Constants.PHONE_WIDTH / 16, Constants.PHONE_WIDTH / 16);
        MyImageView pop_window_right_arrows_icon = (MyImageView) contentView.findViewById(R.id.pop_window_right_arrows_icon);
        pop_window_right_arrows_icon.setSize(Constants.PHONE_WIDTH / 26, Constants.PHONE_WIDTH / 24);
        //取消按钮时间
        pop_window_cancle_btn = (TextView) contentView.findViewById(R.id.pop_window_cancle_btn);
        pop_window_cancle_btn.setOnClickListener(this);
        //确定按钮时间
        pop_window_ok_btn = (TextView) contentView.findViewById(R.id.pop_window_ok_btn);
        pop_window_cancle_btn = (TextView) contentView.findViewById(R.id.pop_window_cancle_btn);
        pop_window_cancle_btn.setOnClickListener(this);
        tag_jiage_content_layout = (LinearLayout) contentView.findViewById(R.id.tag_jiage_content_layout);
        tag_jiage_content_layout.setOnClickListener(this);
        other_author_price_tv = (TextView) contentView.findViewById(R.id.other_author_price_tv);
        other_author_price_tv.setText(Constants.other_author_pop_window_price);
        tag_miaosu_content_layout = (LinearLayout) contentView.findViewById(R.id.tag_miaosu_content_layout);
        tag_miaosu_content_layout.setOnClickListener(this);
        other_author_description_tv = (TextView) contentView.findViewById(R.id.other_author_description_tv);
        other_author_description_tv.setText(Constants.other_author_pop_window_description);
        other_author_pinpai_tv = (TextView) contentView.findViewById(R.id.other_author_pinpai_tv);
        other_author_pinpai_tv.setText(Constants.other_author_pop_window_pinpai);
        other_author_gouyu_tv = (TextView) contentView.findViewById(R.id.other_author_gouyu_tv);
        other_author_gouyu_tv.setText(Constants.other_author_pop_window_gouyu);

        other_author_pinpai_image = (MyImageView) contentView.findViewById(R.id.other_author_pinpai_image);
        other_author_pinpai_image.setSize(Constants.PHONE_WIDTH / 20, Constants.PHONE_WIDTH / 20);
        other_author_price_image = (MyImageView) contentView.findViewById(R.id.other_author_price_image);
        other_author_price_image.setSize(Constants.PHONE_WIDTH / 20, Constants.PHONE_WIDTH / 20);
        other_author_gouyu_image = (MyImageView) contentView.findViewById(R.id.other_author_gouyu_image);
        other_author_gouyu_image.setSize(Constants.PHONE_WIDTH / 20, Constants.PHONE_WIDTH / 20);
        other_author_description_image = (MyImageView) contentView.findViewById(R.id.other_author_description_image);
        other_author_description_image.setSize(Constants.PHONE_WIDTH / 20, Constants.PHONE_WIDTH / 20);

        //设置距离
        tag_pinpai_head_layout = (LinearLayout) contentView.findViewById(R.id.tag_pinpai_head_layout);
        LinearLayout.LayoutParams tag_pinpai_head_params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH / 3, Constants.PHONE_HEIGHT / 14);
        tag_pinpai_head_layout.setLayoutParams(tag_pinpai_head_params);
        tag_pinpai_content_layout = (LinearLayout) contentView.findViewById(R.id.tag_pinpai_content_layout);
        LinearLayout.LayoutParams tag_pinpai_content_params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH / 2, Constants.PHONE_HEIGHT / 14);
        tag_pinpai_content_params.setMargins(Constants.PHONE_WIDTH / 20, 0, 0, 0);
        tag_pinpai_content_layout.setLayoutParams(tag_pinpai_content_params);
        tag_pinpai_content_layout.setOnClickListener(this);

        tag_jiage_head_layout = (LinearLayout) contentView.findViewById(R.id.tag_jiage_head_layout);
        LinearLayout.LayoutParams tag_jiage_head_params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH / 3, Constants.PHONE_HEIGHT / 14);
        tag_jiage_head_layout.setLayoutParams(tag_jiage_head_params);
        tag_jiage_content_layout = (LinearLayout) contentView.findViewById(R.id.tag_jiage_content_layout);
        LinearLayout.LayoutParams tag_jiage_content_params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH / 2, Constants.PHONE_HEIGHT / 14);
        tag_jiage_content_params.setMargins(Constants.PHONE_WIDTH / 20, 0, 0, 0);
        tag_jiage_content_layout.setLayoutParams(tag_jiage_content_params);

        tag_gouyu_head_layout = (LinearLayout) contentView.findViewById(R.id.tag_gouyu_head_layout);
        LinearLayout.LayoutParams tag_gouyu_head_params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH / 3, Constants.PHONE_HEIGHT / 14);
        tag_gouyu_head_layout.setLayoutParams(tag_gouyu_head_params);
        tag_gouyu_content_layout = (LinearLayout) contentView.findViewById(R.id.tag_gouyu_content_layout);
        LinearLayout.LayoutParams tag_gouyu_content_params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH / 2, Constants.PHONE_HEIGHT / 14);
        tag_gouyu_content_params.setMargins(Constants.PHONE_WIDTH / 20, 0, 0, 0);
        tag_gouyu_content_layout.setLayoutParams(tag_gouyu_content_params);
        tag_gouyu_content_layout.setOnClickListener(this);

        tag_miaosu_head_content = (LinearLayout) contentView.findViewById(R.id.tag_miaosu_head_content);
        LinearLayout.LayoutParams tag_miaosu_head_params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH / 3, Constants.PHONE_HEIGHT / 14);
        tag_miaosu_head_content.setLayoutParams(tag_miaosu_head_params);
        tag_miaosu_content_layout = (LinearLayout) contentView.findViewById(R.id.tag_miaosu_content_layout);
        LinearLayout.LayoutParams tag_miaosu_content_params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH / 2, Constants.PHONE_HEIGHT / 14);
        tag_miaosu_content_params.setMargins(Constants.PHONE_WIDTH / 20, 0, 0, 0);
        tag_miaosu_content_layout.setLayoutParams(tag_miaosu_content_params);
        pop_window_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(Constants.other_author_pop_window_pinpai)&&
                        TextUtils.isEmpty(Constants.other_author_pop_window_price)&&
                        TextUtils.isEmpty(Constants.other_author_pop_window_gouyu)&&
                        TextUtils.isEmpty(Constants.other_author_pop_window_description)){
                    //当前信息为空
                    Toast.makeText(CameraActivity.this,"标签的信息不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    popupWindow.dismiss();
                    Constants.all_picture_tag_view.get(mViewpager.getCurrentItem()).addTagItemView(
                            Constants.other_author_pop_window_pinpai,Constants.other_author_pop_window_price,
                            Constants.other_author_pop_window_gouyu,Constants.other_author_pop_window_description);
                     if (Constants.saveTagMsgMap.containsKey(mViewpager.getCurrentItem())){
                         Constants.saveTagMsgMap.get(mViewpager.getCurrentItem()).add(
                                 new TagMsgInfo(Constants.other_author_pop_window_description,
                            Constants.other_author_pop_window_pinpai,
                            Constants.other_author_pop_window_gouyu,
                            Constants.other_author_pop_window_price)
                         );
                     }else {
                         List<TagMsgInfo> list = new ArrayList<TagMsgInfo>();
                         list.add(new TagMsgInfo(Constants.other_author_pop_window_description,
                                 Constants.other_author_pop_window_pinpai,
                                 Constants.other_author_pop_window_gouyu,
                                 Constants.other_author_pop_window_price));
                         Constants.saveTagMsgMap.put(mViewpager.getCurrentItem(),list);
                     }
                }

            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取当前点击图片的位置坐标
                if (isClickPic == true) {
                    float clickX = event.getX();
                    float clickY = event.getY();
                    LogUtils.showVerbose("CameraActivity", "x==" + clickX + "  y==" + clickY);
                }
                break;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(popupWindow!=null&&popupWindow.isShowing())
            popupWindow.dismiss();
        popupWindow=null;
    }

    public void showCancelSelectDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.system_cancel_select_pop_window_layout,null);
        final PopupWindow popupWindow = new PopupWindow(view, Constants.PHONE_WIDTH, Constants.PHONE_HEIGHT, false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.showAsDropDown(head_tv);
        View contentView = popupWindow.getContentView();
        LinearLayout system_cancel_select_pop_window_message_layout = (LinearLayout) contentView.findViewById(R.id.system_cancel_select_pop_window_message_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) ((Constants.PHONE_WIDTH * 1.00 / 7) * 5),
                Constants.PHONE_HEIGHT / 4);
        system_cancel_select_pop_window_message_layout.setLayoutParams(params);
        LinearLayout system_cancel_select_pop_window_cancel_layout = (LinearLayout) contentView.findViewById(R.id.system_cancel_select_pop_window_cancel_layout);
        system_cancel_select_pop_window_cancel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Constants.IMAGE_URL.clear();
                Constants.modify_bitmap_list.clear();
                Constants.display_images.clear();
                Constants.all_picture_tag_view.clear();
                Constants.saveTagMsgMap.clear();
//                Intent intent_cancel = new Intent(CameraActivity.this, ImagesSelectorActivity.class);
//                intent_cancel.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent_cancel);
//                finish();
                DealReturnLogicUtils.dealReturnLogic(CameraActivity.this);
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
}
