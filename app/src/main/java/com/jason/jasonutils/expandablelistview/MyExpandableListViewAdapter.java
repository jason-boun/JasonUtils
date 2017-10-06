package com.jason.jasonutils.expandablelistview;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {
	
	private List<String> groupsNames;
	private SparseArray<List<String>> childInfosCache = new SparseArray<List<String>>();
	
	private Context context;
	public MyExpandableListViewAdapter(Context context){
		this.context = context;
	}

	@Override
	public int getGroupCount() {
		groupsNames = CommonNumberDao.getGroupNames();
		return groupsNames.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getChildrenCount(int groupPosition) {
		List<String> childInfos;
		Object obj  = childInfosCache.get(groupPosition);// 直接拿缓存
		if (obj instanceof List<?>) {
			childInfos = (List<String>)obj;
		} else {
			childInfos = CommonNumberDao.getChildInfosByPosition(groupPosition);
			childInfosCache.put(groupPosition, childInfos);
		}
		return childInfos.size();

	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		TextView tv;
		if (convertView instanceof TextView) {
			tv = (TextView) convertView;
		} else {
			tv = new TextView(context);
			tv.setTextSize(28);
			tv.setTextColor(Color.BLACK);
		}
		tv.setText("     " + groupsNames.get(groupPosition));
		return tv;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		TextView tv;
		if (convertView instanceof TextView) {
			tv = (TextView) convertView;
		} else {
			tv = new TextView(context);
			tv.setTextSize(20);
			tv.setTextColor(Color.GRAY);
		}
		tv.setText("		"+childInfosCache.get(groupPosition).get(childPosition));
		return tv;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;//返回true，可相应点击事件
	}

}
