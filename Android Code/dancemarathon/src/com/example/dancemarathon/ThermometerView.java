package com.example.dancemarathon;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ThermometerView extends View
{

	private float maxValue;
	private Paint bulbPaint;
	private RectF bulbRect;
	private Paint bulbOutlinePaint;
	private int fillColor;
	private int outlineColor;
	private float posX = 0.0f;
	private float posY = 0.0f;
	private float screenWidth;
	private float screenHeight;
	
	public ThermometerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		//Get an array of the attributes
		TypedArray a = context.getTheme().obtainStyledAttributes(
		        attrs,
		        R.styleable.ThermometerView,
		        0, 0);
		
		try
		{
			//Try to get the max value attribute. Put 0 if not defined
			maxValue = a.getFloat(R.styleable.ThermometerView_maxValue, 0);
			fillColor = a.getColor(R.styleable.ThermometerView_fillColor, Color.YELLOW);
			outlineColor = a.getColor(R.styleable.ThermometerView_outlineColor, Color.GRAY);
		}
		finally
		{
			a.recycle();
		}
		
		screenWidth = context.getResources().getDisplayMetrics().widthPixels;
		screenHeight = context.getResources().getDisplayMetrics().heightPixels;
		
		initPaintObjects();

	}
	
	
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		canvas.drawArc(bulbRect, 0, 180, false, bulbOutlinePaint);
	}
	
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int width = 0;
		int height = 0;
		int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = View.MeasureSpec.getMode(widthMeasureSpec);
		
		//Set width
		if(widthMode != View.MeasureSpec.UNSPECIFIED)
			width = View.MeasureSpec.getSize(widthMeasureSpec);
		else
			width = (int) (screenWidth/12);
		
		//Set height
		if(heightMode != View.MeasureSpec.UNSPECIFIED)
			height = View.MeasureSpec.getSize(heightMeasureSpec);
		else
			height = (int) (screenHeight/12);
			
		setMeasuredDimension(width, height);
	}
	
	private void initPaintObjects()
	{
		//Initialize bulb
		bulbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bulbPaint.setColor(fillColor);
		bulbPaint.setStyle(Paint.Style.FILL);
		
		//Initialize bulb outline
		bulbOutlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bulbOutlinePaint.setColor(outlineColor);
		bulbOutlinePaint.setStyle(Paint.Style.FILL);
		
		bulbRect = new RectF(0,0,50,50);
	}
	/**
	 * @return the maxValue
	 */
	public float getMaxValue()
	{
		return maxValue;
	}
	/**
	 * @param maxValue the maxValue to set
	 */
	public void setMaxValue(float maxValue)
	{
		this.maxValue = maxValue;
		//Must update the view
		invalidate();
		requestLayout();
	}

	/**
	 * @return the fillColor
	 */
	public int getFillColor()
	{
		return fillColor;
	}

	/**
	 * @param fillColor the fillColor to set
	 */
	public void setFillColor(int fillColor)
	{
		this.fillColor = fillColor;
		//Must update the view
		invalidate();
		requestLayout();
}

}
