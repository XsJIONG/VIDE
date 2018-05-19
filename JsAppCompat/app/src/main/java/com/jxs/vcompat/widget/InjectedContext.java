package com.jxs.vcompat.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

public class InjectedContext extends ContextWrapper {
	private InjectedResource resource=null;
	public static InjectedContext inject(Context cx, int color) {
		if (!(cx instanceof InjectedContext)) cx = new InjectedContext(cx);
		((InjectedContext) cx).setColor(color);
		return (InjectedContext) cx;
	}
	public InjectedContext(Context cx) {
		super(cx);
		resource = new InjectedResource(super.getResources());
	}
	@Override
	public Resources getResources() {
		return new InjectedResource(super.getResources());
	}
	public void setColor(int color) {
		resource.setColor(color);
	}
	public int getColor() {
		return resource.getColor();
	}
	public static class InjectedResource extends ResourcesWrapper {
		public InjectedResource(Resources r) {
			super(r);
		}
		@Override
		public Drawable getDrawable(int id) throws Resources.NotFoundException {
			Drawable d=super.getDrawable(id);
			if (d != null)
				d.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
			return d;
		}
		public int color;
		public int getColor() {
			return color;
		}
		public void setColor(int color) {
			this.color = color;
		}
	}
}
