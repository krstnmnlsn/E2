package com.coursemingle.server;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.servlet.http.HttpSession;

import com.coursemingle.client.CourseManager;
import com.coursemingle.shared.CourseInfo;
import com.coursemingle.shared.MessageException;
import com.coursemingle.shared.TeachingUnit;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class CourseManagerImpl extends RemoteServiceServlet implements CourseManager{

	private static final PersistenceManagerFactory PMF =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");

	private static XMLCourseParser xcp = new XMLCourseParser();

	// Public method for detecting and adding courses.
	// Strings are NOT cleaned in this method.
	public void addCourses(String newcourses) throws MessageException {
		LinkedList<String> coursestoadd =
				stringIntoListOfCourses(newcourses);

		String errormessage = "";
		for(String course: coursestoadd){
			try{
				addCourse(course);
			}
			catch(MessageException e){
				errormessage = errormessage + "\n" + e.getErrorMessage();
			}
		}
		if(coursestoadd.isEmpty()){
			throw new MessageException("Please try entering real courses");
		}
		else if(errormessage != ""){
			throw new MessageException(errormessage);
		}
	}

	// Public method for detecting and removing courses.
	// Strings are NOT cleaned in this method.
	public void rmCourses(String toberemovedcourses) throws MessageException {
		LinkedList<String> coursestoremove =
				stringIntoListOfCourses(toberemovedcourses);

		String errormessage = "";
		for(String course: coursestoremove){
			try{
				rmCourse(course);
			}
			catch(MessageException e){
				errormessage = errormessage + "\n" + e.getErrorMessage();
			}
		}
		if(coursestoremove.isEmpty()){
			throw new MessageException("Please try entering real courses");
		}
		else if(errormessage != ""){
			throw new MessageException(errormessage);
		}
	}

	// Calls getCurrentCourses for your own courses
	public LinkedList<CourseInfo> getCurrentCourses() {
		return getCurrentCourses(this.getCurrentUserID());
	}


	// Returns all the courses that a person is taking this year.
	public LinkedList<CourseInfo> getCurrentCourses(String uid) {

		// Get all the courses with the student
		PersistenceManager pm = getPersistenceManager();
		List<CourseEntry> results =
				queryForCourseEntry("userID == uid", "String uid",uid, pm);

		LinkedList<CourseInfo> studentscourse = new LinkedList<CourseInfo>();

		// Generate the CourseInfo to be returned
		for(CourseEntry ce: results){
			XMLCourseSection xcs = null;
			try{
				xcs = xcp.getCourseData(ce);
			} catch (XMLNoLongerExistsException e) {
				// this course entry is of a course from an older year
				// do nothing
			} catch (XMLCannotFindCourseException e) {
				// this should NOT happen and the ce is faulty
				pm.deletePersistent(ce);
			}

			// Only add if we can, the returned course is not null;
			if(xcs != null){
				CourseInfo ci = XMLCourseSectiontoCourseInfo(xcs);
				ci.setStudents(getStudentList(ce));
				studentscourse.add(ci);
			}
		}
		pm.close();
		return studentscourse;
	}

	// Tries to find all students in OUR database that is in a course if it can find the course
	// in the xml files
	public LinkedList<String> getStudentList(String coursetoget) throws MessageException{
		String course = cleanCourseString(coursetoget);
		PersistenceManager pm = getPersistenceManager();
		try {
			// Try and find the corresponding CourseInfo
			XMLCourseSection xcs = xcp.getCourseData(course);
			// Call the primary function
			CourseEntry ce = XMLCourseSectiontoCourseEntry(xcs);
			return this.getStudentList(ce);
		} catch (XMLCannotFindCourseException e) {
			throw new MessageException("Could not find " + course);
		}
		finally {
			pm.close();
		}
	}

	// Return the intersection of our courses and a friends courses
	// Possible to return an empty list
	public LinkedList<CourseInfo> getCommonCourses(String userID) {
		LinkedList<CourseInfo> theircourses = getCurrentCourses(userID);
		LinkedList<CourseInfo> ourcourses = getCurrentCourses(getCurrentUserID());
//		for(CourseInfo ci: theircourses){
//			removeifpossible(ci,ourcourses);
//		}
//		return ourcourses;
		
		LinkedList<CourseInfo> mutualCourses = new LinkedList<CourseInfo>();
		
		for (CourseInfo theircourse : theircourses) {
			
			for (CourseInfo ourcourse : ourcourses) {
				if (ourcourse.getSection().equals(theircourse.getSection()) && ourcourse.getCourse_number().equals(theircourse.getCourse_number()) && ourcourse.getSubject().equals(theircourse.getSubject())) {
					mutualCourses.add(ourcourse);
					break;
					
				}
			}
			
		}
		
		
		return mutualCourses;
	}

	public LinkedList<String> getFriendsIn(String course) throws MessageException {
		try {
			LinkedList<String> studentlist = getStudentList(course);
			Map<String,String> myfriends = getMyFriends();
			LinkedList<String> friends = new LinkedList<String>();
			for(Map.Entry<String, String> entry : myfriends.entrySet()){
				friends.add(entry.getValue());
			}
			friends.retainAll(studentlist);
			return friends;
		}
		catch (MessageException e) {
			throw e;
		}
	}
	
	public Integer getNumberofCourseRegistered() {
		PersistenceManager pm = getPersistenceManager();
		List<CourseEntry> results =
				queryForCourseEntry("userID == uid", "String uid","", pm);
		pm.close();
		return results.size();
	}
	
	public Integer getNumberofCourseRegisteredTotal() {
		PersistenceManager pm = getPersistenceManager();
		List<CourseEntry> results =
				queryForCourseEntry("", "String uid","", pm);
		pm.close();
		return results.size();
	}

	// Cleans string and tries to find if the course exists
	// if it does and the user is not already enrolled and the user does not have a schedule conflict
	// adds the course to his schedule
	private void addCourse(String newCourse) throws MessageException{
		String course = cleanCourseString(newCourse);
		PersistenceManager pm = getPersistenceManager();
		try {
			// Try and parse XML for a XMLCourseSection
			XMLCourseSection xcs = xcp.getCourseData(course);

			// Turn the XMLCourseSection into a CourseData
			CourseEntry coursedata;
			if(xcs != null){
				coursedata = XMLCourseSectiontoCourseEntry(xcs);
				coursedata.setUserID(getCurrentUserID());
			}
			else{
				throw new XMLCannotFindCourseException(course);
			}

			// Get the user's current courses
			List<CourseEntry> results =
					queryForCourseEntry("userID == uid", "String uid",getCurrentUserID(), pm);

			// Check if User is already in registered course and if he can add it into his schedule
			for(CourseEntry ce : results){
				XMLCourseSection xmlce = xcp.getCourseData(ce);
				if(ce.equals(coursedata) || xmlce.isScheduleCollision(xcs)){
					throw new CannotAddCourseException(course);
				}
			}      

			// If we've gotten this far, add the new Entry into the database
			pm.makePersistent(coursedata);
		} catch (CannotAddCourseException e) {
			throw new MessageException("You either are already registered in " +
					" or you don't have room" +
					"to be in " + course);
		} catch (XMLCannotFindCourseException e) {
			throw new MessageException("Could not find " + course);
		} catch (XMLNoLongerExistsException e) {
			throw new MessageException("Could not find " + course);
		}
		finally {
			pm.close();
		}
	}

	// Cleans string and tries to find if the course exists
	// if it does and the user is already enrolled
	// removes the course from his schedule
	private void rmCourse(String tobeRemoved) throws MessageException{
		String course = cleanCourseString(tobeRemoved);
		PersistenceManager pm = getPersistenceManager();
		try {
			String[] split = course.split("\\s+");
			String subject = split[0];
			subject.trim();
			String course_number = split[1];
			course_number.trim();
			String section = split[2];
			section.trim();
			if(!subject.matches("[a-zA-Z]{2,4}")
					|| !course_number.matches("[0-6][0-9]{2}[a-zA-Z]?")
					|| !section.matches("[ltLT01234][0-9]([0-9a-zA-Z])")){
				throw new XMLCannotFindCourseException(course);
			}

			int year = getCurrentYear();

			// Get the user's current courses
			List<CourseEntry> results =
					queryForCourseEntry("userID == un", "String un",getCurrentUserID(), pm);

			// Check if User is already in registered course so he can remove it from his schedule
			for(CourseEntry ce : results){
				if(ce.getSubject().equals(subject) &&
						ce.getCourse_number().equals(course_number) &&
						ce.getSection().equals(section) &&
						ce.getYear() == year){
					pm.deletePersistent(ce);
					return;
				}
			}
			throw new NotRegisteredException(course);
		}
		catch (NotRegisteredException e) {
			throw new MessageException("You weren't registered in "+ course +" to begin with");
		} catch (XMLCannotFindCourseException e) {
			throw new MessageException("Could not find " + course);
		}
		finally {
			pm.close();
		}
	}

	private LinkedList<String> getStudentList(CourseEntry ce) {
		PersistenceManager pm = getPersistenceManager();
		// Get all the courses with the subject
		List<CourseEntry> results =
				queryForCourseEntry("subject == s", "String s", ce.getSubject(), pm);
		pm.close();

		// Filter down to the section and course number
		List<CourseEntry> resultsfiltered = new LinkedList<CourseEntry>();
		for(CourseEntry i: results){
			if(isSameSection(i,ce)){
				resultsfiltered.add(i);
			}
		}

		// Return only the student names.
		LinkedList<String> studentsuid = new LinkedList<String>();
		for(CourseEntry i: resultsfiltered){
			studentsuid.add(i.getUserID());
		}
		return this.convertonames(studentsuid);
	}

	// Query
	// eg: queryForCourseEntry("subject == s", "String s", ce.getSubject(), pm);
	private List<CourseEntry>  queryForCourseEntry(String expression,
			String paramdecl, String param, PersistenceManager pm){
		// Get the user's current courses
		Query q = pm.newQuery(CourseEntry.class, expression);
		q.declareParameters(paramdecl);

		@SuppressWarnings("unchecked")
		List<CourseEntry> results = (List<CourseEntry>) q.execute(param);
		return results;
	}

	// Conversion from XMLCourseSection to CourseEntry
	// username is null, as XMLCourseSection does not contain this information.
	private CourseEntry XMLCourseSectiontoCourseEntry(XMLCourseSection xcs) {
		String subject = xcs.getSubject();
		String course_number = xcs.getCourse_number();
		String section = xcs.getSection();
		char season = xcs.getSeason();
		int year = xcs.getYear();
		return new CourseEntry(subject, course_number, section, year, season, null);
	}

	// Conversion from XMLCourseSection to CourseInfo
	// List of student is null, as XMLCourseSection does not contain this information.
	private CourseInfo XMLCourseSectiontoCourseInfo(XMLCourseSection xcs) {
		String subject = xcs.getSubject();
		String course_number = xcs.getCourse_number();
		String section = xcs.getSection();
		char season = xcs.getSeason();
		int year = xcs.getYear();
		LinkedList<TeachingUnit> teachingunits = xcs.getTeachingunits();
		CourseInfo ci = new CourseInfo();
		ci.setCourse_number(course_number);
		ci.setSeason(season);
		ci.setSection(section);
		ci.setSubject(subject);
		ci.setTeachingunits(teachingunits);
		ci.setYear(year);
		return ci;
	}

	private void removeifpossible(CourseInfo ci,
			LinkedList<CourseInfo> ourcourses) {
		for(CourseInfo oci: ourcourses){
			if(this.isSameSection(oci, ci)){
				ourcourses.remove(oci);
				return;
			}
		}
	}

	// Returns true if the courses put in are actually the same sections within the year
	private boolean isSameSection(CourseInfo ce, CourseInfo i) {
		return (i.getSection().equals(ce.getSection())
				&& i.getCourse_number().equals(i.getCourse_number())
				&& i.getSeason() == ce.getSeason() && i.getYear() == ce.getYear());
	}

	// Returns true if the courses put in are actually the same sections within the year
	private boolean isSameSection(CourseEntry oce, CourseEntry ce) {
		return (ce.getSection().equals(oce.getSection())
				&& ce.getCourse_number().equals(oce.getCourse_number())
				&& ce.getSeason() == oce.getSeason() && ce.getYear() == oce.getYear());
	}

	// Cleans a course string to provide consistency within the database
	private String cleanCourseString(String newCourse) {
		return newCourse.toUpperCase();
	}

	private LinkedList<String> stringIntoListOfCourses(String userinput){
		String regex =
				"[a-zA-Z]{2,4}\\s*[0-6][0-9]{2}[a-zA-Z]?\\s*[ltLT01234][0-9][0-9a-zA-Z]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(userinput);
		LinkedList<String> tobereturned = new LinkedList<String>();
		while(m.find()){
			tobereturned.add(m.group());
		}
		return tobereturned;
	}

	// Returns the current year
	private int getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	private HttpSession getSession(){
		return getThreadLocalRequest().getSession();
	}

	private String getCurrentUserID() {
		return (String) getSession().getAttribute("userId");
	}

	private String getCurrentUsername() {
		return (String) getSession().getAttribute("username");
	}

	private Map<String, String> getMyFriends(){
		return (Map<String,String>) getSession().getAttribute("friends");
	}
	
	private LinkedList<String> convertonames(LinkedList<String> listofuids) {
		LinkedList<String> listofnames = new LinkedList<String>();
		UserManagerImpl umi = new UserManagerImpl();
		for(String uid: listofuids){
			String name = umi.getUsername(uid);
			if(name != null){
				listofnames.add(name);
			}
		}
		return listofnames;
	}

	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}



}