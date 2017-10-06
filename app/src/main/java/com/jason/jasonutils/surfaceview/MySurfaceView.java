package com.jason.jasonutils.surfaceview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	
	private SurfaceHolder holder;
	private MyThread myThread;
	
	public MySurfaceView(Context context) {
		super(context);
		
		holder = getHolder();
		holder.addCallback(this);
		myThread = new MyThread();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		myThread.setRun(true);
		myThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		myThread.setRun(false);
		SystemClock.sleep(1000);
		myThread.stop();
	}
	
	/**
	 * 内部类，线程
	 */
	public class MyThread extends Thread{
		private boolean run;
		public boolean isRun() {
			return run;
		}
		public void setRun(boolean run) {
			this.run = run;
		}
		@Override
		public void run() {
			int count = 0;
			Canvas canvas = null;
			while(run){
				try {
					canvas = holder.lockCanvas();
					canvas.drawColor(Color.WHITE);
					
					Paint paint = new Paint();
					paint.setColor(Color.BLACK);
					paint.setTextSize(30);
					
					Rect rect = new Rect(100, 20, 380, 330);
					
					canvas.drawRect(rect, paint);
					count += 2;
					canvas.drawText("Interval = " + count + " seconds.", 100, 410, paint);
					Thread.sleep(2000);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally{
					if(canvas != null){
						holder.unlockCanvasAndPost(canvas);
					}
				}
			}
		}
	}

}
