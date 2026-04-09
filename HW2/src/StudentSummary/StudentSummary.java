package StudentSummary;

import entityClasses.Post;
import entityClasses.Reply;
import database.Database;
import java.util.List;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * StudentSummary will help create the table summary of each student.
 * it will return all reply's from a specific student
 * it will return all posts from a specific student
 * it will return all posts count
 * it will return all reply count
 */
public class StudentSummary {
	public static Database theDatabase = new Database();
	
	/**
     * it will just connect the database so we have access to the users posts..
     */
    public StudentSummary() throws SQLException{
    	theDatabase = new Database();
	    theDatabase.connectToDatabase();
    }
    
    /**
     * this just loops through the database and checks all the posts to see which is made from that specific author
     * @param userName: is the specific student that we want to get all posts from
     * @return will return a list of that students posts
     */
    public List<Post> getStudentPost(String userName){
    	List<Post> studentPost;
    	try {
        	List<Post> returnStudentPost = new ArrayList<Post>();
    		studentPost = theDatabase.loadAllPosts();
    		
    		for(Post post : studentPost) {
    			if(post.getAuthor().equals(userName)) {
    				returnStudentPost.add(post);
    			}
    		}
    		return returnStudentPost;
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	return null;
    }
    
    /**
     * just loops through the database's reply's and matches it will the specific student.
     * @param userName: is the specific user we want all the reply's from.
     * @return: will return a list of that students reply's in the database.
     */
    public List<Reply> getStudentReply(String userName){
    	List<Reply> studentReply;
    	
    	try {
    		List<Reply> returnStudentReply = new ArrayList<Reply>();
    		studentReply = theDatabase.loadAllReplies();
    		
    		for(Reply reply: studentReply) {
    			if(reply.getAuthor().equals(userName)) {
    				returnStudentReply.add(reply);
    			}
    		}
    		return returnStudentReply;
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	return null;
    }
    
    /**
     * just loops through the posts database and makes sure that we count the number of posts made by that user.
     * @param userName: The student we want to count the reply's for.
     * @return: returns the number of posts that student made.
     */
    public int getPostsCount(String userName){
    	List<Post> studentPosts;
    	int numPost = 0;
		try {
			studentPosts = theDatabase.loadAllPosts();
			
			for(Post post : studentPosts) {
	    		if(post.getAuthor().equals(userName)) {
	    			numPost++;
	    		}
	    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return numPost;
    }
    
    /**
     * just loops through the database's reply's and checks to see if it matches the student.
     * @param userName: the student we want to see how many reply's has been made.
     * @return: Will return the number of reply's the student has made.
     */
    public int getReplyCount(String userName) {
    	int numReply = 0;
    	
    	List<Reply> studentReply;
    	
    	try {
    		studentReply = theDatabase.loadAllReplies();
    		
    		for(Reply reply : studentReply) {
    			if(reply.getAuthor().equals(userName)) {
    				numReply++;
    			}
    		}
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	
    	return numReply;
    }

    
}
