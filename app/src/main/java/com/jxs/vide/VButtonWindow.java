package com.jxs.vide;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.jxs.vcompat.ui.ColorUtil;
import com.jxs.vcompat.ui.UI;

public class VButtonWindow extends View implements UI.OnThemeChangeListener,ValueAnimator.AnimatorUpdateListener,Animator.AnimatorListener {
	public static int FirstSubMenuRadius=UI.dp2px(57);
	public static int IconSize=UI.dp2px(20);
	private FloatingView Window;
	private int centerX=0,centerY=0;
	private RectF SubMenuRect=null;
	private FWView fw;
	public VButtonWindow(Context cx, FWView fw) {
		super(cx);
		this.fw = fw;
		Window = new FloatingView(this);
		Window.getLayoutParams().width = -1;
		Window.getLayoutParams().height = -1;
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(UI.getThemeColor());
		paint.setShadowLayer(3, 0, 2, Color.BLACK);
		setLayerType(LAYER_TYPE_SOFTWARE, null);
		highlightPaint = new Paint();
		highlightPaint.setAntiAlias(true);
		highlightPaint.setStyle(Paint.Style.FILL);
		highlightPaint.setColor(ColorUtil.lightColor(UI.getThemeColor(), 30));
		UI.registThemeChangedListener(this, this);
	}
	public void setCenter(int x, int y) {
		this.centerX = x;
		this.centerY = y;
		SubMenuRect = new RectF(x - FirstSubMenuRadius, y - FirstSubMenuRadius, x + FirstSubMenuRadius, y + FirstSubMenuRadius);
		invalidate();
	}
	public boolean show() {
		touchPos = -1;
		return Window.show();
	}
	public boolean hide() {
		return Window.hide();
	}
	public FloatingView getWindow() {
		return Window;
	}
	private boolean expanded=false;
	private float ExpandProgress=0;
	private Paint paint,highlightPaint;
	private boolean animating=false;
	public void expand() {
		if (expanded || animating) return;
		ValueAnimator ani=ValueAnimator.ofFloat(0, 1);
		ani.setDuration(700);
		ani.setInterpolator(new AccelerateDecelerateInterpolator());
		ani.addUpdateListener(this);
		ani.addListener(this);
		ani.start();
	}
	public void collapse() {
		if (!expanded || animating) return;
		ValueAnimator ani=ValueAnimator.ofFloat(1, 0);
		ani.setDuration(700);
		ani.setInterpolator(new AccelerateDecelerateInterpolator());
		touchPos = -1;
		ani.addUpdateListener(this);
		ani.addListener(this);
		ani.start();
	}
	public void toggle() {
		if (expanded) collapse(); else expand();
	}
	@Override
	public void onAnimationUpdate(ValueAnimator v) {
		ExpandProgress = (float) v.getAnimatedValue();
		invalidate();
	}
	@Override
	public void onAnimationStart(Animator ani) {
		animating = true;
	}
	@Override
	public void onAnimationEnd(Animator ani) {
		animating = false;
		expanded = !expanded;
		if (!expanded) hide();
		if (quit) {
			VButton._hide();
			quit = false;
		}
	}
	@Override
	public void onAnimationCancel(Animator ani) {}
	@Override
	public void onAnimationRepeat(Animator ani) {}
	@Override
	public void onThemeChange(String key) {
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		paint.setColor(UI.getThemeColor());
		highlightPaint.setColor(ColorUtil.lightColor(UI.getThemeColor(), 30));
		invalidate();
	}
	public static Bitmap[] Icons=null;
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawArc(SubMenuRect, -90, ExpandProgress * 360, true, paint);
		if (Icons == null) {
			Icons = new Bitmap[IconInfos.length / 2];
			for (int i=0;i < Icons.length;i++) Icons[i] = getIcon(IconInfos[i * 2]);
		}
		if (touchPos != -1) canvas.drawArc(SubMenuRect, touchPos * 60, 60, true, highlightPaint);
		canvas.drawCircle(centerX, centerY, FWView.CircleRadius, paint);
		canvas.drawBitmap(fw.ICON, centerX - fw.ICON.getWidth() / 2, centerY - fw.ICON.getHeight() / 2, null);
		int degree;
		for (int i=0;i < Icons.length;i++) {
			degree = IconInfos[i * 2 + 1];
			if ((degree + 90) % 360 <= ExpandProgress * 360) drawBitmap(canvas, Icons[i], degree);
		}
	}
	private int InnerRadius=-1;
	public final void drawBitmap(Canvas canvas, Bitmap icon, int degress) {
		double rd=(float) degress * Math.PI / 180;
		if (InnerRadius == -1)
			InnerRadius = (FirstSubMenuRadius - FWView.CircleRadius) / 2 + FWView.CircleRadius;
		int ImageX=(int) (centerX + Math.cos(rd) * InnerRadius);
		int ImageY=(int) (centerY + Math.sin(rd) * InnerRadius);
		canvas.drawBitmap(icon, ImageX - icon.getWidth() / 2, ImageY - icon.getHeight() / 2, null);
	}
	private final Bitmap getIcon(int id) {
		return ThumbnailUtils.extractThumbnail(getBitmap(id), IconSize, IconSize, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
	}
	private final Bitmap getBitmap(int id) {
		return ((BitmapDrawable) getResources().getDrawable(id)).getBitmap();
	}
	private int touchPos=-1;
	public static final double getDegree(double ax, double ay, double bx, double by) {
        if (ax == bx) {
			if (by > ay) return 90;
			else if (by == ay) return 0;
			else return 270;
		}
		if (by == ay) {
			if (bx > ax) return 0; else return 180;
		}
		double r=Math.toDegrees(Math.acos(Math.abs(ay - by) / Math.sqrt((ax - bx) * (ax - bx) + (ay - by) * (ay - by))));
		if (bx < ax && by > ay) return 90 + r;
		else if (bx < ax && by < ay) return 270 - r;
		else if (bx > ax && by < ay) return 270 + r;
		else return 90 - r;
    }
	private boolean quit=false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:{
					if (getTouchState(event) == 0 || animating) break;
					double degree=getDegree(centerX, centerY, event.getX(), event.getY());
					for (int i=1;i <= 6;i++) {
						if (degree < i * 60) {
							touchPos = i - 1;
							invalidate();
							break;
						}
					}
					break;
				}
			case MotionEvent.ACTION_UP:{
					if (animating) break;
					switch (getTouchState(event)) {
						case 0:collapse();break;
						case 1:{
								if (touchPos == -1) {
									double degree=getDegree(centerX, centerY, event.getX(), event.getY());
									for (int i=1;i <= 6;i++) {
										if (degree < i * 60) {
											touchPos = i - 1;
											invalidate();
											break;
										}
									}
								}
								onClickButton();
								break;
							}
						default:
							collapse();
							break;
					}
					break;
				}
		}
		return true;
	}
	//-1 - Unknown
	//0 - The inner circle
	//1 - The first submenu
	public int getTouchState(MotionEvent event) {
		double length=Math.sqrt((event.getX() - centerX) * (event.getX() - centerX) + (event.getY() - centerY) * (event.getY() - centerY));
		if (length <= FWView.CircleRadius) return 0;
		if (length <= FirstSubMenuRadius) return 1;
		return -1;
	}
	public int getCenterX() {
		return centerX;
	}
	public int getCenterY() {
		return centerY;
	}
	public static final int[] IconInfos={
		R.drawable.icon_exit,30,
		R.drawable.icon_console,90,
		R.drawable.icon_branch,150,
		R.drawable.icon_debug,210
	};
	private void onClickButton() {
		switch (touchPos) {
			case 0:{
					quit = true;
					collapse();
					break;
				}
			case 1:{
					ConsoleDialog.getInstance().show();
					collapse();
					break;
				}
			case 2:{
					ThreadDialog.getInstance().show();
					collapse();
					break;
				}
			case 3:{
					DebugWindow.getInstance().show();
					collapse();
					break;
				}
		}
	}
}
