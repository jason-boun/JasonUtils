package com.jason.jasonutils.refresh;

import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;

/**
 * 隐藏下拉头的任务，当未进行下拉刷新或下拉刷新完成后，此任务将会使下拉头重新隐藏。
 * @author bo.jia
 */
public class HideHeaderTask extends AsyncTask<Void, Integer, Integer>{
	
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
	 * 下拉头的高度
	 */
	private int hideHeaderHeight;
	
	public HideHeaderTask(RefreshableView bootView, View header, MarginLayoutParams headerLayoutParams){
		this.bootView = bootView;
		this.header = header;
		this.headerLayoutParams = headerLayoutParams;
		this.hideHeaderHeight = -header.getHeight();
	}

	@Override
	protected Integer doInBackground(Void... params) {
		int topMargin = headerLayoutParams.topMargin;
		while (true) {
			topMargin = topMargin + RefreshViewConstants.SCROLL_SPEED;
			if (topMargin <= hideHeaderHeight) {
				topMargin = hideHeaderHeight;
				break;
			}
			publishProgress(topMargin);
		}
		return topMargin;
	}

	@Override
	protected void onProgressUpdate(Integer... topMargin) {
		headerLayoutParams.topMargin = topMargin[0];
		header.setLayoutParams(headerLayoutParams);
	}

	@Override
	protected void onPostExecute(Integer topMargin) {
		headerLayoutParams.topMargin = topMargin;
		header.setLayoutParams(headerLayoutParams);
		bootView.currentStatus = HeaderStatus.FINISHED;
	}


}
