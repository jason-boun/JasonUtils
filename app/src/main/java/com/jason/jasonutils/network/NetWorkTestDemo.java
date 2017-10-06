package com.jason.jasonutils.network;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.ViewUtil;

public class NetWorkTestDemo extends Activity implements OnClickListener {
	
	private TextView tv_show_apn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.network_testdemo_activity);
		tv_show_apn = (TextView) findViewById(R.id.tv_show_apn);
		ViewUtil.setChildsOnClickLisener(this, R.id.ll_networkDemo_container, 0, this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_check_network:
			if(NetUtil.isNetworkAvailable(this)){
				Toast.makeText(this, "Network Available", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.bt_check_gps:
			Toast.makeText(this, NetUtil.isGpsEnabled(this)+"", Toast.LENGTH_SHORT).show();
			break;
		case R.id.bt_check_wifi:
			Toast.makeText(this, NetUtil.isWIFIConnectivity(this)+"", Toast.LENGTH_SHORT).show();
			break;
		case R.id.bt_check_mobile:
			Toast.makeText(this, NetUtil.isMobileConnectivity(this)+"", Toast.LENGTH_SHORT).show();
			break;
		case R.id.bt_check_apn:
			NetUtil.readApn(this);
			String apnInfo= "";
			if(!TextUtils.isEmpty(NetUtil.PROXY_IP)){
				apnInfo = "PROXY_IP=="+NetUtil.PROXY_IP;
			}if(NetUtil.PROXY_PORT!=0){
				apnInfo = apnInfo+", PROXY_PORT=="+NetUtil.PROXY_PORT;
			}if(!TextUtils.isEmpty(apnInfo)){
				tv_show_apn.setText(apnInfo);
			}else{
				Toast.makeText(this, "hava no apn info", Toast.LENGTH_SHORT).show();
				tv_show_apn.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
			}
			break;
		}
	}

}
