package com.jason.jasonutils.fragment.fragmentlife;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jason.jasonutils.tools.MLog;

public class MyFragment extends Fragment {
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		MLog.i("i", "              MyFragment           onAttach   ");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MLog.i("i", "              MyFragment           onCreate   ");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MLog.i("i", "              MyFragment           onCreateView   ");
		
		TextView tv = new TextView(getActivity());
		tv.setText("我的Fragment创建的View");
		return tv;//必须返回一个View来供给Fragment来显示，否则会报错
	}
	
	@Override
	public void onStart() {
		super.onStart();
		MLog.i("i", "              MyFragment           onStart   ");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MLog.i("i", "              MyFragment           onResume   ");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MLog.i("i", "              MyFragment           onPause   ");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		MLog.i("i", "              MyFragment           onStop   ");
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		MLog.i("i", "              MyFragment           onDestroyView   ");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		MLog.i("i", "              MyFragment           onDestroy   ");
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		MLog.i("i", "              MyFragment           onDetach   ");
	}
	
}
