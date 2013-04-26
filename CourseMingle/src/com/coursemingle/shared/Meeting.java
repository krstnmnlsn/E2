package com.coursemingle.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Meeting implements IsSerializable {
	
	// 1,2,or 1-2
	private String term;
	
	// "Mon" "Tue" "Wed" "Thu" "Fri"
	// i couldn't find sat and sun...
	private String day;
	
	// in minutes
	// eg 1:00 = 60
	// eg 2:30 = 150
	private int starttime;
	private int endtime;
	
	private String building;
	private String buildingcd;
	private String buildingaddress;
	
	private String roomno;
	
	public String getTerm() {
		return term;
	}
	public String getDay() {
		return day;
	}
	public int getStarttime() {
		return starttime;
	}
	public int getEndtime() {
		return endtime;
	}
	public String getBuilding() {
		return building;
	}
	public String getRoomno() {
		return roomno;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public void setStarttime(int t) {
		this.starttime = t;
	}
	public void setEndtime(int t) {
		this.endtime = t;
	}
	public void setBuilding(String building) {
		this.building = building;
	}
	public void setRoomno(String roomno) {
		this.roomno = roomno;
	}
	public String getBuildingcd() {
		return buildingcd;
	}
	public String getBuildingaddress() {
		return buildingaddress;
	}
	public void setBuildingcd(String buildingcd) {
		this.buildingcd = buildingcd;
	}
	public void setBuildingaddress(String buildingaddress) {
		this.buildingaddress = buildingaddress;
	}
}
