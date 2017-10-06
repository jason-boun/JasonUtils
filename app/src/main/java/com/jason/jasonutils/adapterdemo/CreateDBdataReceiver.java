package com.jason.jasonutils.adapterdemo;

import java.util.ArrayList;
import java.util.List;

import com.jason.jasonutils.handler.AsyncQueryHandlerDAO;
import com.jason.jasonutils.handler.AsyncQueryHandlerDemoDBHelper;
import com.jason.jasonutils.sortsequence.PinYinUtil;
import com.jason.jasonutils.sortsequence.SortBean;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CreateDBdataReceiver extends BroadcastReceiver {

	public final static String CREATE_DATABASE_INFO = "jason_utils_create_database_info";
	private boolean memoryHasData = false;
	private List<SortBean> packageBeanList = new ArrayList<SortBean>();
	private AsyncQueryHandlerDAO dao;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(CREATE_DATABASE_INFO)){
			generateData(context);
		}
	}

	/**
	 * 向数据库插入数据
	 */
	private void generateData(Context context) {
		dao = new AsyncQueryHandlerDAO(new AsyncQueryHandlerDemoDBHelper(context));
		if(!dao.DbHasData()){
			if(!memoryHasData){
				packageBeanData();
			}
			if(packageBeanList.size()>0){
				for(SortBean sb : packageBeanList){
					dao.insertBeanData(sb);//关键点
				}
			}
			Toast.makeText(context, "插入数据完成", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 封装数据
	 */
	private List<SortBean> packageBeanData() {
		String[] nameArray = new String[]{"张三","李四","王五","赵六","周七","孙八","李波","Jason","Bob","James","123"};
		Long[] numberArray =new Long[]{13866123l,12348456l,8894564l,8461236l,5417842l,1445679l,65789684l,18641235l,3694785641l,54645687l,3578468l};
		SortBean sortBean = null;
		for(int i=0;i<nameArray.length;i++){
			sortBean = new SortBean();
			sortBean.setName(nameArray[i]);
			sortBean.setNumber(numberArray[i]);
			sortBean.setPinYin(PinYinUtil.getPingYin(nameArray[i]));//获取name对应的拼音
			sortBean.setPhoto(new byte[]{});
			packageBeanList.add(sortBean);
		}
		memoryHasData = true;
		return packageBeanList;
	}
}
