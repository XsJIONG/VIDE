package com.jxs.vide;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

public class ProgressFileInputStream extends FileInputStream {
	private OnProgressChangeListener listener=null;
	private long length=-1;
	private long now;
	public ProgressFileInputStream(String path) throws IOException {
		super(path);
		init();
	}
	public ProgressFileInputStream(File f) throws IOException {
		super(f);
		init();
	}
	public ProgressFileInputStream(FileDescriptor fd) throws IOException {
		super(fd);
		init();
	}
	private void init() throws IOException {
		length = getChannel().size();
	}
	public static interface OnProgressChangeListener {
		void onProgressChange(ProgressFileInputStream stream, float progress, long now, long total)
	}
	public void setOnProgressChangeListener(OnProgressChangeListener listener) {
		this.listener = listener;
	}
	public OnProgressChangeListener getOnProgressChangeListener() {
		return listener;
	}
	@Override
	public int read() throws IOException {
		now++;
		invalidate();
		return super.read();
	}
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		now += len;
		invalidate();
		return super.read(b, off, len);
	}
	private void invalidate() {
		if (listener == null) return;
		listener.onProgressChange(this, (float) now / length, now, length);
	}
	public long getReadedLength() {
		return now;
	}
	public long getLength() {
		return length;
	}
}
