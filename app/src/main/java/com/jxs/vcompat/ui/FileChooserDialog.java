package com.jxs.vcompat.ui;

import android.content.Context;
import android.view.View;
import com.jxs.vcompat.fragment.FileChooserFragment;
import java.io.File;

public class FileChooserDialog extends VAlertDialog {
	FileChooserFragment frag;
	public FileChooserDialog(Context cx, File f, FileChooserListener listener) {
		this(cx, f, listener, false);
	}
	public FileChooserDialog(Context cx, File f, final FileChooserListener listener, boolean chooseDir) {
		super(cx);
		frag = new FileChooserFragment(cx, f, new FileChooserFragment.FileChooserListener() {
			@Override
			public void onChoose(File f) {
				if (listener != null) listener.onChoose(f);
				dismiss();
			}
		}, chooseDir);
		super.setView(frag.getView());
	}
	public FileChooserFragment getFragment() {
		return frag;
	}
	public void setNow(File f) {
		frag.setNow(f);
	}
	public File getNow() {
		return frag.getNow();
	}

	@Override
	public VAlertDialog setView(View v) {
		throw new RuntimeException("Stub!");
	}
	public static interface FileChooserListener {
		void onChoose(File f)
	}
}
