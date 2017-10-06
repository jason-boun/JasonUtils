package com.jason.jasonutils.lrucatche;

import java.util.HashSet;

import android.graphics.Bitmap;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.ImageView;

public class MyScrollListener implements OnScrollListener {

	private HashSet<DownloadAsyncTask> taskSet;//记录所需下载的任务。
	private boolean isFirstEnter = true;//这个标识变量很重要，初次进入进入程序因为屏幕不会滚动，通过该变量来判断是否下载图片。
	private int mFirstVisibleItem;//第一张可见图片的下标
	private int mVisibleItemCount;//一屏有多少张图片可见
	
	private GridView mGridView;
	
	public MyScrollListener(GridView mGridView){
		this.mGridView = mGridView;
		taskSet = new HashSet<DownloadAsyncTask>();
	}
	
	/**
	 * 初次进入界面后的下载操作，首次进入程序时onScrollStateChanged并不会调用，在这里为首次进入程序开启下载任务。
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		mFirstVisibleItem = firstVisibleItem;
		mVisibleItemCount = visibleItemCount;
		// 下载的任务应该由onScrollStateChanged里调用，但
		if (isFirstEnter && visibleItemCount > 0) {
			loadBitmaps(firstVisibleItem, visibleItemCount);
			isFirstEnter = false;
		}
	}
	
	/**
	 * 仅当GridView静止时才去下载图片，GridView滑动时取消所有正在下载的任务
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			loadBitmaps(mFirstVisibleItem, mVisibleItemCount);
		} else {
			cancelAllTasks();
		}
	}
	
	/**
	 * 加载Bitmap对象，发现任何一个ImageView的Bitmap对象不在缓存中，就会开启异步线程去下载图片。
	 * @param firstVisibleItem：第一个可见ImageView的下标
	 * @param visibleItemCount：屏幕中总共可见的图标数
	 */
	private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
		try {
			for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
				String imageUrl = PictureUrlArray.imageThumbUrls[i];
				Bitmap bitmap = MyLruCache.getInstance().getFromCache(imageUrl);
				if(bitmap!=null){
					ImageView imageView = (ImageView) mGridView.findViewWithTag(imageUrl);
					if (imageView != null) {
						imageView.setImageBitmap(bitmap);
					}
				}else{
					new DownloadAsyncTask(imageUrl, mGridView,taskSet).execute();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取消所有正在下载或等待下载的任务。
	 */
	public void cancelAllTasks() {
		if (taskSet != null) {
			for (DownloadAsyncTask task : taskSet) {
				task.cancel(false);
			}
		}
	}

}
