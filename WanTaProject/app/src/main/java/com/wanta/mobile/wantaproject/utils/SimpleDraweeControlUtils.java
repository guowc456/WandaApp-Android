package com.wanta.mobile.wantaproject.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by WangYongqiang on 2017/3/16.
 */
public class SimpleDraweeControlUtils {
    //获得到一个圆形的图像
    public static void getRingSimpleDraweeControl(Context context, CustomSimpleDraweeView simpleDraweeView, String picUrl) {
        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                //设置圆形圆角参数
                //.setRoundingParams(rp)
                //设置圆角半径
//                .setRoundingParams(RoundingParams.fromCornersRadius(25))
//                设置圆形圆角参数；RoundingParams.asCircle()是将图像设置成圆形
                .setRoundingParams(RoundingParams.asCircle())
                //设置淡入淡出动画持续时间(单位：毫秒ms)
//                .setFadeDuration(5000)
                //构建
                .build();

        //设置Hierarchy
        simpleDraweeView.setHierarchy(hierarchy);
        //构建Controller
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                //设置需要下载的图片地址
                .setUri(Uri.fromFile(new File(picUrl)))
                //构建
                .build();

        //设置Controller
        simpleDraweeView.setController(controller);

    }

    public static void getNetRingSimpleDraweeControl(Context context, CustomSimpleDraweeView simpleDraweeView, String picUrl) {
        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setCornersRadius(1);
        roundingParams.setRoundAsCircle(true);
        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                //设置圆形圆角参数
                .setRoundingParams(roundingParams)
                //设置圆角半径
//                .setRoundingParams(RoundingParams.fromCornersRadius(25))
//                设置圆形圆角参数；RoundingParams.asCircle()是将图像设置成圆形
//                .setRoundingParams(RoundingParams.asCircle())
                //设置淡入淡出动画持续时间(单位：毫秒ms)
//                .setFadeDuration(5000)
                //构建
                .build();

        //设置Hierarchy
        simpleDraweeView.setHierarchy(hierarchy);
        //构建Controller
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                //设置需要下载的图片地址
                .setUri(Uri.parse(picUrl))
                //构建
                .build();

        //设置Controller
        simpleDraweeView.setController(controller);

    }

    //获取一个四个角都是圆角的网络图片
    public static void getNetFourCornersSimpleDraweeControl(Context context, CustomSimpleDraweeView simpleDraweeView, Uri picUrl) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(picUrl)
                .setAutoRotateEnabled(true)
                .setResizeOptions(new ResizeOptions(250, 250))
                .build();
        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                //设置圆形圆角参数
                //.setRoundingParams(rp)
                //设置圆角半径
                .setRoundingParams(RoundingParams.fromCornersRadius(25))
//                设置圆形圆角参数；RoundingParams.asCircle()是将图像设置成圆形
//                .setRoundingParams(RoundingParams.asCircle())
                //设置淡入淡出动画持续时间(单位：毫秒ms)
//                .setFadeDuration(5000)
                //构建
                .build();

        //设置Hierarchy
        simpleDraweeView.setHierarchy(hierarchy);
        //构建Controller
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .setOldController(simpleDraweeView.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();

        //设置Controller
        simpleDraweeView.setController(controller);

    }

    //获取到一个四个角是圆角simpledrawee对象
    public static void getSimpleDraweeControl(Context context, CustomSimpleDraweeView simpleDraweeView, String picUrl) {
        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                //设置圆形圆角参数
                //.setRoundingParams(rp)
                //设置圆角半径
                .setRoundingParams(RoundingParams.fromCornersRadius(25))
                //设置圆形圆角参数；RoundingParams.asCircle()是将图像设置成圆形
                //.setRoundingParams(RoundingParams.asCircle())
                //设置淡入淡出动画持续时间(单位：毫秒ms)
//                .setFadeDuration(5000)
                //构建
                .build();

        //设置Hierarchy
        simpleDraweeView.setHierarchy(hierarchy);
        //构建Controller
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                //设置需要下载的图片地址
                .setUri(Uri.fromFile(new File(picUrl)))
                //构建
                .build();

        //设置Controller
        simpleDraweeView.setController(controller);

    }

    //设置assets中图片的大小和四个圆角
    public static void setAssetsImageRangle(Context context, CustomSimpleDraweeView simpleDraweeView,
                                            int radioSize, String picName) {
        String filePath = "/data/data/" + context.getPackageName() + "/wanda/" + picName;
        try {
            InputStream inputStream = context.getClass().getClassLoader().getResourceAsStream("assets/" + picName);
//            InputStream inputStream = getActivity().getResources().getAssets().open("popwindow_icon1.png");
            File file = new File(filePath);
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            file.createNewFile();

            OutputStream outputStream = new FileOutputStream(filePath);
            int length = 0;
            byte[] buffer = new byte[1024];
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            simpleDraweeView.setImageURI(Uri.fromFile(new File(filePath)));
            GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                    .setRoundingParams(RoundingParams.fromCornersRadius(radioSize))
                    .build();
            simpleDraweeView.setHierarchy(hierarchy);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    //设置需要下载的图片地址
                    .setUri(Uri.fromFile(new File(filePath)))
                    //构建
                    .build();
            simpleDraweeView.setController(controller);
        } catch (FileNotFoundException e) {
            LogUtils.showVerbose("NewWardrobeFragment", "文件写出失败");
        } catch (IOException e) {
            LogUtils.showVerbose("NewWardrobeFragment", "流读出失败");
        }
    }

    //设置assets中图片的上面的两个角为圆角
    public static void setAssetsImageTopRangle(Context context, CustomSimpleDraweeView simpleDraweeView,
                                               int radioSize, String picName) {
        String filePath = "/data/data/" + context.getPackageName() + "/wanda/" + picName;
        if (!(new File(filePath).exists())){
            try {
                InputStream inputStream = context.getClass().getClassLoader().getResourceAsStream("assets/" + picName);
//            InputStream inputStream = getActivity().getResources().getAssets().open("popwindow_icon1.png");
                File file = new File(filePath);
                File fileParent = file.getParentFile();
                if (!fileParent.exists()) {
                    fileParent.mkdirs();
                }
                file.createNewFile();

                OutputStream outputStream = new FileOutputStream(filePath);
                int length = 0;
                byte[] buffer = new byte[1024];
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            } catch (FileNotFoundException e) {
                LogUtils.showVerbose("NewWardrobeFragment", "文件写出失败");
            } catch (IOException e) {
                LogUtils.showVerbose("NewWardrobeFragment", "流读出失败");
            }
        }
        simpleDraweeView.setImageURI(Uri.fromFile(new File(filePath)));
        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setCornersRadii(radioSize, radioSize, 0, 0);
        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                .setRoundingParams(roundingParams)
                .build();
        simpleDraweeView.setHierarchy(hierarchy);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                //设置需要下载的图片地址
                .setUri(Uri.fromFile(new File(filePath)))
                //构建
                .build();
        simpleDraweeView.setController(controller);
    }
}
