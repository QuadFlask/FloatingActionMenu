package com.flask.floatingactionmenu;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Toast;

public class FloatingActionToggleButton extends FloatingActionButton {
	private static final Interpolator interpolator = new AccelerateDecelerateInterpolator();
	private static final int ANIMATION_DURATION = 300;
	private static final float COLLAPSED_PLUS_ROTATION = 0f;
	private static final float EXPANDED_PLUS_ROTATION = 90 + 45;

	private AnimatorSet toggleOnAnimator;
	private AnimatorSet toggleOffAnimator;

	protected int toggleIcon;
	protected Drawable toggleIconDrawable;
	protected RotatingDrawable rotatingDrawable;
	protected FadingDrawable fadingDrawable;

	protected boolean isOn = false;
	private OnToggleListener onToggleListener;

	public FloatingActionToggleButton(Context context) {
		super(context);
	}

	public FloatingActionToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FloatingActionToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	protected void init(final Context context, AttributeSet attrs) {
		colorNormal = getColor(R.color.material_blue_500);
		colorPressed = getColor(R.color.material_blue_600);
		colorDisabled = getColor(android.R.color.darker_gray);

		if (attrs != null) initAttributes(context, attrs);
		updateBackground();

		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggle();
				Toast.makeText(context, "toggle", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void initAttributes(Context context, AttributeSet attributeSet) {
		TypedArray attr = getTypedArray(context, attributeSet, R.styleable.FloatingActionButton);
		if (attr != null) {
			try {
				colorNormal = attr.getColor(R.styleable.FloatingActionButton_fab_colorNormal, getColor(R.color.material_blue_500));
				colorPressed = attr.getColor(R.styleable.FloatingActionButton_fab_colorPressed, getColor(R.color.material_blue_600));
				colorDisabled = attr.getColor(R.styleable.FloatingActionButton_fab_colorDisabled, colorDisabled);
				normalIcon = attr.getResourceId(R.styleable.FloatingActionButton_fab_normal_icon, 0);
				type = attr.getInt(R.styleable.FloatingActionButton_fab_type, TYPE_NORMAL);
				toggleIcon = attr.getResourceId(R.styleable.FloatingActionButton_fab_toggle_icon, 0);
			} finally {
				attr.recycle();
			}
		}
	}

	private void createAnimations() {
		if (toggleOnAnimator == null) {
			toggleOnAnimator = new AnimatorSet().setDuration(ANIMATION_DURATION);
			toggleOffAnimator = new AnimatorSet().setDuration(ANIMATION_DURATION);

			final ObjectAnimator toggleOnRotation = ObjectAnimator.ofFloat(rotatingDrawable, "rotation", COLLAPSED_PLUS_ROTATION, EXPANDED_PLUS_ROTATION);
			final ObjectAnimator toggleOffRotation = ObjectAnimator.ofFloat(rotatingDrawable, "rotation", EXPANDED_PLUS_ROTATION, COLLAPSED_PLUS_ROTATION);
			final ObjectAnimator toggleOnFading = ObjectAnimator.ofFloat(fadingDrawable, "fading", 0, 1f);
			final ObjectAnimator toggleOffFading = ObjectAnimator.ofFloat(fadingDrawable, "fading", 1f, 0);

			toggleOnRotation.setInterpolator(interpolator);
			toggleOffRotation.setInterpolator(interpolator);
			toggleOnFading.setInterpolator(interpolator);
			toggleOffFading.setInterpolator(interpolator);

			toggleOnRotation.setRepeatMode(ValueAnimator.RESTART);
			toggleOffRotation.setRepeatMode(ValueAnimator.RESTART);
			toggleOnFading.setRepeatMode(ValueAnimator.RESTART);
			toggleOffFading.setRepeatMode(ValueAnimator.RESTART);

			toggleOnAnimator.play(toggleOnRotation).with(toggleOnFading);
			toggleOffAnimator.play(toggleOffRotation).with(toggleOffFading);
		}
	}

	@Override
	protected void updateBackground() {
		LayerDrawable layerDrawable = new LayerDrawable(
				new Drawable[]{
						createFillDrawable(),
						getIconDrawable()
				});

		setBackground(layerDrawable);
	}

	protected Drawable getIconDrawable() {
		if (toggleIconDrawable == null) {
			RotatingDrawable rotatingDrawable = new RotatingDrawable(getCopyOfDrawableFromResources(toggleIcon));
			rotatingDrawable.setRotation(-EXPANDED_PLUS_ROTATION);
			toggleIconDrawable = rotatingDrawable;
		}

		fadingDrawable = new FadingDrawable(new Drawable[]{
				super.getIconDrawable(),
				toggleIconDrawable
		});
		rotatingDrawable = new RotatingDrawable(fadingDrawable);

		int circleSize = getDimension(type == TYPE_NORMAL ? R.dimen.fab_size_normal : R.dimen.fab_size_mini);
		int inset = (circleSize - getDimension(type == TYPE_NORMAL ? R.dimen.fab_icon_size : R.dimen.fab_icon_size_mini)) / 2;

		rotatingDrawable.setLayerInset(0,
				shadowSize + inset, shadowSize + inset,
				shadowSize + inset, shadowSize + inset);

		return rotatingDrawable;
	}

	public void toggle() {
		isOn = !isOn;
		createAnimations();

		if (isOn) toggleOn();
		else toggleOff();
		onToggleListener.onToggle(isOn);
	}

	public void toggleOn() {
		if (isOn) {
			toggleOffAnimator.cancel();
			toggleOnAnimator.start();
		}
	}

	public void toggleOff() {
		if (!isOn) {
			toggleOnAnimator.cancel();
			toggleOffAnimator.start();
		}
	}

	public boolean isToggleOn() {
		return isOn;
	}

	public void setOnToggleListener(OnToggleListener onToggleListener) {
		this.onToggleListener = onToggleListener;
	}

	static class FadingDrawable extends LayerDrawable {
		private float fading = 0;

		public FadingDrawable(Drawable[] layers) {
			super(layers);
			assert (layers.length == 2);
			setFading(0);
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
			return Math.max(0, Math.min(Math.round(fading * 255f), 0xff));
		}
	}

	static class RotatingDrawable extends LayerDrawable {
		public RotatingDrawable(Drawable drawable) {
			super(new Drawable[]{drawable});
		}

		private float rotation;

		@SuppressWarnings("UnusedDeclaration")
		public float getRotation() {
			return rotation;
		}

		@SuppressWarnings("UnusedDeclaration")
		public void setRotation(float rotation) {
			this.rotation = rotation;
			invalidateSelf();
		}

		@Override
		public void draw(Canvas canvas) {
			canvas.save();
			canvas.rotate(rotation, getBounds().centerX(), getBounds().centerY());
			super.draw(canvas);
			canvas.restore();
		}
	}
}