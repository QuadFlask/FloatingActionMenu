package com.flask.floatingactionmenu;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;

public class FloatingActionToggleButton extends FloatingActionButton {
	private static final int ANIMATION_DURATION = 300;
	private static final float COLLAPSED_PLUS_ROTATION = 0f;
	private static final float EXPANDED_PLUS_ROTATION = 90f + 45f;

	private AnimatorSet toggleOnAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);
	private AnimatorSet toggleOffAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);

	protected Drawable toggleDrawable;

	protected RotatingDrawable rotatingDrawable;
	protected FadingDrawable fadingDrawable;
	protected boolean isOn = false;

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
		toggleDrawable = getResources().getDrawable(R.drawable.ic_mode_edit_white_24dp);

		if (attrs != null) initAttributes(context, attrs);
		updateBackground();

		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggle();
			}
		});
	}

	protected void initAttributes(Context context, AttributeSet attributeSet) {
		TypedArray attr = getTypedArray(context, attributeSet, R.styleable.FloatingActionButton);
		if (attr != null) {
			try {
				colorNormal = attr.getColor(R.styleable.FloatingActionButton_fab_colorNormal, colorNormal);
				colorPressed = attr.getColor(R.styleable.FloatingActionButton_fab_colorPressed, getColor(R.color.material_blue_600));
				colorDisabled = attr.getColor(R.styleable.FloatingActionButton_fab_colorDisabled, colorDisabled);
				toggleDrawable = attr.getDrawable(R.styleable.FloatingActionButton_fab_toggle_icon);
				if (toggleDrawable == null)
					toggleDrawable = getResources().getDrawable(R.drawable.ic_mode_edit_white_24dp);
				type = attr.getInt(R.styleable.FloatingActionButton_fab_type, TYPE_NORMAL);
			} finally {
				attr.recycle();
			}
		}
	}

	public void toggle() {
		isOn = !isOn;
		if (isOn) {
			toggleOnAnimation.start();
		} else {
			toggleOffAnimation.start();
		}
	}

	@Override
	protected void updateBackground() {
		LayerDrawable layerDrawable = new LayerDrawable(
				new Drawable[]{
						getResources().getDrawable(type == TYPE_NORMAL ? R.drawable.shadow : R.drawable.shadow_mini),
						createFillDrawable(),
						getIconDrawable()
				});

		setBackground(layerDrawable);
	}

	protected Drawable getIconDrawable() {
		fadingDrawable = new FadingDrawable(new Drawable[]{
				getDrawable(),
				toggleDrawable
		});
		rotatingDrawable = new RotatingDrawable(fadingDrawable);

		final OvershootInterpolator interpolator = new OvershootInterpolator();

		final ObjectAnimator toggleOnRotation = ObjectAnimator.ofFloat(rotatingDrawable, "rotation", EXPANDED_PLUS_ROTATION, COLLAPSED_PLUS_ROTATION);
		final ObjectAnimator toggleOffRotation = ObjectAnimator.ofFloat(rotatingDrawable, "rotation", COLLAPSED_PLUS_ROTATION, EXPANDED_PLUS_ROTATION);
		final ObjectAnimator toggleOnFading = ObjectAnimator.ofFloat(fadingDrawable, "fading", 0, 1f);
		final ObjectAnimator toggleOffFading = ObjectAnimator.ofFloat(fadingDrawable, "fading", 1f, 0);

		toggleOnRotation.setInterpolator(interpolator);
		toggleOffRotation.setInterpolator(interpolator);
		toggleOnFading.setInterpolator(interpolator);
		toggleOffFading.setInterpolator(interpolator);

		toggleOnAnimation.play(toggleOnRotation);
		toggleOnAnimation.play(toggleOnFading);
		toggleOffAnimation.play(toggleOffRotation);
		toggleOffAnimation.play(toggleOffFading);

		return rotatingDrawable;
	}

	private static class FadingDrawable extends LayerDrawable {
		private float fading;

		public FadingDrawable(Drawable[] layers) {
			super(layers);
			assert (layers.length == 2);
			fading = 0;
		}

		public float getFading() {
			return fading;
		}

		public void setFading(float fading) {
			this.fading = fading;
			getDrawable(0).setAlpha(0xff - alphaAsInt(this.fading));
			getDrawable(1).setAlpha(alphaAsInt(this.fading));
			invalidateSelf();
		}

		private int alphaAsInt(float fading) {
			return Math.round(fading * 255f);
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