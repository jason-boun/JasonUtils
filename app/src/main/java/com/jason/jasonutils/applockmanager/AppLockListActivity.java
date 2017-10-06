package com.jason.jasonutils.applockmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jason.jasonutils.R;
import com.jason.jasonutils.appmanager.AppInfoBean;
import com.jason.jasonutils.appmanager.AppInfoUtil;
import com.jason.jasonutils.tools.DensityUtil;
import com.jason.jasonutils.tools.MLog;
import com.jason.jasonutils.tools.MyAsyncTask;

public class AppLockListActivity extends Activity implements OnClickListener{

	private Button bt_unlock_switch, bt_locked_switch;
	private LinearLayout ll_unlock_info,ll_locked_info;
	private TextView tv_unlock_count ,tv_locked_count;
	private ListView lv_unlock_list,lv_locked_list;
	
	private View loading;
	
	private List<AppInfoBean> allAppInfos;
	private List<AppInfoBean> unlockAppInfos = new ArrayList<AppInfoBean>();//未加锁应用的集合
	private List<AppInfoBean> lockedAppInfos = new ArrayList<AppInfoBean>();;//已加锁应用的集合
	
	private AppLockAdapter unlockAdapter;
	private AppLockAdapter lockedAdapter;
	private AppLockDao dao;
	private Activity act;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_lock);
		act = AppLockListActivity.this;
		init();
	}

	private void init() {
		bt_unlock_switch = (Button) findViewById(R.id.bt_unlock_switch);
		bt_locked_switch = (Button) findViewById(R.id.bt_locked_switch);
		
		ll_unlock_info = (LinearLayout) findViewById(R.id.ll_unlock_info);
		ll_locked_info = (LinearLayout) findViewById(R.id.ll_locked_info);
		
		tv_unlock_count = (TextView) findViewById(R.id.tv_unlock_count);
		tv_locked_count = (TextView) findViewById(R.id.tv_locked_count);
		
		lv_unlock_list = (ListView) findViewById(R.id.lv_unlock_list);
		lv_locked_list = (ListView) findViewById(R.id.lv_locked_list);
		
		loading = findViewById(R.id.loading);
		
		bt_unlock_switch.setOnClickListener(this);
		bt_locked_switch.setOnClickListener(this);
		
		bt_unlock_switch.setHeight(DensityUtil.dip2px(act, 60));
		bt_locked_switch.setHeight(DensityUtil.dip2px(act, 60));
		tv_unlock_count.setHeight(DensityUtil.dip2px(act, 30));
		tv_locked_count.setHeight(DensityUtil.dip2px(act, 30));
		initData();
	}
	
	private void initData() {
		new AsyncTask<Void, Void, Void>(){
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loading.setVisibility(View.VISIBLE);
				dao = new AppLockDao(act);
			}

			@Override
			protected Void doInBackground(Void... params) {
				List<String> lockPackageList = dao.findAll();
				allAppInfos = AppInfoUtil.getAppInfo(act);
				if(allAppInfos!=null && allAppInfos.size()>0){
					for(AppInfoBean info : allAppInfos){
						if(lockPackageList.contains(info.getPackName())){
							lockedAppInfos.add(info);
						}else{
							unlockAppInfos.add(info);
						}
					}
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				unlockAdapter = new AppLockAdapter(act, true, unlockAppInfos,tv_unlock_count);
				lockedAdapter = new AppLockAdapter(act, false, lockedAppInfos,tv_locked_count);
				lv_unlock_list.setAdapter(unlockAdapter);
				lv_locked_list.setAdapter(lockedAdapter);
				loading.setVisibility(View.INVISIBLE);
				listViewItemClick();
			}
		}.execute();
	}
	
	private void listViewItemClick() {
		lv_unlock_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				final AppInfoBean appInfo = (AppInfoBean) lv_unlock_list.getItemAtPosition(position);
				MLog.i("ListView 条目===appInfo==="+appInfo.toString());
				new MyAsyncTask() {
					@Override
					protected void onPreExecute() {
						TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
						ta.setDuration(200);
						view.startAnimation(ta);
						String packname = appInfo.getPackName();
						if(!dao.find(packname)){
							dao.add(packname);//添加到数据库
						}
					}
					
					@Override
					protected void doInBackgroud() {
						SystemClock.sleep(300);
					}
					@Override
					protected void onPostExecute() {
						if(unlockAppInfos.contains(appInfo)){
							unlockAppInfos.remove(appInfo);
							unlockAdapter.notifyDataSetChanged();
						}
						if(!lockedAppInfos.contains(appInfo)){
							lockedAppInfos.add(appInfo);
							lockedAdapter.notifyDataSetChanged();
						}
					}
				}.execute();
			}
		});
		
		lv_locked_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				final AppInfoBean appInfo = (AppInfoBean) lv_locked_list.getItemAtPosition(position);
				MLog.i("ListView 条目===appInfo==="+appInfo.toString());
				new MyAsyncTask() {
					@Override
					protected void onPreExecute() {
						TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
						ta.setDuration(200);
						view.startAnimation(ta);
						dao.delete(appInfo.getPackName());//从数据库删除
					}
					
					@Override
					protected void doInBackgroud() {
						SystemClock.sleep(300);
					}
					@Override
					protected void onPostExecute() {
						if(lockedAppInfos.contains(appInfo)){
							lockedAppInfos.remove(appInfo);
							lockedAdapter.notifyDataSetChanged();
						}
						if(!unlockAppInfos.contains(appInfo)){
							unlockAppInfos.add(appInfo);
							unlockAdapter.notifyDataSetChanged();
						}
					}
				}.execute();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_unlock_switch:
			showUnlockInfo();
			break;
		case R.id.bt_locked_switch:
			showLockInfo();
			break;
		}
	}
	
	/**
	 * 展示未加锁列表
	 */
	private void showUnlockInfo() {
		ll_unlock_info.setVisibility(View.VISIBLE);
		bt_unlock_switch.setBackgroundResource(R.drawable.app_lock_tab_left_pressed);
		ll_locked_info.setVisibility(View.INVISIBLE);
		bt_locked_switch.setBackgroundResource(R.drawable.app_lock_tab_right_default);
	}
	
	/**
	 * 展示加锁列表
	 */
	private void showLockInfo() {
		ll_unlock_info.setVisibility(View.INVISIBLE);
		bt_unlock_switch.setBackgroundResource(R.drawable.app_lock_tab_left_default);
		ll_locked_info.setVisibility(View.VISIBLE);
		bt_locked_switch.setBackgroundResource(R.drawable.app_lock_tab_right_pressed);
	}
}
