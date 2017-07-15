package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.PersonMessageSerializable;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.ToastUtil;

/**
 * Created by WangYongqiang on 2016/11/25.
 */
public class ItemPersonDesigenMessageActivity extends BaseActivity implements View.OnClickListener {

    private MyImageView mItem_person_design_message_back;
    private TextView mItem_person_design_message_title;
    private TextView mItem_person_design_message_ok;
    private TextView mItem_person_design_message_waring;
    private EditText mItem_person_design_message_input;
    private int mClick_position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_person_design_message_layout);
        ActivityColection.addActivity(this);
        mClick_position = getIntent().getExtras().getInt("click_position");
        initId();
    }

    private void initId() {
        mItem_person_design_message_back = (MyImageView) this.findViewById(R.id.item_person_design_message_back);
        mItem_person_design_message_back.setSize(Constants.PHONE_WIDTH/17,Constants.PHONE_WIDTH/17);
        mItem_person_design_message_back.setOnClickListener(this);
        mItem_person_design_message_title = (TextView) this.findViewById(R.id.item_person_design_message_title);
//        mItem_person_design_message_title.setTextSize(Constants.PHONE_WIDTH/50);
        mItem_person_design_message_ok = (TextView) this.findViewById(R.id.item_person_design_message_ok);
//        mItem_person_design_message_ok.setTextSize(Constants.PHONE_WIDTH/50);
        mItem_person_design_message_ok.setOnClickListener(this);
        mItem_person_design_message_waring = (TextView) this.findViewById(R.id.item_person_design_message_waring);
//        mItem_person_design_message_waring.setTextSize(Constants.PHONE_WIDTH/55);
        mItem_person_design_message_input = (EditText) this.findViewById(R.id.item_person_design_message_input);
        if (mClick_position==0){
            mItem_person_design_message_input.setLines(1);
            mItem_person_design_message_waring.setText("使用中英文，下划线和数字，昵称一个月只能申请修改一次");
        }else if (mClick_position==5){
            mItem_person_design_message_input.setMinLines(6);
            mItem_person_design_message_input.setSelection(0);
            mItem_person_design_message_waring.setText("有趣的个人介绍能够吸引更多的粉丝哟");
        }
//        mItem_person_design_message_input.setTextSize(Constants.PHONE_WIDTH/50);
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.item_person_design_message_ok:
              if (TextUtils.isEmpty(mItem_person_design_message_input.getText())){
                  ToastUtil.showShort(this,"输入的信息不能为空");
              }else {
                  Intent intent = new Intent(ItemPersonDesigenMessageActivity.this,NewPersonDesignActivity.class);
                  intent.putExtra("input_message",new PersonMessageSerializable(mItem_person_design_message_input.getText().toString()));
                  setResult(RESULT_OK,intent);
                  finish();
              }
              break;
          case R.id.item_person_design_message_back:
              Intent intent = new Intent(ItemPersonDesigenMessageActivity.this,NewPersonDesignActivity.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(intent);
              finish();
              break;
          default:
              break;
      }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (ActivityColection.isContains(this)){
                ActivityColection.removeActivity(this);
                Intent intent = new Intent(ItemPersonDesigenMessageActivity.this,NewPersonDesignActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
