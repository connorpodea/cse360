package testCases;

import entityClasses.Reply;

/**
 * Boundary value and coverage tests for the Reply class.
 */
public class ReplyTest {

    public static void main(String[] args) {
        testValidConstruction();
        testConstructorRejectsEmptyBody();
        testConstructorRejectsEmptyAuthor();
        testConstructorRejectsLongBody();
        testConstructorAcceptsBoundaryLength();
        testUpdateWorksForValidValue();
        testUpdateRejectsEmptyBody();
        testUpdateRejectsLongBody();
        testGettersAndTimestamp();

        System.out.println("All Reply tests passed.");
    }

    private static void testValidConstruction() {
        Reply r = new Reply(1, 10, "Reply body", "author");
        assertEquals("id", 1, r.getId());
        assertEquals("postId", 10, r.getPostId());
        assertEquals("parentPostId", 10, r.getParentPostId());
        assertEquals("body", "Reply body", r.getBody());
        assertEquals("author", "author", r.getAuthor());
    }

    private static void testConstructorRejectsEmptyBody() {
        expectIllegalArgument("empty body", () -> new Reply(2, 10, "", "author"));
    }

    private static void testConstructorRejectsEmptyAuthor() {
        expectIllegalArgument("empty author", () -> new Reply(3, 10, "Reply body", "   "));
    }

    private static void testConstructorRejectsLongBody() {
        String longBody = makeString('b', Reply.MAX_BODY_LENGTH + 1);
        expectIllegalArgument("too-long body", () -> new Reply(4, 10, longBody, "author"));
    }

    private static void testConstructorAcceptsBoundaryLength() {
        String boundaryBody = makeString('b', Reply.MAX_BODY_LENGTH);
        Reply r = new Reply(5, 10, boundaryBody, "author");
        assertEquals("boundary body length", Reply.MAX_BODY_LENGTH, r.getBody().length());
    }

    private static void testUpdateWorksForValidValue() {
        Reply r = new Reply(6, 10, "Old reply", "author");
        r.update("New reply");
        assertEquals("updated body", "New reply", r.getBody());
    }

    private static void testUpdateRejectsEmptyBody() {
        Reply r = new Reply(7, 10, "Old reply", "author");
        expectIllegalArgument("update empty body", () -> r.update(""));
    }

    private static void testUpdateRejectsLongBody() {
        Reply r = new Reply(8, 10, "Old reply", "author");
        String longBody = makeString('b', Reply.MAX_BODY_LENGTH + 1);
        expectIllegalArgument("update too-long body", () -> r.update(longBody));
    }

    private static void testGettersAndTimestamp() {
        Reply r = new Reply(9, 11, "Hello", "author");
        assertNotNull("timestamp", r.getTimestamp());
        assertEquals("getId", 9, r.getId());
        assertEquals("getPostId", 11, r.getPostId());
        assertEquals("getParentPostId", 11, r.getParentPostId());
        assertEquals("getBody", "Hello", r.getBody());
        assertEquals("getAuthor", "author", r.getAuthor());
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

    private static void assertNotNull(String label, Object value) {
        if (value == null) {
            throw new AssertionError("FAIL: expected non-null for " + label);
        }
        System.out.println("PASS: " + label);
    }
}
