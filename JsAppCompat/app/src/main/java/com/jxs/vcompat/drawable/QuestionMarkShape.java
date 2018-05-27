package com.jxs.vcompat.drawable;
import android.content.Context;
import com.jxs.vcompat.ui.BCurve;
import com.jxs.vcompat.ui.UI;

public class QuestionMarkShape extends BShape {
	public BCurve LeftTop,RightTop,RightBottom,Dot;
	public QuestionMarkShape(float stroke) {
		this(1, stroke);
	}
	public QuestionMarkShape(float scale, float stroke) {
		LeftTop = BCurve.createQuadrant(UI.dp2px(7) * scale, -180);
		RightTop = LeftTop.copy().rotate(0, 0, 90);
		RightBottom = new BCurve(UI.dp2px(7) * scale, 0, 0, UI.dp2px(12) * scale, UI.dp2px(7) * scale, UI.dp2px(9) * scale, 0, UI.dp2px(4) * scale);
		Dot = BCurve.createLine(-stroke / 2, UI.dp2px(15) * scale, stroke / 2, UI.dp2px(15) * scale);
		int e=-UI.dp2px(3);
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
