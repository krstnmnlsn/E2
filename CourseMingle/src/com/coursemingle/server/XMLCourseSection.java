package com.coursemingle.server;

import java.util.LinkedList;

import com.coursemingle.shared.Instructor;
import com.coursemingle.shared.TeachingUnit;

public class XMLCourseSection {

	private String subject;
	private String course_number;
	private int year;
	private char season;

	private String section;
	private String activity;
	int credit;
	private String locationcode;
	private LinkedList<TeachingUnit> teachingunits = new LinkedList<TeachingUnit>();;
	private LinkedList<Instructor> instructors;
	private LinkedList<String> comments = new LinkedList<String>();

	public void addTeachingUnit(TeachingUnit tu){
		teachingunits.add(tu);
	}

	public void addInstructors(Instructor i){
		for(Instructor j: instructors){
			if(j.equals(i)){
				return;
			}
		}
		instructors.add(i);
	}

	public void addComment(String comment){
		comments.add(comment);
	}

	public String getSubject() {
		return subject;
	}

	public String getCourse_number() {
		return course_number;
	}

	public int getYear() {
		return year;
	}

	public char getSeason() {
		return season;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setCourse_number(String course_number) {
		this.course_number = course_number;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setSeason(char season) {
		this.season = season;
	}

	public String getSection() {
		return section;
	}
	public String getActivity() {
		return activity;
	}
	public int getCredit() {
		return credit;
	}
	public String getLocationcode() {
		return locationcode;
	}
	public LinkedList<TeachingUnit> getTeachingunits() {
		return teachingunits;
	}
	public LinkedList<Instructor> getInstructors() {
		return instructors;
	}
	public LinkedList<String> getComments() {
		return comments;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public void setLocationcode(String locationcode) {
		this.locationcode = locationcode;
	}
	public void setTeachingunits(LinkedList<TeachingUnit> teachingunits) {
		this.teachingunits = teachingunits;
	}
	public void setInstructors(LinkedList<Instructor> instructors) {
		this.instructors = instructors;
	}
	public void setComments(LinkedList<String> comments) {
		this.comments = comments;
	}

	public boolean isScheduleCollision(XMLCourseSection xcs) {
		// TODO Auto-generated catch block
		/*
		if(xcs.getYear() != this.getYear() || xcs.getSeason() != this.getSeason()){
			return false;
		}
		
		LinkedList<TeachingUnit> tu1term1= xcs.getTeachingunits();
		LinkedList<TeachingUnit> tu2term1= this.getTeachingunits();
		
		LinkedList<TeachingUnit> tu1term2 = new LinkedList<TeachingUnit>();
		LinkedList<TeachingUnit> tu2term2 = new LinkedList<TeachingUnit>();
		
		LinkedList<TeachingUnit> tu1term12 = new LinkedList<TeachingUnit>();
		LinkedList<TeachingUnit> tu2term12 = new LinkedList<TeachingUnit>();
		
		for(TeachingUnit tu: tu1term1){
			if(tu.getTermcd() == "1-2"){
				tu1term12.add(tu);
				tu1term1.remove(tu);
			}
		}
		for(TeachingUnit tu: tu1term1){
			if(tu.getTermcd() == "2"){
				tu1term2.add(tu);
				tu1term1.remove(tu);
			}
		}
		for(TeachingUnit tu: tu2term1){
			if(tu.getTermcd() == "1-2"){
				tu2term12.add(tu);
				tu2term1.remove(tu);
			}
		}
		for(TeachingUnit tu: tu2term1){
			if(tu.getTermcd() == "2"){
				tu2term2.add(tu);
				tu2term1.remove(tu);
			}
		}
		
		
		LinkedList<Meeting> m1t1= new LinkedList<Meeting>();
		LinkedList<Meeting> m2t1= new LinkedList<Meeting>();
		
		LinkedList<Meeting> m1t2 = new LinkedList<Meeting>();
		LinkedList<Meeting> m2t2 = new LinkedList<Meeting>();
		
		*/
		return false;
	}

}
