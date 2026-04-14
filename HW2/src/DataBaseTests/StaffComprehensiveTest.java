package DataBaseTests;

// Correct imports for JUnit Jupiter (JUnit 5)
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.sql.SQLException;
import java.util.List;
import database.Database;
import entityClasses.Post;

public class StaffComprehensiveTest {

    private Database db;

    @BeforeEach
    void setUp() throws SQLException {
        // Access the database singleton
        db = applicationMain.FoundationsMain.database;
        if (db == null) {
            db = new Database();
        }
        db.connectToDatabase();
    }

    @Test
    void testStaffFunctionality() throws SQLException {
        
        // staff CRUD
        int postId = 777;
        Post p = new Post(postId, "Test", "Content", "StudentA", "Homework");
        db.savePost(p);
        
        db.updatePost(postId, "Moderated by Staff");
        List<Post> posts = db.loadAllPosts();
        assertTrue(posts.stream().anyMatch(post -> post.getBody().equals("Moderated by Staff")), 
            "Staff should be able to update any post.");

        // whisper
        // Verify the method exists and executes for the post
        db.updatePostFeedback(postId, "Private staff message", true);
        
        // requests
        db.saveRequest("Account Fix", "Fix student profile", "StaffB");
        List<String[]> requests = db.loadAllRequests();
        int reqId = Integer.parseInt(requests.get(0)[0]);
        
        db.closeRequest(reqId, "Fixed.");
        db.reopenRequest(reqId, "Still broken.", "StaffB");
        
        List<String[]> updatedRequests = db.loadAllRequests();
        // Index 0 is the newest (reopened) request
        assertEquals("REOPENED", updatedRequests.get(0)[4]);
        assertEquals(String.valueOf(reqId), updatedRequests.get(0)[6], 
            "Reopened request must link to the original ID.");

        // parameters
        db.saveEvaluationParameter("Quality", "Must be high quality.");
        List<String[]> params = db.getEvaluationParameters();
        assertTrue(params.stream().anyMatch(param -> param[0].equals("Quality")),
            "Yuvi Story: Evaluation parameter failed to save.");

        // Cleanup
        db.deletePost(postId);
    }
}