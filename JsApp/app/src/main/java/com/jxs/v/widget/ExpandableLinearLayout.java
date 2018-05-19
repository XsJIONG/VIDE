package com.jxs.v.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import java.util.ArrayList;

public class ExpandableLinearLayout extends LinearLayout {
	public ExpandableLinearLayout(Context cx) {
		super(cx);
		init();
	}
	public ExpandableLinearLayout(Context cx, AttributeSet attr) {
		super(cx, attr);
		init();
	}
	public ExpandableLinearLayout(Context cx, AttributeSet attr, int defStyle) {
		super(cx, attr, defStyle);
		init();
	}
	private boolean Expanded=true;
	public boolean isExpanded() {
		return Expanded;
	}
	public ValueAnimator getExpandAnimator() {
		return expandAnimator;
	}
	public ValueAnimator getCollapseAnimator() {
		return collapseAnimator;
	}
	private void init() {
		expandAnimator = ValueAnimator.ofFloat(0, 1);
		expandAnimator.setDuration(1000);
		expandAnimator.setInterpolator(new DecelerateInterpolator());
		expandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(final ValueAnimator ani) {
				post(new Runnable() {
					@Override
					public void run() {
						_Progress = (float) ani.getAnimatedValue();
						invalidate();
						requestLayout();
					}
				});
			}
		});
		expandAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator ani) {
				setVisibility(View.VISIBLE);
				animating = true;
			}
			@Override
			public void onAnimationEnd(Animator ani) {
				animating = false;
				Expanded = true;
			}
			@Override
			public void onAnimationCancel(Animator ani) {}
			@Override
			public void onAnimationRepeat(Animator ani) {}
		});
		collapseAnimator = ValueAnimator.ofFloat(1, 0);
		collapseAnimator.setDuration(1000);
		collapseAnimator.setInterpolator(new DecelerateInterpolator());
		collapseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(final ValueAnimator ani) {
				post(new Runnable() {
					@Override
					public void run() {
						_Progress = (float) ani.getAnimatedValue();
						invalidate();
						requestLayout();
					}
				});
			}
		});
		collapseAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator ani) {
				animating = true;
			}
			@Override
			public void onAnimationEnd(Animator ani) {
				setVisibility(View.GONE);
				animating = false;
				Expanded = false;
			}
			@Override
			public void onAnimationCancel(Animator ani) {}
			@Override
			public void onAnimationRepeat(Animator ani) {}
		});
	}
	public boolean isAnimating() {
		return animating;
	}
	public void expandOrCollapse() {
		expandOrCollapse(true);
	}
	public void expandOrCollapse(boolean animate) {
		if (animating) return;
		if (Expanded) collapse(animate); else expand(animate);
	}
	public void collapse() {
		collapse(true);
	}
	public void expand() {
		expand(true);
	}
	private ValueAnimator expandAnimator,collapseAnimator;
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (animating) {
			if (getOrientation() == HORIZONTAL)
				setMeasuredDimension(width, (int) ((float) height * _Progress));
			else
				setMeasuredDimension((int) ((float) width * _Progress), height);
			return;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = getMeasuredWidth();
		height = getMeasuredHeight();
	}
	private int width=-1,height=-1;
	public void expand(final boolean animate) {
		if (getVisibility() == View.VISIBLE) return;
		if (animating) return;
		post(new Runnable() {
			@Override
			public void run() {
				if (animate) {
					expandAnimator.start();
				} else {
					_Progress = 1;
					Expanded = true;
					setVisibility(View.VISIBLE);
					requestLayout();
				}
			}
		});
	}
	public void collapse(final boolean animate) {
		if (getVisibility() == View.GONE) return;
		if (animating) return;
		post(new Runnable() {
			@Override
			public void run() {
				if (animate) {
					collapseAnimator.start();
				} else {
					_Progress = 0;
					Expanded = false;
					setVisibility(View.GONE);
					requestLayout();
				}
			}
		});
	}
	private float _Progress=-1;
	private boolean animating=false;
}
