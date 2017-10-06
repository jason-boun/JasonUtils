package com.jason.jasonutils.fixpicture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyImageView extends ImageView {

	private int co;
	private int borderwidth;
	
	public MyImageView(Context context) {
		super(context);
	}
	
	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public MyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

    //设置颜色
	public void setColour(int color){
		co = color;
	}
	//设置边框宽度
	public void setBorderWidth(int width){
		borderwidth = width;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 画边框
		Rect rec = canvas.getClipBounds();
		rec.bottom--;
		rec.right--;
		
		//设置边框颜色、宽度
		Paint paint = new Paint();
		paint.setColor(co);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(borderwidth);
		
		canvas.drawRect(rec, paint);
	}
}
