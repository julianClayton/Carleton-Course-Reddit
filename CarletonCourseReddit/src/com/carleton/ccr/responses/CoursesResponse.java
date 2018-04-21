package com.carleton.ccr.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.carleton.ccr.crawler.Submission;
import com.carleton.ccr.lucene.MyLucene;

public class CoursesResponse implements Serializable {

	private static final long serialVersionUID = -2335473147280618004L;
	public String subject;
	public String overallSentiment;
	public ArrayList<Course> courses;
	
	public CoursesResponse(String subject, String overallSentiment) {
		this.subject = subject;
		this.overallSentiment = overallSentiment;
		this.courses = new ArrayList<Course>();
	}
	
	public void addCourse(String course, String sentiment) {
		courses.add(new Course(course, sentiment));
	}
	public class Course implements Serializable {
		private static final long serialVersionUID = 4321451787374757221L;
		public String courseName;
		public String sentiment;
		public ArrayList<Post> posts;
		
		public Course(String courseName, String sentiment) {
			posts = new ArrayList<Post>();
			this.courseName = courseName;
			this.sentiment = sentiment;
			ArrayList<Submission> q = MyLucene.query(courseName);
			for (Submission s : q) {
				posts.add(new Post(s.getText(), s.getUrl()));
			}
		}
	}
	public class Post implements Serializable{
		private static final long serialVersionUID = 9067440995456044771L;
		public String text;
		public String url;
		public Post(String text, String url) {
			this.text = text;
			this.url = url;
		}
	}
}
