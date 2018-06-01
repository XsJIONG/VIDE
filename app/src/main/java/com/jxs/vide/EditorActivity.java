package com.jxs.vide;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.support.v4.view.*;
import android.support.v7.view.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.jxs.vapp.program.*;
import com.jxs.vapp.runtime.*;
import com.jxs.vcompat.activity.*;
import com.jxs.vcompat.drawable.*;
import com.jxs.vcompat.io.*;
import com.jxs.vcompat.ui.*;
import com.jxs.vcompat.widget.*;
import com.kellinwood.security.zipsigner.*;
import com.myopicmobile.textwarrior.android.*;
import dalvik.system.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.View.OnClickListener;
import com.jxs.vapp.program.Console;

import static com.jxs.vide.L.get;

public class EditorActivity extends VActivity {
	TextEditor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			pro = Project.getInstance(getIntent().getStringExtra("project"));
		} catch (Exception e) {err(e, true);}
		if (!pro.getDir().exists()) {
			finish();
			return;
		}
		if (!Project.isProject(pro.getDir())) {
			finish();
			return;
		}
		try {
			pro.loadManifest();
		} catch (Exception e) {}
		editor = new TextEditor(this);
		editor.setTypeface(Typeface.MONOSPACE);
		editor.setOnEditAction(new Runnable() {
				@Override
				public void run() {
					if (FileAction_Save != null) FileAction_Save.setIcon(ui.tintDrawable(R.drawable.v_unsave, UI.getThemeColor()));
				}
			});
		initSymbolLayout();
		initRootView();
		initToolbar();
		initOpenedSpinner();
		openJs(pro.getMainJs());
		initBackDrawable();
		initSupportAC();
		initDexAutoComplete();
	}
	private void initDexAutoComplete() {
		File[] fs=pro.getLibDir().listFiles(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.getName().endsWith(".dex") && f.isFile();
				}
			});
		StringBuffer path=new StringBuffer();
		CTree c=null;
		File cf;
		String p;
		try {
			for (int i=0;i < fs.length;i++) {
				p = fs[i].getPath();
				path.append(p);
				path.append(File.pathSeparator);
				p = p.substring(0, p.lastIndexOf('.'))+"_CTree.txt";
				cf = new File(p);
				if (cf.exists()) {
					c = CTree.fromString(new String(IOUtil.read(new File(p))));
					Global.Q.merge(c);
				}
			}
			if (path.length() != 0) path.deleteCharAt(path.length() - 1);
			Global.ClassLoader = new DexClassLoader(path.toString(), getDir("dex", 0).getAbsolutePath(), null, getClassLoader());
		} catch (IOException e) {}
	}
	private void initSupportAC() {
		CNode jxs=Global.Q.getSon("com").getSon("jxs");
		jxs.removeSon("v");
		jxs.removeSon("vcompat");
		if (pro.isCompat()) Global.Q.merge(Global.Support); else Global.Q.merge(Global.Unsupport);
	}
	@Override
	public void onThemeChange(String key) {
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		super.onThemeChange(key);
		setTitleTextColor(UI.getAccentColor());
		_Toolbar.setBackgroundColor(UI.getThemeColor());
		_BarLayout.setBackgroundColor(UI.getThemeColor());
		SymbolLayout.setBackgroundColor(UI.getThemeColor());
		int w=UI.getAccentColor();
		for (int i=0;i < ContentLayout.getChildCount();i++) ((TextView) ContentLayout.getChildAt(i)).setTextColor(w);
		if (_Menu != null) for (int i=0;i < MenuCount;i++) UI.tintDrawable(_Menu.getItem(i).getIcon(), w);
		UI.tintDrawable(mMenuView.getOverflowIcon(), UI.getAccentColor());
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Global.ClassLoader = null;
		File[] fs=pro.getLibDir().listFiles(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.getName().endsWith(".dex") && f.isFile();
				}
			});
		if (fs.length != 0) {
			Global.Q = null;
			Global.load(this);
		}
	}
	private ArrayAdapter<Jsc> OpenedAdapter;
	private ActionMode FileActionMode=null;
	private android.support.v7.widget.Toolbar _Toolbar;
	private LinearLayout _BarLayout;
	private android.support.v7.widget.ActionMenuView mMenuView;

	private void initToolbar() {
		_Toolbar = new Toolbar(this);
		_BarLayout = new LinearLayout(this);
		_BarLayout.setOrientation(LinearLayout.VERTICAL);
		_BarLayout.addView(_Toolbar);
		RootLayout = new LinearLayout(this);
		RootLayout.setOrientation(LinearLayout.VERTICAL);
		RootLayout.addView(_BarLayout);
		RootLayout.addView(Root);
		setContentView(RootLayout);
		setSupportActionBar(_Toolbar);
		setTitleText(get(L.Title_Edit));
		setTitleTextColor(UI.getAccentColor());
		enableBackButton();
		_Toolbar.setBackgroundColor(UI.getThemeColor());
		try {
			_Toolbar.getMenu();
			Field MVF=android.support.v7.widget.Toolbar.class.getDeclaredField("mMenuView");
			MVF.setAccessible(true);
			mMenuView = (android.support.v7.widget.ActionMenuView) MVF.get(_Toolbar);
			UI.tintDrawable(mMenuView.getOverflowIcon(), UI.getAccentColor());
		} catch (Throwable t) {
			err(t, true);
		}
		_BarLayout.setBackgroundColor(UI.getThemeColor());
		ViewCompat.setElevation(_BarLayout, 10);
		getSupportActionBar().setElevation(0);
	}
	private void initOpenedSpinner() {
		OpenedSpinner = new Spinner(this);
		OpenedAdapter = new ArrayAdapter<Jsc>(this, android.R.layout.simple_spinner_item, Opened);
		OpenedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		OpenedSpinner.setAdapter(OpenedAdapter);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setCustomView(OpenedSpinner);
		OpenedSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
					if (add) {
						add = false;
						return;
					}
					if (pos == nowOpen) {
						closeFile(pos);
					} else {
						openJs(Opened.get(pos));
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
	}
	private boolean add=false;
	private void rCloseFile(int pos) {
		Opened.remove(pos);
		OpenedAdapter.notifyDataSetChanged();
		if (Opened.size() == 0) {

		} else OpenedSpinner.setSelection(nowOpen);
	}
	private void closeFile(final int pos) {
		if (editor.isSaved())
			rCloseFile(pos);
		else {
			ui.newAlertDialog().setTitle(get(L.Remind)).setMessage(get(L.Editor_DidntSave))
				.setPositiveButton(get(L.Editor_SaveAndExit), new VAlertDialog.OnClickListener() {
					@Override
					public void onClick(VAlertDialog dialog, int ppos) {
						try {
							save();
							rCloseFile(pos);
						} catch (IOException e) {
							err(e);
						}
					}
				}).setNegativeButton(get(L.Editor_DirectlyExit), new VAlertDialog.OnClickListener() {
					@Override
					public void onClick(VAlertDialog dialog, int pos) {
						rCloseFile(pos);
					}
				}).setNeutralButton(get(L.Editor_Cancel), null).setCancelable(true).show();
		}
	}
	private LinearLayout RootLayout;
	private RelativeLayout Root;
	private void initRootView() {
		Root = new RelativeLayout(this);
		Root.addView(editor);
		RelativeLayout.LayoutParams para=new RelativeLayout.LayoutParams(-1, -2);
		para.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		Root.addView(SymbolLayout, para);
	}
	public static final char[] SYMBOLS={'\t','{','}','(',')',';',',','.','=','\"','<','>','&','+','-','*','/','[',']','|','!','?','\\',':','_'};
	public static final String TAB="->";
	private LinearLayout ContentLayout;
	private void initSymbolLayout() {
		SymbolLayout = new LinearLayout(this);
		SymbolLayout.setOrientation(LinearLayout.HORIZONTAL);
		SymbolLayout.setBackgroundColor(ui.getThemeColor());
		SymbolLayout.setAlpha(0.7f);
		ContentLayout = new LinearLayout(this);
		ContentLayout.setOrientation(LinearLayout.HORIZONTAL);
		int w=UI.getAccentColor();
		TextView tv;
		LinearLayout.LayoutParams p;
		for (int i=0;i < SYMBOLS.length;i++) {
			tv = new TextView(this);
			tv.setTextColor(w);
			tv.setText(SYMBOLS[i] == '\t' ?TAB: SYMBOLS[i] + "");
			tv.setTextSize(20);
			tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
			tv.setGravity(Gravity.CENTER);
			p = new LinearLayout.LayoutParams(ui.dp2px(38), ui.dp2px(38));
			tv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						editor.paste(((TextView) v).getText().toString().equals(TAB) ?"\t": ((TextView) v).getText().toString());
					}
				});
			ContentLayout.addView(tv, p);
		}
		HorizontalScrollView sc=new HorizontalScrollView(this);
		sc.addView(ContentLayout);
		LinearLayout.LayoutParams para=new LinearLayout.LayoutParams(-1, -2);
		SymbolLayout.addView(sc, para);
	}
	private LinearLayout SymbolLayout;
	private ArrayList<Jsc> Opened=new ArrayList<>();
	private void initBackDrawable() {
		BDrawable d=new BDrawable();
		ArrowShape arr=new ArrowShape(3f);
		QuestionMarkShape q=new QuestionMarkShape(1, 3);
		d.addPair(arr.Top, q.LeftTop,
				  arr.Body, q.RightTop,
				  arr.Bottom, q.RightBottom,
				  arr.Bottom, q.Dot
				  );
		d.setColor(UI.getAccentColor());
		d.setStrokeWidth(3);
		setBackDrawable(d);
	}
	private void readText(final Jsc js) {
		final VProgressDialog prog=ui.newProgressDialog();
		prog.setTitle(get(L.Wait));
		prog.setMessage(get(L.Editor_Reading));
		prog.setCancelable(true);
		prog.show();
		new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						final String s=IOUtil.bytes2String(IOUtil.read(pro.getFile(js)));
						ui.autoOnUi(new Runnable() {
								@Override
								public void run() {
									editor.setText(s);
									refreshAutoComplete(js);
									editor.setSaved(true);
									prog.dismiss();
								}
							});
					} catch (IOException e) {
						ui.print(get(L.Editor_ErrorWhileReading));
						ui.autoOnUi(new Runnable() {
								@Override
								public void run() {
									prog.dismiss();
									finish();
								}
							});
					}
				}
			}).start();
	}
	VProgressDialog mCompileDialog;
	public void setCompileText(final String s) {
		ui.autoOnUi(new Runnable() {
				@Override
				public void run() {
					if (mCompileDialog != null) mCompileDialog.setMessage(s);
				}
			});
	}
	@Override
	public void finish() {
		onBackPressed();
	}
	Runnable mDismissCompileAction=new Runnable() {
		@Override
		public void run() {
			if (mCompileDialog != null) {
				mCompileDialog.dismiss();
				mCompileDialog = null;
			}
		}
	};

	private Menu _Menu;
	private static final int MenuCount=1;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		_Menu = menu;
		menu.add(0, 0, 0, get(L.Editor_File)).setIcon(R.drawable.icon_file).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
		initActions();
		//menu.add(0, 1, 1, get(L.Editor_More)).setIcon(DrawableHelper.getDrawable(R.drawable.icon_class, Color.WHITE)).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add(0, 1, 1, get(L.Editor_Run));
		menu.add(0, 2, 2, get(L.Editor_Output));
		menu.add(0, 3, 3, get(L.Editor_Import));
		menu.add(0, 4, 4, get(L.Editor_Setting));
		int w=UI.getAccentColor();
		for (int i=0;i < MenuCount;i++) UI.tintDrawable(_Menu.getItem(i).getIcon(), w);
		return super.onCreateOptionsMenu(menu);
	}
	Project pro=null;
	private ActionMode.Callback FileAction;
	private MenuItem FileAction_Save=null;
	private void initActions() {
		FileAction = new ActionMode.Callback() {
			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				FileActionMode = mode;
				mode.setTitle(get(L.Editor_File));
				menu.add(0, 0, 0, get(L.Editor_CreateFile)).setIcon(ui.tintDrawable(R.drawable.icon_create, UI.getThemeColor())).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				menu.add(0, 1, 1, get(L.Editor_OpenFile)).setIcon(ui.tintDrawable(R.drawable.icon_open, UI.getThemeColor())).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				FileAction_Save = menu.add(0, 2, 2, get(L.Editor_SaveFile)).setIcon(ui.tintDrawable(editor.isSaved() ?R.drawable.v_save: R.drawable.v_unsave, UI.getThemeColor())).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
				return true;
			}
			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {return false;}
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getOrder()) {
					case 0:{
							VAlertDialog d=ui.newAlertDialog();
							d.setTitle(get(L.Editor_CreateFile)).setEditHint(get(L.Editor_FileName)).setPositiveButton(get(L.Editor_OK), false, new VAlertDialog.OnClickListener() {
									@Override
									public void onClick(VAlertDialog d, int pos) {
										String s=d.getInputText();
										if (s == null || s.length() == 0) {
											d.getEditText().setError(get(L.NameCantEmpty));
											return;
										}
										if (s.contains(File.separator)) {
											d.getEditText().setError(get(L.ContainsIllegalChar));
											return;
										}
										if (!s.endsWith(".js")) s += ".js";
										if (new File(pro.getDir(), s).exists()) {
											d.getEditText().setError(get(L.Editor_FileAlreadyExist));
											return;
										}
										Jsc c=pro.addJs(s, JsExtend.JsVActivity);
										pro.saveManifest();
										ui.print(get(L.Editor_Added));
										openJs(c);
										d.dismiss();
									}
								}).setCancelable(true).show();
							break;
						}
					case 1:{
							ArrayList<Jsc> alljs=pro.getAllJs();
							final ArrayList<Jsc> res=new ArrayList<>();
							for (int i=0;i < alljs.size();i++) if (!Opened.contains(alljs.get(i))) res.add(alljs.get(i));
							VAlertDialog d=ui.newAlertDialog();
							d.setTitle(get(L.Editor_OpenFile));
							if (res.size() == 0) {
								CryView v=new CryView(EditorActivity.this);
								v.setText(get(L.HereIsEmpty));
								d.setView(v);
							} else {
								String[] cs=new String[res.size()];
								for (int i=0;i < res.size();i++) cs[i] = res.get(i).toString();
								d.setItems(cs, new VAlertDialog.OnClickListener() {
										@Override
										public void onClick(VAlertDialog d, int pos) {
											openJs(res.get(pos));
										}
									});
							}
							d.setPositiveButton(get(L.Editor_Close), null).setCancelable(true).show();
							break;
						}
					case 2:{
							if (editor.isSaved()) break;
							new Thread(new Runnable() {
									@Override
									public void run() {
										try {
											IOUtil.write(pro.getFile(getNowOpen()), editor.getText().toString());
											ui.print(get(L.Editor_Saved), get(L.IKnow), UI.getThemeColor(), null);
											ui.autoOnUi(SaveDoneAction);
										} catch (IOException e) {
											ui.alert(get(L.Editor_ErrorWhileSaving), Log.getStackTraceString(e));
										}
									}
								}).start();
							break;
						}
				}
				mode.finish();
				return false;
			}
			@Override
			public void onDestroyActionMode(ActionMode mode) {
				FileAction_Save = null;
			}
		};
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle() == null) return super.onOptionsItemSelected(item);
		switch (item.getOrder()) {
			case 0:startSupportActionMode(FileAction);break;
			case 1:runUi();break;
			case 2:outputUi();break;
			case 3:importUi();break;
			case 4:settingUi();break;
		}
		return super.onOptionsItemSelected(item);
	}
	public void runUi() {
		if (!editor.isSaved()) {
			ui.newAlertDialog().setTitle(get(L.Remind)).setMessage(get(L.Editor_RunWithoutSave))
				.setPositiveButton(get(L.Editor_SaveAndRun), new VAlertDialog.OnClickListener() {
					@Override
					public void onClick(VAlertDialog dialog, int ppos) {
						try {
							save();
							rRun();
						} catch (IOException e) {
							err(e);
						}
					}
				}).setNegativeButton(get(L.Editor_DirectlyRun), new VAlertDialog.OnClickListener() {
					@Override
					public void onClick(VAlertDialog dialog, int pos) {
						rRun();
					}
				}).setNeutralButton(get(L.Editor_Cancel), null).setCancelable(true).show();
		} else rRun();
	}
	private static final File TmpJsc=new File(Environment.getExternalStorageDirectory(), "VIDETmp/Running.jsc");
	private static JsApp Last=null;
	private void rRun() {
		try {
			if (TmpJsc.exists()) TmpJsc.delete();
			TmpJsc.mkdirs(); TmpJsc.delete(); TmpJsc.createNewFile();
			pro.compile(new FileOutputStream(TmpJsc), true);
			if (Last != null) Last.destroy();
			Last = new JsApp(new FileInputStream(TmpJsc));
			Last.run();
			//Mark 等Js结束时移除掉(JsProgram.delete)
		} catch (Throwable e) {
			Global.log(Log.getStackTraceString(e));
			err(e);
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (TmpJsc.exists()) TmpJsc.delete();
	}
	Runnable SaveDoneAction=new Runnable() {
		@Override
		public void run() {
			if (FileAction_Save != null) FileAction_Save.setIcon(ui.tintDrawable(R.drawable.v_save, UI.getThemeColor()));
			editor.setSaved(true);
		}
	};
	private void save() throws IOException {
		IOUtil.write(pro.getFile(getNowOpen()), editor.getText().toString());
		ui.autoOnUi(SaveDoneAction);
	}
	public void outputUi() {
		if (!editor.isSaved()) {
			ui.newAlertDialog().setTitle(get(L.Remind)).setMessage(get(L.Editor_OutputWithoutSave))
				.setPositiveButton(get(L.Editor_SaveAndOutput), new VAlertDialog.OnClickListener() {
					@Override
					public void onClick(VAlertDialog dialog, int ppos) {
						try {
							save();
							rOutput();
						} catch (IOException e) {
							err(e);
						}
					}
				}).setNegativeButton(get(L.Editor_DirectlyOutput), new VAlertDialog.OnClickListener() {
					@Override
					public void onClick(VAlertDialog dialog, int pos) {
						rOutput();
					}
				}).setNeutralButton(get(L.Editor_Cancel), null).setCancelable(true).show();
			return;
		}
		rOutput();
	}
	private void rOutput() {
		ui.newAlertDialog().setTitle(get(L.Editor_Output)).setItems(new String[] {get(L.OutputApk), get(L.OutputJsc)}, new VAlertDialog.OnClickListener() {
				@Override
				public void onClick(VAlertDialog dialog, int pos) {
					switch (pos) {
						case 0:OutputApk(false);break;
						case 1:OutputApk(true);break;
					}
				}
			}).setCancelable(true).setPositiveButton(get(L.Cancel), null).show();
	}
	private void OutputJsc(final File f) {
		ui.newAlertDialog().setEditHint(get(L.OutputJsc)).setTitle(get(L.OutputJsc)).setPositiveButton(get(L.OK), false, new VAlertDialog.OnClickListener() {
				@Override
				public void onClick(VAlertDialog dialog, int pos) {
					String s=dialog.getInputText();
					if (s == null || s.length() == 0) {
						dialog.getEditText().setError(get(L.NameCantEmpty));
						return;
					}
					if (!s.endsWith(".jsc")) s += ".jsc";
					File q=new File(f, s);
					if (q.exists()) {
						dialog.getEditText().setError(get(L.Editor_FileAlreadyExist));
						return;
					}
					dialog.dismiss();
					BuildApkPath = f;
					vdialog = ui.newProgressDialog().setTitle(get(L.Outputing)).setMessage(get(L.Wait)).setCancelable(false);
					vdialog.show();
					jsc = true;
					new Thread(BuildApkRunnable).start();
				}
			}).setNegativeButton(get(L.Cancel), null).setCancelable(true).show();
	}
	private boolean jsc;
	private void OutputApk(final boolean jsc) {
		ui.newFileChooserDialog(Environment.getExternalStorageDirectory(), new FileChooserDialog.FileChooserListener() {
				@Override
				public void onChoose(File f) {
					if (jsc) OutputJsc(f); else outputReal(f);
				}
			}, true).setTitle(get(L.Editor_ChooseOutputDir)).setCancelable(true).show();
	}
	Handler uiHandler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:if (vdialog != null) vdialog.getDialog().setProgressNumberFormat((String) msg.obj);break;
				case 1:if (vdialog != null) {
						vdialog.dismiss();
						vdialog = null;
					}
					break;
				case 2:if (vdialog != null) vdialog.setProgress(msg.obj);break;
			}
		}
	};
	private void sendMessage(int what, Object obj) {
		Message m=new Message();
		m.what = what;
		m.obj = obj;
		uiHandler.sendMessage(m);
	}
	public static final File VF=new File(Environment.getExternalStorageDirectory(), "VIDETmp");
	private Runnable BuildApkRunnable=new Runnable() {
		@Override
		public void run() {
			try {
				if (VF.exists()) IOUtil.delete(VF);
				VF.mkdirs();
				long st=System.currentTimeMillis();
				sendMessage(0, get(L.Editor_Encrypting));
				if (!jsc) sendMessage(2, 0);
				File ftmp=new File(VF, "Main.jsc");
				if (ftmp.exists()) ftmp.delete();
				pro.compile(new FileOutputStream(ftmp));
				if (jsc) {
					sendMessage(1, null);
					ui.print(get(L.OutputSC));
					return;
				}
				sendMessage(0, get(L.Editor_PickingOutApk));
				sendMessage(2, 16);
				File apk=new File(VF, "app_unsign.apk");
				if (apk.exists()) apk.delete();
				apk.createNewFile();
				IOUtil.copy(getAssets().open(pro.isCompat() ?"Compat.apk": "Normal.apk"), new FileOutputStream(apk));
				File tmpXml=new File(VF, "AndroidManifest.xml");
				if (tmpXml.exists()) tmpXml.delete();
				sendMessage(0, get(L.Editor_ExportingAM));
				sendMessage(2, 32);
				ZipManager.extraZipEntry(apk, new String[] {"AndroidManifest.xml"}, new String[] {tmpXml.getPath()});
				sendMessage(0, get(L.Editor_ParsingAM));
				sendMessage(2, 48);
				XmlFactory f=new XmlFactory();
				f.Input = new FileInputStream(tmpXml);
				f.Package = pro.getPackageName();
				f.AppName = pro.getAppName();
				f.Permissions = pro.getPermissionsForString();
				f.VersionCode = pro.getVersionCode();
				f.VersionName = pro.getVersionName();
				f.make();
				f.writeTo(new FileOutputStream(tmpXml));
				sendMessage(0, get(L.Editor_MergingDex));
				sendMessage(2, 69);
				sendMessage(0, get(L.Editor_CompressingApk));
				sendMessage(2, 80);
				ArrayList<File> all=IOUtil.getSonFiles(pro.getAssets());
				String[] needed=pro.getNeededDex();
				int extra=needed.length;
				String[] InZipNames=new String[all.size() + extra + 3];
				InputStream[] Paths=new InputStream[InZipNames.length];
				InZipNames[0] = "AndroidManifest.xml"; Paths[0] = new FileInputStream(tmpXml);
				InZipNames[1] = "assets/Main.jsc"; Paths[1] = new FileInputStream(ftmp);
				InZipNames[2] = "res/drawable-hdpi-v4/icon.png"; Paths[2] = new FileInputStream(pro.getIcon());
				for (int i=0;i < all.size();i++) {
					Paths[i + 3] = new FileInputStream(all.get(i));
					InZipNames[i + 3] = "assets" + IOUtil.getRelativePath(pro.getAssets(), all.get(i));
				}
				for (int i=0;i < needed.length;i++) {
					Paths[all.size() + 3 + i] = getAssets().open(needed[i]);
					InZipNames[all.size() + 3 + i] = "classes" + (i == 0 ?"": String.valueOf(i + 1)) + ".dex";
				}
				ZipManager.addEntrys(apk, InZipNames, Paths);
				ZipSigner signer=new ZipSigner();
				signer.setKeymode("platform");
				final File napk=new File(BuildApkPath, "app.apk");
				if (napk.exists()) napk.delete();
				napk.createNewFile();
				sendMessage(0, get(L.Editor_Signing));
				sendMessage(2, 96);
				signer.signZip(apk.getPath(), napk.getPath());
				apk.delete();
				tmpXml.delete();
				IOUtil.delete(VF);
				ui.print(String.format(get(L.Editor_BuildedApk), (System.currentTimeMillis() - st)));
				sendMessage(1, null);
				ui.autoOnUi(new Runnable() {
						@Override
						public void run() {
							Intent in=new Intent(Intent.ACTION_VIEW);
							in.setDataAndType(Uri.fromFile(napk), "application/vnd.android.package-archive");
							startActivity(in);
						}
					});
			} catch (Exception e) {
				err(e);
				sendMessage(1, null);
			}
		}
	};
	/*private void buildDex(File dex) throws IOException {
	 if (dex.exists()) dex.delete();
	 IOUtil.copy(getAssets().open(pro.isCompat() ?"dexs/Compat": "dexs/Normal"), new FileOutputStream(dex));
	 mergeDex(dex, getAssets().open("dexs/Rhino"));
	 if (pro.isUseDx()) mergeDex(dex, getAssets().open("dexs/Dx"));
	 File[] fs=pro.getLibDir().listFiles(new FileFilter() {
	 @Override
	 public boolean accept(File f) {
	 return f.getName().endsWith(".dex");
	 }
	 });
	 for (File one : fs)
	 mergeDex(dex, new FileInputStream(one));
	 }
	 private static final void mergeDex(File dex, InputStream another) throws IOException {
	 DexMerger m=new DexMerger(new Dex(dex), new Dex(another), CollisionPolicy.KEEP_FIRST);
	 m.merge().writeTo(dex);
	 m = null;
	 }*/
	private VProgressDialog vdialog=null;
	private void outputReal(final File f) {
		BuildApkPath = f;
		vdialog = ui.newProgressDialog().setTitle(get(L.Editor_BuildingApk)).setMessage(get(L.Wait)).setCancelable(false);
		vdialog.getDialog().setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		vdialog.getDialog().setIndeterminate(false);
		vdialog.show();
		jsc = false;
		new Thread(BuildApkRunnable).start();
	}
	File BuildApkPath;

	@Override
	public void onBackPressed() {
		if (!editor.isSaved()) {
			ui.newAlertDialog().setTitle(get(L.Remind)).setMessage(get(L.Editor_DidntSave))
				.setPositiveButton(get(L.Editor_SaveAndExit), new VAlertDialog.OnClickListener() {
					@Override
					public void onClick(VAlertDialog dialog, int pos) {
						try {
							IOUtil.write(pro.getFile(getNowOpen()), editor.getText().toString());
							EditorActivity.super.finish();
						} catch (IOException e) {
							err(e);
						}
					}
				}).setNegativeButton(get(L.Editor_DirectlyExit), new VAlertDialog.OnClickListener() {
					@Override
					public void onClick(VAlertDialog dialog, int pos) {
						EditorActivity.super.finish();
					}
				}).setNeutralButton(get(L.Editor_Cancel), new VAlertDialog.OnClickListener() {
					@Override
					public void onClick(VAlertDialog dialog, int pos) {
						((BDrawable) getBackDrawable()).animateTo(((BDrawable) getBackDrawable()).getProgress(), 0, 600, 0, new DecelerateInterpolator(), null);
					}
				}).setCancelable(true).setOnCancelListener(new VAlertDialog.OnCancelListener() {
					@Override
					public void onCancel(VAlertDialog dialog) {
						((BDrawable) getBackDrawable()).animateTo(((BDrawable) getBackDrawable()).getProgress(), 0, 600, 0, new DecelerateInterpolator(), null);
					}
				}).show();
			((BDrawable) getBackDrawable()).animateTo(((BDrawable) getBackDrawable()).getProgress(), 1, 600, 0, new DecelerateInterpolator(), null);
		} else super.finish();
	}
	private int nowOpen;
	private void openJs(final Jsc js) {
		if (Opened.contains(js)) {
			nowOpen = Opened.indexOf(js);
			OpenedSpinner.setSelection(nowOpen);
			readText(js);
			return;
		}
		Opened.add(js);
		OpenedAdapter.notifyDataSetChanged();
		nowOpen = Opened.size() - 1;
		add = true;
		OpenedSpinner.setSelection(nowOpen);
		readText(js);
	}
	private Spinner OpenedSpinner;
	private Jsc getNowOpen() {
		return Opened.get(nowOpen);
	}
	private static String[] EXTENDS=null;
	public void configUi() {
		final Jsc s=getNowOpen();
		if (EXTENDS == null) {
			EXTENDS = new String[JsExtend.values().length];
			for (int i=0;i < EXTENDS.length;i++) EXTENDS[i] = JsExtend.values()[i].name();
		}
		VAlertDialog d=ui.newAlertDialog().setTitle(s.getName());
		LinearLayout layout=new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		final VEditText ed=new VEditText(this);
		ed.setHint(get(L.Editor_Name));
		ed.setText(s.getName());
		layout.addView(ed);
		layout.setGravity(Gravity.CENTER);
		final Spinner sp=new Spinner(this);
		ArrayAdapter<String> ada=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, EXTENDS);
		ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(ada);
		sp.setSelection(s.getExtend().ordinal());
		layout.addView(sp);
		d.setView(layout).setPositiveButton(get(L.Editor_OK), false, new VAlertDialog.OnClickListener() {
				@Override
				public void onClick(VAlertDialog dialog, int pos) {
					String q=ed.getText().toString();
					if (q == null || q.length() == 0) {
						ed.setError(get(L.NameCantEmpty));
						return;
					}
					if (q.contains(File.separator)) {
						ed.setError(get(L.ContainsIllegalChar));
						return;
					}
					if (!q.endsWith(".js")) q += ".js";
					if (new File(pro.getDir(), q).exists()) {
						ed.setError(get(L.Editor_FileAlreadyExist));
						return;
					}
					pro.getFile(s).renameTo(new File(pro.getDir(), q));
					OpenedAdapter.notifyDataSetChanged();
					s.setName(q);
					s.setExtend(JsExtend.valueOf(sp.getSelectedItem().toString()));
					pro.saveManifest();
					refreshAutoComplete(s);
					dialog.dismiss();
				}
			}).setNegativeButton(get(L.Editor_Cancel), null).setCancelable(true).show();
	}
	private void refreshAutoComplete(Jsc c) {
		AutoCompletePanel.clearConst();
		switch (c.getExtend()) {
			case JsVActivity:{
					AutoCompletePanel.addConst("cx", pro.isCompat() ?JsVActivityCompat.class: JsVActivity.class);
					AutoCompletePanel.addConst("ui", pro.isCompat() ?com.jxs.vcompat.ui.UI.class: com.jxs.v.ui.UI.class);
					AutoCompletePanel.addConst("JsApp", JsApp.class);
					AutoCompletePanel.addConst("console", Console.class);
					break;
				}
			case JsConsole:{
					AutoCompletePanel.addConst("cx", pro.isCompat() ?JsVActivityCompat.class: JsVActivity.class);
					AutoCompletePanel.addConst("ui", pro.isCompat() ?com.jxs.vcompat.ui.UI.class: com.jxs.v.ui.UI.class);
					AutoCompletePanel.addConst("vin", InputStream.class);
					AutoCompletePanel.addConst("vout", PrintStream.class);
					AutoCompletePanel.addConst("JsApp", JsApp.class);
					AutoCompletePanel.addConst("console", Console.class);
					break;
				}
		}
	}
	private void importUi() {
		ui.newFileChooserDialog(Environment.getExternalStorageDirectory(), new FileChooserDialog.FileChooserListener() {
				@Override
				public void onChoose(final File f) {
					VProgressDialog p=new VProgressDialog(EditorActivity.this);
					p.setTitle(get(L.Wait)).setMessage(get(L.Importing)).setCancelable(false).putToUi("ImportAssets").show();
					new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									IOUtil.copy(f, new File(pro.getAssets(), f.getName()));
									VProgressDialog.getFromUi("ImportAssets").dismiss();
									ui.print(get(L.Done));
								} catch (IOException e) {
									VProgressDialog.getFromUi("ImportAssets").dismiss();
									ui.newAlertDialog().setTitle(get(L.Wrong)).setMessage(Log.getStackTraceString(e)).show();
								}
							}
						}).start();
				}
			}, true).setCancelable(true).show();
	}
	private void settingUi() {
		Jsc c=getNowOpen();
		VAlertDialog d=ui.newAlertDialog();
		d.setTitle(getNowOpen().getName());
		LinearLayout layout=new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER_HORIZONTAL);
		final VEditText ed=new VEditText(this);
		ed.setSingleLine(true);
		ed.setHint(get(L.Editor_Name));
		ed.setText(c.getName());
		layout.addView(ed, new LinearLayout.LayoutParams(-1, -2));
		final Spinner spin=new Spinner(this);
		layout.addView(spin, new LinearLayout.LayoutParams(-1, -2));
		ArrayAdapter<JsExtend> q = new ArrayAdapter<JsExtend>(this, android.R.layout.simple_spinner_item, JsExtend.values());
		q.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(q);
		spin.setSelection(c.getExtend().ordinal());
		d.setView(layout).setCancelable(true).setPositiveButton(get(L.Editor_OK), false, new VAlertDialog.OnClickListener() {
				@Override
				public void onClick(VAlertDialog dialog, int pos) {
					String w=ed.getText().toString();
					if (w == null || w.length() == 0) {
						ed.setError(get(L.NameCantEmpty));
						return;
					}
					if (w.contains("/")) {
						ed.setError(get(L.ContainsIllegalChar));
						return;
					}
					if (!w.endsWith(".js")) w += ".js";
					getNowOpen().setName(w);
					getNowOpen().setExtend(JsExtend.valueOf(spin.getSelectedItem().toString()));
					OpenedAdapter.notifyDataSetChanged();
					pro.saveManifest();
					refreshAutoComplete(getNowOpen());
					ui.print(get(L.Editor_ConfigEdited));
					dialog.dismiss();
				}
			}).setNegativeButton(get(L.Editor_Cancel), null).show();
	}
}
