package com.jason.jasonutils.processmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.StorageUtil;

public class ProcessManagerActivity extends Activity {
	
	private TextView tv_taskmanager_count;
	private TextView tv_taskmanger_mem;
	private View loading;
	
	private List<ProcessInfoBean> userTaskInfos;
	private List<ProcessInfoBean> systemTaskInfos;
	
	private int runningProcessCount;
	private long availMem;
	
	private ListView lv_taskmanager;
	private ProcessManagerAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_manager);
		init();
		initData();
	}
	
	private void init() {
		tv_taskmanager_count = (TextView) findViewById(R.id.tv_taskmanager_count);
		tv_taskmanger_mem = (TextView) findViewById(R.id.tv_taskmanger_mem);
		lv_taskmanager = (ListView) findViewById(R.id.lv_taskmanager);
		loading = findViewById(R.id.loading);
	}

	private void initData() {
		runningProcessCount = StorageUtil.getRunningProcessCount(this);
		availMem = StorageUtil.getAvailRam(this);
		
		updateUITips();
		fillData();
		
		lv_taskmanager.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Object obj = lv_taskmanager.getItemAtPosition(position);// 获取当前被点击条目对应的对象.
				if (obj != null && obj instanceof ProcessInfoBean) {
					ProcessInfoBean taskInfo = (ProcessInfoBean) obj;
					if (taskInfo.getPackName().equals(getPackageName())) {
						return;// 说明当前应用就是我们自己
					}
					CheckBox cb = (CheckBox) view.findViewById(R.id.cb_taskmanager);
					if (taskInfo.isChecked()) {
						taskInfo.setChecked(false);// 更改业务bean里面的状态.
						cb.setChecked(false);// 更改界面上的状态
					} else {
						taskInfo.setChecked(true);
						cb.setChecked(true);
					}
				}

			}
		});
	}
	private void fillData() {
		new AsyncTask<Void,Void,Void>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loading.setVisibility(View.VISIBLE);
			}
			
			@Override
			protected Void doInBackground(Void... params) {
				List<ProcessInfoBean> taskInfos = ProcessInfoUtil.getTaskInfos(getApplicationContext());
				userTaskInfos = new ArrayList<ProcessInfoBean>();
				systemTaskInfos = new ArrayList<ProcessInfoBean>();
				for (ProcessInfoBean info : taskInfos) {
					if (info.isUserTask()) {
						userTaskInfos.add(info);
					} else {
						systemTaskInfos.add(info);
					}
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				adapter = new ProcessManagerAdapter(ProcessManagerActivity.this,userTaskInfos,systemTaskInfos);
				lv_taskmanager.setAdapter(adapter);
				loading.setVisibility(View.INVISIBLE);
			}
		}.execute();
	}
	
	public void killAll(View view) {
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<ProcessInfoBean> killedTask = new ArrayList<ProcessInfoBean>();
		long savedMem = 0;
		for (ProcessInfoBean info : userTaskInfos) {// 1.遍历界面 知道哪些条目被勾选了.
			if (info.isChecked()) {
				am.killBackgroundProcesses(info.getPackName());
				killedTask.add(info);
				savedMem += info.getMensize();
			}
		}
		for (ProcessInfoBean info : systemTaskInfos) {
			if (info.isChecked()) {
				am.killBackgroundProcesses(info.getPackName());
				killedTask.add(info);
				savedMem += info.getMensize();
			}
		}
		for (ProcessInfoBean info : killedTask) {
			if (info.isUserTask()) {
				userTaskInfos.remove(info);
			} else {
				systemTaskInfos.remove(info);
			}
		}

		Toast.makeText(this,"杀死了"+ killedTask.size()+ "个进程,释放了"+ Formatter.formatFileSize(getApplicationContext(),savedMem) + "的内存", Toast.LENGTH_SHORT).show();
		adapter.notifyDataSetChanged();// 刷新界面

		runningProcessCount -= killedTask.size();
		availMem += savedMem;
		updateUITips();
	}
	
	public void updateUITips(){
		tv_taskmanager_count.setText("运行进程:" + runningProcessCount + "个");
		tv_taskmanger_mem.setText("可用内存:"+ Formatter.formatFileSize(this, availMem));
	}


}
