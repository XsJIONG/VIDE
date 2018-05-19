package org.mozilla.javascript;

public interface Scriptable {
	void defineProperty(String name, Object obj, int type);
}
