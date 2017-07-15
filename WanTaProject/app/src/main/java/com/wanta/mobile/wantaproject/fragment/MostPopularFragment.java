package com.wanta.mobile.wantaproject.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.ItemMostpopularActivity;
import com.wanta.mobile.wantaproject.activity.LoginActivity;
import com.wanta.mobile.wantaproject.activity.OtherAuthorActivity;
import com.wanta.mobile.wantaproject.adapter.MostPopularFragmentAdapter;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.domain.CacheDataUtils;
import com.wanta.mobile.wantaproject.domain.MostPopularInfo;
import com.wanta.mobile.wantaproject.utils.Constants;
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
 * Created by WangYongqiang on 2016/10/15.
 */
public class MostPopularFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View mView_most_popular;
    private SwipeRefreshLayout mMostpopular_fresh;
    private RecyclerView mMostpopular_recycleview;
    private MostPopularFragmentAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private int num = 0;
    private int currentFid = 0;
    private List<Integer> current_message = new ArrayList<>();
    private List<MostPopularInfo> all_message = new ArrayList<>();
    private int isClickFlag = 0;//0表示第一次可以获取，1表示不能再获取了
    private LinearLayout wardrober_head_title;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView_most_popular = inflater.inflate(R.layout.fragment_most_popular, container, false);
        //加载信息
        initId();
        return mView_most_popular;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    //    @Override
//    public void onAttach(Activity activity) {
//        wardrober_head_title = (LinearLayout) activity.findViewById(R.id.head_title);
//        wardrober_head_title.setVisibility(View.VISIBLE);
//        super.onAttach(activity);
//    }

    @Override
    public void onDestroyView() {
        mMostpopular_recycleview.setAdapter(null);
        mMostpopular_recycleview = null;
        super.onDestroyView();
    }

    private void initId() {
        mMostpopular_fresh = (SwipeRefreshLayout) mView_most_popular.findViewById(R.id.mostpopular_fresh);
        mMostpopular_fresh.setOnRefreshListener(this);
//        //改变加载显示的颜色
        mMostpopular_fresh.setColorSchemeResources(R.color.mostpopular_fresh_color);
        mMostpopular_recycleview = (RecyclerView) mView_most_popular.findViewById(R.id.mostpopular_recycleview);
        //放在子线程中是因为mostpopular_fresh还没有初始化完，加载的图标是不会显示的
        mMostpopular_fresh.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });
    }

    private void init() {
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//防止item来回的变换
        mMostpopular_recycleview.setLayoutManager(staggeredGridLayoutManager);
        mMostpopular_recycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                staggeredGridLayoutManager.invalidateSpanAssignments();//防止出现空白的部分
            }
        });
        mAdapter = new MostPopularFragmentAdapter(context, all_message);
        mMostpopular_recycleview.setAdapter(mAdapter);
        mMostpopular_recycleview.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (NetUtils.checkNet(context)==true){
                        MyHttpUtils.getNetMessage(context, "http://1zou.me/apisq/loadIconsBMinF/uid/" + Constants.USER_ID + "/lmt/10/minfid/" + currentFid, new MyHttpUtils.Callback() {
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
                      NetUtils.showNoNetDialog(context);
                    }

                } else {
//                    LogUtils.showVerbose("MostPopularFragment", "当前的number=" + currentFid);
                }
            }
        });
        mAdapter.setOnMostPopularItemImageListener(new MostPopularFragmentAdapter.onMostPopularItemImageListener() {
            @Override
            public void onItemImageClick(View view, MostPopularInfo mostPopularInfo) {
                final int position = mMostpopular_recycleview.getChildAdapterPosition(view);
                Constants.likenum = Constants.heartNum.get(position).getLikenum();
                if ("null".equals(Constants.heartNum.get(position).getFavourstate())) {
                    Constants.islike = false;
                } else {
                    Constants.islike = true;
                }
                if ("null".equals(Constants.heartNum.get(position).getFollowstate())) {
                    Constants.isCare = false;
                } else {
                    Constants.isCare = true;
                }

                //点击图片的事件
                JumpToItemMostpopularActivity(
                        mostPopularInfo.getIcnfid(), mostPopularInfo.getTitle(), mostPopularInfo.getContent(), mostPopularInfo.getLikenum(), mostPopularInfo.getStorenum(),
                        mostPopularInfo.getUserid(), mostPopularInfo.getUsername(), mostPopularInfo.getUseravatar(), mostPopularInfo.getFavourstate(), mostPopularInfo.getStoredstate(),
                        mostPopularInfo.getLng(), mostPopularInfo.getLat(), mostPopularInfo.getAddress(), mostPopularInfo.getUaddress(), mostPopularInfo.getCreatedat(), mostPopularInfo.getImages(),
                        mostPopularInfo.getFollowstate(), mostPopularInfo.getHeight(), mostPopularInfo.getWeight(), mostPopularInfo.getBust(), mostPopularInfo.getBra(), mostPopularInfo.getCmtnum()
                        ,mostPopularInfo.getTopics(),mostPopularInfo.getBrowsenum(),mostPopularInfo.getTitle_cn(),mostPopularInfo.getContent_cn()
                );
            }
        });
        mAdapter.setOnMostPopularItemTitleListener(new MostPopularFragmentAdapter.onMostPopularItemTitleListener() {
            @Override
            public void onItemTitlsClick(View view, MostPopularInfo mostPopularInfo) {
                final int position = mMostpopular_recycleview.getChildAdapterPosition(view);
                Constants.likenum = Constants.heartNum.get(position).getLikenum();
                if ("null".equals(Constants.heartNum.get(position).getFavourstate())) {
                    Constants.islike = false;
                } else {
                    Constants.islike = true;
                }
                if ("null".equals(Constants.heartNum.get(position).getFollowstate())) {
                    Constants.isCare = false;
                } else {
                    Constants.isCare = true;
                }

                //点击标题的事件
                JumpToItemMostpopularActivity(
                        mostPopularInfo.getIcnfid(), mostPopularInfo.getTitle(), mostPopularInfo.getContent(), mostPopularInfo.getLikenum(), mostPopularInfo.getStorenum(),
                        mostPopularInfo.getUserid(), mostPopularInfo.getUsername(), mostPopularInfo.getUseravatar(), mostPopularInfo.getFavourstate(), mostPopularInfo.getStoredstate(),
                        mostPopularInfo.getLng(), mostPopularInfo.getLat(), mostPopularInfo.getAddress(), mostPopularInfo.getUaddress(), mostPopularInfo.getCreatedat(), mostPopularInfo.getImages(),
                        mostPopularInfo.getFollowstate(), mostPopularInfo.getHeight(), mostPopularInfo.getWeight(), mostPopularInfo.getBust(), mostPopularInfo.getBra(), mostPopularInfo.getCmtnum()
                        ,mostPopularInfo.getTopics(),mostPopularInfo.getBrowsenum(),mostPopularInfo.getTitle_cn(),mostPopularInfo.getContent_cn()
                );
            }
        });
        mAdapter.setOnMostPopularItemContentListener(new MostPopularFragmentAdapter.onMostPopularItemContentListener() {
            @Override
            public void onItemContentClick(View view, MostPopularInfo mostPopularInfo) {
                final int position = mMostpopular_recycleview.getChildAdapterPosition(view);
                Constants.likenum = Constants.heartNum.get(position).getLikenum();
                if ("null".equals(Constants.heartNum.get(position).getFavourstate())) {
                    Constants.islike = false;
                } else {
                    Constants.islike = true;
                }
                if ("null".equals(Constants.heartNum.get(position).getFollowstate())) {
                    Constants.isCare = false;
                } else {
                    Constants.isCare = true;
                }

                //点击标题的事件
                JumpToItemMostpopularActivity(
                        mostPopularInfo.getIcnfid(), mostPopularInfo.getTitle(), mostPopularInfo.getContent(), mostPopularInfo.getLikenum(), mostPopularInfo.getStorenum(),
                        mostPopularInfo.getUserid(), mostPopularInfo.getUsername(), mostPopularInfo.getUseravatar(), mostPopularInfo.getFavourstate(), mostPopularInfo.getStoredstate(),
                        mostPopularInfo.getLng(), mostPopularInfo.getLat(), mostPopularInfo.getAddress(), mostPopularInfo.getUaddress(), mostPopularInfo.getCreatedat(), mostPopularInfo.getImages(),
                        mostPopularInfo.getFollowstate(), mostPopularInfo.getHeight(), mostPopularInfo.getWeight(), mostPopularInfo.getBust(), mostPopularInfo.getBra(), mostPopularInfo.getCmtnum()
                        ,mostPopularInfo.getTopics(),mostPopularInfo.getBrowsenum(),mostPopularInfo.getTitle_cn(),mostPopularInfo.getContent_cn()
                );
            }
        });
        mAdapter.setOnMostPopularItemIconListener(new MostPopularFragmentAdapter.onMostPopularItemIconListener() {
            @Override
            public void onItemIconClick(View view, MostPopularInfo mostPopularInfo) {
                //点击图标的事件
                JumpToOtherAuthorActivity(
                        mostPopularInfo.getIcnfid(), mostPopularInfo.getTitle(), mostPopularInfo.getContent(), mostPopularInfo.getLikenum(), mostPopularInfo.getStorenum(),
                        mostPopularInfo.getUserid(), mostPopularInfo.getUsername(), mostPopularInfo.getUseravatar(), mostPopularInfo.getFavourstate(), mostPopularInfo.getStoredstate(),
                        mostPopularInfo.getLng(), mostPopularInfo.getLat(), mostPopularInfo.getAddress(), mostPopularInfo.getUaddress(), mostPopularInfo.getCreatedat(), mostPopularInfo.getImages(),
                        mostPopularInfo.getFollowstate(), mostPopularInfo.getHeight(), mostPopularInfo.getWeight(), mostPopularInfo.getBust(), mostPopularInfo.getBra(), mostPopularInfo.getCmtnum()
                        ,mostPopularInfo.getTopics(),mostPopularInfo.getBrowsenum(),mostPopularInfo.getTitle_cn(),mostPopularInfo.getContent_cn()
                );
            }
        });
        mAdapter.setOnMostPopularItemNameListener(new MostPopularFragmentAdapter.onMostPopularItemNameListener() {
            @Override
            public void onItemNameClick(View view, MostPopularInfo mostPopularInfo) {
                //点击作者的事件
            }
        });

        mAdapter.setOnMostPopularItemHeartListener(new MostPopularFragmentAdapter.onMostPopularItemHeartListener() {
            @Override
            public void onItemHeartClick(View view, final CustomSimpleDraweeView item_mostpopular_heart, final MostPopularInfo mostPopularInfo, final TextView textView) {
                if (NetUtils.checkNet(context)==true){
                    final int position = mMostpopular_recycleview.getChildAdapterPosition(view);
                    Constants.likenum = Constants.heartNum.get(position).getLikenum();
                    //设置选择的状态
                    if ("null".equals(Constants.heartNum.get(position).getFavourstate())) {
                        Constants.islike = false;
                    } else {
                        Constants.islike = true;
                    }

                    if (Constants.STATUS.equals("reg_log")) {
                        if (Constants.islike == false) {
                            MyHttpUtils.getClickIconHeart(context, 0, mostPopularInfo.getIcnfid(), new MyHttpUtils.Callback() {
                                @Override
                                public void getResponseMsg(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getInt("errorCode") == 0) {
                                            Constants.islike = true;
                                            Constants.likenum = Constants.likenum + 1;
                                            textView.setText((Constants.likenum) + "");
//                                            item_mostpopular_heart.setImageResource(R.mipmap.heart_press);
//                                            item_mostpopular_heart.setImageBitmap(ImageDealUtils.readBitMap(context,R.mipmap.heart_press));
                                            item_mostpopular_heart.setImageURI(Uri.parse("res://com.wanta.mobile.wantaproject/" + R.mipmap.heart_press));
                                            LogUtils.showVerbose("MostPopularFragment", "点赞成功");
                                            Constants.heartNum.set(position, new MostPopularInfo(Constants.likenum, mostPopularInfo.getIcnfid() + ""));
                                        }
                                    } catch (JSONException e) {
                                        LogUtils.showVerbose("MostPopularFragment", "获取数据失败");
//                                        jumpToMainActivity();
                                    }
                                }
                            }, new MyHttpUtils.CallbackError() {
                                @Override
                                public void getResponseMsg(String error) {
                                    LogUtils.showVerbose("MostPopularFragment", "点赞失败");
//                                    jumpToMainActivity();
                                }
                            });

                        } else {
                            MyHttpUtils.getClickIconHeart(context, 1, mostPopularInfo.getIcnfid(), new MyHttpUtils.Callback() {
                                @Override
                                public void getResponseMsg(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getInt("errorCode") == 0) {
                                            Constants.islike = false;
                                            Constants.likenum = Constants.likenum - 1;
                                            textView.setText(Constants.likenum + "");
                                            item_mostpopular_heart.setImageURI(Uri.parse("res://com.wanta.mobile.wantaproject/" + R.mipmap.heart_no_press));
//                                            item_mostpopular_heart.setImageBitmap(ImageDealUtils.readBitMap(context,R.mipmap.heart_no_press));
//                                            item_mostpopular_heart.setImageResource(R.mipmap.heart_no_press);
                                            LogUtils.showVerbose("MostPopularFragment", "取消点赞成功");
                                            Constants.heartNum.set(position, new MostPopularInfo(Constants.likenum, "null"));
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
                        Constants.saveCacheDataList.clear();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        ((Activity)context).finish();
                    }
                }else {
                    NetUtils.showNoNetDialog(context);
                }
            }
        });
    }

    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    public void JumpToItemMostpopularActivity(int icnfid, String title, String content, int likenum,
                                              int storenum, int userid, String username, String useravatar,
                                              String favourstate, String storedstate, String lng, String lat,
                                              String address, String uaddress, String createdat, String images, String followstate,
                                              String height, String weight, String bust, String bra,
                                              int cmtnum,String topics,String browsenum,String title_cn,String content_cn) {
        Intent intent = new Intent(context, ItemMostpopularActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CacheDataUtils.setCurrentNeedToSaveData(icnfid,title,content,likenum,storenum,userid,username,useravatar,favourstate,
                storedstate,lng,lat,address,uaddress,createdat,images,followstate,height,weight,bust,bra,cmtnum,topics,browsenum,title_cn,content_cn);
        Constants.allComments = cmtnum;//记录当前总的评论数

        startActivity(intent);
        ((Activity)context).finish();
    }
    public void JumpToOtherAuthorActivity(int icnfid, String title, String content, int likenum,
                                              int storenum, int userid, String username, String useravatar,
                                              String favourstate, String storedstate, String lng, String lat,
                                              String address, String uaddress, String createdat, String images, String followstate,
                                              String height, String weight, String bust, String bra,
                                              int cmtnum,String topics,String browsenum,String title_cn,String content_cn) {
        Intent intent = new Intent(context, OtherAuthorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("mostpopular", "mostpopular");
        CacheDataUtils.setCurrentNeedToSaveData(icnfid,title,content,likenum,storenum,userid,username,useravatar,favourstate,
                storedstate,lng,lat,address,uaddress,createdat,images,followstate,height,weight,bust,bra,cmtnum,topics,browsenum,title_cn,content_cn);
        startActivity(intent);
        ((Activity)context).finish();
    }

    @Override
    public void onRefresh() {
        MostPopularAsyTask asyTask = new MostPopularAsyTask();
        asyTask.execute();
    }

    class MostPopularAsyTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            mMostpopular_fresh.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {
            num = 0;
            isClickFlag = 0;
            Constants.heartNum.clear();
            if (NetUtils.checkNet(context)==true){
                MyHttpUtils.getNetMessage(context, "http://1zou.me/apisq/loadNewestIcons/uid/" + Constants.USER_ID + "/lmt/10", new MyHttpUtils.Callback() {
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
                                LogUtils.showVerbose("MostPopularFragment", "获取的数据错误");
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
               NetUtils.showNoNetDialog(context);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            mMostpopular_recycleview.scrollToPosition(0);
            mMostpopular_fresh.setRefreshing(false);
        }
    }

    //初始化的数据的解析
    private void initJsonParse(String string) {
        try {
            JSONObject object = new JSONObject(string);
            JSONArray array = object.getJSONArray("datas");
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
                        jsonObject.getString("bust"), jsonObject.getString("uaddress"),
                        jsonObject.getString("height"), jsonObject.getString("followstate"),
                        jsonObject.getString("bra"),
                        cmtnum, jsonObject.getString("topics"), jsonObject.getString("browsenum")
                ,jsonObject.getString("title_cn"),jsonObject.getString("content_cn")));
                Constants.heartNum.add(new MostPopularInfo(
                        jsonObject.getInt("likenum"),
                        jsonObject.getString("favourstate")));

            }
            init();//初始化信息
            //获取最新的fid
            currentFid = JsonParseUtils.getSmallConfid(current_message);
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
                        jsonObject.getString("bust"), jsonObject.getString("uaddress"),
                        jsonObject.getString("height"), jsonObject.getString("followstate"),
                        jsonObject.getString("bra"),
                        cmtnum, jsonObject.getString("topics"), jsonObject.getString("browsenum")
                        ,jsonObject.getString("title_cn"),jsonObject.getString("content_cn")));
                Constants.heartNum.add(new MostPopularInfo(
                        jsonObject.getInt("likenum"),
                        jsonObject.getString("favourstate")));

                LogUtils.showVerbose("MostPopularFragment", "获取的信息=：" + jsonObject.toString());
            }

            //获取最新的fid
            currentFid = JsonParseUtils.getSmallConfid(current_message);
            LogUtils.showVerbose("MostPopularFragment", "最小的icnfig=" + currentFid);

            mAdapter.setShowNumber(all_message);
            mAdapter.notifyItemRangeInserted(mAdapter.getItemCount(), mAdapter.getItemCount() + 10);
            mAdapter.notifyItemRangeChanged(mAdapter.getItemCount(), mAdapter.getItemCount() + 10);
        } catch (JSONException e) {
            LogUtils.showVerbose("MostPopularFragment", "第一级数组解析错误");
        }
    }

}

