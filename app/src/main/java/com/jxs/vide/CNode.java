package com.jxs.vide;

import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CNode implements Comparable<CNode> {
	private CNode parent;
	private HashMap<String, CNode> son=new HashMap<>();
	private String data;
	private boolean isLeaf=false;
	private String FullNameSp=null;
	private String FullNameCache=null;
	public CNode(String data) {
		this.data = data;
	}
	protected void setSons(HashMap<String, CNode> all) {
		this.son = all;
	}
	public HashMap<String, CNode> getSon() {
		return this.son;
	}
	public String getFullName(String seprator) {
		if (FullNameCache != null && seprator.equals(FullNameSp)) return FullNameCache;
		StringBuffer buff=new StringBuffer();
		getFullName(seprator, buff);
		if (buff.toString().endsWith(seprator)) buff.delete(buff.length() - seprator.length(), buff.length());
		FullNameCache = buff.toString();
		FullNameSp = seprator;
		return FullNameCache;
	}
	private void getFullName(String seprator, StringBuffer buffer) {
		if (!isRoot()) {
			buffer.insert(0, data + seprator);
			parent.getFullName(seprator, buffer);
		}
	}
	public boolean hasSon(String...path) {
		CNode now=this;
		for (String one : path) {
			if (!now.hasSon(one)) return false;
			now = now.getSon(one);
		}
		return true;
	}
	public CNode getSon(String...path) {
		CNode n=this;
		for (String one : path) n = n.getSon(one);
		return n;
	}
	public void addSon(CNode c) {
		c.setParent(this);
		this.son.put(c.getData(), c);
	}
	public void merge(CTree t) {
		for (CNode one : t.getAllLeaf())
			addLeaf(one.getFullName(".").split("[.]"));
	}
	public void setData(String data) {
		this.data = data;
		FullNameCache = null;
	}
	public void setParent(CNode pa) {
		parent = pa;
		FullNameCache = null;
	}
	public String getData() {
		return data;
	}
	public CNode getSon(String key) {
		if (!son.containsKey(key)) addSon(key);
		return son.get(key);
	}
	public void addSon(String name) {
		if (son.containsKey(name)) return;
		CNode c=new CNode(name);
		c.setParent(this);
		son.put(name, c);
	}
	public CTree getRoot() {
		CNode now=this;
		while (!now.isRoot()) now = now.getParent();
		return (CTree) now;
	}
	public boolean isRoot() {
		return parent == null;
	}
	public CNode addClass(String name) {
		try {
			Class.forName(name);
		} catch (Throwable err) {return null;}
		return addLeaf(name.split("[.]"));
	}
	public CNode addLeaf(String...all) {
		CNode now=this;
		for (String one : all)
			now = now.getSon(one);
		now.isLeaf = true;
		getRoot().addLeaf(now);
		return now;
	}
	public boolean hasSon(String key, boolean ig) {
		if (ig) {
			for (String one : son.keySet()) if (one.equalsIgnoreCase(key)) return true;
			return false;
		} else return son.containsKey(key);
	}
	public boolean hasSon(String key) {
		return hasSon(key, false);
	}
	public boolean hasLeaf(String[] all) {
		CNode now=this;
		for (String one : all) {
			if (!now.hasSon(one)) return false;
			now = now.getSon(one);
		}
		return now.isLeaf;
	}
	public boolean isLeaf() {
		return isLeaf;
	}
	public void setLeaf(boolean flag) {
		this.isLeaf = flag;
		if (isLeaf)
			getRoot().addLeaf(this);
		else
			getRoot().removeLeaf(this);
	}
	@Override
	public String toString() {
		StringBuffer buff=new StringBuffer();
		innerToString(buff);
		return buff.toString();
	}
	private void innerToString(StringBuffer buffer) {
		if (isRoot()) {
			for (String one : son.keySet()) {
				son.get(one).innerToString(buffer);
				if (buffer.charAt(buffer.length() - 1) != '|') buffer.append(",");
			}
			if (buffer.charAt(buffer.length() - 1) == ',') buffer.deleteCharAt(buffer.length() - 1);
			return;
		}
		if (isLeaf)
			buffer.append(data);
		else {
			buffer.append(data).append(":");
			for (String one : son.keySet()) {
				son.get(one).innerToString(buffer);
				if (buffer.charAt(buffer.length() - 1) != '|') buffer.append(",");
			}
			if (buffer.charAt(buffer.length() - 1) == ',') buffer.deleteCharAt(buffer.length() - 1);
			buffer.append("|");
		}
	}
	public static CNode fromString(String s) {
		return fromString(s, false);
	}
	public static CNode fromString(String s, boolean autoLoad) {
		if (s.length() == 1) return null;
		CNode root=new CNode("");
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
	public static boolean hasClass(String name) {
		if (name.contains("$")) return false;
		try {
			Class.forName(name);
			return true;
		} catch (Throwable t) {return false;}
	}
	public CNode getParent() {
		return parent;
	}
	public CTree toTree() {
		if (parent != null) return null;
		CTree c=new CTree();
		c.setSons(son);
		this.son = null;
		this.data = null;
		return c;
	}
	public void removeSon(String name) {
		if (son == null) {
			try {
				FileOutputStream out=new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "Out.txt"));
				out.write(getFullName(".").getBytes());
				out.close();
				return;
			} catch (IOException e) {e.printStackTrace();}
		}
		CNode c=son.remove(name);
		CTree r=getRoot();
		if (c != null)
			for (CNode one : c.getAllLeaf()) r.removeLeaf(one);
	}
	public ArrayList<CNode> getAllLeaf() {
		ArrayList<CNode> q=new ArrayList<>();
		innerGetAllLeaf(q);
		return q;
	}
	private void innerGetAllLeaf(ArrayList<CNode> bu) {
		if (isLeaf) bu.add(this); else {
			for (CNode one : son.values()) one.innerGetAllLeaf(bu);
		}
	}
	public void cleanUp() {
		CNode[] q=new CNode[son.size()];
		int i=0;
		for (CNode one : son.values()) q[i++] = one;
		for (CNode one : q) one.cleanUp();
		if ((son.size() == 0 && !isLeaf) || (isLeaf && (!hasClass(getFullName(".")))))
			if (getParent() != null) getParent().removeSon(data);
	}
	@Override
	public int compareTo(CNode ano) {
		return data.compareTo(ano.getData());
	}
}
