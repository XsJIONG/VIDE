package com.jxs.v.drawable;

import android.content.Context;
import com.jxs.v.ui.BCurve;
import com.jxs.v.ui.UI;

public class QuestionMarkShape extends BShape {
	public BCurve LeftTop,RightTop,RightBottom,Dot;
	public QuestionMarkShape(Context cx, float stroke) {
		this(cx, 1, stroke);
	}
	public QuestionMarkShape(Context cx, float scale, float stroke) {
		UI ui=new UI(cx);
		LeftTop = BCurve.createQuadrant(ui.dp2px(7) * scale, -180);
		RightTop = LeftTop.copy().rotate(0, 0, 90);
		RightBottom = new BCurve(ui.dp2px(7) * scale, 0, 0, ui.dp2px(12) * scale, ui.dp2px(7) * scale, ui.dp2px(9) * scale, 0, ui.dp2px(4) * scale);
		Dot = BCurve.createLine(-stroke / 2, ui.dp2px(15) * scale, stroke / 2, ui.dp2px(15) * scale);
		int e=-ui.dp2px(3);
		LeftTop.offset(0, e);
		RightTop.offset(0, e);
		RightBottom.offset(0, e);
		Dot.offset(0, e);
	}
	@Override
	public BCurve[] getAll() {
		return new BCurve[] {LeftTop, RightTop, RightBottom, Dot};
	}
}
