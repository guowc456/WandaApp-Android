package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.LoginActivity;
import com.wanta.mobile.wantaproject.activity.MainActivity;
import com.wanta.mobile.wantaproject.activity.TopicsDetailMessageActivity;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.CacheDataHelper;
import com.wanta.mobile.wantaproject.domain.CacheDataUtils;
import com.wanta.mobile.wantaproject.domain.TopicsInfo;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;
import com.wanta.mobile.wantaproject.utils.NetUtils;
import com.wanta.mobile.wantaproject.webview.WebViewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.wanta.mobile.wantaproject.R.id.topics_recycview;

/**
 * Created by Administrator on 2017/5/11.
 */

public class FindRootRecycviewAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<TopicsInfo> topicsInfoList;
    private String[] picUrl = new String[]{
            "http://mp.weixin.qq.com/s/dL3PIuHYIAPk0xd4VT_X-A",
            "http://mp.weixin.qq.com/s/Hs4L0d1p99fNGNIpl7cuHg",
            "http://mp.weixin.qq.com/s/0m-5pN5GR9cTzdSICyjMJg",
            "http://mp.weixin.qq.com/s/T36LxKTgA_J2HXaIQc3Gzw",
            "https://mp.weixin.qq.com/s/5IhZv2A6NaZWuBjp8SNhcg"
    };

    public FindRootRecycviewAdapter(Context context, List<TopicsInfo> topicsInfoList) {
        this.context = context;
        this.topicsInfoList = topicsInfoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_find_root_recycview_layout,null);
        FindRootRecycviewAdapterViewHolder holder = new FindRootRecycviewAdapterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        ((FindRootRecycviewAdapterViewHolder)holder).item_find_root_recycview
        if (position==0){
            //瀑布流显示
            stagLayoutShow(((FindRootRecycviewAdapterViewHolder)holder).item_find_root_recycview);
        }else if (position==1){
            //线性流显示
            lineLayoutShow(((FindRootRecycviewAdapterViewHolder)holder).item_find_root_recycview);
        }
    }

    private void lineLayoutShow(final RecyclerView item_find_root_recycview) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        item_find_root_recycview.setLayoutManager(linearLayoutManager);
        FindFragmentRecycviewAdapter adapter = new FindFragmentRecycviewAdapter(context);
        item_find_root_recycview.setAdapter(adapter);
        adapter.setFindItemClickListener(new FindFragmentRecycviewAdapter.FindItemClickListener() {
            @Override
            public void onItemClick(View view) {
                int childAdapterPosition = item_find_root_recycview.getChildAdapterPosition(view);
                CacheDataHelper.addNullArgumentsMethod();
                CacheDataUtils.setFindImageLink(picUrl[childAdapterPosition]);
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("find_pic_url",picUrl[childAdapterPosition]);
                context.startActivity(intent);
                ((MainActivity)context).finish();
            }
        });

    }

    private void stagLayoutShow(RecyclerView item_find_root_recycview) {
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setOrientation(StaggeredGridLayoutManager.VERTICAL);
        item_find_root_recycview.setLayoutManager(staggeredGridLayoutManager);
        item_find_root_recycview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                staggeredGridLayoutManager.invalidateSpanAssignments();//防止出现顶部出现空白的部分
            }
        });
        TopicsRecycleViewAdapter adapter_topics = new TopicsRecycleViewAdapter(context,topicsInfoList);
        item_find_root_recycview.setAdapter(adapter_topics);
        adapter_topics.setOnTopicsItemHeartListener(new TopicsRecycleViewAdapter.OnTopicsItemHeartListener() {
            @Override
            public void onItemHeartClick(final int position, TopicsInfo info, final TextView heartconnt_tv, final MyImageView topics_heart_imag) {
                if (NetUtils.checkNet(context)==true){
                    Constants.topicsLikeNum = Constants.topicsHeartNum.get(position).getBeliked();
                    //设置选择的状态
                    if ("null".equals(Constants.topicsHeartNum.get(position).getFavourstate())) {
                        Constants.isTopicLike = false;
                    } else {
                        Constants.isTopicLike = true;
                    }

                    if (Constants.STATUS.equals("reg_log")) {
                        if (Constants.isTopicLike == false) {
                            MyHttpUtils.getClickTopicsHeart(context, 0, info.getFid(), new MyHttpUtils.Callback() {
                                @Override
                                public void getResponseMsg(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getInt("errorCode") == 0) {
                                            Constants.isTopicLike = true;
                                            Constants.topicsLikeNum = Constants.topicsLikeNum + 1;
                                            heartconnt_tv.setText((Constants.topicsLikeNum) + "");
                                            topics_heart_imag.setImageResource(R.mipmap.heart_press);
                                            LogUtils.showVerbose("MostPopularFragment", "点赞成功");
                                            Constants.topicsHeartNum.set(position, new TopicsInfo(Constants.topicsLikeNum, Constants.USER_ID + ""));
                                        }
                                    } catch (JSONException e) {
                                        LogUtils.showVerbose("MostPopularFragment", "获取数据失败");
                                    }
                                }
                            }, new MyHttpUtils.CallbackError() {
                                @Override
                                public void getResponseMsg(String error) {
                                    LogUtils.showVerbose("MostPopularFragment", "点赞失败");
                                }
                            });

                        } else {
                            MyHttpUtils.getClickTopicsHeart(context, 1, info.getFid(), new MyHttpUtils.Callback() {
                                @Override
                                public void getResponseMsg(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getInt("errorCode") == 0) {
                                            Constants.isTopicLike = false;
                                            Constants.topicsLikeNum = Constants.topicsLikeNum - 1;
                                            heartconnt_tv.setText(Constants.topicsLikeNum + "");
                                            topics_heart_imag.setImageResource(R.mipmap.heart_no_press);
                                            LogUtils.showVerbose("MostPopularFragment", "取消点赞成功");
                                            Constants.topicsHeartNum.set(position, new TopicsInfo(Constants.topicsLikeNum, "null"));
                                        }
                                    } catch (JSONException e) {
                                        LogUtils.showVerbose("MostPopularFragment", "获取数据失败");
                                    }

                                }
                            }, new MyHttpUtils.CallbackError() {
                                @Override
                                public void getResponseMsg(String error) {
                                    LogUtils.showVerbose("MostPopularFragment", "取消点赞失败");
                                }
                            });

                        }
                    } else {
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((MainActivity)context).finish();
                    }
                }else {
                    NetUtils.showNoNetDialog(context);
                }

            }
        });

        adapter_topics.setOnTopicsItemImageListener(new TopicsRecycleViewAdapter.OnTopicsItemImageListener() {
            @Override
            public void onItemImageClick(TopicsInfo info) {
                loadBrownsNumber(info.getFid());
                CacheDataHelper.addNullArgumentsMethod();
                Constants.whichTopicsPosition = info.getFid();
                Intent intent = new Intent(context, TopicsDetailMessageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("topics","topics");
//                intent.putExtra("click_location",info.getFid());
                context.startActivity(intent);
                ((MainActivity)context).finish();
            }
        });
    }

    private void loadBrownsNumber(int position) {
        MyHttpUtils.getNetMessage(context, "http://1zou.me/apisq/plusTcpBrowse/"+position, new MyHttpUtils.Callback() {
            @Override
            public void getResponseMsg(String response) {

            }
        }, new MyHttpUtils.CallbackError() {
            @Override
            public void getResponseMsg(String error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    class FindRootRecycviewAdapterViewHolder extends RecyclerView.ViewHolder {

        private final RecyclerView item_find_root_recycview;

        public FindRootRecycviewAdapterViewHolder(View itemView) {
            super(itemView);
            item_find_root_recycview = (RecyclerView) itemView.findViewById(R.id.item_find_root_recycview);
        }
    }
}
