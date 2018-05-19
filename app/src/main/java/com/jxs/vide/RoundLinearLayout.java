package com.jxs.vide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.widget.LinearLayout;

public class RoundLinearLayout extends LinearLayout {
	private Path _PathForClip;
	private RectF _Rect;
	public RoundLinearLayout(Context cx) {
		super(cx);
		setWillNotDraw(false);
		_PathForClip = new Path();
		_Rect = new RectF();
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		_Rect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
		update();
	}
	private float _Radius=0;
	public void setRadius(float f) {
		_Radius = f;
		update();
	}
	private void update() {
		_PathForClip.reset();
		if (_Radius > 0) _PathForClip.addRoundRect(_Rect, _Radius, _Radius, Path.Direction.CW);
	}
	@Override
	public void draw(Canvas canvas) {
		if (_Radius > 0) canvas.clipPath(_PathForClip);
		super.draw(canvas);
	}
}
