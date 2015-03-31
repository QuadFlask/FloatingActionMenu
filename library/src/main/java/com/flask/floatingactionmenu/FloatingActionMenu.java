package com.flask.floatingactionmenu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class FloatingActionMenu extends LinearLayout implements OnToggleListener {
	private FloatingActionToggleButton fab;
	private List<FloatingActionButton> fabList;
	private AnimatorSet toggleOnAnimator, toggleOffAnimator;

	public FloatingActionMenu(Context context) {
		super(context);
		init();
	}

	public FloatingActionMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FloatingActionMenu(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		setOrientation(VERTICAL);
//		setGravity(Gravity.CENTER_HORIZONTAL);
		fabList = new ArrayList<>();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		int childCount = getChildCount();
		for (int i = 0; i < childCount - 1; i++) {
			FloatingActionButton child = (FloatingActionButton) getChildAt(i);
			child.setVisibility(GONE);
			addFloatingActionButton(child);
		}
		setFloatingActionToggleButton((FloatingActionToggleButton) getChildAt(childCount - 1));
	}

	public void addFloatingActionButton(FloatingActionButton floatingActionButton) {
		fabList.add(floatingActionButton);
	}

	public void setFloatingActionToggleButton(FloatingActionToggleButton floatingActionToggleButton) {
		fab = floatingActionToggleButton;
		fab.setOnToggleListener(this);
	}

	@Override
	public void onToggle(boolean isOn) {
		createExpandAnimations();
		createCollapseAnimations();
	}

	private void createExpandAnimations() {
		if (toggleOnAnimator == null) {
			int duration = FloatingActionToggleButton.ANIMATION_DURATION;
			int delay = duration;
			int count = fabList.size();
			int inc = duration / count;

			toggleOnAnimator = fab.getToggleOnAnimator();

			Interpolator interpolator = new DecelerateInterpolator();
			for (FloatingActionButton fab : fabList) {
				delay -= inc;
				final ObjectAnimator collapseAlphaAnimator = new ObjectAnimator();
				collapseAlphaAnimator.setInterpolator(interpolator);
				collapseAlphaAnimator.setProperty(View.ALPHA);
				collapseAlphaAnimator.setFloatValues(0, 1f);
				collapseAlphaAnimator.setTarget(fab);
				collapseAlphaAnimator.setStartDelay(delay);
				toggleOnAnimator.play(collapseAlphaAnimator);

				ObjectAnimator collapseYTransAnimator = new ObjectAnimator();
				collapseYTransAnimator.setInterpolator(interpolator);
				collapseYTransAnimator.setProperty(View.TRANSLATION_Y);
				collapseYTransAnimator.setFloatValues(20, 0);
				collapseYTransAnimator.setTarget(fab);
				collapseYTransAnimator.setStartDelay(delay);
				toggleOnAnimator.play(collapseYTransAnimator);

				ObjectAnimator collapseXScaleAnimator = new ObjectAnimator();
				collapseXScaleAnimator.setInterpolator(interpolator);
				collapseXScaleAnimator.setProperty(View.SCALE_X);
				collapseXScaleAnimator.setFloatValues(0, 1f);
				collapseXScaleAnimator.setTarget(fab);
				collapseXScaleAnimator.setStartDelay(delay);
				toggleOnAnimator.play(collapseXScaleAnimator);

				ObjectAnimator collapseYScaleAnimator = new ObjectAnimator();
				collapseYScaleAnimator.setInterpolator(interpolator);
				collapseYScaleAnimator.setProperty(View.SCALE_Y);
				collapseYScaleAnimator.setFloatValues(0, 1f);
				collapseYScaleAnimator.setTarget(fab);
				collapseYScaleAnimator.setStartDelay(delay);
				toggleOnAnimator.play(collapseYScaleAnimator);

				collapseAlphaAnimator.addListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animation) {
						((View) collapseAlphaAnimator.getTarget()).setVisibility(VISIBLE);
					}

					@Override
					public void onAnimationEnd(Animator animation) {
//						((View) collapseAlphaAnimator.getTarget()).setVisibility(GONE);
					}

					@Override
					public void onAnimationCancel(Animator animation) {
					}

					@Override
					public void onAnimationRepeat(Animator animation) {
					}
				});
			}
		}
	}

	private void createCollapseAnimations() {
		if (toggleOffAnimator == null) {
			int duration = FloatingActionToggleButton.ANIMATION_DURATION;
			int delay = 0;
			int count = fabList.size();
			int inc = duration / count;

			toggleOffAnimator = fab.getToggleOffAnimator();

			Interpolator interpolator = new DecelerateInterpolator();
			for (FloatingActionButton fab : fabList) {
				final ObjectAnimator collapseAlphaAnimator = new ObjectAnimator();
				collapseAlphaAnimator.setInterpolator(interpolator);
				collapseAlphaAnimator.setProperty(View.ALPHA);
				collapseAlphaAnimator.setFloatValues(1f, 0);
				collapseAlphaAnimator.setTarget(fab);
				collapseAlphaAnimator.setStartDelay(delay);
				toggleOffAnimator.play(collapseAlphaAnimator);

				ObjectAnimator collapseYTransAnimator = new ObjectAnimator();
				collapseYTransAnimator.setInterpolator(interpolator);
				collapseYTransAnimator.setProperty(View.TRANSLATION_Y);
				collapseYTransAnimator.setFloatValues(0, 20);
				collapseYTransAnimator.setTarget(fab);
				collapseYTransAnimator.setStartDelay(delay);
				toggleOffAnimator.play(collapseYTransAnimator);

				ObjectAnimator collapseXScaleAnimator = new ObjectAnimator();
				collapseXScaleAnimator.setInterpolator(interpolator);
				collapseXScaleAnimator.setProperty(View.SCALE_X);
				collapseXScaleAnimator.setFloatValues(1f, 0);
				collapseXScaleAnimator.setTarget(fab);
				collapseXScaleAnimator.setStartDelay(delay);
				toggleOffAnimator.play(collapseXScaleAnimator);

				ObjectAnimator collapseYScaleAnimator = new ObjectAnimator();
				collapseYScaleAnimator.setInterpolator(interpolator);
				collapseYScaleAnimator.setProperty(View.SCALE_Y);
				collapseYScaleAnimator.setFloatValues(1f, 0);
				collapseYScaleAnimator.setTarget(fab);
				collapseYScaleAnimator.setStartDelay(delay);
				toggleOffAnimator.play(collapseYScaleAnimator);

				collapseAlphaAnimator.addListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animation) {
						((View) collapseAlphaAnimator.getTarget()).setVisibility(VISIBLE);
					}

					@Override
					public void onAnimationEnd(Animator animation) {
						((View) collapseAlphaAnimator.getTarget()).setVisibility(VISIBLE);
					}

					@Override
					public void onAnimationCancel(Animator animation) {
					}

					@Override
					public void onAnimationRepeat(Animator animation) {
					}
				});
				delay += inc;
			}
		}
	}
}