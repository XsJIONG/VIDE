package com.jxs.vide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.dx.command.Main;
import com.jxs.vcompat.activity.VActivity;
import com.jxs.vcompat.fragment.SettingFragment;
import com.jxs.vcompat.io.IOUtil;
import com.jxs.vcompat.ui.ColorUtil;
import com.jxs.vcompat.ui.FileChooserDialog;
import com.jxs.vcompat.ui.UI;
import com.jxs.vcompat.ui.VAlertDialog;
import com.jxs.vcompat.ui.VOnClick;
import com.jxs.vcompat.ui.VProgressDialog;
import com.jxs.vcompat.widget.CardView;
import com.jxs.vcompat.widget.VEditText;
import com.jxs.vcompat.widget.VListView;
import com.jxs.vcompat.widget.VScrollView;
import dalvik.system.DexFile;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import static com.jxs.vide.L.get;

public class ProjectActivity extends VActivity {
	private Project pro;
	private LinearLayout Root;
	private ImageView _Icon;
	private TextView AppNameDes,PackageNameDes;
	private VEditText _AppName,_PackageName;
	@VOnClick("openEditor")
	private Button StartEdit;
	@VOnClick("saveManifest")
	private Button SaveManifest;
	@VOnClick("moreSettings")
	private Button MoreSettings;
	private CardView OtherSetting;
	private SettingFragment SettingFrag;
	private SettingFragment.CheckBoxItem _Compat,_UseDx;
	private VScrollView Scroll;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			pro = Project.getInstance(getIntent().getStringExtra("project"));
		} catch (Exception e) {err(e, true);}
		if (pro == null) {
			finish();
			return;
		}
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
		enableBackButton();
		setTitleText(pro.getName());
		Root = new LinearLayout(this);
		Root.setOrientation(LinearLayout.VERTICAL);
		Root.setGravity(Gravity.CENTER_HORIZONTAL);
		_Icon = new ImageView(this);
		try {
			_Icon.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(pro.getIcon())));
		} catch (IOException e) {
			ui.alert(get(L.Wrong), get(L.CouldNotLoadIcon));
		}
		_Icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i=new Intent(Intent.ACTION_PICK);
				i.setType("image/*");
				i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, 233);
			}
		});
		LinearLayout.LayoutParams para=new LinearLayout.LayoutParams(ui.dp2px(100), ui.dp2px(100));
		para.topMargin = ui.dp2px(20);
		Root.addView(_Icon, para);
		AppNameDes = new TextView(this);
		AppNameDes.setTextColor(UI.getThemeColor());
		AppNameDes.setText(get(L.AppName));
		PackageNameDes = new TextView(this);
		PackageNameDes.setTextColor(UI.getThemeColor());
		PackageNameDes.setText(get(L.PackageName));
		_AppName = new VEditText(this);
		_AppName.setText(pro.getAppName());
		_AppName.setSingleLine();
		LinearLayout Tmp=new LinearLayout(this);
		Tmp.setOrientation(LinearLayout.HORIZONTAL);
		Tmp.addView(AppNameDes, new LinearLayout.LayoutParams(-2, -2));
		Tmp.addView(_AppName, new LinearLayout.LayoutParams(-1, -2));
		Root.addView(Tmp);
		Tmp = new LinearLayout(this);
		Tmp.setOrientation(LinearLayout.HORIZONTAL);
		_PackageName = new VEditText(this);
		_PackageName.setText(pro.getPackageName());
		_PackageName.setSingleLine();
		Tmp.addView(PackageNameDes, new LinearLayout.LayoutParams(-2, -2));
		Tmp.addView(_PackageName, new LinearLayout.LayoutParams(-1, -2));
		Root.addView(Tmp);
		initSettings();
		para = new LinearLayout.LayoutParams(-1, -2);
		int m=ui.dp2px(15);
		para.setMargins(m, m, m, m);
		Scroll = new VScrollView(this);
		Root.addView(OtherSetting, para);
		LinearLayout ButtonLayout=new LinearLayout(this);
		ButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams ButtonPara=new LinearLayout.LayoutParams(0, -2);
		ButtonPara.weight = 1;
		ButtonPara.rightMargin = UI.dp2px(15);
		StartEdit = new Button(this);
		StartEdit.setText(get(L.EnterEditor));
		StartEdit.setBackgroundColor(UI.getThemeColor());
		StartEdit.setTextColor(ColorUtil.getBlackOrWhite(UI.getThemeColor()));
		SaveManifest = new Button(this);
		SaveManifest.setText(get(L.SaveManifest));
		SaveManifest.setBackgroundColor(UI.getThemeColor());
		SaveManifest.setTextColor(ColorUtil.getBlackOrWhite(UI.getThemeColor()));
		MoreSettings = new Button(this);
		MoreSettings.setText(get(L.MoreSettings));
		MoreSettings.setBackgroundColor(UI.getThemeColor());
		MoreSettings.setTextColor(ColorUtil.getBlackOrWhite(UI.getThemeColor()));
		/*try {
		 StartEdit.setElevation(0);
		 SaveManifest.setElevation(0);
		 MoreSettings.setElevation(0);
		 StartEdit.setTranslationZ(0);
		 SaveManifest.setTranslationZ(0);
		 MoreSettings.setTranslationZ(0);
		 } catch (Throwable t) {}*/
		ButtonLayout.addView(StartEdit, ButtonPara);
		ButtonLayout.addView(SaveManifest, ButtonPara);
		ButtonPara = new LinearLayout.LayoutParams(0, -2);
		ButtonPara.weight = 1;
		ButtonLayout.addView(MoreSettings, ButtonPara);
		para = new LinearLayout.LayoutParams(-1, -1);
		para.setMargins(m, m, m, m);
		Root.addView(ButtonLayout, para);
		Scroll.setFillViewport(true);
		Scroll.addView(Root);
		setContentView(Scroll);
	}
	private void initSettings() {
		SettingFrag = new SettingFragment(this);
		_Compat = SettingFrag.addCheckBoxItem(get(L.Project_UseCompat), get(L.Description_Compat), pro.isCompat());
		_UseDx = SettingFrag.addCheckBoxItem(get(L.Project_UseDx), get(L.Description_UseDx), pro.isUseDx());
		OtherSetting = new CardView(this);
		SettingFrag.onAttach();
		OtherSetting.addView(SettingFrag.getView());
	}
	@Override
	public void onThemeChange(String key) {
		super.onThemeChange(key);
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		int c=UI.getThemeColor();
		AppNameDes.setTextColor(c);
		PackageNameDes.setTextColor(c);
		StartEdit.setBackgroundColor(c);
		StartEdit.setTextColor(ColorUtil.getBlackOrWhite(c));
		SaveManifest.setBackgroundColor(c);
		SaveManifest.setTextColor(ColorUtil.getBlackOrWhite(c));
		MoreSettings.setBackgroundColor(c);
		MoreSettings.setTextColor(ColorUtil.getBlackOrWhite(c));
		int w=ColorUtil.getBlackOrWhite(c);
		if (_Menu != null) for (int i=0;i < MENU_COUNT;i++) UI.tintDrawable(_Menu.getItem(i).getIcon(), w);
	}
	public void openEditor(View v) {
		startActivity(new Intent(this, EditorActivity.class).putExtra("project", pro.getName()));
	}
	public void moreSettings(View v) {
		VAlertDialog dialog=ui.newAlertDialog();
		dialog.setTitle(get(L.MoreSettings));
		dialog.setItems(L.gets(L.VersionInfo, L.EditPermissions), new VAlertDialog.OnClickListener() {
			@Override
			public void onClick(VAlertDialog dialog, int pos) {
				if (pos == 0) versionInfo(); else editPermission();
			}
		});
		dialog.setCancelable(true).setPositiveButton(get(L.Cancel), null).show();
	}
	private void versionInfo() {
		VAlertDialog d=ui.newAlertDialog();
		d.setTitle(get(L.VersionInfo));
		LinearLayout layout=new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		final EditText VCED=new VEditText(this);
		VCED.setHint(get(L.VersionCode));
		VCED.setText(String.valueOf(pro.getVersionCode()));
		VCED.setSingleLine(true);
		VCED.setInputType(InputType.TYPE_CLASS_NUMBER);
		layout.addView(VCED);
		final EditText VNED=new VEditText(this);
		VNED.setHint(get(L.VersionName));
		VNED.setText(pro.getVersionName());
		VNED.setSingleLine(true);
		layout.addView(VNED);
		d.setView(layout);
		d.setPositiveButton(get(L.OK), false, new VAlertDialog.OnClickListener() {
			@Override
			public void onClick(VAlertDialog dialog, int pos) {
				String codes=VCED.getText().toString();
				if (codes == null || codes.length() == 0) {
					VCED.setError(get(L.VersionCodeCannotBeEmpty));
					return;
				}
				int code;
				try {
					code = Integer.valueOf(codes);
				} catch (NumberFormatException e) {
					VCED.setError(get(L.ContainsIllegalChar));
					return;
				}
				String s=VNED.getText().toString();
				if (s == null || s.length() == 0) {
					VNED.setError(get(L.VersionNameCannotBeEmpty));
					return;
				}
				pro.setVersionCode(code);
				pro.setVersionName(s);
				pro.saveManifest();
				ui.print(get(L.EditSuccessfully));
				dialog.dismiss();
			}
		}).setNegativeButton(get(L.Cancel), null);
		d.setCancelable(true).show();
	}
	private void editPermission() {
		VAlertDialog dialog=ui.newAlertDialog();
		EditText d=new VEditText(this);
		d.setHint(get(L.EditPermissions));
		LinearLayout layout=new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(d);
		VListView lv=new VListView(this);
		final PermissionAdapter ada=new PermissionAdapter(this, pro);
		lv.setAdapter(ada);
		d.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int st, int en, int ch) {}
			@Override
			public void onTextChanged(CharSequence s, int st, int en, int ch) {
				ada.setFilter(s.toString());
			}
			@Override
			public void afterTextChanged(Editable ed) {}
		});
		layout.addView(lv);
		dialog.setView(layout);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				PermissionAdapter.ViewHolder vh=(PermissionAdapter.ViewHolder) view.getTag();
				if (vh.isChoosed()) {
					pro.removePermission(pos * 2);
					pro.saveManifest();
					vh.setChoosed(false);
				} else {
					pro.addPermission(pos * 2);
					pro.saveManifest();
					vh.setChoosed(true);
				}
			}
		});
		dialog.setCancelable(true).setPositiveButton(get(L.OK), null).show();
	}
	private static class PermissionAdapter extends BaseAdapter {
		private Context cx;
		private Project pro;
		public PermissionAdapter(Context cx, Project pro) {
			this.cx = cx;
			this.pro = pro;
			setFilter(null);
		}
		@Override
		public int getCount() {
			return All.size();
		}
		@Override
		public long getItemId(int id) {
			return id;
		}
		@Override
		public Object getItem(int index) {
			return Global.Permissions[All.get(index)];
		}
		@Override
		public View getView(int pos, View v, ViewGroup parent) {
			LinearLayout root=new LinearLayout(cx);
			TextView tv=new TextView(cx);
			tv.setText(Global.Permissions[All.get(pos) + 1]);
			int d=UI.dp2px(15);
			root.setPadding(d, d, d, d);
			root.addView(tv);
			ViewHolder h=new ViewHolder();
			h.root = root;
			h.tv = tv;
			h.setChoosed(pro.getPermissions().contains(All.get(pos)));
			root.setTag(h);
			return root;
		}
		private String filter=null;
		private ArrayList<Integer> All=new ArrayList<>();
		public void setFilter(String s) {
			if (s == null || s.length() == 0) filter = null; else filter = s.toLowerCase();
			All.clear();
			if (filter == null) {
				for (int i=0;i < Global.Permissions.length;i += 2) All.add(i);
			} else {
				for (int i=0;i < Global.Permissions.length;i += 2)
					if (Global.Permissions[i].toLowerCase().contains(filter) || Global.Permissions[i + 1].toLowerCase().contains(filter)) All.add(i);
			}
			notifyDataSetChanged();
		}
		private static class ViewHolder {
			public TextView tv;
			public LinearLayout root;
			public boolean isChoosed() {
				return ((ColorDrawable) root.getBackground()).getColor() != Color.WHITE;
			}
			public void setChoosed(boolean flag) {
				root.setBackgroundColor(flag ?UI.getThemeColor(): Color.WHITE);
				tv.setTextColor(flag ?ColorUtil.getBlackOrWhite(UI.getThemeColor()): Color.BLACK);
			}
		}
	}
	public void saveManifest(View v) {
		String appname=_AppName.getText().toString();
		String pname=_PackageName.getText().toString();
		String ill=Project.isAppNameValid(appname);
		if (ill != null) {
			_AppName.setError(ill);
			return;
		}
		ill = Project.isPackageNameValid(pname);
		if (ill != null) {
			_PackageName.setError(ill);
			return;
		}
		boolean dif=!appname.equals(pro.getAppName());
		pro.setPackageName(pname);
		pro.setCompat(_Compat.isChecked());
		pro.setUseDx(_UseDx.isChecked());
		if (dif) {
			File dir=pro.getDir();
			File ndir=new File(dir.getParentFile(), appname);
			if (ndir.exists()) {
				_AppName.setError(get(L.Main_ProjectAlreadyExist));
				return;
			}
			dir.renameTo(ndir);
			try {
				pro = Project.getInstance(ndir);
				pro.loadManifest();
				pro.setAppName(appname);
				pro.saveManifest();
			} catch (Exception e) {}
			if (MainActivity.cx != null) MainActivity.cx.notifyProjectRename();
		}
		pro.saveManifest();
		ui.print(get(L.ManifestSaved));
	}
	private static int MENU_COUNT=2;
	private Menu _Menu;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		_Menu = menu;
		menu.add(0, 1, 1, get(L.Delete)).setIcon(R.drawable.icon_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, 2, 2, get(L.Jar_Files)).setIcon(R.drawable.icon_zip).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}
	private VAlertDialog JarDialog;
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getOrder()) {
			case 1:{
					ui.newAlertDialog().setTitle(get(L.Warning)).setMessage(get(L.SureDeleteProject)).setCancelable(true).setPositiveButton(get(L.OK), new VAlertDialog.OnClickListener() {
						@Override
						public void onClick(VAlertDialog dialog, int pos) {
							IOUtil.delete(pro.getDir());
							if (MainActivity.cx != null) {
								MainActivity.cx.notifyProjectRename();
								MainActivity.cx.ui.print(get(L.Deleted));
							}
							finish();
						}
					}).setNegativeButton(get(L.Cancel), null).setOnCancelListener(new VAlertDialog.OnCancelListener() {
						@Override
						public void onCancel(VAlertDialog dialog) {
							JarDialog = null;
						}
					}).show();
					break;
				}
			case 2:{
					final File[] fs=pro.getLibDir().listFiles(new FileFilter() {
						@Override
						public boolean accept(File f) {
							return f.getName().endsWith(".dex");
						}
					});
					String[] show=new String[fs.length];
					for (int i=0;i < fs.length;i++) show[i] = fs[i].getName();
					JarDialog = ui.newAlertDialog().setTitle(get(L.Jar_Files)).setCancelable(true).setPositiveButton(get(L.Import_Jar), new VAlertDialog.OnClickListener() {
						@Override
						public void onClick(VAlertDialog dialog, int pos) {
							importJar();
						}
					}).setNegativeButton(get(L.Cancel), null);
					if (show.length == 0)
						JarDialog.setView(new CryView(ProjectActivity.this));
					else {
						JarDialog.setItems(show, new VAlertDialog.OnClickListener() {
							@Override
							public void onClick(VAlertDialog dialog, int pos) {
								jarDialog(fs[pos]);
							}
						});
					}
					JarDialog.show();
					break;
				}
		}
		return super.onOptionsItemSelected(item);
	}
	private void jarDialog(final File jar) {
		if (!jar.exists()) return;
		ui.newAlertDialog().setTitle(jar.getName()).setItems(L.gets(L.Jar_Rename, L.Jar_Delete), new VAlertDialog.OnClickListener() {
			@Override
			public void onClick(VAlertDialog dialog, int pos) {
				switch (pos) {
					case 0:{
							renameJar(jar);
							break;
						}
					case 1:{
							jar.delete();
							String s=jar.getPath();
							new File(String.format("%s_CTree.txt", s.substring(0, s.lastIndexOf('.')))).delete();
							ui.print(get(L.Jar_Deleted));
							//refreshJarDialog();
							break;
						}
				}
			}
		}).setCancelable(true).show();
	}
	public void renameJar(final File jar) {
		VAlertDialog d=ui.newAlertDialog().setTitle(get(L.Jar_Rename)).setEditHint(get(L.Jar_Name)).setEdit(jar.getName()).setPositiveButton(get(L.OK), false, new VAlertDialog.OnClickListener() {
			@Override
			public void onClick(VAlertDialog dialog, int pos) {
				String s=dialog.getInputText();
				if (s == null || s.length() == 0) {
					dialog.getEditText().setError(get(L.Jar_Name_Empty));
					return;
				}
				if (s.contains(File.separator)) {
					dialog.getEditText().setError(get(L.ContainsIllegalChar));
					return;
				}
				if (!s.endsWith(".dex")) s += ".dex";
				jar.renameTo(new File(jar.getParentFile(), s));
				ui.print(get(L.Jar_Renamed));
				//refreshJarDialog();
				dialog.dismiss();
			}
		}).setNegativeButton(get(L.Cancel), null).setCancelable(true);
		d.getEditText().setSingleLine(true);
		d.show();
	}
	private void importJar() {
		ui.newFileChooserDialog(Environment.getExternalStorageDirectory(), new FileChooserDialog.FileChooserListener() {
			@Override
			public void onChoose(File f) {
				if (f.isDirectory()) {
					ui.alert(get(L.Error), get(L.CanNotChooseDir));
					return;
				}
				if (!f.exists()) return;
				if (!(f.getName().endsWith(".dex") || f.getName().endsWith(".jar"))) {
					ui.alert(get(L.Error), get(L.CanOnlyChooseJarOrDex));
					return;
				}
				if (f.getName().endsWith(".jar")) {
					//try {
					LoadingDialog = ui.newLoadingDialog().setTitle(get(L.Wait)).setMessage(get(L.Converting)).setCancelable(false);
					//} catch (Throwable t) {err(t);}
					LoadingDialog.getDialog().setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					LoadingDialog.getDialog().setIndeterminate(false);
					LoadingDialog.show();
					new Thread(new ConvertJarRunnable(f)).start();
				} else importDex(f);
			}
		}, false).setCancelable(true).show();
	}
	private VProgressDialog LoadingDialog;
	private void importDex(File f) {
		String st=f.getName();
		st = st.substring(0, st.lastIndexOf('.'));
		int count=0;
		File q=null;
		while ((q = new File(pro.getLibDir(), st + (count == 0 ?"": String.format("(%d)", count)) + ".dex")).exists()) count++;
		try {
			IOUtil.copy(f, q);
			CTree c=new CTree();
			DexFile file=new DexFile(q);
			Enumeration<String> e=file.entries();
			String s;
			while (e.hasMoreElements()) {
				s = e.nextElement();
				if (s.contains("$")) continue;
				c.addLeaf(s.split("[.]"));
			}
			file.close();
			String p=q.getPath();
			p = String.format("%s_CTree.txt", p.substring(0, p.lastIndexOf('.')));
			FileOutputStream out=new FileOutputStream(p);
			out.write(c.toString().getBytes());
			out.close();
			ui.print(get(L.Jar_Imported));
			//refreshJarDialog();
		} catch (IOException e) {ui.alert(get(L.Wrong), Log.getStackTraceString(e));}
	}
	private Handler handler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:if (LoadingDialog != null) LoadingDialog.setProgress((Integer) msg.obj);break;
				case 1:if (LoadingDialog != null) {
						LoadingDialog.dismiss();
						LoadingDialog = null;
						break;
					}
			}
		}
	};
	private void sendMsg(int type, Object data) {
		Message msg=new Message();
		msg.what = type;
		msg.obj = data;
		handler.sendMessage(msg);
	}
	private class ConvertJarRunnable implements Runnable {
		public void dismiss() {
			if (LoadingDialog == null) return;
			sendMsg(1, null);
		}
		private File f;
		public ConvertJarRunnable(File jarFile) {
			f = jarFile;
		}
		@Override
		public void run() {
			String st=f.getName();
			st = st.substring(0, st.lastIndexOf('.'));
			int count=0;
			File q=null;
			while ((q = new File(pro.getLibDir(), st + (count == 0 ?"": String.format("(%d)", count)) + ".dex")).exists()) count++;
			try {
				Main.main(new String[] {
							  "--dex","--output",
							  q.getPath(),f.getPath()
						  });
				sendMsg(0, 50);
				CTree c=new CTree();
				DexFile file=new DexFile(q);
				Enumeration<String> e=file.entries();
				String s;
				while (e.hasMoreElements()) {
					s = e.nextElement();
					if (s.contains("$")) continue;
					c.addLeaf(s.split("[.]"));
				}
				file.close();
				String p=q.getPath();
				p = String.format("%s_CTree.txt", p.substring(0, p.lastIndexOf('.')));
				FileOutputStream out=new FileOutputStream(p);
				out.write(c.toString().getBytes());
				out.close();
				sendMsg(1,  null);
			} catch (final Throwable t) {
				ui.autoOnUi(new Runnable() {
					@Override
					public void run() {
						err(t);
					}
				});
			}
			dismiss();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 233 && resultCode == RESULT_OK && data != null) {
			try {
				Uri uri=data.getData();
				int w=_Icon.getWidth();
				BitmapFactory.Options op=new BitmapFactory.Options();
				op.inJustDecodeBounds = true;
				Bitmap RawIcon=BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
				long S=RawIcon.getWidth() * RawIcon.getHeight();
				long mem=Runtime.getRuntime().freeMemory() / 2;
				if (S > mem) {
					int b=(int) Math.ceil((float) S / mem);
					op.inSampleSize = b;
				}
				op.inJustDecodeBounds = false;
				RawIcon = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
				Bitmap fixed=Bitmap.createScaledBitmap(RawIcon, w, w, true);
				RawIcon.recycle();
				FileOutputStream out=new FileOutputStream(pro.getIcon());
				fixed.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.close();
				_Icon.setImageBitmap(fixed);
				System.gc();
			} catch (Throwable t) {
				err(t);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
