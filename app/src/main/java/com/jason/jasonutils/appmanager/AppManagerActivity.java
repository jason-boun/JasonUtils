package com.jason.jasonutils.appmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.StorageUtil;

public class AppManagerActivity extends Activity implements OnClickListener{
	
	private TextView tv_rom;
	private TextView tv_sd;
	private View loading;
	private TextView tv_status;
	
	private ListView appListView;
	private AppManagerAdapter adapter;
	
	private List<AppInfoBean> userAppInfos = new ArrayList<AppInfoBean>();
	private List<AppInfoBean> systemAppInfos = new ArrayList<AppInfoBean>();
	
	private boolean loadDataFinished = false;
	
	private AppInfoBean selectedAppInfoBean;
	private PopupWindow popWindow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);
		init();
		initData();
	}


	private void init(){
		tv_rom  = (TextView) findViewById(R.id.tv_appmanager_rom);
		tv_sd  = (TextView) findViewById(R.id.tv_appmanger_sd);
		appListView = (ListView) findViewById(R.id.lv_appmanager);
		loading = findViewById(R.id.loading);
		tv_status = (TextView) findViewById(R.id.tv_status);
	}
	

	private void initData() {
		tv_rom.setText("内存可用:" + StorageUtil.getAvailableROM(AppManagerActivity.this));
		tv_sd.setText("SD卡可用:" + StorageUtil.getAvailabeSD(AppManagerActivity.this));
		
		appListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if(loadDataFinished){
					if (firstVisibleItem >= (userAppInfos.size() + 1)) {// 说明当前小标签已经到了系统程序
						tv_status.setText("系统程序:(" + systemAppInfos.size()+ ")个");
					} else {
						tv_status.setText("用户程序:(" + userAppInfos.size() + ")个");
					}
				}
			}
		} );
		appListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PopupWindowUtil.dissMissWindow(popWindow);
				//Object obj = parent.getSelectedItem();//
				Object obj = appListView.getItemAtPosition(position);
				if(obj!=null && obj instanceof AppInfoBean){
					selectedAppInfoBean = (AppInfoBean) obj;
					PopupWindowUtil.showPopupWindow(AppManagerActivity.this, popWindow, view);
				}
			}
		});
		updatedData();
	}
	
	private void updatedData(){
		new AsyncTask<Void,Void,Void>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loadDataFinished = false;
				loading.setVisibility(View.VISIBLE);
			}
			@Override
			protected Void doInBackground(Void... params) {
				List<AppInfoBean> appInfos = AppInfoUtil.getAppInfo(getApplicationContext());
				if(appInfos!=null && appInfos.size()>0){
					for(AppInfoBean bean:appInfos){
						if(bean.isUserApp()){
							userAppInfos.add(bean);
						}else{
							systemAppInfos.add(bean);
						}
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				adapter = new AppManagerAdapter(AppManagerActivity.this, userAppInfos, systemAppInfos);
				appListView.setAdapter(adapter);
				loadDataFinished = true;
				loading.setVisibility(View.INVISIBLE);
				tv_status.setText("用户程序:(" + userAppInfos.size() + ")个");
			}
			
		}.execute();
	}


	@Override
	public void onClick(View v) {
		PopupWindowUtil.dissMissWindow(popWindow);
		switch (v.getId()) {
		case R.id.ll_app_onperate_start:
			OperateAppUtil.openApp(AppManagerActivity.this, selectedAppInfoBean.getPackName());
			break;
		case R.id.ll_app_onperate_share:
			OperateAppUtil.shareApp(AppManagerActivity.this, selectedAppInfoBean.getPackName());
			break;
		case R.id.ll_app_onperate_uninstall:
			if(selectedAppInfoBean.isUserApp()){
				OperateAppUtil.uninstallApp(AppManagerActivity.this, selectedAppInfoBean.getPackName());
			}else{
				Toast.makeText(AppManagerActivity.this,"卸载系统应用需要root权限",Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		updatedData();
	}
	
	@Override
	protected void onStop() {
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		PopupWindowUtil.dissMissWindow(popWindow);
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if( PopupWindowUtil.dissMissWindow(popWindow)){
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
