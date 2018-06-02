package com.myopicmobile.textwarrior.android;

import android.content.*;
import android.content.res.*;
import android.graphics.drawable.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.jxs.vide.*;
import com.myopicmobile.textwarrior.common.*;
import java.lang.reflect.*;
import java.util.*;

public class AutoCompletePanel {
	private FreeScrollingTextField _textField;
	private Context _context;
	private static Language _globalLanguage = LanguageNonProg.getInstance();
	private ListPopupWindow _autoCompletePanel;
	private MyAdapter _adapter;
	private Filter _filter;

	private int _verticalOffset;

	private int _height;

	private int _horizontal;

	private CharSequence _constraint;

	private int _backgroundColor;

	private GradientDrawable gd;

	private int _textColor;

	public AutoCompletePanel(FreeScrollingTextField textField) {
		_textField = textField;
		_context = textField.getContext();
		initAutoCompletePanel();

	}

	public void setTextColor(int color) {
		_textColor = color;
		//gd.setStroke(1, color);
		//_autoCompletePanel.setBackgroundDrawable(gd);
	}


	public void setBackgroundColor(int color) {
		_backgroundColor = color;
		gd.setColor(color);
		_autoCompletePanel.setBackgroundDrawable(gd);
	}

	public void setBackground(Drawable color) {
		_autoCompletePanel.setBackgroundDrawable(color);
	}

	private void initAutoCompletePanel() {
		_autoCompletePanel = new ListPopupWindow(_context);
		_autoCompletePanel.setAnchorView(_textField);
		_adapter = new MyAdapter(_context, android.R.layout.simple_list_item_1);
		_autoCompletePanel.setAdapter(_adapter);
		//_autoCompletePanel.setDropDownGravity(Gravity.BOTTOM | Gravity.LEFT);
		_filter = _adapter.getFilter();
		setHeight(300);
		TypedArray array = _context.getTheme().obtainStyledAttributes(new int[] {  
																		  android.R.attr.colorBackground, 
																		  android.R.attr.textColorPrimary, 
																	  }); 
		int backgroundColor = array.getColor(0, 0xFF00FF); 
		int textColor = array.getColor(1, 0xFF00FF); 
		array.recycle();
		gd = new GradientDrawable();
		gd.setColor(backgroundColor);
		gd.setCornerRadius(4);
		gd.setStroke(1, textColor);
		setTextColor(textColor);
		_autoCompletePanel.setBackgroundDrawable(gd);
		_autoCompletePanel.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
				// TODO: Implement this method
				_textField.replaceText(_textField.getCaretPosition() - _constraint.length(), _constraint.length(), (String) p2.getTag());
				_adapter.abort();
				dismiss();
			}
		});

	}

	public void setWidth(int width) {
		// TODO: Implement this method
		_autoCompletePanel.setWidth(width);
	}

	private void setHeight(int height) {
		// TODO: Implement this method

		if (_height != height) {
			_height = height;
			_autoCompletePanel.setHeight(height);
		}
	}

	private void setHorizontalOffset(int horizontal) {
		// TODO: Implement this method
		horizontal = Math.min(horizontal, _textField.getWidth() / 2);
		if (_horizontal != horizontal) {
			_horizontal = horizontal;
			_autoCompletePanel.setHorizontalOffset(horizontal);
		}
	}


	private void setVerticalOffset(int verticalOffset) {
		// TODO: Implement this method
		//verticalOffset=Math.min(verticalOffset,_textField.getWidth()/2);
		int max=0 - _autoCompletePanel.getHeight();
		if (verticalOffset > max) {
			_textField.scrollBy(0, verticalOffset - max);
			verticalOffset = max;
		}
		if (_verticalOffset != verticalOffset) {
			_verticalOffset = verticalOffset;
			_autoCompletePanel.setVerticalOffset(verticalOffset);
		}
	}

	public void update(CharSequence constraint) {
		_adapter.restart();
		_filter.filter(constraint);
	}

	public void show() {
		if (!_autoCompletePanel.isShowing())
			_autoCompletePanel.show();
		_autoCompletePanel.getListView().setFadingEdgeLength(0);
	}

	public void dismiss() {
		if (_autoCompletePanel.isShowing()) {
			_autoCompletePanel.dismiss();
		}
	}
	synchronized public static void setLanguage(Language lang) {
		_globalLanguage = lang;
	}

	synchronized public static Language getLanguage() {
		return _globalLanguage;
	}

	/**
	 * Adapter定义
	 */
	class MyAdapter extends ArrayAdapter<AutoCompleteView> implements Filterable {

		private int _h;
		private Flag _abort;

		private DisplayMetrics dm;

		public MyAdapter(Context context, int resource) {
			super(context, resource);
			_abort = new Flag();
			setNotifyOnChange(false);
			dm = context.getResources().getDisplayMetrics();
		}

		public void abort() {
			_abort.set();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getItem(position).setTextColor(_textColor).getView(getContext());
		}
		public void restart() {
			_abort.clear();
		}
		public int getItemHeight() {
			if (_h != 0)
				return _h;
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			TextView item = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, null);
			item.measure(0, 0);
			_h = item.getMeasuredHeight();
			return _h;
		}
		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					// 此处实现过滤
					// 过滤后利用FilterResults将过滤结果返回
					ArrayList<AutoCompleteView> buf = new ArrayList<>();
					String keyword = String.valueOf(constraint).toLowerCase();
					String[] ss=keyword.split("\\.");
					if (ss.length == 2) {
						String pkg=ss[0];
						keyword = ss[1];
						if (_globalLanguage.isBasePackage(pkg)) {
							String[] keywords=_globalLanguage.getBasePackage(pkg);
							for (String k:keywords) {
								if (k.toLowerCase().startsWith(keyword))
									buf.add(new AutoCompleteView(AutoCompleteView.TYPE_KEYWORD, k));
							}
						}
					} else if (ss.length == 1) {
						if (keyword.charAt(keyword.length() - 1) == '.') {
							String pkg=keyword.substring(0, keyword.length() - 1);
							keyword = "";
							if (_globalLanguage.isBasePackage(pkg)) {
								String[] keywords=_globalLanguage.getBasePackage(pkg);
								for (String k:keywords) {
									buf.add(new AutoCompleteView(AutoCompleteView.TYPE_KEYWORD, k));
								}
							}
						} else {
							String[] keywords = _globalLanguage.getKeywords();
							for (String k:keywords) {
								if (k.indexOf(keyword) == 0)
									buf.add(new AutoCompleteView(AutoCompleteView.TYPE_KEYWORD, k));
							}
						}
					}
					String ori=String.valueOf(constraint).toLowerCase();
					String[] sp=null;
					if (ori.contains(".")) {
						sp = ori.split("[.]");
						if (sp.length == 2) {
							String name=sp[0];
							String q=sp[1].toLowerCase();
							for (Const c : con) if (c.name.equalsIgnoreCase(name)) {
									for (Pair one : c.all) if (one.show.toLowerCase().startsWith(q)) buf.add(new AutoCompleteView(AutoCompleteView.TYPE_STANDARD, one.show, one));
									break;
								}
						} else if (sp.length == 1) {
							String name=sp[0];
							for (Const c : con) if (c.name.equals(name)) {
									for (Pair one : c.all) buf.add(new AutoCompleteView(AutoCompleteView.TYPE_STANDARD, one.show, one));
								}
						}
					} else {
						for (Const c : con)
							if (c.name.toLowerCase().startsWith(ori)) buf.add(new AutoCompleteView(AutoCompleteView.TYPE_CONST, c.name));
					}
					ori = String.valueOf(constraint);
					f:if (ori.length() != 1 && ori.indexOf('.') != -1) {
						String orip = ori.substring(0, ori.lastIndexOf('.'));
						String fil=null;
						if (ori.lastIndexOf('.') != ori.length() - 1) fil = ori.substring(ori.lastIndexOf('.') + 1, ori.length()).toLowerCase();
						Class cl = null;
						try {
							cl = Class.forName(orip);
						} catch (ClassNotFoundException e) {break f;}
						ArrayList<Pair> qw=new ArrayList<>();
						int modi;
						Pair p;
						while (cl != Object.class) {
							for (Method one : cl.getDeclaredMethods()) {
								if ((fil != null) && (!one.getName().toLowerCase().startsWith(fil))) continue;
								modi = one.getModifiers();
								if (!(Modifier.isStatic(modi) && (Modifier.isPublic(modi)))) continue;
								p = Method2Pair(cl, one, false);
								if (!qw.contains(p)) qw.add(p);
							}
							for (Field one : cl.getDeclaredFields()) {
								if ((fil != null) && (!one.getName().toLowerCase().startsWith(fil))) continue;
								modi = one.getModifiers();
								if (!(Modifier.isStatic(modi) && (Modifier.isPublic(modi)))) continue;
								p = Field2Pair(cl, one);
								if (!qw.contains(p)) qw.add(p);
							}
							cl = cl.getSuperclass();
						}
						try {
							cl = Class.forName(orip);
						} catch (ClassNotFoundException e) {}
						for (Pair one : qw) buf.add(new AutoCompleteView(AutoCompleteView.TYPE_STANDARD, one.show, one));
					}
					String qe;
					for (String one : _textField.getLexer().getFunctions())
						if (one.toLowerCase().startsWith(keyword)) buf.add(new AutoCompleteView(AutoCompleteView.TYPE_FUNCTION, one));
					for (String one : _textField.getLexer().getVars())
						if (one.toLowerCase().startsWith(keyword)) buf.add(new AutoCompleteView(AutoCompleteView.TYPE_VAR, one));
					ArrayList<CNode> all=Global.Q.pickOut(qe = String.valueOf(constraint));
					if (qe != null && qe.length() > 1) qe = qe.substring(0, qe.length() - 1);
					Collections.sort(all);
					for (CNode one : all) if (qe != null && one.getFullName(".").equals(qe)) continue; else buf.add(new AutoCompleteView(AutoCompleteView.TYPE_CLASS, one.getData(), one.getFullName(".")));
					all.clear();
					_constraint = keyword;
					FilterResults filterResults = new FilterResults();
					filterResults.values = buf;
					filterResults.count = buf.size();
					return filterResults;
				}
				/**
				 * 本方法在UI线程执行，用于更新自动完成列表
				 */
				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					if (results != null && results.count > 0 && !_abort.isSet()) {
						MyAdapter.this.clear();
						MyAdapter.this.addAll((ArrayList<AutoCompleteView>) results.values);
						int y = _textField.getCaretY() + _textField.rowHeight() / 2 - _textField.getScrollY();
						setHeight(getItemHeight() * Math.min(2, results.count));
						setHorizontalOffset(_textField.getCaretX() - _textField.getScrollX());
						setVerticalOffset(y - _textField.getHeight());//_textField.getCaretY()-_textField.getScrollY()-_textField.getHeight());
						notifyDataSetChanged();
						show();
					} else {
						notifyDataSetInvalidated();
					}
				}

			};
			return filter;
		}
	}
	private static ArrayList<Const> con=new ArrayList<>();
	public static String[] getReal(Class c, Method one, boolean cons) {
		String[] all=new String[2];
		StringBuffer para=new StringBuffer("(");
		Class<?>[] cl=one.getParameterTypes();
		for (int i=0;i < cl.length;i++) {
			para.append(cl[i].getName());
			para.append(i != cl.length - 1 ?",": ")");
		}
		all[0] = para.toString();
		if (all[0].length() == 1) all[0] = L.get(L.NoneParameter);
		if (para.length() == 1) para.append(")");
		para.insert(0, one.getName());
		if (!cons) {
			para.insert(0, '.');
			para.insert(0, c.getName());
		}
		all[1] = para.toString();
		return all;
	}
	public static void clearConst() {
		con.clear();
	}
	public static void removeConst(Const c) {
		con.remove(c);
	}
	public static void removeConst(int index) {
		con.remove(index);
	}
	public static Class getClass(String name) {
		Class c=loadClass(AutoCompletePanel.class.getClassLoader(), name);
		if (c == null) c = loadClass(Global.ClassLoader, name);
		return c;
	}
	private static Class loadClass(ClassLoader loader, String name) {
		try {
			return loader.loadClass(name);
		} catch (ClassNotFoundException e) {return null;}
	}
	public static Const addConst(String name, Class cl) {
		Const c=new Const();
		c.name = name;
		Pair p=null;
		while (cl != Object.class) {
			int modi;
			for (Method one : cl.getDeclaredMethods()) {
				modi = one.getModifiers();
				if (!Modifier.isPublic(modi)) continue;
				p = Method2Pair(cl, one, true);
				if (!c.contains(p)) c.addPair(p);
			}
			for (Field one : cl.getDeclaredFields()) {
				modi = one.getModifiers();
				if (!Modifier.isPublic(modi)) continue;
				p = Field2Pair(cl, one);
				if (!c.contains(p)) c.addPair(p);
			}
			cl = cl.getSuperclass();
		}
		con.add(c);
		return c;
	}
	public static Pair Field2Pair(Class cl, Field f) {
		return new Pair(f.getName(), new String[] {
							f.getName(),
							String.format("%s.%s", cl.getName(), f.getName())
						}, false);
	}
	public static Pair Method2Pair(Class cl, Method m, boolean cons) {
		return new Pair(m.getName(), getReal(cl, m, cons), true);
	}
	public static class Const {
		public String name;
		public ArrayList<Pair> all=new ArrayList<>();
		public void addPair(Pair p) {
			this.all.add(p);
		}
		public boolean contains(Pair p) {
			return all.contains(p);
		}
	}
	public static class Pair {
		public boolean method;
		public String show;
		public String[] real;
		public Pair(String show, String[] real, boolean method) {
			this.method = method;
			this.show = show;
			this.real = real;
		}
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Pair)) return false;
			Pair p=(Pair) obj;
			return this.show.equals(p.show) && this.real[0].equals(p.real[0]) && this.real[1].equals(p.real[1]);
		}
	}
}
