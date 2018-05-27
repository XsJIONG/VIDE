package com.jxs.vide;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.jxs.vcompat.activity.*;
import com.jxs.vcompat.ui.*;
import com.jxs.vcompat.widget.*;
import java.io.*;
import java.util.*;

public class ProjectViewHelper {
	public static HashMap<File,Bitmap> BS=new HashMap<>();
	private VActivity cx;
	private PAdapter adapter;
	private VScrollView Scroller;
	private CryView Cry;
	private FrameLayout Root;
	private GridLayout con;
	private int columeCount;
	public static void recycle() {
		Collection<Bitmap> a=BS.values();
		for (Bitmap one : a) one.recycle();
		BS.clear();
	}
	public void onThemeChange() {
		Cry.onThemeChange(UI.THEME_UI_COLOR);
	}
	public ProjectViewHelper(VActivity ac) {
		this.cx = ac;
		Scroller = new VScrollView(cx);
		Scroller.setFillViewport(true);
		con = new GridLayout(cx);
		Scroller.addView(con, new VScrollView.LayoutParams(-1, -1));
		adapter = new PAdapter(cx, con);
		columeCount = cx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ?3: 5;
		adapter.setColumeCount(columeCount);
		Root = new FrameLayout(cx);
		Root.addView(Scroller, new FrameLayout.LayoutParams(-1, -1));
		Cry = new CryView(cx);
		Cry.setText(L.get(L.NoProject));
		Cry.setVisibility(View.GONE);
		Root.addView(Cry, new FrameLayout.LayoutParams(-1, -1));
		loadData();
	}
	public void loadData() {
		Scroller.setVisibility(View.VISIBLE);
		Cry.setVisibility(View.GONE);
		try {
			adapter.removeAll();
			File[] fs=Project.listFiles();
			boolean find=false;
			for (File one : fs) if (Project.isProject(one)) {
					find = true;
					adapter.add(Project.getInstance(one));
				}
			if (!find) {
				Scroller.setVisibility(View.GONE);
				Cry.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			cx.err(e, true);
		}
	}
	public View getView() {
		return Root;
	}
	private OnProjectClickListener _Listener;
	public void setOnProjectClickListener(OnProjectClickListener listener) {
		this._Listener = listener;
		adapter.setOnItemClickListener(_Listener);
	}
	public OnProjectClickListener getOnProjectClickListener() {
		return _Listener;
	}
	public static interface OnProjectClickListener {
		void onClick(Project pro, View view);
	}
	public static class PAdapter {
		public Vector<Project> f;
		public GridLayout layout;
		private VActivity cx;
		public PAdapter(VActivity cx, GridLayout layout) {
			this.layout = layout;
			this.cx = cx;
			f = new Vector<>();
		}
		private OnProjectClickListener mOnItemClickListener=null;
		public void setOnItemClickListener(OnProjectClickListener li) {
			mOnItemClickListener = li;
		}
		public void add(Project pro) {
			f.add(pro);
			layout.addView(getView(f.size() - 1));
		}
		public void remove(int pos) {
			f.remove(pos);
			layout.removeViewAt(pos);
		}
		public void removeAll() {
			int s=f.size();
			for (int i=0;i < s;i++) {
				remove(0);
			}
		}
		static int DP=-1;
		public View getView(final int index) {
			final Project f=this.f.get(index);
			final FrameLayout ff=new FrameLayout(layout.getContext());
			ff.setBackgroundColor(ColorPalette.getRandom());
			getIcon(f, new BitmapListener() {
					@Override
					public void onGet(final Bitmap b) {
						((Activity) layout.getContext()).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								ff.setBackground(new BitmapDrawable(b));
							}
						});
					}
			});
			TextView name=new TextView(layout.getContext());
			name.setBackgroundColor(0x40000000);
			name.setTextSize(20);
			name.setTextColor(Color.WHITE);
			name.setSingleLine(true);
			int d=UI.dp2px(5);
			name.setPadding(d, d, d, d);
			FrameLayout.LayoutParams para=new FrameLayout.LayoutParams(-1, -2);
			para.gravity = Gravity.BOTTOM;
			ff.addView(name, para);
			LinearLayout root=new LinearLayout(layout.getContext());
			if (DP == -1) DP = new UI(layout.getContext()).dp2px(20);
			CardView c=new CardView(layout.getContext());
			root.addView(c);
			c.addView(ff);
			c.setCardElevation(10f);
			c.setTranslationZ(20f);
			name.setText(f.getName());
			ff.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mOnItemClickListener != null) mOnItemClickListener.onClick(f, ff);
					}
				});
			GridLayout.LayoutParams p=new GridLayout.LayoutParams();
			p.width = ((Activity) layout.getContext()).getWindowManager().getDefaultDisplay().getWidth() / layout.getColumnCount();
			p.height = p.width;
			root.setLayoutParams(p);
			LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(p.width - DP, p.width - DP);
			pp.topMargin = DP / 2;
			pp.leftMargin = DP / 2;
			c.setLayoutParams(pp);
			return root;
		}
		public Project getProject(int pos) {
			return f.get(pos);
		}
		public void setColumeCount(int count) {
			layout.setColumnCount(count);
		}
		public boolean contains(File pro) {
			return f.contains(pro);
		}
		public void getIcon(final Project pro, final BitmapListener l) {
			Bitmap b;
			if ((b = BS.get(pro.getDir())) != null) l.onGet(b); else
				new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								int w=cx.getResources().getDisplayMetrics().widthPixels / layout.getColumnCount();
								BitmapFactory.Options op=new BitmapFactory.Options();
								op.inJustDecodeBounds = true;
								FileInputStream in=new FileInputStream(pro.getIcon());
								Bitmap RawIcon=BitmapFactory.decodeStream(in);
								in.close();
								long S=RawIcon.getWidth() * RawIcon.getHeight();
								long mem=Runtime.getRuntime().freeMemory() / 2;
								if (S > mem) {
									int b=(int) Math.ceil((float) S / mem);
									op.inSampleSize = b;
								}
								op.inJustDecodeBounds = false;
								in = new FileInputStream(pro.getIcon());
								RawIcon = BitmapFactory.decodeStream(in);
								in.close();
								Bitmap fixed=Bitmap.createScaledBitmap(RawIcon, w, w, true);
								RawIcon.recycle();
								if (BS.containsKey(pro.getDir())) BS.get(pro.getDir()).recycle();
								BS.put(pro.getDir(), fixed);
								l.onGet(fixed);
							} catch (Throwable t) {}
						}
					}).start();
		}
		
	}
	public void getIcon(Project pro, BitmapListener l) {
		adapter.getIcon(pro, l);
	}
	public void notifyProjectRename() {
		adapter.removeAll();
		loadData();
	}
	public static interface BitmapListener {
		void onGet(Bitmap b);
	}
}
