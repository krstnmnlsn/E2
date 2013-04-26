package com.coursemingle.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {

	public void checkLoggedIn(AsyncCallback<Void> callback);
	
	public void logout(AsyncCallback<Void> callback);
	
}
