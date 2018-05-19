package com.jxs.vide;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class VAppEntity extends BmobObject {
	public String Title;
	public String Description;
	public BmobFile Content;
	public String Author;
	public boolean OpenSource=false;
}
