package com.carleton.ccr.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.carleton.ccr.db.DatabaseManager;

/*
 * 
 * Class looks through comments 
 * 
 */
public class CourseParser {
	
	
	private static String COURSES_FILE = "courses/courses.txt";
	ArrayList<String> courses;
	private static CourseParser instance;
	
	
	public CourseParser() {
		courses = loadCourses();
	}
	public static CourseParser getInstance() {
		if (instance == null) {
			instance = new CourseParser();
		}
		return instance;
	}
	
	private static void addCoursesToDB() {
		ArrayList<String> courses = getInstance().loadCourses();
		DatabaseManager.getInstance().addCoursesCategoriesToDatabase(courses);
	}

	public ArrayList<String> parsePost(String post) {
		post = post.replaceAll("[,.;:/]", " ");
		String[] words = post.split("\\s+");
		ArrayList<String> coursesInText = new ArrayList<String>();
		for (int i = 0; i< words.length ; i++) {
			for (String course : courses) {
				if (words[i].length() > 3) {
					if (words[i].substring(0, 4).toLowerCase().contains(course.toLowerCase())) {
						String theCourse;
						theCourse = words[i];
						if (i != words.length-1 && StringUtils.isNumeric(words[i+1])) {
							theCourse = words[i] + "-" + words[i+1];
						}
						if (!coursesInText.contains(theCourse)) {
							coursesInText.add(formatCourse(theCourse));
						}
					}
				}
			}
		}
		return coursesInText;
	}
	
	public ArrayList<String> loadCourses() {
		Scanner s;
		ArrayList<String>  list = new ArrayList<String>();
		try {
			s = new Scanner(new File(COURSES_FILE));
			while (s.hasNext()){
			    list.add(s.next());
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}
	private String formatCourse(String course) {
		if (course.length() == 8) {
			course =  course.substring(0, Math.min(course.length(), 4)) + "-" + course.substring(4,Math.min(course.length(), 8 ));
		}
		if (course.length() > 8) {
			String coursecode = course.substring(5,  Math.min(course.length(), 9));
			if (!StringUtils.isNumeric(coursecode)) {
				course = course.substring(0, 4);
			}
		}
		if (course.length() > 4) {
			if (course.charAt(4) != '-') {
				return null;
			}
		}
		course = course.toUpperCase();
		return removeHyphen(course);
	}
	private String removeHyphen(String course) {
		return course.replaceAll("[\\s\\-()]", "");
	}
	public ArrayList<String> getCourses() {
		return courses;
	}
	public static void main(String[] args) {
		//CourseParser p = CourseParser.getInstance();
		//ArrayList<String> c = p.parsePost("Hey guys. There's some cool looking courses in the SYSC deparment but it looks like they're restricted to SYSC students. Is is possible for somene outside the program to take them? The COMP offerings are pretty dismal for low-level programming and hardware applications.Alternatively if anyone knows any good courses along that route in the COMP department I'm all ears");
		//ArrayList<String> c = p.parsePost("Courses are: Psyc 1001 and adsa  Busi 1002, both are online courses. Is it gonna be easy? Will I have time to breathe/rest?");
		//for(String course : c) {
			//System.out.println(course);
		//}
		//CourseParser.getInstance().addCoursesToDB();
	}
	

}
