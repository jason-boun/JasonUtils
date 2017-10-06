package com.jason.jasonutils.lrucatche;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.jason.jasonutils.R;

/**
 * 用给定的RUL通过异步任务从网络获取图片，
 * 将这些图片展示到照片墙上；同时将下载的图片通过缓存技术存储到内存中
 */
public class ShowPcitureActivity extends Activity {
	
	private GridView mGridView;
	private ShowPictureAdapter mAdapter;
	private MyScrollListener mScrollListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lrucache_demo_activity);
		
		mGridView = (GridView) findViewById(R.id.photo_wall);
		mAdapter = new ShowPictureAdapter(this, 0, PictureUrlArray.imageThumbUrls, mGridView);
		mScrollListener = new MyScrollListener(mGridView);
		
		mGridView.setOnScrollListener(mScrollListener);
		mGridView.setAdapter(mAdapter);
	}

	@Override
	protected void onStop() {
		super.onDestroy();
		if(mScrollListener!=null){
			mScrollListener.cancelAllTasks();
		}
	}

}
