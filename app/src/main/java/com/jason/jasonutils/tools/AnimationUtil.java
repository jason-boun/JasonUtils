package com.jason.jasonutils.tools;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

public class AnimationUtil {
	 
	 /**
	  * 显示动画
	  * @param view 显示动画的View
	  * @param animaitonResId 动画资源文件，比如R.anim.call_animation
	  */
	 public static void showAnimation(final View view, final int animaitonResId){
		 view.post(new Runnable() {
			@Override
			public void run() {
				if(animaitonResId!=-1){
					view.setBackgroundResource(animaitonResId);
					AnimationDrawable anim = (AnimationDrawable) view.getBackground();
					anim.start();
				}
			}
		});
	 }
	 
	 /**
	  * 关闭动画
	  */
	 public static void stopAnimation(final View view){
		 view.post(new Runnable() {
			@Override
			public void run() {
				Drawable drawable = view.getBackground();
				if(drawable !=null && drawable instanceof AnimationDrawable){
					AnimationDrawable anim = (AnimationDrawable) drawable;
					if(anim.isRunning()){
						anim.stop();
					}
				}
			}
		});
	 }
}
