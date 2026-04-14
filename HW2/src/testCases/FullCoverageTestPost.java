package testCases;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import entityClasses.Post;

class FullCoverageTestPost {

	@Test
	public void CoverageTest1() {
		try {
			Post p = new Post(1, "Title", "Body", "Student1", "General");   // normal create path
			assertEquals("Title", p.getTitle());
			assertEquals("Body", p.getBody());
			assertEquals("General", p.getCategory());
		} catch (IllegalArgumentException e) {
			fail("*** Error*** This is a valid test case");
		}
	}

	@Test
	public void CoverageTest2() {
		try {
			Post p = new Post(2, "Title", "Body", "Student1", "General");   // author path
			assertEquals("Student1", p.getAuthor());
			assertEquals("Student1", p.getAuthorUserName());
		} catch (IllegalArgumentException e) {
			fail("*** Error*** This is a valid test case");
		}
	}

	@Test
	public void CoverageTest3() {
		try {
			Post p = new Post(3, "Title", "Body", "Student1", "General");   // deleted path
			p.markDeleted();
			assertTrue(p.isDeleted());
		} catch (IllegalArgumentException e) {
			fail("*** Error*** This is a valid test case");
		}
	}

	@Test
	public void CoverageTest4() {
		try {
			Post p = new Post(4, "Old Title", "Old Body", "Student1", "General");   // update path
			p.update("New Title", "New Body");
			assertEquals("New Title", p.getTitle());
			assertEquals("New Body", p.getBody());
		} catch (IllegalArgumentException e) {
			fail("*** Error*** This is a valid test case");
		}
	}

	@Test
	public void CoverageTest5() {
		try {
			Post p = new Post(5, "Old Title", "Old Body", "Student1", "General");   // deleted update path
			p.markDeleted();
			p.update("New Title", "New Body");
			assertEquals("Old Title", p.getTitle());
			assertEquals("Old Body", p.getBody());
			assertTrue(p.isDeleted());
		} catch (IllegalArgumentException e) {
			fail("*** Error*** This is a valid test case");
		}
	}
}