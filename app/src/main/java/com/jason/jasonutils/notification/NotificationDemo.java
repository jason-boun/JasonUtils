package com.jason.jasonutils.notification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jason.jasonutils.R;

@SuppressLint("NewApi")
public class NotificationDemo extends Activity {

	private Button bt_send_notification;
	private TextView tv_show_notifiction_info;
	private String comingNumber = "123456789";
	private String topic = "未接来电";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_demo_activity);

		init();
		showNotificationData();
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		bt_send_notification = (Button) findViewById(R.id.bt_send_notification);
		tv_show_notifiction_info = (TextView) findViewById(R.id.tv_show_notifiction_info);
		
		bt_send_notification.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tv_show_notifiction_info.setText("");
				//sendNotification();
				sendNotificationNewAPI();
			}
		});
	}

	/**
	 * 设置并发送一个notification
	 */
	private void sendNotification(){
		//①创建notification对象
		Notification notification = new Notification(android.R.drawable.stat_sys_phone_call,topic+": "+ comingNumber, System.currentTimeMillis());
		notification.flags = Notification.FLAG_AUTO_CANCEL ;
		
		Intent intent = new Intent(this, NotificationDemo.class);
		intent.putExtra("phoneNumber", "123456789");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		//②设置notification点击事件
		notification.setLatestEventInfo(this, topic, comingNumber, contentIntent);
		
		notificationSetting(notification);
		
		//③通过NotificationManager发送对象
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.notify(0, notification);
	}
	
	public void sendNotificationNewAPI(){
        Intent intent = new Intent(this,NotificationDemo.class);
        intent.putExtra("phoneNumber", topic+": "+ comingNumber);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(NotificationDemo.this, 0, intent, 0); 
        
        Notification noti =null;
        
        if(android.os.Build.VERSION.SDK_INT<11){
        	noti = new Notification(android.R.drawable.stat_sys_phone_call, topic+": "+ comingNumber, System.currentTimeMillis());
        	noti.flags = Notification.FLAG_AUTO_CANCEL;
        	noti.setLatestEventInfo(this, topic, comingNumber, contentIntent);
        }else{
        	Builder builder = new Notification.Builder(NotificationDemo.this);
            builder
            .setSmallIcon(android.R.drawable.stat_sys_phone_call)//设置状态栏里面的图标（小图标）　　　　　　　　　　　　　　　　　　　
       		.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.cootalk_status_icon))//下拉下拉列表里面的图标（大图标） 　　　　　　　
       		.setTicker(topic+": "+ comingNumber) //设置状态栏的显示的信息
       		.setWhen(System.currentTimeMillis())//设置时间发生时间
            .setContentTitle(topic)//设置下拉列表里的标题
            .setContentText(comingNumber)//设置上下文内容
            .setContentIntent(contentIntent)
            .setAutoCancel(true);//设置可以清除
            //noti = builder.build();
            noti = builder.getNotification();
        }
        noti.defaults =Notification.DEFAULT_SOUND;//设置为默认的声音
        setLed(noti);
        NotificationManager nm = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, noti);//显示通知;
	}

	private void notificationSetting(Notification notification) {
		//声音效果
		notification.defaults |= Notification.DEFAULT_SOUND;
		
		//震动效果
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		long[] vibrate = {0,100,200,300};
		notification.vibrate = vibrate;
		
		//闪屏效果
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.ledARGB = 0xff00ff00;
		notification.ledOnMS = 300;
		notification.ledOffMS = 1000;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
	}
	
	public void setLed(Notification notification){
		//亮1秒，暗1分钟，这样循环
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.ledARGB = 0xff00ff00;//设置颜色
		notification.ledOnMS = 1000;//设置led灯亮的时间
		notification.ledOffMS = 1000;//设置led灯暗的时间
		//问题出在了设置ledOffMS,根据SDK的说明:The number of milliseconds for the LED to be on while it's flashing. The hardware will do its best approximation.
		//就是时间的毫秒数，所以我这里赋值为60*1000（1分钟），但是实际出来的效果，灯暗的时间只有10s左右，远远未达到我设定的1分钟，
		//后来我又严证，将ledOnMS和ledOffMS都设置的比较小比如，ledOnMS=1000,ledOffMS=1000,效果没问题，很准确
	}

	/**
	 * 点击通知栏中的notification后，显示传递过来的信息
	 */
	private void showNotificationData() {
		Intent notifiIntent = getIntent();
		String info = notifiIntent.getStringExtra("phoneNumber");
		if(!TextUtils.isEmpty(info)){
			tv_show_notifiction_info.setText(info);
		}
	}

}
