package com.jason.jasonutils.pictureoperate;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.jason.jasonutils.R;

public class TakePicForPhotoActivity extends Activity {
	
	private static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
	private static File mCurrentPhotoFile;
	private static  final int CAMERA_WITH_DATA = 3023;
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	private static final int ICON_SIZE = 96;
	private ImageView iv_show_picture;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_pic);
		iv_show_picture = (ImageView) findViewById(R.id.iv_show_picture);
	}
	
	/**
	 * 通过摄像头获取图片
	 */
	@SuppressLint("SimpleDateFormat")
	public void takeFromCamera(View view){
		PHOTO_DIR.mkdirs();
		String name = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss").format(new Date(System.currentTimeMillis())) + ".jpg";
		mCurrentPhotoFile = new File(PHOTO_DIR, name);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
		startActivityForResult(intent, CAMERA_WITH_DATA);
	}
	
	/**
	 * 通过相册获取图片
	 */
	public void takeFromGalary(View view){
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", ICON_SIZE);
			intent.putExtra("outputY", ICON_SIZE);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 摄像头拍照后，打开系统图库编辑选择图片
	 */
	private void doCropPhoto(File f) {
		try {
			MediaScannerConnection.scanFile(this,new String[] { f.getAbsolutePath() },new String[] { null }, null);//保存图片
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(Uri.fromFile(f), "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", ICON_SIZE);
			intent.putExtra("outputY", ICON_SIZE);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			switch (requestCode) {
			case CAMERA_WITH_DATA:
				doCropPhoto(mCurrentPhotoFile);
				break;
			case PHOTO_PICKED_WITH_DATA:
				setData(data);
				break;
			}
		}
	}
	
	/**
	 * 设置图片
	 */
	public void setData(Intent intent){
		if (intent != null) {
			Bitmap photo = intent.getParcelableExtra("data");
			if (photo != null) {
				iv_show_picture.setImageBitmap(photo);
			}
		}
	}

}
