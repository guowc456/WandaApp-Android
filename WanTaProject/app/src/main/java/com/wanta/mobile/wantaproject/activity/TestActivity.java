package com.wanta.mobile.wantaproject.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.utils.Constants;

/**
 * Created by WangYongqiang on 2016/12/14.
 */
public class TestActivity extends Activity{

    private ImageView mTest_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mTest_image = (ImageView) this.findViewById(R.id.test_image);
        mTest_image.setImageBitmap(Constants.modify_bitmap_list.get(0));
    }
}
