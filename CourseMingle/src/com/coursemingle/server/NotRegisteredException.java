package com.coursemingle.server;

@SuppressWarnings("serial")
public class NotRegisteredException extends Exception {

	private String course;

	public NotRegisteredException(String tobeRemoved) {
		this.course = tobeRemoved;
	}

	public String getCourse(){
		return course;
	}
}