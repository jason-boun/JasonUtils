package com.jason.jasonutils.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jason.jasonutils.R;

public class VersionUpdateUtil {
	private static final String VersionAddr = "http://camtalk.cootel.com/download/";
	private static final String XmlName[] = {"update_phone.xml", "update_pad.xml"};
    private static final int DOWNLOAD = 1;
    private static final int DOWNLOAD_FINISH = 2;
    
    private static final int APK_ERR = 3;
	private Context mContext;
	public static boolean cancelUpdate = false;
	public boolean isForceUpdate = false;
	public boolean isVersionSame = false;
	static boolean isCloseActivityFlag = false;
	
	private ProgressBar mProgress;
	private int progress;
	private Dialog mDownloadDialog;
	
	private String mSavePath;
	
	private HashMap<String, String> mHashMap;
	
    @SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case DOWNLOAD:
                mProgress.setProgress(progress);// 更新下载进度
                break;
            case DOWNLOAD_FINISH:
                installApk();//安装成功后，功能介绍项中的new标记为隐藏状态：
                break;
            case APK_ERR:
            	Toast.makeText(mContext, "无法下载到APK文件请检查网络是否正常", Toast.LENGTH_LONG).show();
            	if(isCloseActivityFlag) {
            		isCloseActivityFlag = false;
            		new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							((Activity)mContext).finish();
						}
					}, 1000*1);
            	}
            	break;
            default:
                break;
            }
        };
    };

    public VersionUpdateUtil(Context context) {
        this.mContext = context;
    }
    
    public boolean isForceUpdate()  {
    	return isForceUpdate;
    }
    
    public boolean isVersionSame() {
		return isVersionSame;
	}
	public static String getVersionAddr(int type){
		String add = null;
		if(type < XmlName.length)
			add = VersionAddr+XmlName[type];	//type: 0:phone 1:pad	
		return add;
	}
	
	public static String getVersionName(int type){
		String name = null;
		if(type < XmlName.length)
			name = XmlName[type];		
		return name;
	}
    /**
     * 下载服务器端的版本配置XML文件
     */
    public void downloadFile (String strHttp){
		try {
	    	HttpURLConnection urlConn = (HttpURLConnection) new URL(strHttp).openConnection();
	    	urlConn.setConnectTimeout(10000);
	    	InputStream is = urlConn.getInputStream();
    		File versionfile = mContext.getFileStreamPath(getVersionName(0));
    		FileOutputStream fos= new FileOutputStream(versionfile);
            byte buf[] = new byte[1024];
            do {
                int numread = is.read(buf);
                if (numread <= 0) {
                    break;
                }
                fos.write(buf, 0, numread);
            } while (!cancelUpdate);
            fos.close();
            is.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
    }
    /**
     * 判断服务端的版本配置文件是否已经下载到手机本地
     */
    public boolean Fileisexists(String filename) {
    	File file = this.mContext.getFileStreamPath(filename); 
        if(!file.exists()){
        	return false;
        }
		return true;
    }
    
    /**
     * 解析版本配置文件，判断是否需要升级
     */
    public boolean isUpdate() throws IOException  {
    	String versionName = "";        
        try {
        	versionName = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;// 获取手机现在运行的软件版本
            File file = this.mContext.getFileStreamPath(getVersionName(0)); 
            if(!file.exists()){
            	return false;
            }
            InputStream in =  new FileInputStream(file);	
            mHashMap = parseXml(in);// 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != mHashMap) {
        	String serviceName = mHashMap.get("version");
        	String lastForcedUpdateVersion = mHashMap.get("lastForcedUpdateVersion");
        	
        	if(!Utils.isStrEmpty(versionName)&&!Utils.isStrEmpty(lastForcedUpdateVersion)&&versionName.compareToIgnoreCase(lastForcedUpdateVersion)<0) {
        		isForceUpdate = true;
        		return true;
        	}
            if(!Utils.isStrEmpty(versionName)&&!Utils.isStrEmpty(serviceName)&&versionName.compareToIgnoreCase(serviceName)==0)  {
            	isVersionSame = true;
            }
        	if(!Utils.isStrEmpty(versionName)&&!Utils.isStrEmpty(serviceName)&&versionName.compareToIgnoreCase(serviceName)<0) {
        		return true;
        	}
        }
        return false;       	
    }
    /**
     * 检测软件更新
     */
    public void checkUpdate() throws NotFoundException, IOException {
        if (isUpdate()) {
            showNoticeDialog(false);// 显示提示对话框
        } else {
            Toast.makeText(mContext, "已经是最新版本了", Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * 显示软件更新对话框，提示是否下载APK文件
     */
    public void showNoticeDialog(final boolean iscloseActivity) {
    	isCloseActivityFlag = iscloseActivity;
    	String msg = "检测到新版本，立即更新吗";
    	if(isCloseActivityFlag) {
    		msg = "有新版本发布，请更新后使用!";
    	}
    	AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
    	builder.setTitle("软件更新").setMessage(msg)
    	.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showDownloadDialog(iscloseActivity);
			}
		})
		.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(isCloseActivityFlag) {
					((Activity)mContext).finish();	
				}
			}
		}).show();
    }

    
    /**
     * 开始下载APK文件，并显示下载进度对话框
     */
    private void showDownloadDialog(final boolean iscloseActivity) {
    	isCloseActivityFlag = iscloseActivity;
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle("正在更新");
        View v = View.inflate(mContext, R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) View.inflate(mContext, R.layout.softupdate_progress, null).findViewById(R.id.update_progress);
        builder.setView(v);
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                cancelUpdate = true;// 设置取消状态
                if(iscloseActivity) {
                	((Activity)mContext).finish();
                }
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.setCancelable(false);
        mDownloadDialog.show();
        downloadApk(mProgress);// 下载文件
    }
    
    /**
     * 开启子线程下载APK文件
     */
    private void downloadApk(ProgressBar mProgress) {
    	new Thread(){
    		public void run() {
    			try {
    				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
    					String sdpath = Environment.getExternalStorageDirectory() + "/";
    					mSavePath = sdpath + "download";
    					URL url = new URL(mHashMap.get("url"));
    					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    					conn.connect();
    					int length = conn.getContentLength();
    					InputStream is = conn.getInputStream();
                        File file = new File(mSavePath);
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        File apkFile = new File(mSavePath, mHashMap.get("name"));
                        FileOutputStream fos = new FileOutputStream(apkFile);
                        int count = 0;
                        byte buf[] = new byte[1024];
                        do {
                            int numread = is.read(buf);
                            count += numread;
                            progress = (int) (((float) count / length) * 100);
                            mHandler.sendEmptyMessage(DOWNLOAD);
                            if (numread <= 0) {
                                mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                                break;
                            }
                            fos.write(buf, 0, numread);
                        } while (!cancelUpdate);// 点击取消就停止下载.
                        fos.close();
                        is.close();
                    }
                } catch (MalformedURLException e)  {
                	mHandler.sendEmptyMessage(APK_ERR);
                    e.printStackTrace();
                } catch (IOException e) {
                	mHandler.sendEmptyMessage(APK_ERR);
                    e.printStackTrace();
                } catch(Exception e) {
                	mHandler.sendEmptyMessage(APK_ERR);
                	e.printStackTrace();
                }
                mDownloadDialog.dismiss();// 取消下载对话框显示
    		};
    	}.start();
    }
    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkfile = new File(mSavePath, mHashMap.get("name"));
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }
    
    /**
     * 解析XML文件，封装为Map数据
     */
    public static HashMap<String, String> parseXml(InputStream in) throws Exception{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		try{  
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(in,"utf-8");
			int eventType = parser.getEventType();  
			while (eventType != XmlPullParser.END_DOCUMENT) {  
				if (eventType == XmlPullParser.START_TAG) {  
					if ("lastForcedUpdateVersion".equals(parser.getName())) {
						hashMap.put("lastForcedUpdateVersion",parser.nextText());
					}else if ("version".equals(parser.getName())) {
						hashMap.put("version",parser.nextText());
					}else if (("name".equals(parser.getName()))) {
						hashMap.put("name",parser.nextText());//软件名称
					}else if (("url".equals(parser.getName()))) {
						hashMap.put("url",parser.nextText());//下载地址
					}else if (("sip_server".equals(parser.getName()))) {
						hashMap.put("sip_server",parser.nextText());//voip
					}else if (("sip_port".equals(parser.getName()))) {
						hashMap.put("sip_port",parser.nextText());
					}else if (("sip_main_account".equals(parser.getName()))){
						hashMap.put("sip_main_account",parser.nextText());
					}else if (("sip_main_token".equals(parser.getName()))){
						hashMap.put("sip_main_token",parser.nextText());
					}else if (("sip_app_id".equals(parser.getName()))){
						hashMap.put("sip_app_id",parser.nextText());
					}
				}  
				eventType = parser.next();  
			}  
		} catch (Exception e) {  
			e.printStackTrace();
		}  
		return hashMap;
    }
    
    /**
     * 对外暴露检查版本更新方法
     */
    public void checkVersion(Activity act,Handler handler,ProgressDialog g_dialog){
    	
    }
}
