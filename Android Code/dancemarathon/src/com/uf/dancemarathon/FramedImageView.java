package com.uf.dancemarathon;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class FramedImageView extends ImageView{
	
	private ArrayList<ImageFrame> frames;
	
	public FramedImageView(Context context) {
		super(context);
		setup();

	}

	public FramedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setup();

	}

	public FramedImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setup();
		
	}
	
	private void setup()
	{
		setFrames(new ArrayList<ImageFrame>());
	}
	
	

	/* (non-Javadoc)
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		boolean handled = super.onTouchEvent(event);
		
		if(event.getAction() == MotionEvent.ACTION_UP)
		{
			float x = event.getX();
			float y = event.getY();
			Log.d("touch", String.valueOf(x)+ String.valueOf(y));
			handleTouchesForFrames(x, y);
		}
		
		
		return handled;
	}
	

	private void handleTouchesForFrames(float x, float y)
	{
		for(int i = 0; i < frames.size(); i++)
		{
			ImageFrame f = frames.get(i);
			
			if(f instanceof OnFrameTouchListener)
			{
				Bitmap bp = ((BitmapDrawable)this.getDrawable()).getBitmap();

				if(f.isPointInFrame(x, y, bp.getWidth(), bp.getHeight()))
					((OnFrameTouchListener) f).onFrameTouch(this);
			}
		}
	}

	public ArrayList<ImageFrame> getFrames() {
		return frames;
	}

	public void setFrames(ArrayList<ImageFrame> frames) {
		this.frames = frames;
	}
	
	
	

}
