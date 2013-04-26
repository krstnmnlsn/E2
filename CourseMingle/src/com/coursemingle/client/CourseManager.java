package com.coursemingle.client;

import java.util.LinkedList;
import java.util.Map;

import com.coursemingle.shared.CourseInfo;
import com.coursemingle.shared.MessageException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("coursemanager")
public interface CourseManager extends RemoteService {
	  public void addCourses(String newCourse) throws MessageException;
	  public void rmCourses(String tobeRemoved) throws MessageException;
	  public LinkedList<CourseInfo> getCurrentCourses();
	  public LinkedList<CourseInfo> getCurrentCourses(String username);
	  public LinkedList<String> getStudentList(String course) throws MessageException;
	  public LinkedList<CourseInfo> getCommonCourses(String userID);
	  public LinkedList<String> getFriendsIn(String course) throws MessageException;
	  
	  public Integer getNumberofCourseRegistered();
}