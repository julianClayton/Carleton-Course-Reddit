package com.carleton.ccr.responses;

import java.io.Serializable;
import java.util.HashMap;

public class CoursesResponse implements Serializable {

	private static final long serialVersionUID = -2335473147280618004L;
	public String subject;
	public String overallSentiment;
	public HashMap<String, String> courses;
	
	public CoursesResponse(String subject, String overallSentiment) {
		this.subject = subject;
		this.overallSentiment = overallSentiment;
		this.courses = new HashMap<String,String>();
	}
	
	public void addCourse(String course, String sentiment) {
		courses.put(course, sentiment);
	}
}
