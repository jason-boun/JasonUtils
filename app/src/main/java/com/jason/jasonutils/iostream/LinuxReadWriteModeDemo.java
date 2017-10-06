package com.jason.jasonutils.iostream;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jason.jasonutils.R;

public class LinuxReadWriteModeDemo extends Activity {
	
	private RadioGroup rg_select_mode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.linux_read_write_mode_activity);
		rg_select_mode = (RadioGroup) findViewById(R.id.rg_select_mode);
	}

	/**
	 * 生成文件点击方法
	 */
	public void generateFile(View view){
		switch (rg_select_mode.getCheckedRadioButtonId()) {
		case R.id.rb_private:
			savePrivate();
			break;
		case R.id.rb_readable:
			saveReadble();
			break;
		case R.id.rb_writeable:
			savewriteable();
			break;
		case R.id.rb_all:
			saveAll();
			break;
		case R.id.rb_append:
			saveAppend();
			break;
		case R.id.rb_sd:
			saveSD();
			break;
		}
	}
	
	/**
	 * 私有模式保存数据
	 */
	private void savePrivate() {
		try {
			FileOutputStream fos = this.openFileOutput("private.txt", MODE_PRIVATE);
			fos.write("私有模式保存的数据".getBytes());
			fos.close();
			Toast.makeText(this, "私有模式写入成功", 0).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 可读模式保存数据
	 */
	private void saveReadble() {
		try {
			FileOutputStream fos = this.openFileOutput("readble.txt", MODE_WORLD_READABLE);
			fos.write("全局可读模式保存的数据".getBytes());
			fos.close();
			Toast.makeText(this, "全局可读模式写入成功", 0).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 可写模式保存数据
	 */
	private void savewriteable() {
		try {
			FileOutputStream fos = this.openFileOutput("writeable.txt", MODE_WORLD_WRITEABLE);
			fos.write("全局可写模式保存的数据".getBytes());
			fos.close();
			Toast.makeText(this, "全局可写模式写入成功", 0).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 以全局可读可写模式保存数据
	 */
	private void saveAll() {
		try {
			FileOutputStream fos = this.openFileOutput("all.txt", MODE_WORLD_WRITEABLE | MODE_WORLD_WRITEABLE);
			fos.write("全局可读可写模式保存的数据".getBytes());
			fos.close();
			Toast.makeText(this, "全局可读可写模式写入成功", 0).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 追加模式保存数据
	 */
	private void saveAppend() {
		try {
			FileOutputStream fos = this.openFileOutput("append.txt", MODE_APPEND);
			fos.write("追加模式保存的数据".getBytes());
			fos.close();
			Toast.makeText(this, "追加模式写入成功", 0).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存文件到SD卡
	 */
	private void saveSD() {
		try {
			File file = new File(Environment.getExternalStorageDirectory(),"sdsave.txt");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write("通过SD保存的数据".getBytes());
			fos.close();
			Toast.makeText(this, "SD卡写入数据成功", 0).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 继续追加模式写文件
	 * @param view
	 */
	public void writeappend(View view){
		try {
			FileOutputStream fos = this.openFileOutput("append.txt", MODE_APPEND);
			fos.write(".....继续追加模式保存的数据".getBytes());
			fos.close();
			Toast.makeText(this, "继续追加模式写入成功", 0).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 写缓存文件
	 * @param view
	 */
	public void writecache(View view){
		try {
			FileOutputStream fos = new FileOutputStream(new File(this.getCacheDir(), "cache.txt"));
			fos.write("缓存数据".getBytes());
			fos.close();
			Toast.makeText(this, "写入缓存数据成功", 0).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
