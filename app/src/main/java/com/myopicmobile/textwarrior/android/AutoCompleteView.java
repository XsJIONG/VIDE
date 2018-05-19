package com.myopicmobile.textwarrior.android;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jxs.vcompat.ui.UI;
import com.jxs.vide.DrawableHelper;
import com.jxs.vide.R;

public class AutoCompleteView {
	public static final int TYPE_KEYWORD=1;
	public static final int TYPE_FUNCTION=2;
	public static final int TYPE_VAR=3;
	public static final int TYPE_CONST=4;
	public static final int TYPE_STANDARD=5;
	public static final int TYPE_CLASS=6;

	public int type;
	public String content;
	public Object data;
	public int textColor=Color.BLACK;
	public AutoCompleteView(int type) {this(type, null, null);}
	public AutoCompleteView(int type, String content) {this(type, content, null);}
	public AutoCompleteView(int type, String content, Object data) {
		this.type = type;
		this.content = content;
		this.data = data;
	}
	public AutoCompleteView setTextColor(int color) {
		textColor = color;
		return this;
	}
	public View getView(Context cx) {
		switch (type) {
			case TYPE_KEYWORD:{
					if (content == null) return null;
					LinearLayout s=new LinearLayout(cx);
					int dp=UI.dp2px(15);
					s.setPadding(dp, dp, dp, dp);
					TextView t=new TextView(cx);
					t.setText(content);
					t.setTextColor(textColor);
					t.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
					t.setTextColor(Color.BLACK);
					s.addView(t);
					s.setTag(content);
					return s;
				}
			case TYPE_CLASS:{
					if (content == null) return null;
					LinearLayout layout=new LinearLayout(cx);
					final int dp=UI.dp2px(12);
					layout.setPadding(dp, dp, dp, dp);
					final ImageView iv=new ImageView(cx);
					iv.setImageDrawable(DrawableHelper.getDrawable(R.drawable.icon_class, UI.getThemeColor()));
					LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(new UI(cx).dp2px(24), 0);
					p.height = p.width;
					p.rightMargin = dp;
					iv.setScaleType(ImageView.ScaleType.FIT_XY);
					layout.setOrientation(LinearLayout.HORIZONTAL);
					layout.setGravity(Gravity.CENTER_VERTICAL);
					layout.addView(iv, p);
					LinearLayout q=new LinearLayout(cx);
					q.setOrientation(LinearLayout.VERTICAL);
					TextView tv=new TextView(cx);
					tv.setTextColor(textColor);
					tv.setText(content);
					q.addView(tv);
					TextView tp=new TextView(cx);
					tp.setText((String) data);
					tp.setSingleLine(true);
					tp.setTextColor(Color.GRAY);
					q.addView(tp);
					layout.addView(q);
					layout.setTag((String) data);
					return layout;
				}
			case TYPE_CONST:
			case TYPE_VAR:
			case TYPE_FUNCTION:{
					if (content == null) return null;
					LinearLayout layout=new LinearLayout(cx);
					final int dp=UI.dp2px(12);
					layout.setPadding(dp, dp, dp, dp);
					final ImageView iv=new ImageView(cx);
					LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(new UI(cx).dp2px(24), 0);
					p.height = p.width;
					p.rightMargin = dp;
					iv.setScaleType(ImageView.ScaleType.FIT_XY);
					layout.setOrientation(LinearLayout.HORIZONTAL);
					layout.setGravity(Gravity.CENTER_VERTICAL);
					layout.addView(iv, p);
					TextView tv=new TextView(cx);
					tv.setText(content);
					tv.setTextColor(textColor);
					layout.addView(tv);
					String TAG=content;
					int id=-1;
					switch (type) {
						case TYPE_VAR:id = R.drawable.icon_var;break;
						case TYPE_FUNCTION:id = R.drawable.icon_function;TAG = content + "(";break;
						case TYPE_CONST:id = R.drawable.icon_const;break;
					}
					layout.setTag(TAG);
					iv.setImageDrawable(DrawableHelper.getDrawable(id, UI.getThemeColor()));
					return layout;
				}
			case TYPE_STANDARD:{
					if (content == null) return null;
					AutoCompletePanel.Pair pair=(AutoCompletePanel.Pair) data;
					LinearLayout layout=new LinearLayout(cx);
					final int dp=UI.dp2px(12);
					layout.setPadding(dp, dp, dp, dp);
					final ImageView iv=new ImageView(cx);
					iv.setImageDrawable(DrawableHelper.getDrawable(pair.method ?R.drawable.icon_const: R.drawable.icon_field, UI.getThemeColor()));
					LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(new UI(cx).dp2px(24), 0);
					p.height = p.width;
					p.rightMargin = dp;
					iv.setScaleType(ImageView.ScaleType.FIT_XY);
					layout.setOrientation(LinearLayout.HORIZONTAL);
					layout.setGravity(Gravity.CENTER_VERTICAL);
					layout.addView(iv, p);
					LinearLayout q=new LinearLayout(cx);
					q.setOrientation(LinearLayout.VERTICAL);
					TextView tv=new TextView(cx);
					tv.setTextColor(textColor);
					tv.setText(content);
					q.addView(tv);
					TextView tp=new TextView(cx);
					tp.setText(pair.real[0]);
					tp.setSingleLine(true);
					tp.setTextColor(Color.GRAY);
					q.addView(tp);
					layout.addView(q);
					layout.setTag(pair.real[1]);
					return layout;
				}
		}
		return null;
	}
}
