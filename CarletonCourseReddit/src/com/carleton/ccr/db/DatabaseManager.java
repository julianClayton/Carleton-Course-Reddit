package com.carleton.ccr.db;
import java.net.UnknownHostException;

import com.carleton.ccr.crawler.Comment;
import com.carleton.ccr.crawler.Post;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;


public class DatabaseManager {
	
	private final String DOC_NUM_COL = "docnum";
	private final String POST_COL = "posts";
	private final String COMMENT_COL = "comments";

	private MongoClient	m;
	private DBCollection col;
	private DB db;
	private static DatabaseManager instance;
	
	public DatabaseManager() {
		instance = this;
		initConnection();
	}

	private int getDocNum() {
		switchCollection(DOC_NUM_COL);
		DBCursor cur = col.find().limit(1);
		int num = 1000;
		if (cur.hasNext()) {
			DBObject obj = cur.next();
			num = (int) obj.get("docnum");
		}
		return num;
	}
	
	public void incrementDocNum() {
		switchCollection(DOC_NUM_COL);
		DBCursor cur = col.find().limit(1);
		int num = 0;
		DBObject obj;
		if (cur.hasNext()) {
			obj = cur.next();
			num = (int) obj.get("docnum");
		}
		col.remove(new BasicDBObject("name","docid"));
		num++;
		DBObject newDocId = BasicDBObjectBuilder.start("name", "docid").add("docnum", num).get();
		col.insert(newDocId);
	}
		
	public void addPostToDb(Post p) {
		incrementDocNum();
		switchCollection(POST_COL);
		DBObject obj = BasicDBObjectBuilder
				.start("id", p.getId())
				.add("title", p.getTitle())
				.add("content", p.getText())
				.add("tags", p.getTags())
				.get();

		col.save(obj);
	}
	
	public void addCommentToDb(Comment c) {
		incrementDocNum();
		switchCollection(POST_COL);
		DBObject obj = BasicDBObjectBuilder
				.start("id", c.getId())
				.add("content", c.getText())
				.add("tags", c.getTags())
				.get();

		col.save(obj);
	}
	
	public static DatabaseManager getInstance() {
		if (instance == null)
			instance = new DatabaseManager();
		return instance;
	}
	
	public static void setInstance(DatabaseManager instance) {
		DatabaseManager.instance = instance;	
	}
	
	private void initConnection() {
		try {
			m = new	MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}	
		db = m.getDB("ccr");	
		switchCollection(POST_COL);
	}
	
	private void switchCollection(String collection) {
		col = db.getCollection(collection);
	}

}
