package testCases;

import entityClasses.*;
import java.util.List;

/**
 * Title: FilterUnansweredTest
 *
 * Description: Prototype test covering functional, boundary, and security.
 */
public class FilterUnansweredTest {
	/** 
     * Default constructor to satisfy Javadoc.
     */
    public FilterUnansweredTest() {
        // Not used.
    }

    /**
     * Entry for running the prototype tests.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        System.out.println("### Filter Unanswered Prototype ####");
        System.out.println("------------------------------------------");

        PostStorage ps = new PostStorage();
        ReplyStorage rs = new ReplyStorage();

        // Setup

        System.out.println("\nSetting Up Test Scenarios");
        System.out.println("------------------------------------------");

        // Scenario 1:Detection
        Post p1 = new Post(1, "Help Needed", "How do loops work?", "StudentA", "Question");
        ps.addPost(p1);
        System.out.println("Scenario 1: New Question Added (0 replies)");

        // Scenario 2: Self Reply
        Post p2 = new Post(2, "Java Issue", "Body text", "StudentB", "Question");
        ps.addPost(p2);

        rs.addReply(new Reply(101, 2, "Nevermind, I found it.", "StudentB"));

        System.out.println("Scenario 2: Self-Reply Added");

        // Scenario 3: Staff Answer
        Post p3 = new Post(3, "Solved", "Body text", "StudentC", "Question");
        ps.addPost(p3);

        rs.addReply(new Reply(102, 3, "Here is the solution.", "StaffMember"));

        System.out.println("Scenario 3: Staff Reply Added");

        // Scenario 4:Deleted Post
        Post p4 = new Post(4, "Delete me", "Ignore", "StudentD", "Question");

        p4.markDeleted();
        ps.addPost(p4);

        System.out.println("Scenario 4: Deleted Post Added");

        // Test Execution

        System.out.println("\nRunning Identification Logic");
        System.out.println("------------------------------------------");

        List<Post> results =
                FilterUnanswered.identifyUnanswered(ps, rs);

        System.out.println("Unanswered Found: "
                + results.size());

        // Test Validation

        System.out.println("\nValidation Results");
        System.out.println("------------------------------------------");

        boolean detectionPass =
                results.contains(p1);

        boolean selfReplyPass =
                results.contains(p2);

        boolean staffReplyPass =
                !results.contains(p3);

        boolean deletedPass =
                !results.contains(p4);

        printResult("Detection Accuracy", detectionPass);
        printResult("Self-Reply Handling", selfReplyPass);
        printResult("Staff Reply Resolution", staffReplyPass);
        printResult("Deleted Post Filtering", deletedPass);

        // Security Tests

        System.out.println("\nSecurity Test (CWE-20)");
        System.out.println("------------------------------------------");

        try {

            new Post(7, "Bad Post", "", "StudentG", "Question");

            printResult(
                    "Empty Body Rejection",
                    false
            );

        } catch (IllegalArgumentException e) {

            printResult(
                    "Empty Body Rejection",
                    true
            );
        }

        // Summary
        boolean overallPass =
                detectionPass &&
                selfReplyPass &&
                staffReplyPass &&
                deletedPass;

        System.out.println("\nFinal Summary");
        System.out.println("------------------------------------------");

        if (overallPass) {
            System.out.println(
                    "[PASS] All core filtering tests succeeded."
            );
        } else {
            System.out.println(
                    "[FAIL] One or more filtering tests failed."
            );
        }
    }

    /**
     * Formats and prints the test result to the console.
     * @param testName The name of the test case
     * @param passed Whether the test passed or failed
     */
    private static void printResult(String testName, boolean passed) {
        if (passed) {
            System.out.println(
                    "[PASS] " + testName
            );
        } else {
            System.out.println(
                    "[FAIL] " + testName
            );
        }
    }
}