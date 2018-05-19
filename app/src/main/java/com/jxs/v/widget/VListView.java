package com.jxs.v.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.EdgeEffect;
import android.widget.ListView;
import com.jxs.v.ui.UI;
import java.lang.reflect.Field;

public class VListView extends ListView implements UI.OnThemeChangeListener {
	public VListView(Context cx) {
		this(cx, null);
	}
	public VListView(Context cx, AttributeSet attr) {
		this(cx, attr, 0);
	}
	public VListView(Context cx, AttributeSet attr, int defStyle) {
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
			Class<?> cl=AbsListView.class;
			Field f=cl.getDeclaredField("mEdgeGlowTop");
			f.setAccessible(true);
			((EdgeEffect) f.get(this)).setColor(color);
			f = cl.getDeclaredField("mEdgeGlowBottom");
			f.setAccessible(true);
			((EdgeEffect) f.get(this)).setColor(color);
		} catch (Throwable e) {}
	}
}
