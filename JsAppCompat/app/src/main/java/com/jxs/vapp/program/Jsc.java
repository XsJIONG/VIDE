package com.jxs.vapp.program;

import android.content.Intent;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class Jsc implements Serializable {
	private String name;
	private String code;
	private JsExtend ex;
	public Jsc(String name, JsExtend extend) {
		this.name = name;
		this.ex = extend;
	}
	public String getName() {
		return name;
	}
	public Jsc setCode(String code) {
		this.code = code;
		return this;
	}
	public String getCode() {
		return code;
	}
	public void setName(String name) {
		this.name = name;
	}
	public JsExtend getExtend() {
		return ex;
	}
	public JSONObject toJSON() {
		try {
			JSONObject obj=new JSONObject();
			obj.put("file", name);
			obj.put("extend", ex.name());
			return obj;
		} catch (JSONException e) {return null;}
	}
	public Jsc(JSONObject obj) {
		name = obj.optString("file");
		ex = JsExtend.valueOf(obj.optString("extend"));
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Jsc)) return false;
		Jsc c=(Jsc) obj;
		return c.name.equals(name) && c.ex == ex;
	}
	public void setExtend(JsExtend e) {
		ex = e;
	}
	@Override
	public String toString() {
		return name;
	}
	private JsApp parent;
	Jsc setParent(JsApp parent) {
		this.parent = parent;
		return this;
	}
	public String getClassName() {
		return name.substring(0, name.lastIndexOf('.'));
	}
	public void run() {
		run(null);
	}
	public void run(String rs) {
		JsProgram pro=new JsProgram(name);
		pro.setJsApp(parent);
		pro.setCode(code);
		String s=JsProgram.getRandomKey();
		pro.upload(s);
		switch (ex) {
			case JsVActivity:{
					//Mark Context May Fix
					Class<?> c=null;
					try {
						c = Class.forName(parent.isCompat() ?"com.jxs.vapp.runtime.JsVActivityCompat": "com.jxs.vapp.runtime.JsVActivity");
					} catch (Exception e) {e.printStackTrace();System.exit(1);}
					JsApp.GlobalContext.startActivity(new Intent(JsApp.GlobalContext, c).putExtra("ID", s).putExtra("RS", rs).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
					break;
				}
			case JsConsole:{
					Class<?> c=null;
					try {
						c = Class.forName(parent.isCompat() ?"com.jxs.vapp.runtime.JsConsoleActivityCompat": "com.jxs.vapp.runtime.JsConsoleActivity");
					} catch (Exception e) {e.printStackTrace();System.exit(1);}
					JsApp.GlobalContext.startActivity(new Intent(JsApp.GlobalContext, c).putExtra("ID", s).putExtra("RS", rs).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
					break;
				}
		}
	}
}
