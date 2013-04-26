package com.coursemingle.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;


import com.coursemingle.client.LoginServiceAsync;
import com.coursemingle.shared.CourseInfo;
import com.coursemingle.shared.Meeting;
import com.coursemingle.shared.MessageException;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LatLngCallback;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;

import com.google.gwt.user.client.Timer;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;


public class CourseMingle implements EntryPoint {
	
	private TextArea enterCourses = new TextArea();
	private VerticalPanel navPanel = new VerticalPanel();
	private VerticalPanel coursePanel = new VerticalPanel();
	private VerticalPanel friendPanel = new VerticalPanel();
	private HorizontalPanel statisticsPanel = new HorizontalPanel();
	private HorizontalPanel schedulePanel = new HorizontalPanel();
	private VerticalPanel descriptionPanel = new VerticalPanel();
	final PopupPanel aDialogBox = new PopupPanel();

	private Button addCourse = new Button("Add Courses");
	protected boolean addcourseavailable = true;
	private Anchor displayMySched	= new Anchor("Display Schedule");
	private Anchor learnMore = new Anchor("Learn More");
	private Anchor viewStatisticsFooterLink = new Anchor("View CourseMingle Statistics");
	private Anchor hideLearnMore = new Anchor("Hide Learn More");
	private Anchor hideMySched	= new Anchor("Hide Schedule");
	private ListBox removeCourses = new ListBox();
	private ListBox viewFriendsInCourses = new ListBox();
	private ListBox viewMutualCourses = new ListBox();
	private VerticalPanel removePanel = new VerticalPanel();
	private HashSet<String> removeCourseString = new HashSet<String>();
	private HashSet<String> friendCourseString = new HashSet<String>();
	private Map<String, String> userFriends = new HashMap<String, String>();

	private int REFRESH_INTERVAL = 100; 
	Timer refreshTimer;
	
	private HorizontalPanel courseInputLabelPanel = new HorizontalPanel();
	private Label courseInputLabel = new Label("Copy and paste your registered course-list below.");
	String msg = "<center>You may copy courses from 'UBC Courses' and simply paste them into the text box below. Our system will do the rest! If you have any questions or encounter any issues feel free to drop us a message in the Shoutbox below. Click outside this dialog box to continue.</center><br /><img src='http://1gb.me/310/tutscreen.png' />";
	private Image imgTut = new Image("/tutscreen.png");
	private LoginServiceAsync loginService = GWT.create(LoginService.class);
	private CourseManagerAsync courseService = GWT.create(CourseManager.class);
	private HTMLPanel mySched = new HTMLPanel(mySchedBuilder(initSchedule()));

	private MapWidget map;
	private Geocoder geocoder;
	private String address = "";
	private static final double  initiallat = 49.2661156;
	private static final double initiallong = -123.2457198;

	// Own is 0
	// Someone elses is 1
	// Cross between someone elses and your own is 2
	private byte displaysetting = 0;
	private String friendusername = "";

	// (Else it is term two)
	private boolean istermonedisplay = true;

	
	// ==================================================================
	// ==================================================================
	
	private Button loginButton = new Button("Facebook Login", new ClickHandler() {
		public void onClick(ClickEvent eventf) {
			Location.replace("/coursemingle/auth");
		}
	});
	private Button removeSelected = new Button("Remove Course" , new ClickHandler() {
		public void onClick(ClickEvent event) {
			removeCourse();
		}
	});
	private Button viewFriendinCourseButton = new Button("View Friend(s) in Course" , new ClickHandler() {
		public void onClick(ClickEvent event) {
			viewCourse();
		}
	});
	private Button viewMutualCoursesButton = new Button("View Mutual Course(s)" , new ClickHandler() {
		public void onClick(ClickEvent event) {
			viewMutualCourses();
		}
	});
	private Button logoutButton = new Button("Log Out", new ClickHandler() {
		public void onClick(ClickEvent event) {
			
			// Log user out by inactivating their session then refresh the page
			AsyncCallback<Void> callback = new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					// login failed
				}

				@Override
				public void onSuccess(Void result) {
					Location.replace("/");
				}
			};
			loginService.logout(callback);
		}
	});
	
	
	

	// ==================================================================
	// ==================================================================
	
	public void onModuleLoad() {		
		
		
		
		// Check if user logged in and display appropriate main page
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				// update UI depending on whether user is logged in or not
				isLogged(0);
				loadLogin();
			}

			@Override
			public void onSuccess(Void result) {
				// update UI depending on whether user is logged in or not
				isLogged(1);
				loadMainPage();		
				resetTimer();
			}
		};

		loginService.checkLoggedIn(callback);
		
//		isLogged(1);
//		loadMainPage();
//		resetTimer();
	}
	
	
	// Controls visibility of main page elements, depending on if user is logged in or not.
	private void isLogged(Integer isLoggedInFlag) {
		if (isLoggedInFlag == 0) {
			RootPanel.get("login-with-facebook").setVisible(true);
			RootPanel.get("maps").setVisible(false);
			RootPanel.get("main").setVisible(false);
			RootPanel.get("friend-viewer").setVisible(false);
			RootPanel.get("wrapper1").setVisible(true);
		} else if (isLoggedInFlag == 1) {
			RootPanel.get("login-with-facebook").setVisible(true);
			RootPanel.get("maps").setVisible(true);
			RootPanel.get("main").setVisible(true);
			RootPanel.get("friend-viewer").setVisible(true);
			RootPanel.get("wrapper1").setVisible(true);
			UserManagerAsync userService = GWT.create(UserManager.class);
			userService.register( new AsyncCallback<Void>() {
				public void onSuccess(Void result) {
				}

				public void onFailure(Throwable e) {
				}
			});
			
			
		}
		
		
	}
	

	// Called after the checkedLoggedIn callback returns, loads main page
	private void loadMainPage() {
		
		loadCourseMingle();
		loadMaps();
		updateRemoveListBox(new LinkedList<CourseInfo>());
		updateFriendSection(new LinkedList<CourseInfo>());

		// Setup timer to refresh list automatically.
		refreshTimer = new Timer() {
			@Override 
			public void run() {
				// own is 0
				// someone elses is 1
				// cross between someone elses and your own is 2
				if(displaysetting == 0){
					getMyInfo();
					getUserFriends();
				}
				else if (displaysetting ==1){
					getFriendInfo();
				}
				else if (displaysetting ==2){
					getOurInfo();
				}

				if(REFRESH_INTERVAL < 1000){
					REFRESH_INTERVAL = 2*REFRESH_INTERVAL;
				}
				else{
					REFRESH_INTERVAL = 2147483647;
				}
				refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
			}
		};
	}
	

	private void loadLogin() {
		// Add the login button to the root panel.
		RootPanel.get("login-with-facebook").add(loginButton);
	}


	private void loadCourseMingle() {
		RootPanel.get("main").add(navPanel);
		navPanel.add(displayMySched);
		navPanel.add(hideMySched);
		RootPanel.get("login-with-facebook").add(logoutButton);
		hideMySched.setVisible(false);
		schedulePanel.add(mySched);
		RootPanel.get("main").add(schedulePanel);
		schedulePanel.setVisible(false);

		int left = (Window.getClientWidth() - 0) / 3;
		int top = (Window.getClientHeight() - 0) / 3;
		aDialogBox.setPopupPosition(left, top);
		//aDialogBox.setTitle(title);
		//aDialogBox.setWidget(new Label(text));
		Button ok = new Button ("OK");
		ok.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				aDialogBox.setVisible(false);
			}
		});
		///aDialogBox.add(ok);
		RootPanel.get("main").add(aDialogBox);
		aDialogBox.setVisible(false);
		aDialogBox.setAutoHideEnabled(true);

		displayMySched.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				displayMySched.setVisible(false);
				hideMySched.setVisible(true);
				schedulePanel.setVisible(true);
			}
		});
		

		hideMySched.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				hideMySched.setVisible(false);
				schedulePanel.setVisible(false);
				displayMySched.setVisible(true);
			}
		});

		coursePanel.add(new InlineHTML("<h5>Add Courses</h5>"));
		descriptionPanel.add(courseInputLabelPanel);
		courseInputLabelPanel.add(courseInputLabel);
		descriptionPanel.add(new InlineHTML("<p>")); // add some whitespace
		descriptionPanel.add(learnMore);
		descriptionPanel.add(hideLearnMore);
		hideLearnMore.setVisible(false);
		HorizontalPanel innerLearnMorePanel = new HorizontalPanel();
		final HTML innerLearnMore = new HTML("<p>You may copy courses from 'UBC Courses' and simply paste them into the text box below. Our system will do the rest! If you have any questions or encounter any issues feel free to drop us a message in the Shoutbox below.</p><br /><img src='/tutscreen.png' />");
		innerLearnMorePanel.add(innerLearnMore);
		innerLearnMore.setVisible(false);
		descriptionPanel.add(innerLearnMorePanel);
		descriptionPanel.add(new InlineHTML("</p>")); // add some whitespace
		descriptionPanel.setWidth(Double.toString(Window.getClientWidth() * 0.5));
		coursePanel.add(enterCourses);
		coursePanel.add(addCourse);
		RootPanel.get("main").add(descriptionPanel);
		RootPanel.get("main").add(coursePanel);
		RootPanel.get("main").add(removePanel);

		// Move cursor focus to the input box.
		enterCourses.setFocus(false);
		enterCourses.setCharacterWidth(60);
		
		
		learnMore.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event){
				learnMore.setVisible(false);
				innerLearnMore.setVisible(true);
				hideLearnMore.setVisible(true);
			}
		});
		
		hideLearnMore.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event){

				innerLearnMore.setVisible(false);
				learnMore.setVisible(true);
				hideLearnMore.setVisible(false);
			}
		});
		
		
		statisticsPanel.add(viewStatisticsFooterLink);
		RootPanel.get("view-statistics-footer").add(statisticsPanel);
		viewStatisticsFooterLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				viewStatisticsFooterLink.setText("Loading...");
				
				//getNumberofUsers
				UserManagerAsync userService = GWT.create(UserManager.class);
				userService.getNumberofUsers(new AsyncCallback<Integer>() {
					
					
					@Override
					public void onFailure(Throwable caught) {	
						//Window.alert("getNumberofCourseRegistered Failed");
					}

					@Override
					public void onSuccess(Integer result) {
						String response = "";
						response+= "Total number of registered users: " + Integer.toString(result);
						viewStatisticsFooterLink.setText(response);
						
						
					}
					
				});
				
			}
			
		});
		
		
		RootPanel.get("friend-viewer").add(friendPanel);

		addCourse.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String courses = enterCourses.getText();
				String response;
				if(courses.trim().equals("")) {
					response =  "Invalid input; please enter your course list copied form UBC Course services. ";
					response+= "Click outside this dialog to continue.";
					aDialogBox.setTitle("Error");
					aDialogBox.setWidget(new Label(response));
					aDialogBox.setVisible(true);
					aDialogBox.center();
				} else {
					addCourse(courses);
				}
			}
		});
	}

	private void loadMaps(){
		Maps.loadMapsApi("AIzaSyAKwedUeyr3NTMWQumb_54lt4tu41UzbTE", "2", false, new Runnable() {
			public void run() {
				geocoder = new Geocoder();
				LatLng centreaddress = LatLng.newInstance(initiallat,initiallong); 
				map = new MapWidget(centreaddress, 15);
				map.setSize((Window.getClientWidth() *0.75 - 10)+"px", "20em");
				map.setZoomLevel(14);
				map.addControl(new LargeMapControl());

				map.setCenter(centreaddress);
				RootPanel.get("maps").add(map);
				
				Window.addResizeHandler(new ResizeHandler() {
					@Override
					public void onResize(ResizeEvent event) {
						map.setSize((Window.getClientWidth() *0.75 - 10)+"px", "20em");
						}
					});
			}
		});
	}

	private void getMyInfo(){
		courseService.getCurrentCourses(new AsyncCallback<LinkedList<CourseInfo>>() {
			@Override
			public void onSuccess(LinkedList<CourseInfo> cis) {
				updateSchedule(cis);
				updateRemoveListBox(cis);
				updateMap(cis);
				updateFriendSection(cis);
			}
			public void onFailure(Throwable caught) {}
		});
	}
	
	private void getFriendInfo(){
		courseService.getCurrentCourses(new AsyncCallback<LinkedList<CourseInfo>>() {
			@Override
			public void onSuccess(LinkedList<CourseInfo> cis) {
				updateSchedule(cis);
				updateRemoveListBox(new LinkedList<CourseInfo>());
				updateMap(cis);
				updateFriendSection(cis);
			}
			public void onFailure(Throwable caught) {}
		});
	}
	
	private void getOurInfo(){
		courseService.getCommonCourses(friendusername, new AsyncCallback<LinkedList<CourseInfo>>() {
			@Override
			public void onSuccess(LinkedList<CourseInfo> cis) {
				updateSchedule(cis);
				updateRemoveListBox(new LinkedList<CourseInfo>());
				updateMap(cis);
				updateFriendSection(cis);
			}
			public void onFailure(Throwable caught) {}
		});
	}
	
	private void  getUserFriends() {
		UserManagerAsync userService = GWT.create(UserManager.class);
		
		userService.getCurrentFriends(new AsyncCallback<Map<String,String>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Map<String, String> result) {
				userFriends = result;
				
			}
			
		});
	}

	private void addCourse(String courses) {
		// Initialize the service proxy.
		if (courseService == null) {
			courseService = GWT.create(CourseManager.class);
		}
		if(addcourseavailable){
			addcourseavailable = false;
			courseService.addCourses(courses, new AsyncCallback<Void>() {
				public void onSuccess(Void result) {
					// do some UI stuff to show success
					String response =  "The course(s) you entered were submitted successfully. ";
					response+= "Click outside this dialog to continue.";
					aDialogBox.setTitle("Success");
					aDialogBox.setWidget(new Label(response));
					aDialogBox.setVisible(true);
					aDialogBox.center();
					addcourseavailable = true;
					resetTimer();
				}

				public void onFailure(Throwable e) {
					// do some UI stuff to show failure
					String response =  "Oops. An error occured while submitting your course information. Please check your input. ";
					//response+=((MessageException)e).getErrorMessage();
					response+= "Click outside this dialog to continue.";
					aDialogBox.setTitle("Error");
					aDialogBox.setWidget(new Label(response));
					aDialogBox.setVisible(true);
					aDialogBox.center();
					addcourseavailable = true;
					resetTimer();
				}
			});
		}
		else{
			String response =  "Please wait for the your first submit to finish. ";
			response+= "Click outside this dialog to continue.";
			aDialogBox.setTitle("Slow Down");
			aDialogBox.setWidget(new Label(response));
			aDialogBox.setVisible(true);
			aDialogBox.center();
		}
	}

	private void removeCourse() {
		Integer selectedIndex = removeCourses.getSelectedIndex();
		String itemSelected = removeCourses.getItemText(selectedIndex);

		if (removeCourses.getSelectedIndex() == 0) {
			String response =  "Please make a selection! ";
			response+= "Click outside this dialog to continue.";
			aDialogBox.setTitle("Error");
			aDialogBox.setWidget(new Label(response));
			aDialogBox.setVisible(true);
			aDialogBox.center();
			return;
		}

		// Initialize the service proxy.
		if (courseService == null) {
			courseService = GWT.create(CourseManager.class);
		}
		courseService.rmCourses(itemSelected, new AsyncCallback<Void>() {
			public void onSuccess(Void result) {
				String response =  "The course selected was removed successfully. ";
				response+= "Click outside this dialog to continue.";
				aDialogBox.setTitle("Success");
				aDialogBox.setWidget(new Label(response));
				aDialogBox.setVisible(true);
				aDialogBox.center();
			}

			public void onFailure(Throwable e) {
				String response =  "Oops. An error occured while trying to remove the selected course.";
				//response+=((MessageException)e).getErrorMessage();
				response+= "Click outside this dialog to continue.";
				aDialogBox.setTitle("Error");
				aDialogBox.setWidget(new Label(response));
				aDialogBox.setVisible(true);
				aDialogBox.center();
			}
		});
		resetTimer();
	}


	// ==================================================================
	// ==================================================================
	
	// CALL THIS WHENEVER YOU WANT SOMETHING TO HAPPEN
	private void resetTimer() {
		REFRESH_INTERVAL= 200;
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
	}


	// ==================================================================
	// ==================================================================
	
	private void updateSchedule(LinkedList<CourseInfo> cis) {
		String[] sched = new String[115];

		String termtocheck = "2";
		if(istermonedisplay){
			termtocheck = "1";
		}

		for (CourseInfo c : cis) {
			LinkedList<Meeting> meetings = c.getTeachingunits().getFirst().getMeetings();

			for(Meeting m: meetings) {
				if(m.getTerm().equals(termtocheck)){

					String day = m.getDay();
					String valInArray = c.toHalfName();

					int start = ((m.getStarttime()-480)/30)*5;
					int end = ((m.getEndtime()-480)/30)*5;
					int offset = -1;

					if(day.equals("Mon")) {
						//dayNum = 0;
						offset = 0;
					} else if (day.equals("Tue")) {
						//dayNum = 1;
						offset = 1;
					} else if (day.equals("Wed")) {
						//dayNum = 2;
						offset = 2;
					} else if (day.equals("Thu")) {
						//dayNum = 3;
						offset = 3;
					} else if (day.equals("Fri")) {
						//dayNum = 4;
						offset = 4;
					}

					if (offset != -1) {
						start += offset;
						end += offset;

						for(int i = start; i<end;i+=5){
							sched[i] = valInArray;
						}
					}
				}
			}
		}

		//private HTMLPanel mySched = new HTMLPanel(mySchedBuilder(myScheduleString));
		schedulePanel.clear();
		schedulePanel.add(new HTMLPanel(mySchedBuilder(sched)));
	}

	private void updateRemoveListBox(LinkedList<CourseInfo> cis) {
		removePanel.clear();
		removeCourses.clear();
		removeCourseString = new HashSet<String>();

		for (CourseInfo c : cis) {
			removeCourseString.add(c.toFullName());
		}

		removeCourses.addItem("Choose a course to remove.");

		for(String course : removeCourseString) {
			removeCourses.addItem(course);
		}

		HorizontalPanel titlePanel = new HorizontalPanel();
		titlePanel.add(new InlineHTML("<br /><h5>Remove Courses</h5>"));
		removePanel.add(titlePanel);
		VerticalPanel selectPanel = new VerticalPanel();
		selectPanel.add(removeCourses);
		removePanel.add(selectPanel);
		removePanel.add(removeSelected);
		removePanel.add(new InlineHTML("<br/>"));
	}


	// ==================================================================
	// ==================================================================
	
	private void updateMap(LinkedList<CourseInfo> cis){
		map.clearOverlays();

		String termtocheck = "2";
		if(istermonedisplay){
			termtocheck = "1";
		}

		final Map<String,String> stufftoplot = new TreeMap<String,String>();
		for(CourseInfo ci : cis){
			LinkedList<Meeting> meetings = ci.getTeachingunits().getFirst().getMeetings();
			for(final Meeting m: meetings) {
				if(m.getTerm().equals(termtocheck)){
					String entries = stufftoplot.get(m.getBuildingaddress());
					String string = ci.toFullName();
					if(entries == null){
						entries = string;
					}
					else if(entries.contains(string)){
						//do nothing
					}
					else{
						entries 
						= entries + ", " + string;
					}
					stufftoplot.put(m.getBuildingaddress(), entries);
				}
			}
		}

		for(final String addr : stufftoplot.keySet()){
			Maps.loadMapsApi("AIzaSyAKwedUeyr3NTMWQumb_54lt4tu41UzbTE", "2", false, new Runnable() {
				public void run() {
					geocoder.getLatLng(addr, new LatLngCallback(){

						@Override
						public void onFailure() {
						}

						@Override
						public void onSuccess(LatLng point) { 		

							MarkerOptions markerOptions = MarkerOptions.newInstance();
							markerOptions.setTitle(stufftoplot.get(addr));
							Marker marker=new Marker(point, markerOptions);
							map.addOverlay(marker);
						}
					});
				}
			});
		}
	}

	

	// ==================================================================
	// ==================================================================
	
	// Constructs html for user schedule
	private String mySchedBuilder(String[] sched) {

		// Open pre-existing html file for additions

		String output = "";

		output+="<table id=\"rounded-corner\" summary=\"Time Table\"><thead><tr><th scope=\"col\" class=\"rounded-left\"></th><th scope=\"col\" class=\"rounded\">Monday</th><th scope=\"col\" class=\"rounded\">Tuesday</th><th scope=\"col\" class=\"rounded\">Wednesday</th><th scope=\"col\" class=\"rounded\">Thursday</th><th scope=\"col\" class=\"rounded-right\">Friday</th></tr></thead><tfoot><tr><td colspan=\"5\" class=\"rounded-foot-left\"><em></em></td><td class=\"rounded-foot-right\">&nbsp;</td></tr></tfoot><tbody>";

		// Would want to grab data about the user's own schedule here,
		// I'll just make up a couple values for now:
		double startingTime = 8;
		double endingTime = 19;
		String timeString;
		String fill;

		// For each hour the user is at school print an extra row in the table
		// (sorry about the strange indexing):
		int counter = 0;
		for (double time=startingTime-1; time<=endingTime-1; time=time+0.5){

			// Construct a string containing the correct hour for this row:
			timeString = Integer.toString((((int) time) % 12) + 1);
			if ((int) time == time){
				timeString = timeString + ":00";
			} else {
				timeString = timeString + ":30";
			}

			// Print table row 
			output+=("<tr><td class=\"leftcol\">" + timeString + "</td>");

			int numDay = 0;
			while (numDay < 5) {

				fill = sched[counter];

				if(fill == null) {
					fill = " ";
				} 

				output+=("<td class=\"timeblk\">"+fill+"</td>");
				numDay++;
				counter++;
			}

			output+=("</tr>");
		}

		// Print out closing of elements.
		output+=("</tbody></table></body></html>");

		//Window.alert(Integer.toString(scheduleItr));
		return output;
	}

	private String[] initSchedule() {
		String[] sched = new String[115];
		int c = 0;

		while(c<115) {
			sched[c] = " ";
			c++;
		}
		return sched;
	}

	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
	}
	
	
	
	private void viewFriend(String friendIdentifier) {
		RootPanel.get("maps").clear();
		RootPanel.get("main").clear();
		
		

	}
	
	private void updateFriendSection(LinkedList<CourseInfo> cis) {
		
		friendPanel.clear();
		friendPanel.add(new InlineHTML("<h5>View Friends In...</h5>"));
				
		viewFriendsInCourses.clear();
		friendCourseString = new HashSet<String>();
		
		for (CourseInfo c : cis) {
			friendCourseString.add(c.toFullName());
		}

		for(String course : removeCourseString) {
			viewFriendsInCourses.addItem(course);
		}
		
		friendPanel.add(viewFriendsInCourses);
		friendPanel.add(viewFriendinCourseButton);
		
		friendPanel.add(new InlineHTML("<br />"));
		friendPanel.add(new InlineHTML("<h5>Common Courses With...</h5>"));
		
		viewMutualCourses.clear();
		
//		for(String friend : userFriends) {
//			viewMutualCourses.addItem(friend);
//			viewMutualCourses.addItem(item, value)
//		}
		
		
		Iterator it = userFriends.entrySet().iterator();
				
		
		while(it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry) it.next();
			viewMutualCourses.addItem((String)pairs.getValue(), (String)pairs.getKey());
		}
		
		
		
		friendPanel.add(viewMutualCourses);
		friendPanel.add(viewMutualCoursesButton);
		
		
	}
	
	private void viewCourse() {
		Integer selectedIndex = viewFriendsInCourses.getSelectedIndex();
		String itemSelected = viewFriendsInCourses.getItemText(selectedIndex);		
		
		courseService.getFriendsIn(itemSelected, new AsyncCallback<LinkedList<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				String response =  "Well this is embarassing, an error occurred. Please try again. ";
				response+= "Click outside this dialog to continue.";
				aDialogBox.setTitle("Error");
				aDialogBox.setWidget(new Label(response));
				aDialogBox.setVisible(true);
				aDialogBox.center();
				
			}

			@Override
			public void onSuccess(LinkedList<String> result) {
				String response =  "Your friends in this course are: ";
	
				
				for (String friend : result) {
					response+= friend + ", ";
				}
				
				if (result.isEmpty()) {
					response = "You have no Facebook friends registered in this course. Invite more of your friends to CourseMingle! ";
				}
				
				response+= "Click outside this dialog to continue.";
				
				aDialogBox.setTitle("Error");
				aDialogBox.setWidget(new Label(response));
				aDialogBox.setVisible(true);
				aDialogBox.center();
				
			}
			
		});			
	}
	
	
	private void viewMutualCourses() {
		Integer selectedIndex = viewMutualCourses.getSelectedIndex();
		//String itemSelected = viewMutualCourses.getItemText(selectedIndex);	
		String itemSelected = viewMutualCourses.getValue(selectedIndex);

		courseService.getCommonCourses(itemSelected, new AsyncCallback<LinkedList<CourseInfo>>() {

			@Override
			public void onFailure(Throwable caught) {
				String response =  "Well this is embarassing, an error occurred. Please try again. ";
				response+= "Click outside this dialog to continue.";
				aDialogBox.setTitle("Error");
				aDialogBox.setWidget(new Label(response));
				aDialogBox.setVisible(true);
				aDialogBox.center();
				
			}

			@Override
			public void onSuccess(LinkedList<CourseInfo> result) {
				String response = "The courses you have in common are: ";
				
				for(CourseInfo ci : result) {
					response += ci.getSubject() + " " + ci.getCourse_number() + " " + ci.getSection() + ", ";
				}
				
				
				if (result.isEmpty()) {
					response = "You have no courses in common with this friend. ";
				}
				
				response += "Click outside this dialog box to continue.";
				
				aDialogBox.setTitle("Error");
				aDialogBox.setWidget(new Label(response));
				aDialogBox.setVisible(true);
				aDialogBox.center();
				
			}
			
		});
	}
}

