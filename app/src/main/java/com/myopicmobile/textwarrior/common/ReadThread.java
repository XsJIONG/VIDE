package com.myopicmobile.textwarrior.common;

import android.os.Handler;
import android.os.Message;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by zyw on 2017/10/2.
 */
public class ReadThread extends Thread {
	public static final int MSG_READ_OK =0x101;
	public static final   int MSG_READ_FAIL =0x102;
	private  Handler handler;
	private  String path;
	public ReadThread(String path, Handler handler) {
		this.path = path;
		this.handler = handler;
	}
	@Override
	public void run() {
		readFile(path);
	}

	private  void readFile(String file) {
		FileInputStream fileInputStream = null;
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		try {
			fileInputStream = new FileInputStream(file);
			byte[] buf=new byte[1024];
			int len=0;
			while ((len = fileInputStream.read(buf)) != -1)
				out.write(buf, 0, len);
			out.close();
			handler.sendMessage(Message.obtain(handler, MSG_READ_OK, new String(out.toByteArray())));
		} catch (IOException e) {
			e.printStackTrace();
			handler.sendMessage(Message.obtain(handler, MSG_READ_FAIL));
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
