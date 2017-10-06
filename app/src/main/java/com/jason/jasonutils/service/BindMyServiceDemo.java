package com.jason.jasonutils.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.jason.jasonutils.R;
import com.jason.jasonutils.service.complexservice.BindAidlServiceDemo;
import com.jason.jasonutils.tools.ViewUtil;

public class BindMyServiceDemo extends Activity implements OnClickListener {
	
	private IMyService myService;
	private Intent serviceIntent;
	private ServiceConnection serviceConnection;
	private boolean hasBindService ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bind_service_demo_activity);
		
		initData();
	}

	private void initData() {
		hasBindService = false;
		serviceIntent = new Intent(BindMyServiceDemo.this, MyService.class);
		serviceConnection = new MyServiceConnection();
		
		ViewUtil.setChildsOnClickLisener(this, R.id.ll_servicedemo_container, 0, this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnStartService:
			startService(serviceIntent);
			Toast.makeText(getApplicationContext(), "开启了服务", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnBindService:
			hasBindService = true;
			bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
			break;
		case R.id.btnCallServiceMethod:
			if(myService !=null){
				Toast.makeText(getApplicationContext(), myService.getServiceInfo(), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btnStopService:
			stopService(serviceIntent);
			hasBindService = false;
			myService = null;
			Toast.makeText(getApplicationContext(), "停止了服务", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnUnbindService:
			if(hasBindService){
				unbindService(serviceConnection);
				hasBindService = false;
				Toast.makeText(getApplicationContext(), "解除了绑定", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.bt_aidlservice_demo:
			startActivity(new Intent(this, BindAidlServiceDemo.class));
			break;
		}
	}
	
	public class MyServiceConnection implements ServiceConnection{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			myService = (IMyService)service;
			Toast.makeText(BindMyServiceDemo.this, "服务连接成功", Toast.LENGTH_SHORT).show();
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			myService = null;
		}
	}

}
