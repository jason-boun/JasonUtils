package com.jason.jasonutils.menu;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.jason.jasonutils.R;

public class MenuDemoActivity extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String files [] = new String[]{"文件一","文件二","文件三","文件四"};
		this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, files));
		
		registerForContextMenu(getListView());
	}
	
	/**
	 * 方法一：View注册上下文菜单，长按注册View，弹出上下文菜单ContextMenu。
	 */
	@Override
	public void registerForContextMenu(View view) {
		super.registerForContextMenu(view);
	}
	
	/**
	 * 调用方法二
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		menu.setHeaderIcon(R.drawable.toast_fixedicon);
		menu.setHeaderTitle("文件操作");
		
		getMenuInflater().inflate(R.menu.operate_message_menu, menu);
		menu.add("添加的文件操作");
	}
	
	/**
	 * 调用方法三
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int position = ((AdapterContextMenuInfo)item.getMenuInfo()).position;
		Toast.makeText(this, "第"+(position+1)+"条记录被点击了", Toast.LENGTH_SHORT).show();
		
		switch (item.getItemId()) {
		case R.id.menudemo_delete_one_msg:
			Toast.makeText(this, "删除第"+(position+1)+"条消息记录", Toast.LENGTH_SHORT).show();
			break;
		case R.id.menudemo_delete_all_msg:
			Toast.makeText(this, "删除全部消息记录", Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onContextItemSelected(item);
	}

}
