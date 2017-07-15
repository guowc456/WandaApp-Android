package com.wanta.mobile.wantaproject.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.RecommendedAttentionAdapter;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.CacheDataUtils;
import com.wanta.mobile.wantaproject.fragment.FindFragment;
import com.wanta.mobile.wantaproject.fragment.MostPopularFragment;
import com.wanta.mobile.wantaproject.fragment.NewWardrobeFragment;
import com.wanta.mobile.wantaproject.fragment.RecommendedAttentionConcernFragment;
import com.wanta.mobile.wantaproject.fragment.RecommendedAttentionNotConcernFragment;
import com.wanta.mobile.wantaproject.fragment.SelfFragment;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.DealReturnLogicUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;

/**
 * Created by Administrator on 2017/4/19.
 */

public class RecommendedAttentionActivity extends BaseActivity implements View.OnClickListener {

    private MyImageView recommende_attention_back_icon;
    private LinearLayout recommende_attention_back_layout;
    private FrameLayout recommende_attention_fragmentlayout;
    private RecommendedAttentionNotConcernFragment notConcernFragment;
    private RecommendedAttentionConcernFragment concernFragment;
    private LinearLayout recommende_attention_not_concern_layout;
    private LinearLayout recommende_attention_concern_layout;
    private TextView recommende_attention_not_concern_tv;
    private TextView recommende_attention_concern_tv;
    private TextView recommende_attention_not_concern_tv_ba_line_color;
    private TextView recommende_attention_concern_tv_bg_line_color;
    //    private int mIcnfid;
//    private String mTitle;
//    private String mContent;
//    private int mLikenum;
//    private int mStorenum;
//    private int mUserid;
//    private String mUsername;
//    private String mUseravatar;
//    private String mFavourstate;
//    private String mStoredstate;
//    private String mLng;
//    private String mLat;
//    private String mAddress;
//    private String uaddress;
//    private String mCreatedat;
//    private String mImages;
//    private String followstate;
//    private String height;
//    private String weight;
//    private String bust;
//    private String bra;
//    private int cmtnum;
//    private String topics;
//    private String browsenum;
//    private String title_cn;
//    private String content_cn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommende_attention_layout);
//        ActivityColection.addActivity(this);
//        Bundle bundle = getIntent().getExtras();
//        mIcnfid = bundle.getInt("icnfid");
//        mTitle = bundle.getString("title");
//        mContent = bundle.getString("content");
//        mLikenum = bundle.getInt("likenum");
//        mStorenum = bundle.getInt("storenum");
//        mUserid = bundle.getInt("userid");
//        mUsername = bundle.getString("username");
//        mUseravatar = bundle.getString("useravatar");
//        mFavourstate = bundle.getString("favourstate");
//        mStoredstate = bundle.getString("storedstate");
//        mLng = bundle.getString("lng");
//        mLat = bundle.getString("lat");
//        mAddress = bundle.getString("address");
//        uaddress = bundle.getString("uaddress");
//        mCreatedat = bundle.getString("createdat");
//        mImages = bundle.getString("images");
//        followstate = bundle.getString("followstate");
//        height = bundle.getString("height");
//        weight = bundle.getString("weight");
//        bust = bundle.getString("bust");
//        bra = bundle.getString("bra");
//        cmtnum = bundle.getInt("cmtnum");
//        topics = bundle.getString("topics");
//        browsenum = bundle.getString("browsenum");
//        title_cn = bundle.getString("title_cn");
//        content_cn = bundle.getString("content_cn");
        initId();
        initData();
    }

    private void initId() {
        recommende_attention_back_icon = (MyImageView) this.findViewById(R.id.recommende_attention_back_icon);
        recommende_attention_back_icon.setSize(Constants.PHONE_WIDTH/10,Constants.PHONE_WIDTH/10);
        recommende_attention_back_layout = (LinearLayout) this.findViewById(R.id.recommende_attention_back_layout);
        recommende_attention_back_layout.setOnClickListener(this);
        recommende_attention_fragmentlayout = (FrameLayout) this.findViewById(R.id.recommende_attention_fragmentlayout);
        recommende_attention_not_concern_layout = (LinearLayout) this.findViewById(R.id.recommende_attention_not_concern_layout);
        recommende_attention_not_concern_layout.setOnClickListener(this);
        recommende_attention_concern_layout = (LinearLayout) this.findViewById(R.id.recommende_attention_concern_layout);
        recommende_attention_concern_layout.setOnClickListener(this);
        recommende_attention_not_concern_tv = (TextView) this.findViewById(R.id.recommende_attention_not_concern_tv);
        recommende_attention_concern_tv = (TextView) this.findViewById(R.id.recommende_attention_concern_tv);
        recommende_attention_not_concern_tv_ba_line_color = (TextView) this.findViewById(R.id.recommende_attention_not_concern_tv_bg_line_color);
        recommende_attention_concern_tv_bg_line_color = (TextView) this.findViewById(R.id.recommende_attention_concern_tv_bg_line_color);
    }

    private void initData() {
        setTabSelection(1);
    }


    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.recommende_attention_back_layout:
//               jumpToOtherAuthorActivity();
                DealReturnLogicUtils.dealReturnLogic(this);
                break;
            case R.id.recommende_attention_concern_layout:
                //已经关注了
                setTabSelection(2);
                recommende_attention_concern_tv.setTextColor(getResources().getColor(R.color.head_bg_color));
//                recommende_attention_concern_line.setBackground(getResources().getDrawable(R.color.head_bg_color));
                recommende_attention_not_concern_tv.setTextColor(Color.BLACK);
//                recommende_attention_not_concern_line.setBackground(getResources().getDrawable(R.color.white));
                break;
            case R.id.recommende_attention_not_concern_layout:
                //推荐关注
                setTabSelection(1);
                recommende_attention_concern_tv.setTextColor(getResources().getColor(R.color.black));
//                recommende_attention_concern_line.setBackground(getDrawable(R.color.white));
                recommende_attention_not_concern_tv.setTextColor(getResources().getColor(R.color.head_bg_color));
//                recommende_attention_not_concern_line.setBackground(getDrawable(R.color.head_bg_color));
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
//            if (ActivityColection.isContains(this)){
//                ActivityColection.removeActivity(this);
//                jumpToOtherAuthorActivity();
//            }
            DealReturnLogicUtils.dealReturnLogic(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void jumpToOtherAuthorActivity(){
        Intent intent = new Intent(RecommendedAttentionActivity.this,OtherAuthorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtras(putBundleExtra());
        startActivity(intent);
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     */
    private void setTabSelection(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt("icnfid", CacheDataUtils.getCurrentNeedToSaveData().getIcnfid());
        // 开启一个Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        removeFragments(transaction);
        switch (index) {
            case 1:
                notConcernFragment = new RecommendedAttentionNotConcernFragment();
                recommende_attention_not_concern_tv_ba_line_color.setBackground(getResources().getDrawable(R.color.head_bg_color));
                recommende_attention_concern_tv_bg_line_color.setBackground(getResources().getDrawable(R.color.white));
                notConcernFragment.setArguments(bundle);
                transaction.replace(R.id.recommende_attention_fragmentlayout, notConcernFragment);
                break;
            case 2:
                concernFragment = new RecommendedAttentionConcernFragment();
                recommende_attention_not_concern_tv_ba_line_color.setBackground(getResources().getDrawable(R.color.white));
                recommende_attention_concern_tv_bg_line_color.setBackground(getResources().getDrawable(R.color.head_bg_color));
                concernFragment.setArguments(bundle);
                transaction.replace(R.id.recommende_attention_fragmentlayout, concernFragment);
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
        if (notConcernFragment != null) {
            transaction.remove(notConcernFragment);
        }
        if (concernFragment != null) {
            transaction.remove(concernFragment);
        }
    }
//    //bundle添加的参数
//    public Bundle putBundleExtra() {
//        Bundle bundle = new Bundle();
//        bundle.putInt("icnfid", mIcnfid);
//        bundle.putString("title", mTitle);
//        bundle.putString("content", mContent);
//        bundle.putInt("likenum", mLikenum);
//        bundle.putInt("storenum", mStorenum);
//        bundle.putInt("userid", mUserid);
//        bundle.putString("username", mUsername);
//        bundle.putString("useravatar", mUseravatar);
//        bundle.putString("favourstate", mFavourstate);
//        bundle.putString("storedstate", mStoredstate);
//        bundle.putString("lng", mLng);
//        bundle.putString("lat", mLat);
//        bundle.putString("address", mAddress);
//        bundle.putString("uaddress", uaddress);
//        bundle.putString("createdat", mCreatedat);
//        bundle.putString("followstate", followstate);
//        bundle.putString("height", height);
//        bundle.putString("weight", weight);
//        bundle.putString("bust", bust);
//        bundle.putString("bra", bra);
//        bundle.putString("images", mImages);
//        bundle.putInt("cmtnum", cmtnum);
//        bundle.putString("topics", topics);
//        bundle.putString("browsenum", browsenum);
//        bundle.putString("title_cn", title_cn);
//        bundle.putString("content_cn", content_cn);
//        return bundle;
//    }
}
