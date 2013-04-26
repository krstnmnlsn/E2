package com.coursemingle.server;

/* Thrown after XML parse is called
 * The parsing for a certain course does not return a result.
 */
@SuppressWarnings("serial")
public class XMLCannotFindCourseException extends Exception {
	
	private String course;
	
	public XMLCannotFindCourseException(String course){
		this.course = course;
	}
	
	public String getCourse(){
		return course;
	}
}
