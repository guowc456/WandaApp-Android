package com.wanta.mobile.wantaproject.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.ItemMostPopularRecycleViewAdapter;
import com.wanta.mobile.wantaproject.adapter.ItemMostpopularViewpagerAdapter;
import com.wanta.mobile.wantaproject.adapter.MostPopularFragmentAdapter;
import com.wanta.mobile.wantaproject.adapter.TopicsDetailMessageAdapter;
import com.wanta.mobile.wantaproject.adapter.TopicsLinkRecycleviewAdapter;
import com.wanta.mobile.wantaproject.customview.AnimationBgView;
import com.wanta.mobile.wantaproject.customview.AnimationView;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.CustomViewPager;
import com.wanta.mobile.wantaproject.customview.ExStaggeredGridLayoutManager;
import com.wanta.mobile.wantaproject.customview.FixGridLayout;
import com.wanta.mobile.wantaproject.customview.KeyboardListenRelativeLayout;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.customview.MyScrollview;
import com.wanta.mobile.wantaproject.domain.CacheDataHelper;
import com.wanta.mobile.wantaproject.domain.CacheDataUtils;
import com.wanta.mobile.wantaproject.domain.CommentsInfo;
import com.wanta.mobile.wantaproject.domain.MostPopularInfo;
import com.wanta.mobile.wantaproject.domain.ReplyCommentInfo;
import com.wanta.mobile.wantaproject.fragment.ItemMostpopularSameStyleMatchFragment;
import com.wanta.mobile.wantaproject.fragment.ItemMostpopularSameStyleSingleFragment;
import com.wanta.mobile.wantaproject.uploadimage.DraweeUtils;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.DealReturnLogicUtils;
import com.wanta.mobile.wantaproject.utils.FullyLinearLayoutManager;
import com.wanta.mobile.wantaproject.utils.ImageDealUtils;
import com.wanta.mobile.wantaproject.utils.JsonParseUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;
import com.wanta.mobile.wantaproject.utils.NetUtils;
import com.wanta.mobile.wantaproject.utils.SimpleDraweeControlUtils;
import com.wanta.mobile.wantaproject.utils.SystemUtils;
import com.wanta.mobile.wantaproject.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/24.
 */
public class ItemMostpopularActivity extends BaseActivity implements View.OnClickListener {

    private AnimationView mAnimation_one;
    private List<String> mList = new ArrayList<>();
    private AnimationBgView mAnimationBgView;
    private boolean isStartAnimation = false;
    private AnimatorSet mAnimSet;
    private FrameLayout mAnimation_layout;
    private CustomSimpleDraweeView mItem_mostpopular__author_icon;
    private TextView mItem_mostpopular__author;
    private TextView mItem_mostpopular_care;
    private MyImageView mItem_mostpopular_income_icon;
    private CustomViewPager mItem_mostpopular_viewpager;
    private List<ImageView> image_list = new ArrayList<>();
    private HashMap<Integer, View> map = new HashMap<>();
    private String clothCatogry[] = {
            "帽子", "外套", "内搭", "裙子", "鞋子", "包包", "钱包"
    };
    private MyImageView mItem_mostpopular_back;
    private MyImageView mItem_mostpopular_share;
    private CustomSimpleDraweeView mItem_mostpopular_msg_comment_icon;
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private RecyclerView mItem_mostpopular_recycleview;
    private List<ReplyCommentInfo> mReplyCommentList = new ArrayList<>();
    private LinearLayout mSame_style_layout;
    private LinearLayout mSingle_style_layout;
    private FrameLayout mStyle_frameLayout;
    private ItemMostpopularSameStyleMatchFragment mMatchFragment;
    private ItemMostpopularSameStyleSingleFragment mSingleFragment;
    private TextView mSame_style_tv;
    private TextView mSingle_style_tv;
    private View mSame_style_line;
    private View mSingle_style_line;
    private LinearLayout mItem_mostpopular_heart_layout;
    private MyImageView mItem_mostpopular_heart_icon;
    private TextView mItem_mostpopular_heart_content;
    private LinearLayout mItem_mostpopular_dialog_layout;
    private MyImageView mItem_mostpopular_dialog_icon;
    private TextView mItem_mostpopular_dialog_content;
    private LinearLayout mItem_mostpopular_store_layout;
    private MyImageView mItem_mostpopular_store_icon;
    private TextView mItem_mostpopular_look_all_comments;
    private KeyboardListenRelativeLayout mItem_mostpopular_msg_keyboard_listener;
    private LinearLayout mItem_mostpopular_msg_edit;
    private LinearLayout mItem_mostpopular_msg_publish;
    private EditText mItem_mostpopular_edit_comment;
    private TextView mItem_mostpopular_msg_publish_ok;
    private EditText mItem_mostpopular_msg_publish_input;

    private TextView mItem_mostpopular_msg_titls;
    private TextView mItem_mostpopular_msg;
    private TextView mItem_mostpopular_msg_date;
    private TextView mItem_mostpopular_msg_store;
    private TextView mItem_mostpopular_msg_click;
    private int CARE_SUCCESS = 1;
    private int CARE_FAILE = 2;
    private int COMMENT_SUCCESS = 3;
    private List<CommentsInfo> mComList = new ArrayList<>();//记录当前的评论点赞的信息
    private LinearLayout mItem_mostpopular_head_layout;
    private LinearLayout mItem_mostpopular_back_layout;
    private LinearLayout mItem_mostpopular_share_layout;
    private LinearLayout mItem_mostpopular_location_msg;
    private MyImageView mItem_mostpopular_location_icon;
    private MyImageView item_mostpopular_body_msg_icon;
    private TextView item_mostpopular_location_msg;
    private int[] topicsArray;
    private TextView item_mostpopular_msg_titls_chinese;
    private TextView item_mostpopular_msg_chinese;
    private TextView translate;
    private boolean isOpenTranslate = false;
    private RecyclerView topics_link_recycleview;
    private MyScrollview mItem_mostpopular_scrollview;
    private TextView current_image_number;
    private TextView all_image_number;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_mostpopular_activity);
        //获取topics中的数字
        topicsArray = JsonParseUtils.getTopicNumber(CacheDataUtils.getCurrentNeedToSaveData().getTopics());
        initComments();
        //初始化当前的同风格搭配和同风格单品的内容
        initId();
        //添加标签的信息
        for (int i = 0; i < clothCatogry.length; i++) {
            mList.add(clothCatogry[i]);
        }
        initAnimation();
    }

    private void initId() {
        List<MostPopularInfo> allPicUrl = null;
        try {
            allPicUrl = JsonParseUtils.getAllPicUrl(CacheDataUtils.getCurrentNeedToSaveData().getImages());
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.showVerbose("ItemMostpopularActivity", "对images解析错误");
        }
        //显示原图图片
        for (int j = 0; j < allPicUrl.size(); j++) {
            final CustomSimpleDraweeView customSimpleDraweeView = new CustomSimpleDraweeView(ItemMostpopularActivity.this);
            customSimpleDraweeView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            //判断当前的图片的高度是否大于规定的高度：Constants.PHONE_HEIGHT / 2
            customSimpleDraweeView.setHeight(Constants.PHONE_HEIGHT / 2);
            //设置如果当前获取的宽度是大于屏幕的宽度，就默认等于屏幕的宽度
            if ((int) (allPicUrl.get(j).getPicWidth() * (((Constants.PHONE_HEIGHT / 2) * 1.00) / allPicUrl.get(j).getPicHeight())) > Constants.PHONE_WIDTH) {
                customSimpleDraweeView.setWidth(Constants.PHONE_WIDTH);
            } else {
                customSimpleDraweeView.setWidth((int) (allPicUrl.get(j).getPicWidth() * (((Constants.PHONE_HEIGHT / 2) * 1.00) / allPicUrl.get(j).getPicHeight())));
            }
            //设置当前的图片是否需要居中显示
            if ((Constants.PHONE_WIDTH / 2 - (int) (allPicUrl.get(j).getPicWidth() * (((Constants.PHONE_HEIGHT / 2) * 1.00) / allPicUrl.get(j).getPicHeight())) / 2) < 0) {
                customSimpleDraweeView.setPaddingLeft(0);
            } else {
                customSimpleDraweeView.setPaddingLeft((Constants.PHONE_WIDTH / 2 - (int) (allPicUrl.get(j).getPicWidth() * (((Constants.PHONE_HEIGHT / 2) * 1.00) / allPicUrl.get(j).getPicHeight())) / 2));
            }
            LogUtils.showVerbose("ItemMostpopularActivity", "宽度差：" + (Constants.PHONE_WIDTH / 2 - (int) (allPicUrl.get(j).getPicWidth() * (((Constants.PHONE_HEIGHT / 2) * 1.00) / allPicUrl.get(j).getPicHeight())) / 2));
            DraweeUtils.resizepic(Uri.parse(Constants.FIRST_PAGE_IMAGE_URL + allPicUrl.get(j).getPicUrl()), customSimpleDraweeView);
            map.put(j, customSimpleDraweeView);
        }

        //描述当前图片的个数
        current_image_number = (TextView) this.findViewById(R.id.current_image_number);
        all_image_number = (TextView) this.findViewById(R.id.all_image_number);
        current_image_number.setText("1");
        all_image_number.setText("/" + map.size());
        mAnimation_layout = (FrameLayout) this.findViewById(R.id.animation_layout);
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) mAnimation_layout.getLayoutParams();
        linearParams.height = Constants.PHONE_HEIGHT / 2;
        linearParams.width = Constants.PHONE_WIDTH / 3 + 50;
        mAnimation_layout.setLayoutParams(linearParams);
        mItem_mostpopular__author_icon = (CustomSimpleDraweeView) this.findViewById(R.id.item_mostpopular_author_icon);
        mItem_mostpopular__author_icon.setWidth(Constants.PHONE_WIDTH / 12);
        mItem_mostpopular__author_icon.setHeight(Constants.PHONE_WIDTH / 12);
        if ("null".equals(CacheDataUtils.getCurrentNeedToSaveData().getUseravatar()) || TextUtils.isEmpty(CacheDataUtils.getCurrentNeedToSaveData().getUseravatar())) {
            mItem_mostpopular__author_icon.setImageBitmap(ImageDealUtils.readBitMap(this,R.mipmap.icon));
        } else {
            SimpleDraweeControlUtils.getNetRingSimpleDraweeControl(this, mItem_mostpopular__author_icon, Constants.FIRST_PAGE_IMAGE_URL + "/" + CacheDataUtils.getCurrentNeedToSaveData().getUseravatar());
        }
        mItem_mostpopular__author_icon.setOnClickListener(this);

        mItem_mostpopular__author = (TextView) this.findViewById(R.id.item_mostpopular_author);
        mItem_mostpopular__author.setText(CacheDataUtils.getCurrentNeedToSaveData().getUsername());
        item_mostpopular_body_msg_icon = (MyImageView) this.findViewById(R.id.item_mostpopular_body_msg_icon);
        item_mostpopular_body_msg_icon.setSize(Constants.PHONE_WIDTH / 12, Constants.PHONE_WIDTH / 12);
        item_mostpopular_body_msg_icon.setOnClickListener(this);
        LinearLayout item_mostpopular_care_layout = (LinearLayout) this.findViewById(R.id.item_mostpopular_care_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH / 5, Constants.PHONE_WIDTH / 14);
        item_mostpopular_care_layout.setLayoutParams(params);
        mItem_mostpopular_care = (TextView) this.findViewById(R.id.item_mostpopular_care);
        mItem_mostpopular_care.setOnClickListener(this);

        if ("null".equals(CacheDataUtils.getCurrentNeedToSaveData().getFollowstate())) {
            LogUtils.showVerbose("ItemMostpopularActivity", "没关注");
            mItem_mostpopular_care.setSelected(false);
            mItem_mostpopular_care.setText("未关注");
        } else {
            mItem_mostpopular_care.setSelected(true);
            mItem_mostpopular_care.setText("已关注");
        }

        TextView item_body_detail_msg = (TextView) this.findViewById(R.id.item_body_detail_msg);
        item_body_detail_msg.setText(CacheDataUtils.getCurrentNeedToSaveData().getHeight() + "cm/" + CacheDataUtils.getCurrentNeedToSaveData().getWeight()
                + "kg    " + CacheDataUtils.getCurrentNeedToSaveData().getBust() + CacheDataUtils.getCurrentNeedToSaveData().getBra());
        mItem_mostpopular_location_icon = (MyImageView) this.findViewById(R.id.item_mostpopular_location_icon);
        mItem_mostpopular_location_icon.setSize(Constants.PHONE_WIDTH / 13, Constants.PHONE_WIDTH / 13);
        item_mostpopular_location_msg = (TextView) this.findViewById(R.id.item_mostpopular_location_msg);
        if (TextUtils.isEmpty(CacheDataUtils.getCurrentNeedToSaveData().getAddress()) || "null".equals(CacheDataUtils.getCurrentNeedToSaveData().getAddress())) {
            item_mostpopular_location_msg.setText("");
            mItem_mostpopular_location_icon.setVisibility(View.INVISIBLE);
            item_mostpopular_location_msg.setVisibility(View.GONE);
        } else {
            item_mostpopular_location_msg.setText(CacheDataUtils.getCurrentNeedToSaveData().getAddress());
            mItem_mostpopular_location_icon.setVisibility(View.VISIBLE);
            item_mostpopular_location_msg.setVisibility(View.VISIBLE);
        }

        mItem_mostpopular_back = (MyImageView) this.findViewById(R.id.item_mostpopular_back);
        mItem_mostpopular_back.setSize(Constants.PHONE_WIDTH / 16, Constants.PHONE_WIDTH / 16);
        mItem_mostpopular_back_layout = (LinearLayout) this.findViewById(R.id.item_mostpopular_back_layout);
        mItem_mostpopular_back_layout.setOnClickListener(this);
        mItem_mostpopular_share = (MyImageView) this.findViewById(R.id.item_mostpopular_share);
        mItem_mostpopular_share_layout = (LinearLayout) this.findViewById(R.id.item_mostpopular_share_layout);
        mItem_mostpopular_share_layout.setOnClickListener(this);
        mItem_mostpopular_share.setSize(Constants.PHONE_WIDTH / 16, Constants.PHONE_WIDTH / 16);
        mItem_mostpopular_msg_comment_icon = (CustomSimpleDraweeView) this.findViewById(R.id.item_mostpopular_msg_comment_icon);
        mItem_mostpopular_msg_comment_icon.setWidth(Constants.PHONE_WIDTH / 12);
        mItem_mostpopular_msg_comment_icon.setHeight(Constants.PHONE_WIDTH / 12);
//        mItem_mostpopular_msg_comment_icon.setImageResource(R.mipmap.icon);
        mItem_mostpopular_msg_comment_icon.setImageBitmap(ImageDealUtils.readBitMap(this,R.mipmap.icon));
        mItem_mostpopular_msg_comment_icon.setOnClickListener(this);

        //文章信息
        mItem_mostpopular_msg_titls = (TextView) this.findViewById(R.id.item_mostpopular_msg_titls);
        if (TextUtils.isEmpty(CacheDataUtils.getCurrentNeedToSaveData().getTitle()) || "null".equals(CacheDataUtils.getCurrentNeedToSaveData().getTitle())) {
            mItem_mostpopular_msg_titls.setText("");
        } else {
            mItem_mostpopular_msg_titls.setText(CacheDataUtils.getCurrentNeedToSaveData().getTitle());
        }
        mItem_mostpopular_msg = (TextView) this.findViewById(R.id.item_mostpopular_msg);
        if (TextUtils.isEmpty(CacheDataUtils.getCurrentNeedToSaveData().getContent()) || "null".equals(CacheDataUtils.getCurrentNeedToSaveData().getContent())) {
            mItem_mostpopular_msg.setText("");
        } else {
            mItem_mostpopular_msg.setText(CacheDataUtils.getCurrentNeedToSaveData().getContent());
        }

        //添加翻译
        item_mostpopular_msg_titls_chinese = (TextView) this.findViewById(R.id.item_mostpopular_msg_titls_chinese);
        item_mostpopular_msg_chinese = (TextView) this.findViewById(R.id.item_mostpopular_msg_chinese);
        item_mostpopular_msg_titls_chinese.setText(CacheDataUtils.getCurrentNeedToSaveData().getTitle_cn());
        item_mostpopular_msg_chinese.setText(CacheDataUtils.getCurrentNeedToSaveData().getContent_cn());
        item_mostpopular_msg_titls_chinese.setVisibility(View.GONE);
        item_mostpopular_msg_chinese.setVisibility(View.GONE);
        translate = (TextView) this.findViewById(R.id.translate);
        translate.setTextColor(Color.rgb(5, 99, 193));
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpenTranslate == true) {
                    isOpenTranslate = false;
                    item_mostpopular_msg_titls_chinese.setVisibility(View.GONE);
                    item_mostpopular_msg_chinese.setVisibility(View.GONE);
                } else {
                    isOpenTranslate = true;
                    item_mostpopular_msg_titls_chinese.setVisibility(View.VISIBLE);
                    item_mostpopular_msg_chinese.setVisibility(View.VISIBLE);
                }

            }
        });

        mItem_mostpopular_msg_date = (TextView) this.findViewById(R.id.item_mostpopular_msg_date);
        if (TextUtils.isEmpty(CacheDataUtils.getCurrentNeedToSaveData().getCreatedat()) || "null".equals(CacheDataUtils.getCurrentNeedToSaveData().getCreatedat())) {
            mItem_mostpopular_msg_date.setText("");
        } else {
            mItem_mostpopular_msg_date.setText(CacheDataUtils.getCurrentNeedToSaveData().getCreatedat());
        }

        mItem_mostpopular_msg_store = (TextView) this.findViewById(R.id.item_mostpopular_msg_store);

        mItem_mostpopular_msg_click = (TextView) this.findViewById(R.id.item_mostpopular_msg_click);
        mItem_mostpopular_msg_click.setText(Constants.likenum + "次赞");

        //风格
        mSame_style_layout = (LinearLayout) this.findViewById(R.id.same_style_layout);
        mSame_style_layout.setOnClickListener(this);
        mSingle_style_layout = (LinearLayout) this.findViewById(R.id.single_style_layout);
        mSingle_style_layout.setOnClickListener(this);
        mStyle_frameLayout = (FrameLayout) this.findViewById(R.id.style_frameLayout);
        mSame_style_tv = (TextView) this.findViewById(R.id.same_style_tv);
        mSingle_style_tv = (TextView) this.findViewById(R.id.single_style_tv);
        mSame_style_line = this.findViewById(R.id.same_style_line);
        mSingle_style_line = this.findViewById(R.id.single_style_line);
        //收藏点赞
        mItem_mostpopular_heart_layout = (LinearLayout) this.findViewById(R.id.item_mostpopular_heart_layout);
        mItem_mostpopular_heart_layout.setOnClickListener(this);
        mItem_mostpopular_heart_icon = (MyImageView) this.findViewById(R.id.item_mostpopular_heart_icon);
        mItem_mostpopular_heart_icon.setSize(Constants.PHONE_WIDTH / 12, Constants.PHONE_WIDTH / 12);
        mItem_mostpopular_heart_icon.setOnClickListener(this);
        if (Constants.islike) {
            mItem_mostpopular_heart_icon.setSelected(true);
        } else {
            mItem_mostpopular_heart_icon.setSelected(false);
        }
        mItem_mostpopular_heart_content = (TextView) this.findViewById(R.id.item_mostpopular_heart_content);
        mItem_mostpopular_heart_content.setText(Constants.likenum + "");
        mItem_mostpopular_dialog_layout = (LinearLayout) this.findViewById(R.id.item_mostpopular_dialog_layout);
        mItem_mostpopular_dialog_layout.setOnClickListener(this);
        mItem_mostpopular_dialog_icon = (MyImageView) this.findViewById(R.id.item_mostpopular_dialog_icon);
        mItem_mostpopular_dialog_icon.setSize(Constants.PHONE_WIDTH / 12, Constants.PHONE_WIDTH / 12);
        mItem_mostpopular_dialog_icon.setOnClickListener(this);
        mItem_mostpopular_dialog_content = (TextView) this.findViewById(R.id.item_mostpopular_dialog_content);
        mItem_mostpopular_dialog_content.setText(Constants.allComments + "");
        mItem_mostpopular_store_layout = (LinearLayout) this.findViewById(R.id.item_mostpopular_store_layout);
        mItem_mostpopular_store_layout.setOnClickListener(this);
        mItem_mostpopular_store_icon = (MyImageView) this.findViewById(R.id.item_mostpopular_store_icon);
        mItem_mostpopular_store_icon.setSize(Constants.PHONE_WIDTH / 20, Constants.PHONE_WIDTH / 20);
        mItem_mostpopular_store_icon.setOnClickListener(this);

        mItem_mostpopular_msg_store.setText(CacheDataUtils.getCurrentNeedToSaveData().getStorenum() + "次收藏");
        Constants.storenum = CacheDataUtils.getCurrentNeedToSaveData().getStorenum();
        if ("null".equals(CacheDataUtils.getCurrentNeedToSaveData().getStoredstate())) {
            Constants.isstore = false;
            mItem_mostpopular_store_icon.setSelected(false);
        } else {
            Constants.isstore = true;
            mItem_mostpopular_store_icon.setSelected(true);
        }
        //查看所有的评论
        mItem_mostpopular_look_all_comments = (TextView) this.findViewById(R.id.item_mostpopular_look_all_comments);
        mItem_mostpopular_look_all_comments.setText("查看全部" + Constants.allComments + "条评论");
        mItem_mostpopular_look_all_comments.setOnClickListener(this);
        //显示输入框
        mItem_mostpopular_msg_edit = (LinearLayout) this.findViewById(R.id.item_mostpopular_msg_edit);
        mItem_mostpopular_msg_publish = (LinearLayout) this.findViewById(R.id.item_mostpopular_msg_publish);
        mItem_mostpopular_edit_comment = (EditText) this.findViewById(R.id.item_mostpopular_edit_comment);
        mItem_mostpopular_edit_comment.setOnClickListener(this);
        mItem_mostpopular_msg_publish_ok = (TextView) this.findViewById(R.id.item_mostpopular_msg_publish_ok);
        mItem_mostpopular_msg_publish_ok.setOnClickListener(this);
        mItem_mostpopular_msg_publish_input = (EditText) this.findViewById(R.id.item_mostpopular_msg_publish_input);
        mItem_mostpopular_msg_keyboard_listener = (KeyboardListenRelativeLayout) this.findViewById(R.id.item_mostpopular_msg_keyboard_listener);
        mItem_mostpopular_msg_keyboard_listener.setOnKeyboardStateChangedListener(new KeyboardListenRelativeLayout.IOnKeyboardStateChangedListener() {
            @Override
            public void onKeyboardStateChanged(int state) {
                switch (state) {
                    case KeyboardListenRelativeLayout.KEYBOARD_STATE_HIDE://软键盘隐藏
                        mItem_mostpopular_msg_edit.setVisibility(View.VISIBLE);
                        mItem_mostpopular_msg_publish.setVisibility(View.GONE);
                        break;
                    case KeyboardListenRelativeLayout.KEYBOARD_STATE_SHOW://软键盘显示
                        mItem_mostpopular_msg_edit.setVisibility(View.GONE);
                        mItem_mostpopular_msg_publish.setVisibility(View.VISIBLE);
                        mItem_mostpopular_msg_publish_input.requestFocus();
                        break;
                    default:
                        break;
                }
            }
        });

        mItem_mostpopular_viewpager = (CustomViewPager) this.findViewById(R.id.item_mostpopular_viewpager);
        mItem_mostpopular_viewpager.setWidth(Constants.PHONE_WIDTH);
        mItem_mostpopular_viewpager.setHeight(Constants.PHONE_HEIGHT / 2);
        ItemMostpopularViewpagerAdapter adapter = new ItemMostpopularViewpagerAdapter(this, map);

        mItem_mostpopular_viewpager.setAdapter(adapter);
        adapter.setOnItemViewpagerListener(new ItemMostpopularViewpagerAdapter.OnItemViewpagerListener() {
            @Override
            public void setItemViewPagerClick(View view, int position) {
//                if (isStartAnimation) {
//                    mAnimation_layout.setVisibility(View.GONE);
//                    isStartAnimation = false;
//                } else {
//                    mAnimation_layout.setVisibility(View.VISIBLE);
//                    isStartAnimation = true;
//                    startAnimation();
//                }
            }
        });
        mItem_mostpopular_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mAnimation_layout.setVisibility(View.GONE);
                isStartAnimation = false;
                current_image_number.setText((position + 1) + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mItem_mostpopular_scrollview = (MyScrollview) this.findViewById(R.id.item_mostpopular_scrollview);
        mItem_mostpopular_head_layout = (LinearLayout) this.findViewById(R.id.item_mostpopular_head_layout);


        //设置话题
        topics_link_recycleview = (RecyclerView) this.findViewById(R.id.topics_link_recycleview);
        ExStaggeredGridLayoutManager staggeredGridLayoutManager = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        topics_link_recycleview.setLayoutManager(staggeredGridLayoutManager);
        TopicsLinkRecycleviewAdapter topicsLinkRecycleviewAdapter = new TopicsLinkRecycleviewAdapter(this, topicsArray);
        topics_link_recycleview.setAdapter(topicsLinkRecycleviewAdapter);
        topicsLinkRecycleviewAdapter.setOnTopicsLinkRecycleviewListener(new TopicsLinkRecycleviewAdapter.OnTopicsLinkRecycleviewListener() {
            @Override
            public void onItemClick(View view) {
                int childAdapterPosition = topics_link_recycleview.getChildAdapterPosition(view);
                Intent intent_topics = new Intent(ItemMostpopularActivity.this, TopicsDetailMessageActivity.class);
                CacheDataUtils.setWhichTopics(topicsArray[childAdapterPosition]);
                CacheDataUtils.addCurrentNeedMsg();
                ActivityColection.addActivity(ItemMostpopularActivity.this);
                intent_topics.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent_topics.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent_topics.putExtra("click_location", topicsArray[childAdapterPosition]);
//                intent_topics.putExtras(putBundleExtra());
                startActivity(intent_topics);
                finish();
            }
        });
        //初始化viewpager
        setTabSelection(1);
        btn_press(1);
    }


    private void initComments() {
        if (NetUtils.checkNet(this) == true) {
            MyHttpUtils.getNetMessage(this, "http://1zou.me/apisq/loadNewCmts/" + CacheDataUtils.getCurrentNeedToSaveData().getIcnfid() + "/" + Constants.USER_ID + "/3", new MyHttpUtils.Callback() {
                @Override
                public void getResponseMsg(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        LogUtils.showVerbose("ItemMostpopularActivity", "response" + response);
                        int errorCode = jsonObject.getInt("errorCode");
                        if (errorCode == 0) {
                            jsonParse(response);
                        } else {
                            LogUtils.showVerbose("ItemMostpopularActivity", "获取网络评论信息错误");
                        }
                    } catch (JSONException e) {
                        LogUtils.showVerbose("ItemMostpopularActivity", "对评论信息解析错误");
                    }
                }
            }, new MyHttpUtils.CallbackError() {
                @Override
                public void getResponseMsg(String error) {

                }
            });
        } else {
            NetUtils.showNoNetDialog(this);
        }

    }

    private void initAnimation() {
        mAnimation_one = new AnimationView(this);
        mAnimation_one.setLocation(Constants.PHONE_WIDTH / 3 + 20, Constants.PHONE_HEIGHT / 4 - 20);
        mAnimation_one.setRadidus(6);
        mAnimationBgView = new AnimationBgView(this);
        mAnimationBgView.setLocation(Constants.PHONE_WIDTH / 3 + 20, Constants.PHONE_HEIGHT / 4 - 20);
        mAnimationBgView.setBgRadidus(11);
        mAnimationBgView.setLineText(mList);
        mAnimationBgView.setOnReturnMessage(new AnimationBgView.OnReturnMessage() {
            @Override
            public void onClickMessage(String returnMsg) {
                ToastUtil.showShort(ItemMostpopularActivity.this, "点击的信息：" + returnMsg);
            }
        });
        mAnimation_layout.addView(mAnimationBgView);
        mAnimation_layout.addView(mAnimation_one);

        mAnimation_layout.setX(50);
        mAnimation_layout.setY(50);
        mAnimation_layout.setVisibility(View.GONE);//no animation
    }

    private void startAnimation() {
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(mAnimation_one, "radidus", 5f, 30f, 5f);
        ObjectAnimator fadeColor = ObjectAnimator.ofFloat(mAnimation_one, "alpha", 0.5f, 0.01f, 0.5f);
        fadeInOut.setRepeatCount(5000);
        fadeColor.setRepeatCount(5000);
        mAnimSet = new AnimatorSet();
        (mAnimSet.play(fadeInOut)).with(fadeColor);
        mAnimSet.setDuration(1000);
        mAnimSet.start();
        mAnimSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 设置跳转到其他界面的事件
     */
    public void setJumpToOtherActivity() {
//        if ("otherAuthorToItemMost".equals(getIntent().getStringExtra("otherAuthorToItemMost"))){
//            Intent intent = new Intent(ItemMostpopularActivity.this, OtherAuthorActivity.class);
//            intent.putExtra("mostpopular","mostpopular");
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
//        }else if ("ownAuthorToItemMost".equals(getIntent().getStringExtra("ownAuthorToItemMost"))){
//            Intent intent = new Intent(ItemMostpopularActivity.this, MainActivity.class);
//            intent.putExtra("person_design","person_design");
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
//        }else {
//            Intent intent = new Intent(ItemMostpopularActivity.this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
//        }
        if (ActivityColection.isContains(this)) {
            ActivityColection.removeActivity(this);
            Intent intent = new Intent(ItemMostpopularActivity.this, ActivityColection.getActivity(Constants.saveCacheDataList.size()).getClass());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            if (Constants.saveCacheDataList.size() > 1) {
                CacheDataUtils.removeCurrentCacheDate();
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_mostpopular_back_layout:
//                setJumpToOtherActivity();
                DealReturnLogicUtils.dealReturnLogic(this);
                break;
            case R.id.item_mostpopular_author_icon:
                CacheDataHelper.addHasArgumentsMethod();
                ActivityColection.addActivity(this);
                Intent intent_to_other_author = new Intent(ItemMostpopularActivity.this, OtherAuthorActivity.class);
                intent_to_other_author.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent_to_other_author.putExtra("itemmostpopular", "itemmostpopular");
                startActivity(intent_to_other_author);
                finish();
                break;
            case R.id.item_mostpopular_msg_comment_icon:
                //设置自己的头像的点击事件
                break;
            case R.id.item_mostpopular_heart_icon:
                if (NetUtils.checkNet(this) == true) {
                    if (Constants.STATUS.equals("reg_log")) {
                        if (Constants.islike == true) {
                            //说明当前的状态时被选中的状态
                            boolean takeCare = MyHttpUtils.getTakeCare(ItemMostpopularActivity.this, 1, CacheDataUtils.getCurrentNeedToSaveData().getIcnfid());
                            if (takeCare) {
                                Constants.likenum = Constants.likenum - 1;
                                mItem_mostpopular_heart_content.setText(Constants.likenum + "");
                                mItem_mostpopular_msg_click.setText(Constants.likenum + "次赞");
                                Toast.makeText(ItemMostpopularActivity.this, "取消点赞", Toast.LENGTH_SHORT).show();
                                Constants.islike = false;
                                CacheDataUtils.getCurrentNeedToSaveData().setLikenum(Constants.likenum);
                                mItem_mostpopular_heart_icon.setSelected(false);
                            }

                        } else {
                            boolean takeCare = MyHttpUtils.getTakeCare(ItemMostpopularActivity.this, 0, CacheDataUtils.getCurrentNeedToSaveData().getIcnfid());
                            if (takeCare) {
                                Constants.likenum = Constants.likenum + 1;
                                mItem_mostpopular_heart_content.setText(Constants.likenum + "");
                                mItem_mostpopular_msg_click.setText(Constants.likenum + "次赞");
                                Toast.makeText(ItemMostpopularActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
                                Constants.islike = true;
                                CacheDataUtils.getCurrentNeedToSaveData().setLikenum(Constants.likenum);
                                mItem_mostpopular_heart_icon.setSelected(true);
                            }

                        }

                    } else {
                        Constants.saveCacheDataList.clear();
                        Intent register_intent = new Intent(ItemMostpopularActivity.this, LoginActivity.class);
                        register_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(register_intent);
                        finish();
                    }

                } else {
                    NetUtils.showNoNetDialog(this);
                }

                break;
            case R.id.item_mostpopular_dialog_icon:
                if (Constants.STATUS.equals("reg_log")) {
                    SystemUtils.showOrHide(this);
                } else {
                    Constants.saveCacheDataList.clear();
                    Intent register_intent = new Intent(ItemMostpopularActivity.this, LoginActivity.class);
                    register_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(register_intent);
                    finish();
                }
                break;
            case R.id.item_mostpopular_store_layout:
//
                if (NetUtils.checkNet(this) == true) {
                    if (Constants.STATUS.equals("reg_log")) {
                        if (Constants.isstore == true) {
                            MyHttpUtils.getStore(this, 1, CacheDataUtils.getCurrentNeedToSaveData().getIcnfid(), new MyHttpUtils.Callback() {
                                @Override
                                public void getResponseMsg(String response) {
                                    if ("0".equals(response)) {
                                        Constants.storenum = Constants.storenum - 1;
                                        mItem_mostpopular_msg_store.setText(Constants.storenum + "次收藏");
                                        Toast.makeText(ItemMostpopularActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                                        Constants.isstore = false;
                                        CacheDataUtils.getCurrentNeedToSaveData().setStorenum(Constants.storenum);
                                        mItem_mostpopular_store_icon.setSelected(false);
                                    }
                                }
                            }, new MyHttpUtils.CallbackError() {
                                @Override
                                public void getResponseMsg(String error) {

                                }
                            });
                        } else {
                            MyHttpUtils.getStore(this, 0, CacheDataUtils.getCurrentNeedToSaveData().getIcnfid(), new MyHttpUtils.Callback() {
                                @Override
                                public void getResponseMsg(String response) {
                                    Constants.storenum = Constants.storenum + 1;
                                    mItem_mostpopular_msg_store.setText(Constants.storenum + "次收藏");
                                    Toast.makeText(ItemMostpopularActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                                    Constants.isstore = true;
                                    CacheDataUtils.getCurrentNeedToSaveData().setStorenum(Constants.storenum);
                                    mItem_mostpopular_store_icon.setSelected(true);
                                }
                            }, new MyHttpUtils.CallbackError() {
                                @Override
                                public void getResponseMsg(String error) {

                                }
                            });
                        }
                    } else {
                        Constants.saveCacheDataList.clear();
                        Intent register_intent = new Intent(ItemMostpopularActivity.this, LoginActivity.class);
                        register_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(register_intent);
                        finish();
                    }

                } else {
                    NetUtils.showNoNetDialog(this);
                }

                break;
            case R.id.item_mostpopular_look_all_comments:
                CacheDataHelper.addHasArgumentsMethod();
                ActivityColection.addActivity(this);
                Intent intent_comment = new Intent(ItemMostpopularActivity.this, ItemMostpopularCommentsActivity.class);
                intent_comment.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent_comment.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_comment);
                finish();
                break;
            case R.id.item_mostpopular_edit_comment:
                if (Constants.STATUS.equals("reg_log")) {
                    SystemUtils.showOrHide(this);
                } else {
                    Constants.saveCacheDataList.clear();
                    Intent register_intent = new Intent(ItemMostpopularActivity.this, LoginActivity.class);
                    register_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(register_intent);
                    finish();
                }
                break;
            case R.id.item_mostpopular_msg_publish_ok:
                if (NetUtils.checkNet(this) == true) {
                    if (TextUtils.isEmpty(mItem_mostpopular_msg_publish_input.getText())) {
                        ToastUtil.showShort(ItemMostpopularActivity.this, "发布信息不能为空");
                    } else {
                        //将评论信息传入到数据库中
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MyHttpUtils.postJsonData("http://1zou.me/apisq/addCmt", "cmtInfo", getPublishCommentMsg(mItem_mostpopular_msg_publish_input.getText().toString()),
                                        new MyHttpUtils.Callback() {
                                            @Override
                                            public void getResponseMsg(String response) {
                                                if ("0".equals(response)) {
                                                    mHandler.sendEmptyMessage(COMMENT_SUCCESS);
                                                }
                                            }
                                        }, new MyHttpUtils.CallbackError() {
                                            @Override
                                            public void getResponseMsg(String error) {

                                            }
                                        });
                            }
                        }) {
                        }.start();

                        //发布消息
                        //关闭软键盘
                        SystemUtils.showOrHide(this);
                    }
                } else {
                    NetUtils.showNoNetDialog(this);
                }

                break;
            case R.id.item_mostpopular_care:
                if (NetUtils.checkNet(this) == true) {
                    if (Constants.STATUS.equals("reg_log")) {
                        if (mItem_mostpopular_care.isSelected()) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    MyHttpUtils.postJsonData("http://1zou.me/apisq/removeFollow", "userInfo", getCancelCareJsonMsg(),
                                            new MyHttpUtils.Callback() {
                                                @Override
                                                public void getResponseMsg(String response) {
                                                    if ("0".equals(response)) {
                                                        mHandler.sendEmptyMessage(CARE_FAILE);
                                                    }
                                                }
                                            }, new MyHttpUtils.CallbackError() {
                                                @Override
                                                public void getResponseMsg(String error) {

                                                }
                                            });
                                }
                            }).start();
                        } else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    MyHttpUtils.postJsonData("http://1zou.me/apisq/addFollow", "userInfo", getAddCareJsonMsg(),
                                            new MyHttpUtils.Callback() {
                                                @Override
                                                public void getResponseMsg(String response) {
                                                    LogUtils.showVerbose("ItemMostpopularActivity", "response=" + response);
                                                    if ("0".equals(response)) {
                                                        mHandler.sendEmptyMessage(CARE_SUCCESS);
                                                    }
                                                }
                                            }, new MyHttpUtils.CallbackError() {
                                                @Override
                                                public void getResponseMsg(String error) {
                                                    LogUtils.showVerbose("ItemMostpopularActivity", "error=" + error);
                                                }
                                            });
                                }
                            }).start();
                        }
                    } else {
                        Constants.saveCacheDataList.clear();
                        Intent register_intent = new Intent(ItemMostpopularActivity.this, LoginActivity.class);
                        register_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(register_intent);
                        finish();
                    }
                } else {
                    NetUtils.showNoNetDialog(this);
                }

                break;
            case R.id.item_mostpopular_share_layout:
                break;
            case R.id.item_mostpopular_body_msg_icon:
                CacheDataHelper.addHasArgumentsMethod();
                ActivityColection.addActivity(this);
                Intent body_msg_intent = new Intent(ItemMostpopularActivity.this, BodyDetailMessageActivity.class);
                body_msg_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(body_msg_intent);
                finish();
                break;
            default:
                break;
        }
    }

    private String getPublishCommentMsg(String inputMsg) {
        JSONObject jsonObject = new JSONObject();
//        {"comment":'new comment','userid':userid1,'iconfid':iconfid2}
        try {
            jsonObject.put("comment", inputMsg);
            jsonObject.put("userid", Constants.USER_ID);
            jsonObject.put("iconfid", CacheDataUtils.getCurrentNeedToSaveData().getIcnfid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    //获取取消关注的参数
    public String getCancelCareJsonMsg() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", Constants.USER_ID);
            jsonObject.put("followid", CacheDataUtils.getCurrentNeedToSaveData().getUserid());//添加被关注者的id
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    //获取添加关注的参数
    public String getAddCareJsonMsg() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", Constants.USER_ID);
            jsonObject.put("followid", CacheDataUtils.getCurrentNeedToSaveData().getUserid());//添加被关注者的id
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == CARE_FAILE) {
                //取消关注
                mItem_mostpopular_care.setSelected(false);//取消关
                mItem_mostpopular_care.setText("未关注");
                CacheDataUtils.getCurrentNeedToSaveData().setFollowstate("null");
                Toast.makeText(ItemMostpopularActivity.this, "取消关注", Toast.LENGTH_SHORT).show();
            } else if (msg.what == CARE_SUCCESS) {
                mItem_mostpopular_care.setSelected(true);//关注成功
                mItem_mostpopular_care.setText("已关注");
                CacheDataUtils.getCurrentNeedToSaveData().setFollowstate(Constants.USER_ID + "");
                Toast.makeText(ItemMostpopularActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
            } else if (msg.what == COMMENT_SUCCESS) {
                Toast.makeText(ItemMostpopularActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                //添加刷新评论的代码
                initComments();
                Constants.allComments = Constants.allComments + 1;
                mItem_mostpopular_look_all_comments.setText("查看全部" + Constants.allComments + "条评论");
                mItem_mostpopular_dialog_icon.setSelected(true);
                mItem_mostpopular_dialog_content.setText(Constants.allComments + "");
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (ActivityColection.isContains(this)) {
////                ActivityColection.removeActivity(this);
//                setJumpToOtherActivity();
//            }
            DealReturnLogicUtils.dealReturnLogic(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     */
    private void setTabSelection(int index) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        removeFragments(transaction);
        switch (index) {
            case 1:
                mMatchFragment = new ItemMostpopularSameStyleMatchFragment();
                transaction.replace(R.id.style_frameLayout, mMatchFragment);
                break;
//            case 2:
//                mSingleFragment = new ItemMostpopularSameStyleSingleFragment();
//                mSingleFragment.setArguments(bundle);
//                transaction.replace(R.id.style_frameLayout, mSingleFragment);
//                break;
            default:
                mMatchFragment = new ItemMostpopularSameStyleMatchFragment();
                transaction.replace(R.id.style_frameLayout, mMatchFragment);
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
        if (mMatchFragment != null) {
            transaction.remove(mMatchFragment);
        }
        if (mSingleFragment != null) {
            transaction.remove(mSingleFragment);
        }
    }

    private void btn_press(int pos) {
        switch (pos) {
            case 1:
                mSame_style_tv.setTextColor(getResources().getColor(R.color.head_bg_color));
                mSingle_style_tv.setTextColor(getResources().getColor(R.color.self_tabs_normal));
                mSame_style_line.setBackgroundColor(getResources().getColor(R.color.head_bg_color));
                mSingle_style_line.setBackgroundColor(getResources().getColor(R.color.line_color));
                break;
            case 2:
                mSingle_style_tv.setTextColor(getResources().getColor(R.color.head_bg_color));
                mSame_style_tv.setTextColor(getResources().getColor(R.color.self_tabs_normal));
                mSingle_style_line.setBackgroundColor(getResources().getColor(R.color.head_bg_color));
                mSame_style_line.setBackgroundColor(getResources().getColor(R.color.line_color));
                break;
            default:
                break;
        }
    }

    private void jsonParse(String string) {
        try {
            JSONObject object = new JSONObject(string);
            JSONArray array = object.getJSONArray("datas");
            Constants.mCommentsInfoList.clear();
            mComList.clear();
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                CommentsInfo commentsInfo = new CommentsInfo(jsonObject.getInt("cmtid"),
                        jsonObject.getString("comment"),
                        jsonObject.getString("createdat"),
                        jsonObject.getInt("rpnum"),
                        jsonObject.getInt("userid"),
                        jsonObject.getString("username"),
                        jsonObject.getString("avatar"),
                        jsonObject.getString("cmtfavour"),
                        jsonObject.getInt("likednum"));
                LogUtils.showVerbose("MostPopularFragment", "cmtid=" + jsonObject.getInt("cmtid") + " comment=" + jsonObject.getString("comment") + " createdat=" +
                        jsonObject.getString("createdat") + " username=" + jsonObject.getString("username") + " avatar=" + jsonObject.getString("avatar"));
                Constants.mCommentsInfoList.add(commentsInfo);
                mComList.add(new CommentsInfo(jsonObject.getInt("cmtid"), jsonObject.getInt("likednum"), jsonObject.getString("cmtfavour")));
            }

            //评论
            mItem_mostpopular_recycleview = (RecyclerView) this.findViewById(R.id.item_mostpopular_recycleview);
            ItemMostPopularRecycleViewAdapter recycleViewAdapter = new ItemMostPopularRecycleViewAdapter(this);
            LogUtils.showVerbose("MostPopularFragment", "输入的数据是=" + Constants.mCommentsInfoList.size());
            recycleViewAdapter.setShowNum(Constants.mCommentsInfoList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mItem_mostpopular_recycleview.setLayoutManager(linearLayoutManager);
            mItem_mostpopular_recycleview.setAdapter(recycleViewAdapter);
            recycleViewAdapter.setOnAgreeClickListener(new ItemMostPopularRecycleViewAdapter.OnAgreeClickListener() {
                @Override
                public void agreeclick(final View itemview, final TextView like_num, final CustomSimpleDraweeView draweeView) {
                    //处理评论中点赞的逻辑
                    if (NetUtils.checkNet(ItemMostpopularActivity.this) == true) {
                        final int position = mItem_mostpopular_recycleview.getChildAdapterPosition(itemview);
                        if ("null".equals(mComList.get(position).getCmtfavour()) || mComList.get(position).getCmtfavour() == null) {
                            Constants.isLikeComment = false;
                        } else {
                            Constants.isLikeComment = true;
                        }
                        Constants.currentCommnetNum = mComList.get(position).getLikednum();
                        if (Constants.isLikeComment == true) {
                            //当前处于点赞的状态
                            MyHttpUtils.getHandCommentClick(ItemMostpopularActivity.this, 1, mComList.get(position).getCmtid(), new MyHttpUtils.Callback() {
                                @Override
                                public void getResponseMsg(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        int errorCode = jsonObject.getInt("errorCode");
                                        if (errorCode == 0) {
                                            Constants.isLikeComment = false;
                                            Constants.currentCommnetNum = Constants.currentCommnetNum - 1;
                                            like_num.setText(Constants.currentCommnetNum + "");
//                                            draweeView.setImageResource(R.mipmap.agree_click);
                                            draweeView.setImageBitmap(ImageDealUtils.readBitMap(ItemMostpopularActivity.this,R.mipmap.agree_click));
                                            mComList.set(position, new CommentsInfo(mComList.get(position).getCmtid(), Constants.currentCommnetNum, "null"));
                                            Toast.makeText(ItemMostpopularActivity.this, "取消点赞成功", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        LogUtils.showVerbose("ItemMostpopularActivity", "当前信息解析错误");
                                    }
                                }
                            }, new MyHttpUtils.CallbackError() {
                                @Override
                                public void getResponseMsg(String error) {

                                }
                            });
                        } else {
                            //当前没有点赞
                            MyHttpUtils.getHandCommentClick(ItemMostpopularActivity.this, 0, mComList.get(position).getCmtid(), new MyHttpUtils.Callback() {
                                @Override
                                public void getResponseMsg(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        int errorCode = jsonObject.getInt("errorCode");
                                        if (errorCode == 0) {
                                            Constants.isLikeComment = true;
                                            Constants.currentCommnetNum = Constants.currentCommnetNum + 1;
                                            like_num.setText(Constants.currentCommnetNum + "");
//                                            draweeView.setImageResource(R.mipmap.agree_click_press);
                                            draweeView.setImageBitmap(ImageDealUtils.readBitMap(ItemMostpopularActivity.this,R.mipmap.agree_click_press));
                                            mComList.set(position, new CommentsInfo(mComList.get(position).getCmtid(), Constants.currentCommnetNum, mComList.get(position).getCmtid() + ""));
                                            Toast.makeText(ItemMostpopularActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        LogUtils.showVerbose("ItemMostpopularActivity", "当前信息解析错误");
                                    }
                                }
                            }, new MyHttpUtils.CallbackError() {
                                @Override
                                public void getResponseMsg(String error) {

                                }
                            });
                        }
                    } else {
                        NetUtils.showNoNetDialog(ItemMostpopularActivity.this);
                    }

                }
            });
            recycleViewAdapter.setOnAuthorMessageClickListener(new ItemMostPopularRecycleViewAdapter.OnAuthorMessageClickListener() {
                @Override
                public void authorClick() {
                    Intent intent_to_other_author = new Intent(ItemMostpopularActivity.this, OtherAuthorActivity.class);
                    intent_to_other_author.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent_to_other_author.putExtra("itemmostpopular", "itemmostpopular");
//                    intent_to_other_author.putExtras(putBundleExtra());
                    startActivity(intent_to_other_author);
                    finish();
                }
            });
            recycleViewAdapter.setOnResponseMessageClickListener(new ItemMostPopularRecycleViewAdapter.OnResponseMessageClickListener() {
                @Override
                public void responseClick(final CommentsInfo commentInfo) {
                    View view = LayoutInflater.from(ItemMostpopularActivity.this).inflate(R.layout.pop_window_response, null);
                    final PopupWindow popupWindow = new PopupWindow(view,
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setFocusable(true);
                    popupWindow.setTouchable(true);
                    popupWindow.showAsDropDown(mItem_mostpopular_head_layout);
                    View my_pop = popupWindow.getContentView();
                    LinearLayout pop_window_response_answer_layout = (LinearLayout) my_pop.findViewById(R.id.pop_window_response_answer_layout);
                    LinearLayout pop_window_response_delete_layout = (LinearLayout) my_pop.findViewById(R.id.pop_window_response_delete_layout);
                    LinearLayout pop_window_response_cancel_layout = (LinearLayout) my_pop.findViewById(R.id.pop_window_response_cancel_layout);
                    pop_window_response_answer_layout.setVisibility(View.VISIBLE);
                    pop_window_response_delete_layout.setVisibility(View.VISIBLE);
                    pop_window_response_cancel_layout.setVisibility(View.VISIBLE);
                    if (Constants.USER_ID == commentInfo.getUserid()) {
                        //当前选中的是自己发布的评论
                        LogUtils.showVerbose("ItemMostpopularActivity", "woshi");
                        pop_window_response_delete_layout.setVisibility(View.VISIBLE);
                    } else {
                        pop_window_response_delete_layout.setVisibility(View.GONE);
                    }
                    //回复按钮
                    pop_window_response_answer_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();//这一步一定要加上，因为这样就不会造成因为windowpop造成内存泄露
                            CacheDataUtils.addCurrentNeedMsg();
                            ActivityColection.addActivity(ItemMostpopularActivity.this);
                            Intent intent = new Intent(ItemMostpopularActivity.this, CommentResponseDetailActivity.class);
                            intent.putExtra("directReply", "directReply");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Bundle bundle = new Bundle();
                            bundle.putString("avatar", commentInfo.getAvatar());
                            bundle.putString("cmtusername", commentInfo.getUsername());
                            bundle.putString("comment", commentInfo.getComment());
                            bundle.putString("cmtfavour", commentInfo.getCmtfavour());
                            bundle.putString("createdat", commentInfo.getCreatedat());
                            bundle.putInt("cmtid", commentInfo.getCmtid());
                            bundle.putInt("cmtlikenum", commentInfo.getLikednum());
                            bundle.putInt("rpnum", commentInfo.getRpnum());
                            bundle.putInt("cmtuserid", commentInfo.getUserid());
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                    });
                    pop_window_response_cancel_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });
                    pop_window_response_delete_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (NetUtils.checkNet(ItemMostpopularActivity.this) == true) {
                                MyHttpUtils.getNetMessage(ItemMostpopularActivity.this, "http://1zou.me/apisq/deltCmt/" + commentInfo.getCmtid(), new MyHttpUtils.Callback() {
                                    @Override
                                    public void getResponseMsg(String response) {
                                        LogUtils.showVerbose("ItemMostpopularActivity", "response=" + response);
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            int errorCode = jsonObject.getInt("errorCode");
                                            if (errorCode == 0) {
                                                initComments();
                                                popupWindow.dismiss();
                                                if (Constants.allComments > 0) {
                                                    Constants.allComments = Constants.allComments - 1;
                                                    mItem_mostpopular_look_all_comments.setText("查看全部" + Constants.allComments + "条评论");
                                                    mItem_mostpopular_dialog_content.setText(Constants.allComments + "");
                                                } else {
                                                    mItem_mostpopular_look_all_comments.setText("当前暂无任何评论");
                                                    mItem_mostpopular_dialog_content.setText(0 + "");
                                                }
                                            }
                                        } catch (JSONException e) {
                                            LogUtils.showVerbose("ItemMostpopularActivity", "删除数据json解析错误");
                                        }
                                    }
                                }, new MyHttpUtils.CallbackError() {
                                    @Override
                                    public void getResponseMsg(String error) {

                                    }
                                });
                            } else {
                                NetUtils.showNoNetDialog(ItemMostpopularActivity.this);
                            }

                        }
                    });

                }
            });

        } catch (JSONException e) {
            LogUtils.showVerbose("MostPopularFragment", "第一级数组解析错误");
        }
    }
}
