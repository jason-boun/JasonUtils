package com.jason.jasonutils.blacknumber;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jason.jasonutils.R;
import com.jason.jasonutils.customview.MyCustomSettingView;
import com.jason.jasonutils.tools.MyAsyncTask;

public class BlackNumberFunctionListActivity extends Activity {

	private LinearLayout loading_data;
	private BlackNumberDao dao;
	private MyCustomSettingView cv_black_number_manager_checkbox;
	private SharedPreferences sp;
	private static final String OPEN_BLACKNUBMER_MANAGER ="open_blacknubmer_manager";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.black_number_fuction_list_activity);
		loading_data = (LinearLayout) findViewById(R.id.loading_data);
		dao = new BlackNumberDao(BlackNumberFunctionListActivity.this);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		
		cv_black_number_manager_checkbox = (MyCustomSettingView) findViewById(R.id.cv_black_number_manager_checkbox);
		
		boolean spSet = sp.getBoolean(OPEN_BLACKNUBMER_MANAGER, false);
		if(spSet){
			startService(new Intent(BlackNumberFunctionListActivity.this, BlackNumberService.class));
		}else{
			stopService(new Intent(BlackNumberFunctionListActivity.this, BlackNumberService.class));
		}
		cv_black_number_manager_checkbox.setChecked(isServiceRunning(this, BlackNumberService.class));
		cv_black_number_manager_checkbox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cv_black_number_manager_checkbox.isChecked()){
					stopService(new Intent(BlackNumberFunctionListActivity.this, BlackNumberService.class));
					cv_black_number_manager_checkbox.setChecked(false);
					sp.edit().putBoolean(OPEN_BLACKNUBMER_MANAGER, false).commit();
				}else{
					startService(new Intent(BlackNumberFunctionListActivity.this, BlackNumberService.class));
					cv_black_number_manager_checkbox.setChecked(true);
					sp.edit().putBoolean(OPEN_BLACKNUBMER_MANAGER, true).commit();
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		cv_black_number_manager_checkbox.setChecked(isServiceRunning(this, BlackNumberService.class));
	}
	
	private void createData() {
		new MyAsyncTask() {
			@Override
			protected void onPreExecute() {
				getSharedPreferences("config", MODE_PRIVATE).edit().putBoolean("hasCreateDatabase", true).commit();
				loading_data.setVisibility(View.VISIBLE);
			}
			
			@Override
			protected void onPostExecute() {
				loading_data.setVisibility(View.INVISIBLE);
				Toast.makeText(BlackNumberFunctionListActivity.this, "数据已经生成", 0).show();
			}
			
			@Override
			protected void doInBackgroud() {
				Random random = new Random();
				for (int i = 1; i < 100; i++) {
					long number = 13500000000l + i;
					int mode = random.nextInt(3)+1;
					dao.add(new BlackNumberBean(String.valueOf(number),String.valueOf(mode)));
				}
			}
		}.execute();
	}

	public void createDataBase(View view){
		if(getSharedPreferences("config", MODE_PRIVATE).getBoolean("hasCreateDatabase", false)){
			Toast.makeText(BlackNumberFunctionListActivity.this, "数据已经存在", 0).show();
			return;
		}
		createData();
	}
	public void optimize(View view){
		startActivity(new Intent(this, ShowOptimizeActivity.class));
	}
	public void batchLoad(View view){
		startActivity(new Intent(this, ShowBatchLoadActivity.class));
	}
	public void pageLoad(View view){
		startActivity(new Intent(this, ShowPageLoadActivity.class));
	}
	
	/**
	 * 判断服务是否运行
	 * @param context
	 * @param clazz
	 * @return
	 */
	public boolean isServiceRunning(Context context, Class clazz){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServicesInfos = am.getRunningServices(1000);
		for(RunningServiceInfo info : runningServicesInfos){
			if(clazz.getName().equals(info.service.getClassName())){
				return true;
			}
		}
		return false;
	}
}
