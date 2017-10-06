package com.jason.jasonutils.pwdlock;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jason.jasonutils.JasonUtilsMainActivity;
import com.jason.jasonutils.MyApplication;
import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.EncryptionTool;

public class FirstSetPwdLockActivity extends Activity {
	
	private EditText et_setlockpwd_pwd, et_setlockpwd_re_pwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_set_lockpwd);
		
		init();
	}

	private void init() {
		et_setlockpwd_pwd = (EditText) findViewById(R.id.et_setlockpwd_pwd);
		et_setlockpwd_re_pwd = (EditText) findViewById(R.id.et_setlockpwd_re_pwd);
		
		et_setlockpwd_pwd.setTextColor(Color.BLACK);
		et_setlockpwd_re_pwd.setTextColor(Color.BLACK);
	}

	/**
	 * 确认点击事件
	 * @param view
	 */
	public void confirm(View view){
		String pwd = et_setlockpwd_pwd.getText().toString().trim();
		String repwd = et_setlockpwd_re_pwd.getText().toString().trim();
		if(TextUtils.isEmpty(pwd) || TextUtils.isEmpty(repwd)){
			Toast.makeText(this, "密码输入不能为空", Toast.LENGTH_SHORT).show();
			et_setlockpwd_pwd.setText("");
			et_setlockpwd_re_pwd.setText("");
			et_setlockpwd_pwd.requestFocus();
			return;
		}else if(pwd.length()<6 || repwd.length()<6){
			Toast.makeText(this, "请输入6位数字密码", Toast.LENGTH_SHORT).show();
			et_setlockpwd_pwd.setText("");
			et_setlockpwd_re_pwd.setText("");
			et_setlockpwd_pwd.requestFocus();
			//et_setlockpwd_pwd.setError(error);
			return;
		} else if(!pwd.equals(repwd)){
			Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
			et_setlockpwd_re_pwd.setText("");
			et_setlockpwd_re_pwd.requestFocus();
			return;
		}
		getSharedPreferences(Constant.MYSP_CONFIG_FILENAME, MODE_PRIVATE).edit().putString(Constant.APP_LOCK_KEY, EncryptionTool.encryptString(pwd)).commit();
		getSharedPreferences(Constant.MYSP_CONFIG_FILENAME, MODE_PRIVATE).edit().putBoolean(Constant.APP_LOCK_TURN_ON, true).commit();
		((MyApplication)getApplication()).startVerify();
		startActivity(new Intent(this, JasonUtilsMainActivity.class));
		finish();
	}
	
	/**
	 * 跳过设置密码步骤
	 * @param view
	 */
	public void skipSetPwd(View view){
		getSharedPreferences(Constant.MYSP_CONFIG_FILENAME, MODE_PRIVATE).edit().putBoolean(Constant.APP_LOCK_SKIP_SET, true).commit();
		((MyApplication)getApplication()).stopVerify();
		startActivity(new Intent(getApplicationContext(), JasonUtilsMainActivity.class));
		finish();
	}
}
