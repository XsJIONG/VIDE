package com.jxs.vcompat.addon;

public class CharReference extends VReference<Character> {
	public void set(char value) {super.set(Character.valueOf(value));}
}
