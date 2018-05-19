package com.jxs.vcompat.ui;

import android.content.Context;
import android.view.View;
import android.widget.ScrollView;
import com.jxs.vcompat.fragment.ColorSelectorFragment;

public class ColorSelector extends VAlertDialog {
	private ColorSelectorFragment f;
	private ScrollView sc;
	public ColorSelector(Context cx) {
		super(cx);
		f = new ColorSelectorFragment(cx);
		sc = new ScrollView(cx);
		sc.setFillViewport(true);
		sc.addView(f.getView());
		super.setView(sc);
		setCancelable(true);
	}
	public ColorSelector setColor(int c) {
		f.setColor(c);
		return this;
	}
	public int getColor() {
		return f.getColor();
	}
	@Override
	public VAlertDialog setView(View v) {
		throw new RuntimeException("Stub!");
	}
}
