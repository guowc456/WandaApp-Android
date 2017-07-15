package com.wanta.mobile.wantaproject.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.WardrobeCalendarActivity;
import com.wanta.mobile.wantaproject.activity.WardrobeDetailActivity;
import com.wanta.mobile.wantaproject.adapter.ExpandAdapter;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.FirstItem;
import com.wanta.mobile.wantaproject.domain.SecondItem;
import com.wanta.mobile.wantaproject.domain.ThirdItem;
import com.wanta.mobile.wantaproject.uploadimage.FileUtils;
import com.wanta.mobile.wantaproject.uploadimage.ImagesSelectorActivity;
import com.wanta.mobile.wantaproject.uploadimage.SelectorSettings;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.SaveMsgUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/16.
 */
public class WardrobeFragment extends Fragment {

    private LinearLayout wardrober_head_title;
    private View mView_wardrobe;
    private ExpandableListView mExpandableListView;

    public int[] cloth_icon = { R.mipmap.almirah_upper,R.mipmap.almirah_trousers,
            R.mipmap.almirah_skirt,R.mipmap.almirah_hat,R.mipmap.almirah_shoes,
            R.mipmap.almirah_scarf,R.mipmap.almirah_belt,R.mipmap.almirah_bag};
    private List<FirstItem> firstList;
    private ExpandAdapter eAdpater;
    private LinearLayout mCalendar_layout;
    private MyImageView mCalendar_icon;
    private TextView mCalendar_title;
    private PopupWindow set_pop;
    private LinearLayout mWardrobe_head_layout;
    private MyImageView mWardrobe_pop_window_delete_img;
    private ImageView mWardrobe_pop_window_img;
    private MyImageView mPopwindow_select_icon1;
    private MyImageView mPopwindow_select_icon2;
    private MyImageView mPopwindow_select_icon3;
    private MyImageView mPopwindow_select_icon4;
    private MyImageView mMWardrobe_pop_window_delete_img1;
    private MyImageView mWardrobe_pop_window_img1;
    private MyImageView mWardrobe_pop_window_img2;
    private MyImageView mWardrobe_pop_window_img3;
    private LinearLayout mPopwindow_layout;
    private PopupWindow set_pop_window;
    private TextView mPopwindow_select_photo;
    private TextView mPopwindow_select_pictures;
    private TextView mPopwindow_select_cancel;
    private ArrayList<String> mResults = new ArrayList<>();
    private PopupWindow init_set_pop;
    private MyImageView mPopwindow_init_icon1;
    private MyImageView mWardrobe_pop_window_init_delete_img;
    private MyImageView mPopwindow_init_select_icon1;
    private MyImageView mPopwindow_init_select_icon2;
    private MyImageView mPopwindow_init_select_icon3;
    private MyImageView mPopwindow_init_select_icon4;
    private MyImageView mPopwindow_init_icon2;
    private TextView mPopwindow_init_tv1;
    private TextView mPopwindow_init_tv2;
    private TextView mPopwindow_init_tv3;
    private TextView mPopwindow_init_tv4;
    private TextView mPopwindow_init_tv5;
    private TextView mPopwindow_init_tv6;
    private LinearLayout mWardrobe_pop_window_delete_img_layout;
    private LinearLayout mWardrobe_pop_window_delete_img1_layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView_wardrobe = inflater.inflate(R.layout.fragment_wardrobe,container,false);
        init();
        initData();
        mHandler.sendEmptyMessageDelayed(1,100);//不加延时，就会出现父布局没有加载完，就会加载popwindow的错误
        return mView_wardrobe;
    }

    @Override
    public void onAttach(Activity activity) {
        wardrober_head_title = (LinearLayout) activity.findViewById(R.id.head_title);
        wardrober_head_title.setVisibility(View.GONE);
        super.onAttach(activity);
    }
//    @Override
//    public void onDetach() {
//        wardrober_head_title.setVisibility(View.GONE);
//        super.onDetach();
//    }
    private void init() {
        mExpandableListView = (ExpandableListView) mView_wardrobe.findViewById(R.id.wardrobe_expandableListView);
        firstList = new ArrayList<FirstItem>();
        mCalendar_layout = (LinearLayout) mView_wardrobe.findViewById(R.id.calendar_layout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH,Constants.PHONE_WIDTH/7);
        mCalendar_layout.setLayoutParams(layoutParams);
        mCalendar_icon = (MyImageView) mView_wardrobe.findViewById(R.id.calendar_icon);
        mCalendar_title = (TextView) mView_wardrobe.findViewById(R.id.calendar_title);
        mCalendar_title.setTextSize(18);
        mWardrobe_head_layout = (LinearLayout) mView_wardrobe.findViewById(R.id.wardrobe_head_layout);
        mCalendar_icon.setSize(Constants.PHONE_WIDTH/7,Constants.PHONE_WIDTH/7);
        //设置日历的点击事件
        mCalendar_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WardrobeCalendarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(intent);
            }
        });
    }

    private void initPopwindows() {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_windows_init_layout, null);
        init_set_pop = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);

        init_set_pop.setBackgroundDrawable(new BitmapDrawable());
        init_set_pop.setOutsideTouchable(true);
        init_set_pop.setFocusable(true);
        init_set_pop.setTouchable(true);
        //	set_pop.setAnimationStyle(R.style.Anim_popWindow_in);
        //set_pop.showAtLocation(frame,Gravity.TOP, 0, 0);
        init_set_pop.showAsDropDown(mWardrobe_head_layout);
        View my_init_pop = init_set_pop.getContentView();
        mPopwindow_init_icon1 = (MyImageView) my_init_pop.findViewById(R.id.popwindow_init_icon1);
        mPopwindow_init_icon1.setSize(Constants.PHONE_WIDTH/8,Constants.PHONE_WIDTH/8);
        mPopwindow_init_icon2 = (MyImageView) my_init_pop.findViewById(R.id.popwindow_init_icon2);
        mPopwindow_init_icon2.setSize(Constants.PHONE_WIDTH/8,Constants.PHONE_WIDTH/8);
        mWardrobe_pop_window_init_delete_img = (MyImageView) my_init_pop.findViewById(R.id.wardrobe_pop_window_init_delete_img);
        mWardrobe_pop_window_init_delete_img.setSize(Constants.PHONE_WIDTH/20,Constants.PHONE_WIDTH/20);
        mPopwindow_init_select_icon1 = (MyImageView) my_init_pop.findViewById(R.id.popwindow_init_select_icon1);
        mPopwindow_init_select_icon2 = (MyImageView) my_init_pop.findViewById(R.id.popwindow_init_select_icon2);
        mPopwindow_init_select_icon3 = (MyImageView) my_init_pop.findViewById(R.id.popwindow_init_select_icon3);
        mPopwindow_init_select_icon4 = (MyImageView) my_init_pop.findViewById(R.id.popwindow_init_select_icon4);
        mPopwindow_init_select_icon1.setSize(Constants.PHONE_WIDTH/24,Constants.PHONE_WIDTH/24);
        mPopwindow_init_select_icon2.setSize(Constants.PHONE_WIDTH/24,Constants.PHONE_WIDTH/24);
        mPopwindow_init_select_icon3.setSize(Constants.PHONE_WIDTH/24,Constants.PHONE_WIDTH/24);
        mPopwindow_init_select_icon4.setSize(Constants.PHONE_WIDTH/24,Constants.PHONE_WIDTH/24);
        mPopwindow_init_tv1 = (TextView) my_init_pop.findViewById(R.id.popwindow_init_tv1);
        mPopwindow_init_tv2 = (TextView) my_init_pop.findViewById(R.id.popwindow_init_tv2);
        mPopwindow_init_tv3 = (TextView) my_init_pop.findViewById(R.id.popwindow_init_tv3);
        mPopwindow_init_tv4 = (TextView) my_init_pop.findViewById(R.id.popwindow_init_tv4);
        mPopwindow_init_tv5 = (TextView) my_init_pop.findViewById(R.id.popwindow_init_tv5);
        mPopwindow_init_tv6 = (TextView) my_init_pop.findViewById(R.id.popwindow_init_tv6);
        mWardrobe_pop_window_init_delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMsgUtils.setCurrentBooleanState(getActivity(),"popwindowState","ispop",true);
                init_set_pop.dismiss();
            }
        });
    }

    private void initData() {
        // TODO Auto-generated method stub
        for (int i = 0; i < Constants.cloth_catogry_chinese.length; i++) {
            FirstItem firstItem = new FirstItem();
            firstItem.setId(i);
            firstItem.setTitle(Constants.cloth_catogry_chinese[i]);
            firstItem.setImage(cloth_icon[i]);
            firstItem.setNumber("您有"+Constants.Cloth_catogry_number[i]+"件");
            List<SecondItem> seList = new ArrayList<SecondItem>();

            SecondItem secondItem = new SecondItem();
            secondItem.setId(i+1);
            secondItem.setTitle("全部" +Constants.cloth_catogry_chinese[i]+"  "+Constants.Cloth_catogry_number[i] + "件");
            seList.add(secondItem);
            List<ThirdItem> thirdList = new ArrayList<ThirdItem>();
            for (int k = 0; k < Constants.cloth_catogry_thrid[i].length; k++) {
                ThirdItem thirdItem = new ThirdItem();
                thirdItem.setId(k);
                thirdItem.setName(Constants.cloth_catogry_thrid[i][k]);
                thirdList.add(thirdItem);
            }
            secondItem.setThirdItems(thirdList);
            firstItem.setSecondItems(seList);
            firstList.add(firstItem);
        }
        eAdpater = new ExpandAdapter(getActivity(), firstList, stvClickEvent);
        mExpandableListView.setAdapter(eAdpater);
        //设置添加的点击事件
        eAdpater.setOnAddClickListener(new ExpandAdapter.OnAddClickListener() {
            @Override
            public void clickAddListener(View view) {
                if (Constants.isWardrobePhotoPopwindow){
                    Constants.isWardrobePhotoPopwindow = false;
                    showPopWindows();
                }else {
                    displaySelectPictures();
                }
            }

        });
        //设置点击条目的事件
        eAdpater.setOnItemClickListener(new ExpandAdapter.OnItemClickListener() {
            @Override
            public void clickItemListener(int firstItem, int secondItem, int thridItem) {
                LogUtils.showVerbose("WardrobeFragment","一级："+firstItem+" 二级："+secondItem+" 三级:"+thridItem);
                Intent intent = new Intent(getActivity(), WardrobeDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("firstItem",firstItem);
                bundle.putInt("thridItem",thridItem);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });
//        eAdpater.setOnFirstItemClickListener(new ExpandAdapter.OnFirstItemClickListener() {
//            @Override
//            public void clickFirstItemListener(int firstItem) {
//                LogUtils.showVerbose("WardrobeFragment","一级位置："+firstItem);
//            }
//        });
    }

    ExpandableListView.OnChildClickListener stvClickEvent = new ExpandableListView.OnChildClickListener() {

        @Override
        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {
            // TODO Auto-generated method stub
//            String msg = "parent_id = " + groupPosition + " child_id = "
//                    + childPosition;
//            Toast.makeText(getActivity(), msg,
//                    Toast.LENGTH_SHORT).show();
            return false;
        }
    };


    private void showPopWindows() {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_windows_layout, null);
        set_pop = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);

        set_pop.setBackgroundDrawable(new BitmapDrawable());
        set_pop.setOutsideTouchable(true);
        set_pop.setFocusable(true);
        set_pop.setTouchable(true);
        //	set_pop.setAnimationStyle(R.style.Anim_popWindow_in);
        //set_pop.showAtLocation(frame,Gravity.TOP, 0, 0);
        set_pop.showAsDropDown(mWardrobe_head_layout);
        View my_pop = set_pop.getContentView();
        mPopwindow_layout = (LinearLayout) my_pop.findViewById(R.id.popwindow_layout);
        mWardrobe_pop_window_delete_img = (MyImageView) my_pop.findViewById(R.id.wardrobe_pop_window_delete_img);
        mWardrobe_pop_window_delete_img.setSize(Constants.PHONE_WIDTH/20,Constants.PHONE_WIDTH/20);
        mWardrobe_pop_window_delete_img_layout = (LinearLayout) my_pop.findViewById(R.id.wardrobe_pop_window_delete_img_layout);
        mWardrobe_pop_window_delete_img1_layout = (LinearLayout) my_pop.findViewById(R.id.wardrobe_pop_window_delete_img1_layout);
        mWardrobe_pop_window_img = (ImageView) my_pop.findViewById(R.id.wardrobe_pop_window_img);
        mWardrobe_pop_window_img.setMaxWidth(Constants.PHONE_WIDTH);
        mWardrobe_pop_window_img.setMaxHeight(Constants.PHONE_HEIGHT/4);
        mPopwindow_select_icon1 = (MyImageView) my_pop.findViewById(R.id.popwindow_select_icon1);
        mPopwindow_select_icon2 = (MyImageView) my_pop.findViewById(R.id.popwindow_select_icon2);
        mPopwindow_select_icon3 = (MyImageView) my_pop.findViewById(R.id.popwindow_select_icon3);
        mPopwindow_select_icon4 = (MyImageView) my_pop.findViewById(R.id.popwindow_select_icon4);
        mPopwindow_select_icon1.setSize(Constants.PHONE_WIDTH/24,Constants.PHONE_WIDTH/24);
        mPopwindow_select_icon2.setSize(Constants.PHONE_WIDTH/24,Constants.PHONE_WIDTH/24);
        mPopwindow_select_icon3.setSize(Constants.PHONE_WIDTH/24,Constants.PHONE_WIDTH/24);
        mPopwindow_select_icon4.setSize(Constants.PHONE_WIDTH/24,Constants.PHONE_WIDTH/24);
        mMWardrobe_pop_window_delete_img1 = (MyImageView) my_pop.findViewById(R.id.wardrobe_pop_window_delete_img1);
        mMWardrobe_pop_window_delete_img1.setSize(Constants.PHONE_WIDTH/20,Constants.PHONE_WIDTH/20);
        mWardrobe_pop_window_img1 = (MyImageView) my_pop.findViewById(R.id.wardrobe_pop_window_img1);
        mWardrobe_pop_window_img2 = (MyImageView) my_pop.findViewById(R.id.wardrobe_pop_window_img2);
        mWardrobe_pop_window_img3 = (MyImageView) my_pop.findViewById(R.id.wardrobe_pop_window_img3);
        mWardrobe_pop_window_img1.setSize(Constants.PHONE_WIDTH/5,Constants.PHONE_WIDTH/5);
        mWardrobe_pop_window_img2.setSize(Constants.PHONE_WIDTH/5,Constants.PHONE_WIDTH/5);
        mWardrobe_pop_window_img3.setSize(Constants.PHONE_WIDTH/5,Constants.PHONE_WIDTH/5);
        final LinearLayout popwindow_layout1 = (LinearLayout) my_pop.findViewById(R.id.popwindow_layout1);
        final LinearLayout popwindow_layout2 = (LinearLayout) my_pop.findViewById(R.id.popwindow_layout2);
        popwindow_layout1.setVisibility(View.VISIBLE);
        popwindow_layout2.setVisibility(View.VISIBLE);
        mWardrobe_pop_window_delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LogUtils.showVerbose("WardrobeFragment","我删除了1");
                if (Constants.isClickDeleteImage==false){
                    popwindow_layout1.setVisibility(View.GONE);
                    Constants.isClickDeleteImage = true;
                    if (Constants.isClickDeleteImage1){
                        set_pop.dismiss();
                        displaySelectPictures();
                    }
                }
            }
        });
        mWardrobe_pop_window_delete_img1_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LogUtils.showVerbose("WardrobeFragment","我删除了2");
                if (Constants.isClickDeleteImage1==false){
                    popwindow_layout2.setVisibility(View.GONE);
                    Constants.isClickDeleteImage1 = true;
                    if (Constants.isClickDeleteImage){
                        set_pop.dismiss();
                        displaySelectPictures();
                    }
                }
            }
        });
    }

    private void displaySelectPictures() {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_windows_select_picture_layout, null);
        set_pop_window = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);

        set_pop_window.setBackgroundDrawable(new BitmapDrawable());
        set_pop_window.setOutsideTouchable(true);
        set_pop_window.setFocusable(true);
        set_pop_window.setTouchable(true);
        //	set_pop.setAnimationStyle(R.style.Anim_popWindow_in);
        //set_pop.showAtLocation(frame,Gravity.TOP, 0, 0);
        set_pop_window.showAsDropDown(mWardrobe_head_layout,0,0);
        View my_pop = set_pop_window.getContentView();
        mPopwindow_select_photo = (TextView) my_pop.findViewById(R.id.popwindow_select_photo);
        mPopwindow_select_pictures = (TextView) my_pop.findViewById(R.id.popwindow_select_pictures);
        mPopwindow_select_cancel = (TextView) my_pop.findViewById(R.id.popwindow_select_cancel);
        mPopwindow_select_photo.setOnClickListener(new View.OnClickListener() {

            private File mMTempImageFile;

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // set the output file of camera
                    try {
                        mMTempImageFile = FileUtils.createTmpFile(getActivity());
                    } catch (IOException e) {
//                        Log.e(TAG, "launchCamera: ", e);
                    }
                    if (mMTempImageFile != null && mMTempImageFile.exists()) {
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mMTempImageFile));
                        startActivity(cameraIntent);
                    } else {
                        Toast.makeText(getActivity(), R.string.camera_temp_file_error, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
                }
                set_pop_window.dismiss();//打开照相机之后就关闭这个弹出框
            }
        });
        mPopwindow_select_pictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ImagesSelectorActivity.class);
                // max number of images to be selected
                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 9);
                // min size of image which will be shown; to filter tiny images (mainly icons)
                intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
                // show camera or not
                intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
                // show wardrobe or not
                intent.putExtra(SelectorSettings.SELECTOR_SHOW_WARDROBE, true);
                // pass current selected images as the initial value
                intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
                // start the selector
                startActivity(intent);
                set_pop_window.dismiss();//打开图片库之后就关闭这个弹出框
            }
        });
        mPopwindow_select_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_pop_window.dismiss();//取消之后就关闭这个弹出框
            }
        });
    }
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                if (SaveMsgUtils.getCurrentBooleanState(getActivity(),"popwindowState","ispop")==false){
//                    Constants.isWardrobeInitPopwindow = false ;
                    LogUtils.showVerbose("WardrobeFragment","当前的信息："+Constants.isWardrobeInitPopwindow);
                    initPopwindows();
                }
            }
        }
    };
}
