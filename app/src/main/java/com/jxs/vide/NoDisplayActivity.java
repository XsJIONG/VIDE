package com.jxs.vide;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.jxs.vapp.program.JsApp;
import java.io.File;
import java.io.FileInputStream;

public class NoDisplayActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent=getIntent();
		File f;
		if (intent.getStringExtra("JscPath") == null) {
			String scheme=intent.getScheme();
			if (!scheme.equals("file")) {
				finish();
				return;
			}
			if (intent.getDataString().length() < 8) {
				finish();
				return;
			}
			String path=intent.getDataString().substring(7);
			f=new File(path);
			if (!f.exists()) {
				finish();
				return;
			}
		} else f=new File(intent.getStringExtra("JscPath"));
		if (JsApp.GlobalContext == null) JsApp.GlobalContext = getApplicationContext();
		try {
			JsApp app=new JsApp(new FileInputStream(f));
			app.run();
			finish();
		} catch (Throwable t) {
			new AlertDialog.Builder(this).setTitle("Error").setMessage(Log.getStackTraceString(t)).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int pos) {
					finish();
				}
			}).show();
		}
	}
}
