package testCases;

import java.util.List;

import entityClasses.Post;
import entityClasses.PostStorage;

/**
 * Coverage tests for PostStorage.
 */
public class PostStorageTest {

    public static void main(String[] args) {
        testAddAndGetById();
        testGetAllPostsReturnsCopy();
        testGetPostByIdRejectsMissingId();
        testFilterByTitleAndBody();
        testFilterRejectsNull();
        testRemovePostById();
        testRemoveMissingPostRejects();
        testDeletePostAndExists();
        testClearAll();
        testAddNullRejected();

        System.out.println("All PostStorage tests passed.");
    }

    private static void testAddAndGetById() {
        PostStorage storage = new PostStorage();
        Post p1 = new Post(1, "Alpha", "First body", "alice", "General");
        storage.addPost(p1);

        assertTrue("exists after add", storage.exists(1));

        Post retrieved = storage.getPostById(1);
        assertEquals("getPostById title", "Alpha", retrieved.getTitle());
    }

    private static void testGetAllPostsReturnsCopy() {
        PostStorage storage = new PostStorage();
        storage.addPost(new Post(2, "Beta", "Body", "bob", "General"));

        List<Post> posts = storage.getAllPosts();
        assertEquals("list size", 1, posts.size());

        posts.clear(); // modify returned list
        assertEquals("storage unchanged by returned copy", 1, storage.getAllPosts().size());
    }

    private static void testGetPostByIdRejectsMissingId() {
        PostStorage storage = new PostStorage();
        expectIllegalArgument("missing post id", () -> storage.getPostById(999));
    }

    private static void testFilterByTitleAndBody() {
        PostStorage storage = new PostStorage();
        storage.addPost(new Post(3, "Java basics", "intro", "alice", "Tech"));
        storage.addPost(new Post(4, "Other title", "contains testing keyword", "bob", "Tech"));

        assertEquals("filter by title", 1, storage.getPostsByFilter("java").size());
        assertEquals("filter by body", 1, storage.getPostsByFilter("testing").size());
    }

    private static void testFilterRejectsNull() {
        PostStorage storage = new PostStorage();
        expectIllegalArgument("null post filter", () -> storage.getPostsByFilter(null));
    }

    private static void testRemovePostById() {
        PostStorage storage = new PostStorage();
        storage.addPost(new Post(5, "To remove", "Body", "alice", "General"));

        storage.removePostById(5);

        assertFalse("removed post no longer exists", storage.exists(5));
        assertEquals("storage now empty", 0, storage.getAllPosts().size());
    }

    private static void testRemoveMissingPostRejects() {
        PostStorage storage = new PostStorage();
        expectIllegalArgument("remove missing post", () -> storage.removePostById(123));
    }

    private static void testDeletePostAndExists() {
        PostStorage storage = new PostStorage();
        storage.addPost(new Post(6, "Delete", "Body", "alice", "General"));

        assertTrue("exists before delete", storage.exists(6));

        storage.deletePost(6);

        assertFalse("exists after delete", storage.exists(6));
    }

    private static void testClearAll() {
        PostStorage storage = new PostStorage();
        storage.addPost(new Post(7, "A", "B", "alice", "General"));
        storage.addPost(new Post(8, "C", "D", "bob", "General"));

        storage.clearAll();

        assertEquals("clearAll size", 0, storage.getAllPosts().size());
    }

    private static void testAddNullRejected() {
        PostStorage storage = new PostStorage();
        expectIllegalArgument("add null post", () -> storage.addPost(null));
    }

    // =========================
    // Helper Methods
    // =========================

    private static void expectIllegalArgument(String label, Runnable action) {
        try {
            action.run();
            throw new AssertionError("FAIL: expected IllegalArgumentException for " + label);
        } catch (IllegalArgumentException expected) {
            System.out.println("PASS: " + label);
        }
    }

    private static void assertEquals(String label, int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError("FAIL: " + label + " expected " + expected + " but was " + actual);
        }
        System.out.println("PASS: " + label);
    }

    private static void assertEquals(String label, String expected, String actual) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError("FAIL: " + label + " expected [" + expected + "] but was [" + actual + "]");
        }
        System.out.println("PASS: " + label);
    }

    private static void assertTrue(String label, boolean condition) {
        if (!condition) {
            throw new AssertionError("FAIL: expected true for " + label);
        }
        System.out.println("PASS: " + label);
    }

    private static void assertFalse(String label, boolean condition) {
        if (condition) {
            throw new AssertionError("FAIL: expected false for " + label);
        }
        System.out.println("PASS: " + label);
    }
}