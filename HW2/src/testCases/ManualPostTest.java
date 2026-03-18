package testCases;

import entityClasses.Post;
import entityClasses.PostStorage;
import entityClasses.Reply;

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
    }
}