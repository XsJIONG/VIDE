package com.jxs.vcompat.ui;

import android.view.ViewGroup;
import android.widget.TextView;

public class BPair {
	public BCurve a,b;
	public BPair() {
		a = new BCurve();
		b = new BCurve();
	}
	public BPair(BCurve from, BCurve to) {
		this.a = from; this.b = to;
	}
	public BCurve translate(float progress) {
		BCurve e=new BCurve();
		e.a = a.a.lerp(b.a, progress);
		e.b = a.b.lerp(b.b, progress);
		e.c = a.c.lerp(b.c, progress);
		e.d = a.d.lerp(b.d, progress);
		return e;
	}
}

