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
public class CalendarDetailOtherPersonFragment extends Fragment{

    private View mFragment_camera_detail_other_person;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment_camera_detail_other_person = inflater.inflate(R.layout.fragment_camera_detail_other_person,container,false);
        return mFragment_camera_detail_other_person;
    }
}
