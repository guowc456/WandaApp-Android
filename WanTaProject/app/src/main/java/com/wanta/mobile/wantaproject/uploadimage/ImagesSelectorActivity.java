package com.wanta.mobile.wantaproject.uploadimage;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.BaseActivity;
import com.wanta.mobile.wantaproject.activity.CameraActivity;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.CacheDataHelper;
import com.wanta.mobile.wantaproject.domain.FolderItem;
import com.wanta.mobile.wantaproject.domain.FolderListContent;
import com.wanta.mobile.wantaproject.domain.ImageItem;
import com.wanta.mobile.wantaproject.domain.ImageListContent;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.DealReturnLogicUtils;
import com.wanta.mobile.wantaproject.utils.ImageCompressUtil;
import com.wanta.mobile.wantaproject.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ImagesSelectorActivity extends BaseActivity
        implements OnImageRecyclerViewInteractionListener, OnFolderRecyclerViewInteractionListener, View.OnClickListener {

    private static final String TAG = "ImageSelector";
    private static final String ARG_COLUMN_COUNT = "column-count";

    private static final int MY_PERMISSIONS_REQUEST_STORAGE_CODE = 197;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA_CODE = 341;

    private int mColumnCount = 3;

    private RecyclerView recyclerView;

    // folder selecting related
    private View mPopupAnchorView;
    private TextView mFolderSelectButton;
    private FolderPopupWindow mFolderPopupWindow;

    private String currentFolderPath;
    private ContentResolver contentResolver;

    private File mTempImageFile;
    private static final int CAMERA_REQUEST_CODE = 694;
    private TextView mCancle_select_photo;
    private TextView mContinue_select_photo;
    private LinearLayout mHead_title_layout;
    private MyImageView mHead_arrows;
    private LinearLayout mHead_layout;
    private LinearLayout mContinue_select_photo_layout;
    private LinearLayout mCancle_select_photo_layout;
    private ProgressDialog progressDialog;
    private int newSelectDay;
    private int newSelectYear;
    private int newSelectMonth;
    private int click_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_selector);


        // hide actionbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //获取来自LunchCameraActivity的数据
//        if ("calendarDetailCameraImageSelector".equals(getIntent().getStringExtra("calendarDetailCameraImageSelector"))) {
//            Bundle extras = getIntent().getExtras();
//            newSelectDay = extras.getInt("newSelectDay");
//            newSelectYear = extras.getInt("newSelectYear");
//            newSelectMonth = extras.getInt("newSelectMonth");
//        }

//        if ("topics_detail_to_imageselector".equals(getIntent().getStringExtra("topics_detail_to_imageselector"))) {
//            click_location = getIntent().getIntExtra("click_location", -1);
//        }
        // get parameters from bundle

        if (!"big_image_scan".equals(getIntent().getStringExtra("big_image_scan"))) {
            LogUtils.showVerbose("ImagesSelectorActivity", "我传的");
            ImageListContent.SELECTED_IMAGES.clear();
            Intent intent = getIntent();
            SelectorSettings.mMaxImageNumber = intent.getIntExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, SelectorSettings.mMaxImageNumber);
            SelectorSettings.isShowCamera = intent.getBooleanExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, SelectorSettings.isShowCamera);
            SelectorSettings.mMinImageSize = intent.getIntExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, SelectorSettings.mMinImageSize);
            SelectorSettings.isShowWardrobe = intent.getBooleanExtra(SelectorSettings.SELECTOR_SHOW_WARDROBE, SelectorSettings.isShowWardrobe);

            ArrayList<String> selected = intent.getStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST);
            if (selected != null && selected.size() > 0) {
                ImageListContent.SELECTED_IMAGES.addAll(selected);
            }
            Constants.IMAGE_URL.clear();
            Constants.modify_bitmap_list.clear();
            Constants.upload_images.clear();
            Constants.upload_images_lrucache.evictAll();
            Constants.upload_images_url.clear();
            Constants.modify_bitmap_list_url.clear();
            Constants.modify_bitmap_list_lrucache.evictAll();

        }

        ImageListContent.bReachMaxNumber = false;
        mHead_layout = (LinearLayout) this.findViewById(R.id.head_layout);
        mContinue_select_photo = (TextView) mHead_layout.findViewById(R.id.continue_select_photo);
//        mContinue_select_photo.setOnClickListener(this);
        mCancle_select_photo = (TextView) mHead_layout.findViewById(R.id.cancle_select_photo);
//        mCancle_select_photo.setOnClickListener(this);

        mContinue_select_photo_layout = (LinearLayout) mHead_layout.findViewById(R.id.continue_select_photo_layout);
        mContinue_select_photo_layout.setOnClickListener(this);
        mCancle_select_photo_layout = (LinearLayout) mHead_layout.findViewById(R.id.cancle_select_photo_layout);
        mCancle_select_photo_layout.setOnClickListener(this);
        mHead_title_layout = (LinearLayout) mHead_layout.findViewById(R.id.head_title_layout);

        // initialize recyclerview
        View rview = findViewById(R.id.image_recycerview);
        // Set the adapter
        if (rview instanceof RecyclerView) {
            Context context = rview.getContext();
            recyclerView = (RecyclerView) rview;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new ImageRecyclerViewAdapter(this, ImageListContent.IMAGES, this));
        }
//        mHead_title_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//
//                if (mFolderPopupWindow == null) {
//                    mFolderPopupWindow = new FolderPopupWindow();
//                    mFolderPopupWindow.initPopupWindow(ImagesSelectorActivity.this);
//                }
//
//                if (mFolderPopupWindow.isShowing()) {
//                    mFolderPopupWindow.dismiss();
//                    mHead_arrows.setImageResource(R.mipmap.head_arrows_up);
//                } else {
////                    mFolderPopupWindow.showAtLocation(mPopupAnchorView, Gravity.BOTTOM, 10, 150);
//                    mHead_arrows.setImageResource(R.mipmap.head_arrows_down);
//                }
//            }
//        });

        currentFolderPath = "";
        FolderListContent.clear();
        ImageListContent.clear();

        updateDoneButton();

        //定义一个加载图片的加载框，目的是确保通知到达，然后可以获取新的图片
        if (!"big_image_scan".equals(getIntent().getStringExtra("big_image_scan"))) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载图片...");
            progressDialog.show();
            handler.sendEmptyMessageDelayed(1, 200);
        } else {
            requestReadStorageRuntimePermission();
        }
//        requestReadStorageRuntimePermission();
    }

    public void requestReadStorageRuntimePermission() {
        if (ContextCompat.checkSelfPermission(ImagesSelectorActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ImagesSelectorActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_STORAGE_CODE);
        } else {
            LoadFolderAndImages();
        }
    }


    public void requestCameraRuntimePermissions() {
        if (ContextCompat.checkSelfPermission(ImagesSelectorActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(ImagesSelectorActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ImagesSelectorActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA_CODE);
        } else {
            launchCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    LoadFolderAndImages();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ImagesSelectorActivity.this, getString(R.string.selector_permission_error), Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_CAMERA_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    launchCamera();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ImagesSelectorActivity.this, getString(R.string.selector_permission_error), Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private final String[] projections = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media._ID};

    // this method is to load images and folders for all
    public void LoadFolderAndImages() {
        Log.d(TAG, "Load Folder And Images...");
        Observable.just("")
                .flatMap(new Func1<String, Observable<ImageItem>>() {
                    @Override
                    public Observable<ImageItem> call(String folder) {
                        List<ImageItem> results = new ArrayList<>();

                        Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        String where = MediaStore.Images.Media.SIZE + " > " + SelectorSettings.mMinImageSize;
//                        String where = MediaStore.Images.Media.SIZE + " > " + 0;
                        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

                        contentResolver = getContentResolver();
                        Cursor cursor = contentResolver.query(contentUri, projections, where, null, sortOrder);
                        if (cursor == null) {
                            Log.d(TAG, "call: " + "Empty images");
                        } else if (cursor.moveToFirst()) {
                            FolderItem allImagesFolderItem = null;
                            int pathCol = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                            int nameCol = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                            int DateCol = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
                            //清空所有大图中的照片
                            Constants.imageList.clear();
                            do {
                                String path = cursor.getString(pathCol);
                                String name = cursor.getString(nameCol);
                                long dateTime = cursor.getLong(DateCol);
                                Constants.imageList.add(path);
                                ImageItem item = new ImageItem(name, path, dateTime);

                                // if FolderListContent is still empty, add "All Images" option
                                if (FolderListContent.FOLDERS.size() == 0) {
                                    // add folder for all image
                                    FolderListContent.selectedFolderIndex = 0;

                                    // use first image's path as cover image path
                                    allImagesFolderItem = new FolderItem(getString(R.string.selector_folder_all), "", path);
                                    FolderListContent.addItem(allImagesFolderItem);

                                    // show camera icon ?
                                    if (SelectorSettings.isShowCamera) {
                                        results.add(ImageListContent.cameraItem);
                                        allImagesFolderItem.addImageItem(ImageListContent.cameraItem);
                                    }

                                    // show wardrobe icon ?
                                    if (SelectorSettings.isShowWardrobe) {
                                        results.add(ImageListContent.wardrobeItem);
                                        allImagesFolderItem.addImageItem(ImageListContent.wardrobeItem);
                                    }
                                }

                                // add image item here, make sure it appears after the camera icon
                                results.add(item);

                                // add current image item to all
                                allImagesFolderItem.addImageItem(item);

                                // find the parent folder for this image, and add path to folderList if not existed
                                String folderPath = new File(path).getParentFile().getAbsolutePath();
                                FolderItem folderItem = FolderListContent.getItem(folderPath);
                                if (folderItem == null) {
                                    // does not exist, create it
                                    folderItem = new FolderItem(StringUtils.getLastPathSegment(folderPath), folderPath, path);
                                    FolderListContent.addItem(folderItem);
                                }
                                folderItem.addImageItem(item);
                            } while (cursor.moveToNext());
                            cursor.close();
                        } // } else if (cursor.moveToFirst()) {
                        return Observable.from(results);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ImageItem>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + Log.getStackTraceString(e));
                    }

                    @Override
                    public void onNext(ImageItem imageItem) {
                        // Log.d(TAG, "onNext: " + imageItem.toString());
                        LogUtils.showVerbose("ImagesSelectorActivity", "imageItem.toString()=" + imageItem.toString());
                        ImageListContent.addItem(imageItem);
                        recyclerView.getAdapter().notifyItemChanged(ImageListContent.IMAGES.size() - 1);
                    }
                });
    }

    public void updateDoneButton() {
        if (ImageListContent.SELECTED_IMAGES.size() == 0) {
            mContinue_select_photo.setEnabled(false);
        } else {
            mContinue_select_photo.setEnabled(true);
        }

        String caption = getResources().getString(R.string.selector_action_done, ImageListContent.SELECTED_IMAGES.size(), SelectorSettings.mMaxImageNumber);
        mContinue_select_photo.setText(caption);
    }

    public void OnFolderChange() {
        mFolderPopupWindow.dismiss();

        FolderItem folder = FolderListContent.getSelectedFolder();
        if (!TextUtils.equals(folder.path, this.currentFolderPath)) {
            this.currentFolderPath = folder.path;
//            mFolderSelectButton.setText(folder.name);

            ImageListContent.IMAGES.clear();
            ImageListContent.IMAGES.addAll(folder.mImages);
            recyclerView.getAdapter().notifyDataSetChanged();
        } else {
            Log.d(TAG, "OnFolderChange: " + "Same folder selected, skip loading.");
        }
    }


    @Override
    public void onFolderItemInteraction(FolderItem item) {
        // dismiss popup, and update image list if necessary
        OnFolderChange();
    }

    @Override
    public void onImageItemInteraction(ImageItem item) {
        if (ImageListContent.bReachMaxNumber) {
            String hint = getResources().getString(R.string.selector_reach_max_image_hint, SelectorSettings.mMaxImageNumber);
            Toast.makeText(ImagesSelectorActivity.this, hint, Toast.LENGTH_SHORT).show();
            ImageListContent.bReachMaxNumber = false;
        }

        if (item.isCamera()) {
            requestCameraRuntimePermissions();
        }

        updateDoneButton();
    }


    public void launchCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // set the output file of camera
            try {
                mTempImageFile = FileUtils.createTmpFile(this);
            } catch (IOException e) {
                Log.e(TAG, "launchCamera: ", e);
            }
            if (mTempImageFile != null && mTempImageFile.exists()) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempImageFile));
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            } else {
                Toast.makeText(this, R.string.camera_temp_file_error, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // after capturing image, return the image path as selected result
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (mTempImageFile != null) {
                    // notify system
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mTempImageFile)));
                    LogUtils.showVerbose("ImagesSelectorActivity", "当前的路径:" + mTempImageFile);
                    Intent intent = new Intent(ImagesSelectorActivity.this, ImagesSelectorActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            } else {
                // if user click cancel, delete the temp file
                while (mTempImageFile != null && mTempImageFile.exists()) {
                    boolean success = mTempImageFile.delete();
                    if (success) {
                        mTempImageFile = null;
                    }
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v == mCancle_select_photo_layout) {
//            if ("calendarDetailCameraImageSelector".equals(getIntent().getStringExtra("calendarDetailCameraImageSelector"))) {
//                ActivityColection.removeActivity(this);
//                Intent intent = new Intent(ImagesSelectorActivity.this, WardrobeCalendarDetailActivity.class);
//                intent.putExtra("calendarDetailCameraImageSelector", "calendarDetailCameraImageSelector");
//                Bundle bundle = new Bundle();
//                bundle.putInt("newSelectDay", newSelectDay);
//                bundle.putInt("newSelectYear", newSelectYear);
//                bundle.putInt("newSelectMonth", newSelectMonth);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                finish();
//            } else {
//                Intent intent = new Intent(ImagesSelectorActivity.this, MainActivity.class);
//                intent.putExtra("select_cancel", "select_cancel");
//                startActivity(intent);
//                finish();
//            }
            DealReturnLogicUtils.dealReturnLogic(this);

        } else if (v == mContinue_select_photo_layout) {
            if (ImageListContent.SELECTED_IMAGES.size() != 0) {
                Constants.IMAGE_URL.clear();
                Constants.modify_bitmap_list.clear();
                Constants.upload_images.clear();
                Constants.modify_bitmap_list_url.clear();
                Constants.upload_images_url.clear();
                Constants.upload_images_lrucache.evictAll();
                Constants.modify_bitmap_list_lrucache.evictAll();
                //产生缓存图片的对象
//            LruCacheUtils lruCacheUtils = new LruCacheUtils();
                for (int j = 0; j < ImageListContent.SELECTED_IMAGES.size(); j++) {
                    if (!Constants.IMAGE_URL.contains(ImageListContent.SELECTED_IMAGES.get(j))) {
                        if (Constants.IMAGE_URL.size() <= 9) {
                            Constants.IMAGE_URL.add(ImageListContent.SELECTED_IMAGES.get(j));
//                        Constants.modify_bitmap_list.add(BitmapFactory.decodeFile(Constants.IMAGE_URL.get(j),getBitmapOption(5)));
//                        Constants.upload_images.add(BitmapFactory.decodeFile(Constants.IMAGE_URL.get(j),getBitmapOption(5)));
//                        Constants.modify_bitmap_list.add(ImageCompressUtil.compressBySize(Constants.IMAGE_URL.get(j),(Constants.PHONE_WIDTH/3)*2,Constants.PHONE_HEIGHT/2));
//                        Constants.upload_images.add(ImageCompressUtil.compressBySize(Constants.IMAGE_URL.get(j),(Constants.PHONE_WIDTH/3)*2,Constants.PHONE_HEIGHT/2));
//                        mLruCacheUtils.addBitmapToMemoryCache(Constants.IMAGE_URL.get(j),ImageCompressUtil.compressBySize(Constants.IMAGE_URL.get(j),(Constants.PHONE_WIDTH/3)*2,Constants.PHONE_HEIGHT/2));
                            Constants.modify_bitmap_list_lrucache.put(Constants.IMAGE_URL.get(j), ImageCompressUtil.compressBySize(Constants.IMAGE_URL.get(j), 600, 800));
                            Constants.upload_images_lrucache.put(Constants.IMAGE_URL.get(j), ImageCompressUtil.compressBySize(Constants.IMAGE_URL.get(j), 600, 800));
                            Constants.modify_bitmap_list_url.add(Constants.IMAGE_URL.get(j));
                            Constants.upload_images_url.add(Constants.IMAGE_URL.get(j));
                        }
                    }
                }
                CacheDataHelper.addNullArgumentsMethod();
                ActivityColection.addActivity(this);
                Intent data = new Intent(ImagesSelectorActivity.this, CameraActivity.class);
//            data.putStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS, ImageListContent.SELECTED_IMAGES);
                data.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                data.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(data);
                finish();
            } else {
                Toast.makeText(ImagesSelectorActivity.this, "请先选择图片", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (ActivityColection.isContains(this)) {
//                if ("topics_detail_to_imageselector".equals(getIntent().getStringExtra("topics_detail_to_imageselector"))){
//                    Intent intent = new Intent(ImagesSelectorActivity.this, TopicsDetailMessageActivity.class);
////                    intent.putExtra("topics_detail_to_imageselector","topics_detail_to_imageselector");
////                    intent.putExtra("click_location", click_location);
//                    startActivity(intent);
//                    finish();
//                }else if ("calendarDetailCameraImageSelector".equals(getIntent().getStringExtra("calendarDetailCameraImageSelector"))){
//                    ActivityColection.removeActivity(this);
//                    Intent intent = new Intent(ImagesSelectorActivity.this, WardrobeCalendarDetailActivity.class);
//                    intent.putExtra("calendarDetailCameraImageSelector","calendarDetailCameraImageSelector");
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("newSelectDay",newSelectDay);
//                    bundle.putInt("newSelectYear",newSelectYear);
//                    bundle.putInt("newSelectMonth",newSelectMonth);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                    finish();
//                }else {
//                    ActivityColection.removeActivity(this);
//                    Intent intent = new Intent(ImagesSelectorActivity.this, MainActivity.class);
//                    intent.putExtra("select_cancel", "select_cancel");
//                    startActivity(intent);
//                    finish();
//                }
//            }
            DealReturnLogicUtils.dealReturnLogic(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    private BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                progressDialog.dismiss();
                requestReadStorageRuntimePermission();
            }
        }
    };

}
