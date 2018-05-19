package com.jxs.vide.lang;

import com.jxs.vide.Global;
import com.jxs.vide.L;
import java.util.HashMap;

public abstract class Lang {
	public static final Class<? extends Lang>[] Langs=new Class[] {
		Lang_Chinese.class,
		Lang_FChinese.class,
		Lang_English.class,
		Lang_Bilibili.class
	};
	private static HashMap<Class<? extends Lang>, Lang> INSTANCES=new HashMap<>();
	protected void set(int i, String lan) {
		L.Language.put(Integer.valueOf(i), lan);
	}
	public static void setLanguage(Class<? extends Lang> cl) {
		Lang l=null;
		if (INSTANCES.containsKey(cl)) l = INSTANCES.get(cl); else {
			try {
				l = cl.newInstance();
				INSTANCES.put(cl, l);
			} catch (Exception e) {}
		}
		Global.Language = l;
		L.Language.clear();
		l.init();
	}
	protected abstract void init();
	public abstract String getName();
}
