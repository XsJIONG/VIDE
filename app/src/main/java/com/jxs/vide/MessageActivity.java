package com.jxs.vide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import com.jxs.vcompat.activity.VActivity;
import com.jxs.vcompat.widget.VScrollView;

public class MessageActivity extends VActivity {
	private VScrollView Root;
	private TextView MsgView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleText(getIntent().getCharSequenceExtra("title"));
		Root = new VScrollView(this);
		MsgView = new TextView(this);
		MsgView.setText(getIntent().getCharSequenceExtra("msg"));
		MsgView.setGravity(Gravity.CENTER);
		Root.addView(MsgView);
		Root.setFillViewport(true);
		setContentView(Root);
		enableBackButton();
	}
	public static Intent getIntent(Context cx, CharSequence title, CharSequence msg) {
		Intent i=new Intent(cx, MessageActivity.class);
		i.putExtra("title", title);
		i.putExtra("msg", msg);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return i;
	}
	public static void showMessage(Context cx, CharSequence title, CharSequence msg) {
		cx.startActivity(getIntent(cx, title, msg));
	}
}
