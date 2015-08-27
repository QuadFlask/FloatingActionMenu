package com.flask.floatingactionmenu;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Property;
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
	private FadingBackgroundView fadingBackgroundView;

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

		int width = fabToggle.getMeasuredWidth(), height = 0;
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
			fabList.add(child);
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

			TextView label = new AutoVisibilityTextView(context);
			label.setTextAppearance(getContext(), labelsStyle);
			label.setText(labelText);
			label.setVisibility(INVISIBLE);
			label.setClickable(false);

			labelList.add(label);
			addView(label);
		}
	}

	public void setFloatingActionToggleButton(FloatingActionToggleButton floatingActionToggleButton) {
		fabToggle = floatingActionToggleButton;
		fabToggle.setOnToggleListener(this);
		fabList.add(fabToggle);
	}

	public void setFadingBackgroundView(FadingBackgroundView fadingBackgroundView) {
		this.fadingBackgroundView = fadingBackgroundView;
		this.fadingBackgroundView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fabToggle.toggleOff();
			}
		});
		this.fadingBackgroundView.setFab(fabToggle);
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
					applyAnimator(fab, delay, true, true);
				}
			}

			delay = duration + inc;
			for (TextView label : labelList) {
				delay -= inc;
				applyAnimator(label, delay, true, false);
			}

			if (fadingBackgroundView != null)
				toggleOnAnimator.play(createObjectAnimator(fadingBackgroundView, View.ALPHA, 0, 0, 1).setDuration(duration));
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
					applyAnimator(fab, delay, false, true);
					delay += inc;
				}
			}
			delay = -inc;
			for (TextView label : labelList) {
				applyAnimator(label, delay, false, false);
				delay += inc;
			}

			if (fadingBackgroundView != null)
				toggleOffAnimator.play(createObjectAnimator(fadingBackgroundView, View.ALPHA, 0, 1f, 0).setDuration(duration));
		}
	}

	private void applyAnimator(View target, int delay, boolean toggle, boolean scaling) {
		AnimatorSet animatorSet = toggle ? toggleOnAnimator : toggleOffAnimator;
		int[] s = new int[]{toggle ? 0 : 1, toggle ? 1 : 0};

		animatorSet.play(createObjectAnimator(target, View.ALPHA, delay, s[0], s[1]));
		animatorSet.play(createObjectAnimator(target, View.TRANSLATION_Y, delay, s[1] * target.getMeasuredHeight() / 4, s[0] * target.getMeasuredHeight() / 4));
		if (scaling) {
			animatorSet.play(createObjectAnimator(target, View.SCALE_X, delay, s[0], s[1]));
			animatorSet.play(createObjectAnimator(target, View.SCALE_Y, delay, s[0], s[1]));
		}
	}

	public void setLabelText(int index, String text) {
		labelList.get(index).setText(text);
		fabList.get(index).setLabelText(text);
	}

	public void setIcon(int index, @DrawableRes int resId) {
		fabList.get(index).setIconDrawable(resId);
	}

	public void setBackgroundColor(int index, int normalColor, int pressedColor) {
		fabList.get(index).setBackgroundColor(normalColor, pressedColor);
	}

	public void setBackgroundColor(int index, int normalColor, @Nullable Boolean calcPressedColorBrighter) {
		fabList.get(index).setBackgroundColor(normalColor, calcPressedColorBrighter);
	}

	private ObjectAnimator createObjectAnimator(Object target, Property property, long delay, float... values) {
		ObjectAnimator objectAnimator = new ObjectAnimator().ofFloat(target, property, values);
		objectAnimator.setInterpolator(interpolator);
		objectAnimator.setStartDelay(delay);
		return objectAnimator;
	}

	public void setOnFloatingActionMenuSelectedListener(final OnFloatingActionMenuSelectedListener onFloatingActionMenuSelectedListener) {
		for (FloatingActionButton fab : fabList) {
			final FloatingActionButton fFab = fab;
			fFab.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onFloatingActionMenuSelectedListener.onFloatingActionMenuSelected(fFab);
				}
			});
		}
	}

	public void removeFloatingActionButton(FloatingActionButton fab) {
		int i = fabList.indexOf(fab);
		removeView(fabList.remove(i));
		removeView(labelList.remove(i));
	}

	protected int getDimension(@DimenRes int id) {
		return getResources().getDimensionPixelSize(id);
	}

	protected TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
		return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
	}
}