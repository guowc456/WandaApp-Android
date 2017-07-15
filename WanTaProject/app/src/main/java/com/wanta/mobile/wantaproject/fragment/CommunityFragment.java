package com.wanta.mobile.wantaproject.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.ShowFragment;
import com.wanta.mobile.wantaproject.utils.ToastUtil;

/**
 * Created by WangYongqiang on 2016/10/16.
 */
public class CommunityFragment extends Fragment implements View.OnClickListener {

    private View mView_community;
    private TabLayout mCommunity_tab_layout;
    private ViewPager mCommunity_viewpager;
    private String[] mHeadTabs;
    private FragmentPagerAdapter mAdapter;
    private int count = 0;
    private ImageView mCommunity_tab_image;
    private LinearLayout community_head_title;
    private MyImageView mCommunity_tab_more_image;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView_community = inflater.inflate(R.layout.fragment_commnity,container,false);
        init();
        setData();
        LogUtils.showVerbose("CommunityFragment","onCreateView");
        return mView_community;
    }

//    @Override
//    public void onAttach(Activity activity) {
//        community_head_title = (LinearLayout) activity.findViewById(R.id.head_title);
//        community_head_title.setVisibility(View.VISIBLE);
//        super.onAttach(activity);
//    }
    private void init() {
        mCommunity_tab_layout = (TabLayout) mView_community.findViewById(R.id.community_tab_layout);
        mCommunity_viewpager = (ViewPager) mView_community.findViewById(R.id.community_viewpager);
        mCommunity_tab_more_image = (MyImageView) mView_community.findViewById(R.id.community_tab_more_image);
        mCommunity_tab_more_image.setSize(Constants.PHONE_WIDTH/12,Constants.PHONE_WIDTH/12);
        mCommunity_tab_more_image.setOnClickListener(this);
        mHeadTabs = getActivity().getResources().getStringArray(R.array.headtabs);
    }

    private void setData() {
        //getFragmentManager到的是activity对所包含fragment的Manager，而如果是fragment嵌套fragment，那么就需要利用getChildFragmentManager()了。
        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                LogUtils.showVerbose("CommunityFragment","position="+position);
                return ShowFragment.getCommunityFragment(position);
            }

            @Override
            public int getCount() {
                return mHeadTabs.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mHeadTabs[position];
            }

        };
        mCommunity_viewpager.setAdapter(mAdapter);
        mCommunity_tab_layout.setupWithViewPager(mCommunity_viewpager);
        mCommunity_tab_layout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tablayout_select_color));
        mCommunity_tab_layout.setTabTextColors(getResources().getColor(R.color.tablayout_normal_color),
                getResources().getColor(R.color.tablayout_select_color));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.community_tab_more_image:
//                ToastUtil.showShort(getActivity(),"更多");
                break;
            default:
                break;
        }
    }
}
