package com.jason.jasonutils.pictureoperate;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.R;

public class BitmapOptionDemo extends Activity{

	private Button bt_bitmapoption_demo,bt_picture_exifinfo_demo;
	private ImageView iv_bitmapoption_show;
	private TextView tv_pic_exif_info_show;
	private String picturePath;
	private Context ctx ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bitmap_option_demo_activity);
		init();
	}

	private void init() {
		picturePath = Environment.getExternalStorageDirectory()+"/"+"test.jpg";
		tv_pic_exif_info_show = (TextView) findViewById(R.id.tv_pic_exif_info_show);
		ctx = BitmapOptionDemo.this;
		bt_picture_exifinfo_demo = (Button) findViewById(R.id.bt_picture_exifinfo_demo);
		bt_picture_exifinfo_demo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getPictureExifInfo(picturePath);
			}
		});
		iv_bitmapoption_show = (ImageView) findViewById(R.id.iv_bitmapoption_show);
		bt_bitmapoption_demo = (Button) findViewById(R.id.bt_bitmapoption_demo);
		bt_bitmapoption_demo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				iv_bitmapoption_show.setImageBitmap(loadPicture((Activity) ctx, picturePath));
			}
		});
	}
	
	/**
	 * 加载图片优化，防止内存溢出
	 */
	public static Bitmap loadPicture(Activity context, String path){
		int wmWidth = context.getWindowManager().getDefaultDisplay().getWidth();
		int wmHeight = context.getWindowManager().getDefaultDisplay().getHeight();
		
		BitmapFactory.Options option = new Options();
		option.inJustDecodeBounds = true;
		if(new File(path).exists()){
			BitmapFactory.decodeFile(path, option);
		}else {
			BitmapFactory.decodeResource(context.getResources(), R.drawable.fixpicture, option);
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
		return BitmapFactory.decodeFile(path, option);
	}
	
	
	public void getPictureExifInfo(String picPaht) {
		try {
			if(!TextUtils.isEmpty(picPaht) && new File(picPaht).exists()){
				ExifInterface exifInfo = new ExifInterface("/sdcard/test.jpg");
				
				exifInfo.setAttribute(ExifInterface.TAG_MAKE, "SAMSUNG");
				exifInfo.setAttribute(ExifInterface.TAG_MODEL, "GT-9100");
				
				StringBuilder sb = new StringBuilder();
				sb.append("TAG_DATETIME=="+exifInfo.getAttribute(ExifInterface.TAG_DATETIME)+"\n");
				sb.append("TAG_MAKE=="+exifInfo.getAttribute(ExifInterface.TAG_MAKE)+"\n");//SAMSUNG
				sb.append("TAG_MODEL=="+exifInfo.getAttribute(ExifInterface.TAG_MODEL)+"\n");//GT-9100
				sb.append("TAG_IMAGE_WIDTH=="+exifInfo.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)+"\n");
				sb.append("TAG_IMAGE_LENGTH=="+exifInfo.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)+"\n");
				sb.append("TAG_ISO=="+exifInfo.getAttribute(ExifInterface.TAG_ISO)+"\n");
				sb.append("TAG_ORIENTATION=="+exifInfo.getAttribute(ExifInterface.TAG_ORIENTATION)+"\n");
				tv_pic_exif_info_show.setText("相片参数"+"\n"+sb.toString());
			}else{
				Toast.makeText(BitmapOptionDemo.this, "文件不存在", 0).show();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
