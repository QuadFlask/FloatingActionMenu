package com.flask.floatingactionmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.animation.OvershootInterpolator;

public class FloatingActionToggleButton extends FloatingActionButton {

	public FloatingActionToggleButton(Context context) {
		super(context);
	}

	public FloatingActionToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FloatingActionToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	protected void init(Context context, AttributeSet attrs) {
		shadowSize = getDimension(R.dimen.fab_shadow_size);
		colorNormal = getColor(R.color.material_blue_500);
		colorPressed = getColor(R.color.material_blue_600);
		colorDisabled = getColor(android.R.color.darker_gray);

		if (attrs != null) initAttributes(context, attrs);
		updateBackground();
	}

	protected void initAttributes(Context context, AttributeSet attributeSet) {
		TypedArray attr = getTypedArray(context, attributeSet, R.styleable.FloatingActionButton);
		if (attr != null) {
			try {
				colorNormal = attr.getColor(R.styleable.FloatingActionButton_fab_colorNormal, colorNormal);
				colorPressed = attr.getColor(R.styleable.FloatingActionButton_fab_colorPressed, getColor(R.color.material_blue_600));
				colorDisabled = attr.getColor(R.styleable.FloatingActionButton_fab_colorDisabled, colorDisabled);
				type = attr.getInt(R.styleable.FloatingActionButton_fab_type, TYPE_NORMAL);
			} finally {
				attr.recycle();
			}
		}
	}

	private static class FadingDrawable extends LayerDrawable {

		/**
		 * Create a new layer drawable with the list of specified layers.
		 *
		 * @param layers A list of drawables to use as layers in this new drawable.
		 */
		public FadingDrawable(Drawable[] layers) {
			super(layers);
		}
	}

	private static class RotatingDrawable extends LayerDrawable {
		public RotatingDrawable(Drawable drawable) {
			super(new Drawable[]{drawable});
		}

		private float mRotation;

		@SuppressWarnings("UnusedDeclaration")
		public float getRotation() {
			return mRotation;
		}

		@SuppressWarnings("UnusedDeclaration")
		public void setRotation(float rotation) {
			mRotation = rotation;
			invalidateSelf();
		}

		@Override
		public void draw(Canvas canvas) {
			canvas.save();
			canvas.rotate(mRotation, getBounds().centerX(), getBounds().centerY());
			super.draw(canvas);
			canvas.restore();
		}
	}
}