package com.coursemingle.client;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserManagerAsync {
	
	void getNumberofUsers(AsyncCallback<Integer> async);
	void register( AsyncCallback<Void> async);
	void getCurrentFriends( AsyncCallback<Map<String,String>> async);
}
