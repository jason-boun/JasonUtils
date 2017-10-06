package com.jason.jasonutils.floatlayerfunction;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.jason.jasonutils.R;

/**
 * 浮层效果，介绍新功能
 */

public class FloatLayer2IntroduceFunction extends Activity {
	
	private SharedPreferences sp ;
	private final String NEW_FUNCNTION = "new_funcntion";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.floatlayer_introduce_function);
		
		showNewFunction();
	}
	
	private void showNewFunction() {
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		int count = sp.getInt(NEW_FUNCNTION, 0);
		if(count<5){
			count++;
			new MyDialog(this, getResources().getDrawable(R.drawable.lxr1)).show();
			sp.edit().putInt(NEW_FUNCNTION, count).commit();
			Toast.makeText(this, "浮层功能展示第"+count+"次", 0).show();
		}else{
			Toast.makeText(this, "浮层功能展示已经超过五次", 0).show();
		}
	}

	private class MyDialog extends Dialog{
		/**
		 * 构造函数
		 * @param context
		 * @param drawable
		 */
		public MyDialog(Context context, final Drawable drawable){
			super(context,R.style.MyDialog);
			
			//设置界面图片
			ImageView imageView = new ImageView(context);
			imageView.setBackgroundDrawable(drawable);
			setContentView(imageView);
			
			//设置屏幕长宽属性
			WindowManager.LayoutParams wlp = getWindow().getAttributes();
			Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			wlp.width = display.getWidth();
			wlp.height = display.getHeight()-38;
			
			//设置界面图片点击事件
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					drawable.setCallback(null);
					MyDialog.super.dismiss();
				}
			});
		}
	}
}
