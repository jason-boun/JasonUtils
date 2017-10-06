package com.jason.jasonutils.spinner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jason.jasonutils.R;

public class SpinnerAdapter extends BaseAdapter {
	private String[] datas;
	private Context context;

	public SpinnerAdapter(String[] datas, Context context) {
		this.datas = datas;
		this.context = context;
	}

	@Override
	public int getCount() {
		return datas == null ? 0 : datas.length;
	}

	@Override
	public Object getItem(int position) {
		return datas[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView v = null;

		v = (TextView) View.inflate(context, R.layout.spinner_item, null);
		v.setText(datas[position].split("-")[0]);

		return v;
	}

}
