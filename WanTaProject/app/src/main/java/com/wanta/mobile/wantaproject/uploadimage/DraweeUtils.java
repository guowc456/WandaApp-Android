package com.wanta.mobile.wantaproject.uploadimage;

import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.wanta.mobile.wantaproject.utils.Constants;

/**
 * Created by WangYongqiang on 2016/11/16.
 */
public class DraweeUtils {
    private static String TAG = "DraweeUtils";

    // http://www.jianshu.com/p/5364957dcf49
    public static void showThumb(Uri uri, SimpleDraweeView draweeView){
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setAutoRotateEnabled(true)
                .setResizeOptions(new ResizeOptions(100, 100))
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .setOldController(draweeView.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        draweeView.setController(controller);
    }

    public static void resizepic(Uri uri, SimpleDraweeView draweeView){
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setAutoRotateEnabled(true)
                .setResizeOptions(new ResizeOptions(250, 250))
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .setOldController(draweeView.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        draweeView.setController(controller);
    }
    //加载衣橱中的图片
    public static void loadWardrobePics(Uri uri, SimpleDraweeView draweeView){
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setAutoRotateEnabled(true)
                .setResizeOptions(new ResizeOptions(150, 150))
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .setOldController(draweeView.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        draweeView.setController(controller);
    }
    //加载标签中的图片
    public static void loadTagsPics(Uri uri, SimpleDraweeView draweeView){
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setAutoRotateEnabled(true)
                .setResizeOptions(new ResizeOptions(85, 85))
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .setOldController(draweeView.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        draweeView.setController(controller);
    }
}

