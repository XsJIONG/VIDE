package com.jxs.vcompat.addon;

public class ByteReference extends VReference<Byte> {
	public void set(byte value) {super.set(Byte.valueOf(value));}
	public void set(Number value) {super.set(Byte.valueOf(value.byteValue()));}
}
