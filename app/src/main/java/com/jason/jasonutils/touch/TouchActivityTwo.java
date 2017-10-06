package com.jason.jasonutils.touch;

import android.content.Intent;
import android.os.Bundle;

import com.jason.jasonutils.R;

public class TouchActivityTwo extends BaseTouchActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.touch_demo_two_activity);
	}

	@Override
	public void showNext() {
		startActivity(new Intent(this, TouchActivityThree.class));
		finish();
		overridePendingTransition(R.anim.trans_next_in, R.anim.trans_next_out);
	}

	@Override
	public void showPre() {
		startActivity(new Intent(this, TouchActivityOne.class));
		finish();
		overridePendingTransition(R.anim.trans_pre_in, R.anim.trans_pre_out);
	}

}
