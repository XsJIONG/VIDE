package com.jxs.v.fragment;

import android.content.Context;
import android.view.View;

public abstract class VFragment {
	Context cx;
	public VFragment(Context cx) {
		this.cx = cx;
	}
	public abstract View getView();
	public abstract Object getTag();
	public void onAttach() {}
	public void onDettach() {}
	public Context getContext() {
		return cx;
	}
}
