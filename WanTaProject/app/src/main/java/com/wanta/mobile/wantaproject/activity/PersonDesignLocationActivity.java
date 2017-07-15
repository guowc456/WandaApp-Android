package com.wanta.mobile.wantaproject.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.PersonDesignLocationRecycleViewAdapter;
import com.wanta.mobile.wantaproject.customview.CustomGridLayoutManager;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.customview.QucklyLocationBar;
import com.wanta.mobile.wantaproject.domain.PersonMessageSerializable;
import com.wanta.mobile.wantaproject.mordel.DomesticDbHelper;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/11/26.
 */
public class PersonDesignLocationActivity extends BaseActivity implements View.OnClickListener {

    private MyImageView mPerson_design_location_back;
    private ViewPager mPerson_design_location_viewpager;
    private FragmentPagerAdapter mAdapter;
    private TabLayout mPerson_design_location_tab_layout;
    private String[] mStrings = {
            "北京", "上海", "广州"
    };
    private String[] current_city = {
            Constants.CURRENT_CITY
    };
    private String[] mStrings3 = {
            "当前", "历史", "热门", "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "w", "x", "y", "z"
    };
    private List<String> names = new ArrayList<>();
    private List<TextView> text_content = new ArrayList<>();//记录当前选中的地址
    private int[] mStrings4 = {
            R.id.domestic_current_location, R.id.domestic_history_choose, R.id.domestic_hot_location,
            R.id.domestic_sequence_A, R.id.domestic_sequence_B, R.id.domestic_sequence_C, R.id.domestic_sequence_D,
            R.id.domestic_sequence_E, R.id.domestic_sequence_F, R.id.domestic_sequence_G, R.id.domestic_sequence_H,
            R.id.domestic_sequence_J, R.id.domestic_sequence_K,
            R.id.domestic_sequence_L, R.id.domestic_sequence_M, R.id.domestic_sequence_N,
            R.id.domestic_sequence_P, R.id.domestic_sequence_Q, R.id.domestic_sequence_R, R.id.domestic_sequence_S,
            R.id.domestic_sequence_T, R.id.domestic_sequence_W,
            R.id.domestic_sequence_X, R.id.domestic_sequence_Y, R.id.domestic_sequence_Z
    };
    private List<RecyclerView> mRecyclerViewList = new ArrayList<>();
    private RecyclerView mDomestic_current_location;
    private RecyclerView mDomestic_history_choose;
    private RecyclerView mDomestic_hot_location;
    private QucklyLocationBar mLocation_quick_bar;
    private LinearLayout mLocation_head_layout;
    private PopupWindow set_pop;
    private RecyclerView mRecyclerView;
    private ScrollView mPerson_design_location_scrollview;
    private String[] mArr;
    private PersonDesignLocationRecycleViewAdapter mSequence_adapter;
    private TextView mPerson_design_location_ok;
    private ProgressDialog mProgressDialog;
    private List<String> mA_array;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_design_location);
        ActivityColection.addActivity(this);
//        LogUtils.showVerbose("PersonDesignLocationActivity", "111");
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在加载位置信息,请稍等......");
//        mHandler.sendEmptyMessageDelayed(5,4000);
        init();
        mProgressDialog.show();
        mHandler.sendEmptyMessageDelayed(5,100);
    }

    private void init() {

        for (int i = 0; i < mStrings3.length; i++) {
            names.add(mStrings3[i]);
        }
        mPerson_design_location_back = (MyImageView) this.findViewById(R.id.person_design_location_back);
        mPerson_design_location_back.setSize(Constants.PHONE_WIDTH / 18, Constants.PHONE_WIDTH / 18);
        mPerson_design_location_back.setOnClickListener(this);
        mPerson_design_location_ok = (TextView) this.findViewById(R.id.person_design_location_ok);
        mPerson_design_location_ok.setOnClickListener(this);
//        //当前城市
//        mDomestic_current_location = (RecyclerView) this.findViewById(R.id.domestic_current_location);
//        CustomGridLayoutManager current_manager = new CustomGridLayoutManager(this, 3);
//        current_manager.setOrientation(GridLayoutManager.VERTICAL);
//        current_manager.setScrollEnabled(false);
//        mDomestic_current_location.setLayoutManager(current_manager);
//        PersonDesignLocationRecycleViewAdapter current_adapter = new PersonDesignLocationRecycleViewAdapter(this, mStrings);
//        mDomestic_current_location.setAdapter(current_adapter);
//        current_adapter.setOnPersonDesignLocationItemListener(new PersonDesignLocationRecycleViewAdapter.OnPersonDesignLocationItemListener() {
//            @Override
//            public void setItemClick(View view) {
//                int position = mDomestic_current_location.getChildAdapterPosition(view);
//                Toast.makeText(PersonDesignLocationActivity.this,position+"",Toast.LENGTH_SHORT).show();
//            }
//        });
////        mDomestic_current_location.setVerticalScrollBarEnabled(false);
//        //历史选择
//        mDomestic_history_choose = (RecyclerView) this.findViewById(R.id.domestic_history_choose);
//        CustomGridLayoutManager history_manager = new CustomGridLayoutManager(this, 3);
//        history_manager.setOrientation(GridLayoutManager.VERTICAL);
//        history_manager.setScrollEnabled(false);
//        mDomestic_history_choose.setLayoutManager(history_manager);
//        PersonDesignLocationRecycleViewAdapter history_adapter = new PersonDesignLocationRecycleViewAdapter(this, mStrings);
//        mDomestic_history_choose.setAdapter(history_adapter);
//////        mDomestic_history_choose.setVerticalScrollBarEnabled(false);
//        //热门地方
//        mDomestic_hot_location = (RecyclerView) this.findViewById(R.id.domestic_hot_location);
//        CustomGridLayoutManager hot_manager = new CustomGridLayoutManager(this, 3);
//        hot_manager.setOrientation(GridLayoutManager.VERTICAL);
//        hot_manager.setScrollEnabled(false);
//        mDomestic_hot_location.setLayoutManager(hot_manager);
//        PersonDesignLocationRecycleViewAdapter hot_adapter = new PersonDesignLocationRecycleViewAdapter(this, mStrings);
//        mDomestic_hot_location.setAdapter(hot_adapter);
//        mDomestic_hot_location.setVerticalScrollBarEnabled(false);

        for (int i = 0; i < 4; i++) {
            if (i > 2) {
                DomesticDbHelper helper = new DomesticDbHelper(this);
                final List<String> cloumnMessage = helper.getCloumnMessage(mStrings3[i]);
                mArr = new String[cloumnMessage.size()];
                for (int j = 0; j < cloumnMessage.size(); j++) {
                    mArr[j] = cloumnMessage.get(j);
                }
            }
            mRecyclerView = (RecyclerView) this.findViewById(mStrings4[i]);
            mRecyclerViewList.add(mRecyclerView);
            CustomGridLayoutManager sequence_manager = new CustomGridLayoutManager(this, 3);
            sequence_manager.setOrientation(GridLayoutManager.VERTICAL);
            sequence_manager.setScrollEnabled(false);
            mRecyclerView.setLayoutManager(sequence_manager);
            if (i == 0) {
                mSequence_adapter = new PersonDesignLocationRecycleViewAdapter(this, current_city);
            } else if (i == 1) {
                mSequence_adapter = new PersonDesignLocationRecycleViewAdapter(this, mStrings);
            } else if (i == 2) {
                mSequence_adapter = new PersonDesignLocationRecycleViewAdapter(this, mStrings);
            } else {
                mSequence_adapter = new PersonDesignLocationRecycleViewAdapter(this, mArr);
            }
            mRecyclerView.setAdapter(mSequence_adapter);
            mSequence_adapter.setOnPersonDesignLocationItemListener(new PersonDesignLocationRecycleViewAdapter.OnPersonDesignLocationItemListener() {
                @Override
                public void setItemClick(View view) {
//                    int position = mRecyclerView.getChildAdapterPosition(view);
                    TextView textView = (TextView) view.findViewById(R.id.item_location_content);
                    if (text_content.size() == 0) {
                        textView.setSelected(true);
                        textView.setTextColor(getResources().getColor(R.color.white));
                        text_content.add(textView);
                    } else {
                        text_content.get(0).setSelected(false);
                        text_content.get(0).setTextColor(getResources().getColor(R.color.text_color));
                        text_content.clear();
                        textView.setSelected(true);
                        textView.setTextColor(getResources().getColor(R.color.white));
                        text_content.add(textView);
                    }
//                    String text = ((TextView) view.findViewById(R.id.item_location_content)).getText().toString();
//                    Toast.makeText(PersonDesignLocationActivity.this, text + "", Toast.LENGTH_SHORT).show();

                }
            });
        }
//        for (int i = 4; i < mStrings3.length; i++) {
//            if (i > 2) {
//                DomesticDbHelper helper = new DomesticDbHelper(this);
//                final List<String> cloumnMessage = helper.getCloumnMessage(mStrings3[i]);
//                mArr = new String[cloumnMessage.size()];
//                for (int j = 0; j < cloumnMessage.size(); j++) {
//                    mArr[j] = cloumnMessage.get(j);
//                }
//            }
//            mRecyclerView = (RecyclerView) this.findViewById(mStrings4[i]);
//            mRecyclerViewList.add(mRecyclerView);
//            CustomGridLayoutManager sequence_manager = new CustomGridLayoutManager(this, 3);
//            sequence_manager.setOrientation(GridLayoutManager.VERTICAL);
//            sequence_manager.setScrollEnabled(false);
//            mRecyclerView.setLayoutManager(sequence_manager);
//            if (i == 0) {
//                mSequence_adapter = new PersonDesignLocationRecycleViewAdapter(this, mStrings);
//            } else if (i == 1) {
//                mSequence_adapter = new PersonDesignLocationRecycleViewAdapter(this, mStrings);
//            } else if (i == 2) {
//                mSequence_adapter = new PersonDesignLocationRecycleViewAdapter(this, mStrings);
//            } else {
//                mSequence_adapter = new PersonDesignLocationRecycleViewAdapter(this, mArr);
//            }
//            mRecyclerView.setAdapter(mSequence_adapter);
//            mSequence_adapter.setOnPersonDesignLocationItemListener(new PersonDesignLocationRecycleViewAdapter.OnPersonDesignLocationItemListener() {
//                @Override
//                public void setItemClick(View view) {
////                    int position = mRecyclerView.getChildAdapterPosition(view);
//                    TextView textView  = (TextView) view.findViewById(R.id.item_location_content);
//                    if (text_content.size()==0){
//                        textView.setSelected(true);
//                        textView.setTextColor(getResources().getColor(R.color.white));
//                        text_content.add(textView);
//                    }else {
//                        text_content.get(0).setSelected(false);
//                        text_content.get(0).setTextColor(getResources().getColor(R.color.text_color));
//                        text_content.clear();
//                        textView.setSelected(true);
//                        textView.setTextColor(getResources().getColor(R.color.white));
//                        text_content.add(textView);
//                    }
////                    String text = ((TextView) view.findViewById(R.id.item_location_content)).getText().toString();
////                    Toast.makeText(PersonDesignLocationActivity.this, text + "", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        }
        //设置快速定位条
        mLocation_quick_bar = (QucklyLocationBar) this.findViewById(R.id.location_quick_bar);
        mLocation_head_layout = (LinearLayout) this.findViewById(R.id.location_head_layout);
        mPerson_design_location_scrollview = (ScrollView) this.findViewById(R.id.person_design_location_scrollview);
        mLocation_quick_bar.setBarWidth(Constants.PHONE_WIDTH / 15);
        mLocation_quick_bar.setBarHeight(Constants.PHONE_HEIGHT - mLocation_head_layout.getHeight());
        mLocation_quick_bar.setQuicklyLocationBarListener(new QucklyLocationBar.QuicklyLocationBarListener() {
            @Override
            public void setItemListener(String currentStr, boolean isup) {
                Message message = new Message();
                message.what = Constants.isChange;
                Bundle bundle = new Bundle();
                bundle.putString("currentStr", currentStr);
                bundle.putBoolean("isup", isup);
                message.setData(bundle);
                mHandler.sendMessage(message);
//                LogUtils.showVerbose("PersonDesignLocationActivity", "currentString=" + currentStr + "  isup" + isup);
            }
        });
    }

    private void showChooseDialog(String currentStr, boolean isup) {
        if (set_pop == null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.pop_window_location, null);
            set_pop = new PopupWindow(contentView, Constants.PHONE_WIDTH / 4, Constants.PHONE_WIDTH / 4, false);

            set_pop.setBackgroundDrawable(new BitmapDrawable());
            set_pop.setOutsideTouchable(true);
            set_pop.setFocusable(true);
            set_pop.setTouchable(true);
            set_pop.showAsDropDown(mLocation_head_layout, (int) (Constants.PHONE_WIDTH / 2.5), Constants.PHONE_HEIGHT / 2 - mLocation_head_layout.getHeight());
        }
        View my_pop = set_pop.getContentView();
        TextView pop_window_text = (TextView) my_pop.findViewById(R.id.pop_window_text);
        pop_window_text.setText(currentStr);
        if (currentStr.length() == 1) {
            pop_window_text.setTextSize(60);
        } else {
            pop_window_text.setTextSize(30);
        }
        if (TextUtils.isEmpty(currentStr)) {
            set_pop.dismiss();
            set_pop = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (ActivityColection.isContains(this)) {
                ActivityColection.removeActivity(this);
                jumpPersonDesignActivity();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.person_design_location_back:
                jumpPersonDesignActivity();
                break;
            case R.id.person_design_location_ok:
                if (text_content.size() == 0) {
                    Toast.makeText(PersonDesignLocationActivity.this, "亲，居住地还没有选择呢？", Toast.LENGTH_SHORT).show();
                } else {
                    Constants.currentLocation = text_content.get(0).getText().toString();
                    jumpPersonDesignActivity();
                }
                break;
            default:
                break;
        }
    }

    public void jumpPersonDesignActivity() {
        Intent intent = new Intent(PersonDesignLocationActivity.this, NewPersonDesignActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("person_design_location", new PersonMessageSerializable(Constants.currentLocation));
        setResult(RESULT_OK, intent);
        finish();
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Constants.isChange) {
                Bundle data = msg.getData();
                LogUtils.showVerbose("PersonDesignLocationActivity", "currentString=" + data.get("currentStr") + "  isup" + data.getBoolean("isup"));
                String msg_currentStr = (String) data.get("currentStr");
                boolean msg_isup = data.getBoolean("isup");
                //弹出选择的对话框
                showChooseDialog(msg_currentStr, msg_isup);

                //滑动scrooll
                double all_height = 0;
                int num = 0;
                if (names.contains(msg_currentStr.toLowerCase())) {
                    for (int j = 0; j < names.size(); j++) {
                        if (names.get(j).equals(msg_currentStr.toLowerCase())) {
                            num = j;
                            for (int i = 0; i < num; i++) {
                                all_height = all_height + mRecyclerViewList.get(i).getHeight();
                            }
                            if (num >= 0 && num < 4) {
                                mPerson_design_location_scrollview.scrollTo(0, 0);
                            } else if (num <= 10 && num >= 4) {
                                mPerson_design_location_scrollview.scrollTo(0, (int) all_height);
                            } else {
                                mPerson_design_location_scrollview.scrollTo(0, (int) all_height + Constants.PHONE_HEIGHT / 2);
                            }
                        }
                    }
                }
            } else if (msg.what == 5) {
                init1();
                mHandler.sendEmptyMessageDelayed(6,5000);
            }else if (msg.what==6){
                mProgressDialog.dismiss();
            }

        }
    };

    public void init1() {

        for (int i = 4; i < mStrings3.length; i++) {
            DomesticDbHelper helper = new DomesticDbHelper(this);
            final List<String> cloumnMessage = helper.getCloumnMessage(mStrings3[i]);
            mArr = new String[cloumnMessage.size()];
            for (int j = 0; j < cloumnMessage.size(); j++) {
                mArr[j] = cloumnMessage.get(j);
            }
            mRecyclerView = (RecyclerView) this.findViewById(mStrings4[i]);
            mRecyclerViewList.add(mRecyclerView);
            CustomGridLayoutManager sequence_manager = new CustomGridLayoutManager(this, 3);
            sequence_manager.setOrientation(GridLayoutManager.VERTICAL);
            sequence_manager.setScrollEnabled(false);
            mRecyclerView.setLayoutManager(sequence_manager);
            mSequence_adapter = new PersonDesignLocationRecycleViewAdapter(this, mArr);
            mRecyclerView.setAdapter(mSequence_adapter);
            mSequence_adapter.setOnPersonDesignLocationItemListener(new PersonDesignLocationRecycleViewAdapter.OnPersonDesignLocationItemListener() {
                @Override
                public void setItemClick(View view) {
//                    int position = mRecyclerView.getChildAdapterPosition(view);
                    TextView textView = (TextView) view.findViewById(R.id.item_location_content);
                    if (text_content.size() == 0) {
                        textView.setSelected(true);
                        textView.setTextColor(getResources().getColor(R.color.white));
                        text_content.add(textView);
                    } else {
                        text_content.get(0).setSelected(false);
                        text_content.get(0).setTextColor(getResources().getColor(R.color.text_color));
                        text_content.clear();
                        textView.setSelected(true);
                        textView.setTextColor(getResources().getColor(R.color.white));
                        text_content.add(textView);
                    }

                }
            });

        }
    }

}
