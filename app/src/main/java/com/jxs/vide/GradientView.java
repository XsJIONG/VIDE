package com.jxs.vide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.View;
import com.jxs.vcompat.ui.UI;

public class GradientView extends View {
	private LinearGradient Gradient=null;
	private Paint _Paint;
	public GradientView(Context cx) {
		super(cx);
		_Paint = new Paint();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		if (Gradient == null) {
			Gradient = new LinearGradient(0, 0, 0, getHeight(), Color.TRANSPARENT, UI.getThemeColor(), Shader.TileMode.CLAMP);
			_Paint.setShader(Gradient);
		}
		canvas.drawRect(0, 0, getWidth(), getHeight(), _Paint);
	}
	public void onThemeChange() {
		Gradient = null;
	}
}
