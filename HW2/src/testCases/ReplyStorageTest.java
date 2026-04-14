package testCases;

import java.util.List;

import entityClasses.Reply;
import entityClasses.ReplyStorage;

/**
 * Coverage tests for ReplyStorage.
 */
public class ReplyStorageTest {

    public static void main(String[] args) {
        testAddAndGetById();
        testGetAllRepliesReturnsCopy();
        testGetReplyByIdRejectsMissingId();
        testUpdateReply();
        testUpdateMissingReplyRejects();
        testDeleteReply();
        testDeleteMissingReplyRejects();
        testRepliesForPost();
        testFilterByBody();
        testFilterRejectsNull();
        testExistsAndClearAll();
        testAddNullRejected();

        System.out.println("All ReplyStorage tests passed.");
    }

    private static void testAddAndGetById() {
        ReplyStorage storage = new ReplyStorage();
        Reply r = new Reply(1, 10, "Reply one", "sam");
        storage.addReply(r);

        assertTrue("exists after add", storage.exists(1));
        assertEquals("getReplyById body", "Reply one", storage.getReplyById(1).getBody());
    }

    private static void testGetAllRepliesReturnsCopy() {
        ReplyStorage storage = new ReplyStorage();
        storage.addReply(new Reply(2, 10, "Reply two", "sam"));

        List<Reply> replies = storage.getAllReplies();
        assertEquals("list size", 1, replies.size());
        replies.clear();
        assertEquals("storage unchanged by returned copy", 1, storage.getAllReplies().size());
    }

    private static void testGetReplyByIdRejectsMissingId() {
        ReplyStorage storage = new ReplyStorage();
        expectIllegalArgument("missing reply id", () -> storage.getReplyById(999));
    }

    private static void testUpdateReply() {
        ReplyStorage storage = new ReplyStorage();
        storage.addReply(new Reply(3, 10, "Old body", "sam"));
        storage.updateReply(3, "New body");
        assertEquals("updated body", "New body", storage.getReplyById(3).getBody());
    }

    private static void testUpdateMissingReplyRejects() {
        ReplyStorage storage = new ReplyStorage();
        expectIllegalArgument("update missing reply", () -> storage.updateReply(100, "x"));
    }

    private static void testDeleteReply() {
        ReplyStorage storage = new ReplyStorage();
        storage.addReply(new Reply(4, 10, "Delete me", "sam"));
        storage.deleteReply(4);
        assertFalse("deleted reply no longer exists", storage.exists(4));
        assertEquals("storage now empty", 0, storage.getAllReplies().size());
    }

    private static void testDeleteMissingReplyRejects() {
        ReplyStorage storage = new ReplyStorage();
        expectIllegalArgument("delete missing reply", () -> storage.deleteReply(123));
    }

    private static void testRepliesForPost() {
        ReplyStorage storage = new ReplyStorage();
        storage.addReply(new Reply(5, 20, "A", "sam"));
        storage.addReply(new Reply(6, 20, "B", "alex"));
        storage.addReply(new Reply(7, 21, "C", "sam"));

        assertEquals("replies for post 20", 2, storage.getRepliesForPost(20).size());
        assertEquals("replies for post 21", 1, storage.getRepliesForPost(21).size());
    }

    private static void testFilterByBody() {
        ReplyStorage storage = new ReplyStorage();
        storage.addReply(new Reply(8, 30, "This contains keyword", "sam"));
        storage.addReply(new Reply(9, 30, "Different text", "alex"));

        assertEquals("filter by keyword", 1, storage.getRepliesByFilter("keyword").size());
    }

    private static void testFilterRejectsNull() {
        ReplyStorage storage = new ReplyStorage();
        expectIllegalArgument("null reply filter", () -> storage.getRepliesByFilter(null));
    }

    private static void testExistsAndClearAll() {
        ReplyStorage storage = new ReplyStorage();
        storage.addReply(new Reply(10, 40, "Body", "sam"));
        assertTrue("exists before clear", storage.exists(10));
        storage.clearAll();
        assertFalse("exists after clear", storage.exists(10));
        assertEquals("clearAll size", 0, storage.getAllReplies().size());
    }

    private static void testAddNullRejected() {
        ReplyStorage storage = new ReplyStorage();
        expectIllegalArgument("add null reply", () -> storage.addReply(null));
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
}