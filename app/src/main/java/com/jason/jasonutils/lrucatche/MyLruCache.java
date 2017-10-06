package com.jason.jasonutils.lrucatche;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import com.jason.jasonutils.tools.MLog;

/**
 * 图片缓存的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少使用的图片移除掉。
 */
public class MyLruCache {
	
	private static int maxMemory = (int) Runtime.getRuntime().maxMemory();// 获取该应用程序可分配到的最大可用内存
	private static int cacheSize = maxMemory / 8;	// 设置图片缓存大小为该APP最大可用内存的1/8
	private static LruCache<String, Bitmap> lruCache;
	
	private static MyLruCache instance;
	private MyLruCache(){}
	@SuppressLint("NewApi")
	public static MyLruCache getInstance(){
		if(instance == null){
			lruCache = new LruCache<String, Bitmap>(cacheSize){
				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getByteCount();
				}
			};
			instance = new MyLruCache();
		}
		return instance;
	}

	/**
	 * 将一张图片存储到LruCache中。键是图片对应的URL，值是下载后对应的Bitmap对象。
	 */
	@SuppressLint("NewApi")
	public void addToCache(String key, Bitmap bitmap) {
		MLog.i("哈哈", "添加到缓存中url key===="+key);
		if (!TextUtils.isEmpty(key) && getFromCache(key) == null && bitmap!=null) {
			lruCache.put(key, bitmap);
		}
	}

	/**
	 * 从LruCache中获取一张图片，如果不存在就返回null。
	 */
	@SuppressLint("NewApi")
	public Bitmap getFromCache(String key) {
		if(!TextUtils.isEmpty(key)){
			return lruCache.get(key);
		}
		return null;
	}
	
}
