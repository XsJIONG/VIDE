package com.jxs.vide;

import java.io.File;
import java.io.PrintStream;
import android.os.Environment;

public class VLogger {
	private static final File LogFile=new File(Environment.getExternalStorageDirectory(), "VIDELog.txt");
	public static PrintStream out;
	static {
		try {
			out = new PrintStream(LogFile);
		} catch (Exception e) {}
	}
}
