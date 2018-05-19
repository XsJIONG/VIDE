package com.jxs.vide;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import com.jxs.vcompat.ui.ColorUtil;

public class BorderDrawable extends Drawable {
	private int _BackGroundColor=Color.WHITE;
	private Paint _BorderPaint;
	public BorderDrawable() {
		init();
	}
	public BorderDrawable(int background, int border) {
		init();
		setBackgroundColor(background);
		setBorderColor(border);
	}
	private void init() {
		_BorderPaint = new Paint();
		_BorderPaint.setStyle(Paint.Style.STROKE);
		_BorderPaint.setStrokeWidth(2);
	}
	public void setStrokeWidth(float width) {
		_BorderPaint.setStrokeWidth(width);
		invalidateSelf();
	}
	@Override
	public void draw(Canvas canvas) {
		canvas.drawColor(_BackGroundColor);
		canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), _BorderPaint);
	}
	public void setBackgroundColor(int color) {
		_BackGroundColor = color;
		invalidateSelf();
	}
	public void setBorderColor(int color) {
		_BorderPaint.setColor(color);
		invalidateSelf();
	}
	@Override
	public void setAlpha(int alpha) {
		_BackGroundColor = ColorUtil.setAlpha(_BackGroundColor, alpha);
		_BorderPaint.setColor(ColorUtil.setAlpha(_BorderPaint.getColor(), alpha));
	}
	@Override
	public void setColorFilter(ColorFilter filter) {
		_BorderPaint.setColorFilter(filter);
	}
	@Override
	public int getOpacity() {
		return PixelFormat.TRANSPARENT;
	}
}
