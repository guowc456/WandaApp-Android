package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.calendar.CalendarDateUtils;
import com.wanta.mobile.wantaproject.calendar.MonthDateView;
import com.wanta.mobile.wantaproject.calendar.WeekDayView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.CacheDataHelper;
import com.wanta.mobile.wantaproject.domain.CacheDataUtils;
import com.wanta.mobile.wantaproject.fragment.WardrobeFragment;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.DealReturnLogicUtils;
import com.wanta.mobile.wantaproject.utils.InterpretView;
import com.wanta.mobile.wantaproject.utils.Interpretor;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/11/15.
 */
public class WardrobeCalendarActivity extends BaseActivity {

    private MyImageView iv_left;
    private MyImageView iv_right;
    private MonthDateView monthDateView;
    private TextView tv_today;
    private WeekDayView weekDayView;
    private MyImageView pre_arrows;
    private LinearLayout pre_arrows_layout;
    private MyImageView share_icon;
    private MyImageView heart_icon;
    private MyImageView value_icon;
    private TextView tv_date;
    private TextView tv_week;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardrobe_calendar);
        initId();
        //设置周一到周日的颜色
        weekDayView.setmWeedayColor(R.color.black);
        weekDayView.setmWeekendColor(R.color.black);
        weekDayView.setmWeekSize(12);
        monthDateView.setmCircleRadius(35);
        monthDateView.setmDaySize(15);
        initNetMessage();
        pre_arrows.setSize(Constants.PHONE_WIDTH / 12, Constants.PHONE_WIDTH / 12);
        share_icon.setSize(Constants.PHONE_WIDTH / 12, Constants.PHONE_WIDTH / 12);
        heart_icon.setSize(Constants.PHONE_WIDTH / 12, Constants.PHONE_WIDTH / 12);
        value_icon.setSize(Constants.PHONE_WIDTH / 12, Constants.PHONE_WIDTH / 12);
        iv_left.setSize(Constants.PHONE_WIDTH / 17, Constants.PHONE_WIDTH / 17);
        iv_right.setSize(Constants.PHONE_WIDTH / 17, Constants.PHONE_WIDTH / 17);
        monthDateView.setDateClick(new MonthDateView.DateClick() {

            @Override
            public void onClickOnDate() {
//                Toast.makeText(getApplication(), "点击了：" + monthDateView.getmSelDay(), Toast.LENGTH_SHORT).show();
                CacheDataHelper.addNullArgumentsMethod();
                ActivityColection.addActivity(WardrobeCalendarActivity.this);
                CacheDataUtils.setSelectYearMonthDay(monthDateView.getmSelYear(), monthDateView.getmSelMonth(), monthDateView.getmSelDay());
                Intent intent = new Intent(WardrobeCalendarActivity.this, WardrobeCalendarDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        setOnlistener();
    }

    private void initNetMessage() {
        //获取当前的时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);

        Calendar calendar = Calendar.getInstance();
        Constants.wardar_calendar_year = calendar.get(Calendar.YEAR);
        Constants.wardar_calendar_month = calendar.get(Calendar.MONTH)+1;
        Constants.wardar_calendar_day = calendar.get(Calendar.DAY_OF_MONTH);

        MyHttpUtils.getNetMessage(this, "http://1zou.me/apisq/loadIconInMonth/uid/"+Constants.USER_ID+"/ymd/" + str, new MyHttpUtils.Callback() {
            @Override
            public void getResponseMsg(String response) {
                //解析数据
                parseResponseMsg(response);
            }
        }, new MyHttpUtils.CallbackError() {
            @Override
            public void getResponseMsg(String error) {

            }
        });
    }

    private void parseResponseMsg(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getInt("errorCode") == 0) {
                if (!"null".equals(jsonObject.getString("datas"))) {
                    JSONObject datas = new JSONObject(jsonObject.getString("datas"));
                    Iterator<String> keys = datas.keys();
                    List<Integer> list = new ArrayList<Integer>();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        list.add(Integer.parseInt(key));
                    }
                    monthDateView.setDaysHasThingList(list);
                    monthDateView.setTextView(tv_date, tv_week);
                } else {
                    monthDateView.setDaysHasThingList(null);
                    monthDateView.setTextView(tv_date, tv_week);
                    LogUtils.showVerbose("WardrobeCalendarActivity", "当前时间没有拍照上传");
                }
            }

        } catch (JSONException e) {
            LogUtils.showVerbose("WardrobeCalendarActivity", "数据解析错误");
        }
    }

    private void initId() {
        iv_left = (MyImageView) this.findViewById(R.id.iv_left);
        iv_right = (MyImageView) this.findViewById(R.id.iv_right);
        monthDateView = (MonthDateView) this.findViewById(R.id.monthDateView);
        tv_date = (TextView) this.findViewById(R.id.date_text);
        tv_week = (TextView) this.findViewById(R.id.week_text);
        tv_today = (TextView) this.findViewById(R.id.tv_today);
        weekDayView = (WeekDayView) this.findViewById(R.id.weekDayView);
        pre_arrows = (MyImageView) this.findViewById(R.id.pre_arrows);
        pre_arrows_layout = (LinearLayout) this.findViewById(R.id.pre_arrows_layout);
        share_icon = (MyImageView) this.findViewById(R.id.share_icon);
        heart_icon = (MyImageView) this.findViewById(R.id.heart_icon);
        value_icon = (MyImageView) this.findViewById(R.id.value_icon);
    }

    private void setOnlistener() {
        pre_arrows_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(WardrobeCalendarActivity.this, MainActivity.class);
//                intent.putExtra("calendar_activity", "calendar_activity");
//                startActivity(intent);
//                finish();
                DealReturnLogicUtils.dealReturnLogic(WardrobeCalendarActivity.this);
            }
        });
        iv_left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                turnLeftLogic();
                monthDateView.onLeftClick();
            }
        });

        iv_right.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                turnRightLogic();
                monthDateView.onRightClick();
            }
        });

        tv_today.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                monthDateView.setTodayToView();
            }
        });
    }

    private void turnLeftLogic() {
        int year = Constants.wardar_calendar_year;
        int month = Constants.wardar_calendar_month;
        int day = Constants.wardar_calendar_day;
        if(month == 0){//若果是1月份，则变成12月份
            year = year-1;
            month = 11;
        }else if(CalendarDateUtils.getMonthDays(year, month) == day){
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month-1;
            day = CalendarDateUtils.getMonthDays(year, month);
        }else{
            month = month-1;
        }
        String str = year+"";
        if (month<10){
            str = str+"0"+month;
        }else {
            str = str +month;
        }
        if (day<10){
            str = str+"0"+day;
        }else {
            str = str+day;
        }
        Constants.wardar_calendar_year = year;
        Constants.wardar_calendar_month = month;
        Constants.wardar_calendar_day = day;
        MyHttpUtils.getNetMessage(this, "http://1zou.me/apisq/loadIconInMonth/uid/"+Constants.USER_ID+"/ymd/" + str, new MyHttpUtils.Callback() {
            @Override
            public void getResponseMsg(String response) {
                //解析数据
                parseResponseMsg(response);
            }
        }, new MyHttpUtils.CallbackError() {
            @Override
            public void getResponseMsg(String error) {

            }
        });
    }

    private void turnRightLogic() {
        int year = Constants.wardar_calendar_year;
        int month = Constants.wardar_calendar_month;
        int day = Constants.wardar_calendar_day;
        if(month == 11){//若果是12月份，则变成1月份
            year = year+1;
            month = 0;
        }else if(CalendarDateUtils.getMonthDays(year, month) == day){
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month + 1;
            day = CalendarDateUtils.getMonthDays(year, month);
        }else{
            month = month + 1;
        }
        String str = year+"";
        if (month<10){
            str = str+"0"+month;
        }else {
            str = str +month;
        }
        if (day<10){
            str = str+"0"+day;
        }else {
            str = str+day;
        }
        Constants.wardar_calendar_year = year;
        Constants.wardar_calendar_month = month;
        Constants.wardar_calendar_day = day;
        MyHttpUtils.getNetMessage(this, "http://1zou.me/apisq/loadIconInMonth/uid/"+Constants.USER_ID+"/ymd/" + str, new MyHttpUtils.Callback() {
            @Override
            public void getResponseMsg(String response) {
                //解析数据
                parseResponseMsg(response);
            }
        }, new MyHttpUtils.CallbackError() {
            @Override
            public void getResponseMsg(String error) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (ActivityColection.isContains(this)) {
//                ActivityColection.removeActivity(this);
//                Intent intent = new Intent(WardrobeCalendarActivity.this, MainActivity.class);
//                intent.putExtra("calendar_activity", "calendar_activity");
//                startActivity(intent);
//                finish();

//            }
            DealReturnLogicUtils.dealReturnLogic(this);
        }
        return super.onKeyDown(keyCode, event);
    }
}

