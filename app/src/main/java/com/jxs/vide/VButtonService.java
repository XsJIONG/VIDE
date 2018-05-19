package com.jxs.vide;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import static com.jxs.vide.L.get;

public class VButtonService extends Service {
	public static VButtonService _Instance;
	@Override
	public IBinder onBind(Intent i) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		_Instance = this;
		VButton q=VButton.getInstance(this);
		q.setOnHideAction(new Runnable() {
			@Override
			public void run() {
				if (VButtonService._Instance != null) VButtonService._Instance.stopSelf();
				ConsoleDialog.getInstance().hide();
				ThreadDialog.getInstance().hide();
				DebugWindow.getInstance().hide();
				for (DebugWindow.CmdWindow one : DebugWindow.CmdWindow.Windows) one.destroy();
			}
		});
		VButton._show();
		Intent closeIntent=new Intent(this, VButtonService.class);
		closeIntent.putExtra("close", true);
		PendingIntent pintent=PendingIntent.getService(this, 0, closeIntent, 0);
		Notification b=new Notification.Builder(this)
		.setTicker(get(L.VButton_Ticker))
		.setContentTitle(get(L.VButton_Title))
		.setContentText(get(L.VButton_Text))
		.setSmallIcon(R.drawable.icon)
		.setContentIntent(pintent).build();
		startForeground(1, b);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent.getBooleanExtra("close", false))
			VButton._hide();
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		VButton._hide();
	}
}
