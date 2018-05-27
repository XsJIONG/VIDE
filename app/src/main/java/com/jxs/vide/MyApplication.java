package com.jxs.vide;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.os.*;
import android.support.multidex.*;
import android.util.*;
import com.jxs.v.addon.*;
import com.jxs.vapp.program.*;
import com.jxs.vapp.runtime.*;
import com.jxs.vcompat.io.*;
import com.jxs.vcompat.ui.*;
import com.jxs.vide.lang.*;
import com.myopicmobile.textwarrior.android.*;
import com.wanjian.cockroach.*;
import java.io.*;
import java.lang.reflect.*;

public class MyApplication extends VApplication implements UI.OnThemeChangeListener {
	public static final File ERROR_OUT=new File(Environment.getExternalStorageDirectory(), "VIDE_Error.txt");
	public static PrintStream Err;
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		if (ERROR_OUT.exists()) ERROR_OUT.delete();
		try {
			Err = new PrintStream(ERROR_OUT);
		} catch (IOException e) {}
		MultiDex.install(this);
		hookInstrumentation(this);
		IOUtil.delete(getExternalCacheDir());
	}
	private static void hookInstrumentation(Context cx) {
		try {
			Class<?> ac=Class.forName("android.app.ActivityThread");
			Method getCurrent=ac.getDeclaredMethod("currentActivityThread");
			getCurrent.setAccessible(true);
			Object CurrentThread=getCurrent.invoke(null);
			Field insf=ac.getDeclaredField("mInstrumentation");
			insf.setAccessible(true);
			insf.set(CurrentThread, new HookInstrumentation());
			VLogger.out.println("Hooked Instrumentation");
		} catch (Throwable t) {
			VLogger.out.println("Error while hookInstrumentation:" + Log.getStackTraceString(t));
		}
	}
	private static class HookInstrumentation extends Instrumentation {
		private static Field mBaseField,mResourcesField;
		private static Class<?>[] ACS=new Class<?>[4];
		private static Field[] ACF=new Field[4];
		public HookInstrumentation() throws NoSuchFieldException,ClassNotFoundException {
			if (ACS[0] == null) {
				/*mBaseField = Activity.class.getSuperclass().getSuperclass().getDeclaredField("mBase");
				 mBaseField.setAccessible(true);
				 mResourcesField = Class.forName("android.app.ContextImpl").getDeclaredField("mResources");
				 mResourcesField.setAccessible(true);*/
				ACS[0] = JsVActivity.class;
				ACS[1] = JsVActivityCompat.class;
				ACS[2] = JsConsoleActivity.class;
				ACS[3] = JsConsoleActivityCompat.class;
				for (int i=0;i < ACF.length;i++) {
					ACF[i] = ACS[i].getDeclaredField("RS");
					ACF[i].setAccessible(true);
				}
			}
		}
		@Override
		public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
			VLogger.out.println("New Activity:" + className);
			Activity ac=super.newActivity(cl, className, intent);
			String s;
			if ((s = intent.getStringExtra("RS")) != null) hookActivity(ac, JsApp.getResources(s));
			return ac;
		}
		@Override
		public Activity newActivity(Class<?> clazz, Context context, IBinder token, Application application, Intent intent, ActivityInfo info, CharSequence title, Activity parent, String id, Object lastNonConfigurationInstance) throws InstantiationException, IllegalAccessException {
			VLogger.out.println("New ActivityF:" + clazz.getName());
			Activity ac=super.newActivity(clazz, context, token, application, intent, info, title, parent, id, lastNonConfigurationInstance);
			String s;
			if ((s = intent.getStringExtra("RS")) != null) hookActivity(ac, JsApp.getResources(s));
			return ac;
		}
		private static void hookActivity(Activity ac, Resources rs) throws IllegalAccessException {
			Class<?> c=ac.getClass();
			for (int i=0;i < ACS.length;i++) if (ACS[i] == c) {
					ACF[i].set(ac, rs);
					return;
				}
			//Context mBase=(Context) mBaseField.get(ac);
			//mResourcesField.set(mBase, rs);
		}
	}
	@Override
	public void onCreate() {
		try {
			super.onCreate();
			Lang.setLanguage(Lang.Langs[Global.getLanguage()]);
			Cockroach.install(new Cockroach.ExceptionHandler() {
					@Override
					public void handlerException(Thread t, final Throwable err) {
						if (err == null) return;
						err.printStackTrace(Err);
						new Handler(Looper.getMainLooper()).post(new Runnable() {
								@Override
								public void run() {
									MessageActivity.showMessage(MyApplication.this, L.get(L.Main_Crash), Log.getStackTraceString(err));
								}
							});
					}
				});
			JsApp.GlobalContext = getApplicationContext();
			if (Global.getThemeColor() != UI.getThemeColor()) UI.setThemeColor(Global.getThemeColor());
			UI.registThemeChangedListener(this, this);
			YoyoNavigationMethod.setCaretColor(UI.getThemeColor());
		} catch (Throwable t) {
			Log.e("VIDE", "Error while Application - onCreate!!!" + Log.getStackTraceString(t));
		}
	}
	@Override
	public void onThemeChange(String key) {
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		Global.setThemeColor(UI.getThemeColor());
		YoyoNavigationMethod.setCaretColor(UI.getThemeColor());
	}
}
