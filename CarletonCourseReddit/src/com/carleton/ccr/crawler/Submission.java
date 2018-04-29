package com.carleton.ccr.crawler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.mongodb.BasicDBObject;

public abstract class Submission extends BasicDBObject implements Serializable{
	private static final long serialVersionUID = 6647438651565912908L;
	String id;
	String postId;
	String url;
	String text;
	String sentiment;
	ArrayList<String> tags;
	String author;
	Date time;
	
	public Submission(String id, String url, String text){
		this.id = id;
		this.url = url;
		this.text = text;
		this.tags = new ArrayList<String>();
	}
	public Submission(String id, String url, String text, String author){
		this.id = id;
		this.url = url;
		this.text = text;
		this.tags = new ArrayList<String>();
		this.author = author;
	}
	public Submission(){};
	
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
	
	public String getSent(){
		return sentiment;
	}
	
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public void setSentiment(String sent){
		this.sentiment = sent;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getAuthor() {
		return author;
	}
	public void setTime(Date date) {
		this.time = date;
	}
	public Date getTime() {
		return time;
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
