package com.jason.jasonutils.cache;

import android.content.Context;

public class FileManager {

    public static String getSaveFilePath(Context context) {
        if (CommonUtil.hasSDCard()) {
            return CommonUtil.getRootFilePath() + context.getPackageName()+"/files/";
        } else {
            return CommonUtil.getRootFilePath() + context.getPackageName()+ "/files/";
        }
    }
}