package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.NewLocationBaiduMapAdapter;
import com.wanta.mobile.wantaproject.mordel.DomesticDbHelper;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2017/1/7.
 */
public class NewLocationBaiduMapActivity extends BaseActivity implements View.OnClickListener {


    private MapView mMapView;
    private LinearLayout mLocation_baidu_map_cancel_layout;
    private LinearLayout mLocation_baidu_map_ok_layout;
    private LinearLayout mLocation_baidu_select_city;
    private LinearLayout mLocation_baidu_select_location;
    private TextView mLocation_baidu_map_city_txt;
    private EditText mLocation_baidu_select_location_txt;
    private int checkPosition;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private int locType;
    private double longitude;
    private double latitude;
    private String addrStr;
    private PoiSearch mPoiSearch;
    public BDLocationListener myListener = new MyLocationListener();
    private List<PoiInfo> dataList;
    private PopupWindow init_set_pop;
    private RecyclerView mLocation_baidu_map_recycleview;
    private NewLocationBaiduMapAdapter adapter;
    private int loadIndex = 0;
    boolean isFirstLoc = true;// 是否首次定位
    private int catogry = 1;//1表示城市选择   2表示具体地址的书写
    private String currentCity = "";
    private PoiInfo currentPoInfo = null;
    private float radius;
    private float direction;
    private String province;
    private String city;
    private String district;
    private int currentPosition;
    private int image_publish_edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplication());
        setContentView(R.layout.activity_location_baidu_map);
        ActivityColection.addActivity(this);
        Bundle extras = getIntent().getExtras();
        currentPosition = extras.getInt("currentPosition");
        image_publish_edit = extras.getInt("image_publish_edit");
        initId();
        initLocation();
    }

    private void initId() {
        mMapView = (MapView) this.findViewById(R.id.bmapView);
        mPoiSearch = PoiSearch.newInstance();
        mLocation_baidu_map_cancel_layout = (LinearLayout) this.findViewById(R.id.location_baidu_map_cancel_layout);
        mLocation_baidu_map_cancel_layout.setOnClickListener(this);
        mLocation_baidu_map_ok_layout = (LinearLayout) this.findViewById(R.id.location_baidu_map_ok_layout);
        mLocation_baidu_map_ok_layout.setOnClickListener(this);
        mLocation_baidu_select_city = (LinearLayout) this.findViewById(R.id.location_baidu_select_city);
        mLocation_baidu_select_city.setOnClickListener(this);
        mLocation_baidu_select_location = (LinearLayout) this.findViewById(R.id.location_baidu_select_location);
        mLocation_baidu_select_location.setOnClickListener(this);
        mLocation_baidu_map_city_txt = (TextView) this.findViewById(R.id.location_baidu_map_city_txt);
        mLocation_baidu_map_city_txt.setOnClickListener(this);
        mLocation_baidu_select_location_txt = (EditText) this.findViewById(R.id.location_baidu_map_location_txt);
        dataList = new ArrayList<PoiInfo>();
    }

    /**
     * 定位
     */
    private void initLocation() {
        //重新设置
        mBaiduMap = mMapView.getMap();
        mBaiduMap.clear();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(17).build()));   // 设置级别

        // 定位初始化
        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener);// 注册定位监听接口

        /**
         * 设置定位参数
         */
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
//		option.setScanSpan(5000);// 设置发起定位请求的间隔时间,ms
        option.setNeedDeviceDirect(true);// 设置返回结果包含手机的方向
        option.setOpenGps(true);
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        mLocationClient.setLocOption(option);
        mLocationClient.start(); // 调用此方法开始定位
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.location_baidu_select_city:
                catogry = 2;
                popwindows_selectCity();
                break;
            case R.id.location_baidu_map_ok_layout:
                mLocation_baidu_select_location_txt.requestFocus();
                //点击确定
                if (!TextUtils.isEmpty(mLocation_baidu_select_location_txt.getText().toString())) {
                    Constants.other_author_pop_window_gouyu = mLocation_baidu_select_location_txt.getText().toString();
                }
               jumpToCameraActivity();
                finish();
                break;
            case R.id.location_baidu_map_cancel_layout:
                Constants.other_author_pop_window_gouyu = "";
               jumpToCameraActivity();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (ActivityColection.isContains(this)){
                ActivityColection.removeActivity(this);
                jumpToCameraActivity();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void jumpToCameraActivity(){
        Intent intent = new Intent();
        intent.setClass(NewLocationBaiduMapActivity.this, CameraActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("currentPosition", currentPosition);
        intent.putExtra("jump_flag",1);
        startActivity(intent);
        finish();
    }
    /**
     * 定位SDK监听函数
     *
     * @author
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null) {
                return;
            }

            locType = location.getLocType();
            LogUtils.showVerbose("NewLocationBaiduMapActivity", "当前定位的返回值是：" + locType);

            longitude = location.getLongitude();
            latitude = location.getLatitude();
            if (location.hasRadius()) {// 判断是否有定位精度半径
                radius = location.getRadius();
            }

            if (locType == BDLocation.TypeNetWorkLocation) {
                addrStr = location.getAddrStr();// 获取反地理编码(文字描述的地址)
                LogUtils.showVerbose("NewLocationBaiduMapActivity", "当前定位的地址是：" + addrStr);
            }

            direction = location.getDirection();// 获取手机方向，【0~360°】,手机上面正面朝北为0°
            province = location.getProvince();// 省份
            city = location.getCity();// 城市
            district = location.getDistrict();// 区县

            LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.icon_marka);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                /**
                 *LatLng:地理坐标基本数据结构
                 *BDLocation.getLatitude() : 获取纬度坐标
                 *BDLocation.getLongitude():获取经度坐标
                 * */
                LatLng ll1 = new LatLng(location.getLatitude(), location.getLongitude());
                /**
                 *MapStatusUpdateFactory.newLatLng(LatLng latLng):设置地图新中心点
                 * */
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll1);
                mBaiduMap.animateMapStatus(u);
            }

            mMapView.showZoomControls(false);
        }

    }

    private void searchCity() {
        OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
            public void onGetPoiResult(PoiResult result) {
                //获取POI检索结果
                if (result.getCurrentPageNum() == 0) {
                    dataList.clear();
                }
                LogUtils.showVerbose("NewLocationBaiduMapActivity", "当前的页面外面:" + result.getCurrentPageNum());
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
                    if (result != null) {
                        if (loadIndex < 2) {
                            LogUtils.showVerbose("NewLocationBaiduMapActivity", "当前的页面:" + result.getCurrentPageNum());
                            if (result.getAllPoi() != null && result.getAllPoi().size() > 0) {
                                for (int i = 0; i < result.getAllPoi().size(); i++) {
                                    LogUtils.showVerbose("NewLocationBaiduMapActivity", "数目:" + result.getTotalPageNum() + "geshu" + result.getTotalPoiNum() + result.getAllPoi().get(i).name + "" + result.getAllPoi().get(i).address);
                                    dataList.add(result.getAllPoi().get(i));
                                    LogUtils.showVerbose("NewLocationBaiduMapActivity", "dataList的大小:" + dataList.size());
                                }
                            }
                            goToNextPage();
                        } else if (loadIndex == 2) {
                            popwindows_selectCity();
                        }
                    }
                }
            }

            public void onGetPoiDetailResult(PoiDetailResult result) {
                //获取Place详情页检索结果
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        };
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city(currentCity)
                .keyword("生活服务")
                .pageNum(loadIndex));
    }

    public void goToNextPage() {
        loadIndex++;
        searchButtonProcess();
    }

    public void searchButtonProcess() {
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city(currentCity)
                .keyword("生活服务")
                .pageNum(loadIndex));
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        if (mLocationClient != null) {
            mLocationClient.stop();
        }

        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mPoiSearch.destroy();
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    private void popwindows_selectCity() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_windows_baidu_map_layout, null);
        init_set_pop = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);

        init_set_pop.setBackgroundDrawable(new BitmapDrawable());
        init_set_pop.setOutsideTouchable(true);
        init_set_pop.setFocusable(true);
        init_set_pop.setTouchable(true);
        //	set_pop.setAnimationStyle(R.style.Anim_popWindow_in);
        //set_pop.showAtLocation(frame,Gravity.TOP, 0, 0);
        init_set_pop.showAsDropDown(mLocation_baidu_select_city);

        View my_init_pop = init_set_pop.getContentView();
        mLocation_baidu_map_recycleview = (RecyclerView) my_init_pop.findViewById(R.id.location_baidu_map_recycleview);
        try {
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            mLocation_baidu_map_recycleview.setLayoutManager(manager);
            adapter = new NewLocationBaiduMapAdapter();
            if (catogry == 1) {
                adapter.setPoiInfoMessage(this, dataList, 1);
            } else if (catogry == 2) {
                DomesticDbHelper helper = new DomesticDbHelper(this);
                List<String> allMessage = helper.getAllMessage();
                adapter.setCityNameMessage(this, allMessage, 2);
            }
            mLocation_baidu_map_recycleview.setAdapter(adapter);
            if (catogry == 1) {
                adapter.setOnNewLocationBaiduItemListener(new NewLocationBaiduMapAdapter.OnNewLocationBaiduItemListener() {
                    @Override
                    public void setItemClick(PoiInfo poiInfo) {
                        currentPoInfo = poiInfo;
                        // 构造定位数据
                        LatLng point = new LatLng(poiInfo.location.latitude, poiInfo.location.longitude);
                        //构建Marker图标
                        BitmapDescriptor bitmap = BitmapDescriptorFactory
                                .fromResource(R.mipmap.icon_marka);
                        //构建MarkerOption，用于在地图上添加Marker
                        OverlayOptions option = new MarkerOptions()
                                .position(point)
                                .icon(bitmap);
                        //在地图上添加Marker，并显示
                        mBaiduMap.addOverlay(option);
                        LogUtils.showVerbose("NewLocationBaiduMapActivity", "选中产生的坐标" + poiInfo.location);
                        LatLng ll1 = new LatLng(poiInfo.location.latitude, poiInfo.location.longitude);
                        /**
                         *MapStatusUpdateFactory.newLatLng(LatLng latLng):设置地图新中心点
                         * */
                        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll1);
                        mBaiduMap.animateMapStatus(u);
                        Constants.other_author_pop_window_gouyu = poiInfo.address;
                        init_set_pop.dismiss();
                    }
                });
            } else if (catogry == 2) {
                adapter.setOnNewLocationCityItemListener(new NewLocationBaiduMapAdapter.OnNewLocationCityItemListener() {
                    @Override
                    public void setItemClick(String cityName) {
                        currentCity = cityName;
                        loadIndex = 0;
                        catogry = 1;
                        mLocation_baidu_map_city_txt.setText(cityName);
                        Constants.other_author_pop_window_gouyu = cityName;
                        LogUtils.showVerbose("NewLocationBaiduMapActivity", "选中的城市名字:" + cityName);
//                        addressToCoordinate();
                        searchCity();
                        init_set_pop.dismiss();
                    }
                });
            }

            //判断是否滑到最低了
            mLocation_baidu_map_recycleview.setOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (isSlideToBottom(recyclerView)) {
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        } catch (Exception e) {
            LogUtils.showVerbose("LocationBaiduMapActivity", "生成json数组错误");
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                popwindows_selectCity();
            }
        }
    };

    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }
}
