package com.jxs.v.drawable;

import android.content.Context;
import com.jxs.v.ui.BCurve;
import com.jxs.v.ui.UI;

public class ArrowShape extends BShape {
	public BCurve Top,Bottom,Body;
	public ArrowShape(Context cx, float stroke) {
		this(new UI(cx).dp2px(16), new UI(cx).dp2px(11), stroke);
	}
	public ArrowShape(float length, float arrowlength, float stroke) {
		arrowlength = (float) (arrowlength / 2 * Math.sqrt(2));
		Top = BCurve.createLine(0, 0, arrowlength, -arrowlength);
		Top.offset(-stroke / 2, 0);
		Top.offset(-length / 2, stroke / 4);
		Body = BCurve.createLine(-length / 2, 0, length / 2, 0);
		Bottom = BCurve.createLine(0, 0, arrowlength, arrowlength);
		Bottom.offset(-stroke / 2, 0);
		Bottom.offset(-length / 2, -stroke / 4);
	}
	@Override
	public BCurve[] getAll() {
		return new BCurve[] {Top, Body, Bottom};
	}
}
