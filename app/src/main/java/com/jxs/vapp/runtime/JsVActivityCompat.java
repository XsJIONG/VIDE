package com.jxs.vapp.runtime;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.database.*;
import android.database.sqlite.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.net.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.support.v7.view.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.view.accessibility.*;
import com.jxs.vapp.program.*;
import com.jxs.vcompat.activity.*;
import com.jxs.vcompat.fragment.*;
import com.jxs.vcompat.ui.*;
import java.io.*;
import org.mozilla.javascript.*;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.view.ActionMode;
import com.jxs.vide.*;

public class JsVActivityCompat extends VActivity {
	private JsProgram program;
	private Resources RS;
	@Override
	public Resources getResources() {
		return RS;
	}
	@Override
	public AssetManager getAssets() {
		return RS.getAssets();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (init())
			callFunction(toArray(savedInstanceState));
	}
	public static final int TYPE_FILE=1;
	private JsObjectRef newJsRef() {return new JsObjectRef();}
	private boolean init() {
		if (program != null && (!program.isDestroyed())) return false;
		if (program != null) program.destroy();
		program = JsProgram.download(getIntent().getStringExtra("ID"));
		if (program==null) {
			if (getIntent().getStringExtra("code")!=null) {
				program=new JsProgram("Learning");
				program.setCode(getIntent().getStringExtra("code"));
			} else {
				try {
					JsApp.INSTANCE=new JsApp(getAssets().open("Main.jsc"));
					JsApp.GlobalContext = getApplicationContext();
					if (JsApp.INSTANCE.getMainJs().getExtend()==JsExtend.JsVActivity) {
						Jsc c=JsApp.INSTANCE.getMainJs();
						program=new JsProgram(c.getName());
						program.setJsApp(JsApp.INSTANCE);
						program.setCode(c.getCode());
					} else return false;
				} catch (Exception e) {
					//Mark
					//FIXME
				}
			}
		}
		program.defineProperty("cx", this, ScriptableObject.CONST);
		program.defineProperty("ui", ui, ScriptableObject.CONST);
		program.setOnErrorListener(new Program.OnErrorListener() {
			@Override
			public void onError(Throwable e) {
				program.setOnErrorListener(null);
				if (!program.callFunction("onError", toArray(e))) {
					ui.newAlertDialog().setTitle("Error").setMessage(Log.getStackTraceString(e)).setCancelable(false).setPositiveButton("OK", new VAlertDialog.OnClickListener() {
						@Override
						public void onClick(VAlertDialog dialog, int pos) {
							finish();
						}
					}).show();
				}
				program.setOnErrorListener(this);
			}
		});
		program.run();
		return true;
	}
	private Object[] toArray(Object...args) {return Program.toArray(args);}
	private boolean callFunction() {if (program == null) return false;return program.callFunction(Program.getMethodName(1), null, null);}
	private boolean callFunction(JsObjectRef ref) {if (program == null) return false;return program.callFunction(Program.getMethodName(1), ref, null);}
	private boolean callFunction(Object[] args) {if (program == null) return false;return program.callFunction(Program.getMethodName(1), null, args);}
	private boolean callFunction(JsObjectRef ref, Object[] args) {if (program == null) return false;return program.callFunction(Program.getMethodName(1), ref, args);}
	private final String join(String...all) {StringBuffer b=new StringBuffer();for (String one : all) b.append(one);return b.toString();}
	private final void EF(String name, int len) {
		throw new IllegalArgumentException(join("Error calling super method ", name, ": Got arguments with a length of ", len+"", ", but can not find matches method."));
	}
	public Object vsuper(String name) throws Throwable {
		return vsuper(name, new Object[0]);
	}
	public Object vsuper(String name, Object...args) throws Throwable {
		if (args==null) throw new IllegalArgumentException("Error calling super method "+name+": Args can not be null");
		switch (name) {
			case "addContentView":
				if (args.length==2&&args[0] instanceof View&&args[1] instanceof ViewGroup.LayoutParams) {super.addContentView((View) args[0], (ViewGroup.LayoutParams) args[1]);return null;}
				EF(name,args.length);break;
			case "applyOverrideConfiguration":
				if (args.length==1&&args[0] instanceof Configuration) {super.applyOverrideConfiguration((Configuration) args[0]);return null;}
				EF(name,args.length);break;
			case "attachBaseContext":
				if (args.length==1&&args[0] instanceof Context) {super.attachBaseContext((Context) args[0]);return null;}
				EF(name,args.length);break;
			case "bindService":
				if (args.length==3&&args[0] instanceof Intent&&args[1] instanceof ServiceConnection&&args[2] instanceof int) return super.bindService((Intent) args[0], (ServiceConnection) args[1], (int) args[2]);
				EF(name,args.length);break;
			case "checkCallingOrSelfPermission":
				if (args.length==1&&args[0] instanceof String) return super.checkCallingOrSelfPermission((String) args[0]);
				EF(name,args.length);break;
			case "checkCallingOrSelfUriPermission":
				if (args.length==2&&args[0] instanceof Uri&&args[1] instanceof int) return super.checkCallingOrSelfUriPermission((Uri) args[0], (int) args[1]);
				EF(name,args.length);break;
			case "checkCallingPermission":
				if (args.length==1&&args[0] instanceof String) return super.checkCallingPermission((String) args[0]);
				EF(name,args.length);break;
			case "checkCallingUriPermission":
				if (args.length==2&&args[0] instanceof Uri&&args[1] instanceof int) return super.checkCallingUriPermission((Uri) args[0], (int) args[1]);
				EF(name,args.length);break;
			case "checkPermission":
				if (args.length==3&&args[0] instanceof String&&args[1] instanceof int&&args[2] instanceof int) return super.checkPermission((String) args[0], (int) args[1], (int) args[2]);
				EF(name,args.length);break;
			case "checkSelfPermission":
				if (args.length==1&&args[0] instanceof String) return super.checkSelfPermission((String) args[0]);
				EF(name,args.length);break;
			case "checkUriPermission":
				if (args.length==4&&args[0] instanceof Uri&&args[1] instanceof int&&args[2] instanceof int&&args[3] instanceof int) return super.checkUriPermission((Uri) args[0], (int) args[1], (int) args[2], (int) args[3]);
				if (args.length==6&&args[0] instanceof Uri&&args[1] instanceof String&&args[2] instanceof String&&args[3] instanceof int&&args[4] instanceof int&&args[5] instanceof int) return super.checkUriPermission((Uri) args[0], (String) args[1], (String) args[2], (int) args[3], (int) args[4], (int) args[5]);
				EF(name,args.length);break;
			case "clearWallpaper":
				if (args.length==0) {super.clearWallpaper();return null;}
				EF(name,args.length);break;
			case "closeContextMenu":
				if (args.length==0) {super.closeContextMenu();return null;}
				EF(name,args.length);break;
			case "closeOptionsMenu":
				if (args.length==0) {super.closeOptionsMenu();return null;}
				EF(name,args.length);break;
			case "createConfigurationContext":
				if (args.length==1&&args[0] instanceof Configuration) return super.createConfigurationContext((Configuration) args[0]);
				EF(name,args.length);break;
			case "createDisplayContext":
				if (args.length==1&&args[0] instanceof Display) return super.createDisplayContext((Display) args[0]);
				EF(name,args.length);break;
			case "createPackageContext":
				if (args.length==2&&args[0] instanceof String&&args[1] instanceof int) return super.createPackageContext((String) args[0], (int) args[1]);
				EF(name,args.length);break;
			case "createPendingResult":
				if (args.length==3&&args[0] instanceof int&&args[1] instanceof Intent&&args[2] instanceof int) return super.createPendingResult((int) args[0], (Intent) args[1], (int) args[2]);
				EF(name,args.length);break;
			case "databaseList":
				if (args.length==0) return super.databaseList();
				EF(name,args.length);break;
			case "deleteDatabase":
				if (args.length==1&&args[0] instanceof String) return super.deleteDatabase((String) args[0]);
				EF(name,args.length);break;
			case "deleteFile":
				if (args.length==1&&args[0] instanceof String) return super.deleteFile((String) args[0]);
				EF(name,args.length);break;
			case "disableBackButton":
				if (args.length==0) {super.disableBackButton();return null;}
				EF(name,args.length);break;
			case "dispatchGenericMotionEvent":
				if (args.length==1&&args[0] instanceof MotionEvent) return super.dispatchGenericMotionEvent((MotionEvent) args[0]);
				EF(name,args.length);break;
			case "dispatchKeyEvent":
				if (args.length==1&&args[0] instanceof KeyEvent) return super.dispatchKeyEvent((KeyEvent) args[0]);
				EF(name,args.length);break;
			case "dispatchKeyShortcutEvent":
				if (args.length==1&&args[0] instanceof KeyEvent) return super.dispatchKeyShortcutEvent((KeyEvent) args[0]);
				EF(name,args.length);break;
			case "dispatchPopulateAccessibilityEvent":
				if (args.length==1&&args[0] instanceof AccessibilityEvent) return super.dispatchPopulateAccessibilityEvent((AccessibilityEvent) args[0]);
				EF(name,args.length);break;
			case "dispatchTouchEvent":
				if (args.length==1&&args[0] instanceof MotionEvent) return super.dispatchTouchEvent((MotionEvent) args[0]);
				EF(name,args.length);break;
			case "dispatchTrackballEvent":
				if (args.length==1&&args[0] instanceof MotionEvent) return super.dispatchTrackballEvent((MotionEvent) args[0]);
				EF(name,args.length);break;
			case "dump":
				if (args.length==4&&args[0] instanceof String&&args[1] instanceof FileDescriptor&&args[2] instanceof PrintWriter&&args[3] instanceof String[]) {super.dump((String) args[0], (FileDescriptor) args[1], (PrintWriter) args[2], (String[]) args[3]);return null;}
				EF(name,args.length);break;
			case "enableBackButton":
				if (args.length==0) {super.enableBackButton();return null;}
				EF(name,args.length);break;
			case "enforceCallingOrSelfPermission":
				if (args.length==2&&args[0] instanceof String&&args[1] instanceof String) {super.enforceCallingOrSelfPermission((String) args[0], (String) args[1]);return null;}
				EF(name,args.length);break;
			case "enforceCallingOrSelfUriPermission":
				if (args.length==3&&args[0] instanceof Uri&&args[1] instanceof int&&args[2] instanceof String) {super.enforceCallingOrSelfUriPermission((Uri) args[0], (int) args[1], (String) args[2]);return null;}
				EF(name,args.length);break;
			case "enforceCallingPermission":
				if (args.length==2&&args[0] instanceof String&&args[1] instanceof String) {super.enforceCallingPermission((String) args[0], (String) args[1]);return null;}
				EF(name,args.length);break;
			case "enforceCallingUriPermission":
				if (args.length==3&&args[0] instanceof Uri&&args[1] instanceof int&&args[2] instanceof String) {super.enforceCallingUriPermission((Uri) args[0], (int) args[1], (String) args[2]);return null;}
				EF(name,args.length);break;
			case "enforcePermission":
				if (args.length==4&&args[0] instanceof String&&args[1] instanceof int&&args[2] instanceof int&&args[3] instanceof String) {super.enforcePermission((String) args[0], (int) args[1], (int) args[2], (String) args[3]);return null;}
				EF(name,args.length);break;
			case "enforceUriPermission":
				if (args.length==5&&args[0] instanceof Uri&&args[1] instanceof int&&args[2] instanceof int&&args[3] instanceof int&&args[4] instanceof String) {super.enforceUriPermission((Uri) args[0], (int) args[1], (int) args[2], (int) args[3], (String) args[4]);return null;}
				if (args.length==7&&args[0] instanceof Uri&&args[1] instanceof String&&args[2] instanceof String&&args[3] instanceof int&&args[4] instanceof int&&args[5] instanceof int&&args[6] instanceof String) {super.enforceUriPermission((Uri) args[0], (String) args[1], (String) args[2], (int) args[3], (int) args[4], (int) args[5], (String) args[6]);return null;}
				EF(name,args.length);break;
			case "err":
				if (args.length==1&&args[0] instanceof Throwable) {super.err((Throwable) args[0]);return null;}
				if (args.length==2&&args[0] instanceof Throwable&&args[1] instanceof boolean) {super.err((Throwable) args[0], (boolean) args[1]);return null;}
				EF(name,args.length);break;
			case "fileList":
				if (args.length==0) return super.fileList();
				EF(name,args.length);break;
			case "findViewById":
				if (args.length==1&&args[0] instanceof int) return super.findViewById((int) args[0]);
				EF(name,args.length);break;
			case "finish":
				if (args.length==0) {super.finish();return null;}
				EF(name,args.length);break;
			case "finishActivity":
				if (args.length==1&&args[0] instanceof int) {super.finishActivity((int) args[0]);return null;}
				EF(name,args.length);break;
			case "finishActivityFromChild":
				if (args.length==2&&args[0] instanceof Activity&&args[1] instanceof int) {super.finishActivityFromChild((Activity) args[0], (int) args[1]);return null;}
				EF(name,args.length);break;
			case "finishAffinity":
				if (args.length==0) {super.finishAffinity();return null;}
				EF(name,args.length);break;
			case "finishAfterTransition":
				if (args.length==0) {super.finishAfterTransition();return null;}
				EF(name,args.length);break;
			case "finishAndRemoveTask":
				if (args.length==0) {super.finishAndRemoveTask();return null;}
				EF(name,args.length);break;
			case "finishFromChild":
				if (args.length==1&&args[0] instanceof Activity) {super.finishFromChild((Activity) args[0]);return null;}
				EF(name,args.length);break;
			case "getActionBar":
				if (args.length==0) return super.getActionBar();
				EF(name,args.length);break;
			case "getApplicationContext":
				if (args.length==0) return super.getApplicationContext();
				EF(name,args.length);break;
			case "getApplicationInfo":
				if (args.length==0) return super.getApplicationInfo();
				EF(name,args.length);break;
			case "getBaseContext":
				if (args.length==0) return super.getBaseContext();
				EF(name,args.length);break;
			case "getCacheDir":
				if (args.length==0) return super.getCacheDir();
				EF(name,args.length);break;
			case "getCallingActivity":
				if (args.length==0) return super.getCallingActivity();
				EF(name,args.length);break;
			case "getCallingPackage":
				if (args.length==0) return super.getCallingPackage();
				EF(name,args.length);break;
			case "getChangingConfigurations":
				if (args.length==0) return super.getChangingConfigurations();
				EF(name,args.length);break;
			case "getClassLoader":
				if (args.length==0) return super.getClassLoader();
				EF(name,args.length);break;
			case "getCodeCacheDir":
				if (args.length==0) return super.getCodeCacheDir();
				EF(name,args.length);break;
			case "getComponentName":
				if (args.length==0) return super.getComponentName();
				EF(name,args.length);break;
			case "getContentResolver":
				if (args.length==0) return super.getContentResolver();
				EF(name,args.length);break;
			case "getContentTransitionManager":
				if (args.length==0) return super.getContentTransitionManager();
				EF(name,args.length);break;
			case "getCurrentFocus":
				if (args.length==0) return super.getCurrentFocus();
				EF(name,args.length);break;
			case "getDatabasePath":
				if (args.length==1&&args[0] instanceof String) return super.getDatabasePath((String) args[0]);
				EF(name,args.length);break;
			case "getDelegate":
				if (args.length==0) return super.getDelegate();
				EF(name,args.length);break;
			case "getDir":
				if (args.length==2&&args[0] instanceof String&&args[1] instanceof int) return super.getDir((String) args[0], (int) args[1]);
				EF(name,args.length);break;
			case "getDrawerToggleDelegate":
				if (args.length==0) return super.getDrawerToggleDelegate();
				EF(name,args.length);break;
			case "getExternalCacheDir":
				if (args.length==0) return super.getExternalCacheDir();
				EF(name,args.length);break;
			case "getExternalCacheDirs":
				if (args.length==0) return super.getExternalCacheDirs();
				EF(name,args.length);break;
			case "getExternalFilesDir":
				if (args.length==1&&args[0] instanceof String) return super.getExternalFilesDir((String) args[0]);
				EF(name,args.length);break;
			case "getExternalFilesDirs":
				if (args.length==1&&args[0] instanceof String) return super.getExternalFilesDirs((String) args[0]);
				EF(name,args.length);break;
			case "getExternalMediaDirs":
				if (args.length==0) return super.getExternalMediaDirs();
				EF(name,args.length);break;
			case "getFileStreamPath":
				if (args.length==1&&args[0] instanceof String) return super.getFileStreamPath((String) args[0]);
				EF(name,args.length);break;
			case "getFilesDir":
				if (args.length==0) return super.getFilesDir();
				EF(name,args.length);break;
			case "getFragmentManager":
				if (args.length==0) return super.getFragmentManager();
				EF(name,args.length);break;
			case "getIntent":
				if (args.length==0) return super.getIntent();
				EF(name,args.length);break;
			case "getLastCustomNonConfigurationInstance":
				if (args.length==0) return super.getLastCustomNonConfigurationInstance();
				EF(name,args.length);break;
			case "getLastNonConfigurationInstance":
				if (args.length==0) return super.getLastNonConfigurationInstance();
				EF(name,args.length);break;
			case "getLayoutInflater":
				if (args.length==0) return super.getLayoutInflater();
				EF(name,args.length);break;
			case "getLoaderManager":
				if (args.length==0) return super.getLoaderManager();
				EF(name,args.length);break;
			case "getLocalClassName":
				if (args.length==0) return super.getLocalClassName();
				EF(name,args.length);break;
			case "getLogTag":
				if (args.length==0) return super.getLogTag();
				EF(name,args.length);break;
			case "getMainLooper":
				if (args.length==0) return super.getMainLooper();
				EF(name,args.length);break;
			case "getMenuInflater":
				if (args.length==0) return super.getMenuInflater();
				EF(name,args.length);break;
			case "getNoBackupFilesDir":
				if (args.length==0) return super.getNoBackupFilesDir();
				EF(name,args.length);break;
			case "getObbDir":
				if (args.length==0) return super.getObbDir();
				EF(name,args.length);break;
			case "getObbDirs":
				if (args.length==0) return super.getObbDirs();
				EF(name,args.length);break;
			case "getPackageCodePath":
				if (args.length==0) return super.getPackageCodePath();
				EF(name,args.length);break;
			case "getPackageManager":
				if (args.length==0) return super.getPackageManager();
				EF(name,args.length);break;
			case "getPackageName":
				if (args.length==0) return super.getPackageName();
				EF(name,args.length);break;
			case "getPackageResourcePath":
				if (args.length==0) return super.getPackageResourcePath();
				EF(name,args.length);break;
			case "getParentActivityIntent":
				if (args.length==0) return super.getParentActivityIntent();
				EF(name,args.length);break;
			case "getPreferences":
				if (args.length==1&&args[0] instanceof int) return super.getPreferences((int) args[0]);
				EF(name,args.length);break;
			case "getReferrer":
				if (args.length==0) return super.getReferrer();
				EF(name,args.length);break;
			case "getRequestedOrientation":
				if (args.length==0) return super.getRequestedOrientation();
				EF(name,args.length);break;
			case "getSharedPreferences":
				if (args.length==2&&args[0] instanceof String&&args[1] instanceof int) return super.getSharedPreferences((String) args[0], (int) args[1]);
				EF(name,args.length);break;
			case "getStatusBarHeight":
				if (args.length==0) return super.getStatusBarHeight();
				EF(name,args.length);break;
			case "getSupportActionBar":
				if (args.length==0) return super.getSupportActionBar();
				EF(name,args.length);break;
			case "getSupportFragmentManager":
				if (args.length==0) return super.getSupportFragmentManager();
				EF(name,args.length);break;
			case "getSupportLoaderManager":
				if (args.length==0) return super.getSupportLoaderManager();
				EF(name,args.length);break;
			case "getSupportParentActivityIntent":
				if (args.length==0) return super.getSupportParentActivityIntent();
				EF(name,args.length);break;
			case "getSystemService":
				if (args.length==1&&args[0] instanceof String) return super.getSystemService((String) args[0]);
				EF(name,args.length);break;
			case "getSystemServiceName":
				if (args.length==1&&args[0] instanceof Class) return super.getSystemServiceName((Class) args[0]);
				EF(name,args.length);break;
			case "getTaskId":
				if (args.length==0) return super.getTaskId();
				EF(name,args.length);break;
			case "getTheme":
				if (args.length==0) return super.getTheme();
				EF(name,args.length);break;
			case "getTitleText":
				if (args.length==0) return super.getTitleText();
				EF(name,args.length);break;
			case "getVFragmentManager":
				if (args.length==0) return super.getVFragmentManager();
				EF(name,args.length);break;
			case "getVoiceInteractor":
				if (args.length==0) return super.getVoiceInteractor();
				EF(name,args.length);break;
			case "getWallpaper":
				if (args.length==0) return super.getWallpaper();
				EF(name,args.length);break;
			case "getWallpaperDesiredMinimumHeight":
				if (args.length==0) return super.getWallpaperDesiredMinimumHeight();
				EF(name,args.length);break;
			case "getWallpaperDesiredMinimumWidth":
				if (args.length==0) return super.getWallpaperDesiredMinimumWidth();
				EF(name,args.length);break;
			case "getWindow":
				if (args.length==0) return super.getWindow();
				EF(name,args.length);break;
			case "getWindowManager":
				if (args.length==0) return super.getWindowManager();
				EF(name,args.length);break;
			case "grantUriPermission":
				if (args.length==3&&args[0] instanceof String&&args[1] instanceof Uri&&args[2] instanceof int) {super.grantUriPermission((String) args[0], (Uri) args[1], (int) args[2]);return null;}
				EF(name,args.length);break;
			case "hasWindowFocus":
				if (args.length==0) return super.hasWindowFocus();
				EF(name,args.length);break;
			case "invalidateOptionsMenu":
				if (args.length==0) {super.invalidateOptionsMenu();return null;}
				EF(name,args.length);break;
			case "isChangingConfigurations":
				if (args.length==0) return super.isChangingConfigurations();
				EF(name,args.length);break;
			case "isDestroyed":
				if (args.length==0) return super.isDestroyed();
				EF(name,args.length);break;
			case "isFinishing":
				if (args.length==0) return super.isFinishing();
				EF(name,args.length);break;
			case "isImmersive":
				if (args.length==0) return super.isImmersive();
				EF(name,args.length);break;
			case "isRestricted":
				if (args.length==0) return super.isRestricted();
				EF(name,args.length);break;
			case "isTaskRoot":
				if (args.length==0) return super.isTaskRoot();
				EF(name,args.length);break;
			case "isVoiceInteraction":
				if (args.length==0) return super.isVoiceInteraction();
				EF(name,args.length);break;
			case "isVoiceInteractionRoot":
				if (args.length==0) return super.isVoiceInteractionRoot();
				EF(name,args.length);break;
			case "moveTaskToBack":
				if (args.length==1&&args[0] instanceof boolean) return super.moveTaskToBack((boolean) args[0]);
				EF(name,args.length);break;
			case "navigateUpTo":
				if (args.length==1&&args[0] instanceof Intent) return super.navigateUpTo((Intent) args[0]);
				EF(name,args.length);break;
			case "navigateUpToFromChild":
				if (args.length==2&&args[0] instanceof Activity&&args[1] instanceof Intent) return super.navigateUpToFromChild((Activity) args[0], (Intent) args[1]);
				EF(name,args.length);break;
			case "onSupportActionModeFinished":
				if (args.length==1&&args[0] instanceof ActionMode) {super.onSupportActionModeFinished((ActionMode) args[0]);return null;}
				EF(name,args.length);break;
			case "onSupportActionModeStarted":
				if (args.length==1&&args[0] instanceof ActionMode) {super.onSupportActionModeStarted((ActionMode) args[0]);return null;}
				EF(name,args.length);break;
			case "onActivityReenter":
				if (args.length==2&&args[0] instanceof int&&args[1] instanceof Intent) {super.onActivityReenter((int) args[0], (Intent) args[1]);return null;}
				EF(name,args.length);break;
			case "onActivityResult":
				if (args.length==3&&args[0] instanceof int&&args[1] instanceof int&&args[2] instanceof Intent) {super.onActivityResult((int) args[0], (int) args[1], (Intent) args[2]);return null;}
				EF(name,args.length);break;
			case "onApplyThemeResource":
				if (args.length==3&&args[0] instanceof Resources.Theme&&args[1] instanceof int&&args[2] instanceof boolean) {super.onApplyThemeResource((Resources.Theme) args[0], (int) args[1], (boolean) args[2]);return null;}
				EF(name,args.length);break;
			case "onAttachFragment":
				if (args.length==1&&args[0] instanceof Fragment) {super.onAttachFragment((Fragment) args[0]);return null;}
				if (args.length==1&&args[0] instanceof Fragment) {super.onAttachFragment((Fragment) args[0]);return null;}
				EF(name,args.length);break;
			case "onAttachedToWindow":
				if (args.length==0) {super.onAttachedToWindow();return null;}
				EF(name,args.length);break;
			case "onBackPressed":
				if (args.length==0) {super.onBackPressed();return null;}
				EF(name,args.length);break;
			case "onChildTitleChanged":
				if (args.length==2&&args[0] instanceof Activity&&args[1] instanceof CharSequence) {super.onChildTitleChanged((Activity) args[0], (CharSequence) args[1]);return null;}
				EF(name,args.length);break;
			case "onConfigurationChanged":
				if (args.length==1&&args[0] instanceof Configuration) {super.onConfigurationChanged((Configuration) args[0]);return null;}
				EF(name,args.length);break;
			case "onContentChanged":
				if (args.length==0) {super.onContentChanged();return null;}
				EF(name,args.length);break;
			case "onContextItemSelected":
				if (args.length==1&&args[0] instanceof MenuItem) return super.onContextItemSelected((MenuItem) args[0]);
				EF(name,args.length);break;
			case "onContextMenuClosed":
				if (args.length==1&&args[0] instanceof Menu) {super.onContextMenuClosed((Menu) args[0]);return null;}
				EF(name,args.length);break;
			case "onCreate":
				if (args.length==1&&args[0] instanceof Bundle) {super.onCreate((Bundle) args[0]);return null;}
				EF(name,args.length);break;
			case "onCreateContextMenu":
				if (args.length==3&&args[0] instanceof ContextMenu&&args[1] instanceof View&&args[2] instanceof ContextMenu.ContextMenuInfo) {super.onCreateContextMenu((ContextMenu) args[0], (View) args[1], (ContextMenu.ContextMenuInfo) args[2]);return null;}
				EF(name,args.length);break;
			case "onCreateDescription":
				if (args.length==0) return super.onCreateDescription();
				EF(name,args.length);break;
			case "onCreateDialog":
				if (args.length==1&&args[0] instanceof int) return super.onCreateDialog((int) args[0]);
				if (args.length==2&&args[0] instanceof int&&args[1] instanceof Bundle) return super.onCreateDialog((int) args[0], (Bundle) args[1]);
				EF(name,args.length);break;
			case "onCreateSupportNavigateUpTaskStack":
				if (args.length==1&&args[0] instanceof TaskStackBuilder) {super.onCreateSupportNavigateUpTaskStack((TaskStackBuilder) args[0]);return null;}
				EF(name,args.length);break;
			case "onCreateOptionsMenu":
				if (args.length==1&&args[0] instanceof Menu) return super.onCreateOptionsMenu((Menu) args[0]);
				EF(name,args.length);break;
			case "onCreatePanelMenu":
				if (args.length==2&&args[0] instanceof int&&args[1] instanceof Menu) return super.onCreatePanelMenu((int) args[0], (Menu) args[1]);
				EF(name,args.length);break;
			case "onCreatePanelView":
				if (args.length==1&&args[0] instanceof int) return super.onCreatePanelView((int) args[0]);
				EF(name,args.length);break;
			case "onCreateSupportNavigateUpTaskStack":
				if (args.length==1&&args[0] instanceof TaskStackBuilder) {super.onCreateSupportNavigateUpTaskStack((TaskStackBuilder) args[0]);return null;}
				EF(name,args.length);break;
			case "onCreateThumbnail":
				if (args.length==2&&args[0] instanceof Bitmap&&args[1] instanceof Canvas) return super.onCreateThumbnail((Bitmap) args[0], (Canvas) args[1]);
				EF(name,args.length);break;
			case "onCreateView":
				if (args.length==3&&args[0] instanceof String&&args[1] instanceof Context&&args[2] instanceof AttributeSet) return super.onCreateView((String) args[0], (Context) args[1], (AttributeSet) args[2]);
				if (args.length==4&&args[0] instanceof View&&args[1] instanceof String&&args[2] instanceof Context&&args[3] instanceof AttributeSet) return super.onCreateView((View) args[0], (String) args[1], (Context) args[2], (AttributeSet) args[3]);
				EF(name,args.length);break;
			case "onDestroy":
				if (args.length==0) {super.onDestroy();return null;}
				EF(name,args.length);break;
			case "onDetachedFromWindow":
				if (args.length==0) {super.onDetachedFromWindow();return null;}
				EF(name,args.length);break;
			case "onEnterAnimationComplete":
				if (args.length==0) {super.onEnterAnimationComplete();return null;}
				EF(name,args.length);break;
			case "onGenericMotionEvent":
				if (args.length==1&&args[0] instanceof MotionEvent) return super.onGenericMotionEvent((MotionEvent) args[0]);
				EF(name,args.length);break;
			case "onKeyDown":
				if (args.length==2&&args[0] instanceof int&&args[1] instanceof KeyEvent) return super.onKeyDown((int) args[0], (KeyEvent) args[1]);
				EF(name,args.length);break;
			case "onKeyLongPress":
				if (args.length==2&&args[0] instanceof int&&args[1] instanceof KeyEvent) return super.onKeyLongPress((int) args[0], (KeyEvent) args[1]);
				EF(name,args.length);break;
			case "onKeyMultiple":
				if (args.length==3&&args[0] instanceof int&&args[1] instanceof int&&args[2] instanceof KeyEvent) return super.onKeyMultiple((int) args[0], (int) args[1], (KeyEvent) args[2]);
				EF(name,args.length);break;
			case "onKeyShortcut":
				if (args.length==2&&args[0] instanceof int&&args[1] instanceof KeyEvent) return super.onKeyShortcut((int) args[0], (KeyEvent) args[1]);
				EF(name,args.length);break;
			case "onKeyUp":
				if (args.length==2&&args[0] instanceof int&&args[1] instanceof KeyEvent) return super.onKeyUp((int) args[0], (KeyEvent) args[1]);
				EF(name,args.length);break;
			case "onLowMemory":
				if (args.length==0) {super.onLowMemory();return null;}
				EF(name,args.length);break;
			case "onMenuOpened":
				if (args.length==2&&args[0] instanceof int&&args[1] instanceof Menu) return super.onMenuOpened((int) args[0], (Menu) args[1]);
				EF(name,args.length);break;
			case "onNavigateUp":
				if (args.length==0) return super.onNavigateUp();
				EF(name,args.length);break;
			case "onNavigateUpFromChild":
				if (args.length==1&&args[0] instanceof Activity) return super.onNavigateUpFromChild((Activity) args[0]);
				EF(name,args.length);break;
			case "onNewIntent":
				if (args.length==1&&args[0] instanceof Intent) {super.onNewIntent((Intent) args[0]);return null;}
				EF(name,args.length);break;
			case "onOptionsItemSelected":
				if (args.length==1&&args[0] instanceof MenuItem) return super.onOptionsItemSelected((MenuItem) args[0]);
				EF(name,args.length);break;
			case "onOptionsMenuClosed":
				if (args.length==1&&args[0] instanceof Menu) {super.onOptionsMenuClosed((Menu) args[0]);return null;}
				EF(name,args.length);break;
			case "onPanelClosed":
				if (args.length==2&&args[0] instanceof int&&args[1] instanceof Menu) {super.onPanelClosed((int) args[0], (Menu) args[1]);return null;}
				EF(name,args.length);break;
			case "onPause":
				if (args.length==0) {super.onPause();return null;}
				EF(name,args.length);break;
			case "onPostCreate":
				if (args.length==1&&args[0] instanceof Bundle) {super.onPostCreate((Bundle) args[0]);return null;}
				EF(name,args.length);break;
			case "onPostResume":
				if (args.length==0) {super.onPostResume();return null;}
				EF(name,args.length);break;
			case "onPrepareDialog":
				if (args.length==2&&args[0] instanceof int&&args[1] instanceof Dialog) {super.onPrepareDialog((int) args[0], (Dialog) args[1]);return null;}
				if (args.length==3&&args[0] instanceof int&&args[1] instanceof Dialog&&args[2] instanceof Bundle) {super.onPrepareDialog((int) args[0], (Dialog) args[1], (Bundle) args[2]);return null;}
				EF(name,args.length);break;
			case "onPrepareSupportNavigateUpTaskStack":
				if (args.length==1&&args[0] instanceof TaskStackBuilder) {super.onPrepareSupportNavigateUpTaskStack((TaskStackBuilder) args[0]);return null;}
				EF(name,args.length);break;
			case "onPrepareOptionsMenu":
				if (args.length==1&&args[0] instanceof Menu) return super.onPrepareOptionsMenu((Menu) args[0]);
				EF(name,args.length);break;
			case "onPrepareOptionsPanel":
				if (args.length==2&&args[0] instanceof View&&args[1] instanceof Menu) return super.onPrepareOptionsPanel((View) args[0], (Menu) args[1]);
				EF(name,args.length);break;
			case "onPreparePanel":
				if (args.length==3&&args[0] instanceof int&&args[1] instanceof View&&args[2] instanceof Menu) return super.onPreparePanel((int) args[0], (View) args[1], (Menu) args[2]);
				EF(name,args.length);break;
			case "onPrepareSupportNavigateUpTaskStack":
				if (args.length==1&&args[0] instanceof TaskStackBuilder) {super.onPrepareSupportNavigateUpTaskStack((TaskStackBuilder) args[0]);return null;}
				EF(name,args.length);break;
			case "onProvideAssistData":
				if (args.length==1&&args[0] instanceof Bundle) {super.onProvideAssistData((Bundle) args[0]);return null;}
				EF(name,args.length);break;
			case "onProvideReferrer":
				if (args.length==0) return super.onProvideReferrer();
				EF(name,args.length);break;
			case "onRequestPermissionsResult":
				if (args.length==3&&args[0] instanceof int&&args[1] instanceof String[]&&args[2] instanceof int[]) {super.onRequestPermissionsResult((int) args[0], (String[]) args[1], (int[]) args[2]);return null;}
				EF(name,args.length);break;
			case "onRestart":
				if (args.length==0) {super.onRestart();return null;}
				EF(name,args.length);break;
			case "onRestoreInstanceState":
				if (args.length==1&&args[0] instanceof Bundle) {super.onRestoreInstanceState((Bundle) args[0]);return null;}
				EF(name,args.length);break;
			case "onResume":
				if (args.length==0) {super.onResume();return null;}
				EF(name,args.length);break;
			case "onResumeFragments":
				if (args.length==0) {super.onResumeFragments();return null;}
				EF(name,args.length);break;
			case "onRetainCustomNonConfigurationInstance":
				if (args.length==0) return super.onRetainCustomNonConfigurationInstance();
				EF(name,args.length);break;
			case "onSaveInstanceState":
				if (args.length==1&&args[0] instanceof Bundle) {super.onSaveInstanceState((Bundle) args[0]);return null;}
				EF(name,args.length);break;
			case "onSearchRequested":
				if (args.length==0) return super.onSearchRequested();
				EF(name,args.length);break;
			case "onStart":
				if (args.length==0) {super.onStart();return null;}
				EF(name,args.length);break;
			case "onStateNotSaved":
				if (args.length==0) {super.onStateNotSaved();return null;}
				EF(name,args.length);break;
			case "onStop":
				if (args.length==0) {super.onStop();return null;}
				EF(name,args.length);break;
			case "onSupportActionModeFinished":
				if (args.length==1&&args[0] instanceof ActionMode) {super.onSupportActionModeFinished((ActionMode) args[0]);return null;}
				EF(name,args.length);break;
			case "onSupportActionModeStarted":
				if (args.length==1&&args[0] instanceof ActionMode) {super.onSupportActionModeStarted((ActionMode) args[0]);return null;}
				EF(name,args.length);break;
			case "onSupportContentChanged":
				if (args.length==0) {super.onSupportContentChanged();return null;}
				EF(name,args.length);break;
			case "onSupportNavigateUp":
				if (args.length==0) return super.onSupportNavigateUp();
				EF(name,args.length);break;
			case "onThemeChange":
				if (args.length==1&&args[0] instanceof String) {super.onThemeChange((String) args[0]);return null;}
				EF(name,args.length);break;
			case "onTitleChanged":
				if (args.length==2&&args[0] instanceof CharSequence&&args[1] instanceof int) {super.onTitleChanged((CharSequence) args[0], (int) args[1]);return null;}
				EF(name,args.length);break;
			case "onTouchEvent":
				if (args.length==1&&args[0] instanceof MotionEvent) return super.onTouchEvent((MotionEvent) args[0]);
				EF(name,args.length);break;
			case "onTrackballEvent":
				if (args.length==1&&args[0] instanceof MotionEvent) return super.onTrackballEvent((MotionEvent) args[0]);
				EF(name,args.length);break;
			case "onTrimMemory":
				if (args.length==1&&args[0] instanceof int) {super.onTrimMemory((int) args[0]);return null;}
				EF(name,args.length);break;
			case "onUserInteraction":
				if (args.length==0) {super.onUserInteraction();return null;}
				EF(name,args.length);break;
			case "onUserLeaveHint":
				if (args.length==0) {super.onUserLeaveHint();return null;}
				EF(name,args.length);break;
			case "onVisibleBehindCanceled":
				if (args.length==0) {super.onVisibleBehindCanceled();return null;}
				EF(name,args.length);break;
			case "onWindowAttributesChanged":
				if (args.length==1&&args[0] instanceof WindowManager.LayoutParams) {super.onWindowAttributesChanged((WindowManager.LayoutParams) args[0]);return null;}
				EF(name,args.length);break;
			case "onWindowFocusChanged":
				if (args.length==1&&args[0] instanceof boolean) {super.onWindowFocusChanged((boolean) args[0]);return null;}
				EF(name,args.length);break;
			case "onWindowStartingSupportActionMode":
				if (args.length==1&&args[0] instanceof ActionMode.Callback) return super.onWindowStartingSupportActionMode((ActionMode.Callback) args[0]);
				EF(name,args.length);break;
			case "onWindowStartingSupportActionMode":
				if (args.length==1&&args[0] instanceof ActionMode.Callback) return super.onWindowStartingSupportActionMode((ActionMode.Callback) args[0]);
				EF(name,args.length);break;
			case "openContextMenu":
				if (args.length==1&&args[0] instanceof View) {super.openContextMenu((View) args[0]);return null;}
				EF(name,args.length);break;
			case "openFileInput":
				if (args.length==1&&args[0] instanceof String) return super.openFileInput((String) args[0]);
				EF(name,args.length);break;
			case "openFileOutput":
				if (args.length==2&&args[0] instanceof String&&args[1] instanceof int) return super.openFileOutput((String) args[0], (int) args[1]);
				EF(name,args.length);break;
			case "openOptionsMenu":
				if (args.length==0) {super.openOptionsMenu();return null;}
				EF(name,args.length);break;
			case "openOrCreateDatabase":
				if (args.length==4&&args[0] instanceof String&&args[1] instanceof int&&args[2] instanceof SQLiteDatabase.CursorFactory&&args[3] instanceof DatabaseErrorHandler) return super.openOrCreateDatabase((String) args[0], (int) args[1], (SQLiteDatabase.CursorFactory) args[2], (DatabaseErrorHandler) args[3]);
				if (args.length==3&&args[0] instanceof String&&args[1] instanceof int&&args[2] instanceof SQLiteDatabase.CursorFactory) return super.openOrCreateDatabase((String) args[0], (int) args[1], (SQLiteDatabase.CursorFactory) args[2]);
				EF(name,args.length);break;
			case "overridePendingTransition":
				if (args.length==2&&args[0] instanceof int&&args[1] instanceof int) {super.overridePendingTransition((int) args[0], (int) args[1]);return null;}
				EF(name,args.length);break;
			case "peekWallpaper":
				if (args.length==0) return super.peekWallpaper();
				EF(name,args.length);break;
			case "postponeEnterTransition":
				if (args.length==0) {super.postponeEnterTransition();return null;}
				EF(name,args.length);break;
			case "recreate":
				if (args.length==0) {super.recreate();return null;}
				EF(name,args.length);break;
			case "registerComponentCallbacks":
				if (args.length==1&&args[0] instanceof ComponentCallbacks) {super.registerComponentCallbacks((ComponentCallbacks) args[0]);return null;}
				EF(name,args.length);break;
			case "registerForContextMenu":
				if (args.length==1&&args[0] instanceof View) {super.registerForContextMenu((View) args[0]);return null;}
				EF(name,args.length);break;
			case "registerReceiver":
				if (args.length==2&&args[0] instanceof BroadcastReceiver&&args[1] instanceof IntentFilter) return super.registerReceiver((BroadcastReceiver) args[0], (IntentFilter) args[1]);
				if (args.length==4&&args[0] instanceof BroadcastReceiver&&args[1] instanceof IntentFilter&&args[2] instanceof String&&args[3] instanceof Handler) return super.registerReceiver((BroadcastReceiver) args[0], (IntentFilter) args[1], (String) args[2], (Handler) args[3]);
				EF(name,args.length);break;
			case "releaseInstance":
				if (args.length==0) return super.releaseInstance();
				EF(name,args.length);break;
			case "removeStickyBroadcast":
				if (args.length==1&&args[0] instanceof Intent) {super.removeStickyBroadcast((Intent) args[0]);return null;}
				EF(name,args.length);break;
			case "removeStickyBroadcastAsUser":
				if (args.length==2&&args[0] instanceof Intent&&args[1] instanceof UserHandle) {super.removeStickyBroadcastAsUser((Intent) args[0], (UserHandle) args[1]);return null;}
				EF(name,args.length);break;
			case "reportFullyDrawn":
				if (args.length==0) {super.reportFullyDrawn();return null;}
				EF(name,args.length);break;
			case "requestVisibleBehind":
				if (args.length==1&&args[0] instanceof boolean) return super.requestVisibleBehind((boolean) args[0]);
				EF(name,args.length);break;
			case "revokeUriPermission":
				if (args.length==2&&args[0] instanceof Uri&&args[1] instanceof int) {super.revokeUriPermission((Uri) args[0], (int) args[1]);return null;}
				EF(name,args.length);break;
			case "sendBroadcast":
				if (args.length==1&&args[0] instanceof Intent) {super.sendBroadcast((Intent) args[0]);return null;}
				if (args.length==2&&args[0] instanceof Intent&&args[1] instanceof String) {super.sendBroadcast((Intent) args[0], (String) args[1]);return null;}
				EF(name,args.length);break;
			case "sendBroadcastAsUser":
				if (args.length==2&&args[0] instanceof Intent&&args[1] instanceof UserHandle) {super.sendBroadcastAsUser((Intent) args[0], (UserHandle) args[1]);return null;}
				if (args.length==3&&args[0] instanceof Intent&&args[1] instanceof UserHandle&&args[2] instanceof String) {super.sendBroadcastAsUser((Intent) args[0], (UserHandle) args[1], (String) args[2]);return null;}
				EF(name,args.length);break;
			case "sendOrderedBroadcast":
				if (args.length==2&&args[0] instanceof Intent&&args[1] instanceof String) {super.sendOrderedBroadcast((Intent) args[0], (String) args[1]);return null;}
				if (args.length==7&&args[0] instanceof Intent&&args[1] instanceof String&&args[2] instanceof BroadcastReceiver&&args[3] instanceof Handler&&args[4] instanceof int&&args[5] instanceof String&&args[6] instanceof Bundle) {super.sendOrderedBroadcast((Intent) args[0], (String) args[1], (BroadcastReceiver) args[2], (Handler) args[3], (int) args[4], (String) args[5], (Bundle) args[6]);return null;}
				EF(name,args.length);break;
			case "sendOrderedBroadcastAsUser":
				if (args.length==8&&args[0] instanceof Intent&&args[1] instanceof UserHandle&&args[2] instanceof String&&args[3] instanceof BroadcastReceiver&&args[4] instanceof Handler&&args[5] instanceof int&&args[6] instanceof String&&args[7] instanceof Bundle) {super.sendOrderedBroadcastAsUser((Intent) args[0], (UserHandle) args[1], (String) args[2], (BroadcastReceiver) args[3], (Handler) args[4], (int) args[5], (String) args[6], (Bundle) args[7]);return null;}
				EF(name,args.length);break;
			case "sendStickyBroadcast":
				if (args.length==1&&args[0] instanceof Intent) {super.sendStickyBroadcast((Intent) args[0]);return null;}
				EF(name,args.length);break;
			case "sendStickyBroadcastAsUser":
				if (args.length==2&&args[0] instanceof Intent&&args[1] instanceof UserHandle) {super.sendStickyBroadcastAsUser((Intent) args[0], (UserHandle) args[1]);return null;}
				EF(name,args.length);break;
			case "sendStickyOrderedBroadcast":
				if (args.length==6&&args[0] instanceof Intent&&args[1] instanceof BroadcastReceiver&&args[2] instanceof Handler&&args[3] instanceof int&&args[4] instanceof String&&args[5] instanceof Bundle) {super.sendStickyOrderedBroadcast((Intent) args[0], (BroadcastReceiver) args[1], (Handler) args[2], (int) args[3], (String) args[4], (Bundle) args[5]);return null;}
				EF(name,args.length);break;
			case "sendStickyOrderedBroadcastAsUser":
				if (args.length==7&&args[0] instanceof Intent&&args[1] instanceof UserHandle&&args[2] instanceof BroadcastReceiver&&args[3] instanceof Handler&&args[4] instanceof int&&args[5] instanceof String&&args[6] instanceof Bundle) {super.sendStickyOrderedBroadcastAsUser((Intent) args[0], (UserHandle) args[1], (BroadcastReceiver) args[2], (Handler) args[3], (int) args[4], (String) args[5], (Bundle) args[6]);return null;}
				EF(name,args.length);break;
			case "setBackButtonColor":
				if (args.length==1&&args[0] instanceof int) {super.setBackButtonColor((int) args[0]);return null;}
				EF(name,args.length);break;
			case "setContentView":
				if (args.length==2&&args[0] instanceof View&&args[1] instanceof ViewGroup.LayoutParams) {super.setContentView((View) args[0], (ViewGroup.LayoutParams) args[1]);return null;}
				if (args.length==1&&args[0] instanceof View) {super.setContentView((View) args[0]);return null;}
				if (args.length==1&&args[0] instanceof int) {super.setContentView((int) args[0]);return null;}
				EF(name,args.length);break;
			case "setEnterSharedElementCallback":
				if (args.length==1&&args[0] instanceof SharedElementCallback) {super.setEnterSharedElementCallback((SharedElementCallback) args[0]);return null;}
				if (args.length==1&&args[0] instanceof SharedElementCallback) {super.setEnterSharedElementCallback((SharedElementCallback) args[0]);return null;}
				EF(name,args.length);break;
			case "setExitSharedElementCallback":
				if (args.length==1&&args[0] instanceof SharedElementCallback) {super.setExitSharedElementCallback((SharedElementCallback) args[0]);return null;}
				if (args.length==1&&args[0] instanceof SharedElementCallback) {super.setExitSharedElementCallback((SharedElementCallback) args[0]);return null;}
				EF(name,args.length);break;
			case "setFinishOnTouchOutside":
				if (args.length==1&&args[0] instanceof boolean) {super.setFinishOnTouchOutside((boolean) args[0]);return null;}
				EF(name,args.length);break;
			case "setImmersive":
				if (args.length==1&&args[0] instanceof boolean) {super.setImmersive((boolean) args[0]);return null;}
				EF(name,args.length);break;
			case "setIntent":
				if (args.length==1&&args[0] instanceof Intent) {super.setIntent((Intent) args[0]);return null;}
				EF(name,args.length);break;
			case "setLogTag":
				if (args.length==1&&args[0] instanceof String) {super.setLogTag((String) args[0]);return null;}
				EF(name,args.length);break;
			case "setRequestedOrientation":
				if (args.length==1&&args[0] instanceof int) {super.setRequestedOrientation((int) args[0]);return null;}
				EF(name,args.length);break;
			case "setStatusBarColor":
				if (args.length==1&&args[0] instanceof int) {super.setStatusBarColor((int) args[0]);return null;}
				EF(name,args.length);break;
			case "setSubTitle":
				if (args.length==1&&args[0] instanceof CharSequence) {super.setSubTitle((CharSequence) args[0]);return null;}
				EF(name,args.length);break;
			case "setSubTitleTextColor":
				if (args.length==1&&args[0] instanceof int) {super.setSubTitleTextColor((int) args[0]);return null;}
				EF(name,args.length);break;
			case "setSupportActionBar":
				if (args.length==1&&args[0] instanceof Toolbar) {super.setSupportActionBar((Toolbar) args[0]);return null;}
				EF(name,args.length);break;
			case "setSupportProgress":
				if (args.length==1&&args[0] instanceof int) {super.setSupportProgress((int) args[0]);return null;}
				EF(name,args.length);break;
			case "setSupportProgressBarIndeterminate":
				if (args.length==1&&args[0] instanceof boolean) {super.setSupportProgressBarIndeterminate((boolean) args[0]);return null;}
				EF(name,args.length);break;
			case "setSupportProgressBarIndeterminateVisibility":
				if (args.length==1&&args[0] instanceof boolean) {super.setSupportProgressBarIndeterminateVisibility((boolean) args[0]);return null;}
				EF(name,args.length);break;
			case "setSupportProgressBarVisibility":
				if (args.length==1&&args[0] instanceof boolean) {super.setSupportProgressBarVisibility((boolean) args[0]);return null;}
				EF(name,args.length);break;
			case "setTheme":
				if (args.length==1&&args[0] instanceof int) {super.setTheme((int) args[0]);return null;}
				EF(name,args.length);break;
			case "setTitle":
				if (args.length==1&&args[0] instanceof CharSequence) {super.setTitle((CharSequence) args[0]);return null;}
				if (args.length==1&&args[0] instanceof int) {super.setTitle((int) args[0]);return null;}
				EF(name,args.length);break;
			case "setTitleBackground":
				if (args.length==1&&args[0] instanceof int) {super.setTitleBackground((int) args[0]);return null;}
				EF(name,args.length);break;
			case "setTitleColor":
				if (args.length==1&&args[0] instanceof int) {super.setTitleColor((int) args[0]);return null;}
				EF(name,args.length);break;
			case "setTitleElevation":
				if (args.length==1&&args[0] instanceof float) {super.setTitleElevation((float) args[0]);return null;}
				EF(name,args.length);break;
			case "setTitleText":
				if (args.length==1&&args[0] instanceof CharSequence) {super.setTitleText((CharSequence) args[0]);return null;}
				EF(name,args.length);break;
			case "setTitleTextColor":
				if (args.length==1&&args[0] instanceof int) {super.setTitleTextColor((int) args[0]);return null;}
				EF(name,args.length);break;
			case "setVisible":
				if (args.length==1&&args[0] instanceof boolean) {super.setVisible((boolean) args[0]);return null;}
				EF(name,args.length);break;
			case "setWallpaper":
				if (args.length==1&&args[0] instanceof Bitmap) {super.setWallpaper((Bitmap) args[0]);return null;}
				if (args.length==1&&args[0] instanceof InputStream) {super.setWallpaper((InputStream) args[0]);return null;}
				EF(name,args.length);break;
			case "shouldShowRequestPermissionRationale":
				if (args.length==1&&args[0] instanceof String) return super.shouldShowRequestPermissionRationale((String) args[0]);
				EF(name,args.length);break;
			case "shouldUpRecreateTask":
				if (args.length==1&&args[0] instanceof Intent) return super.shouldUpRecreateTask((Intent) args[0]);
				EF(name,args.length);break;
			case "showAssist":
				if (args.length==1&&args[0] instanceof Bundle) return super.showAssist((Bundle) args[0]);
				EF(name,args.length);break;
			case "showLockTaskEscapeMessage":
				if (args.length==0) {super.showLockTaskEscapeMessage();return null;}
				EF(name,args.length);break;
			case "startActionMode":
				if (args.length==1&&args[0] instanceof ActionMode.Callback) return super.startSupportActionMode((ActionMode.Callback) args[0]);
				EF(name,args.length);break;
			case "startActivities":
				if (args.length==2&&args[0] instanceof Intent[]&&args[1] instanceof Bundle) {super.startActivities((Intent[]) args[0], (Bundle) args[1]);return null;}
				if (args.length==1&&args[0] instanceof Intent[]) {super.startActivities((Intent[]) args[0]);return null;}
				EF(name,args.length);break;
			case "startActivity":
				if (args.length==1&&args[0] instanceof Intent) {super.startActivity((Intent) args[0]);return null;}
				if (args.length==2&&args[0] instanceof Intent&&args[1] instanceof Bundle) {super.startActivity((Intent) args[0], (Bundle) args[1]);return null;}
				EF(name,args.length);break;
			case "startActivityForResult":
				if (args.length==2&&args[0] instanceof Intent&&args[1] instanceof int) {super.startActivityForResult((Intent) args[0], (int) args[1]);return null;}
				if (args.length==3&&args[0] instanceof Intent&&args[1] instanceof int&&args[2] instanceof Bundle) {super.startActivityForResult((Intent) args[0], (int) args[1], (Bundle) args[2]);return null;}
				EF(name,args.length);break;
			case "startActivityFromChild":
				if (args.length==3&&args[0] instanceof Activity&&args[1] instanceof Intent&&args[2] instanceof int) {super.startActivityFromChild((Activity) args[0], (Intent) args[1], (int) args[2]);return null;}
				if (args.length==4&&args[0] instanceof Activity&&args[1] instanceof Intent&&args[2] instanceof int&&args[3] instanceof Bundle) {super.startActivityFromChild((Activity) args[0], (Intent) args[1], (int) args[2], (Bundle) args[3]);return null;}
				EF(name,args.length);break;
			case "startActivityFromFragment":
				if (args.length==3&&args[0] instanceof Fragment&&args[1] instanceof Intent&&args[2] instanceof int) {super.startActivityFromFragment((Fragment) args[0], (Intent) args[1], (int) args[2]);return null;}
				if (args.length==3&&args[0] instanceof Fragment&&args[1] instanceof Intent&&args[2] instanceof int) {super.startActivityFromFragment((Fragment) args[0], (Intent) args[1], (int) args[2]);return null;}
				EF(name,args.length);break;
			case "startActivityIfNeeded":
				if (args.length==2&&args[0] instanceof Intent&&args[1] instanceof int) return super.startActivityIfNeeded((Intent) args[0], (int) args[1]);
				if (args.length==3&&args[0] instanceof Intent&&args[1] instanceof int&&args[2] instanceof Bundle) return super.startActivityIfNeeded((Intent) args[0], (int) args[1], (Bundle) args[2]);
				EF(name,args.length);break;
			case "startInstrumentation":
				if (args.length==3&&args[0] instanceof ComponentName&&args[1] instanceof String&&args[2] instanceof Bundle) return super.startInstrumentation((ComponentName) args[0], (String) args[1], (Bundle) args[2]);
				EF(name,args.length);break;
			case "startIntentSender":
				if (args.length==6&&args[0] instanceof IntentSender&&args[1] instanceof Intent&&args[2] instanceof int&&args[3] instanceof int&&args[4] instanceof int&&args[5] instanceof Bundle) {super.startIntentSender((IntentSender) args[0], (Intent) args[1], (int) args[2], (int) args[3], (int) args[4], (Bundle) args[5]);return null;}
				if (args.length==5&&args[0] instanceof IntentSender&&args[1] instanceof Intent&&args[2] instanceof int&&args[3] instanceof int&&args[4] instanceof int) {super.startIntentSender((IntentSender) args[0], (Intent) args[1], (int) args[2], (int) args[3], (int) args[4]);return null;}
				EF(name,args.length);break;
			case "startIntentSenderForResult":
				if (args.length==7&&args[0] instanceof IntentSender&&args[1] instanceof int&&args[2] instanceof Intent&&args[3] instanceof int&&args[4] instanceof int&&args[5] instanceof int&&args[6] instanceof Bundle) {super.startIntentSenderForResult((IntentSender) args[0], (int) args[1], (Intent) args[2], (int) args[3], (int) args[4], (int) args[5], (Bundle) args[6]);return null;}
				if (args.length==6&&args[0] instanceof IntentSender&&args[1] instanceof int&&args[2] instanceof Intent&&args[3] instanceof int&&args[4] instanceof int&&args[5] instanceof int) {super.startIntentSenderForResult((IntentSender) args[0], (int) args[1], (Intent) args[2], (int) args[3], (int) args[4], (int) args[5]);return null;}
				EF(name,args.length);break;
			case "startIntentSenderFromChild":
				if (args.length==7&&args[0] instanceof Activity&&args[1] instanceof IntentSender&&args[2] instanceof int&&args[3] instanceof Intent&&args[4] instanceof int&&args[5] instanceof int&&args[6] instanceof int) {super.startIntentSenderFromChild((Activity) args[0], (IntentSender) args[1], (int) args[2], (Intent) args[3], (int) args[4], (int) args[5], (int) args[6]);return null;}
				if (args.length==8&&args[0] instanceof Activity&&args[1] instanceof IntentSender&&args[2] instanceof int&&args[3] instanceof Intent&&args[4] instanceof int&&args[5] instanceof int&&args[6] instanceof int&&args[7] instanceof Bundle) {super.startIntentSenderFromChild((Activity) args[0], (IntentSender) args[1], (int) args[2], (Intent) args[3], (int) args[4], (int) args[5], (int) args[6], (Bundle) args[7]);return null;}
				EF(name,args.length);break;
			case "startLockTask":
				if (args.length==0) {super.startLockTask();return null;}
				EF(name,args.length);break;
			case "startManagingCursor":
				if (args.length==1&&args[0] instanceof Cursor) {super.startManagingCursor((Cursor) args[0]);return null;}
				EF(name,args.length);break;
			case "startNextMatchingActivity":
				if (args.length==1&&args[0] instanceof Intent) return super.startNextMatchingActivity((Intent) args[0]);
				if (args.length==2&&args[0] instanceof Intent&&args[1] instanceof Bundle) return super.startNextMatchingActivity((Intent) args[0], (Bundle) args[1]);
				EF(name,args.length);break;
			case "startPostponedEnterTransition":
				if (args.length==0) {super.startPostponedEnterTransition();return null;}
				EF(name,args.length);break;
			case "startSearch":
				if (args.length==4&&args[0] instanceof String&&args[1] instanceof boolean&&args[2] instanceof Bundle&&args[3] instanceof boolean) {super.startSearch((String) args[0], (boolean) args[1], (Bundle) args[2], (boolean) args[3]);return null;}
				EF(name,args.length);break;
			case "startService":
				if (args.length==1&&args[0] instanceof Intent) return super.startService((Intent) args[0]);
				EF(name,args.length);break;
			case "startSupportActionMode":
				if (args.length==1&&args[0] instanceof ActionMode.Callback) return super.startSupportActionMode((ActionMode.Callback) args[0]);
				EF(name,args.length);break;
			case "stopLockTask":
				if (args.length==0) {super.stopLockTask();return null;}
				EF(name,args.length);break;
			case "stopManagingCursor":
				if (args.length==1&&args[0] instanceof Cursor) {super.stopManagingCursor((Cursor) args[0]);return null;}
				EF(name,args.length);break;
			case "stopService":
				if (args.length==1&&args[0] instanceof Intent) return super.stopService((Intent) args[0]);
				EF(name,args.length);break;
			case "supportFinishAfterTransition":
				if (args.length==0) {super.supportFinishAfterTransition();return null;}
				EF(name,args.length);break;
			case "supportInvalidateOptionsMenu":
				if (args.length==0) {super.supportInvalidateOptionsMenu();return null;}
				EF(name,args.length);break;
			case "supportNavigateUpTo":
				if (args.length==1&&args[0] instanceof Intent) {super.supportNavigateUpTo((Intent) args[0]);return null;}
				EF(name,args.length);break;
			case "supportPostponeEnterTransition":
				if (args.length==0) {super.supportPostponeEnterTransition();return null;}
				EF(name,args.length);break;
			case "supportRequestWindowFeature":
				if (args.length==1&&args[0] instanceof int) return super.supportRequestWindowFeature((int) args[0]);
				EF(name,args.length);break;
			case "supportShouldUpRecreateTask":
				if (args.length==1&&args[0] instanceof Intent) return super.supportShouldUpRecreateTask((Intent) args[0]);
				EF(name,args.length);break;
			case "supportStartPostponedEnterTransition":
				if (args.length==0) {super.supportStartPostponedEnterTransition();return null;}
				EF(name,args.length);break;
			case "takeKeyEvents":
				if (args.length==1&&args[0] instanceof boolean) {super.takeKeyEvents((boolean) args[0]);return null;}
				EF(name,args.length);break;
			case "triggerSearch":
				if (args.length==2&&args[0] instanceof String&&args[1] instanceof Bundle) {super.triggerSearch((String) args[0], (Bundle) args[1]);return null;}
				EF(name,args.length);break;
			case "unbindService":
				if (args.length==1&&args[0] instanceof ServiceConnection) {super.unbindService((ServiceConnection) args[0]);return null;}
				EF(name,args.length);break;
			case "unregisterComponentCallbacks":
				if (args.length==1&&args[0] instanceof ComponentCallbacks) {super.unregisterComponentCallbacks((ComponentCallbacks) args[0]);return null;}
				EF(name,args.length);break;
			case "unregisterForContextMenu":
				if (args.length==1&&args[0] instanceof View) {super.unregisterForContextMenu((View) args[0]);return null;}
				EF(name,args.length);break;
			case "unregisterReceiver":
				if (args.length==1&&args[0] instanceof BroadcastReceiver) {super.unregisterReceiver((BroadcastReceiver) args[0]);return null;}
				EF(name,args.length);break;
		}
		return null;
	}
	@Override
	public void addContentView(View p0, ViewGroup.LayoutParams p1) {
		if (!callFunction(toArray(p0, p1))) super.addContentView(p0, p1);
	}
	@Override
	public void applyOverrideConfiguration(Configuration p0) {
		if (!callFunction(toArray(p0))) super.applyOverrideConfiguration(p0);
	}
	@Override
	protected void attachBaseContext(Context p0) {
		if (!callFunction(toArray(p0))) super.attachBaseContext(p0);
	}
	@Override
	public boolean bindService(Intent p0, ServiceConnection p1, int p2) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1, p2))) return super.bindService(p0, p1, p2); else return (boolean) ref.get();
	}
	@Override
	public int checkCallingOrSelfPermission(String p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.checkCallingOrSelfPermission(p0); else return (int) ref.get();
	}
	@Override
	public int checkCallingOrSelfUriPermission(Uri p0, int p1) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1))) return super.checkCallingOrSelfUriPermission(p0, p1); else return (int) ref.get();
	}
	@Override
	public int checkCallingPermission(String p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.checkCallingPermission(p0); else return (int) ref.get();
	}
	@Override
	public int checkCallingUriPermission(Uri p0, int p1) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1))) return super.checkCallingUriPermission(p0, p1); else return (int) ref.get();
	}
	@Override
	public int checkPermission(String p0, int p1, int p2) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1, p2))) return super.checkPermission(p0, p1, p2); else return (int) ref.get();
	}
	@Override
	public int checkSelfPermission(String p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.checkSelfPermission(p0); else return (int) ref.get();
	}
	@Override
	public int checkUriPermission(Uri p0, int p1, int p2, int p3) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1, p2, p3))) return super.checkUriPermission(p0, p1, p2, p3); else return (int) ref.get();
	}
	@Override
	public int checkUriPermission(Uri p0, String p1, String p2, int p3, int p4, int p5) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1, p2, p3, p4, p5))) return super.checkUriPermission(p0, p1, p2, p3, p4, p5); else return (int) ref.get();
	}
	@Override
	public void clearWallpaper() throws IOException {
		if (!callFunction()) super.clearWallpaper();
	}
	@Override
	public void closeContextMenu() {
		if (!callFunction()) super.closeContextMenu();
	}
	@Override
	public void closeOptionsMenu() {
		if (!callFunction()) super.closeOptionsMenu();
	}
	@Override
	public Context createConfigurationContext(Configuration p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.createConfigurationContext(p0); else return (Context) ref.get();
	}
	@Override
	public Context createDisplayContext(Display p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.createDisplayContext(p0); else return (Context) ref.get();
	}
	@Override
	public Context createPackageContext(String p0, int p1) throws PackageManager.NameNotFoundException {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1))) return super.createPackageContext(p0, p1); else return (Context) ref.get();
	}
	@Override
	public PendingIntent createPendingResult(int p0, Intent p1, int p2) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1, p2))) return super.createPendingResult(p0, p1, p2); else return (PendingIntent) ref.get();
	}
	@Override
	public String[] databaseList() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.databaseList(); else return (String[]) ref.get();
	}
	@Override
	public boolean deleteDatabase(String p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.deleteDatabase(p0); else return (boolean) ref.get();
	}
	@Override
	public boolean deleteFile(String p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.deleteFile(p0); else return (boolean) ref.get();
	}
	@Override
	public void disableBackButton() {
		if (!callFunction()) super.disableBackButton();
	}
	@Override
	public boolean dispatchGenericMotionEvent(MotionEvent p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.dispatchGenericMotionEvent(p0); else return (boolean) ref.get();
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.dispatchKeyEvent(p0); else return (boolean) ref.get();
	}
	@Override
	public boolean dispatchKeyShortcutEvent(KeyEvent p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.dispatchKeyShortcutEvent(p0); else return (boolean) ref.get();
	}
	@Override
	public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.dispatchPopulateAccessibilityEvent(p0); else return (boolean) ref.get();
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.dispatchTouchEvent(p0); else return (boolean) ref.get();
	}
	@Override
	public boolean dispatchTrackballEvent(MotionEvent p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.dispatchTrackballEvent(p0); else return (boolean) ref.get();
	}
	@Override
	public void dump(String p0, FileDescriptor p1, PrintWriter p2, String[] p3) {
		if (!callFunction(toArray(p0, p1, p2, p3))) super.dump(p0, p1, p2, p3);
	}
	@Override
	public void enableBackButton() {
		if (!callFunction()) super.enableBackButton();
	}
	@Override
	public void enforceCallingOrSelfPermission(String p0, String p1) {
		if (!callFunction(toArray(p0, p1))) super.enforceCallingOrSelfPermission(p0, p1);
	}
	@Override
	public void enforceCallingOrSelfUriPermission(Uri p0, int p1, String p2) {
		if (!callFunction(toArray(p0, p1, p2))) super.enforceCallingOrSelfUriPermission(p0, p1, p2);
	}
	@Override
	public void enforceCallingPermission(String p0, String p1) {
		if (!callFunction(toArray(p0, p1))) super.enforceCallingPermission(p0, p1);
	}
	@Override
	public void enforceCallingUriPermission(Uri p0, int p1, String p2) {
		if (!callFunction(toArray(p0, p1, p2))) super.enforceCallingUriPermission(p0, p1, p2);
	}
	@Override
	public void enforcePermission(String p0, int p1, int p2, String p3) {
		if (!callFunction(toArray(p0, p1, p2, p3))) super.enforcePermission(p0, p1, p2, p3);
	}
	@Override
	public void enforceUriPermission(Uri p0, int p1, int p2, int p3, String p4) {
		if (!callFunction(toArray(p0, p1, p2, p3, p4))) super.enforceUriPermission(p0, p1, p2, p3, p4);
	}
	@Override
	public void enforceUriPermission(Uri p0, String p1, String p2, int p3, int p4, int p5, String p6) {
		if (!callFunction(toArray(p0, p1, p2, p3, p4, p5, p6))) super.enforceUriPermission(p0, p1, p2, p3, p4, p5, p6);
	}
	@Override
	public void err(Throwable p0) {
		if (!callFunction(toArray(p0))) super.err(p0);
	}
	@Override
	public void err(Throwable p0, boolean p1) {
		if (!callFunction(toArray(p0, p1))) super.err(p0, p1);
	}
	@Override
	public String[] fileList() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.fileList(); else return (String[]) ref.get();
	}
	@Override
	public View findViewById(int p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.findViewById(p0); else return (View) ref.get();
	}
	@Override
	public void finish() {
		if (!callFunction()) super.finish();
	}
	@Override
	public void finishActivity(int p0) {
		if (!callFunction(toArray(p0))) super.finishActivity(p0);
	}
	@Override
	public void finishActivityFromChild(Activity p0, int p1) {
		if (!callFunction(toArray(p0, p1))) super.finishActivityFromChild(p0, p1);
	}
	@Override
	public void finishAffinity() {
		if (!callFunction()) super.finishAffinity();
	}
	@Override
	public void finishAfterTransition() {
		if (!callFunction()) super.finishAfterTransition();
	}
	@Override
	public void finishAndRemoveTask() {
		if (!callFunction()) super.finishAndRemoveTask();
	}
	@Override
	public void finishFromChild(Activity p0) {
		if (!callFunction(toArray(p0))) super.finishFromChild(p0);
	}
	@Override
	public Context getApplicationContext() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getApplicationContext(); else return (Context) ref.get();
	}
	@Override
	public ApplicationInfo getApplicationInfo() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getApplicationInfo(); else return (ApplicationInfo) ref.get();
	}
	@Override
	public Context getBaseContext() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getBaseContext(); else return (Context) ref.get();
	}
	@Override
	public File getCacheDir() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getCacheDir(); else return (File) ref.get();
	}
	@Override
	public ComponentName getCallingActivity() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getCallingActivity(); else return (ComponentName) ref.get();
	}
	@Override
	public String getCallingPackage() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getCallingPackage(); else return (String) ref.get();
	}
	@Override
	public int getChangingConfigurations() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getChangingConfigurations(); else return (int) ref.get();
	}
	@Override
	public ClassLoader getClassLoader() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getClassLoader(); else return (ClassLoader) ref.get();
	}
	@Override
	public File getCodeCacheDir() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getCodeCacheDir(); else return (File) ref.get();
	}
	@Override
	public ComponentName getComponentName() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getComponentName(); else return (ComponentName) ref.get();
	}
	@Override
	public ContentResolver getContentResolver() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getContentResolver(); else return (ContentResolver) ref.get();
	}
	@Override
	public View getCurrentFocus() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getCurrentFocus(); else return (View) ref.get();
	}
	@Override
	public File getDatabasePath(String p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.getDatabasePath(p0); else return (File) ref.get();
	}
	@Override
	public AppCompatDelegate getDelegate() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getDelegate(); else return (AppCompatDelegate) ref.get();
	}
	@Override
	public File getDir(String p0, int p1) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1))) return super.getDir(p0, p1); else return (File) ref.get();
	}
	@Override
	public ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getDrawerToggleDelegate(); else return (ActionBarDrawerToggle.Delegate) ref.get();
	}
	@Override
	public File getExternalCacheDir() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getExternalCacheDir(); else return (File) ref.get();
	}
	@Override
	public File[] getExternalCacheDirs() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getExternalCacheDirs(); else return (File[]) ref.get();
	}
	@Override
	public File getExternalFilesDir(String p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.getExternalFilesDir(p0); else return (File) ref.get();
	}
	@Override
	public File[] getExternalFilesDirs(String p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.getExternalFilesDirs(p0); else return (File[]) ref.get();
	}
	@Override
	public File[] getExternalMediaDirs() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getExternalMediaDirs(); else return (File[]) ref.get();
	}
	@Override
	public File getFileStreamPath(String p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.getFileStreamPath(p0); else return (File) ref.get();
	}
	@Override
	public File getFilesDir() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getFilesDir(); else return (File) ref.get();
	}
	@Override
	public Intent getIntent() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getIntent(); else return (Intent) ref.get();
	}
	@Override
	public Object getLastCustomNonConfigurationInstance() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getLastCustomNonConfigurationInstance(); else return (Object) ref.get();
	}
	@Override
	public Object getLastNonConfigurationInstance() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getLastNonConfigurationInstance(); else return (Object) ref.get();
	}
	@Override
	public LayoutInflater getLayoutInflater() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getLayoutInflater(); else return (LayoutInflater) ref.get();
	}
	@Override
	public String getLocalClassName() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getLocalClassName(); else return (String) ref.get();
	}
	@Override
	public String getLogTag() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getLogTag(); else return (String) ref.get();
	}
	@Override
	public Looper getMainLooper() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getMainLooper(); else return (Looper) ref.get();
	}
	@Override
	public MenuInflater getMenuInflater() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getMenuInflater(); else return (MenuInflater) ref.get();
	}
	@Override
	public File getNoBackupFilesDir() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getNoBackupFilesDir(); else return (File) ref.get();
	}
	@Override
	public File getObbDir() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getObbDir(); else return (File) ref.get();
	}
	@Override
	public File[] getObbDirs() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getObbDirs(); else return (File[]) ref.get();
	}
	@Override
	public String getPackageCodePath() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getPackageCodePath(); else return (String) ref.get();
	}
	@Override
	public PackageManager getPackageManager() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getPackageManager(); else return (PackageManager) ref.get();
	}
	@Override
	public String getPackageName() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getPackageName(); else return (String) ref.get();
	}
	@Override
	public String getPackageResourcePath() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getPackageResourcePath(); else return (String) ref.get();
	}
	@Override
	public Intent getParentActivityIntent() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getParentActivityIntent(); else return (Intent) ref.get();
	}
	@Override
	public SharedPreferences getPreferences(int p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.getPreferences(p0); else return (SharedPreferences) ref.get();
	}
	@Override
	public Uri getReferrer() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getReferrer(); else return (Uri) ref.get();
	}
	@Override
	public int getRequestedOrientation() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getRequestedOrientation(); else return (int) ref.get();
	}
	@Override
	public SharedPreferences getSharedPreferences(String p0, int p1) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1))) return super.getSharedPreferences(p0, p1); else return (SharedPreferences) ref.get();
	}
	@Override
	public int getStatusBarHeight() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getStatusBarHeight(); else return (int) ref.get();
	}
	@Override
	public ActionBar getSupportActionBar() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getSupportActionBar(); else return (ActionBar) ref.get();
	}
	@Override
	public FragmentManager getSupportFragmentManager() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getSupportFragmentManager(); else return (FragmentManager) ref.get();
	}
	@Override
	public LoaderManager getSupportLoaderManager() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getSupportLoaderManager(); else return (LoaderManager) ref.get();
	}
	@Override
	public Intent getSupportParentActivityIntent() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getSupportParentActivityIntent(); else return (Intent) ref.get();
	}
	@Override
	public Object getSystemService(String p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.getSystemService(p0); else return (Object) ref.get();
	}
	@Override
	public String getSystemServiceName(Class p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.getSystemServiceName(p0); else return (String) ref.get();
	}
	@Override
	public int getTaskId() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getTaskId(); else return (int) ref.get();
	}
	@Override
	public Resources.Theme getTheme() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getTheme(); else return (Resources.Theme) ref.get();
	}

	@Override
	public CharSequence getTitleText() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getTitleText(); else return (CharSequence) ref.get();
	}
	@Override
	public VFragmentManager getVFragmentManager() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getVFragmentManager(); else return (VFragmentManager) ref.get();
	}
	@Override
	public Drawable getWallpaper() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getWallpaper(); else return (Drawable) ref.get();
	}
	@Override
	public int getWallpaperDesiredMinimumHeight() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getWallpaperDesiredMinimumHeight(); else return (int) ref.get();
	}
	@Override
	public int getWallpaperDesiredMinimumWidth() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getWallpaperDesiredMinimumWidth(); else return (int) ref.get();
	}
	@Override
	public Window getWindow() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getWindow(); else return (Window) ref.get();
	}
	@Override
	public WindowManager getWindowManager() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.getWindowManager(); else return (WindowManager) ref.get();
	}
	@Override
	public void grantUriPermission(String p0, Uri p1, int p2) {
		if (!callFunction(toArray(p0, p1, p2))) super.grantUriPermission(p0, p1, p2);
	}
	@Override
	public boolean hasWindowFocus() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.hasWindowFocus(); else return (boolean) ref.get();
	}
	@Override
	public void invalidateOptionsMenu() {
		if (!callFunction()) super.invalidateOptionsMenu();
	}
	@Override
	public boolean isChangingConfigurations() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.isChangingConfigurations(); else return (boolean) ref.get();
	}
	@Override
	public boolean isDestroyed() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.isDestroyed(); else return (boolean) ref.get();
	}
	@Override
	public boolean isFinishing() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.isFinishing(); else return (boolean) ref.get();
	}
	@Override
	public boolean isImmersive() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.isImmersive(); else return (boolean) ref.get();
	}
	@Override
	public boolean isRestricted() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.isRestricted(); else return (boolean) ref.get();
	}
	@Override
	public boolean isTaskRoot() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.isTaskRoot(); else return (boolean) ref.get();
	}
	@Override
	public boolean isVoiceInteraction() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.isVoiceInteraction(); else return (boolean) ref.get();
	}
	@Override
	public boolean isVoiceInteractionRoot() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.isVoiceInteractionRoot(); else return (boolean) ref.get();
	}
	@Override
	public boolean moveTaskToBack(boolean p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.moveTaskToBack(p0); else return (boolean) ref.get();
	}
	@Override
	public boolean navigateUpTo(Intent p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.navigateUpTo(p0); else return (boolean) ref.get();
	}
	@Override
	public boolean navigateUpToFromChild(Activity p0, Intent p1) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1))) return super.navigateUpToFromChild(p0, p1); else return (boolean) ref.get();
	}
	@Override
	public void onActivityReenter(int p0, Intent p1) {
		if (!callFunction(toArray(p0, p1))) super.onActivityReenter(p0, p1);
	}
	@Override
	protected void onActivityResult(int p0, int p1, Intent p2) {
		if (!callFunction(toArray(p0, p1, p2))) super.onActivityResult(p0, p1, p2);
	}
	@Override
	protected void onApplyThemeResource(Resources.Theme p0, int p1, boolean p2) {
		if (!callFunction(toArray(p0, p1, p2))) super.onApplyThemeResource(p0, p1, p2);
	}
	@Override
	public void onAttachedToWindow() {
		if (!callFunction()) super.onAttachedToWindow();
	}
	@Override
	public void onBackPressed() {
		if (!callFunction()) super.onBackPressed();
	}
	@Override
	protected void onChildTitleChanged(Activity p0, CharSequence p1) {
		if (!callFunction(toArray(p0, p1))) super.onChildTitleChanged(p0, p1);
	}
	@Override
	public void onConfigurationChanged(Configuration p0) {
		if (!callFunction(toArray(p0))) super.onConfigurationChanged(p0);
	}
	@Override
	public void onContentChanged() {
		if (!callFunction()) super.onContentChanged();
	}
	@Override
	public boolean onContextItemSelected(MenuItem p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.onContextItemSelected(p0); else return (boolean) ref.get();
	}
	@Override
	public void onContextMenuClosed(Menu p0) {
		if (!callFunction(toArray(p0))) super.onContextMenuClosed(p0);
	}
	@Override
	public void onCreateContextMenu(ContextMenu p0, View p1, ContextMenu.ContextMenuInfo p2) {
		if (!callFunction(toArray(p0, p1, p2))) super.onCreateContextMenu(p0, p1, p2);
	}
	@Override
	public CharSequence onCreateDescription() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.onCreateDescription(); else return (CharSequence) ref.get();
	}
	@Override
	protected Dialog onCreateDialog(int p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.onCreateDialog(p0); else return (Dialog) ref.get();
	}
	@Override
	protected Dialog onCreateDialog(int p0, Bundle p1) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1))) return super.onCreateDialog(p0, p1); else return (Dialog) ref.get();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.onCreateOptionsMenu(p0); else return (boolean) ref.get();
	}
	@Override
	public boolean onCreatePanelMenu(int p0, Menu p1) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1))) return super.onCreatePanelMenu(p0, p1); else return (boolean) ref.get();
	}
	@Override
	public View onCreatePanelView(int p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.onCreatePanelView(p0); else return (View) ref.get();
	}
	@Override
	public void onCreateSupportNavigateUpTaskStack(TaskStackBuilder p0) {
		if (!callFunction(toArray(p0))) super.onCreateSupportNavigateUpTaskStack(p0);
	}
	@Override
	public boolean onCreateThumbnail(Bitmap p0, Canvas p1) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1))) return super.onCreateThumbnail(p0, p1); else return (boolean) ref.get();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		callFunction();
	}
	@Override
	public void onDetachedFromWindow() {
		if (!callFunction()) super.onDetachedFromWindow();
	}
	@Override
	public void onEnterAnimationComplete() {
		if (!callFunction()) super.onEnterAnimationComplete();
	}
	@Override
	public boolean onGenericMotionEvent(MotionEvent p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.onGenericMotionEvent(p0); else return (boolean) ref.get();
	}
	@Override
	public boolean onKeyDown(int p0, KeyEvent p1) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1))) return super.onKeyDown(p0, p1); else return (boolean) ref.get();
	}
	@Override
	public boolean onKeyLongPress(int p0, KeyEvent p1) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1))) return super.onKeyLongPress(p0, p1); else return (boolean) ref.get();
	}
	@Override
	public boolean onKeyMultiple(int p0, int p1, KeyEvent p2) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1, p2))) return super.onKeyMultiple(p0, p1, p2); else return (boolean) ref.get();
	}
	@Override
	public boolean onKeyShortcut(int p0, KeyEvent p1) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1))) return super.onKeyShortcut(p0, p1); else return (boolean) ref.get();
	}
	@Override
	public boolean onKeyUp(int p0, KeyEvent p1) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1))) return super.onKeyUp(p0, p1); else return (boolean) ref.get();
	}
	@Override
	public void onLowMemory() {
		if (!callFunction()) super.onLowMemory();
	}
	@Override
	public boolean onMenuOpened(int p0, Menu p1) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1))) return super.onMenuOpened(p0, p1); else return (boolean) ref.get();
	}
	@Override
	public boolean onNavigateUp() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.onNavigateUp(); else return (boolean) ref.get();
	}
	@Override
	public boolean onNavigateUpFromChild(Activity p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.onNavigateUpFromChild(p0); else return (boolean) ref.get();
	}
	@Override
	protected void onNewIntent(Intent p0) {
		if (!callFunction(toArray(p0))) super.onNewIntent(p0);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.onOptionsItemSelected(p0); else return (boolean) ref.get();
	}
	@Override
	public void onOptionsMenuClosed(Menu p0) {
		if (!callFunction(toArray(p0))) super.onOptionsMenuClosed(p0);
	}
	@Override
	public void onPanelClosed(int p0, Menu p1) {
		if (!callFunction(toArray(p0, p1))) super.onPanelClosed(p0, p1);
	}
	@Override
	protected void onPause() {
		super.onPause();
		callFunction();
	}
	@Override
	protected void onPostCreate(Bundle p0) {
		if (!callFunction(toArray(p0))) super.onPostCreate(p0);
	}
	@Override
	protected void onPostResume() {
		if (!callFunction()) super.onPostResume();
	}
	@Override
	protected void onPrepareDialog(int p0, Dialog p1) {
		if (!callFunction(toArray(p0, p1))) super.onPrepareDialog(p0, p1);
	}
	@Override
	protected void onPrepareDialog(int p0, Dialog p1, Bundle p2) {
		if (!callFunction(toArray(p0, p1, p2))) super.onPrepareDialog(p0, p1, p2);
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.onPrepareOptionsMenu(p0); else return (boolean) ref.get();
	}
	@Override
	protected boolean onPrepareOptionsPanel(View p0, Menu p1) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1))) return super.onPrepareOptionsPanel(p0, p1); else return (boolean) ref.get();
	}
	@Override
	public boolean onPreparePanel(int p0, View p1, Menu p2) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1, p2))) return super.onPreparePanel(p0, p1, p2); else return (boolean) ref.get();
	}
	@Override
	public void onPrepareSupportNavigateUpTaskStack(TaskStackBuilder p0) {
		if (!callFunction(toArray(p0))) super.onPrepareSupportNavigateUpTaskStack(p0);
	}
	@Override
	public void onProvideAssistData(Bundle p0) {
		if (!callFunction(toArray(p0))) super.onProvideAssistData(p0);
	}
	@Override
	public Uri onProvideReferrer() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.onProvideReferrer(); else return (Uri) ref.get();
	}
	@Override
	public void onRequestPermissionsResult(int p0, String[] p1, int[] p2) {
		if (!callFunction(toArray(p0, p1, p2))) super.onRequestPermissionsResult(p0, p1, p2);
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		callFunction();
	}
	@Override
	protected void onRestoreInstanceState(Bundle p0) {
		if (!callFunction(toArray(p0))) super.onRestoreInstanceState(p0);
	}
	@Override
	protected void onResume() {
		super.onResume();
		callFunction();
	}
	@Override
	protected void onResumeFragments() {
		if (!callFunction()) super.onResumeFragments();
	}
	@Override
	public Object onRetainCustomNonConfigurationInstance() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.onRetainCustomNonConfigurationInstance(); else return (Object) ref.get();
	}
	@Override
	protected void onSaveInstanceState(Bundle p0) {
		if (!callFunction(toArray(p0))) super.onSaveInstanceState(p0);
	}
	@Override
	public boolean onSearchRequested() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.onSearchRequested(); else return (boolean) ref.get();
	}
	@Override
	protected void onStart() {
		super.onStart();
		callFunction();
	}
	@Override
	public void onStateNotSaved() {
		if (!callFunction()) super.onStateNotSaved();
	}
	@Override
	protected void onStop() {
		super.onStop();
		callFunction();
	}
	@Override
	public void onSupportActionModeFinished(ActionMode p0) {
		if (!callFunction(toArray(p0))) super.onSupportActionModeFinished(p0);
	}
	@Override
	public void onSupportActionModeStarted(ActionMode p0) {
		if (!callFunction(toArray(p0))) super.onSupportActionModeStarted(p0);
	}
	@Override
	public void onSupportContentChanged() {
		if (!callFunction()) super.onSupportContentChanged();
	}
	@Override
	public boolean onSupportNavigateUp() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.onSupportNavigateUp(); else return (boolean) ref.get();
	}
	@Override
	public void onThemeChange(String p0) {
		if (!callFunction(toArray(p0))) super.onThemeChange(p0);
	}
	@Override
	protected void onTitleChanged(CharSequence p0, int p1) {
		if (!callFunction(toArray(p0, p1))) super.onTitleChanged(p0, p1);
	}
	@Override
	public boolean onTouchEvent(MotionEvent p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.onTouchEvent(p0); else return (boolean) ref.get();
	}
	@Override
	public boolean onTrackballEvent(MotionEvent p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.onTrackballEvent(p0); else return (boolean) ref.get();
	}
	@Override
	public void onTrimMemory(int p0) {
		if (!callFunction(toArray(p0))) super.onTrimMemory(p0);
	}
	@Override
	public void onUserInteraction() {
		if (!callFunction()) super.onUserInteraction();
	}
	@Override
	protected void onUserLeaveHint() {
		if (!callFunction()) super.onUserLeaveHint();
	}
	@Override
	public void onVisibleBehindCanceled() {
		if (!callFunction()) super.onVisibleBehindCanceled();
	}
	@Override
	public void onWindowAttributesChanged(WindowManager.LayoutParams p0) {
		if (!callFunction(toArray(p0))) super.onWindowAttributesChanged(p0);
	}
	@Override
	public void onWindowFocusChanged(boolean p0) {
		if (!callFunction(toArray(p0))) super.onWindowFocusChanged(p0);
	}
	@Override
	public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.onWindowStartingSupportActionMode(p0); else return (ActionMode) ref.get();
	}
	@Override
	public void openContextMenu(View p0) {
		if (!callFunction(toArray(p0))) super.openContextMenu(p0);
	}
	@Override
	public FileInputStream openFileInput(String p0) throws FileNotFoundException {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.openFileInput(p0); else return (FileInputStream) ref.get();
	}
	@Override
	public FileOutputStream openFileOutput(String p0, int p1) throws FileNotFoundException {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1))) return super.openFileOutput(p0, p1); else return (FileOutputStream) ref.get();
	}
	@Override
	public void openOptionsMenu() {
		if (!callFunction()) super.openOptionsMenu();
	}
	@Override
	public SQLiteDatabase openOrCreateDatabase(String p0, int p1, SQLiteDatabase.CursorFactory p2, DatabaseErrorHandler p3) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1, p2, p3))) return super.openOrCreateDatabase(p0, p1, p2, p3); else return (SQLiteDatabase) ref.get();
	}
	@Override
	public SQLiteDatabase openOrCreateDatabase(String p0, int p1, SQLiteDatabase.CursorFactory p2) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1, p2))) return super.openOrCreateDatabase(p0, p1, p2); else return (SQLiteDatabase) ref.get();
	}
	@Override
	public void overridePendingTransition(int p0, int p1) {
		if (!callFunction(toArray(p0, p1))) super.overridePendingTransition(p0, p1);
	}
	@Override
	public Drawable peekWallpaper() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.peekWallpaper(); else return (Drawable) ref.get();
	}
	@Override
	public void postponeEnterTransition() {
		if (!callFunction()) super.postponeEnterTransition();
	}
	@Override
	public void recreate() {
		if (!callFunction()) super.recreate();
	}
	@Override
	public void registerComponentCallbacks(ComponentCallbacks p0) {
		if (!callFunction(toArray(p0))) super.registerComponentCallbacks(p0);
	}
	@Override
	public void registerForContextMenu(View p0) {
		if (!callFunction(toArray(p0))) super.registerForContextMenu(p0);
	}
	@Override
	public Intent registerReceiver(BroadcastReceiver p0, IntentFilter p1) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1))) return super.registerReceiver(p0, p1); else return (Intent) ref.get();
	}
	@Override
	public Intent registerReceiver(BroadcastReceiver p0, IntentFilter p1, String p2, Handler p3) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1, p2, p3))) return super.registerReceiver(p0, p1, p2, p3); else return (Intent) ref.get();
	}
	@Override
	public boolean releaseInstance() {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref)) return super.releaseInstance(); else return (boolean) ref.get();
	}
	@Override
	public void removeStickyBroadcast(Intent p0) {
		if (!callFunction(toArray(p0))) super.removeStickyBroadcast(p0);
	}
	@Override
	public void removeStickyBroadcastAsUser(Intent p0, UserHandle p1) {
		if (!callFunction(toArray(p0, p1))) super.removeStickyBroadcastAsUser(p0, p1);
	}
	@Override
	public void reportFullyDrawn() {
		if (!callFunction()) super.reportFullyDrawn();
	}
	@Override
	public boolean requestVisibleBehind(boolean p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.requestVisibleBehind(p0); else return (boolean) ref.get();
	}
	@Override
	public void revokeUriPermission(Uri p0, int p1) {
		if (!callFunction(toArray(p0, p1))) super.revokeUriPermission(p0, p1);
	}
	@Override
	public void sendBroadcast(Intent p0) {
		if (!callFunction(toArray(p0))) super.sendBroadcast(p0);
	}
	@Override
	public void sendBroadcast(Intent p0, String p1) {
		if (!callFunction(toArray(p0, p1))) super.sendBroadcast(p0, p1);
	}
	@Override
	public void sendBroadcastAsUser(Intent p0, UserHandle p1) {
		if (!callFunction(toArray(p0, p1))) super.sendBroadcastAsUser(p0, p1);
	}
	@Override
	public void sendBroadcastAsUser(Intent p0, UserHandle p1, String p2) {
		if (!callFunction(toArray(p0, p1, p2))) super.sendBroadcastAsUser(p0, p1, p2);
	}
	@Override
	public void sendOrderedBroadcast(Intent p0, String p1) {
		if (!callFunction(toArray(p0, p1))) super.sendOrderedBroadcast(p0, p1);
	}
	@Override
	public void sendOrderedBroadcast(Intent p0, String p1, BroadcastReceiver p2, Handler p3, int p4, String p5, Bundle p6) {
		if (!callFunction(toArray(p0, p1, p2, p3, p4, p5, p6))) super.sendOrderedBroadcast(p0, p1, p2, p3, p4, p5, p6);
	}
	@Override
	public void sendOrderedBroadcastAsUser(Intent p0, UserHandle p1, String p2, BroadcastReceiver p3, Handler p4, int p5, String p6, Bundle p7) {
		if (!callFunction(toArray(p0, p1, p2, p3, p4, p5, p6, p7))) super.sendOrderedBroadcastAsUser(p0, p1, p2, p3, p4, p5, p6, p7);
	}
	@Override
	public void sendStickyBroadcast(Intent p0) {
		if (!callFunction(toArray(p0))) super.sendStickyBroadcast(p0);
	}
	@Override
	public void sendStickyBroadcastAsUser(Intent p0, UserHandle p1) {
		if (!callFunction(toArray(p0, p1))) super.sendStickyBroadcastAsUser(p0, p1);
	}
	@Override
	public void sendStickyOrderedBroadcast(Intent p0, BroadcastReceiver p1, Handler p2, int p3, String p4, Bundle p5) {
		if (!callFunction(toArray(p0, p1, p2, p3, p4, p5))) super.sendStickyOrderedBroadcast(p0, p1, p2, p3, p4, p5);
	}
	@Override
	public void sendStickyOrderedBroadcastAsUser(Intent p0, UserHandle p1, BroadcastReceiver p2, Handler p3, int p4, String p5, Bundle p6) {
		if (!callFunction(toArray(p0, p1, p2, p3, p4, p5, p6))) super.sendStickyOrderedBroadcastAsUser(p0, p1, p2, p3, p4, p5, p6);
	}
	@Override
	public void setBackButtonColor(int p0) {
		if (!callFunction(toArray(p0))) super.setBackButtonColor(p0);
	}
	@Override
	public void setContentView(View p0, ViewGroup.LayoutParams p1) {
		if (!callFunction(toArray(p0, p1))) super.setContentView(p0, p1);
	}
	@Override
	public void setContentView(View p0) {
		if (!callFunction(toArray(p0))) super.setContentView(p0);
	}
	@Override
	public void setContentView(int p0) {
		if (!callFunction(toArray(p0))) super.setContentView(p0);
	}
	@Override
	public void setEnterSharedElementCallback(SharedElementCallback p0) {
		if (!callFunction(toArray(p0))) super.setEnterSharedElementCallback(p0);
	}
	@Override
	public void setExitSharedElementCallback(SharedElementCallback p0) {
		if (!callFunction(toArray(p0))) super.setExitSharedElementCallback(p0);
	}
	@Override
	public void setFinishOnTouchOutside(boolean p0) {
		if (!callFunction(toArray(p0))) super.setFinishOnTouchOutside(p0);
	}
	@Override
	public void setImmersive(boolean p0) {
		if (!callFunction(toArray(p0))) super.setImmersive(p0);
	}
	@Override
	public void setIntent(Intent p0) {
		if (!callFunction(toArray(p0))) super.setIntent(p0);
	}
	@Override
	public void setLogTag(String p0) {
		if (!callFunction(toArray(p0))) super.setLogTag(p0);
	}
	@Override
	public void setRequestedOrientation(int p0) {
		if (!callFunction(toArray(p0))) super.setRequestedOrientation(p0);
	}
	@Override
	public void setStatusBarColor(int p0) {
		if (!callFunction(toArray(p0))) super.setStatusBarColor(p0);
	}
	@Override
	public void setSubTitle(CharSequence p0) {
		if (!callFunction(toArray(p0))) super.setSubTitle(p0);
	}
	@Override
	public void setSubTitleTextColor(int p0) {
		if (!callFunction(toArray(p0))) super.setSubTitleTextColor(p0);
	}
	@Override
	public void setSupportActionBar(Toolbar p0) {
		if (!callFunction(toArray(p0))) super.setSupportActionBar(p0);
	}
	@Override
	public void setSupportProgress(int p0) {
		if (!callFunction(toArray(p0))) super.setSupportProgress(p0);
	}
	@Override
	public void setSupportProgressBarIndeterminate(boolean p0) {
		if (!callFunction(toArray(p0))) super.setSupportProgressBarIndeterminate(p0);
	}
	@Override
	public void setSupportProgressBarIndeterminateVisibility(boolean p0) {
		if (!callFunction(toArray(p0))) super.setSupportProgressBarIndeterminateVisibility(p0);
	}
	@Override
	public void setSupportProgressBarVisibility(boolean p0) {
		if (!callFunction(toArray(p0))) super.setSupportProgressBarVisibility(p0);
	}
	@Override
	public void setTheme(int p0) {
		if (!callFunction(toArray(p0))) super.setTheme(p0);
	}
	@Override
	public void setTitle(CharSequence p0) {
		if (!callFunction(toArray(p0))) super.setTitle(p0);
	}
	@Override
	public void setTitle(int p0) {
		if (!callFunction(toArray(p0))) super.setTitle(p0);
	}
	@Override
	public void setTitleBackground(int p0) {
		if (!callFunction(toArray(p0))) super.setTitleBackground(p0);
	}
	@Override
	public void setTitleColor(int p0) {
		if (!callFunction(toArray(p0))) super.setTitleColor(p0);
	}
	@Override
	public void setTitleElevation(float p0) {
		if (!callFunction(toArray(p0))) super.setTitleElevation(p0);
	}
	@Override
	public void setTitleText(CharSequence p0) {
		if (!callFunction(toArray(p0))) super.setTitleText(p0);
	}
	@Override
	public void setTitleTextColor(int p0) {
		if (!callFunction(toArray(p0))) super.setTitleTextColor(p0);
	}
	@Override
	public void setVisible(boolean p0) {
		if (!callFunction(toArray(p0))) super.setVisible(p0);
	}
	@Override
	public void setWallpaper(Bitmap p0) throws IOException {
		if (!callFunction(toArray(p0))) super.setWallpaper(p0);
	}
	@Override
	public void setWallpaper(InputStream p0) throws IOException {
		if (!callFunction(toArray(p0))) super.setWallpaper(p0);
	}
	@Override
	public boolean shouldShowRequestPermissionRationale(String p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.shouldShowRequestPermissionRationale(p0); else return (boolean) ref.get();
	}
	@Override
	public boolean shouldUpRecreateTask(Intent p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.shouldUpRecreateTask(p0); else return (boolean) ref.get();
	}
	@Override
	public boolean showAssist(Bundle p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.showAssist(p0); else return (boolean) ref.get();
	}
	@Override
	public void showLockTaskEscapeMessage() {
		if (!callFunction()) super.showLockTaskEscapeMessage();
	}
	@Override
	public void startActivities(Intent[] p0, Bundle p1) {
		if (!callFunction(toArray(p0, p1))) super.startActivities(p0, p1);
	}
	@Override
	public void startActivities(Intent[] p0) {
		if (!callFunction(toArray(p0))) super.startActivities(p0);
	}
	@Override
	public void startActivity(Intent p0) {
		if (!callFunction(toArray(p0))) super.startActivity(p0);
	}
	@Override
	public void startActivity(Intent p0, Bundle p1) {
		if (!callFunction(toArray(p0, p1))) super.startActivity(p0, p1);
	}
	@Override
	public void startActivityForResult(Intent p0, int p1) {
		if (!callFunction(toArray(p0, p1))) super.startActivityForResult(p0, p1);
	}
	@Override
	public void startActivityForResult(Intent p0, int p1, Bundle p2) {
		if (!callFunction(toArray(p0, p1, p2))) super.startActivityForResult(p0, p1, p2);
	}
	@Override
	public void startActivityFromChild(Activity p0, Intent p1, int p2) {
		if (!callFunction(toArray(p0, p1, p2))) super.startActivityFromChild(p0, p1, p2);
	}
	@Override
	public void startActivityFromChild(Activity p0, Intent p1, int p2, Bundle p3) {
		if (!callFunction(toArray(p0, p1, p2, p3))) super.startActivityFromChild(p0, p1, p2, p3);
	}
	@Override
	public void startActivityFromFragment(Fragment p0, Intent p1, int p2) {
		if (!callFunction(toArray(p0, p1, p2))) super.startActivityFromFragment(p0, p1, p2);
	}
	@Override
	public boolean startActivityIfNeeded(Intent p0, int p1) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1))) return super.startActivityIfNeeded(p0, p1); else return (boolean) ref.get();
	}
	@Override
	public boolean startActivityIfNeeded(Intent p0, int p1, Bundle p2) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1, p2))) return super.startActivityIfNeeded(p0, p1, p2); else return (boolean) ref.get();
	}
	@Override
	public boolean startInstrumentation(ComponentName p0, String p1, Bundle p2) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1, p2))) return super.startInstrumentation(p0, p1, p2); else return (boolean) ref.get();
	}
	@Override
	public void startIntentSender(IntentSender p0, Intent p1, int p2, int p3, int p4, Bundle p5) throws IntentSender.SendIntentException {
		if (!callFunction(toArray(p0, p1, p2, p3, p4, p5))) super.startIntentSender(p0, p1, p2, p3, p4, p5);
	}
	@Override
	public void startIntentSender(IntentSender p0, Intent p1, int p2, int p3, int p4) throws IntentSender.SendIntentException {
		if (!callFunction(toArray(p0, p1, p2, p3, p4))) super.startIntentSender(p0, p1, p2, p3, p4);
	}
	@Override
	public void startIntentSenderForResult(IntentSender p0, int p1, Intent p2, int p3, int p4, int p5, Bundle p6) throws IntentSender.SendIntentException {
		if (!callFunction(toArray(p0, p1, p2, p3, p4, p5, p6))) super.startIntentSenderForResult(p0, p1, p2, p3, p4, p5, p6);
	}
	@Override
	public void startIntentSenderForResult(IntentSender p0, int p1, Intent p2, int p3, int p4, int p5) throws IntentSender.SendIntentException {
		if (!callFunction(toArray(p0, p1, p2, p3, p4, p5))) super.startIntentSenderForResult(p0, p1, p2, p3, p4, p5);
	}
	@Override
	public void startIntentSenderFromChild(Activity p0, IntentSender p1, int p2, Intent p3, int p4, int p5, int p6) throws IntentSender.SendIntentException {
		if (!callFunction(toArray(p0, p1, p2, p3, p4, p5, p6))) super.startIntentSenderFromChild(p0, p1, p2, p3, p4, p5, p6);
	}
	@Override
	public void startIntentSenderFromChild(Activity p0, IntentSender p1, int p2, Intent p3, int p4, int p5, int p6, Bundle p7) throws IntentSender.SendIntentException {
		if (!callFunction(toArray(p0, p1, p2, p3, p4, p5, p6, p7))) super.startIntentSenderFromChild(p0, p1, p2, p3, p4, p5, p6, p7);
	}
	@Override
	public void startLockTask() {
		if (!callFunction()) super.startLockTask();
	}
	@Override
	public void startManagingCursor(Cursor p0) {
		if (!callFunction(toArray(p0))) super.startManagingCursor(p0);
	}
	@Override
	public boolean startNextMatchingActivity(Intent p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.startNextMatchingActivity(p0); else return (boolean) ref.get();
	}
	@Override
	public boolean startNextMatchingActivity(Intent p0, Bundle p1) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0, p1))) return super.startNextMatchingActivity(p0, p1); else return (boolean) ref.get();
	}
	@Override
	public void startPostponedEnterTransition() {
		if (!callFunction()) super.startPostponedEnterTransition();
	}
	@Override
	public void startSearch(String p0, boolean p1, Bundle p2, boolean p3) {
		if (!callFunction(toArray(p0, p1, p2, p3))) super.startSearch(p0, p1, p2, p3);
	}
	@Override
	public ComponentName startService(Intent p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.startService(p0); else return (ComponentName) ref.get();
	}
	@Override
	public ActionMode startSupportActionMode(ActionMode.Callback p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.startSupportActionMode(p0); else return (ActionMode) ref.get();
	}
	@Override
	public void stopLockTask() {
		if (!callFunction()) super.stopLockTask();
	}
	@Override
	public void stopManagingCursor(Cursor p0) {
		if (!callFunction(toArray(p0))) super.stopManagingCursor(p0);
	}
	@Override
	public boolean stopService(Intent p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.stopService(p0); else return (boolean) ref.get();
	}
	@Override
	public void supportFinishAfterTransition() {
		if (!callFunction()) super.supportFinishAfterTransition();
	}
	@Override
	public void supportInvalidateOptionsMenu() {
		if (!callFunction()) super.supportInvalidateOptionsMenu();
	}
	@Override
	public void supportNavigateUpTo(Intent p0) {
		if (!callFunction(toArray(p0))) super.supportNavigateUpTo(p0);
	}
	@Override
	public void supportPostponeEnterTransition() {
		if (!callFunction()) super.supportPostponeEnterTransition();
	}
	@Override
	public boolean supportRequestWindowFeature(int p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.supportRequestWindowFeature(p0); else return (boolean) ref.get();
	}
	@Override
	public boolean supportShouldUpRecreateTask(Intent p0) {
		JsObjectRef ref=newJsRef();
		if (!callFunction(ref, toArray(p0))) return super.supportShouldUpRecreateTask(p0); else return (boolean) ref.get();
	}
	@Override
	public void supportStartPostponedEnterTransition() {
		if (!callFunction()) super.supportStartPostponedEnterTransition();
	}
	@Override
	public void takeKeyEvents(boolean p0) {
		if (!callFunction(toArray(p0))) super.takeKeyEvents(p0);
	}
	@Override
	public void triggerSearch(String p0, Bundle p1) {
		if (!callFunction(toArray(p0, p1))) super.triggerSearch(p0, p1);
	}
	@Override
	public void unbindService(ServiceConnection p0) {
		if (!callFunction(toArray(p0))) super.unbindService(p0);
	}
	@Override
	public void unregisterComponentCallbacks(ComponentCallbacks p0) {
		if (!callFunction(toArray(p0))) super.unregisterComponentCallbacks(p0);
	}
	@Override
	public void unregisterForContextMenu(View p0) {
		if (!callFunction(toArray(p0))) super.unregisterForContextMenu(p0);
	}
	@Override
	public void unregisterReceiver(BroadcastReceiver p0) {
		if (!callFunction(toArray(p0))) super.unregisterReceiver(p0);
	}
}
