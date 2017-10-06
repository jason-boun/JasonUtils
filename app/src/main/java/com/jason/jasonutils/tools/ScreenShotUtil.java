package com.jason.jasonutils.tools;  
  
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
  
public class ScreenShotUtil {
	
	/**
	 *  保存当前界面截图
	 */
	public static void doScreenShot(final Activity act, File filePath){
		new Thread(){
			public void run() {
				shoot(act, null);
			};
		}.start();
	}
  
    private static Bitmap takeScreenShot(Activity activity) { 
        // View是你需要截图的View  
        View view = activity.getWindow().getDecorView();  
        view.setDrawingCacheEnabled(true);  
        view.buildDrawingCache();  
        Bitmap b1 = view.getDrawingCache();  
  
        // 获取状态栏高度  
        Rect frame = new Rect();  
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);  
        int statusBarHeight = frame.top;  
  
        // 获取屏幕长和高  
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();  
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();  
        // 去掉标题栏  
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);  
        view.destroyDrawingCache();  
        return b;  
    }  
  
    private static void savePic(Activity act ,Bitmap b, File filePath) {  
        FileOutputStream fos = null;  
        try {  
            fos = new FileOutputStream(filePath);  
            if (null != fos) {  
                b.compress(Bitmap.CompressFormat.PNG, 100, fos);  
                fos.flush();  
                fos.close();  
                Utils.ThreadShowToast(act, "成功保存截图到系统相册");
                Utils.notifySDCardReMounted(act);
            }  
        } catch (FileNotFoundException e) {  
             e.printStackTrace();  
             Utils.ThreadShowToast(act, "保存失败");
        } catch (IOException e) {  
             e.printStackTrace();  
             Utils.ThreadShowToast(act, "保存失败");
        }  
    }  
  
    private static void shoot(Activity a, File filePath) {  
    	if (filePath == null) {  
        	filePath = new File(Environment.getExternalStorageDirectory()+"/DCIM/Camera","JasonUtils_"+ Utils.getDateFormatStr()+".jpg");
        }  
        if (!filePath.getParentFile().exists()) {  
            filePath.getParentFile().mkdirs();  
        }
        SystemClock.sleep(1000);
        ScreenShotUtil.savePic(a,ScreenShotUtil.takeScreenShot(a), filePath); 
    }  
  
}