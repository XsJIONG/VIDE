package com.jxs.vide;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.util.Log;
import com.jxs.v.addon.VApplication;
import com.jxs.vapp.program.JsApp;
import com.jxs.vcompat.ui.UI;
import com.jxs.vide.lang.Lang;
import com.jxs.vide.lang.Lang_Chinese;
import com.myopicmobile.textwarrior.android.YoyoNavigationMethod;
import com.wanjian.cockroach.Cockroach;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

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
