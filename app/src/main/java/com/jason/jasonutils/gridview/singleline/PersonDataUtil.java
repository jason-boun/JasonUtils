package com.jason.jasonutils.gridview.singleline;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

import com.jason.jasonutils.R;


public class PersonDataUtil {
	
	public static final String SELECTED_PERSON_LIST = "selected_person_list";
	public static final String INTENT_FROM_TYPE = "Intent_From_type";
	public static final String INTENT_FROM_SINGLE_GRIDVIEW = "Intent_From_single_gridview";
	public static final String INTENT_FROM_COMPLEX_GRIDVIEW = "intent_from_complex_gridview";
	
	public static String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	/**
	 * 获取制定长度的随机字符串
	 * @param len
	 * @return
	 */
	public static String getRandomStr(int len){
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<len;i++){
			sb.append(base.charAt(random.nextInt(base.length())));
		}
		return sb.toString();
	}
	
	/**
	 * 获取数据
	 * @param listSize：需要生成多少个随机数据
	 * @param startIndex：数据对象的name开头名字从哪个数字开始
	 * @return
	 */
	public static List<Person> dataFactory(int listSize,int startIndex){
		List<Person> dataSource = new ArrayList<Person>();
		Person person  = null;
		for(int i=1; i<=listSize;i++){
			person = new Person();
			person.setJid(getRandomStr(6)+"@163.com");
			person.setNickname(startIndex + i + "_" + person.getJid().substring(0,person.getJid().indexOf("@")));
			person.setName(person.getNickname());
			person.setChecked(false);
			dataSource.add(person);
		}
		return dataSource;
	}
	
	/**
	 * 头像byte[]数据
	 * @param context
	 * @param ResouceId
	 * @return
	 */
	public static byte[] getPhotoData(Context context, int ResouceId){
		Bitmap btmap = null;
		if(ResouceId > 0){
			btmap = BitmapFactory.decodeResource(context.getResources(), ResouceId);
		}else{
			btmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.user_bg);
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		btmap.compress(CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}
	/**
	 * 头像Bitmap数据
	 * @param context
	 * @param ResouceId
	 * @return
	 */
	public static Bitmap getBitmapData(Context context, int ResouceId, boolean isRounded){
		byte[] photoData = getPhotoData(context, ResouceId);
		Bitmap bitmap = BitmapFactory.decodeByteArray(photoData, 0, photoData.length, null);
		if(isRounded){
			bitmap = getRoundedCornerBitmap(bitmap, 10, 10);
		}
		return bitmap;
	}
	
	/**
	 * 获取默认Bitmap数据
	 * @param context
	 * @param isRounded
	 * @return
	 */
	public static Bitmap getDefaultBitmap(Context context,boolean isRounded){
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.user_bg);
		return bitmap = isRounded ? getRoundedCornerBitmap(bitmap, 10, 10) : bitmap;
	}
	
	/**
	 * 获得圆角图片的方法
	 * @param bitmap
	 * @param roundPx
	 * @param roundPy
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx,float roundPy) {
        Bitmap resultBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap);

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

        return resultBitmap;
    }
	
    /** 
     * dip 转为 px
     */   
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }

}
