package com.jxs.vide;

import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.jxs.vcompat.activity.*;
import com.jxs.vcompat.ui.*;
import com.jxs.vide.vbutton.*;

public class SplashActivity extends VActivity {
	private boolean isForeground;
	private boolean isPosted=false;
	private LinearLayout root;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		root = new LinearLayout(this);
		root.setGravity(Gravity.CENTER);
		ImageView Icon=new ImageView(this);
		Icon.setImageResource(R.drawable.v_icon);
		root.addView(Icon, new LinearLayout.LayoutParams(UI.dp2px(200), UI.dp2px(200)));
		setContentView(root);
		setStatusBarTransparent();
		Global.checkBmob(this);
		if (Global.isShowVButton())
			startService(new Intent(SplashActivity.this, VButtonService.class));
		root.setBackgroundColor(UI.getThemeColor());
		isForeground = true;
		post();
	}
	@Override
	protected void onPause() {
		super.onPause();
		isForeground = false;
	}
	@Override
	protected void onResume() {
		super.onResume();
		isForeground = true;
		if (isPosted) post();
	}
	private void post() {
		isPosted = false;
		new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					isPosted = true;
					if (!isForeground) return;
					startActivity(new Intent(SplashActivity.this, MainActivity.class));
					finish();
				}
			}, Global.getSplashTime());
	}
	@Override
	public void onThemeChange(String key) {
		super.onThemeChange(key);
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		if (isForeground) root.setBackgroundColor(UI.getThemeColor());
	}
}
