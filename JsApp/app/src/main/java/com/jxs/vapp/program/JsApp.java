package com.jxs.vapp.program;

import android.content.*;
import android.content.res.*;
import com.jxs.v.io.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.*;
import java.util.*;
import java.util.zip.*;
import org.json.*;

public class JsApp {
	public static JsApp INSTANCE;
	private Manifest manifest;
	public static Context GlobalContext;
	private static HashMap<String,Resources> AllRs=new HashMap<>();
	private File App;
	private Resources RS;
	private String ID;
	private boolean isCompat=true;
	//private File TempDir=new File("/data/data/" + GlobalContext.getPackageName() + "/files/JsAppTemp");
	//private File TempDir=new File("/sdcard/VIDETmp");
	//private DexClassLoader ClassLoader;
	public static Resources getResources(String id) {
		return AllRs.get(id);
	}
	public String getID() {
		return ID;
	}
	private static ByteBuffer IntBuffer=ByteBuffer.allocate(Integer.SIZE / 4);
	private static int byteArrayToInt(byte[] data) {
		IntBuffer.position(0);
		IntBuffer.put(data, 0, data.length);
		IntBuffer.flip();
		return IntBuffer.getInt();
	}
	private static ByteBuffer LongBuffer=ByteBuffer.allocate(Long.SIZE / 4);
	private static long byteArrayToLong(byte[] data) {
		LongBuffer.position(0);
		LongBuffer.put(data, 0, data.length);
		LongBuffer.flip();
		return LongBuffer.getLong();
	}
	private static String readString(InputStream input) throws IOException {
		byte[] len=new byte[4];
		input.read(len);
		len = new byte[byteArrayToInt(len)];
		input.read(len);
		return new String(len);
	}
	private void init(InputStream in) throws IOException, JSONException {
		manifest = new Manifest();
		manifest.parse(new JSONObject(new String(readString(in))));
		ArrayList<Jsc> all=manifest.getAllJs();
		for (int i=0;i < all.size();i++)
			all.get(i).setParent(this);
		isCompat = manifest.isCompat();
		for (int i=0;i < all.size();i++) all.get(i).setCode(new String(EncryptUtil.decrypt(EncryptUtil.decrypt(readString(in).getBytes(), EncryptUtil.Type.Base64), EncryptUtil.Type.GZip)));
		all = null;
	}
	public interface DataWriter {
		void write(String name, byte[] data)
	}
	public static void write(InputStream in, DataWriter wr) throws IOException {
		byte[] tmpFS=new byte[4];
		byte[] buf=new byte[1024];
		if (in.read(tmpFS) == -1) return;
		int FS=byteArrayToInt(tmpFS);
		tmpFS = new byte[8];
		long ada,counter;
		int readed;
		ByteArrayOutputStream os=new ByteArrayOutputStream();
		String st;
		for (int i=0;i < FS;i++) {
			st = readString(in);
			in.read(tmpFS);
			ada = byteArrayToLong(tmpFS);
			counter = 0;
			while (counter < ada) {
				readed = ada - counter > 1024 ?1024: (int) (ada - counter);
				in.read(buf, 0, readed);
				os.write(buf, 0, readed);
				counter += readed;
			}
			wr.write(st, os.toByteArray());
			os.reset();
		}
	}
	public JsApp(InputStream in) throws IOException, JSONException {
		this(in, true);
	}
	public JsApp(InputStream in, boolean loadVApp) throws IOException, JSONException {
		init(in);
		if (!loadVApp) return;
		//For VApp
		byte[] tmpFS=new byte[4];
		byte[] buf=new byte[1024];
		if (in.read(tmpFS) == -1) return;
		int FS=byteArrayToInt(tmpFS);
		App = new File(GlobalContext.getExternalCacheDir(), hashCode() + ".apk");
		App.deleteOnExit();
		ZipInputStream zin=new ZipInputStream(GlobalContext.getAssets().open("Normal.apk"));
		ZipOutputStream zout=new ZipOutputStream(new FileOutputStream(App));
		ZipEntry entry;
		int read;
		String name;
		while ((entry = zin.getNextEntry()) != null) {
			name = entry.getName();
			if (name.startsWith("res") || name.equals("resources.arsc") || name.startsWith("org")) continue;
			zout.putNextEntry(entry);
			while ((read = zin.read(buf)) != -1) zout.write(buf, 0, read);
		}
		zin.close();
		tmpFS = new byte[8];
		long ada,counter;
		int readed;
		for (int i=0;i < FS;i++) {
			zout.putNextEntry(new ZipEntry(readString(in)));
			in.read(tmpFS);
			ada = byteArrayToLong(tmpFS);
			counter = 0;
			while (counter < ada) {
				readed = ada - counter > 1024 ?1024: (int) (ada - counter);
				in.read(buf, 0, readed);
				zout.write(buf, 0, readed);
				counter += readed;
			}
			zout.closeEntry();
		}
		zout.close();
		try {
			AssetManager m=AssetManager.class.newInstance();
			Method add=AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
			add.invoke(m, GlobalContext.getPackageResourcePath());
			add.invoke(m, App.getAbsolutePath());
			Method en=AssetManager.class.getDeclaredMethod("ensureStringBlocks");
			en.setAccessible(true);
			en.invoke(m);
			RS = new Resources(m, GlobalContext.getResources().getDisplayMetrics(), GlobalContext.getResources().getConfiguration());
			AllRs.put(ID = nextID(), RS);
		} catch (Throwable t) {}
	}
	private static int Counter=0;
	private static String nextID() {
		return String.valueOf(Counter++);
	}
	public boolean isCompat() {
		return isCompat;
	}
	public Jsc getMainJs() {
		return manifest.getMainJs();
	}
	public void run() {
		getMainJs().run(ID);
	}
	public ArrayList<Jsc> getAllJs() {
		return manifest.getAllJs();
	}
	public Jsc getJsc(String name) {
		ArrayList<Jsc> all=manifest.getAllJs();
		Jsc j=null;
		for (int i=0;i < all.size();i++) {
			if (all.get(i).getName().equals(name)) {
				j = all.get(i);
				break;
			}
		}
		return j;
	}
	public Manifest getManifest() {
		return this.manifest;
	}
	public boolean isUseDx() {
		return manifest.isUseDx();
	}
	public Object getEntity(String name) {
		Jsc j=getJsc(name);
		if (j == null) return null;
		switch (j.getExtend()) {
			case JsVActivity:{
					JsProgram pro=new JsProgram(j.getName());
					pro.setJsApp(this);
					pro.setCode(j.getCode());
					String s=JsProgram.getRandomKey();
					pro.upload(s);
					Class<?> c=null;
					try {
						c = Class.forName(isCompat ?"com.jxs.vapp.runtime.JsVActivityCompat": "com.jxs.vapp.runtime.JsVActivity");
					} catch (Exception e) {e.printStackTrace();System.exit(1);}
					return new Intent(JsApp.GlobalContext, c).putExtra("ID", s).putExtra("RS", ID).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				}
			case JsConsole:{
					JsProgram pro=new JsProgram(j.getName());
					pro.setJsApp(this);
					pro.setCode(j.getCode());
					String s=JsProgram.getRandomKey();
					pro.upload(s);
					Class<?> c=null;
					try {
						c = Class.forName(isCompat ?"com.jxs.vapp.runtime.JsConsoleActivityCompat": "com.jxs.vapp.runtime.JsConsoleActivity");
					} catch (Exception e) {e.printStackTrace();System.exit(1);}
					return new Intent(JsApp.GlobalContext, c).putExtra("ID", s).putExtra("RS", ID).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				}
		}
		return null;
	}
	public void destroy() {
		if (App != null) App.delete();
		AllRs.remove(this.ID);
	}
}
