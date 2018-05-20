package com.jxs.vide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.Space;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jxs.vcompat.activity.VActivity;
import com.jxs.vcompat.ui.ColorUtil;
import com.jxs.vcompat.ui.UI;
import com.jxs.vcompat.widget.CardView;
import com.jxs.vcompat.widget.VScrollView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LearnListActivity extends VActivity {
	public static final int GROUP_SPACE=UI.dp2px(35);
	private static ArrayList<Lesson.Fase> FASES=null;
	private LinearLayout Root;
	private VScrollView Scroller;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleText(L.get(L.Title_Learn));
		if (FASES == null) initFases();
		enableBackButton();
		Root = new LinearLayout(this);
		Root.setOrientation(LinearLayout.VERTICAL);
		initViews();
		Scroller = new VScrollView(this);
		Scroller.setFillViewport(true);
		Scroller.addView(Root, new VScrollView.LayoutParams(-1, -1));
		setContentView(Scroller);
	}
	private void initViews() {
		int pad=ui.dp2px(20);
		LinearLayout.LayoutParams para=new LinearLayout.LayoutParams(-1, -2);
		para.setMargins(pad, pad, pad, 0);
		Lesson.Fase one;
		for (int i=0;i < FASES.size();i++) {
			one = FASES.get(i);
			Root.addView(new FaseView(this, one), para);
			for (Lesson l : one.Son)
				Root.addView(new LessonView(this, l), para);
			Root.addView(new Space(this), new LinearLayout.LayoutParams(-1, GROUP_SPACE));
		}
	}
	@Override
	public void onThemeChange(String key) {
		super.onThemeChange(key);
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		ViewGroup v;
		for (int i=0;i < Root.getChildCount();i++) {
			v = (ViewGroup) Root.getChildAt(i);
			if (v instanceof FaseView) ((FaseView) v).onThemeChange(); else ((LessonView) v).onThemeChange();
		}
	}
	private void initFases() {
		FASES = new ArrayList<>();
		try {
			Scanner scan=null;
			if (Global.LEARN_DEBUG) scan = new Scanner(new FileInputStream(new File(Environment.getExternalStorageDirectory(), "Lessons/List"))); else scan = new Scanner(getAssets().open("lessons/List"));
			Lesson.Fase present=null;
			String read;
			while (scan.hasNextLine()) {
				present = new Lesson.Fase(scan.nextLine());
				while ((read = scan.nextLine()) != null && !read.equals("end."))
					present.add(Lesson.fromString(read));
				FASES.add(present);
			}
		} catch (IOException e) {err(e, true);}
	}
	public static class FaseView extends LinearLayout {
		private Lesson.Fase f;
		private TextView title;
		private View split;
		public FaseView(Context cx, Lesson.Fase f) {
			super(cx);
			this.f = f;
			init();
		}
		private void init() {
			setOrientation(VERTICAL);
			title = new TextView(getContext());
			title.setTextSize(20);
			title.setText(f.Name);
			title.setGravity(Gravity.LEFT);
			addView(title);
			split = new View(getContext());
			LinearLayout.LayoutParams para=new LinearLayout.LayoutParams(-1, UI.dp2px(2));
			para.topMargin = UI.dp2px(5);
			addView(split, para);
			onThemeChange();
		}
		public void onThemeChange() {
			title.setTextColor(UI.getThemeColor());
			split.setBackgroundColor(UI.getThemeColor());
		}
	}
	public static class LessonView extends CardView {
		private TextView title,subtitle;
		private LinearLayout layout;
		private Lesson l;
		public LessonView(Context cx, Lesson l) {
			super(cx);
			this.l = l;
			init();
		}
		private void init() {
			layout = new LinearLayout(getContext());
			layout.setOrientation(LinearLayout.VERTICAL);
			int d=UI.dp2px(15);
			layout.setPadding(d, d, d, d);
			title = new TextView(getContext());
			title.setTextSize(14);
			title.setGravity(Gravity.LEFT);
			title.setSingleLine(true);
			layout.addView(title);
			subtitle = new TextView(getContext());
			subtitle.setTextSize(9);
			subtitle.setGravity(Gravity.LEFT);
			subtitle.setMaxLines(4);
			layout.addView(subtitle);
			setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					getContext().startActivity(new Intent(getContext(), LearnActivity.class).putExtra("name", l.getName()).putExtra("title", l.getTitle()));
				}
			});
			addView(layout);
			setRadius(5);
			setCardElevation(7f);
			setTranslationZ(10f);
			onThemeChange();
			title.setText(l.getTitle());
			subtitle.setText(l.getSubTitle());
		}
		private void onThemeChange() {
			layout.setBackgroundColor(UI.getThemeColor());
			int w=UI.getAccentColor();
			title.setTextColor(w);
			subtitle.setTextColor(w);
		}
	}
}
