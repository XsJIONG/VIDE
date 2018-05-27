package com.jxs.vide;

import cn.bmob.v3.*;
import cn.bmob.v3.datatype.*;

public class VAppEntity extends BmobObject {
	public String Title;
	public String Description;
	public BmobFile Content;
	public String Author;
	public boolean OpenSource=false;
	public int Likes=0;
}
