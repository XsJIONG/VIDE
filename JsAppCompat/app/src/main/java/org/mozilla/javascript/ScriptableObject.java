package org.mozilla.javascript;

public class ScriptableObject implements Scriptable {
	public static final int CONST=1;
	public void defineProperty(String name, Object obj, int type) {}
	public static Object getProperty(Scriptable obj, String name) {return null;}
	public static Scriptable getTopLevelScope(Scriptable scope) {return null;}
}
