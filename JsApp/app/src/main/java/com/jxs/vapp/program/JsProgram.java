package com.jxs.vapp.program;

import android.util.*;
import com.jxs.vide.*;
import java.lang.reflect.*;
import java.util.*;
import org.mozilla.javascript.*;

public class JsProgram extends Program {
	public static HashMap<Object,JsProgram> Internet=new HashMap<>();
	private static ArrayList<Runnable> Listeners=new ArrayList<>();
	public static void addUpdateListener(Runnable action) {
		Listeners.add(action);
	}
	public static void removeUpdateListener(Runnable r) {
		Listeners.remove(r);
	}
	public static void upload(Object key, JsProgram app) {
		Internet.put(key, app);
		app.key = key;
		for (Runnable one : Listeners) one.run();
	}
	private Object key;
	public Object getKey() {
		return key;
	}
	public void upload(Object key) {
		upload(key, this);
	}
	public static JsProgram download(Object key) {
		return Internet.get(key);
	}
	public static JsProgram delete(Object key) {
		return Internet.remove(key);
	}
	private static long Counter=0;
	public static String getRandomKey() {
		Counter++;
		return "JsAppInternet_Tmp_" + Counter;
	}
	private Context cx;
	private ScriptableObject scope;
	private String programName;
	public JsProgram(String programName) {
		this(null, programName);
	}
	private String _Code=null;
	public void setCode(String code) {
		this._Code = code;
	}
	private Class<?> _CodeClass=null;
	public void setCodeClass(Class<?> cl) {
		_CodeClass = cl;
	}
	public String getCode() {
		return _Code;
	}
	public void run() {
		if (_Code != null) eval(_Code);
		if (_CodeClass != null) try {
				eval(_CodeClass);
			} catch (Exception e) {}
	}
	public JsProgram(JsApp exe, String programName) {
		cx = org.mozilla.javascript.Context.enter();
		cx.setOptimizationLevel(-1);
		scope = new ImporterTopLevel(cx);
		//Mark
		if (exe != null) setJsApp(exe);
		this.programName = programName == null ?"": programName;
		scope.defineProperty("console", Console.getInstance(), ScriptableObject.CONST);
	}
	public void setJsApp(JsApp app) {
		scope.defineProperty("JsApp", app, ScriptableObject.CONST);
	}
	public void setScriptableObject(ScriptableObject scope) {
		this.scope = scope;
	}
	@Override
	public Object eval(String source) {
		try {
			return cx.evaluateString(scope, source, programName, 1, null);
		} catch (Throwable e) {
			onError(e);
			return null;
		}
	}
	public void defineProperty(String name, Object value) {
		defineProperty(name, value, ScriptableObject.CONST);
	}
	public void defineProperty(String name, Object value, int attr) {
		scope.defineProperty(name, value, attr);
	}
	public ScriptableObject getScriptableObject() {
		return scope;
	}
	public Context getContext() {
		return cx;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String name) {
		this.programName = name;
	}
	public void eval(Script script) {
		script.exec(cx, scope);
	}
	public void eval(Class<?> clz) throws NoSuchMethodException,IllegalAccessException,InvocationTargetException {
		if (!Script.class.isAssignableFrom(clz)) throw new RuntimeException(clz.getName() + " is not a Script class");
		try {
			eval((Script) clz.newInstance());
		} catch (Exception e) {}
	}
	private void unbilievableException(Throwable t) {
		new RuntimeException("I really can't bilieve it:" + t.toString());
	}
	@Override
	public boolean callFunction(String name, JsObjectRef ref, Object[] args) {
		if (cx == null || scope == null) return false;
		try {
			if (args == null) args = new Object[0];
			Object function=ScriptableObject.getProperty(scope, name);
			if (!(function instanceof org.mozilla.javascript.Function)) return false;
			org.mozilla.javascript.Function f=(org.mozilla.javascript.Function) function;
			Scriptable topLevel=ScriptableObject.getTopLevelScope(scope);
			Object o;
			if (ref == null)
				o = f.call(cx, topLevel, scope, args);
			else
				ref.set((o = f.call(cx, topLevel, scope, args)));
			return !Undefined.isUndefined(o);
		} catch (Throwable err) {
			onError(err);
			return false;
		}
	}
	@Override
	public void destroy() {
		super.destroy();
		cx.exit();
		cx = null;
		scope = null;
		System.gc();
	}
}
