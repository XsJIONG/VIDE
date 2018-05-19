package com.jxs.v.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import java.util.ArrayList;

public class BDrawable extends Drawable {
	private Paint PaintLine;
	private ArrayList<BPair> cs=new ArrayList<>();
	private ArrayList<BCurve> simple=new ArrayList<>();
	private Path path=new Path();
	private float progress=0;
	public BDrawable() {
		init();
	}
	public void setProgress(float pro) {
		if (pro > 1) return;
		this.progress = pro;
		invalidateSelf();
	}
	public void add(BCurve...c) {
		for (BCurve one : c) simple.add(one);
		invalidateSelf();
	}
	public void addPair(BCurve...objs) {
		if (objs.length % 2 != 0) return;
		for (int i=0;i < objs.length;i += 2) addPair(objs[i], objs[i + 1]);
	}
	public float getProgress() {
		return progress;
	}
	private void init() {
		PaintLine = new Paint();
		PaintLine.setColor(Color.WHITE);
		PaintLine.setStyle(Paint.Style.STROKE);
		PaintLine.setStrokeWidth(2);
	}
	public void setStrokeWidth(float width) {
		PaintLine.setStrokeWidth(width);
		invalidateSelf();
	}
	public void removePair(int index) {
		cs.remove(index);
	}
	public void remove(int index) {
		simple.remove(index);
	}
	public void removeAllPair() {
		cs.clear();
	}
	public void removeAllSingle() {
		simple.clear();
	}
	public void removeAll() {
		cs.clear();
		simple.clear();
	}
	public float getStrokeWidth() {
		return PaintLine.getStrokeWidth();
	}
	@Override
	public void setColorFilter(ColorFilter c) {
		PaintLine.setColorFilter(c);
	}
	@Override
	public void setColorFilter(int color, PorterDuff.Mode mode) {
		PaintLine.setColor(color);
		if (mode != null) PaintLine.setXfermode(new PorterDuffXfermode(mode));
		invalidateSelf();
	}
	public void setColor(int color) {
		PaintLine.setColor(color);
	}
	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}
	public void addPair(BCurve a, BCurve b) {
		cs.add(new BPair(a, b));
		invalidateSelf();
	}
	@Override
	public void setAlpha(int alpha) {
		PaintLine.setAlpha(alpha);
		invalidateSelf();
	}
	public int getColor() {
		return PaintLine.getColor();
	}
	@Override
	public void draw(Canvas c) {
		path.rewind();
		for (BPair one : cs) one.translate(progress).addTo(path);
		for (BCurve one : simple) one.addTo(path);
		c.save();
		c.translate(getBounds().centerX(), getBounds().centerY());
		c.drawPath(path, PaintLine);
		c.restore();
	}
	ValueAnimator ani=null;
	public void animateTo(float target) {
		animateTo(progress, target, 500, 0, null, null);
	}
	public void animateTo(float target, Animator.AnimatorListener listener) {
		animateTo(progress, target, 500, 0, null, listener);
	}
	public void animateTo(float from, float to, Animator.AnimatorListener listener) {
		animateTo(from, to, 500, 0, null, listener);
	}
	public void animateTo(float to, int duration) {
		animateTo(progress, to, duration, 0, null, null);
	}
	public void animateTo(float from, float to, int duration) {
		animateTo(from, to, duration, 0, null, null);
	}
	public void animateTo(float from, float target, int duration, int repeatCount, Interpolator inter, Animator.AnimatorListener listener) {
		if (inter == null) inter = new LinearInterpolator();
		if (ani != null && ani.isRunning()) ani.cancel();
		ani = ValueAnimator.ofFloat(from, target);
		ani.setDuration(duration);
		ani.setInterpolator(inter);
		ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator v) {
				setProgress((float) v.getAnimatedValue());
			}
		});
		if (repeatCount != 0) ani.setRepeatCount(repeatCount);
		if (listener != null) ani.addListener(listener);
		ani.start();
	}
}
