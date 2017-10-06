package com.jason.jasonutils.touch;

import com.jason.jasonutils.R;

import android.content.Intent;
import android.os.Bundle;

public class TouchActivityThree extends BaseTouchActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.touch_demo_three_activity);
	}

	@Override
	public void showNext() {
		startActivity(new Intent(this, TouchActivityOne.class));
		finish();
		overridePendingTransition(R.anim.trans_next_in, R.anim.trans_next_out);
	}

	@Override
	public void showPre() {
		startActivity(new Intent(this, TouchActivityTwo.class));
		finish();
		overridePendingTransition(R.anim.trans_pre_in, R.anim.trans_pre_out);
	}

}
