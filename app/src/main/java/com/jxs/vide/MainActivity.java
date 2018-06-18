package com.jxs.vide;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v4.view.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.support.v7.graphics.drawable.*;
import android.support.v7.widget.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.widget.*;
import android.widget.AdapterView.*;
import cn.bmob.v3.*;
import cn.bmob.v3.exception.*;
import cn.bmob.v3.listener.*;
import com.jxs.vapp.program.*;
import com.jxs.vcompat.activity.*;
import com.jxs.vcompat.ui.*;
import com.jxs.vcompat.widget.*;
import java.io.*;
import java.util.*;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View.OnClickListener;

import static com.jxs.vide.L.get;

public class MainActivity extends VActivity {
	public static final int LOGIN_PAGE=0,USERINFO_PAGE=1;
	public static final Object LOGOUT_TAG="Logout";
	public static final int DRAWER_WIDTH=UI.dp2px(300);
	public static MainActivity cx;
	private Toolbar Title;
	private AppBarLayout TitleLayout;
	private DrawerLayout Drawer;
	private LinearLayout Content,ItemLayout;
	private RelativeLayout Root;
	private FloatingActionButton FAB;
	private ProjectViewHelper VH;
	private SharedPreferences Pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		cx = this;
		Pref = getSharedPreferences("Config", MODE_PRIVATE);
		super.onCreate(savedInstanceState);
		Global.checkBmob(this);
		initProjectsView();
		initDrawer();
		updateUser();
		setContentView(Drawer);
		//Signature Check
		if (getSignature(this) != 1484347571 || getSignature(this) + 5 != 1484347576 || !((getSignature(this) + "").equals("1484347571"))) {
			ui.newAlertDialog().setTitle(new String(new byte[]{-24, -83, -90, -27, -111, -118})).setMessage(new String(new byte[]{-28, -67, -96, -26, -119, -128, -28, -72, -117, -24, -67, -67, -25, -102, -124, -24, -67, -81, -28, -69, -74, -26, -104, -81, -24, -94, -85, -23, -121, -115, -25, -83, -66, -27, -112, -115, -28, -65, -82, -26, -108, -71, -25, -102, -124, -25, -101, -105, -25, -119, -120, 10, -24, -81, -73, -24, -127, -108, -25, -77, -69, -28, -67, -100, -24, -128, -123, 81, 81, 50, 53, 48, 56, 53, 49, 48, 52, 56, -26, -99, -91, -24, -114, -73, -27, -113, -106, -26, -83, -93, -25, -119, -120, -24, -67, -81, -28, -69, -74})).setPositiveButton(new String(new byte[]{-25, -95, -82, -27, -82, -102}), new VAlertDialog.OnClickListener() {
					@Override
					public void onClick(VAlertDialog dialog, int pos) {
						android.os.Process.killProcess(android.os.Process.myPid());
						System.exit(0);
					}
				}).setOnCancelListener(new VAlertDialog.OnCancelListener() {
					@Override
					public void onCancel(VAlertDialog dialog) {
						android.os.Process.killProcess(android.os.Process.myPid());
						System.exit(0);
					}
				}).setCancelable(true).show();
		}
		try {
			Class.forName(new String(new byte[]{99, 99, 46, 98, 105, 110, 109, 116, 46, 115, 105, 103, 110, 97, 116, 117, 114, 101, 46, 80, 109, 115, 72, 111, 111, 107, 65, 112, 112, 108, 105, 99, 97, 116, 105, 111, 110}));
			ui.newAlertDialog().setTitle(new String(new byte[]{-24, -83, -90, -27, -111, -118})).setMessage(new String(new byte[]{-28, -67, -96, -27, -66, -120, -27, -92, -87, -25, -100, -97, -27, -107, -118, 10, -28, -67, -96, -25, -100, -97, -25, -102, -124, -24, -82, -92, -28, -72, -70, -27, -113, -86, -23, -100, -128, -24, -90, -127, -25, -108, -88, 77, 84, -25, -82, -95, -25, -112, -122, -27, -103, -88, -27, -123, -115, -25, -83, -66, -27, -112, -115, -23, -86, -116, -24, -81, -127, -27, -80, -79, -27, -113, -81, -28, -69, -91, -25, -96, -76, -24, -89, -93, 86, 73, 68, 69, -27, -112, -105, -17, -68, -97, 10, -27, -81, -71, -28, -70, -114, -25, -108, -88, -26, -120, -73, 10, -28, -67, -96, -26, -119, -128, -28, -67, -65, -25, -108, -88, -25, -102, -124, 86, 73, 68, 69, -26, -104, -81, -24, -94, -85, -28, -69, -106, -28, -70, -70, -28, -65, -82, -26, -108, -71, -24, -65, -121, -25, -102, -124, -25, -101, -105, -25, -119, -120, 10, -24, -81, -73, -24, -127, -108, -25, -77, -69, -28, -67, -100, -24, -128, -123, 81, 81, 50, 53, 48, 56, 53, 49, 48, 52, 56, -26, -99, -91, -24, -114, -73, -27, -113, -106, -26, -83, -93, -25, -119, -120})).setPositiveButton(new String(new byte[]{-25, -95, -82, -27, -82, -102}), new VAlertDialog.OnClickListener() {
					@Override
					public void onClick(VAlertDialog dialog, int pos) {
						android.os.Process.killProcess(android.os.Process.myPid());
						System.exit(0);
					}
				}).setOnCancelListener(new VAlertDialog.OnCancelListener() {
					@Override
					public void onCancel(VAlertDialog dialog) {
						android.os.Process.killProcess(android.os.Process.myPid());
						System.exit(0);
					}
				}).setCancelable(true).show();
		} catch (Exception e) {}
		int c=Pref.getInt("Count", 0);
		if (c != -1) {
			if (c != 0 && (c % 4 == 0)) {
				shareMe(this);
				Pref.edit().putInt("Count", -1).commit();
				c = 0;
			}
			Pref.edit().putInt("Count", ++c).commit();
		}
		checkUpdate();
	}
	public static void shareMe(final VActivity ac) {
		ac.ui.newAlertDialog().setTitle(get(L.Please)).setMessage(get(L.ShareApp)).setCancelable(false).setPositiveButton(get(L.Share), new VAlertDialog.OnClickListener() {
				@Override
				public void onClick(VAlertDialog dialog, int pos) {
					Intent in=new Intent(Intent.ACTION_SEND);
					in.setType("text/plain");
					in.putExtra(Intent.EXTRA_SUBJECT, "VIDE");
					in.putExtra(Intent.EXTRA_TEXT, get(L.ShareContent));
					in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					ac.startActivity(Intent.createChooser(in, get(L.Share)));
				}
			}).setNegativeButton(get(L.AppMarket), new VAlertDialog.OnClickListener() {
				@Override
				public void onClick(VAlertDialog dialog, int pos) {
					Intent in=new Intent(Intent.ACTION_VIEW);
					in.setData(Uri.parse("market://details?id=com.jxs.vide"));
					in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					ac.startActivity(in);
				}
			}).setNeutralButton(get(L.Nop), new VAlertDialog.OnClickListener() {
				@Override
				public void onClick(VAlertDialog dialog, int pos) {
					final Snackbar bar=Snackbar.make(ac.findViewById(android.R.id.content), get(L.Badguy), Snackbar.LENGTH_SHORT);
					bar.setAction(get(L.IRepented), new OnClickListener() {
							@Override
							public void onClick(View v) {
								shareMe(ac);
								bar.dismiss();
							}
						});
					bar.setActionTextColor(UI.getThemeColor());
					bar.show();
				}
			}).show();
	}
	public static void onUserStateChanged() {
		if (MainActivity.cx != null) MainActivity.cx.updateUser();
	}
	private void updateUser() {
		VUser _User=BmobUser.getCurrentUser(VUser.class);
		UserLayout.setVisibility(View.GONE);
		LoginLayout.setVisibility(View.GONE);
		if (_User == null) {
			LoginLayout.setVisibility(View.VISIBLE);
		} else {
			UserLayout.setVisibility(View.VISIBLE);
			_User.downloadIcon(new VUser.IconListener() {
					@Override
					public void onDone(Bitmap b) {
						UserIcon.setTag(0);
						UserIcon.setImageBitmap(b);
					}
				});
			UserName.setText(_User.getUsername());
		}
	}
	private ActionBarDrawerToggle DrawerToggle;
	private FloatingActionButton FABCreateSimple,FABCreateConsole;
	private FloatingActionButton[] AllFAB;
	private static final int P=UI.dp2px(32);
	private static final int FS=UI.dp2px(64);
	private static final int SPLIT=UI.dp2px(12);
	private void initProjectsView() {
		Root = new RelativeLayout(this);
		VH = new ProjectViewHelper(this);
		VH.setOnProjectClickListener(new ProjectViewHelper.OnProjectClickListener() {
				@Override
				public void onClick(Project pro, View view) {
					startActivity(new Intent(MainActivity.this, ProjectActivity.class).putExtra("project", pro.getName()));
				}
			});
		Root.addView(VH.getView(), new RelativeLayout.LayoutParams(-1, -1));
		FAB = new FloatingActionButton(this);
		FABCreateSimple = new FloatingActionButton(this);
		FABCreateConsole = new FloatingActionButton(this);
		FAB.setImageDrawable(DrawableHelper.getDrawable(R.drawable.v_add, UI.getAccentColor()));
		FABCreateSimple.setImageDrawable(DrawableHelper.getDrawable(R.drawable.icon_activity, UI.getAccentColor()));
		FABCreateConsole.setImageDrawable(DrawableHelper.getDrawable(R.drawable.icon_console, UI.getAccentColor()));
		try {
			ColorStateList s=ColorStateList.valueOf(UI.getThemeColor());
			FAB.setBackgroundTintList(s);
			FABCreateSimple.setBackgroundTintList(s);
			FABCreateConsole.setBackgroundTintList(s);
		} catch (Exception e) {}
		AllFAB = new FloatingActionButton[] {FABCreateSimple,FABCreateConsole};
		RelativeLayout.LayoutParams para;
		for (int i=0;i < AllFAB.length;i++) {
			para = new RelativeLayout.LayoutParams(FS, FS);
			para.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			para.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			para.rightMargin = para.bottomMargin = P;
			para.bottomMargin += (i + 1) * (FS + SPLIT);
			AllFAB[i].setVisibility(View.GONE);
			Root.addView(AllFAB[i], para);
		}
		para = new RelativeLayout.LayoutParams(FS, FS);
		para.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		para.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		para.rightMargin = para.bottomMargin = P;
		Root.addView(FAB, para);
		para = new RelativeLayout.LayoutParams(-1, -1);
		para.addRule(RelativeLayout.CENTER_IN_PARENT);
		FAB.setOnClickListener(new OnClickListener() {
				private boolean Rotate=false;
				@Override
				public void onClick(View v) {
					RotateAnimation ani=new RotateAnimation(Rotate ?225: 0, Rotate ?0: 225, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
					ani.setDuration(500);
					ani.setFillAfter(true);
					ani.setInterpolator(new AccelerateDecelerateInterpolator());
					for (int i=0;i < AllFAB.length;i++) {
						AlphaAnimation alpha=new AlphaAnimation(Rotate ?1: 0, Rotate ?0: 1);
						alpha.setDuration(300);
						FAB.startAnimation(ani);
						if (!Rotate) AllFAB[i].setVisibility(View.VISIBLE); else alpha.setAnimationListener(new Animation.AnimationListener() {
									@Override
									public void onAnimationEnd(Animation ani) {
										FABCreateSimple.setVisibility(View.GONE);
										FABCreateConsole.setVisibility(View.GONE);
									}
									@Override
									public void onAnimationStart(Animation ani) {}
									@Override
									public void onAnimationRepeat(Animation ani) {}
								});
						AllFAB[i].startAnimation(alpha);
						AllFAB[i].setVisibility(View.VISIBLE);
					}
					Rotate = !Rotate;
				}
			});
		FABCreateSimple.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					createUI(true);
					FAB.performClick();
				}
			});
		FABCreateConsole.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					createUI(false);
					FAB.performClick();
				}
			});
	}
	private LinearLayout LoginLayout;
	private RelativeLayout UserLayout;
	private void initDrawer() {
		Content = new LinearLayout(this);
		Content.setOrientation(LinearLayout.VERTICAL);
		Title = new Toolbar(this);
		Title.setTitle(getResources().getString(R.string.app_name));
		setSupportActionBar(Title);
		Title.setBackgroundColor(UI.getThemeColor());
		Title.setTitleTextColor(UI.getAccentColor());
		TitleLayout = new AppBarLayout(this);
		TitleLayout.addView(Title);
		Content.addView(TitleLayout);
		Content.addView(Root, new LinearLayout.LayoutParams(-1, -1));
		ViewCompat.setElevation(TitleLayout, 10f);
		Drawer = new DrawerLayout(this);
		Drawer.addView(Content);
		DrawerLayout.LayoutParams para=new DrawerLayout.LayoutParams(DRAWER_WIDTH, -1);
		int margin=UI.dp2px(8);
		para.setMargins(margin, margin, margin, margin);
		para.gravity = Gravity.LEFT;
		Drawer.addView(getDrawerView(), para);
		DrawerToggle = new ActionBarDrawerToggle(this, Drawer, Title, R.string.drawer_open, R.string.drawer_close);
		DrawerToggle.syncState();
		Drawer.setDrawerListener(DrawerToggle);
		((DrawerArrowDrawable) Title.getNavigationIcon()).setColor(UI.getAccentColor());
	}
	private GradientView DrawerGradient;
	private ImageView UserIcon;
	private TextView UserName;
	private RelativeLayout InfoLayout;
	public static final int ICON_SIZE=UI.dp2px(64);
	private View getDrawerView() {
		int w=UI.getAccentColor();
		RoundLinearLayout layout=new RoundLinearLayout(this);
		layout.setRadius(5f);
		layout.setBackgroundColor(UI.getThemeColor());
		layout.setOrientation(LinearLayout.VERTICAL);
		InfoLayout = new RelativeLayout(this);
		InfoLayout.setBackgroundResource(R.drawable.drawer_background);
		layout.addView(InfoLayout, new LinearLayout.LayoutParams(DRAWER_WIDTH, DRAWER_WIDTH));
		DrawerGradient = new GradientView(this);
		InfoLayout.addView(DrawerGradient, new RelativeLayout.LayoutParams(-1, -1));
		LoginLayout = new LinearLayout(this);
		LoginLayout.setOrientation(LinearLayout.VERTICAL);
		LoginLayout.setGravity(Gravity.CENTER);
		LoginLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(MainActivity.this, LoginActivity.class));
				}
			});
		ImageView UnknownIcon=new ImageView(this);
		UnknownIcon.setImageDrawable(ui.tintDrawable(R.drawable.icon_user, w));
		LoginLayout.addView(UnknownIcon, new LinearLayout.LayoutParams(ICON_SIZE, ICON_SIZE));
		TextView Login=new TextView(this);
		Login.setText(get(L.Login));
		Login.setGravity(Gravity.CENTER);
		Login.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 27);
		Login.setTextColor(w);
		LinearLayout.LayoutParams LoginTextParams=new LinearLayout.LayoutParams(-1, -2);
		LoginTextParams.topMargin = UI.dp2px(20);
		LoginLayout.addView(Login, LoginTextParams);
		InfoLayout.addView(LoginLayout, new RelativeLayout.LayoutParams(-1, -1));
		UserLayout = new RelativeLayout(this);
		LinearLayout UserContentLayout = new LinearLayout(this);
		UserContentLayout.setGravity(Gravity.CENTER);
		UserContentLayout.setOrientation(LinearLayout.VERTICAL);
		UserIcon = new ImageView(this);
		UserIcon.setTag(-1);
		UserIcon.setImageDrawable(ui.tintDrawable(R.drawable.icon_user, w));
		UserContentLayout.addView(UserIcon, new LinearLayout.LayoutParams(ICON_SIZE, ICON_SIZE));
		UserName = new TextView(this);
		UserName.setGravity(Gravity.CENTER);
		UserName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 27);
		UserName.setTextColor(w);
		UserContentLayout.addView(UserName, LoginTextParams);
		ImageView Logout=new ImageView(this);
		Logout.setImageResource(R.drawable.icon_logout);
		Logout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					BmobUser.logOut();
					fadeFromUserToLogin();
				}
			});
		UserLayout.addView(UserContentLayout, new RelativeLayout.LayoutParams(-1, -1));
		RelativeLayout.LayoutParams LogoutPara=new RelativeLayout.LayoutParams(UI.dp2px(32), UI.dp2px(32));
		LogoutPara.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		LogoutPara.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		LogoutPara.rightMargin = LogoutPara.bottomMargin = UI.dp2px(16);
		UserLayout.addView(Logout, LogoutPara);
		InfoLayout.addView(UserLayout, new RelativeLayout.LayoutParams(-1, -1));
		layout.setClickable(true);
		ItemLayout = new LinearLayout(this);
		ItemLayout.setOrientation(LinearLayout.VERTICAL);
		//Items - Start
		ItemLayout.addView(newItemView(R.drawable.icon_book, get(L.Title_Learn), new StartActivityListener(LearnListActivity.class)));
		ItemLayout.addView(newItemView(R.drawable.icon_news, get(L.Notice), new OnClickListener() {
								   @Override
								   public void onClick(View v) {
									   noticeUi();
								   }
							   }));
		ItemLayout.addView(newItemView(R.drawable.icon_app, get(L.Title_VApp), new StartActivityListener(VAppActivity.class)));
		ItemLayout.addView(newItemView(R.drawable.icon_settings, get(L.Title_Setting), new StartActivityListener(SettingActivity.class)));
		ItemLayout.addView(newItemView(R.drawable.v_about, get(L.Title_About), new StartActivityListener(AboutActivity.class)));
		ItemLayout.addView(newItemView(R.drawable.icon_exit, get(L.Exit), new OnClickListener() {
								   @Override
								   public void onClick(View v) {
									   killAll();
								   }
							   }));
		//Items - End
		VScrollView ItemScroller=new VScrollView(this);
		ItemScroller.setFillViewport(true);
		ItemScroller.addView(ItemLayout, new VScrollView.LayoutParams(-1, -1));
		layout.addView(ItemScroller, new LinearLayout.LayoutParams(-1, -1));
		return layout;
	}
	private VListView NoticeList;
	private NoticeAdapter NoticeAdapter;
	private SwipeRefreshLayout NoticeRefresh;
	private RelativeLayout NoticeLayout;
	private CryView NoticeCry;
	private void noticeUi() {
		if (NoticeList != null) return;
		NoticeLayout = new RelativeLayout(this);
		NoticeCry = new CryView(this);
		NoticeCry.setText(get(L.HereIsEmpty));
		NoticeCry.setVisibility(View.GONE);
		NoticeLayout.addView(NoticeCry, -1, -1);
		NoticeList = new VListView(this);
		NoticeAdapter = new NoticeAdapter(this);
		NoticeList.setAdapter(NoticeAdapter);
		NoticeList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
					final NoticeEntity en=NoticeAdapter.get(pos);
					ui.newAlertDialog().setTitle(en.Title).setMessage(en.Message).setCancelable(true).setNeutralButton(UI.getColorString(en.Author, Color.GRAY), false, null).setPositiveButton(get(L.OK), null).setNegativeButton(get(L.Copy), new VAlertDialog.OnClickListener() {
							@Override
							public void onClick(VAlertDialog dialog, int pos) {
								((android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setText(en.Message);
								ui.print(get(L.Copied));
							}
						}).show();
				}
			});
		NoticeRefresh = new SwipeRefreshLayout(this);
		NoticeRefresh.setColorSchemeColors(new int[]{UI.getThemeColor()});
		NoticeRefresh.addView(NoticeList);
		NoticeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				@Override
				public void onRefresh() {
					loadNoticeData();
				}
			});
		NoticeLayout.addView(NoticeRefresh, -1, -1);
		NoticeList.setVisibility(View.GONE);
		ui.newAlertDialog().setView(NoticeLayout).setCancelable(true).setOnCancelListener(new VAlertDialog.OnCancelListener() {
				@Override
				public void onCancel(VAlertDialog dialog) {
					NoticeList = null;
					NoticeAdapter = null;
					NoticeRefresh = null;
					NoticeLayout = null;
					NoticeCry = null;
				}
			}).show();
		loadNoticeData();
	}
	private void loadNoticeData() {
		if (NoticeList == null) return;
		NoticeRefresh.setRefreshing(true);
		BmobQuery<NoticeEntity> q=new BmobQuery<>();
		q.order("-updatedAt");
		q.findObjects(new FindListener<NoticeEntity>() {
				@Override
				public void done(List<NoticeEntity> data, BmobException e) {
					if (NoticeList == null) return;
					if (e != null) {
						NoticeRefresh.setRefreshing(false);
						Global.onBmobErr(ui, e);
						return;
					}
					NoticeAdapter.clear();
					NoticeAdapter.addAll(data);
					if (data.size() == 0) {
						NoticeCry.setVisibility(View.VISIBLE);
						NoticeList.setVisibility(View.GONE);
					} else {
						NoticeCry.setVisibility(View.GONE);
						NoticeList.setVisibility(View.VISIBLE);
					}
					NoticeRefresh.setRefreshing(false);
				}
			});
	}
	private static class NoticeAdapter extends BaseAdapter {
		private Context cx;
		private ArrayList<NoticeEntity> All=new ArrayList<>();
		public NoticeAdapter(Context cx) {
			this.cx = cx;
		}
		@Override
		public Object getItem(int pos) {
			return All.get(pos);
		}
		@Override
		public int getCount() {
			return All.size();
		}
		public void add(NoticeEntity en) {
			All.add(en);
			notifyDataSetChanged();
		}
		public void remove(NoticeEntity en) {
			All.remove(en);
			notifyDataSetChanged();
		}
		public void remove(int index) {
			All.remove(index);
			notifyDataSetChanged();
		}
		public NoticeEntity get(int index) {
			return All.get(index);
		}
		public void addAll(Collection<NoticeEntity> ens) {
			All.addAll(ens);
			notifyDataSetChanged();
		}
		public void clear() {
			All.clear();
			notifyDataSetChanged();
		}
		@Override
		public long getItemId(int pos) {
			return pos;
		}
		@Override
		public View getView(int pos, View v, ViewGroup vg) {
			int t=UI.dp2px(10);
			LinearLayout root=new LinearLayout(cx);
			root.setPadding(t, t, t, t);
			root.setOrientation(LinearLayout.VERTICAL);
			TextView Title=new TextView(cx);
			Title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
			Title.setTextSize(30);
			TextView Content=new TextView(cx);
			Content.setEllipsize(TextUtils.TruncateAt.END);
			Content.setMaxLines(3);
			NoticeEntity en=All.get(pos);
			Title.setText(en.Title);
			Content.setText(en.Message);
			root.addView(Title);
			LinearLayout.LayoutParams para=new LinearLayout.LayoutParams(-1, -1);
			para.topMargin = t;
			root.addView(Content, para);
			CardView Card=new CardView(cx);
			Card.setRadius(10);
			Card.addView(root);
			ViewHolder holder=new ViewHolder();
			holder.Card = Card;
			holder.Title = Title;
			holder.Content = Content;
			holder.onThemeChange();
			LinearLayout R=new LinearLayout(cx);
			R.setTag(holder);
			para = new LinearLayout.LayoutParams(-1, -1);
			para.topMargin = para.bottomMargin = para.leftMargin = para.rightMargin = t;
			R.addView(Card, para);
			return R;
		}
		private static class ViewHolder {
			public CardView Card;
			public TextView Title,Content;
			public void onThemeChange() {
				Card.setBackgroundColor(UI.getThemeColor());
				Title.setTextColor(UI.getAccentColor());
				Content.setTextColor(UI.getAccentColor());
			}
		}
	}
	private void fadeFromUserToLogin() {
		AlphaAnimation ani=new AlphaAnimation(1, 0);
		ani.setDuration(700);
		ani.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation ani) {}
				@Override
				public void onAnimationRepeat(Animation ani) {}
				@Override
				public void onAnimationEnd(Animation ani) {
					UserLayout.setVisibility(View.GONE);
				}
			});
		ani.setFillAfter(false);
		UserLayout.startAnimation(ani);
		ani = new AlphaAnimation(0, 1);
		ani.setDuration(700);
		ani.setFillAfter(false);
		LoginLayout.setVisibility(View.VISIBLE);
		LoginLayout.startAnimation(ani);
	}
	public static class StartActivityListener implements OnClickListener {
		private Class<? extends Activity> _Class=null;
		public StartActivityListener(Class<? extends Activity> clz) {
			this._Class = clz;
		}
		@Override
		public void onClick(View v) {
			MainActivity.cx.startActivity(new Intent(MainActivity.cx, _Class));
		}
	}
	private ArrayList<LinearLayout> Items=new ArrayList<>();
	private View newItemView(int id, String title, OnClickListener click) {
		int w=UI.getAccentColor();
		LinearLayout layout=new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		int p=UI.dp2px(12);
		layout.setPadding(p, p, p * 2, p);
		ImageView icon=new ImageView(this);
		icon.setImageDrawable(DrawableHelper.getDrawable(id, w));
		LinearLayout.LayoutParams para=new LinearLayout.LayoutParams(UI.dp2px(24), ui.dp2px(24));
		para.rightMargin = UI.dp2px(10);
		layout.addView(icon, para);
		TextView t=new TextView(this);
		t.setGravity(Gravity.LEFT);
		t.setTextColor(w);
		t.setText(title);
		t.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
		layout.addView(t);
		layout.setClickable(true);
		TypedValue value=new TypedValue();
		getTheme().resolveAttribute(android.R.attr.selectableItemBackground, value, true);
		TypedArray arr=getTheme().obtainStyledAttributes(value.resourceId, new int[] {android.R.attr.selectableItemBackground});
		layout.setBackground(arr.getDrawable(0));
		arr.recycle();
		layout.setOnClickListener(click);
		Items.add(layout);
		return layout;
	}
	@Override
	public void onThemeChange(String key) {
		super.onThemeChange(key);
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		int w=UI.getAccentColor();
		((DrawerArrowDrawable) Title.getNavigationIcon()).setColor(UI.getAccentColor());
		Drawer.getChildAt(1).setBackgroundColor(UI.getThemeColor());
		ImageView iv;
		for (LinearLayout one : Items) {
			iv = (ImageView) one.getChildAt(0);
			iv.setImageDrawable(UI.tintDrawable(iv.getDrawable(), w));
			((TextView) one.getChildAt(1)).setTextColor(w);
		}
		FAB.setImageDrawable(DrawableHelper.getDrawable(R.drawable.v_add, UI.getAccentColor()));
		FABCreateSimple.setImageDrawable(DrawableHelper.getDrawable(R.drawable.icon_activity, UI.getAccentColor()));
		FABCreateConsole.setImageDrawable(DrawableHelper.getDrawable(R.drawable.icon_console, UI.getAccentColor()));
		try {
			ColorStateList cs=ColorStateList.valueOf(UI.getThemeColor());
			FAB.setBackgroundTintList(cs);
			FABCreateSimple.setBackgroundTintList(cs);
			FABCreateConsole.setBackgroundTintList(cs);
		} catch (Throwable e) {}
		if (DrawerGradient != null) DrawerGradient.onThemeChange();
		((ImageView) LoginLayout.getChildAt(0)).setImageDrawable(ui.tintDrawable(R.drawable.icon_user, w));
		((TextView) LoginLayout.getChildAt(1)).setTextColor(w);
		if (UserIcon.getTag() == -1) UserIcon.setImageDrawable(ui.tintDrawable(R.drawable.icon_user, w));
		((TextView) ((ViewGroup) UserLayout.getChildAt(0)).getChildAt(1)).setTextColor(w);
		VH.onThemeChange();
		if (NoticeList != null) for (int i=0;i < NoticeList.getChildCount();i++) ((NoticeAdapter.ViewHolder) NoticeList.getChildAt(i).getTag()).onThemeChange();
		if (NoticeList != null) {
			NoticeList.setGlowColor(UI.getThemeColor());
			NoticeRefresh.setColorSchemeColors(new int[]{UI.getThemeColor()});
		}
	}
	int viewHeight;
	public static int getSignature(Context cx) {
		PackageManager pm=cx.getPackageManager();
		try {
			PackageInfo info=pm.getPackageInfo(cx.getPackageName(), PackageManager.GET_SIGNATURES);
			StringBuffer b=new StringBuffer();
			for (Signature one : info.signatures) b.append(one.toCharsString());
			return b.toString().hashCode();
		} catch (PackageManager.NameNotFoundException e) {e.printStackTrace();}
		return -1;
	}
	private void createUI(final boolean app) {
		VAlertDialog dialog=new VAlertDialog(this);
		dialog.setTitle(get(L.Main_CreateProject)).setCancelable(true);
		LinearLayout layout=new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		final VEditText ed=new VEditText(this);
		ed.setHint(get(L.Main_AppName));
		ed.setSingleLine();
		final VEditText ped=new VEditText(this);
		ped.setHint(get(L.Main_PackageName));
		ped.setSingleLine();
		layout.addView(ed);
		layout.addView(ped);
		dialog.setView(layout);
		dialog.setPositiveButton(get(L.OK), false, new VAlertDialog.OnClickListener() {
				@Override
				public void onClick(VAlertDialog dialog, int pos) {
					String name=ed.getText().toString();
					String ill=Project.isAppNameValid(name);
					if (ill != null) {
						ed.setError(ill);
						return;
					}
					String p=ped.getText().toString();
					ill = Project.isPackageNameValid(p);
					if (ill != null) {
						ped.setError(ill);
						return;
					}
					File f=new File(Project.PATH, name);
					if (f.exists()) {
						ed.setError(get(L.Main_ProjectAlreadyExist));
						return;
					}
					try {
						Project pro=Project.getInstance(f, true);
						pro.setAppName(name);
						pro.setPackageName(p);
						Jsc sc=pro.addJs("Main.js", app ?JsExtend.JsVActivity: JsExtend.JsConsole);
						pro.setMainJs(sc);
						pro.saveManifest();
					} catch (Exception e) {
						err(e);
					}
					ui.print(get(L.Main_ProjectCreated));
					dialog.dismiss();
					VH.loadData();
				}
			});
		dialog.show();
	}
	public void notifyProjectRename() {
		VH.notifyProjectRename();
	}
	public void checkUpdate() {
		try {
			int c=getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
			BmobQuery<AppEntity> q=new BmobQuery<>();
			q.addWhereGreaterThan("VCode", c);
			q.order("VCode");
			q.findObjects(new FindListener<AppEntity>() {
					@Override
					public void done(List<AppEntity> data, BmobException e) {
						if (e != null) return;
						if (data == null || data.size() == 0) return;
						final AppEntity en=data.get(0);
						final File target=getUpdateFile(en.VName);
						VAlertDialog dialog=ui.newAlertDialog();
						dialog.setTitle(String.format(get(L.UpdateDes), en.VName))
							.setMessage(en.Content)
							.setCancelable(!en.Force)
							.setNegativeButton(get(L.Cancel), null).setPositiveButton(get(L.Update), false, new VAlertDialog.OnClickListener() {
								@Override
								public void onClick(final VAlertDialog dialog, int pos) {
									if (target.exists()) {
										installApk(target);
										dialog.dismiss();
									}
									dialog.setCancelable(false);
									dialog.setPositiveButton("0%", false, null);
									en.File.download(target, new DownloadFileListener() {
											@Override
											public void done(String path, BmobException e) {
												dialog.dismiss();
												if (e != null) {
													Global.onBmobErr(ui, e);
													return;
												}
												installApk(target);
											}
											@Override
											public void onProgress(Integer pro, long size) {
												dialog.setPositiveButton(pro + "%", false, null);
											}
										});
								}
							}).show();
					}
				});
		} catch (Throwable t) {}
	}
	private void installApk(File f) {
		Intent in = new Intent(Intent.ACTION_VIEW);
		in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		in.setDataAndType(Uri.fromFile(f), "application/vnd.android.package-archive");
	}
	public static File getUpdateFile(String n) {
		File dir=new File(Environment.getExternalStorageDirectory(), "Android/data/com.jxs.vide/Update");
		if (!dir.exists()) dir.mkdirs();
		File want=new File(dir, n);
		for (File one : dir.listFiles())
			if (!one.equals(want)) one.delete();
		return want;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ProjectViewHelper.recycle();
	}
}
