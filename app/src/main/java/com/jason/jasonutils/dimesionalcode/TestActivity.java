package com.jason.jasonutils.dimesionalcode;

import com.jason.jasonutils.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends Activity implements OnClickListener{

	EditText et_info;
	ImageView iv_show;
	Button bt_get,bt_show, bt_show2;
	Bitmap bitmap;
	TextView tv_show;
	DimensionaLCode ecodeingView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dimensional_code);
        
        et_info = (EditText) findViewById(R.id.et_info);
        iv_show = (ImageView) findViewById(R.id.iv_show);
        bt_get = (Button) findViewById(R.id.bt_get);
        bt_show = (Button) findViewById(R.id.bt_show);
        bt_show2 = (Button) findViewById(R.id.bt_show2);
        tv_show = (TextView) findViewById(R.id.tv_show);
        
        bt_get.setOnClickListener(this);
        bt_show.setOnClickListener(this);
        bt_show2.setOnClickListener(this);
		
		ecodeingView = new DimensionaLCode();
		/*String loc = ActivityManagerNative.getDefault().getConfiguration().locale.getCountry(); 
		ActivityManager am = ActivityManagerNative.getDefault();  */
    }

	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.bt_get:
			String info = et_info.getText().toString().trim();
			if(TextUtils.isEmpty(info)){
				Toast.makeText(TestActivity.this, "信息不能为空", Toast.LENGTH_SHORT).show();
			}else{
				ecodeingView.createImage(info, System.currentTimeMillis()+"", iv_show);
			}
			break;
		case R.id.bt_show:
			if(iv_show.getDrawable()!=null){
				Bitmap bitmap = ((BitmapDrawable) iv_show.getDrawable()).getBitmap();
				if(bitmap!= null)
					ecodeingView.scanningImage(bitmap, tv_show);
			}else{
				Toast.makeText(TestActivity.this, "无可扫描的二维码图片", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.bt_show2:
			openCamera();
			break;
		}
	}
	/**
	 * 展示扫描到的信息
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(data != null){
			tv_show.setText(data.getStringExtra("Code"));
		}else{
			Toast.makeText(TestActivity.this, "木有扫描到二维码", Toast.LENGTH_SHORT).show();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 通过摄像头获取二维码bitmap信息
	 * @param act
	 */
	public void openCamera(){
		Intent intent = new Intent();
		intent.setClass(this, CameraRenderActivity.class);
		startActivityForResult(intent, 0);
	}
	
	
	/**
	 * 及时销毁对象，回收内存
	 */
	@Override
	protected void onStop() {
		super.onStop();
		TestActivity.this.finish();
	}
}
