package com.jason.jasonutils.fixpicture;

import java.io.ByteArrayInputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import com.jason.jasonutils.tools.MLog;
import android.widget.ImageView;

import com.jason.jasonutils.R;
import com.jason.jasonutils.bean.TestPerson;

public class FixPictureActivity extends Activity {
	
	private ImageView iv_show_photo;
	private TestPerson person;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fixpicture_activity);
		
		init();
		//setGroupPhoto();
		
		//合成群头像：N张图片合成技术
		Bitmap[] bitmaps =new Bitmap[4]  ;
		for(int i=0;i<4;i++){
			bitmaps[i] = BitmapFactory.decodeResource(getResources(),R.drawable.icon);
		}
		Bitmap bitmap = mergeBitmap(bitmaps);
		iv_show_photo.setImageBitmap(bitmap);
	}

	/**
	 * 调用方法，合成图片
	 */
	private void setGroupPhoto() {
		Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.fixpicture);
		Bitmap zoomBitmap = zoomBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher), (bm.getWidth() / 4), (bm.getHeight() / 4));
		Bitmap bitmap = createBitmap(bm, zoomBitmap);
		iv_show_photo.setImageBitmap(bitmap);
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		iv_show_photo = (ImageView) findViewById(R.id.iv_show_photo);
		person = new TestPerson();
		
		if(person!=null){
			byte[]photo = person.photo;
			if (photo != null) {
				ByteArrayInputStream bais = new ByteArrayInputStream(photo);	 		      
				iv_show_photo.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeStream(bais), 30, 30));
			}else{
				iv_show_photo.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.fixpicture), 10, 10));
			}
		}
	}

	/**
	 * 获得圆角图片的方法
	 * @param bitmap
	 * @param roundPx
	 * @param roundPy
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx,float roundPy) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPy, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
	
	/**
	 * 合并图片
	 * @param src
	 * @param watermark
	 * @return
	 */
	private Bitmap createBitmap(Bitmap src, Bitmap watermark) {
        String tag = "createBitmap";
        MLog.i(tag, "create a new bitmap");
        if (src == null) {
                return null;
        }

        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        // draw src into
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        // draw watermark into
        // 在src的右下角画watermark
        cv.drawBitmap(watermark, w - ww - 5, h - wh - 5, null);//设置ic_launcher的位置
        // cv.drawBitmap(watermark, 0, 0, null);
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // store
        cv.restore();// 存储
        return newb;

	}
	
	/**
	 * 缩放图片
	 * @param src
	 * @param destWidth
	 * @param destHeigth
	 * @return
	 */
	private Bitmap zoomBitmap(Bitmap src, int destWidth, int destHeigth) {
        String tag = "lessenBitmap";
        if (src == null) {
                return null;
        }
        int w = src.getWidth();// 源文件的大小
        int h = src.getHeight();

        // calculate the scale - in this case = 0.4f
        float scaleWidth = ((float) destWidth) / w;// 宽度缩小比例
        float scaleHeight = ((float) destHeigth) / h;// 高度缩小比例
        MLog.i(tag, "bitmap width is :" + w);
        MLog.i(tag, "bitmap height is :" + h);
        MLog.i(tag, "new width is :" + destWidth);
        MLog.i(tag, "new height is :" + destHeigth);
        MLog.i(tag, "scale width is :" + scaleWidth);
        MLog.i(tag, "scale height is :" + scaleHeight);
        Matrix m = new Matrix();// 矩阵
        m.postScale(scaleWidth, scaleHeight);// 设置矩阵比例
        Bitmap resizedBitmap = Bitmap.createBitmap(src, 0, 0, w, h, m, true);// 直接按照矩阵的比例把源文件画入进行
        return resizedBitmap;
	}
	
	/**
	 * 将多张位图拼接成一张
	 * @param bitmaps
	 * @return
	 */
	private Bitmap add2Bitmap(Bitmap[] bitmaps) {
        int width =bitmaps[0].getWidth()*2 ;
        int height = Math.max(bitmaps[0].getHeight(), bitmaps[1].getHeight())*2;
		Bitmap newBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(newBitmap); 
		canvas.drawBitmap(bitmaps[0], 0, 0, null);
		canvas.drawBitmap(bitmaps[1], width/2, 0, null);
		canvas.drawBitmap(bitmaps[2], 0, height/2, null);
		canvas.drawBitmap(bitmaps[3], width/2, height/2, null);
		return newBitmap;
	}
	
	/**
	 * 将多张位图拼接成一张
	 * @param bitmaps
	 * @return
	 */
	public static Bitmap mergeBitmap(Bitmap[] bitmaps) {
		int width = bitmaps[0].getWidth();
		int height = bitmaps[0].getHeight();
		Bitmap newBitmap ;
		Canvas canvas ;
		if(bitmaps.length==1){
			return bitmaps[0];
		}else if(bitmaps.length==2){
			width = width*2;
			newBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			canvas = new Canvas(newBitmap);
			canvas.drawBitmap(bitmaps[0], 0, 0, null);
			canvas.drawBitmap(bitmaps[1], width/2, 0, null);
			return newBitmap;
		}else{
			width = width*2;
			height = height*2;
			newBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			canvas = new Canvas(newBitmap); 
			canvas.drawBitmap(bitmaps[0], width/4, 0, null);
			canvas.drawBitmap(bitmaps[1], 0, height/2, null);
			canvas.drawBitmap(bitmaps[2], width/2, height/2, null);
			return newBitmap;
		}
	}
}
