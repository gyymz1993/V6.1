package com.mybetterandroid.wheel.other;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ProgressBar;

public class MyProgress extends ProgressBar {
	String text;
	Paint mPaint;

	public MyProgress(Context context) {
		super(context);
		initText();
	}

	public MyProgress(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initText();
	}

	public MyProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		initText();
	}

	@Override
	public synchronized void setProgress(int progress) {
		setText(progress);
		super.setProgress(progress);

	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Rect rect = new Rect();
		this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
		int x = (getWidth() / 2) - rect.centerX();
		int y = (getHeight() / 2) - rect.centerY();
		canvas.drawText(this.text, x, y, this.mPaint);
	}

	private void initText() {
		this.mPaint = new Paint();
		this.mPaint.setColor(Color.WHITE);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int displayWidth = dm.widthPixels;
		if (displayWidth > 480) {
			this.mPaint.setTextSize(36);
		} else {
			this.mPaint.setTextSize(18);
		}

	}

	@SuppressWarnings("unused")
	private void setText() {
		setText(this.getProgress());
	}

	private void setText(int progress) {
		int i = (progress * 100) / this.getMax();
		this.text = String.valueOf(i) + "%";
	}

	public static int getScreenWidth(Activity context) {
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		return metric.widthPixels;
	}

	public static int getScreenHeight(Activity context) {
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		return metric.heightPixels;
	}

}