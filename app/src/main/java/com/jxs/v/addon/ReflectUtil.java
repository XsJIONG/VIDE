package com.jxs.v.addon;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtil {
	public static Object getStaticField(Class cl, String name) {
		try {
			Field f=cl.getField(name);
			f.setAccessible(true);
			return f.get(null);
		} catch (Exception e) {return null;}
	}
	public static Object getField(Object obj, String name) {
		try {
			Field f=obj.getClass().getField(name);
			f.setAccessible(true);
			return f.get(obj);
		} catch (Exception e) {return null;}
	}
	public static Object invoke(Object obj, String name, Class[] type, Object...args) {
		try {
			Method m=obj.getClass().getDeclaredMethod(name, type);
			m.setAccessible(true);
			return m.invoke(obj, args);
		} catch (Exception e) {return null;}
	}
	public static Object invokeStatic(Class<?> cl, String name, Class[] type, Object[]...arg) {
		try {
			Method m=cl.getDeclaredMethod(name, type);
			m.setAccessible(true);
			return m.invoke(null, arg);
		} catch (Exception e) {return null;}
	}
	public static Object invoke(Object obj, String name, Object... arg) {
		Class[] cl=new Class[arg.length];
		for (int i=0;i < arg.length;i++) cl[i] = arg[i].getClass();
		return invoke(obj, name, cl, arg);
	}
	public static Object invokeStatic(Class<?> cl, String name, Object... arg) {
		Class[] t=new Class[arg.length];
		for (int i=0;i < arg.length;i++) t[i] = arg[i].getClass();
		return invokeStatic(cl, name, t, arg);
	}
	private ReflectUtil() {}
}
