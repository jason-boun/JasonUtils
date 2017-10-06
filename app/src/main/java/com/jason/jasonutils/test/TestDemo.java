package com.jason.jasonutils.test;

import java.io.File;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.jason.jasonutils.JasonUtilsMainActivity;
import com.jason.jasonutils.R;
import com.jason.jasonutils.alertdialog.AlertDialogActivity;
import com.jason.jasonutils.contact.InsertSysContactTool;
import com.jason.jasonutils.contact.SystemContact;
import com.jason.jasonutils.dimesionalcode.DimensionaLCodeTool;
import com.jason.jasonutils.enhance.enumdemo.EnumConstant;
import com.jason.jasonutils.enhance.enumdemo.EnumTest;
import com.jason.jasonutils.greendao.dao.GreenDaoTest;
import com.jason.jasonutils.iostream.StreamTools;
import com.jason.jasonutils.location.LocationUtil;
import com.jason.jasonutils.scroller.ScrollerDemoActivity;
import com.jason.jasonutils.smile.SmileUtils;
import com.jason.jasonutils.sortsequence.PinYinUtil;
import com.jason.jasonutils.string.StringUtil;
import com.jason.jasonutils.toast.MyTimerToast;
import com.jason.jasonutils.tools.AnimationUtil;
import com.jason.jasonutils.tools.DateUtil;
import com.jason.jasonutils.tools.ImageViewUtil;
import com.jason.jasonutils.tools.LogUtil;
import com.jason.jasonutils.tools.MLog;
import com.jason.jasonutils.tools.PropertiesUtil;
import com.jason.jasonutils.tools.ScreenShotUtil;
import com.jason.jasonutils.tools.Utils;
import com.jason.jasonutils.tools.ViewUtil;
import com.jason.jasonutils.widgetcomponent.CircleImageView;
import com.jason.jasonutils.window.poptiputil.SMSPreviewUtils;

public class TestDemo extends Activity implements OnClickListener {
	
	private static final String TAG = "TestDemo"; 
	private Button bt_send_msg;
	private EditText et_edit_info, et_edit_info2;
	private TextView tv_show_info;
	private TextView tv_show_edit_info;
	private Timer timer;
	private TimerTask task;
	MyTimerToast toast ;
	
	private ImageView iv_test_image,iv_test_image2;
	private boolean isShow = false;
	
	private Context ctx;
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				try {
					//Toast.makeText(TestDemo.this, "自定义土司显示时长", 1).show();
					//因为它可以调用外部类的方法和字段，那么这个匿名内部类必然持有外部activity实例的引用。可能到安置内存泄露
					test1();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("test onCreate");
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);//禁止截屏
		setContentView(R.layout.text_demo_activity);
		
		bt_send_msg = (Button) findViewById(R.id.bt_send_msg);
		bt_send_msg.setOnClickListener(this);
		
		et_edit_info = (EditText) findViewById(R.id.et_edit_info);
		et_edit_info2 = (EditText) findViewById(R.id.et_edit_info2);
		//setEditTextFilter(et_edit_info);
		// et_edit_info.setImeOptions(EditorInfo.IME_ACTION_SEND);
		tv_show_info = (TextView) findViewById(R.id.tv_show_info);
		tv_show_edit_info = (TextView) findViewById(R.id.tv_show_edit_info);
		
		MLog.i("哈哈", getPropertiesData());
		ctx = TestDemo.this;
		iv_test_image = (CircleImageView) findViewById(R.id.iv_test_image);
		iv_test_image2 = (ImageView) findViewById(R.id.iv_test_image2);
		setBitmapRounder();
		//EditText以指定的字符显示
		// setEditTextPWDFormat(et_edit_info);
		// isHome();
		// debugTest();
		
		//test1();
		//printProcessorCount();
		
		getTextHeightWidth();
	}
	private void getTextHeightWidth() {
		Map<String, Integer> result = ViewUtil.getTextHeightWidth(tv_show_edit_info);
		System.out.println("Width=="+ result.get("Width"));
		System.out.println("Height=="+ result.get("Height"));
	}
	
	

	/**
	 * 测试view instanceof TextView,不会报空
	 * 证明关键字instanceof在执行的时候做了判空处理
	 */
	private void test1() {
		View view = null;
		if(view instanceof TextView){
			Toast.makeText(this, "view 是TextView", Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(this, "view 不是TextView", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_send_msg:
			//getSDInfo();
			//getContactsFromSys();
			//getContactsFromSys2();
			//testInsert(this);
			//batchInsertContacts2sys();
			//Log4StringUtil();
			//scrollerDemo();
			//msgPopTip();
			//isNumber();
			//isChinese();
			//lawful();
			//getScreenResolution();
			//myCountDownTimer(10000,1000,tv_show_info);
			//getJson();
			//getEditTextContent(et_edit_info);
			//getLocationParams();
			//setEditTextError();
			//decodeDemo();
			//alertDialogDemo();
			//ThreadOperateUI();
			//delSysContactByPhone();
			//isMobilePhoneNubmer();
			//ThreadShowToast(this);
			//logText();
			//screenShot();
			//genDimenCard();
			//unCaughtErrorLogTest();
			//useViewUpdateThreadUI(et_edit_info);
			//getDateString();
			//fixData();
			//testSpannable();
			//showAnim();
			// editText();
			//debugTest();
			//enumTest();
			//testPinyinHelper();
			//ViewUtil.setTextViewInfo(tv_show_info, "微信支付", "推荐");
			//ViewUtil.setPromotionText(tv_show_info, "微信支付", "推荐");
//			textStringWithSpaceStart(et_edit_info);
			//GreenDaoTest.insertData2DB(this);
			//GreenDaoTest.greenDaoTest(this);
			HashSetTest();
			break;
		}
	}

	public static void HashSetTest(){
		HashSet<Integer> set = new HashSet<Integer>();
		set.add(1);
		set.add(2);
		set.add(3);
		set.add(4);
		set.add(2);
		set.add(3);
		Log.i("jason", "set=="+set.toString());
	}
	
	public void textStringWithSpaceStart(EditText et){
		String str = et.getText().toString();
		if(str== null){
			Toast.makeText(this, "为null", Toast.LENGTH_SHORT).show();
			return;
		}
		if(str!=null && str.length()>0 && " ".equals(str)){
			Toast.makeText(this, "空格\"\"", Toast.LENGTH_SHORT).show();
		}
		str = str.trim();
		if(TextUtils.isEmpty(str)){
			Toast.makeText(this, "被trim后变空串了", Toast.LENGTH_SHORT).show();
		}
		System.out.println("str length = "+ str.length());
		System.out.println("str length = "+str.trim().length());
		System.out.println(str);
	}
	
	/**
	 * 测试第三方jar包PinyinHelper的拼音转换功能
	 */
	public void testPinyinHelper(){
		try {
			String result = PinYinUtil.getPinYin("中国");
			MLog.iTag(result);
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 枚举静态变量
	 */
	public void enumTest(){
		System.out.println(EnumTest.A);
		EnumConstant.setName("hiahiea");//改变常量池的数据
		System.out.println(EnumConstant.enum_name);
		EnumTest.A.setName(EnumConstant.enum_name);//通过set方法对枚举中的元素数据更新
		System.out.println(EnumTest.A);
	}
	
	/**
	 * Eclipse debug调试练习
	 */
	public void debugTest() {
		Toast.makeText(this, "debug调试开始", Toast.LENGTH_SHORT).show();
		int sum = 0;
		sum = stepTest(sum);
		System.out.println("sum==" + sum);
		Toast.makeText(this, "debug调试结束", Toast.LENGTH_SHORT).show();
	}

	/**
	 * debug测试step over 和step info的区别
	 * 
	 * @param sum
	 * @return
	 */
	public int stepTest(int sum) {
		for (int i = 0; i <= 10; i++) {
			sum += i;
		}
		return sum;
	}

	/**
	 * 测试EditText默认获取到的字符串是""还是null。 结论：如果EditText没有任何字符写入，默认获取字符串后不是null而是""
	 */
	private void editText() {
		String info = et_edit_info.getText().toString();
		boolean resullt = (info == null);
		boolean resullt2 = (info.equals(""));
		MLog.i("info==" + info + ";	resullt==" + resullt + ";	resullt2==" + resullt2);
	}

	/*
	 * Frame动画效果Demo
	 */
	public void showAnim(){
		iv_test_image.setVisibility(View.GONE);
		iv_test_image2.setVisibility(View.VISIBLE);
		if(isShow==false){
			isShow = true;
			AnimationUtil.showAnimation(iv_test_image2, R.anim.call_animation);
		}else{
			isShow = false;
			AnimationUtil.stopAnimation(iv_test_image2);
		}
	}
	
	
	/**
	 * 图片圆角处理
	 */
	public void setBitmapRounder(){
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_tool_6);
		iv_test_image.setImageBitmap(ImageViewUtil.toRoundCorner(bitmap, null));
	}
	
	/**
	 * 在文本框中显示图片
	 */
	public void testSpannable(){
		CharSequence text = "碰到星的文本★，就显示一个表情图片，现在我给你一个★"; 
		String regex = "★";
		tv_show_edit_info.setText(SmileUtils.textViewShowImage2(ctx, text, regex));
	}
	
	/**
	 * 展示资源文件中的占位符数据
	 * String中的value也可简单写成：%d（表示整数），%f（表示浮点数），%s（表示字符串）
	 */
	private void fixData() {
		tv_show_edit_info.setText(getString(R.string.fix_data, "参数1","参数2",18,15.36f));
	}

	/**
	 * 通过日期选择器来选择生日
	 */
	public void getDateString(){
		DateUtil.getBirthdayString(this,tv_show_edit_info);
	}
	
	/**
	 * 通过View获取Context来在子线程更新UI
	 */
	public void useViewUpdateThreadUI(View view){
		Utils.useViewUpdateThreadUI(view);
	}
	
	public void unCaughtErrorLogTest(){
		String str = null;
		if(str.equals("123456")){
			MLog.i("错误日志问题");
		}
	}
	
	/**
	 *  保存当前界面截图
	 */
	public void screenShot(){
		ScreenShotUtil.doScreenShot(this, null);
	}
	
	public String getSignatureMD5SHA(Activity context) {
		String result = null;
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo("com.uip.start", PackageManager.GET_SIGNATURES);
			for(Signature sn:info.signatures){
				try {
					MessageDigest md = MessageDigest.getInstance("SHA");
					md.update(sn.toByteArray());
					result = Base64.encodeToString(md.digest(), Base64.DEFAULT);
					if(!TextUtils.isEmpty(result.trim())){
						MLog.i(context.getClass().getSimpleName(), "Reuslt==="+ result);
						break;
					}
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
	
    //监听电话
    public class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber){
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: 
                    MLog.i("TestDemo", "挂断状态");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                	MLog.i("TestDemo", "响铃状态");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK: 
                	MLog.i("TestDemo", "接听状态");
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }
    
	/**
	 * 挂断电话.
	 * @param incomingNumber 电话号码
	 */
	public void endCall(String incomingNumber) {
		try {
			Class clazz = getClassLoader().loadClass("android.os.ServiceManager");
			Method method = clazz.getMethod("getService", new Class[] { String.class });
			// IBinder b = ServiceManager.getService(TELEPHONY_SERVICE);
			IBinder b = (IBinder) method.invoke(null, new String[] { TELEPHONY_SERVICE });
			ITelephony iTeletphony = ITelephony.Stub.asInterface(b);//进程间通信：获取接口
			iTeletphony.endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean getSmsManager(String number,String smg){
		MLog.i(TAG, "开始发送");
		try {
			Class clazz = getClassLoader().loadClass("android.telephony.SmsManager");
			Method method = clazz.getMethod("getSecond");
			SmsManager smsManager = (SmsManager)method.invoke(clazz, null);
			smsManager.sendTextMessage(number, null, smg, null, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 发送mcwill短信
	 * @param number
	 * @param smg
	 * @return
	 */
	public boolean sendMcWillSms(String number,String smg){
		try {
			Class clazz = getClassLoader().loadClass("android.telephony.SmsManager");
			Method[] methods =clazz.getDeclaredMethods();
			Method targetMethod = null;
			for(Method me:methods){	
				if(me.getName().equals("getSecond")){
					targetMethod = me;
					break;
				}
			}
			if(targetMethod !=null){
				Object obj = targetMethod.invoke(clazz, null);
				boolean condition = obj.getClass().equals(SmsManager.class);
				if(condition){
					((SmsManager)obj).sendTextMessage(number, null, smg, null, null);
					return true;	
				}
				return false;
			}else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean sendMessage(String number,String smg){
		SmsManager manger = SmsManager.getDefault();
		manger.sendTextMessage(number, null, smg, null, null);
		return true;
	}
	
	public void getDeviceInfo(){
		String str1 = android.os.Build.MODEL;
		String str2 = android.os.Build.VERSION.SDK ;
		String str3 = android.os.Build.VERSION.RELEASE;
		MLog.i(TAG, str1);//11-18 17:45:56.316: I/TestDemo(10440): Mi108
		MLog.i(TAG, str2);//11-18 17:45:56.316: I/TestDemo(10440): 10
		MLog.i(TAG, str3);//11-18 17:45:56.316: I/TestDemo(10440): 2.3.4
		
		
		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE); 
	    String deviceid = tm.getDeviceId(); //串号
	    String tel = tm.getLine1Number(); 
	    String imei =tm.getSimSerialNumber(); 
	    String imsi =tm.getSubscriberId();//串号
	    
	    MLog.i(TAG, "deviceid=="+deviceid);//11-18 20:23:13.523: I/TestDemo(12044): deviceid==354386040502549
	    MLog.i(TAG, "tel=="+tel);//11-18 20:23:13.523: I/TestDemo(12044): tel==null
	    MLog.i(TAG, "imei=="+imei);//11-18 20:23:13.523: I/TestDemo(12044): imei==null
	    MLog.i(TAG, "imsi=="+deviceid);//11-18 20:23:13.523: I/TestDemo(12044): imsi==354386040502549
	}

	/**
	 * 通过子线程来更新UI
	 */
	public void ThreadOperateUI(){
		new Thread(){
			@Override
			public void run() {
				et_edit_info2.post(new Runnable() {
					@Override
					public void run() {
						et_edit_info2.requestFocus();
						et_edit_info2.setText("第二个编辑框通过子线程获取到焦点");
					}
				});
			};
		}.start();
	}
	
	/**
	 * 子线程弹出Toast
	 * @param act
	 */
	public void ThreadShowToast(final Activity act){
		Utils.ThreadShowToast(act, "子线程弹出Toast提示");
	}
	
	/**
	 * 以Activity的方式显示一个Dialog
	 */
	public void alertDialogDemo(){
		Intent intent = new Intent(this,AlertDialogActivity.class);
		intent.putExtra(AlertDialogActivity.TITLE,"温馨提示");
		intent.putExtra(AlertDialogActivity.MESSAGE,"你确定要加密通讯录并上传吗？");
		intent.putExtra(AlertDialogActivity.SHOW_CANCEL_BT,true);
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			if(data!=null && data.getBooleanExtra(AlertDialogActivity.CONFIRM, false)){
				Toast.makeText(this, "确定加密", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void setEditTextError(){
		String info = et_edit_info.getText().toString().trim();
		if(!TextUtils.isEmpty(info) && info.equals("1328546")){
			et_edit_info.requestFocus();
			et_edit_info.setError("输入不一致");
		}
	}
	
	/**
	 * 测试StringUtil工具类
	 */
	public static void Log4StringUtil(){
		LogUtil.logInfo(StringUtil.myTrim("   ASDFD SDF"));
	}
	
	/**
	 * 获取EditText输入的内容
	 * @return
	 */
	public String getEditInfo(){
		String info = et_edit_info.getText().toString();
		if(info!=null && info.trim().length()>0){
			Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
			return info;
		}
		return null;
	}
	
	public static String md5hash(String key) {
        MessageDigest hash = null;
        try {
            hash = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        hash.update(key.getBytes());
        byte[] digest = hash.digest();
        StringBuilder builder = new StringBuilder();
        for (int b : digest) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString((b >> 0) & 0xf));
        }
        return builder.toString();
	}
	
	/**
	 * 动态获取状态栏高度：
	 */
	public void getStatusBarH(){
		Rect r = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
		int statusBarH = r.top;
		MLog.i("哈哈", statusBarH+"");
	}
	
	/**
	 * 获取给定字符串的二维码图片
	 */
	public void genDimenCard(){
		DimensionaLCodeTool dct = new DimensionaLCodeTool(this);
		String textInfo = "name：飞龙；age：28；info：123456789987654321；";
		String savePicName = "微信账号";
		Bitmap bm = dct.createImage(textInfo, savePicName);
		if(bm!=null){
			
			iv_test_image.setImageBitmap(bm);
		}
	}
	
	/**
	 * SD卡容量信息
	 */
	public void getSDInfo(){
		String total = Utils.getSDSize(this);
		String available = Utils.getSDAvailableSize(this);
		String info = "总大小="+total+"; 可用大小="+available;
		Toast.makeText(this, info,Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 删除指定目录及子目录
	 */
	public void delDir(){
		File dir = new File(Environment.getExternalStorageDirectory(),"haha");
		StreamTools.deleteDir(dir);
	}
	
	/**
	 * 获取系统所有联系人信息
	 * @param resolver
	 */
	private static List<Map<String, Object>> getContactsInformation(ContentResolver resolver){
		List<Map<String, Object>> list = null;
		//查询id
		Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,new String[]{ContactsContract.Contacts._ID,ContactsContract.Contacts.DISPLAY_NAME},null,null,null);
		if(cursor==null || cursor.getCount()==0){
			return null;
		}else{
			list = new ArrayList<Map<String, Object>>();
		}
		Map<String, Object> map = null;
	    while (cursor.moveToNext()) {
	    	map = new HashMap<String, Object>();
	        
	    	String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));  
	    	map.put("id", id);
	    	
	        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	        map.put("name", name);
	        
	        //头像
	        String phoneWhere = ContactsContract.Data.CONTACT_ID + " = ? and " + ContactsContract.Data.MIMETYPE + " = ?";
	        String[] phoneWhereParams = new String[]{id, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE};
	        Cursor photoCur = resolver.query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO}, phoneWhere, phoneWhereParams, null);
	        if (photoCur.moveToFirst()) {
	          byte[]image = photoCur.getBlob(photoCur.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO));
	          map.put("image",image);
	          photoCur.close();
	        }
	       
	        //号码：包括手机、单位电话、家庭电话
	       Cursor pCur = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.TYPE} ,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id, null,  null );  
	       while(pCur.moveToNext()){  
	           String num = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));  
	           String type = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)); 
	           if(TextUtils.isEmpty(num)){
	        	   continue;
	           }
	           if(type.equals("1"))
	         	  map.put("homeTel",num.replace("-", ""));
	           else if(type.equals("2"))
	         	  map.put("telephone",num.replace("-", ""));
	           else if(type.equals("3"))
	         	  map.put("workTel",num.replace("-", ""));
	           else if(type.equals("7"))
	         	  map.put("otherTel",num.replace("-", ""));
	       }
	       pCur.close();
	      
	       //邮箱
	       Cursor emailCur = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null , ContactsContract.CommonDataKinds.Email.CONTACT_ID +" = "+ id, null,  null );   
	       if(emailCur.moveToFirst()){
	    	   String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
		       if(TextUtils.isEmpty(email)) continue;
		       map.put("email", email);
		       emailCur.close();
	       }   
	       
	      //地址
	       String addrWhere = ContactsContract.Data.CONTACT_ID +  " = ? AND "  + ContactsContract.Data.MIMETYPE +  " = ?" ;   
	       String[] addrWhereParams = new  String[]{id, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};   
	       Cursor addrCur = resolver.query(ContactsContract.Data.CONTENT_URI, null , addrWhere, addrWhereParams,  null );  
	       while(addrCur.moveToNext()) {  
			   String street = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));  
			   String city = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));  
			   String state = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION)); 
			   String postalCode = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));  
			   StringBuilder addrStr = new StringBuilder();
			   if(!TextUtils.isEmpty(state)){
				   addrStr.append(state).append("/");
			   }
			   if(!TextUtils.isEmpty(city)){
				   addrStr.append(city).append("/");
			   }
			   if(!TextUtils.isEmpty(street)){
				   addrStr.append(street).append("/");
			   }
			   if(!TextUtils.isEmpty(postalCode)){
				   addrStr.append(postalCode);
			   }
			   if(addrStr.toString().endsWith("/")){
				   addrStr.deleteCharAt(addrStr.length()-1);
			   }
			   if(addrStr.length()>0){
				 map.put("address", addrStr.toString()); 
			   }
	       	}
	       addrCur.close();
	       list.add(map);
	     }
	    System.gc();
	    cursor.close();
	    return list;
	}
	

	
	/**
	 * 向系统通讯录中插入数据
	 * 首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获得系统返回的rawContactId
	 * 这时后面插入data表的数据，只有执行空值插入，才能使插入的联系人在通讯录里面可见
	 */
	public void testInsert(final Context context) {
		new Thread(){
			@Override
			public void run() {
				for(int i=0; i<300;i++){
					ContentValues values = new ContentValues();
					//首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获得系统返回的rawContactId
					Uri rawContactUri = context.getContentResolver().insert(RawContacts.CONTENT_URI, values);
					long rawContactId = ContentUris.parseId(rawContactUri);
					//往data表里写入姓名
					values.clear();
					values.put(Data.RAW_CONTACT_ID, rawContactId);
					values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE); //内容类型
				    values.put(StructuredName.GIVEN_NAME, "信威"+i);
				    context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
				    
				    //往data表里写入手机号码
				    values.clear();
				    values.put(Data.RAW_CONTACT_ID, rawContactId);
				    values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
				    values.put(Phone.NUMBER, "13712334"+i);
				    values.put(Phone.TYPE, Phone.TYPE_MOBILE);
				    context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
				    //往data表里写入家庭电话
				    values.clear();
				    values.put(Data.RAW_CONTACT_ID, rawContactId);
				    values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
				    values.put(Phone.NUMBER, "138122384"+i);
				    values.put(Phone.TYPE, Phone.TYPE_HOME);
				    context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
				    //往data表里写入单位电话
				    values.clear();
				    values.put(Data.RAW_CONTACT_ID, rawContactId);
				    values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
				    values.put(Phone.NUMBER, "13921009"+i);
				    values.put(Phone.TYPE, Phone.TYPE_WORK);
				    context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
				    
				    //往data表里写入Email
				    values.clear();
				    values.put(Data.RAW_CONTACT_ID, rawContactId);
				    values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
				    values.put(Email.DATA, "xinwei"+i+"@163.com");
				    values.put(Email.TYPE, Email.TYPE_WORK);
				    context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
				    
				    values = null;
				}
			};
		}.start();
	}
	
	public List<Map> getContactsInformation3(Activity context){
		List <Map> list = new ArrayList<Map>();
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME }, null, null, null);
		/*
		Map<Integer,String> mimeTypeMap = new HashMap<Integer, String>();
		mimeTypeMap.put(1, "vnd.android.cursor.item/email_v2");//邮箱
		mimeTypeMap.put(5, "vnd.android.cursor.item/phone_v2");//电话号码
		mimeTypeMap.put(8, "vnd.android.cursor.item/postal-address_v2");//地址
		mimeTypeMap.put(10, "vnd.android.cursor.item/photo");//头像
		*/
		if(cursor!=null){
			MLog.i("哈哈", cursor.getCount()+"");
			Map map = null;
			while(cursor.moveToNext()){
				map = new HashMap();
				//获取id
				String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
				map.put("id", id);

				//获取名字
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				map.put("name", name);
				
				//获取id对应的数据：
				String []projection = new String[]{};
				//String where = "raw_contact_id = ?";
				String where = "raw_contact_id = ?";
				Uri uri = Uri.parse("content://com.android.contacts/data");
				Cursor personCursor = resolver.query(uri, null ,where, new String[]{id}, null);
				while(personCursor.moveToNext()){
					//String[] columns = personCursor.getColumnNames();
					switch(personCursor.getInt(personCursor.getColumnIndex("mimetype"))){
					case 1:
						String email = personCursor.getString(personCursor.getColumnIndex("data1"));
						if(!TextUtils.isEmpty(email)){
							map.put("email", email);
						}
						break;
					case 5:
						String num = personCursor.getString(personCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						String type = personCursor.getString(personCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
						if (type.equals("1"))
							map.put("homeTel", num.replace("-", ""));
						else if (type.equals("2"))
							map.put("telephone", num.replace("-", ""));
						else if (type.equals("3"))
							map.put("workTel", num.replace("-", ""));
						break;
					case 8:
						String address = personCursor.getString(personCursor.getColumnIndex("data1"));
						if(!TextUtils.isEmpty(address)){
							map.put("address", address);
						}
						break;
					case 10:
						byte[] image = personCursor.getBlob(personCursor.getColumnIndex("data15"));
						if(image!=null && image.length>0){
							map.put("image", image);
						}
						break;
					}
				}
				list.add(map);
			}
		}
		return list;
	}
	
	/**
	 * 批量插入系统联系人
	 */
	public void batchInsertContacts2sys(){
		InsertSysContactTool.testInsert(this);
	}
	
	/**
	 * 获取SDcard中配置文件中的数据
	 * @return
	 */
	public String getPropertiesData(){
		String IP = PropertiesUtil.getIP();
		String PORT = PropertiesUtil.getPORT();
		return "IP=="+IP+"--------"+"PORT=="+PORT;
	}
	
	/**
	 * Scroller的API演示
	 */
	public void scrollerDemo(){
		startActivity(new Intent(this, ScrollerDemoActivity.class));
	}
	
	/**
	 * 消息弹窗
	 */
	public void msgPopTip(){
		SMSPreviewUtils.getInstance().doShow(this, "这是一条新消息\n这是一条新消息\n这是一条新消息\n这是一条新消息\n这是一条新消息\n这是一条新消息\n这是一条新消息", "Jason");
	}
	
	public void isNumber(){
		if(Utils.isIntegerStr("6-4523")){
			int temp = Integer.parseInt("6-4523");
			MLog.i("哈哈",temp+"");
			MLog.i("哈哈",(temp<0)+"");
		}
	}
	
	public void isChinese(){
		boolean result = Utils.isChineseStr("中国啊哈哈");
		MLog.i("哈哈","是不是中文==="+result+"");
	}
	
	public void lawful(){
		boolean result = Utils.lawful("asdf_43658adf_asdfoi487");
		MLog.i("哈哈","数字、字母、下划线==="+result+"");
	}
	
	
	/**
	 * 手机分辨率
	 */
	public void getScreenResolution(){
		Toast.makeText(this,Utils.getScreenResolution(this),Toast.LENGTH_SHORT).show();
	}

	/**
	 * 倒计时
	 * @param millisInFuture
	 * @param countDownInterval
	 * @param tv_show_info
     */
	public void myCountDownTimer(long millisInFuture, long countDownInterval,TextView tv_show_info){
		Utils.myCountDownTimer(millisInFuture, countDownInterval, tv_show_info);
	}
	
	public void getJson() {
		try {
			JSONObject js1 = new JSONObject();
			js1.put("name", "hahah");
			js1.put("pwd", "123456");
			MLog.i("哈哈", "1111===="+js1.toString());
			
			JSONObject js2 = new JSONObject(js1.toString());
			Object object = js2.get("name");
			MLog.i("哈哈", "2222===="+new JSONObject().put("name", object.toString()).toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 给EditText设置过滤器
	 */
	private void setEditTextFilter(EditText et) {
		InputFilter[] filters = new InputFilter[2];
		filters[0] = new InputFilter.LengthFilter(6);
		filters[1] = new InputFilter(){
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				if(source.length()==0){
					Character.isDigit(source.charAt(0));
					return null;
				}
				return null;
			}
		};
		et.setFilters(filters);
	}
	
	/**
	 * 重新覆写PasswordTransformationMethod类，使EditText获取的文本以指定的文本格式显示
	 * @param editText
	 */
	public void setEditTextPWDFormat(EditText editText){
		Utils.setEditTextPWDFormat(editText);
	}
	
	
	public void getEditTextContent(EditText editText){
		String info = editText.getText().toString().trim();
		if(TextUtils.isEmpty(info))
			return;
		else 
			tv_show_edit_info.setText(info);
	}
	
	public void getLocationParams(){
		LocationUtil instance = LocationUtil.getInstance(this);
		double latitude = instance.getLatitude();
		double longitude = instance.getLongitude();
		
		MLog.i("哈哈", "latitude=="+latitude+"; longitude==="+longitude);
		MLog.i("哈哈", "s2c=="+instance.getLocationParams());
	}
	
	/**
	 * 加密解密Demo
	 */
	public void decodeDemo(){
		String str = "哈哈";
		
		String encodeResult = Utils.encryptString(str);
		String decodeResult = Utils.decryptString(encodeResult);
		
		MLog.i("哈哈", "encodeResult === "+encodeResult);
		MLog.i("哈哈", "decodeResult === "+decodeResult);
	}
	
	public void isHome(){
		boolean isHome = Utils.isHome(this);
		MLog.i("哈哈", "isHome == "+isHome);
	}
	
	/**
	 * 删除系统联系人
	 */
	public void delSysContactByPhone(){
		SystemContact.delSysContactByPhoneNumber(this, et_edit_info.getText().toString().trim());
	}
	
	/**
	 * 判断输入的是不是手机号码
	 */
	public void isMobilePhoneNubmer(){
		boolean result = Utils.isMobileNumber(et_edit_info.getText().toString().trim());
		String info = (result?"":"不")+"是手机号码";
		Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 自定义日志测试
	 */
	public void logText(){
		MLog.v("v打印日志","v second日志","v three 日志");
		MLog.i("d打印日志");
		MLog.i("i打印日志");
		MLog.w("w打印日志");
		MLog.e("e打印日志");
	}
	
	/**
	 * 获取设备的CPU核心数
	 */
	public void printProcessorCount(){
		int processorCount = Runtime.getRuntime().availableProcessors();
		System.out.println("该设备的cpu核心数是："+processorCount);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		System.out.println("test onStart");
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		System.out.println("test onRestoreInstanceState");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("onResume ---width == "+ tv_show_edit_info.getWidth());
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK ){
			System.out.println("test back");
			startActivity(new Intent(this, JasonUtilsMainActivity.class));
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("test onPause");
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(et_edit_info!=null){
			et_edit_info.onSaveInstanceState();
		}
		System.out.println("test onSaveInstanceState");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		System.out.println("test onStop");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("test onDestroy");
		handler.removeCallbacksAndMessages(null);
	}
	
}
