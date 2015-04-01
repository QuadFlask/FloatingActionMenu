package com.flask.floatingactionmenu;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DimenRes;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FloatingActionMenu extends ViewGroup implements OnToggleListener {
	private FloatingActionToggleButton fabToggle;
	private List<FloatingActionButton> fabList = new ArrayList<>();
	private List<TextView> labelList = new ArrayList<>();
	private AnimatorSet toggleOnAnimator, toggleOffAnimator;
	private int labelsStyle;

	private int maxButtonWidth;

	public FloatingActionMenu(Context context) {
		super(context);
	}

	public FloatingActionMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public FloatingActionMenu(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(final Context context, AttributeSet attrs) {
		if (attrs != null) initAttributes(context, attrs);
	}

	protected void initAttributes(Context context, AttributeSet attributeSet) {
		TypedArray attr = getTypedArray(context, attributeSet, R.styleable.FloatingActionButton);
		if (attr != null) {
			try {
				labelsStyle = attr.getResourceId(R.styleable.FloatingActionButton_fab_labelStyle, 0);
			} finally {
				attr.recycle();
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		measureChildren(widthMeasureSpec, heightMeasureSpec);

		int width = fabToggle.getMeasuredWidth(), height = fabToggle.getMeasuredHeight();
		int labelMargin = getDimension(R.dimen.fab_label_margin);
		int maxLabelWidth = 0;

		for (int i = 0; i < fabList.size(); i++) {
			FloatingActionButton fab = fabList.get(i);

			width = Math.max(fab.getMeasuredWidth(), width);
			height += fab.getMeasuredHeight();
			maxLabelWidth = Math.max(maxLabelWidth, labelList.get(i).getMeasuredWidth());
		}
		maxButtonWidth = width;
		width += maxLabelWidth + labelMargin;

		setMeasuredDimension(width, height);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int buttonsHorizontalCenter = r - l - fabToggle.getMeasuredWidth() / 2;
		int labelMargin = getDimension(R.dimen.fab_label_margin);
		int labelOffset = maxButtonWidth / 2 + labelMargin;
		int labelXNearButton = buttonsHorizontalCenter - labelOffset;

		int nextY = b - t;
		for (int i = fabList.size() - 1; i >= 0; i--) {
			FloatingActionButton fab = fabList.get(i);

			int x = buttonsHorizontalCenter - fab.getMeasuredWidth() / 2;
			int y = nextY - fab.getMeasuredHeight();
			fab.layout(x, y, x + fab.getMeasuredWidth(), y + fab.getMeasuredHeight());

			TextView label = labelList.get(i);
			int labelXAwayFromButton = labelXNearButton - label.getMeasuredWidth();
			int labelLeft = labelXAwayFromButton;
			int labelRight = labelXNearButton;
			int labelTop = y + (fab.getMeasuredHeight() - label.getMeasuredHeight()) / 2;
			label.layout(labelLeft, labelTop, labelRight, labelTop + label.getMeasuredHeight());

			nextY = y;
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		int childCount = getChildCount();
		for (int i = 0; i < childCount - 1; i++) {
			FloatingActionButton child = (FloatingActionButton) getChildAt(i);
			child.setVisibility(INVISIBLE);
			child.setClickable(false);
			addFloatingActionButtonList(child);
		}
		setFloatingActionToggleButton((FloatingActionToggleButton) getChildAt(childCount - 1));
		fabToggle.setVisibility(VISIBLE);
		fabToggle.setClickable(true);

		createLabels();
	}

	private void createLabels() {
		Context context = new ContextThemeWrapper(getContext(), labelsStyle);

		for (FloatingActionButton fab : fabList) {
			String labelText = fab.getLabelText();

			TextView label = new TextView(context);
			label.setTextAppearance(getContext(), labelsStyle);
			label.setText(labelText);
			label.setVisibility(INVISIBLE);
			label.setClickable(false);

			labelList.add(label);
			addView(label);
		}
	}

	public void addFloatingActionButtonList(FloatingActionButton floatingActionButton) {
		fabList.add(floatingActionButton);
	}

	public void setFloatingActionToggleButton(FloatingActionToggleButton floatingActionToggleButton) {
		fabToggle = floatingActionToggleButton;
		fabToggle.setOnToggleListener(this);
		fabList.add(fabToggle);
	}

	@Override
	public void onToggle(boolean isOn) {
		createExpandAnimations();
		createCollapseAnimations();
	}

	private static final Interpolator interpolator = new DecelerateInterpolator();

	private void createExpandAnimations() {
		if (toggleOnAnimator == null) {
			int duration = FloatingActionToggleButton.ANIMATION_DURATION;
			int delay = duration;
			int count = fabList.size();
			int inc = duration / count;

			toggleOnAnimator = fabToggle.getToggleOnAnimator();

			for (FloatingActionButton fab : fabList) {
				if (!fab.equals(fabToggle)) {
					delay -= inc;
					final ObjectAnimator expandAlphaAnimator = createObjectAnimator(fab, View.ALPHA, delay, 0, 1f);
					toggleOnAnimator.play(expandAlphaAnimator);
					toggleOnAnimator.play(createObjectAnimator(fab, View.TRANSLATION_Y, delay, fab.getMeasuredHeight() / 4, 0));
					toggleOnAnimator.play(createObjectAnimator(fab, View.SCALE_X, delay, 0, 1f));
					toggleOnAnimator.play(createObjectAnimator(fab, View.SCALE_Y, delay, 0, 1f));

					expandAlphaAnimator.addListener(new AutoAlphaShowingAnimatorListener(expandAlphaAnimator));
				}
			}

			delay = duration;
			for (TextView label : labelList) {
				delay -= inc;
				final ObjectAnimator expandAlphaAnimator = createObjectAnimator(label, View.ALPHA, delay, 0, 1f);
				toggleOnAnimator.play(expandAlphaAnimator);
				toggleOnAnimator.play(createObjectAnimator(label, View.TRANSLATION_Y, delay, label.getMeasuredHeight() / 4, 0));

				expandAlphaAnimator.addListener(new AutoAlphaShowingAnimatorListener(expandAlphaAnimator));
			}
		}
	}

	private void createCollapseAnimations() {
		if (toggleOffAnimator == null) {
			int duration = FloatingActionToggleButton.ANIMATION_DURATION;
			int delay = 0;
			int count = fabList.size();
			int inc = duration / count;

			toggleOffAnimator = fabToggle.getToggleOffAnimator();

			for (FloatingActionButton fab : fabList) {
				if (!fab.equals(fabToggle)) {
					final ObjectAnimator collapseAlphaAnimator = createObjectAnimator(fab, View.ALPHA, delay, 1f, 0);
					toggleOffAnimator.play(collapseAlphaAnimator);
					toggleOffAnimator.play(createObjectAnimator(fab, View.TRANSLATION_Y, delay, 0, fab.getMeasuredHeight() / 4));
					toggleOffAnimator.play(createObjectAnimator(fab, View.SCALE_X, delay, 1f, 0));
					toggleOffAnimator.play(createObjectAnimator(fab, View.SCALE_Y, delay, 1f, 0));

					collapseAlphaAnimator.addListener(new AutoAlphaHidingAnimatorListener(collapseAlphaAnimator));
					delay += inc;
				}
			}
			delay = 0;
			for (TextView label : labelList) {
				final ObjectAnimator collapseAlphaAnimator = createObjectAnimator(label, View.ALPHA, delay, 1f, 0);
				toggleOffAnimator.play(collapseAlphaAnimator);
				toggleOffAnimator.play(createObjectAnimator(label, View.TRANSLATION_Y, delay, 0, label.getMeasuredHeight() / 4));

				collapseAlphaAnimator.addListener(new AutoAlphaHidingAnimatorListener(collapseAlphaAnimator));
				delay += inc;
			}
		}
	}

	private ObjectAnimator createObjectAnimator(Object target, Property property, long delay, float... values) {
		ObjectAnimator objectAnimator = new ObjectAnimator().ofFloat(target, property, values);
		objectAnimator.setInterpolator(interpolator);
		objectAnimator.setStartDelay(delay);
		return objectAnimator;
	}

	static class AutoAlphaShowingAnimatorListener extends AnimatorListenerAdapter {
		private ObjectAnimator objectAnimator;

		AutoAlphaShowingAnimatorListener(ObjectAnimator objectAnimator) {
			this.objectAnimator = objectAnimator;
		}

		@Override
		public void onAnimationStart(Animator animation) {
			((View) objectAnimator.getTarget()).setVisibility(VISIBLE);
			((View) objectAnimator.getTarget()).setClickable(true);
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			((View) objectAnimator.getTarget()).setVisibility(VISIBLE);
		}
	}

	static class AutoAlphaHidingAnimatorListener extends AnimatorListenerAdapter {
		private ObjectAnimator objectAnimator;

		AutoAlphaHidingAnimatorListener(ObjectAnimator objectAnimator) {
			this.objectAnimator = objectAnimator;
		}

		@Override
		public void onAnimationStart(Animator animation) {
			((View) objectAnimator.getTarget()).setVisibility(VISIBLE);
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			((View) objectAnimator.getTarget()).setVisibility(INVISIBLE);
			((View) objectAnimator.getTarget()).setClickable(false);
		}
	}

	static abstract class AnimatorListenerAdapter implements AnimatorListener {
		@Override
		public void onAnimationStart(Animator animation) {
		}

		@Override
		public void onAnimationEnd(Animator animation) {
		}

		@Override
		public void onAnimationCancel(Animator animation) {
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
		}
	}

	protected int getDimension(@DimenRes int id) {
		return getResources().getDimensionPixelSize(id);
	}

	protected TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
		return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
	}
}