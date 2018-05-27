package com.jxs.vide;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import com.jxs.vcompat.activity.VActivity;
import com.jxs.vcompat.ui.ColorUtil;
import com.jxs.vcompat.ui.UI;
import com.jxs.vcompat.ui.VProgressDialog;
import com.jxs.vide.R;
import java.util.stream.IntStream;

import static com.jxs.vide.L.get;

public class LoginActivity extends VActivity {
	public static final int ARROW_SIZE=UI.dp2px(10);
	public static final int SWIPE_DOWN_TIME=700,FADE_IN_TIME=700;
	private LoginViewHelper Helper;
	private Toolbar Title;
	private RelativeLayout Root;
	private LinearLayout InfoLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BmobUser.getCurrentUser(VUser.class) != null) {
			finish();
			return;
		}
		initViews();
		onConfigChange();
		setStatusBarTransparent();
		Helper.setOnAnimationEndListener(new Runnable() {
			@Override
			public void run() {
				fadeIn();
			}
		});
	}
	private void fadeIn() {
		AlphaAnimation ani=new AlphaAnimation(0, 1);
		ani.setDuration(FADE_IN_TIME);
		ani.setStartOffset(300);
		InfoLayout.setVisibility(View.VISIBLE);
		LoginLayout.setVisibility(View.VISIBLE);
		InfoLayout.startAnimation(ani);
		Pager.startAnimation(ani);
	}
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Helper.swipeDown(SWIPE_DOWN_TIME);
	}
	private LinearLayout LoginLayout,RegistLayout;
	private CircleEditText UserNameED,PasswordED,RegistUserNameED,RegistPasswordED;
	private TextView LoginText,RegistText;
	private LoginRegistBar ButtonBar,RegistButtonBar;
	private PageLayout Pager;
	private void initViews() {
		Title = new Toolbar(this);
		Title.setBackgroundColor(Color.TRANSPARENT);
		setSupportActionBar(Title);
		Title.setTitleTextColor(Color.TRANSPARENT);
		enableBackButton();
		Root = new RelativeLayout(this);
		InfoLayout = new LinearLayout(this);
		InfoLayout.setOrientation(LinearLayout.VERTICAL);
		InfoLayout.setGravity(Gravity.CENTER);
		ImageView icon=new ImageView(this);
		icon.setImageResource(R.drawable.v_icon);
		int ic=(getResources().getConfiguration().orientation == 1 ?getWindowManager().getDefaultDisplay().getWidth(): getWindowManager().getDefaultDisplay().getHeight()) / 3;
		InfoLayout.addView(icon, new LinearLayout.LayoutParams(ic, ic));
		TextView name=new TextView(this);
		name.setTypeface(Global.getScratch());
		name.setText(getResources().getString(R.string.app_name));
		name.setTextSize(25);
		LinearLayout.LayoutParams para=new LinearLayout.LayoutParams(-2, -2);
		name.setGravity(Gravity.CENTER);
		para.topMargin = UI.dp2px(30);
		InfoLayout.addView(name, para);
		InfoLayout.setVisibility(View.GONE);
		LoginLayout = new LinearLayout(this);
		LoginLayout.setOrientation(LinearLayout.VERTICAL);
		LoginLayout.setGravity(Gravity.CENTER_VERTICAL);
		para = new LinearLayout.LayoutParams(-1, -2);
		para.leftMargin = para.rightMargin = UI.dp2px(15);
		LoginText = new TextView(this);
		LoginText.setText(get(L.Login));
		LoginText.setGravity(Gravity.LEFT);
		LoginText.setTextSize(25);
		LoginLayout.addView(LoginText, para);
		para = new LinearLayout.LayoutParams(-1, -2);
		para.topMargin = para.leftMargin = para.rightMargin = UI.dp2px(15);
		UserNameED = new CircleEditText(this);
		UserNameED.setIconResource(R.drawable.icon_username);
		UserNameED.setSingleLine();
		UserNameED.setHintTextColor(Color.GRAY);
		UserNameED.setHint(get(L.UserName));
		LoginLayout.addView(UserNameED, para);
		PasswordED = new CircleEditText(this);
		PasswordED.setIconResource(R.drawable.icon_key);
		PasswordED.setSingleLine();
		PasswordED.setTypeface(Typeface.MONOSPACE);
		PasswordED.setHintTextColor(Color.GRAY);
		PasswordED.setHint(get(L.Password));
		PasswordED.setTransformationMethod(new PassWordTM());
		LoginLayout.addView(PasswordED, para);
		ButtonBar = new LoginRegistBar(this);
		ButtonBar.setInLoginLayout(true);
		para = new LinearLayout.LayoutParams(-1, UI.dp2px(40) + LoginRegistBar.PAD * 2);
		para.leftMargin = para.topMargin = para.rightMargin = UI.dp2px(15);
		LoginLayout.addView(ButtonBar, para);
		LoginLayout.setVisibility(View.INVISIBLE);
		Pager = new PageLayout(this);
		Pager.addView(LoginLayout);
		initRegistLayout();
		Pager.addView(RegistLayout);
		ButtonBar.setLoginListener(new Runnable() {
			@Override
			public void run() {
				onLogin(UserNameED.getText().toString(), PasswordED.getText().toString());
			}
		});
		ButtonBar.setRegistListener(new Runnable() {
			@Override
			public void run() {
				Pager.setCurrentScreen(1);
			}
		});
		setContentView(Root);
		onThemeChange(UI.THEME_UI_COLOR);
	}
	private void initRegistLayout() {
		RegistLayout = new LinearLayout(this);
		RegistLayout.setOrientation(LinearLayout.VERTICAL);
		RegistLayout.setGravity(Gravity.CENTER_VERTICAL);
		LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(-1, -2);
		para.leftMargin = para.rightMargin = UI.dp2px(15);
		RegistText = new TextView(this);
		RegistText.setText(get(L.Regist));
		RegistText.setGravity(Gravity.LEFT);
		RegistText.setTextSize(25);
		RegistLayout.addView(RegistText, para);
		para = new LinearLayout.LayoutParams(-1, -2);
		para.topMargin = para.leftMargin = para.rightMargin = UI.dp2px(15);
		RegistUserNameED = new CircleEditText(this);
		RegistUserNameED.setIconResource(R.drawable.icon_username);
		RegistUserNameED.setSingleLine();
		RegistUserNameED.setHintTextColor(Color.GRAY);
		RegistUserNameED.setHint(get(L.UserName));
		RegistLayout.addView(RegistUserNameED, para);
		RegistPasswordED = new CircleEditText(this);
		RegistPasswordED.setIconResource(R.drawable.icon_key);
		RegistPasswordED.setSingleLine();
		RegistPasswordED.setTypeface(Typeface.MONOSPACE);
		RegistPasswordED.setHintTextColor(Color.GRAY);
		RegistPasswordED.setHint(get(L.Password));
		RegistPasswordED.setTransformationMethod(new PassWordTM());
		RegistLayout.addView(RegistPasswordED, para);
		RegistButtonBar = new LoginRegistBar(this);
		RegistButtonBar.setInLoginLayout(false);
		para = new LinearLayout.LayoutParams(-1, UI.dp2px(40) + LoginRegistBar.PAD * 2);
		para.leftMargin = para.topMargin = para.rightMargin = UI.dp2px(15);
		RegistLayout.addView(RegistButtonBar, para);
		RegistLayout.setVisibility(View.INVISIBLE);
		RegistButtonBar.setLoginListener(new Runnable() {
			@Override
			public void run() {
				Pager.setCurrentScreen(0);
			}
		});
		RegistButtonBar.setRegistListener(new Runnable() {
			@Override
			public void run() {
				onRegist();
			}
		});
	}
	private void onRegist() {
		final String strUserName=RegistUserNameED.getText().toString();
		final String strPassword=RegistPasswordED.getText().toString();
		if (strUserName == null || strUserName.length() == 0) {
			RegistUserNameED.setError(get(L.UserNameCannotEmpty));
			return;
		}
		if (strPassword == null || strPassword.length() == 0) {
			RegistPasswordED.setError(get(L.PasswordCannotEmpty));
			return;
		}
		if (strUserName.length() > Global.UserNameMaxLength) {
			RegistUserNameED.setError(String.format(get(L.UserNameExceed), Global.UserNameMaxLength));
			return;
		}
		if (strPassword.length() > Global.PasswordMaxLength) {
			RegistPasswordED.setError(String.format(get(L.PasswordExceed), Global.PasswordMaxLength));
			return;
		}
		VUser user=new VUser();
		user.setUsername(strUserName);
		user.setPassword(strPassword);
		final VProgressDialog dialog=ui.newProgressDialog();
		dialog.setTitle(get(L.Wait)).setMessage(get(L.Registing)).setCancelable(false).show();
		user.signUp(new SaveListener<VUser>() {
			@Override
			public void done(VUser user, BmobException e) {
				dialog.dismiss();
				if (e == null) {
					ui.print(get(L.RegistSuccess));
					onLogin(strUserName, strPassword);
				} else {
					Global.onBmobErr(ui, e);
				}
			}
		});
	}
	private void onLogin(String name, String pwd) {
		if (name == null || name.length() == 0) {
			ui.print(get(L.UserNameCannotEmpty));
			return;
		}
		if (pwd == null || pwd.length() == 0) {
			ui.print(get(L.PasswordCannotEmpty));
			return;
		}
		if (name.length() > Global.UserNameMaxLength) {
			ui.print(String.format(get(L.UserNameExceed), Global.UserNameMaxLength));
			return;
		}
		if (pwd.length() > Global.PasswordMaxLength) {
			ui.print(String.format(get(L.PasswordExceed), Global.PasswordMaxLength));
			return;
		}
		final VProgressDialog dialog=ui.newProgressDialog();
		dialog.setTitle(get(L.Wait)).setMessage(get(L.Logining)).setCancelable(false).show();
		BmobUser.loginByAccount(name, pwd, new LogInListener<VUser>() {
			@Override
			public void done(VUser user, BmobException e) {
				dialog.dismiss();
				if (e == null) {
					ui.print(get(L.Logined));
					MainActivity.onUserStateChanged();
					finish();
				} else {
					if (e.getErrorCode() == 205 || e.getErrorCode() == 101) {
						ui.print(get(L.InvalidUserNameOrPassword));
					} else Global.onBmobErr(ui, e);
				}
			}
		});
	}
	public static class PassWordTM extends PasswordTransformationMethod {
		@Override
		public CharSequence getTransformation(final CharSequence cs, View view) {
			return new CharSequence() {
				@Override
				public int length() {
					return cs.length();
				}
				@Override
				public char charAt(int index) {
					return '·';
				}
				@Override
				public CharSequence subSequence(int start, int end) {
					return cs.subSequence(start, end);
				}
			};
		}
	}
	@Override
	public void onThemeChange(String key) {
		super.onThemeChange(key);
		int w=UI.getAccentColor();
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		if (Helper != null) Helper.onThemeChange();
		Root.setBackgroundColor(UI.getThemeColor());
		LoginText.setTextColor(w);
		RegistText.setTextColor(w);
		UserNameED.setTextColor(w);
		RegistUserNameED.setTextColor(w);
		PasswordED.setTextColor(w);
		RegistPasswordED.setTextColor(w);
		ButtonBar.onThemeChange();
		RegistButtonBar.onThemeChange();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		onConfigChange();
	}
	private void onConfigChange() {
		float pro=Helper == null ?0: Helper.getProgress();
		if (Helper != null) Helper.remove();
		Display dis=getWindowManager().getDefaultDisplay();
		boolean vertical=getResources().getConfiguration().orientation == 1;
		Helper = (vertical ?new VerticalLoginView(this): new HorizontalLoginView(this));
		Helper.setProgress(pro);
		Root.removeView(InfoLayout);
		Root.removeView(Pager);
		((TextView) InfoLayout.getChildAt(1)).setTextColor(UI.getThemeColor());
		Root.addView(InfoLayout, vertical ?new RelativeLayout.LayoutParams(-1, dis.getHeight() / 2): new RelativeLayout.LayoutParams(dis.getWidth() / 2, -1));
		RelativeLayout.LayoutParams para=new RelativeLayout.LayoutParams(0, 0);
		para.width = dis.getWidth() / (vertical ?1: 2);
		para.height = dis.getHeight() / (vertical ?2: 1);
		para.addRule(vertical ?RelativeLayout.ALIGN_PARENT_BOTTOM: RelativeLayout.ALIGN_PARENT_RIGHT);
		Root.addView(Pager, para);
		if (Helper != null) Helper.onConfigChange();
	}
	public static abstract class LoginViewHelper implements Animator.AnimatorListener {
		public abstract void setProgress(float pro)
		public abstract float getProgress()
		public abstract void onThemeChange()
		public abstract void onConfigChange()
		private Runnable _OnAnimationEndListener;
		public final void setOnAnimationEndListener(Runnable action) {
			_OnAnimationEndListener = action;
		}
		public final Runnable getOnAnimationEndListener() {
			return _OnAnimationEndListener;
		}
		public final void swipeDown(int duration) {
			ValueAnimator ani=ValueAnimator.ofFloat(0, 1);
			ani.setDuration(duration);
			ani.setStartDelay(500);
			ani.setInterpolator(new DecelerateInterpolator());
			ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator ani) {
					setProgress((float) ani.getAnimatedValue());
				}
			});
			ani.addListener(this);
			ani.start();
		}
		@Override
		public void onAnimationStart(Animator ani) {}
		@Override
		public void onAnimationRepeat(Animator ani) {}
		@Override
		public void onAnimationCancel(Animator ani) {}
		@Override
		public void onAnimationEnd(Animator ani) {
			if (_OnAnimationEndListener != null) _OnAnimationEndListener.run();
		}
		public abstract void remove()
	}
	public static class VerticalLoginView extends LoginViewHelper {
		private LoginActivity cx;
		private ArrowView _View;
		private float _Progress;
		public VerticalLoginView(LoginActivity cx) {
			this.cx = cx;
			_View = new ArrowView(cx);
			cx.Root.addView(_View, new RelativeLayout.LayoutParams(-1, cx.getWindowManager().getDefaultDisplay().getHeight() / 2 + ARROW_SIZE));
		}
		@Override
		public void onConfigChange() {
			_View.onConfigChange();
		}
		@Override
		public void remove() {
			cx.Root.removeView(_View);
		}
		@Override
		public void setProgress(float pro) {
			_Progress = pro;
			_View.setProgress(pro);
		}
		@Override
		public float getProgress() {
			return _Progress;
		}
		@Override
		public void onThemeChange() {
			_View.onThemeChange();
		}
		public static class ArrowView extends View {
			private Paint _Paint;
			private Path ArrowPath;
			private float pro=0;
			public ArrowView(Context cx) {
				super(cx);
				setLayerType(LAYER_TYPE_SOFTWARE, null);
				_Paint = new Paint();
				_Paint.setStyle(Paint.Style.FILL);
				_Paint.setAntiAlias(true);
				_Paint.setColor(UI.getAccentColor());
				_Paint.setShadowLayer(10, 0, 3, Color.GRAY);
			}
			public void setProgress(float pro) {
				this.pro = pro;
				invalidate();
			}
			public void onConfigChange() {
				ArrowPath = null;
				invalidate();
			}
			@Override
			protected void onDraw(Canvas canvas) {
				canvas.translate(0, -(getHeight() - ARROW_SIZE) * (1 - pro));
				if (ArrowPath == null) {
					ArrowPath = new Path();
					int c=getWidth() / 2;
					ArrowPath.moveTo(0, 0);
					ArrowPath.lineTo(getWidth(), 0);
					ArrowPath.lineTo(getWidth(), getHeight() - ARROW_SIZE);
					ArrowPath.lineTo(c + ARROW_SIZE, getHeight() - ARROW_SIZE);
					ArrowPath.lineTo(c, getHeight());
					ArrowPath.lineTo(c - ARROW_SIZE, getHeight() - ARROW_SIZE);
					ArrowPath.lineTo(0, getHeight() - ARROW_SIZE);
					ArrowPath.close();
				}
				canvas.drawPath(ArrowPath, _Paint);
			}
			public void onThemeChange() {
				_Paint.setColor(UI.getAccentColor());
			}
		}
	}
	public static class HorizontalLoginView extends LoginViewHelper {
		private LoginActivity cx;
		private float _Progress;
		private ArrowView _View;
		public HorizontalLoginView(LoginActivity cx) {
			this.cx = cx;
			_View = new ArrowView(cx);
			cx.Root.addView(_View, new RelativeLayout.LayoutParams(cx.getWindowManager().getDefaultDisplay().getWidth() / 2 + ARROW_SIZE, -1));
		}
		@Override
		public void setProgress(float pro) {
			_Progress = pro;
			_View.setProgress(pro);
		}
		@Override
		public void onConfigChange() {
			_View.onConfigChange();
		}
		@Override
		public float getProgress() {
			return _Progress;
		}
		@Override
		public void remove() {
			cx.Root.removeView(_View);
		}
		@Override
		public void onThemeChange() {
			_View.onThemeChange();
		}
		public static class ArrowView extends View {
			private Paint _Paint;
			private Path ArrowPath;
			private float pro=0;
			public ArrowView(Context cx) {
				super(cx);
				setLayerType(LAYER_TYPE_SOFTWARE, null);
				_Paint = new Paint();
				_Paint.setStyle(Paint.Style.FILL);
				_Paint.setAntiAlias(true);
				_Paint.setColor(UI.getAccentColor());
				_Paint.setShadowLayer(10, 3, 0, Color.GRAY);
			}
			public void setProgress(float pro) {
				this.pro = pro;
				invalidate();
			}
			@Override
			protected void onDraw(Canvas canvas) {
				canvas.translate(-(getWidth() - ARROW_SIZE) * (1 - pro), 0);
				if (ArrowPath == null) {
					ArrowPath = new Path();
					int c=getHeight() / 2;
					ArrowPath.moveTo(0, 0);
					ArrowPath.lineTo(getWidth() - ARROW_SIZE, 0);
					ArrowPath.lineTo(getWidth() - ARROW_SIZE, c - ARROW_SIZE);
					ArrowPath.lineTo(getWidth(), c);
					ArrowPath.lineTo(getWidth() - ARROW_SIZE, c + ARROW_SIZE);
					ArrowPath.lineTo(getWidth() - ARROW_SIZE, getHeight());
					ArrowPath.lineTo(0, getHeight());
					ArrowPath.close();
				}
				canvas.drawPath(ArrowPath, _Paint);
			}
			public void onConfigChange() {
				ArrowPath = null;
				invalidate();
			}
			public void onThemeChange() {
				_Paint.setColor(UI.getAccentColor());
			}
		}
	}
}
