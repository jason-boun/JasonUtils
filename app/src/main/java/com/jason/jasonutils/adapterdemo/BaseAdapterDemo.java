package com.jason.jasonutils.adapterdemo;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import com.jason.jasonutils.R;
import com.jason.jasonutils.sortsequence.PinYinUtil;
import com.jason.jasonutils.sortsequence.SortBean;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseAdapterDemo extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MyBaseAdapter baseAdapter = new MyBaseAdapter(this, generateData());
		setListAdapter(baseAdapter);
	}
	
	public List<SortBean> generateData(){
		List<SortBean> packageBeanList = new ArrayList<SortBean>();
		String[] nameArray = new String[]{"张三","李四","王五","赵六","周七","孙八","李波","Jason","Bob","James","123"};
		Long[] numberArray =new Long[]{13866123l,12348456l,8894564l,8461236l,5417842l,1445679l,65789684l,18641235l,3694785641l,54645687l,3578468l};
		SortBean sortBean = null;
		for(int i=0;i<nameArray.length;i++){
			sortBean = new SortBean();
			sortBean.setName(nameArray[i]);
			sortBean.setNumber(numberArray[i]);
			sortBean.setPinYin(PinYinUtil.getPingYin(nameArray[i]));//获取name对应的拼音
			sortBean.setPhoto(new byte[]{});
			packageBeanList.add(sortBean);
		}
		return packageBeanList;
	}
	
	public class MyBaseAdapter extends BaseAdapter{
		private Context context;
		private List<SortBean> beanList;
		
		public MyBaseAdapter(Context context,List<SortBean> beanList ){
			this.context = context;
			this.beanList = beanList;
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
			if(sortBean.getPhoto()!=null && sortBean.getPhoto().length>0){
				holder.photo.setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(sortBean.getPhoto())));
			}else{
				holder.photo.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.contact_photo));
			}
			
			//字母序列判断与显示
			String currentPinYin = PinYinUtil.getAlpha(sortBean.getPinYin());//获取拼音字段的首字母
			String prePinYin = (position-1)>=0? PinYinUtil.getAlpha((beanList.get(position-1)).getPinYin()) : " ";
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
}
