package com.carleton.ccr.Analytics;

import java.util.HashMap;

import com.carleton.ccr.db.DatabaseManager;
import com.carleton.ccr.responses.AnalyticsResponse;

public class Analytics {

	static HashMap<String, Integer> numberOfClassesStats;
	HashMap<String, Integer> numberOfPostsForClassStats;
	static DatabaseManager dbm;
	/*
	 * Method computes all analytics needed before being retrieved by the server
	 */

	
	public static HashMap<String, Integer> getNumberOfClassesStats() {	
		dbm = DatabaseManager.getInstance();
		return dbm.loadNumberOfCoursesPerSubject();
	}
	public static HashMap<String, Integer> getNumberOfPostsForClassStats() {
		dbm = DatabaseManager.getInstance();
		return dbm.loadNumberOfPostsPerCourse();
	}


	
	
	
}
