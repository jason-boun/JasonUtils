package com.jason.jasonutils.dbframeworktool.dbtest;

import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;

import com.jason.jasonutils.dbframeworktool.bean.News;
import com.jason.jasonutils.dbframeworktool.daoimpl.NewsDaoImpl;

public class DBTest extends AndroidTestCase {
	private static final String TAG = "DBTest";

	public void getNewsDaoImpl() {
		NewsDaoImpl impl=new NewsDaoImpl(getContext());
		impl.getInstance();
	}
	
	public void testInsert() {
		NewsDaoImpl impl=new NewsDaoImpl(getContext());
		News m=new News();
		m.setSummary("测试摘要一");
		m.setTitle("测试标题一");
		impl.insert(m);
	}
	
	public void testDelete() {
		NewsDaoImpl impl=new NewsDaoImpl(getContext());
		impl.delete(0);
	}
	
	public void testUpdate() {
		NewsDaoImpl impl=new NewsDaoImpl(getContext());
		
		News m=new News();
		m.setId(1);
		m.setSummary("测试摘要一");
		m.setTitle("测试标题一_修改");
		
		impl.update(m);
	}
	
	public void testFindAll() {
		NewsDaoImpl impl=new NewsDaoImpl(getContext());
		List<News> findAll = impl.findAll();
		
		Log.i(TAG, "size:"+findAll.size());
	}
	
}
