package com.jason.jasonutils.processmanager;

import android.graphics.drawable.Drawable;

public class ProcessInfoBean {
	
	private String appName;
	private String packName;
	private Drawable appIcon;
	private long mensize;//long byte
	private boolean checked;
	private boolean userTask;

	public boolean isUserTask() {
		return userTask;
	}

	public void setUserTask(boolean userTask) {
		this.userTask = userTask;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public long getMensize() {
		return mensize;
	}

	public void setMensize(long mensize) {
		this.mensize = mensize;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	

	@Override
	public String toString() {
		return "TaskInfo [appName=" + appName + ", packName=" + packName
				+ ", mensize=" + mensize + ", userTask=" + userTask + "]";
	}

}
