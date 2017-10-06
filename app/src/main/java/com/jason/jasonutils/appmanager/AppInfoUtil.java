package com.jason.jasonutils.appmanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 获取手机安装应用的具体信息
 */
public class AppInfoUtil {
	
	
	/**
	 * 获取手机安装的所有APP的信息
	 * @param context
	 * @return
	 */
	public static List<AppInfoBean> getAppInfo(Context context){
		
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> PackageInfos = pm.getInstalledPackages(0);
		
		List<AppInfoBean> infos = new ArrayList <AppInfoBean>();
		AppInfoBean bean = null;
		
		for(PackageInfo info : PackageInfos){
			bean = new AppInfoBean();
			bean.setPackName(info.packageName);
			bean.setVersion(info.versionName);
			bean.setAppName(info.applicationInfo.loadLabel(pm).toString());
			bean.setAppIcon(info.applicationInfo.loadIcon(pm));
			
			if((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==0){
				bean.setUserApp(true);
			}else{
				bean.setUserApp(false);
			}
			
			if((info.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0){
				bean.setInRom(true);
			}else{
				bean.setInRom(false);
			}
			
			infos.add(bean);
		}
		return infos;
	}

}
