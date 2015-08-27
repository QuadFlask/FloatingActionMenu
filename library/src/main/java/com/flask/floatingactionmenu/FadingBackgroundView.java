package com.flask.floatingactionmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class FadingBackgroundView extends View {
	protected int color = 0xd0ffffff;

	public FadingBackgroundView(Context context) {
		super(context);
	}

	public FadingBackgroundView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public FadingBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	protected void init(Context context, AttributeSet attrs) {
		TypedArray attr = getTypedArray(context, attrs, R.styleable.FloatingActionButton);
		if (attr != null) {
			try {
				color = attr.getColor(R.styleable.FloatingActionButton_fab_fadingColor, 0x80ffffff);
			} finally {
				attr.recycle();
			}
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		setBackgroundColor(color);
		setAlpha(0);
	}

	@Override
	public void setAlpha(float alpha) {
		super.setAlpha(alpha);
		if (alpha == 0) {
			setVisibility(GONE);
			setClickable(false);
		} else {
			setVisibility(VISIBLE);
			setClickable(true);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return getAlpha() != 0 & super.onTouchEvent(event);
	}

	public void setFab(FloatingActionButton fab) {
	}

	protected TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
		return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
	}
}