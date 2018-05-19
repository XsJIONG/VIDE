package com.jxs.v.animation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import com.jxs.v.ui.UI;
import com.jxs.v.ui.VAnimation;

public class RevealAnimation extends VAnimation<View> {
	private Point _Center=null;
	private RevealView _View;
	public RevealAnimation(Context cx) {
		super(new View(cx));
		init(UI.getThemeColor());
	}
	public RevealAnimation(Context cx, int color) {
		super(new View(cx));
		init(color);
	}
	public RevealAnimation(View v, int cx, int cy, int color) {
		this(v, new Point(cx, cy), color);
	}
	public RevealAnimation(View v) {
		this(v, Color.WHITE);
	}
	public RevealAnimation(View v, int color) {
		super(v);
		init(color);
	}
	public RevealAnimation(View v, Point center, int color) {
		super(v);
		_Center = center;
		init(color);
	}
	public void setColor(int color) {
		_View.setColor(color);
	}
	public void collapse() {
		_View.collapse();
	}
	public void setRadius(int r) {
		_View.setRadius(r);
	}
	@Override
	public void setDuration(long mills) {
		_View.setDuration(mills);
	}
	@Override
	public void setInterpolator(Interpolator porlator) {
		_View.setInterpolator(porlator);
	}
	private void init(int color) {
		_View = new RevealView(getView(), _Center, color, new Listener() {
			@Override
			public void onStart() {
				animationStart();
			}
			@Override
			public void onEnd() {
				animationStop();
			}
		});
	}
	@Override
	public void start() {
		if (isStopped()) return;
		Activity ac=(Activity) getContext();
		if (_Center == null) {
			int[] pos=new int[2];
			getView().getLocationInWindow(pos);
			_Center = new Point(pos[0] + getView().getWidth() / 2, pos[1] + getView().getHeight() / 2);
			_View.setCenter(_Center);
		}
		((ViewGroup) ac.getWindow().getDecorView()).addView(_View);
		_View.start();
	}
	public void setClip(boolean flag) {
		_View.setClip(flag);
	}
	public void clipRect(Rect r) {
		_View.clipRect(r);
	}
	@Override
	public void pause() {
		_View.pause();
	}
	@Override
	public void stop() {
		if (!isRunning()) return;
		_View.pause();
		((ViewGroup) ((Activity) getView().getContext()).getWindow().getDecorView()).removeView(_View);
	}
	public void clipPath(Path p) {
		_View.clipPath(p);
	}
	public void setClipper(Clipper c) {
		_View.setClipper(c);
	}
	public void setView(View v) {
		_View.setView(v);
	}
	public void setInnerView(View v) {
		_View.setInnerView(v);
	}
	public void setCenter(int x, int y) {
		setCenter(new Point(x, y));
	}
	public void setCenter(Point p) {
		_Center = p;
		_View.setCenter(p);
	}
	public void setInnerView(View v, FrameLayout.LayoutParams para) {
		_View.setInnerView(v, para);
	}
	public static class RevealView extends View {
		private Paint _Paint;
		private Listener listener;
		private Point p;
		private View v;
		public RevealView(View v, Point st, int color, Listener listener) {
			super(v.getContext());
			init(color);
			this.listener = listener;
			this.p = st;
			this.v = v;
		}
		private View inner;
		private FrameLayout.LayoutParams para;
		public void setInnerView(View v) {
			setInnerView(v, new FrameLayout.LayoutParams(-2, -2));
		}
		public void setView(View v) {
			this.v = v;
		}
		private float target=-1;
		public void collapse() {
			if (ani != null && ani.isRunning()) return;
			Point a=null,b,c,d;
			if (target == -1) {
				if (v != null) {
					Rect rect=new Rect();
					v.getDrawingRect(rect);
					int[] pos=new int[2];
					v.getLocationInWindow(pos);
					rect.offset(pos[0], pos[1]);
					if (clipper == null) clipper = new Clipper(rect);
					a = new Point(rect.left, rect.top); b = new Point(rect.right, rect.top); c = new Point(rect.right, rect.bottom); d = new Point(rect.left, rect.bottom);
					target = max(dis(a, p), dis(b, p), dis(c, p), dis(d, p));
				}
				if (a == null) {
					a = new Point(0, 0); b = new Point(getWidth(), 0); c = new Point(getWidth(), getHeight()); d = new Point(0, getHeight());
					target = max(dis(a, p), dis(b, p), dis(c, p), dis(d, p));
				}
			}
			if (inner != null) {
				Rect w=new Rect();
				inner.getDrawingRect(w);
				int[] pos=new int[2];
				inner.getLocationInWindow(pos);
				w.offset(pos[0], pos[1]);
				a = new Point(w.left, w.top); b = new Point(w.right, w.top); c = new Point(w.right, w.bottom); d = new Point(w.left, w.bottom);
				qwer = max(dis(a, p), dis(b, p), dis(c, p), dis(d, p));
			}
			ani = ValueAnimator.ofFloat(target, 0);
			ani.setDuration(mill == -1 ?1300: mill);
			ani.setInterpolator(polator == null ?new AccelerateInterpolator(): polator);
			ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator ani) {
					_Radius = (float) ani.getAnimatedValue();
					invalidate();
				}
			});
			ani.addListener(new Animator.AnimatorListener() {
				@Override
				public void onAnimationStart(Animator ani) {
					if (listener != null) listener.onStart();
				}
				@Override
				public void onAnimationEnd(Animator ani) {
					if (listener != null) listener.onEnd();
					getDecor().removeView(RevealView.this);
					getDecor().removeView(inner);
				}
				@Override
				public void onAnimationCancel(Animator ani) {}
				@Override
				public void onAnimationRepeat(Animator ani) {}
			});
			ani.start();
		}
		private Clipper clipper=null;
		public void setClipper(Clipper q) {
			this.clipper = q;
		}
		public void setInnerView(View v, FrameLayout.LayoutParams para) {
			this.inner = v;
			this.para = para;
		}
		public void setRadius(int r) {
			target = r;
		}
		private boolean clip=true;
		public void setClip(boolean flag) {
			clip = flag;
		}
		public void setCenter(Point p) {
			this.p = p;
		}
		private void init(int color) {
			_Paint = new Paint();
			_Paint.setStyle(Paint.Style.FILL);
			_Paint.setColor(color);
			_Paint.setAntiAlias(true);
			setBackgroundColor(Color.TRANSPARENT);
		}
		public void setColor(int color) {
			_Paint.setColor(color);
		}
		private static final float dis(Point a, Point b) {
			return (float) (Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y)));
		}
		private static final float max(float...all) {
			float max=Float.MIN_VALUE;
			for (float one : all) if (one > max) max = one;
			return max;
		}
		private long mill=-1;
		public void setDuration(long mill) {
			this.mill = mill;
			if (ani != null) ani.setDuration(mill);
		}
		private Interpolator polator=null;
		public void setInterpolator(Interpolator i) {
			this.polator = i;
			if (ani != null) ani.setInterpolator(polator);
		}
		public void clipPath(Path p) {
			clipPath(p);
		}
		public void clipRect(Rect r) {
			this.clipper = new Clipper(r);
		}
		private ValueAnimator ani=null;
		public void pause() {
			if (ani != null) ani.pause();
		}
		public ViewGroup getDecor() {
			return (ViewGroup) ((Activity) getContext()).getWindow().getDecorView();
		}
		private float qwer=-1;
		public void start() {
			if (ani != null && ani.isRunning()) return;
			if (inner != null) {
				getDecor().addView(inner, para);
				inner.setVisibility(View.INVISIBLE);
				inner.post(new Runnable() {
					@Override
					public void run() {
						Rect w=new Rect();
						inner.getDrawingRect(w);
						int[] pos=new int[2];
						inner.getLocationInWindow(pos);
						w.offset(pos[0], pos[1]);
						Point a=new Point(w.left, w.top), b=new Point(w.right, w.top), c=new Point(w.right, w.bottom), d=new Point(w.left, w.bottom);
						qwer = max(dis(a, p), dis(b, p), dis(c, p), dis(d, p));
						rStart();
					}
				});
			} else {
				if (v == null && target == -1) {
					Point a = new Point(0, 0), b = new Point(getWidth(), 0), c = new Point(getWidth(), getHeight()), d = new Point(0, getHeight());
					target = max(dis(a, p), dis(b, p), dis(c, p), dis(d, p));
				}
				rStart();
			}
			if (target == -1) {
				if (v != null) {
					Rect rect=new Rect();
					v.getDrawingRect(rect);
					int[] pos=new int[2];
					v.getLocationInWindow(pos);
					rect.offset(pos[0], pos[1]);
					if (clipper == null) clipper = new Clipper(rect);
					Point a=new Point(rect.left, rect.top), b=new Point(rect.right, rect.top), c=new Point(rect.right, rect.bottom), d=new Point(rect.left, rect.bottom);
					target = max(dis(a, p), dis(b, p), dis(c, p), dis(d, p));
				}
			} else rStart();
		}
		private void rStart() {
			ani = ValueAnimator.ofFloat(0, target);
			ani.setDuration(mill == -1 ?1300: mill);
			ani.setInterpolator(polator == null ?new AccelerateInterpolator(): polator);
			ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator ani) {
					_Radius = (float) ani.getAnimatedValue();
					invalidate();
				}
			});
			ani.addListener(new Animator.AnimatorListener() {
				@Override
				public void onAnimationStart(Animator ani) {
					if (listener != null) listener.onStart();
				}
				@Override
				public void onAnimationEnd(Animator ani) {
					if (listener != null) listener.onEnd();
				}
				@Override
				public void onAnimationCancel(Animator ani) {}
				@Override
				public void onAnimationRepeat(Animator ani) {}
			});
			ani.start();
		}
		private float _Radius=-1;
		@Override
		protected void onDraw(Canvas canvas) {
			if (_Radius == -1) return;
			if (clip && clipper != null)
				clipper.clip(canvas, _Radius);
			canvas.drawCircle(p.x, p.y, _Radius, _Paint);
			if (qwer != -1 && _Radius > qwer && inner.getVisibility() == View.INVISIBLE) inner.setVisibility(View.VISIBLE);
			if (qwer != -1 && _Radius < qwer && inner.getVisibility() == View.VISIBLE) inner.setVisibility(View.INVISIBLE);
		}
	}
	private static interface Listener {
		public void onStart();
		public void onEnd();
	}
	public static class Clipper {
		private Rect q=null;
		private Path p=null;
		public Clipper() {}
		public Clipper(Rect r) {
			this.q = r;
		}
		public Clipper(Path p) {
			this.p = p;
		}
		public void clip(Canvas c, float radius) {
			if (q != null) c.clipRect(q);
			if (p != null) c.clipPath(p);
		}
	}
}
