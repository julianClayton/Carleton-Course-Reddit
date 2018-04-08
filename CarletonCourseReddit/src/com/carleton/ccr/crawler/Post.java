package com.carleton.ccr.crawler;

import java.util.ArrayList;
import java.util.List;

public class Post {
	String id;
	String url;
	String title;
	String text;
	ArrayList<String> tags;
	List<Comment> comments;
	
	public Post(String id, String url, String title, String text){
		this.id = id;
		this.url = url;
		this.title = title;
		this.text = text;
		tags = new ArrayList<String>();
	}
	
	public Post(String text, ArrayList<String> tags){
		this.text = text;
		this.tags = tags;
	}
	
	public String getId(){
		return id;
	}
	
	public String getText(){
		return text;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getURL(){
		return url;
	}
	
	public List<String> getTags(){
		return tags;
	}
	
	public String toString(){
		return "ID: " + id + " Title: " + title + " Text: " + text + "\n";
		
	}
	public void addTags(ArrayList<String> newTags) {
		for (String tag : newTags) {
			if (!tags.contains(tag) && tag != null) {
				tags.add(tag);
			}
		}
	}
}
