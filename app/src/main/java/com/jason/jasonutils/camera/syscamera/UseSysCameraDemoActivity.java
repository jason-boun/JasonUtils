package com.jason.jasonutils.camera.syscamera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.jason.jasonutils.R;

public class UseSysCameraDemoActivity extends Activity implements OnClickListener {
	
	private Button bt_camera_usesys_camera_getpic;
	private ImageView iv_camera_usesys_camera_showpic;
	private Uri picUri;
	private File picFile;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.use_syscamera_to_capture);
		init();
	}

	private void init() {
		bt_camera_usesys_camera_getpic = (Button) findViewById(R.id.bt_camera_usesys_camera_getpic);
		iv_camera_usesys_camera_showpic = (ImageView) findViewById(R.id.iv_camera_usesys_camera_showpic);
		bt_camera_usesys_camera_getpic.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_camera_usesys_camera_getpic:
			useSysCamera2getPic();
			break;
		}
	}
	
	private void useSysCamera2getPic(){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		picFile = getFile(MEDIA_TYPE_IMAGE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFile));
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		Bitmap bitmap = getPicBitmapData(picFile);
		Bitmap bitmap = getPicBitmapData(Uri.fromFile(picFile));
		if(bitmap!=null){
			iv_camera_usesys_camera_showpic.setImageBitmap(bitmap);
		}
	}
	
	public Bitmap getPicBitmapData(File file){
		 return BitmapFactory.decodeFile(file.getPath(), getOption(this, Uri.fromFile(file)));
	}
	
	/**
	 * 通过Uri路径获取信息
	 * @param picUri
	 * @return
	 */
	public Bitmap getPicBitmapData(Uri picUri){
		try {
			InputStream is = getContentResolver().openInputStream(picUri);
			return BitmapFactory.decodeStream(is, null, getOption(this, picUri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * 创建图片路径和名称
	 * @param fileType
	 * @return
	 */
	private File getFile(int fileType){
		File mediaFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"MyCameraApp");
		if(!mediaFileDir.exists()){
			if(!mediaFileDir.mkdirs()){
				return null;
			}
		}
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.CHINA).format(new Date());
		File mediaFile ;
		if(fileType == MEDIA_TYPE_IMAGE){
			mediaFile = new File(mediaFileDir.getPath() + File.separator + "IMG_" + timeStamp +".jpg");
		}else if(fileType == MEDIA_TYPE_VIDEO){
			mediaFile = new File(mediaFileDir.getPath() + File.separator + "VIDEO_" + timeStamp +".mp4");
		}else{
			return null;
		}
		return mediaFile;
	}
	
	/**
	 * 图片缩放所需的参数Option
	 * @param act
	 * @param fileUri
	 * @return
	 */
	public static BitmapFactory.Options getOption (Activity act,Uri fileUri){
		int wmWidth = act.getWindowManager().getDefaultDisplay().getWidth();
		int wmHeight = act.getWindowManager().getDefaultDisplay().getHeight();
		String path = fileUri.getPath();
		Options option = new Options();
		option.inJustDecodeBounds = true;
		if(new File(path).exists()){
			BitmapFactory.decodeFile(path, option);
		}else {
			BitmapFactory.decodeResource(act.getResources(), R.drawable.fixpicture, option);
		}
		int bitmapWith = option.outWidth;
		int bitmapHeight = option.outHeight;
		
		if(bitmapWith>wmWidth || bitmapHeight>wmHeight){
			int xScale = bitmapWith/wmWidth;
			int yScale = bitmapHeight / wmHeight;
			if(xScale>yScale){
				option.inSampleSize = xScale;
			}else{
				option.inSampleSize = yScale;
			}
		}else{
			option.inSampleSize = 1;
		}
		option.inJustDecodeBounds = false;
		return option;
	}

}
