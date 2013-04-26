package tests;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class RegexTest {

	@SuppressWarnings("unused")
	@Test
	public void RegexTest0() {
		LinkedList<String > result = 
				recognizeMultiCourse("BIOL 100 101 MATH 300 300 CPSC 310 100");
		int c= 0;
	}
	
	
	public LinkedList<String> recognizeMultiCourse(String input){
		String regex = "[a-zA-Z]{2,4}\\s*[0-6][0-9]{2}[a-zA-Z]?\\s*[ltLT01234][0-9]([0-9]|[a-zA-Z])";
		Pattern p = Pattern.compile(regex); 
		Matcher m = p.matcher(input);
		LinkedList<String> tobeprocessed = new LinkedList<String>();
		while(m.find()){
			tobeprocessed.add(m.group());
		}
		return tobeprocessed;
	}
}
