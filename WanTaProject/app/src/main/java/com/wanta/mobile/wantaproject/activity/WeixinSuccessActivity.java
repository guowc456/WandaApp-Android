package com.wanta.mobile.wantaproject.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.fragment.successLoginFragment;


public class WeixinSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weixin_login);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.sample_content_fragment);
        if (fragment == null) {
            fragment = new successLoginFragment();
            fm.beginTransaction()
                    .add(R.id.sample_content_fragment, fragment)
                    .commit();
        }
    }
}
