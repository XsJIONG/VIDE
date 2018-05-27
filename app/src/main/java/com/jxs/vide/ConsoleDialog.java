package com.jxs.vide;

import android.graphics.Color;
import android.widget.TextView;
import com.jxs.vapp.program.Console;
import com.jxs.vcompat.ui.UI;
import com.jxs.vcompat.widget.VScrollView;

public class ConsoleDialog extends FloatingWindow implements Console.UpdateListener {
	private VScrollView sc;
	private TextView Content;
	private static ConsoleDialog _Instance;
	public static ConsoleDialog getInstance() {
		if (_Instance == null) _Instance = new ConsoleDialog();
		return _Instance;
	}
	private ConsoleDialog() {
		super(L.get(L.ConsoleTitle));
		setBounds(UI.dp2px(100), UI.dp2px(100), UI.dp2px(250), UI.dp2px(200));
		Content = new TextView(getContext());
		Content.setTextColor(Color.WHITE);
		Content.setText(Console.getInstance().getText());
		Content.setBackgroundColor(Color.BLACK);
		sc = new VScrollView(getContext());
		sc.setFillViewport(true);
		sc.addView(Content, new VScrollView.LayoutParams(-1, -1));
		setView(sc);
		Console.getInstance().addUpdateListener(this);
	}
	@Override
	public void onThemeChange(String key) {
		super.onThemeChange(key);
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		sc.setGlowColor(UI.getThemeColor());
	}
	@Override
	public void onUpdate(Console c) {
		Content.setText(c.getText());
	}
}
