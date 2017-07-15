package com.wanta.mobile.wantaproject.fragment;

import android.app.Activity;
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
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.ItemMostpopularActivity;
import com.wanta.mobile.wantaproject.activity.LoginActivity;
import com.wanta.mobile.wantaproject.activity.OtherAuthorActivity;
import com.wanta.mobile.wantaproject.adapter.MostPopularFragmentAdapter;
import com.wanta.mobile.wantaproject.adapter.SameStyleTagChooseRecycleViewAdapter;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.MyScrollview;
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
 * Created by WangYongqiang on 2016/10/22.
 */
public class ItemMostpopularSameStyleSingleFragment extends Fragment {

    private View mView_single_style;
    private String[] single_style_choose;
    private RecyclerView item_mostpopular_single_style_recycleview;
    private RecyclerView single_style_msg_list_recycleview;
    private MostPopularFragmentAdapter mAdapter;
    private List<MostPopularInfo> mFirstMessage;
    private int num = 0;
    private List<Integer> current_message = new ArrayList<>();
    private List<MostPopularInfo> all_message = new ArrayList<>();
    private MyScrollview mItem_mostpopular_scrollview;
    private int currentFid = 0;
    private int mIcnfid;
    private String mTitle;
    private String mContent;
    private int mLikenum;
    private int mStorenum;
    private int mUserid;
    private String mUsername;
    private String mUseravatar;
    private String mFavourstate;
    private String mStoredstate;
    private String mLng;
    private String mLat;
    private String mAddress;
    private String mCreatedat;
    private String mImages;
    private String followstate;
    private String height;
    private String weight;
    private String bust;
    private String bra;
    private int cmtnum;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView_single_style = inflater.inflate(R.layout.item_mostpopular_same_style_single_layout,container,false);
        Bundle bundle = getArguments();
        mIcnfid = bundle.getInt("icnfid");
        mTitle = bundle.getString("title");
        mContent = bundle.getString("content");
        mLikenum = bundle.getInt("likenum");
        mStorenum = bundle.getInt("storenum");
        mUserid = bundle.getInt("userid");
        mUsername = bundle.getString("username");
        mUseravatar = bundle.getString("useravatar");
        mFavourstate = bundle.getString("favourstate");
        mStoredstate = bundle.getString("storedstate");
        mLng = bundle.getString("lng");
        mLat = bundle.getString("lat");
        mAddress = bundle.getString("address");
        mCreatedat = bundle.getString("createdat");
        mImages = bundle.getString("images");
        followstate = bundle.getString("followstate");
        height = bundle.getString("height");
        weight = bundle.getString("weight");
        bust = bundle.getString("bust");
        bra = bundle.getString("bra");
        cmtnum = bundle.getInt("cmtnum");
        init();
        initNetMessage();
        return mView_single_style;
    }

    @Override
    public void onAttach(Activity activity) {
        mItem_mostpopular_scrollview = (MyScrollview) activity.findViewById(R.id.item_mostpopular_scrollview);
        super.onAttach(activity);
    }
    private void init() {
        single_style_choose = getResources().getStringArray(R.array.item_mostpopular_single_style_tabs);
        item_mostpopular_single_style_recycleview = (RecyclerView) mView_single_style.findViewById(R.id.item_mostpopular_single_style_recycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        item_mostpopular_single_style_recycleview.setLayoutManager(linearLayoutManager);
        final SameStyleTagChooseRecycleViewAdapter sameStyleTagChooseRecycleViewAdapter = new SameStyleTagChooseRecycleViewAdapter(getActivity(), single_style_choose);
        item_mostpopular_single_style_recycleview.setAdapter(sameStyleTagChooseRecycleViewAdapter);
        sameStyleTagChooseRecycleViewAdapter.setOnStyleChooseItemListener(new SameStyleTagChooseRecycleViewAdapter.OnStyleChooseItemListener() {
            @Override
            public void setStyleChooseItemClick(View view) {
                int position = item_mostpopular_single_style_recycleview.getChildAdapterPosition(view);
//                ToastUtil.showShort(getActivity(),"单品position="+position);
                sameStyleTagChooseRecycleViewAdapter.setSelectPosition(position);
                sameStyleTagChooseRecycleViewAdapter.notifyDataSetChanged();
            }
        });
//        currentFid = Constants.minConfid;
//        all_message.clear();
//        all_message = Constants.style_data;
//        initId();
    }

    private void initNetMessage() {
        MyHttpUtils.getNetMessage(getActivity(), "http://1zou.me/apisq/loadNewestIcons/uid/"+Constants.USER_ID+"/lmt/5", new MyHttpUtils.Callback() {
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
    }
    private void initId() {

        single_style_msg_list_recycleview = (RecyclerView) mView_single_style.findViewById(R.id.single_style_msg_list_recycleview);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        single_style_msg_list_recycleview.setLayoutManager(staggeredGridLayoutManager);
        mAdapter = new MostPopularFragmentAdapter(getActivity(),all_message);
        single_style_msg_list_recycleview.setAdapter(mAdapter);
        mItem_mostpopular_scrollview.setBottomListener(new MyScrollview.getBottomListener() {
            @Override
            public void getBottomPosition(int d) {
                if (d == 0) {
                    LogUtils.showVerbose("ItemMostpopularSameStyleMatchFragment", "我倒底部了");
                    num = num + 1;
                    currentFid = currentFid - 5;
                    LogUtils.showVerbose("MostPopularFragment", "当前的number=" + currentFid);
                    MyHttpUtils.getNetMessage(getActivity(), "http://1zou.me/apisq/loadIconsBMinF/uid/"+Constants.USER_ID+"/lmt/5/minfid/" + currentFid, new MyHttpUtils.Callback() {
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
                                                           final int position = single_style_msg_list_recycleview.getChildAdapterPosition(view);
                                                           Constants.likenum = Constants.heartNum.get(position).getLikenum();
                                                           if ("null".equals(Constants.heartNum.get(position).getFavourstate())) {
                                                               Constants.islike = false;
                                                           } else {
                                                               Constants.islike = true;
                                                           }

                                                           //点击图片的事件
                                                           JumpToItemMostpopularActivity(
                                                                   mostPopularInfo.getIcnfid(), mostPopularInfo.getTitle(), mostPopularInfo.getContent(), mostPopularInfo.getLikenum(), mostPopularInfo.getStorenum(),
                                                                   mostPopularInfo.getUserid(), mostPopularInfo.getUsername(), mostPopularInfo.getUseravatar(), mostPopularInfo.getFavourstate(), mostPopularInfo.getStoredstate(),
                                                                   mostPopularInfo.getLng(), mostPopularInfo.getLat(), mostPopularInfo.getAddress(), mostPopularInfo.getCreatedat(), mostPopularInfo.getImages(),
                                                                   mostPopularInfo.getFollowstate(),mostPopularInfo.getHeight(),mostPopularInfo.getWeight(),mostPopularInfo.getBust(),mostPopularInfo.getBra(),mostPopularInfo.getCmtnum()
                                                           );
                                                       }
                                                   }

        );
        mAdapter.setOnMostPopularItemTitleListener(new MostPopularFragmentAdapter.onMostPopularItemTitleListener() {
                                                       @Override
                                                       public void onItemTitlsClick(View view, MostPopularInfo mostPopularInfo) {
                                                           //点击标题的事件
                                                           final int position = single_style_msg_list_recycleview.getChildAdapterPosition(view);
                                                           Constants.likenum = Constants.heartNum.get(position).getLikenum();
                                                           if ("null".equals(Constants.heartNum.get(position).getFavourstate())) {
                                                               Constants.islike = false;
                                                           } else {
                                                               Constants.islike = true;
                                                           }

                                                           //点击标题的事件
//                ToastUtil.showShort(getActivity(),"标题");
                                                           JumpToItemMostpopularActivity(
                                                                   mostPopularInfo.getIcnfid(), mostPopularInfo.getTitle(), mostPopularInfo.getContent(), mostPopularInfo.getLikenum(), mostPopularInfo.getStorenum(),
                                                                   mostPopularInfo.getUserid(), mostPopularInfo.getUsername(), mostPopularInfo.getUseravatar(), mostPopularInfo.getFavourstate(), mostPopularInfo.getStoredstate(),
                                                                   mostPopularInfo.getLng(), mostPopularInfo.getLat(), mostPopularInfo.getAddress(), mostPopularInfo.getCreatedat(), mostPopularInfo.getImages(),
                                                                   mostPopularInfo.getFollowstate(),mostPopularInfo.getHeight(),mostPopularInfo.getWeight(),mostPopularInfo.getBust(),mostPopularInfo.getBra(),mostPopularInfo.getCmtnum()
                                                           );
                                                       }
                                                   }

        );
        mAdapter.setOnMostPopularItemContentListener(new MostPopularFragmentAdapter.onMostPopularItemContentListener() {
                                                         @Override
                                                         public void onItemContentClick(View view, MostPopularInfo mostPopularInfo) {
                                                             //点击内容的事件
                                                             final int position = single_style_msg_list_recycleview.getChildAdapterPosition(view);
                                                             Constants.likenum = Constants.heartNum.get(position).getLikenum();
                                                             if ("null".equals(Constants.heartNum.get(position).getFavourstate())) {
                                                                 Constants.islike = false;
                                                             } else {
                                                                 Constants.islike = true;
                                                             }

                                                             //点击内容的事件
                                                             JumpToItemMostpopularActivity(
                                                                     mostPopularInfo.getIcnfid(), mostPopularInfo.getTitle(), mostPopularInfo.getContent(), mostPopularInfo.getLikenum(), mostPopularInfo.getStorenum(),
                                                                     mostPopularInfo.getUserid(), mostPopularInfo.getUsername(), mostPopularInfo.getUseravatar(), mostPopularInfo.getFavourstate(), mostPopularInfo.getStoredstate(),
                                                                     mostPopularInfo.getLng(), mostPopularInfo.getLat(), mostPopularInfo.getAddress(), mostPopularInfo.getCreatedat(), mostPopularInfo.getImages(),
                                                                     mostPopularInfo.getFollowstate(),mostPopularInfo.getHeight(),mostPopularInfo.getWeight(),mostPopularInfo.getBust(),mostPopularInfo.getBra(),mostPopularInfo.getCmtnum()
                                                             );
                                                         }
                                                     }

        );
        mAdapter.setOnMostPopularItemIconListener(new MostPopularFragmentAdapter.onMostPopularItemIconListener() {
                                                      @Override
                                                      public void onItemIconClick(View view, MostPopularInfo mostPopularInfo) {
                                                          //点击图标的事件
//                                                          ToastUtil.showShort(getActivity(), "图标");
                                                          Intent intent_to_other_author = new Intent(getActivity(),OtherAuthorActivity.class);
                                                          intent_to_other_author.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                          intent_to_other_author.putExtra("itemmostpopular","itemmostpopular");
                                                          Bundle bundle_to_other_author = new Bundle();
                                                          bundle_to_other_author.putInt("icnfid", mIcnfid);
                                                          bundle_to_other_author.putString("title", mTitle);
                                                          bundle_to_other_author.putString("content", mContent);
                                                          bundle_to_other_author.putInt("likenum", mLikenum);
                                                          bundle_to_other_author.putInt("storenum", mStorenum);
                                                          bundle_to_other_author.putInt("userid", mUserid);
                                                          bundle_to_other_author.putString("username", mUsername);
                                                          bundle_to_other_author.putString("useravatar", mUseravatar);
                                                          bundle_to_other_author.putString("favourstate", mFavourstate);
                                                          bundle_to_other_author.putString("storedstate", mStoredstate);
                                                          bundle_to_other_author.putString("lng", mLng);
                                                          bundle_to_other_author.putString("lat", mLat);
                                                          bundle_to_other_author.putString("address", mAddress);
                                                          bundle_to_other_author.putString("createdat", mCreatedat);
                                                          bundle_to_other_author.putString("followstate", followstate);
                                                          bundle_to_other_author.putString("height", height);
                                                          bundle_to_other_author.putString("weight", weight);
                                                          bundle_to_other_author.putString("bust", bust);
                                                          bundle_to_other_author.putString("bra", bra);
                                                          bundle_to_other_author.putString("images", mImages);
                                                          bundle_to_other_author.putInt("cmtnum", cmtnum);
                                                          intent_to_other_author.putExtras(bundle_to_other_author);
                                                          startActivity(intent_to_other_author);
                                                          getActivity().finish();
                                                      }
                                                  }

        );
        mAdapter.setOnMostPopularItemNameListener(new MostPopularFragmentAdapter.onMostPopularItemNameListener() {
                                                      @Override
                                                      public void onItemNameClick(View view, MostPopularInfo mostPopularInfo) {
                                                          //点击作者的事件
//                                                          ToastUtil.showShort(getActivity(), "作者");
                                                      }
                                                  }

        );
        mAdapter.setOnMostPopularItemHeartListener(new MostPopularFragmentAdapter.onMostPopularItemHeartListener() {
                                                       @Override
                                                       public void onItemHeartClick(View view, final CustomSimpleDraweeView item_mostpopular_heart, final MostPopularInfo mostPopularInfo, final TextView textView) {
                                                           final int position = single_style_msg_list_recycleview.getChildAdapterPosition(view);
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
                                                   }

        );
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

            }
            initId();//初始化信息
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

                LogUtils.showVerbose("MostPopularFragment", "解析信息："+new MostPopularInfo().toString());
            }

            //获取最新的fid
            currentFid = JsonParseUtils.getSmallConfid(current_message);
            LogUtils.showVerbose("MostPopularFragment", "最小的icnfig="+currentFid);

            mAdapter.setShowNumber(all_message);
            mAdapter.notifyItemRangeInserted(mAdapter.getItemCount(), mAdapter.getItemCount() + 5);
            mAdapter.notifyItemRangeChanged(mAdapter.getItemCount(), mAdapter.getItemCount() + 5);
        } catch (JSONException e) {
            LogUtils.showVerbose("MostPopularFragment", "第一级数组解析错误");
        }
    }
    public void JumpToItemMostpopularActivity(int icnfid, String title, String content, int likenum,
                                              int storenum, int userid, String username, String useravatar,
                                              String favourstate, String storedstate, String lng, String lat,
                                              String address, String createdat, String images,String followstate,
                                              String height,String weight,String bust,String bra,int cmtnum) {
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
        bundle.putString("followstate", followstate);
        bundle.putString("height", height);
        bundle.putString("weight", weight);
        bundle.putString("bust", bust);
        bundle.putString("bra", bra);
        bundle.putInt("cmtnum", cmtnum);
        Constants.allComments = cmtnum;//记录当前总的评论数

        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().finish();
    }
}
