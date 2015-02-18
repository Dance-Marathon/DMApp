package com.uf.dancemarathon;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MapActivity extends Activity {

	private TouchImageView mMap;
	private ArrayList<ImageFrame> frames;
	
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		mMap = (TouchImageView) findViewById(R.id.map_image);
		
		initFrames();
		mMap.setOnTouchListener(new MyTouchListener());
		
	}
	
	private void initFrames()
	{
		ImageFrame f1 = new ImageFrame(0.0, 0.0, 1.0, 0.5);
		
		frames = new ArrayList<ImageFrame>();
		
		frames.add(f1);
	}
	
	private void makeFrameToast()
	{
		Toast t = Toast.makeText(this, "IT WORKED!!", Toast.LENGTH_LONG);
		t.show();
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void handleTouchesForFrames(float x, float y, float imgWidth, float imgHeight)
	{
		for(int i = 0; i < frames.size(); i++)
		{
			ImageFrame f = frames.get(i);
			
			if(f.isPointInFrame(x, y,imgWidth, imgHeight))
				makeFrameToast();
			
		}
	}
	
	private class MyTouchListener implements View.OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_UP)
			{
				float imgH = v.getHeight();
				float imgW = v.getWidth();
				float x = event.getX();
				float y = event.getY();
				
				handleTouchesForFrames(x, y, imgW, imgH);
			}
			return true;
		}
		
	}
}
