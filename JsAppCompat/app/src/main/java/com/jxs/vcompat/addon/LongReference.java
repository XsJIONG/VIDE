package com.jxs.vcompat.addon;

public class LongReference extends VReference<Long> {
	public void set(long value) {super.set(Long.valueOf(value));}
	public void set(Number value) {super.set(Long.valueOf(value.longValue()));}
}
