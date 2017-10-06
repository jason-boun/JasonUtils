package com.jason.jasonutils.window.poptiputil;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jason.jasonutils.JasonUtilsMainActivity;
import com.jason.jasonutils.R;

@SuppressLint("ViewConstructor")
public class ScreenDisplaySmsView extends LinearLayout implements View.OnClickListener {

	public static final int PREVIOUS_PAGE = 1;
	public static final int NEXT_PAGE = 2;
	public static final int FLING_MIN_DISTANCE = 200, FLING_MIN_VELOCITY = 200;
	
	private Context mContext;
	private LayoutInflater mLayout;
	private Handler mHandler;

	private ImageView mMsgPhoto;
	private TextView mMsgName;
	private TextView mMsgDate;
	private TextView mMsgBodyTv;
	
	private ImageView mMsgClose;
	private Button mMsgReplyButton;
	private Button mMsgReadButton;
	
	private Bitmap defaultPhotoBitmap;

	public ScreenDisplaySmsView(Context context, Handler handle) {
		super(context);
		mContext = context;
		mHandler = handle;
		mLayout = LayoutInflater.from(mContext);
		View view = mLayout.inflate(R.layout.layout_screendisplay_sms, null);
		defaultPhotoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_bg);
		initResourceRefs(view);
	}

	private void initResourceRefs(View view) {
		mMsgPhoto = (ImageView) view.findViewById(R.id.iv_photo);
		mMsgName = (TextView) view.findViewById(R.id.tv_name);
		mMsgDate = (TextView) view.findViewById(R.id.tv_date);
		
		mMsgBodyTv = (TextView) view.findViewById(R.id.msg_popu_body);
		mMsgBodyTv.setMovementMethod(ScrollingMovementMethod.getInstance());
		mMsgBodyTv.setLongClickable(true);
		
		mMsgClose = (ImageView) view.findViewById(R.id.msg_popu_close);
		mMsgReadButton = (Button) view.findViewById(R.id.msg_popu_read);
		mMsgReplyButton = (Button) view.findViewById(R.id.msg_popu_reply);
		
		mMsgClose.setOnClickListener(this);
		mMsgReadButton.setOnClickListener(this);
		mMsgReplyButton.setOnClickListener(this);

		addView(view, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	@SuppressLint("SimpleDateFormat")
	public void addMsg(String msgBody,String fromName) {
		mMsgName.setText(fromName);
		mMsgPhoto.setImageBitmap(defaultPhotoBitmap);
		mMsgDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
		mMsgBodyTv.setText(msgBody);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.msg_popu_close:
			mHandler.sendEmptyMessage(SMSPreviewUtils.MSG_POPU_CLOSE);
			break;
		case R.id.msg_popu_read:
			mHandler.sendEmptyMessage(SMSPreviewUtils.MSG_POPU_CLOSE);
			break;
		case R.id.msg_popu_reply:
			mHandler.sendEmptyMessage(SMSPreviewUtils.MSG_POPU_CLOSE);
			replyParams();
			break;
		}
	}
	
	/**
	 * 点击回复跳转设置
	 */
	public void replyParams(){
		Intent chatIntent = new Intent(mContext, JasonUtilsMainActivity.class);
		chatIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		chatIntent.putExtra("windows", "1");
		mContext.startActivity(chatIntent);
	}

}
