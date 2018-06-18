package com.jxs.v.addon;

import android.app.Application;
import android.content.Context;
import android.support.multidex.*;

public class VApplication extends Application {
	private static Context cx;

	@Override
	public void onCreate() {
		try {
			//CrashHandler.getInstance().init();
			super.onCreate();
		} catch (Throwable t) {}
		cx = getApplicationContext();
		MultiDex.install(this);
	}

	public static Context getContext() {
		return cx;
	}
}
