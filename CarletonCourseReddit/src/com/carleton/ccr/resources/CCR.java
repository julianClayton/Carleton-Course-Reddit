package com.carleton.ccr.resources;

import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;

import com.carleton.ccr.crawler.Comment;
import com.carleton.ccr.crawler.Submission;
import com.carleton.ccr.db.DatabaseManager;
import com.carleton.ccr.lucene.MyLucene;
import com.carleton.ccr.util.Tuple;

@Path("/ccr")
public class CCR {

	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	private String name;

	public CCR() {
		name = "Carleton reddit courses";
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String ccr() {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<html><head><title>Carleton Course Searcher</title></head><body><h1>"+ name +"</h1></body></html>");
		
		htmlBuilder.append("<body>");
		htmlBuilder.append("<ul>");	
		ArrayList<String> courseCategories = DatabaseManager.getInstance().loadCourseCategoriesFromDatabase();
		for (String course : courseCategories) {
			
			htmlBuilder.append("<li><a href=\"" + "ccr/courses/categories/" + course + "\">"+course +"</a></li>");
		}
		htmlBuilder.append("<ul>");
		return htmlBuilder.toString();
	}
	
	@GET
	@Path("courses/categories/{CATEGORY}")
	public String getCoursesByCategory(@PathParam("CATEGORY") String category) {
		DatabaseManager dbm = DatabaseManager.getInstance();
		Tuple<String, ArrayList<String>> coursesTuple = dbm.loadCoursesByCategory(category);

		ArrayList<String> courses = coursesTuple.y;
		String sentiment = coursesTuple.x;
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<html>");
		htmlBuilder.append("<head><title> Courses for: " + category);
		htmlBuilder.append("</title>Courses for: " + category + ", overall opinion: " + sentiment +  "</head>");
		for (String course : courses) {
			htmlBuilder.append("<li><a href=\"" + "/CarletonCourseReddit/ccr/courses/search/" + course + "\">"+ course +"</a>" + ", opinion: " + dbm.getSentimentForCourse(course) +"</li>");
		}
		htmlBuilder.append("<ul>");
		return htmlBuilder.toString();
	}
	
	@GET
	@Path("courses/search/{COURSE_QUERY}")
	@Produces(MediaType.TEXT_HTML) 
	public String queryCourses(@PathParam("COURSE_QUERY") String query) {
		StringBuilder htmlBuilder = new StringBuilder();
		ArrayList<Submission> comments = new ArrayList<Submission>();
		ArrayList<String> courselist = new ArrayList<String>(Arrays.asList(query.split("[+]")));
		htmlBuilder.append("<html>");
		htmlBuilder.append("<head><title> Overview of: ");
		
		for (String aCourse : courselist) {
			ArrayList<Submission> aquery = MyLucene.query(aCourse);
			if (aquery != null) {
				comments.addAll(MyLucene.query(aCourse));
			}
		}
		htmlBuilder.append(courselist.get(0));
		for (int i = 1; i < courselist.size(); i++) {
			htmlBuilder.append(courselist.get(i));
		}
		htmlBuilder.append("</title></head>");
		htmlBuilder.append("<body>");
		htmlBuilder.append("<ul>");	
		for (Submission comment : comments) {
			System.out.println(comment.getText());
			htmlBuilder.append("<li>" + comment.getText() + " " + "<a href=\""+ comment.getUrl() + "\">" + "POST</a>" + "</li>");
		}
		htmlBuilder.append("<ul>");
		return htmlBuilder.toString();
	}
}
