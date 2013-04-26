package com.coursemingle.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@SuppressWarnings("serial")
@RemoteServiceRelativePath("auth/")
public class FBAuth extends HttpServlet {

	// Fields from our facebook app to be used in communication with facebook    
    private static final String secret = "3ae7a83619ad65d8a9f137f1af3d67dc";
    private static final String client_id = "442460755805006"; 
    // This is the list of permissions that we would like, for all options see:
    // https://developers.facebook.com/docs/reference/login/extended-permissions/
    private static final String[] perms = new String[] {"publish_stream", "email"};
    // Redirect url should point back to this servlet so that facebook information 
    // is returned to be parsed here.
    private static final String redirect_uri = "http://www.coursemingle2.appspot.com/coursemingle/auth/"; 
    //private static final String redirect_uri = "http://localhost:8888/coursemingle/auth/"; 
    
    // This method is called when users are redirected here on clicking the 'login with facebook'
    // button on the front page.  The method first checks for a "?code=" in the url (there isn't
    // one when the user is first sent here and so we know to redirect them to facebook-login), 
    // and then if the code parameter exists uses that to ask facebook for the desired user-info.
    // After user-info is gathered/checked the user's session is marked as logged in (this may not
    // be a very secure way of doing things) and the user is re-directed back to the home page.
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	
    	// Getting the session, or making a new one if it doesn't already exist.
    	// response.setContentType("text/html");
        HttpSession session = request.getSession(true);
    	
        // Checking if returning from facebook with a code parameter or coming for 
        // the first time from the home page.
        String code = request.getParameter("code");
        if (code == null || code.equals("")) {
        	// Send to facebook login.
        	response.sendRedirect(getLoginRedirectURL());
        } else {
        
	        // Returning from facebook case.  First getting the token from facebook then 
	        // using that to grab all the required user-information
	        String token = null;
	        try {
	            String g = getAuthURL(code);
	            URL u = new URL(g);
	            URLConnection c = u.openConnection();
	            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
	            String inputLine;
	            StringBuffer b = new StringBuffer();
	            while ((inputLine = in.readLine()) != null)
	                b.append(inputLine + "\n");            
	            in.close();
	            token = b.toString();
	            if (token.startsWith("{"))
	                throw new Exception("error on requesting token: " + token + " with code: " + code);
	        } catch (Exception e) {
	                // An error occurred but I'm not sure what to do about it.  
	        		// Leaving it silent like this might be bad ...
	        }

	        // The graph will be a string of all user information asked for.
	        String graph = null;
	        try {
	            String g = "https://graph.facebook.com/me?" + token;
	            URL u = new URL(g);
	            URLConnection c = u.openConnection();
	            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
	            String inputLine;
	            StringBuffer b = new StringBuffer();
	            while ((inputLine = in.readLine()) != null)
	                b.append(inputLine + "\n");            
	            in.close();
	            graph = b.toString();
	        } catch (Exception e) {
	                // Again, could stand to add some error handling here.
	        }

	        // Also grab friends
	        FriendJsonParser parser = new FriendJsonParser();
	        HashMap<String,String> friends = parser.parse("https://graph.facebook.com/me/friends?limit=5000&" + token);

	        String facebookId;
	        String firstName;
	        String middleNames;
	        String lastName;
	        String email;

	        // Now extracting user information, there is probably a better way of doing this ...
	        // Should also add interactions with our data here right?  Check if user is already 
	        // registered or needs to be added.  
	        facebookId = graph.substring( graph.indexOf("id") + 5, graph.indexOf("name") - 3);
	        firstName = graph.substring( graph.indexOf("first_name") + 13, graph.indexOf("last_name") - 3 );
	        lastName = graph.substring( graph.indexOf("last_name") + 12, graph.indexOf("link") - 3);       

	        // Marking the user as logged in by adding their own facebookId to the session.  
	        // The method checkLoggedIn in LoginService currently checks this field of the session 
	        // just to see if null or not.  Can change that to identify user and present them with 
	        // the correct schedule.
        	session.setAttribute("userId", facebookId );
        	session.setAttribute("username", firstName + " " + lastName);
	        session.setAttribute("friends", friends);
        	
        	// Return the user to the homepage.
   	        response.sendRedirect("http://www.coursemingle2.appspot.com/"); 
        }
      }
   
    
    

   // A method to generate the first url we visit at facebook to grab the token.
   public String getLoginRedirectURL() {
    	String URL = "https://graph.facebook.com/oauth/authorize?client_id=" + 
        client_id + "&display=page&redirect_uri=" +  redirect_uri+"&scope=";
    	// not sure if this is the ideal way to go about this:
    	for (int i=0; i<perms.length; i++) URL = URL+","+perms[i];
    	return URL;
    }
    
    // A method to generate the 2nd url we visit at facebook with our token to grab user info.
    public String getAuthURL(String authCode) {
        return "https://graph.facebook.com/oauth/access_token?client_id=" + 
        client_id+"&redirect_uri=" + redirect_uri+"&client_secret="+secret+"&code="+authCode;
    }
    


}