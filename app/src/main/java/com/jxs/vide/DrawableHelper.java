package com.jxs.vide;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.util.HashMap;

public class DrawableHelper {
	private static HashMap<Integer,Bitmap> Map=new HashMap<>();
	public static Drawable getDrawable(int id, int color) {
		Integer q=Integer.valueOf(id);
		Drawable d=null;
		if (Map.containsKey(q)) {
			return bitmap2drawable(Map.get(q), color);
		} else {
			d = MyApplication.getContext().getResources().getDrawable(id);
			Map.put(q, ((BitmapDrawable) d).getBitmap());
			d.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
			return d;
		}
	}
	public static Drawable bitmap2drawable(Bitmap b, int color) {
		BitmapDrawable d=new BitmapDrawable(b);
		d.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
		return d;
	}
	private DrawableHelper() {}
}
