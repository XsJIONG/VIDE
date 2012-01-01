package com.jxs.vcompat.fragment;

import android.content.*;
import android.graphics.*;
import android.support.v7.widget.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import com.jxs.vcompat.ui.*;
import com.jxs.vcompat.widget.*;
import java.io.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;

public class ConsoleFragment extends VFragment {
	public static final String TAG="VConsole";

	public ConsoleFragment(Context cx, Runnable r) {
		super(cx);
		this.consoleRunnable = r;
	}
	@Override
	public Object getTag() {
		return "VConsole";
	}
	private VScrollView scroll;
	private AppCompatEditText scr;
	private int AcceptLength;
	private UI ui;
	private boolean TextChanging=false,reading=true;
	@Override
	public View getView() {
		ui = new UI(getContext());
		scr = new AppCompatEditText(getContext());
		scr.setGravity(Gravity.TOP | Gravity.LEFT);
		scroll = new VScrollView(getContext());
		scroll.addView(scr, new ScrollView.LayoutParams(ScrollView.LayoutParams.FILL_PARENT, ScrollView.LayoutParams.FILL_PARENT));
		scroll.setFillViewport(true);
		scr.setTypeface(Typeface.MONOSPACE);
		AcceptLength = 0;
		scr.addTextChangedListener(new TextWatcher() {
				private CharSequence last;
				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
					if (TextChanging) return;
					last = scr.getText().subSequence(0, scr.length());
				}
				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
					if (TextChanging) return;
					if (scr.getSelectionStart() <= AcceptLength || (!reading)) {
						TextChanging = true;
						scr.setText(last);
						scr.setSelection(scr.length());
						TextChanging = false;
						last = null;
						return;
					}
					last = scr.getText();
					if (last.toString().endsWith("\n")) {
						int i=scr.length() - 1;
						while (i > 0) {
							i--;
							if (last.toString().charAt(i) == '\n') break;
						}
						String h=last.toString().substring(AcceptLength + 1);
						last = null;
						AcceptLength = scr.length() - 1;
						if (reading)
							onRead(h);
					}
				}
				@Override
				public void afterTextChanged(Editable p1) {}
			});
		scr.setText("");
		//scr.setBackgroundDrawable(null);
		setBackgroundColor(Color.BLACK);
		setForegroundColor(Color.WHITE);
		scroll.post(new Runnable() {
				@Override
				public void run() {
					new Thread(new Runnable() {
							@Override
							public void run() {
								if (consoleRunnable != null) consoleRunnable.run();
							}
						}).start();
				}
			});
		return scroll;
	}
	public void injectToSystem() {
		System.setIn(in);
		System.setOut(out);
		System.setErr(out);
	}
	void onRead(String s) {
		putToRead(s);
		if (Prompt != null) out.print(Prompt);
	}
	String Prompt=null;
	public void setPrompt(String prompt) {
		this.Prompt = prompt;
	}
	public String getPrompt() {return this.Prompt;}
	public void disableRead() {reading = false;}
	public void enableRead() {reading = true;}
	Queue TempRead=new LinkedList();
	CharBuffer CB=CharBuffer.allocate(1);
	ByteBuffer BB=ByteBuffer.allocate(5);
	CharsetEncoder CS=Charset.defaultCharset().newEncoder();
	void putToRead(String str) {
		synchronized (TempRead) {
			for (int i=0;i < str.length();i++) {
				CB.rewind();
				CB.append(str.charAt(i));
				CB.rewind();
				BB.rewind();
				CS.encode(CB, BB, false);
				int pos=BB.position();
				BB.rewind();
				for (int j=0;j < pos;j++)
					TempRead.add(Integer.valueOf(BB.get()));
			}
			TempRead.notifyAll();
		}
	}
	private Runnable consoleRunnable=null;
	public InputStream in=new InputStream() {
		boolean close=false;
		@Override
		public int read() {
			if (close) return -1;
			synchronized (TempRead) {
				try {
					if (TempRead.size() == 0) TempRead.wait();
				} catch (InterruptedException e) {}
			}
			return ((Integer) TempRead.poll()).intValue();
		}
		@Override
		public int read(byte[] b, int off, int len) {
			if (close) return -1;
			synchronized (TempRead) {
				try {
					if (TempRead.size() == 0) TempRead.wait();
				} catch (InterruptedException e) {}
			}
			int i=0;
			while (!TempRead.isEmpty() && i < len) {
				b[off + i] = (byte) ((Integer) TempRead.poll()).intValue();
				i++;
			}
			return i;
		}
		@Override
		public void close() {close = true;}
	};
	public void setBackgroundColor(final int color) {
		ui.autoOnUi(new Runnable() {
				@Override
				public void run() {
					scr.setBackgroundColor(color);
				}
			});
	}
	public void setForegroundColor(final int color) {
		ui.autoOnUi(new Runnable() {
				@Override
				public void run() {
					scr.setTextColor(color);
				}
			});
	}
	public PrintStream out=new PrintStream(new OutputStream() {
			@Override
			public void write(final byte[] b, final int off, final int len) {
				ui.autoOnUi(new Runnable() {
						@Override
						public void run() {
							TextChanging = true;
							scr.getText().append(new String(b, off, len));
							TextChanging = false;
							AcceptLength = scr.length() - 1;
							scr.setSelection(scr.length());
						}
					});
			}
			@Override
			public void write(final int ch) {
				ui.autoOnUi(new Runnable() {
						@Override
						public void run() {
							TextChanging = true;
							scr.getText().append((char) ch);
							TextChanging = false;
							AcceptLength = scr.length() - 1;
							scr.setSelection(scr.length());
						}
					});
			}
		}, true);
	public void clearScreen() {
		ui.autoOnUi(clearScreenAction);
	}
	Runnable clearScreenAction=new Runnable() {
		@Override
		public void run() {
			TextChanging = true;
			scr.setText("");
			TextChanging = false;
			scr.setSelection(scr.length());
		}
	};
	public void moveToEnd() {
		ui.autoOnUi(moveToEndAction);
	}
	Runnable moveToEndAction=new Runnable() {
		@Override
		public void run() {
			AcceptLength = scr.length() - 1;
		}
	};
}
