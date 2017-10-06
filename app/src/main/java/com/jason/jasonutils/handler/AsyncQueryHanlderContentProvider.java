package com.jason.jasonutils.handler;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class AsyncQueryHanlderContentProvider extends ContentProvider {
	
	private static final int QUERY = 1;
	private static final int INSERT = 2;
	private static final int DELETE = 3;
	private static final int UPDATE = 4;
	public static final String MY_ASYNCQUERY_AUTHORITY = "com.jason.myprovider";
	
	private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {//绑定操作类型
		uriMatcher.addURI(MY_ASYNCQUERY_AUTHORITY, "query", QUERY);
		uriMatcher.addURI(MY_ASYNCQUERY_AUTHORITY, "insert", INSERT);
		uriMatcher.addURI(MY_ASYNCQUERY_AUTHORITY, "delete", DELETE);
		uriMatcher.addURI(MY_ASYNCQUERY_AUTHORITY, "update", UPDATE);
	}
	
	private SQLiteDatabase db;
	
	@Override
	public boolean onCreate() {
		db = new AsyncQueryHandlerDemoDBHelper(getContext()).getWritableDatabase();
		return false;
	}
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if(uriMatcher.match(uri)==INSERT){
			db.insert(AsyncQueryHandlerDemoDBHelper.TABLE_NAME, null, values);
		}
		return null;
	}
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		if(uriMatcher.match(uri)==QUERY){
			return db.rawQuery("select * from "+AsyncQueryHandlerDemoDBHelper.TABLE_NAME, null);
		}
		return null;
	}
	@Override
	public String getType(Uri uri) {
		return null;
	}


	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}

}
