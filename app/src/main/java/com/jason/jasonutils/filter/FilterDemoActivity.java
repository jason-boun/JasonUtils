package com.jason.jasonutils.filter;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.jason.jasonutils.R;

public class FilterDemoActivity extends Activity {
	
	private EditText et_search;
	private ListView lv_show_list;
	private FilterDemoListAdapter mAdapter;
	private ArrayList<PersonBean> fromList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_demo_activity);
		
		init();
		initData();
	}

	private void init() {
		et_search = (EditText) findViewById(R.id.et_search);
		lv_show_list = (ListView) findViewById(R.id.lv_show_list);
		
		et_search.addTextChangedListener(searchWatcher);
	}

	private void initData() {
		if(fromList==null){
			fromList = new ArrayList<PersonBean>();
		}
		fromList.addAll(generateData());
		mAdapter = new FilterDemoListAdapter(this, fromList);
		lv_show_list.setAdapter(mAdapter);
	}
	
	private TextWatcher searchWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			mAdapter.getmFilter().filter(s);
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		@Override
		public void afterTextChanged(Editable s) {
		}
	}; 
	
	/**
	 * 产生模拟测试数据
	 */
	private ArrayList<PersonBean> generateData(){
		ArrayList<PersonBean> oriList = new ArrayList<PersonBean>();
		PersonBean personBean = null;
		Random random = new Random();
		for(int i=0;i<30;i++){
			personBean = new PersonBean();
			personBean.setName(getRandomStr(5));
			personBean.setAge(random.nextInt(35));
			oriList.add(personBean);
		}
		return oriList;
	}
	
	/**
	 * 获取随机字符串
	 * @param len 需要获取的长度
	 * @return [a-z][A-Z][0-9]
	 */
	public String getRandomStr(int len){
		int BaseLen = base.length();
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for(int i=0;i<len;i++){
			sb.append(base.charAt(random.nextInt(BaseLen)));
		}
		return sb.toString();
	}
	static String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
}
