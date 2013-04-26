package com.coursemingle.shared;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TeachingUnit implements IsSerializable {

	// 1, 2, or 1-2 are the ones i've seen
	private String termcd;
	// eg. Sep 04, 2012"
	private String startwk;
	private String endwk;
	
	private LinkedList<Meeting> meetings = new LinkedList<Meeting>();
	
	public String getTermcd() {
		return termcd;
	}
	public String getStartwk() {
		return startwk;
	}
	public String getEndwk() {
		return endwk;
	}
	public LinkedList<Meeting> getMeetings() {
		return meetings;
	}
	public void setTermcd(String termcd) {
		this.termcd = termcd;
	}
	public void setStartwk(String startwk) {
		this.startwk = startwk;
	}
	public void setEndwk(String endwk) {
		this.endwk = endwk;
	}
	public void setMeetings(LinkedList<Meeting> metings) {
		meetings = metings;
	}
	
	public void addMeeting(Meeting meeting){
		for(Meeting m: meetings){
			if( m.equals(meeting)){
				return;
			}
		}
			meetings.add(meeting);
	}
	
}
