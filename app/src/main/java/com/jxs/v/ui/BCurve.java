package com.jxs.v.ui;

import android.graphics.Path;

public class BCurve {
	public Point a,b,c,d;
	public BCurve() {
		this.a = new Point();
		this.b = new Point();
		this.c = new Point();
		this.d = new Point();
	}
	public BCurve(Point a, Point b, Point c, Point d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	public BCurve(float ax, float ay, float bx, float by, float cx, float cy, float dx, float dy) {
		this.a = new Point(ax, ay);
		this.b = new Point(bx, by);
		this.c = new Point(cx, cy);
		this.d = new Point(dx, dy);
	}
	public BCurve offset(float x, float y) {
		a.offset(x, y);
		b.offset(x, y);
		c.offset(x, y);
		d.offset(x, y);
		return this;
	}
	public BCurve(BCurve from) {
		a = from.a.copy();
		b = from.b.copy();
		c = from.c.copy();
		d = from.d.copy();
	}
	public BCurve rotate(Point p, double degree) {
		return rotate(p.x, p.y, degree);
	}
	public BCurve rotate(float cx, float cy, double degree) {
		a.rotate(cx, cy, degree);
		b.rotate(cx, cy, degree);
		c.rotate(cx, cy, degree);
		d.rotate(cx, cy, degree);
		return this;
	}
	public static BCurve createLine(Point a, Point b) {
		return createLine(a.x, a.y, b.x, b.y);
	}
	public static BCurve createLine(float ax, float ay, float bx, float by) {
		BCurve c=new BCurve();
		c.a.set(ax, ay);
		c.b.set(bx, by);
		c.c.set((ax + bx) / 2, (ay + by) / 2);
		c.d = c.c.copy();
		return c;
	}
	public static BCurve createQuadrant(float radius, float degree) {
		BCurve c=new BCurve();
		float dv = (float) ((4f / 3f) * Math.tan(Math.PI / 8)) * radius;
        c.a.set(0, radius);
        c.b.set(radius, 0);
        c.c.set(c.a.x + dv, c.a.y);
        c.d.set(c.b.x, c.b.y + dv);
		if (degree != 0) c.rotate(0, 0, degree);
		return c;
	}
	public BCurve addTo(Path path) {
		path.moveTo(a.x, a.y);
		path.cubicTo(c.x, c.y, d.x, d.y, b.x, b.y);
		return this;
	}
	public BCurve copy() {
		return new BCurve(a.copy(), b.copy(), c.copy(), d.copy());
	}
}

