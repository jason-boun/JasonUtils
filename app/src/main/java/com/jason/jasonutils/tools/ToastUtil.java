package com.jason.jasonutils.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.R;
/**
 * 自定义CustomToast类
 */
public class ToastUtil {
	
	public static PopupWindow popupWindow = null;
	public static CountDownTimer timer = null;
	
	/**
	 * 带有Icon的Toast
	 */
	public static void showCustomToast(Context context, CharSequence text, Bitmap bitmap){
		makeText(context, text, Toast.LENGTH_SHORT, bitmap).show();
	}
	
	/**
	 * 带有Icon的Toast
	 */
	public static void showCustomToast(Context context, int resId, Bitmap bitmap){
		showCustomToast(context, context.getResources().getString(resId), bitmap);  
	}
	
	/**
	 * 只带文本信息的Toast
	 */
	public static void showCustomToast(Context context, CharSequence text){
		makeText(context, text,Toast.LENGTH_SHORT,null).show();
	}
	
	/**
	 * 只带文本信息的Toast
	 */
	public static void showCustomToast(Context context, int resId){
		showCustomToast(context, context.getResources().getString(resId));
	}
	
	
	/**
	 * 子线程提示Toast
	 * @param act
	 * @param tips
	 */
	public static void showToast4Thread(final Activity act,final String tips){
		act.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showCustomToast(act, tips);
			}
		});
	}
	
	/**
	 * 通过View获取Context在子线程中更新UI
	 */
	public static void useViewShowToast(final View view,final String tips){
		view.post(new Runnable() {
			@Override
			public void run() {
				showCustomToast(view.getContext(), tips);
			}
		});
	}

	/**
	 * 获取LayoutInflater对象，加载自定义View，并将该View挂载到Toast对象上
	 * @param context
	 * @param text
	 * @param duration
	 * @param bitmap
	 * @return
	 */
	@SuppressLint("InflateParams")
	private static Toast makeText(Context context, CharSequence text, int duration, Bitmap bitmap) {
		Toast toast = new Toast(context);
        //View toastView = View.inflate(context, R.layout.customizedtoast, null);//方式三（源码实质是调用方式二）
		//LayoutInflater layoutInflater = LayoutInflater.from(context);//方式二（源码实质是调用方式一）
		try{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//方式一
			View toastView = inflater.inflate(R.layout.customizedtoast, null);  
			if(bitmap !=null ){
				ImageView imageView = (ImageView) toastView.findViewById(R.id.iv_toasticon);  
				imageView.setImageBitmap(bitmap);
				imageView.setVisibility(View.VISIBLE);
			}
			TextView textView = (TextView) toastView.findViewById(R.id.tv_toastcontent);  
			textView.setText(text);
			
			toast.setView(toastView);  
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 140);  
			toast.setDuration(duration);  
		}catch(Exception e){
			e.printStackTrace();
			return toast;
		}
		return toast;
    } 

	/**
	 * 显示一个自定义的toast
	 * @param text
	 * @param view
	 * @param type
	 * 默认0挨着view的下方显示，1挨着view的上方显示
	 */
	protected void showFailToast(final Activity act, String text, View view, int type) {
		LayoutInflater inflate = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View toastView = inflate.inflate(R.layout.my_toast, null);
		if (popupWindow == null) {
			popupWindow = new PopupWindow(toastView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			if (text != null) {
				TextView tv = (TextView) toastView.findViewById(R.id.tv_mytoast);
				tv.setText(text);
			}
			timer = new CountDownTimer(2000, 1000) {
				@Override
				public void onTick(long millisUntilFinished) {

				}

				@Override
				public void onFinish() {
					if (popupWindow.isShowing()) {
						popupWindow.dismiss();
						// timer.cancel();
					}
				}
			};
		}
		Rect anchorRect = getAnchorRect(view);
		timer.onFinish();
		timer.start();
		if (type != 1){
			popupWindow.showAtLocation(view, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, anchorRect.bottom);
		} else{
			popupWindow.showAtLocation(view, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, anchorRect.top);
		}
	}
	
	public static Rect getAnchorRect(View v) {
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		Rect anchorRect = new Rect(location[0], location[1], location[0] + v.getWidth(), location[1] + v.getHeight());
		return anchorRect;
	}
	
	/**
	 * 调用自定Toast类的方法，演示不同Toast类型
	 */
	public static void showCustomToast(Context ctx){
		Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.toast_fixedicon);
		ToastUtil.showCustomToast(ctx, "只带文本信息的自定义土司展示");
		ToastUtil.showCustomToast(ctx, R.string.app_name);
		ToastUtil.showCustomToast(ctx, R.string.app_name, bitmap);
		ToastUtil.showCustomToast(ctx, "带有icon的自定义土司展示", bitmap);
	}
}
