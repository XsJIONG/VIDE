package com.jxs.vcompat.addon;

public class FloatReference extends VReference<Float> {
	public void set(float value) {super.set(Float.valueOf(value));}
	public void set(Number value) {super.set(Float.valueOf(value.floatValue()));}
}
