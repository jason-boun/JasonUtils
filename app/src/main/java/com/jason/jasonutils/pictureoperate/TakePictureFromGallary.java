package com.jason.jasonutils.pictureoperate;

import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.jason.jasonutils.R;

public class TakePictureFromGallary extends Activity {
	
	private Button bt_take_pic_from_gallary_demo;
	private ImageView iv_take_pic_from_gallary_show;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_picture_from_gallary_activity);
		
		init();
	}
	
	private void init() {
		iv_take_pic_from_gallary_show = (ImageView) findViewById(R.id.iv_take_pic_from_gallary_show);
		bt_take_pic_from_gallary_demo = (Button) findViewById(R.id.bt_take_pic_from_gallary_demo);
		bt_take_pic_from_gallary_demo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				takePicIntent();
			}
		});
	}

	// 从系统的图库里面 获取一张照片
	public void takePicIntent() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.PICK");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("image/*");
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			Uri uri = data.getData();// 获取到系统图库返回回来图片的uri
			try {
				InputStream is = getContentResolver().openInputStream(uri);
				int windowWidth = getWindowManager().getDefaultDisplay().getWidth();
				int windowHeight = getWindowManager().getDefaultDisplay().getHeight();
				BitmapFactory.Options opts = new Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(is, null, opts);
				int bitmapHeight = opts.outHeight;
				int bitmapWidth = opts.outWidth;

				if (bitmapHeight > windowHeight || bitmapWidth > windowWidth) {
					int scaleX = bitmapWidth/windowWidth;
					int scaleY = bitmapHeight/windowHeight;
					if(scaleX>scaleY){
						opts.inSampleSize = scaleX;
					}else{
						opts.inSampleSize = scaleY;
					}
				}else{
					opts.inSampleSize = 1;
				}
				opts.inJustDecodeBounds = false;
				is = getContentResolver().openInputStream(uri);//注意流的操作，重新得到流
				Bitmap bitmap = BitmapFactory.decodeStream(is, null, opts);
				iv_take_pic_from_gallary_show.setImageBitmap(watermarkBitmap(bitmap,null,"哈哈哈"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 添加文字生成水印
	 * @param srcBitmap
	 * @return
	 */
	public Bitmap waterMarked(Bitmap srcBitmap, String martStr){
		Bitmap destBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
		Canvas canvas = new Canvas(destBitmap);
		canvas.drawBitmap(srcBitmap, new Matrix(), new Paint());
		Paint paint = new Paint();
		paint.setTextSize(20);
		paint.setColor(Color.RED);
		paint.setTypeface(Typeface.create("", Typeface.ITALIC));
		canvas.drawText(martStr, srcBitmap.getWidth()*2/3 +10, srcBitmap.getHeight()-20, paint);
		return destBitmap;
	}
	  
    /**
     * 图片叠加生成水印
     * @param src
     * @param waterMark
     * @return
     */
    public Bitmap waterMarked(Bitmap src ,Bitmap waterMark){
        Bitmap photoMark = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(photoMark);
        canvas.drawBitmap(src, 0, 0, null);
        canvas.drawBitmap(waterMark, src.getWidth() - waterMark.getWidth(), src.getHeight() - waterMark.getHeight(), null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return photoMark;
    }
	
	/**
	 * 获取图片缩小的图片
	 * @param src
	 * @param max
	 * @return
	 */
    public static Bitmap scaleBitmap(String src,int max) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;        
        BitmapFactory.decodeFile(src, options); 
        max=max/10;
        int be = options.outWidth / max;
         if(be%10 !=0)
          be+=10;
         be=be/10;
         if (be <= 0)
          be = 1;
        options.inSampleSize = be;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(src, options);        
    }
    /**
     *  加水印
     * @param src
     * @param watermark
     * @param title
     * @return
     */
    public static Bitmap watermarkBitmap(Bitmap src, Bitmap watermark, String title) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight(); 
        Bitmap newb= Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src    
        Paint paint=new Paint();
        //加入图片
        if (watermark != null) {
            int ww = watermark.getWidth();
            int wh = watermark.getHeight();
            paint.setAlpha(50);
            cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, paint);// 在src的右下角画入水印            
        }
        //加入文字
        if(title!=null) {
            String familyName ="宋体";
            Typeface font = Typeface.create(familyName,Typeface.BOLD);            
            TextPaint textPaint=new TextPaint();
            textPaint.setColor(Color.RED);
            textPaint.setTypeface(font);
            textPaint.setTextSize(22);
            //这里是自动换行的
            StaticLayout layout = new StaticLayout(title,textPaint,w,Alignment.ALIGN_NORMAL,1.0F,0.0F,true);
            layout.draw(cv);
            //文字就加左上角算了
            //cv.drawText(title,0,40,paint); 
        }
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 存储
        return newb;
    }
}
