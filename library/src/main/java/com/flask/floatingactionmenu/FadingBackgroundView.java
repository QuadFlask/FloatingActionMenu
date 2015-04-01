package com.flask.floatingactionmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class FadingBackgroundView extends View {
	private int color = 0x80ffffff;

	public FadingBackgroundView(Context context) {
		super(context);
	}

	public FadingBackgroundView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FadingBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
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
	public float getAlpha() {
		return super.getAlpha();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return getAlpha() != 0 & super.onTouchEvent(event);
	}
}