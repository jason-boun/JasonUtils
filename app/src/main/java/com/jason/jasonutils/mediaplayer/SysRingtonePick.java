package com.jason.jasonutils.mediaplayer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public class SysRingtonePick extends Activity {
	
	private String ringtoneUri = null;//"content://settings/system/ringtone"
	protected String path = null;
	protected static final String SYS_RINGTONE_PATH_KEY = "sys_ringtone_path_key";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * 通过Intent获取系统RingTone的URI
	 */
	public void picSysRingTone(){
		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
		Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, ringUri);
		startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK && requestCode == 1 && data !=null){
			ringtoneUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI).toString();
			if(ringtoneUri != null){	//ringtoneUri===content://media/internal/audio/media/79
				String[] proj={MediaStore.Images.Media.DATA};
				Cursor cursor = getContentResolver().query(Uri.parse(ringtoneUri), proj, null, null, null);
				int column_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				path = cursor.getString(column_index);	//path===/system/media/audio/ringtones/Aquila.ogg
				getSharedPreferences("config", MODE_PRIVATE).edit().putString(SYS_RINGTONE_PATH_KEY, path).commit();
			}
		}
	}
	
}
