/*
 * Copyright (c) 2011 Tah Wei Hoon.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License Version 2.0,
 * with full text available at http://www.apache.org/licenses/LICENSE-2.0.html
 *
 * This software is provided "as is". Use at your own risk.
 */
package com.myopicmobile.textwarrior.common;


/**
 * Singleton class containing the symbols and operators of the Javascript language
 */
public class LanguageJavascript extends Language {
	private static Language _theOne = null;

	private final static String[] keywords = {
		"arguments","break","byte",
		"case","catch","const",
		"continue","debugger","default","delete",
		"else","eval","export",
		"false","finally",
		"for","function","if",
		"import","in","instanceof",
		"let","new","null",
		"return","switch","this","throw","throws","true",
		"try","typeof","undefined","var","while","with","yield"
	};
	private final static char[] BASIC_OPERATORS = {
		'(', ')', '{', '}', '.', ',', ';', '=', '+', '-',
		'/', '*', '&', '!', '|', ':', '[', ']', '<', '>',
		'?', '~', '%', '^'
	};
	public static Language getInstance() {
		if (_theOne == null) {
			_theOne = new LanguageJavascript();
		}
		return _theOne;
	}

	public boolean isLineAStart(char c) {
		return false;
	}
	public LanguageJavascript() {
		setKeywords(keywords);
		setOperators(BASIC_OPERATORS);
	}
}
