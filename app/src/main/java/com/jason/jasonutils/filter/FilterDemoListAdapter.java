package com.jason.jasonutils.filter;

import java.util.ArrayList;

import com.jason.jasonutils.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

/**
 * 在Adapter类中增加过滤器字段，同时对外提供getFilter方法；
 * 在提供Filter实例时，处理过滤逻辑，同时返回过滤结果到数据List中，并刷新适配器。
 * 也可以直接让FilterDemoListAdapter实现过滤器接口来实现该功能
 */
public class FilterDemoListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<PersonBean> dataList;
	private ArrayList<PersonBean> fullDataList = new ArrayList<PersonBean>();//传递过来的原始集合
	
	@SuppressWarnings("unchecked")
	public FilterDemoListAdapter(Context context, ArrayList<PersonBean> fromList){
		this.context = context;
		if(fromList.size()==0){
			dataList = new ArrayList<PersonBean>();
		}else{
			//记录当前需要展示的数据
			this.dataList = fromList;
			//克隆一个包含相同数据的容器，但这个容器只是一个备份参考容器，即 适配器使用的容器数据必须还是dataList
			this.fullDataList = (ArrayList<PersonBean>) dataList.clone();
		}
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.filter_demo_list_item, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_age = (TextView) convertView.findViewById(R.id.tv_age);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		PersonBean personBean = dataList.get(position);
		holder.tv_name.setText(personBean.getName());
		holder.tv_age.setText(personBean.getAge()+"");
		
		return convertView;
	}
	
	private class ViewHolder{
		TextView tv_name, tv_age;
	}
	
	public Filter getmFilter() {
		return mFilter;
	}
	
	private Filter mFilter = new Filter() {
		String latestSearchStr = "";
		
		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			dataList = (ArrayList<PersonBean>) results.values;
			if(results.count>0){
				notifyDataSetChanged();
			}else{
				notifyDataSetInvalidated();
			}
		}
		
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults filterResults = new FilterResults();
			if(constraint.length()==0){
				filterResults.count = fullDataList.size();
				filterResults.values = fullDataList;
			}else{
				boolean isAdd = false;
				if(constraint.length()>latestSearchStr.length()){
					isAdd = true;
					latestSearchStr = (String) constraint;
				}
				//如果是搜索字符串继续增加，则在上次结果的基础上（范围内）进行搜索（通过isAdd字段判断优化了搜索范围）
				ArrayList<PersonBean> tempResults = new ArrayList<PersonBean>();
				for(PersonBean p: isAdd? dataList:fullDataList){
					if(p.getName().contains(constraint) || String.valueOf(p.getAge()).contains(constraint)){
						tempResults.add(p);
					}
				}
				filterResults.count = tempResults.size();
				filterResults.values = tempResults;
			}
			return filterResults;
		}
	};
}
