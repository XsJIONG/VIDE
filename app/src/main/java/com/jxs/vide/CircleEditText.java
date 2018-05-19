package com.jxs.vide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.util.AttributeSet;
import android.widget.EditText;
import com.jxs.vcompat.ui.UI;

public class CircleEditText extends EditText {
	public CircleEditText(Context cx) {
		super(cx);
		init();
	}
	public CircleEditText(Context cx, AttributeSet attr) {
		super(cx, attr);
		init();
	}
	public CircleEditText(Context cx, AttributeSet attr, int defStyle) {
		super(cx, attr, defStyle);
		init();
	}
	private void init() {
		BackgroundPaint = new Paint();
		BackgroundPaint.setStyle(Paint.Style.FILL);
		BackgroundPaint.setAntiAlias(true);
		setBackgroundDrawable(new Drawable() {
			@Override
			public void setAlpha(int alpha) {}
			@Override
			public void setColorFilter(ColorFilter cf) {}
			@Override
			public int getOpacity() {
				return PixelFormat.TRANSLUCENT;
			}
			@Override
			public void draw(Canvas canvas) {
				if (!PaddingSetted) {
					setPadding(getHeight(), IconPadding, IconPadding, IconPadding);
					PaddingSetted = true;
				}
				BackgroundPaint.setStyle(Paint.Style.FILL);
				BackgroundPaint.setColor(BackColor);
				int r=getHeight() / 2;
				canvas.drawRoundRect(0, 0, getWidth(), getHeight(), r, r, BackgroundPaint);
				BackgroundPaint.setColor(IconBackColor);
				int radius=getHeight() / 2 - IconPadding;
				canvas.drawCircle(IconPadding + radius, IconPadding + radius, radius, BackgroundPaint);
				BackgroundPaint.setStyle(Paint.Style.STROKE);
				BackgroundPaint.setStrokeWidth(1);
				BackgroundPaint.setColor(Color.WHITE);
				canvas.drawCircle(IconPadding + radius, IconPadding + radius, radius, BackgroundPaint);
				if (Icon != null && (!fixed)) {
					int size=(getHeight() - IconPadding * 2) / 3 * 2;
					Icon = ThumbnailUtils.extractThumbnail(Icon, size, size);
					fixed = true;
				}
				canvas.drawBitmap(Icon, IconPadding + (radius * 2 - Icon.getWidth()) / 2, IconPadding + (radius * 2 - Icon.getHeight()) / 2, null);
			}
		});
		setSingleLine();
	}
	private int BackColor=0x90E0E0E0,IconBackColor=0xC0757575;
	private int IconPadding=UI.dp2px(10);
	private Paint BackgroundPaint;
	public void setBackColor(int color) {
		BackColor = color;
		invalidate();
	}
	public int getBackColor() {
		return BackColor;
	}
	public void setIconBackColor(int color) {
		IconBackColor = color;
		invalidate();
	}
	private Bitmap Icon;
	private boolean fixed=false;
	public void setIconResource(int id) {
		this.Icon = ((BitmapDrawable) getContext().getResources().getDrawable(id)).getBitmap();
		fixed = false;
		invalidate();
	}
	public int getIconBackColor() {
		return IconBackColor;
	}
	private boolean PaddingSetted=false;
	public void setIconPadding(int pad) {
		IconPadding = pad;
		PaddingSetted = false;
		invalidate();
	}
	public int getIconPadding() {
		return IconPadding;
	}
}
