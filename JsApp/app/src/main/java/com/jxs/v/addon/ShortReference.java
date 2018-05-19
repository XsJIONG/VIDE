package com.jxs.v.addon;

public class ShortReference extends VReference<Short> {
	public void set(short value) {super.set(Short.valueOf(value));}
	public void set(Number value) {super.set(Short.valueOf(value.shortValue()));}
}
