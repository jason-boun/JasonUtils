package com.jason.jasonutils.cache;

import java.io.File;

import android.content.Context;

public abstract class AbstractFileCache {

	protected String dirString;
	protected Context context;

	public AbstractFileCache(Context context) {
		this.context = context;
		dirString = getCacheDir();
		boolean ret = FileHelper.createDirectory(dirString);
	}

	public File getFile(String url) {
		File f = new File(getSavePath(url));
		return f;
	}

	public abstract String getSavePath(String url);

	public abstract String getCacheDir();

	public void clear() {
		FileHelper.deleteDirectory(dirString);
	}

}