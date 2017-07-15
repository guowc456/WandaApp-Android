package com.wanta.mobile.wantaproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.utils.Constants;

/**
 * Created by WangYongqiang on 2016/10/22.
 */
public class ItemSelfMessageFragment extends Fragment {

    private View mView_selflog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView_selflog = inflater.inflate(R.layout.item_self_message_layout,container,false);
        return mView_selflog;
    }
}
