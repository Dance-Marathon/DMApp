package com.uf.dancemarathon;

import android.view.View;

public class TouchFrame extends ImageFrame{

	private OnFrameTouchListener listener;
	
	public TouchFrame(double min_X, double min_Y, double max_X, double max_Y) {
		super(min_X, min_Y, max_X, max_Y);
		// TODO Auto-generated constructor stub
		
		//Default listener does nothing
		listener = new OnFrameTouchListener(){

			@Override
			public void onFrameTouch(View v) {
				// TODO Auto-generated method stub
				return;
			}
			
		};
	}
	
	protected void onTouch(View v)
	{
		listener.onFrameTouch(v);
	}
	
	protected void setOnFrameTouchListener(OnFrameTouchListener l)
	{
		listener = l;
	}

}
