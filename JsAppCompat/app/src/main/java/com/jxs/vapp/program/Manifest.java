package com.jxs.vapp.program;

import com.jxs.vcompat.io.IOUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Manifest implements Serializable {
	public static final String TAG_ALL_JS="Js";
	public static final String TAG_PROJECT_NAME="ProjectName";
	public static final String TAG_MAIN_JS="Main";
	public static final String TAG_IS_COMPAT="Compat";
	public static final String TAG_PACKAGE_NAME="PackageName";
	public static final String TAG_APP_NAME="AppName";
	public static final String TAG_USE_DX="UseDx";
	public static final String TAG_VERSION_CODE="VersionCode";
	public static final String TAG_VERSION_NAME="VersionName";
	public static final String TAG_PERMISSIONS="Permissions";
	public static final String TAG_TYPE="Type";
	public static final int TYPE_APK=0,TYPE_VAPP=1;

	private ArrayList<Jsc> AllJs=new ArrayList<>();
	private boolean Writable=false;
	private File OutFile;
	private String ProjectName;
	private int MainJs=-1;
	private String PackageName;
	private String AppName;
	private boolean isCompat=true;
	private boolean UseDx=false;
	private int VersionCode;
	private String VersionName;
	private int Type=TYPE_APK;
	private ArrayList<Integer> Permissions=new ArrayList<>();
	public Manifest() {
		Permissions.add(18); //WRITE_EXTERNAL_STORAGE
		Permissions.add(134); //MOUNT_UNMOUNT_FILESYSTEMS
	}
	public void parse(InputStream in) throws IOException,JSONException {
		parse(new JSONObject(new String(IOUtil.read(in))));
	}
	public Jsc getMainJs() {
		return AllJs.get(MainJs);
	}
	public void setCompat(boolean flag) {
		isCompat = flag;
	}
	public void parse(File f) throws IOException,JSONException {
		if (!f.exists()) return;
		parse(new FileInputStream(f));
		Writable = true;
		OutFile = f;
	}
	public void parse(JSONObject obj) {
		recycle();
		JSONArray JSONJs=obj.optJSONArray(TAG_ALL_JS);
		for (int i=0;i < JSONJs.length();i++) AllJs.add(new Jsc(JSONJs.optJSONObject(i)));
		ProjectName = obj.optString(TAG_PROJECT_NAME);
		MainJs = obj.optInt(TAG_MAIN_JS);
		isCompat = obj.optBoolean(TAG_IS_COMPAT, true);
		PackageName = obj.optString(TAG_PACKAGE_NAME);
		AppName = obj.optString(TAG_APP_NAME);
		UseDx = obj.optBoolean(TAG_USE_DX, false);
		VersionCode = obj.optInt(TAG_VERSION_CODE, 1);
		VersionName = obj.optString(TAG_VERSION_NAME, "1.0.0");
		Type = obj.optInt(TAG_TYPE, TYPE_APK);
		JSONArray arr=obj.optJSONArray(TAG_PERMISSIONS);
		if (arr != null) {
			Permissions.clear();
			for (int i=0;i < arr.length();i++) Permissions.add(arr.optInt(i));
		}
	}
	public ArrayList<Integer> getPermissions() {
		return Permissions;
	}
	public void setType(int type) {
		this.Type = type;
	}
	public int getType() {
		return this.Type;
	}
	public void addPermission(int index) {
		if (Permissions.contains(index)) return;
		Permissions.add(Integer.valueOf(index));
	}
	public void setPermissions(ArrayList<Integer> per) {
		this.Permissions = per;
	}
	public void recycle() {
		ProjectName = null; MainJs = -1; AllJs.clear();
	}
	public boolean isCompat() {
		return isCompat;
	}
	public boolean isWritable() {
		return Writable;
	}
	public boolean isUseDx() {
		return UseDx;
	}
	public void setUseDx(boolean use) {
		UseDx = use;
	}
	public void setProjectName(String s) {
		ProjectName = s;
	}
	public String getProjectName() {
		return ProjectName;
	}
	public String getAppName() {
		return AppName;
	}
	public void setAppName(String s) {
		this.AppName = s;
	}
	public void setPackageName(String s) {
		this.PackageName = s;
	}
	public String getPackageName() {
		return PackageName;
	}
	public ArrayList<Jsc> getAllJs() {
		return AllJs;
	}
	public boolean setMainJs(int index) {
		return setMainJs(AllJs.get(index));
	}
	public boolean setMainJs(Jsc jsc) {
		if (!AllJs.contains(jsc)) return false;
		MainJs = AllJs.indexOf(jsc);
		return true;
	}
	public boolean addJs(Jsc jsc) {
		if (AllJs.contains(jsc)) return false;
		AllJs.add(jsc);
		return true;
	}
	public void saveToFile() throws IOException {
		if (OutFile == null) throw new IllegalStateException("Didn't use file to create instance");
		save(new FileOutputStream(OutFile));
	}
	public void save(OutputStream out) throws IOException {
		try {
			out.write(toJSON().toString().getBytes());
			out.close();
		} catch (JSONException e) {}
	}
	public void setOutputFile(File out) {
		OutFile = out;
		if (!out.exists()) {
			out.mkdirs();
			out.delete();
		}
	}
	public JSONObject toJSON() throws JSONException {
		JSONObject obj=new JSONObject();
		if (ProjectName != null) obj.put(TAG_PROJECT_NAME, ProjectName);
		JSONArray JSONJs=new JSONArray();
		for (int i=0;i < AllJs.size();i++) JSONJs.put(AllJs.get(i).toJSON());
		obj.put(TAG_ALL_JS, JSONJs);
		if (MainJs != -1) obj.put(TAG_MAIN_JS, MainJs);
		obj.put(TAG_IS_COMPAT, isCompat);
		obj.put(TAG_APP_NAME, AppName);
		obj.put(TAG_PACKAGE_NAME, PackageName);
		obj.put(TAG_USE_DX, UseDx);
		obj.put(TAG_VERSION_CODE, VersionCode);
		obj.put(TAG_VERSION_NAME, VersionName);
		JSONArray arr=new JSONArray();
		for (int i=0;i < Permissions.size();i++)
			arr.put(Permissions.get(i));
		obj.put(TAG_PERMISSIONS, arr);
		obj.put(TAG_TYPE, Type);
		return obj;
	}
	public void setVersionCode(int code) {
		this.VersionCode = code;
	}
	public int getVersionCode() {
		return VersionCode;
	}
	public void setVersionName(String name) {
		this.VersionName = name;
	}
	public String getVersionName() {
		return VersionName;
	}
}
