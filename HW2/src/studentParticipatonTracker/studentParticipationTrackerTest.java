package studentParticipationTracker;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import studentParticipationTracker;
import java.util.ArrayList;
import java.util.List;

import entityClasses.Post;
import entityClasses.Reply;

/**
 * JUnit tests for the student participation tracker prototype.
 * Verifies that only replies to other students are counted
 * and that the three-reply participation requirement is checked correctly.
 */
class StudentParticipationTrackerTest {

    /**
     * Verifies that a student with no replies has a valid reply count of 0
     * and does not meet the participation requirement.
     */
    @Test
    public void PrototypeTest1() {
        try {
            StudentParticipationTracker tracker = new StudentParticipationTracker();
            List<Post> posts = new ArrayList<Post>();
            List<Reply> replies = new ArrayList<Reply>();

            // Only one post exists and Student1 has not replied at all.
            // This verifies the lowest case where the valid reply count should stay at 0.
            posts.add(new Post(1, "Post 1", "Body", "Student2", "General"));

            assertEquals(0, tracker.countRepliesToOtherStudents("Student1", posts, replies));   // 0 valid replies
            assertTrue(!tracker.hasMetReplyRequirement("Student1", posts, replies));
        } catch (Exception e) {
            fail("*** Error*** This is a valid test case");
        }
    }

    /**
     * Verifies that a student with two valid replies is counted correctly
     * but still does not meet the three-reply requirement.
     */
    @Test
    public void PrototypeTest2() {
        try {
            StudentParticipationTracker tracker = new StudentParticipationTracker();
            List<Post> posts = new ArrayList<Post>();
            List<Reply> replies = new ArrayList<Reply>();

            // Both replies are to posts owned by other students, so both should count.
            posts.add(new Post(1, "Post 1", "Body", "Student2", "General"));
            posts.add(new Post(2, "Post 2", "Body", "Student3", "General"));

            replies.add(new Reply(1, 1, "Reply 1", "Student1"));
            replies.add(new Reply(2, 2, "Reply 2", "Student1"));

            assertEquals(2, tracker.countRepliesToOtherStudents("Student1", posts, replies));   // 2 valid replies
            assertTrue(!tracker.hasMetReplyRequirement("Student1", posts, replies));
        } catch (Exception e) {
            fail("*** Error*** This is a valid test case");
        }
    }

    /**
     * Verifies that exactly three valid replies satisfies the participation requirement.
     * This is the main boundary test for the prototype.
     */
    @Test
    public void PrototypeTest3() {
        try {
            StudentParticipationTracker tracker = new StudentParticipationTracker();
            List<Post> posts = new ArrayList<Post>();
            List<Reply> replies = new ArrayList<Reply>();

            // This is the boundary where the requirement should become true.
            // It checks that exactly 3 valid replies is enough to satisfy the rule.
            posts.add(new Post(1, "Post 1", "Body", "Student2", "General"));
            posts.add(new Post(2, "Post 2", "Body", "Student3", "General"));
            posts.add(new Post(3, "Post 3", "Body", "Student4", "General"));

            replies.add(new Reply(1, 1, "Reply 1", "Student1"));
            replies.add(new Reply(2, 2, "Reply 2", "Student1"));
            replies.add(new Reply(3, 3, "Reply 3", "Student1"));

            assertEquals(3, tracker.countRepliesToOtherStudents("Student1", posts, replies));   // exactly 3
            assertTrue(tracker.hasMetReplyRequirement("Student1", posts, replies));
        } catch (Exception e) {
            fail("*** Error*** This is a valid test case");
        }
    }

    /**
     * Verifies that more than three valid replies still counts correctly
     * and continues to satisfy the requirement.
     */
    @Test
    public void PrototypeTest4() {
        try {
            StudentParticipationTracker tracker = new StudentParticipationTracker();
            List<Post> posts = new ArrayList<Post>();
            List<Reply> replies = new ArrayList<Reply>();

            // More than three valid replies should still count normally.
            posts.add(new Post(1, "Post 1", "Body", "Student2", "General"));
            posts.add(new Post(2, "Post 2", "Body", "Student3", "General"));
            posts.add(new Post(3, "Post 3", "Body", "Student4", "General"));
            posts.add(new Post(4, "Post 4", "Body", "Student5", "General"));

            replies.add(new Reply(1, 1, "Reply 1", "Student1"));
            replies.add(new Reply(2, 2, "Reply 2", "Student1"));
            replies.add(new Reply(3, 3, "Reply 3", "Student1"));
            replies.add(new Reply(4, 4, "Reply 4", "Student1"));

            assertEquals(4, tracker.countRepliesToOtherStudents("Student1", posts, replies));   // more than 3
            assertTrue(tracker.hasMetReplyRequirement("Student1", posts, replies));
        } catch (Exception e) {
            fail("*** Error*** This is a valid test case");
        }
    }

    /**
     * Verifies that replies made only to the student's own posts
     * do not count toward the participation requirement.
     */
    @Test
    public void PrototypeTest5() {
        try {
            StudentParticipationTracker tracker = new StudentParticipationTracker();
            List<Post> posts = new ArrayList<Post>();
            List<Reply> replies = new ArrayList<Reply>();

            // Student1 only replied to their own posts, so none of these should count.
            // This confirms that self-replies do not satisfy the participation requirement.
            posts.add(new Post(1, "Own Post 1", "Body", "Student1", "General"));
            posts.add(new Post(2, "Own Post 2", "Body", "Student1", "General"));

            replies.add(new Reply(1, 1, "Self Reply 1", "Student1"));
            replies.add(new Reply(2, 2, "Self Reply 2", "Student1"));

            assertEquals(0, tracker.countRepliesToOtherStudents("Student1", posts, replies));   // self-replies only
            assertTrue(!tracker.hasMetReplyRequirement("Student1", posts, replies));
        } catch (Exception e) {
            fail("*** Error*** This is a valid test case");
        }
    }

    /**
     * Verifies that mixed replies are handled correctly, so self-replies
     * are ignored and only replies to other students are counted.
     */
    @Test
    public void PrototypeTest6() {
        try {
            StudentParticipationTracker tracker = new StudentParticipationTracker();
            List<Post> posts = new ArrayList<Post>();
            List<Reply> replies = new ArrayList<Reply>();

            // The self-reply should be ignored and only the three replies to others should count.
            // This verifies that mixed valid and invalid replies are handled correctly.
            posts.add(new Post(1, "Own Post", "Body", "Student1", "General"));
            posts.add(new Post(2, "Other Post 1", "Body", "Student2", "General"));
            posts.add(new Post(3, "Other Post 2", "Body", "Student3", "General"));
            posts.add(new Post(4, "Other Post 3", "Body", "Student4", "General"));

            replies.add(new Reply(1, 1, "Self Reply", "Student1"));
            replies.add(new Reply(2, 2, "Valid Reply 1", "Student1"));
            replies.add(new Reply(3, 3, "Valid Reply 2", "Student1"));
            replies.add(new Reply(4, 4, "Valid Reply 3", "Student1"));

            assertEquals(3, tracker.countRepliesToOtherStudents("Student1", posts, replies));   // mixed replies
            assertTrue(tracker.hasMetReplyRequirement("Student1", posts, replies));
        } catch (Exception e) {
            fail("*** Error*** This is a valid test case");
        }
    }
}