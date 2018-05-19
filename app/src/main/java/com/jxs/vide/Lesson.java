package com.jxs.vide;
import java.util.ArrayList;

public class Lesson {
	private String Title;
	private String SubTitle;
	private String Name;
	public Lesson(String name) {
		this.Name = name;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		this.Name = name;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		this.Title = title;
	}
	public String getSubTitle() {
		return SubTitle;
	}
	public void setSubTitle(String subTitle) {
		this.SubTitle = subTitle;
	}
	public static Lesson fromString(String s) {
		String[] sp=s.split("#", 3);
		Lesson l=new Lesson(sp[2]);
		l.Title = sp[0];
		l.SubTitle = sp[1];
		return l;
	}
	@Override
	public String toString() {
		return String.format("%s#%s#%s", Title, SubTitle, Name);
	}
	public static class Fase {
		public ArrayList<Lesson> Son=new ArrayList<>();
		public String Name;
		public Fase(String name) {
			this.Name = name;
		}
		public void add(Lesson l) {
			Son.add(l);
		}
	}
}
