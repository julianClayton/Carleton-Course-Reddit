package com.carleton.ccr.crawler;

import java.util.ArrayList;
import java.util.List;

public class Comment {
	String id;
	String text;
	ArrayList<String> tags;
	
	public Comment(String id, String text){
		this.id = id;
		this.text = text;
		tags = new ArrayList<String>();
	}
	
	public Comment(String text, ArrayList<String> tags){
		this.text = text;
		this.tags = tags;
		tags = new ArrayList<String>();
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
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	public void addTags(ArrayList<String> moreTags) {
		for (String tag : moreTags) {
			if (!tags.contains(tag) && tag != null) {
				tags.add(tag);
			}
		}
	}

}
