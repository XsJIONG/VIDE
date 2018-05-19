/*
 * Copyright (c) 2013 Tah Wei Hoon.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License Version 2.0,
 * with full text available at http://www.apache.org/licenses/LICENSE-2.0.html
 *
 * This software is provided "as is". Use at your own risk.
 */

package com.myopicmobile.textwarrior.common;

import java.util.HashMap;

public abstract class ColorScheme {
	public enum Colorable {
		FOREGROUND, BACKGROUND, SELECTION_FOREGROUND, SELECTION_BACKGROUND,
		CARET_FOREGROUND, CARET_BACKGROUND, CARET_DISABLED, LINE_HIGHLIGHT,
		NON_PRINTING_GLYPH, COMMENT, KEYWORD,STRING,
		INTEGER_LITERAL,OPERATOR,FUNCTION,VAR_CONST_LET,LR,DOUBLE_SYMBOL_DELIMITED_MULTILINE,LITERAL,NAME
		}

	protected HashMap<Colorable, Integer> _colors = generateDefaultColors();

	public void setColor(Colorable colorable, int color) {
		_colors.put(colorable, color);
	}

	public int getColor(Colorable colorable) {
		Integer color = _colors.get(colorable);
		if (color == null) {
			TextWarriorException.fail("Color not specified for " + colorable);
			return 0;
		}
		return color.intValue();
	}

	// Currently, color scheme is tightly coupled with semantics of the token types
	public int getTokenColor(int tokenType) {
		Colorable element;
		switch (tokenType) {
			case Lexer.NORMAL://常规
				element = Colorable.FOREGROUND;
				break;
			case Lexer.KEYWORD://关键词
				element = Colorable.KEYWORD;
				break;
			case Lexer.FUNCTION://function
				element = Colorable.FUNCTION;
				break;
			case Lexer.VAR_CONST_LET://var const let
				element = Colorable.VAR_CONST_LET;
				break;
			case Lexer.LR://括号
				element = Colorable.LR;
				break;
			case Lexer.DOUBLE_SYMBOL_DELIMITED_MULTILINE://null undefined
				element = Colorable.DOUBLE_SYMBOL_DELIMITED_MULTILINE;
				break;
			case Lexer.INTEGER_LITERAL://数字
				element = Colorable.INTEGER_LITERAL;
				break;
			case Lexer.COMMENT://注释
				element = Colorable.COMMENT;
				break;
			case Lexer.SINGLE_SYMBOL_DELIMITED_A: //字符串
				element = Colorable.STRING;
				break;
			case Lexer.OPERATOR: //数学运算符
				element = Colorable.OPERATOR;
				break;
			default:
				TextWarriorException.fail("Invalid token type");
				element = Colorable.FOREGROUND;
				break;
		}
		return getColor(element);
	}

	/**
	 * Whether this color scheme uses a dark background, like black or dark grey.
	 */
	public abstract boolean isDark();


	private HashMap<Colorable, Integer> generateDefaultColors() {
		// High-contrast, black-on-white color scheme
		HashMap<Colorable, Integer> colors = new HashMap<Colorable, Integer>(Colorable.values().length);
		colors.put(Colorable.FOREGROUND, 0xffcfbfad);//前景色
		colors.put(Colorable.BACKGROUND, 0xffffffff);
		colors.put(Colorable.SELECTION_FOREGROUND, 0xffffffff);//选择文本的前景色
		colors.put(Colorable.SELECTION_BACKGROUND, 0xff404040);//选择文本的背景色
		colors.put(Colorable.CARET_FOREGROUND, 0xffffffff);
		colors.put(Colorable.CARET_BACKGROUND, 0xffffffff);
		colors.put(Colorable.CARET_DISABLED, 0xff545454);
		colors.put(Colorable.LINE_HIGHLIGHT, 0x20888888);
		colors.put(Colorable.NON_PRINTING_GLYPH, 0xff2b91af);//行号
		colors.put(Colorable.COMMENT, 0xff61492d); //注释
		colors.put(Colorable.KEYWORD, 0xffae1570); //关键字
		colors.put(Colorable.FUNCTION, 0xffae1561); //function
		colors.put(Colorable.DOUBLE_SYMBOL_DELIMITED_MULTILINE, 0xFFFF0000); //null undefined
		colors.put(Colorable.LR, 0xffff007f);//括号
		colors.put(Colorable.VAR_CONST_LET, 0xffae1570); //VAR_CONST_LET
		colors.put(Colorable.INTEGER_LITERAL, 0xFF05F1D9); //数字
		colors.put(Colorable.STRING, 0xffba7a22); //字符串
		colors.put(Colorable.OPERATOR, 0xFFF40843);//宏定义
		return colors;
	}
	// In ARGB format: 0xAARRGGBB
	private static final int BLACK = 0xFF000000;
	private static final int BLUE = 0xFF0000FF;
	private static final int DARK_RED = 0xFFA31515;
	private static final int DARK_BLUE = 0xFFD040DD;
	private static final int GREY = 0xFF808080;
	private static final int LIGHT_GREY = 0xFFAAAAAA;
	private static final int MAROON = 0xFF800000;
	private static final int INDIGO = 0xFF2A40FF;
	private static final int OLIVE_GREEN = 0xFF3F7F5F;
	private static final int PURPLE = 0xFFDD4488;
	private static final int RED = 0xFFFF0000;
	private static final int WHITE = 0xFFFFFFE0;
	private static final int PURPLE2 = 0xFFFF00FF;
	private static final int LIGHT_BLUE = 0xFF6080FF;
	private static final int LIGHT_BLUE2 = 0xFF40B0FF;
	private static final int GREEN = 0xFF88AA88;
}
