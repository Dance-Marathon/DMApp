package com.uf.dancemarathon;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

public class MapActivity extends Activity {

	private FramedImageView mMap;
	private ArrayList<ImageFrame> frames;
	private Matrix matrix = new Matrix();
	private float scale = 1f;
	private ScaleGestureDetector SGD;
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		mMap = (FramedImageView) findViewById(R.id.map_image);
		
		initFrames();
		mMap.setFrames(frames);
		
		//Used to handle pinch zoom
		SGD = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
			   public boolean onScale(ScaleGestureDetector detector) {
			      scale *= detector.getScaleFactor();
			      scale = Math.max(0.1f, Math.min(scale, 5.0f));
			      matrix.setScale(scale, scale);
			      mMap.setImageMatrix(matrix);
			      return true;
			   }
			});
		
	}
		
	private void initFrames()
	{
		TouchFrame f1 = new TouchFrame(0.2, 0.2, 0.7, 0.7);
		f1.setOnFrameTouchListener(new OnFrameTouchListener(){

			@Override
			public void onFrameTouch(View v) {
				// TODO Auto-generated method stub
				makeFrameToast();
			}
			
		});
		
		
		frames = new ArrayList<ImageFrame>();
		
		frames.add(f1);
	}
	
	private void makeFrameToast()
	{
		Toast t = Toast.makeText(this, "IT WORKED!!", Toast.LENGTH_LONG);
		t.show();
	}

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		SGD.onTouchEvent(event);
		return true;
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
}
