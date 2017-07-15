package com.wanta.mobile.wantaproject.uploadimage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.MainActivity;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.ImageItem;
import com.wanta.mobile.wantaproject.domain.ImageListContent;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ViewHolder> {

    private final List<ImageItem> mValues;
    private final OnImageRecyclerViewInteractionListener mListener;
    private static final String TAG = "ImageAdapter";
    private Context mContext;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE_CODE = 197;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA_CODE = 341;
    private File mTempImageFile;

    public ImageRecyclerViewAdapter(Context mContext, List<ImageItem> items, OnImageRecyclerViewInteractionListener listener) {
        mValues = items;
        mListener = listener;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_image_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ImageItem imageItem = mValues.get(position);
        holder.mItem = imageItem;

        Uri newURI;
        if (!imageItem.isCamera() && !imageItem.isWardrobe()) {
            // draw image first
            File imageFile = new File(imageItem.path);
            if (imageFile.exists()) {
                newURI = Uri.fromFile(imageFile);
            } else {
                newURI = FileUtils.getUriByResId(R.mipmap.default_image);
            }
            DraweeUtils.showThumb(newURI, holder.mDrawee);

            holder.mImageName.setVisibility(View.GONE);
            holder.mChecked.setVisibility(View.VISIBLE);
            holder.mChecked.setSize(Constants.PHONE_WIDTH / 16, Constants.PHONE_WIDTH / 16);
            if (ImageListContent.isImageSelected(imageItem.path)) {
//                holder.mMask.setVisibility(View.VISIBLE);
                holder.mChecked.setImageResource(R.mipmap.image_selected);
            } else {
//                holder.mMask.setVisibility(View.GONE);
                holder.mChecked.setImageResource(R.mipmap.image_unselected);
            }
        } else if (imageItem.isWardrobe()) {
            newURI = FileUtils.getUriByResId(R.mipmap.wardrobe_icon);
            DraweeUtils.showThumb(newURI, holder.mDrawee);

            holder.mImageName.setVisibility(View.GONE);
            holder.mChecked.setVisibility(View.GONE);
            holder.mMask.setVisibility(View.GONE);
        } else {
            // camera icon, not normal image
            newURI = FileUtils.getUriByResId(R.mipmap.camera_icon);
            DraweeUtils.showThumb(newURI, holder.mDrawee);

            holder.mImageName.setVisibility(View.VISIBLE);
            holder.mChecked.setVisibility(View.GONE);
            holder.mMask.setVisibility(View.GONE);
        }
//        //点击图片可以查看原图
        holder.mDrawee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LogUtils.showVerbose("ImageRecyclerViewAdapter","我点击了");
                if (!holder.mItem.isWardrobe() && !holder.mItem.isCamera()) {
                    Intent intent = new Intent(mContext, BigImageScanActivity.class);
                    intent.putExtra("image_click_position", position - 2);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);
                } else if (holder.mItem.isWardrobe()) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("image_recycleview_adapter", "image_recycleview_adapter");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);
                } else {
//                    launchCamera();
                    mListener.onImageItemInteraction(holder.mItem);
                }

            }
        });
//        //点击选择标志，可以进行选择    点击当前的layout布局，增大点击的受力面积
        holder.mImage_checked_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.mItem.isCamera() && !holder.mItem.isWardrobe()) {
                    if (!ImageListContent.isImageSelected(imageItem.path)) {
                        // just select one new image, make sure total number is ok
                        if (ImageListContent.SELECTED_IMAGES.size() < SelectorSettings.mMaxImageNumber) {
                            ImageListContent.toggleImageSelected(imageItem.path);
//                            LogUtils.showVerbose("BigImageScanActivity","原path="+imageItem.path);
                            notifyItemChanged(position);
                        } else {
                            // set flag
                            ImageListContent.bReachMaxNumber = true;
                        }
                    } else {
                        // deselect
                        ImageListContent.toggleImageSelected(imageItem.path);
                        notifyItemChanged(position);
                    }
                }
                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
                    mListener.onImageItemInteraction(holder.mItem);
                }
            }
        });

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Log.d(TAG, "onClick: " + holder.mItem.toString());
//                if(!holder.mItem.isCamera()&&!holder.mItem.isWardrobe()) {
//                    if(!ImageListContent.isImageSelected(imageItem.path)) {
//                        // just select one new image, make sure total number is ok
//                        if(ImageListContent.SELECTED_IMAGES.size() < SelectorSettings.mMaxImageNumber) {
//                            ImageListContent.toggleImageSelected(imageItem.path);
//                            notifyItemChanged(position);
//                        } else {
//                            // set flag
//                            ImageListContent.bReachMaxNumber = true;
//                        }
//                    } else {
//                        // deselect
//                        ImageListContent.toggleImageSelected(imageItem.path);
//                        notifyItemChanged(position);
//                    }
//                } else if (holder.mItem.isWardrobe()){
//                    LogUtils.showVerbose("ImageRecyclerViewAdapter","我是衣橱");
//                    Intent intent = new Intent(mContext, MainActivity.class);
//                    intent.putExtra("image_recycleview_adapter","image_recycleview_adapter");
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    mContext.startActivity(intent);
//                }else {
//                    // do nothing here, listener will launch camera to capture image
//                }
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onImageItemInteraction(holder.mItem);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final SimpleDraweeView mDrawee;
        public final MyImageView mChecked;
        public final View mMask;
        public ImageItem mItem;
        public TextView mImageName;
        private final LinearLayout mImage_checked_layout;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDrawee = (SimpleDraweeView) view.findViewById(R.id.image_drawee);
            assert mDrawee != null;
            mMask = view.findViewById(R.id.image_mask);
            assert mMask != null;
            mChecked = (MyImageView) view.findViewById(R.id.image_checked);
            assert mChecked != null;
            mImageName = (TextView) view.findViewById(R.id.image_name);
            assert mImageName != null;
            mImage_checked_layout = (LinearLayout) view.findViewById(R.id.image_checked_layout);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH/12,Constants.PHONE_WIDTH/12);
//            mImage_checked_layout.setLayoutParams(params);
            assert mImage_checked_layout !=null;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
