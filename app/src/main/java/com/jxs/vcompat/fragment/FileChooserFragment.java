package com.jxs.vcompat.fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jxs.vcompat.ui.UI;
import com.jxs.vcompat.widget.VListView;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;

public class FileChooserFragment extends VFragment implements OnItemClickListener {
	VListView list;
	Button ok,last;
	File dir;
	LinearLayout layout,buttonlayout;
	boolean chooseDir;
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
	public FileChooserFragment(Context cx, File f, FileChooserListener listener, boolean chooseDir, FileFilter filter) {
		super(cx);
		this.dir = f;
		this.chooseDir = chooseDir;
		this.filter = filter;
		this.listener = listener;
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
		path.setText(dir.getPath());
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
		adapter.notifyDataSetChanged();
	}
	FileAdapter adapter;
	@Override
	public View getView() {
		list = new VListView(getContext());
		layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		path = new TextView(getContext());
		path.setGravity(Gravity.CENTER);
		layout.addView(path, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		LinearLayout.LayoutParams p=getDivideParams();
		p.height = 0;
		p.width = LinearLayout.LayoutParams.MATCH_PARENT;
		layout.addView(list, p);
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
				pa = new UI(getContext()).dp2px(15);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView t=new TextView(getContext());
			t.setText(getShow(getItem(position)));
			t.setTextColor(Color.BLACK);
			LinearLayout layout=new LinearLayout(getContext());
			layout.setPadding(pa, pa, pa, pa);
			layout.addView(t);
			return layout;
		}
		public static String getShow(File f) {
			if (f.isDirectory()) {
				return "💼" + f.getName();
			} else {
				return "💾" + f.getName();
			}
		}
	}
	@Override
	public Object getTag() {return "VFileChooser";}
	public static interface FileChooserListener {
		void onChoose(File f)
	}
}
