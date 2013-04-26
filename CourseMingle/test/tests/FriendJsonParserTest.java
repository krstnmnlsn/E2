package tests;

import java.util.Map;
import org.junit.Test;

import com.coursemingle.server.FriendJsonParser;

public class FriendJsonParserTest {

	
	String input = "https://graph.facebook.com/me/friends?access_token=AAACEdEose0cBAHKuj1BDtaCURIvFzCx7UKMqbo1S1pT9Pz6sGuRDbQa0fDFgym4ZA57h9VPUDX1IbYZCVtb1oVkuA5f5KQzPmbfTeQOgZDZD";


	@SuppressWarnings("unused")
	@Test
	public void Test0() {
		FriendJsonParser handler = new FriendJsonParser();
		Map<String,String> retvalue = handler.parse(input);
		int c= 0;
	}

}
