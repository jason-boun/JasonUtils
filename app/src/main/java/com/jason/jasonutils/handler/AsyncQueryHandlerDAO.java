package com.jason.jasonutils.handler;

import java.util.ArrayList;
import android.database.Cursor;
import com.jason.jasonutils.sortsequence.SortBean;

public class AsyncQueryHandlerDAO {
	
	private AsyncQueryHandlerDemoDBHelper helper;
	
	public AsyncQueryHandlerDAO(AsyncQueryHandlerDemoDBHelper helper){
		this.helper = helper;
	}
	
	/**
	 * 插入数据
	 */
	public void insertBeanData(SortBean sb){
		if(null!=sb){
			String sql = "insert into "+AsyncQueryHandlerDemoDBHelper.TABLE_NAME+" (name, number, pinyin, photo) values (?, ?, ?, ?)";
			helper.getWritableDatabase().execSQL(sql, new Object[]{sb.getName(),sb.getNumber(), sb.getPinYin(),sb.getPhoto()});
		}
	}
	
	/**
	 * 查询所有数据
	 */
	public ArrayList<SortBean> queryAllBeanData(){
		String sql = "select * from "+ AsyncQueryHandlerDemoDBHelper.TABLE_NAME ;
		Cursor cursor = helper.getReadableDatabase().rawQuery(sql, null);
		if(null!= cursor && cursor.getCount()>0){
			ArrayList<SortBean> resultList = new ArrayList<SortBean>();
			SortBean sbFromDB ;
			while(cursor.moveToNext()){
				sbFromDB = new SortBean();
				sbFromDB.setPhoto(cursor.getBlob(cursor.getColumnIndex("photo")));
				sbFromDB.setName(cursor.getString(cursor.getColumnIndex("name")));
				sbFromDB.setNumber(Long.parseLong(cursor.getString(cursor.getColumnIndex("number"))));
				sbFromDB.setPinYin(cursor.getString(cursor.getColumnIndex("pinyin")));
				resultList.add(sbFromDB);
			}
			return resultList;
		}
		return null;
	}
	
	/**
	 * 判断数据库中是否有数据
	 * @return
	 */
	public boolean DbHasData(){
		Cursor cursor = helper.getReadableDatabase().rawQuery("select * from "+ AsyncQueryHandlerDemoDBHelper.TABLE_NAME , null);
		if(null!=cursor && cursor.getCount()>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 根据名字匹配，模糊查询
	 * @param name
	 * @return
	 */
	public Cursor queryDataByName(String name){
		String sql = "select * from " + AsyncQueryHandlerDemoDBHelper.TABLE_NAME + " where name like '"+ name +"%'";
		return helper.getReadableDatabase().rawQuery(sql, null);
	}

}
