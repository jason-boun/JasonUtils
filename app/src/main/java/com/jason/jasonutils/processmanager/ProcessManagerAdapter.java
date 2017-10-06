package com.jason.jasonutils.processmanager;

import java.util.List;

import com.jason.jasonutils.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProcessManagerAdapter extends BaseAdapter {
	
	private Activity act;
	private List<ProcessInfoBean> userTaskInfos;
	private List<ProcessInfoBean> systemTaskInfos;
	
	public ProcessManagerAdapter(Activity act, List<ProcessInfoBean> userTaskInfos, List<ProcessInfoBean> systemTaskInfos){
		this.act = act;
		this.userTaskInfos = userTaskInfos;
		this.systemTaskInfos = systemTaskInfos;
	}

	public int getCount() {
		SharedPreferences sp = act.getSharedPreferences("config", act.MODE_PRIVATE);
		boolean showsystem = sp.getBoolean("showsystem", true);
		if (showsystem) {
			return userTaskInfos.size() + 1 + systemTaskInfos.size() + 1;
		} else {
			return userTaskInfos.size() + 1;
		}
	}

	public Object getItem(int position) {
		ProcessInfoBean taskinfo;
		if (position == 0 || position == (userTaskInfos.size() + 1)) {
			return null;
		} else if (position <= userTaskInfos.size()) {
			taskinfo = userTaskInfos.get(position - 1);
		} else {// 系统进程
			int newposition = position - 1 - userTaskInfos.size() - 1;
			taskinfo = systemTaskInfos.get(newposition);
		}
		return taskinfo;

	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ProcessInfoBean taskinfo = null;
		if (position == 0) {
			TextView tv = new TextView(act);
			tv.setText("用户进程:" + userTaskInfos.size() + "个");
			tv.setBackgroundColor(Color.GRAY);
			tv.setTextColor(Color.BLACK);
			return tv;
		} else if (position == (userTaskInfos.size() + 1)) {
			TextView tv = new TextView(act);
			tv.setText("系统进程:" + systemTaskInfos.size() + "个");
			tv.setBackgroundColor(Color.GRAY);
			tv.setTextColor(Color.BLACK);
			return tv;
		} else if (position <= userTaskInfos.size()) {
			taskinfo = userTaskInfos.get(position - 1);
		} else {// 系统进程
			int newposition = position - 1 - userTaskInfos.size() - 1;
			taskinfo = systemTaskInfos.get(newposition);
		}
		ViewHolder holder;
		if (convertView != null && convertView instanceof RelativeLayout) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = View.inflate(act, R.layout.process_manager_list_item, null);
			holder = new ViewHolder();
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_taskmanger_icon);
			holder.tv_mem = (TextView) convertView.findViewById(R.id.tv_taskmanager_mem);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_taskmanager_name);
			holder.cb = (CheckBox) convertView.findViewById(R.id.cb_taskmanager);
			convertView.setTag(holder);
		}
		holder.iv_icon.setImageDrawable(taskinfo.getAppIcon());
		holder.tv_name.setText(taskinfo.getAppName());
		holder.tv_mem.setText("内存占用:" + Formatter.formatFileSize(act, taskinfo.getMensize()));

		// 每一次都去重新初始化checkbox的状态.
		holder.cb.setChecked(taskinfo.isChecked());
		if (act.getPackageName().equals(taskinfo.getPackName())) {
			holder.cb.setVisibility(View.INVISIBLE);
		} else {
			holder.cb.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	
	static class ViewHolder {
		TextView tv_name;
		TextView tv_mem;
		ImageView iv_icon;
		CheckBox cb;

	}
}
