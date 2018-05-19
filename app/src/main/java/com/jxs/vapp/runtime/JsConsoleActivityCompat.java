package com.jxs.vapp.runtime;

import android.os.Bundle;
import android.util.Log;
import com.jxs.vapp.program.JsApp;
import com.jxs.vapp.program.JsExtend;
import com.jxs.vapp.program.JsProgram;
import com.jxs.vapp.program.Jsc;
import com.jxs.vapp.program.Program;
import com.jxs.vcompat.activity.ConsoleActivity;
import com.jxs.vcompat.ui.VAlertDialog;
import org.mozilla.javascript.ScriptableObject;

public class JsConsoleActivityCompat extends ConsoleActivity {
	public static final String ThreadJs="new java.lang.Thread({run:function() {\n%s\n}}).start();";
	private JsProgram program;
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
		program.defineProperty("vin", in, ScriptableObject.CONST);
		program.defineProperty("vout", out, ScriptableObject.CONST);
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
