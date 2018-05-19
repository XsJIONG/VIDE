package com.jxs.vcompat.activity;

import android.os.*;
import com.jxs.vcompat.fragment.*;
import java.io.*;

public abstract class ConsoleActivity extends VActivity {
	private ConsoleFragment frag;
	public InputStream in;
	public PrintStream out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		frag = new ConsoleFragment(this, new Runnable() {
			@Override
			public void run() {
				ConsoleActivity.this.onBegin();
			}
		});
		getVFragmentManager().add(android.R.id.content, frag.TAG, frag);
		in = frag.in;
		out = frag.out;
    }
	public void injectToSystem() {
		frag.injectToSystem();
	}
	public void clearScreen() {
		frag.clearScreen();
	}
	public void disableRead() {
		frag.disableRead();
	}
	public void enableRead() {
		frag.enableRead();
	}
	public void setPrompt(String prompt) {
		frag.setPrompt(prompt);
	}
	public String getPrompt() {
		return frag.getPrompt();
	}
	public void moveToEnd() {
		frag.moveToEnd();
	}
	public void setForegroundColor(int color) {
		frag.setForegroundColor(color);
	}
	public void setBackgroundColor(int color) {
		frag.setBackgroundColor(color);
	}
	public abstract void onBegin();
}
