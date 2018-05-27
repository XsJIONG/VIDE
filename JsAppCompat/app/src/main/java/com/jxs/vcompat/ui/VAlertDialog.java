package com.jxs.vcompat.ui;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.jxs.vcompat.widget.*;

public class VAlertDialog implements UI.OnThemeChangeListener {
	public static final String THEME_FULL_COLOR="VAlertDialog_FULLCOLOR";
	private Context cx;
	private AlertDialog dialog;
	private VEditText edit;
	private TextView Message;
	private VScrollView SC;
	public AlertDialog getDialog() {
		return dialog;
	}
	public VAlertDialog(Context cx) {
		this.cx = cx;
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					dialog = new AlertDialog.Builder(VAlertDialog.this.cx).create();
					dialog.setCancelable(false);
				}
			});
	}
	public VAlertDialog setMessage(final CharSequence s) {
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					if (Message == null) {
						Message = new TextView(cx);
						Message.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
						Message.setPadding(UI.dp2px(24), UI.dp2px(18), UI.dp2px(24), 0);
						Message.setText(s);
						Message.setTextColor(Color.BLACK);
						VScrollView SC=new VScrollView(cx);
						SC.setFillViewport(true);
						SC.addView(Message, -1, -2);
						setView(SC);
					} else {
						Message.setText(s);
						setView(SC);
					}
				}
			});
		return this;
	}
	public VAlertDialog setItems(final CharSequence[] cs, final OnClickListener listener) {
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					setItems(cs, true, listener);
				}
			}
		);
		return this;
	}
	public VAlertDialog setTitle(final CharSequence s) {
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					title = s;
					dialog.setTitle(s);
				}
			});
		return this;
	}
	public VAlertDialog setItems(final CharSequence[] cs, final boolean autoClose, final OnClickListener listener) {
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					VListView list=new VListView(cx);
					ArrayAdapter<CharSequence> ada=new ArrayAdapter<CharSequence>(cx, android.R.layout.simple_list_item_1, cs);
					list.setAdapter(ada);
					list.setDivider(null);
					list.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> pa, View v, int pos, long id) {
								if (listener != null) listener.onClick(VAlertDialog.this, pos);
								if (autoClose) dismiss();
							}
						});
					setView(list);
				}
			});
		return this;
	}
	public VAlertDialog setEditHint(final CharSequence hint) {
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					checkEdit();
					edit.setHint(hint);
				}
			});
		return this;
	}
	public VAlertDialog setView(final View v) {
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					dialog.setView(v);
				}
			});
		return this;
	}
	public VAlertDialog setEditTextColor(final int color) {
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					checkEdit();
					edit.setTextColor(color);
				}
			});
		return this;
	}
	@Override
	public void onThemeChange(final String key) {
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					if (key.equals(UI.THEME_UI_COLOR) && isShowing()) {
						if (PositiveSetted) dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(UI.getThemeColor());
						if (NegativeSetted) dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(UI.getThemeColor());
						if (NeutralSetted) dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(UI.getThemeColor());
					}
				}
			});
	}
	public VAlertDialog setNegativeButton(final String s, final OnClickListener lis) {
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					setNegativeButton(UI.getColorString(s, UI.getThemeColor()), lis);
					mButtonString[1] = true;
				}
			});
		return this;
	}
	public VAlertDialog setNeutralButton(final String s, final OnClickListener lis) {
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					setNeutralButton(UI.getColorString(s, UI.getThemeColor()), lis);
					mButtonString[2] = true;
				}
			});
		return this;
	}
	private boolean[] mButtonString={false, false, false};
	private void checkEdit() {
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					if (edit == null) {
						edit = new VEditText(cx);
						dialog.setView(edit);
					}
				}
			});
	}
	public VAlertDialog setCancelable(final boolean flag) {
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					dialog.setCanceledOnTouchOutside(flag);
				}
			});
		return this;
	}
	public VAlertDialog setEdit(final CharSequence text) {
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					checkEdit();
					edit.setText(text);
				}
			});
		return this;
	}
	public VEditText getEditText() {
		return edit;
	}
	public String getInputText() {
		return edit == null ?null: edit.getText().toString();
	}
	public static final String THEME_FULL_COLOR_VALUE="VAlertDialog_FullColorValue";
	private CharSequence title;
	public void show() {
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					dialog.show();
					if (PositiveSetted) {dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new android.view.View.OnClickListener() {
								@Override
								public void onClick(View v) {
									if (PositiveListener != null) PositiveListener.onClick(VAlertDialog.this, -1);
									if (PositiveClose) dismiss();
								}
							});
						dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(UI.getThemeColor());
					}
					if (NegativeSetted) {
						dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new android.view.View.OnClickListener() {
								@Override
								public void onClick(View v) {
									if (NegativeListener != null) NegativeListener.onClick(VAlertDialog.this, -1);
									if (NegativeClose) dismiss();
								}
							});
						dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(UI.getThemeColor());
					}
					if (NeutralSetted) {
						dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new android.view.View.OnClickListener() {
								@Override
								public void onClick(View v) {
									if (NeutralListener != null) NeutralListener.onClick(VAlertDialog.this, -1);
									if (NeutralClose) dismiss();
								}
							});
						dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(UI.getThemeColor());
					}
					if (fullColor || UI.getThemeBooleanData(THEME_FULL_COLOR, false)) {
						int back=UI.getThemeIntData(THEME_FULL_COLOR_VALUE, UI.getThemeColor());
						dialog.getWindow().setBackgroundDrawable(new ColorDrawable(back));
						if (title instanceof String) dialog.setTitle(UI.getColorString(title, ColorUtil.getBlackOrWhite(back)));
						if (!PositiveColored) dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ColorUtil.getBlackOrWhite(back));
						if (!NegativeColored) dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ColorUtil.getBlackOrWhite(back));
						if (!NeutralColored) dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(ColorUtil.getBlackOrWhite(back));
					}
					UI.registThemeChangedListener(VAlertDialog.this, VAlertDialog.this);
				}
			});
	}
	public VAlertDialog setOnCancelListener(final OnCancelListener lis) {
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialogs) {
								if (lis != null) lis.onCancel(VAlertDialog.this);
								UI.removeThemeChangedListener(this);
							}
						});
				}
			});
		return this;
	}
	public void setPositiveButtonCloseOnClick(boolean flag) {
		PositiveClose = flag;
	}
	public void setNegativeButtonCloseOnClick(boolean flag) {
		NegativeClose = flag;
	}
	public void setNeutralButtonCloseOnClick(boolean flag) {
		NeutralClose = flag;
	}
	public VAlertDialog dismiss() {
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					if (dialog.isShowing()) {
						dialog.dismiss();
						UI.removeThemeChangedListener(this);
					}
				}
			});
		return this;
	}
	public VAlertDialog setPositiveButton(CharSequence s, OnClickListener li) {
		return setPositiveButton(s, true, li);
	}
	public VAlertDialog setPositiveButton(final CharSequence s, final boolean closeOnClick, final OnClickListener li) {
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					if (!(s instanceof String)) PositiveColored = true;
					mButtonString[0] = false;
					dialog.setButton(AlertDialog.BUTTON_POSITIVE, s, new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int pos) {}});
					PositiveListener = li;
					PositiveSetted = true;
					PositiveClose = closeOnClick;
				}
			});
		return this;
	}
	private boolean fullColor=false;
	public void setFullColor(final boolean fc) {
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					fullColor = fc;
					if (edit != null) edit.setUnderLineColor(ColorUtil.getBlackOrWhite(UI.getThemeIntData(THEME_FULL_COLOR_VALUE, UI.getThemeColor())));
				}
			});
	}
	public VAlertDialog setNegativeButton(CharSequence s, OnClickListener li) {
		return setNegativeButton(s, true, li);
	}
	private OnClickListener PositiveListener=null,NegativeListener=null,NeutralListener=null;
	private boolean PositiveColored=false, NegativeColored=false, NeutralColored=false;
	public VAlertDialog setNegativeButton(final CharSequence s, final boolean closeOnClick, final OnClickListener li) {
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					if (!(s instanceof String)) NegativeColored = true;
					mButtonString[1] = false;
					dialog.setButton(AlertDialog.BUTTON_NEGATIVE, s, new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int pos) {}});
					NegativeListener = li;
					NegativeSetted = true;
					NegativeClose = closeOnClick;
				}
			});
		return this;
	}
	private boolean PositiveSetted=false,NegativeSetted=false,NeutralSetted=false,PositiveClose=true,NegativeClose=true,NeutralClose=true;
	public boolean isShowing() {
		return dialog.isShowing();
	}
	public VAlertDialog setNeutralButton(CharSequence s, OnClickListener li) {
		return setNeutralButton(s, true, li);
	}
	public VAlertDialog setNeutralButton(final CharSequence s, final boolean closeOnClick, final OnClickListener li) {
		UI.autoOnUi(cx, new Runnable() {
				@Override
				public void run() {
					if (!(s instanceof String)) NeutralColored = true;
					mButtonString[2] = false;
					dialog.setButton(AlertDialog.BUTTON_NEUTRAL, s, new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int pos) {}});
					NeutralListener = li;
					NeutralSetted = true;
					NeutralClose = closeOnClick;
				}
			});
		return this;
	}
	public static interface OnClickListener {
		void onClick(VAlertDialog dialog, int pos)
	}
	public static interface OnCancelListener {
		void onCancel(VAlertDialog dialog)
	}
	public VAlertDialog putToUi(Object key) {
		UI.putInfo(key, this);
		return this;
	}
	public static void removeFromUi(Object key) {
		UI.removeInfo(key);
	}
	public static VAlertDialog getFromUi(Object key) {
		return (VAlertDialog) UI.getInfo(key);
	}
}
