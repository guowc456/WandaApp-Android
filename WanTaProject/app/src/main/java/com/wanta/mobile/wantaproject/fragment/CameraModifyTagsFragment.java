package com.wanta.mobile.wantaproject.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.OtherAuthorInputDescriptionActivity;
import com.wanta.mobile.wantaproject.activity.OtherAuthorInputPinPaiActivity;
import com.wanta.mobile.wantaproject.activity.OtherAuthorInputPriceActivity;
import com.wanta.mobile.wantaproject.activity.TagsDetailActivity;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/16.
 */
public class CameraModifyTagsFragment extends Fragment {

    private View view_tags;
    private TextView mTags_bag;
    private TextView mTags_belt;
    private TextView mTags_hat;
    private TextView mTags_other;
    private TextView mTags_scarf;
    private TextView mTags_shoes;
    private TextView mTags_skirt;
    private TextView mTags_trousers;
    private TextView mTags_upper;
    private List<String> tagsList = new ArrayList<>();
    private TextView mTags_confirm;
    private TextView mTags_cancle;
    private LinearLayout camera_modify_add_flags_layout;
    private TextView head_tv;
    private LinearLayout other_author_price_layout;
    private int currentPosition;
    private int image_publish_edit;
    private int jump_flag;
    private String price;
    private TextView pop_window_cancle_btn;
    private PopupWindow popupWindow;
    private TextView other_author_price_tv;
    private View pop_window_ok_btn;
    private LinearLayout other_author_description_layout;
    private TextView other_author_description_tv;
    private LinearLayout tag_pinpai_head_layout;
    private LinearLayout tag_pinpai_content_layout;
    private LinearLayout tag_jiage_content_layout;
    private LinearLayout tag_jiage_head_layout;
    private LinearLayout tag_gouyu_head_layout;
    private LinearLayout tag_gouyu_content_layout;
    private LinearLayout tag_miaosu_content_content;
    private LinearLayout tag_miaosu_head_content;
    private LinearLayout tag_miaosu_content_layout;
    private TextView other_author_pinpai_tv;
    private MyImageView other_author_pinpai_image;
    private MyImageView other_author_price_image;
    private MyImageView other_author_gouyu_image;
    private MyImageView other_author_description_image;
    private TextView other_author_gouyu_tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_tags = inflater.inflate(R.layout.fragment_camera_modify_tags,container,false);
//        initId();
        return view_tags;
    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        head_tv = (TextView) activity.findViewById(R.id.head_tv);
//        super.onAttach(activity);
//    }
//
//    private void initId() {
//        Bundle arguments = getArguments();
//        currentPosition = arguments.getInt("currentPosition");
//        image_publish_edit = arguments.getInt("image_publish_edit");
//        price = arguments.getString("price");
//        jump_flag = arguments.getInt("jump_flag");
//
//        if (jump_flag==1){
//            showPopWindows();
//        }
//        camera_modify_add_flags_layout = (LinearLayout) view_tags.findViewById(R.id.camera_modify_add_flags_layout);
//        camera_modify_add_flags_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPopWindows();
//            }
//        });
//    }
//
//    private void showPopWindows() {
//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_window_add_flags_layout,null);
//        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setTouchable(true);
//        popupWindow.setFocusable(true);
//        popupWindow.showAsDropDown(head_tv);
//        View contentView = popupWindow.getContentView();
//        MyImageView pop_window_select_icon = (MyImageView) contentView.findViewById(R.id.pop_window_select_icon);
//        pop_window_select_icon.setSize(Constants.PHONE_WIDTH/16,Constants.PHONE_WIDTH/16);
//        MyImageView pop_window_right_arrows_icon = (MyImageView) contentView.findViewById(R.id.pop_window_right_arrows_icon);
//        pop_window_right_arrows_icon.setSize(Constants.PHONE_WIDTH/26,Constants.PHONE_WIDTH/24);
//        //取消按钮时间
//        pop_window_cancle_btn = (TextView) contentView.findViewById(R.id.pop_window_cancle_btn);
//        pop_window_cancle_btn.setOnClickListener(this);
//        //确定按钮时间
//        pop_window_ok_btn = (TextView) contentView.findViewById(R.id.pop_window_ok_btn);
//        pop_window_ok_btn.setOnClickListener(this);
//        pop_window_cancle_btn = (TextView) contentView.findViewById(R.id.pop_window_cancle_btn);
//        pop_window_cancle_btn.setOnClickListener(this);
//        tag_jiage_content_layout = (LinearLayout) contentView.findViewById(R.id.tag_jiage_content_layout);
//        tag_jiage_content_layout.setOnClickListener(this);
//        other_author_price_tv = (TextView) contentView.findViewById(R.id.other_author_price_tv);
//        other_author_price_tv.setText(Constants.other_author_pop_window_price);
//        tag_miaosu_content_layout = (LinearLayout) contentView.findViewById(R.id.tag_miaosu_content_layout);
//        tag_miaosu_content_layout.setOnClickListener(this);
//        other_author_description_tv = (TextView) contentView.findViewById(R.id.other_author_description_tv);
//        other_author_description_tv.setText(Constants.other_author_pop_window_description);
//        other_author_pinpai_tv = (TextView) contentView.findViewById(R.id.other_author_pinpai_tv);
//        other_author_pinpai_tv.setText(Constants.other_author_pop_window_pinpai);
//        other_author_gouyu_tv = (TextView) contentView.findViewById(R.id.other_author_gouyu_tv);
//        other_author_gouyu_tv.setText(Constants.other_author_pop_window_gouyu);
//
//        other_author_pinpai_image = (MyImageView) contentView.findViewById(R.id.other_author_pinpai_image);
//        other_author_pinpai_image.setSize(Constants.PHONE_WIDTH/20,Constants.PHONE_WIDTH/20);
//        other_author_price_image = (MyImageView) contentView.findViewById(R.id.other_author_price_image);
//        other_author_price_image.setSize(Constants.PHONE_WIDTH/20,Constants.PHONE_WIDTH/20);
//        other_author_gouyu_image = (MyImageView) contentView.findViewById(R.id.other_author_gouyu_image);
//        other_author_gouyu_image.setSize(Constants.PHONE_WIDTH/20,Constants.PHONE_WIDTH/20);
//        other_author_description_image = (MyImageView) contentView.findViewById(R.id.other_author_description_image);
//        other_author_description_image.setSize(Constants.PHONE_WIDTH/20,Constants.PHONE_WIDTH/20);
//
//        //设置距离
//        tag_pinpai_head_layout = (LinearLayout) contentView.findViewById(R.id.tag_pinpai_head_layout);
//        LinearLayout.LayoutParams tag_pinpai_head_params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH/3,Constants.PHONE_HEIGHT/14);
//        tag_pinpai_head_layout.setLayoutParams(tag_pinpai_head_params);
//        tag_pinpai_content_layout = (LinearLayout) contentView.findViewById(R.id.tag_pinpai_content_layout);
//        LinearLayout.LayoutParams tag_pinpai_content_params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH/2,Constants.PHONE_HEIGHT/14);
//        tag_pinpai_content_params.setMargins(Constants.PHONE_WIDTH/20,0,0,0);
//        tag_pinpai_content_layout.setLayoutParams(tag_pinpai_content_params);
//        tag_pinpai_content_layout.setOnClickListener(this);
//
//        tag_jiage_head_layout = (LinearLayout) contentView.findViewById(R.id.tag_jiage_head_layout);
//        LinearLayout.LayoutParams tag_jiage_head_params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH/3,Constants.PHONE_HEIGHT/14);
//        tag_jiage_head_layout.setLayoutParams(tag_jiage_head_params);
//        tag_jiage_content_layout = (LinearLayout) contentView.findViewById(R.id.tag_jiage_content_layout);
//        LinearLayout.LayoutParams tag_jiage_content_params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH/2,Constants.PHONE_HEIGHT/14);
//        tag_jiage_content_params.setMargins(Constants.PHONE_WIDTH/20,0,0,0);
//        tag_jiage_content_layout.setLayoutParams(tag_jiage_content_params);
//
//        tag_gouyu_head_layout = (LinearLayout) contentView.findViewById(R.id.tag_gouyu_head_layout);
//        LinearLayout.LayoutParams tag_gouyu_head_params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH/3,Constants.PHONE_HEIGHT/14);
//        tag_gouyu_head_layout.setLayoutParams(tag_gouyu_head_params);
//        tag_gouyu_content_layout = (LinearLayout) contentView.findViewById(R.id.tag_gouyu_content_layout);
//        LinearLayout.LayoutParams tag_gouyu_content_params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH/2,Constants.PHONE_HEIGHT/14);
//        tag_gouyu_content_params.setMargins(Constants.PHONE_WIDTH/20,0,0,0);
//        tag_gouyu_content_layout.setLayoutParams(tag_gouyu_content_params);
//
//        tag_miaosu_head_content = (LinearLayout) contentView.findViewById(R.id.tag_miaosu_head_content);
//        LinearLayout.LayoutParams tag_miaosu_head_params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH/3,Constants.PHONE_HEIGHT/14);
//        tag_miaosu_head_content.setLayoutParams(tag_miaosu_head_params);
//        tag_miaosu_content_layout = (LinearLayout) contentView.findViewById(R.id.tag_miaosu_content_layout);
//        LinearLayout.LayoutParams tag_miaosu_content_params = new LinearLayout.LayoutParams(Constants.PHONE_WIDTH/2,Constants.PHONE_HEIGHT/14);
//        tag_miaosu_content_params.setMargins(Constants.PHONE_WIDTH/20,0,0,0);
//        tag_miaosu_content_layout.setLayoutParams(tag_miaosu_content_params);
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.tag_jiage_content_layout:
//                Intent intent = new Intent(getActivity(), OtherAuthorInputPriceActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                Bundle bundle = new Bundle();
//                bundle.putInt("currentPosition",currentPosition);
//                bundle.putInt("image_publish_edit",image_publish_edit);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                getActivity().finish();
//                break;
//            case R.id.tag_miaosu_content_layout:
//                Intent intent_to_description = new Intent(getActivity(), OtherAuthorInputDescriptionActivity.class);
//                intent_to_description.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                Bundle bundle_to_description = new Bundle();
//                bundle_to_description.putInt("currentPosition",currentPosition);
//                bundle_to_description.putInt("image_publish_edit",image_publish_edit);
//                intent_to_description.putExtras(bundle_to_description);
//                startActivity(intent_to_description);
//                getActivity().finish();
//                break;
//            case R.id.tag_pinpai_content_layout:
//                Intent intent_to_pinpai = new Intent(getActivity(), OtherAuthorInputPinPaiActivity.class);
//                intent_to_pinpai.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent_to_pinpai);
//                getActivity().finish();
//                break;
//            case R.id.pop_window_cancle_btn:
//                popupWindow.dismiss();
//                other_author_price_tv.setText("");
//                other_author_description_tv.setText("");
//                break;
//            case R.id.pop_window_ok_btn:
//                break;
//        }
//    }
}
