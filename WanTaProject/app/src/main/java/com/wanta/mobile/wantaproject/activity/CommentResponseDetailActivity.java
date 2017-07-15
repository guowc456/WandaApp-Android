package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.CommentResponseDetailRecycleViewAdapter;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.customview.KeyboardListenRelativeLayout;
import com.wanta.mobile.wantaproject.domain.CacheDataUtils;
import com.wanta.mobile.wantaproject.domain.ResponseInfo;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.DealReturnLogicUtils;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.MyHttpUtils;
import com.wanta.mobile.wantaproject.utils.NetUtils;
import com.wanta.mobile.wantaproject.utils.SimpleDraweeControlUtils;
import com.wanta.mobile.wantaproject.utils.SystemUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2017/1/10.
 */
public class CommentResponseDetailActivity extends BaseActivity implements View.OnClickListener {

    private CustomSimpleDraweeView mComments_response_detail_author_icon;
    private String mAvatar;
    private String mUsername;
    private String mComment;
    private String mCmtfavour;
    private String mCreatedat;
    private int mCmtid;
    private int mLikenum;
    private int mRpnum;
    private int mUserid;
    private TextView mComments_response_detail_author;
    private TextView mComments_response_detail_replay_content;
    private TextView mComments_response_detail_date;
    private TextView mComments_response_detail_like_num;
    private CustomSimpleDraweeView mComments_response_detail_response_back;
    private RecyclerView mComments_response_detail_recycleview;
    private List<ResponseInfo> all_message = new ArrayList<>();
    private List<ResponseInfo> current_message = new ArrayList<>();
    private int num = 0;
    private LinearLayout mResponse_detail_response_edit_layout;
    private EditText mResponse_detail_response_edit_input;
    private KeyboardListenRelativeLayout mResponse_detail_keyboardListener;
    private LinearLayout mResponse_detail_response_publish_layout;
    private TextView mResponse_detail_response_publish_ok;
    private EditText mResponse_detail_response_publish_input;
    private int currentUserId = 0;
    private int currentCommentId = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_response_detail);
//        ActivityColection.addActivity(this);
        Bundle extras = getIntent().getExtras();
        mAvatar = extras.getString("avatar");
        mUsername = extras.getString("cmtusername");
        mComment = extras.getString("comment");
        mCmtfavour = extras.getString("cmtfavour");
        mCreatedat = extras.getString("createdat");
        mCmtid = extras.getInt("cmtid");
        mLikenum = extras.getInt("cmtlikenum");
        mRpnum = extras.getInt("rpnum");
        mUserid = extras.getInt("cmtuserid");

        initId();
        LoadResonseMesg();
    }

    private void initId() {
        mComments_response_detail_author_icon = (CustomSimpleDraweeView) this.findViewById(R.id.comments_response_detail_author_icon);
        mComments_response_detail_author_icon.setWidth(Constants.PHONE_WIDTH/14);
        mComments_response_detail_author_icon.setHeight(Constants.PHONE_WIDTH/14);
        SimpleDraweeControlUtils.getNetRingSimpleDraweeControl(this,mComments_response_detail_author_icon,Constants.FIRST_PAGE_IMAGE_URL+mAvatar);
        mComments_response_detail_author = (TextView) this.findViewById(R.id.comments_response_detail_author);
        mComments_response_detail_author.setText(mUsername);
        mComments_response_detail_replay_content = (TextView) this.findViewById(R.id.comments_response_detail_replay_content);
        mComments_response_detail_replay_content.setText(mComment);
        mComments_response_detail_date = (TextView) this.findViewById(R.id.comments_response_detail_date);
        mComments_response_detail_date.setText(mCreatedat);
        mComments_response_detail_response_back = (CustomSimpleDraweeView) this.findViewById(R.id.comments_response_detail_response_back);
        mComments_response_detail_response_back.setHeight(Constants.PHONE_WIDTH/12);
        mComments_response_detail_response_back.setWidth(Constants.PHONE_WIDTH/12);
        mComments_response_detail_response_back.setImageResource(R.mipmap.pre_arrows);
        mComments_response_detail_response_back.setOnClickListener(this);
        mResponse_detail_response_edit_layout = (LinearLayout) this.findViewById(R.id.response_detail_response_edit_layout);
        mResponse_detail_response_edit_layout.setOnClickListener(this);
        mResponse_detail_response_edit_input = (EditText) this.findViewById(R.id.response_detail_response_edit_input);
        mResponse_detail_response_edit_input.setOnClickListener(this);
        mResponse_detail_response_publish_layout = (LinearLayout) this.findViewById(R.id.response_detail_response_publish_layout);
        mResponse_detail_response_publish_ok = (TextView) this.findViewById(R.id.response_detail_response_publish_ok);
        mResponse_detail_response_publish_ok.setOnClickListener(this);
        mResponse_detail_response_publish_input = (EditText) this.findViewById(R.id.response_detail_response_publish_input);
        mResponse_detail_keyboardListener = (KeyboardListenRelativeLayout) this.findViewById(R.id.response_detail_keyboardListener);
        mResponse_detail_keyboardListener.setOnKeyboardStateChangedListener(new KeyboardListenRelativeLayout.IOnKeyboardStateChangedListener() {
            @Override
            public void onKeyboardStateChanged(int state) {
                switch (state) {
                    case KeyboardListenRelativeLayout.KEYBOARD_STATE_HIDE://软键盘隐藏
                        mResponse_detail_response_edit_layout.setVisibility(View.VISIBLE);
                        mResponse_detail_response_publish_layout.setVisibility(View.GONE);
                        break;
                    case KeyboardListenRelativeLayout.KEYBOARD_STATE_SHOW://软键盘显示
                        mResponse_detail_response_edit_layout.setVisibility(View.GONE);
                        mResponse_detail_response_publish_layout.setVisibility(View.VISIBLE);
                        mResponse_detail_response_publish_input.requestFocus();
                        break;
                    default:
                        break;
                }
            }
        });
        mComments_response_detail_recycleview = (RecyclerView) this.findViewById(R.id.comments_response_detail_recycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mComments_response_detail_recycleview.setLayoutManager(linearLayoutManager);
    }

    private void LoadResonseMesg() {
        if (NetUtils.checkNet(this)==true){
            current_message.clear();
            all_message.clear();
            MyHttpUtils.getNetMessage(this, "http://1zou.me/apisq/loadRps/"+mCmtid+"/10/0", new MyHttpUtils.Callback() {
                @Override
                public void getResponseMsg(String response) {
                    LogUtils.showVerbose("CommentResponseDetailActivity","response="+response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int errorCode = jsonObject.getInt("errorCode");
                        if (errorCode==0){
                            jsonParse(jsonObject.getJSONArray("datas").toString());
                        }
                    } catch (JSONException e) {
                        LogUtils.showVerbose("CommentResponseDetailActivity","解析数据错误");
                    }
                }
            }, new MyHttpUtils.CallbackError() {
                @Override
                public void getResponseMsg(String error) {

                }
            });
        }else {
            NetUtils.showNoNetDialog(this);
        }

    }
    //解析获取到的信息
    private void jsonParse(String response) {
        current_message.clear();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i =0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int rpid = jsonObject.getInt("rpid");
                String rpcontent = jsonObject.getString("rpcontent");
                int rp_userid = jsonObject.getInt("rp_userid");
                int be_rp_userid = jsonObject.getInt("be_rp_userid");
                String createdat = jsonObject.getString("createdat");
                JSONObject rp_userinfo = jsonObject.getJSONObject("rp_userinfo");
                String rp_username = rp_userinfo.getString("username");
                String rp_avatar = rp_userinfo.getString("avatar");
                JSONObject be_rp_userinfo = jsonObject.getJSONObject("be_rp_userinfo");
                String be_rp_username = be_rp_userinfo.getString("username");
                String be_rp_avatar = be_rp_userinfo.getString("avatar");
                ResponseInfo responseInfo = new ResponseInfo(rpid,rpcontent,rp_userid,be_rp_userid,
                        createdat,rp_username,rp_avatar,be_rp_username,be_rp_avatar);
                all_message.add(responseInfo);
                current_message.add(responseInfo);
            }
            //显示当前数据
            showAdapterMsg();
        } catch (JSONException e) {
            LogUtils.showVerbose("CommentResponseDetailActivity","数组解析错误");
        }
    }

    private void showAdapterMsg() {
        CommentResponseDetailRecycleViewAdapter adapter = new CommentResponseDetailRecycleViewAdapter(this);
        adapter.setShowNum(all_message);
        mComments_response_detail_recycleview.setAdapter(adapter);
        if(num!=0){
            adapter.notifyItemRangeInserted(num*10,(num+1)*10);
            adapter.notifyItemRangeChanged(num*10,(num+1)*10);
        }
        mComments_response_detail_recycleview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isSlideToBottom(recyclerView)) {
                    if (NetUtils.checkNet(CommentResponseDetailActivity.this)==true){
                        num = num + 1;
                        MyHttpUtils.getNetMessage(CommentResponseDetailActivity.this, "http://1zou.me/apisq/loadRps/"+mCmtid+"/10/"+(num*10), new MyHttpUtils.Callback() {
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

                    }else {
                        NetUtils.showNoNetDialog(CommentResponseDetailActivity.this);
                    }

                } else {
//                    LogUtils.showVerbose("MostPopularFragment", "当前的number=" + currentFid);
                }
            }
        });
        adapter.setOnAuthorMessageClickListener(new CommentResponseDetailRecycleViewAdapter.OnAuthorMessageClickListener() {
            @Override
            public void authorClick() {

            }
        });
        adapter.setOnResponseMessageClickListener(new CommentResponseDetailRecycleViewAdapter.OnResponseMessageClickListener() {
            @Override
            public void responseClick(ResponseInfo responseInfo) {
                currentUserId = responseInfo.getBe_rp_userid();
                currentCommentId = responseInfo.getRpid();
                //添加回复的信息
                SystemUtils.showOrHide(CommentResponseDetailActivity.this);
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comments_response_detail_response_back:
//                jumpToComments();
                DealReturnLogicUtils.dealReturnLogic(this);
                break;
            case R.id.response_detail_response_edit_input:
                currentUserId = mUserid;
                currentCommentId = mCmtid;
                SystemUtils.showOrHide(CommentResponseDetailActivity.this);
                break;
            case R.id.response_detail_response_publish_ok:
                //添加回复信息
                addResponseMsg();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
//            if (ActivityColection.isContains(this)){
//                ActivityColection.removeActivity(this);
//                jumpToComments();
//            }
            DealReturnLogicUtils.dealReturnLogic(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void jumpToComments(){
//        Intent intent = new Intent();
//        if ("indirectReply".equals(getIntent().getStringExtra("indirectReply"))){
//            //是从评论界面跳回到回复界面的
//            intent.setClass(CommentResponseDetailActivity.this,ItemMostpopularCommentsActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        }else if ("directReply".equals(getIntent().getStringExtra("directReply"))){
//            //是从二级界面跳转到回复界面
//            intent.setClass(CommentResponseDetailActivity.this,ItemMostpopularActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        }
//
//        startActivity(intent);
//        finish();
        DealReturnLogicUtils.dealReturnLogic(this);
    }
    private void addResponseMsg() {
        if (NetUtils.checkNet(this)==true){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyHttpUtils.postJsonData("http://1zou.me/apisq/addRp", "rpInfo", getPublishMsg(), new MyHttpUtils.Callback() {
                        @Override
                        public void getResponseMsg(String response) {
                            if ("0".equals(response)){
                                LoadResonseMesg();
                            }else {
                                LogUtils.showVerbose("CommentResponseDetailActivity","response="+response);
                            }
                        }
                    }, new MyHttpUtils.CallbackError() {
                        @Override
                        public void getResponseMsg(String error) {

                        }
                    });
                }
            }).start();
            SystemUtils.showOrHide(this);
        }else {
            NetUtils.showNoNetDialog(this);
        }

    }

    public String getPublishMsg(){
//        { "rpInfo":{"rpcontent":'new rp content','userid':userid1,
//                'rp_userid':rpuserid,'commentid':commentid }  }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rpcontent",mResponse_detail_response_publish_input.getText().toString());
            jsonObject.put("userid",Constants.USER_ID);
            jsonObject.put("rp_userid",currentUserId);
            jsonObject.put("commentid",currentCommentId);
            LogUtils.showVerbose("CommentResponseDetailActivity", "rpcontent:"+mResponse_detail_response_publish_input.getText().toString()+" userid="+Constants.USER_ID+" mUserid:"+currentUserId+" mCmtid:"+currentCommentId);
        } catch (JSONException e) {
            LogUtils.showVerbose("CommentResponseDetailActivity", "数据添加失败");
        }
        return jsonObject.toString();
    }
}
