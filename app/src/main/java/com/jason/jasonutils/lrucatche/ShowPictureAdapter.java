package com.jason.jasonutils.lrucatche;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.jason.jasonutils.R;

public class ShowPictureAdapter extends ArrayAdapter<String> {

	public ShowPictureAdapter(Context context, int textViewResourceId, String[] objects, GridView gridView) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.lrucache_demo_photo_item, null);
			holder = new ViewHolder();
			holder.photo = (ImageView) convertView.findViewById(R.id.photo);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		final String url = getItem(position);
		holder.photo.setTag(url);// 给ImageView设置一个Tag，保证异步加载图片时不会乱序
		Bitmap bitmap = MyLruCache.getInstance().getFromCache(url);
		if (bitmap != null) {
			holder.photo.setImageBitmap(bitmap);
		} else {
			holder.photo.setImageResource(R.drawable.app_tool_6);
		}
		return convertView;
	}
	
	public static class ViewHolder {
		ImageView photo;
	}

}