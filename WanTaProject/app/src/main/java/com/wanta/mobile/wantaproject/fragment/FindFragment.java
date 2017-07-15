package com.wanta.mobile.wantaproject.fragment;

import android.app.Activity;
import android.content.Context;
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
import com.wanta.mobile.wantaproject.activity.TopicsDetailMessageActivity;
import com.wanta.mobile.wantaproject.adapter.FindFragmentRecycviewAdapter;
import com.wanta.mobile.wantaproject.adapter.FindRootRecycviewAdapter;
import com.wanta.mobile.wantaproject.adapter.TopicsRecycleViewAdapter;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.MostPopularInfo;
import com.wanta.mobile.wantaproject.domain.TopicsInfo;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;
import com.wanta.mobile.wantaproject.utils.NetUtils;
import com.wanta.mobile.wantaproject.webview.WebViewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/16.
 */
public class FindFragment extends Fragment {

    private View mView_find;
    private RecyclerView mFind_recycview;
    private LinearLayout wardrober_head_title;
    private String[] picUrl = new String[]{
            "http://mp.weixin.qq.com/s/dL3PIuHYIAPk0xd4VT_X-A",
            "http://mp.weixin.qq.com/s/Hs4L0d1p99fNGNIpl7cuHg",
            "http://mp.weixin.qq.com/s/0m-5pN5GR9cTzdSICyjMJg",
            "http://mp.weixin.qq.com/s/T36LxKTgA_J2HXaIQc3Gzw",
            "https://mp.weixin.qq.com/s/5IhZv2A6NaZWuBjp8SNhcg"
    };
    private RecyclerView topics_recycview;
    private List<TopicsInfo> topicsInfoList = new ArrayList<>();
    private int likeNumber;
    private boolean islike;
    private RecyclerView find_root_recycview;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView_find = inflater.inflate(R.layout.fragment_find,container,false);
        initTopics();
        return mView_find;
    }

//    @Override
//    public void onAttach(Activity activity) {
//        wardrober_head_title = (LinearLayout) activity.findViewById(R.id.head_title);
//        wardrober_head_title.setVisibility(View.VISIBLE);
//        super.onAttach(activity);
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDestroyView() {
        find_root_recycview.setAdapter(null);
        find_root_recycview = null;
        super.onDestroyView();
    }

    private void initTopics() {
        if(NetUtils.checkNet(context)==true){
            MyHttpUtils.getNetMessage(context, "http://1zou.me/apisq/loadTpc/"+Constants.USER_ID, new MyHttpUtils.Callback() {
                @Override
                public void getResponseMsg(String response) {
                    LogUtils.showVerbose("FindFragment","话题的数据"+response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int errorCode = jsonObject.getInt("errorCode");
                        if (errorCode==0){
                            //解析正确
                            initJsonParse(response);
                        }
                    } catch (JSONException e) {
                        LogUtils.showVerbose("FindFragment","话题信息解析错误");
                    }
                }
            }, new MyHttpUtils.CallbackError() {
                @Override
                public void getResponseMsg(String error) {
                    LogUtils.showVerbose("FindFragment","获取数据错误");
                }
            });
        }else{
            NetUtils.showNoNetDialog(context);
        }

    }

    private void initviewpager() {

        find_root_recycview = (RecyclerView) mView_find.findViewById(R.id.find_root_recycview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        find_root_recycview.setLayoutManager(linearLayoutManager);
        FindRootRecycviewAdapter adapter = new FindRootRecycviewAdapter(context,topicsInfoList);
        find_root_recycview.setAdapter(adapter);

//        topics_recycview = (RecyclerView) mView_find.findViewById(R.id.find_topics_recycview);
//        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//防止item来回的变换
//        topics_recycview.setLayoutManager(staggeredGridLayoutManager);
//        topics_recycview.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                staggeredGridLayoutManager.invalidateSpanAssignments();//防止出现空白的部分
//            }
//        });
//        TopicsRecycleViewAdapter adapter_topics = new TopicsRecycleViewAdapter(getActivity(),topicsInfoList);
//        topics_recycview.setAdapter(adapter_topics);
//        adapter_topics.setOnTopicsItemHeartListener(new TopicsRecycleViewAdapter.OnTopicsItemHeartListener() {
//            @Override
//            public void onItemHeartClick(final int position, TopicsInfo info, final TextView heartconnt_tv, final MyImageView topics_heart_imag) {
//                if (NetUtils.checkNet(getActivity())==true){
//                    Constants.topicsLikeNum = Constants.topicsHeartNum.get(position).getBeliked();
//                    //设置选择的状态
//                    if ("null".equals(Constants.topicsHeartNum.get(position).getFavourstate())) {
//                        Constants.isTopicLike = false;
//                    } else {
//                        Constants.isTopicLike = true;
//                    }
//
//                    if (Constants.STATUS.equals("reg_log")) {
//                        if (Constants.isTopicLike == false) {
//                            MyHttpUtils.getClickTopicsHeart(getActivity(), 0, info.getFid(), new MyHttpUtils.Callback() {
//                                @Override
//                                public void getResponseMsg(String response) {
//                                    try {
//                                        JSONObject jsonObject = new JSONObject(response);
//                                        if (jsonObject.getInt("errorCode") == 0) {
//                                            Constants.isTopicLike = true;
//                                            Constants.topicsLikeNum = Constants.topicsLikeNum + 1;
//                                            heartconnt_tv.setText((Constants.topicsLikeNum) + "");
//                                            topics_heart_imag.setImageResource(R.mipmap.heart_press);
//                                            LogUtils.showVerbose("MostPopularFragment", "点赞成功");
//                                            Constants.topicsHeartNum.set(position, new TopicsInfo(Constants.topicsLikeNum, Constants.USER_ID + ""));
//                                        }
//                                    } catch (JSONException e) {
//                                        LogUtils.showVerbose("MostPopularFragment", "获取数据失败");
//                                    }
//                                }
//                            }, new MyHttpUtils.CallbackError() {
//                                @Override
//                                public void getResponseMsg(String error) {
//                                    LogUtils.showVerbose("MostPopularFragment", "点赞失败");
//                                }
//                            });
//
//                        } else {
//                            MyHttpUtils.getClickTopicsHeart(getActivity(), 1, info.getFid(), new MyHttpUtils.Callback() {
//                                @Override
//                                public void getResponseMsg(String response) {
//                                    try {
//                                        JSONObject jsonObject = new JSONObject(response);
//                                        if (jsonObject.getInt("errorCode") == 0) {
//                                            Constants.isTopicLike = false;
//                                            Constants.topicsLikeNum = Constants.topicsLikeNum - 1;
//                                            heartconnt_tv.setText(Constants.topicsLikeNum + "");
//                                            topics_heart_imag.setImageResource(R.mipmap.heart_no_press);
//                                            LogUtils.showVerbose("MostPopularFragment", "取消点赞成功");
//                                            Constants.topicsHeartNum.set(position, new TopicsInfo(Constants.topicsLikeNum, "null"));
//                                        }
//                                    } catch (JSONException e) {
//                                        LogUtils.showVerbose("MostPopularFragment", "获取数据失败");
//                                    }
//
//                                }
//                            }, new MyHttpUtils.CallbackError() {
//                                @Override
//                                public void getResponseMsg(String error) {
//                                    LogUtils.showVerbose("MostPopularFragment", "取消点赞失败");
//                                }
//                            });
//
//                        }
//                    } else {
//                        Intent intent = new Intent(getActivity(), LoginActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        getActivity().finish();
//                    }
//                }else {
//                    NetUtils.showNoNetDialog(getActivity());
//                }
//
//            }
//        });
//
//       adapter_topics.setOnTopicsItemImageListener(new TopicsRecycleViewAdapter.OnTopicsItemImageListener() {
//           @Override
//           public void onItemImageClick(TopicsInfo info) {
//               Intent intent = new Intent(getActivity(), TopicsDetailMessageActivity.class);
//               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//               intent.putExtra("topics","topics");
//               intent.putExtra("click_location",info.getFid());
//               startActivity(intent);
//               getActivity().finish();
//           }
//       });

//        //更新的代码
//        mFind_recycview = (RecyclerView) mView_find.findViewById(R.id.find_recycview);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mFind_recycview.setLayoutManager(linearLayoutManager);
//        FindFragmentRecycviewAdapter adapter = new FindFragmentRecycviewAdapter(getActivity());
//        mFind_recycview.setAdapter(adapter);
//        adapter.setFindItemClickListener(new FindFragmentRecycviewAdapter.FindItemClickListener() {
//            @Override
//            public void onItemClick(View view) {
//                int childAdapterPosition = mFind_recycview.getChildAdapterPosition(view);
//                Intent intent = new Intent(getActivity(), WebViewActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("find_pic_url",picUrl[childAdapterPosition]);
//                startActivity(intent);
//                getActivity().finish();
//            }
//        });

    }

    //初始化的数据的解析
    private void initJsonParse(String string) {
        topicsInfoList.clear();
        Constants.topicsHeartNum.clear();
        try {
            JSONObject object = new JSONObject(string);
            JSONArray array = object.getJSONArray("datas");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                topicsInfoList.add(new TopicsInfo(
                        jsonObject.getInt("fid"),jsonObject.getString("title"),
                        jsonObject.getString("content"),jsonObject.getString("imgpath"),
                        jsonObject.getInt("bebrowsed"),jsonObject.getInt("beliked"),
                        jsonObject.getInt("iconnum"),jsonObject.getInt("width"),
                        jsonObject.getInt("height"),jsonObject.getString("favourstate")));
                Constants.topicsHeartNum.add(new TopicsInfo(
                        jsonObject.getInt("beliked"),
                        jsonObject.getString("favourstate")));

            }
            initviewpager();//初始化信息

        } catch (JSONException e) {
            LogUtils.showVerbose("MostPopularFragment", "第一级数组解析错误");
        }
    }
}
