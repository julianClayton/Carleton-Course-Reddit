package com.carleton.ccr.crawler;

import java.util.List;

public class Comment {
	String id;
	String text;
	List<String> tags;
	
	public Comment(String id, String text){
		this.id = id;
		this.text = text;
	}
	
	public Comment(String text, List<String> tags){
		this.text = text;
		this.tags = tags;
	}
	
	public String getId(){
		return id;
	}
	
	public String getTitle(){
		return id;
	}
	
	public String getText(){
		return text;
	}
	
	public List<String> getTags(){
		return tags;
	}
	
	public String toString(){
		return "ID: " + id + " Text: " + text + "\n";
		
	}

}
