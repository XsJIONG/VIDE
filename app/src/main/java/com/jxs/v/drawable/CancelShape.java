package com.jxs.v.drawable;

import android.content.Context;
import com.jxs.v.ui.BCurve;
import com.jxs.v.ui.UI;

public class CancelShape extends BShape {
	public BCurve LeftRight, RightLeft;
	public CancelShape() {
		this(UI.dp2px(12));
	}
	public CancelShape(float length) {
		length /= 2;
		length = (float) (length * Math.sqrt(2) / 2);
		LeftRight = BCurve.createLine(-length, -length, length, length);
		RightLeft = BCurve.createLine(length, -length, -length, length);
	}
	@Override
	public BCurve[] getAll() {
		return new BCurve[] {LeftRight, RightLeft};
	}
}
