package org.mozilla.javascript;

import java.lang.reflect.Method;

public interface VposedListener {
	void beforeInvoke(Context cx, Scriptable thisObj, Object obj, Object[] args, Vposed p);
	void afterInvoke(Context cx, Scriptable thisObj, Object obj, Object[] args, Vposed p);
}
