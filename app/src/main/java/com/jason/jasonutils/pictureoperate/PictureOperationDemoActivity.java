package com.jason.jasonutils.pictureoperate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jason.jasonutils.R;

public class PictureOperationDemoActivity extends Activity implements OnClickListener {
	
	private Button bt_picture_bitmapoption_demo;
	private Button bt_pic_picture_from_gallary;
	private Button bt_matrix_operate_demo;
	private Button bt_take_pic_for_photo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picture_operate_demo_activity);
		
		init();
	}

	private void init() {
		bt_picture_bitmapoption_demo = (Button) findViewById(R.id.bt_picture_bitmapoption_demo);
		bt_pic_picture_from_gallary = (Button) findViewById(R.id.bt_pic_picture_from_gallary);
		bt_matrix_operate_demo = (Button) findViewById(R.id.bt_matrix_operate_demo);
		bt_take_pic_for_photo = (Button) findViewById(R.id.bt_take_pic_for_photo);
		bt_picture_bitmapoption_demo.setOnClickListener(this);
		bt_pic_picture_from_gallary.setOnClickListener(this);
		bt_matrix_operate_demo.setOnClickListener(this);
		bt_take_pic_for_photo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_picture_bitmapoption_demo:
			startActivity(new Intent(this, BitmapOptionDemo.class));
			break;
		case R.id.bt_pic_picture_from_gallary:
			startActivity(new Intent(this, TakePictureFromGallary.class));
			break;
		case R.id.bt_matrix_operate_demo:
			startActivity(new Intent(this, BitmapMatrixDemo.class));
			break;
		case R.id.bt_take_pic_for_photo:
			startActivity(new Intent(this, TakePicForPhotoActivity.class));
			break;
		}
	}
}
