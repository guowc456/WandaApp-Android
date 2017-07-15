package com.wanta.mobile.wantaproject.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.ItemMostpopularActivity;
import com.wanta.mobile.wantaproject.activity.LoginActivity;
import com.wanta.mobile.wantaproject.adapter.MostPopularFragmentAdapter;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.domain.MostPopularInfo;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.JsonParseUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/10/15.
 */
public class JapaneseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View mView_japanese;
    private SwipeRefreshLayout japanese_fresh;
    private RecyclerView japanese_recycleview;
    private MostPopularFragmentAdapter mAdapter;
    private int num = 0;
    private List<Integer> current_message = new ArrayList<>();
    private List<MostPopularInfo> all_message = new ArrayList<>();
    private int currentFid = 0;
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView_japanese = inflater.inflate(R.layout.fragment_japanese,null);
        initId();
        return mView_japanese;
    }
    private void initId() {
        japanese_fresh = (SwipeRefreshLayout) mView_japanese.findViewById(R.id.japanese_fresh);
        japanese_recycleview = (RecyclerView) mView_japanese.findViewById(R.id.japanese_recycleview);
        japanese_fresh.setOnRefreshListener(this);
//        //改变加载显示的颜色
        japanese_fresh.setColorSchemeResources(R.color.mostpopular_fresh_color);
        //放在子线程中是因为mostpopular_fresh还没有初始化完，加载的图标是不会显示的
        japanese_fresh.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });
    }

    private void init() {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        japanese_recycleview.setLayoutManager(staggeredGridLayoutManager);
        mAdapter = new MostPopularFragmentAdapter(getActivity(),all_message);
        japanese_recycleview.setAdapter(mAdapter);
        japanese_recycleview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LogUtils.showVerbose("MostPopularFragment", "111");
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LogUtils.showVerbose("MostPopularFragment", "是否滑动到底=" + isSlideToBottom(recyclerView));
                if (isSlideToBottom(recyclerView)) {
                    num = num + 1;
//                    currentFid = currentFid - 5;
//                    LogUtils.showVerbose("MostPopularFragment","当前的number="+num);
//                    LogUtils.showVerbose("MostPopularFragment","当前的申请的条件="+"http://1zou.me/apisq/loadIconsBP/uid/5/lmt/10/ofst/"+(num*10));
                    LogUtils.showVerbose("MostPopularFragment", "当前的number=" + currentFid);
                    MyHttpUtils.getNetMessage(getActivity(), "http://1zou.me/apisq/loadIconsBMinF/uid/" + Constants.USER_ID + "/lmt/10/minfid/" + currentFid, new MyHttpUtils.Callback() {
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
                                e.printStackTrace();
                            }

                        }
                    }, new MyHttpUtils.CallbackError() {
                        @Override
                        public void getResponseMsg(String error) {

                        }
                    });
                } else {
                    LogUtils.showVerbose("MostPopularFragment", "当前的number=" + currentFid);
                }
            }
        });

        mAdapter.setOnMostPopularItemImageListener(new MostPopularFragmentAdapter.onMostPopularItemImageListener() {
            @Override
            public void onItemImageClick(View view, MostPopularInfo mostPopularInfo) {
                //点击图片的事件
                JumpToItemMostpopularActivity(
                        mostPopularInfo.getIcnfid(),mostPopularInfo.getTitle(),mostPopularInfo.getContent(),mostPopularInfo.getLikenum(),mostPopularInfo.getStorenum(),
                        mostPopularInfo.getUserid(),mostPopularInfo.getUsername(),mostPopularInfo.getUseravatar(),mostPopularInfo.getFavourstate(),mostPopularInfo.getStoredstate(),
                        mostPopularInfo.getLng(),mostPopularInfo.getLat(),mostPopularInfo.getAddress(),mostPopularInfo.getCreatedat(),mostPopularInfo.getImages(),mostPopularInfo.getCmtnum()
                );
            }
        });
        mAdapter.setOnMostPopularItemTitleListener(new MostPopularFragmentAdapter.onMostPopularItemTitleListener() {
            @Override
            public void onItemTitlsClick(View view, MostPopularInfo mostPopularInfo) {
                //点击标题的事件
//                ToastUtil.showShort(getActivity(),"标题");
                JumpToItemMostpopularActivity(
                        mostPopularInfo.getIcnfid(),mostPopularInfo.getTitle(),mostPopularInfo.getContent(),mostPopularInfo.getLikenum(),mostPopularInfo.getStorenum(),
                        mostPopularInfo.getUserid(),mostPopularInfo.getUsername(),mostPopularInfo.getUseravatar(),mostPopularInfo.getFavourstate(),mostPopularInfo.getStoredstate(),
                        mostPopularInfo.getLng(),mostPopularInfo.getLat(),mostPopularInfo.getAddress(),mostPopularInfo.getCreatedat(),mostPopularInfo.getImages(),mostPopularInfo.getCmtnum()
                );
            }
        });
        mAdapter.setOnMostPopularItemContentListener(new MostPopularFragmentAdapter.onMostPopularItemContentListener() {
            @Override
            public void onItemContentClick(View view, MostPopularInfo mostPopularInfo) {
                //点击内容的事件
                JumpToItemMostpopularActivity(
                        mostPopularInfo.getIcnfid(),mostPopularInfo.getTitle(),mostPopularInfo.getContent(),mostPopularInfo.getLikenum(),mostPopularInfo.getStorenum(),
                        mostPopularInfo.getUserid(),mostPopularInfo.getUsername(),mostPopularInfo.getUseravatar(),mostPopularInfo.getFavourstate(),mostPopularInfo.getStoredstate(),
                        mostPopularInfo.getLng(),mostPopularInfo.getLat(),mostPopularInfo.getAddress(),mostPopularInfo.getCreatedat(),mostPopularInfo.getImages(),mostPopularInfo.getCmtnum()
                );
            }
        });
        mAdapter.setOnMostPopularItemIconListener(new MostPopularFragmentAdapter.onMostPopularItemIconListener() {
            @Override
            public void onItemIconClick(View view, MostPopularInfo mostPopularInfo) {
                //点击图标的事件
//                ToastUtil.showShort(getActivity(),"图标");
            }
        });
        mAdapter.setOnMostPopularItemNameListener(new MostPopularFragmentAdapter.onMostPopularItemNameListener() {
            @Override
            public void onItemNameClick(View view, MostPopularInfo mostPopularInfo) {
                //点击作者的事件
//                ToastUtil.showShort(getActivity(),"作者");
            }
        });
        mAdapter.setOnMostPopularItemHeartListener(new MostPopularFragmentAdapter.onMostPopularItemHeartListener() {
            @Override
            public void onItemHeartClick(View view, final CustomSimpleDraweeView item_mostpopular_heart, final MostPopularInfo mostPopularInfo, final TextView textView) {
                final int position = japanese_recycleview.getChildAdapterPosition(view);
                Constants.likenum = Constants.heartNum.get(position).getLikenum();
                if ("null".equals(Constants.heartNum.get(position).getFavourstate())) {
                    Constants.islike = false;
                } else {
                    Constants.islike = true;
                }

                if (Constants.STATUS.equals("reg_log")) {
                    if (Constants.islike == false) {
                        MyHttpUtils.getClickIconHeart(getActivity(), 0, mostPopularInfo.getIcnfid(), new MyHttpUtils.Callback() {
                            @Override
                            public void getResponseMsg(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getInt("errorCode") == 0) {
                                        Constants.islike = true;
                                        Constants.likenum = Constants.likenum + 1;
                                        textView.setText((Constants.likenum) + "");
                                        item_mostpopular_heart.setImageResource(R.mipmap.heart_press);
                                        LogUtils.showVerbose("MostPopularFragment", "点赞成功");
                                        Constants.heartNum.set(position,new MostPopularInfo(Constants.likenum,mostPopularInfo.getIcnfid()+""));
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
                        MyHttpUtils.getClickIconHeart(getActivity(), 1, mostPopularInfo.getIcnfid(), new MyHttpUtils.Callback() {
                            @Override
                            public void getResponseMsg(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getInt("errorCode") == 0) {
                                        Constants.islike = false;
                                        Constants.likenum = Constants.likenum - 1;
                                        textView.setText(Constants.likenum + "");
                                        item_mostpopular_heart.setImageResource(R.mipmap.heart_no_press);
                                        LogUtils.showVerbose("MostPopularFragment", "取消点赞成功");
                                        Constants.heartNum.set(position,new MostPopularInfo(Constants.likenum,"null"));
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
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();
                }


            }
        });
    }
    public void JumpToItemMostpopularActivity(int icnfid, String title, String content, int likenum,
                                              int storenum, int userid, String username, String useravatar,
                                              String favourstate, String storedstate, String lng, String lat,
                                              String address, String createdat, String images,int cmtnum) {
        Intent intent = new Intent(getActivity(), ItemMostpopularActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putInt("icnfid", icnfid);
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putInt("likenum", likenum);
        bundle.putInt("storenum", storenum);
        bundle.putInt("userid", userid);
        bundle.putString("username", username);
        bundle.putString("useravatar", useravatar);
        bundle.putString("favourstate", favourstate);
        bundle.putString("storedstate", storedstate);
        bundle.putString("lng", lng);
        bundle.putString("lat", lat);
        bundle.putString("address", address);
        bundle.putString("createdat", createdat);
        bundle.putString("images", images);
        bundle.putInt("cmtnum", cmtnum);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onRefresh() {
        MostPopularAsyTask asyTask = new MostPopularAsyTask();
        asyTask.execute();
    }

    class MostPopularAsyTask extends AsyncTask<String,Integer,String> {
        @Override
        protected void onPreExecute() {
            japanese_fresh.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {
            num = 0;
            Constants.heartNum.clear();
            MyHttpUtils.getNetMessage(getActivity(), "http://1zou.me/apisq/loadNewestIcons/uid/" + Constants.USER_ID + "/lmt/10", new MyHttpUtils.Callback() {
                @Override
                public void getResponseMsg(String response) {
                    LogUtils.showVerbose("MostPopularFragment", "response=" + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int errorCode = jsonObject.getInt("errorCode");
                        if (errorCode == 0) {
                            //获取信息正确
                            initJsonParse(response);
                        } else {
                            LogUtils.showVerbose("MostPopularFragment", "获取的数据错误");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new MyHttpUtils.CallbackError() {
                @Override
                public void getResponseMsg(String error) {

                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
           japanese_recycleview.scrollToPosition(0);
            japanese_fresh.setRefreshing(false);
        }
    }

    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }
    //初始化的数据的解析
    private void initJsonParse(String string){
        try {
            JSONObject object = new JSONObject(string);
            JSONArray array = object.getJSONArray("datas");
            current_message.clear();
            all_message.clear();
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                int cmtnum = 0;
                if (jsonObject.has("cmtnum")){
                    cmtnum = jsonObject.getInt("cmtnum");
                }else {
                    cmtnum = 0;
                }
                current_message.add(jsonObject.getInt("icnfid"));
                all_message.add(new MostPopularInfo(jsonObject.getInt("icnfid"),
                        jsonObject.getString("title"),
                        jsonObject.getString("content"),
                        jsonObject.getInt("likenum"),
                        jsonObject.getInt("storenum"),
                        jsonObject.getInt("userid"),
                        jsonObject.getString("username"),
                        jsonObject.getString("useravatar"),
                        jsonObject.getString("favourstate"),
                        jsonObject.getString("storedstate"),
                        jsonObject.getString("lng"),
                        jsonObject.getString("lat"),
                        jsonObject.getString("address"),
                        jsonObject.getString("createdat"),
                        jsonObject.getString("images"),
                        jsonObject.getString("weight"),
                        jsonObject.getString("bust"),jsonObject.getString("uaddress"),
                        jsonObject.getString("height"),jsonObject.getString("followstate"),
                        jsonObject.getString("bra"),
                        cmtnum,jsonObject.getString("topics"),jsonObject.getString("browsenum")
                        ,jsonObject.getString("title_cn"),jsonObject.getString("content_cn")));
                Constants.heartNum.add(new MostPopularInfo(
                        jsonObject.getInt("likenum"),
                        jsonObject.getString("favourstate")));

            }
            init();//初始化信息
            //获取最新的fid
            currentFid = JsonParseUtils.getSmallConfid(current_message);
            LogUtils.showVerbose("MostPopularFragment", "最小的icnfig="+currentFid);
        } catch (JSONException e) {
            LogUtils.showVerbose("MostPopularFragment", "第一级数组解析错误");
        }
    }
    private void jsonParse(String string) {
        try {
            JSONObject object = new JSONObject(string);
            JSONArray array = object.getJSONArray("datas");
            current_message.clear();
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                int cmtnum = 0;
                if (jsonObject.has("cmtnum")){
                    cmtnum = jsonObject.getInt("cmtnum");
                }else {
                    cmtnum = 0;
                }
                current_message.add(jsonObject.getInt("icnfid"));
                all_message.add(new MostPopularInfo(jsonObject.getInt("icnfid"),
                        jsonObject.getString("title"),
                        jsonObject.getString("content"),
                        jsonObject.getInt("likenum"),
                        jsonObject.getInt("storenum"),
                        jsonObject.getInt("userid"),
                        jsonObject.getString("username"),
                        jsonObject.getString("useravatar"),
                        jsonObject.getString("favourstate"),
                        jsonObject.getString("storedstate"),
                        jsonObject.getString("lng"),
                        jsonObject.getString("lat"),
                        jsonObject.getString("address"),
                        jsonObject.getString("createdat"),
                        jsonObject.getString("images"),
                        jsonObject.getString("weight"),
                        jsonObject.getString("bust"),jsonObject.getString("uaddress"),
                        jsonObject.getString("height"),jsonObject.getString("followstate"),
                        jsonObject.getString("bra"),
                        cmtnum,jsonObject.getString("topics"),jsonObject.getString("browsenum")
                        ,jsonObject.getString("title_cn"),jsonObject.getString("content_cn")));
                Constants.heartNum.add(new MostPopularInfo(
                        jsonObject.getInt("likenum"),
                        jsonObject.getString("favourstate")));

                LogUtils.showVerbose("MostPopularFragment", "解析信息："+new MostPopularInfo().toString());
            }

            //获取最新的fid
            currentFid = JsonParseUtils.getSmallConfid(current_message);
            LogUtils.showVerbose("MostPopularFragment", "最小的icnfig="+currentFid);

            mAdapter.setShowNumber(all_message);
            mAdapter.notifyItemRangeInserted(mAdapter.getItemCount(), mAdapter.getItemCount() + 10);
            mAdapter.notifyItemRangeChanged(mAdapter.getItemCount(), mAdapter.getItemCount() + 10);
        } catch (JSONException e) {
            LogUtils.showVerbose("MostPopularFragment", "第一级数组解析错误");
        }
    }

}
