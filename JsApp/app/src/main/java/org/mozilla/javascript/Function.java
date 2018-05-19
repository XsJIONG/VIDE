package org.mozilla.javascript;

public interface Function {
	Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args);
}
