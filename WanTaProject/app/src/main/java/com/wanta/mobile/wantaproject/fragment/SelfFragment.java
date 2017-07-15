package com.wanta.mobile.wantaproject.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.BodyDetailMessageActivity;
import com.wanta.mobile.wantaproject.activity.ItemMostpopularActivity;
import com.wanta.mobile.wantaproject.activity.LoginActivity;
import com.wanta.mobile.wantaproject.activity.NewPersonDesignActivity;
import com.wanta.mobile.wantaproject.activity.PersonalDesignActivity;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.CacheDataHelper;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.ImageDealUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;
import com.wanta.mobile.wantaproject.utils.NetUtils;
import com.wanta.mobile.wantaproject.utils.ShowFragment;
import com.wanta.mobile.wantaproject.utils.SimpleDraweeControlUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by WangYongqiang on 2016/10/16.
 */
public class SelfFragment extends Fragment implements View.OnClickListener {

    private View mView_self;
    private LinearLayout mHead_title;
    private TabLayout mSelf_tablayout;
    private ViewPager mSelf_viewpager;
    private String[] mSelf_tabs;
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private MyImageView mSelf_author_icon;
    private TextView mSelf_design;
    private CustomSimpleDraweeView mSelf_head_icon;
    private TextView mSelf_care_content;
    private TextView mSelf_care_content_number;
    private TextView mSelf_fensi_content;
    private TextView mSelf_fensi_content_number;
    private TextView mSelf_like_content;
    private TextView mSelf_like_content_number;
    private TextView mSelf_store_content;
    private TextView mSelf_store_content_number;
    private TextView mSelf_author;
    private TextView mSelf_location;
    private TextView mSelf_author_height;
    private TextView mSelf_income;
    private TextView mSelf_money;
    private MyImageView mSelf_location_icon;
    private MyImageView mSelf_income_icon;
    private MyImageView mSelf_shopping_icon;
    private TextView mSelf_shopping;
    private MyImageView mSelf_ordering_icon;
    private TextView mSelf_ordering;
    private MyImageView mSelf_tickets_icon;
    private TextView mSelf_tickets;
    private MyImageView mSelf_credit_icon;
    private TextView mSelf_credit;
    private LinearLayout mSelf_design_layout;
    private LinearLayout mSelf_body_msg_layout;
    private TextView self_author_sigin;
    private LinearLayout self_author_msg_layout;
    private int iconnum = 0;
//    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView_self = inflater.inflate(R.layout.fragment_self,container,false);
        init();
        initMyMsg();
//        setData();
        return mView_self;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    private void initMyMsg() {
        if (NetUtils.checkNet(getActivity())==true){
            MyHttpUtils.getNetMessage(getActivity(), "http://1zou.me/apisq/userDetialStatic/"+Constants.USER_ID, new MyHttpUtils.Callback() {
                @Override
                public void getResponseMsg(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int errorCode = jsonObject.getInt("errorCode");
                        if (errorCode==0){
                            JSONArray datas = jsonObject.getJSONArray("datas");
                            JSONObject json = datas.getJSONObject(0);
                            if ("null".equals(json.getString("iconnum"))){
                                iconnum = 0;
                            }else {
                                iconnum = json.getInt("iconnum");
                            }
                            mSelf_fensi_content_number.setText(json.getInt("fansnum")+"");
                            if ("null".equals(json.getString("storednum"))){
                                mSelf_store_content_number.setText("0");
                            }else {
                                mSelf_store_content_number.setText(json.getString("storednum"));
                            }
                            if ("null".equals(json.getString("icnbrowsenum"))){
                                mSelf_care_content_number.setText("0");
                            }else {
                                mSelf_care_content_number.setText(json.getString("icnbrowsenum"));
                            }
                            if ("null".equals(json.getString("icnbelikednum"))){
                                mSelf_like_content_number.setText("0");
                            }else {
                                mSelf_like_content_number.setText(json.getString("icnbelikednum"));
                            }
                            //名字mSelf_author
                            if ("null".equals(json.getString("username"))){
                                mSelf_author.setText(Constants.USER_NAME);
                            }else {
                                mSelf_author.setText(json.getString("username"));
                            }
                            //位置信息
                            if ("null".equals(json.getString("address"))){
                                mSelf_location.setText("");
                            }else {
                                mSelf_location.setText(json.getString("address"));
                            }
                            //个性签名
                            if ("null".equals(json.getString("sign"))){
                                self_author_sigin.setText("");
                            }else {
                                self_author_sigin.setText("个性签名: "+json.getString("sign"));
                            }
                            //身体数据
                            String bodyMsg = "";
                            if (!"null".equals(json.getString("height"))){
                                bodyMsg = bodyMsg+json.getString("height")+"   ";
                            }
                            if (!"null".equals(json.getString("weight"))){
                                bodyMsg = bodyMsg+json.getString("weight")+"   ";
                            }
                            if (!"null".equals(json.getString("career"))){
                                bodyMsg = bodyMsg+json.getString("career")+"   ";
                            }
                            mSelf_author_height.setText(bodyMsg);
//                            mHandle.sendEmptyMessage(1);
                            setData();
                        }
                    } catch (JSONException e) {
                        LogUtils.showVerbose("MostPopularFragment","数据解析错误");
                    }
                }
            }, new MyHttpUtils.CallbackError() {
                @Override
                public void getResponseMsg(String error) {

                }
            });
        }else {
            NetUtils.showNoNetDialog(getActivity());
        }
    }

    private void init() {
        mSelf_author_icon = (MyImageView) mView_self.findViewById(R.id.self_author_icon);
        mSelf_author_icon.setSize(Constants.PHONE_WIDTH/10,Constants.PHONE_WIDTH/10);
        mSelf_design = (TextView) mView_self.findViewById(R.id.self_design);
        mSelf_design_layout = (LinearLayout) mView_self.findViewById(R.id.self_design_layout);
//        mSelf_design.setTextSize(Constants.PHONE_WIDTH/50);
        mSelf_head_icon = (CustomSimpleDraweeView) mView_self.findViewById(R.id.self_head_icon);
        mSelf_head_icon.setWidth(Constants.PHONE_WIDTH/8);
        mSelf_head_icon.setHeight(Constants.PHONE_WIDTH/8);

        if (TextUtils.isEmpty(Constants.AVATAR)){
//            mSelf_head_icon.setImageResource(R.mipmap.self_head_icon);
            mSelf_head_icon.setImageBitmap(ImageDealUtils.readBitMap(getActivity(),R.mipmap.self_head_icon));
        }else {
            SimpleDraweeControlUtils.getNetRingSimpleDraweeControl(getActivity(),mSelf_head_icon,Constants.FIRST_PAGE_IMAGE_URL+Constants.AVATAR);
        }
        mSelf_care_content = (TextView) mView_self.findViewById(R.id.self_care_content);
//        mSelf_care_content.setTextSize(Constants.PHONE_WIDTH/56);
        mSelf_care_content_number = (TextView) mView_self.findViewById(R.id.self_care_content_number);
//        mSelf_care_content_number.setTextSize(Constants.PHONE_WIDTH/56);
        mSelf_fensi_content = (TextView) mView_self.findViewById(R.id.self_fensi_content);
//        mSelf_fensi_content.setTextSize(Constants.PHONE_WIDTH/56);
        mSelf_fensi_content_number = (TextView) mView_self.findViewById(R.id.self_fensi_content_number);
//        mSelf_fensi_content_number.setTextSize(Constants.PHONE_WIDTH/56);
        mSelf_like_content = (TextView) mView_self.findViewById(R.id.self_like_content);
//        mSelf_like_content.setTextSize(Constants.PHONE_WIDTH/56);
        mSelf_like_content_number = (TextView) mView_self.findViewById(R.id.self_like_content_number);
//        mSelf_like_content_number.setTextSize(Constants.PHONE_WIDTH/56);
        mSelf_store_content = (TextView) mView_self.findViewById(R.id.self_store_content);
//        mSelf_store_content.setTextSize(Constants.PHONE_WIDTH/56);
        mSelf_store_content_number = (TextView) mView_self.findViewById(R.id.self_store_content_number);
//        mSelf_store_content_number.setTextSize(Constants.PHONE_WIDTH/56);
        mSelf_author = (TextView) mView_self.findViewById(R.id.self_author);
//        mSelf_author.setTextSize(Constants.PHONE_WIDTH/50);
        mSelf_location = (TextView) mView_self.findViewById(R.id.self_location);
//        mSelf_location.setTextSize(Constants.PHONE_WIDTH/56);
        self_author_sigin = (TextView) mView_self.findViewById(R.id.self_author_sigin);
        mSelf_author_height = (TextView) mView_self.findViewById(R.id.self_author_height);
//        mSelf_author_height.setTextSize(Constants.PHONE_WIDTH/56);
        mSelf_income = (TextView) mView_self.findViewById(R.id.self_income);
//        mSelf_income.setTextSize(Constants.PHONE_WIDTH/50);
        mSelf_money = (TextView) mView_self.findViewById(R.id.self_money);
//        mSelf_money.setTextSize(Constants.PHONE_WIDTH/50);
        mSelf_location_icon = (MyImageView) mView_self.findViewById(R.id.self_location_icon);
        mSelf_location_icon.setSize(Constants.PHONE_WIDTH/15,Constants.PHONE_WIDTH/15);
        mSelf_income_icon = (MyImageView) mView_self.findViewById(R.id.self_income_icon);
        mSelf_income_icon.setSize(Constants.PHONE_WIDTH/15,Constants.PHONE_WIDTH/15);
        mSelf_shopping_icon = (MyImageView) mView_self.findViewById(R.id.self_shopping_icon);
        mSelf_shopping_icon.setSize(Constants.PHONE_WIDTH/6,Constants.PHONE_WIDTH/6);
        mSelf_shopping = (TextView) mView_self.findViewById(R.id.self_shopping);
//        mSelf_shopping.setTextSize(Constants.PHONE_WIDTH/55);
        mSelf_ordering_icon = (MyImageView) mView_self.findViewById(R.id.self_ordering_icon);
        mSelf_ordering_icon.setSize(Constants.PHONE_WIDTH/6,Constants.PHONE_WIDTH/6);
        mSelf_ordering = (TextView) mView_self.findViewById(R.id.self_ordering);
//        mSelf_ordering.setTextSize(Constants.PHONE_WIDTH/55);
        mSelf_tickets_icon = (MyImageView) mView_self.findViewById(R.id.self_tickets_icon);
        mSelf_tickets_icon.setSize(Constants.PHONE_WIDTH/6,Constants.PHONE_WIDTH/6);
        mSelf_tickets = (TextView) mView_self.findViewById(R.id.self_tickets);
//        mSelf_tickets.setTextSize(Constants.PHONE_WIDTH/55);
        mSelf_credit_icon = (MyImageView) mView_self.findViewById(R.id.self_credit_icon);
        mSelf_credit_icon.setSize(Constants.PHONE_WIDTH/6,Constants.PHONE_WIDTH/6);
        mSelf_credit = (TextView) mView_self.findViewById(R.id.self_credit);
//        mSelf_credit.setTextSize(Constants.PHONE_WIDTH/55);
        mSelf_design_layout.setOnClickListener(this);

        mSelf_tablayout = (TabLayout) mView_self.findViewById(R.id.self_tablayout);
        mSelf_viewpager = (ViewPager) mView_self.findViewById(R.id.self_viewpager);
        mSelf_tabs = getActivity().getResources().getStringArray(R.array.self_tabs);

        mSelf_body_msg_layout = (LinearLayout) mView_self.findViewById(R.id.self_body_msg_layout);
        mSelf_body_msg_layout.setOnClickListener(this);

        self_author_msg_layout = (LinearLayout) mView_self.findViewById(R.id.self_author_msg_layout);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams((int) ((Constants.PHONE_WIDTH*1.00/4)*2.5), ViewGroup.LayoutParams.WRAP_CONTENT);
        self_author_msg_layout.setLayoutParams(params1);
    }
    private void setData() {
        mFragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                LogUtils.showVerbose("SelfFragment","position="+position);
                return ShowFragment.getSelfFragment(position);
            }

            @Override
            public int getCount() {
                return mSelf_tabs.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (position==0){
                    return mSelf_tabs[position]+"·"+iconnum;
                }else {
                    return mSelf_tabs[position];
                }
            }
        };
        mSelf_viewpager.setAdapter(mFragmentPagerAdapter);
        mSelf_tablayout.setupWithViewPager(mSelf_viewpager);
        mSelf_tablayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.self_tabs_indecator));
        mSelf_tablayout.setTabTextColors(getResources().getColor(R.color.self_tabs_normal),
                getResources().getColor(R.color.self_tabs_select));
    }

    @Override
    public void onAttach(Activity activity) {
        mHead_title = (LinearLayout) activity.findViewById(R.id.head_title);
        mHead_title.setVisibility(View.GONE);
        super.onAttach(activity);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                setData();
            }
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.self_design_layout:
                //jump to personal designing
                if (Constants.STATUS.equals("reg_log")) {
                    CacheDataHelper.addNullArgumentsMethod();
                    Intent intent = new Intent(getActivity(), NewPersonDesignActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }else {
                    Intent register_intent = new Intent(getActivity(), LoginActivity.class);
                    register_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(register_intent);
                    getActivity().finish();
                }

                break;
            case R.id.self_body_msg_layout:
                CacheDataHelper.addNullArgumentsMethod();
                Intent intent1 = new Intent(getActivity(), BodyDetailMessageActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("self_msg","self_msg");
                startActivity(intent1);
                getActivity().finish();
                break;
        }
    }
}
