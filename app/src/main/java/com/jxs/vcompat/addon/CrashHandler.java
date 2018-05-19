package com.jxs.vcompat.addon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import android.os.Environment;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
	@Override
	public void uncaughtException(Thread t, Throwable thr) {
		if (t == null && thr != null) DEFAULT.uncaughtException(t, thr);
		onCrash(thr);
	}
	public void onCrash(Throwable thr) {
		OutputStream out=getOutputStream();
		if (out != null) {
			PrintWriter wr=new PrintWriter(getOutputStream());
			thr.printStackTrace(wr);
			wr.close();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
	}

	private static CrashHandler INSTANCE;
	public static CrashHandler getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CrashHandler();
		return INSTANCE;
	}
	File OutputFile=new File(Environment.getExternalStorageDirectory(), "CrashOutput");
	public void setOutputFile(File f) {
		OutputFile=f;
	}
	public File getOutputFile() {
		return OutputFile;
	}
	public OutputStream getOutputStream() {
		try {
			return new FileOutputStream(OutputFile);
		} catch (IOException e) {
			return null;
		}
	}
	Thread.UncaughtExceptionHandler DEFAULT;
	public void init() {
		DEFAULT = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	private CrashHandler() {}
}
