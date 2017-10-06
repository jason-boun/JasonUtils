package com.jason.jasonutils.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.ZipUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

public class ZipDemoActivity extends Activity{
	private String deviece = Build.MODEL;//设备
	private String sysName = Build.VERSION.RELEASE;
	
	private static String sdPath = Environment.getExternalStorageDirectory()+"/myzip";
	private static File saveFile = new File(sdPath, "savefile.txt");
	private static File zipFile = new File(sdPath, "zipfile.zip");
	private static File unZipFile = new File(sdPath, "unzipfile.txt");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_demo_activity);
		TextView tv_info = (TextView) findViewById(R.id.tv_show_info);
		//LoginLog(deviece, sysName, tv_info);
		ZipDemoActivity.zipFile();
		SystemClock.sleep(1000);
		tv_info.setText(ZipDemoActivity.unZipFile());
		
	}

	/**
	 * 创建JSON串数据
	 * @param @return 
	 * @return String 
	 * @throws 
	 */
	public static String getJsonData(){
		JSONObject jsObj = new JSONObject();
		try {
			jsObj.put("name", "哈哈");
			jsObj.put("age", 55);
			return jsObj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 压缩数据
	 * @param @return 
	 * @return boolean 
	 * @throws 
	 */
	public static boolean zipFile(){
		FileOutputStream fos = null;
		if(!new File(sdPath).exists()){
			new File(sdPath).mkdir();
		}
		try {
			fos = new FileOutputStream(saveFile);
			byte[] info = Base64.encode(getJsonData().getBytes(), Base64.DEFAULT);
			fos.write(info);
			fos.close();
			
			List<File> files = new ArrayList<File>();
			files.add(saveFile);
			ZipUtils.zipFiles(files, zipFile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 解压数据并显示
	 * @param @return 
	 * @return String 
	 * @throws 
	 */
	public static String unZipFile(){
		try {
			ZipUtils.upZipFile(zipFile.getAbsolutePath(), unZipFile.getAbsolutePath());
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(unZipFile.getAbsolutePath());
				String content = ZipUtils.getStringByInputStream(fis);
				JSONObject jsonObject = new JSONObject(new String(android.util.Base64.decode(content, Base64.DEFAULT)));
				fis.close();
				return jsonObject.toString();
			} catch (Exception e1) {
				return "";
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 模拟登录
	 * @param @param deviece
	 * @param @param sysName
	 * @param @param tv_info 
	 * @return void 
	 */
	public void LoginLog(String deviece, String sysName, TextView tv_info) {
		new Thread(){
			public void run() {
				try {
					fixLoginLog();
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
		
		tv_info.setText("deviece=="+deviece+";\nsysName==="+sysName);
	}
	
	
	public static final String TestServerLogFixUrl= "http://10.0.19.17/smt/rest/logLoginAdd";
	/**
	 * 服务端处理登录日志管理：只是测试环境使用：http://10.0.19.17/smt/rest/
	 */
	@SuppressLint("SimpleDateFormat")
	public void fixLoginLog(){
		JSONObject obj = new JSONObject();
		try {
			Log.i("哈哈", "");
			obj.put("loginMobile", "123456789");
			obj.put("showLoginDatetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			obj.put("nickName", "我的昵称哈哈");
			obj.put("email", "test@gmail.com");
			obj.put("systemType", "Android"+Build.VERSION.RELEASE);
			obj.put("deviceType", Build.MODEL);
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/json");
			headers.put("Accept", "application/json");
			String result2 = httpPost(TestServerLogFixUrl, obj.toString(), headers);
			Log.i("哈哈", "登录Log处理请求返回结果==="+result2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Post请求
	 */
	public static String httpPost(String url, String params,Map<String,String> headers) throws Exception
	{
		String response = null;
		HttpClient httpclient = new DefaultHttpClient();
		
		//创建HttpPost对象
		HttpPost httppost = new HttpPost(url);
		
		if(headers!=null)
		{
			 for (Entry<String,String> item : headers.entrySet()) {
				httppost.addHeader(item.getKey(), item.getValue());
			}
		}
		
		try
		{
			//设置httpPost请求参数
			httppost.setEntity(new StringEntity(params,HTTP.UTF_8));
			//使用execute方法发送HTTP Post请求，并返回HttpResponse对象
			HttpResponse httpResponse = httpclient.execute(httppost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if(statusCode==HttpStatus.SC_OK)
			{
				//获得返回结果
				response = EntityUtils.toString(httpResponse.getEntity());
			}
			else
			{
				response = "返回码："+statusCode;
			}
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}

	static String url = "http://10.0.19.17/smt/rest/logLoginAdd";
	static String jsonInfo = "{'loginMobile':'13592345678', 'showLoginDatetime':'2014-9-28 15:25:25', 'nickName':'我的昵称','email':'testa@aa.com','systemType':'ios','deviceType':'4s'}";

	public static String post1(String url, String json) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		String result = "";
		try {
			StringEntity s = new StringEntity(json, HTTP.UTF_8);
			s.setContentType("application/json");
			post.setEntity(s);
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = res.getEntity();
				if (entity != null) {
					result = EntityUtils.toString(entity);
					System.out.println(result);
					return result;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	

}
