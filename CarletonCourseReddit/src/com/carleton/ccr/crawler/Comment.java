package com.carleton.ccr.crawler;

import java.util.ArrayList;
import java.util.List;

public class Comment {
	String id;
	String postId;
	String url;
	String text;
	String title;
	ArrayList<String> tags;
	String type;
	
	public Comment(String id, String pId,  String text, String type){
		this.id = id;
		this.postId = pId;
		this.text = text;
		this.tags = new ArrayList<String>();
		this.type = type;
	}
	
	public Comment(String id, String url, String text){
		this.id = id;
		this.url = url;
		this.text = text;
		this.tags = new ArrayList<String>();
	}
	
	public Comment(String id, String pId, String url, String text, ArrayList<String> tags){
		this.id = id;
		this.postId = pId;
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
	
	public List<String> getTags(){
		return tags;
	}
	
	public String toString(){
		return "\n------------------------------\nID: " + id + "  URL : " + url + 
				"\nTitle: " + title + "\nText: " + text + "";
		
	}
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public void setTitle(String t){
		this.title = t;
	}
	
	public void addTags(ArrayList<String> moreTags) {
		for (String tag : moreTags) {
			if (!tags.contains(tag) && tag != null) {
				tags.add(tag);
			}
		}
	}

}
