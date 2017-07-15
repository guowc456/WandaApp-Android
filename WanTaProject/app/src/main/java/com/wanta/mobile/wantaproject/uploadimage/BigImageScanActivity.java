package com.wanta.mobile.wantaproject.uploadimage;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.BaseActivity;
import com.wanta.mobile.wantaproject.activity.CameraActivity;
import com.wanta.mobile.wantaproject.adapter.BigImageScanAdapter;
import com.wanta.mobile.wantaproject.customview.DetialGallery;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.ImageListContent;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.ImageCompressUtil;
import com.wanta.mobile.wantaproject.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WangYongqiang on 2016/12/20.
 */
public class BigImageScanActivity extends BaseActivity implements View.OnClickListener {

    private MyImageView mBig_image_scan_back;
    private TextView mBig_image_scan_ok;
    private MyImageView mBig_image_scan_select;
    private ViewPager mBig_image_scan_viewpager;
    private Map<Integer, SimpleDraweeView> map = new HashMap<>();
    private LinearLayout mBig_image_scan_head_layout;
    private LinearLayout mBig_image_scan_bottom_layout;
    private DetialGallery mBig_image_scan_gallery;
    private int currentPosition = 0;
    private LinearLayout mBig_image_scan_select_layout;
    private LinearLayout mBig_image_scan_back_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image_scan);
        ActivityColection.addActivity(this);
        initId();

    }

    private void initId() {
        mBig_image_scan_head_layout = (LinearLayout) this.findViewById(R.id.big_image_scan_head_layout);
        mBig_image_scan_bottom_layout = (LinearLayout) this.findViewById(R.id.big_image_scan_bottom_layout);
        mBig_image_scan_bottom_layout.setOnClickListener(this);
        mBig_image_scan_back = (MyImageView) this.findViewById(R.id.big_image_scan_back);
        mBig_image_scan_back.setSize(Constants.PHONE_WIDTH / 18, Constants.PHONE_WIDTH / 18);
//        mBig_image_scan_back.setOnClickListener(this);

        mBig_image_scan_back_layout = (LinearLayout) this.findViewById(R.id.big_image_scan_back_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH/6,Constants.PHONE_WIDTH/8);
        mBig_image_scan_back_layout.setLayoutParams(params);
        mBig_image_scan_back_layout.setOnClickListener(this);

        mBig_image_scan_ok = (TextView) this.findViewById(R.id.big_image_scan_ok);
//        mBig_image_scan_ok.setOnClickListener(this);
        mBig_image_scan_select = (MyImageView) this.findViewById(R.id.big_image_scan_select);
//        mBig_image_scan_select.setOnClickListener(this);
        mBig_image_scan_select.setSize(Constants.PHONE_WIDTH / 18, Constants.PHONE_WIDTH / 18);
        mBig_image_scan_select_layout = (LinearLayout) this.findViewById(R.id.big_image_scan_select_layout);
        mBig_image_scan_select_layout.setOnClickListener(this);

        mBig_image_scan_gallery = (DetialGallery) this.findViewById(R.id.big_image_scan_gallery);
        final BigImageScanAdapter adapter = new BigImageScanAdapter(this,Constants.imageList);
        mBig_image_scan_gallery.setAdapter(adapter);
        mBig_image_scan_gallery.setSpacing(Constants.PHONE_WIDTH/6);
        //获取传过来的位置
        int image_click_position = getIntent().getIntExtra("image_click_position", 10000);
        LogUtils.showVerbose("BigImageScanActivity","image_click_position="+image_click_position);
        if (image_click_position!=10000){

            mBig_image_scan_gallery.setSelection(image_click_position);
//            adapter.notifyDataSetInvalidated();
        }else {
            mBig_image_scan_gallery.setSelection(0);
        }
        //获取之前选择的位置
        mBig_image_scan_gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                String path = Constants.imageList.get(position);
                if (ImageListContent.isImageSelected(path)){
                    //说明当前的图片已经选择过了
                    mBig_image_scan_select.setImageResource(R.mipmap.image_selected);
                }else {
                    mBig_image_scan_select.setImageResource(R.mipmap.image_unselected);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.big_image_scan_back_layout:
//                LogUtils.showVerbose("BigImageScanActivity","点击了");
                jumpImageSelectorActivity();
                break;
            case R.id.big_image_scan_select_layout:
                if(!ImageListContent.isImageSelected(Constants.imageList.get(currentPosition))) {
                    // just select one new image, make sure total number is ok
                    if(ImageListContent.SELECTED_IMAGES.size() < SelectorSettings.mMaxImageNumber) {
                        ImageListContent.toggleImageSelected(Constants.imageList.get(currentPosition));
                        mBig_image_scan_select.setImageResource(R.mipmap.image_selected);
                    } else {
                        ImageListContent.bReachMaxNumber = true;
//                        Toast.makeText(BigImageScanActivity.this,"您已经选择了9张图片了",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // deselect
                    ImageListContent.toggleImageSelected(Constants.imageList.get(currentPosition));
                    mBig_image_scan_select.setImageResource(R.mipmap.image_unselected);
                }
                break;
            case R.id.big_image_scan_bottom_layout:
                if (ImageListContent.SELECTED_IMAGES.size()!=0){
                    Constants.IMAGE_URL.clear();
                    Constants.modify_bitmap_list.clear();
                    Constants.upload_images.clear();
                    Constants.modify_bitmap_list_url.clear();
                    Constants.upload_images_url.clear();
                    Constants.upload_images_lrucache.evictAll();
                    Constants.modify_bitmap_list_lrucache.evictAll();
                    for (int j=0;j<ImageListContent.SELECTED_IMAGES.size();j++){
                        if (!Constants.IMAGE_URL.contains(ImageListContent.SELECTED_IMAGES.get(j))){
                            if (Constants.IMAGE_URL.size()<=9){
                                Constants.IMAGE_URL.add(ImageListContent.SELECTED_IMAGES.get(j));
//                            Constants.modify_bitmap_list.add(BitmapFactory.decodeFile(Constants.IMAGE_URL.get(j),getBitmapOption(5)));
//                            Constants.upload_images.add(BitmapFactory.decodeFile(Constants.IMAGE_URL.get(j),getBitmapOption(5)));
//                            Constants.modify_bitmap_list.add(ImageCompressUtil.compressBySize(Constants.IMAGE_URL.get(j),(Constants.PHONE_WIDTH/3)*2,Constants.PHONE_HEIGHT/2));
//                            Constants.upload_images.add(ImageCompressUtil.compressBySize(Constants.IMAGE_URL.get(j),(Constants.PHONE_WIDTH/3)*2,Constants.PHONE_HEIGHT/2));
                                Constants.modify_bitmap_list_lrucache.put(Constants.IMAGE_URL.get(j), ImageCompressUtil.compressBySize(Constants.IMAGE_URL.get(j), 600, 800));
                                Constants.upload_images_lrucache.put(Constants.IMAGE_URL.get(j), ImageCompressUtil.compressBySize(Constants.IMAGE_URL.get(j), 600, 800));
                                Constants.modify_bitmap_list_url.add(Constants.IMAGE_URL.get(j));
                                Constants.upload_images_url.add(Constants.IMAGE_URL.get(j));
                            }
                        }
                    }
                    Intent data = new Intent(BigImageScanActivity.this, CameraActivity.class);
//            data.putStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS, ImageListContent.SELECTED_IMAGES);
                    startActivity(data);
                    finish();
                }else {
                    Toast.makeText(BigImageScanActivity.this,"请选择图片",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (ActivityColection.isContains(this)){
                ActivityColection.removeActivity(this);
                jumpImageSelectorActivity();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void jumpImageSelectorActivity() {
        Intent intent = new Intent(BigImageScanActivity.this, ImagesSelectorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("big_image_scan","big_image_scan");
        startActivity(intent);
        finish();
    }
    private BitmapFactory.Options getBitmapOption(int inSampleSize){
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }
}
