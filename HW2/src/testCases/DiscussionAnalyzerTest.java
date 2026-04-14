package testCases;

import java.util.List;

import entityClasses.DiscussionAnalyzer;
import entityClasses.Post;
import entityClasses.PostStorage;
import entityClasses.Reply;
import entityClasses.ReplyStorage;
import entityClasses.User;

/**
 * Console-based tests for {@link DiscussionAnalyzer}.
 *
 * These tests verify that the analyzer counts distinct discussion partners
 * correctly, ignores duplicate replies to the same student, ignores self-replies,
 * and returns unique answered posts in a stable order.
 */
public class DiscussionAnalyzerTest {

    /**
     * Runs all tests from the command line.
     *
     * @param args command-line arguments, not used
     */
    public static void main(String[] args) {
        DiscussionAnalyzerTest test = new DiscussionAnalyzerTest();
        test.runAll();
    }

    /**
     * Executes every test in this class.
     */
    private void runAll() {
        testCountsUniqueAuthorsAndIgnoresDuplicatesAndSelfReplies();
        testRequirementFailsWhenFewerThanThreeDistinctStudents();
        testNullUserRejected();
        testAnsweredPostsAreReturnedWithoutDuplicates();
        System.out.println("All DiscussionAnalyzer tests passed.");
    }

    /**
     * Builds a DiscussionAnalyzer for the provided storages.
     *
     * @param posts the post storage to use
     * @param replies the reply storage to use
     * @return a configured DiscussionAnalyzer
     */
    private DiscussionAnalyzer buildAnalyzer(PostStorage posts, ReplyStorage replies) {
        return new DiscussionAnalyzer(posts, replies);
    }

    /**
     * Creates the target user used by the tests.
     *
     * @return a valid student user named sam
     */
    private User buildTargetUser() {
        return new User("sam", "pw", "sam@example.com", "Sam", "", "Student", "Sam", false, false, true);
    }

    /**
     * Seeds a shared discussion setup used by multiple tests.
     * The data includes:
     * - three distinct external authors
     * - duplicate replies to the same post
     * - a self-reply
     * - an orphaned reply pointing to a missing post
     * - a reply from another user that should not count
     *
     * @param posts the post storage to populate
     * @param replies the reply storage to populate
     */
    private void seedBaseDiscussions(PostStorage posts, ReplyStorage replies) {
        posts.addPost(new Post(1, "A question", "Body A", "alice", "General"));
        posts.addPost(new Post(2, "B question", "Body B", "bob", "General"));
        posts.addPost(new Post(3, "C question", "Body C", "carol", "General"));
        posts.addPost(new Post(4, "D question", "Body D", "dana", "General"));
        posts.addPost(new Post(5, "Sam post", "Own post", "sam", "General"));

        replies.addReply(new Reply(11, 1, "Reply to Alice", "sam"));
        replies.addReply(new Reply(12, 2, "Reply to Bob", "sam"));
        replies.addReply(new Reply(13, 3, "First reply to Carol", "sam"));
        replies.addReply(new Reply(14, 3, "Second reply to Carol", "sam"));
        replies.addReply(new Reply(15, 5, "Self reply", "sam"));
        replies.addReply(new Reply(16, 999, "Orphaned reply", "sam"));
        replies.addReply(new Reply(17, 1, "Reply by another user", "alex"));
    }

    /**
     * Verifies that the analyzer counts only unique authors and ignores
     * duplicate replies and self-replies.
     */
    private void testCountsUniqueAuthorsAndIgnoresDuplicatesAndSelfReplies() {
        PostStorage posts = new PostStorage();
        ReplyStorage replies = new ReplyStorage();
        seedBaseDiscussions(posts, replies);

        DiscussionAnalyzer analyzer = buildAnalyzer(posts, replies);
        User sam = buildTargetUser();

        int actual = analyzer.countUniqueStudentsRepliedTo(sam);
        assertEquals("unique author count", 3, actual);
        assertTrue("three-student requirement", analyzer.hasMetThreeStudentRequirement(sam));

        List<Post> answered = analyzer.getPostsAnsweredByUser(sam);
        assertEquals("answered post count", 3, answered.size());
        assertEquals("first answered post", 1, answered.get(0).getId());
        assertEquals("second answered post", 2, answered.get(1).getId());
        assertEquals("third answered post", 3, answered.get(2).getId());
    }

    /**
     * Verifies that the requirement fails when fewer than three distinct
     * students were answered.
     */
    private void testRequirementFailsWhenFewerThanThreeDistinctStudents() {
        PostStorage posts = new PostStorage();
        ReplyStorage replies = new ReplyStorage();
        posts.addPost(new Post(1, "A question", "Body A", "alice", "General"));
        posts.addPost(new Post(2, "B question", "Body B", "bob", "General"));

        replies.addReply(new Reply(21, 1, "Reply 1", "sam"));
        replies.addReply(new Reply(22, 1, "Reply 2", "sam"));
        replies.addReply(new Reply(23, 2, "Reply 3", "sam"));

        DiscussionAnalyzer analyzer = buildAnalyzer(posts, replies);
        User sam = buildTargetUser();

        assertEquals("only two unique authors", 2, analyzer.countUniqueStudentsRepliedTo(sam));
        assertFalse("requirement should fail", analyzer.hasMetThreeStudentRequirement(sam));
    }

    /**
     * Verifies that invalid input is rejected.
     */
    private void testNullUserRejected() {
        DiscussionAnalyzer analyzer = buildAnalyzer(new PostStorage(), new ReplyStorage());
        try {
            analyzer.countUniqueStudentsRepliedTo(null);
            throw new AssertionError("Expected IllegalArgumentException for null user.");
        } catch (IllegalArgumentException expected) {
            // Expected path: null users must not be accepted.
        }
    }

    /**
     * Verifies that answered posts are returned without duplicates and in the
     * order they were first encountered.
     */
    private void testAnsweredPostsAreReturnedWithoutDuplicates() {
        PostStorage posts = new PostStorage();
        ReplyStorage replies = new ReplyStorage();
        seedBaseDiscussions(posts, replies);
        DiscussionAnalyzer analyzer = buildAnalyzer(posts, replies);

        List<Post> answered = analyzer.getPostsAnsweredByUser(buildTargetUser());
        assertEquals("duplicate replies should collapse to one post", 3, answered.size());
        assertEquals("unique post ids preserved", 1, answered.get(0).getId());
        assertEquals("unique post ids preserved", 2, answered.get(1).getId());
        assertEquals("unique post ids preserved", 3, answered.get(2).getId());
    }

    /**
     * Simple integer equality assertion for console tests.
     *
     * @param label test label
     * @param expected expected value
     * @param actual actual value
     */
    private static void assertEquals(String label, int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError(label + " expected " + expected + " but was " + actual);
        }
        System.out.println("PASS: " + label + " = " + actual);
    }

    /**
     * Simple boolean true assertion for console tests.
     *
     * @param label test label
     * @param condition condition that must be true
     */
    private static void assertTrue(String label, boolean condition) {
        if (!condition) {
            throw new AssertionError(label + " expected true but was false");
        }
        System.out.println("PASS: " + label);
    }

    /**
     * Simple boolean false assertion for console tests.
     *
     * @param label test label
     * @param condition condition that must be false
     */
    private static void assertFalse(String label, boolean condition) {
        if (condition) {
            throw new AssertionError(label + " expected false but was true");
        }
        System.out.println("PASS: " + label);
    }
}