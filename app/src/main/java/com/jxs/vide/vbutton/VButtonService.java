package com.jxs.vide.vbutton;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import com.jxs.vide.*;
import java.util.*;

public class VButtonService extends Service {
	public static ArrayList<Bitmap> All=new ArrayList<>();
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
			.setTicker(L.get(L.VButton_Ticker))
			.setContentTitle(L.get(L.VButton_Title))
			.setContentText(L.get(L.VButton_Text))
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
		for (int i=0;i < All.size();i++) All.get(i).recycle();
		All.clear();
		FWView.ICON = null;
		VButtonWindow.Icons = null;
	}
}
