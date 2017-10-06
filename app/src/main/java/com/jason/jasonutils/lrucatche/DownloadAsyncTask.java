package com.jason.jasonutils.lrucatche;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.GridView;
import android.widget.ImageView;

public class DownloadAsyncTask extends AsyncTask<Void, Void, Bitmap> {

	private String imageUrl;
	private GridView gridView;
	private HashSet<DownloadAsyncTask> taskSet;
	
	public DownloadAsyncTask(String imageUrl, GridView gridView,HashSet<DownloadAsyncTask> taskSet){
		this.imageUrl = imageUrl;
		this.gridView = gridView;
		this.taskSet = taskSet;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		taskSet.add(this);//将下载任务加入任务队列
	}
	
	@Override
	protected Bitmap doInBackground(Void... params) {
		if(!TextUtils.isEmpty(imageUrl)){
			return downloadBitmap(imageUrl);
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		if(result!=null){
			ImageView photoView = (ImageView)gridView.findViewWithTag(imageUrl);
			if(photoView!=null){
				photoView.setImageBitmap(result);
			}
		}
		MyLruCache.getInstance().addToCache(imageUrl, result);//将下载好的图片与对应的RUL以键值对的形式存储到缓存中
		taskSet.remove(this);//将下载任务移出任务队列
	}
	
	/**
	 * 建立HTTP请求，并获取Bitmap对象。
	 * @param imageUrl
	 * @return
	 */
	private Bitmap downloadBitmap(String imageUrl) {
		Bitmap bitmap = null;
		HttpURLConnection con = null;
		try {
			URL url = new URL(imageUrl);
			con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(5 * 1000);
			con.setReadTimeout(10 * 1000);
			con.setDoInput(true);
			con.setDoOutput(true);
			bitmap = BitmapFactory.decodeStream(con.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return bitmap;
	}

}
