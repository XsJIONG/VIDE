package com.jxs.vcompat.activity;

import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v4.view.*;
import android.support.v7.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.jxs.vcompat.drawable.*;
import com.jxs.vcompat.fragment.*;
import com.jxs.vcompat.ui.*;
import java.util.*;

public class VActivity extends AppCompatActivity implements UI.OnThemeChangeListener {
	public static ArrayList<VActivity> Activities=new ArrayList<>();
	public UI ui;
	private boolean StatusT=false;
	VFragmentManager mFragmentManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Activities.add(this);
		ui = new UI(this);
		mFragmentManager = new VFragmentManager(this);
		super.onCreate(savedInstanceState);
		ui.registThemeChangedListener(this, this);
		setStatusBarColor(UI.getThemeColor());
		if (getSupportActionBar() != null) {
			setTitleElevation(10f);
			rSetTitleBackground(UI.getThemeColor());
			rSetTitleTextColor(UI.getAccentColor());
			BackDrawable = new ArrowShape(this, 5f).toSimpleDrawable();
			UI.tintDrawable(BackDrawable, UI.getAccentColor());
			((BDrawable) BackDrawable).setStrokeWidth(5f);
			getSupportActionBar().setHomeAsUpIndicator(BackDrawable);
			getSupportActionBar().setLogo(null);
			disableBackButton();
		}
	}
	public void setStatusBarTransparent() {
		StatusT = true;
		Window window=getWindow();
		window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
			View v=((ViewGroup) findViewById(Window.ID_ANDROID_CONTENT)).getChildAt(0);
			if (v != null) {
				ViewCompat.setFitsSystemWindows(v, false);
				ViewCompat.requestApplyInsets(v);
			}
		} else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			ViewGroup content=(ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
			View c=content.getChildAt(0);
			if (c != null) {
				if ("marginAdded".equals(c.getTag())) {
					FrameLayout.LayoutParams para=(FrameLayout.LayoutParams) c.getLayoutParams();
					para.topMargin -= getStatusBarHeight();
					c.setLayoutParams(para);
					c.setTag(null);
				}
				ViewCompat.setFitsSystemWindows(c, false);
			}
		}
	}
	public void setBackButtonColor(int color) {
		if (BackDrawable != null) BackDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
	}
	public void setStatusBarColor(int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(color);
		}
	}
	@Override
	public void onAttachedToWindow() {
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		super.onAttachedToWindow();
	}
	private Drawable BackDrawable;
	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	public void setBackDrawable(Drawable d) {
		this.BackDrawable = d;
		getSupportActionBar().setHomeAsUpIndicator(d);
	}
	public Drawable getBackDrawable() {
		return BackDrawable;
	}
	public VFragmentManager getVFragmentManager() {
		return mFragmentManager;
	}
	@Override
	public void onThemeChange(String key) {
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		if (getSupportActionBar() != null && !_TitleBackgroundSetted) rSetTitleBackground(UI.getThemeColor());
		if (getSupportActionBar() != null && !_TitleTextSetted) rSetTitleTextColor(UI.getAccentColor());
		if (!StatusT) setStatusBarColor(UI.getThemeColor());
		setBackButtonColor(UI.getAccentColor());
	}
	int titleColor=0xFF000000;
	int subTitleColor=0xFF555555;
	private boolean _TitleTextSetted=false;
	public void setTitleTextColor(int color) {
		_TitleTextSetted = true;
		rSetTitleTextColor(color);
	}
	private void rSetTitleTextColor(int color) {
		this.titleColor = color;
		setTitleText(getTitleText());
	}
	public void setTitleElevation(float elevation) {
		if (UI.supportElevation())
			getSupportActionBar().setElevation(elevation);
	}
	public void setTitleText(CharSequence title) {
		getSupportActionBar().setTitle(ui.getColorString(title, titleColor));
	}
	public void setSubTitleTextColor(int color) {
		subTitleColor = color;
		setSubTitle(getSupportActionBar().getSubtitle());
	}
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		ViewLoader.load(this);
	}
	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		ViewLoader.load(this);
	}
	public void setSubTitle(CharSequence title) {
		getSupportActionBar().setSubtitle(ui.getColorString(title, subTitleColor));
	}
	private boolean _TitleBackgroundSetted=false;
	public void setTitleBackground(int background) {
		_TitleBackgroundSetted = true;
		rSetTitleBackground(background);
	}
	private void rSetTitleBackground(int background) {
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(background));
	}
	public void enableBackButton() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	public void disableBackButton() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
	}
	public CharSequence getTitleText() {
		return getSupportActionBar().getTitle() == null ?getTitle(): getSupportActionBar().getTitle();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) finish();
		return super.onOptionsItemSelected(item);
	}
	private String TAG="V";
	public void setLogTag(String tag) {
		TAG = tag;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ui.removeThemeChangedListener(this);
		Activities.remove(this);
	}
	public String getLogTag() {
		return TAG;
	}
	public void err(Throwable t) {
		err(t, false);
	}
	public void err(final Throwable t, final boolean force) {
		ui.autoOnUi(new Runnable() {
				@Override
				public void run() {
					String config=Log.getStackTraceString(t);
					Log.e(TAG, config);
					ui.newAlertDialog()
						.setTitle("Error")
						.setMessage(config)
						.setPositiveButton("OK", force ?new VAlertDialog.OnClickListener() {
											   @Override
											   public void onClick(VAlertDialog dialog, int pos) {
												   finish();
											   }
										   }: null)
						.setCancelable(force ?false: true)
						.show();
				}
			});
	}
	public static void killAll() {
		for (VActivity one : Activities) one.finish();
	}
}
