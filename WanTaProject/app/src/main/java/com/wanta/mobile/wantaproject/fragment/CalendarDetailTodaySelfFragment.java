package com.wanta.mobile.wantaproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanta.mobile.wantaproject.R;

/**
 * Created by WangYongqiang on 2016/10/16.
 */
public class CalendarDetailTodaySelfFragment extends Fragment{

    private View mFragment_camera_detail_today_self_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment_camera_detail_today_self_view = inflater.inflate(R.layout.fragment_camera_detail_today_self,container,false);
        return mFragment_camera_detail_today_self_view;
    }
}
