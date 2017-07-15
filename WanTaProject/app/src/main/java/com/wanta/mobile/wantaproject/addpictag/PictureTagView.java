package com.wanta.mobile.wantaproject.addpictag;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.wanta.mobile.wantaproject.R;

public class PictureTagView extends RelativeLayout implements OnEditorActionListener{

	private Context context;
	private TextView tvPictureTagLabel;
	private EditText etPictureTagLabel;
	private View loTag;
	public enum Status{Normal,Edit}
	public enum Direction{Left,Right}
	private Direction direction = Direction.Left;
	private InputMethodManager imm;
	private static final int ViewWidth = 80;
	private static final int ViewHeight = 50;
	private String pinpai;
	private String jiage;
	private String gouyu;
	private String miaosu;

	public PictureTagView(Context context, Direction direction,String pinpai_name,String jiage_content,String gouyu_content,String miaosu_content) {
		super(context);
		this.context = context;
		this.direction = direction;
		this.pinpai = pinpai_name;
		this.jiage = jiage_content;
		this.gouyu = gouyu_content;
		this.miaosu = miaosu_content;
		initViews();
		init();
		initEvents();
	}

	/** 初始化视图 **/
	protected void initViews(){
		LayoutInflater.from(context).inflate(R.layout.picturetagview, this,true);
		tvPictureTagLabel = (TextView) findViewById(R.id.tvPictureTagLabel);
		etPictureTagLabel = (EditText) findViewById(R.id.etPictureTagLabel);
		TextView pinpai_name = (TextView) this.findViewById(R.id.pinpai_name);
		TextView jiage_content = (TextView) this.findViewById(R.id.jiage_content);
		TextView gouyu_content = (TextView) this.findViewById(R.id.gouyu_content);
		TextView miaosu_content = (TextView) this.findViewById(R.id.miaosu_content);
		pinpai_name.setText(pinpai);
		jiage_content.setText(jiage);
		gouyu_content.setText(gouyu);
		miaosu_content.setText(miaosu);
		loTag = findViewById(R.id.loTag);
	}
	/** 初始化 **/
	protected void init(){
		imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		directionChange();
	}

	/** 初始化事件 **/
	protected void initEvents(){
		etPictureTagLabel.setOnEditorActionListener(this);
	}
	
	public void setStatus(Status status){
		switch(status){
		case Normal:
			tvPictureTagLabel.setVisibility(View.VISIBLE);
			etPictureTagLabel.clearFocus();
			tvPictureTagLabel.setText(etPictureTagLabel.getText());
			etPictureTagLabel.setVisibility(View.GONE);
			//隐藏键盘
			imm.hideSoftInputFromWindow(etPictureTagLabel.getWindowToken() , 0); 
			break;
		case Edit:
			tvPictureTagLabel.setVisibility(View.GONE);
			etPictureTagLabel.setVisibility(View.VISIBLE);
			etPictureTagLabel.requestFocus();
			//弹出键盘
			imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
			break;
		}
	}
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		setStatus(Status.Normal);
		return true;
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		View parent = (View) getParent();
		int halfParentW = (int) (parent.getWidth()*0.5);
		int center = (int) (l + (this.getWidth()*0.5));
		if(center<=halfParentW){
			direction = Direction.Left;
		}
		else{
			direction = Direction.Right;
		}
		directionChange();
	}
	private void directionChange(){
		switch(direction){
		case Left:
//			loTag.setBackgroundResource(R.mipmap.bg_picturetagview_tagview_left);
			break;
		case Right:
//			loTag.setBackgroundResource(R.mipmap.bg_picturetagview_tagview_right);
			break;
		}
	}
	public static int getViewWidth(){
		return ViewWidth;
	}
	public static int getViewHeight(){
		return ViewHeight;
	}
}
