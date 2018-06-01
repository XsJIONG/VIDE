package com.jxs.vide;
import android.view.*;
import android.widget.*;
import com.jxs.vcompat.ui.*;
import com.jxs.vcompat.widget.*;
import com.jxs.vide.vbutton.*;

public class MessageDialog extends FloatingWindow {
	private String msg;
	private VScrollView sc;
	private TextView v;
	public static void showMessage(String title, String msg) {
		new MessageDialog(title, msg).show();
	}
	public MessageDialog(String title, String s) {
		super(title);
		Display d=getWindowManager().getDefaultDisplay();
		int width=UI.dp2px(250);
		int height=UI.dp2px(200);
		setBounds((d.getWidth() - width) / 2, (d.getHeight() - height) / 2, width, height);
		this.msg = s;
		sc = new VScrollView(getContext());
		sc.setFillViewport(true);
		v = new TextView(getContext());
		v.setText(msg);
		v.setTextColor(UI.getThemeColor());
		sc.addView(v, new VScrollView.LayoutParams(-1, -1));
		setView(sc);
	}
}
