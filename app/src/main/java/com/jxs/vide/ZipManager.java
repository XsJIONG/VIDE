package com.jxs.vide;

import android.util.Log;
import com.jxs.vcompat.io.IOUtil;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipManager {
	public static void extraZipEntry(File file, String[] entryFiles, String[] recFiles)throws IOException {
		ZipFile zipFile=null;
		try {
			zipFile = new ZipFile(file);
			byte[] buff=createBuffBytes();
			int len;
			final int size=entryFiles.length;
			for (int i=0;i < size;i++) {
				final ZipEntry entry = zipFile.getEntry(entryFiles[i]);
				InputStream stream=null;
				FileOutputStream fos=null;
				try {
					stream = zipFile.getInputStream(entry);
					fos = new FileOutputStream(recFiles[i]);
					while ((len = stream.read(buff)) > 0) {
						fos.write(buff, 0, len);
					}
				} catch (IOException e) {
					throw e;
				} finally {
					closeQuietly(fos, stream);
				}
			}
		} catch (IOException e) {
			throw e;
		} finally {
			closeQuietly(zipFile);
		}
	}
	public static void addEntrys(File zipFile, String[] entryNames, InputStream[] mapFiles) throws IOException {
		File tempFile = File.createTempFile(zipFile.getName(), null);
		tempFile.delete();
		tempFile.deleteOnExit();
		IOUtil.copy(zipFile, tempFile);
		zipFile.delete();
		zipFile.createNewFile();
		ZipInputStream zin =null;
		ZipOutputStream zout =null;
		try {
			zin = new ZipInputStream(new FileInputStream(tempFile));
			zout = new ZipOutputStream(new FileOutputStream(zipFile));
			ZipEntry entry = zin.getNextEntry();
			final int size = (entryNames == null ? 0 : entryNames.length);
			byte[] buff = createBuffBytes();
			int len;
			String name="";
			s:while (entry != null) {
				name = entry.getName();
				for (int i=0;i < entryNames.length;i++)
					if (name.equalsIgnoreCase(entryNames[i])) {
						entry = zin.getNextEntry();
						continue s;
					}
				zout.putNextEntry(new ZipEntry(name));
				while ((len = zin.read(buff)) > 0)
					zout.write(buff, 0, len);
				zout.closeEntry();
				entry = zin.getNextEntry();
			}
			if (size > 0) {
				for (int i=0;i < size;i++) {
					try {
						zout.putNextEntry(new ZipEntry(entryNames[i]));
						while ((len = mapFiles[i].read(buff)) > 0)
							zout.write(buff, 0, len);
					} catch (IOException e) {
						throw e;
					} finally {
						zout.closeEntry();
						closeQuietly(mapFiles[i]);
					}
				}
			}
			zout.finish();
		} catch (Exception e) {
			throw new RuntimeException(Log.getStackTraceString(e));
		} finally {
			closeQuietly(zin, zout);
			tempFile.delete();
		}
	}
	private static byte[] createBuffBytes() {
		return new byte[4096];
	}
	private static void closeQuietly(Closeable...objs) {
		for (Closeable one : objs) {
			try {
				if (one != null) one.close();
			} catch (IOException e) {}
		}
	}
}
