package com.jxs.vide;

import android.os.Handler;
import android.app.ProgressDialog;
import android.os.Message;

public class DialogHandler extends Handler {
	private ProgressDialog Dialog;
	public DialogHandler(ProgressDialog dialog) {
		Dialog=dialog;
	}
	private void sendMsg(int what, Object obj) {
		Message s=new Message();
		s.what=what;
		s.obj=obj;
		sendMessage(s);
	}
	public void setMessage(CharSequence s) {
		sendMsg(0, s);
	}
	public void setTitle(CharSequence title) {
		sendMsg(1, title);
	}
	public void setCancelable(boolean c) {
		sendMsg(2, Boolean.valueOf(c));
	}
	public void dismiss() {
		sendMsg(3, null);
	}
	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case 0:
				if (Dialog!=null) Dialog.setMessage((CharSequence) msg.obj);
				break;
			case 1:
				if (Dialog!=null) Dialog.setTitle((CharSequence) msg.obj);
				break;
			case 2:
				if (Dialog!=null) Dialog.setCancelable((Boolean) msg.obj);
				break;
			case 3:
				if (Dialog!=null&&Dialog.isShowing()) Dialog.dismiss();
				break;
		}
	}
}
