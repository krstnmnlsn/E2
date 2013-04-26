package com.coursemingle.client;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("usermanager")
public interface UserManager extends RemoteService {
	public Integer getNumberofUsers();
	public void register();
	public Map<String,String> getCurrentFriends();

}