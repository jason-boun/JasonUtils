package com.jason.jasonutils.actionbar;

import java.lang.reflect.Field;

import com.jason.jasonutils.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.Toast;

public class ActionBarDemo extends Activity {
	
	private MenuItem menuItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.actionbar_demo_main_activity);
		getOverFlowMenu();
	}
	
	/**
	 * 加载menu文件夹中的布局文件
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 getMenuInflater().inflate(R.menu.actionbar_demo_menu, menu);
		 return true;
	}

	/**
	 * 响应菜单每一项的点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Toast.makeText(this, "menu_settings press", Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_about:
			Toast.makeText(this, "action_about press", Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_search:
			Toast.makeText(this, "action_search press", Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_edit:
			Toast.makeText(this, "action_edit press", Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_email:
			Toast.makeText(this, "action_email press", Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 通过反射来设置系统配置类ViewConfiguration的字段，来显示具有三个点功能(更过功能的)的按键
	 */
	private void getOverFlowMenu(){
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyfield = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if(null!=menuKeyfield){
				menuKeyfield.setAccessible(true);
				menuKeyfield.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
