package com.wanta.mobile.wantaproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.ByteArrayBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.MainActivity;
import com.wanta.mobile.wantaproject.activity.NewPublishActivity;
import com.wanta.mobile.wantaproject.activity.WardrobeCalendarDetailActivity;
import com.wanta.mobile.wantaproject.adapter.OtherAuthorRecycviewAdapter;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.domain.CacheDataHelper;
import com.wanta.mobile.wantaproject.domain.MostPopularInfo;
import com.wanta.mobile.wantaproject.phonecamera.LunchCameraActivity;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;
import com.wanta.mobile.wantaproject.utils.NetUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/16.
 */
public class CalendarDetailPublishArginFragment extends Fragment implements View.OnClickListener {

    private View mFragment_camera_detail_public_again_view;
    private CustomSimpleDraweeView mCalendar_detail_publish_again_icon;
    private LinearLayout calendar_detail_publish_again_layout;
    private List<Integer> list;
    private RecyclerView calendar_detail_publish_recycview;
    private List<MostPopularInfo> all_message = new ArrayList<>();
    private OtherAuthorRecycviewAdapter mAdapter;
    private String currentPictureMsg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment_camera_detail_public_again_view = inflater.inflate(R.layout.fragment_camera_detail_public_again,container,false);
        init();
        initNetMsg();
        return mFragment_camera_detail_public_again_view;
    }


    private void init() {
        mCalendar_detail_publish_again_icon = (CustomSimpleDraweeView) mFragment_camera_detail_public_again_view.findViewById(R.id.calendar_detail_publish_again_icon);
        mCalendar_detail_publish_again_icon.setHeight(Constants.PHONE_HEIGHT/4);
        mCalendar_detail_publish_again_icon.setWidth((int) ((Constants.PHONE_WIDTH*1.00/4)*3));
        calendar_detail_publish_again_layout = (LinearLayout) mFragment_camera_detail_public_again_view.findViewById(R.id.calendar_detail_publish_again_layout);
        calendar_detail_publish_again_layout.setOnClickListener(this);
        calendar_detail_publish_recycview = (RecyclerView) mFragment_camera_detail_public_again_view.findViewById(R.id.calendar_detail_publish_recycview);
    }
    private void initNetMsg() {
        //获取对应的时间
        MyHttpUtils.getNetMessage(getActivity(), "http://1zou.me/apisq/loadIconInMonth/uid/"+Constants.USER_ID+"/ymd/" + getClickTimeString(), new MyHttpUtils.Callback() {
            @Override
            public void getResponseMsg(String response) {
                //解析数据
                parseResponseMsg(response);
            }
        }, new MyHttpUtils.CallbackError() {
            @Override
            public void getResponseMsg(String error) {

            }
        });
    }
    private void parseResponseMsg(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getInt("errorCode") == 0) {
                if (!"null".equals(jsonObject.getString("datas"))) {
                    JSONObject datas = new JSONObject(jsonObject.getString("datas"));
                    Iterator<String> keys = datas.keys();
                    list = new ArrayList<Integer>();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        //设置当前显示的信息
                        if (key.equals(Constants.currentDay+"")){
                            JSONArray jsonArray = datas.getJSONArray(key);
                            for (int i=0;i<jsonArray.length();i++){
                                list.add((Integer) jsonArray.get(i));
                            }
                        }
                    }
                    //获取相应的图片
                    mHandler.sendEmptyMessage(1);
                } else {
                    LogUtils.showVerbose("WardrobeCalendarActivity", "当前时间没有拍照上传");
                }
            }

        } catch (JSONException e) {
            LogUtils.showVerbose("WardrobeCalendarActivity", "数据解析错误");
        }
    }
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        postCurrentDayMsg(getPublishCurrentMsg(list));
                    }
                }).start();
            }else if (msg.what==2){
                initJsonParse(currentPictureMsg);
            }
        }
    };

    private void postCurrentDayMsg(String publishCurrentMsg) {
        // 链接超时，请求超时设置
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);
        HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000);

        // 请求参数设置
        HttpClient client = new DefaultHttpClient(httpParams);
        HttpPost post = new HttpPost(
                "http://1zou.me/apisq/loadIconInFids");
        MultipartEntity entity = new MultipartEntity();
        try {
            entity.addPart("iconinfo",
                    new StringBody(publishCurrentMsg, Charset.forName("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setEntity(entity);
        HttpResponse resp = null;
        try {
            resp = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
//            LogUtils.showVerbose("MainActivity", "Response:" + EntityUtils.toString(resp.getEntity()));
//            LogUtils.showVerbose("MainActivity", "errorcode:" + EntityUtils.getContentCharSet(resp.getEntity()));
            currentPictureMsg = EntityUtils.toString(resp.getEntity());
            if (resp.getStatusLine().getStatusCode()==200){
                mHandler.sendEmptyMessage(2);
//                initJsonParse(str);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initJsonParse(String string) {
        try {
            JSONObject object = new JSONObject(string);
            JSONArray array = object.getJSONArray("datas");
            //是否已经发布文章了
            if (array.length()==0){
                calendar_detail_publish_again_layout.setVisibility(View.VISIBLE);
                calendar_detail_publish_recycview.setVisibility(View.GONE);
            }else {
                calendar_detail_publish_again_layout.setVisibility(View.GONE);
                calendar_detail_publish_recycview.setVisibility(View.VISIBLE);
                all_message.clear();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    int cmtnum = 0;
                    if (jsonObject.has("cmtnum")) {
                        cmtnum = jsonObject.getInt("cmtnum");
                    } else {
                        cmtnum = 0;
                    }
                    all_message.add(new MostPopularInfo(jsonObject.getInt("icnfid"),
                            jsonObject.getString("title"),
                            jsonObject.getString("content"),
                            jsonObject.getInt("likenum"),
                            jsonObject.getInt("storenum"),
                            jsonObject.getInt("userid"),
                            Constants.USER_NAME,
                            Constants.AVATAR,
                            jsonObject.getString("favourstate"),
                            jsonObject.getString("storedstate"),
                            jsonObject.getString("lng"),
                            jsonObject.getString("lat"),
                            jsonObject.getString("address"),
                            jsonObject.getString("createdat"),
                            jsonObject.getString("images"),
                            Constants.WEIGHT,
                            Constants.BUST, Constants.UADRESS,
                            Constants.HEIGHT, "null",
                            Constants.BRA,
                            cmtnum, jsonObject.getString("topics"), jsonObject.getString("browsenum")
                            ,jsonObject.getString("title_cn"),jsonObject.getString("content_cn")));

                }
                initData();
            }
        } catch (JSONException e) {
            LogUtils.showVerbose("ItemSelfLogFragment", "第一级数组解析错误");
        }
    }
    public void initData(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        calendar_detail_publish_recycview.setLayoutManager(linearLayoutManager);
        mAdapter = new OtherAuthorRecycviewAdapter(getActivity());
        mAdapter.setOtherAuthorListMsg(all_message,2);
        calendar_detail_publish_recycview.setAdapter(mAdapter);

    }
    private String getPublishCurrentMsg(List<Integer> list) {
//        {'iconinfo':{ 'userid':userid1,
//                'fids':[fid1,fid2,fid3]
//        } }
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i=0;i<list.size();i++){
            jsonArray.put(list.get(i));
        }
        try {
            jsonObject.put("userid",Constants.USER_ID);
            jsonObject.put("fids",jsonArray);
        } catch (JSONException e) {

        }
        return jsonObject.toString();
    }

    private String getClickTimeString() {
        String str = Constants.currentYear+"";
        if ((Constants.currentMonth+1)<10){
            str=str+"0"+(Constants.currentMonth+1);
        }else {
            str=str+(Constants.currentMonth+1);
        }
        if (Constants.currentDay<10){
            str = str+"0"+Constants.currentDay;
        }else {
            str = str+Constants.currentDay;
        }
        return str;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.calendar_detail_publish_again_layout:
                CacheDataHelper.addNullArgumentsMethod();
                Intent intent = new Intent(getActivity(), LunchCameraActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("calendar_detail","calendar_detail");
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }
}
