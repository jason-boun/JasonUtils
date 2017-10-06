package com.jason.jasonutils.pictureoperate;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.jason.jasonutils.R;

public class BitmapMatrixDemo extends Activity implements OnClickListener {
	
	private Button bt_picture_matrix_resize_demo;
	private ImageView iv_picture_matrix_src,iv_picture_matrix_dest;
	//private String picPath =Environment.getExternalStorageDirectory()+"/"+"test.jpg";
	private Bitmap srcBitmap = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picture_matrix_operate_demo_activity);
		init();
	}

	private void init() {
		bt_picture_matrix_resize_demo = (Button) findViewById(R.id.bt_picture_matrix_resize_demo);
		bt_picture_matrix_resize_demo.setOnClickListener(this);
		
		iv_picture_matrix_src = (ImageView) findViewById(R.id.iv_picture_matrix_src);
		iv_picture_matrix_dest = (ImageView) findViewById(R.id.iv_picture_matrix_dest);
		//srcBitmap = BitmapOptionDemo.loadPicture(this, picPath);
		srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_tool_10);
		iv_picture_matrix_src.setImageBitmap(srcBitmap);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_picture_matrix_resize_demo:
			mirrorPicture(srcBitmap);
			break;
		}
	}
	
	/**
	 * 尺寸变换
	 * @param srcBitmap
	 */
	public void resizePicture(Bitmap srcBitmap){
		Bitmap baseBitmap = Bitmap.createBitmap(srcBitmap.getWidth()*2, srcBitmap.getHeight()/2, srcBitmap.getConfig());
		Canvas canvas = new Canvas(baseBitmap);
		Matrix matrix = new Matrix();
		/*matrix.setValues(new float[] {
				2, 0, 0,
				0, 0.5f, 0,
				0, 0, 1
			});*/
		matrix.setScale(2.0f, 0.5f);
		canvas.drawBitmap(srcBitmap, matrix, new Paint());
		iv_picture_matrix_dest.setImageBitmap(baseBitmap);
	}
	
	/**
	 * 角度旋转
	 */
	public void rotakePicture(Bitmap srcBitmap){
		Matrix matrix = new Matrix();
		matrix.setRotate(45, srcBitmap.getWidth()/2, srcBitmap.getHeight()/2);
		
		Bitmap source = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Config.ARGB_8888);
		Bitmap baseBitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
		Canvas canvas = new Canvas(baseBitmap);
		
		canvas.drawBitmap(srcBitmap, matrix, new Paint());
		iv_picture_matrix_dest.setImageBitmap(baseBitmap);
	}
	
	/**
	 * 位移
	 */
	public void translatePicture(Bitmap srcBitmap){
		Matrix matrix = new Matrix();
		//设置位移
		matrix.setTranslate(1.5f,-10);
		Bitmap baseBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, false);
		Canvas canvas = new Canvas(baseBitmap);
		canvas.drawBitmap(baseBitmap, matrix, new Paint());
		iv_picture_matrix_dest.setImageBitmap(baseBitmap);
	}
	
	/**
	 * 镜像变换
	 */
	public void mirrorPicture(Bitmap srcBitmap){
		Bitmap source = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
		Matrix matrix = new Matrix();

		//把x轴的值 变成负的
		matrix.setScale(1, -1);
		matrix.postTranslate(0, source.getHeight()/2);
		
		Bitmap baseBitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
		Canvas canvas = new Canvas(baseBitmap);
		canvas.drawBitmap(srcBitmap, matrix, new Paint());
		
		iv_picture_matrix_dest.setImageBitmap(baseBitmap);

	}
	

}
