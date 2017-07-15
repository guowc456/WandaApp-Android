package com.wanta.mobile.wantaproject.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.CameraActivity;
import com.wanta.mobile.wantaproject.adapter.CameraModifyMeiHuaRecycleViewAdapter;
import com.wanta.mobile.wantaproject.adapter.CameraViewpagerAdapter;
import com.wanta.mobile.wantaproject.addpictag.PictureTagLayout;
import com.wanta.mobile.wantaproject.addpictag.PictureTagViewpagerAdapter;
import com.wanta.mobile.wantaproject.customview.CustomViewPager;
import com.wanta.mobile.wantaproject.customview.FilterColorView;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.FilterTools;
import com.wanta.mobile.wantaproject.utils.ImageCompressUtil;
import com.wanta.mobile.wantaproject.utils.LogUtils;

/**
 * Created by WangYongqiang on 2016/10/16.
 */
public class CameraModifyMeiHuaFragment extends Fragment {

    private View view_meihua;
    private RecyclerView mCamera_modify_meihua_recycleview;
    private String mList[];
    private CameraModifyMeiHuaRecycleViewAdapter mModifyMeiHuaRecycleViewAdapter;
    private float[] colorArray = {1, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0};
    private PictureTagViewpagerAdapter mAdapter;
    private CustomViewPager viewpager;
    private SeekBar modify_seekbar_max;
    private SeekBar modify_seekbar_min;
    private RadioButton mCamera_modify_radio_r;
    private RadioButton mCamera_modify_radio_g;
    private RadioButton mCamera_modify_radio_b;
    private FilterColorView mFilterColorView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_meihua = inflater.inflate(R.layout.fragment_camera_modify_meihua, container, false);
        initId();
        return view_meihua;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        CameraActivity cameraActivity = (CameraActivity) activity;
        mAdapter = cameraActivity.getAdapter();
        viewpager = (CustomViewPager) activity.findViewById(R.id.viewpager);
    }

    private void initId() {
        mList = getResources().getStringArray(R.array.camera_modify_tabs);
        mCamera_modify_meihua_recycleview = (RecyclerView) view_meihua.findViewById(R.id.camera_modify_meihua_recycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mCamera_modify_meihua_recycleview.setLayoutManager(linearLayoutManager);
        mModifyMeiHuaRecycleViewAdapter = new CameraModifyMeiHuaRecycleViewAdapter(getActivity(), mList);
        mCamera_modify_radio_r = (RadioButton) view_meihua.findViewById(R.id.camera_modify_radio_r);
        mCamera_modify_radio_g = (RadioButton) view_meihua.findViewById(R.id.camera_modify_radio_g);
        mCamera_modify_radio_b = (RadioButton) view_meihua.findViewById(R.id.camera_modify_radio_b);
        mCamera_modify_meihua_recycleview.setAdapter(mModifyMeiHuaRecycleViewAdapter);
        mModifyMeiHuaRecycleViewAdapter.setMeihuaRecycleViewClickListener(new CameraModifyMeiHuaRecycleViewAdapter.MeihuaRecycleViewClickListener() {

            @Override
            public void setOnItemClick(View v, LinearLayout image_bg_color) {
                int position = mCamera_modify_meihua_recycleview.getChildAdapterPosition(v);
                LogUtils.showVerbose("CameraModifyMeiHuaFragment", "position1=" + position);
                FilterColorView filterColorView = Constants.display_images.get(viewpager.getCurrentItem());
//                filterColorView.setColorArray(getDifferentColor(position));
                filterColorView.setIsModifyPic(true);
//                filterColorView.setPixelSize(getPixelArray(filterColorView.getBitmap(),getDifferentColorRamp(position),getDifferentGraeyLevel(position)[0],getDifferentGraeyLevel(position)[1]));
                filterColorView.setPixelSize(FilterTools.getPixelArray(filterColorView.getBitmap(),getDifferentColorRamp(position),getDifferentGraeyLevel(position)[0],getDifferentGraeyLevel(position)[1]));
//                LogUtils.showVerbose("CameraModifyMeiHuaFragment","最大值="+Constants.picStretchMax+"  最小值="+Constants.picStretchMin);
                LogUtils.showVerbose("CameraModifyMeiHuaFragment","最大值="+filterColorView.getBitmap().getWidth()+"  最小值="+filterColorView.getBitmap().getHeight());
//                Constants.upload_images_lrucache.evictAll();
                for (int i = 0; i < Constants.upload_images_url.size(); i++) {
                    if (viewpager.getCurrentItem() == i) {
                        Constants.upload_images_lrucache.remove(Constants.upload_images_url.get(viewpager.getCurrentItem()));
//                        Constants.upload_images_lrucache.put(Constants.upload_images_url.get(viewpager.getCurrentItem()), FilterTools.getModifyPic(filterColorView.getBitmap(),FilterTools.getPixelArray(filterColorView.getBitmap(),getDifferentColorRamp(position),getDifferentGraeyLevel(position)[0],getDifferentGraeyLevel(position)[1])));
                        Constants.upload_images_lrucache.put(Constants.upload_images_url.get(viewpager.getCurrentItem()), FilterTools.getModifyPic(ImageCompressUtil.compressBySize(Constants.upload_images_url.get(viewpager.getCurrentItem()),600,800)
                                ,FilterTools.getPixelArray(filterColorView.getBitmap(),getDifferentColorRamp(position),getDifferentGraeyLevel(position)[0],getDifferentGraeyLevel(position)[1])));
                        //实现添加标签并且也可以实现标签的正确添加
                        Bitmap newBitmap = FilterTools.getModifyPic(ImageCompressUtil.compressBySize(Constants.upload_images_url.get(viewpager.getCurrentItem()),600,800)
                                ,FilterTools.getPixelArray(filterColorView.getBitmap(),getDifferentColorRamp(position),getDifferentGraeyLevel(position)[0],getDifferentGraeyLevel(position)[1]));
                        PictureTagLayout pictureTagLayout = Constants.all_picture_tag_view.get(viewpager.getCurrentItem());
                        pictureTagLayout.setBackground(new BitmapDrawable(getActivity().getResources(),newBitmap));
                        Constants.all_picture_tag_view.set(viewpager.getCurrentItem(), pictureTagLayout);
                    }
//                    else {
//                        Constants.upload_images_lrucache.put(Constants.upload_images_url.get(i), ImageCompressUtil.compressBySize(Constants.upload_images_url.get(i), 600, 800));
////                        PictureTagLayout pictureTagLayout = Constants.all_picture_tag_view.get(i);
////                        pictureTagLayout.setBackground(new BitmapDrawable(getActivity().getResources(),ImageCompressUtil.compressBySize(Constants.upload_images_url.get(i), 600, 800)));
////                        Constants.all_picture_tag_view.set(i, pictureTagLayout);
//                    }
                }
                //设置当选中的时候背景的颜色为红色
                image_bg_color.setBackgroundColor(getResources().getColor(R.color.head_bg_color));
                for (int i=0;i<mList.length;i++){
                    if (i!=position){
                        mModifyMeiHuaRecycleViewAdapter.notifyItemChanged(i);
                    }
                }
            }
        });
//        mModifyMeiHuaRecycleViewAdapter.setMeihuaRecycleViewClickListener(new CameraModifyMeiHuaRecycleViewAdapter.MeihuaRecycleViewClickListener() {
//
//            @Override
//            public void setOnItemClick(View v, LinearLayout image_bg_color) {
//                int position = mCamera_modify_meihua_recycleview.getChildAdapterPosition(v);
//                LogUtils.showVerbose("CameraModifyMeiHuaFragment", "position1=" + position);
//                FilterColorView filterColorView = Constants.display_images.get(viewpager.getCurrentItem());
//                filterColorView.setColorArray(getDifferentColor(position));
//                Constants.upload_images_lrucache.evictAll();
//                for (int i = 0; i < Constants.upload_images_url.size(); i++) {
//                    if (viewpager.getCurrentItem() == i) {
//                        Constants.upload_images_lrucache.put(Constants.upload_images_url.get(viewpager.getCurrentItem()), createBitmap(filterColorView.getBitmap(), getDifferentColor(position)));
//                    } else {
//                        Constants.upload_images_lrucache.put(Constants.upload_images_url.get(i), ImageCompressUtil.compressBySize(Constants.upload_images_url.get(i), 600, 800));
//                    }
//                }
//                //设置当选中的时候背景的颜色为红色
//                image_bg_color.setBackgroundColor(getResources().getColor(R.color.head_bg_color));
//                for (int i=0;i<mList.length;i++){
//                    if (i!=position){
//                        mModifyMeiHuaRecycleViewAdapter.notifyItemChanged(i);
//                    }
//                }
//            }
//        });
//        //设置图片的灰度值的最大值
//        modify_seekbar_max = (SeekBar) view_meihua.findViewById(R.id.modify_seekbar_max);
//        modify_seekbar_max.setMax(255);
//        modify_seekbar_max.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mFilterColorView = Constants.display_images.get(viewpager.getCurrentItem());
//                        if (mCamera_modify_radio_r.isChecked()) {
//                            mFilterColorView.setPicR(100);
//                        } else if (mCamera_modify_radio_g.isChecked()) {
//                            mFilterColorView.setPicG(200);
//                        } else if (mCamera_modify_radio_b.isChecked()) {
//                            mFilterColorView.setPicB(300);
//                        }
//                        mFilterColorView.setPicTranslateMax(255 - progress);//设置灰度值的最大值是从255开始
//                        Constants.upload_images_lrucache.evictAll();
//                        for (int i = 0; i < Constants.upload_images_url.size(); i++) {
//                            if (viewpager.getCurrentItem() == i) {
//                                Constants.upload_images_lrucache.put(Constants.upload_images_url.get(viewpager.getCurrentItem()), createBitmap1(mFilterColorView.getBitmap()));
//                            } else {
//                                Constants.upload_images_lrucache.put(Constants.upload_images_url.get(i), ImageCompressUtil.compressBySize(Constants.upload_images_url.get(i), 600, 800));
//                            }
//                        }
//                    }
//                }).start();
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//        //设置图片的灰度值的最小值
//        modify_seekbar_min = (SeekBar) view_meihua.findViewById(R.id.modify_seekbar_min);
//        modify_seekbar_min.setMax(254);
//        modify_seekbar_min.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//            private FilterColorView mFilterColorView;
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mFilterColorView = Constants.display_images.get(viewpager.getCurrentItem());
//                        if (mCamera_modify_radio_r.isChecked()) {
//                            mFilterColorView.setPicR(100);
//                        } else if (mCamera_modify_radio_g.isChecked()) {
//                            mFilterColorView.setPicG(200);
//                        } else if (mCamera_modify_radio_b.isChecked()) {
//                            mFilterColorView.setPicB(300);
//                        }
//                        mFilterColorView.setPicTranslateMin(progress);//设置灰度值的最小值是从0开始
//                        Constants.upload_images_lrucache.evictAll();
//                        for (int i = 0; i < Constants.upload_images_url.size(); i++) {
//                            if (viewpager.getCurrentItem() == i) {
//                                Constants.upload_images_lrucache.put(Constants.upload_images_url.get(viewpager.getCurrentItem()), createBitmap1(mFilterColorView.getBitmap()));
//                            } else {
//                                Constants.upload_images_lrucache.put(Constants.upload_images_url.get(i), ImageCompressUtil.compressBySize(Constants.upload_images_url.get(i), 600, 800));
//                            }
//                        }
//                    }
//                }).start();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//        //红色的选择通道
//        mCamera_modify_radio_r.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LogUtils.showVerbose("CameraModifyMeiHuaFragment", "点击红的了");
//                if (!mCamera_modify_radio_r.isChecked()) {
//                    mFilterColorView.setPicTranslateMax(0);
//                    mFilterColorView.setPicTranslateMin(0);
////                    mFilterColorView.setPicB(0);
////                    mFilterColorView.setPicR(0);
////                    mFilterColorView.setPicG(0);
////                    mFilterColorView.setBitmap(Constants.upload_images_lrucache.get(Constants.modify_bitmap_list_url.get(viewpager.getCurrentItem())));
//                    modify_seekbar_min.setProgress(0);
//                    modify_seekbar_max.setProgress(0);
//                    mFilterColorView.fresh(0);
//                    mFilterColorView = Constants.display_images.get(viewpager.getCurrentItem());
//                    mFilterColorView.setPicB(0);
//                    mFilterColorView.setPicR(0);
//                    mFilterColorView.setPicG(0);
//                }
//            }
//        });
//        //绿色的选择通道
//        mCamera_modify_radio_g.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LogUtils.showVerbose("CameraModifyMeiHuaFragment", "点击绿的了");
//                if (!mCamera_modify_radio_g.isChecked()) {
//                    mFilterColorView.setPicTranslateMax(0);
//                    mFilterColorView.setPicTranslateMin(0);
////                    mFilterColorView.setPicB(0);
////                    mFilterColorView.setPicR(0);
////                    mFilterColorView.setPicG(0);
////                    mFilterColorView.setBitmap(Constants.upload_images_lrucache.get(Constants.modify_bitmap_list_url.get(viewpager.getCurrentItem())));
//                    modify_seekbar_min.setProgress(0);
//                    modify_seekbar_max.setProgress(0);
//                    modify_seekbar_max.setClickable(true);
//                    mFilterColorView.fresh(0);
//                    mFilterColorView = Constants.display_images.get(viewpager.getCurrentItem());
//                    mFilterColorView.setPicB(0);
//                    mFilterColorView.setPicR(0);
//                    mFilterColorView.setPicG(0);
//                }
//            }
//        });
//        //蓝色的选择通道
//        mCamera_modify_radio_b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LogUtils.showVerbose("CameraModifyMeiHuaFragment", "点击蓝的了");
//                if (!mCamera_modify_radio_b.isChecked()) {
//                    mFilterColorView.setPicTranslateMax(0);
//                    mFilterColorView.setPicTranslateMin(0);
////                    mFilterColorView.setPicB(0);
////                    mFilterColorView.setPicR(0);
////                    mFilterColorView.setPicG(0);
////                    mFilterColorView.setBitmap(Constants.upload_images_lrucache.get(Constants.modify_bitmap_list_url.get(viewpager.getCurrentItem())));
//                    modify_seekbar_min.setProgress(0);
//                    modify_seekbar_max.setProgress(0);
//                    mFilterColorView.fresh(0);
//                    mFilterColorView = Constants.display_images.get(viewpager.getCurrentItem());
//                    mFilterColorView.setPicB(0);
//                    mFilterColorView.setPicR(0);
//                    mFilterColorView.setPicG(0);
//                }
//            }
//        });
    }

//    public float[] getDifferentColor(int position) {
//        float[] colors = null;
//        switch (position) {
//            case 0://原图
//                colors = new float[]{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0};
//                break;
//            case 1://绿色
//                colors = new float[]{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0};
//                break;
//            case 2://紫色
//                colors = new float[]{1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0};
//                break;
//            case 3://灰色
//                colors = new float[]{0.33F, 0.59F, 0.11F, 0, 0, 0.33F, 0.59F, 0.11F, 0, 0, 0.33F, 0.59F, 0.11F, 0, 0, 0, 0, 0, 1, 0,};
//                break;
//            case 4://怀旧效果
//                colors = new float[]{0.393F, 0.769F, 0.189F, 0, 0, 0.349F, 0.686F, 0.168F, 0, 0, 0.272F, 0.534F, 0.131F, 0, 0, 0, 0, 0, 1, 0,};
//                break;
//            case 5://高饱和度
//                colors = new float[]{1.438F, -0.122F, -0.016F, 0, -0.03F, -0.062F, 1.378F, -0.016F, 0, 0.05F, -0.062F, -0.122F, 1.483F, 0, -0.02F, 0, 0, 0, 1, 0,};
//                break;
//            default:
//                break;
//        }
//        return colors;
//    }
    //获取不同的灰度值  第一个是最小值，第二个是最大值
    public double[] getDifferentGraeyLevel(int position) {
        double[] num = new double[2];
        switch (position) {
            case 0://原色
                num[0] = 1;
                num[1] = 1;
                break;
            case 1://红色效果1
                num[0] = 0.6;
                num[1] = 0.98;
                break;
            case 2://红色效果2
                num[0] = 0.1;
                num[1] = 0.98;
                break;
            case 3://绿色
                num[0] = 0.1;
                num[1] = 0.98;
                break;
            case 4://蓝色
                num[0] = 0.2;
                num[1] = 0.98;
                break;
            case 5://红色
                num[0] = 0.3;
                num[1] = 0.98;
                break;
            case 6://绿色
                num[0] = 0.2;
                num[1] = 0.8;
                break;
            case 7://蓝色
                num[0] = 0.02;
                num[1] = 0.9;
                break;
            case 8://红色
                num[0] = 0.1;
                num[1] = 0.7;
                break;
            case 9://蓝色
                num[0] = 0.1;
                num[1] = 0.9;
                break;
            case 10://红色
                num[0] = 0.15;
                num[1] = 0.85;
                break;
            case 11://绿色
                num[0] = 0.1;
                num[1] = 0.9;
                break;
            case 12://蓝色
                num[0] = 0.33;
                num[1] = 0.67;
                break;
        }
        return num;
    }
    //获取不同的色道
    public int getDifferentColorRamp(int position) {
        int number = 0;
        switch (position) {
            case 0:
                number = 4;
                break;
            case 1://红色通道
                number = 1;
                break;
            case 2:
                number = 1;
                break;
            case 3:
                number = 2;
                break;
            case 4:
                number = 3;
                break;
            case 5:
                number = 1;
                break;
            case 6:
                number = 2;
                break;
            case 7:
                number = 3;
                break;
            case 8:
                number = 1;
                break;
            case 9:
                number = 3;
                break;
            case 10:
                number = 1;
                break;
            case 11:
                number = 2;
                break;
            case 12:
                number = 3;
                break;
        }
        return number;
    }

//    /**
//     * 动态调整颜色的色组
//     *
//     * @param r 红色
//     * @param g 绿色
//     * @param b 蓝色
//     * @return
//     */
//    public float[] getColorArray(int r, int g, int b) {
//        float[] colors = null;
//        colors = new float[]{
//                1 * r, 0, 0, 0, 0,
//                0, 1 * g, 0, 0, 0,
//                0, 0, 1 * b, 0, 0,
//                0, 0, 0, 1, 0
//        };
//        return colors;
//    }

//    public Bitmap createBitmap(Bitmap bitmap, float[] coa) {
//        LogUtils.showVerbose("FilterColorView", "111");
//        Bitmap newBitmap = Bitmap.createBitmap(Constants.PHONE_WIDTH, Constants.PHONE_HEIGHT / 2, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(newBitmap);
//        ColorMatrix matrix = new ColorMatrix();
//        matrix.set(coa);
//        Paint mpaint = new Paint();
//        mpaint.setColorFilter(new ColorMatrixColorFilter(matrix));
//        if (Constants.PHONE_WIDTH / 2 - bitmap.getWidth() / 2 >= 0) {
//            canvas.drawBitmap(bitmap, Constants.PHONE_WIDTH / 2 - bitmap.getWidth() / 2, (Constants.PHONE_HEIGHT / 4 - bitmap.getHeight() / 2), mpaint);
//        } else {
//            canvas.drawBitmap(bitmap, 0, (Constants.PHONE_HEIGHT / 4 - bitmap.getHeight() / 2), mpaint);
//        }
//        bitmap = newBitmap;
//        return bitmap;
//    }

//    public Bitmap createBitmap1(Bitmap bitmap) {
//        Bitmap mNewbitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas newcanvas = new Canvas(mNewbitmap);
//        newcanvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.ANTI_ALIAS_FLAG)); //转为可修改
//
//        int[] pixelSize = new int[mNewbitmap.getWidth() * mNewbitmap.getHeight()];
//        int[] dstPixelSize = new int[mNewbitmap.getWidth() * mNewbitmap.getHeight()];
//
//        mNewbitmap.getPixels(pixelSize, 0, mNewbitmap.getWidth(), 0, 0, mNewbitmap.getWidth(), mNewbitmap.getHeight());
//
//        for (int i = 0; i < mNewbitmap.getWidth() * mNewbitmap.getHeight(); i++) {
//            int color = pixelSize[i];
//
//            int r = Color.red(color);
//            int g = Color.green(color);
//            int b = Color.blue(color);
//            int alpha = Color.alpha(color);
//            dstPixelSize[i] = Color.argb(alpha, getTranslateValue(r), getTranslateValue(g), getTranslateValue(b));
//        }
//        mNewbitmap.setPixels(dstPixelSize, 0, mNewbitmap.getWidth(), 0, 0, mNewbitmap.getWidth(), mNewbitmap.getHeight());
//        return mNewbitmap;
//    }


//    //获取修改后的图片
//    public Bitmap getModifyPic(Bitmap bitmap, int[] pixelSize){
//        Paint myPaint = new Paint();
//        Bitmap mNewbitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas newcanvas = new Canvas(mNewbitmap);
//        newcanvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.ANTI_ALIAS_FLAG)); //转为可修改
//
//        mNewbitmap.setPixels(pixelSize, 0, mNewbitmap.getWidth(), 0, 0, mNewbitmap.getWidth(), mNewbitmap.getHeight());
//        newcanvas.drawBitmap(mNewbitmap, Constants.PHONE_WIDTH / 2 - mNewbitmap.getWidth() / 2, (Constants.PHONE_HEIGHT / 4 - mNewbitmap.getHeight() / 2), myPaint);
//        return mNewbitmap;
//    }
//    //获取修改后的像素点数组
//    public int[] getPixelArray(Bitmap newbitmap, int flag_color,double minLevel,double maxLevel){
//        int[] mPixelSize = new int[newbitmap.getWidth() * newbitmap.getHeight()];
//        int[] mDstPixelSize = new int[newbitmap.getWidth() * newbitmap.getHeight()];
//        int[] mAllSize = new int[newbitmap.getWidth() * newbitmap.getHeight()];//获取一个空的直方图
//        newbitmap.getPixels(mPixelSize, 0, newbitmap.getWidth(), 0, 0, newbitmap.getWidth(), newbitmap.getHeight());
//        getHistogram(newbitmap,mPixelSize,flag_color,mAllSize);
//        setMaxLevel(maxLevel,newbitmap,mAllSize);
//        setMinLevel(minLevel,newbitmap,mAllSize);
//        for (int i = 0; i < newbitmap.getWidth() * newbitmap.getHeight(); i++) {
//            int color = mPixelSize[i];
//
//            int r = Color.red(color);
//            int g = Color.green(color);
//            int b = Color.blue(color);
//            int alpha = Color.alpha(color);
//            switch (flag_color){
//                case 1:
//                    //红色通道
//                    mDstPixelSize[i] = Color.argb(alpha, getTranslateValue(r), g, b);
//                    break;
//                case 2:
//                    //绿色通道
//                    mDstPixelSize[i] = Color.argb(alpha, r, getTranslateValue(g), b);
//                    break;
//                case 3:
//                    //蓝色通道
//                    mDstPixelSize[i] = Color.argb(alpha, r, g, getTranslateValue(b));
//                    break;
//            }
//
//        }
//        return mDstPixelSize;
//    }
//    //先把图片的像素点统计出来形成直方图，当直方图中累计的和达到相应的标准的时候，然后停止累加，然后把累加的这部分中的直方图的最大值和最小值作为里面的最大值和最小值
//    //获取直方图
//    public void getHistogram(Bitmap newbitmap, int[] mPixelSize, int flag_color, int[] mAllSize){
//        for (int i = 0; i < newbitmap.getWidth() * newbitmap.getHeight(); i++) {
//            int color = mPixelSize[i];
//
//            int r = Color.red(color);
//            int g = Color.green(color);
//            int b = Color.blue(color);
//            int alpha = Color.alpha(color);
//            switch (flag_color){
//                case 1:
//                    //红色通道
//                    mAllSize[r] = mAllSize[r] + 1;
//                    break;
//                case 2:
//                    //绿色通道
//                    mAllSize[g] = mAllSize[g] + 1;
//                    break;
//                case 3:
//                    //蓝色通道
//                    mAllSize[b] = mAllSize[b] + 1;
//                    break;
//            }
//        }
//    }
//    //获得需要的最大值和最小值
//    /*
//    * percent  表示需要的灰度比是多少
//    * */
//    public void setMaxLevel(double percent, Bitmap newbitmap, int[] mAllSize){
//        Constants.picStretchMax = getMaxValue(percent,newbitmap,mAllSize);
//    }
//    public void setMinLevel(double percent, Bitmap newbitmap, int[] mAllSize){
//        Constants.picStretchMin = getMinValue(percent,newbitmap,mAllSize);
//    }
//
//    public int getMaxValue(double percent, Bitmap newbitmap, int[] mAllSize){
//        //对直方图进行排序
//        int arr[] = new int[newbitmap.getHeight()*newbitmap.getWidth()];
//        int number = (int) (percent*newbitmap.getHeight()*newbitmap.getWidth());
//        LogUtils.showVerbose("CameraModifyMeiHuaFragment","最大值number="+number);
//        int max = mAllSize[0];
//        int allNumber = 0;
//        for (int i=0;i<mAllSize.length;i++){
//            if (max<=number){
//                if ((max +mAllSize[i]<=number)){
//                    max = max +mAllSize[i];
//                }
//            }else {
//                max = i;
//                return max;
//            }
//        }
//        return max;
//    }
//    public int getMinValue(double percent, Bitmap newbitmap, int[] mAllSize){
//        //对直方图进行排序
//        int number = (int) (percent*newbitmap.getHeight()*newbitmap.getWidth());
//        LogUtils.showVerbose("CameraModifyMeiHuaFragment","最小值number="+number);
//        int min = mAllSize[0];
//        for (int i=0;i<mAllSize.length;i++){
//            if (min<=number){
//                if ((min+mAllSize[i])<=number){
//                    min = min +mAllSize[i];
//                }
//            }else {
//                min = i;
//                return min;
//            }
//        }
//        return min;
//    }
//
//    //得到变换后的灰度值
//    public int getTranslateValue(int value) {
//        int currentValue = 0;
//        if (value < Constants.picStretchMin) {
//            currentValue = 0;
//        } else if (value > Constants.picStretchMax) {
//            currentValue = 255;
//        } else {
//            currentValue = (int) (((value - Constants.picStretchMin) * 255 * 1.00) / (Constants.picStretchMax - Constants.picStretchMin));
//        }
//        return currentValue;
//    }

    //通过url把地址转化为bitmap图片

}
