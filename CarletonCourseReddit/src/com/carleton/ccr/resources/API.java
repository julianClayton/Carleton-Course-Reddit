package com.carleton.ccr.resources;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.carleton.ccr.db.DatabaseManager;
import com.carleton.ccr.responses.CoursesResponse;
import com.carleton.ccr.util.Tuple;

@Path("/ccr/rest")
public class API {

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<String> ccr() {
		return  DatabaseManager.getInstance().loadCourseCategoriesFromDatabase();
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
}
