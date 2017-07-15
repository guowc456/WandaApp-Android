package com.wanta.mobile.wantaproject.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.CacheDataHelper;
import com.wanta.mobile.wantaproject.fragment.CommunityFragment;
import com.wanta.mobile.wantaproject.fragment.FindFragment;
import com.wanta.mobile.wantaproject.fragment.MostPopularFragment;
import com.wanta.mobile.wantaproject.fragment.NewWardrobeFragment;
import com.wanta.mobile.wantaproject.fragment.SelfFragment;
import com.wanta.mobile.wantaproject.fragment.WardrobeFragment;
import com.wanta.mobile.wantaproject.uploadimage.ImagesSelectorActivity;
import com.wanta.mobile.wantaproject.uploadimage.SelectorSettings;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.ImageDealUtils;
import com.wanta.mobile.wantaproject.utils.InterpretView;
import com.wanta.mobile.wantaproject.utils.Interpretor;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;
import com.wanta.mobile.wantaproject.utils.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by WangYongqiang on 2016/10/12.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private FindFragment mFindFragment;
    private SelfFragment mSelfFragment;
    private ArrayList<String> mResults = new ArrayList<>();
    private int exitnum = 0;
    private MostPopularFragment mMostPopularFragment;
    private LinearLayout mHead_title;
    private NewWardrobeFragment newWardrobeFragment;
    private LinearLayout community_layout;
    private LinearLayout wardrobe_layout;
    private LinearLayout find_layout;
    private LinearLayout self_layout;
    private MyImageView img_community;
    private MyImageView img_wardrobe;
    private MyImageView img_find;
    private MyImageView img_self;
    private TextView tv_community;
    private TextView tv_wardrobe;
    private TextView tv_find;
    private TextView tv_self;
    private LinearLayout camera_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityColection.clearAllActivity();
        Constants.saveCacheDataList.clear();
        ActivityColection.addActivity(this);
        //判断是否已经登录过了
        SharedPreferences sharedPreferences = getSharedPreferences("saveloginmsg", Context.MODE_PRIVATE);
        String state = sharedPreferences.getString("state", "");
        if ("reg_log".equals(state)) {
            //说明已经登陆成功了
            Constants.USER_ID = sharedPreferences.getInt("userId", 0);
            Constants.USER_NAME = sharedPreferences.getString("name", "");
            Constants.AVATAR = sharedPreferences.getString("atatar", "");
            Constants.STATUS = state;

        } else {
            Constants.USER_ID = 0;
            Constants.USER_NAME = "m_wdanonymous";
            Constants.STATUS = "reg";
        }
        init();
    }

    private void init() {
        community_layout = (LinearLayout) this.findViewById(R.id.community_layout);
        wardrobe_layout = (LinearLayout) this.findViewById(R.id.wardrobe_layout);
        find_layout = (LinearLayout) this.findViewById(R.id.find_layout);
        self_layout = (LinearLayout) this.findViewById(R.id.self_layout);
        img_community = (MyImageView) this.findViewById(R.id.img_community);
        img_wardrobe = (MyImageView) this.findViewById(R.id.img_wardrobe);
        img_find = (MyImageView) this.findViewById(R.id.img_find);
        img_self = (MyImageView) this.findViewById(R.id.img_self);
        tv_community = (TextView) this.findViewById(R.id.tv_community);
        tv_wardrobe = (TextView) this.findViewById(R.id.tv_wardrobe);
        tv_find = (TextView) this.findViewById(R.id.tv_find);
        tv_self = (TextView) this.findViewById(R.id.tv_self);
        camera_layout = (LinearLayout) this.findViewById(R.id.camera_layout);
        img_community.setSize(Constants.PHONE_WIDTH / 15, Constants.PHONE_WIDTH / 15);
        img_wardrobe.setSize(Constants.PHONE_WIDTH / 15, Constants.PHONE_WIDTH / 15);
        img_find.setSize(Constants.PHONE_WIDTH / 15, Constants.PHONE_WIDTH / 15);
        img_self.setSize(Constants.PHONE_WIDTH / 15, Constants.PHONE_WIDTH / 12);
        tv_community.setText("社区");
        tv_wardrobe.setText("我的衣橱");
        tv_find.setText("发现");
        tv_self.setText("我的");
        mHead_title = (LinearLayout) this.findViewById(R.id.head_title);
        MyImageView shouye_head_image = (MyImageView) mHead_title.findViewById(R.id.shouye_head_image);
        shouye_head_image.setImageBitmap(ImageDealUtils.readBitMap(this, R.mipmap.shouye_head_icon));
//        MyImageView shouye_head_image = (MyImageView) this.findViewById(R.id.shouye_head_image);
        shouye_head_image.setSize(Constants.PHONE_WIDTH / 2, Constants.PHONE_WIDTH / 20);
        ImageView camera = (ImageView) mHead_title.findViewById(R.id.camera);
        camera.setImageBitmap(ImageDealUtils.readBitMap(this, R.mipmap.new_camera));
        community_layout.setOnClickListener(this);
        wardrobe_layout.setOnClickListener(this);
        find_layout.setOnClickListener(this);
        self_layout.setOnClickListener(this);
//        camera.setOnClickListener(this);
        camera_layout.setOnClickListener(this);
        Constants.IMAGE_URL.clear();//清空选中的图片
        initClothCatogryNumber();

    }

    public void initLogic() {

//        String str = getIntent().getStringExtra("calendar_activity");
//        if ("calendar_activity".equals(str) || "image_recycleview_adapter".equals(getIntent().getStringExtra("image_recycleview_adapter"))
//                || "wardrobe_detail".equals(getIntent().getStringExtra("wardrobe_detail"))) {
//            btn_press(2);
//            setTabSelection(2);
//        } else if ("select_cancel".equals(getIntent().getStringExtra("select_cancel"))) {
//            btn_press(1);
//            setTabSelection(1);
//        } else if ("person_design".equals(getIntent().getStringExtra("person_design"))) {
//            btn_press(4);
//            setTabSelection(4);
//        } else if ("webview_to_find".equals(getIntent().getStringExtra("webview_to_find"))){
//            btn_press(3);
//            setTabSelection(3);
//        }else {
//            btn_press(1);
//            setTabSelection(1);
//        }
        if (Constants.selectFragmentNumber==1||Constants.selectFragmentNumber==3){
            mHead_title.setVisibility(View.VISIBLE);
        }else {
            mHead_title.setVisibility(View.GONE);
        }
        btn_press(Constants.selectFragmentNumber);
        setTabSelection(Constants.selectFragmentNumber);
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
                mMostPopularFragment = new MostPopularFragment();
                transaction.replace(R.id.fragment_content, mMostPopularFragment);
                break;
            case 2:
                newWardrobeFragment = new NewWardrobeFragment();
                transaction.replace(R.id.fragment_content, newWardrobeFragment);
                break;
            case 3:
                mFindFragment = new FindFragment();
                transaction.replace(R.id.fragment_content, mFindFragment);
                break;
            case 4:
                mSelfFragment = new SelfFragment();
                transaction.replace(R.id.fragment_content, mSelfFragment);
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
        if (mMostPopularFragment != null) {
            transaction.remove(mMostPopularFragment);
        }
        if (newWardrobeFragment != null) {
            transaction.remove(newWardrobeFragment);
        }
        if (mFindFragment != null) {
            transaction.remove(mFindFragment);
        }
        if (mSelfFragment != null) {
            transaction.remove(mSelfFragment);
        }
    }

    //主界面按钮选择器
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.community_layout:
                //选中的是社团按钮
//                LogUtils.showVerbose("MainActivity", "community_layout");
                Constants.selectFragmentNumber = 1;
                mHead_title.setVisibility(View.VISIBLE);
                btn_press(1);
                setTabSelection(1);
                break;
            case R.id.wardrobe_layout:
                //选中的是我的衣橱
//                LogUtils.showVerbose("MainActivity", "wardrobe_layout");
                Constants.selectFragmentNumber = 2;
                mHead_title.setVisibility(View.GONE);
                btn_press(2);
                setTabSelection(2);
                break;
            case R.id.find_layout:
                //选中的是发现
//                LogUtils.showVerbose("MainActivity", "find_layout");
                Constants.selectFragmentNumber = 3;
                mHead_title.setVisibility(View.VISIBLE);
                btn_press(3);
                setTabSelection(3);
                break;
            case R.id.self_layout:
                //选中的是我的
//                LogUtils.showVerbose("MainActivity", "self_layout");
                Constants.selectFragmentNumber = 4;
                mHead_title.setVisibility(View.GONE);
                btn_press(4);
                setTabSelection(4);
                break;
            case R.id.camera_layout:
                CacheDataHelper.addNullArgumentsMethod();
                Constants.selectFragmentNumber = 1;
//                ToastUtil.showShort(this,"我是相机");
                Intent intent = new Intent(MainActivity.this, ImagesSelectorActivity.class);
                // max number of images to be selected
                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 9);
                // min size of image which will be shown; to filter tiny images (mainly icons)
                intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
                // show camera or not
                intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
                // show wardrobe or not
                intent.putExtra(SelectorSettings.SELECTOR_SHOW_WARDROBE, true);
                // pass current selected images as the initial value
                intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
                // start the selector
                startActivity(intent);

                break;
            default:
                break;
        }
    }
    //测试微信第三方登陆

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (ActivityColection.isContains(this)) {
                ActivityColection.removeActivity(this);
                ActivityColection.removeAll();
                exitnum = exitnum + 1;
                Toast.makeText(MainActivity.this, "再点击一次就退出应用程序", Toast.LENGTH_SHORT).show();
            } else if (exitnum == 1) {
                System.exit(0);
            }
        }
        return true;
    }

    private void btn_press(int pos) {
        switch (pos) {
            case 1:
                //选中的是社团按钮
//                img_community.setImageResource(R.mipmap.one_sel);
                img_community.setImageBitmap(ImageDealUtils.readBitMap(this, R.mipmap.one_sel));
                tv_community.setTextColor(getResources().getColor(R.color.main_btn_color));
//                img_wardrobe.setImageResource(R.mipmap.two);
                img_wardrobe.setImageBitmap(ImageDealUtils.readBitMap(this, R.mipmap.two));
                tv_wardrobe.setTextColor(Color.GRAY);
//                img_find.setImageResource(R.mipmap.three);
                img_find.setImageBitmap(ImageDealUtils.readBitMap(this, R.mipmap.three));
                tv_find.setTextColor(Color.GRAY);
//                img_self.setImageResource(R.mipmap.four);
                img_self.setImageBitmap(ImageDealUtils.readBitMap(this, R.mipmap.four));
                tv_self.setTextColor(Color.GRAY);
                break;
            case 2:
                //选中的是我的衣橱
//                img_community.setImageResource(R.mipmap.one);
                img_community.setImageBitmap(ImageDealUtils.readBitMap(this, R.mipmap.one));
                tv_community.setTextColor(Color.GRAY);
//                img_wardrobe.setImageResource(R.mipmap.two_sel);
                img_wardrobe.setImageBitmap(ImageDealUtils.readBitMap(this, R.mipmap.two_sel));
                tv_wardrobe.setTextColor(getResources().getColor(R.color.main_btn_color));
//                img_find.setImageResource(R.mipmap.three);
                img_find.setImageBitmap(ImageDealUtils.readBitMap(this, R.mipmap.three));
                tv_find.setTextColor(Color.GRAY);
//                img_self.setImageResource(R.mipmap.four);
                img_self.setImageBitmap(ImageDealUtils.readBitMap(this, R.mipmap.four));
                tv_self.setTextColor(Color.GRAY);
                break;
            case 3:
                //选中的是发现
//                img_community.setImageResource(R.mipmap.one);
                img_community.setImageBitmap(ImageDealUtils.readBitMap(this, R.mipmap.one));
                tv_community.setTextColor(Color.GRAY);
//                img_wardrobe.setImageResource(R.mipmap.two);
                img_wardrobe.setImageBitmap(ImageDealUtils.readBitMap(this, R.mipmap.two));
                tv_wardrobe.setTextColor(Color.GRAY);
//                img_find.setImageResource(R.mipmap.three_sel);
                img_find.setImageBitmap(ImageDealUtils.readBitMap(this, R.mipmap.three_sel));
                tv_find.setTextColor(getResources().getColor(R.color.main_btn_color));
//                img_self.setImageResource(R.mipmap.four);
                img_self.setImageBitmap(ImageDealUtils.readBitMap(this, R.mipmap.four));
                tv_self.setTextColor(Color.GRAY);
                break;
            case 4:
                //选中的是我的
//                img_community.setImageResource(R.mipmap.one);
                img_community.setImageBitmap(ImageDealUtils.readBitMap(this, R.mipmap.one));
                tv_community.setTextColor(Color.GRAY);
//                img_wardrobe.setImageResource(R.mipmap.two);
                img_wardrobe.setImageBitmap(ImageDealUtils.readBitMap(this, R.mipmap.two));
                tv_wardrobe.setTextColor(Color.GRAY);
//                img_find.setImageResource(R.mipmap.three);
                img_find.setImageBitmap(ImageDealUtils.readBitMap(this, R.mipmap.three));
                tv_find.setTextColor(Color.GRAY);
//                img_self.setImageResource(R.mipmap.four_sel);
                img_self.setImageBitmap(ImageDealUtils.readBitMap(this, R.mipmap.four_sel));
                tv_self.setTextColor(getResources().getColor(R.color.main_btn_color));
                break;
            default:
                break;
        }
    }

    private void initClothCatogryNumber() {
        if (NetUtils.checkNet(this) == true) {
            MyHttpUtils.getNetMessage(this, "http://1zou.me/api/typenums/" + Constants.USER_ID, new MyHttpUtils.Callback() {
                @Override
                public void getResponseMsg(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Constants.Cloth_catogry_number[0] = jsonObject.getString("upper");
                        Constants.Cloth_catogry_number[1] = jsonObject.getString("trouser");
                        Constants.Cloth_catogry_number[2] = jsonObject.getString("skirt");
                        Constants.Cloth_catogry_number[3] = jsonObject.getString("hat");
                        Constants.Cloth_catogry_number[4] = jsonObject.getString("shoe");
                        Constants.Cloth_catogry_number[5] = jsonObject.getString("scarf");
                        Constants.Cloth_catogry_number[6] = jsonObject.getString("waist");
                        Constants.Cloth_catogry_number[7] = jsonObject.getString("bag");
                        initLogic();
                    } catch (JSONException e) {
                        LogUtils.showVerbose("MainActivity", "json数据信息解析错误");
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
}
