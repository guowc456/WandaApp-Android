package com.wanta.mobile.wantaproject.fragment;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.RecommendedAttentionAdapter;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.RecommendCareInfo;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;
import com.wanta.mobile.wantaproject.utils.NetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/26.
 */

public class RecommendedAttentionConcernFragment extends Fragment {

    private View view;
    private RecyclerView fragment_recommende_attention_recycleview;
    private int icnfid;
    private List<RecommendCareInfo> recommendCareInfoList = new ArrayList<>();
    private List<RecommendCareInfo> recommendIsCare = new ArrayList<>();
    private ImageView commentImageView;
    private RecommendCareInfo commentRecommendCare ;
    private int commentPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recommended_attention_layout, null);
        initId();
        initNetMsg();
//        initData();
        return view;
    }

    private void initNetMsg() {
        if (NetUtils.checkNet(getActivity()) == true) {
            MyHttpUtils.getNetMessage(getActivity(), "http://1zou.me/apisq/getFollows/" + "8458", new MyHttpUtils.Callback() {
                @Override
                public void getResponseMsg(String response) {
                    jsonParse(response);
                }
            }, new MyHttpUtils.CallbackError() {
                @Override
                public void getResponseMsg(String error) {

                }
            });
        } else {
            NetUtils.showNoNetDialog(getActivity());
        }
    }

    private void jsonParse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int errorCode = jsonObject.getInt("errorCode");
            if (errorCode == 0) {
                JSONArray datas = jsonObject.getJSONArray("datas");
                for (int i = 0; i < datas.length(); i++) {
                    JSONObject datasJSONObject = datas.getJSONObject(i);
                    recommendCareInfoList.add(new RecommendCareInfo(datasJSONObject.getInt("userid"),
                            datasJSONObject.getString("username"), datasJSONObject.getString("avatar"),
                            datasJSONObject.getInt("height"), datasJSONObject.getString("weight"),
                            datasJSONObject.getString("address"), datasJSONObject.getString("bra"),
                            datasJSONObject.getInt("funsnum"),
                            datasJSONObject.getString("sign"),"true"));
                    recommendIsCare.add(new RecommendCareInfo("true"));
                }
                initData();
            }
        } catch (JSONException e) {
            LogUtils.showVerbose("RecommendedAttentionConcernFragment", "数据解析错误");
        }
    }

    private void initId() {
        icnfid = getArguments().getInt("icnfid");
        fragment_recommende_attention_recycleview = (RecyclerView) view.findViewById(R.id.fragment_recommende_attention_recycleview);
    }

    private void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragment_recommende_attention_recycleview.setLayoutManager(linearLayoutManager);
        RecommendedAttentionAdapter attentionAdapter = new RecommendedAttentionAdapter(getActivity(), 2);
        attentionAdapter.setRecommendList(recommendCareInfoList);
        fragment_recommende_attention_recycleview.setAdapter(attentionAdapter);
        attentionAdapter.setOnRecommendedAttentionCareListener(new RecommendedAttentionAdapter.OnRecommendedAttentionCareListener() {
            @Override
            public void onItemClick(View view, final RecommendCareInfo recommendCareInfo, final MyImageView imageview) {
                if (NetUtils.checkNet(getActivity())==true){
                    final int childAdapterPosition = fragment_recommende_attention_recycleview.getChildAdapterPosition(view);
                    String isCareFlag = recommendCareInfoList.get(childAdapterPosition).getIsCare();
                    if ("false".equals(isCareFlag)){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MyHttpUtils.postJsonData("http://1zou.me/apisq/addFollow", "userInfo", getAddCareJsonMsg(recommendCareInfo.getUserid()),
                                        new MyHttpUtils.Callback() {
                                            @Override
                                            public void getResponseMsg(String response) {
                                                LogUtils.showVerbose("ItemMostpopularActivity", "response=" + response);
                                                if ("0".equals(response)) {
//                                                        recommendCareInfo.setIsCare("true");
//                                                        imageview.setSelected(false);
//                                                        recommendCareInfoList.set(childAdapterPosition,new RecommendCareInfo("true"));
//                                                        attentionAdapter.setRecommendList(recommendCareInfoList);
                                                    commentImageView = imageview;
                                                    commentRecommendCare = recommendCareInfo;
                                                    commentPosition = childAdapterPosition;
                                                    handler.sendEmptyMessage(1);
                                                }
                                            }
                                        }, new MyHttpUtils.CallbackError() {
                                            @Override
                                            public void getResponseMsg(String error) {
                                                LogUtils.showVerbose("ItemMostpopularActivity", "error=" + error);
                                            }
                                        });
                            }
                        }).start();
                        LogUtils.showVerbose("RecommendedAttentionNotConcernFragment","关注了");
                    }else {
                        LogUtils.showVerbose("RecommendedAttentionNotConcernFragment","没有关注");
//                            imageview.setSelected(true);
//                            recommendCareInfo.setIsCare("false");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MyHttpUtils.postJsonData("http://1zou.me/apisq/removeFollow", "userInfo", getCancelCareJsonMsg(recommendCareInfo.getUserid()),
                                        new MyHttpUtils.Callback() {
                                            @Override
                                            public void getResponseMsg(String response) {
                                                if ("0".equals(response)) {
//                                                        recommendCareInfo.setIsCare("false");
//                                                        imageview.setSelected(true);
//                                                        recommendCareInfoList.set(childAdapterPosition,new RecommendCareInfo("false"));
//                                                        attentionAdapter.setRecommendList(recommendCareInfoList);
                                                    commentImageView = imageview;
                                                    commentRecommendCare = recommendCareInfo;
                                                    commentPosition = childAdapterPosition;
                                                    handler.sendEmptyMessage(2);
                                                }
                                            }
                                        }, new MyHttpUtils.CallbackError() {
                                            @Override
                                            public void getResponseMsg(String error) {

                                            }
                                        });
                            }
                        }).start();
                    }
                }else {
                    NetUtils.showNoNetDialog(getActivity());
                }
            }
        });
    }

    //获取取消关注的参数
    public String getCancelCareJsonMsg(int followid) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", Constants.USER_ID);
            jsonObject.put("followid", followid);//添加被关注者的id
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    //获取添加关注的参数
    public String getAddCareJsonMsg(int followid) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", Constants.USER_ID);
            jsonObject.put("followid", followid);//添加被关注者的id
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                Toast.makeText(getActivity(),"关注成功",Toast.LENGTH_SHORT).show();
                commentImageView.setSelected(false);
                commentRecommendCare.setIsCare("true");
                recommendCareInfoList.set(commentPosition,new RecommendCareInfo("true"));
            }else if (msg.what==2){
                Toast.makeText(getActivity(),"取消关注成功",Toast.LENGTH_SHORT).show();
                commentImageView.setSelected(true);
                commentRecommendCare.setIsCare("false");
                recommendCareInfoList.set(commentPosition,new RecommendCareInfo("false"));
            }
        }
    };

}
