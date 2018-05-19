package com.jxs.vide;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class RotingView extends View {
	private float x=0;
	private float y=0;
	private Paint paint;
	private ValueAnimator xa,ya;

	public void setColor(int color) {
		paint.setColor(color);
		invalidate();
	}
	public void destroy() {
		if (xa != null) xa.pause();
		xa = null;
		if (ya != null) ya.pause();
		ya = null;
	}
	public RotingView(Context ctx) {
		super(ctx);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		xa = ValueAnimator.ofFloat(0, 180, 180, 0, 0);
		xa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator ani) {
				x = (float) ani.getAnimatedValue();
				invalidate();
			}
		});
		xa.setInterpolator(new LinearInterpolator());
		xa.setDuration(3000);
		xa.setRepeatCount(-1);
		ya = ValueAnimator.ofFloat(0, 0, 180, 180, 0);
		ya.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator ani) {
				y = (float) ani.getAnimatedValue();
				invalidate();
			}
		});
		ya.setInterpolator(new LinearInterpolator());
		ya.setDuration(3000);
		ya.setRepeatCount(-1);
		setMinimumHeight(600);
		setMinimumWidth(600);
	}

	public void start() {
		xa.start();
		ya.start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRect(new RectF(getWidth() / 5, getHeight() / 5, getWidth() * 4 / 5, getHeight() * 4 / 5), paint);
		setRotationX(x);
		setRotationY(y);
	}
}
