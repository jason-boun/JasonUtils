package com.jason.jasonutils.tabhost;

import com.jason.jasonutils.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class TabHostDemo extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_tabhost_demo);
		
		TabHost tabHost = getTabHost();
		
		TabSpec tabSpec1 = tabHost.newTabSpec("tab1");
		TabSpec tabSpec2 = tabHost.newTabSpec("tab2");
		TabSpec tabSpec3 = tabHost.newTabSpec("tab3");
		TabSpec tabSpec4 = tabHost.newTabSpec("tab4");
		
		tabSpec1.setIndicator("密码", getResources().getDrawable(R.drawable.password2));
		tabSpec2.setIndicator("网络", getResources().getDrawable(R.drawable.checknetwork));
		tabSpec3.setIndicator("设置", getResources().getDrawable(R.drawable.setting));
		tabSpec4.setIndicator("关于", getResources().getDrawable(R.drawable.aboutme));
		
		tabSpec1.setContent(new Intent(this, ActivityOne.class));
		tabSpec2.setContent(new Intent(this, ActivityTwo.class));
		tabSpec3.setContent(new Intent(this, ActivityThree.class));
		tabSpec4.setContent(new Intent(this, ActivityFour.class));
		
		tabHost.addTab(tabSpec1);
		tabHost.addTab(tabSpec2);
		tabHost.addTab(tabSpec3);
		tabHost.addTab(tabSpec4);
		
	}
	
}
