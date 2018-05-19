package com.jxs.vide;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import pxb.android.axml.AxmlReader;
import pxb.android.axml.AxmlVisitor;
import pxb.android.axml.AxmlWriter;
import pxb.android.axml.NodeVisitor;
import java.io.InputStream;

public class XmlFactory {
	public static final String Q="http://schemas.android.com/apk/res/android";

	private AxmlWriter w;
	public String Package=null,VersionName=null,Permissions[]=null,AppName=null;
	public int VersionCode=-1;
	public InputStream Input;
	public void make() throws IOException {
		w = new AxmlWriter();
		AxmlReader reader=AxmlReader.create(Input);
		reader.accept(new AxmlVisitor() {
			@Override
			public NodeVisitor visitFirst(String ns, String name) {
				return new AttrVisitor(name, w.visitFirst(ns, name));
			}
			@Override
			public void visitNamespace(String p, String url, int l) {
				w.visitNamespace(p, url, l);
			}
		});
	}
	public void writeTo(OutputStream out) throws IOException {
		w.writeTo(out);
	}
	public byte[] getBytes() {
		try {
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			w.writeTo(out);
			out.close();
			return out.toByteArray();
		} catch (IOException e) {return null;}
	}
	private class AttrVisitor extends NodeVisitor {
		String tag;
		NodeVisitor v;
		public AttrVisitor(String name, NodeVisitor v) {
			this.v = v;
			this.tag = name;
			if (tag.equals("manifest")&&Permissions!=null)
				for (String one:Permissions) v.visitChild(null,"uses-permission").visitContentAttr(Q,"name",0x1010003,AxmlVisitor.TYPE_STRING,one);
		}
		@Override
		public void visitContentAttr(String ns, String name, int resourceId, int type, Object obj) {
			switch (name) {
				case "package":obj = Package == null ?obj: Package;break;
				case "versionCode":obj = VersionCode == -1 ?obj: VersionCode;break;
				case "versionName":obj = VersionName == null ?obj: VersionName;break;
				case "label":if (obj.equals("JsApp") && AppName != null) obj = AppName;break;
			}
			v.visitContentAttr(ns, name, resourceId, type, obj);
		}
		@Override
		public NodeVisitor visitChild(String ns, String name) {
			return new AttrVisitor(name, v.visitChild(ns, name));
		}
		@Override
		public void visitLineNumber(int ln) {
			v.visitLineNumber(ln);
		}
		@Override
		public void visitContentText(int lineNumber, String value) {
			v.visitContentText(lineNumber, value);
		}
		@Override
		public void visitBegin() {
			v.visitBegin();
		}
		@Override
		public void visitContentEnd() {
			v.visitContentEnd();
		}
		@Override
		public void visitEnd() {
			v.visitEnd();
		}
	}
}
