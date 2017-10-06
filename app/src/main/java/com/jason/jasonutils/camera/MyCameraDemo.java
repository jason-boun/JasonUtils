package com.jason.jasonutils.camera;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.jason.jasonutils.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import com.jason.jasonutils.tools.MLog;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MyCameraDemo extends Activity implements Callback {
	
	private SurfaceView previewSV;
	private SurfaceHolder mySurfaceHolder;
	private AutoFocusCallback myAutoFocusCallback;
	private ImageButton mPhotoImgBtn;
	private boolean isPreview = false;
	private Camera myCamera;
	private Bitmap mBitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//设置全屏无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.camera_demo_activity);
		
		init();
		initFocusSetting();
	}

	/**
	 * 初始化SurfaceView
	 */
	private void init() {
		previewSV = (SurfaceView) findViewById(R.id.previewSV);
		mySurfaceHolder = previewSV.getHolder();
		mySurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
		mySurfaceHolder.addCallback(this);
		mySurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		mPhotoImgBtn = (ImageButton)findViewById(R.id.photoImgBtn);
		//手动设置拍照ImageButton的大小为120×120,原图片大小是64×64
		LayoutParams lp = mPhotoImgBtn.getLayoutParams();
		lp.width = 120;
		lp.height = 120;
		mPhotoImgBtn.setLayoutParams(lp);
		mPhotoImgBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isPreview && myCamera!=null){
					myCamera.takePicture(myShutterCallback, null, myJpegCallback);	
				}
			}
		});
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
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
					v.setBackgroundDrawable(v.getBackground());
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
					v.setBackgroundDrawable(v.getBackground());
					
				}
				return false;
			}
		});
	}

	/**
	 * 自动聚焦变量回调
	 */
	private void initFocusSetting() {
		myAutoFocusCallback = new AutoFocusCallback(){
			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				if(success){
					MLog.i("哈哈", "myAutoFocusCallback: success...");
				}else{
					MLog.i("哈哈", "myAutoFocusCallback: 失败了...");
				}
			}
			
		};
	}
	/**
	 * 为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量
	 */
	
	//快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
	private ShutterCallback myShutterCallback = new ShutterCallback(){
		@Override
		public void onShutter() {
			MLog.i("哈哈", "myShutterCallback:onShutter...");
		}
		
	};
	
	// 拍摄的未压缩原数据的回调,可以为null
	private PictureCallback myRawCallback = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			MLog.i("哈哈", "myRawCallback:onPictureTaken...");
		}
	};
	
	private PictureCallback myJpegCallback = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			if(null != data){
				mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
				myCamera.stopPreview();
				isPreview = false;
			}
			//设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。图片竟然不能旋转了，故这里要旋转下
			Matrix matrix = new Matrix();
			matrix.postRotate((float)90.0);
			Bitmap rotaBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, false);
			//保存图片到sdcard
			if(null != rotaBitmap)
			{
				saveJpeg(rotaBitmap);
			}

			//再次进入预览
			myCamera.startPreview();
			isPreview = true;
		}
		
	};
	
	/*给定一个Bitmap，进行保存*/
	public void saveJpeg(Bitmap bm){
		String savePath = "/mnt/sdcard/rectPhoto/";
		File folder = new File(savePath);
		if(!folder.exists()) //如果文件夹不存在则创建
		{
			folder.mkdir();
		}
		long dataTake = System.currentTimeMillis();
		String jpegName = savePath + dataTake +".jpg";
		MLog.i("哈哈", "saveJpeg:jpegName--" + jpegName);
		//File jpegFile = new File(jpegName);
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);

			//			//如果需要改变大小(默认的是宽960×高1280),如改成宽600×高800
			//			Bitmap newBM = bm.createScaledBitmap(bm, 600, 800, false);

			bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			MLog.i("哈哈", "saveJpeg：存储完毕！");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			MLog.i("哈哈", "saveJpeg:存储失败！");
			e.printStackTrace();
		}
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

}
