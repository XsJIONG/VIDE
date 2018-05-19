package com.jxs.vide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import android.graphics.drawable.BitmapDrawable;

public class VUser extends BmobUser {
	private static HashMap<String,ArrayList<IconListener>> Tasks=new HashMap<>();
	private BmobFile _Icon;
	public void setIcon(BmobFile icon) {
		_Icon = icon;
	}
	public BmobFile getIcon() {
		return _Icon;
	}
	public void downloadIcon(IconListener listener) {
		if (_Icon==null) {
			listener.onDone(((BitmapDrawable) MyApplication.getContext().getResources().getDrawable(R.drawable.icon_user)).getBitmap());
			return;
		}
		if (getIconCacheFile().exists()) {
			try {
				listener.onDone(BitmapFactory.decodeStream(new FileInputStream(getIconCacheFile())));
			} catch (IOException e) {
				VLogger.out.println("Can not load icon from:" + getIconCacheFile().getAbsolutePath());
				e.printStackTrace(VLogger.out);
			}
			return;
		}
		if (Tasks.get(getObjectId()) == null) postDownloadTask(listener); else {
			//Bmob Result listener run on the UI Thread
			//So wo need not to use synchronized
			Tasks.get(getObjectId()).add(listener);
		}
	}
	private void postDownloadTask(IconListener listener) {
		if (Tasks.get(getObjectId()) != null) return;
		ArrayList<IconListener> arr=new ArrayList<>();
		arr.add(listener);
		Tasks.put(getObjectId(), arr);
		_Icon.download(getIconCacheFile(), new DownloadFileListener() {
			@Override
			public void done(String path, BmobException e) {
				if (e == null) {
					ArrayList<IconListener> listeners=Tasks.get(getObjectId());
					if (listeners == null) return;
					try {
						Bitmap b=BitmapFactory.decodeStream(new FileInputStream(getIconCacheFile()));
						for (int i=0;i < listeners.size();i++) if (listeners.get(i) != null) listeners.get(i).onDone(b);
						Tasks.remove(getObjectId());
					} catch (IOException er) {
						VLogger.out.println("Can not load icon from:" + getIconCacheFile().getAbsolutePath());
						e.printStackTrace(VLogger.out);
					}
				} else {
					VLogger.out.println("Err while downloading file:");
					e.printStackTrace(VLogger.out);
				}
			}
			@Override
			public void onProgress(Integer pro, long ha) {}
		});
	}
	public static final File IconCacheDir=new File(Environment.getExternalStorageDirectory(), String.format("Android/data/com.jxs.vide/user/s"));
	public File getIconCacheFile() {
		if (!IconCacheDir.exists()) IconCacheDir.mkdirs();
		return new File(IconCacheDir, getObjectId());
	}
	public static interface IconListener {
		void onDone(Bitmap icon)
	}
}
