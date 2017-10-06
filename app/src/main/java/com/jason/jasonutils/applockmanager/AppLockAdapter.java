package com.jason.jasonutils.applockmanager;

import java.util.List;

import com.jason.jasonutils.R;
import com.jason.jasonutils.appmanager.AppInfoBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppLockAdapter extends BaseAdapter {
	
	private Context ctx;
	private boolean unLock;
	private List<AppInfoBean> dataList;
	private TextView countInfo;

	public AppLockAdapter(Context context, boolean unLock, List<AppInfoBean> dataList,TextView countInfo) {
		this.ctx = context;
		this.unLock = unLock;
		this.dataList = dataList;
		this.countInfo = countInfo;
	}

	@Override
	public int getCount() {
		countInfo.setText((unLock?"未":"已")+"加锁程序:"+dataList.size()+"个");
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AppInfoBean appinfo = dataList.get(position);
		ViewHolder holder;
		View view;
		if (convertView != null && convertView instanceof RelativeLayout) {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		} else {
			view = View.inflate(ctx,R.layout.list_applock_unlock, null);
			holder = new ViewHolder();
			holder.iv_icon = (ImageView) view.findViewById(R.id.iv_applock_icon);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_applock_name);
			holder.iv_status = (ImageView) view.findViewById(R.id.iv_applock_status);
			view.setTag(holder);
		}
		holder.iv_icon.setImageDrawable(appinfo.getAppIcon());
		holder.tv_name.setText(appinfo.getAppName());
		if (unLock) {
			holder.iv_status.setImageResource(R.drawable.list_button_lock_pressed);
		}else{
			holder.iv_status.setImageResource(R.drawable.list_button_unlock_pressed);
		}
		return view;
	}
	
	static class ViewHolder{
		ImageView iv_icon;
		TextView tv_name;
		ImageView iv_status;
	}

}
