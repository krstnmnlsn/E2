package com.coursemingle.server;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FriendJsonParser {

	public HashMap<String,String> parse(String url) {
		HashMap<String,String> returnlist = new HashMap<String,String>();
		try {
			String input;
			try{
				// Try to get the data from a url
				InputStream is = new URL(url).openStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

				StringBuilder stringbuilder = new StringBuilder();
				int charpointer;
				while ((charpointer = rd.read()) != -1) {
					stringbuilder.append((char) charpointer);
				}
				input = stringbuilder.toString();
			}
			catch(Exception e){
				// couldn't get data so just return an empty dataset
				return returnlist;
			}

			// parse the data
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(input);

			final JSONObject jsonObject = (JSONObject) obj;
			final JSONArray listoffriends = (JSONArray) jsonObject.get("data");

			// Base case: jsonstuff is empty
			if(listoffriends.size() == 0){
				return returnlist;
			}
			// Else iterate through jsonstuff and then follow the next pointer to more friends
			else{
				for(int i =0; i<listoffriends.size();i++){
					try{
						final JSONObject friend = (JSONObject) listoffriends.get(i);
						String name = (String) friend.get("name");
						String id = (String) friend.get("id");
						returnlist.put(id, name);
					}
					catch(Exception e){}
				}
				final JSONObject paging = (JSONObject) jsonObject.get("paging");
				String next = (String) paging.get("next");

				returnlist.putAll(parse(next));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return returnlist;
	}

}