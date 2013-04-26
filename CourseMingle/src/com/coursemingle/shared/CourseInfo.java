package com.coursemingle.shared;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CourseInfo implements IsSerializable {

	
	// The ball that is thrown back and forth from manager to client
	// Season is one of: S or W
	// Any location needed is within the teaching units' meetings
	// Some courses have different locations depending on day of the week
	private String subject;
	private String course_number;
	private String section;
	private int year;
	private char season;
	private LinkedList<String> students;
	private LinkedList<TeachingUnit> teachingunits;

	public CourseInfo(){
	}
	
	public String toHalfName(){
		return getSubject() + " " + getCourse_number();
	}
	
	public String toFullName(){
		return getSubject() + " " + getCourse_number() + " " + getSection();
	}
	
	public LinkedList<TeachingUnit> getTeachingunits() {
		return teachingunits;
	}

	public void setTeachingunits(LinkedList<TeachingUnit> teachingunits) {
		this.teachingunits = teachingunits;
	}

	public String getSubject() {
		return subject;
	}

	public String getCourse_number() {
		return course_number;
	}

	public String getSection() {
		return section;
	}

	public int getYear() {
		return year;
	}

	public char getSeason() {
		return season;
	}

	public LinkedList<String> getStudents() {
		return students;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setCourse_number(String course_number) {
		this.course_number = course_number;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setSeason(char season) {
		this.season = season;
	}

	public void setStudents(LinkedList<String> students) {
		this.students = students;
	}

}