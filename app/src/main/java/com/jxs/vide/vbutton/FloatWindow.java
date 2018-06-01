package com.jxs.vide.vbutton;

import android.content.*;
import android.graphics.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.jxs.vide.*;

public class FloatWindow {
	private FloatWindow() {}
	private static FloatWindow INSTANCE=null;
	public static FloatWindow getInstance() {
		if (INSTANCE == null) INSTANCE = new FloatWindow();
		return INSTANCE;
	}
	private Context cx;
	private WindowManager _WindowManager;
	private WindowManager.LayoutParams _Params;
	private Button _Button;
	public void show(Context ctx) {
		this.cx = ctx;
		_WindowManager = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
		_Params = new WindowManager.LayoutParams();
		_Params.type = WindowManager.LayoutParams.TYPE_PHONE;
		_Params.format = PixelFormat.RGBA_8888;
		_Params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		_Params.gravity = Gravity.TOP | Gravity.LEFT;
		_Params.x = 0;
		_Params.y = 300;
		_Params.width = -2;
		_Params.height = -2;
		_Button = new Button(cx);
		_Button.setText("Continue");
		_Button.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				_Params.x = (int) event.getRawX() - _Button.getMeasuredWidth() / 2;
				_Params.y = (int) event.getRawY() - _Button.getMeasuredHeight() / 2 - getStatusBarHeight(cx);
				_WindowManager.updateViewLayout(_Button, _Params);
				return false;
			}
		});
		_Button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Global.Breaking = false;
				Global.LOCK.notifyAll();
			}
		});
		_WindowManager.addView(_Button, _Params);
	}
	private static int getStatusBarHeight(Context cx) {
		int result = 0;
		int resourceId = cx.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = cx.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
}
