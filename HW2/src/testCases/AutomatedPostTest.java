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
        
        System.out.println("\n=== UPDATE FUNCTIONALITY TESTS ===");

        // 1. Update Post (Positive)
        try {
            Post updatePost = new Post(20, "Old Title", "Old Body", "Student1", "General");
            updatePost.update("New Title", "New Body");
            if (updatePost.getTitle().equals("New Title") && updatePost.getBody().equals("New Body")) {
                System.out.println("PASS: Existing post updated with valid new title/body.");
            } else {
                System.out.println("FAIL: Post update did not apply new values.");
            }
        } catch (Exception e) { System.out.println("FAIL: Valid post update."); }

        // 2. Update Post (Negative)
        try {
            Post deletedUpdatePost = new Post(21, "Keep Title", "Keep Body", "Student1", "General");
            deletedUpdatePost.markDeleted();
            deletedUpdatePost.update("Blocked Title", "Blocked Body");
            if (deletedUpdatePost.getTitle().equals("Keep Title") && deletedUpdatePost.getBody().equals("Keep Body")) {
                System.out.println("PASS: System blocked updating a deleted post.");
            } else {
                System.out.println("FAIL: Deleted post was updated.");
            }
        } catch (Exception e) { System.out.println("FAIL: Deleted post update block."); }

        try {
            Post emptyUpdatedTitle = new Post(22, "Start Title", "Start Body", "Student1", "General");
            emptyUpdatedTitle.update("", "Still Good Body");
            System.out.println("FAIL: System allowed empty updated title.");
        } catch (IllegalArgumentException e) { System.out.println("PASS: System prevented empty updated title."); }

        try {
            Post emptyUpdatedBody = new Post(23, "Start Title", "Start Body", "Student1", "General");
            emptyUpdatedBody.update("Still Good Title", "   ");
            System.out.println("FAIL: System allowed empty updated body.");
        } catch (IllegalArgumentException e) { System.out.println("PASS: System prevented empty updated body."); }

        try {
            Post longUpdatedTitle = new Post(24, "Start Title", "Start Body", "Student1", "General");
            longUpdatedTitle.update(longTitle, "Valid Body");
            System.out.println("FAIL: System allowed updated title > 200.");
        } catch (IllegalArgumentException e) { System.out.println("PASS: System rejected updated title > 200."); }

        try {
            Post longUpdatedBody = new Post(25, "Start Title", "Start Body", "Student1", "General");
            longUpdatedBody.update("Valid Title", longBody);
            System.out.println("FAIL: System allowed updated body > 5000.");
        } catch (IllegalArgumentException e) { System.out.println("PASS: System rejected updated body > 5000."); }

        try {
            Post maxUpdatedPost = new Post(26, "Start Title", "Start Body", "Student1", "General");
            maxUpdatedPost.update(maxT, maxB);
            if (maxUpdatedPost.getTitle().equals(maxT) && maxUpdatedPost.getBody().equals(maxB)) {
                System.out.println("PASS: Post updated at exactly max lengths.");
            } else {
                System.out.println("FAIL: Max length post update did not apply new values.");
            }
        } catch (Exception e) { System.out.println("FAIL: Post update at max lengths."); }

        // 3. Update Reply (Positive)
        try {
            Reply updatedReply = new Reply(60, 10, "Old reply body", "User2");
            updatedReply.update("New reply body");
            if (updatedReply.getBody().equals("New reply body")) {
                System.out.println("PASS: Existing reply updated with valid new body.");
            } else {
                System.out.println("FAIL: Reply update did not apply new body.");
            }
        } catch (Exception e) { System.out.println("FAIL: Valid reply update."); }

        System.out.println("\n=== DELETE FUNCTIONALITY TESTS ===");

        // 1. Delete Post (Positive)
        try {
            Post deletePost = new Post(30, "Delete Me", "Delete Body", "Student1", "General");
            Reply keepReply = new Reply(70, 30, "Reply stays", "User3");
            postStore.addPost(deletePost);
            replyStore.addReply(keepReply);
            deletePost.markDeleted();
            if (deletePost.isDeleted()) {
                System.out.println("PASS: Existing post marked deleted and not removed.");
            } else {
                System.out.println("FAIL: Post deleted flag was not set.");
            }
            if (replyStore.getRepliesForPost(30).size() > 0) {
                System.out.println("PASS: Replies still exist and are accessible after post deletion.");
            } else {
                System.out.println("FAIL: Replies did not remain after post deletion.");
            }
        } catch (Exception e) { System.out.println("FAIL: Positive post delete rule test."); }

        // 2. Delete Reply (Positive)
        try {
            Reply deleteReply = new Reply(80, 10, "Delete reply", "User4");
            replyStore.addReply(deleteReply);
            replyStore.deleteReply(80);
            if (!replyStore.exists(80)) {
                System.out.println("PASS: Reply removed successfully.");
            } else {
                System.out.println("FAIL: Reply still exists after delete.");
            }
        } catch (Exception e) { System.out.println("FAIL: Positive reply delete test."); }

        System.out.println("PASS: Read tests complete.");
    }
}
