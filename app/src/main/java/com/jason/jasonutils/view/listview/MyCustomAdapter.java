package com.jason.jasonutils.view.listview;

import java.util.ArrayList;
import java.util.TreeSet;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.DensityUtil;

public class MyCustomAdapter extends BaseAdapter {
	 
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;
    private Context context;
    private LayoutInflater mInflater;

    private ArrayList<String> mData = new ArrayList<String>();//数据
    private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();//存储分隔线所在的位置序号

    public MyCustomAdapter(Context context) {
    	this.context = context;
        mInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSeparatorItem(final String item) {
        mData.add(item);
        mSeparatorsSet.add(mData.size() - 1);//记录分隔线在数据容器中的位置
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = getItemViewType(position);
        if(convertView!=null){
        	Log.i("哈哈","convertView " + convertView.hashCode());
        }
        if (convertView == null) {
        	convertView = new TextView(context);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView;
            switch (type) {
                case TYPE_ITEM:
                	holder.textView.setText("条目");
                    holder.textView.setHeight(DensityUtil.dip2px(context, 40));
                    break;
                case TYPE_SEPARATOR:
                	holder.textView.setText("分隔线");
                    holder.textView.setHeight(DensityUtil.dip2px(context, 25));
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.textView.setText(mData.get(position));
        return convertView;
    }
    
    public static class ViewHolder {  
        public TextView textView;  
    } 
}
