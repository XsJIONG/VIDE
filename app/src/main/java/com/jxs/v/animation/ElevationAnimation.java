package com.jxs.v.animation;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import com.jxs.v.ui.VAnimation;

public class ElevationAnimation extends VAnimation<View> {
	private float from=-1, to=-1;
	public ElevationAnimation(View v, float from, float to) {
		super(v);
		this.from = from;
		this.to = to;
		animator = ValueAnimator.ofFloat(from, to);
		animator.setInterpolator(new DecelerateInterpolator());
		animator.setDuration(300);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator ani) {
				getView().setElevation((float) ani.getAnimatedValue());
			}
		});
	}
	ValueAnimator animator=null;
	@Override
	public void start() {
		animator.start();
		animationStart();
	}
	@Override
	public void pause() {
		animator.cancel();
	}
	@Override
	public void stop() {
		animator.cancel();
		animationStop();
	}
	public float getFrom() {
		return from;
	}
	public float getTo() {
		return to;
	}
}
