package testCases;

import entityClasses.Post;

/**
 * Boundary value and coverage tests for the Post class.
 */
public class PostTest {

    public static void main(String[] args) {
        testValidConstruction();
        testConstructorRejectsEmptyTitle();
        testConstructorRejectsEmptyBody();
        testConstructorRejectsEmptyAuthor();
        testConstructorRejectsLongTitle();
        testConstructorRejectsLongBody();
        testConstructorAcceptsBoundaryLengths();
        testUpdateWorksForValidValues();
        testUpdateRejectsEmptyTitle();
        testUpdateRejectsEmptyBody();
        testUpdateRejectsLongTitle();
        testUpdateRejectsLongBody();
        testDeletedPostIgnoresUpdate();
        testGettersAndTimestamp();

        System.out.println("All Post tests passed.");
    }

    private static void testValidConstruction() {
        Post p = new Post(1, "Title", "Body", "author", "General");
        assertEquals("id", 1, p.getId());
        assertEquals("title", "Title", p.getTitle());
        assertEquals("body", "Body", p.getBody());
        assertEquals("author", "author", p.getAuthor());
        assertEquals("category", "General", p.getCategory());
        assertFalse("deleted", p.isDeleted());
    }

    private static void testConstructorRejectsEmptyTitle() {
        expectIllegalArgument("empty title", () -> new Post(2, "", "Body", "author", "General"));
    }

    private static void testConstructorRejectsEmptyBody() {
        expectIllegalArgument("empty body", () -> new Post(3, "Title", "   ", "author", "General"));
    }

    private static void testConstructorRejectsEmptyAuthor() {
        expectIllegalArgument("empty author", () -> new Post(4, "Title", "Body", "", "General"));
    }

    private static void testConstructorRejectsLongTitle() {
        String longTitle = makeString('t', Post.MAX_TITLE_LENGTH + 1);
        expectIllegalArgument("too-long title", () -> new Post(5, longTitle, "Body", "author", "General"));
    }

    private static void testConstructorRejectsLongBody() {
        String longBody = makeString('b', Post.MAX_BODY_LENGTH + 1);
        expectIllegalArgument("too-long body", () -> new Post(6, "Title", longBody, "author", "General"));
    }

    private static void testConstructorAcceptsBoundaryLengths() {
        String title = makeString('t', Post.MAX_TITLE_LENGTH);
        String body = makeString('b', Post.MAX_BODY_LENGTH);
        Post p = new Post(7, title, body, "author", "General");
        assertEquals("boundary title length", Post.MAX_TITLE_LENGTH, p.getTitle().length());
        assertEquals("boundary body length", Post.MAX_BODY_LENGTH, p.getBody().length());
    }

    private static void testUpdateWorksForValidValues() {
        Post p = new Post(8, "Old Title", "Old Body", "author", "General");
        p.update("New Title", "New Body");
        assertEquals("updated title", "New Title", p.getTitle());
        assertEquals("updated body", "New Body", p.getBody());
    }

    private static void testUpdateRejectsEmptyTitle() {
        Post p = new Post(9, "Old Title", "Old Body", "author", "General");
        expectIllegalArgument("update empty title", () -> p.update("", "New Body"));
    }

    private static void testUpdateRejectsEmptyBody() {
        Post p = new Post(10, "Old Title", "Old Body", "author", "General");
        expectIllegalArgument("update empty body", () -> p.update("New Title", "   "));
    }

    private static void testUpdateRejectsLongTitle() {
        Post p = new Post(11, "Old Title", "Old Body", "author", "General");
        String longTitle = makeString('t', Post.MAX_TITLE_LENGTH + 1);
        expectIllegalArgument("update too-long title", () -> p.update(longTitle, "New Body"));
    }

    private static void testUpdateRejectsLongBody() {
        Post p = new Post(12, "Old Title", "Old Body", "author", "General");
        String longBody = makeString('b', Post.MAX_BODY_LENGTH + 1);
        expectIllegalArgument("update too-long body", () -> p.update("New Title", longBody));
    }

    private static void testDeletedPostIgnoresUpdate() {
        Post p = new Post(13, "Old Title", "Old Body", "author", "General");
        p.markDeleted();
        p.update("Should Not Change", "Should Not Change");
        assertEquals("deleted title unchanged", "Old Title", p.getTitle());
        assertEquals("deleted body unchanged", "Old Body", p.getBody());
        assertTrue("deleted flag", p.isDeleted());
    }

    private static void testGettersAndTimestamp() {
        Post p = new Post(14, "Title", "Body", "author", "General");
        assertNotNull("timestamp", p.getTimestamp());
        assertNotNull("formatted timestamp", p.getFormattedTimestamp());
        p.setTitle("Changed Title");
        p.setBody("Changed Body");
        assertEquals("setTitle", "Changed Title", p.getTitle());
        assertEquals("setBody", "Changed Body", p.getBody());
        assertEquals("getPostId", 14, p.getPostId());
        assertEquals("getAuthorUserName", "author", p.getAuthorUserName());
    }

    private static String makeString(char ch, int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }

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

    private static void assertNotNull(String label, Object value) {
        if (value == null) {
            throw new AssertionError("FAIL: expected non-null for " + label);
        }
        System.out.println("PASS: " + label);
    }
}