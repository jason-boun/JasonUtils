package com.jason.jasonutils.gridview;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.jason.jasonutils.R;

public class SingleLineGridViewSimpleDemo extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.singleline_gridview_activity);  
        
		initGridView();
	}
	
	public void initGridView(){
		GridView gridview = (GridView) findViewById(R.id.grid);  
        ImageAdapter adapter = new ImageAdapter(this);//一个继承BaseAdapter的自定义适配器  
        adapter.setImages(getImages());
        gridview.setAdapter(adapter);
        gridview.setNumColumns(adapter.getCount());
  
        gridview.setOnItemClickListener(new OnItemClickListener() {  
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(SingleLineGridViewSimpleDemo.this, "" + position, Toast.LENGTH_SHORT).show();
			}  
        });
	}
	  /** 
     * 取图片
     * @return 
     */  
    private List<Bitmap> getImages() {  
        List<Bitmap> list = new ArrayList<Bitmap>();  
        for (int i=1; i<=12; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
            list.add(bitmap);  
        }  
        return list;
    } 
	
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;
	    private List<Bitmap> images = new ArrayList<Bitmap>();

	    public ImageAdapter(Context c) {
	        mContext = c;
	    }

	    public List<Bitmap> getImages() {
			return images;
		}
		public void setImages(List<Bitmap> images) {
			this.images = images;
		}

		public int getCount() {
	        return images.size();
	    }

	    public Object getItem(int position){
	        return images.get(position);
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView;
	        if (convertView == null) {
	            imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(50, 50));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(5, 5, 5, 5);
	        } else {
	            imageView = (ImageView) convertView;
	        }
	        imageView.setImageBitmap(images.get(position));
	        return imageView;
	    }
	}
	
}
