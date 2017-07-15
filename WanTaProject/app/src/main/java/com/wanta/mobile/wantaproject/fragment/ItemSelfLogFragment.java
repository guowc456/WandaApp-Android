package com.wanta.mobile.wantaproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.LoginActivity;
import com.wanta.mobile.wantaproject.adapter.MostPopularFragmentAdapter;
import com.wanta.mobile.wantaproject.adapter.OtherAuthorRecycviewAdapter;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.CacheDataHelper;
import com.wanta.mobile.wantaproject.domain.MostPopularInfo;
import com.wanta.mobile.wantaproject.uploadimage.ImagesSelectorActivity;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.ImageDealUtils;
import com.wanta.mobile.wantaproject.utils.JsonParseUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;
import com.wanta.mobile.wantaproject.utils.NetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/22.
 */
public class ItemSelfLogFragment extends Fragment implements View.OnClickListener {

    private View mView_selflog;
    private CustomSimpleDraweeView mSelf_log_icon;
    private LinearLayout publish_first_diary_layout;
    private RecyclerView self_my_recycview;
    private List<Integer> current_message = new ArrayList<>();
    private List<MostPopularInfo> all_message = new ArrayList<>();
    private OtherAuthorRecycviewAdapter mAdapter;
    private int num = 0;
    private int currentFid;
    private LinearLayout publish_my_diarys_layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView_selflog = inflater.inflate(R.layout.item_self_log_layout,container,false);
        initId();
        initNetMsg();
//        initData();
        return mView_selflog;
    }

    private void initNetMsg() {
        if (NetUtils.checkNet(getActivity())==true){
            MyHttpUtils.getNetMessage(getActivity(), "http://1zou.me/apisq/loadOwnIcon/uid/"+Constants.USER_ID+"/lmt/10/ofset/0", new MyHttpUtils.Callback() {
                @Override
                public void getResponseMsg(String response) {
//                    LogUtils.showVerbose("MostPopularFragment", "response="+response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int errorCode = jsonObject.getInt("errorCode");
                        if (errorCode == 0) {
                            //获取信息正确
                            initJsonParse(response);
                        } else {
                            LogUtils.showVerbose("ItemSelfLogFragment", "获取的数据错误");
                        }
                    } catch (JSONException e) {
//                        e.printStackTrace();
                    }

                }
            }, new MyHttpUtils.CallbackError() {
                @Override
                public void getResponseMsg(String error) {
                }
            });
        }else {
            NetUtils.showNoNetDialog(getActivity());
        }
    }

    private void initId() {
        mSelf_log_icon = (CustomSimpleDraweeView) mView_selflog.findViewById(R.id.self_log_icon);
        mSelf_log_icon.setHeight(Constants.PHONE_HEIGHT/6);
        mSelf_log_icon.setWidth((int) ((Constants.PHONE_WIDTH*1.00/5)*3));
        mSelf_log_icon.setImageBitmap(ImageDealUtils.readBitMap(getActivity(),R.mipmap.self_log_icon));
        publish_first_diary_layout = (LinearLayout) mView_selflog.findViewById(R.id.publish_first_diary_layout);
        publish_first_diary_layout.setOnClickListener(this);
        self_my_recycview = (RecyclerView) mView_selflog.findViewById(R.id.self_my_recycview);
        publish_my_diarys_layout = (LinearLayout) mView_selflog.findViewById(R.id.publish_my_diarys_layout);
    }

    public void initData(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        self_my_recycview.setLayoutManager(linearLayoutManager);
        mAdapter = new OtherAuthorRecycviewAdapter(getActivity());
        mAdapter.setOtherAuthorListMsg(all_message,2);
        self_my_recycview.setAdapter(mAdapter);

        self_my_recycview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    LogUtils.showVerbose("MostPopularFragment", "111");
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                LogUtils.showVerbose("MostPopularFragment", "是否滑动到底=" + isSlideToBottom(recyclerView));
                if (isSlideToBottom(recyclerView)) {
                    num = num + 1;
                    if (NetUtils.checkNet(getActivity())==true){
                        MyHttpUtils.getNetMessage(getActivity(), "http://1zou.me/apisq/loadOwnIcon/uid/"+Constants.USER_ID+"/lmt/10/ofset/" + num*10, new MyHttpUtils.Callback() {
                            @Override
                            public void getResponseMsg(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int errorCode = jsonObject.getInt("errorCode");
                                    if (errorCode == 0) {
                                        //获取信息正确
                                        jsonParse(response);
                                    } else {
                                        LogUtils.showVerbose("MostPopularFragment", "获取的数据错误");
                                    }
                                } catch (JSONException e) {
//                                e.printStackTrace();
                                }

                            }
                        }, new MyHttpUtils.CallbackError() {
                            @Override
                            public void getResponseMsg(String error) {
                            }
                        });
                    }else {
                        NetUtils.showNoNetDialog(getActivity());
                    }

                } else {
//                    LogUtils.showVerbose("MostPopularFragment", "当前的number=" + currentFid);
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        self_my_recycview.setAdapter(null);
        self_my_recycview = null;
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.publish_first_diary_layout:
                CacheDataHelper.addNullArgumentsMethod();
                Intent intent = new Intent(getActivity(), ImagesSelectorActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }
    //初始化的数据的解析
    private void initJsonParse(String string) {
        try {
            JSONObject object = new JSONObject(string);
            JSONArray array = object.getJSONArray("datas");
            //是否已经发布文章了
            if (array.length()==0){
                publish_first_diary_layout.setVisibility(View.VISIBLE);
                publish_my_diarys_layout.setVisibility(View.GONE);
            }else {
                publish_first_diary_layout.setVisibility(View.GONE);
                publish_my_diarys_layout.setVisibility(View.VISIBLE);
                Constants.heartNum.clear();
                current_message.clear();
                all_message.clear();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    int cmtnum = 0;
                    if (jsonObject.has("cmtnum")) {
                        cmtnum = jsonObject.getInt("cmtnum");
                    } else {
                        cmtnum = 0;
                    }
                    current_message.add(jsonObject.getInt("icnfid"));
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
//                    all_message.add(new MostPopularInfo(
//                            jsonObject.getInt("userid"),jsonObject.getInt("icnfid"),jsonObject.getString("title"),jsonObject.getString("title_cn"),
//                            jsonObject.getString("content"),jsonObject.getString("content_cn"),jsonObject.getInt("likenum"),jsonObject.getInt("browsenum")+"",
//                            jsonObject.getInt("storenum"),jsonObject.getString("favourstate"),jsonObject.getString("storedstate"),jsonObject.getString("lng"),
//                            jsonObject.getString("lat"),jsonObject.getString("address"),jsonObject.getString("createdat"),jsonObject.getString("images"),jsonObject.getString("topics"))
//                    );
                    Constants.heartNum.add(new MostPopularInfo(
                            jsonObject.getInt("likenum"),
                            jsonObject.getString("favourstate")));

                }
                initData();
            }
        } catch (JSONException e) {
            LogUtils.showVerbose("ItemSelfLogFragment", "第一级数组解析错误");
        }
    }
    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    private void jsonParse(String string) {
        try {
            JSONObject object = new JSONObject(string);
            JSONArray array = object.getJSONArray("datas");
            current_message.clear();
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                int cmtnum = 0;
                if (jsonObject.has("cmtnum")) {
                    cmtnum = jsonObject.getInt("cmtnum");
                } else {
                    cmtnum = 0;
                }
                current_message.add(jsonObject.getInt("icnfid"));
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
                Constants.heartNum.add(new MostPopularInfo(
                        jsonObject.getInt("likenum"),
                        jsonObject.getString("favourstate")));

                LogUtils.showVerbose("MostPopularFragment", "获取的信息=：" + jsonObject.toString());
            }

            //获取最新的fid
//            currentFid = JsonParseUtils.getSmallConfid(current_message);
//            LogUtils.showVerbose("MostPopularFragment", "最小的icnfig=" + currentFid);

            mAdapter.setOtherAuthorListMsg(all_message,2);
            mAdapter.notifyItemRangeInserted(mAdapter.getItemCount(), mAdapter.getItemCount() + 10);
            mAdapter.notifyItemRangeChanged(mAdapter.getItemCount(), mAdapter.getItemCount() + 10);
        } catch (JSONException e) {
            LogUtils.showVerbose("MostPopularFragment", "第一级数组解析错误");
        }
    }
}
