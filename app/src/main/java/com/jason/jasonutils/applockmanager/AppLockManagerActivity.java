package com.jason.jasonutils.applockmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jason.jasonutils.R;
import com.jason.jasonutils.customview.MyCustomSettingView;
import com.jason.jasonutils.tools.MD5Utils;
import com.jason.jasonutils.tools.Utils;

public class AppLockManagerActivity extends Activity {
	
	private SharedPreferences sp;
	private Button bt_set_lock_pwd, bt_app_lock_manager;
	private MyCustomSettingView cv_applock_service_switch;
	public static final String HAS_SET_APPLOCKPWD= "has_set_applockpwd";
	private Activity act;
	private AlertDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_lock_manager);
		act = AppLockManagerActivity.this;
		sp = getSharedPreferences("config", MODE_APPEND);
		
		bt_set_lock_pwd = (Button) findViewById(R.id.bt_set_lock_pwd);
		bt_app_lock_manager = (Button) findViewById(R.id.bt_app_lock_manager);
		cv_applock_service_switch = (MyCustomSettingView) findViewById(R.id.cv_applock_service_switch);
		cv_applock_service_switch.setChecked(Utils.isServiceRunning(act, AppLockService.class));
		cv_applock_service_switch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cv_applock_service_switch.isChecked()){
					operateService(false);
				}else{
					if(TextUtils.isEmpty(sp.getString(HAS_SET_APPLOCKPWD, ""))){
						Toast.makeText(act, "请先设置程序锁密码", 0).show();
						return;
					}
					operateService(true);
				}
			}
		});
		updateUI();
	}

	/**
	 * 通过是否设置密码来控制密码锁入口
	 * @param view
	 */
	public void setPwd(View view){
		if(TextUtils.isEmpty(sp.getString(HAS_SET_APPLOCKPWD, ""))){
			showSetupPwdDialog();
		}else{
			showNormalDialog();
		}
	}
	
	public void manager(View view){
		startActivity(new Intent(act, AppLockListActivity.class));
	}

	/**
	 * 显示设置密码对话框
	 */
	protected void showSetupPwdDialog() {
		AlertDialog.Builder builder = new Builder(act);
		builder.setTitle("请设置密码");
		
		View view = View.inflate(act, R.layout.app_lock_dialog_setup_pwd, null);
		final EditText et_password = (EditText) view.findViewById(R.id.et_password);
		final EditText et_password_confirm = (EditText) view.findViewById(R.id.et_password_confirm);
		Button bt_ok = (Button) view.findViewById(R.id.bt_ok);
		Button bt_cancle = (Button) view.findViewById(R.id.bt_cancle);
		
		bt_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String password = et_password.getText().toString().trim();
				String password_confirm = et_password_confirm.getText().toString().trim();
				if (TextUtils.isEmpty(password) || TextUtils.isEmpty(password_confirm)) {
					Toast.makeText(getApplicationContext(), "密码不能为空", 1).show();
					return;
				}
				if (password.equals(password_confirm)) {
					sp.edit().putString(HAS_SET_APPLOCKPWD, MD5Utils.encode(password_confirm)).commit();
					operateService(true);
					dialog.dismiss();
					updateUI();
				} else {
					Toast.makeText(getApplicationContext(), "两次密码不一致", 1).show();
					return;
				}
			}
		});
		bt_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		builder.setView(view);
		dialog = builder.show();
	}

	/**
	 * 显示挂关闭密码对话框
	 */
	protected void showNormalDialog() {
		AlertDialog.Builder builder = new Builder(act);
		builder.setTitle("请输入旧密码");
		
		View view = View.inflate(act, R.layout.app_lock_dialog_normal_password, null);
		final EditText et_password = (EditText) view.findViewById(R.id.et_password);
		Button bt_ok = (Button) view.findViewById(R.id.bt_ok);
		Button bt_cancle = (Button) view.findViewById(R.id.bt_cancle);
		
		bt_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		bt_ok.setOnClickListener(new  View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String password = et_password.getText().toString().trim();
				if(TextUtils.isEmpty(password)){
					Toast.makeText(getApplicationContext(), "密码不能为空", 1).show();
					return;
				}
				if(sp.getString(HAS_SET_APPLOCKPWD, "").equals(MD5Utils.encode(password))){
					sp.edit().putString(HAS_SET_APPLOCKPWD, "").commit();
					operateService(false);
					dialog.dismiss();
					updateUI();
				}else{
					Toast.makeText(getApplicationContext(), "密码错误", 1).show();
					return;
				}
			}
		});
		builder.setView(view);
		dialog  =  builder.show();
	}
	
	public void updateUI(){
		if(TextUtils.isEmpty(sp.getString(HAS_SET_APPLOCKPWD, ""))){
			bt_set_lock_pwd.setText("设置程序锁密码");
			bt_app_lock_manager.setEnabled(false);
		}else{
			bt_set_lock_pwd.setText("关闭程序锁密码");
			bt_app_lock_manager.setEnabled(true);
		}
	}
	
	public void operateService(boolean open){
		if(open){
			cv_applock_service_switch.setChecked(true);
			startService(new Intent(act, AppLockService.class));
		}else{
			cv_applock_service_switch.setChecked(false);
			stopService(new Intent(act, AppLockService.class));
		}
	}

}
