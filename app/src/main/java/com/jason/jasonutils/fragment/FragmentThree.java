package com.jason.jasonutils.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jason.jasonutils.R;

public class FragmentThree extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_view_demo, container, false);
		TextView tv = (TextView) view.findViewById(R.id.tv_fragment_view_info);
		tv.setText("Fragment 3");
		tv.setTextColor(Color.RED);
		
		return view;
	}

}
