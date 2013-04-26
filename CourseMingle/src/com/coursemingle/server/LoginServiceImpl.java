package com.coursemingle.server;

import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.coursemingle.client.LoginService;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LoginServiceImpl extends RemoteServiceServlet implements
		LoginService {
	
	
	public void checkLoggedIn() throws Exception {
		
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		
		if (session.getAttribute("userId") == null){
			throw new Exception();
		}
		
	}
	
	
	public void logout() {
		
		  getThreadLocalRequest().getSession().invalidate();
		  
	}
	
	
	
}
