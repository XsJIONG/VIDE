package com.jxs.vcompat.fragment;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.support.v4.widget.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.jxs.vcompat.ui.*;
import com.jxs.vcompat.widget.*;
import java.io.*;
import java.util.*;

import android.view.View.OnClickListener;

public class FileChooserFragment extends VFragment implements OnItemClickListener {
	private static Bitmap FolderBitmap,FileBitmap;
	public static BitmapDrawable FolderDrawable,FileDrawable;
	public static int IconSize,RightMargin;
	private SwipeRefreshLayout Refresh;
	public static void checkBitmap() {
		if (FolderBitmap != null) return;
		byte[] data=android.util.Base64.decode("iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAAABHNCSVQICAgIfAhkiAAAAo1JREFU\neJzt2rFy2zAURNEr1dE/4c/5N1KvuE8KJ4ooAoxF2wMKe8/MK2w3pBdaY94YJEmSJEmSJEmSJEmS\n5grw68WmfMPvIdZE/0CfnelbfhOBCv3DtAU6esVPvy3wRQr9Q7QFOnrlT78t8EmF/uHZAh2N8Om3\nBTYq9A/NWZ/SyO5LTDt4QWd9pmZ6n1R28HLOx6ZUE/ykaQcv5nxspkaGm5UdvJTz3JRKjptNO3gh\n57mZqkluUHbwMs62KYs0N5h28CLOtpkqeUqSJEmSpFAH4Aqcej+IurgegUvvp1A3lyNw7v0U6uZs\nA2SzAcLZAOFsgHA2QDgbINz5APwAfvZ+EnVxOgJveAASXYG3458vvAfkuQD8PQDeA/Kc4d8BsAHy\n2ADhbIBwNkA4GyCcDRDuDO//EgZuAxOduFsEuQ3McuU989ufAPAekOSW9f0B8B6Q45a1DZDJBghn\nA4SzAcLZAOFsgHC3rA9333QbmONEZRHkNjDDbQsI8wMA3gMSzDJ+PADeA8Y3y9gGyGMDhLMBwtkA\n4WyAcDZAuFnGh4cfug0c320LCMsGcBs4ttkWEJYHALwHjGyRbe0AeA8Y1yJbGyCLDRDOBghnA4Sz\nAcLZAOEW2T5uAsFt4MhmW0CoN4DbwDEttoBQPwDgPWBE1UxbB8B7wHiqmdoAOWyAcDZAOBsgnA0Q\nzgYIV820tgkEt4EjWmwBod0AbgPHUt0CQvsAgPeAkTSzXDsA3gPG0czSBshgA4SzAcLZAOFsgHA2\nQLhmlq1NILgNHEl1CwjrDeA2cAzNLSCsHwDwHjCC1Qz/dwC8B7y+1QxtgPHZAOFsgHA2QDgbIJwN\nEG41w7VNILgNHEFzCyhJkiRJkrL8Bo92etvl8h4FAAAAAElFTkSuQmCC\n", android.util.Base64.DEFAULT);
		FolderBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		data = android.util.Base64.decode("iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAAABHNCSVQICAgIfAhkiAAAAwJJREFU\neJzt3UFOVEEURuGjMTAy7kPBjbgxRxqI4siNiLoJxbHRHQAjA8GBmhAiXdD14Fb1f76kpkVb9+R1\n7Bo8kCRJkiRJqrML7AFHwClwUbxaXvb9c/XPNnAAnFM/9NsEcIERdNsGPlM/7HUDMIJO76gfdG8A\nRrCmXcZ77K8bgBGsYY/6IS8ZgBHc0hH1Q146ACO4hRPqh3wXARjBDVUP+C4DMIIbqB7wXQdgBA3V\nA76PAIxgheoB31cARnCN6gHfZwBG8B/VA77vAIzgiuoBVwRgBJdUD7gqgI2N4Op9fkv1gCsD2KgI\nrrvPb6kecHUAGxHBqvv8luoBjxDA9BGsus9vqR7wKAFMG0HrPr+lesAjBTBlBK37/JbqAY8WwHQR\ntO7zW6oHPGIAU0XQus9vqR7wqAFME8HIBzh7AFNEMPoBzh7A8BHMcICzBzB0BLMc4OwBDBvBTAc4\newBDRjDbAc4ewHARzHiAswcwVASzHuDsASwSwaPeDcSD6g/Q42H1B1AtAwhnAOEMIJwBhDOAcAYQ\nzt8B2m7yY1Clrt8hfAKEM4BwBhDOAMIZQDgDCGcA4QwgnAGEM4BwBhDOAMIZQDgDCGcA4QwgnAGE\nM4BwBhDOAMIZQDgDCGcA4QwgnAGEM4BwBhDOAMIZQDgDCGcA4QwgnAGEM4BwBhDOAMIZQDgDCGcA\n4QwgnAGEM4BwBhDOAMIZQDgDCGcA4Qxgbse9GxjA3H70bmAAc/vQu8ESrz1tvVKl9TdGfyXLqM6B\n58C3nk18AszrgM7hg0+AWX0EXgC/ejfyCTCXc2CfhYYPvjZuBqfAd+AQeA8cLbn5CAEs8TWkNfkV\nEM4AwhlAOAMIZwDhDCCcAYRb4v/gx8DjBfZJdAw8qfwASzwBfi6wR6ru+/xeSwRwuMAeqbrv80ew\nA5zx51bPdfN1Bjxd47yH9Jb6A51t7a910oPaAj5Rf6izrMO/Z7ZRtoA3+HWwap0Be2zg8C/bAV4D\nX4ET6g+9ep0AX4BXwLOOc5UkSZIkSeryG7ei65jwyWVaAAAAAElFTkSuQmCC\n", android.util.Base64.DEFAULT);
		FileBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		data = null;
		FolderDrawable = new BitmapDrawable(FolderBitmap);
		UI.tintDrawable(FolderDrawable, UI.getThemeColor());
		FileDrawable = new BitmapDrawable(FileBitmap);
		UI.tintDrawable(FileDrawable, UI.getThemeColor());
		UI.registThemeChangedListener(FileChooserFragment.class, new UI.OnThemeChangeListener() {
				@Override
				public void onThemeChange(String key) {
					if (!key.equals(UI.THEME_UI_COLOR)) return;
					UI.tintDrawable(FolderDrawable, UI.getThemeColor());
					UI.tintDrawable(FileDrawable, UI.getThemeColor());
				}
			});
		IconSize = UI.dp2px(24);
		RightMargin = UI.dp2px(8);
		System.gc();
	}
	public void setLastText(CharSequence cs) {
		last.setText(cs);
	}
	public void setChooseDirText(CharSequence cs) {
		ok.setText(cs);
	}
	public CharSequence getLastText() {
		return last.getText();
	}
	public CharSequence getChooseDirText() {
		return ok.getText();
	}
	private VListView list;
	private Button ok,last;
	private File dir;
	private LinearLayout layout,buttonlayout;
	private boolean chooseDir;
	public static final FileFilter ALL_FILTER=new FileFilter() {
		@Override
		public boolean accept(File f) {
			return true;
		}
	};
	FileChooserListener listener;
	public FileChooserFragment(Context cx, File f, FileChooserListener listener) {
		this(cx, f, listener, false);
	}
	public FileChooserFragment(Context cx, File f, FileChooserListener listener, boolean chooseDir) {
		this(cx, f, listener, chooseDir, ALL_FILTER);
	}
	public FileChooserFragment(Context cx, File f, FileChooserListener lis, boolean chooseDir, FileFilter filter) {
		super(cx);
		checkBitmap();
		this.dir = f;
		this.chooseDir = chooseDir;
		this.filter = filter;
		this.listener = lis;
		this.Refresh = new SwipeRefreshLayout(cx);
		Refresh.setColorSchemeColors(new int[] {UI.getThemeColor()});
		Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				@Override
				public void onRefresh() {
					setNow(getNow());
				}
			});
		list = new VListView(getContext());
		layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		path = new TextView(getContext());
		path.setGravity(Gravity.CENTER);
		layout.addView(path, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		LinearLayout.LayoutParams p=getDivideParams();
		p.height = 0;
		p.width = LinearLayout.LayoutParams.MATCH_PARENT;
		Refresh.addView(list, -1, -1);
		layout.addView(Refresh, p);
		buttonlayout = new LinearLayout(getContext());
		buttonlayout.setOrientation(LinearLayout.HORIZONTAL);
		layout.addView(buttonlayout);
		last = new Button(cx);
		last.setText("上一层");
		last.setBackgroundDrawable(null);
		last.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dir = dir.getParentFile();
					update();
				}
			});
		buttonlayout.addView(last, getDivideParams());
		if (chooseDir) {
			ok = new Button(cx);
			ok.setText("选择文件夹");
			ok.setBackgroundDrawable(null);
			ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (listener != null)
							listener.onChoose(dir);
						onDettach();
					}
				});
			buttonlayout.addView(ok, getDivideParams());
		}
		adapter = new FileAdapter(getContext(), ds);
		list.setAdapter(adapter);
		update();
		list.setOnItemClickListener(this);
		Refresh.post(new Runnable() {
			@Override
			public void run() {
				Refresh.setRefreshing(false);
				path.setText(dir.getPath());
			}
		});
	}
	FileFilter filter;
	public File getNow() {
		return dir;
	}
	public void setNow(File f) {
		dir = f;
		update();
	}
	private void update() {
		if (list == null) return;
		Refresh.setRefreshing(true);
		new Thread(new Runnable() {
				@Override
				public void run() {
					ds.clear();
					File[] fs=dir.listFiles();
					if (fs == null) fs = new File[] {};
					int dq=0;
					for (File one : fs) if (one.isDirectory()) dq++;
					String[] tmpp=new String[dq];
					String[] tmp=new String[fs.length - dq];
					int p1=0,p2=0;
					for (int i=0;i < fs.length;i++) if (fs[i].isDirectory()) tmpp[p1++] = fs[i].getName(); else tmp[p2++] = fs[i].getName();
					Arrays.sort(tmpp);
					Arrays.sort(tmp);
					for (int i=0;i < tmpp.length;i++) fs[i] = new File(dir, tmpp[i]);
					for (int i=0;i < tmp.length;i++) fs[dq + i] = new File(dir, tmp[i]);
					for (File one : fs) ds.add(one);
					list.post(new Runnable() {
							@Override
							public void run() {
								path.setText(dir.getPath());
								adapter.notifyDataSetChanged();
								Refresh.setRefreshing(false);
							}
						});
				}
			}).start();
	}
	FileAdapter adapter;
	@Override
	public View getView() {
		return layout;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		File f=ds.get(pos);
		if (f.isDirectory()) {
			dir = f;
			update();
		} else {
			if (listener != null)
				listener.onChoose(f);
			onDettach();
		}
	}
	TextView path;
	static LinearLayout.LayoutParams getDivideParams() {
		LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		p.weight = 1f;
		return p;
	}
	ArrayList<File> ds=new ArrayList<>();
	static class FileAdapter extends ArrayAdapter<File> {
		static int pa=-1;
		public FileAdapter(Context cx, ArrayList<File> data) {
			super(cx, android.R.layout.simple_list_item_1, data);
			if (pa == -1)
				pa = UI.dp2px(15);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			File f=getItem(position);
			TextView t=new TextView(getContext());
			t.setText(f.getName());
			t.setGravity(Gravity.CENTER);
			t.setTextColor(Color.BLACK);
			ImageView iv=new ImageView(getContext());
			iv.setImageDrawable(f.isDirectory() ?FolderDrawable: FileDrawable);
			LinearLayout.LayoutParams para=new LinearLayout.LayoutParams(IconSize, IconSize);
			para.rightMargin = RightMargin;
			iv.setLayoutParams(para);
			LinearLayout layout=new LinearLayout(getContext());
			layout.setPadding(pa, pa, pa, pa);
			layout.addView(iv);
			layout.addView(t);
			return layout;
		}
	}
	@Override
	public Object getTag() {return "VFileChooser";}
	public static interface FileChooserListener {
		void onChoose(File f)
	}
}
