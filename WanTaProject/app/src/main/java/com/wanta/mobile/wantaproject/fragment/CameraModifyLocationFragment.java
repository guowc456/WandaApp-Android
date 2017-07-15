package com.wanta.mobile.wantaproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.LocationBaiduMapActivity;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by WangYongqiang on 2016/10/16.
 */
public class CameraModifyLocationFragment extends Fragment {

    private View view_location;
    private ImageView mCamera_modify_location_image;
    private TextView mCamera_modify_location_location;
    private MyImageView mCamera_modify_location_icon;
    private MyImageView mCamera_modify_temperature_icon;
    private TextView mCamera_modify_location_weather;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view_location = inflater.inflate(R.layout.fragment_camera_modify_location,container,false);
        initId();
        return view_location;
    }

    private void initId() {
        mCamera_modify_location_icon = (MyImageView) view_location.findViewById(R.id.camera_modify_location_icon);
        mCamera_modify_location_icon.setSize(Constants.PHONE_WIDTH/11,Constants.PHONE_WIDTH/11);
        mCamera_modify_temperature_icon = (MyImageView) view_location.findViewById(R.id.camera_modify_temperature_icon);
        mCamera_modify_temperature_icon.setSize(Constants.PHONE_WIDTH/15,Constants.PHONE_WIDTH/15);
        mCamera_modify_location_image = (ImageView) view_location.findViewById(R.id.camera_modify_location_image);
        mCamera_modify_location_image.setMaxWidth(Constants.PHONE_WIDTH);
        mCamera_modify_location_image.setMaxHeight(Constants.PHONE_HEIGHT/5);
        mCamera_modify_location_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocationBaiduMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });
        mCamera_modify_location_location = (TextView) view_location.findViewById(R.id.camera_modify_location_location);
        mCamera_modify_location_location.setText(Constants.CURRENT_ADDRESS);
        mCamera_modify_location_weather = (TextView) view_location.findViewById(R.id.camera_modify_location_weather);
        getTemper();
    }
    private void getTemper() {
        String city = getCurrentCity(Constants.CURRENT_CITY);
        //获取当前的温度信息
        MyHttpUtils.getNetMessage(getActivity(), "http://1zou.me/api/temper/" + city, new MyHttpUtils.Callback() {
            @Override
            public void getResponseMsg(String response) {
                LogUtils.showVerbose("WardrobeCalendarDetailActivity", "response=" + response);
//                {"low":1,"high":0}
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int low = jsonObject.getInt("low");
                    int high = jsonObject.getInt("high");
                    mCamera_modify_location_weather.setText(low + "°C-" + high + "°C");
                } catch (JSONException e) {
                    LogUtils.showVerbose("WardrobeCalendarDetailActivity", "温度信息解析错误");
                }
            }
        }, new MyHttpUtils.CallbackError() {
            @Override
            public void getResponseMsg(String error) {

            }
        });
    }

    private String getCurrentCity(String currentCity) {
        String city = "";
        if (currentCity.contains("市")){
            city =  currentCity.substring(0,currentCity.length()-1);
        }else {
            city = currentCity ;
        }
        LogUtils.showVerbose("WardrobeCalendarDetailActivity", "city="+city);
        return city;
    }
}
