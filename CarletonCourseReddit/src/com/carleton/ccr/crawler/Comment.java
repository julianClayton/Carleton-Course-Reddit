package com.carleton.ccr.crawler;

import java.util.ArrayList;

public class Comment extends Submission {
	String parentId;
	
	public Comment(String id, String url, String text){
		super(id, url, text);
	}
	
	public Comment(String id, String url, String text, ArrayList<String> tags){
		super(id, url, text, tags);
	}
	
	public Comment(String id, String pId, String url, String text, ArrayList<String> tags){
		super(id, url, text, tags);
		this.parentId = pId;
	}
	
	public String getPostId(){
		return parentId;
	}

	@Override
	public String toString(){
		return "\n------------------------------\nID: " + id + "  URL : " + url + 
			 "\nText: " + text + "";
		
	}

}
