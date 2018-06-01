package com.jxs.vide.vbutton;

import android.content.*;
import android.graphics.*;
import android.media.*;
import android.view.*;
import com.jxs.vcompat.ui.*;
import com.jxs.vide.*;

public class FWView extends View implements UI.OnThemeChangeListener {
	public static int StatusBarHeight=-1;
	public static final int CircleRadius=UI.dp2px(25);
	static Bitmap ICON=null;
	private VButtonWindow Window;
	private Paint paint;
	private VButton vb;
	public FWView(Context cx) {
		super(cx);
		init();
	}
	public void setVButton(VButton vb) {
		this.vb = vb;
	}
	private void init() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(UI.getThemeColor());
		paint.setShadowLayer(3, 0, 2, Color.BLACK);
		setLayerType(LAYER_TYPE_SOFTWARE, null);
		Window = new VButtonWindow(getContext(), this);
		UI.registThemeChangedListener(this, this);
		setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					WindowManager.LayoutParams para=(WindowManager.LayoutParams) getLayoutParams();
					Window.setCenter(para.x + getWidth() / 2, para.y + getHeight() / 2);
					Window.show();
					Window.expand();
				}
			});
	}
	@Override
	protected void onDraw(Canvas canvas) {
		if (StatusBarHeight == -1) {
			StatusBarHeight = 0;
			int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
			if (resourceId > 0)
				StatusBarHeight = getResources().getDimensionPixelSize(resourceId);
		}
		if (ICON == null) {
			ICON = BitmapFactory.decodeResource(getResources(), R.drawable.v_icon);
			int w=(int) ((float) CircleRadius * Math.sqrt(2));
			ICON = ThumbnailUtils.extractThumbnail(ICON, w, w);
			VButtonService.All.add(ICON);
		}
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, CircleRadius, paint);
		canvas.drawBitmap(ICON, (getWidth() - ICON.getWidth()) / 2, (getHeight() - ICON.getHeight()) / 2, null);
	}

	@Override
	public void onThemeChange(String key) {
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		paint.setColor(UI.getThemeColor());
		invalidate();
		this.Window.onThemeChange(key);
	}
	private float _StartX,_StartY;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		float x=event.getRawX();
		float y=event.getRawY() - StatusBarHeight;
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:{
					_StartX = event.getX();
					_StartY = event.getY();
					break;
				}
			case MotionEvent.ACTION_MOVE:{
					vb.getLayoutParams().x = (int) (x - _StartX);
					vb.getLayoutParams().y = (int) (y - _StartY);
					vb.getWindowManager().updateViewLayout(this, vb.getLayoutParams());
					break;
				}
			case MotionEvent.ACTION_UP:{
					Global.setVButtonPos(vb.getLayoutParams().x, vb.getLayoutParams().y);
					break;
				}
		}
		return true;
	}
}
