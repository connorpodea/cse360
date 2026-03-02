package guiPostManagement;

import entityClasses.Post;
import entityClasses.PostStorage;

/**
 * Stores post data used by the discussion board screens.
 * This model helps users create posts and keeps the shared post list for the assignment stories.
 */
public class ModelPostManagement {
    
    // Reference to the storage class created in entityClasses
    private static PostStorage postStorage = new PostStorage();

    /**
     * Creates the post management model object.
     */
    public ModelPostManagement() {
    }

    /**
     * Creates a post and adds it to storage.
     *
     * @param id the post id
     * @param title the post title
     * @param body the post body
     * @param author the username that created the post
     * @param category the selected post category
     * @return the created post
     */
    public static Post createPost(int id, String title, String body, String author, String category) {
        Post newPost = new Post(id, title, body, author, category);
        postStorage.addPost(newPost);
        
        // This allows the Controller to 'catch' the post and save it to the Database
        return newPost; 
    }
    
    /**
     * Finds the next unused post id.
     *
     * @return the next post id value
     */
    public static int getNextPostId() {
        int maxId = 0;
        for (entityClasses.Post p : postStorage.getAllPosts()) {
            if (p.getId() > maxId) maxId = p.getId();
        }
        return maxId + 1; // Always gives a brand new ID
    }
    
    /**
     * Returns the shared post storage.
     *
     * @return the post storage object
     */
    public static PostStorage getPostStorage() {
        return postStorage;
    }
}
