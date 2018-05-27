package com.jxs.v.fragment;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.jxs.v.ui.*;
import com.jxs.v.widget.*;

public class SettingFragment extends VFragment implements UI.OnThemeChangeListener {
	private LinearLayout Layout;
	private DivideDrawable Divider;
	private VScrollView Scroll;
	public SettingFragment(Context cx) {
		super(cx);
		Layout = new LinearLayout(cx);
		Layout.setOrientation(LinearLayout.VERTICAL);
		Divider = new DivideDrawable(UI.getThemeColor());
		Layout.setDividerDrawable(Divider);
		Layout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
		Scroll = new VScrollView(cx);
		Scroll.setFillViewport(true);
		VScrollView.LayoutParams para=new VScrollView.LayoutParams(-1, -1);
		para.gravity = Gravity.TOP;
		Scroll.addView(Layout, para);
	}
	private int marginTop=0;
	public void addSpace(int height) {
		marginTop += height;
	}
	public int getMarginTop() {
		int tmp=marginTop;
		marginTop = 0;
		return tmp;
	}
	public void dismissDivider() {
		Layout.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
	}
	public void enableDivider() {
		Layout.setShowDividers(Layout.getShowDividers() ^ LinearLayout.SHOW_DIVIDER_NONE);
	}
	public void enableDividerBegin() {
		enableDivider();
		Layout.setShowDividers(Layout.getShowDividers() | LinearLayout.SHOW_DIVIDER_BEGINNING);
	}
	public void disableDividerBegin() {
		enableDivider();
		Layout.setShowDividers(Layout.getShowDividers() ^ LinearLayout.SHOW_DIVIDER_BEGINNING);
	}
	public void enableDividerMiddle() {
		enableDivider();
		Layout.setShowDividers(Layout.getShowDividers() | LinearLayout.SHOW_DIVIDER_MIDDLE);
	}
	public void disableDividerMiddle() {
		enableDivider();
		Layout.setShowDividers(Layout.getShowDividers() ^ LinearLayout.SHOW_DIVIDER_MIDDLE);
	}
	public void enableDividerEnd() {
		enableDivider();
		Layout.setShowDividers(Layout.getShowDividers() | LinearLayout.SHOW_DIVIDER_END);
	}
	public void disableDividerEnd() {
		enableDivider();
		Layout.setShowDividers(Layout.getShowDividers() ^ LinearLayout.SHOW_DIVIDER_END);
	}
	private static class DivideDrawable extends Drawable {
		private Paint paint;
		@Override
		public void setAlpha(int a) {}
		@Override
		public void setColorFilter(ColorFilter filter) {}
		@Override
		public int getOpacity() {return 0;}
		public void setColor(int color) {
			paint.setColor(color);
			invalidateSelf();
		}
		public DivideDrawable(int color) {
			paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(color);
			paint.setStrokeWidth(1);
		}
		@Override
		public void draw(Canvas c) {
			c.drawRect(getBounds(), paint);
		}
		@Override
		public int getIntrinsicHeight() {
			return 1;
		}
	}
	@Override
	public View getView() {
		Divider.setColor(UI.getThemeColor());
		return Scroll;
	}
	public SimpleItem addSimpleItem(CharSequence title) {
		return addSimpleItem(title, null);
	}
	public SimpleItem addSimpleItem(CharSequence title, CharSequence subTitle) {
		SimpleItem item=new SimpleItem(getContext());
		item.setTitle(title);
		item.setSubTitle(subTitle);
		item.setPadding(item.getPaddingLeft(), item.getPaddingTop() + getMarginTop(), item.getPaddingRight(), item.getPaddingBottom());
		Layout.addView(item);
		return item;
	}
	public CheckBoxItem addCheckBoxItem(CharSequence title) {
		return addCheckBoxItem(title, null);
	}
	public CheckBoxItem addCheckBoxItem(CharSequence title, CharSequence subTitle) {
		return addCheckBoxItem(title, subTitle, false);
	}
	public CheckBoxItem addCheckBoxItem(CharSequence title, CharSequence subTitle, boolean checked) {
		CheckBoxItem item=new CheckBoxItem(getContext());
		item.setTitle(title);
		item.setSubTitle(subTitle);
		item.setChecked(checked);
		item.setPadding(item.getPaddingLeft(), item.getPaddingTop() + getMarginTop(), item.getPaddingRight(), item.getPaddingBottom());
		Layout.addView(item);
		return item;
	}
	public SwitchItem addSwitchItem(CharSequence title) {
		return addSwitchItem(title, null);
	}
	public SwitchItem addSwitchItem(CharSequence title, CharSequence subTitle) {
		return addSwitchItem(title, subTitle, false);
	}
	public SwitchItem addSwitchItem(CharSequence title, CharSequence subTitle, boolean checked) {
		SwitchItem item=new SwitchItem(getContext());
		item.setTitle(title);
		item.setSubTitle(subTitle);
		item.setChecked(checked);
		item.setPadding(item.getPaddingLeft(), item.getPaddingTop() + getMarginTop(), item.getPaddingRight(), item.getPaddingBottom());
		Layout.addView(item);
		return item;
	}
	public GroupView addGroup(CharSequence title) {
		GroupView view=new GroupView(getContext());
		view.setText(title);
		view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getMarginTop(), view.getPaddingRight(), view.getPaddingBottom());
		Layout.addView(view);
		return view;
	}
	@Override
	public void onAttach() {
		UI.registThemeChangedListener(this, this);
		super.onAttach();
	}
	@Override
	public void onDettach() {
		UI.removeThemeChangedListener(this);
		super.onDettach();
	}
	@Override
	public Object getTag() {
		return SettingFragment.class;
	}
	@Override
	public void onThemeChange(String key) {
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		Divider.setColor(UI.getThemeColor());
		Layout.invalidate();
	}
	public static class GroupView extends TextView implements UI.OnThemeChangeListener {
		public GroupView(Context cx) {
			this(cx, null);
		}
		public GroupView(Context cx, AttributeSet attr) {
			this(cx, attr, 0);
		}
		public GroupView(Context cx, AttributeSet attr, int defStyle) {
			super(cx, attr, defStyle);
			init();
		}
		@Override
		protected void onAttachedToWindow() {
			super.onAttachedToWindow();
			UI.registThemeChangedListener(this, this);
		}
		@Override
		protected void onDetachedFromWindow() {
			super.onDetachedFromWindow();
			UI.removeThemeChangedListener(this);
		}
		@Override
		public void onThemeChange(String key) {
			if (!key.equals(UI.THEME_UI_COLOR)) return;
			setTextColor(UI.getThemeColor());
		}
		private void init() {
			setTextSize(20);
			setTextColor(UI.getThemeColor());
			int dp=UI.dp2px(15);
			setPadding(dp, dp, dp, dp);
		}
	}
	public static class SimpleItem extends LinearLayout implements UI.OnThemeChangeListener,OnClickListener {
		public SimpleItem(Context cx) {
			this(cx, null);
		}
		public SimpleItem(Context cx, AttributeSet attr) {
			this(cx, attr, 0);
		}
		public SimpleItem(Context cx, AttributeSet attr, int defStyle) {
			super(cx, attr, defStyle);
			init();
		}
		private TextView _Title, _SubTitle;
		private LinearLayout _DesLayout;
		private OnClickListener listener=null;
		private void init() {
			setOrientation(HORIZONTAL);
			int p=UI.dp2px(15);
			setPadding(p, p, p, p);
			setGravity(Gravity.CENTER);
			setClickable(true);
			TypedValue value=new TypedValue();
			getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, value, true);
			TypedArray arr=getContext().getTheme().obtainStyledAttributes(value.resourceId, new int[] {android.R.attr.selectableItemBackground});
			setBackground(arr.getDrawable(0));
			arr.recycle();
			_DesLayout = new LinearLayout(getContext());
			_DesLayout.setOrientation(LinearLayout.VERTICAL);
			LinearLayout.LayoutParams para=new LinearLayout.LayoutParams(0, -2);
			para.weight = 1;
			addView(_DesLayout, para);
			_Title = new TextView(getContext());
			_Title.setTextColor(UI.getThemeColor());
			_Title.setTextSize(13);
			_SubTitle = new TextView(getContext());
			_SubTitle.setTextColor(ColorUtil.lightColor(UI.getThemeColor(), 40));
			_SubTitle.setTextSize(8);
			super.setOnClickListener(this);
			setLongClickable(true);
			setOnLongClickListener(new OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						if (showContentWhenLongClick) {
							new VAlertDialog(getContext()).setTitle(getTitle()).setMessage(getSubTitle()).setCancelable(true).show();
							return true;
						} else return false;
					}
				});
		}
		private boolean showContentWhenLongClick=true;
		public void setShowContentWhenLongClick(boolean flag) {
			showContentWhenLongClick = flag;
		}
		public boolean isShowContentWhenLongClick() {
			return showContentWhenLongClick;
		}
		public String getTitle() {
			return _Title.getText().toString();
		}
		public String getSubTitle() {
			return _SubTitle.getText().toString();
		}
		@Override
		public boolean onInterceptTouchEvent(MotionEvent ev) {
			return true;
		}
		private boolean addTitle=false, addSubTitle=false;
		public void setTitle(CharSequence title) {
			if (title == null) {
				_Title.setText("");
				if (addTitle) {
					_DesLayout.removeView(_Title);
					addTitle = true;
				}
				return;
			}
			if (!addTitle) {
				_DesLayout.addView(_Title, 0);
				_Title.setTag(true);
			}
			_Title.setText(title);
		}
		public void setTitleTextSize(int size) {
			_Title.setTextSize(size);
		}
		public void setSubTitleTextSize(int size) {
			_SubTitle.setTextSize(size);
		}
		public void setSubTitle(CharSequence text) {
			if (text == null) {
				_SubTitle.setText("");
				if (addSubTitle) {
					_DesLayout.removeView(_SubTitle);
					addSubTitle = false;
				}
				return;
			}
			if (!addSubTitle) {
				_DesLayout.addView(_SubTitle);
				addSubTitle = true;
			}
			_SubTitle.setText(text);
		}
		@Override
		protected void onAttachedToWindow() {
			super.onAttachedToWindow();
			UI.registThemeChangedListener(this, this);
		}
		@Override
		protected void onDetachedFromWindow() {
			super.onDetachedFromWindow();
			UI.removeThemeChangedListener(this);
		}
		@Override
		public void onThemeChange(String key) {
			if (!key.equals(UI.THEME_UI_COLOR)) return;
			int c=UI.getThemeColor();
			_Title.setTextColor(c);
			_SubTitle.setTextColor(ColorUtil.lightColor(c, 40));
		}
		@Override
		public void setOnClickListener(View.OnClickListener l) {
			this.listener = l;
		}
		@Override
		public void onClick(View v) {
			if (listener != null) listener.onClick(v);
		}
	}
	public static class CheckBoxItem extends SimpleItem {
		private CheckBox box;
		public CheckBoxItem(Context cx) {
			this(cx, null);
		}
		public CheckBoxItem(Context cx, AttributeSet attr) {
			this(cx, attr, 0);
		}
		public CheckBoxItem(Context cx, AttributeSet attr, int defStyle) {
			super(cx, attr, defStyle);
			init();
		}
		private void init() {
			box = new CheckBox(getContext());
			try {
				box.setButtonTintList(ColorStateList.valueOf(UI.getThemeColor()));
			} catch (Throwable t) {}
			addView(box);
		}
		public boolean isChecked() {
			return box.isChecked();
		}
		@Override
		public void onThemeChange(String key) {
			super.onThemeChange(key);
			if (!key.equals(UI.THEME_UI_COLOR)) return;
			try {
				box.setButtonTintList(ColorStateList.valueOf(UI.getThemeColor()));
			} catch (Throwable t) {}
		}
		public void setChecked(boolean flag) {
			box.setChecked(flag);
		}
		@Override
		public void onClick(View v) {
			box.toggle();
			super.onClick(v);
		}
	}
	public static class SwitchItem extends SimpleItem {
		private Switch sw;
		public SwitchItem(Context cx) {
			this(cx, null);
		}
		public SwitchItem(Context cx, AttributeSet attr) {
			this(cx, attr, 0);
		}
		public SwitchItem(Context cx, AttributeSet attr, int defStyle) {
			super(cx, attr, defStyle);
			init();
		}
		private void init() {
			sw = new Switch(getContext());
			try {
				sw.getThumbDrawable().setColorFilter(UI.getThemeColor(), PorterDuff.Mode.SRC_ATOP);
			} catch (Throwable t) {}
			addView(sw);
		}
		public boolean isChecked() {
			return sw.isChecked();
		}
		@Override
		public void onThemeChange(String key) {
			super.onThemeChange(key);
			if (!key.equals(UI.THEME_UI_COLOR)) return;
			try {
				sw.getThumbDrawable().setColorFilter(UI.getThemeColor(), PorterDuff.Mode.SRC_ATOP);
			} catch (Throwable t) {}
		}
		public void setChecked(boolean flag) {
			sw.setChecked(flag);
		}
		@Override
		public void onClick(View v) {
			sw.toggle();
			super.onClick(v);
		}
	}
}
