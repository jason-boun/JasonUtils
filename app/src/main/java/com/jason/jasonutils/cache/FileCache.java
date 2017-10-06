package com.jason.jasonutils.cache;


import android.content.Context;

/**
 * 在本应用下创建一个随机名的文件
 */

public class FileCache extends AbstractFileCache{

    public FileCache(Context context) {
        super(context);
    }


    @Override
    public String getSavePath(String url) {
        String filename = String.valueOf(url.hashCode());
        return getCacheDir() + filename;
    }

    @Override
    public String getCacheDir() {
        return FileManager.getSaveFilePath(context);
    }

}