package com.wanta.mobile.wantaproject.utils;


import android.support.v4.app.Fragment;

import com.wanta.mobile.wantaproject.fragment.EuropeFragment;
import com.wanta.mobile.wantaproject.fragment.GoddessFragment;
import com.wanta.mobile.wantaproject.fragment.ItemFragment1;
import com.wanta.mobile.wantaproject.fragment.ItemFragment2;
import com.wanta.mobile.wantaproject.fragment.ItemFragment3;
import com.wanta.mobile.wantaproject.fragment.ItemFragment4;
import com.wanta.mobile.wantaproject.fragment.ItemFragment5;
import com.wanta.mobile.wantaproject.fragment.ItemFragment6;
import com.wanta.mobile.wantaproject.fragment.ItemMostpopularSameStyleMatchFragment;
import com.wanta.mobile.wantaproject.fragment.ItemMostpopularSameStyleSingleFragment;
import com.wanta.mobile.wantaproject.fragment.ItemSelfLogFragment;
import com.wanta.mobile.wantaproject.fragment.ItemSelfMessageFragment;
import com.wanta.mobile.wantaproject.fragment.JapaneseFragment;
import com.wanta.mobile.wantaproject.fragment.MostPopularFragment;
import com.wanta.mobile.wantaproject.fragment.NewestFragment;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class ShowFragment {
    public static Fragment getCommunityFragment(int pos){
        Fragment fragment = null;
        switch (pos){
            case 0:
                fragment = new MostPopularFragment();//最美的
                break;
            case 1:
                fragment = new NewestFragment();
                break;
            case 2:
                fragment = new EuropeFragment();
                break;
            case 3:
                fragment = new JapaneseFragment();
                break;
            case 4:
                fragment = new GoddessFragment();
                break;
            default:
                break;
        }
        return fragment;
    }
    //我的里面的fragment
    public static Fragment getSelfFragment(int pos){
        Fragment fragment = null;
        switch (pos){
            case 0:
                fragment = new ItemSelfLogFragment();
                break;
            case 1:
                fragment = new ItemSelfMessageFragment();
                break;
            default:
                break;
        }
        return fragment;
    }
    //首页二级里面的viewpager
    public static Fragment getItemMostpopularSyleFragment(int pos){
        Fragment fragment = null;
        switch (pos){
            case 0:
                fragment = new ItemMostpopularSameStyleMatchFragment();//同风格搭配
                break;
            case 1:
                fragment = new ItemMostpopularSameStyleSingleFragment();//同风格单品
                break;
            default:
                break;
        }
        return fragment;
    }
    //

    public static Fragment getItemFragment(int pos){
        Fragment fragment = null;
        switch (pos){
            case 0:
                fragment = new ItemFragment1();
                break;
            case 1:
                fragment = new ItemFragment2();
                break;
            case 2:
                fragment = new ItemFragment3();
                break;
            case 3:
                fragment = new ItemFragment4();
                break;
            case 4:
                fragment = new ItemFragment5();
                break;
            case 5:
                fragment = new ItemFragment6();
                break;
            default:
                break;
        }
        return fragment;
    }
}
