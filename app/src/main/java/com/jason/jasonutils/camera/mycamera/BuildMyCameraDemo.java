package com.jason.jasonutils.camera.mycamera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;

import com.jason.jasonutils.R;

public class BuildMyCameraDemo extends Activity implements OnClickListener {
	
	private SurfaceView sv_mycamera_show_surfaceview;
	private ImageView iv_mycamera_show_result;
	private Button bt_mycamera_capture;
	private SurfaceHolder mHolder;
	private Camera camera;
	private Bitmap mBitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//设置全屏无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.my_camera_demo_activity);
		init();
	}

	private void init() {
		sv_mycamera_show_surfaceview = (SurfaceView) findViewById(R.id.sv_mycamera_show_surfaceview);
		iv_mycamera_show_result = (ImageView) findViewById(R.id.iv_mycamera_show_result);
		
		bt_mycamera_capture = (Button) findViewById(R.id.bt_mycamera_capture);
		setButtonParams(bt_mycamera_capture);
		bt_mycamera_capture.setOnClickListener(this);
		
		if(chekCameraAviable(this)){
			camera = getCameraInstance(this);
			mHolder = sv_mycamera_show_surfaceview.getHolder();
			mHolder.addCallback(new Callback() {
				@Override
				public void surfaceDestroyed(SurfaceHolder holder) {
					try {
						camera.setPreviewDisplay(holder);
						camera.startPreview();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				@Override
				public void surfaceCreated(SurfaceHolder holder) {
					
				}
				@Override
				public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
					if(holder == null){
						return;
					}
					camera.stopPreview();
					try {
						camera.setPreviewDisplay(holder);
						camera.startPreview();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	public boolean chekCameraAviable(Context context){
		return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}
	
	public Camera getCameraInstance(Context context){
		try{
			if(camera == null){
				camera = Camera.open();
			}
			if(camera !=null){
				Parameters parameters = camera.getParameters();
				parameters.setPictureFormat(ImageFormat.JPEG);
				parameters.setPictureSize(1280, 960);
				parameters.setFocusMode(Parameters.FOCUS_MODE_AUTO);
				int orientation = context.getResources().getConfiguration().orientation;
				if(orientation == Configuration.ORIENTATION_PORTRAIT){
					camera.setDisplayOrientation(90);
				}
				camera.setParameters(parameters);
				camera.startPreview();
				return camera;
			}
		}catch (Exception e) {
			e.printStackTrace();
			releaseCamera(camera);
		}
		return null;
	}
	
	public void releaseCamera(Camera camera){
		if(camera !=null){
			camera.release();
			camera = null;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseCamera(camera);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_mycamera_capture:
			capture(camera);
			break;
		}
	}
	
	public void capture(Camera camera){
		if(camera != null){
			camera.takePicture(new ShutterCallback() {
				@Override
				public void onShutter() {
					//快门按下的回调，在这里可以设置拍照声音
				}
			}, null, new PictureCallback() {
				@Override
				public void onPictureTaken(byte[] data, Camera camera) {
					if(data!=null && data.length>0){
						mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
						if(mBitmap != null){
							camera.stopPreview();
							
							Matrix matrix = new Matrix();
							matrix.postRotate(90);
							Bitmap finalBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, false);
							iv_mycamera_show_result.setImageBitmap(finalBitmap);
							try {
								File filePic = new File(Environment.getExternalStorageDirectory()+"PIC_" + System.currentTimeMillis()+".jpg");
								FileOutputStream fos = new FileOutputStream(filePic);
								finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
								fos.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
							camera.startPreview();
						}
					}
				}
			});
		}
	}
	
	//为了使图片按钮按下和弹起状态不同，采用过滤颜色的方法.按下的时候让图片颜色变淡
	public void setButtonParams(Button mPhotoImgBtn){
		mPhotoImgBtn.setOnTouchListener(new OnTouchListener() {
			public final  float[] BT_SELECTED=new float[]
					{ 2, 0, 0, 0, 2,
					0, 2, 0, 0, 2,
					0, 0, 2, 0, 2,
					0, 0, 0, 1, 0 };			    
			public final float[] BT_NOT_SELECTED=new float[]
					{ 1, 0, 0, 0, 0,
					0, 1, 0, 0, 0,
					0, 0, 1, 0, 0,
					0, 0, 0, 1, 0 };
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
					v.setBackgroundDrawable(v.getBackground());
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
					v.setBackgroundDrawable(v.getBackground());
				}
				return false;
			}
		});
	}
}
