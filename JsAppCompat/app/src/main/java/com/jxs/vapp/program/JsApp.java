package com.jxs.vapp.program;

import android.content.Context;
import android.content.Intent;
import com.jxs.vcompat.io.EncryptUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

public class JsApp {
	public static JsApp INSTANCE;
	private Manifest manifest;
	public static Context GlobalContext;
	private boolean isCompat=true;
	//private File TempDir=new File("/data/data/" + GlobalContext.getPackageName() + "/files/JsAppTemp");
	//private File TempDir=new File("/sdcard/VIDETmp");
	//private DexClassLoader ClassLoader;
	private static ByteBuffer Buffer=ByteBuffer.allocate(4);
	private static int byteArrayToInt(byte[] data) {
		Buffer.position(0);
		Buffer.put(data, 0, data.length);
		Buffer.flip();
		return Buffer.getInt();
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
	/*private void readDex(InputStream in) throws IOException {
	 if (TempDir.isFile()) TempDir.delete();
	 if (!TempDir.exists()) TempDir.mkdirs();
	 File TempDex=new File(TempDir, "classes.dex");
	 if (TempDex.exists()) TempDex.delete();
	 FileOutputStream out=new FileOutputStream(TempDex);
	 byte[] buff=new byte[1024];
	 int read;
	 while ((read = in.read(buff)) != -1) out.write(buff, 0, read);
	 out.close();
	 in.close();
	 ClassLoader = new DexClassLoader(TempDex.getPath(), GlobalContext.getCodeCacheDir().getPath(), null, GlobalContext.getClassLoader());
	 }*/
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
