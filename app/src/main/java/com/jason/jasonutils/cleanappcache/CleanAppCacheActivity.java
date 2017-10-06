package com.jason.jasonutils.cleanappcache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.MLog;

public class CleanAppCacheActivity extends Activity {
	
	private ProgressBar pb_clean_cache_progress;
	private TextView tv_clean_cache_scan_info;
	private LinearLayout ll_clean_cache_list_show;
	private View ll_clean_cache_loading;
	
	private PackageManager pm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clean_cache);
		addTestCache();
		init();
	}

	private void init() {
		pb_clean_cache_progress = (ProgressBar) findViewById(R.id.pb_clean_cache_progress);
		tv_clean_cache_scan_info = (TextView) findViewById(R.id.tv_clean_cache_scan_info);
		ll_clean_cache_list_show = (LinearLayout) findViewById(R.id.ll_clean_cache_list_show);
		ll_clean_cache_loading = findViewById(R.id.ll_clean_cache_loading);
		
		pm = getPackageManager();
		getData();
	}

	/**
	 * 通过反射机制来获取各个APP的数据
	 */
	private void getData() {
		new AsyncTask<Void,Object,Void>(){
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				ll_clean_cache_loading.setVisibility(View.VISIBLE);
				tv_clean_cache_scan_info.setText("正在初始化数据...");
			}
			
			@Override
			protected Void doInBackground(Void... params) {
				try{
					SystemClock.sleep(100);
					List<PackageInfo> packages = pm.getInstalledPackages(0);
					pb_clean_cache_progress.setMax(packages.size());
					int progress = 0;
					for(PackageInfo info : packages){
						String packageName = info.packageName;
						Method method = PackageManager.class.getMethod("getPackageSizeInfo", new Class[]{String.class, IPackageStatsObserver.class});
						method.invoke(pm, new Object[]{packageName, new MyPackageObserver(packageName)});
						progress++;
						SystemClock.sleep(30);
						pb_clean_cache_progress.setProgress(progress);
						publishProgress(info);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onProgressUpdate(Object... values) {
				super.onProgressUpdate(values);
				PackageInfo info = (PackageInfo) values[0];
				tv_clean_cache_scan_info.setText(info.applicationInfo.loadLabel(pm));
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				ll_clean_cache_loading.setVisibility(View.INVISIBLE);
				tv_clean_cache_scan_info.setText("扫描完毕...");
			}
			
		}.execute();
	}
	
	/**
	 * 跨进程获取各个APP的缓存大小
	 */
	private class MyPackageObserver extends IPackageStatsObserver.Stub {

		private String packageName;
		public MyPackageObserver(String packageName) {
			this.packageName = packageName;
		}

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
			long cacheSize = pStats.cacheSize; //long codeSize = pStats.codeSize; //long dataSize = pStats.dataSize;
			if(cacheSize>0){
				final String size = Formatter.formatFileSize(getApplicationContext(), cacheSize);
				MLog.i("发现一条缓存"+packageName+size);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						TextView tv = new TextView(getApplicationContext());
						tv.setTextSize(20);
						tv.setTextColor(Color.BLACK);
						tv.setText(packageName + ":" + size);
						setOnItemClick(tv, packageName);
						ll_clean_cache_list_show.addView(tv);
					}
				});
			}
		}
	}
	
	/**
	 * 点击跳转到清理缓存页面，并做了新旧版本的适配
	 * @param tv
	 * @param packageName
	 */
	public void setOnItemClick(View tv , final String packageName){
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if (Build.VERSION.SDK_INT >= 9) {
					intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setData(Uri.parse("package:" + packageName));
					startActivity(intent);
				}else {
					intent.setAction(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.addCategory("android.intent.category.VOICE_LAUNCH");
					intent.putExtra("pkg", packageName);
					startActivity(intent);
				}
			}
		});
	}

	/**
	 * 一键清理缓存
	 * @param view
	 */
	public void cleanAll(View view){
		Method deleteAllMethod = null;
		Method[]  methods = PackageManager.class.getMethods();
		for(Method method: methods){
			if("freeStorageAndNotify".equals(method.getName())){
				deleteAllMethod = method;
				break;
			}
		}
		try {
			deleteAllMethod.invoke(pm, new Object[]{Long.MAX_VALUE,new MyClearCacheObserver()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class MyClearCacheObserver extends IPackageDataObserver.Stub{
		@Override
		public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
			MLog.i("一键清理完毕"+succeeded);
			if(succeeded){
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(), "清理完毕",Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
	}
	
	/**
	 * 添加缓存数据
	 */
	public void addTestCache(){
		try {
			File file = new File(getCacheDir(),"haha.text");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write("哈哈哈哈".getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
