package com.jxs.vcompat.ui;

import android.graphics.Color;

public class ColorUtil {
	public static int remixColor(int color, int ncolor) {
		int a=Color.alpha(color), r=Color.red(color); int g=Color.green(color); int b=Color.blue(color);
		a += Color.alpha(ncolor); r += Color.red(ncolor);  g += Color.green(ncolor); b += Color.blue(ncolor);
		return Color.argb(a > 255 ?255: a, r > 255 ?255: r, g > 255 ?255: g, b > 255 ?255: b);
	}
	public static int lightColor(int color, int light) {
		return remixColor(color, Color.rgb(light, light, light));
	}
	public static int darkColor(int color, int dark) {
		int a=Color.alpha(color), r=Color.red(color); int g=Color.green(color); int b=Color.blue(color);
		a -= dark; r -= dark; g -= dark; b -= dark;
		return Color.argb(a < 0 ?0: a, r < 0 ?0: r, g < 0 ?0: g, b < 0 ?0: b);
	}
	public static int toBlackWhite(int color) {
		int all=Color.red(color) + Color.green(color) + Color.blue(color);
		all /= 3;
		return Color.argb(Color.alpha(color), all, all, all);
	}
	public static int getBlackOrWhite(int background) {
		int r=Color.red(background); int g=Color.green(background); int b=Color.blue(background);
		int all=(r + g + b) / 3;
		if (all >= 200) return Color.BLACK; else return Color.WHITE;
	}
	public static int getComplementaryColor(int color) {
		int r=Color.red(color); int g=Color.green(color); int b=Color.blue(color);
		return Color.argb(Color.alpha(color), 255 - r, 255 - g, 255 - b);
	}
	public static int setAlpha(int color, int alpha) {
		return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
	}
	private ColorUtil() {}
}
