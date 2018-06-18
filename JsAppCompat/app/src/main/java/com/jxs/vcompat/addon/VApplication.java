package com.jxs.vcompat.addon;

import android.app.Application;
import android.content.Context;
import com.jxs.vcompat.ui.UI;
import android.support.multidex.*;

public class VApplication extends Application {
	private static Context cx;

	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler.getInstance().init();
		cx = getApplicationContext();
		MultiDex.install(this);
	}

	public static Context getContext() {
		return cx;
	}
}
