package com.carleton.ccr.crawler;

import java.io.Serializable;
import java.util.ArrayList;

public class Comment extends Submission implements Serializable {
	private static final long serialVersionUID = 7076472757792158007L;
	public String parentId;
	
	public Comment() {
		
	}
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
	
	public String getParentId(){
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString(){
		return "\n------------------------------\nID: " + id + "  URL : " + url + 
			 "\nText: " + text + "";
		
	}

}
