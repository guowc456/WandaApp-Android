package com.wanta.mobile.wantaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.adapter.PersonDesignProfessionRecycleViewAdapter;
import com.wanta.mobile.wantaproject.customview.MyImageView;
import com.wanta.mobile.wantaproject.domain.PersonMessageSerializable;
import com.wanta.mobile.wantaproject.domain.ProfessionInfo;
import com.wanta.mobile.wantaproject.utils.ActivityColection;
import com.wanta.mobile.wantaproject.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/12/16.
 */
public class PersonDesignProfessionActivity extends BaseActivity implements View.OnClickListener {

    private MyImageView mPerson_design_profession_back;
    private RecyclerView mPerson_design_profession_recycleview;
    private String[] mProfession_item1;
    private String chooseProfession = "";
    private List<ProfessionInfo> mProfessionInfoList = new ArrayList<>();
    private TextView mPerson_design_profession_ok;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_design_profession);
        ActivityColection.addActivity(this);
        initId();
    }

    private void initId() {
        //获取不同层次的职业数据
        mProfession_item1 = getResources().getStringArray(R.array.profession_item1);
        mPerson_design_profession_back = (MyImageView) this.findViewById(R.id.person_design_profession_back);
        mPerson_design_profession_back.setSize(Constants.PHONE_WIDTH/17,Constants.PHONE_WIDTH/17);
        mPerson_design_profession_back.setOnClickListener(this);
        mPerson_design_profession_ok = (TextView) this.findViewById(R.id.person_design_profession_ok);
        mPerson_design_profession_ok.setOnClickListener(this);
        mPerson_design_profession_recycleview = (RecyclerView) this.findViewById(R.id.person_design_profession_recycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mPerson_design_profession_recycleview.setLayoutManager(linearLayoutManager);
        PersonDesignProfessionRecycleViewAdapter adapter = new PersonDesignProfessionRecycleViewAdapter(this,mProfession_item1);
        mPerson_design_profession_recycleview.setAdapter(adapter);
        adapter.setOnPersonDesignItemListener(new PersonDesignProfessionRecycleViewAdapter.OnPersonDesignProfessionItemListener() {
            @Override
            public void setItemClick(View view) {
                LinearLayout item_person_design_layout = (LinearLayout) view.findViewById(R.id.item_person_design_layout);
                TextView textView = (TextView) view.findViewById(R.id.item_profession);
                int position = mPerson_design_profession_recycleview.getChildAdapterPosition(view);
                if (mProfessionInfoList.size()==0){
                    ProfessionInfo professionInfo = new ProfessionInfo();
                    professionInfo.setLinearLayout(item_person_design_layout);
                    professionInfo.setPosition(position);
                    professionInfo.setTextView(textView);
                    mProfessionInfoList.add(professionInfo);
                    item_person_design_layout.setBackgroundColor(getResources().getColor(R.color.profession_bg_color));
                    textView.setTextColor(getResources().getColor(R.color.white));
                }else {
                    ProfessionInfo professionInfo = mProfessionInfoList.get(0);
                    int position1 = professionInfo.getPosition();
                    if (position!=position1){
                        professionInfo.getLinearLayout().setBackgroundColor(getResources().getColor(R.color.white));
                        professionInfo.getTextView().setTextColor(getResources().getColor(R.color.text_color));
                        mProfessionInfoList.clear();
                        ProfessionInfo professionInfo1 = new ProfessionInfo();
                        professionInfo1.setLinearLayout(item_person_design_layout);
                        professionInfo1.setPosition(position);
                        professionInfo1.setTextView(textView);
                        mProfessionInfoList.add(professionInfo1);
                        item_person_design_layout.setBackgroundColor(getResources().getColor(R.color.profession_bg_color));
                        textView.setTextColor(getResources().getColor(R.color.white));
                    }
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (ActivityColection.isContains(this)){
                ActivityColection.removeActivity(this);
                jumpPersonDesignActivity();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.person_design_profession_back:
                jumpPersonDesignActivity();
                break;
            case R.id.person_design_profession_ok:
                if (mProfessionInfoList.size()==0){
                    Toast.makeText(PersonDesignProfessionActivity.this,"亲，您还没有选择职业呢？",Toast.LENGTH_SHORT).show();
                }else {
                    Constants.currentProfession = mProfessionInfoList.get(0).getTextView().getText().toString();
                    jumpPersonDesignActivity();
                }
                break;
            default:
                break;
        }
    }
    //跳转到个人信息设置界面
    public void jumpPersonDesignActivity(){
        Intent intent = new Intent(PersonDesignProfessionActivity.this,PersonalDesignActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("person_design_profession",new PersonMessageSerializable(Constants.currentProfession));
        setResult(RESULT_OK,intent);
        finish();

    }
}
