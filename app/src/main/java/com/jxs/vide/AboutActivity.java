package com.jxs.vide;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import com.jxs.vcompat.activity.VActivity;

import static com.jxs.vide.L.get;

public class AboutActivity extends VActivity {
	public static final byte[] DATA={-26, -124, -97, -24, -80, -94, -28, -67, -96, -28, -67, -65, -25, -108, -88, 86, 73, 68, 69, -17, -68, -127, 10, -24, -65, -103, -28, -72, -86, -24, -67, -81, -28, -69, -74, -26, -104, -81, -28, -67, -100, -24, -128, -123, 88, 115, 46, 74, 73, 79, 78, 71, -28, -72, -128, -28, -70, -70, -25, -117, -84, -25, -85, -117, -25, -96, -108, -27, -113, -111, -25, -102, -124, -28, -67, -100, -27, -109, -127, 10, -28, -67, -122, -26, -104, -81, -25, -96, -108, -27, -113, -111, -24, -65, -121, -25, -88, -117, -28, -72, -83, -28, -71, -97, -27, -80, -111, -28, -72, -115, -28, -70, -122, -28, -69, -106, -28, -70, -70, -25, -102, -124, -27, -72, -82, -27, -118, -87, 10, -27, -100, -88, -26, -83, -92, -26, -124, -97, -24, -80, -94, -28, -69, -91, -28, -72, -117, -28, -72, -70, -24, -67, -81, -28, -69, -74, -27, -120, -74, -28, -67, -100, -27, -127, -102, -27, -121, -70, -24, -76, -95, -25, -116, -82, -25, -102, -124, -27, -80, -113, -28, -68, -103, -28, -68, -76, 10, 75, 105, 110, 103, -62, -73, 83, 84, 32, 45, 32, -26, -113, -112, -28, -66, -101, -27, -123, -77, -28, -70, -114, -25, -68, -106, -24, -66, -111, -27, -103, -88, -28, -69, -91, -27, -113, -118, -27, -123, -74, -28, -69, -106, -24, -67, -81, -28, -69, -74, -26, -106, -71, -23, -99, -94, -25, -102, -124, -27, -72, -82, -27, -118, -87, 10, -25, -70, -94, -27, -113, -74, -25, -106, -81, -28, -70, -122, 32, 45, 32, -26, -113, -112, -27, -121, -70, -27, -92, -89, -23, -121, -113, -27, -69, -70, -24, -82, -82, 10, 10, -27, -100, -88, -26, -83, -92, -24, -121, -76, -23, -126, -93, -28, -70, -101, -24, -96, -94, -24, -96, -94, -26, -84, -78, -27, -118, -88, -26, -84, -78, -27, -101, -66, -26, -118, -124, -24, -94, -83, -25, -102, -124, -28, -70, -70, 10, -26, -100, -84, -24, -67, -81, -28, -69, -74, -27, -73, -78, -26, -73, -69, -27, -118, -96, -25, -83, -66, -27, -112, -115, -23, -86, -116, -24, -81, -127, -28, -69, -91, -27, -113, -118, -28, -72, -128, -25, -77, -69, -27, -120, -105, -23, -104, -78, -26, -118, -124, -24, -94, -83, -26, -114, -86, -26, -106, -67, 10, -24, -81, -73, -27, -91, -67, -24, -121, -86, -28, -72, -70, -28, -71, -117, -17, -68, -127, 10, 10, -26, -84, -94, -24, -65, -114, -26, -73, -69, -27, -118, -96, -28, -67, -100, -24, -128, -123, 81, 81, -27, -113, -115, -23, -90, -120, 66, 85, 71, -17, -68, -102, 50, 53, 48, 56, 53, 49, 48, 52, 56};
	
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleText(get(L.Title_About));
		enableBackButton();
		setSubTitle(get(L.DaShang));
		tv = new TextView(this);
		tv.setText(new String(DATA));
		tv.setGravity(Gravity.CENTER);
		setContentView(tv);
	}
}
