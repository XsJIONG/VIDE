package com.jxs.vide;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import com.jxs.vapp.program.JsExtend;
import com.jxs.vapp.program.Jsc;
import com.jxs.vapp.program.Manifest;
import com.jxs.vcompat.io.EncryptUtil;
import com.jxs.vcompat.io.IOUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONException;

import static com.jxs.vide.L.get;

public class Project implements Serializable {
	public static HashMap<String,Project> Internet=new HashMap<>();
	public static final String FILE_MANIFEST="Manifest.json";
	private File dir;
	private Manifest _Manifest;
	public static Project getInstance(File path, boolean autoLoad) throws IOException,JSONException {
		if (Internet.get(path) != null) return Internet.get(path);
		return new Project(path, autoLoad);
	}
	public static Project getInstance(File dir) throws IOException,JSONException {
		return getInstance(dir, false);
	}
	public static Project getInstance(String name) throws IOException,JSONException {
		return getInstance(new File(PATH, name));
	}
	public void setType(int type) {
		_Manifest.setType(type);
	}
	public int getType() {
		return _Manifest.getType();
	}
	public void removePermission(int index) {
		for (int i=0;i < _Manifest.getPermissions().size();i++) if (_Manifest.getPermissions().get(i) == index) {
				_Manifest.getPermissions().remove(i);
				return;
			}
	}
	public void removePermission(String name) {
		_Manifest.getPermissions().remove(name);
	}
	public String[] getNeededDex() {
		ArrayList<String> a=new ArrayList<>();
		if (isCompat()) a.add("dexs/Compat"); else a.add("dexs/Normal");
		if (_Manifest.isUseDx()) a.add("dexs/Dx");
		a.add("dexs/Rhino");
		return a.toArray(new String[0]);
	}
	private Project(String name) throws IOException,JSONException {
		this(new File(PATH, name));
	}
	public boolean isCompat() {
		return _Manifest.isCompat();
	}
	public void setCompat(boolean flag) {
		_Manifest.setCompat(flag);
	}
	public Jsc addJs(String name, JsExtend ex) {
		Jsc c=new Jsc(name, ex);
		addJs(c);
		return c;
	}
	public File getFile(Jsc js) {
		return new File(dir, js.getName());
	}
	public String getAppName() {
		return _Manifest.getAppName();
	}
	public String getPackageName() {
		return _Manifest.getPackageName();
	}
	public boolean isUseDx() {
		return _Manifest.isUseDx();
	}
	public void setUseDx(boolean use) {
		_Manifest.setUseDx(use);
	}
	public void setAppName(String s) {
		_Manifest.setAppName(s);
	}
	public void setPackageName(String s) {
		_Manifest.setPackageName(s);
	}
	public void addJs(Jsc js) {
		File f=getFile(js);
		if (f.exists()) return;
		try {
			f.createNewFile();
		} catch (IOException e) {}
		_Manifest.addJs(js);
	}
	public File getNomediaFile() {
		return new File(dir, ".nomedia");
	}
	public Jsc findJscByName(String name) {
		ArrayList<Jsc> all=_Manifest.getAllJs();
		for (int i=0;i < all.size();i++) if (name.equals(all.get(i).getName())) return all.get(i);
		return null;
	}
	private Project(File f) throws IOException,JSONException {
		this(f, false);
	}
	public File getManifestFile() {
		return new File(dir, FILE_MANIFEST);
	}
	public ArrayList<Jsc> getAllJs() {
		return _Manifest.getAllJs();
	}
	public File getLibDir() {
		File f=new File(dir, "libs");
		if (f.isFile()) f.delete();
		if (!f.exists()) f.mkdirs();
		return f;
	}
	private Project(File f, boolean autoLoad) throws IOException,JSONException {
		Internet.put(f.getName(), this);
		this.dir = f;
		if (!getNomediaFile().exists()) IOUtil.createNewFile(getNomediaFile());
		if ((!f.exists()) || !isProject(f)) {
			IOUtil.delete(f);
			boolean s=f.mkdirs();
			if (!s)
				MessageActivity.showMessage(MyApplication.getContext(), "ERRRRRRRRRRRRRRRRRRRRRRRR!", "Oops...");
			_Manifest = new Manifest();
			_Manifest.setOutputFile(getManifestFile());
			new File(f, "assets").mkdirs();
			new File(f, "libs").mkdirs();
			saveManifest();
		} else {
			_Manifest = new Manifest();
			_Manifest.setOutputFile(getManifestFile());
			if (!getAssets().exists()) getAssets().mkdirs();
			if (autoLoad) _Manifest.parse(getManifestFile());
		}
	}
	public File getAssets() {
		return new File(dir, "assets");
	}
	public void loadManifest() throws IOException,JSONException {
		_Manifest.parse(getManifestFile());
	}
	public File getBuildDex() {
		File f=new File(dir, "build/classes.dex");
		if (!f.exists()) {
			f.mkdirs();
			f.delete();
		}
		return f;
	}
	public void saveManifest() {
		try {
			_Manifest.save(new FileOutputStream(getManifestFile()));
		} catch (IOException e) {}
	}
	public File getDir() {
		return this.dir;
	}
	public void setName(String name) {
		File nf=new File(dir.getParentFile(), name);
		if (nf.exists()) IOUtil.delete(nf);
		dir.renameTo(nf);
	}
	public Manifest getManifest() {
		return _Manifest;
	}
	public void setProjectName(String name) {
		_Manifest.setProjectName(name);
	}
	public String getProjectName() {
		return _Manifest.getProjectName();
	}
	public Jsc getMainJs() {
		return _Manifest.getMainJs();
	}
	public void setMainJs(Jsc s) {
		_Manifest.setMainJs(s);
	}
	public String getName() {
		return this.dir.getName();
	}
	public File getIcon() {
		File f=new File(dir, "icon.png");
		if (!f.exists()) {
			try {
				((BitmapDrawable) MyApplication.getContext().getResources().getDrawable(R.drawable.icon)).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(f));
			} catch (IOException e) {return null;}
		}
		return f;
	}
	public static final File PATH=new File(Environment.getExternalStorageDirectory(), "VIDE");
	public static boolean isProject(File f) {
		if (!PATH.exists()) PATH.mkdirs();
		if (f.isFile()) return false;
		return (new File(f, FILE_MANIFEST).exists());
	}
	public static File[] listFiles() {
		if (!PATH.exists()) PATH.mkdirs();
		return PATH.listFiles();
	}
	public void delete() {
		IOUtil.delete(dir);
	}
	public static final File TMP=new File("/data/data/" + MyApplication.getContext().getPackageName() + "/files/CompileTemp/");
	private static ByteBuffer buffer=ByteBuffer.allocate(4);
	public static byte[] intToByteArray(int s) {
		buffer.putInt(0, s);
		return buffer.array();
	}
	public void compile(OutputStream output) throws Exception {
		writeString(output, _Manifest.toJSON().toString());
		ArrayList<Jsc> s=_Manifest.getAllJs();
		for (int i=0;i < s.size();i++)
			writeString(output, new String(EncryptUtil.encrypt(EncryptUtil.encrypt(IOUtil.read(getFile(s.get(i))), EncryptUtil.Type.GZip), EncryptUtil.Type.Base64)));
		output.close();
	}
	private static void writeString(OutputStream output, String str) throws IOException {
		byte[] q=str.getBytes();
		output.write(intToByteArray(q.length));
		output.write(q);
		q = null;
	}
	public void setVersionCode(int code) {
		_Manifest.setVersionCode(code);
	}
	public int getVersionCode() {
		return _Manifest.getVersionCode();
	}
	public void setVersionName(String name) {
		_Manifest.setVersionName(name);
	}
	public String getVersionName() {
		return _Manifest.getVersionName();
	}
	public ArrayList<Integer> getPermissions() {
		return _Manifest.getPermissions();
	}
	public void addPermission(int index) {
		_Manifest.addPermission(index);
	}
	public void setPermissions(ArrayList<Integer> per) {
		_Manifest.setPermissions(per);
	}
	public String[] getPermissionsForString() {
		ArrayList<Integer> ps=_Manifest.getPermissions();
		String[] s=new String[ps.size()];
		for (int i=0;i < s.length;i++) {
			s[i] = Global.Permissions[ps.get(i)];
		}
		return s;
	}
	/*private void compileDex(OutputStream output) throws Exception {
	 if (TMP.isFile()) TMP.delete();
	 if (!TMP.exists()) TMP.mkdirs();
	 writeString(output, _Manifest.toJSON().toString());
	 ArrayList<Jsc> s=getAllJs();
	 byte[] cl=null;
	 String name=null;
	 File f=null;
	 CfOptions mCfOptions=new CfOptions();
	 DexOptions mDexOptions=new DexOptions();
	 DexFile DexFile=new DexFile(mDexOptions);
	 DirectClassFile ClassFile=null;
	 for (int i=0;i < s.size();i++) {
	 f = getFile(s.get(i));
	 name = s.get(i).getClassName();
	 cl = JsCompiler.compile(name, new String(IOUtil.read(f)));
	 ClassFile = new DirectClassFile(cl, name, false);
	 ClassFile.setAttributeFactory(StdAttributeFactory.THE_ONE);
	 ClassFile.getMagic();
	 DexFile.add(CfTranslator.translate(ClassFile, cl, mCfOptions, mDexOptions, DexFile));
	 }
	 try {
	 DexFile.writeTo(output, null, false);
	 } catch (Exception e) {
	 e.printStackTrace();
	 }
	 output.close();
	 cl = null;
	 DexFile = null;
	 ClassFile = null;
	 System.gc();
	 }*/
	public static String isPackageNameValid(String p) {
		if (p == null || p.length() == 0)
			return get(L.PackageNameCantEmpty);
		if (p.charAt(0) == '.' || p.charAt(p.length() - 1) == '.')
			return get(L.Editor_PackageNameContainsIllegalChar);
		char c;
		for (int i=0;i < p.length();i++) {
			c = p.charAt(i);
			if (!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '.'))
				return get(L.ContainsIllegalChar);
		}
		String[] ps=p.split("[.]");
		if (ps.length < 2) return get(L.PkgLessThanTwo);
		for (String one : ps) if (Character.isDigit(one.charAt(0)))
				return get(L.Editor_PackageNameStartsWithDigit);
		return null;
	}
	public static String isAppNameValid(String n) {
		if (n == null || n.length() == 0)
			return get(L.NameCantEmpty);
		if (n.contains(File.separator)) return get(L.ContainsIllegalChar);
		return null;
	}
}
