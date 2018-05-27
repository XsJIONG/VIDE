package com.jxs.vide;

import android.content.Context;
import android.view.Display;
import com.jxs.vcompat.ui.UI;

public class VButton extends FloatingView implements UI.OnThemeChangeListener {
	public static final int SIZE=UI.dp2px(57);
	private static VButton _instance;
	private VButton(Context cx) {
		super(new FWView(cx));
		((FWView) getView()).setVButton(this);
		Display d=getWindowManager().getDefaultDisplay();
		int[] pos=Global.getVButtonPos();
		getLayoutParams().x = (pos[0] == -1 ?((d.getWidth() - SIZE) / 2): pos[0]);
		getLayoutParams().y = (pos[1] == -1 ?((d.getHeight() - SIZE) / 2): pos[1]);
		getLayoutParams().width = SIZE;
		getLayoutParams().height = SIZE;
	}
	public static VButton getInstance(Context cx) {
		if (_instance == null)
			_instance = new VButton(cx);
		return _instance;
	}
	public static boolean _isShowing() {
		if (_instance == null) return false;
		return _instance.isShowing();
	}
	public static boolean _show() {
		if (_instance == null) return false;
		return _instance.show();
	}
	public static boolean _hide() {
		if (_instance == null) return false;
		return _instance.hide();
	}
	@Override
	public void onThemeChange(String key) {
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		((FWView) getView()).onThemeChange(key);
	}
}
