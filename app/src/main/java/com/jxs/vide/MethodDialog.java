package com.jxs.vide;

import android.content.*;
import com.jxs.vcompat.ui.*;

public class MethodDialog implements MethodFragment.ChooseListener {
	private VAlertDialog Dialog;
	private MethodFragment Fragment;
	private ChooseListener Listener;
	public MethodDialog(Context cx) {
		Dialog = new VAlertDialog(cx);
		Fragment = new MethodFragment(cx);
		Fragment.setChooseListener(this);
		Dialog.setView(Fragment.getView()).setCancelable(true);
	}
	public void setCurrent(CNode c) {
		Fragment.setCurrent(c);
	}
	public void setLastText(CharSequence cs) {
		Fragment.setLastText(cs);
	}
	public void setChoosePkgText(CharSequence cs) {
		Fragment.setChoosePkgText(cs);
	}
	public CharSequence getLastText() {
		return Fragment.getLastText();
	}
	public CharSequence getChoosePkgText() {
		return Fragment.getChoosePkgText();
	}
	public void backToPre() {
		Fragment.backToPre();
	}
	public CNode getCurrent() {
		return Fragment.getCurrent();
	}
	public boolean isChoosePkg() {
		return Fragment.isChoosePkg();
	}
	public void setChoosePkg(boolean b) {
		Fragment.setChoosePkg(b);
	}
	public MethodDialog show() {
		Dialog.show();
		return this;
	}
	
	public MethodDialog dismiss() {
		Dialog.dismiss();
		return this;
	}
	public VAlertDialog getDialog() {
		return Dialog;
	}
	public MethodFragment getFragment() {
		return Fragment;
	}
	public void setChooseListener(ChooseListener l) {
		this.Listener = l;
	}
	public ChooseListener getChooseListener() {
		return this.Listener;
	}
	@Override
	public void onChoose(CNode c) {
		Dialog.dismiss();
		if (Listener != null) Listener.onChoose(c);
	}
	public static interface ChooseListener {
		public void onChoose(CNode c)
	}
}
