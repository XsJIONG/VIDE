package com.jxs.v.addon;

import android.app.Application;
import android.content.Context;

public class VApplication extends Application {
	private static Context cx;

	@Override
	public void onCreate() {
		try {
			//CrashHandler.getInstance().init();
			super.onCreate();
		} catch (Throwable t) {}
		cx = getApplicationContext();
	}

	public static Context getContext() {
		return cx;
	}
}
