package com.jxs.v.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class VProgressDialog {
	Context cx;
	ProgressDialog dialog;
	public VProgressDialog(Context cx) {
		this.cx = cx;
		UI.post(cx, new Runnable() {
			@Override
			public void run() {
				dialog = new ProgressDialog(VProgressDialog.this.cx);
			}
		});
	}
	public ProgressDialog getDialog() {
		return dialog;
	}
	public VProgressDialog setTitle(final CharSequence s) {
		UI.post(cx, new Runnable() {
			@Override
			public void run() {
				dialog.setTitle(s);
			}
		});
		return this;
	}
	public VProgressDialog setProgress(final int pro) {
		UI.post(cx, new Runnable() {
			@Override
			public void run() {
				dialog.setProgress(pro);
			}
		});
		return this;
	}
	public VProgressDialog setMessage(final CharSequence s) {
		UI.post(cx, new Runnable() {
			@Override
			public void run() {
				dialog.setMessage(s);
			}
		});
		return this;
	}
	public VProgressDialog setCancelable(final boolean flag) {
		UI.post(cx, new Runnable() {
			@Override
			public void run() {
				dialog.setCancelable(flag);
			}
		});
		return this;
	}
	public VProgressDialog setButton(final CharSequence text, final DialogInterface.OnClickListener on) {
		UI.post(cx, new Runnable() {
			@Override
			public void run() {
				dialog.setButton(text, on);
			}
		});
		return this;
	}
	public VProgressDialog dismiss() {
		UI.post(cx, new Runnable() {
			@Override
			public void run() {
				dialog.dismiss();
			}
		});
		return this;
	}
	public ProgressDialog show() {
		UI.post(cx, new Runnable() {
			@Override
			public void run() {
				dialog.show();
			}
		});
		return dialog;
	}
	public VProgressDialog putToUi(Object key) {
		UI.putInfo(key, this);
		return this;
	}
	public static void removeFromUi(Object key) {
		UI.removeInfo(key);
	}
	public static VProgressDialog getFromUi(Object key) {
		return (VProgressDialog) UI.getInfo(key);
	}
}
