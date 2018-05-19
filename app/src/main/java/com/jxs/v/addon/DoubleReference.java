package com.jxs.v.addon;

public class DoubleReference extends VReference<Double> {
	public void set(double value) {super.set(Double.valueOf(value));}
	public void set(Number value) {super.set(Double.valueOf(value.doubleValue()));}
}
