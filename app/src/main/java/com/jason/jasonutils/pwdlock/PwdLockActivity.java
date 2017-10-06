package com.jason.jasonutils.pwdlock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;

import com.jason.jasonutils.tools.EncryptionTool;
import com.jason.jasonutils.tools.MLog;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.JasonUtilsMainActivity;
import com.jason.jasonutils.MyApplication;
import com.jason.jasonutils.R;

public class PwdLockActivity extends Activity implements OnClickListener {

	protected EditText pinCodeField1 = null;
	protected EditText pinCodeField2 = null;
	protected EditText pinCodeField3 = null;
	protected EditText pinCodeField4 = null;
	protected EditText pinCodeField5=null;
	protected EditText pinCodeField6=null;
	protected InputFilter[] filters = null;
	protected TextView topMessage = null;
	protected TextView errorMessage=null;
	protected int errorCount = 1 ;
	private long tempLockTime = 0L;
	private int requestCode = 0;
	private String newPwdRecord = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.app_passcode_keyboard);
		
		init();
	}

	private void init() {
		topMessage = (TextView) findViewById(R.id.top_message);
		errorMessage=(TextView) findViewById(R.id.error_message);
		errorMessage.setVisibility(View.INVISIBLE);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String message = extras.getString("message");
			if (message != null) {
				topMessage.setText(message);
			}
		}

		filters = new InputFilter[2];
		filters[0] = new InputFilter.LengthFilter(1);
		filters[1] = onlyNumber;

		// Setup the pin fields row
		pinCodeField1 = (EditText) findViewById(R.id.pincode_1);
		setupPinItem(pinCodeField1);

		pinCodeField2 = (EditText) findViewById(R.id.pincode_2);
		setupPinItem(pinCodeField2);

		pinCodeField3 = (EditText) findViewById(R.id.pincode_3);
		setupPinItem(pinCodeField3);

		pinCodeField4 = (EditText) findViewById(R.id.pincode_4);
		setupPinItem(pinCodeField4);
		
		pinCodeField5=(EditText) findViewById(R.id.pincode_5);
		setupPinItem(pinCodeField5);
		
		pinCodeField6=(EditText) findViewById(R.id.pincode_6);
		setupPinItem(pinCodeField6);
		

		// setup the keyboard
		((ImageButton) findViewById(R.id.button0))
				.setOnClickListener(defaultButtonListener);
		((ImageButton) findViewById(R.id.button1))
				.setOnClickListener(defaultButtonListener);
		((ImageButton) findViewById(R.id.button2))
				.setOnClickListener(defaultButtonListener);
		((ImageButton) findViewById(R.id.button3))
				.setOnClickListener(defaultButtonListener);
		((ImageButton) findViewById(R.id.button4))
				.setOnClickListener(defaultButtonListener);
		((ImageButton) findViewById(R.id.button5))
				.setOnClickListener(defaultButtonListener);
		((ImageButton) findViewById(R.id.button6))
				.setOnClickListener(defaultButtonListener);
		((ImageButton) findViewById(R.id.button7))
				.setOnClickListener(defaultButtonListener);
		((ImageButton) findViewById(R.id.button8))
				.setOnClickListener(defaultButtonListener);
		((ImageButton) findViewById(R.id.button9))
				.setOnClickListener(defaultButtonListener);
		((ImageButton) findViewById(R.id.button_erase))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						checkEditTextDel();
						if (pinCodeField1.isFocused()) {
						} else if (pinCodeField2.isFocused()) {
							pinCodeField1.requestFocus();
							pinCodeField1.setText("");
						} else if (pinCodeField3.isFocused()) {
							pinCodeField2.requestFocus();
							pinCodeField2.setText("");
						} else if (pinCodeField4.isFocused()) {
							pinCodeField3.requestFocus();
							pinCodeField3.setText("");
						}else if(pinCodeField5.isFocused()) {
							pinCodeField4.requestFocus();
							pinCodeField4.setText("");
						}else if(pinCodeField6.isFocused()) {
							if(TextUtils.isEmpty(pinCodeField6.getText())) {
								pinCodeField5.requestFocus();
								pinCodeField5.setText("");
							}else{
								pinCodeField6.requestFocus();
								pinCodeField6.setText("");
							}
						}
					}
				});
		requestCode = getIntent().getIntExtra(Constant.APP_LOCK_OPERATE_FLAG, 0);
		if(requestCode == Constant.TURN_ON_APPLOCK){
			setTips("请设置密码，建议勿与登录密码相同");
			MLog.i("哈哈", "前来设置新的密码");
		}else if(requestCode == Constant.TURN_OFF_APPLOCK){
			setTips("请输入密码");
			MLog.i("哈哈", "前来关闭密码");
		}
	}
	
	/**
	 * 检测输入是否仅为数字，因此密码只支持数字
	 */
	private InputFilter onlyNumber = new InputFilter() {
		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {

			if (source.length() > 1)
				return "";

			if (source.length() == 0) // erase
				return null;

			try {
				int number = Integer.parseInt(source.toString());
				if ((number >= 0) && (number <= 9))
					return String.valueOf(number);
				else
					return "";
			} catch (NumberFormatException e) {
				return "";
			}
		}
	};
	/**
	 * 当选中那个EditText时，清空里面的数据
	 */
	private OnTouchListener otl = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v instanceof EditText) {
//				((EditText) v).setText("");
			}
			return false;
		}
	};
	
	/**
	 * 为每个文本框定义自定义属性
	 * @param item 当前文本框
	 */
	protected void setupPinItem(EditText item) {
		item.setInputType(InputType.TYPE_NULL);
		item.setFilters(filters);
		item.setOnTouchListener(otl);
		item.setTextColor(Color.BLACK);
		item.setTransformationMethod(new AsteriskPasswordTransformationMethod());
	}

	public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
		@Override  
		public CharSequence getTransformation(CharSequence source, View view) {  
		    return new PasswordCharSequence(source);  
		}  
		private class PasswordCharSequence implements CharSequence {  
		    private CharSequence mSource;  
		    public PasswordCharSequence(CharSequence source) {  
		        mSource = source;
		    }  
		    public char charAt(int index) {  
		        return '●';
		    }  
		    public int length() {  
		        return mSource.length();
		    }  
		    public CharSequence subSequence(int start, int end) {  
		        return mSource.subSequence(start, end);
		    }  
		}
	}
	
	/**
	 * 检测当点击Del键时，从后面有数据的开始删除
	 */
	private void checkEditTextDel(){
		
		if(!TextUtils.isEmpty(pinCodeField5.getText()))
		{
			pinCodeField6.requestFocus();
		}else if(!TextUtils.isEmpty(pinCodeField4.getText()))
		{
			pinCodeField5.requestFocus();
		}else if(!TextUtils.isEmpty(pinCodeField3.getText()))
		{
			pinCodeField4.requestFocus();
		}else if(!TextUtils.isEmpty(pinCodeField2.getText()))
		{
			pinCodeField3.requestFocus();
		}else if(!TextUtils.isEmpty(pinCodeField1.getText()))
		{
			pinCodeField2.requestFocus();
		}
	}
	
	
	/**
	 * 默认多个按钮调用的点击事件
	 */
	private OnClickListener defaultButtonListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			int currentValue = -1;
			int id = arg0.getId();
			if (id == R.id.button0) {
				currentValue = 0;
			} else if (id == R.id.button1) {
				currentValue = 1;
			} else if (id == R.id.button2) {
				currentValue = 2;
			} else if (id == R.id.button3) {
				currentValue = 3;
			} else if (id == R.id.button4) {
				currentValue = 4;
			} else if (id == R.id.button5) {
				currentValue = 5;
			} else if (id == R.id.button6) {
				currentValue = 6;
			} else if (id == R.id.button7) {
				currentValue = 7;
			} else if (id == R.id.button8) {
				currentValue = 8;
			} else if (id == R.id.button9) {
				currentValue = 9;
			} else {
			}

			// set the value and move the focus
			String currentValueString = String.valueOf(currentValue);
			checkEditTextIsNull();
			if (pinCodeField1.isFocused()) {
				pinCodeField1.setText(currentValueString);
				pinCodeField2.requestFocus();
				pinCodeField2.setText("");
			} else if (pinCodeField2.isFocused()) {
				pinCodeField2.setText(currentValueString);
				pinCodeField3.requestFocus();
				pinCodeField3.setText("");
			} else if (pinCodeField3.isFocused()) {
				pinCodeField3.setText(currentValueString);
				pinCodeField4.requestFocus();
				pinCodeField4.setText("");
			} else if (pinCodeField4.isFocused()) {
				pinCodeField4.setText(currentValueString);
				pinCodeField5.requestFocus();
				pinCodeField5.setText("");
			}else if(pinCodeField5.isFocused())
			{
				pinCodeField5.setText(currentValueString);
				pinCodeField6.requestFocus();
				pinCodeField6.setText("");
			}else if(pinCodeField6.isFocused())
			{
				pinCodeField6.setText(currentValueString);
			}

			if (pinCodeField6.getText().toString().length()>0
					&&pinCodeField5.getText().toString().length()>0
					&&pinCodeField4.getText().toString().length() > 0
					&& pinCodeField3.getText().toString().length() > 0
					&& pinCodeField2.getText().toString().length() > 0
					&& pinCodeField1.getText().toString().length() > 0) {
				onPinLockInserted();
			}
		}
	};
	
	/**
	 * 检测前几项是否为空，如果空先填写前面的项
	 */
	public void checkEditTextIsNull(){
		if(TextUtils.isEmpty(pinCodeField1.getText()))
		{
			pinCodeField1.requestFocus();
			pinCodeField2.setText("");
			pinCodeField3.setText("");
			pinCodeField4.setText("");
			pinCodeField5.setText("");
			pinCodeField6.setText("");
			return;
		}else if(TextUtils.isEmpty(pinCodeField2.getText()))
		{
			pinCodeField2.requestFocus();
			pinCodeField3.setText("");
			pinCodeField4.setText("");
			pinCodeField5.setText("");
			pinCodeField6.setText("");
			return;
		}else if(TextUtils.isEmpty(pinCodeField3.getText()))
		{
			pinCodeField3.requestFocus();
			pinCodeField4.setText("");
			pinCodeField5.setText("");
			pinCodeField6.setText("");
			return;
		}else if(TextUtils.isEmpty(pinCodeField4.getText()))
		{
			pinCodeField4.requestFocus();
			pinCodeField5.setText("");
			pinCodeField6.setText("");
			return;
		}else if(TextUtils.isEmpty(pinCodeField5.getText()))
		{
			pinCodeField5.requestFocus();
			pinCodeField6.setText("");
			return;
		}else if(TextUtils.isEmpty(pinCodeField6.getText()))
		{
			pinCodeField6.requestFocus();
		}
	}
	
	/**
	 * 当6位密码输入结束后，调用该方法
	 */
	protected void onPinLockInserted(){
		if(isLock(this)){
			showPasswordError(Constant.MODE_LOCK);//错误处理
			return;
		}
		String passLock = pinCodeField1.getText().toString() 
				+ pinCodeField2.getText().toString() 
				+ pinCodeField3.getText().toString() 
				+ pinCodeField4.getText().toString()
				+ pinCodeField5.getText().toString()
				+ pinCodeField6.getText().toString();
		if(requestCode == Constant.TURN_ON_APPLOCK){
			if(newPwdRecord.length()==0){
				newPwdRecord = passLock;
				setTips("请再次输入密码");
				cleanEditText();
				return;
			}else{
				if(!newPwdRecord.equals(passLock)){
					setTips("两次密码不一致，请重新输入");
					newPwdRecord = "";
					cleanEditText();
				}else{
					getSharedPreferences(Constant.MYSP_CONFIG_FILENAME, MODE_PRIVATE).edit().putString(Constant.APP_LOCK_KEY,EncryptionTool.encryptString(passLock)).commit();
					newPwdRecord = "";
					operateAppLock(true);
					//返回关闭或者开启程序锁的结果状态码
					finish();
				}
				return;
			}
		}
		String sp_lock_pwd = getSharedPreferences(Constant.MYSP_CONFIG_FILENAME, MODE_PRIVATE).getString(Constant.APP_LOCK_KEY, "");
		if(requestCode == Constant.TURN_OFF_APPLOCK){
			if(!passLock.equals(EncryptionTool.decryptString(sp_lock_pwd))){
				setTips("密码错误，请重新输入");
				cleanEditText();
			}else{
				operateAppLock(false);//返回关闭或者开启程序锁的结果状态码
				finish();
			}
			return;
		}
    	if(passLock.equals(EncryptionTool.decryptString(sp_lock_pwd))){
    		if(!getIntent().getBooleanExtra(Constant.NEED_BACK, false)){
    			startActivity(new Intent(this, JasonUtilsMainActivity.class));
    		}
    		cleanLockSPparams(this);
    		finish();
    	}else{
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
                    showPasswordError(Constant.MODE_VERIFY);//错误处理
				}
			});
    	}
	}
	
	/**
	 * 检测密码是否输入错误，如错误调用该方法
	 * @param Mode 输入错误模式
	 */
	protected void showPasswordError(int mode) {
		if(mode == Constant.MODE_VERIFY){
			cleanEditText();
			int first = getSharedPreferences(Constant.MYSP_CONFIG_FILENAME, MODE_PRIVATE).getInt(Constant.ERROR_PWD_COUNT, 1);
			errorCount = first==0?1:first;
			if(errorCount < Constant.LIMITED_ERROR_TIME){
				findViewById(R.id.AppUnlockLinearLayout1).startAnimation(AnimationUtils.loadAnimation(PwdLockActivity.this, R.anim.shake));
				errorMessage.setVisibility(View.VISIBLE);
				errorMessage.setText("您还有"+(Constant.LIMITED_ERROR_TIME - errorCount)+"次输入机会。输错"+Constant.LIMITED_ERROR_TIME+"次将锁定"+Constant.LIMITED_LOCK_TIME/(1000*60)+"分钟");
				errorCount++;
				getSharedPreferences(Constant.MYSP_CONFIG_FILENAME, MODE_PRIVATE).edit().putInt(Constant.ERROR_PWD_COUNT, errorCount).commit();
			}else if(errorCount == Constant.LIMITED_ERROR_TIME){
				getSharedPreferences(Constant.MYSP_CONFIG_FILENAME, MODE_PRIVATE).edit().putLong(Constant.LOCK_PWD_TIME, System.currentTimeMillis()).commit();
				errorMsgTip();
			}
		}else if(mode==Constant.MODE_LOCK){
			errorMsgTip();
		}
	}
	
	/**
	 * 锁定之后的提示
	 * @param time
	 */
	public void errorMsgTip(){
		tempLockTime = getSharedPreferences(Constant.MYSP_CONFIG_FILENAME, MODE_PRIVATE).getLong(Constant.LOCK_PWD_TIME, 0L);
		setTips("您的软件已被锁定，请在"+(Constant.LIMITED_LOCK_TIME-((System.currentTimeMillis()-tempLockTime)))/(1000*60)+"分钟后再试。");
		Toast.makeText(this, "您的软件已被锁定", Toast.LENGTH_SHORT).show();
		cleanEditText();
	}
	
	public void setTips(String tips){
		errorMessage.setVisibility(View.VISIBLE);
		errorMessage.setText(tips);
	}
	/**
	 * 清空密码输入区域
	 */
	public void cleanEditText(){
		pinCodeField1.setText("");
		pinCodeField2.setText("");
		pinCodeField3.setText("");
		pinCodeField4.setText("");
		pinCodeField5.setText("");
		pinCodeField6.setText("");
		pinCodeField1.requestFocus();
	}
	
	/**
	 * 如果输入成功，则擦除错误记录
	 */
	public static void cleanLockSPparams(Context context){
		context.getSharedPreferences(Constant.MYSP_CONFIG_FILENAME, MODE_PRIVATE).edit().putLong(Constant.LOCK_PWD_TIME, 0L).commit();
		context.getSharedPreferences(Constant.MYSP_CONFIG_FILENAME, MODE_PRIVATE).edit().putInt(Constant.ERROR_PWD_COUNT, 0).commit();
	}
	
	/**
	 * 是否已被锁定
	 * @param context
	 * @return
	 */
	public static boolean isLock(Context context){
		long period = System.currentTimeMillis() - context.getSharedPreferences(Constant.MYSP_CONFIG_FILENAME, MODE_PRIVATE).getLong(Constant.LOCK_PWD_TIME, 0L);
		return period>0 && period<Constant.LIMITED_LOCK_TIME;
	}
	
	/**
	 * 在密码验证界面，按返回键回到home界面
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
			Intent home = new Intent(Intent.ACTION_MAIN);
			home.addCategory(Intent.CATEGORY_HOME);
			startActivity(home);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onClick(View v) {
		
	}
	
	/**
	 * 通过程序锁管理界面来操作程序锁
	 */
	public void operateAppLock(boolean turnOn){
		getSharedPreferences(Constant.MYSP_CONFIG_FILENAME, MODE_PRIVATE).edit().putBoolean(Constant.APP_LOCK_SKIP_SET, false).commit();
		if(turnOn){
			getSharedPreferences(Constant.MYSP_CONFIG_FILENAME, MODE_PRIVATE).edit().putBoolean(Constant.APP_LOCK_TURN_ON, true).commit();
			((MyApplication)getApplication()).startVerify();
			Toast.makeText(this, "密码设置成功", Toast.LENGTH_SHORT).show();
		}else{
			getSharedPreferences(Constant.MYSP_CONFIG_FILENAME, MODE_PRIVATE).edit().putBoolean(Constant.APP_LOCK_TURN_ON, false).commit();
			((MyApplication)getApplication()).stopVerify();
		}
	}
}
