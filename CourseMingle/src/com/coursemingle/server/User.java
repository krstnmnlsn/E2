package com.coursemingle.server;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class User {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String userID;
	@Persistent
	private String username;
	@Persistent
	private Date createdate;
	
	public User() {
		this.createdate = new Date();
	}

	public String getUserID() {
		return userID;
	}

	public String getUsername() {
		return username;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}


}