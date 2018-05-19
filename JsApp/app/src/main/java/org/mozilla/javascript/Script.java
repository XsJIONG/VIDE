package org.mozilla.javascript;

public interface Script
{
	void exec(Context cx, ScriptableObject scope);
}
