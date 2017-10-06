package com.jason.jasonutils.tools;

import android.app.ProgressDialog;
import android.content.Context;

public class DialogUtil {
	

	/**
	 * 饼状进度条
	 * @param tipStr 提示信息
	 * @return
	 */
	public static void showProgressDialog(ProgressDialog progressDialog, Context context, String title, String tipStrMsg){
		dismissDilaog(progressDialog);
		progressDialog =ProgressDialog.show(context, title, tipStrMsg);
	}
	/**
	 * 取消进度条
	 * @param progressDialog
	 */
	public static void dismissDilaog(ProgressDialog progressDialog){
		if(progressDialog!=null){
			if(progressDialog.isShowing()){
				progressDialog.dismiss();
			}
			progressDialog = null;
		}
	}

}
