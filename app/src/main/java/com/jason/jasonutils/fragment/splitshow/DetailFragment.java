package com.jason.jasonutils.fragment.splitshow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailFragment extends Fragment {

	//创建DetailFragment的实例
	public static DetailFragment getInstance(int index){
		DetailFragment df = new DetailFragment();
		Bundle datas = new Bundle();
		datas.putInt("index", index);
		df.setArguments(datas);
		return df;
	}
	
	//得到当期显示数据的下标
	public int getShowIndex(){
		return getArguments().getInt("index", 0);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		TextView tv = new TextView(getActivity());
		tv.setText(TilteFragment.data[getShowIndex()]);
		return tv;
	}
}
