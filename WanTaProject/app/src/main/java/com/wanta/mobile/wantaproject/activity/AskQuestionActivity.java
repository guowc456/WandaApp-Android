package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.AskQuestionRecycleViewAdapter;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.utils.AdapterUtils;
import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;
import com.wanta.mobile.wantaproject.utils.SaveMsgUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by WangYongqiang on 2016/12/8.
 */
public class AskQuestionActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mAskquestion_head_shoulder;
    private String[] mHead_should;
    private RecyclerView mAskquestion_trunk_limbs;
    private String[] mTrunk_limbs;
    private RecyclerView mAskquestion_whole_body;
    private String[] mWhole_body;
    private RecyclerView mAskquestion_beautiful_show;
    private String[] mBeautiful_show;
    private RecyclerView mAskquestion_body_height;
    private String[] mBody_height;
    private RecyclerView mAskquestion_body_weight;
    private String[] mBody_weight;
    private RecyclerView mAskquestion_facial_skin;
    private String[] mFacial_skin;
    private RecyclerView mAskquestion_look_otherstyle;
    private String[] mLook_otherstyle;
    private RecyclerView mAskquestion_commend_mystyle;
    private String[] mCommend_mystyle;
    private AskQuestionRecycleViewAdapter mHead_shoulder_adapter;
    private AskQuestionRecycleViewAdapter mTrunk_limbs_adapter;
    private AskQuestionRecycleViewAdapter mWhole_body_adapter;
    private AskQuestionRecycleViewAdapter mBeautiful_show_adapter;
    private AskQuestionRecycleViewAdapter mBody_height_adapter;
    private AskQuestionRecycleViewAdapter mBody_weight_adapter;
    private AskQuestionRecycleViewAdapter mFacial_skin_adapter;
    private AskQuestionRecycleViewAdapter mLook_otherstyle_adapter;
    private AskQuestionRecycleViewAdapter mCommend_mystyle_adapter;
    private TextView mAskquestion_btn_restart;
    private TextView mAskquestion_btn_ok;
    private TextView mAskquestion_btn_skip;
    private List<String> disvantages = new ArrayList<>();//记录当前的缺点
    private List<String> advantages = new ArrayList<>();//记录当前的优点
    private List<String> situation = new ArrayList<>();//记录当前的优点
    private List<String> trystyle = new ArrayList<>();//记录当前的优点
    private HashMap<Integer,View> height_map = new HashMap<>();
    private HashMap<Integer,View> weight_map = new HashMap<>();
    private HashMap<Integer,View> face_type_map = new HashMap<>();
    private HashMap<Integer,View> face_color_map = new HashMap<>();
    private List<String> disvantages_array = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_askquestion);
        initId();
        initData();
    }

    private void initId() {
        mHead_should = getResources().getStringArray(R.array.ask_question_head_should);//头，肩膀
        mAskquestion_head_shoulder = (RecyclerView) this.findViewById(R.id.askquestion_head_shoulder);
        GridLayoutManager head_shoulder_gridlayout = new GridLayoutManager(this, 4);
        head_shoulder_gridlayout.setOrientation(GridLayoutManager.VERTICAL);
        mAskquestion_head_shoulder.setLayoutManager(head_shoulder_gridlayout);
        mHead_shoulder_adapter = new AskQuestionRecycleViewAdapter(this, mHead_should, 1);
        mAskquestion_head_shoulder.setAdapter(mHead_shoulder_adapter);

        //躯干及四肢
        mAskquestion_trunk_limbs = (RecyclerView) this.findViewById(R.id.askquestion_trunk_limbs);
        mTrunk_limbs = getResources().getStringArray(R.array.ask_question_trunk_limbs);
        GridLayoutManager trunk_limbs_gridlayout = new GridLayoutManager(this, 4);
        trunk_limbs_gridlayout.setOrientation(GridLayoutManager.VERTICAL);
        mAskquestion_trunk_limbs.setLayoutManager(trunk_limbs_gridlayout);
        mTrunk_limbs_adapter = new AskQuestionRecycleViewAdapter(this, mTrunk_limbs, 1);
        mAskquestion_trunk_limbs.setAdapter(mTrunk_limbs_adapter);

        //整体全身
        mAskquestion_whole_body = (RecyclerView) this.findViewById(R.id.askquestion_whole_body);
        mWhole_body = getResources().getStringArray(R.array.ask_question_whole_body);
        GridLayoutManager whole_body_gridlayout = new GridLayoutManager(this, 4);
        whole_body_gridlayout.setOrientation(GridLayoutManager.VERTICAL);
        mAskquestion_whole_body.setLayoutManager(whole_body_gridlayout);
        mWhole_body_adapter = new AskQuestionRecycleViewAdapter(this, mWhole_body, 1);
        mAskquestion_whole_body.setAdapter(mWhole_body_adapter);
        //优点展现
        mAskquestion_beautiful_show = (RecyclerView) this.findViewById(R.id.askquestion_beautiful_show);
        mBeautiful_show = getResources().getStringArray(R.array.ask_question_beautiful_show);
        GridLayoutManager beautiful_show_gridlayout = new GridLayoutManager(this, 3);
        beautiful_show_gridlayout.setOrientation(GridLayoutManager.VERTICAL);
        mAskquestion_beautiful_show.setLayoutManager(beautiful_show_gridlayout);
        mBeautiful_show_adapter = new AskQuestionRecycleViewAdapter(this, mBeautiful_show, 2);
        mAskquestion_beautiful_show.setAdapter(mBeautiful_show_adapter);
        //身高
        mAskquestion_body_height = (RecyclerView) this.findViewById(R.id.askquestion_body_height);
        mBody_height = getResources().getStringArray(R.array.ask_question_body_height);
        GridLayoutManager body_height_gridlayout = new GridLayoutManager(this, 3);
        body_height_gridlayout.setOrientation(GridLayoutManager.VERTICAL);
        mAskquestion_body_height.setLayoutManager(body_height_gridlayout);
        mBody_height_adapter = new AskQuestionRecycleViewAdapter(this, mBody_height, 2);
        mAskquestion_body_height.setAdapter(mBody_height_adapter);
        //体重
        mAskquestion_body_weight = (RecyclerView) this.findViewById(R.id.askquestion_body_weight);
        mBody_weight = getResources().getStringArray(R.array.ask_question_body_weight);
        GridLayoutManager body_weight_gridlayout = new GridLayoutManager(this, 3);
        body_weight_gridlayout.setOrientation(GridLayoutManager.VERTICAL);
        mAskquestion_body_weight.setLayoutManager(body_weight_gridlayout);
        mBody_weight_adapter = new AskQuestionRecycleViewAdapter(this, mBody_weight, 2);
        mAskquestion_body_weight.setAdapter(mBody_weight_adapter);
        //脸型肤质
        mAskquestion_facial_skin = (RecyclerView) this.findViewById(R.id.askquestion_facial_skin);
        mFacial_skin = getResources().getStringArray(R.array.ask_question_facial_skin);
        GridLayoutManager facial_skin_gridlayout = new GridLayoutManager(this, 2);
        facial_skin_gridlayout.setOrientation(GridLayoutManager.VERTICAL);
        mAskquestion_facial_skin.setLayoutManager(facial_skin_gridlayout);
        mFacial_skin_adapter = new AskQuestionRecycleViewAdapter(this, mFacial_skin, 3);
        mAskquestion_facial_skin.setAdapter(mFacial_skin_adapter);
        //看其他人的风格
        mAskquestion_look_otherstyle = (RecyclerView) this.findViewById(R.id.askquestion_look_otherstyle);
        mLook_otherstyle = getResources().getStringArray(R.array.ask_question_look_otherstyle);
        GridLayoutManager look_otherstyle_gridlayout = new GridLayoutManager(this, 3);
        look_otherstyle_gridlayout.setOrientation(GridLayoutManager.VERTICAL);
        mAskquestion_look_otherstyle.setLayoutManager(look_otherstyle_gridlayout);
        mLook_otherstyle_adapter = new AskQuestionRecycleViewAdapter(this, mLook_otherstyle, 1);
        mAskquestion_look_otherstyle.setAdapter(mLook_otherstyle_adapter);
        //比人评价我的风格
        mAskquestion_commend_mystyle = (RecyclerView) this.findViewById(R.id.askquestion_commend_mystyle);
        mCommend_mystyle = getResources().getStringArray(R.array.ask_question_commend_mystyle);
        GridLayoutManager commend_mystyle_gridlayout = new GridLayoutManager(this, 3);
        commend_mystyle_gridlayout.setOrientation(GridLayoutManager.VERTICAL);
        mAskquestion_commend_mystyle.setLayoutManager(commend_mystyle_gridlayout);
        mCommend_mystyle_adapter = new AskQuestionRecycleViewAdapter(this, mCommend_mystyle, 4);
        mAskquestion_commend_mystyle.setAdapter(mCommend_mystyle_adapter);

        mAskquestion_btn_restart = (TextView) this.findViewById(R.id.askquestion_btn_restart);
        mAskquestion_btn_restart.setOnClickListener(this);
        mAskquestion_btn_ok = (TextView) this.findViewById(R.id.askquestion_btn_ok);
        mAskquestion_btn_ok.setOnClickListener(this);
        mAskquestion_btn_skip = (TextView) this.findViewById(R.id.askquestion_btn_skip);
        mAskquestion_btn_skip.setOnClickListener(this);

    }

    private void initData() {
        mHead_shoulder_adapter.setAskQuestionRecycleViewClickListener(new AskQuestionRecycleViewAdapter.AskQuestionRecycleViewClickListener() {
            @Override
            public void setOnItemClick(View view) {
                int position = mAskquestion_head_shoulder.getChildAdapterPosition(view);
                TextView item_askquestion_content = (TextView) view.findViewById(R.id.item_askquestion_content);
                Constants.askquestion_msg[0] = addDisvantages(disvantages,mHead_should[position],item_askquestion_content).toString();
                LogUtils.showVerbose("AskQuestionActivity", "disvantages=" +Constants.askquestion_msg[0].toString());
            }
        });
        mTrunk_limbs_adapter.setAskQuestionRecycleViewClickListener(new AskQuestionRecycleViewAdapter.AskQuestionRecycleViewClickListener() {
            @Override
            public void setOnItemClick(View view) {
                int position = mAskquestion_trunk_limbs.getChildAdapterPosition(view);
                TextView item_askquestion_content = (TextView) view.findViewById(R.id.item_askquestion_content);
                Constants.askquestion_msg[0]=addDisvantages(disvantages,mTrunk_limbs[position], item_askquestion_content).toString();
                LogUtils.showVerbose("AskQuestionActivity", "disvantages=" +Constants.askquestion_msg[0].toString());
            }
        });
        mWhole_body_adapter.setAskQuestionRecycleViewClickListener(new AskQuestionRecycleViewAdapter.AskQuestionRecycleViewClickListener() {
            @Override
            public void setOnItemClick(View view) {
                int position = mAskquestion_whole_body.getChildAdapterPosition(view);
                TextView item_askquestion_content = (TextView) view.findViewById(R.id.item_askquestion_content);
                Constants.askquestion_msg[0]= addDisvantages(disvantages,mWhole_body[position], item_askquestion_content).toString();
                LogUtils.showVerbose("AskQuestionActivity", "disvantages=" +Constants.askquestion_msg[0].toString());
            }
        });
        mBeautiful_show_adapter.setAskQuestionRecycleViewClickListener(new AskQuestionRecycleViewAdapter.AskQuestionRecycleViewClickListener() {
            @Override
            public void setOnItemClick(View view) {
                int position = mAskquestion_beautiful_show.getChildAdapterPosition(view);
                TextView item_askquestion_content = (TextView) view.findViewById(R.id.item_askquestion_content);
                MyImageView item_askquestion_image_face = (MyImageView) view.findViewById(R.id.item_askquestion_image_face);
                Constants.askquestion_msg[1]=addAdvantages(mBeautiful_show[position],item_askquestion_content,item_askquestion_image_face).toString();
                LogUtils.showVerbose("AskQuestionActivity", "优势是=" +Constants.askquestion_msg[1].toString());
            }
        });
        mBody_height_adapter.setAskQuestionRecycleViewClickListener(new AskQuestionRecycleViewAdapter.AskQuestionRecycleViewClickListener() {
            @Override
            public void setOnItemClick(View view) {
                int position = mAskquestion_body_height.getChildAdapterPosition(view);
                addHeightAndWeight(position,view,height_map,1);
                if (height_map.size()!=0){
                    Integer next = height_map.keySet().iterator().next();
                    List<String> list = new ArrayList<String>();
                    list.add(mBody_height[next]);
                    Constants.askquestion_msg[2] = mBody_height[next];
                    LogUtils.showVerbose("AskQuestionActivity", "身高是=" +Constants.askquestion_msg[2].toString());
                }else {
                    Constants.askquestion_msg[2] = "";
                    LogUtils.showVerbose("AskQuestionActivity", "身高是=" +Constants.askquestion_msg[2].toString());
                }
            }
        });
        mBody_weight_adapter.setAskQuestionRecycleViewClickListener(new AskQuestionRecycleViewAdapter.AskQuestionRecycleViewClickListener() {
            @Override
            public void setOnItemClick(View view) {
                int position = mAskquestion_body_weight.getChildAdapterPosition(view);
                addHeightAndWeight(position,view,weight_map,1);
                if (weight_map.size()!=0){
                    Integer next = weight_map.keySet().iterator().next();
                    Constants.askquestion_msg[3] = mBody_weight[next].toString();
                    LogUtils.showVerbose("AskQuestionActivity", "体重是=" +Constants.askquestion_msg[3].toString());
                }else {
                    Constants.askquestion_msg[3] = "";
                    LogUtils.showVerbose("AskQuestionActivity", "体重是=" +Constants.askquestion_msg[3].toString());
                }
            }
        });
        mFacial_skin_adapter.setAskQuestionRecycleViewClickListener(new AskQuestionRecycleViewAdapter.AskQuestionRecycleViewClickListener() {
            @Override
            public void setOnItemClick(View view) {
//                popwindow_select_icon
                int position = mAskquestion_facial_skin.getChildAdapterPosition(view);
                if (position%2==0){
                    //当前是脸的颜色
                    addHeightAndWeight(position,view,face_color_map,2);
                    if (face_color_map.size()!=0){
                        Integer next = face_color_map.keySet().iterator().next();
                        Constants.askquestion_msg[5] = mFacial_skin[next].toString();
                        LogUtils.showVerbose("AskQuestionActivity", "脸的颜色=" +Constants.askquestion_msg[5].toString());
                    }else {
                        Constants.askquestion_msg[5] = "";
                        LogUtils.showVerbose("AskQuestionActivity", "脸的颜色=" +Constants.askquestion_msg[5].toString());
                    }
                }else {
                    //当前是脸的类型
                    addHeightAndWeight(position,view,face_type_map,2);
                    if (face_type_map.size()!=0){
                        Integer next = face_type_map.keySet().iterator().next();
                        Constants.askquestion_msg[4] = mFacial_skin[next].toString();
                        LogUtils.showVerbose("AskQuestionActivity", "脸的类型=" +Constants.askquestion_msg[4].toString());
                    }else {
                        Constants.askquestion_msg[4] = "";
                        LogUtils.showVerbose("AskQuestionActivity", "脸的类型=" +Constants.askquestion_msg[4].toString());
                    }
                }

            }
        });
        mLook_otherstyle_adapter.setAskQuestionRecycleViewClickListener(new AskQuestionRecycleViewAdapter.AskQuestionRecycleViewClickListener() {
            @Override
            public void setOnItemClick(View view) {
                int position = mAskquestion_look_otherstyle.getChildAdapterPosition(view);
                TextView item_askquestion_content = (TextView) view.findViewById(R.id.item_askquestion_content);
                Constants.askquestion_msg[6] = addDisvantages(situation,mLook_otherstyle[position], item_askquestion_content).toString();
                LogUtils.showVerbose("AskQuestionActivity", "其他人穿的=" +Constants.askquestion_msg[6].toString());
            }
        });
        mCommend_mystyle_adapter.setAskQuestionRecycleViewClickListener(new AskQuestionRecycleViewAdapter.AskQuestionRecycleViewClickListener() {
            @Override
            public void setOnItemClick(View view) {
                int position = mAskquestion_commend_mystyle.getChildAdapterPosition(view);
                TextView item_askquestion_content = (TextView) view.findViewById(R.id.item_askquestion_content);
                Constants.askquestion_msg[7] = addDisvantages(trystyle,mCommend_mystyle[position], item_askquestion_content).toString();
                LogUtils.showVerbose("AskQuestionActivity", "我的风格=" +Constants.askquestion_msg[7].toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.askquestion_btn_restart:
                LogUtils.showVerbose("AskQuestionActivity", "重新来");
                mAskquestion_head_shoulder.setAdapter(mHead_shoulder_adapter);
                mHead_shoulder_adapter.notifyDataSetChanged();
                mAskquestion_trunk_limbs.setAdapter(mTrunk_limbs_adapter);
                mTrunk_limbs_adapter.notifyDataSetChanged();
                mAskquestion_whole_body.setAdapter(mWhole_body_adapter);
                mWhole_body_adapter.notifyDataSetChanged();
                mAskquestion_beautiful_show.setAdapter(mBeautiful_show_adapter);
                mBeautiful_show_adapter.notifyDataSetChanged();
                mAskquestion_body_height.setAdapter(mBody_height_adapter);
                mBody_height_adapter.notifyDataSetChanged();
                mAskquestion_body_weight.setAdapter(mBody_weight_adapter);
                mBody_weight_adapter.notifyDataSetChanged();
                mAskquestion_facial_skin.setAdapter(mFacial_skin_adapter);
                mFacial_skin_adapter.notifyDataSetChanged();
                mAskquestion_look_otherstyle.setAdapter(mLook_otherstyle_adapter);
                mLook_otherstyle_adapter.notifyDataSetChanged();
                mAskquestion_commend_mystyle.setAdapter(mCommend_mystyle_adapter);
                mCommend_mystyle_adapter.notifyDataSetChanged();
                disvantages.clear();
                advantages.clear();
                situation.clear();
                trystyle.clear();
                height_map.clear();
                weight_map.clear();
                face_color_map.clear();
                face_type_map.clear();
                for (int i=0;i<Constants.askquestion_msg.length;i++){
                    Constants.askquestion_msg[i] = "";
                }
                break;
            case R.id.askquestion_btn_ok:
                LogUtils.showVerbose("AskQuestionActivity", "选好了");
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ASK_REQUEST_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                LogUtils.showVerbose("AskQuestionActivity","返回信息:"+response.toString());
//                                Intent intent = new Intent(AskQuestionActivity.this,MainActivity.class);
//                                startActivity(intent);
//                                finish();
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LogUtils.showVerbose("MainActivity", "error.getMessage()=" + error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {

                        //在这里设置需要post的参数
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("datas", getWelcomePageMsg(getArray1(getDeviced()),
                                toArray(Constants.askquestion_msg[0]),
                                toArray(Constants.askquestion_msg[1]),
                                getArray2(Constants.askquestion_msg[2],Constants.askquestion_msg[3]),
                                getArray(Constants.askquestion_msg[4]),
                                getArray(Constants.askquestion_msg[5]),
                                toArray(Constants.askquestion_msg[6]),
                                toArray(Constants.askquestion_msg[7])));
                        isEmptyOrNor();
                        return map;
                    }
                };
                requestQueue.add(stringRequest);
//                Constants.isAskQuestion = true;
                SaveMsgUtils.setCurrentBooleanState(this,"questionState","saveState",true);
                Intent intent1 = new Intent(AskQuestionActivity.this,MainActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.askquestion_btn_skip:
//                Constants.isAskQuestion = true;
                SaveMsgUtils.setCurrentBooleanState(this,"questionState","saveState",true);
                LogUtils.showVerbose("AskQuestionActivity", "跳过");
                Intent intent = new Intent(AskQuestionActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    private void isEmptyOrNor() {
        if (!TextUtils.isEmpty(Constants.askquestion_msg[0])){
            Constants.askquestion_msg[0] = "";
        }else if (!TextUtils.isEmpty(Constants.askquestion_msg[1])){
            Constants.askquestion_msg[1] = "";
        }else if (!TextUtils.isEmpty(Constants.askquestion_msg[2])){
            Constants.askquestion_msg[2] = "";
        }else if (!TextUtils.isEmpty(Constants.askquestion_msg[3])){
            Constants.askquestion_msg[3] = "";
        }else if (!TextUtils.isEmpty(Constants.askquestion_msg[4])){
            Constants.askquestion_msg[4] = "";
        }else if (!TextUtils.isEmpty(Constants.askquestion_msg[5])){
            Constants.askquestion_msg[5] = "";
        }else if (!TextUtils.isEmpty(Constants.askquestion_msg[6])){
            Constants.askquestion_msg[6] = "";
        }else if (!TextUtils.isEmpty(Constants.askquestion_msg[7])){
            Constants.askquestion_msg[7] = "";
        }
    }

    /**
     * 欢迎页界面信息的获取
     */
    public String getWelcomePageMsg(ArrayList deviceid,ArrayList disvantages,ArrayList advantages,ArrayList hweight,ArrayList facetype,ArrayList facecolor,ArrayList situation,ArrayList trystyle) {
        Map<String, ArrayList> map = new HashMap<String, ArrayList>();
        map.put("deviceid", deviceid);
        map.put("disvantages", disvantages);
        map.put("advantages", advantages);
        map.put("hweight", hweight);
        map.put("facetype", facetype);
        map.put("facecolor", facecolor);
        map.put("situation", situation);
        map.put("trystyle", trystyle);
        JSONObject object = new JSONObject(map);
        return object.toString();
    }
    public String addDisvantages(List<String> mlist, String string, TextView item_askquestion_content){
        List<String> list = mlist;
        String str = "";
        if (list.size()>0){
            if (list.contains(string)){
                list.remove(string);
                item_askquestion_content.setTextColor(Color.GRAY);
                item_askquestion_content.setSelected(false);
            }else {
                list.add(string);
                item_askquestion_content.setTextColor(Color.WHITE);
                item_askquestion_content.setSelected(true);
            }
        }else {
            list.add(string);
            item_askquestion_content.setTextColor(Color.WHITE);
            item_askquestion_content.setSelected(true);
        }
//        return list;
        return getString(list);
    }
    public String addAdvantages(String string, TextView item_askquestion_content, MyImageView item_askquestion_image_face){
        if (advantages.size()>0){
            if (advantages.contains(string)){
                advantages.remove(string);
                item_askquestion_content.setTextColor(Color.GRAY);
                item_askquestion_content.setSelected(false);
//                item_askquestion_image_face.setSize(Constants.PHONE_WIDTH/20,Constants.PHONE_WIDTH/20);
                item_askquestion_image_face.setBackground(getResources().getDrawable(R.mipmap.askquestion_no_select));
            }else {
                advantages.add(string);
                item_askquestion_content.setTextColor(Color.WHITE);
                item_askquestion_content.setSelected(true);
//                item_askquestion_image_face.setSize(Constants.PHONE_WIDTH/20,Constants.PHONE_WIDTH/20);
                item_askquestion_image_face.setBackground(getResources().getDrawable(R.mipmap.askquestion_selected));
            }
        }else {
            advantages.add(string);
            item_askquestion_content.setTextColor(Color.WHITE);
            item_askquestion_content.setSelected(true);
//            item_askquestion_image_face.setSize(Constants.PHONE_WIDTH/20,Constants.PHONE_WIDTH/20);
            item_askquestion_image_face.setBackground(getResources().getDrawable(R.mipmap.askquestion_selected));
        }
//        return advantages;
        return getString(advantages);
    }
    public void addHeightAndWeight(int position, View view,HashMap<Integer, View> map1,int flag){
        HashMap<Integer, View> map = map1;
        if (map.size()!=0){
            Iterator<Integer> iterator = map.keySet().iterator();
            Integer next = iterator.next();
            if (position==next){
                if (flag==1){
                    ((TextView)map.get(next).findViewById(R.id.item_askquestion_content)).setTextColor(Color.GRAY);
                    ((TextView)map.get(next).findViewById(R.id.item_askquestion_content)).setSelected(false);
                    ((MyImageView)map.get(next).findViewById(R.id.item_askquestion_image_face)).setBackground(getResources().getDrawable(R.mipmap.askquestion_no_select));
                }else if (flag==2){
                    ((TextView)map.get(next).findViewById(R.id.item_askquestion_content)).setTextColor(Color.GRAY);
                    ((TextView)map.get(next).findViewById(R.id.item_askquestion_content)).setSelected(false);
                    ((MyImageView)map.get(next).findViewById(R.id.item_askquestion_image_draw)).setBackground(getResources().getDrawable(R.mipmap.askquestion_no_select));
                }
                map.clear();
            }else {
                if (flag==1){
                    ((TextView)map.get(next).findViewById(R.id.item_askquestion_content)).setTextColor(Color.GRAY);
                    ((TextView)map.get(next).findViewById(R.id.item_askquestion_content)).setSelected(false);
                    ((MyImageView)map.get(next).findViewById(R.id.item_askquestion_image_face)).setBackground(getResources().getDrawable(R.mipmap.askquestion_no_select));
                    map.clear();
                    ((TextView)view.findViewById(R.id.item_askquestion_content)).setTextColor(Color.WHITE);
                    ((TextView)view.findViewById(R.id.item_askquestion_content)).setSelected(true);
                    ((MyImageView)view.findViewById(R.id.item_askquestion_image_face)).setBackground(getResources().getDrawable(R.mipmap.askquestion_selected));
                    map.put(position,view);
                }else if (flag==2){
                    ((TextView)map.get(next).findViewById(R.id.item_askquestion_content)).setTextColor(Color.GRAY);
                    ((TextView)map.get(next).findViewById(R.id.item_askquestion_content)).setSelected(false);
                    ((MyImageView)map.get(next).findViewById(R.id.item_askquestion_image_draw)).setBackground(getResources().getDrawable(R.mipmap.askquestion_no_select));
                    map.clear();
                    ((TextView)view.findViewById(R.id.item_askquestion_content)).setTextColor(Color.WHITE);
                    ((TextView)view.findViewById(R.id.item_askquestion_content)).setSelected(true);
                    ((MyImageView)view.findViewById(R.id.item_askquestion_image_draw)).setBackground(getResources().getDrawable(R.mipmap.popwindow_select_icon));
                    map.put(position,view);
                }
            }
        }else {
            if (flag==1){
                TextView item_askquestion_content = (TextView) view.findViewById(R.id.item_askquestion_content);
                MyImageView item_askquestion_image_face = (MyImageView) view.findViewById(R.id.item_askquestion_image_face);
                item_askquestion_content.setTextColor(Color.WHITE);
                item_askquestion_content.setSelected(true);
                item_askquestion_image_face.setBackground(getResources().getDrawable(R.mipmap.askquestion_selected));
                map.put(position,view);
            }else if (flag==2){
                TextView item_askquestion_content = (TextView) view.findViewById(R.id.item_askquestion_content);
                MyImageView item_askquestion_image_face = (MyImageView) view.findViewById(R.id.item_askquestion_image_draw);
                item_askquestion_content.setTextColor(Color.WHITE);
                item_askquestion_content.setSelected(true);
                item_askquestion_image_face.setBackground(getResources().getDrawable(R.mipmap.popwindow_select_icon));
                map.put(position,view);
            }
        }
    }
    //huo
    public String getDeviced(){
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public String getString(List<String> list){
        String str = "";
        for (int i=0;i<list.size();i++){
            if (i==list.size()-1){
                str = str + list.get(i);
            }else {
                str = str + list.get(i)+";";
            }
        }
        return str;
    }

    //将字符串转化为数组
    public ArrayList<String> toArray(String string){
        String[] split = string.split(";");
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i=0;i<split.length;i++){
            arrayList.add(split[i]);
        }
        return arrayList;
    }

    public ArrayList<String> getArray2(String string1,String string2){
        ArrayList<String> list = new ArrayList<>();
        list.add(string1);
        list.add(string2);
        return list;
    }
    public ArrayList getArray1(String string){
        ArrayList<String> list = new ArrayList<>();
        list.add(string);
        return list;
    }
    //将字符串转化为list数组
    public ArrayList getArray(String string){
        List<String> list_str = new ArrayList<>();
         list_str = Arrays.asList(string);//将字符串转化为数组
        ArrayList<String> list = new ArrayList<>();
        for (int i=0;i<list_str.size();i++){
            LogUtils.showVerbose("AskQuestionActivity","输出信息="+list_str.get(i));
            list.add(list_str.get(i));
        }
        return list;
    }
}
