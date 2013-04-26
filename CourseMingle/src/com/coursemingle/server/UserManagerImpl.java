package com.coursemingle.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.servlet.http.HttpSession;

import com.coursemingle.client.UserManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class UserManagerImpl extends RemoteServiceServlet implements UserManager{

	private static final PersistenceManagerFactory PMF =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	public Integer getNumberofUsers(){
		PersistenceManager pm = getPersistenceManager();
		try {
			List<User> results =
					queryForUser("userID != uid", "String uid",getCurrentUserID(), pm);
			return results.size() + 1;
		} 
		finally {
			pm.close();
		}
	}
	
	public String getUsername(String userid){
		PersistenceManager pm = getPersistenceManager();
		try {
			List<User> results =
					queryForUser("userID == uid", "String uid",userid, pm);
			String name = "";
			for(User u: results){
				name = u.getUsername();
			}
			return name;
		} 
		finally {
			pm.close();
		}
	}

	public void register(){
		PersistenceManager pm = getPersistenceManager();
		String userID = this.getCurrentUserID();
		String username = this.getCurrentUsername();
		try {
			List<User> results =
					queryForUser("userID == ui", "String ui",getCurrentUserID(), pm);
			User u;
			if(results.isEmpty()){
				u= new User();
				u.setUserID(userID);
				u.setUsername(username);
			}
			else{
				u = results.get(0);
				u.setUsername(username);
			}
			pm.makePersistent(u);
		} 
		finally {
			pm.close();
		}
	}

	// Query
	// eg: queryForUserEntry("userID == ui", "String un", getCurrentUserID, pm);
	private List<User>  queryForUser(String expression,
			String paramdecl, String param, PersistenceManager pm){
		Query q = pm.newQuery(User.class, expression);
		q.declareParameters(paramdecl);

		@SuppressWarnings("unchecked")
		List<User> results = (List<User>) q.execute(param);
		return results;
	}

	private HttpSession getSession(){
		return getThreadLocalRequest().getSession();
	}

	private String getCurrentUserID() {
		return (String) getSession().getAttribute("userId");
	}

	private String getCurrentUsername() {
		return (String) getSession().getAttribute("username");
	}
	
	public Map<String,String> getCurrentFriends() {
		Map<String, String> nameValPair = (Map<String,String>) getSession().getAttribute("friends");
		Map<String, String> retFriends = new HashMap<String, String>();
		PersistenceManager pm = getPersistenceManager();
		List<User> friendReturned = queryForUser("userID != ui", "String ui", getCurrentUserID(), pm);
		
		
		Iterator it = nameValPair.entrySet().iterator();
		
		for (User friendRet : friendReturned) {
			while(it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry) it.next();
				String uid = (String)pairs.getKey();
				String username = (String)pairs.getValue();
				
				if (friendRet.getUserID().equals(uid)) {
					retFriends.put(uid, username);
				}
				
			}
		}
		
		
		
		return retFriends;
		
		//return nameValPair;
	}

	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

}