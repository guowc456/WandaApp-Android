package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;

/**
 * Created by Administrator on 2017/3/23.
 */

public class CeLiangBiaoZhunActivity extends BaseActivity {

    private CustomSimpleDraweeView celiang_biaozhun_icon;
    private LinearLayout celiang_biaozhun_back_layout;
    private MyImageView celiang_biaozhun_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celiang_biaozhun_layout);
        ActivityColection.addActivity(this);
        initId();
    }

    private void initId() {
        celiang_biaozhun_icon = (CustomSimpleDraweeView) this.findViewById(R.id.celiang_biaozhun_icon);
        celiang_biaozhun_icon.setHeight(Constants.PHONE_HEIGHT-Constants.PHONE_HEIGHT/10);
        celiang_biaozhun_icon.setWidth(Constants.PHONE_WIDTH);
        celiang_biaozhun_back_layout = (LinearLayout) this.findViewById(R.id.celiang_biaozhun_back_layout);
        celiang_biaozhun_back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToNewPersonDesign();
            }
        });
        celiang_biaozhun_back = (MyImageView) this.findViewById(R.id.celiang_biaozhun_back);
        celiang_biaozhun_back.setSize(Constants.PHONE_WIDTH/16,Constants.PHONE_WIDTH/16);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (ActivityColection.isContains(this)){
                ActivityColection.removeActivity(this);
                jumpToNewPersonDesign();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void jumpToNewPersonDesign(){
        Intent intent = new Intent(CeLiangBiaoZhunActivity.this,NewPersonDesignActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
