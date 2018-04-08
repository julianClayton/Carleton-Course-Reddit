package com.carleton.ccr.crawler;

import java.util.ArrayList;

public class Post extends Submission{
	String title;
	
	public Post(String id, String url, String text, String title) {
		super(id, url, text);
		this.title = title;
	}
	
	public Post(String id, String url, String text, ArrayList<String> tags, String title) {
		super(id, url, text, tags);
		this.title = title;
	}
	
	public String getTitle(){
		return title;
	}
	
	@Override
	public String toString(){
		return"\n------------------------------\nID: " + id + "  URL : " + url 
				 + "\nTitle : " + title + "\nText: " + text + "";
		
	}
	
}
