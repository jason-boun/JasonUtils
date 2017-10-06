package com.jason.jasonutils.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * 为第三方应用访问worker数据库表格提供访问接口
 */
public class WorkerProvider extends ContentProvider {
	
	public static String WORKER_AUTHRITY = "com.jason.jasonutils.workerprovider";
	
	public static final String PATH_INSERT = "insert";
	public static final String PATH_UPDATE = "update";
	public static final String PATH_QUERY = "query";
	public static final String PATH_DELETE = "delete";
	
	public static final int CODE_INSERT = 10;
	public static final int CODE_UPDATE = 20;
	public static final int CODE_QUERY = 30;
	public static final int CODE_DELETE = 40;
	
	private String WORK_TABLE = WorkerSQLiteOpenHelper.TABLE_NAME;

	private SQLiteDatabase db;
	private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		matcher.addURI(WORKER_AUTHRITY, PATH_INSERT, CODE_INSERT);
		matcher.addURI(WORKER_AUTHRITY, PATH_UPDATE, CODE_UPDATE);
		matcher.addURI(WORKER_AUTHRITY, PATH_QUERY, CODE_QUERY);
		matcher.addURI(WORKER_AUTHRITY, PATH_DELETE, CODE_DELETE);
	}

	@Override
	public boolean onCreate() {
		db = new WorkerSQLiteOpenHelper(getContext()).getWritableDatabase();
		return db==null? false:true;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if(matcher.match(uri)==CODE_INSERT){
			db.insert(WORK_TABLE, null, values);
			dbChangedNotify(ContentUris.withAppendedId(uri, CODE_INSERT));
		}else{
			throw new IllegalArgumentException("传入的uri路径有误");
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if(matcher.match(uri)==CODE_DELETE){
			int delete = db.delete(WORK_TABLE, selection, selectionArgs);
			if(delete!=0){
				dbChangedNotify(ContentUris.withAppendedId(uri, CODE_DELETE));
			}
			return delete;
		}else{
			return 0;
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		if(matcher.match(uri)==CODE_UPDATE){
			int update = db.update(WORK_TABLE, values, selection, selectionArgs);
			if(update!=0){
				dbChangedNotify(ContentUris.withAppendedId(uri, CODE_UPDATE));
			}
			return update;
		}
		return 0;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		//SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		if(matcher.match(uri)==CODE_QUERY){
			return db.query(WORK_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
		}
		return null;
	}
	
	/**
	 * 数据库变化通知
	 */
	private void dbChangedNotify(Uri uri){
		getContext().getContentResolver().notifyChange(uri, null);//第二个参数如为null，则只要注册在该URI上的监听者都可以收到通知。
	}

}
