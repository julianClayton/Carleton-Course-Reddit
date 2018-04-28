package com.carleton.ccr.crawler;

import java.io.Serializable;
import java.util.ArrayList;

public class Post extends Submission implements Serializable{
	private static final long serialVersionUID = -4322445167531204189L;
	
	String title;
	private ArrayList<Comment> replies;
	
	public Post(String id, String url, String text, String title) {
		super(id, url, text);
		this.title = title;
		replies = new ArrayList<Comment>();
	}
	
	public Post(String id, String url, String text, ArrayList<String> tags, String title) {
		super(id, url, text, tags);
		this.title = title;
		replies = new ArrayList<Comment>();

	}
	
	public String getTitle(){
		return title;
	}
	public void addReply(Comment c) {
		replies.add(c);
	}
	public void addAllReplies(ArrayList<Comment> r) {
		replies.addAll(r);
	}
	public ArrayList<Comment> getReplies() {
		return replies;
	}
	@Override
	public String toString(){
		return"\n------------------------------\nID: " + id + "  URL : " + url 
				 + "\nTitle : " + title + "\nText: " + text + "";
		
	}
	
}
