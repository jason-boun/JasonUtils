package com.jason.jasonutils.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jason.jasonutils.R;

public class WebViewActivity extends Activity {

	private TextView title_text;
	private WebView webview_content;
	private ProgressBar progressBar;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_activity);
		
		intent = getIntent();
		initWebView();
		showWebContent();
	}

	/**
	 * 初始化webView参数
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	public void initWebView() {
		// 初始化控件
		title_text = (TextView) findViewById(R.id.title_text);
		webview_content = (WebView) findViewById(R.id.webview_content);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		title_text.setText(intent.getStringExtra("title"));

		// 设置WebView属性
		WebSettings settings = webview_content.getSettings();
		settings.setAllowFileAccess(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setDisplayZoomControls(false);
		settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);  
		settings.setSavePassword(false);
		settings.setJavaScriptEnabled(true);
		settings.setDefaultTextEncodingName("utf-8");

		// 设置执行加载webview网页时所要执行的一些方法
		webview_content.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				progressBar.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				progressBar.setVisibility(View.INVISIBLE);
				super.onPageFinished(view, url);
			}
		});
		webview_content.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				WebViewActivity.this.getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
				super.onProgressChanged(view, newProgress);
			}
		});
	}

	/**
	 *  通过意图获取URL参数，展示页面
	 */
	public void showWebContent() {
		new Thread() {
			public void run() {
				webview_content.loadUrl(intent.getData().toString());
			};
		}.start();
	}

	/**
	 * 处理返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webview_content.canGoBack()) {
			webview_content.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 访问web网页的入口：打开自定义浏览器
	 * @param act
	 */
	public static void openWebView(Activity act) {
		Intent intent = new Intent(act, WebViewActivity.class);
		intent.setData(Uri.parse("http://www.baidu.com"));
		intent.putExtra("title", "JasonUtil Browser");
		act.startActivity(intent);
	}

}
