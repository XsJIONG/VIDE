package com.jxs.vapp.program;

public abstract class Program {
	public void setOnErrorListener(OnErrorListener listener) {
		mErrorListener = listener;
	}
	public void onError(Throwable e) {
		if (mErrorListener != null) mErrorListener.onError(e);
	}
	public boolean callFunction(String name) {
		return callFunction(name, null, null);
	}
	public boolean callFunction(String name, Object[] args) {
		return callFunction(name, null, args);
	}
	public boolean callFunction(String name, JsObjectRef ref) {
		return callFunction(name, ref, null);
	}
	public abstract boolean callFunction(String name, JsObjectRef ref, Object[] args);
	public boolean callFunction() {
		return callFunction(getMethodName(1));
	}
	public boolean callFunction(Object[] args) {
		return callFunction(getMethodName(1), args);
	}
	public boolean callFunction(JsObjectRef ref) {
		return callFunction(getMethodName(1), ref);
	}
	public boolean callFunction(JsObjectRef ref, Object[] args) {
		return callFunction(getMethodName(1), ref, args);
	}
	public abstract Object eval(String source);
	public void destroy() {
		if (destroyed) return;
		destroyed = true;
	}
	public static String getMethodName(int last) {
		return Thread.currentThread().getStackTrace()[3 + last].getMethodName();
	}
	public static Object[] toArray(Object...args) {
		return args;
	}
	public boolean isDestroyed() {
		return destroyed;
	}
	boolean destroyed=false;
	private OnErrorListener mErrorListener=null;
	public static interface OnErrorListener {
		void onError(Throwable e);
	}
}
