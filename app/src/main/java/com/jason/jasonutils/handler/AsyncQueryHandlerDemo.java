package com.jason.jasonutils.handler;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import com.jason.jasonutils.tools.MLog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.jason.jasonutils.R;
import com.jason.jasonutils.sortsequence.PinYinUtil;
import com.jason.jasonutils.sortsequence.SortBean;

public class AsyncQueryHandlerDemo extends Activity implements OnClickListener {
	
	private Button bt_generate_data, bt_query_data;
	private ListView lv_show_data;
	private List<SortBean> beanList = new ArrayList<SortBean>();//最终展示数据的封装List
	private List<SortBean> packageBeanList = new ArrayList<SortBean>();//如果数据库为空，则封装数据，存入数据库
	private MySortComparator<SortBean> myComparator = new MySortComparator<SortBean>();
	private AsyncQueryListViewAdapter queryAdapter;
	private MyAsyncQueryHandler myAsyncQueryHandler;
	//private AsyncQueryHandlerDemoDBHelper mySqliteOpenHelpler;
	private AsyncQueryHandlerDAO dao;
	private boolean memoryHasData = false;
	
	private static final int QUERY_FROM_DATABASEHELPER = 0;//直接操作数据库
	private static final int QUERY_FROM_CONTENTPROVIDER = 1;//通过contentProvider来操作数据
	private static final int QUERY_FROM_ASYNCHANDLER = 2;//通过异步查询框架来操作数据
	private int queryMode = QUERY_FROM_ASYNCHANDLER;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.asyncqueryhandler_demo_activity);
		
		init();
		initData();
	}

	private void init() {
		bt_generate_data = (Button) findViewById(R.id.bt_generate_asyncqueryhandler_data);
		bt_query_data = (Button) findViewById(R.id.bt_query_asyncqueryhandler_data);
		
		lv_show_data = (ListView) findViewById(R.id.lv_show_asyncqueryhandler_data_list);
		
		bt_generate_data.setOnClickListener(this);
		bt_query_data.setOnClickListener(this);
	}

	private void initData() {
		if(beanList.size()>1){
			Collections.sort(beanList, myComparator);
		}
		queryAdapter = new AsyncQueryListViewAdapter(this, beanList);
		lv_show_data.setAdapter(queryAdapter);
		
		//创建数据库表格
		//mySqliteOpenHelpler = new AsyncQueryHandlerDemoDBHelper(this);
		dao = new AsyncQueryHandlerDAO(new AsyncQueryHandlerDemoDBHelper(this));
		//创建异步查询实例，要传递实例——ContentProvider的client对象：ContentResolver
		myAsyncQueryHandler = new MyAsyncQueryHandler(this.getContentResolver());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_generate_asyncqueryhandler_data:
			switch (queryMode) {
			case QUERY_FROM_DATABASEHELPER:
				generateData();
				break;
			case QUERY_FROM_CONTENTPROVIDER:
				generateDataFromContentProvider();
				break;
			case QUERY_FROM_ASYNCHANDLER:
				generateDataFromMyAsyncHandler();
				break;
			}
			break;
		case R.id.bt_query_asyncqueryhandler_data:
			switch (queryMode) {
			case QUERY_FROM_DATABASEHELPER:
				queryData();
				break;
			case QUERY_FROM_CONTENTPROVIDER:
				queryDataFromContentProvider();
				break;
			case QUERY_FROM_ASYNCHANDLER:
				queryDataFromMyAsyncHandler();
				break;
			}
			break;
		}
	}

	/********************************************** 直接通过操作数据来获取数据：AsyncQueryHandlerDemoOpenHelper *****************************/

	/**
	 * 生成数据，插入数据库
	 */
	private void generateData() {
		if(dao.DbHasData()){
			Toast.makeText(this, "数据库中已有数据", Toast.LENGTH_SHORT).show();
		}else{
			if(!memoryHasData){
				packageBeanData();
			}
			if(packageBeanList.size()>0){
				for(SortBean sb : packageBeanList){
					dao.insertBeanData(sb);//关键点，直接操作数据库
				}
				Toast.makeText(this, "数据准备完毕", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 从数据库中查询
	 */
	private void queryData() {
		if(dao.DbHasData()){
			ArrayList<SortBean> resultData = dao.queryAllBeanData();
			if(null!=resultData && resultData.size()>0){
				beanList.clear();
				beanList.addAll(resultData);
				Collections.sort(beanList, myComparator);
				queryAdapter.notifyDataSetChanged();
			}
		}
	}
	
	/********************************************** 通过ContentProvider来操作数据库 ********************************************************/

	/**
	 * 通过内容提供者来对数据库插入数据
	 */
	private void generateDataFromContentProvider(){
		if(dao.DbHasData()){
			Toast.makeText(this, "数据库中已有数据", Toast.LENGTH_SHORT).show();
		}else{
			if(!memoryHasData){
				packageBeanData();
			}
			if(packageBeanList.size()>0){
				Uri uri = Uri.parse("content://com.jason.myprovider/insert");
				ContentValues cv;
				for(SortBean sb : packageBeanList){
					cv = new ContentValues();
					cv.put("photo", sb.getPhoto());
					cv.put("name", sb.getName());
					cv.put("number", sb.getNumber());
					cv.put("pinyin", sb.getPinYin());
					getContentResolver().insert(uri, cv);//关键点，通过ContentResolver的客户端ContentResolver来操作数据库
				}
				Toast.makeText(this, "数据准备完毕", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	/**
	 * 通过内容提供者查询数据
	 */
	private void queryDataFromContentProvider(){
		Uri uri = Uri.parse("content://com.jason.myprovider/query");
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);//关键点，通过ContentResolver的客户端ContentResolver来操作数据库
		if(null!= cursor && cursor.getCount()>0){
			beanList.clear();
			SortBean sbFromDB ;
			while(cursor.moveToNext()){
				sbFromDB = new SortBean();
				sbFromDB.setPhoto(cursor.getBlob(cursor.getColumnIndex("photo")));
				sbFromDB.setName(cursor.getString(cursor.getColumnIndex("name")));
				sbFromDB.setNumber(Long.parseLong(cursor.getString(cursor.getColumnIndex("number"))));
				sbFromDB.setPinYin(cursor.getString(cursor.getColumnIndex("pinyin")));
				beanList.add(sbFromDB);
			}
			Collections.sort(beanList, myComparator);
			queryAdapter.notifyDataSetChanged();
		}else{
			Toast.makeText(this, "数据库中没有数据，请先生成数据", Toast.LENGTH_SHORT).show();
		}
	}
	
	/********************************************** 异步查询框架：AsyncQueryHandler ********************************************************/
	
	private void generateDataFromMyAsyncHandler(){
		if(dao.DbHasData()){
			Toast.makeText(this, "数据库中已有数据", Toast.LENGTH_SHORT).show();
		}else{
			if(!memoryHasData){
				packageBeanData();
			}
			if(packageBeanList.size()>0){
				Uri uri = Uri.parse("content://com.jason.myprovider/insert");
				ContentValues cv;
				for(SortBean sb : packageBeanList){
					cv = new ContentValues();
					cv.put("photo", sb.getPhoto());
					cv.put("name", sb.getName());
					cv.put("number", sb.getNumber());
					cv.put("pinyin", sb.getPinYin());
					myAsyncQueryHandler.startInsert(0, null, uri, cv);//关键点
				}
				Toast.makeText(this, "数据准备完毕", Toast.LENGTH_SHORT).show();
			}
		
		}
	}
	
	private void queryDataFromMyAsyncHandler(){
		Uri uri = Uri.parse("content://com.jason.myprovider/query");
		myAsyncQueryHandler.startQuery(0, null, uri, null, null, null, null);//关键点
	}
	
	/**
	 * 继承自AsyncQueryHandler类的自定义异步查询类。
	 */
	public class MyAsyncQueryHandler extends AsyncQueryHandler{
		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}
		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			super.onQueryComplete(token, cookie, cursor);
			if(cursor!=null && cursor.getCount()>0){//查询到数据之后，关键点：cursor
				SortBean sbFromDB ;
				beanList.clear();
				while(cursor.moveToNext()){
					sbFromDB = new SortBean();
					sbFromDB.setPhoto(cursor.getBlob(cursor.getColumnIndex("photo")));
					sbFromDB.setName(cursor.getString(cursor.getColumnIndex("name")));
					sbFromDB.setNumber(Long.parseLong(cursor.getString(cursor.getColumnIndex("number"))));
					sbFromDB.setPinYin(cursor.getString(cursor.getColumnIndex("pinyin")));
					beanList.add(sbFromDB);
				}
				MLog.i("哈哈", beanList.size()+"查询出来的数据");
				Collections.sort(beanList, myComparator);
				queryAdapter.notifyDataSetChanged();
			}
		}
		@Override
		protected void onInsertComplete(int token, Object cookie, Uri uri) {
			super.onInsertComplete(token, cookie, uri);
			MLog.i("哈哈", "往数据库中插入一条数据");
		}
		
		
	}

	/********************************************** 封装数据，比较排序：MySortComparator ********************************************************/
	
	/**
	 * 封装数据
	 */
	private void packageBeanData() {
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
		memoryHasData = true;
	}
	
	/**
	 * 比较器，通过首字母比较排序
	 */
	public class MySortComparator <T> implements Comparator<T>{
		@Override
		public int compare(T lhs, T rhs) {
			return Collator.getInstance(java.util.Locale.CHINA).compare(((SortBean)lhs).getPinYin(),((SortBean)rhs).getPinYin());
		}
	}
}
