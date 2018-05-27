package com.jxs.vapp.runtime;

import android.content.res.*;
import android.os.*;
import android.util.*;
import com.jxs.v.activity.*;
import com.jxs.v.ui.*;
import com.jxs.vapp.program.*;
import org.mozilla.javascript.*;

public class JsConsoleActivity extends ConsoleActivity {
	public static final String ThreadJs="new java.lang.Thread({run:function() {\n%s\n}}).start();";
	private JsProgram program;
	private Resources RS;
	@Override
	public Resources getResources() {
		return RS;
	}
	@Override
	public AssetManager getAssets() {
		return RS.getAssets();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!init()) finish();
		if (program != null) {
			program.setCode(String.format(ThreadJs, program.getCode()));
			program.run();
		}
	}
	private boolean init() {
		if (program != null && (!program.isDestroyed())) return false;
		if (program != null) program.destroy();
		program = JsProgram.download(getIntent().getStringExtra("ID"));
		if (program == null) {
			if (getIntent().getStringExtra("code") != null) {
				program = new JsProgram("Learning");
				program.setCode(getIntent().getStringExtra("code"));
			} else {
				try {
					JsApp.INSTANCE = new JsApp(getAssets().open("Main.jsc"));
					JsApp.GlobalContext = getApplicationContext();
					if (JsApp.INSTANCE.getMainJs().getExtend() == JsExtend.JsConsole) {
						Jsc c=JsApp.INSTANCE.getMainJs();
						program = new JsProgram(c.getName());
						program.setJsApp(JsApp.INSTANCE);
						program.setCode(c.getCode());
					} else {
						JsApp.INSTANCE.run();
						finish();
						return false;
					}
				} catch (Exception e) {
					//Mark
					//FIXME
				}
			}
		}
		program.defineProperty("cx", this, ScriptableObject.CONST);
		program.defineProperty("ui", ui, ScriptableObject.CONST);
		program.defineProperty("vout", out, ScriptableObject.CONST);
		program.defineProperty("vin", in, ScriptableObject.CONST);
		program.setOnErrorListener(new Program.OnErrorListener() {
			@Override
			public void onError(Throwable e) {
				program.setOnErrorListener(null);
				if (!program.callFunction("onError", new Object[]{e})) {
					ui.newAlertDialog().setTitle("Error").setMessage(Log.getStackTraceString(e)).setCancelable(false).setPositiveButton("OK", new VAlertDialog.OnClickListener() {
						@Override
						public void onClick(VAlertDialog dialog, int pos) {
							finish();
						}
					}).show();
				}
				program.setOnErrorListener(this);
			}
		});
		return true;
	}
	@Override
	public void onBegin() {

	}
}
