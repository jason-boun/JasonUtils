package com.jason.jasonutils.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;

import com.jason.jasonutils.R;
import com.jason.jasonutils.network.NetUtil;

public class FragmentActivityDemo extends FragmentActivity {
	
	private FragmentManager fm;
	private FragmentTransaction ta;
	private RelativeLayout rl_error_item;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_fragment_demo);
		fm = getSupportFragmentManager();
		rl_error_item = (RelativeLayout) findViewById(R.id.rl_error_item);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(NetUtil.isConnected(getApplicationContext())){
			rl_error_item.setVisibility(View.GONE);
		}else{
			rl_error_item.setVisibility(View.VISIBLE);
		}
	}
	public void setNetwork(View view){
		if(rl_error_item.getVisibility()==View.VISIBLE){
			Intent intent =new Intent();
			intent.setAction("android.intent.action.MAIN");
			intent.setAction("android.settings.SETTINGS");
			intent.addCategory("android.intent.category.DEFAULT");
			this.startActivityForResult(intent,0);
		}
	}
	
	public void password(View view){
		ta = fm.beginTransaction();
		ta.replace(R.id.ll_fragment_container, new FragmentOne());
		ta.commit();
	}
	public void network(View view){
		ta = fm.beginTransaction();
		ta.replace(R.id.ll_fragment_container, new FragmentTwo());
		ta.commit();
		
	}
	public void aboutme(View view){
		ta = fm.beginTransaction();
		ta.replace(R.id.ll_fragment_container, new FragmentThree());
		ta.commit();
	}
	public void setting(View view){
		ta = fm.beginTransaction();
		ta.replace(R.id.ll_fragment_container, new FragmentFour());
		ta.commit();
	}
}
