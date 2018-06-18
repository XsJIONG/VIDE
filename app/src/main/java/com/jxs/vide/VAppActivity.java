package com.jxs.vide;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v4.widget.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import cn.bmob.v3.*;
import cn.bmob.v3.exception.*;
import cn.bmob.v3.listener.*;
import com.jxs.v.widget.*;
import com.jxs.vapp.program.*;
import com.jxs.vcompat.activity.*;
import com.jxs.vcompat.io.*;
import com.jxs.vcompat.ui.*;
import com.jxs.vcompat.widget.*;
import java.io.*;
import java.util.*;

import android.support.design.widget.FloatingActionButton;
import android.view.View.OnClickListener;
import com.jxs.v.widget.CardView;
import com.jxs.vcompat.widget.VListView;

import static com.jxs.vide.L.get;

public class VAppActivity extends VActivity {
	public static VAppActivity Top;
	private SwipeRefreshLayout Refresh;
	private VListView ContentList;
	private VAppAdapter Adapter;
	private RelativeLayout Root;
	private FloatingActionButton Upload;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Top = this;
		super.onCreate(savedInstanceState);
		enableBackButton();
		setTitleText(get(L.Title_VApp));
		setSubTitle(get(L.HaveFun));
		Global.checkBmob(this);
		initView();
		setContentView(Root);
		loadData();
	}
	public static void showMessage(CharSequence cs) {
		if (Top == null) return;
		Top.ui.print(cs);
	}
	public static void refresh() {
		if (Top == null) return;
		Top.loadData();
	}
	private void loadData() {
		Refresh.post(new Runnable() {
				@Override
				public void run() {
					Refresh.setRefreshing(true);
				}
			});
		BmobQuery<VAppEntity> q=new BmobQuery<>();
		q.order("-createdAt");
		q.findObjects(new FindListener<VAppEntity>() {
				@Override
				public void done(List<VAppEntity> data, BmobException e) {
					if (e != null) {
						Global.onBmobErr(ui, e);
						Refresh.setRefreshing(false);
						return;
					}
					if (data == null) return;
					Adapter.clear();
					for (VAppEntity one : data) Adapter.add(one);
					Adapter.notifyDataSetChanged();
					Refresh.setRefreshing(false);
				}
			});
	}
	@Override
	public void onThemeChange(String key) {
		super.onThemeChange(key);
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		Upload.setBackgroundTintList(ColorStateList.valueOf(UI.getThemeColor()));
		for (int i=0;i < ContentList.getChildCount();i++) ((VAppAdapter.ViewHolder) ContentList.getChildAt(i).getTag()).onThemeChange();
		Refresh.setColorSchemeColors(new int[]{UI.getThemeColor()});
	}
	public static final File TempJscDir=new File(Environment.getExternalStorageDirectory(), "Android/data/com.jxs.vide/Jsc");
	private void initView() {
		Root = new RelativeLayout(this);
		Refresh = new SwipeRefreshLayout(this);
		Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				@Override
				public void onRefresh() {
					Global.UserNames.clear();
					loadData();
				}
			});
		Refresh.setColorSchemeColors(new int[]{UI.getThemeColor()});
		ContentList = new VListView(this);
		ContentList.setDividerHeight(0);
		ContentList.setDivider(null);
		ContentList.setSelector(new ColorDrawable(Color.TRANSPARENT));
		Adapter = new VAppAdapter(this);
		ContentList.setAdapter(Adapter);
		Refresh.addView(ContentList);
		Root.addView(Refresh, new RelativeLayout.LayoutParams(-1, -1));
		Upload = new FloatingActionButton(this);
		Upload.setImageResource(R.drawable.icon_upload);
		Upload.setBackgroundTintList(ColorStateList.valueOf(UI.getThemeColor()));
		RelativeLayout.LayoutParams UploadPara=new RelativeLayout.LayoutParams(UI.dp2px(64), UI.dp2px(64));
		UploadPara.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		UploadPara.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		UploadPara.bottomMargin = UploadPara.rightMargin = UI.dp2px(32);
		Root.addView(Upload, UploadPara);
		Upload.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (BmobUser.getCurrentUser(VUser.class) == null) {
						ui.toast(get(L.LoginFirst));
						startActivity(new Intent(VAppActivity.this, LoginActivity.class));
						return;
					}
					startActivity(new Intent(VAppActivity.this, BmobUser.getCurrentUser(VUser.class) == null ?LoginActivity.class: UploadVAppActivity.class));
				}
			});
		ContentList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
					final VAppEntity en=Adapter.getItem(pos);
					final android.support.v7.widget.PopupMenu menu=new android.support.v7.widget.PopupMenu(VAppActivity.this, view, Gravity.CENTER);
					Menu q=menu.getMenu();
					q.add(0, 0, 0, get(L.VAppRun));
					boolean liked=false;
					BmobUser user=BmobUser.getCurrentUser();
					if (user != null && en.Likes.contains(user.getObjectId())) liked = true;
					q.add(0, 1, 1, get(liked ?L.Dislike: L.Like));
					q.add(0, 2, 2, get(L.VAppDetail));
					if (en.OpenSource) q.add(0, 3, 3, get(L.VAppClone));
					if (user != null && user.getObjectId().equals(en.Author)) q.add(0, 4, 4, get(L.VAppDelete));
					menu.setOnMenuItemClickListener(new android.support.v7.widget.PopupMenu.OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(MenuItem item) {
								switch (item.getOrder()) {
									case 0:runJsc(en);break;
									case 1:{
											VUser user=BmobUser.getCurrentUser(VUser.class);
											if (user == null) {
												ui.toast(get(L.LoginFirst));
												startActivity(new Intent(VAppActivity.this, LoginActivity.class));
												break;
											}
											if (en.Likes.contains(user.getObjectId())) {
												en.Likes.remove(user.getObjectId());
												ui.newProgressDialog().setTitle(get(L.Wait)).setMessage(get(L.Dislike)).setCancelable(false).putToUi("Likes").show();
												en.update(new UpdateListener() {
														@Override
														public void done(BmobException e) {
															if (e != null) {
																Global.onBmobErr(ui, e);
																VProgressDialog.getFromUi("Likes").dismiss();
																return;
															}
															VProgressDialog.getFromUi("Likes").dismiss();
															refresh();
															ui.print(get(L.Disliked));
														}
													});
												break;
											}
											en.Likes.add(user.getObjectId());
											ui.newProgressDialog().setTitle(get(L.Wait)).setMessage(get(L.Like)).setCancelable(false).putToUi("Likes").show();
											en.update(new UpdateListener() {
													@Override
													public void done(BmobException e) {
														if (e != null) {
															Global.onBmobErr(ui, e);
															VProgressDialog.getFromUi("Likes").dismiss();
															return;
														}
														VProgressDialog.getFromUi("Likes").dismiss();
														refresh();
														ui.print(get(L.Liked));
													}
												});
											break;
										}
									case 2:{
											ui.newAlertDialog().setTitle(get(L.VAppDetail)).setMessage(en.Description).setCancelable(true).setPositiveButton(get(L.OK), null).show();
											break;
										}
									case 3:{
											if (!TempJscDir.exists()) TempJscDir.mkdirs();
											downloadJsc(en, new File(TempJscDir, "Tmp"), false);
											break;
										}
									case 4:{
											final VProgressDialog pro=ui.newProgressDialog();
											pro.setTitle(get(L.Wait)).setMessage(get(L.Deleting)).setCancelable(false).show();
											new Thread(new Runnable() {
													@Override
													public void run() {
														en.Content.delete(new UpdateListener() {
																@Override
																public void done(final BmobException e) {
																	if (e != null) {
																		ui.autoOnUi(new Runnable() {
																				@Override
																				public void run() {
																					pro.dismiss();
																					Global.onBmobErr(ui, e);
																				}
																			});
																		return;
																	}
																	en.delete(new UpdateListener() {
																			@Override
																			public void done(final BmobException e) {
																				if (e != null) {
																					ui.autoOnUi(new Runnable() {
																							@Override
																							public void run() {
																								pro.dismiss();
																								Global.onBmobErr(ui, e);
																							}
																						});
																					return;
																				}
																				ui.autoOnUi(new Runnable() {
																						@Override
																						public void run() {
																							pro.dismiss();
																							ui.print(get(L.Deleted));
																							refresh();
																						}
																					});
																			}
																		});
																}
															});
													}
												}).start();
											break;
										}
								}
								menu.dismiss();
								return true;
							}
						});
					menu.show();
				}
			});
	}
	private void runJsc(VAppEntity en) {
		downloadJsc(en, getTempFile(en), true);
	}
	private void runJsc(File f) {
		try {
			JsApp app=new JsApp(new FileInputStream(f));
			app.run();
		} catch (Throwable t) {
			err(t);
		}
	}
	private void downloadJsc(VAppEntity en, File f, boolean run) {
		VProgressDialog dialog=ui.newProgressDialog();
		dialog.setTitle(get(L.Wait));
		dialog.setCancelable(false).setMessage(String.format(get(L.Downloading), 0));
		ProgressDialog d=dialog.show();
		new Thread(new DownloadRunnable(this, en, f, new DialogHandler(d), run)).start();
	}
	public static class DownloadRunnable implements Runnable {
		private DialogHandler H;
		private VAppEntity en;
		private File f;
		private VAppActivity cx;
		private boolean run;
		public DownloadRunnable(VAppActivity cx, VAppEntity en, File f, DialogHandler handler, boolean run) {
			H = handler;
			this.en = en;
			this.f = f;
			this.cx = cx;
			this.run = run;
		}
		@Override
		public void run() {
			if (en.Content == null) return;
			en.Content.download(f, new DownloadFileListener() {
					@Override
					public void done(String path, BmobException e) {
						if (e != null) {
							H.dismiss();
							Global.onBmobErr(cx.ui, e);
							return;
						}
						if (run) {
							H.dismiss();
							cx.ui.autoOnUi(new Runnable() {
									@Override
									public void run() {
										cx.runJsc(f);
									}
								});
							return;
						}
						try {
							FileInputStream ins=new FileInputStream(f);
							JsApp app=new JsApp(ins, false);
							String name=app.getManifest().getAppName();
							File q=new File(Project.PATH, name);
							int qwe=0;
							while (q.exists()) q = new File(Project.PATH, join(name, "-", ++qwe));
							Project pro=Project.getInstance(q, true);
							File dir=pro.getDir();
							Manifest A=app.getManifest();
							pro.getManifest().loadFrom(A);
							pro.setAppName(dir.getName());
							ArrayList<Jsc> s=A.getAllJs();
							Jsc c;
							for (int i=0;i < s.size();i++) {
								c = s.get(i);
								IOUtil.write(new File(dir, c.getName()), c.getCode());
							}
							pro.saveManifest();
							MainActivity.cx.notifyProjectRename();
							final File libs=pro.getDir();
							if (!libs.exists()) libs.mkdirs();
							JsApp.write(ins, new JsApp.DataWriter() {
									private File f;
									@Override
									public void write(String name, byte[] data) {
										try {
											f = new File(libs, name);
											IOUtil.createNewFile(f);
											IOUtil.write(f, data);
											data = null;
										} catch (Throwable t) {
											Global.log("VApp Clone", Log.getStackTraceString(t));
										}
									}
								});
							ins.close();
							H.dismiss();
							cx.ui.print(String.format(get(L.Cloned), pro.getAppName()));
						} catch (final Throwable er) {
							H.dismiss();
							cx.ui.autoOnUi(new Runnable() {
									@Override
									public void run() {
										cx.err(er);
									}
								});
						}
					}
					@Override
					public void onProgress(Integer value, long size) {
						H.setMessage(String.format(get(L.Downloading), value));
					}
				});
		}
	}
	private static String join(Object...args) {
		StringBuffer buf=new StringBuffer();
		for (Object one : args) buf.append(one);
		return buf.toString();
	}
	public static File getTempFile(VAppEntity entity) {
		if (!TempJscDir.exists()) TempJscDir.mkdirs();
		return new File(TempJscDir, entity.getObjectId());
	}
	public static class VAppAdapter extends ArrayAdapter<VAppEntity> {
		private static HashMap<String,ArrayList<TextView>> All=new HashMap<>();
		public VAppAdapter(Context cx) {
			super(cx, android.R.layout.simple_list_item_1, new ArrayList<VAppEntity>());
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			VAppEntity entity=getItem(position);
			LinearLayout Content=new LinearLayout(getContext());
			Content.setGravity(Gravity.CENTER);
			Content.setClickable(false);
			CardView Card=new CardView(getContext());
			Card.setCardElevation(5f);
			Card.setRadius(5f);
			LinearLayout root=new LinearLayout(getContext());
			root.setOrientation(LinearLayout.VERTICAL);
			TextView Title=new TextView(getContext());
			Title.setText(getTitle(entity));
			Title.setGravity(Gravity.LEFT);
			Title.setSingleLine(true);
			Title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
			LinearLayout LikeLayout=new LinearLayout(getContext());
			TextView asd=new TextView(getContext());
			asd.setText(String.valueOf(entity.Likes.size()));
			asd.setGravity(Gravity.CENTER);
			asd.setTextColor(UI.getAccentColor());
			ImageView iv=new ImageView(getContext());
			iv.setImageResource(R.drawable.icon_love);
			LikeLayout.setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout.LayoutParams tpara=new LinearLayout.LayoutParams(-2, -1);
			tpara.rightMargin = UI.dp2px(10);
			LikeLayout.addView(asd, tpara);
			LikeLayout.addView(iv, new LinearLayout.LayoutParams(UI.dp2px(16), UI.dp2px(16)));
			LinearLayout TopLayout=new LinearLayout(getContext());
			TopLayout.setOrientation(LinearLayout.HORIZONTAL);
			TopLayout.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams TitlePara=new LinearLayout.LayoutParams(0, -2);
			TitlePara.weight = 1;
			TopLayout.addView(Title, TitlePara);
			TopLayout.addView(LikeLayout);
			root.addView(TopLayout, new LinearLayout.LayoutParams(-1, -2));
			TextView SubTitle=new TextView(getContext());
			SubTitle.setMaxEms(15);
			SubTitle.setSingleLine(true);
			SubTitle.setMaxLines(1);
			SubTitle.setGravity(Gravity.LEFT);
			SubTitle.setText(entity.Description);
			SubTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
			LinearLayout bottomLayout=new LinearLayout(getContext());
			bottomLayout.setOrientation(LinearLayout.HORIZONTAL);
			bottomLayout.setGravity(Gravity.BOTTOM);
			LinearLayout.LayoutParams SubPara=new LinearLayout.LayoutParams(0, -2);
			SubPara.weight = 1;
			bottomLayout.addView(SubTitle, SubPara);
			final TextView User=new TextView(getContext());
			User.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
			User.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
			bottomLayout.addView(User);
			LinearLayout.LayoutParams BottomPara=new LinearLayout.LayoutParams(-1, -2);
			BottomPara.topMargin = UI.dp2px(15);
			root.addView(bottomLayout, BottomPara);
			int p=UI.dp2px(13);
			Card.setContentPadding(p, p, p, p);
			Card.addView(root, new CardView.LayoutParams(-1, -1));
			LinearLayout.LayoutParams para=new LinearLayout.LayoutParams(-1, -1);
			para.setMargins(p, p, p, p);
			Content.addView(Card, para);
			ViewHolder holder=new ViewHolder();
			holder.Card = Card;
			holder.Title = Title;
			holder.Des = SubTitle;
			holder.User = User;
			holder.openSource = entity.OpenSource;
			holder.onThemeChange();
			holder.Like = asd;
			holder.LikeView = iv;
			Content.setTag(holder);
			if (Global.UserNames.containsKey(entity.Author)) User.setText(Global.UserNames.get(entity.Author)); else if (All.containsKey(entity.Author)) All.get(entity.Author).add(User); else {
				final ArrayList<TextView> S=new ArrayList<>();
				S.add(User);
				All.put(entity.Author, S);
				BmobQuery<VUser> q=new BmobQuery<>();
				q.addWhereEqualTo("objectId", entity.Author);
				q.setLimit(1); //WTF
				q.findObjects(new FindListener<VUser>() {
						@Override
						public void done(List<VUser> data, BmobException e) {
							if (e != null) {
								Global.onBmobErr(new UI(getContext()), e);
								return;
							}
							if (data == null || data.size() != 1) return;
							VUser u=data.get(0);
							Global.UserNames.put(u.getObjectId(), u.getUsername());
							String s=u.getUsername();
							ArrayList<TextView> needed=All.remove(u.getObjectId());
							for (int i=0;i < needed.size();i++) needed.get(i).setText(s);
						}
					});
			}
			return Content;
		}
		private static String OS=null;
		private static CharSequence getTitle(VAppEntity entity) {
			if (entity.OpenSource) {
				if (OS == null) OS = get(L.OpenSource);
				return getTitle(entity.Title + " " + OS);
			} else return entity.Title;
		}
		private static CharSequence getTitle(String q) {
			SpannableString s=new SpannableString(q);
			int en=q.length() - OS.length();
			s.setSpan(new ForegroundColorSpan(UI.getAccentColor()), 0, en, 0);
			s.setSpan(new ForegroundColorSpan(UI.getThemeColor()), en, q.length(), 0);
			s.setSpan(new BackgroundColorSpan(UI.getAccentColor()), en, q.length(), 0);
			return s;
		}
		private static class ViewHolder {
			public CardView Card;
			public TextView Title,Des,User,Like;
			public ImageView LikeView;
			public boolean openSource;
			public void onThemeChange() {
				int ui=UI.getThemeColor();
				int w=UI.getAccentColor();
				if (Card != null) Card.setCardBackgroundColor(ui);
				if (Title != null) Title.setTextColor(w);
				if (Des != null) Des.setTextColor(w);
				if (User != null) User.setTextColor(w);
				if (Like != null) Like.setTextColor(w);
				if (LikeView != null) UI.tintDrawable(LikeView.getDrawable(), w);
				if (openSource) Title.setText(getTitle(Title.getText().toString()));
			}
		}
	}
}
