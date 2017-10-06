package com.jason.jasonutils.appmanager;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.PopupWindow;

import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.DensityUtil;

public class PopupWindowUtil {

	/**
	 * 创建一个PopupWindow
	 * @param act
	 * @param popWindow
	 * @param parent
	 * @return
	 */
	public static PopupWindow showPopupWindow(Activity act, PopupWindow popWindow, View parent){
		View windownView = LayoutInflater.from(act).inflate(R.layout.app_operate_popup_item, null);
		
		ScaleAnimation animation = new ScaleAnimation(0.2F, 1.4F, 0.2F, 1.4F, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
		animation.setDuration(300);
		windownView.startAnimation(animation);
		
		windownView.findViewById(R.id.ll_app_onperate_start).setOnClickListener( (OnClickListener) act);
		windownView.findViewById(R.id.ll_app_onperate_share).setOnClickListener( (OnClickListener) act);
		windownView.findViewById(R.id.ll_app_onperate_uninstall) .setOnClickListener((OnClickListener) act);
		
		popWindow = new PopupWindow(windownView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		popWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		
		int[] location = new int[2];
		parent.getLocationInWindow(location);
		popWindow.showAtLocation(parent, Gravity.TOP|Gravity.LEFT, location[0]+DensityUtil.dip2px(act, 60), location[1]);
		
		return popWindow;
	}
	
	/**
	 * 销毁一个PopupWindow
	 * @param window
	 */
	public static boolean dissMissWindow(PopupWindow window){
		if(window !=null){
			if(window.isShowing()){
				window.dismiss();
			}
			window = null;
			return true;
		}
		return false;
	}
	
}
