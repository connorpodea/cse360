package testCases;

import entityClasses.Post;
import entityClasses.PostStorage;
import entityClasses.Reply;
import entityClasses.ReplyStorage;

/**
 * Provides a manual verification driver for the Post system. 
 * This class outputs results to the console to allow for human verification 
 * of the system state, specifically post listing and deletion status.
 */
public class ManualPostTest {

    /**
     * Entry point for the manual test driver. Performs standard CRUD 
     * operations and prints status updates to the console.
     * * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        PostStorage storage = new PostStorage();
        ReplyStorage replyStorage = new ReplyStorage();
        
        // 1. Test Create
        Post p1 = new Post(10, "Hello World", "This is a test post.", "Student1", "Tech");
        storage.addPost(p1);
        
        // 2. Test Read All
        System.out.println("--- Testing List View ---");
        for (Post p : storage.getAllPosts()) {
            System.out.println("ID: " + p.getId() + " | Title: " + p.getTitle());
        }

        // 3. Test Read Details & Deleted Status
        System.out.println("\n--- Testing Deleted Status ---");
        p1.markDeleted();
        if (p1.isDeleted()) {
            System.out.println("Post " + p1.getId() + " is correctly marked as [DELETED]");
        }
        
        // 4. Test Reply Creation
        System.out.println("\n--- Testing Reply ---");
        try {
            Reply r = new Reply(1, 10, "", "Author"); // Should fail
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }

        // 5. Test Update Post
        System.out.println("\n--- Testing Update Post ---");
        Post p2 = new Post(11, "Original Title", "Original Body", "Student2", "General");
        System.out.println("Before Update -> Title: " + p2.getTitle() + " | Body: " + p2.getBody());
        p2.update("Updated Title", "Updated Body");
        System.out.println("After Update -> Title: " + p2.getTitle() + " | Body: " + p2.getBody());

        // 6. Test Delete Post With Replies Remaining
        System.out.println("\n--- Testing Delete Post ---");
        Post p3 = new Post(12, "Delete Target", "Delete Body", "Student3", "General");
        Reply r2 = new Reply(2, 12, "Reply still here", "Student4");
        replyStorage.addReply(r2);
        p3.markDeleted();
        System.out.println("Deleted Status: " + p3.isDeleted());
        System.out.println("Replies Still Exist: " + replyStorage.getRepliesForPost(12).size());

        // 7. Test Deleted State Blocks Update
        System.out.println("\n--- Testing Deleted Post Update Block ---");
        String deletedTitleBefore = p3.getTitle();
        String deletedBodyBefore = p3.getBody();
        p3.update("Should Not Change", "Should Not Change");
        if (p3.getTitle().equals(deletedTitleBefore) && p3.getBody().equals(deletedBodyBefore)) {
            System.out.println("Deleted post update was blocked.");
        } else {
            System.out.println("Deleted post update was not blocked.");
        }

        // 8. Test Update Reply
        System.out.println("\n--- Testing Update Reply ---");
        Reply r3 = new Reply(3, 10, "Before Reply Update", "Student5");
        System.out.println("Before Update -> " + r3.getBody());
        r3.update("After Reply Update");
        System.out.println("After Update -> " + r3.getBody());

        // 9. Test Delete Reply
        System.out.println("\n--- Testing Delete Reply ---");
        Reply r4 = new Reply(4, 10, "Delete This Reply", "Student6");
        replyStorage.addReply(r4);
        System.out.println("Reply Count Before Delete: " + replyStorage.getAllReplies().size());
        replyStorage.deleteReply(4);
        System.out.println("Reply Count After Delete: " + replyStorage.getAllReplies().size());
    }
}
