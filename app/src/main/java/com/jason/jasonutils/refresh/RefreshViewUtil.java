package com.jason.jasonutils.refresh;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 下拉刷新的使用工具类
 * @author bo.jia
 */
public class RefreshViewUtil {
	
	public static BaseAdapter adapter;
	public static PullToRefreshListener refreshListener;
	
	public static void initRefreshView(BaseAdapter mAdapter, PullToRefreshListener mRefreshListener){
		adapter = mAdapter;
		refreshListener = mRefreshListener;
	}
	
	/**
	 * 保存刷新时间戳
	 * @param context
	 * @param mId 为了防止不同界面的下拉刷新在上次更新时间上互相有冲突，使用id来做区分
	 */
	public static void updateRefreshTimeStamp(Context context,int mId){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		preferences.edit().putLong(RefreshViewConstants.UPDATED_AT + mId, System.currentTimeMillis()).commit();
	}
	
	/**
	 * 获取上次刷新的时间戳
	 * @param context
	 * @param mId 为了防止不同界面的下拉刷新在上次更新时间上互相有冲突，使用id来做区分
	 * @return 上次刷新时间戳，单位为毫秒
	 */
	public static long getLastRefreshTimeStamp(Context context,int mId){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getLong(RefreshViewConstants.UPDATED_AT + mId, -1);//上次更新时间的毫秒值
	}
	
	/**
	 * 刷新下拉头中上次更新时间的文字描述。
	 */
	public static void refreshUpdatedAtValue(TextView updateAt, int mId) {
		long lastUpdateTime = RefreshViewUtil.getLastRefreshTimeStamp(updateAt.getContext(), mId);
		long currentTime = System.currentTimeMillis();
		long timePassed = currentTime - lastUpdateTime;
		long timeIntoFormat;
		String updateAtValue;
		if (lastUpdateTime == -1) {
			updateAtValue = "暂未更新过";
		} else if (timePassed < 0) {
			updateAtValue = "时间有问题";
		} else if (timePassed < RefreshViewConstants.ONE_MINUTE) {
			updateAtValue = "刚刚更新";
		} else if (timePassed < RefreshViewConstants.ONE_HOUR) {
			timeIntoFormat = timePassed / RefreshViewConstants.ONE_MINUTE;
			String value = timeIntoFormat + "分钟";
			updateAtValue = String.format("上次更新于%1$s前", value);
		} else if (timePassed < RefreshViewConstants.ONE_DAY) {
			timeIntoFormat = timePassed / RefreshViewConstants.ONE_HOUR;
			String value = timeIntoFormat + "小时";
			updateAtValue = String.format("上次更新于%1$s前", value);
		} else if (timePassed < RefreshViewConstants.ONE_MONTH) {
			timeIntoFormat = timePassed / RefreshViewConstants.ONE_DAY;
			String value = timeIntoFormat + "天";
			updateAtValue = String.format("上次更新于%1$s前", value);
		} else if (timePassed < RefreshViewConstants.ONE_YEAR) {
			timeIntoFormat = timePassed / RefreshViewConstants.ONE_MONTH;
			String value = timeIntoFormat + "个月";
			updateAtValue = String.format("上次更新于%1$s前", value);
		} else {
			timeIntoFormat = timePassed / RefreshViewConstants.ONE_YEAR;
			String value = timeIntoFormat + "年";
			updateAtValue = String.format("上次更新于%1$s前", value);
		}
		updateAt.setText(updateAtValue);
	}

}
