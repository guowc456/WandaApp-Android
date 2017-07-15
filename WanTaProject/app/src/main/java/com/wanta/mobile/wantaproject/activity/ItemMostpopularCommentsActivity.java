package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.ItemMostPopularRecycleViewAdapter;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.KeyboardListenRelativeLayout;
import com.wanta.mobile.wantaproject.domain.CacheDataUtils;
import com.wanta.mobile.wantaproject.domain.CommentsInfo;
import com.wanta.mobile.wantaproject.domain.ReplyCommentInfo;
import com.wanta.mobile.wantaproject.uploadimage.DividerItemDecoration;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.DealReturnLogicUtils;
import com.wanta.mobile.wantaproject.utils.JsonParseUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;
import com.wanta.mobile.wantaproject.utils.SystemUtils;
import com.wanta.mobile.wantaproject.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/11/22.
 */
public class ItemMostpopularCommentsActivity extends BaseActivity implements View.OnClickListener {

    private CustomSimpleDraweeView mItem_mostpopular_comment_back;
    private RecyclerView mItem_mostpopular_comment_recycleview;
    private List<ReplyCommentInfo> mReplyCommentList = new ArrayList<>();
    private LinearLayout mItem_mostpopular_comment_edit;
    private LinearLayout mItem_mostpopular_comment_publish;
    private EditText mItem_mostpopular_comment_edit_input;
    private TextView mItem_mostpopular_comment_publish_ok;
    private KeyboardListenRelativeLayout mKeyboardListener;
    private EditText mItem_mostpopular_comment_publish_input;
    private int COMMENT_SUCCESS = 5;
    private List<CommentsInfo> all_commnetss = new ArrayList<>();//保存所有的信息
    private List<CommentsInfo> mComList = new ArrayList<>();//备份
    private List<Integer> current_message = new ArrayList<>();//保存当前的的评论的id
    private int mSmallConfid;
    int num = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_mostpopular_comments);
        all_commnetss.clear();
        initId();
        loadNetMessage();
//        initData();
    }

    private void loadNetMessage() {
        MyHttpUtils.getNetMessage(this, "http://1zou.me/apisq/loadNewCmts/" + CacheDataUtils.getCurrentNeedToSaveData().getIcnfid() + "/" + Constants.USER_ID + "/10", new MyHttpUtils.Callback() {
            @Override
            public void getResponseMsg(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int errorCode = jsonObject.getInt("errorCode");
                    LogUtils.showVerbose("ItemMostpopularCommentsActivity", "response=" + response);
                    if (errorCode == 0) {
                        jsonParse(response);
                    } else {
                        LogUtils.showVerbose("ItemMostpopularCommentsActivity", "获取网络评论信息错误");
                    }
                } catch (JSONException e) {
                    LogUtils.showVerbose("ItemMostpopularCommentsActivity", "对评论信息解析错误");
                }
            }
        }, new MyHttpUtils.CallbackError() {
            @Override
            public void getResponseMsg(String error) {

            }
        });
    }

    private void initId() {
        mItem_mostpopular_comment_back = (CustomSimpleDraweeView) this.findViewById(R.id.item_mostpopular_comments_back);
        mItem_mostpopular_comment_back.setWidth(Constants.PHONE_WIDTH/12);
        mItem_mostpopular_comment_back.setHeight(Constants.PHONE_WIDTH/12);
        mItem_mostpopular_comment_back.setImageResource(R.mipmap.pre_arrows);
//        mItem_mostpopular_comment_back.setSize(Constants.PHONE_WIDTH/12,Constants.PHONE_WIDTH/12);
        mItem_mostpopular_comment_back.setOnClickListener(this);
        mItem_mostpopular_comment_recycleview = (RecyclerView) this.findViewById(R.id.item_mostpopular_comment_recycleview);

        //评价
        mItem_mostpopular_comment_edit = (LinearLayout) this.findViewById(R.id.item_mostpopular_comment_edit);
        mItem_mostpopular_comment_publish = (LinearLayout) this.findViewById(R.id.item_mostpopular_comment_publish);
        mItem_mostpopular_comment_edit_input = (EditText) this.findViewById(R.id.item_mostpopular_comment_edit_input);
        mItem_mostpopular_comment_publish_ok = (TextView) this.findViewById(R.id.item_mostpopular_comment_publish_ok);
        mItem_mostpopular_comment_publish_input = (EditText) this.findViewById(R.id.item_mostpopular_comment_publish_input);
        mItem_mostpopular_comment_publish_ok.setOnClickListener(this);
        mItem_mostpopular_comment_edit_input.setOnClickListener(this);
        mItem_mostpopular_comment_edit.setVisibility(View.VISIBLE);
        mItem_mostpopular_comment_publish.setVisibility(View.GONE);

        mKeyboardListener = (KeyboardListenRelativeLayout) this.findViewById(R.id.keyboardListener);
        mKeyboardListener.setOnKeyboardStateChangedListener(new KeyboardListenRelativeLayout.IOnKeyboardStateChangedListener() {
            @Override
            public void onKeyboardStateChanged(int state) {
                switch (state) {
                    case KeyboardListenRelativeLayout.KEYBOARD_STATE_HIDE://软键盘隐藏
                        mItem_mostpopular_comment_edit.setVisibility(View.VISIBLE);
                        mItem_mostpopular_comment_publish.setVisibility(View.GONE);
                        break;
                    case KeyboardListenRelativeLayout.KEYBOARD_STATE_SHOW://软键盘显示
                        mItem_mostpopular_comment_edit.setVisibility(View.GONE);
                        mItem_mostpopular_comment_publish.setVisibility(View.VISIBLE);
                        mItem_mostpopular_comment_publish_input.requestFocus();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initData() {
        ItemMostPopularRecycleViewAdapter recycleViewAdapter = new ItemMostPopularRecycleViewAdapter(this);
        recycleViewAdapter.setShowNum(all_commnetss);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mItem_mostpopular_comment_recycleview.setLayoutManager(linearLayoutManager);
        mItem_mostpopular_comment_recycleview.setAdapter(recycleViewAdapter);
        mItem_mostpopular_comment_recycleview.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST,R.color.line_color));
        mItem_mostpopular_comment_recycleview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                LogUtils.showVerbose("MostPopularFragment", "是否滑动到底=" + isSlideToBottom(recyclerView));
                if (isSlideToBottom(recyclerView)) {
                    //判断当前的数据是否加载完
                    if (num*10<=all_commnetss.size()){
                        num = num + 1;
                        MyHttpUtils.getNetMessage(ItemMostpopularCommentsActivity.this, "http://1zou.me/apisq/loadCmtByMinFid/" + CacheDataUtils.getCurrentNeedToSaveData().getIcnfid() + "/" + Constants.USER_ID + "/" + mSmallConfid + "/" + 10, new MyHttpUtils.Callback() {
                            @Override
                            public void getResponseMsg(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int errorCode = jsonObject.getInt("errorCode");
                                    if (errorCode == 0) {
                                        //获取信息正确
                                        jsonParse(response);
                                    } else {
                                        LogUtils.showVerbose("ItemMostpopularCommentsActivity", "获取的数据错误");
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

                } else {
//                    LogUtils.showVerbose("MostPopularFragment", "当前的number=" + currentFid);
                }
            }
        });
        //设置点赞的功能
        recycleViewAdapter.setOnAgreeClickListener(new ItemMostPopularRecycleViewAdapter.OnAgreeClickListener() {
            @Override
            public void agreeclick(View itemView, final TextView like_num, final CustomSimpleDraweeView draweeView) {
                //处理评论中点赞的逻辑
                final int position = mItem_mostpopular_comment_recycleview.getChildAdapterPosition(itemView);
                if ("null".equals(mComList.get(position).getCmtfavour())) {
                    Constants.isLikeComment = false;
                } else {
                    Constants.isLikeComment = true;
                }
                Constants.currentCommnetNum = mComList.get(position).getLikednum();
                if (Constants.isLikeComment == true) {
                    //当前处于点赞的状态
                    MyHttpUtils.getHandCommentClick(ItemMostpopularCommentsActivity.this, 1, mComList.get(position).getCmtid(), new MyHttpUtils.Callback() {
                        @Override
                        public void getResponseMsg(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int errorCode = jsonObject.getInt("errorCode");
                                if (errorCode==0){
                                    Constants.isLikeComment = false;
                                    Constants.currentCommnetNum = Constants.currentCommnetNum -1 ;
                                    like_num.setText(Constants.currentCommnetNum+"");
//                                    draweeView.setImageResource(R.mipmap.agree_click);
                                    mComList.set(position,new CommentsInfo(mComList.get(position).getCmtid(),Constants.currentCommnetNum,"null"));
                                    Toast.makeText(ItemMostpopularCommentsActivity.this,"取消点赞成功",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                LogUtils.showVerbose("ItemMostpopularCommentsActivity","当前信息解析错误");
                            }
                        }
                    }, new MyHttpUtils.CallbackError() {
                        @Override
                        public void getResponseMsg(String error) {

                        }
                    });
                } else {
                    //当前没有点赞
                    MyHttpUtils.getHandCommentClick(ItemMostpopularCommentsActivity.this, 0, mComList.get(position).getCmtid(), new MyHttpUtils.Callback() {
                        @Override
                        public void getResponseMsg(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int errorCode = jsonObject.getInt("errorCode");
                                if (errorCode==0){
                                    Constants.isLikeComment = true;
                                    Constants.currentCommnetNum = Constants.currentCommnetNum + 1 ;
                                    like_num.setText(Constants.currentCommnetNum+"");
//                                    draweeView.setImageResource(R.mipmap.agree_click);
                                    mComList.set(position,new CommentsInfo(mComList.get(position).getCmtid(),Constants.currentCommnetNum,mComList.get(position).getCmtid()+""));
                                    Toast.makeText(ItemMostpopularCommentsActivity.this,"点赞成功",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                LogUtils.showVerbose("ItemMostpopularCommentsActivity","当前信息解析错误");
                            }
                        }
                    }, new MyHttpUtils.CallbackError() {
                        @Override
                        public void getResponseMsg(String error) {

                        }
                    });
                }
            }
        });
        recycleViewAdapter.setOnAuthorMessageClickListener(new ItemMostPopularRecycleViewAdapter.OnAuthorMessageClickListener() {
            @Override
            public void authorClick() {
                LogUtils.showVerbose("ItemMostpopularCommentsActivity","11111");
            }
        });
        recycleViewAdapter.setOnResponseMessageClickListener(new ItemMostPopularRecycleViewAdapter.OnResponseMessageClickListener() {
            @Override
            public void responseClick(CommentsInfo commentInfo) {
                LogUtils.showVerbose("ItemMostpopularCommentsActivity","2");
                CacheDataUtils.addCurrentNeedMsg();
                ActivityColection.addActivity(ItemMostpopularCommentsActivity.this);
                Intent intent = new Intent(ItemMostpopularCommentsActivity.this, CommentResponseDetailActivity.class);
                intent.putExtra("indirectReply","indirectReply");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putString("avatar", commentInfo.getAvatar());
                bundle.putString("cmtusername", commentInfo.getUsername());
                bundle.putString("comment", commentInfo.getComment());
                bundle.putString("cmtfavour", commentInfo.getCmtfavour());
                bundle.putString("createdat", commentInfo.getCreatedat());
                bundle.putInt("cmtid", commentInfo.getCmtid());
                bundle.putInt("cmtlikenum", commentInfo.getLikednum());
                bundle.putInt("rpnum", commentInfo.getRpnum());
                bundle.putInt("cmtuserid", commentInfo.getUserid());
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
    private void jsonParse(String string) {
        try {
            JSONObject object = new JSONObject(string);
            current_message.clear();
            JSONArray array = object.getJSONArray("datas");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                CommentsInfo commentsInfo = new CommentsInfo(jsonObject.getInt("cmtid"),
                        jsonObject.getString("comment"),
                        jsonObject.getString("createdat"),
                        jsonObject.getInt("rpnum"),
                        jsonObject.getInt("userid"),
                        jsonObject.getString("username"),
                        jsonObject.getString("avatar"),
                        jsonObject.getString("cmtfavour"),
                        jsonObject.getInt("likednum"));
                LogUtils.showVerbose("MostPopularFragment", "cmtid=" + jsonObject.getInt("cmtid") + " comment=" + jsonObject.getString("comment") + " createdat=" +
                        jsonObject.getString("createdat") + " username=" + jsonObject.getString("username") + " avatar=" + jsonObject.getString("avatar"));
                all_commnetss.add(commentsInfo);
                mComList.add(new CommentsInfo(jsonObject.getInt("cmtid"),jsonObject.getInt("likednum"),jsonObject.getString("cmtfavour")));
                current_message.add(jsonObject.getInt("cmtid"));
                mSmallConfid = JsonParseUtils.getSmallConfid(current_message);
            }
            if (num==1){
//                initId();
                mHandler.sendEmptyMessage(1);
            }else {
                mItem_mostpopular_comment_recycleview.getAdapter().notifyItemRangeInserted(num*10,(num+1)*10);
                mItem_mostpopular_comment_recycleview.getAdapter().notifyItemRangeChanged(num*10,(num+1)*10);
            }
        } catch (JSONException e) {
            LogUtils.showVerbose("MostPopularFragment", "第一级数组解析错误");
        }
    }
    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
//            if (ActivityColection.isContains(this)){
//                ActivityColection.removeActivity(this);
//                jumpToItemMostpopularActivity();
//            }
            DealReturnLogicUtils.dealReturnLogic(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_mostpopular_comments_back:
//                jumpToItemMostpopularActivity();
                DealReturnLogicUtils.dealReturnLogic(this);
                break;
            case R.id.item_mostpopular_comment_edit_input:
                SystemUtils.showOrHide(this);
                break;
            case R.id.item_mostpopular_comment_publish_ok:
                if (TextUtils.isEmpty(mItem_mostpopular_comment_publish_input.getText())){
                    ToastUtil.showShort(ItemMostpopularCommentsActivity.this,"发布信息不能为空");
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MyHttpUtils.postJsonData("http://1zou.me/apisq/addCmt", "cmtInfo", getPublishCommentMsg(mItem_mostpopular_comment_publish_input.getText().toString()),
                                    new MyHttpUtils.Callback() {
                                        @Override
                                        public void getResponseMsg(String response) {
                                            if ("0".equals(response)){
                                                mHandler.sendEmptyMessage(COMMENT_SUCCESS);
                                                Constants.allComments = Constants.allComments + 1;
                                            }
                                        }
                                    }, new MyHttpUtils.CallbackError() {
                                        @Override
                                        public void getResponseMsg(String error) {

                                        }
                                    });
//                            if (cmtInfo==true){
//
//                            }
                        }
                    }){}.start();
                    //发布消息
                    //关闭软键盘
                    SystemUtils.showOrHide(this);
                }
                break;
            default:
                break;
        }
    }
    public void jumpToItemMostpopularActivity(){
        Intent intent = new Intent(ItemMostpopularCommentsActivity.this,ItemMostpopularActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    private String getPublishCommentMsg(String  inputMsg) {
        JSONObject jsonObject = new JSONObject();
//        {"comment":'new comment','userid':userid1,'iconfid':iconfid2}
        try {
            jsonObject.put("comment",inputMsg);
            jsonObject.put("userid",Constants.USER_ID);
            jsonObject.put("iconfid",CacheDataUtils.getCurrentNeedToSaveData().getIcnfid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                initData();
            }else if (msg.what == COMMENT_SUCCESS){
                Toast.makeText(ItemMostpopularCommentsActivity.this,"评论成功",Toast.LENGTH_SHORT).show();
                num = 1;
                all_commnetss.clear();
                mComList.clear();
                loadNetMessage();
            }
        }
    };

}
