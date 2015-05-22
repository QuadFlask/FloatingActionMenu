package com.flask.floatingactionmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class RevealBackgroundView extends FadingBackgroundView {
	private Paint paint;
	private float maxRadius = 0;
	private int x, y;

	public RevealBackgroundView(Context context) {
		super(context);
	}

	public RevealBackgroundView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RevealBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void init(Context context, AttributeSet attrs) {
		super.init(context, attrs);
		paint = new Paint();
		paint.setColor(color);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		maxRadius = (float) Math.sqrt(getWidth() * getWidth() + getHeight() * getHeight());
	}

	@Override
	public void setFab(FloatingActionButton fab) {
		super.setFab(fab);
		x = (int) fab.getX();
		y = (int) fab.getY();
	}

	@Override
	public void draw(Canvas canvas) {
		maxRadius = (float) Math.sqrt(canvas.getWidth() * canvas.getWidth() + canvas.getHeight() * canvas.getHeight());
		float radius = maxRadius * getAlpha();
		canvas.drawCircle(x, y, radius, paint);
	}

	@Override
	public void setAlpha(float alpha) {
		super.setAlpha(alpha);
		invalidate();
	}
}