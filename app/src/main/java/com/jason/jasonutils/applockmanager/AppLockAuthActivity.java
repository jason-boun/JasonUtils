package com.jason.jasonutils.applockmanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.MD5Utils;

public class AppLockAuthActivity extends Activity {
	
	private EditText et_applock_auth_pwd;
	private Button bt_applock_auth_ok;
	private String packageName = "";
	
	private String enterPwd = "";
	private SharedPreferences sp;
	private Activity act;
	
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applock_auth);
		
		act = AppLockAuthActivity.this;
		
		et_applock_auth_pwd = (EditText) findViewById(R.id.et_applock_auth_pwd);
		et_applock_auth_pwd.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);
		bt_applock_auth_ok = (Button) findViewById(R.id.bt_applock_auth_ok);
		
		packageName = getIntent().getStringExtra(AppLockService.PACKAGE_NAME);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		
		bt_applock_auth_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				enterPwd = et_applock_auth_pwd.getText().toString().trim();
				if(TextUtils.isEmpty(enterPwd)){
					Toast.makeText(act,"密码不能为空",Toast.LENGTH_SHORT).show();
					return;
				}else if(MD5Utils.encode(enterPwd).equals(sp.getString(AppLockManagerActivity.HAS_SET_APPLOCKPWD, ""))){
					sendBroadcast(new Intent().setAction(AppLockService.ACTION_RELEASE_APPLOCK).putExtra(AppLockService.PACKAGE_NAME,packageName));
					act.finish();
				}else{
					Toast.makeText(act,"密码不正确",Toast.LENGTH_SHORT).show();
					et_applock_auth_pwd.startAnimation(AnimationUtils.loadAnimation(act, R.anim.shake));
					et_applock_auth_pwd.getText().clear();
					return;
				}
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
