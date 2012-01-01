package com.jxs.vcompat.widget;

import android.animation.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.animation.*;

public abstract class VPath extends Drawable implements ValueAnimator.AnimatorUpdateListener {
	public static final int TYPE_NORMAL=0,TYPE_DYNAMIC=1,TYPE_STATIC=2;

	private Paint mPaint,mBackgroundPaint;
	private Path mPath;
	private PathMeasure mPathMeasure;
	private ValueAnimator Animation=null;
	private int mType=TYPE_NORMAL;
	private float mLength=0;
	private int mStyle;
	public VPath() {
		this(Style.Material);
	}
	public VPath(int style) {
		mStyle = style;
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(3);
		mBackgroundPaint = new Paint();
		mBackgroundPaint.setAntiAlias(false);
		mBackgroundPaint.setColor(Color.TRANSPARENT);
		mPath = new Path();
		Animation = ValueAnimator.ofFloat(0, 1);
		Animation.setRepeatMode(ValueAnimator.RESTART);
		Animation.setRepeatCount(ValueAnimator.INFINITE);
		Animation.setDuration(mDuration);
		Animation.setInterpolator(new LinearInterpolator());
		Animation.addUpdateListener(this);
		mLogoPaint = new Paint(mPaint);
		mLogoPaint.setStyle(Paint.Style.FILL);
		mLogoPaint.setAlpha(0);
		mDst = new Path();
		Bounds = new Rect();
		BoundsR = new Rect();
		applyStyle();
	}
	public void setStyle(int s) {
		mStyle = s;
		applyStyle();
	}
	public Paint getLogoPaint() {
		return mLogoPaint;
	}
	public int getStyle() {
		return mStyle;
	}
	public static interface Style {
		public int Material=0,Tiny=1,Telegram=2,Normal=3;
	}
	private void applyStyle() {
		switch (mStyle) {
			case Style.Material:mPaint.setStrokeJoin(Paint.Join.MITER);setRotateDuration(900);setSwitchDuration(500);mPaint.setStrokeCap(Paint.Cap.SQUARE);mLogoPaint.setStrokeCap(Paint.Cap.SQUARE);setStrokeWidth(6);setDuration(1800);setType(TYPE_NORMAL);setSpace(0.05f);break;
			case Style.Tiny:mPaint.setStrokeJoin(Paint.Join.ROUND);setRotateDuration(900);setSwitchDuration(500);mPaint.setStrokeCap(Paint.Cap.ROUND);mLogoPaint.setStrokeCap(Paint.Cap.ROUND);setStrokeWidth(7);setDuration(3000);setType(TYPE_NORMAL);setSpace(0.05f);break;
			case Style.Telegram:mPaint.setStrokeJoin(Paint.Join.ROUND);setSwitchDuration(500);mPaint.setStrokeCap(Paint.Cap.ROUND);mLogoPaint.setStrokeCap(Paint.Cap.ROUND);setStrokeWidth(8);setDuration(6000);setType(TYPE_DYNAMIC);setMinProgress(0.01f);setDynamicChangeDuration(10000);break;
			case Style.Normal:mPaint.setStrokeJoin(Paint.Join.MITER);setSwitchDuration(500);mPaint.setStrokeCap(Paint.Cap.SQUARE);mLogoPaint.setStrokeCap(Paint.Cap.SQUARE);setStrokeWidth(6);setType(TYPE_STATIC);setOffset((float)3 / 4);break;
		}
	}
	public void setType(int type) {
		mType = type;
		invalidateSelf();
	}
	public int getType() {
		return mType;
	}
	public void setStrokeWidth(int w) {
		mPaint.setStrokeWidth(w);
		updatePath();
	}
	public Paint getMainPaint() {
		return mPaint;
	}
	public Paint getBackgroundPaint() {
		return mBackgroundPaint;
	}
	public void setBackgroundColor(int color) {
		mBackgroundPaint.setColor(color);
	}
	public int getBackgroundColor() {
		return mBackgroundPaint.getColor();
	}
	private int mAlpha=255;
	private int mLogoAlpha=0;
	public void setColor(int color) {
		mPaint.setColor(color);
		mPaint.setAlpha(mAlpha);
		mLogoPaint.setColor(color);
		mLogoPaint.setAlpha(mLogoAlpha);
	}
	@Override
	public int getAlpha() {
		return mAlpha;
	}
	public int getColor() {
		return mPaint.getColor();
	}
	@Override
	public void setAlpha(int a) {
		mAlpha = a;
		mPaint.setAlpha(a);
	}
	@Override
	public void setColorFilter(ColorFilter cf) {
		mPaint.setColorFilter(cf);
		mLogoPaint.setColorFilter(cf);
	}
	private long mDuration=3000;
	public void setDuration(long d) {
		mDuration = d;
		Animation.setDuration(d);
	}
	public boolean isNormal() {
		return mType == TYPE_NORMAL;
	}
	public boolean isDynamic() {
		return mType == TYPE_DYNAMIC;
	}
	public boolean isStatic() {
		return mType == TYPE_STATIC;
	}
	public long getDuration() {
		return mDuration;
	}
	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}
	private Rect Bounds,BoundsR;
	@Override
	public void setBounds(int left, int top, int right, int bottom) {
		BoundsR.set(left, top, right, bottom);
		calculateBounds();
		updatePath();
	}
	@Override
	public void setBounds(Rect b) {
		setBounds(b.left, b.top, b.right, b.bottom);
	}
	private int MarginLeft=0,MarginTop=0,MarginRight=0,MarginBottom=0;
	public void setMargins(int l, int t, int r, int b) {
		MarginLeft = l;
		MarginTop = t;
		MarginRight = r;
		MarginBottom = b;
		calculateBounds();
	}
	public int getMarginLeft() {
		return MarginLeft;
	}
	public int getMarginTop() {
		return MarginTop;
	}
	public int getMarginRight() {
		return MarginRight;
	}
	public int getMarginBottom() {
		return MarginBottom;
	}
	private boolean CW=true;
	public void setCW(boolean f) {
		CW = f;
		updatePath();
	}
	private void calculateBounds() {
		Bounds.set(BoundsR);
		Bounds.left += MarginLeft;
		Bounds.top += MarginTop;
		Bounds.right -= MarginRight;
		Bounds.bottom -= MarginBottom;
	}
	public boolean isCW() {
		return CW;
	}
	private boolean isEmpty(Rect r) {
		if (r.left == 0 && r.top == 0 && r.right == 0 && r.bottom == 0) return true;
		return false;
	}
	private void updatePath() {
		if (isEmpty(Bounds)) return;
		mPath.reset();
		addPath(mPath, Bounds, mPaint.getStrokeWidth(), CW);
		if (mPathMeasure == null) mPathMeasure = new PathMeasure(mPath, false); else mPathMeasure.setPath(mPath, false);
		mLength = mPathMeasure.getLength();
	}
	public abstract void addPath(Path p, Rect b, float sw, boolean cw);
	private boolean started=false;
	private boolean running=false;
	private boolean Finished=false;
	private ValueAnimator RotateAnimation;
	private long mRotateDuration=700;
	public void setRotateDuration(long d) {
		mRotateDuration = d;
	}
	public long getRotateDuration() {
		return mRotateDuration;
	}
	public void start() {
		if (isNormal() && RotateAnimation == null) {
			RotateAnimation = ValueAnimator.ofFloat(0, 1);
			RotateAnimation.setRepeatCount(ValueAnimator.INFINITE);
			RotateAnimation.setRepeatMode(ValueAnimator.RESTART);
			RotateAnimation.setDuration(mRotateDuration);
			RotateAnimation.setInterpolator(new LinearInterpolator());
			RotateAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator ani) {
						if (mPathMeasure == null) return;
						float value=ani.getAnimatedValue();
						if (value < 0.5) safeGetSegment(mFraction + mSpace * mLength * value * 2, mFraction + mSpace * mLength + (1f - mSpace * 2) * mLength * value * 2);
						else safeGetSegment(mFraction + mSpace * mLength + (value = (float) (value - 0.5) * 2) * (1 - mSpace) * mLength, mFraction + (1f - mSpace) * mLength + value * mSpace * mLength * 2);
						invalidateSelf();
					}
				});
		}
		if (isStatic()) {
			mProgress = 0;
			running = true;
		} else {
			if (!started) {
				Animation.start();
				if (isNormal()) RotateAnimation.start();
				started = true;
			} else {
				Animation.resume();
				if (isNormal()) RotateAnimation.resume();
				running = true;
			}
		}
	}
	public void pause() {
		Animation.pause();
		if (isNormal()) RotateAnimation.pause();
		running = false;
	}
	public boolean isRunning() {
		return running;
	}
	private Path mDst;
	private float mOffset=(float)3 / 4;
	public void setOffset(float f) {
		mOffset = f;
		invalidateSelf();
	}
	public float getOffset() {
		return mOffset;
	}
	@Override
	public void draw(Canvas c) {
		if (isDynamic())
			if (Finished) safeGetSegment(0, mLength); else safeGetSegment(mFraction, mFraction + mProgress * mLength);
		if (isStatic())
			if (Finished) safeGetSegment(0, mLength); else safeGetSegment(mOffset * mLength, (mOffset + mProgress) * mLength);
		c.drawRect(Bounds, mBackgroundPaint);
		c.drawPath(mDst, mPaint);
		if (mCurrentLogo != null) mCurrentLogo.draw(c, mLogoPaint, Bounds.width());
	}
	private float minProgress=0.03f;
	private float mProgress=0;
	private float mFraction=0;
	public void setMinProgress(float min) {
		if (isDynamic() || isStatic()) {
			minProgress = min;
			mProgress = Math.max(mProgress, minProgress);
			invalidateSelf();
		}
	}
	public float getMinProgress() {
		return minProgress;
	}
	private ValueAnimator.AnimatorUpdateListener mChangeProgressListener=new ValueAnimator.AnimatorUpdateListener() {
		@Override
		public void onAnimationUpdate(ValueAnimator ani) {
			if (mPathMeasure == null) return;
			mProgress = ani.getAnimatedValue();
			invalidateSelf();
		}
	};
	private boolean mAutoCheck=true;
	public void setAutoCheck(boolean ac) {
		mAutoCheck = ac;
	}
	public boolean isAutoCheck() {
		return mAutoCheck;
	}
	private Runnable mDoneRunnable=null;
	public void setDoneRunnable(Runnable r) {
		mDoneRunnable = r;
	}
	public Runnable getDoneRunnable() {
		return mDoneRunnable;
	}
	private boolean autoShowDone=true;
	public void setAutoShowDone(boolean a) {
		autoShowDone = a;
	}
	public boolean isAutoShowDone() {
		return autoShowDone;
	}
	public void showDone() {
		setCurrentLogo(new DoneLogo());
		fadeInLogo();
	}
	public void showPause() {
		setCurrentLogo(new PauseLogo());
		fadeInLogo();
	}
	public void clearLogo() {
		fadeOutLogo();
	}
	private Animator.AnimatorListener mChangeAnimatorListener=new Animator.AnimatorListener() {
		@Override
		public void onAnimationStart(Animator ani) {}
		@Override
		public void onAnimationEnd(Animator ani) {
			mChangeProgress = null;
			if (toH) {
				toH = false;
				if (autoShowDone) showDone();
				if (mDoneRunnable != null) mDoneRunnable.run();
				Finished = true;
			}
			if (Last != -1) {
				smoothChangeProgress(Last);
				Last = -1;
			}
		}
		@Override
		public void onAnimationCancel(Animator ani) {
			mChangeProgress = null;
			toH = false;
		}
		@Override
		public void onAnimationRepeat(Animator ani) {}
	};
	private Animator.AnimatorListener mFadeAnimatorListener=new Animator.AnimatorListener() {
		@Override
		public void onAnimationStart(Animator ani) {}
		@Override
		public void onAnimationEnd(Animator ani) {
			mDisplayAnimation = null;
		}
		@Override
		public void onAnimationCancel(Animator ani) {
			mDisplayAnimation = null;
		}
		@Override
		public void onAnimationRepeat(Animator ani) {}
	};
	private ValueAnimator mChangeProgress;
	private long mDynamicChangeDuration=500;
	public void setDynamicChangeDuration(long l) {
		mDynamicChangeDuration = l;
	}
	public long getDynamicChangeDuration() {
		return mDynamicChangeDuration;
	}
	private Paint mLogoPaint;
	private ValueAnimator.AnimatorUpdateListener mDisplayListener=new ValueAnimator.AnimatorUpdateListener() {
		@Override
		public void onAnimationUpdate(ValueAnimator ani) {
			mLogoPaint.setAlpha(ani.getAnimatedValue());
			invalidateSelf();
		}
	};
	private ValueAnimator mDisplayAnimation;
	private Logo mCurrentLogo;
	private long mSwitchDuration=1000;
	public void setCurrentLogo(Logo l) {
		mCurrentLogo = l;
	}
	public Logo getCurrentLogo() {
		return mCurrentLogo;
	}
	private void fadeInLogo() {
		smoothDisplayLogo(true);
	}
	public void fadeOutLogo() {
		smoothDisplayLogo(false);
	}
	public void setSwitchDuration(long d) {
		mSwitchDuration = d;
	}
	public long getSwitchDuration() {
		return mSwitchDuration;
	}
	private void smoothDisplayLogo(boolean in) {
		if (mDisplayAnimation != null) mDisplayAnimation.cancel();
		mDisplayAnimation = ValueAnimator.ofInt(mLogoPaint.getAlpha(), in ?255: 0);
		mDisplayAnimation.setDuration(mSwitchDuration);
		mDisplayAnimation.addUpdateListener(mDisplayListener);
		mDisplayAnimation.addListener(mFadeAnimatorListener);
		mDisplayAnimation.setInterpolator(new LinearInterpolator());
		mDisplayAnimation.start();
	}
	private boolean toH=false;
	private float Last=-1;
	public boolean isFinished() {
		return Finished;
	}
	private void smoothChangeProgress(float target) {
		if (target == mProgress) return;
		if (mChangeProgress != null) {
			if (target > Last) Last = target;
			return;
		}
		if (target != 1 && Finished) {
			Finished = false;
			fadeOutLogo();
		}
		mChangeProgress = ValueAnimator.ofFloat(mProgress, target);
		mChangeProgress.setDuration(Math.abs((long) ((target - mProgress) * mDynamicChangeDuration)));
		mChangeProgress.addUpdateListener(mChangeProgressListener);
		mChangeProgress.setInterpolator(new LinearInterpolator());
		mChangeProgress.addListener(mChangeAnimatorListener);
		if (target == 1) toH = true;
		mChangeProgress.start();
	}
	public synchronized void setProgress(float pro) {
		if (isDynamic() || isStatic()) {
			if (pro > 1) pro = 1;
			smoothChangeProgress(Math.max(minProgress, pro));
		}
	}
	private float mSpace=0.03f;
	public void setSpace(float s) {
		mSpace = s;
		invalidateSelf();
	}
	public float getSpace() {
		return mSpace;
	}
	@Override
	public void onAnimationUpdate(ValueAnimator f) {
		float value=f.getAnimatedValue();
		mFraction = value * mLength;
		invalidateSelf();
	}
	private void safeGetSegment(float start, float end) {
		mDst.reset();
		mDst.rLineTo(0, 0);
		if (start > mLength) start %= mLength;
		if (end > mLength) end %= mLength;
		if (end < start) {
			mPathMeasure.getSegment(start, mLength, mDst, true);
			mPathMeasure.getSegment(0, end, mDst, true);
		} else mPathMeasure.getSegment(start, end, mDst, true);
	}
	private static void drawRect(Canvas c, Paint p, float l, float t, float r, float b, int w) {
		c.drawRect(l * w, t * w, r * w, b * w, p);
	}
	private static void drawLines(Canvas c, Paint p, float[] ps, int w) {
		float[] ss=new float[ps.length];
		for (int i=0;i < ss.length;i++) ss[i] = ps[i] * w;
		c.drawLines(ss, p);
	}
	public static class PauseLogo implements Logo {
		public void draw(Canvas c, Paint p, int w) {
			p.setStyle(Paint.Style.FILL);
			drawRect(c, p, (float)3 / 9 - (float) 1 / 16, (float)1 / 4, (float)4 / 9 - (float)1 / 16, (float)3 / 4, w);
			drawRect(c, p, (float)6 / 9 - (float) 1 / 16, (float)1 / 4, (float)7 / 9 - (float)1 / 16, (float)3 / 4, w);
		}
	}
	public static class DoneLogo implements Logo {
		private static final float R=(float) 1 / 4;
		private static final float O=(float) 1 / 14;
		private static final float[] FS={(float)1 / 2 - R + O,(float)1 / 2,(float)1 / 2 - R / 2 + O,(float)1 / 2 + R / 2,(float)1 / 2 - R / 2 + O,(float)1 / 2 + R / 2,(float)1 / 2 + R / 2 + O,(float)1 / 2 - R / 2};
		@Override
		public void draw(Canvas c, Paint p, int w) {
			p.setStyle(Paint.Style.STROKE);
			p.setStrokeWidth(5f);
			drawLines(c, p, FS, w);
		}
	}
	public static interface Logo {
		public void draw(Canvas c, Paint p, int w);
	}
	private int mDefWidth=80,mDefHeight=80;
	public void setDefaultWidth(int w) {
		mDefWidth = w;
	}
	public int getDefaultWidth() {
		return mDefWidth;
	}
	public void setDefaultHeight(int h) {
		mDefHeight = h;
	}
	public int getDefaultHeight() {
		return mDefHeight;
	}
	@Override
	public int getIntrinsicWidth() {
		return mDefWidth + MarginLeft + MarginRight;
	}
	@Override
	public int getIntrinsicHeight() {
		return mDefHeight + MarginTop + MarginBottom;
	}
	public static class VCircle extends VPath {
		public VCircle() {super();}
		public VCircle(int s) {super(s);}
		@Override
		public void addPath(Path p, Rect b, float sw, boolean cw) {
			p.addCircle(b.centerX(), b.centerY(), (Math.min(b.width(), b.height()) - sw) / 2, Path.Direction.CW);
		}
	}
	public static class VRect extends VPath {
		public VRect() {super();}
		public VRect(int s) {super(s);}
		@Override
		public void addPath(Path p, Rect b, float sw, boolean cw) {
			sw /= 2;
			p.addRect(b.left + sw, b.top + sw, b.right - sw, b.bottom - sw, Path.Direction.CW);
		}
	}
	public static class VSquare extends VPath {
		public VSquare() {super();}
		public VSquare(int s) {super(s);}
		@Override
		public void addPath(Path p, Rect b, float sw, boolean cw) {
			float R=(Math.min(b.width(), b.height()) - sw) / 2;
			int cx=b.centerX(),cy=b.centerY();
			p.addRect(cx - R, cy - R, cx + R, cy + R, Path.Direction.CW);
		}
	}
}
