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
import com.carleton.ccr.db.DatabaseManager;
import com.carleton.ccr.lucene.MyLucene;


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
			
			htmlBuilder.append("<li><a href=\"" + "courses/categories/" + course + "\">"+course +"</a></li>");
		}
		htmlBuilder.append("<ul>");
		return htmlBuilder.toString();
	}
	@GET
	@Path("courses/categories/{CATEGORY}")
	public String getCoursesByCategory(@PathParam("CATEGORY") String category) {
		ArrayList<String> courses = DatabaseManager.getInstance().loadCoursesByCategory(category);

		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<html>");
		htmlBuilder.append("<head><title> Courses for: " + category);
		htmlBuilder.append("</title></head>");
		for (String course : courses) {
			htmlBuilder.append("<li><a href=\"" + "/CarletonCourseReddit/rest/ccr/courses/search/" + course + "\">"+course +"</a></li>");
		}
		htmlBuilder.append("<ul>");
		return htmlBuilder.toString();
	}
	
	@GET
	@Path("courses/search/{COURSE_QUERY}")
	@Produces(MediaType.TEXT_HTML) 
	public String queryCourses(@PathParam("COURSE_QUERY") String query) {
		StringBuilder htmlBuilder = new StringBuilder();
		ArrayList<Comment> comments = new ArrayList<Comment>();
		ArrayList<String> courselist = new ArrayList<String>(Arrays.asList(query.split("[+]")));
		String coursecode = "";
		/*if (courselist.get(0).length() == 8) {
			coursecode = courselist.get(0).substring(4,8);
		}
		if (courselist.size() == 1 && StringUtils.isNumeric(coursecode)) {
			return "Single Course!";
		}*/
		htmlBuilder.append("<html>");
		htmlBuilder.append("<head><title> Overview of: ");
		
		for (String aCourse : courselist) {
			ArrayList<Comment> aquery = MyLucene.query(aCourse);
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
		for (Comment comment : comments) {
			System.out.println(comment.getText());
			htmlBuilder.append("<li>" + comment.getText() + "</li>");
		}
		htmlBuilder.append("<ul>");
		return htmlBuilder.toString();
	}
}
