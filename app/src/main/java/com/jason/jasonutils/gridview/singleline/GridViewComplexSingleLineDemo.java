package com.jason.jasonutils.gridview.singleline;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jason.jasonutils.R;
import com.jason.jasonutils.gridview.complex.PersonListActivity;
import com.jason.jasonutils.tools.MLog;

public class GridViewComplexSingleLineDemo extends Activity implements OnClickListener {
	
	private ListView lv_contact_list_show;
	private ListAdatpter listAdapter;
	private GridView gv_contact_selected_show;
	private GridViewAdapter gridAdapter;
	private List<Person> ContactList = PersonDataUtil.dataFactory(5,0);
	private List<Person> selectedList = new ArrayList<Person>() ;
	
	private Button bt_select_confirm;
	private int ImageWidth, ImageSpace, imagePadding ;
	private Activity act;
	
	private HorizontalScrollView scrollView;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if(!act.isFinishing()){
					updateUI();
				}
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridview_complex_singleline_demo_acitvity);
		init();
		initListViewData();
		initGridViewData();
		initOperateGridViewItem();
	}

	private void init() {
		lv_contact_list_show = (ListView) findViewById(R.id.lv_contact_list_show);
		gv_contact_selected_show = (GridView) findViewById(R.id.gv_contact_selected_show);
		bt_select_confirm = (Button) findViewById(R.id.bt_select_confirm);
		bt_select_confirm.setOnClickListener(this);
		act = GridViewComplexSingleLineDemo.this;
		if(ContactList.size()>0){
			byte[] photoData = PersonDataUtil.getPhotoData(act, -1);
			for(Person p:ContactList){
				p.setPhoto(photoData);
				p.setChecked(false);
			}
		}
		setConfirmButton(0);
		ImageWidth = PersonDataUtil.dip2px(act, 40);
		ImageSpace = PersonDataUtil.dip2px(act, 3);
		imagePadding = PersonDataUtil.dip2px(act, 4);
	}

	private void initListViewData() {
		listAdapter = new ListAdatpter(this, ContactList);
		lv_contact_list_show.setAdapter(listAdapter);
		lv_contact_list_show.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Person p = ContactList.get(position);
				if(p.isChecked()){
					p.setChecked(false);
					if(selectedList.contains(p)){
						selectedList.remove(p);
					}
				}else{
					p.setChecked(true);
					if(!selectedList.contains(p)){
						selectedList.add(p);
					}
				}
				mHandler.sendEmptyMessage(0);
			}
		});
	}

	/**
	 * ListAdatpter
	 */
	public class ListAdatpter extends BaseAdapter{
		private Context context;
		private List<Person> adapterContactList ;
		
		public ListAdatpter(Context context, List<Person> adapterContactList){
			this.context = context;
			this.adapterContactList = adapterContactList;
		}

		@Override
		public int getCount() {
			return adapterContactList.size();
		}

		@Override
		public Object getItem(int position) {
			return adapterContactList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ContactHolder holder = null;
			if(convertView ==null){
				holder = new ContactHolder();
				convertView = View.inflate(context, R.layout.gridview_singleline_demo_item, null);
				holder.iv_person_info_photo =  (ImageView) convertView.findViewById(R.id.iv_person_info_photo);
				holder.tv_person_info_name = (TextView) convertView.findViewById(R.id.tv_person_info_name);
				holder.tv_person_info_number = (TextView) convertView.findViewById(R.id.tv_person_info_number);
				holder.iv_person_checked = (ImageView) convertView.findViewById(R.id.iv_person_checked);
				convertView.setTag(holder);
			}else{
				holder = (ContactHolder) convertView.getTag();
			}
			Person person = adapterContactList.get(position);
			MLog.i("哈哈","adapter position====="+person.toString());
			holder.iv_person_info_photo.setImageBitmap(PersonDataUtil.getBitmapData(context, 0, true));
			holder.tv_person_info_name.setText(person.getName());
			holder.tv_person_info_number.setText(person.getJid());
			if(person.isChecked()){
				holder.iv_person_checked.setImageResource(R.drawable.ic_checkbox_checked);
			}else{
				holder.iv_person_checked.setImageResource(R.drawable.ic_checkbox_unchecked);
			}
			return convertView;
		}
		public class ContactHolder{
			private ImageView iv_person_info_photo;
			private TextView tv_person_info_name;
			private TextView tv_person_info_number;
			private ImageView iv_person_checked;
		}
	}
	
	private void initGridViewData() {
		gridAdapter = new GridViewAdapter(act, selectedList);
		gv_contact_selected_show.setHorizontalSpacing(ImageSpace);
		gv_contact_selected_show.setAdapter(gridAdapter);
		scrollView = (HorizontalScrollView) findViewById(R.id.hsv_gridview_scrollview);
		updateGridViewData();
	}
	
	private void updateGridViewData(){
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) new LinearLayout.LayoutParams((selectedList.size()+1)*(ImageWidth+ImageSpace), LayoutParams.WRAP_CONTENT);
		gv_contact_selected_show.setLayoutParams(params);
		gv_contact_selected_show.setNumColumns((selectedList.size()+1));
		scrollView.fullScroll(View.FOCUS_RIGHT);
	}
	
	private void initOperateGridViewItem(){
		gv_contact_selected_show.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Object obj = parent.getItemAtPosition(position);
				if(obj != null && obj instanceof Person){
					Person person = (Person) obj;
					person.setChecked(false);
					selectedList.remove(person);
					mHandler.sendEmptyMessage(0);
				}
			}
		});
	}

	/**
	 * GridViewAdapter
	 */
	public class GridViewAdapter extends BaseAdapter{
		private Context context;
		private List<Person> adapterSelectedList ;
		private Bitmap emptyBitmap;
		
		public GridViewAdapter(Context context, List<Person> adapterSelectedList){
			this.context = context;
			this.adapterSelectedList = adapterSelectedList;
			emptyBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.select_groupchat_person_empty_pic);
		}

		@Override
		public int getCount() {
			return adapterSelectedList.size()+1;
		}

		@Override
		public Object getItem(int position) {
			if(position < adapterSelectedList.size()){
				return adapterSelectedList.get(position);
			}else{
				return null;
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
	    	ImageView imageView = new ImageView(context);
	    	imageView.setLayoutParams(new GridView.LayoutParams(ImageWidth, ImageWidth));
            imageView.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);
            
            if(position == adapterSelectedList.size()){
            	imageView.setImageBitmap(emptyBitmap);
            }else if (position < adapterSelectedList.size() && adapterSelectedList.size()>0){
            	Person person = adapterSelectedList.get(position);
            	if(person.getPhoto()!=null && person.getPhoto().length>0){
            		Bitmap bitmap = BitmapFactory.decodeByteArray(person.getPhoto(), 0, person.getPhoto().length);
        		    imageView.setImageBitmap(PersonDataUtil.getRoundedCornerBitmap(bitmap, 10, 10));
            	}else{
            		imageView.setImageBitmap(PersonDataUtil.getDefaultBitmap(context, true));
            	}
	    	}
	    	return imageView;
	    }
	}
	
	/**
	 * 刷新页面和数据
	 */
	private void updateUI(){
		listAdapter.notifyDataSetChanged();
		gridAdapter.notifyDataSetChanged();
		updateGridViewData();
		setConfirmButton(selectedList.size());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_select_confirm:
			transformSelectData();
			break;
		}
	}
	private void transformSelectData() {
		if(selectedList.size()>0){
			Intent intent = new Intent(act,PersonListActivity.class);
			intent.putExtra(PersonDataUtil.INTENT_FROM_TYPE, PersonDataUtil.INTENT_FROM_SINGLE_GRIDVIEW);
			
			Bundle bundle = new Bundle();
			bundle.putSerializable(PersonDataUtil.SELECTED_PERSON_LIST, (Serializable) selectedList);
			
			intent.putExtras(bundle);
			startActivity(intent);
		}
		act.finish();
	}
	
	/**
	 * 根据选择人数设置确定按钮属性
	 * @param count
	 */
	public void setConfirmButton(Integer count){
		if(count == 0){
			bt_select_confirm.setBackgroundResource(R.drawable.confirm_add_groupchat_person_normal_empty_pic);
			bt_select_confirm.setEnabled(false);
		}else {
			bt_select_confirm.setBackgroundResource(R.drawable.btn_groupchat_person_confirm_bg);
			bt_select_confirm.setEnabled(true);
		}
		bt_select_confirm.setText("确定"+"("+count+")");
	}
}
