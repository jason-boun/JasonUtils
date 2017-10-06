package com.jason.jasonutils.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StatFs;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Formatter;
import android.text.format.Time;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.R;
import com.jason.jasonutils.collection.SetDemo;
import com.jason.jasonutils.filter.PersonBean;
import com.jason.jasonutils.pwdlock.Constant;


public class Utils {
	
	/**
	 * 将秒时间转换成日时分格式
	 * @param lasttime
	 * @return
	 */
	public String getLasttime(String lasttime) 
	{
		StringBuffer result = new StringBuffer();
		if (StringUtils.isNumericSpace(lasttime)) 
		{
			int time = Integer.parseInt(lasttime);
			int day = time / (24 * 60 * 60);
			result.append(day).append("天");
			if (day > 0) {
				time = time - day * 24 * 60 * 60;
			}
			int hour = time / 3600;
			result.append(hour).append("时");
			if (hour > 0) {
				time = time - hour * 60 * 60;
			}
			int minute = time / 60;
			result.append(minute).append("分");
		}
		return result.toString();
	}
	/**
	 * 毫秒转换为格式：00:00:00
	 * @param time
	 * @return
	 */
	public static String handleTime(long time){
		time = time/1000;
		int h = (int)(time/(60*60));
		int m = (int)(time%(60*60))/60;
		int s = (int)(time%(60*60)%60);
		return h+":"+(m<10?"0"+m:m)+":"+(s<10?"0"+s:s);
	}
	
	/**
	 * 如果是当天的时间，则显示time；如果非当天的时间，则显示日期。
	 * @param context
	 * @param time
	 * @return
	 */
	public static String formatTime(Context context, long time){
		Time todayTime = new Time();
		todayTime.setToNow();
		todayTime.hour = 0;
		todayTime.minute = 0;
		todayTime.second = 0;
		long datumTime = todayTime.toMillis(false);
		if(time-datumTime >0 && time-datumTime< DateUtils.DAY_IN_MILLIS){
			return DateFormat.getTimeFormat(context).format(time);
		}else{
			return DateFormat.getDateFormat(context).format(time);
		}
	}
	/**
	 * 判断是否当天的时间。
	 * @param context
	 * @param time
	 * @return
	 */
	public static boolean isTodayTime(long time){
		Time todayTime = new Time();
		todayTime.setToNow();
		todayTime.hour = 0;
		todayTime.minute = 0;
		todayTime.second = 0;
		long datumTime = todayTime.toMillis(false);
		return time-datumTime >0 && time-datumTime< DateUtils.DAY_IN_MILLIS ? true:false;
	}
	
	/**
	 * 获取SD卡总大小
	 * @param context
	 * @return
	 */
	public static String getSDSize(Context context){
		File dir = Environment.getExternalStorageDirectory();
		StatFs statFs = new StatFs(dir.getPath());
		
		long blockSize = statFs.getBlockSize();
		long blockCount = statFs.getBlockCount();
		long totalSize =  blockSize * blockCount;
		
		String totalSizeStr = Formatter.formatFileSize(context, totalSize);
		return totalSizeStr;
	}
	
	/**
	 * 获取SD卡可用大小
	 * @param context
	 * @return
	 */
	public static String getSDAvailableSize(Context context){
		File dir = Environment.getExternalStorageDirectory();
		StatFs statFs = new StatFs(dir.getPath());
		
		long blockSize = statFs.getBlockSize();
		long blockCount = statFs.getAvailableBlocks();
		long totalAvailableSize =  blockSize * blockCount;
		
		String availableSizeStr = Formatter.formatFileSize(context, totalAvailableSize);
		return availableSizeStr;
	}
	
    /**
	 * 隐藏软键盘   
	 */
	public static void keyBoardHindden(Activity act){
		((InputMethodManager)act.getSystemService(act.INPUT_METHOD_SERVICE))
		.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);  
	}
	
	/**
	 * 隐藏键盘，不依赖控件的初始化
	 * @param context
	 */
	public static void keyBoardHindden2(Activity context){
		context.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); 
	}
	
	/**
	 * 隐藏软键盘，
	 * @param act
	 */
	private void keyBoardHindden3(Activity act) {
		if (act.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (act.getCurrentFocus() != null){
				InputMethodManager manager = (InputMethodManager)act.getSystemService(Context.INPUT_METHOD_SERVICE);
				manager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
	
	/**
	 * 显示软键盘
	 */
	public static void keyBoardShow(Context context, View view){
		((InputMethodManager)context.getSystemService(context.INPUT_METHOD_SERVICE)).showSoftInput(view, 0); 
	}
	
	/**
	 * 通过系统接口创建快捷图标
	 * @param ctx
	 * @param appName
	 * @param clazz
	 * @param resourceId
	 * @return
	 */
	public static boolean createShortCut(Activity ctx,String appName, Class<?> clazz,int resourceId){
		boolean create = false;
		if(!isShortCutExist(ctx, appName)){
			
			//相应点击
			Intent click = new Intent();
			click.setAction(Intent.ACTION_MAIN);
			click.addCategory(Intent.CATEGORY_LAUNCHER);
			click.setComponent(new ComponentName(ctx, clazz));
			
			//创建图标
			Intent shortCut = new Intent();
			shortCut.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
			ShortcutIconResource iconResource = Intent.ShortcutIconResource.fromContext(ctx, resourceId);
			shortCut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);
			shortCut.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
			shortCut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,click);
			
			ctx.sendBroadcast(shortCut);
			create = true;
		}
		return create;
	}
	private static boolean isShortCutExist(Context ctx,String appName){
		boolean exist = false;
		Uri uri = Uri.parse("content://com.android.launcher2.settings/favorites");
		String selection = " title = ?";
		String[] selectionArgs = new String[]{appName};
		Cursor cursor = ctx.getContentResolver().query(uri, null, selection, selectionArgs, null);
		if(cursor.moveToFirst()){
			exist = true;
		}
		cursor.close();
		return exist;
	}
	/**
	 * 判断字符串是否数字
	 * @param str
	 * @return
	 */
	public static boolean isIntegerStr(String str){
		return str!=null && str.matches("^-?[0-9]+");
		//"-?[0-9]+.?[0-9]+"
		//[0-9]+
		//"^-?[0-9]+"
	}
	
	
	/**
	 * 中文正则
	 * @param str
	 * @return
	 */
	public static boolean isChineseStr(String str){
		//Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]+");
		//Matcher matcher = pattern.matcher(str);
		//return str!=null && matcher.matches();
		
		return str.matches("[\u4e00-\u9fa5]+");
	}
	
	/**
	 * 字母、数字、下划线正则
	 * @param str
	 * @return
	 */
	public static boolean lawful(String str){
		return str!=null && str.matches("[A-Za-z0-9_]+");
	}
	
	/**
	 * 获取屏幕分辨率
	 * @param context
	 * @return
	 */
	public static String getScreenResolution(Context context ){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		return display.getWidth() +" * "+display.getHeight();
	}
	
	
	/**
	 * 倒计时
	 * @param millisInFuture
	 * @param countDownInterval
	 */
	public static void myCountDownTimer(long millisInFuture, long countDownInterval, final TextView tv_show_info){
		
		new CountDownTimer(millisInFuture,countDownInterval) {
			@Override
			public void onTick(long millisUntilFinished) {
				tv_show_info.setText("还剩：" + millisUntilFinished+"毫秒");
				if(millisUntilFinished <= 5000){
					changeCountDownTimerIntervalTime(this, 500);//小于5s后提示频率加快
				}
			}
			@Override
			public void onFinish() {
				tv_show_info.setText("时间到");
			}
		}.start();
		
	}
	
	/**
	 * 反射父类CountDownTimer的mCountdownInterval字段，动态改变回调频率
	 * @param time
	 * @return
	 */
	public static boolean changeCountDownTimerIntervalTime(CountDownTimer myTimer, long time){
		 try {
		    Class<?> clazz = Class.forName("android.os.CountDownTimer");
		    Field field = clazz.getDeclaredField("mCountdownInterval");
		    field.setAccessible(true);
		    field.set(myTimer, time);
		    return true;
		    } catch (Exception e) {
		        MLog.i("Ye", "反射类android.os.CountDownTimer失败： " + e);
		        return false;
		    }	
	}
	
	/**
	 *  JSON串与JSON对象之间的相互转换
	 */
	public static void getJson(){
		try {
			org.json.JSONObject js1 = new org.json.JSONObject();
			js1.put("name", "hhahah");
			js1.put("pwd", "123456");
			System.out.println(js1.toString());
			
			org.json.JSONObject js2 = new org.json.JSONObject(js1.toString());
		
			String result = new org.json.JSONObject().put("name", js2.get("name")).toString();
			System.out.println(result);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 禁止在某个Activity中截屏
	 * @param act
	 */
	public static void preventCapture(Activity act){
		act.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
	}
	
	public static void CaptureScreen(){
		
	}
	
	/**
	 * 重新覆写PasswordTransformationMethod类，使EditText获取的文本以指定的文本格式显示
	 * @param editText
	 */
	public static void setEditTextPWDFormat(EditText editText){
		editText.setTransformationMethod(new MyPasswordTransformationMethod());
	}
	
	private static class MyPasswordTransformationMethod extends PasswordTransformationMethod{
		@Override
		public CharSequence getTransformation(CharSequence source, View view) {
			return new PasswordCharSequence(source);
		}
		class PasswordCharSequence implements CharSequence {  
		    private CharSequence mSource;  
		    public PasswordCharSequence(CharSequence source) {  
		        mSource = source; // Store char sequence  
		    }  
		    public char charAt(int index) {
		    	char ch = mSource.charAt(index);
		    	if(ch=='a' || ch =='b'){
		    		return mSource.charAt(index);
		    	}else{
		    		return '●'; // This is the important part
		    	}
		    }  
		    public int length() {  
		        return mSource.length(); // Return default  
		    }  
		    public CharSequence subSequence(int start, int end) {  
		        return mSource.subSequence(start, end); // Return default  
		    }  
		}
	}
	
	/**
	 * 解密
	 * @param decStr
	 * @return
	 */
	public static String decryptString(String decStr) {
		try {
			DESKeySpec keySpec = new DESKeySpec(Constant.PASSWORD_ENC_SECRET.getBytes("UTF-8"));
			SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);

			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] plainTextPwdBytes = cipher.doFinal(Base64.decode(decStr, Base64.DEFAULT));
			return new String(plainTextPwdBytes);
		} catch (Exception e) {
		}
		return decStr;
	}

	/**
	 * 加密
	 * @param encStr
	 * @return
	 */
	public static String encryptString(String encStr) {
		try {
			DESKeySpec keySpec = new DESKeySpec(Constant.PASSWORD_ENC_SECRET.getBytes("UTF-8"));
			SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);

			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			String encrypedPwd = Base64.encodeToString(cipher.doFinal(encStr.getBytes("UTF-8")), Base64.DEFAULT);
			return encrypedPwd;
		} catch (Exception e) {
		}
		return encStr;
	}
	
	/**
	 * 获得属于桌面的应用的应用包名称
	 * @return 返回包含所有包名的字符串列表
	 */
	public static List<String> getHomes(Context context) {
		List<String> names = new ArrayList<String>();
		List<ResolveInfo> resolveInfo = context.getPackageManager().queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), PackageManager.MATCH_DEFAULT_ONLY);
		for(ResolveInfo ri : resolveInfo){
			names.add(ri.activityInfo.packageName);
			System.out.println(ri.activityInfo.packageName);
		}
		return names;
	}
	/**
	 * 判断当前界面是否是桌面
	 */
	public static final String laucher_package_name = "com.android.settings";
	public static boolean isHome2(Context context){
		ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		return laucher_package_name.equals((rti.get(0).topActivity.getPackageName()));
	}
	/**
	 * 使用排除法，如果当前界面不是所有机子上安装的包名，那么它就是LAUCHER界面了
	 * @param context
	 * @return
	 */
	public static boolean isHome(Context context){
		ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		return !getHomes(context).contains((rti.get(0).topActivity.getPackageName()));
	}
	//<uses-permission android:name="android.permission.GET_TASKS" />
	
	/**
	 * SD卡重新挂载广播
	 * @param context
	 */
	public static void notifySDCardReMounted(Context context){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
		intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
		context.sendBroadcast(intent);
	}
	public static String savePicPath = Environment.getExternalStorageDirectory().toString()+ "/DCIM/Camera"; 
	/**
	 * 保存图片到本地
	 * @param context
	 * @param path
	 */
	public static void savePic2Local(final Context context, final String path) {
		if(path==null){
			return ;
		}else{
			new AsyncTask<String, Void, Boolean>(){
				@Override
				protected Boolean doInBackground(String... params) {
					try {
						String picName = "JasonUtils_"+ getDateFormatStr() + ".jpg";
						FileInputStream fis = new FileInputStream(params[0]);
				        FileOutputStream fos = new FileOutputStream(new File(savePicPath, picName));
				        int len = 0;
				        byte[]buffer = new byte[1024];
				        while((len = fis.read(buffer))!=-1){
				        	fos.write(buffer, 0, len);
				        	fos.flush();
				        }
				        fis.close();
		                fos.close();
						return true;
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}
				@Override
				protected void onPostExecute(Boolean result) {
					super.onPostExecute(result);
					if(result){
						notifySDCardReMounted(context);
						Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
					}
				}
			}.execute(path);
		}
	}
	
	/**
	 * 判断是否是手机号码
	 * @param number
	 * @return
	 */
	public static boolean isMobileNumber(String number){
		Pattern pattern = Pattern.compile("^[1][3578]\\d{9}$");
		Matcher matcher = pattern.matcher(number);
		return matcher.matches();
	}
	
	/**
	 * md5的加密
	 * @param password
	 * @return
	 */
	public  static String md5Encode(String password){
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] result = digest.digest(password.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : result) {
				int number = b & 0xff; //加盐 | 0x23
				String str = Integer.toHexString(number);
				if(str.length()==1){
					sb.append("0");
				}
				sb.append(str);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 判断服务是否运行
	 * @param context
	 * @param clazz
	 * @return
	 */
	public static boolean isServiceRunning(Context context, Class serviceClass){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServicesInfos = am.getRunningServices(1000);
		for(RunningServiceInfo info : runningServicesInfos){
			if(serviceClass.getName().equals(info.service.getClassName())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 子线程弹出Toast
	 * @param act
	 */
	public static void ThreadShowToast(final Activity act,final String tips){
		act.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(act,tips,0).show();
			}
		});
	}
	
	/**
	 * 获取时间格式字符串
	 * @return：yyyyMMdd_HHmmss
	 */
	public static String getDateFormatStr(){
		return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	}
	
	
	static String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	/**
	 * 获取随机字符串
	 * @param len 需要获取的长度
	 * @return [a-z][A-Z][0-9]
	 */
	public static String getRandomStr(int len){
		int BaseLen = base.length();
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for(int i=0;i<len;i++){
			sb.append(base.charAt(random.nextInt(BaseLen)));
		}
		return sb.toString();
	}
	
	/**
	 * 产生模拟测试数据
	 */
	public static ArrayList<PersonBean> generateData(){
		ArrayList<PersonBean> oriList = new ArrayList<PersonBean>();
		PersonBean personBean = null;
		Random random = new Random();
		for(int i=0;i<30;i++){
			personBean = new PersonBean();
			personBean.setName(getRandomStr(5));
			personBean.setAge(random.nextInt(35));
			personBean.setSalary(String.valueOf(8000+random.nextInt(4000)));
			oriList.add(personBean);
		}
		return oriList;
	}
	
	/**
	 * 通过View获取Context来在子线程更新UI
	 */
	public static void useViewUpdateThreadUI(final View view){
		new Thread(){
			public void run() {
				view.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(view.getContext(), "通过View获取Context来在子线程更新UI",Toast.LENGTH_SHORT).show();
					}
				});
			};
		}.start();
	}
	
	public static boolean isStrEmpty(String str) {
		return null == str || "".equals(str.trim());
	}
	
	/**
	 * CharSequence text = "碰到星的文本★，就显示一个表情图片R.drawable.ee_15，现在给你一个星★"; 
	 */
	public static SpannableStringBuilder textViewShowImage(Context ctx,CharSequence text,String regex){
		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		Matcher matcher = Pattern.compile(regex).matcher(text);
		while(matcher.find()){
			builder.setSpan(new ImageSpan(ctx, R.drawable.ee_15), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return builder;
	}
	
	public static Spannable textViewShowImage2(Context ctx,CharSequence text,String regex){
		Spannable spannable = Spannable.Factory.getInstance().newSpannable(text);
		Matcher matcher = Pattern.compile(regex).matcher(text);
		while(matcher.find()){
			spannable.setSpan(new ImageSpan(ctx, R.drawable.ee_15), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return spannable;
	}
	
	/**
	 * 获取一个类的全名（包名+类名）
	 */
	public static String getCanonicalName(Class<?> clazz){
		return clazz.getCanonicalName();
	}
	/**
	 * 获取一个类的类名（不含包名）
	 */
	public static String getSimpleName(Class<?> clazz){
		return clazz.getSimpleName();
	}
	
	/**
	 * 判断一个字符串是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isDigitsByStrings(String str) {
		int len = str.length();
		for (int i = 0; i < len; i++) {
			if (!Character.isDigit(str.charAt(i)))
				return false;
		}
		return true;
	}
	
	/**
	 * 获得圆角图片的方法
	 * @param bitmap
	 * @param roundPx
	 * @param roundPy
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx, float roundPy) {

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
	
}
