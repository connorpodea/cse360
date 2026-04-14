package testCases;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import entityClasses.Post;
import entityClasses.PostStorage;

class FullCoverageTestPostStorage {

	@Test
	public void CoverageTest1() {
		try {
			PostStorage store = new PostStorage();
			Post p = new Post(1, "Title", "Body", "Student1", "General");   // add path
			store.addPost(p);
			assertEquals(1, store.getAllPosts().size());
		} catch (IllegalArgumentException e) {
			fail("*** Error*** This is a valid test case");
		}
	}

	@Test
	public void CoverageTest2() {
		try {
			PostStorage store = new PostStorage();
			store.addPost(new Post(2, "Title 1", "Body 1", "Student1", "General"));
			store.addPost(new Post(3, "Title 2", "Body 2", "Student2", "General"));   // get all path
			assertEquals(2, store.getAllPosts().size());
		} catch (IllegalArgumentException e) {
			fail("*** Error*** This is a valid test case");
		}
	}

	@Test
	public void CoverageTest3() {
		try {
			PostStorage store = new PostStorage();
			Post p = new Post(4, "Find Me", "Body", "Student1", "General");
			store.addPost(p);   // get by id path
			assertEquals("Find Me", store.getPostById(4).getTitle());
		} catch (IllegalArgumentException e) {
			fail("*** Error*** This is a valid test case");
		}
	}

	@Test
	public void CoverageTest4() {
		try {
			PostStorage store = new PostStorage();
			Post p = new Post(5, "Delete State", "Body", "Student1", "General");
			store.addPost(p);
			p.markDeleted();   // deleted remains in storage
			assertEquals(1, store.getAllPosts().size());
			assertTrue(store.exists(5));
		} catch (IllegalArgumentException e) {
			fail("*** Error*** This is a valid test case");
		}
	}

	@Test
	public void CoverageTest5() {
		try {
			PostStorage store = new PostStorage();
			Post p = new Post(6, "Delete State", "Body", "Student1", "General");
			store.addPost(p);
			p.markDeleted();   // deleted flag preserved after retrieval
			assertTrue(store.getPostById(6).isDeleted());
		} catch (IllegalArgumentException e) {
			fail("*** Error*** This is a valid test case");
		}
	}


}