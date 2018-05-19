package com.jxs.vide;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jxs.vapp.program.JsProgram;
import com.jxs.vapp.runtime.JsVActivityCompat;
import com.jxs.vcompat.activity.VActivity;
import com.jxs.vcompat.drawable.ArrowShape;
import com.jxs.vcompat.io.IOUtil;
import com.jxs.vcompat.ui.BDrawable;
import com.jxs.vcompat.ui.ColorUtil;
import com.jxs.vcompat.ui.UI;
import com.jxs.vcompat.ui.VOnClick;
import java.io.File;
import java.io.PrintStream;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptableObject;

import static com.jxs.vide.L.get;
import android.app.Activity;

public class LearnActivity extends VActivity {
	private LinearLayout Root,BarLayout,Addon;
	private View SplitLine;
	private Toolbar ActionBar;
	@VOnClick("onButtonClick")
	private Button Control;
	private TextEditor Code;
	private TextView Content;
	private JsProgram Program;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String name=getIntent().getStringExtra("name");
		Program = new JsProgram(name);
		try {
			if (Global.LEARN_DEBUG) Program.setCode(new String(IOUtil.read(new File(Environment.getExternalStorageDirectory(), "Lessons/" + name)))); else Program.setCode(new String(IOUtil.read(getAssets().open(String.format("lessons/%s", name)))));
			Program.getScriptableObject().defineProperty("a", this, ScriptableObject.CONST);
		} catch (Throwable e) {err(e, true);}
		initContentView();
		setContentView(Root);
		setSupportActionBar(ActionBar);
		setTitleText(getIntent().getStringExtra("title"));
		enableBackButton();
		onThemeChange(UI.THEME_UI_COLOR);
		BDrawable BackDrawable = new ArrowShape(this, 5f).toSimpleDrawable();
		UI.tintDrawable(BackDrawable, ColorUtil.getBlackOrWhite(UI.getThemeColor()));
		BackDrawable.setStrokeWidth(5f);
		getSupportActionBar().setHomeAsUpIndicator(BackDrawable);
		getSupportActionBar().setLogo(null);
		setBackDrawable(BackDrawable);
		Program.run();
	}
	private void initContentView() {
		ActionBar = new Toolbar(this);
		BarLayout = new LinearLayout(this);
		BarLayout.setBackgroundColor(UI.getThemeColor());
		BarLayout.setOrientation(LinearLayout.VERTICAL);
		BarLayout.addView(ActionBar);
		ViewCompat.setElevation(BarLayout, 10);
		Content = new TextView(this);
		Content.setMaxLines(20);
		Content.setVerticalScrollBarEnabled(true);
		Content.setSingleLine(false);
		int dp=ui.dp2px(15);
		Content.setPadding(dp, dp, dp, dp);
		Content.setTextColor(ColorUtil.getBlackOrWhite(UI.getThemeColor()));
		Content.setMovementMethod(ScrollingMovementMethod.getInstance());
		BarLayout.addView(Content);
		Addon = new LinearLayout(this);
		Addon.setOrientation(LinearLayout.VERTICAL);
		SplitLine = new View(this);
		int dp2=ui.dp2px(13);
		SplitLine.setBackgroundColor(ColorUtil.getBlackOrWhite(UI.getThemeColor()));
		Addon.addView(SplitLine, new LinearLayout.LayoutParams(-1, ui.dp2px(2)));
		Addon.setPadding(dp2, 0, dp2, dp2);
		Control = new Button(this);
		Control.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		Control.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		Control.setTextSize(13);
		Control.setTextColor(ColorUtil.getBlackOrWhite(UI.getThemeColor()));
		Control.setText(get(L.Run));
		Control.setBackground(null);
		Control.setPadding(dp - dp2, 0, dp - dp2, 0);
		Addon.addView(Control);
		BarLayout.addView(Addon);
		Addon.setVisibility(View.GONE);
		Root = new LinearLayout(this);
		Root.setOrientation(LinearLayout.VERTICAL);
		Root.addView(BarLayout);
		Code = new TextEditor(this);
		Code.setTypeface(Typeface.MONOSPACE);
		Code.setText("");
		Root.addView(Code);
	}
	@Override
	public void onThemeChange(String key) {
		super.onThemeChange(key);
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		BarLayout.setBackgroundColor(UI.getThemeColor());
		Control.setTextColor(ColorUtil.getBlackOrWhite(UI.getThemeColor()));
		SplitLine.setBackgroundColor(ColorUtil.getBlackOrWhite(UI.getThemeColor()));
		Content.setTextColor(ColorUtil.getBlackOrWhite(UI.getThemeColor()));
	}
	public void setContent(CharSequence s) {
		Content.setText(s);
	}
	public void setCode(CharSequence s) {
		Code.setText(s);
	}
	public TextView getContentTextView() {
		return Content;
	}
	public CharSequence getCode() {
		return Code.getText();
	}
	public TextEditor getCodeEditor() {
		return Code;
	}
	public PrintStream OutInstance=null;
	public void setButtonEnable(boolean flag) {
		Addon.setVisibility(flag ?View.VISIBLE: View.GONE);
	}
	public void setButtonText(CharSequence s) {
		Control.setText(s);
	}
	public void onButtonClick(View v) {
		Program.callFunction("onClick", new Object[]{v});
	}
	public void setEditable(boolean f) {
		Code.setEditable(f);
	}
	public boolean isEditable() {
		return Code.isEditable();
	}
	private Class<? extends Activity> RunClass=JsVActivityCompat.class;
	public void setRunClass(String className) throws ClassNotFoundException,ClassCastException {
		RunClass=(Class<? extends Activity>) Class.forName(className);
	}
	public void run() {
		if (Program.callFunction("onRunClicked")) return;
		String code=Code.getText().toString();
		try {
			Script s=Program.getContext().compileString(code, "Learning", 1, null);
			if (Program.callFunction("onRun", new Object[]{s})) return;
			s = null;
			startActivityForResult(new Intent(LearnActivity.this, RunClass).putExtra("code", code), 1);
		} catch (Throwable t) {
			Program.callFunction("onCompileError", new Object[]{t});
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) Program.callFunction("onActivityFinish", new Object[]{data});
	}
}
