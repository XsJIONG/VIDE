package com.jxs.vide;

import java.util.ArrayList;
import java.util.Set;
import java.lang.reflect.Array;
import com.jxs.vcompat.ui.UI;
import java.io.FileOutputStream;

public class CTree extends CNode {
	private ArrayList<CNode> Leafs=new ArrayList<>();
	public ArrayList<CNode> getLeafs() {
		return Leafs;
	}
	void addLeaf(CNode n) {
		if (!Leafs.contains(n)) Leafs.add(n);
	}
	void removeLeaf(CNode n) {
		Leafs.remove(n);
	}
	public ArrayList<CNode> pickOut(String key) {
		ArrayList<CNode> res=new ArrayList<>();
		if (!key.contains(".")) {
			key = key.toLowerCase();
			for (int i=0;i < Leafs.size();i++) if (Leafs.get(i).getData().toLowerCase().startsWith(key)) res.add(Leafs.get(i));
			return res;
		}
		String[] all=key.split("[.]");
		CNode now=this;
		for (int i=0;i < all.length - 1;i++) {
			if (!now.hasSon(all[i])) break;
			now = now.getSon(all[i]);
		}
		Set<String> q=now.getSon().keySet();
		String t=all[all.length - 1].toLowerCase();
		for (String one : q) if (one.toLowerCase().startsWith(t) && (!res.contains(now.getSon(one)))) res.add(now.getSon(one));
		return res;
	}
	public static CTree fromString(String s) {
		return fromString(s, false);
	}
	public static CTree fromString(String s, boolean autoLoad) {
		if (s.length() == 1) return null;
		CTree root=new CTree();
		CNode now=root;
		int last=0;
		char q;
		for (int i=0;i < s.length();i++) {
			q = s.charAt(i);
			if (q == ':') {
				now = now.getSon(s.substring(last, i));
				last = i + 1; 
			} else if (q == '|') {
				if (last != i) {
					now.getSon(s.substring(last, i)).setLeaf(true);
					if (autoLoad) now.getFullName(".");
				}
				last = i + 1;
				now = now.getParent();
			} else if (q == ',') {
				now.getSon(s.substring(last, i)).setLeaf(true);
				if (autoLoad) now.getFullName(".");
				last = i + 1;
			}
		}
		return root;
	}
	@Override
	public CTree toTree() {
		return this;
	}
	public CTree() {
		super("");
	}
}
