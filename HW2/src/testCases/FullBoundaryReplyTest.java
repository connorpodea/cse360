package testCases;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import entityClasses.Post;

class FullBoundaryTestPost {

	@Test
	public void BoundaryTest1() {
		try {
			Post p = new Post(1, "A", "B", "Student1", "General");   // min valid
			assertEquals("A", p.getTitle());
			assertEquals("B", p.getBody());
		} catch (IllegalArgumentException e) {
			fail("*** Error*** This is a valid test case");
		}
	}

	@Test
	public void BoundaryTest2() {
		try {
			new Post(2, "", "Body", "Student1", "General");   // min-
			fail("*** Error*** Empty title should be rejected");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	@Test
	public void BoundaryTest3() {
		try {
			new Post(3, "Title", "", "Student1", "General");   // min-
			fail("*** Error*** Empty body should be rejected");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	@Test
	public void BoundaryTest4() {
		try {
			String maxTitle = "";
			String maxBody = "";
			for (int i = 0; i < Post.MAX_TITLE_LENGTH; i++) maxTitle += "t";
			for (int i = 0; i < Post.MAX_BODY_LENGTH; i++) maxBody += "b";
			Post p = new Post(4, maxTitle, maxBody, "Student1", "General");   // max valid
			assertEquals(Post.MAX_TITLE_LENGTH, p.getTitle().length());
			assertEquals(Post.MAX_BODY_LENGTH, p.getBody().length());
		} catch (IllegalArgumentException e) {
			fail("*** Error*** This is a valid test case");
		}
	}

	@Test
	public void BoundaryTest5() {
		try {
			String tooLongTitle = "";
			for (int i = 0; i < Post.MAX_TITLE_LENGTH + 1; i++) tooLongTitle += "t";
			new Post(5, tooLongTitle, "Body", "Student1", "General");   // max+
			fail("*** Error*** Title above max should be rejected");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	@Test
	public void BoundaryTest6() {
		try {
			String tooLongBody = "";
			for (int i = 0; i < Post.MAX_BODY_LENGTH + 1; i++) tooLongBody += "b";
			new Post(6, "Title", tooLongBody, "Student1", "General");   // max+
			fail("*** Error*** Body above max should be rejected");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
}