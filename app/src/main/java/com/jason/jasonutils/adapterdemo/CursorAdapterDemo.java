package com.jason.jasonutils.adapterdemo;

import java.io.ByteArrayInputStream;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jason.jasonutils.R;

public class CursorAdapterDemo extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Uri uri = Uri.parse("content://com.jason.myprovider/query");
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);//关键点
		setListAdapter(new MyCursorAdapter(this, cursor));
	}

	public class MyCursorAdapter extends CursorAdapter{
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
}
