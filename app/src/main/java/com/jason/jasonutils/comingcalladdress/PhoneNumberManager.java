package com.jason.jasonutils.comingcalladdress;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import com.jason.jasonutils.tools.MLog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.R;
import com.jason.jasonutils.customview.MyCustomSettingView;

public class PhoneNumberManager extends Activity {
	
	private MyCustomSettingView cv_show_address_manager_checkbox;
	private EditText et_query_number;
	private TextView tv_query_result;
	private Vibrator vibrator;
	private RelativeLayout rl_address_change_location;
	private RelativeLayout rl_address_change_bg;
	private TextView tv_address_color_bg;
	private static final String[] items = {"半透明","活力橙","卫士蓝","苹果绿","金属灰"};
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.number_address_manager_activity);
		init();
		dynamicQuery();
		openPhoneService();
		changeAddressViewLocation();
		chengeAddressViewColor();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		boolean serviceRunning = isServiceRunning(this, PhoneNumAddressService.class);
		cv_show_address_manager_checkbox.setChecked(serviceRunning);
	}

	private void init() {
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
	}

	/**
	 * 随着号码输入，动态查询归属地
	 */
	private void dynamicQuery() {
		tv_query_result = (TextView) findViewById(R.id.tv_query_result);
		et_query_number = (EditText) findViewById(R.id.et_query_number);
		et_query_number.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				tv_query_result.setText("归属地:"+AddressDao.getAddress(getApplicationContext(), s.toString()));
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
	
	/**
	 * 打开和关闭电话监听服务
	 */
	private void openPhoneService() {
		cv_show_address_manager_checkbox = (MyCustomSettingView) findViewById(R.id.cv_show_address_manager_checkbox);
		boolean serviceRunning = isServiceRunning(this, PhoneNumAddressService.class);
		cv_show_address_manager_checkbox.setChecked(serviceRunning);
		cv_show_address_manager_checkbox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cv_show_address_manager_checkbox.isChecked()){
					stopService(new Intent(PhoneNumberManager.this, PhoneNumAddressService.class));
					cv_show_address_manager_checkbox.setChecked(false);
				}else{
					startService(new Intent(PhoneNumberManager.this, PhoneNumAddressService.class));
					cv_show_address_manager_checkbox.setChecked(true);
				}
			}
		});
	}

	/**
	 * 调整归属地显示View的位置
	 */
	private void changeAddressViewLocation() {
		rl_address_change_location = (RelativeLayout) findViewById(R.id.rl_address_change_location);
		rl_address_change_location.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), DragAddressViewActivity.class));
			}
		});
	}

	/**
	 * 修改归属地显示的背景色
	 */
	private void chengeAddressViewColor() {
		tv_address_color_bg = (TextView) findViewById(R.id.tv_address_color_bg);
		tv_address_color_bg.setText(items[sp.getInt("which", 0)]);
		
		rl_address_change_bg = (RelativeLayout) findViewById(R.id.rl_address_change_bg);
		rl_address_change_bg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showSelectDialog();
			}
		});
	}

	/**
	 * 弹出北背景色单选对话框
	 */
	protected void showSelectDialog() {
		final Recorder recorder = new Recorder();
		AlertDialog.Builder builder = new Builder(PhoneNumberManager.this);
		builder.setTitle("更改归属地背景");
		final int preSelected = sp.getInt("which", 0);
		recorder.tempSelected = preSelected;
		builder.setSingleChoiceItems(items, preSelected, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				recorder.tempSelected = which;
			}
		});
		
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				sp.edit().putInt("which", recorder.tempSelected).commit();
				tv_address_color_bg.setText(items[recorder.tempSelected]);
			}
		});
		
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.show();
	}
	
	/**
	 * 为了解决一个内部类使用外部的变量必须用final修饰的问题，单独创造一个只有整型数值字段的类，也是醉了；
	 * 不能在内部更新值的Integer对象有什么 用，感觉这届Integer封装能力不行啊
	 */
	public class Recorder{
		int tempSelected;
	}

	/**
	 * 查询归属地数据库
	 * @param view
	 */
	public void query(View view){
		String numbber = et_query_number.getText().toString().trim();
		if(TextUtils.isEmpty(numbber)){
			
			Toast.makeText(getApplicationContext(), "查询号码不能为空", 0).show();
			et_query_number.getText().clear();
			
			Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
			et_query_number.startAnimation(animation);
			vibrator.vibrate(100);
			return;
		}
		tv_query_result.setText("归属地:"+AddressDao.getAddress(getApplicationContext(), numbber));
	}

	/**
	 * 判断是否运行
	 * @param context
	 * @param clazz
	 * @return
	 */
	public boolean isServiceRunning(Context context, Class clazz){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServicesInfos = am.getRunningServices(1000);
		for(RunningServiceInfo info : runningServicesInfos){
			if(clazz.getName().equals(info.service.getClassName())){
				MLog.i("哈哈","判断服务是否开启的方法被调用====true");
				return true;
			}
		}
		MLog.i("哈哈","判断服务是否开启的方法被调用 ====false");
		return false;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
