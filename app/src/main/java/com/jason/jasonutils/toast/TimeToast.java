package com.jason.jasonutils.toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

public class TimeToast {
    private Toast toast;  
    private Field field;  
    private Object obj;  
    private Method showMethod;  
    private Method hideMethod;  
    private int time;  
  
    public TimeToast(Context c, String text,int time) {  
        toast = Toast.makeText(c, text, time);  
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);  
        this.time=time;  
        reflectionTN();  
    }  
public void show(){  
    toast.show();  
    Handler handler = new Handler();  
    handler.postDelayed(new Runnable() {  
  
        @Override  
        public void run() {  
            try {  
                hideMethod.invoke(obj, new Object[]{});// 调用TN对象的hide()方法，关闭toast
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }, time);  
      
}  
    private void reflectionTN() {  
        try {  
            field = toast.getClass().getDeclaredField("mTN");  
            field.setAccessible(true);  
            obj = field.get(toast);  
            showMethod = obj.getClass().getDeclaredMethod("show", new Class<?>[]{});
            hideMethod = obj.getClass().getDeclaredMethod("hide", new Class<?>[]{});
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    } 
}
