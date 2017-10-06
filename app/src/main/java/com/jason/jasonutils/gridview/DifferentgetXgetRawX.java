package com.jason.jasonutils.gridview;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.MLog;

/**
 * MotionEvent中getX()和getRawX()的区别
 * @author jiabo
 */
public class DifferentgetXgetRawX extends Activity implements OnTouchListener {
	
	Button btn = null;  
	int x = 0;  
    int y = 0;  
    int rawx = 0;  
    int rawy = 0;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getx_getrawx_different);
		btn = (Button) findViewById(R.id.bt_button);  
        btn.setOnTouchListener(this);  
	}

	/**
	 * getX()是表示Widget相对于自身左上角的x坐标,而getRawX()是表示相对于屏幕左上角的x坐标值
	 * (注意:这个屏幕左上角是手机屏幕左上角,不管activity是否有titleBar或是否全屏幕),getY(),getRawY()一样的道理
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int eventaction = event.getAction();  
        switch (eventaction) {  
        case MotionEvent.ACTION_MOVE:  
            x = (int) event.getX();  
            y = (int) event.getY();  
            rawx = (int) event.getRawX();  
            rawy = (int) event.getRawY();  
            
            Toast.makeText(this, "getX=" + x + "getY=" + y + "\n" + "getRawX=" + rawx + "getRawY=" + rawy + "\n", Toast.LENGTH_LONG).show();
            MLog.i("DEBUG", "getX=" + x + "getY=" + y + "\n" + "getRawX=" + rawx + "getRawY=" + rawy + "\n");  
            break;  
        }  
        return false; 
	}
}
