package com.jason.jasonutils.sortsequence;

import java.util.HashMap;

import com.jason.jasonutils.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import com.jason.jasonutils.tools.MLog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ZmBar extends ImageButton {
	private TextView mDialogText;
	private Handler mHandler;
	private ListView mList;
	private float mHight;
	private String[] letters = new String[] { "#", "A", "B", "C", "D", "E",
			"F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
			"S", "T", "U", "V", "W", "X", "Y", "Z" };
	private HashMap<String, Integer> alphaIndexer;

	public ZmBar(Context context) {
		super(context);
	}

	public ZmBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	public ZmBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void init(Activity ctx) {
		mDialogText = (TextView) ctx.findViewById(R.id.sort_list_fast_position);
		mDialogText.setVisibility(View.INVISIBLE);
		mHandler = new Handler();
	}

	public void setListView(ListView mList) {
		this.mList = mList;
	}

	public void setAlphaIndexer(HashMap<String, Integer> alphaIndexer) {
		this.alphaIndexer = alphaIndexer;
	}

	public void setHight(float mHight) {
		this.mHight = mHight;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int act = event.getAction();
		float y = event.getY();
		final int oldChoose = choose;
		int selectIndex = (int) (y / (mHight / letters.length));
		MLog.i("哈哈", "selectIndex=="+selectIndex);
		MLog.i("哈哈", "y=="+y);
		MLog.i("哈哈", "mHight=="+mHight);
		MLog.i("哈哈", "letters.length=="+letters.length);
		
		if (selectIndex > -1 && selectIndex < letters.length) {
			String key = letters[selectIndex];
			if (alphaIndexer.containsKey(key)) {
				int pos = alphaIndexer.get(key);
				if (mList.getHeaderViewsCount() > 0) {//防止listView有标题view：HeaderView
					mList.setSelection(pos);
					this.mList.setSelectionFromTop(pos + mList.getHeaderViewsCount(), 0);
				} else {
					this.mList.setSelectionFromTop(pos, 0);
				}
				mDialogText.setText(letters[selectIndex]);
			}
		}

		switch (act) {
		case MotionEvent.ACTION_DOWN:
			if (oldChoose != selectIndex) {
				if (selectIndex > 0 && selectIndex < letters.length) {
					choose = selectIndex;
					invalidate();
				}
			}
			if (mHandler != null) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (mDialogText != null && mDialogText.getVisibility() == View.INVISIBLE) {
							mDialogText.setVisibility(VISIBLE);
						}
					}
				});
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (oldChoose != selectIndex) {
				if (selectIndex > 0 && selectIndex < letters.length) {
					choose = selectIndex;
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			choose = -1;
			if (mHandler != null) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (mDialogText != null && mDialogText.getVisibility() == View.VISIBLE) {
							mDialogText.setVisibility(INVISIBLE);
						}
					}
				});
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	Paint paint = new Paint();
	int choose = -1;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int height = getHeight();
		int width = getWidth();
		int singleHeight = height / letters.length;
		for (int i = 0; i < letters.length; i++) {
			paint.setColor(Color.WHITE);
			   if(height<=480) {
    	    	   paint.setTextSize(10);
    	       }else {
    	    	   paint.setTextSize(16);
    	       }
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			if (i == choose) {
				paint.setColor(Color.parseColor("#00BFFF"));
				paint.setFakeBoldText(true);
			}
			float xPos = width / 2 - paint.measureText(letters[i]) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(letters[i], xPos, yPos, paint);
			paint.reset();
		}
	}
}
