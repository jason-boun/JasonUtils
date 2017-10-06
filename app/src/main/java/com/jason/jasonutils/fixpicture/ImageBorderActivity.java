package com.jason.jasonutils.fixpicture;

import com.jason.jasonutils.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

public class ImageBorderActivity extends Activity {
	
	private MyImageView image = null;
	private MyImageView image1 = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.border_activity);
        
        image = (MyImageView)findViewById(R.id.iamge);
        image.setColour(Color.YELLOW);
        image.setBorderWidth(10);
        
        image1 = (MyImageView)findViewById(R.id.iamge1);
        image1.setColour(Color.GREEN);
        image1.setBorderWidth(5);
    }

}
