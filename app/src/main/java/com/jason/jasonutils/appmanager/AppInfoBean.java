package com.jason.jasonutils.appmanager;

import android.graphics.drawable.Drawable;

public class AppInfoBean {

	private Drawable appIcon;
	private String appName;
	private String packName;
	private String version;
	private boolean inRom;//是否安装在手机内存
	private boolean userApp;//是否是用户自己安装的程序

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isInRom() {
		return inRom;
	}

	public void setInRom(boolean inRom) {
		this.inRom = inRom;
	}

	public boolean isUserApp() {
		return userApp;
	}

	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}

	@Override
	public String toString() {
		return "AppInfo [appName=" + appName + ", packName=" + packName
				+ ", version=" + version + ", inRom=" + inRom + ", userApp="
				+ userApp + "]";
	}

}
