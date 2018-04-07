package com.carleton.ccr.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

/*
 * 
 * Class looks through comments 
 * 
 */
public class Parser {
	
	
	private static String COURSES_FILE = "courses/courses.txt";
	ArrayList<String> courses;
	private static Parser instance;
	
	
	public Parser() {
		courses = loadFromFile();
	}
	public static Parser getInstance() {
		if (instance == null) {
			instance = new Parser();
		}
		return instance;
	}
	

	public ArrayList<String> parsePost(String post) {
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
	
	private ArrayList<String> loadFromFile() {
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
		return course.toUpperCase();
	}
	public static void main(String[] args) {
		Parser p = Parser.getInstance();
		//ArrayList<String> c = p.parsePost("Hey guys. There's some cool looking courses in the SYSC deparment but it looks like they're restricted to SYSC students. Is is possible for somene outside the program to take them? The COMP offerings are pretty dismal for low-level programming and hardware applications.Alternatively if anyone knows any good courses along that route in the COMP department I'm all ears");
		//ArrayList<String> c = p.parsePost("Hahahaha (barely managing) Honestly COMS3004 isn’t that bad. Memorize the slides so you can quickly lookup the things you can’t recall. Have your notes divided in a way that’s fast to go through so you can find your answer in O(nlogn) time.Focus on architectur");

		for (String courser : c){
			System.out.println(courser);
		}
	}

}
