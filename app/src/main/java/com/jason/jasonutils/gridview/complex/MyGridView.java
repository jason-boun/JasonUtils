package com.jason.jasonutils.gridview.complex;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
/**
 * 自定义GridView，使其可被ScrollView所包裹
 */
public class MyGridView extends GridView {
	 
	public MyGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyGridView(Context context, AttributeSet attrs) { 
		super(context, attrs);
	}

	public MyGridView(Context context) {
		super(context);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST));
	}

}
