package com.jason.jasonutils.popupwindown;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jason.jasonutils.R;


public class PopupWindownDemo extends Activity implements OnClickListener {
	
	private PopupWindow popWindow;//创建弹出窗体
	private Button bt_show_bottom_popupwindown, bt_show_mount_popupwindown, bt_show_acitivity_window;
	public static final int SD1 = 10;
	public static final int SD2 = 20;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popupwindown_activity);
		init();
	}
	
	
	private void init() {
		bt_show_bottom_popupwindown = (Button) findViewById(R.id.bt_show_bottom_popupwindown);
		bt_show_acitivity_window = (Button) findViewById(R.id.bt_show_acitivity_window);
		bt_show_mount_popupwindown = (Button) findViewById(R.id.bt_show_mount_popupwindown);
		
		bt_show_bottom_popupwindown.setOnClickListener(this);
		bt_show_acitivity_window.setOnClickListener(this);
		bt_show_mount_popupwindown.setOnClickListener(this);
	}

	
	/**
	 * 处理返回键点击事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK ){
			if(popWindow != null && popWindow.isShowing()){
				popWindow.dismiss();
				popWindow = null;
				return true;
			}
		}
		return super.onKeyDown(keyCode, event); 
	}


	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.bt_show_bottom_popupwindown:
			createBottomPopupWindown();
			break;
		case R.id.bt_show_acitivity_window:
			startActivityForResult(new Intent(PopupWindownDemo.this, SelectListActivity.class), 30);
			break;
		case R.id.bt_show_mount_popupwindown:
			createMountPopupWindown();
			break;
		case R.id.bt_save_dimencard:
			dismissPopUpWindow();
			Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
			break;
		case R.id.bt_cancel_dimencard:
			dismissPopUpWindow();
			Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
			break;
		case R.id.bt_popup_mount_select1:
			Toast.makeText(this, "选择条目一", Toast.LENGTH_SHORT).show();
			dismissPopUpWindow();
			break;
		case R.id.bt_popup_mount_select2:
			Toast.makeText(this, "选择条目二", Toast.LENGTH_SHORT).show();
			dismissPopUpWindow();
			break;
		}
	}
	
	private void createMountPopupWindown() {
		View popWindView = View.inflate(PopupWindownDemo.this, R.layout.popup_mount_select, null); 
		popWindView.findViewById(R.id.bt_popup_mount_select1).setOnClickListener(PopupWindownDemo.this);
		popWindView.findViewById(R.id.bt_popup_mount_select2).setOnClickListener(PopupWindownDemo.this);
		
		popWindow = new PopupWindow(popWindView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
		popWindow.setBackgroundDrawable(new ColorDrawable(R.color.blue));
		popWindow.setFocusable(true);
		popWindow.setTouchable(true);
		popWindow.setOutsideTouchable(true);
		
		//int[] location = new int[2];
		//popWindView.getLocationInWindow(location);
		//popWindow.showAsDropDown(bt_show_mount_popupwindown, (int)(location[0]+ dip2px(PopupWindownDemo.this, 50)), (int)(location[1]+dip2px(PopupWindownDemo.this, 10))); 
		popWindow.showAsDropDown(bt_show_mount_popupwindown,3,-5);
	}
	
    /** 
     * dip 转为 px
     */   
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }

	/**
	 * 创建PopupWindown窗体
	 */
	public void createBottomPopupWindown(){
		View decorView = this.getWindow().getDecorView(); 
		View popWindView = View.inflate(this, R.layout.popupwindown_dimen_item, null); 
		popWindView.findViewById(R.id.bt_save_dimencard).setOnClickListener(this);
		popWindView.findViewById(R.id.bt_cancel_dimencard).setOnClickListener(this);
		
		popWindow = new PopupWindow(popWindView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, false);
		//popWindow.setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
		popWindow.setFocusable(true);
		popWindow.setTouchable(true);
		popWindow.setOutsideTouchable(false);
		
		int[] location = new int[2];
		popWindView.getLocationInWindow(location);
		popWindow.showAtLocation(decorView, Gravity.RIGHT | Gravity.BOTTOM, location[0],location[1]);
	}
	/**
	 * 关闭PopupWindown窗体
	 */
	private void dismissPopUpWindow() {
		if (popWindow != null && popWindow.isShowing()) {
			popWindow.dismiss();
			popWindow = null;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 30){
			switch (resultCode) {
			case SD1:
				Toast.makeText(this, "选择了SD1", 0).show();
				break;
			case SD2:
				Toast.makeText(this, "选择了SD2", 0).show();
				break;
			default:
				Toast.makeText(this, "没有选择", 0).show();
				break;
			}
		}
	}
}
