package com.jxs.vide;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jxs.v.ui.VAlertDialog;
import com.jxs.vcompat.ui.UI;
import com.jxs.vcompat.widget.VListView;
import java.util.ArrayList;

public class ThreadDialog extends FloatingWindow {
	private static ThreadDialog _Instance;
	public static ThreadDialog getInstance() {
		if (_Instance == null) _Instance = new ThreadDialog();
		return _Instance;
	}
	private VListView list;
	private SwipeRefreshLayout Refresh;
	private ThreadAdapter adapter;
	private Tree root,now;
	private LinearLayout layout;
	private Button last;
	private ThreadDialog() {
		setBounds(UI.dp2px(100), UI.dp2px(100), UI.dp2px(200), UI.dp2px(250));
		list = new VListView(getContext());
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
				Node n=now.child.get(pos);
				if (n instanceof Tree) {
					now = (Tree) n;
					update();
				} else showThreadDialog(n.thread);
			}
		});
		root = new Tree(L.get(L.ThreadDialogTitle));
		layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		Refresh = new SwipeRefreshLayout(getContext());
		Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				Refresh.setRefreshing(true);
				loadData();
				Refresh.setRefreshing(false);
			}
		});
		layout.setBackgroundColor(Color.WHITE);
		last = new Button(getContext());
		last.setText(L.get(L.Last));
		last.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				now = now.parent;
				update();
			}
		});
		adapter = new ThreadAdapter();
		list.setAdapter(adapter);
		layout.addView(last);
		layout.addView(Refresh);
		Refresh.addView(list);
		Refresh.setColorSchemeColors(new int[] {UI.getThemeColor()});
		setView(layout);
		loadData();
	}
	@Override
	public void onThemeChange(String key) {
		super.onThemeChange(key);
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		list.setGlowColor(UI.getThemeColor());
	}
	public static class ThreadAdapter extends BaseAdapter {
		private ArrayList<Node> nodes=new ArrayList<>();
		@Override
		public int getCount() {
			return nodes.size();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout root=new LinearLayout(parent.getContext());
			int dp=UI.dp2px(10);
			root.setPadding(dp, dp, dp, dp);
			TextView v=new TextView(parent.getContext());
			v.setTextColor(Color.BLACK);
			v.setText(Node2String(nodes.get(position)));
			root.addView(v);
			return root;
		}
		public String Node2String(Node n) {
			if (n instanceof Tree) return n.data + " > "; else return n.data;
		}
		@Override
		public long getItemId(int pos) {
			return pos;
		}
		@Override
		public Object getItem(int pos) {
			return nodes.get(pos);
		}
		public void clear() {
			nodes.clear();
		}
		public void add(Node node) {
			nodes.add(node);
		}
	}
	private void loadData() {
		ThreadGroup no=Thread.currentThread().getThreadGroup();
		root.child.clear();
		while (no.getParent() != null) no = no.getParent();
		loadThreadGroup(root, no);
		now = root;
		update();
	}
	public void update() {
		if (now.parent == null) last.setVisibility(View.GONE); else last.setVisibility(View.VISIBLE);
		setTitle(now.data);
		adapter.clear();
		for (Node one : now.child)
			adapter.add(one);
		adapter.notifyDataSetChanged();
	}
	private void loadThreadGroup(Tree current, ThreadGroup group) {
		Thread[] all=new Thread[group.activeCount()];
		group.enumerate(all);
		Tree node;
		Node n;
		for (final Thread one : all) {
			n = new Node(one.getName());
			n.thread = one;
			n.parent = current;
			current.child.add(n);
		}
		ThreadGroup[] q=new ThreadGroup[group.activeGroupCount()];
		group.enumerate(q);
		for (ThreadGroup one : q) {
			node = new Tree(one.getName());
			node.parent = current;
			current.child.add(node);
			loadThreadGroup(node, one);
		}
	}
	public void showThreadDialog(Thread t) {
		VAlertDialog b=new VAlertDialog(getContext());
		b.setTitle(UI.getColorString(t.getName(), Color.BLACK));
		StringBuffer q=new StringBuffer();
		for (StackTraceElement one : t.getStackTrace())
			q.append("at ").append(one.getClassName()).append(".").append(one.getMethodName()).append("(").append(one.getFileName()).append(":").append(one.getLineNumber()).append(")\n");
		b.setMessage(UI.getColorString(String.format(L.get(L.ThreadInfo), t.getName(), t.getId(), t.getPriority(), q.toString()), Color.BLACK));
		AlertDialog dialog=b.setCancelable(true).setPositiveButton(L.get(L.OK), null).getDialog();
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		dialog.show();
	}
	public static class Node {
		public Tree parent;
		public String data;
		public Thread thread;
		public Node() {}
		public Node(String data) {
			this.data = data;
		}
	}
	public static class Tree extends Node {
		public ArrayList<Node> child=new ArrayList<>();
		public Tree() {}
		public Tree(String data) {
			super(data);
		}
	}
}
