package com.jxs.vide;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;

public class PageLayout extends FrameLayout {
	public static final Object TAG_DONT_ENUM="DontEnum";
	private int curScreen=0;
	public PageLayout(Context cx) {
		super(cx);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}
	@Override
	public void onViewAdded(View child) {
		super.onViewAdded(child);
		if (child.getTag()==TAG_DONT_ENUM) return;
		int f=-1;
		int dec=0;
		for (int i=0;i < getChildCount();i++) {
			if (getChildAt(i).getTag() == TAG_DONT_ENUM) {
				dec++;
				continue;
			}
			if (getChildAt(i) == child) {
				f = i - dec;
				break;
			}
		}
		if (f == -1) return;
		if (f != curScreen) child.setVisibility(View.INVISIBLE);
	}
	public void setCurrentScreen(int index) {
		if (curScreen == index) return;
		if (index >= getChildCount()) return;
		final View v=getChildAt(curScreen);
		AlphaAnimation ani=new AlphaAnimation(1, 0);
		ani.setDuration(700);
		ani.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation ani) {}
			@Override
			public void onAnimationRepeat(Animation ani) {}
			@Override
			public void onAnimationEnd(Animation ani) {
				v.setVisibility(View.INVISIBLE);
			}
		});
		v.startAnimation(ani);
		curScreen = index;
		getChildAt(curScreen).setVisibility(View.VISIBLE);
		ani = new AlphaAnimation(0, 1);
		ani.setDuration(700);
		getChildAt(curScreen).startAnimation(ani);
	}
	public int getCurrentScreen() {
		return curScreen;
	}
}
