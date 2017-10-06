package com.jason.jasonutils.sortsequence;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jason.jasonutils.R;

public class SortListAdapter extends BaseAdapter {
	
	private Context context;
	private List<SortBean> beanList;
	private HashMap<String,Integer> zmbarIndex = null;
	
	public SortListAdapter(Context context,List<SortBean> beanList, ZmBar zmBar){
		this.context = context;
		this.beanList = beanList;
		this.zmbarIndex = new HashMap<String,Integer>();//为按拼音搜索做准备：存储容器中拼音首字母在容器中的位置。
		int size = beanList.size();
		if(size >0){
			for(int i=0;i< size; i++){
				String tempName =  PinYinUtil.getAlpha(beanList.get(i).getPinYin());
				if(!zmbarIndex.containsKey(tempName)){
					zmbarIndex.put(tempName, i);//为按拼音搜索做准备：存储容器中拼音首字母在容器中的位置。
				}
			}
		}
		zmBar.setAlphaIndexer(zmbarIndex);
	}

	@Override
	public int getCount() {
		return beanList.size();
	}

	@Override
	public Object getItem(int position) {
		return beanList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.sort_list_item, null);
			holder.pinYinLable = (TextView) convertView.findViewById(R.id.tv_pinyin_type);
			holder.photo = (ImageView) convertView.findViewById(R.id.iv_item_photo);
			holder.name = (TextView) convertView.findViewById(R.id.tv_item_name);
			holder.number = (TextView) convertView.findViewById(R.id.tv_item_number);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		SortBean sortBean = beanList.get(position);
		holder.name.setText(sortBean.getName());
		holder.number.setText(sortBean.getNumber()+"");
		if(sortBean.getPhoto()!=null){
			holder.photo.setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(sortBean.getPhoto())));
		}else{
			holder.photo.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.contact_photo));
		}
		
		//字母序列判断与显示
		String currentPinYin = PinYinUtil.getAlpha(sortBean.getPinYin());//获取当天条目的拼音首字母
		String prePinYin = (position-1)>=0? PinYinUtil.getAlpha((beanList.get(position-1)).getPinYin()) : " ";//获取上一个条目的拼音首字母
		if(!prePinYin.equals(currentPinYin)){
			holder.pinYinLable.setVisibility(View.VISIBLE);
			holder.pinYinLable.setText(currentPinYin);
		}else{
			holder.pinYinLable.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	public class ViewHolder{
		ImageView photo;
		TextView name;
		TextView number;
		TextView pinYinLable;
	}

}
