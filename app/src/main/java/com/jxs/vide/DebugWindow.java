package com.jxs.vide;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.support.v4.widget.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.jxs.v.ui.*;
import com.jxs.vapp.program.*;
import com.jxs.vcompat.ui.*;
import com.jxs.vcompat.widget.*;
import java.util.*;
import org.mozilla.javascript.*;

import com.jxs.v.ui.VAlertDialog;
import com.jxs.vcompat.ui.UI;
import org.mozilla.javascript.Context;

import static com.jxs.vide.L.get;

public class DebugWindow extends FloatingWindow implements Runnable {
	private static DebugWindow _Instance;
	public static DebugWindow getInstance() {
		if (_Instance == null) _Instance = new DebugWindow();
		return _Instance;
	}
	public static boolean hasInstance() {
		return _Instance == null;
	}
	public static void clearInstance() {
		_Instance = null;
	}
	private VListView list;
	private JsProgramAdapter adapter;
	private DebugWindow() {
		super(get(L.Debug));
		setBounds(UI.dp2px(100), UI.dp2px(100), UI.dp2px(200), UI.dp2px(250));
		list = new VListView(getContext());
		list.setBackgroundColor(Color.WHITE);
		adapter = new JsProgramAdapter();
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
					CmdWindow.getInstance(JsProgram.download(parent.getItemAtPosition(pos))).show();
				}
			});
		run();
		JsProgram.addUpdateListener(this);
		setView(list);
	}
	@Override
	public void onThemeChange(String key) {
		super.onThemeChange(key);
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		list.setGlowColor(UI.getThemeColor());
	}
	public static class JsProgramAdapter extends BaseAdapter {
		private ArrayList<Object> nodes=new ArrayList<>();
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
			v.setText(nodes.get(position).toString());
			root.addView(v);
			return root;
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
		public void add(Object node) {
			nodes.add(node);
		}
	}
	@Override
	public void run() {
		adapter.clear();
		for (Object one : JsProgram.Internet.keySet())
			adapter.add(one);
		adapter.notifyDataSetChanged();
	}
	public static class CmdWindow extends FloatingWindow implements OnClickListener {
		public static ArrayList<CmdWindow> Windows=new ArrayList<>();
		private JsProgram program;
		private LinearLayout root;
		private VScrollView sc;
		private Button eval,variable;
		public static CmdWindow getInstance(JsProgram program) {
			for (CmdWindow one : Windows) if (one.program == program) return one;
			return new CmdWindow(program);
		}
		private CmdWindow(JsProgram program) {
			super(program.getKey().toString());
			Windows.add(this);
			this.program = program;
			setBounds(UI.dp2px(100), UI.dp2px(100), UI.dp2px(250), UI.dp2px(200));
			root = new LinearLayout(getContext());
			root.setOrientation(LinearLayout.VERTICAL);
			eval = new Button(getContext());
			eval.setText(get(L.Debugger_Evaluate));
			eval.setTag(0);
			eval.setOnClickListener(this);
			variable = new Button(getContext());
			variable.setText(get(L.Debugger_Variable));
			variable.setTag(1);
			variable.setOnClickListener(this);
			root.addView(eval);
			root.addView(variable);
			root.setBackgroundColor(Color.WHITE);
			sc = new VScrollView(getContext());
			sc.setFillViewport(true);
			sc.addView(root, new VScrollView.LayoutParams(-1, -1));
			setView(sc);
		}
		public void destroy() {
			hide();
		}
		@Override
		public void onThemeChange(String key) {
			super.onThemeChange(key);
			if (!key.equals(UI.THEME_UI_COLOR)) return;
			sc.setGlowColor(UI.getThemeColor());
		}
		private static final boolean inCurrentThread(Context cx) {
			return Context.getCurrentContext() == cx;
		}
		@Override
		public void onClick(View v) {
			switch ((int) v.getTag()) {
				case 0:{
						if (!inCurrentThread(program.getContext())) {
							MessageDialog.showMessage(get(L.Warning), get(L.Debugger_NotInCurrentThread));
							return;
						}
						AlertDialog.Builder b=new AlertDialog.Builder(getContext()).setTitle(UI.getColorString(get(L.Debugger_Evaluate), Color.BLACK));
						final TextEditor editor=new TextEditor(getContext());
						editor.setTypeface(Typeface.MONOSPACE);
						editor.setText("");
						editor.getAutoCompletePanel().setTextColor(Color.WHITE);
						b.setView(editor).setPositiveButton(get(L.Debugger_EvalCode), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int pos) {
									Script s=null;
									try {
										s = program.getContext().compileString(editor.getText().toString(), "Debugger", 1, null);
									} catch (Throwable t) {
										MessageDialog.showMessage(get(L.Debugger_CompileErr), Log.getStackTraceString(t));
										return;
									}
									try {
										s.exec(program.getContext(), program.getScriptableObject());
									} catch (Throwable t) {
										MessageDialog.showMessage(get(L.Debugger_ExecErr), Log.getStackTraceString(t));
										return;
									}
								}
							}).setNegativeButton(get(L.Cancel), null).setCancelable(true);
						AlertDialog d=b.create();
						d.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
						d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
						d.getWindow().setTitleColor(Color.BLACK);
						d.show();
						hide();
						break;
					}
				case 1:{
						VariableDialog.getInstance(program).show();
						hide();
						break;
					}
			}
		}
	}
	public static void systemDialog(android.content.Context cx, String title, String msg) {
		new VAlertDialog(cx).setTitle(title).setMessage(msg).setPositiveButton(get(L.OK), null).setCancelable(true).show();
	}
	public static class VariableDialog extends FloatingWindow {
		private static ArrayList<VariableDialog> Windows=new ArrayList<>();
		private JsProgram program;
		public static VariableDialog getInstance(JsProgram program) {
			for (VariableDialog one : Windows) if (one.program == program) return one;
			return new VariableDialog(program);
		}
		private Scriptable current;
		private VListView list;
		private SwipeRefreshLayout refresh;
		private String currentId=null;
		private Button last;
		private LinearLayout root;
		private ScriptableAdapter adapter;
		private TextView state;
		private VariableDialog(JsProgram program) {
			super(get(L.Debugger_Variable));
			setBounds(UI.dp2px(100), UI.dp2px(100), UI.dp2px(200), UI.dp2px(250));
			this.program = program;
			current = program.getScriptableObject();
			root = new LinearLayout(getContext());
			root.setOrientation(LinearLayout.VERTICAL);
			refresh = new SwipeRefreshLayout(getContext());
			root.setBackgroundColor(Color.WHITE);
			refresh.setColorSchemeColors(new int[] {UI.getThemeColor()});
			last = new Button(getContext());
			last.setText(get(L.Last));
			last.setVisibility(View.GONE);
			state = new TextView(getContext());
			state.setTextColor(UI.getThemeColor());
			state.setGravity(Gravity.CENTER);
			clearState();
			root.addView(state);
			root.addView(last);
			root.addView(refresh);
			list = new VListView(getContext());
			adapter = new ScriptableAdapter();
			list.setAdapter(adapter);
			refresh.addView(list);
			refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						refresh.setRefreshing(true);
						loadData();
						refresh.setRefreshing(false);
					}
				});
			list.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
						Object key=adapter.getItem(pos);
						Object obj = current.get(key.toString(), current);
						if (obj instanceof Scriptable) {
							current = (Scriptable) obj;
							currentId = key.toString();
							loadData();
						} else {
							AlertDialog d=new AlertDialog.Builder(getContext())
								.setTitle(Context.toString(obj))
								.setMessage(getDescribe(adapter.getItem(pos), obj))
								.setCancelable(true)
								.setPositiveButton(get(L.OK), null)
								.create();
							d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
							d.show();
						}
					}
				});
			setView(root);
			last.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						current = current.getParentScope();
						loadData();
					}
				});
			loadData();
		}
		@Override
		public void onThemeChange(String key) {
			super.onThemeChange(key);
			if (!key.equals(UI.THEME_UI_COLOR)) return;
			list.setGlowColor(UI.getThemeColor());
		}
		private void clearState() {
			state.setText("");
			state.setVisibility(View.GONE);
		}
		private void setState(String s) {
			state.setText(s);
			state.setVisibility(View.VISIBLE);
		}
		private void loadData() {
			adapter.clear();
			adapter.addAll(current.getIds());
			setState(current.getClass().getName());
			setTitle(current.getParentScope() == null ?get(L.Debugger_Variable): currentId);
			adapter.notifyDataSetChanged();
			last.setVisibility(current.getParentScope() == null ?View.GONE: View.VISIBLE);
		}
	}
	public static final String getDescribe(Object name, Object s) {
		name = Context.toString(name);
		String q=get(L.Debugger_ScriptableInfo);
		while (true) {
			if (s == null)
				return String.format(q, name, "null", "null");
			if (Undefined.isUndefined(s))
				return String.format(q, name, "undefined", "undefined");
			if (s instanceof String)
				return String.format(q, name, "java.lang.String", s);
			if (s instanceof CharSequence)
				return String.format(q, name, "java.lang.CharSequence", ((CharSequence) s).toString());
			if (s instanceof Number)
				return String.format(q, name, s.getClass(), String.valueOf((Number) s));
			if (s instanceof NativeJavaObject) {
				s = ((NativeJavaObject) s).unwrap();
				continue;
			}
			if (s instanceof Scriptable) {
				s = ((Scriptable) s).getDefaultValue(String.class);
				continue;
			}
			return s.toString();
		}
	}
	public static class ScriptableAdapter extends BaseAdapter {
		private ArrayList<Object> keys=new ArrayList<>();
		@Override
		public int getCount() {
			return keys.size();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout root=new LinearLayout(parent.getContext());
			int dp=UI.dp2px(10);
			root.setPadding(dp, dp, dp, dp);
			TextView v=new TextView(parent.getContext());
			v.setTextColor(Color.BLACK);
			v.setText(Context.toString(keys.get(position)));
			root.addView(v);
			return root;
		}
		@Override
		public long getItemId(int pos) {
			return pos;
		}
		@Override
		public Object getItem(int pos) {
			return keys.get(pos);
		}
		public void clear() {
			keys.clear();
		}
		public void add(Object id) {
			if (UniqueTag.NOT_FOUND == id) return;
			keys.add(id);
		}
		public void addAll(Object[] objs) {
			for (Object one : objs) add(one);
		}
	}
}
