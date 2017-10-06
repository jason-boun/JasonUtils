package com.jason.jasonutils.cmbpay;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import cmb.pb.util.CMBKeyboardFunc;

import com.jason.jasonutils.R;

public class MyCMBPayActivity extends Activity {
	
	static private WebView webview;
	
	private String testUrl = "";
	private CMBPayStateCallback payStateCallback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cmb_pay_webview_activity);
		testUrl = getPayUrl();
		init();		
	}
	@SuppressWarnings("deprecation")
	public void init(){
		webview = (WebView) findViewById(R.id.webview);
		
		WebSettings set = webview.getSettings();
        set.setJavaScriptEnabled(true);
        set.setSaveFormData(false);
        set.setSavePassword(false);
        set.setSupportZoom(false);
        
        //webview.loadUrl(DevUrl);
        payStateCallback = new CMBPayStateCallback();
        webview.addJavascriptInterface(payStateCallback, CMBPayStateCallback.WEBVIEW_PARAM_NAME);
        
        LoadUrl();
        webview.setWebViewClient( new  WebViewClient() {
            public   boolean   shouldOverrideUrlLoading(WebView view, String url){
				CMBKeyboardFunc kbFunc = new CMBKeyboardFunc(MyCMBPayActivity.this);
            	if(kbFunc.HandleUrlCall(webview, url) == false) {
            		return super.shouldOverrideUrlLoading(view, url);
            	} else {
            		return true;
            	}
          }
        });		
	}
	
	private void LoadUrl() {
		try {
			CookieSyncManager.createInstance(MyCMBPayActivity.this.getApplicationContext());  
            CookieManager.getInstance().removeAllCookie();
            CookieSyncManager.getInstance().sync(); 
		}catch(Exception e) {
		}
		webview.loadUrl(testUrl);
	}
	
	
	public static String getPayUrl(){
		String url = "";
		String PATH = Environment.getExternalStorageDirectory()+"/paymentconfig.properties";
		Properties properties = new Properties();
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			File file = new File(PATH);
			if(file.exists()){
				try {
					properties.load(new FileInputStream(file));
					url = properties.getProperty("cmb_pay_url");
					return url;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return url;
	}
}
