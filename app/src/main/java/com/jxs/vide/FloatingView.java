package com.jxs.vide;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class FloatingView {
	private WindowManager _WM;
	private WindowManager.LayoutParams _Para;
	private View v;
	private boolean showing=false;
	private Runnable onShow,onHide;
	public FloatingView(View v) {
		if (v == null) throw new IllegalArgumentException();
		this.v = v;
		init();
	}
	public Context getContext() {
		return v.getContext();
	}
	public View getView() {
		return v;
	}
	public void setView(View v) {
		if (v == null) throw new IllegalArgumentException();
		this.v = v;
	}
	private void init() {
		_WM = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		_Para = new WindowManager.LayoutParams();
		_Para.format = PixelFormat.RGBA_8888;
		_Para.flags = _Para.FLAG_NOT_FOCUSABLE;
		_Para.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		_Para.gravity = Gravity.TOP | Gravity.LEFT;
		_Para.width = -2;
		_Para.height = -2;
	}
	public WindowManager getWindowManager() {
		return _WM;
	}
	public WindowManager.LayoutParams getLayoutParams() {
		return _Para;
	}
	private boolean checkOverlayPermission() {
		if (Build.VERSION.SDK_INT >= 23) {
			if (Settings.canDrawOverlays(getContext())) return true;
			getContext().startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
			Toast.makeText(getContext(), L.get(L.PlzEnableOverlay), Toast.LENGTH_LONG).show();
			return false;
		} else return true;
	}
	public boolean isShowing() {
		return showing;
	}
	public boolean show() {
		if (showing) return false;
		if (!checkOverlayPermission()) return false;
		if (v == null) throw new RuntimeException("Dont use reflect!");
		_WM.addView(v, _Para);
		showing = true;
		if (onShow != null) onShow.run();
		return true;
	}
	public boolean hide() {
		if (!showing) return false;
		if (v == null) throw new RuntimeException("Dont use reflect!");
		try {
			_WM.removeView(v);
		} catch (Throwable e) {return false;}
		showing = false;
		if (onHide != null) onHide.run();
		return true;
	}
	public void setOnShowAction(Runnable action) {
		this.onShow = action;
	}
	public void setOnHideAction(Runnable action) {
		this.onHide = action;
	}
	public Runnable getOnShowAction() {
		return onShow;
	}
	public Runnable getOnHideAction() {
		return onHide;
	}
}
