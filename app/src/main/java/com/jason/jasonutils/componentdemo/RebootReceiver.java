package com.jason.jasonutils.componentdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.jason.jasonutils.tools.MLog;

public class RebootReceiver extends BroadcastReceiver {
	
	private static final String REBOOT_ACTION = "android.intent.action.BOOT_COMPLETED";
	//private static final String POWER_PRESENT = "android.intent.action.USER_PRESENT";
	//private static final String SCREEN_OFF = "android.intent.action.SCREEN_OFF";
	//private static final String SCREEN_ON = "android.intent.action.SCREEN_ON";
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if("start_reboot_broadcast".equals(intent.getAction())){
			MLog.i("哈哈", "收到手动发送的广播通知了");
			Intent service = new Intent(context, RebootService.class);
			service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(service);
		}else if(REBOOT_ACTION.equals(intent.getAction())){
			MLog.i("哈哈", "收到手机重启广播通知了");
			Intent service = new Intent(Intent.ACTION_RUN);
			service.setClass(context, RebootService.class);
			service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(service);
		}/*else if(POWER_PRESENT.equals(intent.getAction())){
			MLog.i("哈哈", "收到按压电源键广播通知了");
			Intent service = new Intent(context, RebootService.class);
			service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(service);
		}else if(SCREEN_OFF.equals(intent.getAction())){
			MLog.i("哈哈", "解锁");
			Intent service = new Intent(Intent.ACTION_RUN);
			service.setClass(context, RebootService.class);
			service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(service);
		}else if(SCREEN_ON.equals(intent.getAction())){
			MLog.i("哈哈", "锁屏");
			Intent service = new Intent(Intent.ACTION_RUN);
			service.setClass(context, RebootService.class);
			service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(service);
		}*/
	}
}
