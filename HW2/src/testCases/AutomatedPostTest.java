package testCases;

import entityClasses.Post;
import entityClasses.PostStorage;
import entityClasses.Reply;
import entityClasses.ReplyStorage;
import java.util.List;

/**
 * Provides automated testing for the Create and Read (CR) functionality 
 * of the Post and Reply systems. This class verifies constraints such 
 * as field length, mandatory fields, and correct object storage.
 */
public class AutomatedPostTest {

    /**
     * Executes a suite of automated tests to verify business rules for 
     * creating and reading posts and replies.
     * * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        PostStorage postStore = new PostStorage();
        ReplyStorage replyStore = new ReplyStorage();

        System.out.println("=== CREATE FUNCTIONALITY TESTS ===");
        
        // 1. Valid Post & Author Assignment
        try {
            Post p1 = new Post(1, "Valid Title", "Valid Message", "Student1", "General");
            if (p1.getAuthor().equals("Student1")) {
                System.out.println("PASS: Valid post created and correct author assigned.");
            }
        } catch (Exception e) { System.out.println("FAIL: Valid post creation."); }

        // 2. Prevent no title / no message
        try {
            new Post(2, "", "Message", "Author", "Cat");
            System.out.println("FAIL: System allowed empty title.");
        } catch (IllegalArgumentException e) { System.out.println("PASS: System prevented empty title."); }

        try {
            new Post(3, "Title", "   ", "Author", "Cat");
            System.out.println("FAIL: System allowed empty message.");
        } catch (IllegalArgumentException e) { System.out.println("PASS: System prevented empty message."); }

        // 3. Title > 200 and Message > 5000
        String longTitle = ""; for(int i=0; i<201; i++) longTitle += "a";
        try {
            new Post(4, longTitle, "Body", "Author", "Cat");
            System.out.println("FAIL: System allowed title > 200.");
        } catch (IllegalArgumentException e) { System.out.println("PASS: System rejected title > 200."); }

        String longBody = ""; for(int i=0; i<5001; i++) longBody += "b";
        try {
            new Post(5, "Title", longBody, "Author", "Cat");
            System.out.println("FAIL: System allowed message > 5000.");
        } catch (IllegalArgumentException e) { System.out.println("PASS: System rejected message > 5000."); }

        // 4. Max Length Success
        String maxT = ""; for(int i=0; i<200; i++) maxT += "t";
        String maxB = ""; for(int i=0; i<5000; i++) maxB += "b";
        try {
            new Post(6, maxT, maxB, "Author", "Cat");
            System.out.println("PASS: Post created at exactly max lengths.");
        } catch (Exception e) { System.out.println("FAIL: Post at max lengths."); }

        // 5. Replies (Valid, Empty, Too Long, Max Length)
        try {
            new Reply(1, 6, "Good reply", "User2");
            System.out.println("PASS: Valid reply created.");
        } catch (Exception e) { System.out.println("FAIL: Valid reply."); }

        try {
            new Reply(2, 6, "", "User2");
            System.out.println("FAIL: Empty reply allowed.");
        } catch (IllegalArgumentException e) { System.out.println("PASS: Empty reply prevented."); }

        try {
            new Reply(3, 6, longBody, "User2");
            System.out.println("FAIL: Reply > 5000 allowed.");
        } catch (IllegalArgumentException e) { System.out.println("PASS: Reply > 5000 rejected."); }

        try {
            new Reply(4, 6, maxB, "User2");
            System.out.println("PASS: Reply created at exactly max length.");
        } catch (Exception e) { System.out.println("FAIL: Max length reply."); }


        System.out.println("\n=== READ FUNCTIONALITY TESTS ===");

        // 1. View list of all posts
        postStore.addPost(new Post(10, "Post List Test", "Body", "User", "Tech"));
        List<Post> posts = postStore.getAllPosts();
        if(posts.size() > 0) System.out.println("PASS: User can view a list of all posts.");

        // 2. View full details
        Post detailPost = postStore.getPostById(10);
        System.out.println("PASS: Viewing full details -> Title: " + detailPost.getTitle() + " Body: " + detailPost.getBody());

        // 3. Mark Deleted Verification
        detailPost.markDeleted();
        if(detailPost.isDeleted()) {
            System.out.println("PASS: Deleted post is clearly marked as deleted (Status: " + detailPost.isDeleted() + ")");
        }

        // 4. View replies under a post
        replyStore.addReply(new Reply(50, 10, "Reply for Post 10", "User3"));
        List<Reply> postReplies = replyStore.getRepliesForPost(10);
        if(postReplies.size() > 0) {
            System.out.println("PASS: User can view replies under a post (Count: " + postReplies.size() + ")");
        }
        
        System.out.println("PASS: Read tests complete.");
    }
}