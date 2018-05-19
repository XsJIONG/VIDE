package com.jxs.vcompat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EdgeEffect;
import android.widget.ScrollView;
import com.jxs.vcompat.ui.UI;
import java.lang.reflect.Field;

public class VScrollView extends ScrollView implements UI.OnThemeChangeListener {
	public VScrollView(Context cx) {
		this(cx, null);
	}
	public VScrollView(Context cx, AttributeSet attr) {
		this(cx, attr, 0);
	}
	public VScrollView(Context cx, AttributeSet attr, int defStyle) {
		super(cx, attr, defStyle);
		rSetGlowColor(UI.getThemeColor());
	}
	@Override
	public void onThemeChange(String key) {
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		if (!settedGlowColor) rSetGlowColor(UI.getThemeColor());
	}
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		UI.registThemeChangedListener(this, this);
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		UI.removeThemeChangedListener(this);
	}
	private boolean settedGlowColor=false;
	public void setGlowColor(int color) {
		rSetGlowColor(color);
		settedGlowColor = true;
	}
	private void rSetGlowColor(int color) {
		try {
			Class<?> cl=ScrollView.class;
			Field f=cl.getDeclaredField("mEdgeGlowTop");
			f.setAccessible(true);
			((EdgeEffect) f.get(this)).setColor(color);
			f = cl.getDeclaredField("mEdgeGlowBottom");
			f.setAccessible(true);
			((EdgeEffect) f.get(this)).setColor(color);
		} catch (Throwable e) {}
	}
}
