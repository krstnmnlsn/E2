package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.coursemingle.server.CourseEntry;
import com.coursemingle.server.XMLCannotFindCourseException;
import com.coursemingle.server.XMLCourseParser;
import com.coursemingle.server.XMLCourseSection;
import com.coursemingle.server.XMLNoLongerExistsException;

public class XMLCourseParserTest {

	XMLCourseParser xcp = new XMLCourseParser();
	
	// This will fail a percentage of the time due to server lag.
	@SuppressWarnings("unused")
	@Test
	public void XMLCourseParserTest0() {
		try {
			CourseEntry ce = new CourseEntry("BIOL", "111", "101", 2012, 'W',null);
			XMLCourseSection xcs = xcp.getCourseData(ce);
			int c = 0;
		} catch (XMLCannotFindCourseException e) {
			fail();
		} catch (XMLNoLongerExistsException e) {
			fail();
		}
	}
	
	@SuppressWarnings("unused")
	@Test
	public void XMLCourseParserTest1() {
		try {
			CourseEntry ce = new CourseEntry("BIOL", "111", "100", 2012, 'W',null);
			XMLCourseSection xcs = xcp.getCourseData(ce);
			fail();
		} catch (XMLCannotFindCourseException e) {
		} catch (XMLNoLongerExistsException e) {
			fail();
		}
	}
	
	@SuppressWarnings("unused")
	@Test
	public void XMLCourseParserTest2() {
		try {
			CourseEntry ce = new CourseEntry("BIOL", "111", "101", 2011, 'W',null);
			XMLCourseSection xcs = xcp.getCourseData(ce);
			fail();
		} catch (XMLCannotFindCourseException e) {
			fail();
		} catch (XMLNoLongerExistsException e) {
		}
	}
	@SuppressWarnings("unused")
	@Test
	public void XMLCourseParserTest3() {
		try {
			XMLCourseSection xcs = xcp.getCourseData("BIOL 111 101");
		} catch (XMLCannotFindCourseException e) {
			fail();
		}
	}
	@SuppressWarnings("unused")
	@Test
	public void XMLCourseParserTest4() {
		try {
			XMLCourseSection xcs = xcp.getCourseData("CPSC 310 101");
			int c = 0;
		} catch (XMLCannotFindCourseException e) {
			fail();
		}
	}
	
	@SuppressWarnings("unused")
	@Test
	public void XMLCourseParserTest5() {
		try {
			XMLCourseSection xcs = xcp.getCourseData("Azerbaijan 111 101");
			fail();
		} catch (XMLCannotFindCourseException e) {	
		}
	}
	
}
