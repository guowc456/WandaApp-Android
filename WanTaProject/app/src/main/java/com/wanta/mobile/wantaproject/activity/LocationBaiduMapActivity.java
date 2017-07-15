package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.LocationBaiduMapAdapter;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.mordel.DomesticDbHelper;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.SaveMsgUtils;
import com.wanta.mobile.wantaproject.weixinutils.HttpCallBackListener;
import com.wanta.mobile.wantaproject.weixinutils.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2017/1/7.
 */
public class LocationBaiduMapActivity extends BaseActivity implements View.OnClickListener {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;
    boolean isFirstLoc = true;// 是否首次定位
    public MyLocationListenner myListener = new MyLocationListenner();
    private LinearLayout mLocation_baidu_select_city;
    private LinearLayout mLocation_baidu_select_location;
    private PopupWindow init_set_pop;
    private RecyclerView mLocation_baidu_map_recycleview;
    private TextView mLocation_baidu_map_city_txt;
    private TextView mLocation_baidu_select_location_txt;
    private LinearLayout mLocation_baidu_map_cancel_layout;
    private LinearLayout mLocation_baidu_map_ok_layout;
    private String currentCity  = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplication());
        setContentView(R.layout.activity_location_baidu_map);
        ActivityColection.addActivity(this);
        initId();
        initData();
    }

    private void initId() {
        mMapView = (MapView) this.findViewById(R.id.bmapView);
        mLocation_baidu_map_cancel_layout = (LinearLayout) this.findViewById(R.id.location_baidu_map_cancel_layout);
        mLocation_baidu_map_cancel_layout.setOnClickListener(this);
        mLocation_baidu_map_ok_layout = (LinearLayout) this.findViewById(R.id.location_baidu_map_ok_layout);
        mLocation_baidu_map_ok_layout.setOnClickListener(this);
        mLocation_baidu_select_city = (LinearLayout) this.findViewById(R.id.location_baidu_select_city);
        mLocation_baidu_select_city.setOnClickListener(this);
        mLocation_baidu_select_location = (LinearLayout) this.findViewById(R.id.location_baidu_select_location);
        mLocation_baidu_select_location.setOnClickListener(this);
        mLocation_baidu_map_city_txt = (TextView) this.findViewById(R.id.location_baidu_map_city_txt);
        mLocation_baidu_select_location_txt = (TextView) this.findViewById(R.id.location_baidu_map_location_txt);

    }

    private void initData() {
// 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        /**
         *setMyLocationEnabled(boolean enabled):设置是否允许定位图层
         * */
        mBaiduMap.setMyLocationEnabled(true);

        // 定位初始化
        mLocClient = new LocationClient(this);

        /**
         * registerLocationListener(BDLocationListener listener) :
         *              注册定位监听函数
         * */
        mLocClient.registerLocationListener(myListener);


        LocationClientOption option = new LocationClientOption();
        /**
         *setOpenGps(boolean openGps) :是否打开gps进行定位
         * */
        option.setOpenGps(true);

        /**
         *  setCoorType(java.lang.String coorType) :设置坐标类型
         * */
        option.setCoorType("bd09ll");

        /**
         *setScanSpan(int scanSpan) : 设置扫描间隔，单位是毫秒
         * */
        option.setScanSpan(1000);

        /**
         *setLocOption(LocationClientOption locOption) : 设置 LocationClientOption
         * */
        mLocClient.setLocOption(option);

        /**
         *start():启动定位sdk
         * */
        mLocClient.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mMapView = null;
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.location_baidu_map_cancel_layout:
//                Intent intent = new Intent(LocationBaiduMapActivity.this,CameraActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("baidumap","baidumap");
//                startActivity(intent);
//                finish();
                jumpToCameraActivity();
                break;
            case R.id.location_baidu_select_city:
                popwindows_selectCity();
                break;
            case R.id.location_baidu_select_location:
//                LogUtils.showVerbose("LocationBaiduMapActivity","我选择具体的地方了");
                popwindows_selectLocation();
                break;
            case R.id.location_baidu_map_ok_layout:
                //重新定位搜索
                if (!TextUtils.isEmpty(currentCity)){
                    //当前的定位信息不为空，定位新的城市


                }else {
                    //把当前的位置重新定位
                }
                break;
        }
    }
    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        /**
         *onReceiveLocation(BDLocation location):定位请求回调函数
         * */
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }

            /**
             *MyLocationData:定位数据
             *MyLocationData.Builder:定位数据建造器
             *accuracy:定位精度
             *direction:GPS定位时方向角度
             *latitude:百度纬度坐标
             *longitude:百度经度坐标
             * */
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            /**
             *setMyLocationData(MyLocationData data):
             *              设置定位数据, 只有先允许定位图层后设置数据才会生效，参见 setMyLocationEnabled(boolean)
             * */
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                /**
                 *LatLng:地理坐标基本数据结构
                 *BDLocation.getLatitude() : 获取纬度坐标
                 *BDLocation.getLongitude():获取经度坐标
                 * */
                LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
                /**
                 *MapStatusUpdateFactory.newLatLng(LatLng latLng):设置地图新中心点
                 * */
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    private void popwindows_selectCity() {
//        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_windows_baidu_map_layout, null);
//        init_set_pop = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
//
//        init_set_pop.setBackgroundDrawable(new BitmapDrawable());
//        init_set_pop.setOutsideTouchable(true);
//        init_set_pop.setFocusable(true);
//        init_set_pop.setTouchable(true);
//        //	set_pop.setAnimationStyle(R.style.Anim_popWindow_in);
//        //set_pop.showAtLocation(frame,Gravity.TOP, 0, 0);
//        init_set_pop.showAsDropDown(mLocation_baidu_select_city);
//        View my_init_pop = init_set_pop.getContentView();
//        mLocation_baidu_map_recycleview = (RecyclerView) my_init_pop.findViewById(R.id.location_baidu_map_recycleview);
//        try {
//
//            final DomesticDbHelper helper = new DomesticDbHelper(this);
//            LinearLayoutManager manager = new LinearLayoutManager(this);
//            manager.setOrientation(LinearLayoutManager.VERTICAL);
//            mLocation_baidu_map_recycleview.setLayoutManager(manager);
////            LocationBaiduMapAdapter adapter = new LocationBaiduMapAdapter(this,helper.getAllMessage());
////            mLocation_baidu_map_recycleview.setAdapter(adapter);
////            adapter.setOnLocationBaiduItemListener(new LocationBaiduMapAdapter.OnLocationBaiduItemListener() {
////                @Override
////                public void setItemClick(View view) {
////                    int position = mLocation_baidu_map_recycleview.getChildAdapterPosition(view);
////                    mLocation_baidu_map_city_txt.setText(helper.getAllMessage().get(position));
////                    currentCity = helper.getAllMessage().get(position);
////                    init_set_pop.dismiss();
////                }
////            });
//        } catch (Exception e) {
//            LogUtils.showVerbose("LocationBaiduMapActivity","生成json数组错误");
//        }
    }
    private void popwindows_selectLocation() {
//        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_windows_baidu_map_layout, null);
//        init_set_pop = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
//
//        init_set_pop.setBackgroundDrawable(new BitmapDrawable());
//        init_set_pop.setOutsideTouchable(true);
//        init_set_pop.setFocusable(true);
//        init_set_pop.setTouchable(true);
//        //	set_pop.setAnimationStyle(R.style.Anim_popWindow_in);
//        //set_pop.showAtLocation(frame,Gravity.TOP, 0, 0);
//        init_set_pop.showAsDropDown(mLocation_baidu_select_location);
//        View my_init_pop = init_set_pop.getContentView();
//        mLocation_baidu_map_recycleview = (RecyclerView) my_init_pop.findViewById(R.id.location_baidu_map_recycleview);
//        try {
//
//            final DomesticDbHelper helper = new DomesticDbHelper(this);
//            LinearLayoutManager manager = new LinearLayoutManager(this);
//            manager.setOrientation(LinearLayoutManager.VERTICAL);
//            mLocation_baidu_map_recycleview.setLayoutManager(manager);
//            LocationBaiduMapAdapter adapter = new LocationBaiduMapAdapter(this,helper.getAllMessage());
//            mLocation_baidu_map_recycleview.setAdapter(adapter);
//            adapter.setOnLocationBaiduItemListener(new LocationBaiduMapAdapter.OnLocationBaiduItemListener() {
//                @Override
//                public void setItemClick(View view) {
//                    int position = mLocation_baidu_map_recycleview.getChildAdapterPosition(view);
//                    mLocation_baidu_select_location_txt.setText(helper.getAllMessage().get(position));
//                    init_set_pop.dismiss();
//                }
//            });
//        } catch (Exception e) {
//            LogUtils.showVerbose("LocationBaiduMapActivity","生成json数组错误");
//        }
    }
    public void jumpToCameraActivity() {
        Intent intent = new Intent(LocationBaiduMapActivity.this, CameraActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("image_publish_edit", currentPosition);
        intent.putExtra("jump_flag", 1);
//        Constants.other_author_pop_window_pinpai = other_author_input_pinpai_msg_tv.getText().toString();
        startActivity(intent);
        finish();
    }

}
