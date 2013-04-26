package com.coursemingle.server;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class CourseEntry {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String subject;
	@Persistent
	private String course_number;
	@Persistent
	private String section;
	@Persistent
	private int year;
	@Persistent
	private char season;
	@Persistent
	private String userID;
	@Persistent
	private Date createdate;
	
	public CourseEntry() {
		this.createdate = new Date();
	}

	public CourseEntry(String subject, String course_number, 
			String section, int year, char season, String username) {
		this.subject = subject;
		this.course_number = course_number;
		this.section = section;
		this.year = year;
		this.season = season;
		this.userID = username;
		this.createdate = new Date();
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public void setUserID(String username) {
		this.userID =username;
	}

	public Long getId() {
		return id;
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

	public String getUserID() {
		return userID;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CourseEntry other = (CourseEntry) obj;
		if (course_number == null) {
			if (other.course_number != null)
				return false;
		} else if (!course_number.equals(other.course_number))
			return false;
		if (section == null) {
			if (other.section != null)
				return false;
		} else if (!section.equals(other.section))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (season != other.season)
			return false;
		if (userID == null) {
			if (other.userID != null)
				return false;
		} else if (!userID.equals(other.userID))
			return false;
		if (year != other.year)
			return false;
		return true;
	}

}