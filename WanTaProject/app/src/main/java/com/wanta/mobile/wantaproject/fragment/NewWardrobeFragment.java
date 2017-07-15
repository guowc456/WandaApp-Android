package com.wanta.mobile.wantaproject.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.activity.LoginActivity;
import com.wanta.mobile.wantaproject.domain.CacheDataHelper;
import com.wanta.mobile.wantaproject.domain.CacheDataUtils;
import com.wanta.mobile.wantaproject.phonecamera.LunchCameraActivity;
import com.wanta.mobile.wantaproject.phonepics.PhonePicsSelectActivity;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.WardrobeCalendarActivity;
import com.wanta.mobile.wantaproject.activity.WardrobeDetailActivity;
import com.wanta.mobile.wantaproject.adapter.CustomRecycleviewAdapter;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.CustomChildInfo;
import com.wanta.mobile.wantaproject.domain.CustomGroupInfo;
import com.wanta.mobile.wantaproject.uploadimage.FileUtils;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;
import com.wanta.mobile.wantaproject.utils.SaveMsgUtils;
import com.wanta.mobile.wantaproject.utils.SimpleDraweeControlUtils;
import com.wanta.mobile.wantaproject.utils.SystemUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/16.
 */
public class NewWardrobeFragment extends Fragment {

    private LinearLayout wardrober_head_title;
    private View mView_wardrobe;
    private List<CustomGroupInfo> groupList = new ArrayList<>();

    public int[] cloth_icon = {R.mipmap.calendar, 0, R.mipmap.almirah_upper, 0, R.mipmap.almirah_trousers, 0,
            R.mipmap.almirah_skirt, 0, R.mipmap.almirah_hat, 0, R.mipmap.almirah_shoes, 0,
            R.mipmap.almirah_scarf, 0, R.mipmap.almirah_belt, 0, R.mipmap.almirah_bag, 0};
    public String[] cloth_name = new String[]{
            "万搭日历", "", "上衣", "", "裤子", "", "裙子", "", "帽子", "", "鞋子", "", "围巾", "", "腰带", "", "包", ""
    };
    public String[] cloth_number = new String[cloth_name.length * 2];
    private RecyclerView new_wardrobe_fragment_recycle;
    private CustomRecycleviewAdapter adapter;
    private MyImageView new_wardrobe_group_arrows;
    private MyImageView new_wardrobe_group_icon;
    private PopupWindow set_pop;
    private LinearLayout mWardrobe_head_layout;
    private LinearLayout mPopwindow_layout;
    private MyImageView mWardrobe_pop_window_delete_img;
    private LinearLayout mWardrobe_pop_window_delete_img_layout;
    private LinearLayout mWardrobe_pop_window_delete_img1_layout;
    private CustomSimpleDraweeView mWardrobe_pop_window_img;
    private MyImageView mPopwindow_select_icon2;
    private MyImageView mPopwindow_select_icon1;
    private MyImageView mPopwindow_select_icon3;
    private MyImageView mPopwindow_select_icon4;
    private MyImageView mMWardrobe_pop_window_delete_img1;
    private MyImageView mWardrobe_pop_window_img1;
    private MyImageView mWardrobe_pop_window_img2;
    private MyImageView mWardrobe_pop_window_img3;
    private PopupWindow set_pop_window;
    private TextView mPopwindow_select_photo;
    private TextView mPopwindow_select_pictures;
    private TextView mPopwindow_select_cancel;
    private PopupWindow init_set_pop;
    private MyImageView mPopwindow_init_icon1;
    private MyImageView mPopwindow_init_icon2;
    private MyImageView mWardrobe_pop_window_init_delete_img;
    private MyImageView mPopwindow_init_select_icon1;
    private MyImageView mPopwindow_init_select_icon2;
    private MyImageView mPopwindow_init_select_icon3;
    private MyImageView mPopwindow_init_select_icon4;
    private TextView mPopwindow_init_tv1;
    private TextView mPopwindow_init_tv2;
    private TextView mPopwindow_init_tv3;
    private TextView mPopwindow_init_tv4;
    private TextView mPopwindow_init_tv5;
    private TextView mPopwindow_init_tv6;
    private ArrayList<String> mResults = new ArrayList<>();
    private TextView new_wardrobe_pop_window;
    public  String pop_window_name[] = new String[]{
            "请在全部上衣内添加","请在全部裤子内添加","请在全部裙子内添加","请在全部帽子内添加","请在全部鞋子内添加","请在全部围巾内添加","请在全部腰带内添加","请在全部包内添加"
    };
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView_wardrobe = inflater.inflate(R.layout.fragment_new_wardrobe, container, false);
        initId();
        initData();
        mHandler.sendEmptyMessageDelayed(1, 100);//不加延时，就会出现父布局没有加载完，就会加载popwindow的错误
        return mView_wardrobe;
    }


    @Override
    public void onDestroyView() {
        new_wardrobe_fragment_recycle.setAdapter(null);
        new_wardrobe_fragment_recycle = null;
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void initId() {
        //得出衣服的数量
        for (int i = 0; i < (Constants.Cloth_catogry_number.length) * 2 + 2; i++) {
            if (i % 2 == 0) {
                if (i == 0) {
                    cloth_number[i] = "";
                } else {
                    cloth_number[i] = Constants.Cloth_catogry_number[i / 2 - 1];
                }
            }
        }
        for (int i = 0; i < cloth_name.length; i++) {
            CustomGroupInfo groupInfo = new CustomGroupInfo();
            groupInfo.setClothName(cloth_name[i]);
            groupInfo.setClothNumber("您有" + cloth_number[i] + "件");
            groupInfo.setIconUrl(cloth_icon[i]);
            groupInfo.setGroupExpend(false);
            CustomChildInfo childInfo = new CustomChildInfo();
            childInfo.setChildContent("衣服的名字" + i);
            childInfo.setChildExpend(false);
            groupInfo.setChildInfo(childInfo);
            groupList.add(groupInfo);
        }
        new_wardrobe_fragment_recycle = (RecyclerView) mView_wardrobe.findViewById(R.id.new_wardrobe_fragment_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        new_wardrobe_fragment_recycle.setLayoutManager(linearLayoutManager);
        adapter = new CustomRecycleviewAdapter(context, groupList);
        new_wardrobe_fragment_recycle.setAdapter(adapter);

        mWardrobe_head_layout = (LinearLayout) mView_wardrobe.findViewById(R.id.wardrobe_head_layout);
        new_wardrobe_pop_window = (TextView) mView_wardrobe.findViewById(R.id.new_wardrobe_pop_window);

    }

    private void initData() {
        adapter.setOnCustomGroupItemClickListener(new CustomRecycleviewAdapter.OnCustomGroupItemClickListener() {
            @Override
            public void onItemClick(View groupView, CustomGroupInfo groupInfo, int groupPosition) {

            }
        });
        //点击万搭日历的事件
        adapter.setOnCustomArrowsClickListener(new CustomRecycleviewAdapter.OnCustomArrowsClickListener() {
            @Override
            public void onItemClick(View view, int groupPosition) {
//                LogUtils.showVerbose("NewWardrobeFragment","我点击了箭头了");
//                if (groupPosition == 0) {
//                    Intent intent = new Intent(getActivity(), WardrobeCalendarActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    getActivity().startActivity(intent);
//                    getActivity().finish();
//                }
            }
        });
        //点击父目录衣服类别和数据的事件
        adapter.setOnCustomClothCatogryClickListener(new CustomRecycleviewAdapter.OnCustomClothCatogryClickListener() {
            @Override
            public void onItemClick(View view, int groupPosition) {
//                LogUtils.showVerbose("NewWardrobeFragment","衣服类别和数目");
                if (groupPosition == 0) {
                    CacheDataHelper.addNullArgumentsMethod();
                    Intent intent = new Intent(context, WardrobeCalendarActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                } else {
                    if ("0".equals(Constants.Cloth_catogry_number[groupPosition / 2 - 1])){
                        SystemUtils.showSystemPopWindow(context,new_wardrobe_pop_window,"您还未上传任何单品",pop_window_name[groupPosition / 2 - 1]);
                    }else {
                        CacheDataHelper.addNullArgumentsMethod();
                        CacheDataUtils.setSelectImagePosition(groupPosition / 2 - 1,0);
                        Intent intent = new Intent(context, WardrobeDetailActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putInt("firstItem", groupPosition / 2 - 1);
//                        bundle.putInt("thridItem", 0);
//                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        ((Activity)context).finish();
                    }
                }
            }
        });
        //添加图片的功能
        adapter.setOnCustomAddIconClickListener(new CustomRecycleviewAdapter.OnCustomAddIconClickListener() {


            @Override
            public void onItemClick(int position) {
//                LogUtils.showVerbose("NewWardrobeFragment","添加功能的按钮位置"+position);
                if ("reg_log".equals(Constants.STATUS)){
                    Constants.currentClothType = getType(position);
                    LogUtils.showVerbose("NewWardrobeFragment","当前的类型"+Constants.currentClothType);

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("isWardrobePhotoPopwindow",Context.MODE_PRIVATE);
                    boolean isClick = sharedPreferences.getBoolean("isClick", true);
                    if (isClick==true) {
                        SharedPreferences shareds = getActivity().getSharedPreferences("isWardrobePhotoPopwindow",Context.MODE_PRIVATE);
                        shareds.edit().putBoolean("isClick",false).commit();
                        showPopWindows();
                    } else {
                        displaySelectPictures();
                    }
                }else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    ((Activity)context).finish();
                }

            }
        });
        //点击二级目录的点击事件
        adapter.setOnCustomClothAttributeClickListener(new CustomRecycleviewAdapter.OnCustomClothAttributeClickListener() {
            @Override
            public void onItemClick(int groupPosition, int childPosition) {
//                LogUtils.showVerbose("NewWardrobeFragment","groupPosition="+(groupPosition/2-1)+"  childPosition="+childPosition);
                CacheDataHelper.addNullArgumentsMethod();
                CacheDataUtils.setSelectImagePosition(groupPosition / 2 - 1,0);
                Intent intent = new Intent(context, WardrobeDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("firstItem", groupPosition / 2 - 1);
//                bundle.putInt("thridItem", childPosition);
//                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }
    //获取当前点击的衣服的类型
    private String getType(int position) {
        LogUtils.showVerbose("NewWardrobeFragment","当前的位置"+position);
        int newPosition = position/2-1;
        return Constants.cloth_catogry_english[newPosition];
    }

    private void showPopWindows() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_windows_layout, null);
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
        mWardrobe_pop_window_delete_img.setSize(Constants.PHONE_WIDTH / 20, Constants.PHONE_WIDTH / 20);
        mWardrobe_pop_window_delete_img_layout = (LinearLayout) my_pop.findViewById(R.id.wardrobe_pop_window_delete_img_layout);
        mWardrobe_pop_window_delete_img1_layout = (LinearLayout) my_pop.findViewById(R.id.wardrobe_pop_window_delete_img1_layout);
        mWardrobe_pop_window_img = (CustomSimpleDraweeView) my_pop.findViewById(R.id.wardrobe_pop_window_img);
        mWardrobe_pop_window_img.setWidth((int) ((Constants.PHONE_WIDTH*1.00/10)*8));
        mWardrobe_pop_window_img.setHeight(Constants.PHONE_HEIGHT/4);
        SimpleDraweeControlUtils.setAssetsImageRangle(context,
                mWardrobe_pop_window_img,25,"popwindow_icon1.png");
        mPopwindow_select_icon1 = (MyImageView) my_pop.findViewById(R.id.popwindow_select_icon1);
        mPopwindow_select_icon2 = (MyImageView) my_pop.findViewById(R.id.popwindow_select_icon2);
        mPopwindow_select_icon3 = (MyImageView) my_pop.findViewById(R.id.popwindow_select_icon3);
        mPopwindow_select_icon4 = (MyImageView) my_pop.findViewById(R.id.popwindow_select_icon4);
        mPopwindow_select_icon1.setSize(Constants.PHONE_WIDTH / 24, Constants.PHONE_WIDTH / 24);
        mPopwindow_select_icon2.setSize(Constants.PHONE_WIDTH / 24, Constants.PHONE_WIDTH / 24);
        mPopwindow_select_icon3.setSize(Constants.PHONE_WIDTH / 24, Constants.PHONE_WIDTH / 24);
        mPopwindow_select_icon4.setSize(Constants.PHONE_WIDTH / 24, Constants.PHONE_WIDTH / 24);
        mMWardrobe_pop_window_delete_img1 = (MyImageView) my_pop.findViewById(R.id.wardrobe_pop_window_delete_img1);
        mMWardrobe_pop_window_delete_img1.setSize(Constants.PHONE_WIDTH / 20, Constants.PHONE_WIDTH / 20);
        mWardrobe_pop_window_img1 = (MyImageView) my_pop.findViewById(R.id.wardrobe_pop_window_img1);
        mWardrobe_pop_window_img2 = (MyImageView) my_pop.findViewById(R.id.wardrobe_pop_window_img2);
        mWardrobe_pop_window_img3 = (MyImageView) my_pop.findViewById(R.id.wardrobe_pop_window_img3);
        mWardrobe_pop_window_img1.setSize(Constants.PHONE_WIDTH / 5, Constants.PHONE_WIDTH / 5);
        mWardrobe_pop_window_img2.setSize(Constants.PHONE_WIDTH / 5, Constants.PHONE_WIDTH / 5);
        mWardrobe_pop_window_img3.setSize(Constants.PHONE_WIDTH / 5, Constants.PHONE_WIDTH / 5);
        final LinearLayout popwindow_layout1 = (LinearLayout) my_pop.findViewById(R.id.popwindow_layout1);
        final LinearLayout popwindow_layout2 = (LinearLayout) my_pop.findViewById(R.id.popwindow_layout2);
        popwindow_layout1.setVisibility(View.VISIBLE);
        popwindow_layout2.setVisibility(View.VISIBLE);
        mWardrobe_pop_window_delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LogUtils.showVerbose("WardrobeFragment","我删除了1");
                if (Constants.isClickDeleteImage == false) {
                    popwindow_layout1.setVisibility(View.GONE);
                    Constants.isClickDeleteImage = true;
                    if (Constants.isClickDeleteImage1) {
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
                if (Constants.isClickDeleteImage1 == false) {
                    popwindow_layout2.setVisibility(View.GONE);
                    Constants.isClickDeleteImage1 = true;
                    if (Constants.isClickDeleteImage) {
                        set_pop.dismiss();
                        displaySelectPictures();
                    }
                }
            }
        });
    }

    private void displaySelectPictures() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_windows_select_picture_layout, null);
        set_pop_window = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);

        set_pop_window.setBackgroundDrawable(new BitmapDrawable());
        set_pop_window.setOutsideTouchable(true);
        set_pop_window.setFocusable(true);
        set_pop_window.setTouchable(true);
        //	set_pop.setAnimationStyle(R.style.Anim_popWindow_in);
        //set_pop.showAtLocation(frame,Gravity.TOP, 0, 0);
        set_pop_window.showAsDropDown(mWardrobe_head_layout, 0, 0);
        View my_pop = set_pop_window.getContentView();
        mPopwindow_select_photo = (TextView) my_pop.findViewById(R.id.popwindow_select_photo);
        mPopwindow_select_pictures = (TextView) my_pop.findViewById(R.id.popwindow_select_pictures);
        mPopwindow_select_cancel = (TextView) my_pop.findViewById(R.id.popwindow_select_cancel);
        mPopwindow_select_photo.setOnClickListener(new View.OnClickListener() {

            private File mMTempImageFile;

            @Override
            public void onClick(View v) {
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//                    // set the output file of camera
//                    try {
//                        mMTempImageFile = FileUtils.createTmpFile(getActivity());
//                    } catch (IOException e) {
////                        Log.e(TAG, "launchCamera: ", e);
//                    }
//                    if (mMTempImageFile != null && mMTempImageFile.exists()) {
//                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mMTempImageFile));
//                        startActivity(cameraIntent);
//                    } else {
//                        Toast.makeText(getActivity(), R.string.camera_temp_file_error, Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(getActivity(), R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
//                }
                Intent intent = new Intent(context, LunchCameraActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("wardrobe_camera","wardrobe_camera");
                startActivity(intent);
                getActivity().finish();
                set_pop_window.dismiss();//打开照相机之后就关闭这个弹出框
            }
        });
        mPopwindow_select_pictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ImagesSelectorActivity.class);
//                // max number of images to be selected
//                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 9);
//                // min size of image which will be shown; to filter tiny images (mainly icons)
//                intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
//                // show camera or not
//                intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
//                // show wardrobe or not
//                intent.putExtra(SelectorSettings.SELECTOR_SHOW_WARDROBE, true);
//                // pass current selected images as the initial value
//                intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
//                // start the selector
//                startActivity(intent);
                CacheDataHelper.addNullArgumentsMethod();
                Intent intent = new Intent(context, PhonePicsSelectActivity.class);
                intent.putExtra("currentSize",9);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
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

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (SaveMsgUtils.getCurrentBooleanState(context, "popwindowState", "ispop") == false) {
//                    Constants.isWardrobeInitPopwindow = false ;
                    LogUtils.showVerbose("WardrobeFragment", "当前的信息：" + Constants.isWardrobeInitPopwindow);
                    initPopwindows();
                }
            }
        }
    };

    private void initPopwindows() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_windows_init_layout, null);
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
        mPopwindow_init_icon1.setSize(Constants.PHONE_WIDTH / 8, Constants.PHONE_WIDTH / 8);
        mPopwindow_init_icon2 = (MyImageView) my_init_pop.findViewById(R.id.popwindow_init_icon2);
        mPopwindow_init_icon2.setSize(Constants.PHONE_WIDTH / 8, Constants.PHONE_WIDTH / 8);
        mWardrobe_pop_window_init_delete_img = (MyImageView) my_init_pop.findViewById(R.id.wardrobe_pop_window_init_delete_img);
        mWardrobe_pop_window_init_delete_img.setSize(Constants.PHONE_WIDTH / 20, Constants.PHONE_WIDTH / 20);
        mPopwindow_init_select_icon1 = (MyImageView) my_init_pop.findViewById(R.id.popwindow_init_select_icon1);
        mPopwindow_init_select_icon2 = (MyImageView) my_init_pop.findViewById(R.id.popwindow_init_select_icon2);
        mPopwindow_init_select_icon3 = (MyImageView) my_init_pop.findViewById(R.id.popwindow_init_select_icon3);
        mPopwindow_init_select_icon4 = (MyImageView) my_init_pop.findViewById(R.id.popwindow_init_select_icon4);
        mPopwindow_init_select_icon1.setSize(Constants.PHONE_WIDTH / 24, Constants.PHONE_WIDTH / 24);
        mPopwindow_init_select_icon2.setSize(Constants.PHONE_WIDTH / 24, Constants.PHONE_WIDTH / 24);
        mPopwindow_init_select_icon3.setSize(Constants.PHONE_WIDTH / 24, Constants.PHONE_WIDTH / 24);
        mPopwindow_init_select_icon4.setSize(Constants.PHONE_WIDTH / 24, Constants.PHONE_WIDTH / 24);
        mPopwindow_init_tv1 = (TextView) my_init_pop.findViewById(R.id.popwindow_init_tv1);
        mPopwindow_init_tv2 = (TextView) my_init_pop.findViewById(R.id.popwindow_init_tv2);
        mPopwindow_init_tv3 = (TextView) my_init_pop.findViewById(R.id.popwindow_init_tv3);
        mPopwindow_init_tv4 = (TextView) my_init_pop.findViewById(R.id.popwindow_init_tv4);
        mPopwindow_init_tv5 = (TextView) my_init_pop.findViewById(R.id.popwindow_init_tv5);
        mPopwindow_init_tv6 = (TextView) my_init_pop.findViewById(R.id.popwindow_init_tv6);
        mWardrobe_pop_window_init_delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMsgUtils.setCurrentBooleanState(context, "popwindowState", "ispop", true);
                init_set_pop.dismiss();
            }
        });
    }

}
