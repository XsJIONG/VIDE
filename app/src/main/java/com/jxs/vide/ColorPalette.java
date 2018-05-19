package com.jxs.vide;

import java.util.Random;

public class ColorPalette {
	public static int[] Colors={
		0xffe65c6f,0xfff98d77,0xffffcb69,0xffffe269,0xfffffa85,
		0xff76b8cc,0xff87ccdb,0xffa2d9e1,0xffbfe4e5,0xffd0eee6
	};
	private static Random r=new Random();
	public static int getRandom() {
		return Colors[r.nextInt(Colors.length)];
	}
}
