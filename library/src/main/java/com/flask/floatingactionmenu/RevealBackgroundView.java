package com.flask.floatingactionmenu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

public class RevealBackgroundView extends FadingBackgroundView {
	private Paint paint;
	private float maxRadius = 0;
	private int[] pos = new int[2];
	private int[] pos2 = new int[2];
	private FloatingActionButton fab;

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
		this.fab = fab;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (fab != null) {
			fab.getLocationOnScreen(pos);
			getLocationOnScreen(pos2);
			pos[0] -= pos2[0] - fab.getWidth() / 2;
			pos[1] -= pos2[1] - fab.getHeight() / 2;
			float dx = right - left;
			float dy = bottom - top;
			maxRadius = (float) Math.sqrt(dx * dx + dy * dy);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawCircle(pos[0], pos[1], maxRadius * alpha, paint);
	}

	private float alpha = 0;

	@Override
	public void setAlpha(float alpha) {
		this.alpha = alpha;
		if (alpha == 0) {
			setVisibility(GONE);
			setClickable(false);
		} else {
			setVisibility(VISIBLE);
			setClickable(true);
		}
		invalidate();
	}
}