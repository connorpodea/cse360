package MVCPostManagement;

import java.util.List;

import MVCReplyManagement.ModelReplyManagement;
import entityClasses.DiscussionAnalyzer;
import entityClasses.Post;
import entityClasses.PostStorage;
import entityClasses.StudentParticipationSummary;

/** Model for post management. */
public class ModelPostManagement {
    
    // Reference to the storage class created in entityClasses
    private static PostStorage postStorage = new PostStorage();

    /** Creates the model object. */
    public ModelPostManagement() {}

    /**
     * Creates a post.
     * @param id the post id
     * @param title the post title
     * @param body the post body
     * @param author the post author
     * @param category the post category
     * @return the new post
     */
    public static Post createPost(int id, String title, String body, String author, String category) {
        Post newPost = new Post(id, title, body, author, category);
        postStorage.addPost(newPost);
        
        // This allows the Controller to 'catch' the post and save it to the Database
        return newPost; 
    }
    
    /**
     * Returns the next post id.
     * @return the next post id
     */
    public static int getNextPostId() {
        int maxId = 0;
        for (entityClasses.Post p : postStorage.getAllPosts()) {
            if (p.getId() > maxId) maxId = p.getId();
        }
        return maxId + 1; // Always gives a brand new ID
    }
    
    /**
     * Returns the post storage.
     * @return the post storage
     */
    public static PostStorage getPostStorage() {
        return postStorage;
    }

    /**
     * Returns the shared discussion analyzer used by staff-facing features.
     * @return the discussion analyzer
     */
    public static DiscussionAnalyzer getDiscussionAnalyzer() {
        return new DiscussionAnalyzer(postStorage, ModelReplyManagement.getReplyStorage());
    }

    /**
     * Returns a participation summary for the specified user.
     * @param user the user to summarize
     * @return the participation summary
     */
    public static StudentParticipationSummary getStudentParticipationSummary(entityClasses.User user) {
        return getDiscussionAnalyzer().summarizeStudent(user);
    }

    /**
     * Returns unanswered question posts for grading review.
     * @return the unanswered posts
     */
    public static List<Post> getUnansweredPosts() {
        return getDiscussionAnalyzer().getUnansweredQuestionPosts();
    }
}
