package com.flask.floatingactionmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class FadingBackgroundView extends View {
	private int fading = 0x00;
	private int color = 0xffffff;

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
		updateBackgroundColor();
	}

	public void setFading(int fading) {
		this.fading = Math.max(0, Math.min(fading, 0xff));
		updateBackgroundColor();
		invalidate();
	}

	public int getFading() {
		return fading;
	}

	private void updateBackgroundColor() {
		if (fading == 0) {
			setVisibility(GONE);
			setClickable(false);
		} else {
			setVisibility(VISIBLE);
			setClickable(true);
		}
		setBackgroundColor(fading << 24 | color);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return fading != 0;
	}
}