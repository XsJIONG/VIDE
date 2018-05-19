package com.jxs.vapp.program;

import java.util.ArrayList;
import org.mozilla.javascript.Context;

public class Console {
	private static Console _Instance=null;
	private Console() {}
	private ArrayList<UpdateListener> Listener=new ArrayList<>();
	public static Console getInstance() {
		if (_Instance == null) _Instance = new Console();
		return _Instance;
	}
	private StringBuffer Text=new StringBuffer();
	public void log(Object obj) {
		log("Script", obj);
	}
	public void log(String tag, Object obj) {
		Text.append(String.format("<%s> %s\n", tag, Context.toString(obj)));
		for (UpdateListener one : Listener) one.onUpdate(this);
	}
	public void cls() {
		Text.setLength(0);
		for (UpdateListener one : Listener) one.onUpdate(this);
	}
	public String getText() {
		return Text.toString();
	}
	public void addUpdateListener(UpdateListener listener) {
		this.Listener.add(listener);
	}
	public void removeUpdateListener(UpdateListener listener) {
		this.Listener.remove(listener);
	}
	public static interface UpdateListener {
		void onUpdate(Console c)
	}
}
