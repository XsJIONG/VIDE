package com.jxs.vcompat.ui;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v4.graphics.drawable.*;
import android.text.*;
import android.text.style.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;
import java.util.*;

public class UI {
	public static final String THEME_UI_COLOR="UI_ThemeColor";
	private Context cx;
	public UI(Context cx) {
		this.cx = cx;
	}
	private Toast showingToast=null;
	public void toast(final CharSequence cs) {
		post(new Runnable() {
				@Override
				public void run() {
					if (showingToast == null) showingToast = Toast.makeText(cx, cs, Toast.LENGTH_SHORT); else {
						showingToast.setText(cs);
						showingToast.setDuration(Toast.LENGTH_SHORT);
					}
					showingToast.show();
				}
			});
	}
	public Context getContext() {
		return cx;
	}
	public static int getAccentColor() {
		return AccentColor;
	}
	public static boolean supportElevation() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
	}
	public Drawable tintDrawable(int res, int color) {
		Drawable d=DrawableCompat.wrap(cx.getResources().getDrawable(res).mutate());
		DrawableCompat.setTint(d, color);
		return d;
	}
	public ColorStateList toColorStateList(int color) {
		return ColorStateList.valueOf(color);
	}
	public static FileFilter DEFAULT_FILTER=new FileFilter() {
		@Override
		public boolean accept(File f) {
			return true;
		}
	};
	public static Drawable tintDrawable(Drawable d, int color) {
		d = DrawableCompat.wrap(d);
		DrawableCompat.setTint(d, color);
		return d;
	}
	private static Bundle mThemeData=new Bundle();
	public static void putThemeData(String key, boolean b) {
		mThemeData.putBoolean(key, b);
	}
	public static void putThemeData(String key, int i) {
		mThemeData.putInt(key, i);
	}
	public static void putThemeData(String key, short i) {
		mThemeData.putShort(key, i);
	}
	public static void putThemeData(String key, long i) {
		mThemeData.putLong(key, i);
	}
	public static void putThemeData(String key, double i) {
		mThemeData.putDouble(key, i);
	}
	public static void putThemeData(String key, float i) {
		mThemeData.putFloat(key, i);
	}
	public static void putThemeData(String key, char i) {
		mThemeData.putChar(key, i);
	}
	public static void putThemeData(String key, byte i) {
		mThemeData.putByte(key, i);
	}
	private static HashMap<String, Object> mOtherThemeData=new HashMap<>();
	public static void putThemeData(String key, Object obj) {
		mOtherThemeData.put(key, obj);
	}
	public void print(final CharSequence c) {
		post(new Runnable() {
				@Override
				public void run() {
					Snackbar.make(getActivity().findViewById(android.R.id.content), c, Snackbar.LENGTH_SHORT).show();
				}
			});
	}
	public void print(final CharSequence c, final CharSequence action, final int color, final OnClickListener listener) {
		post(new Runnable() {
				@Override
				public void run() {
					Snackbar.make(getActivity().findViewById(android.R.id.content), c, Snackbar.LENGTH_SHORT).setAction(action, listener == null ?new OnClickListener() {
																															@Override
																															public void onClick(View v) {}
																														}: listener).setActionTextColor(color).show();
				}
			});
	}
	public void post(Runnable r) {
		post(cx, r);
	}
	public static void post(Context cx, Runnable r) {
		if (shouldOnUi(cx)) onUi(cx, r); else r.run();
	}
	public boolean shouldOnUi() {
		return shouldOnUi(cx);
	}
	private static boolean shouldOnUi(Context cx) {
		return AutoUi && !(inUiThread(cx));
	}
	public static int getThemeIntData(String key, int def) {
		return mThemeData.getInt(key, def);
	}
	public static byte getThemeByteData(String key, byte def) {
		return mThemeData.getByte(key, def);
	}
	public static short getThemeShortData(String key, short def) {
		return mThemeData.getShort(key, def);
	}
	public static String getThemeStringData(String key, String def) {
		return mThemeData.getString(key, def);
	}
	public static void putThemeData(String key, String value) {
		mThemeData.putString(key, value);
	}
	public static long getThemeLongData(String key, long def) {
		return mThemeData.getLong(key, def);
	}
	public static double getThemeDoubleData(String key, double def) {
		return mThemeData.getDouble(key, def);
	}
	public static float getThemeFloatData(String key, float def) {
		return mThemeData.getFloat(key, def);
	}
	public static boolean getThemeBooleanData(String key, boolean def) {
		return mThemeData.getBoolean(key, def);
	}
	public static char getThemeCharData(String key, char def) {
		return mThemeData.getChar(key, def);
	}
	public static Object getThemeObjectData(String key, Object def) {
		return (mOtherThemeData.containsKey(key)) ?mOtherThemeData.get(key): def;
	}
	public FileChooserDialog newFileChooserDialog(File f, FileChooserDialog.FileChooserListener listener) {
		return new FileChooserDialog(cx, f, listener);
	}
	public static interface ChooseFileListener {
		void onChoose(File f)
	}
	public FileChooserDialog newFileChooserDialog(File f, FileChooserDialog.FileChooserListener listener, boolean chooseDir) {
		return new FileChooserDialog(cx, f, listener, chooseDir);
	}
	public File getLastFile(File def) {
		String p=cx.getSharedPreferences("UI", Context.MODE_PRIVATE).getString("lastFile", null);
		if (p == null || p.length() == 0) return def; else return new File(p);
	}
	public Activity getActivity() {
		return (Activity) cx;
	}
	public static void removeThemeChangedListener(Object key) {
		mThemeChangeListeners.remove(key);
	}
	private static HashMap<Object,OnThemeChangeListener> mThemeChangeListeners=new HashMap<>();
	public static interface OnThemeChangeListener {
		void onThemeChange(String key)
	}
	public void autoOnUi(Runnable r) {
		autoOnUi(cx, r);
	}
	public static void autoOnUi(Context cx, Runnable r) {
		if (inUiThread(cx)) r.run(); else onUi(cx, r);
	}
	private static int AccentColor=0xFFFFFFFF;
	public static void setThemeColor(int color) {
		putThemeData(THEME_UI_COLOR, color);
		AccentColor = ColorUtil.getBlackOrWhite(color);
		for (OnThemeChangeListener one : mThemeChangeListeners.values()) one.onThemeChange(THEME_UI_COLOR);
	}
	public static int getThemeColor() {
		return getThemeIntData(THEME_UI_COLOR, 0xFFFF5500);
	}
	public static final String THEME_DEFAULT_POSITIVE_TEXT="UI.DefaultPositiveText";
	public void alert(final CharSequence title, final CharSequence msg) {
		newAlertDialog().setTitle(title).setMessage(msg).setPositiveButton(UI.getThemeStringData(THEME_DEFAULT_POSITIVE_TEXT, "确定"), null).setCancelable(true).show();
	}
	public static void registThemeChangedListener(Object key, OnThemeChangeListener listener) {
		mThemeChangeListeners.put(key, listener);
	}
	public VProgressDialog newProgressDialog() {
		return new VProgressDialog(cx);
	}
	public VAlertDialog newAlertDialog() {
		return new VAlertDialog(cx);
	}
	public static SpannableString getColorString(CharSequence cs, int color) {
		SpannableString str=null;
		str = new SpannableString(cs);
		str.setSpan(new ForegroundColorSpan(color), 0, str.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
		return str;
	}
	public static int dp2px(int dp) {
		return (int) Math.ceil(dp * Resources.getSystem().getDisplayMetrics().density);
	}
	public boolean inUiThread() {
		return inUiThread(cx);
	}
	public static boolean inUiThread(Context cx) {
		return cx.getMainLooper().getThread().getId() == Thread.currentThread().getId();
	}
	public View getDecorView() {
		if (!(cx instanceof Activity)) return null;
		return ((Activity) cx).getWindow().getDecorView();
	}
	private static boolean AutoUi=true;
	public static boolean isAutoUi() {return AutoUi;}
	public static void setAutoUi(boolean flag) {AutoUi = flag;}
	public void onUi(Runnable action) {
		new Handler(cx.getMainLooper()).post(action);
	}
	public static void onUi(Context cx, Runnable action) {
		new Handler(cx.getMainLooper()).post(action);
	}
	private static HashMap<Object,Object> InfoMap=new HashMap<>();
	public static void putInfo(Object key, Object info) {
		InfoMap.put(key, info);
	}
	public static Object getInfo(Object key) {
		return InfoMap.get(key);
	}
	public static Object removeInfo(Object key) {
		return InfoMap.remove(key);
	}
}
