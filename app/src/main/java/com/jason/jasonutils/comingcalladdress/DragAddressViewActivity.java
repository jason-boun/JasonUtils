package com.jason.jasonutils.comingcalladdress;

import com.jason.jasonutils.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class DragAddressViewActivity extends Activity {
	
	private TextView tv_darg_view;
	private ImageView iv_drag_view;
	private SharedPreferences sp;
	private WindowManager wm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		setContentView(R.layout.drag_address_view_activity);
		
		init();
		touchOperate();
	}

	private void init() {
		sp = getSharedPreferences("config", MODE_PRIVATE);
		iv_drag_view = (ImageView) findViewById(R.id.iv_drag_view);
		tv_darg_view = (TextView) findViewById(R.id.tv_darg_view);
		
		int lastx = sp.getInt("lastx", 0);
		int lasty = sp.getInt("lasty", 0);
		
		RelativeLayout.LayoutParams parms = (LayoutParams) iv_drag_view.getLayoutParams();
		parms.leftMargin = lastx;
		parms.topMargin = lasty;
		iv_drag_view.setLayoutParams(parms);
		
		wm = getWindowManager();
	}
	
	public void touchOperate(){
		iv_drag_view.setOnTouchListener(new OnTouchListener() {
			int startX;
			int startY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int b = iv_drag_view.getBottom();
					int t = iv_drag_view.getTop();
					int l = iv_drag_view.getLeft();
					int r = iv_drag_view.getRight();
					
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					int dx = newX - startX;
					int dy = newY - startY;
					
					int newr = r + dx;
					int newl = l + dx;
					int newt = t + dy;
					int newb = b + dy;
					
					//超出左右边界就就阻止移动和记录轨迹
					if (newl < 0 || newt < 0 || newr > wm.getDefaultDisplay().getWidth() || newb > wm.getDefaultDisplay().getHeight()) {
						break;
					}
					iv_drag_view.layout(newl, newt, newr, newb);
					
					//处理文本提示框的位置
					int tv_height = tv_darg_view.getBottom() - tv_darg_view.getTop();
					if (newt > wm.getDefaultDisplay().getHeight() / 2) {
						// 说明手指 已经在窗体的下半部分.tv_darg_view设置到窗体的最上面
						tv_darg_view.layout(0, 0, wm.getDefaultDisplay().getWidth(), tv_height);
					}else{
						// 手指已经在窗体的上半部分.tv_darg_view 设置到窗体的最下面
						tv_darg_view.layout(0, 
								wm.getDefaultDisplay().getHeight() - tv_height - 30, 
								wm.getDefaultDisplay().getWidth(), 
								wm.getDefaultDisplay().getHeight() - 30);
					}
					
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();

					break;
				case MotionEvent.ACTION_UP:
					sp.edit().putInt("lastx", iv_drag_view.getLeft()).putInt("lasty", iv_drag_view.getTop()).commit();
					break;
				}
				return true;
			}
		});
	}
	
}
