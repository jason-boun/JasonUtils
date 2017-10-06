package com.jason.jasonutils.refresh;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;

/**
 * 正在刷新的任务，在此任务中会去回调注册进来的下拉刷新监听器。
 * @author bo.jia
 */
public class RefreshingTask extends AsyncTask<Void, Integer, Void>{
	/**
	 * 自定义布局View对象实例
	 */
	private RefreshableView bootView;
	
	/**
	 * 下拉头的View
	 */
	private View header;
	
	/**
	 * 下拉头的布局参数
	 */
	private MarginLayoutParams headerLayoutParams;
	
	/**
	 * 下拉刷新的回调接口
	 */
	private PullToRefreshListener mListener;
	
	public RefreshingTask(RefreshableView bootView, View header, MarginLayoutParams headerLayoutParams, PullToRefreshListener mListener){
		this.bootView = bootView;
		this.header = header;
		this.headerLayoutParams = headerLayoutParams;
		this.mListener = mListener;
	}

	@Override
	protected Void doInBackground(Void... params) {
		int topMargin = headerLayoutParams.topMargin;
		while (true) {
			topMargin = topMargin + RefreshViewConstants.SCROLL_SPEED;
			if (topMargin <= 0) {
				topMargin = 0;
				break;
			}
			publishProgress(topMargin);
			SystemClock.sleep(10);
		}
		bootView.currentStatus = HeaderStatus.REFRESHING;
		publishProgress(0);
		if (mListener != null) {
			mListener.onRefresh();
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... topMargin) {
		bootView.updateHeaderView();
		headerLayoutParams.topMargin = topMargin[0];
		header.setLayoutParams(headerLayoutParams);
	}

}
