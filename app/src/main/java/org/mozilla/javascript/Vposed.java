package org.mozilla.javascript;

public class Vposed {
	private Object returnObj=null;
	private boolean returnOverrided=false, refuseInvokeMethod=false;
	public void setReturnObject(Object obj) {
		returnOverrided=true;
		returnObj=obj;
	}
	public void refuseInvokeMethod(boolean flag) {
		refuseInvokeMethod=flag;
	}
	public boolean isReturnOverrided() {
		return returnOverrided;
	}
	public boolean isRefuseInvokeMethod() {
		return refuseInvokeMethod;
	}
	public Object getReturnObject() {
		return returnObj;
	}
}
