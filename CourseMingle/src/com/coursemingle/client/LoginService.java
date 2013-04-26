package com.coursemingle.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
		
	public void checkLoggedIn() throws Exception;

	public void logout();
	
}
