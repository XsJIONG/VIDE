package com.jxs.vcompat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.jxs.vcompat.ui.ColorUtil;
import com.jxs.vcompat.ui.UI;

public class FloatingActionButton extends CardView implements UI.OnThemeChangeListener {
	public FloatingActionButton(Context cx) {
		super(cx);
		init();
	}
	public FloatingActionButton(Context cx, AttributeSet attr) {
		super(cx, attr);
		init();
	}
	public FloatingActionButton(Context cx, AttributeSet attr, int defStyle) {
		super(cx, attr, defStyle);
		init();
	}
	private int _PrimaryColor, _DarkedColor;
	private void init() {
		setButtonColorReal(UI.getThemeColor());
		UI.registThemeChangedListener(this, this);
	}
	private void setButtonColorReal(int color) {
		_PrimaryColor = color;
		_DarkedColor = ColorUtil.darkColor(_PrimaryColor, 20);
		setCardBackgroundColor(_PrimaryColor);
		invalidate();
	}
	@Override
	public void onThemeChange(String key) {
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		if (!_SettedColor) {
			setButtonColorReal(UI.getThemeColor());
			invalidate();
		}
	}
	private Drawable _Icon=null;
	public void setImageDrawable(Drawable d) {
		_Icon = d;
		invalidate();
	}
	public void setImageResource(int res) {
		setImageDrawable(getResources().getDrawable(res));
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		UI.removeThemeChangedListener(this);
	}
	private boolean _SettedColor=false;
	public void setButtonColor(int color) {
		setButtonColorReal(color);
		_SettedColor = true;
	}
	private int _ButtonSize=-1;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		_ButtonSize = Math.min(w, h) / 2;
		setRadius(_ButtonSize);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		if (_Icon != null) {
			_Icon.setBounds(0, 0, _ButtonSize * 2, _ButtonSize * 2);
			_Icon.draw(canvas);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				setCardBackgroundColor(_DarkedColor);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				setCardBackgroundColor(_PrimaryColor);
				invalidate();
				break;
		}
		return true;
	}
}
