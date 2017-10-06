package com.jason.jasonutils.tools;

import java.util.HashMap;
import java.util.Map;

import com.jason.jasonutils.R;

import android.app.Activity;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;

public class ViewUtil {
	
	/**
	 * 给某一个ViewGroup容器的子View设置点击监听器
	 * @param parent 点击事件View的父容器
	 * @param startIndex 第一个需要设置点击事件的View在父容器的index
	 * @param count 从第一个需要设置点击事件的View开始总共需要设置几个View
	 * @param listtener 需要添加的监听接口
	 */
	public static void setChildsOnClickLisener(ViewGroup parent, int startIndex, int count, View.OnClickListener lisener){
		if(startIndex<0 || startIndex+count > parent.getChildCount()){
			return;
		}
		for(int i=startIndex; i<startIndex + count; i++){
			parent.getChildAt(i).setOnClickListener(lisener);
		}
	}
	
	/**
	 * 给某一个ViewGroup容器的子View设置点击监听器
	 * @param act Activity 
	 * @param containerId 点击事件View的父容器资源id
	 * @param startIndex 第一个需要设置点击事件的View在父容器的index
	 * @param lisener 需要添加的监听接口
	 */
	public static void setChildsOnClickLisener(Activity act, int containerId, int startIndex, View.OnClickListener lisener){
		View view = act.findViewById(containerId);
		if(view instanceof ViewGroup){
			ViewGroup container = (ViewGroup) act.findViewById(containerId);
			setChildsOnClickLisener(container, startIndex, container.getChildCount(), lisener);
		}
	}
	
	/**
	 * 获取指定View的宽度和高度
	 * @param view
	 * @return 返回HashMap数据，其中宽度和高度的key值分别为Width和Height
	 */
	public static Map<String, Integer> getTextHeightWidth(final View view) {
		final Map<String, Integer> result = new HashMap<String, Integer>();
		ViewTreeObserver treeObserver = view.getViewTreeObserver();
		treeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				result.clear();
				result.put("Width", view.getWidth());
				result.put("Height", view.getHeight());
			}
		});
		return result;
	}
	
	/**
	 * 设置TextView文本不同字体大小和颜色
	 * @param textView
	 * @param str1
	 * @param str2
	 */
	public static void setTextViewInfo(TextView textView, String str1, String str2){
		//设置字体大小的size属性竟然不起作用
		String htmlText = "<font color=#000000 size=30sp>"+str1+"</font>" + "<font color=#ff6600 size=10sp>"+str2+"</font>";
		textView.setText(Html.fromHtml(htmlText));
	}
	
	/**
	 * 同一个TextView的文字设置不同的大小和颜色
	 * @param textView
	 * @param srcStr1
	 * @param srcStr2
	 */
	public static void setPromotionText(TextView textView, String srcStr1, String srcStr2){
		SpannableString ss = new SpannableString(srcStr1 + srcStr2);  
        ss.setSpan(new TextAppearanceSpan(textView.getContext(), R.style.payment_paymethod_text), 0, srcStr1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  
        ss.setSpan(new TextAppearanceSpan(textView.getContext(), R.style.payment_paymethod_promotion_text), srcStr1.length(), ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  
        textView.setText(ss, TextView.BufferType.SPANNABLE);
        //或者通过类的fromHtml方法通过标签形式来设置文本的颜色及字体大小属性
        //setTextViewInfo(textView,srcStr1,srcStr2);
	}

}
