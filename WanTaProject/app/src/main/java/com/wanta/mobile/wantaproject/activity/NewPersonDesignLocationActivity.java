package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.NewPersonDesignLocationRecycleViewAdapter;
import com.wanta.mobile.wantaproject.adapter.PersonDesignLocationRecycleViewAdapter;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.customview.QucklyLocationBar;
import com.wanta.mobile.wantaproject.domain.PersonMessageSerializable;
import com.wanta.mobile.wantaproject.mordel.DomesticDbHelper;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by WangYongqiang on 2017/3/18.
 */
public class NewPersonDesignLocationActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private List<TextView> text_content = new ArrayList<>();//记录当前选中的地址
    private NewPersonDesignLocationRecycleViewAdapter mSequence_adapter;
    private List<Integer> letterData = new ArrayList<>();
    private QucklyLocationBar mLocation_quick_bar;
    private LinearLayout mLocation_head_layout;
    private PopupWindow set_pop;
    private GridLayoutManager gridLayoutManager;
    private int currentPosition;
    private MyImageView mPerson_design_location_back;
    private TextView mPerson_design_location_ok;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_person_design_location_layout);
        initId();
    }

    private void initId() {
        //遍历出显示标题栏的位置
        for (int i = 0; i < Constants.all_location_msg.size(); i++) {
            if (judgeContainsStr(Constants.all_location_msg.get(i)) == true || Constants.all_location_msg.get(i).equals("当前")
                    || Constants.all_location_msg.get(i).equals("历史") || Constants.all_location_msg.get(i).equals("热门")) {
                letterData.add(i);
            }
        }
        mPerson_design_location_back = (MyImageView) this.findViewById(R.id.person_design_location_back);
        mPerson_design_location_back.setSize(Constants.PHONE_WIDTH / 18, Constants.PHONE_WIDTH / 18);
        mPerson_design_location_back.setOnClickListener(this);
        mPerson_design_location_ok = (TextView) this.findViewById(R.id.person_design_location_ok);
        mPerson_design_location_ok.setOnClickListener(this);
        mRecyclerView = (RecyclerView) this.findViewById(R.id.new_person_design_location_recycle);
        gridLayoutManager = new GridLayoutManager(this, 3);
        //动态的改变GridLayoutManager显示的列数
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mSequence_adapter = new NewPersonDesignLocationRecycleViewAdapter(this, Constants.all_location_msg, letterData);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mSequence_adapter);
        mSequence_adapter.setOnPersonDesignLocationItemListener(new NewPersonDesignLocationRecycleViewAdapter.OnPersonDesignLocationItemListener() {
            @Override
            public void setItemClick(View view) {
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

        //设置快速定位条
        mLocation_quick_bar = (QucklyLocationBar) this.findViewById(R.id.location_quick_bar);
        mLocation_head_layout = (LinearLayout) this.findViewById(R.id.location_head_layout);
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
////                LogUtils.showVerbose("PersonDesignLocationActivity", "currentString=" + currentStr + "  isup" + isup);
            }
        });
    }

    /**
     * 该方法主要使用正则表达式来判断字符串中是否包含字母
     *
     * @param cardNum 待检验的原始卡号
     * @return 返回是否包含
     */
    public boolean judgeContainsStr(String cardNum) {
        String regex = ".*[A-Z]+.*";
        Matcher m = Pattern.compile(regex).matcher(cardNum);
        return m.matches();
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Constants.isChange) {
                Bundle data = msg.getData();
                String msg_currentStr = (String) data.get("currentStr");
                boolean msg_isup = data.getBoolean("isup");
                //弹出选择的对话框
                showChooseDialog(msg_currentStr, msg_isup);
                int firstItem = gridLayoutManager.findFirstVisibleItemPosition();
                int lastItem = gridLayoutManager.findLastVisibleItemPosition();
                final int currentScrollowPosition = getCurrentScrollowPosition(msg_currentStr);
                LogUtils.showVerbose("NewPersonDesignLocationActivity", "当前的垂直位置=" + currentScrollowPosition);
                //然后区分情况
                if (currentScrollowPosition <= firstItem) {
                    //当要置顶的项在当前显示的第一个项的前面时
                    mRecyclerView.scrollToPosition(currentScrollowPosition);
//                    mRecyclerView.setVerticalScrollbarPosition(currentScrollowPosition);
                } else if (currentScrollowPosition <= lastItem) {
                    //当要置顶的项已经在屏幕上显示时
                    int top = mRecyclerView.getChildAt(currentScrollowPosition - firstItem).getTop();
                    mRecyclerView.scrollBy(0, top);
                } else {
                    //当要置顶的项在当前显示的最后一项的后面时
                    mRecyclerView.scrollToPosition(currentScrollowPosition);
//                    mRecyclerView.setVerticalScrollbarPosition(currentScrollowPosition);
                }
            }

        }
    };

    private void showChooseDialog(String currentStr, boolean isup) {
        if (set_pop == null) {
            View contentView = LayoutInflater.from(NewPersonDesignLocationActivity.this).inflate(R.layout.pop_window_location, null);
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

    //获取当前字母的位子
    public int getCurrentScrollowPosition(String letterString) {
        if (letterString.equals("当前") || letterString.equals("历史") || letterString.equals("热门") || letterString.equals("A")) {
            currentPosition= 0;
        } else if (letterString.equals("B")) {
            currentPosition = letterData.get(4);
        } else if (letterString.equals("C")) {
            currentPosition = letterData.get(5);
        }else if (letterString.equals("D")) {
            currentPosition = letterData.get(6);
        }else if (letterString.equals("E")) {
            currentPosition = letterData.get(7);
        }else if (letterString.equals("F")) {
            currentPosition = letterData.get(8);
        }else if (letterString.equals("G")) {
            currentPosition = letterData.get(9);
        }else if (letterString.equals("H")) {
            currentPosition = letterData.get(10);
        }else if (letterString.equals("J")) {
            currentPosition = letterData.get(11);
        }else if (letterString.equals("K")) {
            currentPosition = letterData.get(12);
        }else if (letterString.equals("L")) {
            currentPosition = letterData.get(13);
        }else if (letterString.equals("M")) {
            currentPosition = letterData.get(14);
        }else if (letterString.equals("N")) {
            currentPosition = letterData.get(15);
        }else if (letterString.equals("P")) {
            currentPosition = letterData.get(16);
        }else if (letterString.equals("Q")) {
            currentPosition = letterData.get(17);
        }else if (letterString.equals("R")) {
            currentPosition = letterData.get(18);
        }else if (letterString.equals("S")) {
            currentPosition = letterData.get(19);
        }else if (letterString.equals("T")) {
            currentPosition = letterData.get(20);
        }else if (letterString.equals("W")) {
            currentPosition = letterData.get(21);
        }else if (letterString.equals("X")) {
            currentPosition = letterData.get(22);
        }else if (letterString.equals("Y")) {
            currentPosition = letterData.get(23);
        }else if (letterString.equals("Z")) {
            currentPosition = letterData.get(24);
        }
        return currentPosition;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.person_design_location_back:
                jumpPersonDesignActivity();
                break;
            case R.id.person_design_location_ok:
                if (text_content.size() == 0) {
                    Toast.makeText(NewPersonDesignLocationActivity.this, "亲，居住地还没有选择呢？", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(NewPersonDesignLocationActivity.this, NewPersonDesignActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("person_design_location", new PersonMessageSerializable(Constants.currentLocation));
        setResult(RESULT_OK, intent);
        finish();
    }
}
