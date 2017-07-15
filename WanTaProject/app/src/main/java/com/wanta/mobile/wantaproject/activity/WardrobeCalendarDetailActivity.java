package com.wanta.mobile.wantaproject.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.CalendarDetailRecycleAdapter;
import com.wanta.mobile.wantaproject.calendar.CalendarDateUtils;
import com.wanta.mobile.wantaproject.calendar.DateAdapter;
import com.wanta.mobile.wantaproject.calendar.SpecialCalendar;
import com.wanta.mobile.wantaproject.customview.CustomCalendarView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.CacheDataHelper;
import com.wanta.mobile.wantaproject.fragment.CalendarDetailOtherPersonFragment;
import com.wanta.mobile.wantaproject.fragment.CalendarDetailPublishArginFragment;
import com.wanta.mobile.wantaproject.fragment.CalendarDetailTodaySelfFragment;
import com.wanta.mobile.wantaproject.fragment.CommunityFragment;
import com.wanta.mobile.wantaproject.fragment.FindFragment;
import com.wanta.mobile.wantaproject.fragment.SelfFragment;
import com.wanta.mobile.wantaproject.fragment.WardrobeFragment;
import com.wanta.mobile.wantaproject.phonecamera.LunchCameraActivity;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.DealReturnLogicUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;
import com.wanta.mobile.wantaproject.utils.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by WangYongqiang on 2016/11/15.
 */
public class WardrobeCalendarDetailActivity extends BaseActivity implements GestureDetector.OnGestureListener, View.OnClickListener {

    private MyImageView mCalendar_detail_back_icon;
    private LinearLayout mCalendar_detail_back_layout;
    private TextView mCalendar_detail_current_month;
    private MyImageView mCalendar_detail_camera;
    private int mSelectDay;
    private int mSelectYear;
    private int mSelectMonth;
    private int year_c;
    private int month_c;
    private int day_c;
    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private int week_c;
    private int week_num;
    private int currentNum;
    private int currentWeek;
    private DateAdapter dateAdapter;
    private int daysOfMonth = 0; // 某月的天数
    private int dayOfWeek = 0; // 具体某一天是星期几
    private int weeksOfMonth = 0;
    private SpecialCalendar sc = null;
    private boolean isLeapyear = false; // 是否为闰年
    private int selectPostion = 0;
    private String dayNumbers[] = new String[7];
    private GridView gridView;
    private ViewFlipper mFlipper;
    private GestureDetector gestureDetector = null;
    private int currentSelectYear = 0;
    private int currentSelectMonth = 0;
    private int currentSelectDay = 0;
    private MyImageView mCalendar_detail_location_icon;
    private TextView mCalendar_detail_location_content;
    private MyImageView mCalendar_detail_temperature_icon;
    private TextView mCalendar_detail_temperature_content;
    private ViewPager mCalendar_detail_viewpager;
    private FrameLayout mCalendar_detail_fragment;
    private CalendarDetailPublishArginFragment mPublishArginFragment;
    private CalendarDetailTodaySelfFragment mTodaySelfFragment;
    private CalendarDetailOtherPersonFragment mOtherPersonFragment;
    private LinearLayout mCalendar_detail_camera_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardrobe_calendar_detail);
        mSelectDay = Constants.currentDay;
        mSelectYear = Constants.currentYear;
        mSelectMonth = Constants.currentMonth + 1;
        currentSelectYear = mSelectYear;
        currentSelectMonth = mSelectMonth;
        currentSelectDay = mSelectDay;
//        LogUtils.showVerbose("WardrobeCalendarDetailActivity","mSelectDay="+mSelectDay+" mSelectYear="+mSelectYear+" mSelectMonth="+mSelectMonth);
        init();
        initId();
        initData();
    }

    private void init() {
        year_c = mSelectYear;
        month_c = mSelectMonth;
        day_c = mSelectDay;
        //设置当前显示的时间
        currentYear = year_c;
        currentMonth = month_c;
        currentDay = day_c;
        sc = new SpecialCalendar();
        getCalendar(year_c, month_c);
        week_num = getWeeksOfMonth();
        currentNum = week_num;
        if (dayOfWeek == 7) {
            week_c = day_c / 7 + 1;
        } else {
            if (day_c <= (7 - dayOfWeek)) {
                week_c = 1;
            } else {
                if ((day_c - (7 - dayOfWeek)) % 7 == 0) {
                    week_c = (day_c - (7 - dayOfWeek)) / 7 + 1;
                } else {
                    week_c = (day_c - (7 - dayOfWeek)) / 7 + 2;
                }
            }
        }
        currentWeek = week_c;
        getCurrent();
    }

    private void initId() {
        mCalendar_detail_back_icon = (MyImageView) this.findViewById(R.id.calendar_detail_back_icon);
        mCalendar_detail_back_icon.setSize(Constants.PHONE_WIDTH / 14, Constants.PHONE_WIDTH / 14);
        mCalendar_detail_camera = (MyImageView) this.findViewById(R.id.calendar_detail_camera);
        mCalendar_detail_camera.setSize(Constants.PHONE_WIDTH / 14, Constants.PHONE_WIDTH / 14);
        mCalendar_detail_camera_layout = (LinearLayout) this.findViewById(R.id.calendar_detail_camera_layout);
        mCalendar_detail_camera_layout.setOnClickListener(this);
        mCalendar_detail_back_layout = (LinearLayout) this.findViewById(R.id.calendar_detail_back_layout);
        mCalendar_detail_back_layout.setOnClickListener(this);
        mCalendar_detail_current_month = (TextView) this.findViewById(R.id.calendar_detail_current_month);
        mCalendar_detail_current_month.setText(mSelectMonth + "月");
        mFlipper = (ViewFlipper) this.findViewById(R.id.flipper1);

        mCalendar_detail_location_icon = (MyImageView) this.findViewById(R.id.calendar_detail_location_icon);
        mCalendar_detail_location_icon.setSize(Constants.PHONE_WIDTH / 10, Constants.PHONE_WIDTH / 10);
        mCalendar_detail_location_content = (TextView) this.findViewById(R.id.calendar_detail_location_content);
        mCalendar_detail_location_content.setText(Constants.CURRENT_CITY + "");
        mCalendar_detail_temperature_icon = (MyImageView) this.findViewById(R.id.calendar_detail_temperature_icon);
        mCalendar_detail_temperature_icon.setSize(Constants.PHONE_WIDTH / 10, Constants.PHONE_WIDTH / 10);
        mCalendar_detail_temperature_content = (TextView) this.findViewById(R.id.calendar_detail_temperature_content);
        mCalendar_detail_fragment = (FrameLayout) this.findViewById(R.id.calendar_detail_fragment);
        //获取当前的温度
        getTemper();
        //设置当前默认的界面
        setTabSelection(0);
    }

    private void getTemper() {
        if (NetUtils.checkNet(this) == true) {
            String city = getCurrentCity(Constants.CURRENT_CITY);
//        LogUtils.showVerbose("WardrobeCalendarDetailActivity", "天气url=" + "http://1zou.me/api/temper/" + city);
            //获取当前的温度信息
            try {
                MyHttpUtils.getNetMessage(this, "http://1zou.me/api/temper/" + URLEncoder.encode(city, "UTF-8"), new MyHttpUtils.Callback() {
                    @Override
                    public void getResponseMsg(String response) {
                        LogUtils.showVerbose("WardrobeCalendarDetailActivity", "response=" + response);
                        //                {"low":1,"high":0}
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int low = jsonObject.getInt("low");
                            int high = jsonObject.getInt("high");
                            mCalendar_detail_temperature_content.setText(low + "°C-" + high + "°C");
                            LogUtils.showVerbose("WardrobeCalendarDetailActivity", "temper=" + low + "°C-" + high + "°C");
                        } catch (JSONException e) {
                            LogUtils.showVerbose("WardrobeCalendarDetailActivity", "温度信息解析错误");
                        }
                    }
                }, new MyHttpUtils.CallbackError() {
                    @Override
                    public void getResponseMsg(String error) {
                        LogUtils.showVerbose("WardrobeCalendarDetailActivity", "温度信息解析错误");
                    }
                });
            } catch (UnsupportedEncodingException e) {
                LogUtils.showVerbose("WardrobeCalendarDetailActivity", "汉字转换成utf-8失败");
            }

        } else {
            NetUtils.showNoNetDialog(this);
        }

    }

    private String getCurrentCity(String currentCity) {
        String city = "";
        if (currentCity.contains("市")) {
            city = currentCity.substring(0, currentCity.length() - 1);
        } else {
            city = currentCity;
        }
        LogUtils.showVerbose("WardrobeCalendarDetailActivity", "city=" + city);
        return city;
    }

    private void initData() {
        gestureDetector = new GestureDetector(this);
        mFlipper = (ViewFlipper) findViewById(R.id.flipper1);
        dateAdapter = new DateAdapter(this, getResources(), currentYear,
                currentMonth, currentWeek, currentNum, selectPostion,
                currentWeek == 1 ? true : false);
        addGridView();
        dayNumbers = dateAdapter.getDayNumbers();
        gridView.setAdapter(dateAdapter);
        selectPostion = dateAdapter.getTodayPosition(mSelectYear + "", mSelectMonth + "", mSelectDay + "");//算出某一个天在这一周中的位置
        gridView.setSelection(selectPostion);
        mFlipper.addView(gridView, 0);
    }

    /**
     * 判断某年某月所有的星期数
     *
     * @param year
     * @param month
     */
    public int getWeeksOfMonth(int year, int month) {
        // 先判断某月的第一天为星期几
        int preMonthRelax = 0;
        int dayFirst = getWhichDayOfWeek(year, month);
        int days = sc.getDaysOfMonth(sc.isLeapYear(year), month);
        if (dayFirst != 7) {
            preMonthRelax = dayFirst;
        }
        if ((days + preMonthRelax) % 7 == 0) {
            weeksOfMonth = (days + preMonthRelax) / 7;
        } else {
            weeksOfMonth = (days + preMonthRelax) / 7 + 1;
        }
        return weeksOfMonth;

    }

    /**
     * 判断某年某月的第一天为星期几
     *
     * @param year
     * @param month
     * @return
     */
    public int getWhichDayOfWeek(int year, int month) {
        return sc.getWeekdayOfMonth(year, month);

    }

    /**
     * @param year
     * @param month
     */
    public int getLastDayOfWeek(int year, int month) {
        return sc.getWeekDayOfLastMonth(year, month,
                sc.getDaysOfMonth(isLeapyear, month));
    }

    public void getCalendar(int year, int month) {
        isLeapyear = sc.isLeapYear(year); // 是否为闰年
        daysOfMonth = sc.getDaysOfMonth(isLeapyear, month); // 某月的总天数
        dayOfWeek = sc.getWeekdayOfMonth(year, month); // 某月第一天为星期几
    }

    public int getWeeksOfMonth() {
        int preMonthRelax = 0;
        if (dayOfWeek != 7) {
            preMonthRelax = dayOfWeek;
        }
        if ((daysOfMonth + preMonthRelax) % 7 == 0) {
            weeksOfMonth = (daysOfMonth + preMonthRelax) / 7;
        } else {
            weeksOfMonth = (daysOfMonth + preMonthRelax) / 7 + 1;
        }
        return weeksOfMonth;
    }

    /**
     * 重新计算当前的年月
     */
    public void getCurrent() {
        if (currentWeek > currentNum) {
            if (currentMonth + 1 <= 12) {
                currentMonth++;
            } else {
                currentMonth = 1;
                currentYear++;
            }
            currentWeek = 1;
            currentNum = getWeeksOfMonth(currentYear, currentMonth);
        } else if (currentWeek == currentNum) {
            if (getLastDayOfWeek(currentYear, currentMonth) == 6) {
            } else {
                if (currentMonth + 1 <= 12) {
                    currentMonth++;
                } else {
                    currentMonth = 1;
                    currentYear++;
                }
                currentWeek = 1;
                currentNum = getWeeksOfMonth(currentYear, currentMonth);
            }

        } else if (currentWeek < 1) {
            if (currentMonth - 1 >= 1) {
                currentMonth--;
            } else {
                currentMonth = 12;
                currentYear--;
            }
            currentNum = getWeeksOfMonth(currentYear, currentMonth);
            currentWeek = currentNum - 1;
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        int gvFlag = 0;
        if (e1.getX() - e2.getX() > 80) {
            // 向左滑
            addGridView();
            currentWeek++;
            getCurrent();
            dateAdapter = new DateAdapter(this, getResources(), currentYear,
                    currentMonth, currentWeek, currentNum, selectPostion,
                    currentWeek == 1 ? true : false);
            dayNumbers = dateAdapter.getDayNumbers();
            gridView.setAdapter(dateAdapter);
            gvFlag++;
            mFlipper.addView(gridView, gvFlag);
            if (dateAdapter.getCurrentYear(selectPostion) == currentSelectYear
                    && dateAdapter.getCurrentMonth(selectPostion) == currentSelectMonth
                    && dayNumbers[selectPostion].equals(currentSelectDay + "")) {
                dateAdapter.setSeclection(selectPostion);
            }
            mCalendar_detail_current_month.setText(dateAdapter.getCurrentMonth(selectPostion) + "月");
            this.mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_left_in));
            this.mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_left_out));
            this.mFlipper.showNext();
            mFlipper.removeViewAt(0);
            return true;

        } else if (e1.getX() - e2.getX() < -80) {
            addGridView();
            currentWeek--;
            getCurrent();
            dateAdapter = new DateAdapter(this, getResources(), currentYear,
                    currentMonth, currentWeek, currentNum, selectPostion,
                    currentWeek == 1 ? true : false);
            dayNumbers = dateAdapter.getDayNumbers();
            gridView.setAdapter(dateAdapter);
            gvFlag++;
            mFlipper.addView(gridView, gvFlag);
            if (dateAdapter.getCurrentYear(selectPostion) == currentSelectYear
                    && dateAdapter.getCurrentMonth(selectPostion) == currentSelectMonth
                    && dayNumbers[selectPostion].equals(currentSelectDay + "")) {
                dateAdapter.setSeclection(selectPostion);
            }
            mCalendar_detail_current_month.setText(dateAdapter.getCurrentMonth(selectPostion) + "月");
            this.mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_right_in));
            this.mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_right_out));
            this.mFlipper.showPrevious();
            mFlipper.removeViewAt(0);
            return true;
            // }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        gridView = new GridView(this);
        gridView.setNumColumns(7);
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);
        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return WardrobeCalendarDetailActivity.this.gestureDetector.onTouchEvent(event);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectPostion = position;
                dateAdapter.setSeclection(position);
                dateAdapter.notifyDataSetChanged();
                currentSelectYear = dateAdapter.getCurrentYear(selectPostion);
                currentSelectMonth = dateAdapter.getCurrentMonth(selectPostion);
                currentSelectDay = Integer.parseInt(dayNumbers[position]);
            }
        });
        gridView.setLayoutParams(params);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onDown(MotionEvent e) {

        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calendar_detail_back_layout:
//                jumpToWardrobeCalendar();
                DealReturnLogicUtils.dealReturnLogic(this);
                break;
            case R.id.calendar_detail_camera_layout:
                //设置照相机的点击事件
                CacheDataHelper.addNullArgumentsMethod();
                Intent intent = new Intent(WardrobeCalendarDetailActivity.this, LunchCameraActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("calendar_detail", "calendar_detail");
//                Bundle bundle = new Bundle();
//                bundle.putInt("newSelectDay", mSelectDay);
//                bundle.putInt("newSelectYear", mSelectYear);
//                bundle.putInt("newSelectMonth", mSelectMonth);
//                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (ActivityColection.isContains(this)){
//                ActivityColection.removeActivity(this);
//                jumpToWardrobeCalendar();
//            }
            DealReturnLogicUtils.dealReturnLogic(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void jumpToWardrobeCalendar() {
        Intent intent = new Intent(WardrobeCalendarDetailActivity.this, WardrobeCalendarActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
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
            case 0:
                mPublishArginFragment = new CalendarDetailPublishArginFragment();
                transaction.replace(R.id.calendar_detail_fragment, mPublishArginFragment);
                break;
            case 1:
                mTodaySelfFragment = new CalendarDetailTodaySelfFragment();
                transaction.replace(R.id.calendar_detail_fragment, mTodaySelfFragment);
                break;
            case 2:
                mOtherPersonFragment = new CalendarDetailOtherPersonFragment();
                transaction.replace(R.id.calendar_detail_fragment, mOtherPersonFragment);
                break;
            default:
                mPublishArginFragment = new CalendarDetailPublishArginFragment();
                transaction.replace(R.id.calendar_detail_fragment, mPublishArginFragment);
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
        if (mPublishArginFragment != null) {
            transaction.remove(mPublishArginFragment);
        }
        if (mTodaySelfFragment != null) {
            transaction.remove(mTodaySelfFragment);
        }
        if (mOtherPersonFragment != null) {
            transaction.remove(mOtherPersonFragment);
        }
    }

}

