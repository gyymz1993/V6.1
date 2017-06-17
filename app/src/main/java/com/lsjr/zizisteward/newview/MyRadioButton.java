package com.lsjr.zizisteward.newview;

import com.lsjr.zizisteward.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class MyRadioButton extends RadioButton {

	private int mMDrawableSize;

	public MyRadioButton(Context context) {
		this(context, null);
	}

	public MyRadioButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		Drawable drawableLeft = null, drawableTop = null, drawableRight = null, drawableBottom = null;
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyRadioButton);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.MyRadioButton_drawableSize:
				mMDrawableSize = a.getDimensionPixelSize(R.styleable.MyRadioButton_drawableSize, 100);

				break;
			case R.styleable.MyRadioButton_drawableLeft:
				drawableLeft = a.getDrawable(attr);
				break;
			case R.styleable.MyRadioButton_drawableRight:
				drawableRight = a.getDrawable(attr);
				break;
			case R.styleable.MyRadioButton_drawableBottom:
				drawableBottom = a.getDrawable(attr);
				break;
			case R.styleable.MyRadioButton_drawableTop:
				drawableTop = a.getDrawable(attr);
				break;

			}
		}
		a.recycle();
		setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
	}

	public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {

		if (left != null) {
			left.setBounds(0, 0, mMDrawableSize, mMDrawableSize);
		}
		if (right != null) {
			right.setBounds(0, 0, mMDrawableSize, mMDrawableSize);
		}
		if (top != null) {
			top.setBounds(0, 0, mMDrawableSize, mMDrawableSize);
		}
		if (bottom != null) {
			bottom.setBounds(0, 0, mMDrawableSize, mMDrawableSize);
		}
		setCompoundDrawables(left, top, right, bottom);
	}
}
