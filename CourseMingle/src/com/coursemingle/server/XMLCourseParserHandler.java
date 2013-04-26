package com.coursemingle.server;

import java.util.LinkedList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.coursemingle.shared.Instructor;
import com.coursemingle.shared.Meeting;
import com.coursemingle.shared.TeachingUnit;

public class XMLCourseParserHandler extends DefaultHandler {

	boolean foundnothing = true;

	// Section
	public XMLCourseSection cs;
	public TeachingUnit temp_tu;

	private boolean inTu;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {
		if(qName == "section"){
			cs = new XMLCourseSection();
			for (int i=0; i<attributes.getLength(); i++) {
				// Get names and values for each attribute
				if(attributes.getQName(i) == "key"){
					cs.setSection(attributes.getValue(i));
				}
				else if(attributes.getQName(i) == "activity"){
					cs.setActivity(attributes.getValue(i));
				}
				else if(attributes.getQName(i) == "credits"){
					try{
						cs.setCredit(Integer.parseInt(attributes.getValue(i)));
					}
					catch(Exception e){
						cs.setCredit(0);
					}
				}
				else if(attributes.getQName(i) == "locationcode"){
					cs.setLocationcode(attributes.getValue(i));
				}
			}
		}
		else if(qName == "instructors"){
			cs.setInstructors(new LinkedList<Instructor>());
		}
		else if(qName == "instructor"){
			Instructor ins = new Instructor();
			for (int i=0; i<attributes.getLength(); i++) {
				if(attributes.getQName(i) == "name"){
					ins.setInstructor_name(attributes.getValue(i));
				}
				else if(attributes.getQName(i) == "ubcid"){
					ins.setInstructor_id(Integer.parseInt(attributes.getValue(i)));
				}
			}
			cs.addInstructors(ins);
		}
		else if(qName == "comment"){
			for (int i=0; i<attributes.getLength(); i++) {
				if(attributes.getQName(i) == "text"){
					cs.addComment(attributes.getValue(i));
				}
			}
		}	
		else if(qName == "teachingunit"){
			inTu = true;
			temp_tu = new TeachingUnit();
			for (int i=0; i<attributes.getLength(); i++) {
				// Get names and values for each attribute
				if(attributes.getQName(i) == "termcd"){
					temp_tu.setTermcd(attributes.getValue(i));
				}
				else if(attributes.getQName(i) == "startwk"){
					temp_tu.setStartwk(attributes.getValue(i));
				}
				else if(attributes.getQName(i) == "endwk"){
					temp_tu.setEndwk(attributes.getValue(i));
				}
			}
		}
		else if(qName == "meeting"){
			if(inTu){
				Meeting m = new Meeting();
				for (int i=0; i<attributes.getLength(); i++) {
					if(attributes.getQName(i) == "term"){
						m.setTerm(attributes.getValue(i));
					}
					else if(attributes.getQName(i) == "day"){
						m.setDay(attributes.getValue(i));
					}
					else if(attributes.getQName(i) == "starttime"){
						String[] a = attributes.getValue(i).split(":");
						int t = Integer.parseInt(a[0]) * 60 + Integer.parseInt(a[1]);
						m.setStarttime(t);
					}
					else if(attributes.getQName(i) == "endtime"){
						String[] a = attributes.getValue(i).split(":");
						int t = Integer.parseInt(a[0]) * 60 + Integer.parseInt(a[1]);
						m.setEndtime(t);
					}
					else if(attributes.getQName(i) == "building"){
						m.setBuilding(attributes.getValue(i));
					}
					else if(attributes.getQName(i) == "buildingcd"){
						String code = attributes.getValue(i);
						m.setBuilding(code);
						String address = 
								BuildingCodeAddressMap.getDefaultInstance().getAddress(code);
						m.setBuildingaddress(address);
					}
					else if(attributes.getQName(i) == "roomno"){
						m.setRoomno(attributes.getValue(i));
					}
				}
				temp_tu.addMeeting(m);
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(qName == "teachingunit"){
			cs.addTeachingUnit(temp_tu);
			inTu = false;
		}
	}

	public XMLCourseSection getResult() {
		return cs;
	}

}
