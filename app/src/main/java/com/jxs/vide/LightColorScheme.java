package com.jxs.vide;

import com.myopicmobile.textwarrior.common.ColorScheme;

public class LightColorScheme extends ColorScheme {
	public static LightColorScheme instance_;
	@Override
	public boolean isDark() {
		return false;
	}
	public LightColorScheme() {
		instance_=this;
		setColor(Colorable.CARET_BACKGROUND, 0xFFFF5722);
		setColor(Colorable.CARET_FOREGROUND, 0xFFFF5722);
		setColor(Colorable.CARET_DISABLED, 0xFF9E9E9E);
		setColor(Colorable.FUNCTION, 0xFFF44336);
		setColor(Colorable.VAR_CONST_LET, 0xFFF44336);
		setColor(Colorable.KEYWORD, 0xFFF44336);
		setColor(Colorable.LITERAL, 0xFFF44336);
		setColor(Colorable.NAME, 0xFF03A9F4);
		setColor(Colorable.FOREGROUND, 0xFF009688);
		setColor(Colorable.COMMENT, 0xFF4CAF50);
		setColor(Colorable.SELECTION_FOREGROUND, 0xFFFF5722);
		setColor(Colorable.BACKGROUND, 0xFFFFFFFF);
	}
	/*public static void setCaretColor(int color) {
		setColor(Colorable.CARET_BACKGROUND, color);
		setColor(Colorable.CARET_FOREGROUND, color);
	}*/
}
