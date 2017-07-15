package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.KeyboardListenRelativeLayout;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;

/**
 * Created by Administrator on 2017/3/29.
 */

public class OtherAuthorInputDescriptionActivity extends BaseActivity implements View.OnClickListener {

    private MyImageView other_author_input_description_back_icon;
    private EditText other_author_input_description_msg_tv;
    private KeyboardListenRelativeLayout other_author_input_description_keyboardlistener;
    private LinearLayout other_author_input_description_ok;
    private int currentPosition;
    private int image_publish_edit;
    private LinearLayout other_author_input_description_back_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_author_input_description_layout);
        ActivityColection.addActivity(this);
        Bundle extras = getIntent().getExtras();
        currentPosition = extras.getInt("currentPosition");
        image_publish_edit = extras.getInt("image_publish_edit");
        initId();
    }

    private void initId() {
        other_author_input_description_back_icon = (MyImageView) this.findViewById(R.id.other_author_input_description_back_icon);
        other_author_input_description_back_icon.setSize(Constants.PHONE_WIDTH/10,Constants.PHONE_WIDTH/10);
        other_author_input_description_msg_tv = (EditText) this.findViewById(R.id.other_author_input_description_msg_tv);
        other_author_input_description_ok = (LinearLayout) this.findViewById(R.id.other_author_input_description_ok);
        other_author_input_description_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(other_author_input_description_msg_tv.getText())){
                    Toast.makeText(OtherAuthorInputDescriptionActivity.this, "描述信息不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    jumpToCameraActivity();
                }
            }
        });
        other_author_input_description_keyboardlistener = (KeyboardListenRelativeLayout) this.findViewById(R.id.other_author_input_description_keyboardlistener);
        other_author_input_description_keyboardlistener.setOnKeyboardStateChangedListener(new KeyboardListenRelativeLayout.IOnKeyboardStateChangedListener() {
            @Override
            public void onKeyboardStateChanged(int state) {
                switch (state) {
                    case KeyboardListenRelativeLayout.KEYBOARD_STATE_HIDE://软键盘隐藏

                        break;
                    case KeyboardListenRelativeLayout.KEYBOARD_STATE_SHOW://软键盘显示
                        other_author_input_description_msg_tv.requestFocus();
                        break;
                    default:
                        break;
                }
            }
        });

        other_author_input_description_back_layout = (LinearLayout) this.findViewById(R.id.other_author_input_description_back_layout);
        other_author_input_description_back_layout.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (ActivityColection.isContains(this)){
                ActivityColection.removeActivity(this);
                jumpToCameraActivity();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void jumpToCameraActivity(){
        Intent intent = new Intent(OtherAuthorInputDescriptionActivity.this,CameraActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("currentPosition", currentPosition);
        intent.putExtra("jump_flag",1);
        Constants.other_author_pop_window_description = other_author_input_description_msg_tv.getText().toString();
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.other_author_input_description_back_layout:
                jumpToCameraActivity();
                break;
        }
    }
}
