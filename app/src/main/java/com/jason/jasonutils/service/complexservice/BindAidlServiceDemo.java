package com.jason.jasonutils.service.complexservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonsubutils.complexservice.IMyService;
import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.MLog;
import com.jason.jasonutils.tools.ViewUtil;

public class BindAidlServiceDemo extends Activity implements OnClickListener{

	private TextView aidlInfo;
	private IMyService myService;
	private Intent serviceIntent = new Intent("com.jason.jasonsubutils.complexservice.MyService");
	
	private ServiceConnection myConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			myService = IMyService.Stub.asInterface(service);
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bind_aidl_service_activity);
		init();
	}

	private void init() {
		aidlInfo = (TextView) findViewById(R.id.aidlInfo);
		ViewUtil.setChildsOnClickLisener(this, R.id.ll_aidlservice_operate_container, 0, this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBindAIDLService:
			bindService(serviceIntent, myConnection, Context.BIND_AUTO_CREATE);
			Toast.makeText(this, "服务已经绑定", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnInvokeAIDLService:
			getAidlInfo();
			break;
		case R.id.btnInvokeServiceMethod:
			invokeServiceMethod();
			break;
		}
	}

	private void getAidlInfo() {
		try{
			String s = "";
			//获取序列化对象数据
			s = "Product.id = " + myService.getProduct().getId() + "\n";
			s += "Product.name = " + myService.getProduct().getName() + "\n";
			s += "Product.price = " + myService.getProduct().getPrice() + "\n";
			
			//获取序列化Map数据
			s += myService.getMap("China", myService.getProduct()).toString();
			
			aidlInfo.setText(s);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void invokeServiceMethod() {
		try {
			String result = myService.callMethodInService("我通过服务来远程调用你的方法");
			//两次Toast是为了看清楚信息，要不然时间太短了
			Toast.makeText(BindAidlServiceDemo.this, result,Toast.LENGTH_LONG).show();
			Toast.makeText(BindAidlServiceDemo.this, result,Toast.LENGTH_LONG).show();
			MLog.i("哈哈", result);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
