package com.jxs.vide;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jxs.vapp.program.JsApp;
import com.jxs.vcompat.ui.ColorUtil;
import com.jxs.vcompat.ui.UI;

public class FloatingWindow extends LinearLayout implements UI.OnThemeChangeListener {
	public static final int EDGE=UI.dp2px(5);
	private FloatingView Window;
	private RelativeLayout TitleBar;
	private TextView Title;
	private Paint cornerPaint,edgePaint;
	private FrameLayout container,edgeContainter;
	private ImageView Close;
	public FloatingWindow() {
		this("Window", null);
	}
	public FloatingWindow(String title) {
		this(title, null);
	}
	public void setTitle(String title) {
		Title.setText(title);
	}
	public void setBounds(int x, int y, int width, int height) {
		WindowManager.LayoutParams para=getLayoutParams();
		para.x = x;
		para.y = y;
		para.width = width;
		para.height = height;
		if (isShowing()) getWindowManager().updateViewLayout(this, para);
	}
	public void setSize(int width, int height) {
		getLayoutParams().width = width;
		getLayoutParams().height = height;
		if (isShowing()) getWindowManager().updateViewLayout(this, getLayoutParams());
	}
	public void setLocation(int x, int y) {
		getLayoutParams().x = x;
		getLayoutParams().y = y;
		if (isShowing()) getWindowManager().updateViewLayout(this, getLayoutParams());
	}
	public FloatingWindow(String title, View v) {
		super(JsApp.GlobalContext);
		Window = new FloatingView(this);
		Window.getLayoutParams().flags |= WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
		setOrientation(VERTICAL);
		TitleBar = new RelativeLayout(getContext());
		TitleBar.setBackgroundColor(UI.getThemeColor());
		Title = new TextView(getContext());
		Title.setText(title);
		Title.setTextSize(15);
		RelativeLayout.LayoutParams para=new RelativeLayout.LayoutParams(-2, -2);
		para.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		int p=UI.dp2px(10);
		para.setMargins(p, p, p, p);
		TitleBar.addView(Title, para);
		Close = new ImageView(getContext());
		para = new RelativeLayout.LayoutParams(UI.dp2px(24), UI.dp2px(24));
		para.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		para.setMargins(p, p, p, p);
		TitleBar.addView(Close, para);
		Close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hide();
			}
		});
		addView(TitleBar, new LinearLayout.LayoutParams(-1, -2));
		TitleBar.setOnTouchListener(new OnTouchListener() {
			private float _StartX,_StartY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (!_Moveable) return false;
				float x=event.getRawX();
				float y=event.getRawY() - FWView.StatusBarHeight;
				switch (event.getActionMasked()) {
					case MotionEvent.ACTION_DOWN:{
							_StartX = event.getX();
							_StartY = event.getY();
							break;
						}
					case MotionEvent.ACTION_MOVE:{
							if (y < _StartY) return true;
							Window.getLayoutParams().x = (int) (x - _StartX);
							Window.getLayoutParams().y = (int) (y - _StartY);
							Window.getWindowManager().updateViewLayout(FloatingWindow.this, Window.getLayoutParams());
							onMove(Window.getLayoutParams().x, Window.getLayoutParams().y);
							break;
						}
				}
				return true;
			}
		});
		setWillNotDraw(false);
		cornerPaint = new Paint();
		cornerPaint.setAntiAlias(true);
		cornerPaint.setStyle(Paint.Style.FILL);
		cornerPaint.setColor(Color.WHITE);
		edgePaint = new Paint();
		edgePaint.setStyle(Paint.Style.STROKE);
		edgePaint.setStrokeWidth(5);
		edgePaint.setColor(UI.getThemeColor());
		UI.registThemeChangedListener(this, this);
		edgeContainter = new FrameLayout(getContext());
		edgeContainter.setPadding(EDGE, 0, EDGE, EDGE);
		container = new FrameLayout(getContext());
		if (v != null) container.addView(v);
		edgeContainter.addView(container, new FrameLayout.LayoutParams(-1, -1));
		addView(edgeContainter, new LinearLayout.LayoutParams(-1, -1));
	}
	public void setView(View v) {
		container.removeAllViews();
		if (v != null) container.addView(v, new FrameLayout.LayoutParams(-1, -1));
	}
	public boolean show() {
		onThemeChange(UI.THEME_UI_COLOR);
		UI.registThemeChangedListener(this, this);
		return Window.show();
	}
	public boolean hide() {
		UI.registThemeChangedListener(this, this);
		return Window.hide();
	}
	public boolean isShowing() {
		return Window.isShowing();
	}
	public WindowManager getWindowManager() {
		return Window.getWindowManager();
	}
	public WindowManager.LayoutParams getLayoutParams() {
		return Window.getLayoutParams();
	}
	@Override
	public void onThemeChange(String key) {
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		int w=UI.getAccentColor();
		Title.setTextColor(w);
		edgePaint.setColor(UI.getThemeColor());
		edgeContainter.setBackgroundColor(UI.getThemeColor());
		Close.setImageDrawable(DrawableHelper.getDrawable(R.drawable.icon_close, w));
		TitleBar.setBackgroundColor(UI.getThemeColor());
	}
	public static final int CORNER=UI.dp2px(15);
	private RectF CornerRect=null;
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (CornerRect == null)
			CornerRect = new RectF(getWidth() - CORNER, getHeight() - CORNER, getWidth() + CORNER, getHeight() + CORNER);
		if (!_Resizable) return;
		canvas.drawArc(CornerRect, 180, 90, true, cornerPaint);
		if (LeftTop != null && RightBottom != null)
			canvas.drawRect(Math.min(LeftTop.x, RightBottom.x), Math.min(LeftTop.y, RightBottom.y), Math.max(LeftTop.x, RightBottom.x), Math.max(LeftTop.y, RightBottom.y), edgePaint);
	}
	private Point LeftTop,RightBottom;
	private boolean Scaleing=false;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (!_Resizable) return false;
		if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
			float x=event.getX() + Window.getLayoutParams().x;
			float y=event.getY() + Window.getLayoutParams().y;
			x -= Window.getLayoutParams().x;
			y -= Window.getLayoutParams().y;
			float dis=(float) Math.sqrt((getWidth() - x) * (getWidth() - x) + (getHeight() - y) * (getHeight() - y));
			if (dis <= CORNER) {
				WindowManager.LayoutParams para=Window.getLayoutParams();
				CornerRect.offset(para.x, para.y);
				LeftTop = new Point(para.x, para.y);
				RightBottom = new Point(para.x + para.width, para.y + para.height);
				Display d=Window.getWindowManager().getDefaultDisplay();
				setPadding(para.x, para.y, d.getWidth() - para.x - para.width, d.getHeight() - para.y - para.height - FWView.StatusBarHeight);
				Window.getLayoutParams().x = 0;
				Window.getLayoutParams().y = 0;
				Window.getLayoutParams().width = -1;
				Window.getLayoutParams().height = -1;
				Window.getWindowManager().updateViewLayout(FloatingWindow.this, para);
				invalidate();
				Scaleing = true;
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!Scaleing) return false;
		float x=event.getX();
		float y=event.getY();
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_MOVE:{
					RightBottom = new Point((int) x, (int) y);
					invalidate();
					break;
				}
			case MotionEvent.ACTION_UP:
				if (LeftTop == null || RightBottom == null) return true;
				WindowManager.LayoutParams para=Window.getLayoutParams();
				para.x = Math.min(LeftTop.x, RightBottom.x); para.y = Math.min(LeftTop.y, RightBottom.y);
				para.width = Math.abs(RightBottom.x - LeftTop.x); para.height = Math.abs(LeftTop.y - RightBottom.y);
				setPadding(0, 0, 0, 0);
				Window.getWindowManager().updateViewLayout(FloatingWindow.this, para);
				LeftTop = null;
				RightBottom = null;
				CornerRect = null;
				Scaleing = false;
				onResize(para.width, para.height);
				break;
		}
		return true;
	}
	private boolean _Resizable=true,_Moveable=true;
	public void setResizable(boolean re) {
		_Resizable = re;
		invalidate();
	}
	public void setMoveable(boolean re) {
		_Moveable = re;
	}
	protected void onMove(int x, int y) {}
	protected void onResize(int width, int height) {}
}
