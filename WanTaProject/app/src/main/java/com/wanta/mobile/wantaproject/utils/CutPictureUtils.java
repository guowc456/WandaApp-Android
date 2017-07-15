package com.wanta.mobile.wantaproject.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileNotFoundException;

/**
 * Created by WangYongqiang on 2016/12/27.
 */
public class CutPictureUtils {
    private Activity activity;
//    public final int CROP_ACTIVITY_RESULT=3;
    public CutPictureUtils(Activity activity){
        this.activity=activity;
    }
    private Uri imageUri=Uri.parse("file:///sdcard/temp.jpg");

    /*
    * 剪切图片
    */
    public Uri crop(Uri uri) {
        // 裁剪图片

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false); // no face detection
        activity.startActivityForResult(intent, Constants.CROP_ACTIVITY_RESULT);

        return imageUri;
    }

    public Bitmap decodeUriAsBitmap(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
