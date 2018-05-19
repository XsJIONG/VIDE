package com.jxs.v.drawable;

import android.content.Context;
import com.jxs.v.ui.BCurve;
import com.jxs.v.ui.UI;

public class SimpleArrowShape extends BShape {
	public BCurve Top,Bottom;
	public SimpleArrowShape(Context cx, float stroke) {
		this(new UI(cx).dp2px(12), stroke);
	}
	public SimpleArrowShape(float length, float stroke) {
		length = (float) (length / 2 * Math.sqrt(2));
		Top = BCurve.createLine(length, 0, 0, -length);
		Top.offset(0, stroke / 2);
		Bottom = BCurve.createLine(length, 0, 0, length);
		Bottom.offset(0, -stroke / 2);
	}
	@Override
	public BCurve[] getAll() {
		return new BCurve[] {Top, Bottom};
	}
}
