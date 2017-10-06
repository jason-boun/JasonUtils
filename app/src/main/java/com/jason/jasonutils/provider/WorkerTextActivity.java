package com.jason.jasonutils.provider;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.MLog;

/**
 * 通过provider来操作数据库
 */
public class WorkerTextActivity extends Activity implements OnClickListener {
	
	private Button bt_provider_demo_insert, bt_provider_demo_delete, bt_provider_demo_update, bt_provider_demo_query;
	private TextView tv_provider_demo_showinfo;
	private ContentResolver resolver ;
	private ProgressDialog progressDialog;
	private Context ctx;
	private String mAuthrity = WorkerProvider.WORKER_AUTHRITY;
	private int ToastTime = Toast.LENGTH_SHORT;
	private MyWorkerDBObserver dbObserver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.provider_worker_demo_activity);
		init();
	}

	private void init() {
		bt_provider_demo_insert = (Button) findViewById(R.id.bt_provider_demo_insert);
		bt_provider_demo_delete = (Button) findViewById(R.id.bt_provider_demo_delete);
		bt_provider_demo_update = (Button) findViewById(R.id.bt_provider_demo_update);
		bt_provider_demo_query = (Button) findViewById(R.id.bt_provider_demo_query);
		
		tv_provider_demo_showinfo = (TextView) findViewById(R.id.tv_provider_demo_showinfo);
		
		bt_provider_demo_insert.setOnClickListener(this);
		bt_provider_demo_delete.setOnClickListener(this);
		bt_provider_demo_update.setOnClickListener(this);
		bt_provider_demo_query.setOnClickListener(this);
		
		ctx = WorkerTextActivity.this;
		resolver = getContentResolver();
		dbObserver = new MyWorkerDBObserver(handler);
		resolver.registerContentObserver(getUri(mAuthrity, ""), true, dbObserver);//注册数据库内容观察者
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_provider_demo_insert:
			insertDataTask();
			break;
		case R.id.bt_provider_demo_delete:
			if(delete()!=0){
				Toast.makeText(this, "张三15被删除", ToastTime).show();
			}
			break;
		case R.id.bt_provider_demo_update:
			if(update()!=0){
				Toast.makeText(this, "张三17被更新", ToastTime).show();
			}
			break;
		case R.id.bt_provider_demo_query:
			boolean res = showQueryResult();
			if(!res){
				Toast.makeText(this, "没有查询到结果", ToastTime).show();
			}
			break;
		}
	}

	/**
	 * 增
	 */
	private void insert(){
		ContentValues values ;
		Uri uri = getUri(mAuthrity, WorkerProvider.PATH_INSERT);
		for(int i=1;i<31;i++){
			values = new ContentValues();
			values.put("name", "张三"+ i);
			values.put("age", 25+i);
			values.put("salary", 8000+(i*200));
			resolver.insert(uri, values);
		}
	}
	
	/**
	 * 删
	 */
	private int delete(){
		return resolver.delete(getUri(mAuthrity, WorkerProvider.PATH_DELETE), "name = ? ", new String []{"张三15"});
	}
	
	/**
	 * 删除所有
	 */
	private int deleteAll(){
		return resolver.delete(getUri(mAuthrity, WorkerProvider.PATH_DELETE), null, null);
	}
	
	/**
	 * 改
	 */
	private int update(){
		ContentValues values = new ContentValues();
		values.put("age", 39);
		values.put("salary", 20000);
		String where = "name = ? ";
		String []selectionArgs = {"张三17"};
		return resolver.update(getUri(mAuthrity, WorkerProvider.PATH_UPDATE), values, where, selectionArgs);
	}
	
	/**
	 * 查
	 * @return
	 */
	private Cursor query(){
		String []projection = {"name","age","salary"};
		String selection = "name like ?";
		String []selectionArgs = {"%张三1%"};
		String sortOrder = "salary desc";
		return resolver.query(getUri(mAuthrity, WorkerProvider.PATH_QUERY), projection, selection, selectionArgs, sortOrder);
	}
	
	/**
	 * 获取provider的授权地址
	 * @param operation
	 * @return
	 */
	private Uri getUri(String authrity, String operation){
		Uri uri = null;
		if(null!=operation && !TextUtils.isEmpty(operation.trim())){
			uri = Uri.parse("content://" + authrity+"/" + operation);
		}else{
			uri = Uri.parse("content://" + authrity);
		}
		return uri;
	}
	
	/**
	 * 对provider查询返回的cursor进行解析，封装为业务Bean
	 * @return
	 */
	private List<WorkerBean> QueryResult() {
		Cursor cursor = query();
		List<WorkerBean> list = new ArrayList<WorkerBean>();
		if(cursor!=null && cursor.getCount()>0){
			WorkerBean workerBean = null;
			while(cursor.moveToNext()){
				workerBean = new WorkerBean();
				workerBean.setName(cursor.getString(cursor.getColumnIndex("name")));
				workerBean.setAge(cursor.getInt(cursor.getColumnIndex("age")));
				workerBean.setSalary(cursor.getFloat(cursor.getColumnIndex("salary")));
				list.add(workerBean);
			}
		}
		return list;
	}
	
	/**
	 * 展示查询结果
	 */
	private boolean showQueryResult(){
		List<WorkerBean> list = QueryResult();
		if(list.size()>0){
			StringBuffer sb = new StringBuffer();
			for(WorkerBean wb:list){
				sb.append(wb.toString());
				sb.append("\n");
			}
			String result = sb.toString();
			if(result.endsWith("\n")){
				result.substring(0, result.lastIndexOf("\n"));
			}
			tv_provider_demo_showinfo.setText(result);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 异步线程插入数据到数据库
	 */
	private void insertDataTask(){
		new AsyncTask<Void, Void, Void>(){
			@Override
			protected void onPreExecute() {
				progressDialog = ProgressDialog.show(ctx, "", "正在处理...");
				super.onPreExecute();
			}
			@Override
			protected Void doInBackground(Void... params) {
				insert();
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if(progressDialog!=null){
					if(progressDialog.isShowing()){
						progressDialog.dismiss();
					}
					progressDialog = null;
				}
				Toast.makeText(ctx, "操作完毕", ToastTime).show();
			}
		}.execute();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		deleteAll();
		if(handler!=null){
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}
		if(resolver != null){
			if(dbObserver !=null){
				resolver.unregisterContentObserver(dbObserver);
			}
			dbObserver = null;
			resolver = null;
		}
	}
	/**
	 * 内容提供者变化需要注册的观察者
	 */
	private class MyWorkerDBObserver extends ContentObserver{

		private Handler mHandler;
		public MyWorkerDBObserver(Handler handler) {
			super(handler);
			this.mHandler = handler;
		}
		
		@SuppressLint("NewApi")
		@Override
		public void onChange(boolean selfChange, Uri uri) {
			Message msg = mHandler.obtainMessage();
			msg.obj = uri;
			msg.sendToTarget();
			super.onChange(selfChange, uri);
		}
	}

	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg !=null && msg.obj instanceof Uri){
				Uri uri = (Uri) msg.obj;
				int code = (int) ContentUris.parseId(uri);
				switch (code) {
				case WorkerProvider.CODE_INSERT:
					MLog.i("哈哈", "数据库新增了数据，你现在可以刷数据库数据了：Uri == " + uri.toString());
					break;
				case WorkerProvider.CODE_UPDATE:
					MLog.i("哈哈", "数据库数据更新了，你现在可以刷数据库数据了：Uri == " + uri.toString());
					break;
				case WorkerProvider.CODE_DELETE:
					MLog.i("哈哈", "数据库删除了数据，你现在可以刷数据库数据了：Uri == " + uri.toString());
					break;
				default:
					break;
				}
			}
		};
	};
}
