package com.jxs.vcompat.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.jxs.vcompat.widget.VScrollView;

public class ColorSelectorFragment extends VFragment implements AppCompatSeekBar.OnSeekBarChangeListener {
	private VScrollView sc;
	private LinearLayoutCompat Root;
	private View Color;
	private AppCompatSeekBar r,g,b;
	private AppCompatSeekBar alpha;
	private boolean EnableAlpha=false;
	@Override
	public Object getTag() {
		return ColorSelectorFragment.class;
	}
	public ColorSelectorFragment(Context cx) {
		super(cx);
		Root = new LinearLayoutCompat(getContext());
		Root.setOrientation(LinearLayoutCompat.VERTICAL);
		Color = new View(getContext());
		r = new AppCompatSeekBar(getContext());
		g = new AppCompatSeekBar(getContext());
		b = new AppCompatSeekBar(getContext());
		Root.addView(Color, new LinearLayoutCompat.LayoutParams(-1, 300));
		LinearLayoutCompat layout=null;
		TextView v=null;
		layout = new LinearLayoutCompat(getContext());
		v = new TextView(getContext());
		layout.setOrientation(LinearLayoutCompat.HORIZONTAL);
		v.setText("R");
		layout.addView(v, new LinearLayoutCompat.LayoutParams(-2, -2));
		layout.addView(r, new LinearLayoutCompat.LayoutParams(-1, -2));
		Root.addView(layout, new LinearLayoutCompat.LayoutParams(-1, -2));
		layout = new LinearLayoutCompat(getContext());
		v = new TextView(getContext());
		layout.setOrientation(LinearLayoutCompat.HORIZONTAL);
		v.setText("G");
		layout.addView(v, new LinearLayoutCompat.LayoutParams(-2, -2));
		layout.addView(g, new LinearLayoutCompat.LayoutParams(-1, -2));
		Root.addView(layout, new LinearLayoutCompat.LayoutParams(-1, -2));
		layout = new LinearLayoutCompat(getContext());
		v = new TextView(getContext());
		layout.setOrientation(LinearLayoutCompat.HORIZONTAL);
		v.setText("B");
		layout.addView(v, new LinearLayoutCompat.LayoutParams(-2, -2));
		layout.addView(b, new LinearLayoutCompat.LayoutParams(-1, -2));
		Root.addView(layout, new LinearLayoutCompat.LayoutParams(-1, -2));
		r.setMax(255);
		g.setMax(255);
		b.setMax(255);
		setColor(android.graphics.Color.BLACK);
		r.setOnSeekBarChangeListener(this);
		g.setOnSeekBarChangeListener(this);
		b.setOnSeekBarChangeListener(this);
		Color.post(new Runnable() {
			@Override
			public void run() {
				Color.setLayoutParams(new LinearLayoutCompat.LayoutParams(Color.getWidth(), Color.getWidth()));
			}
		});
		sc = new VScrollView(cx);
		sc.setFillViewport(true);
		sc.addView(Root);
	}
	public void enableAlpha() {
		EnableAlpha = true;
		if (alpha == null) {
			alpha = new AppCompatSeekBar(getContext());
			alpha.setMax(255);
			LinearLayoutCompat layout = new LinearLayoutCompat(getContext());
			TextView v = new TextView(getContext());
			layout.setOrientation(LinearLayoutCompat.HORIZONTAL);
			v.setText("A");
			layout.addView(v, new LinearLayoutCompat.LayoutParams(-2, -2));
			layout.addView(alpha);
			Root.addView(layout);
		}
	}
	public void disableAlpha() {
		EnableAlpha = false;
		if (alpha != null) {
			Root.removeViewAt(1);
			alpha = null;
		}
	}
	public boolean isAlphaEnabled() {
		return EnableAlpha;
	}
	public void setColor(int c) {
		Color.setBackgroundColor(c);
		if (EnableAlpha) alpha.setProgress(android.graphics.Color.alpha(c));
		r.setProgress(android.graphics.Color.red(c));
		g.setProgress(android.graphics.Color.green(c));
		b.setProgress(android.graphics.Color.blue(c));
	}
	public int getColor() {
		return ((ColorDrawable) Color.getBackground()).getColor();
	}
	@Override
	public View getView() {
		return sc;
	}
	@Override
	public void onProgressChanged(SeekBar s, int pro, boolean ru) {
		if (EnableAlpha) Color.setBackgroundColor(android.graphics.Color.argb(alpha.getProgress(), r.getProgress(), g.getProgress(), b.getProgress())); else Color.setBackgroundColor(android.graphics.Color.rgb(r.getProgress(), g.getProgress(), b.getProgress()));
	}
	@Override
	public void onStartTrackingTouch(SeekBar s) {}
	@Override
	public void onStopTrackingTouch(SeekBar s) {}
}
