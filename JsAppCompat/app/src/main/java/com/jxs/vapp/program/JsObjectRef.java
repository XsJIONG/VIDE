package com.jxs.vapp.program;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Undefined;

public class JsObjectRef {
	private Object obj;
	public boolean undefined() {
		return (!isSetted()) || get() == null || Undefined.isUndefined(get());
	}
	public Object get() {
		return Context.javaToJS(obj, null);
	}
	private boolean _Setted=false;
	public void set(Object obj) {
		this.obj = obj;
		_Setted = true;
	}
	public boolean isSetted() {
		return _Setted;
	}
}
