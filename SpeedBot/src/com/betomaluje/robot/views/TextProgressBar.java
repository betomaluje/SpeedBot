package com.betomaluje.robot.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class TextProgressBar extends ProgressBar {

	private String text;
	private Paint textPaint;
	private float textSize = 15;
	private int textColor = Color.BLACK;

	public TextProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		// text = "";
	}

	public TextProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		text = "";
	}

	public TextProgressBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		// text = "";		
	}

	@Override
	public synchronized int getMax() {
		// TODO Auto-generated method stub
		return super.getMax();
	}

	@Override
	public synchronized int getProgress() {
		// TODO Auto-generated method stub
		return super.getProgress();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		textPaint = new Paint();
		textPaint.setColor(textColor);
		textPaint.setTextSize(textSize);
		
		Rect bounds = new Rect();
		textPaint.getTextBounds(text, 0, text.length(), bounds);		
		int x = getWidth() / 2 - bounds.centerX();
		int y = getHeight() / 2 - bounds.centerY();
		canvas.drawText(text, x, y, textPaint);
	}

	public synchronized void setText(String text) {
		this.text = text;
		drawableStateChanged();
		postInvalidate();
	}

	@Override
	public synchronized void setMax(int max) {
		// TODO Auto-generated method stub
		super.setMax(max);
	}

	@Override
	public synchronized void setProgress(int progress) {
		// TODO Auto-generated method stub
		super.setProgress(progress);
		postInvalidate();
	}

	public int getTextColor() {
	    return textColor;
	}

	public synchronized void setTextColor(int textColor) {
	    this.textColor = textColor;
	    postInvalidate();
	}
	
	public float getTextSize() {
		return textSize;
	}

	public synchronized void setTextSize(float textSize) {
		this.textSize = textSize;
		postInvalidate();
	}
}
