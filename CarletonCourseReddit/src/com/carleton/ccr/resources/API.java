package com.carleton.ccr.resources;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.carleton.ccr.Analytics.Analytics;
import com.carleton.ccr.db.DatabaseManager;
import com.carleton.ccr.responses.AnalyticsResponse;
import com.carleton.ccr.responses.CoursesResponse;
import com.carleton.ccr.util.Tuple;

@Path("/ccr/rest")
public class API {

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<String> ccr() {
		DatabaseManager dbm = DatabaseManager.getInstance();
		ArrayList<String> subjects = dbm.loadCourseCategoriesFromDatabase();
		ArrayList<String> ret = new ArrayList<String>();
		for (String subject : subjects) {
			if (!dbm.loadCoursesByCategory(subject).y.isEmpty()) {
				ret.add(subject);
			}
		}
		return  ret;
	}
	@GET
	@Path("courses/categories/{CATEGORY}")
	@Produces(MediaType.APPLICATION_JSON)
	public CoursesResponse getCoursesByCategory(@PathParam("CATEGORY") String category) {
		DatabaseManager dbm = DatabaseManager.getInstance();
		Tuple<String, ArrayList<String>> coursesTuple = dbm.loadCoursesByCategory(category);

		ArrayList<String> courses = coursesTuple.y;
		String sentiment = coursesTuple.x;

		CoursesResponse cr = new CoursesResponse(category, sentiment);
		for (String course : courses) {
			cr.addCourse(course, dbm.getSentimentForCourse(course));
		}
		return cr;
	}
	@GET
	@Path("analytics/classes") 
	@Produces(MediaType.APPLICATION_JSON)
	public AnalyticsResponse getClassAnalytics() {
		return new AnalyticsResponse(Analytics.getNumberOfClassesStats());
	}
	@GET
	@Path("analytics/posts") 
	@Produces(MediaType.APPLICATION_JSON)
	public AnalyticsResponse getPostAnalytics() {
		return new AnalyticsResponse(Analytics.getNumberOfPostsForClassStats());
	}
}
