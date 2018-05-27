package com.jxs.vide;

import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v4.view.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import cn.bmob.v3.*;
import cn.bmob.v3.datatype.*;
import cn.bmob.v3.exception.*;
import cn.bmob.v3.listener.*;
import com.jxs.vcompat.activity.*;
import com.jxs.vcompat.animation.*;
import com.jxs.vcompat.drawable.*;
import com.jxs.vcompat.ui.*;
import com.jxs.vcompat.widget.*;
import java.io.*;

import android.support.v7.widget.Toolbar;

import static com.jxs.vide.L.get;

public class UploadVAppActivity extends VActivity {
	private LinearLayout Root;
	private AppBarLayout BarLayout;
	private Toolbar Title;
	private ProjectViewHelper VH;
	private VEditText Des;
	private View LastView;
	private Project Choosed;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initToolbar();
		initView();
		setContentView(Root);
	}
	private void initView() {
		Root = new LinearLayout(this);
		Root.setOrientation(LinearLayout.VERTICAL);
		Root.addView(BarLayout);
		VH = new ProjectViewHelper(this);
		VH.setOnProjectClickListener(new ProjectViewHelper.OnProjectClickListener() {
				@Override
				public void onClick(Project pro, final View view) {
					if (view == LastView) return;
					if (LastView != null) LastView.setAlpha(1);
					LastView = view;
					view.setAlpha(0.77f);
					Choosed = pro;
					updateTitle();
				}
			});
		Root.addView(VH.getView(), new LinearLayout.LayoutParams(-1, -1));
	}
	private void updateTitle() {
		if (Choosed == null) {
			Title.setTitle(get(L.Title_UploadVApp));
			return;
		}
		Title.setTitle(String.format("%s [%s]", get(L.Title_UploadVApp), Choosed.getName()));
	}
	private CheckBox Open;
	private TextView OpenDes;
	private void initToolbar() {
		BarLayout = new AppBarLayout(this);
		Title = new Toolbar(this);
		Title.setTitle(get(L.Title_UploadVApp));
		Title.setBackgroundColor(UI.getThemeColor());
		BarLayout.setBackgroundColor(UI.getThemeColor());
		int w=UI.getAccentColor();
		Title.setTitleTextColor(w);
		BarLayout.addView(Title);
		setSupportActionBar(Title);
		BDrawable BackDrawable = new ArrowShape(5f).toSimpleDrawable();
		UI.tintDrawable(BackDrawable, UI.getAccentColor());
		BackDrawable.setStrokeWidth(5f);
		Title.setNavigationIcon(BackDrawable);
		ViewCompat.setElevation(BarLayout, 5f);
		Des = new VEditText(this);
		Des.setUnderLineColor(w);
		Des.setHint(get(L.Description));
		Des.setHintTextColor(w);
		Des.setTextColor(w);
		BarLayout.addView(Des);
		Open = new CheckBox(this);
		Open.setChecked(false);
		try {
			Open.setButtonTintList(ColorStateList.valueOf(UI.getAccentColor()));
		} catch (Throwable t) {}
		OpenDes = new TextView(this);
		OpenDes.setTextColor(UI.getAccentColor());
		OpenDes.setText(get(L.OpenSource));
		LinearLayout OpenSource=new LinearLayout(this);
		OpenSource.setOrientation(LinearLayout.HORIZONTAL);
		OpenSource.addView(Open, new LinearLayout.LayoutParams(-2, -2));
		OpenSource.addView(OpenDes);
		BarLayout.addView(OpenSource);
	}
	@Override
	public void onThemeChange(String key) {
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		super.onThemeChange(key);
		int w=UI.getAccentColor();
		Des.setUnderLineColor(w);
		Des.setHintTextColor(w);
		Des.setTextColor(w);
		BarLayout.setBackgroundColor(UI.getThemeColor());
		OpenDes.setTextColor(w);
		try {
			Open.setButtonTintList(ColorStateList.valueOf(w));
		} catch (Throwable t) {}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, 1, 1, get(L.Title_UploadVApp)).setIcon(R.drawable.icon_done).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}
	private RevealAnimation Ani;
	private TextView CenterText;
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getOrder()) {
			case 1:{
					if (Ani != null) break;
					if (Choosed == null) {
						ui.print(get(L.DidnotChoosed));
						break;
					}
					Des.setError(null);
					String s=Des.getText().toString();
					if (s == null || s.length() == 0) {
						Des.setError(get(L.DesCannotBeEmpty));
						break;
					}
					if (s.length() > Global.DescriptionMaxLength) {
						Des.setError(String.format(get(L.DesExceed), Global.DescriptionMaxLength));
						break;
					}
					CenterText = new TextView(this);
					CenterText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
					CenterText.setText(get(L.Wait));
				 	CenterText.setTextColor(UI.getAccentColor());
					FrameLayout.LayoutParams para=new FrameLayout.LayoutParams(-2, -2);
					para.gravity = Gravity.CENTER;
					Ani = new RevealAnimation(Root, getWindowManager().getDefaultDisplay().getWidth() - UI.dp2px(16), UI.dp2px(16), UI.getThemeColor());
					Ani.setInnerView(CenterText, para);
					Ani.setDuration(700);
					Ani.start();
					new Thread(new UploadRunnable(DialogHandler, Choosed, s, Open.isChecked())).start();
				}
		}
		return super.onOptionsItemSelected(item);
	}
	private Handler DialogHandler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					if (CenterText != null) CenterText.setText(((String) msg.obj));
					break;
				case 1:
					if (Ani != null) Ani.postAnimationEndAction(new Runnable() {
								@Override
								public void run() {
									Ani.collapse();
								}
							});
					break;
				case 2:
					CharSequence[] cs=(CharSequence[]) msg.obj;
					ui.alert(cs[0], cs[1]);
					break;
				case 3:
					Global.onBmobErr(ui, (BmobException) msg.obj);
					break;
				case 4:
					finish();
					VAppActivity.showMessage(get(L.Uploaded));
					VAppActivity.refresh();
					break;
			}
		}
	};
	private static class UploadRunnable implements Runnable {
		private Handler H;
		private Project Pro;
		private String Des;
		private boolean open;
		public UploadRunnable(Handler dialogH, Project Projec, String des, boolean open) {
			this.H = dialogH;
			this.Pro = Projec;
			this.Des = des;
			this.open = open;
		}
		private void sendMsg(int what, Object obj) {
			Message q=new Message();
			q.what = what;
			q.obj = obj;
			H.sendMessage(q);
		}
		private void setText(String s) {
			sendMsg(0, s);
		}
		private void dismiss() {
			sendMsg(1, null);
		}
		private void alert(CharSequence title, CharSequence msg) {
			sendMsg(2, new CharSequence[]{title,msg});
		}
		private void bmobErr(BmobException e) {
			sendMsg(3, e);
		}
		@Override
		public void run() {
			setText(get(L.Compiling));
			File tmpJsc=null;
			try {
				Pro.loadManifest();
				tmpJsc = File.createTempFile("Tmp", ".jsc");
				tmpJsc.deleteOnExit();
				Pro.compile(new FileOutputStream(tmpJsc), true);
				setText(get(L.Uploading));
				final BmobFile FileEntity=new BmobFile(tmpJsc);
				FileEntity.upload(new UploadFileListener() {
						@Override
						public void done(BmobException e) {
							if (e != null) {
								dismiss();
								bmobErr(e);
								return;
							}
							VAppEntity entity=new VAppEntity();
							entity.Author = BmobUser.getCurrentUser(VUser.class).getObjectId();
							entity.Content = FileEntity;
							entity.Description = Des;
							entity.Title = Pro.getName();
							entity.OpenSource = open;
							entity.save(new SaveListener<String>() {
									@Override
									public void done(String id, BmobException e) {
										if (e != null) {
											dismiss();
											bmobErr(e);
											return;
										}
										dismiss();
										sendMsg(4, null);
									}
								});
						}
					});
			} catch (Throwable e) {
				dismiss();
				alert(get(L.Error), Log.getStackTraceString(e));
			}
		}
	}
}
