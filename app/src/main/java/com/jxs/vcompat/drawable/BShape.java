package com.jxs.vcompat.drawable;

import com.jxs.vcompat.ui.BCurve;
import com.jxs.vcompat.ui.BDrawable;
import com.jxs.vcompat.ui.Point;

public abstract class BShape {
	public void rotate(Point center, int degree) {
		rotate(center.x, center.y, degree);
	}
	public void rotate(float x, float y, int degree) {
		for (BCurve one : getAll())
			one.rotate(x, y, degree);
	}
	public void offset(float x, float y) {
		for (BCurve one : getAll())
			one.offset(x, y);
	}
	public BDrawable toSimpleDrawable() {
		BDrawable d=new BDrawable();
		d.add(getAll());
		return d;
	}
	public abstract BCurve[] getAll();
}
