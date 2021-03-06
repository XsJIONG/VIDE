package com.jxs.vide;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.jxs.vcompat.fragment.*;
import com.jxs.vcompat.ui.*;
import com.jxs.vcompat.widget.*;
import java.util.*;

import android.view.View.OnClickListener;
import android.support.v4.widget.*;

public class MethodFragment extends VFragment implements OnItemClickListener {
	private LinearLayout ButtonLayout;
	private Button Pre,ChoosePkg;
	private VListView List;
	private MethodAdapter Adapter;
	private LinearLayout Root;
	private ChooseListener Listener;
	private VEditText Search;
	private SwipeRefreshLayout Refresh;
	public MethodFragment(Context cx) {
		super(cx);
		Search = new VEditText(cx);
		Search.setHint(L.get(L.Search));
		Search.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence cs, int st, int en, int ch) {}
				@Override
				public void onTextChanged(CharSequence cs, int st, int en, int ch) {
					Adapter.setFilter(Search.getText().toString().trim());
				}
				@Override
				public void afterTextChanged(Editable ed) {}
			});
		Root = new LinearLayout(cx);
		Root.addView(Search, -1, -2);
		Root.setOrientation(LinearLayout.VERTICAL);
		ButtonLayout = new LinearLayout(cx);
		ButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
		Pre = new Button(cx);
		Pre.setBackgroundDrawable(null);
		Pre.setText(L.get(L.Last));
		Pre.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Adapter.backToPre();
				}
			});
		Refresh=new SwipeRefreshLayout(cx);
		Refresh.setColorSchemeColors(new int[] {UI.getThemeColor()});
		List = new VListView(cx);
		Adapter = new MethodAdapter(cx, Global.Q, Pre);
		List.setAdapter(Adapter);
		List.setOnItemClickListener(this);
		ChoosePkg = new Button(cx);
		ChoosePkg.setText(L.get(L.ChoosePkg));
		ChoosePkg.setBackgroundDrawable(null);
		ChoosePkg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (Listener != null) Listener.onChoose(Adapter.getCurrent());
				}
			});
		LinearLayout.LayoutParams para=new LinearLayout.LayoutParams(0, -2);
		para.weight = 1f;
		ButtonLayout.addView(Pre, para);
		ButtonLayout.addView(ChoosePkg, para);
		ChoosePkg.setVisibility(isChoosePkg() ?View.VISIBLE: View.GONE);
		para = new LinearLayout.LayoutParams(-1, 0);
		para.weight = 1;
		Refresh.addView(List, -1, -1);
		Root.addView(Refresh, para);
		para = new LinearLayout.LayoutParams(-1, -2);
		Root.addView(ButtonLayout, para);
		Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				setCurrent(getCurrent());
			}
		});
		setCurrent(Global.Q);
		Refresh.post(new Runnable() {
			@Override
			public void run() {
				Refresh.setRefreshing(false);
			}
		});
	}
	public void backToPre() {
		Adapter.backToPre();
	}
	public void setCurrent(CNode node) {
		Refresh.setRefreshing(true);
		Adapter.directSetCurrent(node);
		new Thread(new Runnable() {
				@Override
				public void run() {
					Adapter.reloadData();
					Root.post(new Runnable() {
							@Override
							public void run() {
								Adapter.notifyDataSetChanged();
								Pre.setVisibility(Adapter.getCurrent() == Adapter.Root ?View.GONE: View.VISIBLE);
								Refresh.setRefreshing(false);
							}
						});
				}
			}).start();
	}
	public CNode getCurrent() {
		return Adapter.getCurrent();
	}
	public void setChooseListener(ChooseListener l) {
		this.Listener = l;
	}
	public ChooseListener getChooseListener() {
		return this.Listener;
	}
	public void setLastText(CharSequence cs) {
		Pre.setText(cs);
	}
	public void setChoosePkgText(CharSequence cs) {
		ChoosePkg.setText(cs);
	}
	public CharSequence getLastText() {
		return Pre.getText();
	}
	public CharSequence getChoosePkgText() {
		return ChoosePkg.getText();
	}
	public boolean isChoosePkg() {
		return Adapter.isChoosePkg;
	}
	public void setChoosePkg(boolean b) {
		ChoosePkg.setVisibility(b ?View.VISIBLE: View.GONE);
		Adapter.setChoosePkg(b);
	}
	@Override
	public View getView() {
		return Root;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		CNode c=Adapter.getItem(pos);
		if (!c.isLeaf()) {
			setCurrent(c);
			return;
		}
		if (Listener != null) Listener.onChoose(c);
	}
	public static interface ChooseListener {
		void onChoose(CNode node)
	}
	@Override
	public Object getTag() {return "MethodFragment";}
	private static class MethodAdapter extends BaseAdapter {
		private static int Padding=-1;
		private CNode Current,Root;
		private ArrayList<CNode> Temp=new ArrayList<>();
		private ArrayList<CNode> ForShow=new ArrayList<>();
		private Context cx;
		private Button pre;
		public MethodAdapter(Context cx, CNode root, Button pre) {
			this.pre = pre;
			this.cx = cx;
			Root = root;
			if (Padding == -1) Padding = UI.dp2px(15);
			FileChooserFragment.checkBitmap();
		}
		private String Filter=null;
		public void setFilter(String f) {
			this.Filter = f;
			ForShow.clear();
			if (f == null) {
				ForShow.addAll(Temp);
				return;
			}
			f = f.toUpperCase();
			for (int i=0;i < Temp.size();i++) if (Temp.get(i).getData().toUpperCase().contains(f)) ForShow.add(Temp.get(i));
			notifyDataSetChanged();
		}
		public CNode getCurrent() {
			return Current;
		}
		public void backToPre() {
			setCurrent(Current.getParent());
		}
		public void setCurrent(CNode c) {
			if (c == null) return;
			Current = c;
			pre.setVisibility(Current == Root ?View.GONE: View.VISIBLE);
			refreshShowList();
		}
		public void directSetCurrent(CNode c) {
			this.Current = c;
		}
		public void reloadData() {
			Temp.clear();
			ForShow.clear();
			for (CNode one : Current.getSon().values()) if ((isChoosePkg && !one.isLeaf()) || !isChoosePkg) Temp.add(one);
			Collections.sort(Temp, new Comparator<CNode>() {
					@Override
					public int compare(CNode a, CNode b) {
						return a.getData().compareTo(b.getData());
					}
				});
			ForShow.addAll(Temp);
			setFilter(Filter);
		}
		public void refreshShowList() {
			reloadData();
			notifyDataSetChanged();
		}
		private boolean isChoosePkg=false;
		public boolean isChoosePkg() {
			return isChoosePkg;
		}
		public void setChoosePkg(boolean b) {
			isChoosePkg = b;
			refreshShowList();
		}
		@Override
		public int getCount() {
			return ForShow.size();
		}
		@Override
		public CNode getItem(int q) {
			return ForShow.get(q);
		}
		@Override
		public long getItemId(int pos) {
			return pos;
		}
		@Override
		public View getView(int pos, View v, ViewGroup group) {
			CNode node=getItem(pos);
			TextView t=new TextView(cx);
			t.setText(node.getData());
			t.setGravity(Gravity.CENTER);
			t.setTextColor(Color.BLACK);
			ImageView iv=new ImageView(cx);
			iv.setImageDrawable(node.isLeaf() ?DrawableHelper.getDrawable(R.drawable.icon_class, UI.getThemeColor()): FileChooserFragment.FolderDrawable);
			LinearLayout.LayoutParams para=new LinearLayout.LayoutParams(FileChooserFragment.IconSize, FileChooserFragment.IconSize);
			para.rightMargin = FileChooserFragment.RightMargin;
			iv.setLayoutParams(para);
			LinearLayout layout=new LinearLayout(cx);
			layout.setPadding(Padding, Padding, Padding, Padding);
			layout.addView(iv);
			layout.addView(t);
			return layout;
		}
	}
}
