package com.coursemingle.server;

import java.net.URL;
import java.util.Calendar;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


public class XMLCourseParser {

	private static final int TRYATTEMPTS = 1;
	
	final String baseURL ="https://courses.students.ubc.ca/cs/servlets/SRVCourseSchedule?";

	// Return throws Course Exception if course does not exist in XML
	// This will fail a percentage of the time due to server lag.
	// Returns a course with all the appropriate data.
	//		   coursename is of form: [a-zA-Z]{2,4}\s*[0-6][0-9]{2}[a-zA-Z]?\s*[ltLT01234][0-9]{2}
	public XMLCourseSection getCourseData(String coursename) throws XMLCannotFindCourseException{
		String[] results = coursename.split("\\s+");
		String subject = results[0];
		String course_number = results[1];
		String section = results[2];
		subject.trim();
		course_number.trim();
		section.trim();
		if(!subject.matches("[a-zA-Z]{2,4}") 
				|| !course_number.matches("[0-6][0-9]{2}[a-zA-Z]?") 
				|| !section.matches("[ltLT01234][0-9][0-9a-zA-Z]")){
			throw new XMLCannotFindCourseException(coursename);
		}
		try{
			return XMLParse(subject,course_number,section,'W');
		}
		catch(Exception e){
			return XMLParse(subject,course_number,section,'S');
		}	
	}

	// This will fail a percentage of the time due to server lag.
	public XMLCourseSection getCourseData(CourseEntry ce) 
			throws XMLNoLongerExistsException, XMLCannotFindCourseException{
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		if(ce.getYear()!=year){
			throw new XMLNoLongerExistsException(ce);
		}
		else{
			String subject = ce.getSubject();
			String course_number = ce.getCourse_number();
			String section = ce.getSection();
			char season = ce.getSeason();
			return XMLParse(subject,course_number,section, season);
		}
	}

	// Actual function body of any parsing
	// This will fail a percentage of the time due to server lag.
	private  XMLCourseSection XMLParse (String subject, String course_number, String section, char season)
			throws XMLCannotFindCourseException{

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		String finalURL = baseURL + "sessyr=" + year + "&sesscd=" + season + "&req=5&dept=" 
				+ subject + "&course=" + course_number + "&section=" + section + "&output=5";

		XMLCourseSection xcs = null;

		int tries = 0;
		while(xcs == null && tries<=TRYATTEMPTS){
			try {
				XMLReader reader = XMLReaderFactory.createXMLReader();
				XMLCourseParserHandler handler = new XMLCourseParserHandler();
				reader.setContentHandler(handler);
				reader.parse(new InputSource(new URL(finalURL).openStream()));
				
				xcs = handler.getResult();
				xcs.setSubject(subject.toUpperCase());
				xcs.setCourse_number(course_number.toUpperCase());
				xcs.setYear(year);
				xcs.setSeason(season);
				
				return xcs;
			} catch (Exception e) {
				if(tries==TRYATTEMPTS){
					throw new XMLCannotFindCourseException(subject+course_number+section);
				}
				else{
					tries++;
				}
			} 
		}
		return xcs;
	}

}