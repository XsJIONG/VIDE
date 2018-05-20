package com.jxs.vide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import com.jxs.vcompat.ui.ColorUtil;
import com.jxs.vcompat.ui.UI;

import static com.jxs.vide.L.get;

public class LoginRegistBar extends View {
	public static final int PAD=15;
	public static final int OUT_BOUNDS=UI.dp2px(5);
	private Paint BackgroundPaint,ClearPaint;
	private Paint TPaint;
	private boolean inLoginLayout=true;
	public LoginRegistBar(Context cx) {
		super(cx);
		setLayerType(LAYER_TYPE_SOFTWARE, null);
		BackgroundPaint = new Paint();
		BackgroundPaint.setStyle(Paint.Style.FILL);
		BackgroundPaint.setAntiAlias(true);
		BackgroundPaint.setShadowLayer(10, 2, 2, Color.GRAY);
		ClearPaint = new Paint();
		ClearPaint.setStyle(Paint.Style.FILL);
		ClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		ClearPaint.setAntiAlias(true);
		TPaint = new TextPaint();
		TPaint.setTextSize(UI.dp2px(30));
		TPaint.setTextAlign(Paint.Align.CENTER);
		onThemeChange();
	}
	public void onThemeChange() {
		BackgroundPaint.setColor(UI.getAccentColor());
		invalidate();
	}
	public void setColor(int color) {
		BackgroundPaint.setColor(color);
		invalidate();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawRoundRect(PAD, PAD, getWidth() - PAD, getHeight() - PAD, getHeight() / 2 - PAD, getHeight() / 2 - PAD, BackgroundPaint);
		int r=getHeight() / 2 + OUT_BOUNDS;
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, r, ClearPaint);
		TPaint.setColor(UI.getThemeColor());
		drawCenter(canvas, inLoginLayout ?get(L.Regist): get(L.Login), new Rect(PAD, PAD, getWidth() / 2 - PAD - r, getHeight() - PAD * 2));
		drawCenter(canvas, inLoginLayout ?get(L.Login): get(L.Regist), new Rect(getWidth() / 2 + r, PAD, getWidth() - PAD, getHeight() - PAD));
		TPaint.setColor(UI.getAccentColor());
		drawCenter(canvas, get(L.Login_OR), new Rect(getWidth() / 2 - r, PAD, getWidth() / 2 + r, getHeight() - PAD));
	}
	public void setInLoginLayout(boolean flag) {
		inLoginLayout = flag;
		invalidate();
	}
	public boolean isInLoginLayout() {
		return inLoginLayout;
	}
	public void drawCenter(Canvas canvas, String text, Rect rect) {
		Paint.FontMetricsInt m=TPaint.getFontMetricsInt();
		int y=(rect.top + rect.bottom) / 2 - (m.bottom + m.top) / 2;
		canvas.drawText(text, rect.centerX(), y, TPaint);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			int x=(int) event.getX();
			int y=(int) event.getY();
			if (x < PAD || y < PAD || x > getWidth() - PAD || y > getHeight() - PAD) return true;
			int ra=getHeight() / 2 - PAD;
			int cr=ra + OUT_BOUNDS;
			int bar=getWidth() / 2 - PAD - ra - cr;
			if (x <= PAD + ra) {
				if (distance(x, y, PAD + ra, PAD + ra) <= ra)
					if (inLoginLayout) onRegist(); else onLogin();
				return true;
			}
			if (x <= PAD + ra + bar) {
				if (inLoginLayout) onRegist(); else onLogin();
				return true;
			}
			if (x <= getWidth() / 2) {
				if (distance(x, y, getWidth() / 2, getHeight() / 2) > cr)
					if (inLoginLayout) onRegist(); else onLogin();
				return true;
			}
			if (x <= getWidth() / 2 + cr) {
				if (distance(x, y, getWidth() / 2, getHeight() / 2) > cr)
					if (inLoginLayout) onLogin(); else onRegist();
				return true;
			}
			if (x <= getWidth() / 2 + cr + bar) {
				if (inLoginLayout) onLogin(); else onRegist();
				return true;
			}
			if (distance(x, y, getWidth() - PAD - ra, getHeight() - PAD - ra) <= ra)
				if (inLoginLayout) onLogin(); else onRegist();
		}
		return true;
	}
	private static int distance(int ax, int ay, int bx, int by) {
		return (int) Math.sqrt((ax - bx) * (ax - bx) + (ay - by) * (ay - by));
	}
	private Runnable LRegist,LLogin;
	public void setRegistListener(Runnable l) {
		this.LRegist = l;
	}
	public void setLoginListener(Runnable l) {
		this.LLogin = l;
	}
	private void onRegist() {
		if (LRegist != null) LRegist.run();
	}
	private void onLogin() {
		if (LLogin != null) LLogin.run();
	}
}
