package com.jason.jasonutils.provider;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;
import com.jason.jasonutils.tools.MLog;

public class WorkerTestCase extends AndroidTestCase {
	
	private WorkerDAO workerDao;

	/**
	 * 测试框架初始化数据
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		workerDao = new WorkerDAO(getContext());
	}

	/**
	 * 测试框架释放数据
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		workerDao.releaseDB();
	}
	
	/**
	 * 增
	 */
	public void insertTest(){
		ContentValues values = new ContentValues();
		values.put("name", "wangwu");
		values.put("age", 26);
		values.put("salary", 16000);
		boolean insert = workerDao.insertWorker(values);
		assertEquals(true, insert);
	}
	
	/**
	 * 删
	 */
	public void deleteTest(){
		boolean delete = workerDao.deleteWorker("name = ? ", new String[]{"wangwu"});
		assertEquals(true, delete);
	}
	
	/**
	 * 改
	 */
	public void updateTest(){
		ContentValues values = new ContentValues();
		values.put("age", 30);
		values.put("salary", 18000);
		boolean update = workerDao.updateWorker(values, "name = ? ", new String[]{"wangwu"});
		assertEquals(true, update);
	}
	
	/**
	 * 查
	 * @return
	 */
	public List<WorkerBean> queryTest(){
		List<WorkerBean> workerList = new ArrayList<WorkerBean>();
		WorkerBean worker = null;
		Cursor cursor = workerDao.queryWorker(new String[]{"name","age","salary"}, null, null, "salary desc");
		if(cursor!=null){
			while(cursor.moveToNext()){
				worker = new WorkerBean();
				worker.setName(cursor.getString(cursor.getColumnIndex("name")));
				worker.setAge(cursor.getInt(cursor.getColumnIndex("age")));
				worker.setSalary(cursor.getFloat(cursor.getColumnIndex("salary")));
				workerList.add(worker);
				if(worker!=null){
					MLog.i("哈哈", worker.toString());
				}
			}
		}
		assertEquals(true, worker!=null);
		return workerList;
	}

}
