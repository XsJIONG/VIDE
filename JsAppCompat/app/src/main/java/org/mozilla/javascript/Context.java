package org.mozilla.javascript;

public class Context {
	private Context() {}
	public static Context enter() {
		return new Context();
	}
	public void setOptimizationLevel(int i) {}
	public void setGeneratingDebug(boolean flag) {}
	public Object evaluateString(Scriptable scope, String code, String name, int lineno, Object wtf) {return null;}
	public void exit() {}
	public static Object javaToJS(Object obj, Scriptable wtf) {return null;}
	public final void setApplicationClassLoader(ClassLoader loader) {}
	public static String toString(Object obj) {return null;}
}
