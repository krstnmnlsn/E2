package com.coursemingle.server;

/* Thrown because a course that exists can not be added because of conflicts or prior registration 
 * to this course
 */
@SuppressWarnings("serial")
public class CannotAddCourseException extends Exception {
	
	private String course;
	
	public CannotAddCourseException(String course){
		this.course = course;
	}
	
	public String getCourse(){
		return course;
	}
}
