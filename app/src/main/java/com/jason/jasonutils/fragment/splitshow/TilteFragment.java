package com.jason.jasonutils.fragment.splitshow;

import com.jason.jasonutils.R;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TilteFragment extends ListFragment {

	public static String[] data = new String[]{
			"星期一",
			"星期二",
			"星期三",
			"星期四",
			"星期五",
			"星期六",
			"星期天"
			};
	
	private int mCurrentPosition = 0;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, android.R.id.text1, data));
		showDetail(mCurrentPosition);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		showDetail(position);
	}
	
	/**
	 * 显示详情信息
	 */
	private void showDetail(int index){
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		getListView().setItemChecked(index, true);//指定listview选定条目
		
		//Fragment的管理器
		FragmentManager fm = getFragmentManager();
		DetailFragment df = (DetailFragment) fm.findFragmentById(R.id.detail);
		
		if(df == null || df.getShowIndex() != index){
			df = DetailFragment.getInstance(index);
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.detail, df);
			ft.commit();
		}
	}
}
