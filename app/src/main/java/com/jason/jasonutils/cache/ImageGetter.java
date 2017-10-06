package com.jason.jasonutils.cache;

import android.graphics.Bitmap;

public abstract class ImageGetter{
	
	String key;
	public ImageGetter(String key){
		this.key = key;	
	}
	
	public String getKey(){
		return key;
	}
    
    public abstract Bitmap getBitmap();
}