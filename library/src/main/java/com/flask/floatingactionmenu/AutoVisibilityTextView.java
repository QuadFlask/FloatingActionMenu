package com.flask.floatingactionmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class AutoVisibilityTextView extends TextView {
	public AutoVisibilityTextView(Context context) {
		super(context);
	}

	public AutoVisibilityTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoVisibilityTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
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
}