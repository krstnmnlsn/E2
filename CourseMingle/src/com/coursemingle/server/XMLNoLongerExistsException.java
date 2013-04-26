package com.coursemingle.server;


/* Thrown prior to XML parsing
 * Is the result of the course being checked is from a year before now.
 * UBC SCC will NOT contain that xml file anymore
 */
@SuppressWarnings("serial")
public class XMLNoLongerExistsException extends Exception{
	private CourseEntry ce;
	
	public XMLNoLongerExistsException(CourseEntry ce){
		this.ce = ce;
	}
	
	public CourseEntry getCourseEntry(){
		return ce;
	}
}
