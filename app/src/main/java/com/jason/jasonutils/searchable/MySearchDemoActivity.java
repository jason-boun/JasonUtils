package com.jason.jasonutils.searchable;

import java.io.ByteArrayInputStream;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jason.jasonutils.R;
import com.jason.jasonutils.handler.AsyncQueryHandlerDAO;
import com.jason.jasonutils.handler.AsyncQueryHandlerDemoDBHelper;

public class MySearchDemoActivity extends ListActivity {
	
	private ListView mListView;
	private MyCursorAdapter mCursorAdapter;
	//private AsyncQueryHandlerDemoDBHelper myDBHelper;
	private AsyncQueryHandlerDAO dao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//myDBHelper = new AsyncQueryHandlerDemoDBHelper(this);
		dao = new AsyncQueryHandlerDAO(new AsyncQueryHandlerDemoDBHelper(this));
		
		mListView = getListView();
		mCursorAdapter = new MyCursorAdapter(this, null);
		mListView.setAdapter(mCursorAdapter);
		
		onSearchRequested();
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		handleIntent(intent);
		
	}
	private void handleIntent(Intent intent) {
		if(null!= intent && Intent.ACTION_SEARCH.equals(intent.getAction()) ){
			doMySearch(intent.getStringExtra(SearchManager.QUERY));
		}
	}

	/**
	 * 通过传递的关键字进行搜索，并展示结果
	 * @param query
	 */
	private void doMySearch(String query) {
		mCursorAdapter.changeCursor(dao.queryDataByName(query));
		initTitle();
	}
	
	/**
	 * 数据绑定Adapter
	 */
	public class MyCursorAdapter extends CursorAdapter{
		@SuppressWarnings("deprecation")
		public MyCursorAdapter(Context context, Cursor c) {
			super(context, c);
		}
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return LayoutInflater.from(context).inflate(R.layout.sort_list_item, null);
		}
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			ImageView photo = (ImageView) view.findViewById(R.id.iv_item_photo);
			TextView name = (TextView) view.findViewById(R.id.tv_item_name);
			TextView number = (TextView) view.findViewById(R.id.tv_item_number);
			
			byte[] temPhoto = cursor.getBlob(cursor. getColumnIndex("photo"));
			if(null!=temPhoto && temPhoto.length>0){
				photo.setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(temPhoto)));
			}else{
				photo.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.contact_photo));
			}
			name.setText(cursor.getString(cursor.getColumnIndex("name")));
			number.setText(cursor.getString(cursor.getColumnIndex("number")));
		}
	}
	
	public void initTitle() {
		setTitle("搜索到了" + mCursorAdapter.getCount() + "条匹配记录");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Cursor cursor = mCursorAdapter.getCursor();
		if(cursor != null && !cursor.isClosed()){
			cursor.close();
		}
	}
}
