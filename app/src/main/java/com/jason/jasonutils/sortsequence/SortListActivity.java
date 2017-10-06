package com.jason.jasonutils.sortsequence;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import com.jason.jasonutils.tools.MLog;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.R;

public class SortListActivity extends Activity implements OnClickListener  {
	
	private EditText et_input_name, et_input_number;
	private Button bt_enter_data;
	private TextView tv_sort_list_title;
	private ListView lv_show_name_list;
	private String[] nameArray = new String[]{"张三","李四","王五","赵六","周七","孙八","李明","Jason","Bob","James","123"};
	private Long[] numberArray =new Long[]{13866123l,12348456l,8894564l,8461236l,5417842l,1445679l,65789684l,18641235l,3694785641l,54645687l,3578468l};
	private List<SortBean> beanList = new ArrayList<SortBean>();
	private SortListAdapter sortAdapter;
	private MySortComparator<SortBean> mComparator = new MySortComparator<SortBean>();
	private ZmBar alpha;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sort_list_activity);
		
		init();
		initData();
		initAdapter();
	}

	private void init() {
		 et_input_name = (EditText) findViewById(R.id.et_input_name);
		 et_input_number = (EditText) findViewById(R.id.et_input_number);
		 tv_sort_list_title = (TextView) findViewById(R.id.tv_sort_list_title);
		 bt_enter_data = (Button) findViewById(R.id.bt_enter_data);
		 lv_show_name_list = (ListView) findViewById(R.id.lv_show_name_list);
		 alpha = (ZmBar) findViewById(R.id.sort_list_fast_scroller);
		 
		 lv_show_name_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				MLog.i("哈哈", "position=="+position+"被点击");
				Toast.makeText(SortListActivity.this, "position=="+position+"被点击", Toast.LENGTH_SHORT).show();
			}
		});
		 
		 lv_show_name_list.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_UP:
					if(lv_show_name_list.getFirstVisiblePosition()!=0){
						tv_sort_list_title.setVisibility(View.GONE);
					}else{
						tv_sort_list_title.setVisibility(View.VISIBLE);
					}
					break;
				}
				return false;//如果返回true，则点击事件无法处理；如果为false，则处理完触摸事件后继续向下传递
			}
		});
		bt_enter_data.setOnClickListener(this);
	}

	private void initData() {
		SortBean sortBean = null;
		for(int i=0;i<nameArray.length;i++){
			sortBean = new SortBean();
			sortBean.setName(nameArray[i]);
			sortBean.setNumber(numberArray[i]);
			sortBean.setPinYin(PinYinUtil.getPingYin(nameArray[i]));//获取name对应的拼音
			sortBean.setPhoto(getPhotoData());
			beanList.add(sortBean);
		}
	}
	
	private void initAdapter() {
		Collections.sort(beanList, mComparator);
		sortAdapter = new SortListAdapter(this, beanList, alpha);
		lv_show_name_list.setAdapter(sortAdapter);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_enter_data:
			String enter_name = et_input_name.getText().toString().trim();
			String enter_number = et_input_number.getText().toString().trim();
			if(!TextUtils.isEmpty(enter_name) && !TextUtils.isEmpty(enter_number)){
				beanList.add(new SortBean(enter_name, Long.parseLong(enter_number), PinYinUtil.getPingYin(enter_name), getPhotoData()));
				refreshUI();
			}else{
				Toast.makeText(this, "姓名和号码不能为空", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	/**
	 * 刷新数据和页面
	 */
	public void refreshUI(){
		et_input_name.setText("");
		et_input_number.setText("");
		Collections.sort(beanList, mComparator);
		sortAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 自定义比较器，通过首字母比较排序
	 */
	public class MySortComparator <T> implements Comparator<T>{
		@Override
		public int compare(T lhs, T rhs) {
			//return Collator.getInstance(java.util.Locale.CHINA).compare(((SortBean)lhs).getPinYin(),((SortBean)rhs).getPinYin());
			return ((SortBean)lhs).getPinYin().compareToIgnoreCase(((SortBean)rhs).getPinYin());
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus){
			initAlhpa();
		}
	}
	
	/**
	 * 初始化alpha控件需要传递的参数
	 */
	public void initAlhpa(){
		alpha.init(SortListActivity.this);
		alpha.setVisibility(View.VISIBLE);
		alpha.setListView(lv_show_name_list);
		alpha.setHight(alpha.getHeight());
	}
	
	/**
	 * 获取头像数据：byte[]
	 * @return
	 */
	public byte[] getPhotoData(){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BitmapFactory.decodeResource(getResources(), R.drawable.contact_photo).compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	 }

}
