package com.jxs.vcompat.ui;

public class Point {
	public float x=-1,y=-1;
	public Point() {

	}
	public Point(float x, float y) {
		this.x = x; this.y = y;
	}
	public Point set(Point p) {
		this.x = p.x; this.y = p.y;
		return this;
	}
	public Point set(float x, float y) {
		this.x = x; this.y = y;
		return this;
	}
	public Point copy() {
		return new Point(x, y);
	}
	public Point offset(float x, float y) {
		this.x += x; this.y += y;
		return this;
	}
	public Point rotate(Point p, double degree) {
		return rotate(p.x, p.y, degree);
	}
	public Point lerp(Point t, float progress) {
		return new Point(x + (t.x - x) * progress, y + (t.y - y) * progress);
	}
	public Point rotate(float cx, float cy, double degree) {
		degree = Math.toRadians(degree);
		float nx=(float) (Math.cos(degree) * (x - cx) - Math.sin(degree) * (y - cy) + cx);
		float ny=(float) (Math.sin(degree) * (x - cx) + Math.cos(degree) * (y - cy) + cy);
		this.x = nx; this.y = ny;
		return this;
	}
}

