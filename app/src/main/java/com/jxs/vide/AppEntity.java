package com.jxs.vide;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class AppEntity extends BmobObject {
	public String VName,Content;
	public boolean Force;
	public int VCode;
	public BmobFile File;
}
