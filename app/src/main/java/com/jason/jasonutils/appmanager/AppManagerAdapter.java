package com.jason.jasonutils.appmanager;

import java.util.List;

import com.jason.jasonutils.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppManagerAdapter extends BaseAdapter {
	
	private List<AppInfoBean> userAppInfos ;
	private List<AppInfoBean> systemAppInfos ;
	private Context ctx;
	
	public AppManagerAdapter(Context ctx, List<AppInfoBean> userAppInfos, List<AppInfoBean> systemAppInfos){
		this.ctx = ctx;
		this.userAppInfos = userAppInfos;
		this.systemAppInfos = systemAppInfos;
	}

	@Override
	public int getCount() {
		return userAppInfos.size()+1 + systemAppInfos.size()+1; 
	}

	@Override
	public Object getItem(int position) {
		if(position == 0){
			return null;
		}else if(position == userAppInfos.size()+1){
			return null;
		}else if(position <= userAppInfos.size()){
			int newPosition = position - 1;
			return userAppInfos.get(newPosition);
		}else{
			int newPosition = position - userAppInfos.size()-2;
			return systemAppInfos.get(newPosition);
		}
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AppInfoBean infoBean = null;
		if(position == 0){
			TextView tv = new TextView(ctx);
			tv.setBackgroundColor(Color.GRAY);
			tv.setTextColor(Color.BLACK);
			tv.setText("用户程序:(" + userAppInfos.size() + ")个");
			return tv;
		}else if(position == userAppInfos.size()+1){
			TextView tv = new TextView(ctx);
			tv.setBackgroundColor(Color.GRAY);
			tv.setText("系统程序:(" + systemAppInfos.size() + ")个");
			tv.setTextColor(Color.BLACK);
			return tv;
		}else if(position <= userAppInfos.size()+1){
			infoBean = userAppInfos.get(position-1);
		}else {
			infoBean = systemAppInfos.get(position-1-userAppInfos.size()-1);
		}
		
		ViewHolder holder ;
		if(convertView !=null && convertView instanceof RelativeLayout){
			holder = (ViewHolder) convertView.getTag();
		}else{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(ctx).inflate(R.layout.app_manager_list_item, null);
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_app_name);
			holder.tv_location = (TextView) convertView.findViewById(R.id.tv_app_location);
			holder.tv_version = (TextView) convertView.findViewById(R.id.tv_app_version);
			
			convertView.setTag(holder);
		}
		
		holder.iv_icon.setImageDrawable(infoBean.getAppIcon());
		holder.tv_name.setText(infoBean.getAppName());
		holder.tv_location.setText(infoBean.isInRom()?("手机内存"):("SD卡"));
		holder.tv_version.setText(infoBean.getVersion());
		
		return convertView;
	}
	

	static class ViewHolder {
		TextView tv_name;
		TextView tv_version;
		TextView tv_location;
		ImageView iv_icon;
	}

}
