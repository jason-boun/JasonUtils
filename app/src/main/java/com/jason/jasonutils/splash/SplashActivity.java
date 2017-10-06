package com.jason.jasonutils.splash;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.jason.jasonutils.tools.MLog;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.JasonUtilsMainActivity;
import com.jason.jasonutils.R;
import com.jason.jasonutils.pwdlock.PwdLockActivity;
import com.jason.jasonutils.pwdlock.Constant;
import com.jason.jasonutils.pwdlock.FirstSetPwdLockActivity;
import com.jason.jasonutils.tools.CopyFileUtils;
import com.jason.jasonutils.tools.ShortCutUtil;

public class SplashActivity extends Activity {
	
	public static final String TAG = SplashActivity.class.getSimpleName();
	public static final int PARSE_SUCCESS = 1;
	public static final int PARSE_ERROR = 2;
	public static final int SERVER_ERROR = 3;
	public static final int URL_ERROR = 4;
	public static final int NETWORK_ERROR = 5;
	protected static final int DOWNLOAD_ERROR = 6;
	protected static final int SDCARD_ERROR = 7;
	protected static final int DOWNLOAD_SUCCESS = 8;
	private PackageManager pm;
	private TextView tv_splash_version,tv_splash_appname;
	private UpdateInfoBean updateInfo;
	private ProgressDialog pd;
	private String savePath = Environment.getExternalStorageDirectory()+"/new_JasonUtils.apk";
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {			case PARSE_SUCCESS:
				Toast.makeText(SplashActivity.this, "解析成功..", Toast.LENGTH_SHORT).show();
				if (getAppVersion().equals(updateInfo.getVersion())) {
					loadHomeUI();
				} else {
					showUpdateDialog();
				}
				break;
			case PARSE_ERROR:
				Toast.makeText(getApplicationContext(), "解析失败..", Toast.LENGTH_SHORT).show();
				loadHomeUI();
				break;
			case SERVER_ERROR:
				Toast.makeText(getApplicationContext(), "服务器错误..", Toast.LENGTH_SHORT).show();
				loadHomeUI();
				break;
			case URL_ERROR:
				Toast.makeText(getApplicationContext(), "url错误..", Toast.LENGTH_SHORT).show();
				loadHomeUI();
				break;
			case NETWORK_ERROR:
				Toast.makeText(SplashActivity.this, "网络错误..", Toast.LENGTH_SHORT).show();
				loadHomeUI();
				break;
			case DOWNLOAD_ERROR:
				Toast.makeText(getApplicationContext(), "下载失败 ..", Toast.LENGTH_SHORT).show();
				loadHomeUI();
				break;
			case SDCARD_ERROR:
				Toast.makeText(getApplicationContext(), "SD卡错误 ..", Toast.LENGTH_SHORT).show();
				loadHomeUI();
				break;
			case DOWNLOAD_SUCCESS:
				File file = (File) msg.obj;//下载成功了.. 替换安装下载的apk.
				installApk(file);
				break;}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		splashAnimation(findViewById(R.id.rl_splash));//设置splash动画
		init();
	}
	
	/**
	 * 初始化控件
	 */
	private void init() {
		pm = getPackageManager();
		
		tv_splash_appname = (TextView) findViewById(R.id.tv_splash_appname);
		tv_splash_appname.getPaint().setFakeBoldText(true);//加粗
		//tv_splash_appname.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.getPaint().setFakeBoldText(true);//加粗
		
		tv_splash_version.setText("版本:" + getAppVersion());
		new Thread(new CheckVersionTask()).start();// 检查服务器上的版本信息
		
		copyNumberDB();
		copyCommonDB();
	}
	
	/**
	 * 拷贝资产目录下的数据库 释放到 手机的文件系统里面. /data/data/包名/files
	 */
	public void copyNumberDB(){
		final File addressDBFile = new File(this.getFilesDir(), "address.db");
		if (addressDBFile.exists() && addressDBFile.length() > 0) {
			MLog.i(TAG, "文件已经存在,不需要再次拷贝");
		} else {
			MLog.i(TAG, "文件不存在,拷贝数据库");
			new Thread() {
				public void run() {
					File f = CopyFileUtils.copyAssetsFile(SplashActivity.this,
							"address.db", addressDBFile.getAbsolutePath());
					if (f == null) {
						MLog.i(TAG, "拷贝失败");
					} else {
						MLog.i(TAG, "拷贝成功");
					}
				};
			}.start();
		}
	}
	
	/**
	 * 拷贝常用号码数据库
	 */
	public void copyCommonDB(){
		//拷贝常用号码数据库
		final File commonDBFile = new File(this.getFilesDir(), "commonnum.db");
		if (commonDBFile.exists() && commonDBFile.length() > 0) {
			MLog.i(TAG, "文件已经存在,不需要再次拷贝");
		} else {
			MLog.i(TAG, "文件不存在,拷贝数据库");
			new Thread() {
				public void run() {
					File f = CopyFileUtils.copyAssetsFile(SplashActivity.this, "commonnum.db", commonDBFile.getAbsolutePath());
					if (f == null) {
						MLog.i(TAG, "拷贝失败");
					} else {
						MLog.i(TAG, "拷贝成功");
					}
				};
			}.start();
		}
	}
	
	/**
	 * 弹出来升级提醒的对话框
	 */
	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("升级提醒");
		builder.setMessage(updateInfo.getDescription());
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 下载新的版本 . 替换安装
				pd = new ProgressDialog(SplashActivity.this);
				pd.setTitle("更新提醒");
				pd.setMessage("正在下载新的apk...");
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.show();//注意 界面的更新  必须写在主线程
				new Thread() {
					public void run() {
						Message msg = Message.obtain();
						try {
							File file = DownLoadManager.downLoad(updateInfo.getPath(), savePath, pd);
							if (file != null) {
								msg.what = DOWNLOAD_SUCCESS;
								msg.obj = file;
							} else {
								msg.what = DOWNLOAD_ERROR;
							}
						} catch (Exception e) {
							e.printStackTrace();
							msg.what = SDCARD_ERROR;
						}finally{
							handler.sendMessage(msg);
							pd.dismiss();
						}
					};
				}.start();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				loadHomeUI();
			}
		});
		builder.show();
	}

	/**
	 * 异步线程查询版本是否需要更新
	 */
	private class CheckVersionTask implements Runnable {
		@Override
		public void run() {
			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			boolean update = sp.getBoolean("update", false);
			if(!update){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				loadHomeUI();
				return;
			}
			Message msg = Message.obtain();
			long startTime = System.currentTimeMillis();// 判断 如果时间小于5秒 睡一下 睡够5秒
			try {
				URL url = new URL(getResources().getString(R.string.serverurl));
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(2000);
				int code = conn.getResponseCode();
				if (code == 200) {
					InputStream is = conn.getInputStream();
					updateInfo = UpdateInfoParser.getUpdateInfo(is);
					if (updateInfo != null) {
						msg.what = PARSE_SUCCESS;
					} else {
						msg.what = PARSE_ERROR;
					}
				} else {
					msg.what = SERVER_ERROR;
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				msg.what = URL_ERROR;
			} catch (NotFoundException e) {
				e.printStackTrace();
				msg.what = URL_ERROR;
			} catch (IOException e) {
				e.printStackTrace();
				msg.what = NETWORK_ERROR;
			} finally {
				long endtime = System.currentTimeMillis();
				long dTime = endtime - startTime;
				if (dTime < 2000) {
					try {
						Thread.sleep(2000 - dTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				handler.sendMessage(msg);
			}
		}
	}

	/**
	 * 给一个view设置动画
	 * @param view
	 */
	public void splashAnimation(View view){
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
	}
	
	/**
	 * 安装一个新的APK
	 * @param file
	 */
	protected void installApk(File file) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		startActivity(intent);
	}
	
	/**
	 * 获取当前应用程序的版本号
	 * @return
	 */
	public String getAppVersion() {
		PackageInfo packInfo;
		try {
			packInfo = pm.getPackageInfo(getPackageName(), 0);
			String version = packInfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 进入主界面
	 */
	public void loadHomeUI() {
		Intent intent = null;
		String lock_pwd = getSharedPreferences(Constant.MYSP_CONFIG_FILENAME, MODE_PRIVATE).getString(Constant.APP_LOCK_KEY, "");
		Boolean lock_turn_on = getSharedPreferences(Constant.MYSP_CONFIG_FILENAME, MODE_PRIVATE).getBoolean(Constant.APP_LOCK_TURN_ON, false);
		Boolean app_lock_skip_set = getSharedPreferences(Constant.MYSP_CONFIG_FILENAME, MODE_PRIVATE).getBoolean(Constant.APP_LOCK_SKIP_SET, false);
		if(app_lock_skip_set){
			intent = new Intent(this, JasonUtilsMainActivity.class);
		}else if(TextUtils.isEmpty(lock_pwd)){
			intent = new Intent(this, FirstSetPwdLockActivity.class);
		}else if(!TextUtils.isEmpty(lock_pwd) && lock_turn_on){
			intent = new Intent(this, PwdLockActivity.class);
		}else{
			intent = new Intent(this, JasonUtilsMainActivity.class);
		}
		startActivity(intent);
		CreateShortCut();
		finish();
	}

	/**
	 * 创建快捷图标
	 */
	public void CreateShortCut(){
		ShortCutUtil.createShortCut(this,SplashActivity.class, "JasonUtils", R.drawable.app_tool_2);
	}

}
