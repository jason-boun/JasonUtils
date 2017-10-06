package com.jason.jasonutils.widget;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class MyWidget extends AppWidgetProvider {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

	@Override
	public void onEnabled(Context context) {
		context.startService(new Intent(context,MyWidgetService.class));//增加小窗口组件时调用
		super.onEnabled(context);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		context.stopService(new Intent(context,MyWidgetService.class));//移除小窗口组件时调用
	}

}
