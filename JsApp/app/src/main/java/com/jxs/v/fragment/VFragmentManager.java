package com.jxs.v.fragment;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashMap;

public class VFragmentManager {
	Activity ac;
	public VFragmentManager(Activity ac) {
		this.ac = ac;
	}
	public void add(int id, Object key, VFragment frag) {
		add((ViewGroup) ac.findViewById(id), key, frag);
	}
	public void add(ViewGroup group, Object key, final VFragment frag) {
		frags.put(key, frag);
		View v=frag.getView();
		v.post(new Runnable() {
				@Override
				public void run() {
					frag.onAttach();
				}
			});
		group.addView(v);
	}
	public void remove(Object key) {
		frags.remove(key).onDettach();
	}
	HashMap<Object, VFragment> frags=new HashMap<>();
	public void onAttach() {
		for (VFragment one : frags.values()) one.onAttach();
	}
	public void onDettach() {
		for (VFragment one : frags.values()) one.onDettach();
	}
}
