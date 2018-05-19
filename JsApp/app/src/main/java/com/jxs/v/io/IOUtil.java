package com.jxs.v.io;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import com.jxs.v.addon.IntReference;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class IOUtil {
	public static void createNewFile(File f) throws IOException {
		f.getParentFile().exists();
		f.createNewFile();
	}
	public static void writeParcel(Parcelable obj, OutputStream out) throws IOException {
		Parcel p=Parcel.obtain();
		p.writeParcelable(obj, 0);
		p.setDataPosition(0);
		out.write(p.marshall());
		out.close();
		p.recycle();
	}
	public static Parcelable readParcel(InputStream in) throws IOException {
		ByteArrayOutputStream arr=new ByteArrayOutputStream();
		int read;
		byte[] buff=new byte[512];
		while ((read = in.read(buff)) != -1) arr.write(buff, 0, read);
		in.close();
		byte[] data=arr.toByteArray();
		arr.close();
		Parcel d=Parcel.obtain();
		d.unmarshall(data, 0, data.length);
		d.setDataPosition(0);
		return d.readParcelable(IOUtil.class.getClassLoader());
	}
	public static void writeParcels(Parcelable[] obj, OutputStream out) throws IOException {
		Parcel p=Parcel.obtain();
		p.writeParcelableArray(obj, 0);
		p.setDataPosition(0);
		out.write(p.marshall());
		out.close();
		p.recycle();
	}
	public static Parcelable[] readParcels(InputStream in) throws IOException {
		ByteArrayOutputStream arr=new ByteArrayOutputStream();
		int read;
		byte[] buff=new byte[512];
		while ((read = in.read(buff)) != -1) arr.write(buff, 0, read);
		in.close();
		byte[] data=arr.toByteArray();
		arr.close();
		Parcel d=Parcel.obtain();
		d.unmarshall(data, 0, data.length);
		d.setDataPosition(0);
		return d.readParcelableArray(IOUtil.class.getClassLoader());
	}
	public static void copy(InputStream in, OutputStream out) throws IOException {
		int read;
		byte[] buff=new byte[1024];
		while ((read = in.read(buff)) != -1) out.write(buff, 0, read);
		in.close();
		out.close();
	}
	public static String bytes2String(byte[] bs) {
		return new String(bs);
	}
	public static String bytes2String(byte[] bs, String charset) throws UnsupportedEncodingException {
		return new String(bs, charset);
	}
	public static void delete(File f) {
		if (f.isDirectory()) {
			File[] fs=f.listFiles();
			if (fs != null) for (File one : fs) one.delete();
			f.delete();
		} else {
			f.delete();
		}
	}
	public static byte[] read(File f) throws IOException {
		return read(new FileInputStream(f));
	}
	public static File getCacheFile(Context cx, String name) throws IOException {
		File f=new File(cx.getCacheDir(), name);
		if (f.exists()) f.delete();
		f.createNewFile();
		return f;
	}
	public static byte[] read(InputStream in) throws IOException {
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		int read;
		byte[] buff=new byte[1024];
		while ((read = in.read(buff)) != -1) out.write(buff, 0, read);
		in.close();
		out.close();
		return out.toByteArray();
	}
	public static void copy(File in, File out) throws IOException {
		if (!in.exists()) return;
		if (out.exists()) delete(out);
		if (in.isDirectory())
			for (File one : in.listFiles()) copy(one, new File(out, one.getName()));
		else copy(new FileInputStream(in), new FileOutputStream(out));
	}
	public static void write(File f, String data) throws IOException {
		write(new FileOutputStream(f), data.getBytes());
	}
	public static void write(File f, byte[] data) throws IOException {
		write(new FileOutputStream(f), data);
	}
	public static void write(OutputStream out, String data) throws IOException {
		write(out, data.getBytes());
	}
	public static void write(OutputStream out, byte[] data) throws IOException {
		out.write(data);
	}
	public static SharedPreferences readSP(Context cx, String name) {
		return cx.getSharedPreferences(name, Context.MODE_PRIVATE);
	}
	public static SharedPreferences.Editor writeSP(Context cx, String name) {
		return readSP(cx, name).edit();
	}
	public static int getSonCount(File f) {
		if (!f.exists()) return 0;
		IntReference ref=new IntReference();
		ref.set(0);
		rGetSonCount(f, ref);
		return ref.get() - 1;
	}
	private static void rGetSonCount(File f, IntReference ref) {
		ref.set(ref.get() + 1);
		if (f.isDirectory()) for (File one : f.listFiles()) rGetSonCount(one, ref);
	}
	public static ArrayList<File> getSonFiles(File f) {
		ArrayList<File> arr=new ArrayList<>();
		if (f.isFile()) return arr;
		for (File one : f.listFiles()) rGetSonFiles(one, arr);
		return arr;
	}
	private static void rGetSonFiles(File f, ArrayList<File> arr) {
		arr.add(f);
		if (f.isDirectory()) for (File one : f.listFiles()) rGetSonFiles(one, arr);
	}
	public static String getRelativePath(File dir, File son) {
		String s=son.getPath();
		String d=dir.getPath();
		if (d.endsWith("/")) d = d.substring(0, d.length() - 1);
		return s.substring(d.length(), s.length());
	}
	private IOUtil() {}
}
