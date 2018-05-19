package com.jxs.vcompat.addon;

public class IntReference extends VReference<Integer> {
	public void set(int value) {super.set(Integer.valueOf(value));}
	public void set(Number value) {super.set(Integer.valueOf(value.intValue()));}
}
