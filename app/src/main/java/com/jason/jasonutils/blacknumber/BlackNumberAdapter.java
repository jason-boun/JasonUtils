package com.jason.jasonutils.blacknumber;

import java.util.List;

import android.content.Context;
import com.jason.jasonutils.tools.MLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jason.jasonutils.R;

public class BlackNumberAdapter extends BaseAdapter {
	
	private List<BlackNumberBean> dataList = null;
	private Context context;
	private BlackNumberDao dao;
	public BlackNumberAdapter(Context context, List<BlackNumberBean> dataList, BlackNumberDao dao){
		this.context = context;
		this.dataList = dataList;
		this.dao = dao;
	}

	@Override
	public int getCount() {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.black_number_list_item, null);
			holder.tv_number = (TextView) convertView.findViewById(R.id.tv_black_number_phone);
			holder.tv_mode = (TextView) convertView.findViewById(R.id.tv_black_number_mode);
			holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_black_number_delete);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final BlackNumberBean bean = dataList.get(position);
		MLog.i("哈哈", "position==="+position+"==="+bean.toString());
		
		holder.tv_number.setText(bean.getNumber());
		String mode = bean.getMode();
		switch (Integer.parseInt(mode)) {
		case 1:
			holder.tv_mode.setText("全部拦截");
			break;
		case 2:
			holder.tv_mode.setText("电话拦截");
			break;
		case 3:
			holder.tv_mode.setText("短信拦截");
			break;
		}
		holder.iv_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(dataList.contains(bean)){
					dataList.remove(bean);
				}
				dao.delete(bean.getNumber());
				notifyDataSetChanged();
			}
		});
		return convertView;
	}
	
	private class ViewHolder{
		TextView tv_number;
		TextView tv_mode;
		ImageView iv_delete;
	}
}
