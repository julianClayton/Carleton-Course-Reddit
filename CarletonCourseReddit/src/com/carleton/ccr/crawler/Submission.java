package com.carleton.ccr.crawler;

import java.util.ArrayList;

public abstract class Submission {
	String id;
	String postId;
	String url;
	String text;
	ArrayList<String> tags;
	
	public Submission(String id, String url, String text){
		this.id = id;
		this.url = url;
		this.text = text;
		this.tags = new ArrayList<String>();
	}
	
	public Submission(String id, String url, String text, ArrayList<String> tags){
		this.id = id;
		this.url = url;
		this.text = text;
		this.tags = tags;
	}
	
	public String getId(){
		return id;
	}
	
	public String getUrl(){
		return url;
	}
	
	public String getPostId(){
		return postId;
	}
	
	public String getText(){
		return text;
	}
	
	public ArrayList<String> getTags(){
		return tags;
	}
	
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public void addTags(ArrayList<String> moreTags) {
		for (String tag : moreTags) {
			if (!tags.contains(tag) && tag != null) {
				tags.add(tag);
			}
		}
	}
	
	public String toString(){
		return "";
		
	}
	
}
