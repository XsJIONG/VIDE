package com.jxs.vapp.program;

import android.content.*;
import android.content.res.*;
import com.jxs.vcompat.io.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.*;
import java.util.*;
import java.util.zip.*;
import org.json.*;
import com.jxs.vide.*;
import android.os.*;

public class JsApp {
	public static JsApp INSTANCE;
	private Manifest manifest;
	public static Context GlobalContext;
	private boolean isCompat=true;
	//private File TempDir=new File("/data/data/" + GlobalContext.getPackageName() + "/files/JsAppTemp");
	//private File TempDir=new File("/sdcard/VIDETmp");
	//private DexClassLoader ClassLoader;
	private static ByteBuffer IntBuffer=ByteBuffer.allocate(Integer.SIZE / 4);
	private static int byteArrayToInt(byte[] data) {
		IntBuffer.position(0);
		IntBuffer.put(data, 0, data.length);
		IntBuffer.flip();
		return IntBuffer.getInt();
	}
	private static String readString(InputStream input) throws IOException {
		byte[] len=new byte[4];
		input.read(len);
		len = new byte[byteArrayToInt(len)];
		input.read(len);
		return new String(len);
	}
	public JsApp(InputStream in) throws IOException, JSONException {
		manifest = new Manifest();
		manifest.parse(new JSONObject(new String(readString(in))));
		ArrayList<Jsc> all=manifest.getAllJs();
		for (int i=0;i < all.size();i++)
			all.get(i).setParent(this);
		isCompat = manifest.isCompat();
		for (int i=0;i < all.size();i++) all.get(i).setCode(new String(EncryptUtil.decrypt(EncryptUtil.decrypt(readString(in).getBytes(), EncryptUtil.Type.Base64), EncryptUtil.Type.GZip)));
		all = null;
	}
	public boolean isCompat() {
		return isCompat;
	}
	public Jsc getMainJs() {
		return manifest.getMainJs();
	}
	public void run() {
		getMainJs().run();
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
					return new Intent(JsApp.GlobalContext, c).putExtra("ID", s).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
					return new Intent(JsApp.GlobalContext, c).putExtra("ID", s).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				}
		}
		return null;
	}
}
