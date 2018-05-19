package com.jxs.vcompat.addon;

public class VReference<T> {
	T value=null;
	boolean seted=false;
	public T get() {return value;}
	public void set(T s) {seted = true;value = s;}
	public void recycle() {}
	public boolean isSetted() {return seted;}
	public void setSetted(boolean flag) {this.seted = flag;}
}
