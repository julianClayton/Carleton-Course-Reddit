package com.carleton.ccr.db;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.carleton.ccr.Analytics.Analytics;
import com.carleton.ccr.crawler.Comment;
import com.carleton.ccr.crawler.Post;
import com.carleton.ccr.crawler.Submission;
import com.carleton.ccr.lucene.MyLucene;
import com.carleton.ccr.util.ListUtils;
import com.carleton.ccr.util.Tuple;
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
	private final String COURSE_CATEGORIES = "course-categories";
	
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
	
	public boolean getDocument(String id) {
		switchCollection(POST_COL);
		DBCursor cur = col.find(new BasicDBObject("id", id));
		DBObject obj = null;
		if (cur.hasNext()) {
			obj = cur.next();
		}
		if (obj == null) {
			return false;
		}
		
		return true;
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
				.add("url", p.getUrl())
				.add("content", p.getText())
				.add("tags", p.getTags())
				.add("sentiment", p.getSent())
				.add("postId", p.getPostId())
				.add("type", "post")
				.add("replies", p.getReplies())
				.add("author", p.getAuthor())
				.add("datetime", p.getTime())
				.get();

		col.save(obj);
	}
	public void addReplyToPost(String id, Comment c) {
		Post p = getPost(id);
		p.addReply(c);
		addPostToDb(p);
	}
	public void addCommentToDb(Comment c) {
		incrementDocNum();
		switchCollection(POST_COL);
		DBObject obj = BasicDBObjectBuilder
				.start("id", c.getId())
				.add("url",c.getUrl())
				.add("content", c.getText())
				.add("tags", c.getTags())
				.add("sentiment", c.getSent())
				.add("parentId", c.getParentId())
				.add("postId", c.getPostId())
				.add("type", "comment")
				.add("author", c.getAuthor())
				.add("datetime", c.getTime())
				.get();

		col.save(obj);
	}
	
	public Comment getComment(String id) {
		switchCollection(POST_COL);
		DBCursor cur = col.find(new BasicDBObject("id", id));
		DBObject obj = null;
		if (cur.hasNext()) {
			obj = cur.next();
		}
		if (obj == null) {
			return null;
		}
		Comment com = new Comment((String) obj.get("id"), (String) obj.get("url"), (String) obj.get("content"), (ArrayList<String>) obj.get("tags"));
		com.setAuthor((String)obj.get("author"));
		
		com.setTime((Date) obj.get("datetime"));
		return com;
	}
	
	public Post getPost(String id) {
		switchCollection(POST_COL);
		DBCursor cur = col.find(new BasicDBObject("id", id));
		DBObject obj = null;
		if (cur.hasNext()) {
			obj = cur.next();
		}
		if (obj == null) {
			return null;
		}
		Post post = new Post((String) obj.get("id"), (String) obj.get("url"), (String) obj.get("content"), 
				(ArrayList<String>) obj.get("tags"), (String) obj.get("title"));
		post.setAuthor((String)obj.get("author"));
		post.setTime((Date) obj.get("datetime"));

		return post;
	}
	
	public void addCoursesCategoriesToDatabase(ArrayList<String> courses) {
		switchCollection(COURSE_CATEGORIES);
		
		for (String course : courses) {
			DBObject obg = BasicDBObjectBuilder
					.start("course", course)
					.get();
			col.save(obg);
		}
	}
	public ArrayList<String> loadCourseCategoriesFromDatabase() {
		switchCollection(COURSE_CATEGORIES);
		ArrayList<String> courseCategories = new ArrayList<String>();
		DBCursor cursor = col.find();
		DBObject obj = null;
		while(cursor.hasNext()) {
			obj = cursor.next();
			courseCategories.add((String) obj.get("course"));
		}
		return courseCategories;
	}

	public HashMap<String,Integer> loadNumberOfCoursesPerSubject() {
		ArrayList<String> subjects = loadCourseCategoriesFromDatabase();
		HashMap<String, Integer> coursesPerSubject = new HashMap<String, Integer>();
		for (String subject : subjects) {
			Tuple<String, ArrayList<String>> res = loadCoursesByCategory(subject);
			ArrayList<String> courses = res.y;
			if (courses.size() > 0) {
				coursesPerSubject.put(subject, courses.size());
			}
			
		}
		
		return coursesPerSubject;
	}

	public HashMap<String,Integer> loadNumberOfPostsPerCourse() {
		ArrayList<String> subjects = loadCourseCategoriesFromDatabase();
		HashMap<String, Integer> postsPerCourse = new HashMap<String, Integer>();
		for (String subject : subjects) {
			Tuple<String, ArrayList<String>> res = loadCoursesByCategory(subject);
			ArrayList<String> courses = res.y;
			for (String course : courses) {
				ArrayList<Submission> q = MyLucene.query(course);
				postsPerCourse.put(course, q.size());
			}
		}
		return postsPerCourse;
	}
	public String getSentimentForCourse(String course) {
		switchCollection(POST_COL);
		DBCursor cursor = col.find();
		DBObject obj = null;
		ArrayList<String> courses = new ArrayList<String>();
		ArrayList<String> sentiments = new ArrayList<String>();
		while (cursor.hasNext()) {
			obj = cursor.next();
			ArrayList<String> tags = (ArrayList<String>) obj.get("tags");
			String sentiment = (String) obj.get("sentiment");
			for (String tag : tags) {
				if (tag.toLowerCase().equals(course.toLowerCase()) && !courses.contains(tag)) {
					courses.add(tag);
					sentiments.add(sentiment);
					
				}
			}
		}
		String sentiment = null;
		if (sentiments.size() > 0) {
			sentiment = ListUtils.mostCommon(sentiments);
		}
		return sentiment;
	}
	public Tuple<String, ArrayList<String>> loadCoursesByCategory(String category) {
		switchCollection(POST_COL);
		DBCursor cursor = col.find();
		DBObject obj = null;
		ArrayList<String> courses = new ArrayList<String>();
		ArrayList<String> sentiments = new ArrayList<String>();
		while (cursor.hasNext()) {
			obj = cursor.next();
			ArrayList<String> tags = (ArrayList<String>) obj.get("tags");
			String sentiment = (String) obj.get("sentiment");
			for (String tag : tags) {
				if (tag.substring(0, 4).toLowerCase().equals(category.toLowerCase()) && !courses.contains(tag)) {
					courses.add(tag);
					sentiments.add(sentiment);
					
				}
			}
		}
		String sentiment = null;
		if (sentiments.size() > 0) {
			sentiment = ListUtils.mostCommon(sentiments);
		}
		return new Tuple<String, ArrayList<String>>(sentiment, courses);
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

	
	public DBCursor getAllDocCursor(){
		switchCollection(POST_COL);
		DBCursor cursor = col.find();	
		return cursor;
	}

}
