package com.jason.jasonutils.network;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.jasonutils.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import com.jason.jasonutils.tools.MLog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistDemo extends Activity {

	
	private static final String TAG = "哈哈";
	private Button bt_send;
	private TextView tv_result_show;
	private EditText et_content_enter;
	public static final String address ="http://uc.coomarts.com";//"http://192.168.3.227:8088"; 
	public static final String path = "/ucenter/isUserNameExist";
	private String responseStr = "";
	private static String jsonKey = "userPhoneNo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regist_demo_activity);
		
		init();
	}

	@SuppressLint("ShowToast")
	private void init() {
		et_content_enter = (EditText) findViewById(R.id.et_content_enter);
		bt_send = (Button) findViewById(R.id.bt_send);
		tv_result_show = (TextView) findViewById(R.id.tv_result_show);
		
		bt_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = et_content_enter.getText().toString().trim();
				if(TextUtils.isEmpty(content)){
					Toast.makeText(RegistDemo.this, "查询词汇不能为空", 0).show();
				}else{
					try {
						getNetResult(jsonKey,content);
						tv_result_show.setText("返回结果 responseStr==="+responseStr);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	/**
	 * 发送请求
	 * @param phone
	 * @return
	 * @throws JSONException
	 */
	private int getNetResult(String key, String content) throws JSONException {
		String requestJson = new JSONObject().put(key, content).toString();
		MLog.i("哈哈", requestJson);
		responseStr = sendRequest(address + path, requestJson);
		if (TextUtils.isEmpty(responseStr)) {
			return 3;
		}
		JSONObject responseJson = new JSONObject(responseStr);
		return responseJson.getInt("result");
	}

	/**
	 * 网络请求与返回
	 * @param Url
	 * @param content
	 * @return
	 */
	public static String sendRequest(String Url, String content) {
		MLog.i(TAG, "Url-->"+Url);
		String repstr = "";
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(Url);
		try {
			request.setEntity(new StringEntity(content, HTTP.UTF_8));
			HttpResponse rep = client.execute(request);// 执行请求，并返回响应对象
			if (rep.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = rep.getEntity();
				repstr = EntityUtils.toString(entity);
			}else{
				MLog.i(TAG, "服务器返回状态"+rep.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			MLog.i(TAG, "请求服务器返回结果-->"+repstr);
			return repstr;
		}
		MLog.i(TAG, "请求服务器返回结果-->"+repstr);
		return repstr;
	}


}
