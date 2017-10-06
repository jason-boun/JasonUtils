package com.jason.jasonutils.network;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class NetUtil {
	
	private Context mContext;
	private HttpClient mClient;
	private HttpGet get;
	private HttpPost post;
	private HttpRequest request;
	private HttpResponse response;
	public static String PROXY_IP;
	public static int PROXY_PORT;
	private static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
	
	private static WifiLock wifilock = null;
	
	/**
	 * 构造函数
	 * @param context
	 */
	public NetUtil(Context context){
		mContext = context;
		mClient=new DefaultHttpClient();
		
	}
	
	/**
	 * 获取网络管理者实例
	 * @param c
	 * @return
	 */
	public static ConnectivityManager getConnectivityManager(Context context) {
		return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	
	/**
	 * 设置WAP代理和端口
	 */
	public void setProxyPort(){
		if(!TextUtils.isEmpty(PROXY_IP) && !TextUtils.isEmpty(String.valueOf(PROXY_PORT))){
			HttpHost host=new HttpHost(PROXY_IP, PROXY_PORT);
			mClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
		}
	}
	
	/**
	 * 显示当前网络是否可用
	 */
	public static boolean isConnected(Context c) {
		NetworkInfo ni = getConnectivityManager(c).getActiveNetworkInfo();
		return ni != null && ni.isConnected();
	}
	
	/**
	 * 判断网络连接状态
	 */
	public static boolean isNetworkAvailable(final Context context){
		ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo info = cm.getActiveNetworkInfo();
	    if(info!=null && info.isAvailable()){
	    	return true;
	    }else{
	    	new AlertDialog.Builder(context).setTitle("提示").setMessage("设置网络").setIcon(android.R.drawable.btn_star).setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					((Activity) context).startActivityForResult(new Intent("android.settings.WIRELESS_SETTINGS"),0);
				}
			}).setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			}).create().show();
	    	return false;
	    }
	 }
	
	/**
	 * 通过读取系统数据库，获取手机APN：获取当前处于激活状态的APN信息
	 */
	public static void readApn(Context context) {
		ContentResolver resolver = context.getContentResolver();
		Cursor query = resolver.query(PREFERRED_APN_URI, null, null, null, null);
		if (query != null && query.moveToFirst()) {
			PROXY_IP = query.getString(query.getColumnIndex("proxy"));
			PROXY_PORT = query.getInt(query.getColumnIndex("port"));
		}
	}
	
	/**
	 * WIFI是否连接
	 */
	public static boolean isWIFIConnectivity(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return info!=null && info.isConnected();
	}
	
	/**
	 * 手机数据网络是否连接
	 */
	public static boolean isMobileConnectivity(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return info!=null && info.isConnected();
	}
	
	/** 
     * GPS是否打开 
     * @param context 
     * @return 
     */ 
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));  
        List<String> accessibleProviders = lm.getProviders(true);  
        return accessibleProviders != null && accessibleProviders.size() > 0;  
    }
    
	/**
	 * 使用Wifi连接, wifi锁
	 */
	public static void startUseWifi(Context c) {
		WifiManager wifimanager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
		if (wifimanager != null) {
			wifilock = wifimanager.createWifiLock(WifiManager.WIFI_MODE_FULL, "meq");
			if (wifilock != null) {
				wifilock.acquire();
			}
		}
	}

	/**
	 * 释放wifi锁
	 */
	public static void stopUseWifi() {
		if (wifilock != null && wifilock.isHeld()) {
			wifilock.release();
		}
		wifilock = null;
	}

	/**
	 * 通过HttpPost发送xml数据流
	 * @param url
	 * @param xml
	 * @return
	 */
	public InputStream sendXml(String url, String xml) {
		post = new HttpPost(url);
		StringEntity entity;
		try {
			entity = new StringEntity(xml, HTTP.UTF_8);
			post.setEntity(entity);
			response = mClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				return response.getEntity().getContent();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getConnTypeName() {
		  ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		  if(networkInfo == null) {
			  return NET_TYPE_NO_NETWORK;
		  } else {
			  return networkInfo.getTypeName();
		  }
		}

	public static final String NET_TYPE_WIFI = "WIFI";
	public static final String NET_TYPE_MOBILE = "MOBILE";
	public static final String NET_TYPE_NO_NETWORK = "no_network";
	
	/**
	 * 通过指定路径获取数据流(通过HttpURLConnection实现网络通讯)
	 * @param path
	 * @param requestMethod
	 * @return
	 */
	public static InputStream getNetStream(String path, String requestMethod){
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if(null!= requestMethod){
				conn.setRequestMethod(requestMethod);
			}else{
				conn.setRequestMethod("POST");
			}
			conn.setConnectTimeout(5000);
			int code = conn.getResponseCode();
			if(code==200){
				return conn.getInputStream();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将流数据转为字符串
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String getStrFromStream(InputStream is, String strEncode) throws IOException{
		String result = null;
		if(is !=null){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len=is.read(buffer))!=-1){
				baos.write(buffer, 0, len);
				baos.flush();
			}
			is.close();
			if(null!=strEncode && strEncode.length()>0){
				result = new String(baos.toByteArray(),strEncode);
			}else{
				result = new String(baos.toByteArray(),HTTP.UTF_8);
			}
			
			baos.close();
		}
		return result;
	}
	
	/**
	 * 图片的获取与缓存处理
	 * @param iv 
	 * @param filePath
	 * @param url：网络请求地址
	 * @param requestMethod：网络请求方式
	 * @return
	 */
	public static boolean getPicFromNet(ImageView iv, String filePath, String url, String requestMethod){
		File picFile = new File(filePath);
		if(picFile.exists()){
			iv.setImageURI(Uri.fromFile(picFile));//使用本地缓存
		}else{
			InputStream is = getNetStream(url, requestMethod);//从网络下拉
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			iv.setImageBitmap(bitmap);
			try {
				FileOutputStream os = new FileOutputStream(picFile);//保存到本地
				bitmap.compress(CompressFormat.JPEG, 100, os);
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * httpClient的GET请求
	 * @param path
	 * @param strings
	 * @return
	 * @throws Exception
	 */
	public static String httpClientGet(String path,String ...strings) throws Exception{
		if(null == path || path.length()==0){
			return null;
		}
		if(null!= strings && strings.length>0){
			HttpClient client = new DefaultHttpClient();
			String uriPath = null;
			//组拼字URI
			uriPath = path+"?";
			for(int i=0;i<strings.length;i++){
				uriPath += strings[i]+"="+strings[i]+"&";
			}
			if(uriPath.endsWith("&")){
				uriPath = uriPath.substring(0, uriPath.lastIndexOf("&"));
			}
			//Get请求
			HttpGet get = new HttpGet(uriPath);
			HttpResponse response = client.execute(get);
			int code = response.getStatusLine().getStatusCode();
			if(code == 200){
				InputStream is = response.getEntity().getContent();
				Header header = response.getEntity().getContentEncoding();
				return getStrFromStream(is, header.getValue());
			}else{
				return null;
			}
		}
		return null;
	}
	
	/**
	 * httpClient的POST请求(重载：传递键值对数据)
	 * @param path
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String httpClientPost(String path, Map<String,String> data) throws Exception{
		if(null == path || path.length()==0){
			return null;
		}
		if(data !=null && data.size()>0){
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(path);
			//封装POST请求的数据
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			Set<String> keySet = data.keySet();
			if(keySet!=null && keySet.size()>0){
				for(String key :keySet){
					parameters.add(new BasicNameValuePair(key, data.get(key)));
				}
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
			//发送POST请求
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				InputStream is = response.getEntity().getContent();
				Header header = response.getEntity().getContentEncoding();
				return getStrFromStream(is, header.getValue());
			}else{
				return null;
			}
		}
		return null;
	}
	/**
	 * httpClient的POST请求(重载：直接传递字符串数据)
	 * @param path
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String httpClientPost(String path, String data) throws Exception{
		if(null == path || path.length()==0){
			return null;
		}
		if(data !=null && data.length()>0){
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(path);
			StringEntity entity = new StringEntity(data,HTTP.UTF_8);
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				return getStrFromStream(response.getEntity().getContent(), HTTP.UTF_8);
			}else{
				return null;
			}
		}
		return null;
	}
	/**
	 * httpClient的POST请求(重载：直接传递字符串数据)
	 * @param path
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String httpClientPostWithStr(String path, String data) throws Exception{
		if(null == path || path.length()==0){
			return null;
		}
		if(data !=null && data.length()>0){
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(path);
			StringEntity entity = new StringEntity(data,HTTP.UTF_8);
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				return EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
			}else{
				return response.getStatusLine().getStatusCode()+"";
			}
		}
		return null;
	}
}
