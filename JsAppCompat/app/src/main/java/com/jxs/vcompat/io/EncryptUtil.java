package com.jxs.vcompat.io;

import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class EncryptUtil {
	public static interface Type {
		int GZip=0,Base64=1;
	}
	public static byte[] encrypt(byte[] data, int type) {
		try {
			switch (type) {
				case Type.GZip:{
						ByteArrayOutputStream out=new ByteArrayOutputStream();
						GZIPOutputStream gzip=new GZIPOutputStream(out);
						gzip.write(data);
						gzip.close();
						return out.toByteArray();
					}
				case Type.Base64:{
						ByteArrayOutputStream out=new ByteArrayOutputStream();
						Base64OutputStream b=new Base64OutputStream(out, Base64.DEFAULT);
						b.write(data);
						b.close();
						return out.toByteArray();
					}
			}
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static byte[] decrypt(byte[] data, int type) {
		try {
			switch (type) {
				case Type.GZip:{
						ByteArrayOutputStream out=new ByteArrayOutputStream();
						GZIPInputStream in=new GZIPInputStream(new ByteArrayInputStream(data));
						byte[] buff=new byte[512]; int read;
						while ((read = in.read(buff)) != -1) out.write(buff, 0, read);
						in.close();
						out.close();
						return out.toByteArray();
					}
				case Type.Base64:{
						ByteArrayOutputStream out=new ByteArrayOutputStream();
						Base64InputStream in=new Base64InputStream(new ByteArrayInputStream(data), Base64.DEFAULT);
						byte[] buff=new byte[512]; int read;
						while ((read = in.read(buff)) != -1) out.write(buff, 0, read);
						in.close();
						out.close();
						return out.toByteArray();
					}
			}
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	private EncryptUtil() {}
}
