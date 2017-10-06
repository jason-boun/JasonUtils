package com.jason.jasonutils.gridview;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;
/**
 * 让gridView中的点击其他条目时候有相应事件，点击gridView中的其他非item部分，相应另一部分事件
 * @author Administrator
 */
public class GridViewSimpleDemo extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupGridView();
	}

	private void setupGridView() {
		 String[] data = new String[]{"aaa", "bbb", "CCC", "ddd", "eee", "fff", "GGG","GGG","GGG","GGG","GGG","GGG","GGG","GGG","GGG","GGG"};
	     GridView gdView = new GridView(this);
	     gdView.setMinimumWidth(100);
	     gdView.setNumColumns(4);
	     gdView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data));
	     setContentView(gdView);
	     
	     /**
	      * item条目之外点击的相应事件：可以在for循环区域之外的地方实现需要的动作。
	      */
	     gdView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
                    
                    float rawX = event.getRawX();
                    float rawY = event.getRawY();
                    GridView gv = (GridView)v;
                    
                    int childSize = gv.getChildCount();
                    for(int i = 0; i < childSize; i++){
                        View child = gv.getChildAt(i);
                        Rect r = new Rect();
                        child.getGlobalVisibleRect(r);
                        if(rawX > r.left && rawX < r.right && rawY > r.top && rawY < r.bottom){
                            System.out.println("i am on child view ");
                            Toast.makeText(GridViewSimpleDemo.this, "子view被点到了", Toast.LENGTH_SHORT).show();
                            return false;//如果返回时false则点击事件会继续向下传递，比如相应onItemClick事件，如果返回true则阻止点击事件进一步传递
                        }
                    }
                    Toast.makeText(GridViewSimpleDemo.this, "子view之外的区域被点击到了", Toast.LENGTH_SHORT).show();
                    return false; 
				}
				return false;
			}
		});
	     
	     /**
	      * item条目点击相应事件
	      */
	     gdView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(GridViewSimpleDemo.this, "子view " +position+" 被点击了", Toast.LENGTH_SHORT).show();
			}
		});
	}

}
