package com.jxs.v.ui;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ViewLoader {
	public static void load(final Activity ac) {
		try {
			for (final Field af : ac.getClass().getDeclaredFields()) {
				af.setAccessible(true);
				if (af.isAnnotationPresent(VID.class)) {
					VID id=af.getAnnotation(VID.class);
					if (id.value() != -1) af.set(Modifier.isStatic(af.getModifiers()) ?null: ac, ac.findViewById(id.value()));
				}
				if (af.isAnnotationPresent(VOnClick.class)) {
					VOnClick v=af.getAnnotation(VOnClick.class);
					if (v.value() != null) {
						try {
							final Method m=ac.getClass().getDeclaredMethod(v.value(), new Class[] {View.class});
							m.setAccessible(true);
							((View) af.get(Modifier.isStatic(af.getModifiers()) ?null: ac)).setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										try {
											m.invoke(Modifier.isStatic(af.getModifiers()) ?null: ac, v);
										} catch (IllegalAccessException e) {Log.e("VL", Log.getStackTraceString(e));} catch (InvocationTargetException e) {Log.e("VL", Log.getStackTraceString(e.getCause()));}
									}
								});
						} catch (NoSuchMethodError e) {
							Log.e("VL", "Cannot find method " + v.value() + " with argument [View]");
						}
					}
				}
			}
			for (final Method am : ac.getClass().getDeclaredMethods()) {
				am.setAccessible(true);
				if (am.isAnnotationPresent(VBindClick.class)) {
					VBindClick v=am.getAnnotation(VBindClick.class);
					if (v.value() != -1) {
						ac.findViewById(v.value()).setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									try {
										am.invoke(Modifier.isStatic(am.getModifiers()) ?null: ac, v);
									} catch (Exception e) {
										Log.e("VL", Log.getStackTraceString(e));
									}
								}
							});
					}
				}
			}
		} catch (Exception e) {Log.e("VL", Log.getStackTraceString(e));}
	}

	private ViewLoader() {}
}

