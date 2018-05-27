package com.jxs.vide;

import android.content.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.support.v7.app.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import com.android.permission.rom.*;
import com.jxs.vcompat.activity.*;
import com.jxs.vcompat.fragment.*;
import com.jxs.vcompat.ui.*;
import com.jxs.vide.lang.*;
import java.lang.reflect.*;

import static com.jxs.vide.L.get;

public class SettingActivity extends VActivity {
	public static final int GROUP_SPACE=UI.dp2px(35);
	public static SettingActivity cx;
	private SettingFragment Q;
	private SettingFragment.SwitchItem VButtonItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		cx = this;
		super.onCreate(savedInstanceState);
		setTitleText(get(L.Title_Setting));
		enableBackButton();
		int dp=ui.dp2px(18);
		initSetting();
		Q.onAttach();
		Q.getView().setPadding(dp, 0, dp, 0);
		setContentView(Q.getView());
	}
	private void initSetting() {
		Q = new SettingFragment(this);
		Q.addGroup(get(L.Setting_GUI));
		Q.addSimpleItem(get(L.Setting_ThemeColor), get(L.Setting_ThemeColor_Des)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setThemeColor();
				}
			});
		Q.addSimpleItem(get(L.Setting_SplashTime), get(L.Setting_SplashTime_Des)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setSplashTime();
				}
			});
		Q.addSimpleItem(get(L.Setting_Language), get(L.Setting_Language_Des)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setLanguage();
				}
			});
		Q.addSpace(GROUP_SPACE);
		Q.addGroup(get(L.Setting_Extra));
		VButtonItem = Q.addSwitchItem(get(L.ShowVButton), get(L.ShowVButton_Subtitle));
		VButtonItem.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					SettingFragment.SwitchItem item=(SettingFragment.SwitchItem) v;
					if (item.isChecked()) {
						if (!checkOverlayPermission(SettingActivity.this)) {
							item.setChecked(false);
							ui.print(get(L.PlzEnableOverlay));
							applyOverlayPermission(SettingActivity.this);
							return;
						}
						Global.setShowVButton(true);
						startService(new Intent(SettingActivity.this, VButtonService.class));
					} else {
						Global.setShowVButton(false);
						VButton._hide();
					}
				}
			});
		VButtonItem.setChecked(Global.isShowVButton());
		Q.addSwitchItem(get(L.EnableVIDELog)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Global.setShowVIDELog(((SettingFragment.SwitchItem) v).isChecked());
				}
			});
		Q.addSimpleItem(get(L.ShareVIDE), get(L.Nothing)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MainActivity.shareMe(SettingActivity.this);
				}
			});
	}
	public static void setVButtonItemChecked(boolean c) {
		if (cx == null) return;
		cx.VButtonItem.setChecked(c);
	}
	private void setSplashTime() {
		VAlertDialog d=ui.newAlertDialog().setTitle(get(L.Setting_SplashTime)).setEditHint(get(L.Setting_SplashTime_Hint)).setEdit(String.valueOf(Global.getSplashTime())).setCancelable(true).setNegativeButton(get(L.Cancel), null).setPositiveButton(get(L.OK), false, new VAlertDialog.OnClickListener() {
				@Override
				public void onClick(VAlertDialog dialog, int pos) {
					long res;
					String s=dialog.getEditText().getText().toString();
					if (s == null || s.length() == 0) res = 0; else {
						try {
							res = Long.parseLong(s);
						} catch (NumberFormatException e) {
							dialog.getEditText().setError(get(L.IllegalNumber));
							return;
						}
					}
					if (res < 0) {
						dialog.getEditText().setText(get(L.SplashTimeCantNegative));
						return;
					}
					Global.setSplashTime(res);
					dialog.dismiss();
					ui.print(get(L.Setted));
				}
			});
		d.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
		d.getEditText().setSelection(d.getEditText().getText().length());
		d.show();
	}
	public void setThemeColor() {
		new com.jxs.vcompat.ui.ColorSelector(this).setColor(UI.getThemeColor()).setPositiveButton(get(L.OK), new VAlertDialog.OnClickListener() {
				@Override
				public void onClick(VAlertDialog dialog, int pos) {
					UI.setThemeColor(((ColorSelector) dialog).getColor());
					ui.print(get(L.Setted));
				}
			}).setNegativeButton(get(L.Cancel), null).setCancelable(true).show();
	}
	private int selectedLanguage=-1;
	public void setLanguage() {
		AlertDialog.Builder b=new AlertDialog.Builder(this);
		b.setTitle(get(L.Setting_Language));
		String[] all=new String[Lang.Langs.length];
		int index=-1;
		try {
			for (int i=0;i < all.length;i++) {
				all[i] = Lang.Langs[i].newInstance().getName();
				if (Lang.Langs[i].isInstance(Global.Language)) index = selectedLanguage = i;
			}
		} catch (Throwable t) {
			err(t, true);
			return;
		}
		if (index == -1) return;
		selectedLanguage = -1;
		b.setSingleChoiceItems(all, index, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int pos) {
					selectedLanguage = pos;
				}
			});
		b.setPositiveButton(get(L.OK), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int pos) {
					Lang.setLanguage(Lang.Langs[selectedLanguage]);
					Global.setLanguage(selectedLanguage);
					killAll();
					Intent i=getPackageManager().getLaunchIntentForPackage(getPackageName());
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}
			}).setNegativeButton(get(L.Cancel), null).setCancelable(true).show();
	}
	public static final String TAG="VIDE";
	public final boolean checkOverlayPermission(Context context) {
		if (Build.VERSION.SDK_INT < 23) {
			if (RomUtils.checkIsMiuiRom())
				return MiuiUtils.checkFloatWindowPermission(context);
			else if (RomUtils.checkIsMeizuRom())
				return MeizuUtils.checkFloatWindowPermission(context);
			else if (RomUtils.checkIsHuaweiRom())
				return HuaweiUtils.checkFloatWindowPermission(context);
			else if (RomUtils.checkIs360Rom())
				return QikuUtils.checkFloatWindowPermission(context);
			else if (RomUtils.checkIsOppoRom())
				return OppoUtils.checkFloatWindowPermission(context);
		}
		if (RomUtils.checkIsMeizuRom()) {
			return MeizuUtils.checkFloatWindowPermission(context);
		} else {
			Boolean result = true;
			if (Build.VERSION.SDK_INT >= 23) {
				try {
					Class clazz = Settings.class;
					Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
					result = (Boolean) canDrawOverlays.invoke(null, context);
				} catch (Exception e) {
					err(e, true);
				}
			}
			return result;
		}
	}
	public final void applyOverlayPermission(Context context) {
		if (Build.VERSION.SDK_INT < 23) {
			if (RomUtils.checkIsMiuiRom())
				MiuiUtils.applyMiuiPermission(context);
			else if (RomUtils.checkIsMeizuRom())
				MeizuUtils.applyPermission(context);
			else if (RomUtils.checkIsHuaweiRom())
				HuaweiUtils.applyPermission(context);
			else if (RomUtils.checkIs360Rom())
				QikuUtils.applyPermission(context);
			else if (RomUtils.checkIsOppoRom())
				OppoUtils.applyOppoPermission(context);
		}
		if (RomUtils.checkIsMeizuRom()) {
			MeizuUtils.applyPermission(context);
		} else {
			if (Build.VERSION.SDK_INT >= 23) {
				try {
					Class clazz = Settings.class;
					Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");
					Intent intent = new Intent(field.get(null).toString());
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setData(Uri.parse("package:" + context.getPackageName()));
					context.startActivity(intent);
				} catch (Exception e) {
					err(e, true);
				}
			}
		}
	}
}
