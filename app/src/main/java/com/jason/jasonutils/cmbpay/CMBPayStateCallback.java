package com.jason.jasonutils.cmbpay;

import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * WebView通过该类与H5页面进行交互
 * @author bo.jia
 */
public class CMBPayStateCallback {
	
	public static final String WEBVIEW_PARAM_NAME = "cmbMerchantBridge";
	public static final String RESULT_KEY = "pay_status";
	
	public static final String RESULT_PAYING = "0";
	public static final String RESULT_FAILED = "1";
	public static final String RESULT_SUCCESS = "2";
	
	private String resultCode = RESULT_PAYING;
	
	/**
	 * 支付H5页面状态回调方法
	 * @param payData
	 */
	@JavascriptInterface
	public void initCmbSignNetPay(String payData){
		try{
			JSONObject result = JSON.parseObject(payData);
			if(result != null && result.containsKey(RESULT_KEY)){
				resultCode = (String)result.get(RESULT_KEY);
				System.out.println("resultCode =" +resultCode);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 获取当前页面的支付状态
	 * @return
	 */
	public String getResultCode() {
		return resultCode;
	}

}
