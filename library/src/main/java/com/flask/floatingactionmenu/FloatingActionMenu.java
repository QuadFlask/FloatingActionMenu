package com.flask.floatingactionmenu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DimenRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
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
		int margin = getDimension(R.dimen.fab_margin);
		int labelMargin = getDimension(R.dimen.fab_label_margin);
		int maxLabelWidth = 0;

		for (int i = 0; i < fabList.size(); i++) {
			FloatingActionButton fab = fabList.get(i);

			width = Math.max(fab.getMeasuredWidth(), width);
			height += fab.getMeasuredHeight() + margin * 2;
			maxLabelWidth = Math.max(maxLabelWidth, labelList.get(i).getMeasuredWidth());
		}
		maxButtonWidth = width;
		width += maxLabelWidth + labelMargin;

		setMeasuredDimension(width, height);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int fabToggleY = b - t - fabToggle.getMeasuredHeight();
		int buttonsHorizontalCenter = r - l - fabToggle.getMeasuredWidth() / 2;
		int margin = getDimension(R.dimen.fab_margin);
		int labelMargin = getDimension(R.dimen.fab_label_margin);
		int labelOffset = maxButtonWidth / 2 + labelMargin;
		int labelXNearButton = buttonsHorizontalCenter - labelOffset;

		int fabToggleLeft = buttonsHorizontalCenter - fabToggle.getMeasuredWidth() / 2;
		fabToggle.layout(fabToggleLeft, fabToggleY, fabToggleLeft + fabToggle.getMeasuredWidth(), fabToggleY + fabToggle.getMeasuredHeight());

		int nextY = fabToggleY - margin;
		for (int i = fabList.size() - 1; i >= 0; i--) {
			FloatingActionButton fab = fabList.get(i);

			int x = buttonsHorizontalCenter - fab.getMeasuredWidth() / 2;
			int y = nextY - fab.getMeasuredHeight();
			fab.layout(x, y, x + fab.getMeasuredWidth(), y + fab.getMeasuredHeight());
			fab.setTranslationY(getMeasuredHeight());

			TextView label = labelList.get(i);
			int labelXAwayFromButton = labelXNearButton - label.getMeasuredWidth();
			int labelLeft = labelXAwayFromButton;
			int labelRight = labelXNearButton;
			int labelTop = y - (fab.getMeasuredHeight() - label.getMeasuredHeight()) / 2;
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

		createLabels();
	}

	private void createLabels() {
		Context context = new ContextThemeWrapper(getContext(), labelsStyle);

		for (FloatingActionButton fab : fabList) {
			String labelText = fab.getLabelText();

			TextView label = new TextView(context);
			label.setTextAppearance(getContext(), labelsStyle);
			label.setText(labelText);

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

			toggleOnAnimator = fabToggle.getToggleOnAnimator();

			Interpolator interpolator = new DecelerateInterpolator();
			for (FloatingActionButton fab : fabList) {
				delay -= inc;
				final ObjectAnimator collapseAlphaAnimator = new ObjectAnimator().ofFloat(fab, View.ALPHA, 0, 1f);
				collapseAlphaAnimator.setInterpolator(interpolator);
				collapseAlphaAnimator.setStartDelay(delay);
				toggleOnAnimator.play(collapseAlphaAnimator);

				ObjectAnimator collapseYTransAnimator = new ObjectAnimator().ofFloat(fab, View.TRANSLATION_Y, 32, 0);
				collapseYTransAnimator.setInterpolator(interpolator);
				collapseYTransAnimator.setStartDelay(delay);
				toggleOnAnimator.play(collapseYTransAnimator);

				ObjectAnimator collapseXScaleAnimator = new ObjectAnimator().ofFloat(fab, View.SCALE_X, 0, 1f);
				collapseXScaleAnimator.setInterpolator(interpolator);
				collapseXScaleAnimator.setStartDelay(delay);
				toggleOnAnimator.play(collapseXScaleAnimator);

				ObjectAnimator collapseYScaleAnimator = new ObjectAnimator().ofFloat(fab, View.SCALE_Y, 0, 1f);
				collapseYScaleAnimator.setInterpolator(interpolator);
				collapseYScaleAnimator.setStartDelay(delay);
				toggleOnAnimator.play(collapseYScaleAnimator);

				collapseAlphaAnimator.addListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animation) {
						((View) collapseAlphaAnimator.getTarget()).setVisibility(VISIBLE);
						((View) collapseAlphaAnimator.getTarget()).setClickable(true);
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

			Interpolator interpolator = new DecelerateInterpolator();
			for (FloatingActionButton fab : fabList) {
				final ObjectAnimator collapseAlphaAnimator = new ObjectAnimator().ofFloat(fab, View.ALPHA, 1f, 0);
				collapseAlphaAnimator.setInterpolator(interpolator);
				collapseAlphaAnimator.setStartDelay(delay);
				toggleOffAnimator.play(collapseAlphaAnimator);

				ObjectAnimator collapseYTransAnimator = new ObjectAnimator().ofFloat(fab, View.TRANSLATION_Y, 0, 32);
				collapseYTransAnimator.setInterpolator(interpolator);
				collapseYTransAnimator.setStartDelay(delay);
				toggleOffAnimator.play(collapseYTransAnimator);


				ObjectAnimator collapseXScaleAnimator = new ObjectAnimator().ofFloat(fab, View.SCALE_X, 1f, 0);
				collapseXScaleAnimator.setInterpolator(interpolator);
				collapseXScaleAnimator.setStartDelay(delay);
				toggleOffAnimator.play(collapseXScaleAnimator);

				ObjectAnimator collapseYScaleAnimator = new ObjectAnimator().ofFloat(fab, View.SCALE_Y, 1f, 0);
				collapseYScaleAnimator.setInterpolator(interpolator);
				collapseYScaleAnimator.setStartDelay(delay);
				toggleOffAnimator.play(collapseYScaleAnimator);

				collapseAlphaAnimator.addListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animation) {
						((View) collapseAlphaAnimator.getTarget()).setVisibility(VISIBLE);
					}

					@Override
					public void onAnimationEnd(Animator animation) {
						((View) collapseAlphaAnimator.getTarget()).setVisibility(INVISIBLE);
						((View) collapseAlphaAnimator.getTarget()).setClickable(false);
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

	protected int getDimension(@DimenRes int id) {
		return getResources().getDimensionPixelSize(id);
	}

	protected TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
		return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
	}
}