package StudentSummary;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import entityClasses.Post;
import entityClasses.Reply;
import database.Database;

import java.sql.SQLException;
import java.util.List;

/**
 * This is the summary tests class where we will tests to see if it return the correct results we want.
 */
class StudentSummaryTests {
	// create the databse and summary class
	private StudentSummary summary;
	private Database db;
	
	/**
	 * this will initalize the database and add some data before we begin testing.
	 */
	
	@BeforeEach
	public void setUp() throws Exception{
		// initalize both
		summary = new StudentSummary();
		db = StudentSummary.theDatabase;
	}
	
	/**
	 * This is the first test, so we will just initalize a valid student post, then call the getPostCount to see if it return 1
	 */
	@Test
	public void ValidStudentPostCount() {
		// create a valid student with one post.
		String uniqueUser = "Student_" + (int)(Math.random() * 100000);
		Post post = new Post((int)(Math.random() * 100000), "Test", "Test", uniqueUser, "General");
		try {
			// save the post in the database
			db.savePost(post);
			
			// verify that the post count is 1
			assertEquals(1, summary.getPostsCount(uniqueUser));
		} catch (SQLException e) {
			assertEquals(e.getMessage(), "This is a valid test");
		}
	}
	
	/**
	 * will initalize a valid reply for a student, then will call the function
	 * will test to see if the function correctly return the number of reply
	 */
	@Test
	public void validStudentReplyCount() {
		String uniqueUser = "Student_" + (int)(Math.random() * 100000);
		Reply reply = new Reply((int)(Math.random() * 100000), 200, "Test", uniqueUser);
		try {
			// save the reply in the database
			db.saveReply(reply);
			
			//verify it returns a valid reply
			assertEquals(1, summary.getReplyCount(uniqueUser));
		}
		catch(SQLException e) {
			assertEquals(e.getMessage(), "This is a valid test");
		}
	}
	
	/**
	 * This will corectly test to see if we get the list of student Post
	 * will test by correctly creating a valid student reply
	 * then we will call the summary getreply function and get a list of replys from the student
	 * then test it by checking to see if the body of the reply matches the list reply body
	 */
	@Test
	public void ValidGetStudentPost() {
		// generate a unique userId
		String uniqueUser = "Student_" + (int)(Math.random() * 100000);
		Post post = new Post((int)(Math.random() * 100000), "Test", "Test", uniqueUser, "General");
		
		try {
			// save post to the database
			db.savePost(post);
			// create a new list for that students post
			List<Post> student= summary.getStudentPost(uniqueUser);
			
			//check to ensure that the body from the database and list match
			assertEquals("Test", student.get(0).getBody());
		}
		catch(SQLException e) {
			assertEquals(e.getMessage(), "This is a valid test");
		}
	}
	
	/**
	 * This will test to see if the summary getReply function works
	 * will intialize a valid student reply and then call the summary function
	 * what should happen is when we compare the body with the list of student reply body
	 * it should math
	 */
	@Test
	public void ValidGetStudentReply() {
		// generate a unique user id
		String uniqueUser = "Student_" + (int)(Math.random() * 100000);
		Reply reply = new Reply((int)(Math.random() * 100000), 200, "Test", uniqueUser);
		try {
			// save the valid reply in the database
			db.saveReply(reply);
			
			// call the function and save the reply in the list
			List<Reply> studentReply = summary.getStudentReply(uniqueUser);
			
			// then test to ensure the body in the database and the body in the list match
			assertEquals("Test", studentReply.get(0).getBody());
		}
		catch(SQLException e) {
			assertEquals(e.getMessage(), "This is a valid test");
		}
	}

}
