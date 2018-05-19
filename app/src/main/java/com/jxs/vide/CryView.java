package com.jxs.vide;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jxs.vcompat.ui.UI;

public class CryView extends LinearLayout implements UI.OnThemeChangeListener {
	public CryView(Context cx) {
		super(cx);
		init();
	}
	public CryView(Context cx, AttributeSet attr) {
		super(cx, attr);
		init();
	}
	public CryView(Context cx, AttributeSet attr, int defStyle) {
		super(cx, attr, defStyle);
		init();
	}
	private TextView _Face;
	private TextView _Text;
	private void init() {
		setGravity(Gravity.CENTER);
		setOrientation(VERTICAL);
		_Face = new TextView(getContext());
		_Face.setTextColor(UI.getThemeColor());
		_Face.setText(":(");
		_Face.setTextSize(33);
		_Face.setGravity(Gravity.CENTER);
		addView(_Face);
		_Text = new TextView(getContext());
		_Text.setText(L.get(L.HereIsEmpty));
		_Text.setTextColor(UI.getThemeColor());
		_Text.setTextSize(12f);
		LinearLayout.LayoutParams para=new LinearLayout.LayoutParams(-2, -2);
		para.topMargin = UI.dp2px(15);
		addView(_Text, para);
	}
	public void setText(CharSequence s) {
		_Text.setText(s);
	}
	@Override
	public void onThemeChange(String key) {
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		_Face.setTextColor(UI.getThemeColor());
		_Text.setTextColor(UI.getThemeColor());
	}
}
