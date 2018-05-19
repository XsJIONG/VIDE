package com.jxs.v.ui;

import android.content.Context;
import android.view.View;
import android.view.animation.Interpolator;
import java.util.ArrayList;

public abstract class VAnimation<T extends View> {
	private T view;
	public VAnimation(T v) {
		this.view = v;
	}
	public final T getView() {
		return view;
	}
	public abstract void start();
	public void pause() {}
	public void resume() {}
	public void stop() {}
	private Listener mListener=null;
	private boolean stopped=false;
	private boolean running=false;
	public void setListener(Listener listener) {
		mListener = listener;
	}
	public void setDuration(long mills) {}
	public void setInterpolator(Interpolator porlator) {}
	private ArrayList<Runnable> EndActions=new ArrayList<>();
	public void postAnimationEndAction(Runnable action) {
		if (!stopped) action.run(); else EndActions.add(action);
	}
	protected synchronized final void animationStart() {
		stopped = false;
		running = true;
		if (mListener != null) mListener.onStart();
	}
	protected synchronized final void animationStop() {
		stopped = true;
		running = false;
		for (Runnable one : EndActions) one.run();
		EndActions.clear();
		if (mListener != null) mListener.onStop();
	}
	protected synchronized final void animationPause() {
		running = false;
		if (mListener != null) mListener.onPause();
	}
	public boolean isStopped() {
		return stopped;
	}
	public Context getContext() {
		return view.getContext();
	}
	public boolean isRunning() {
		return running;
	}
	public static abstract class Listener {
		void onStart() {}
		void onStop() {}
		void onPause() {}
	}
}
