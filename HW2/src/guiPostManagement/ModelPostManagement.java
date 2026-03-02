package guiPostManagement;

import entityClasses.Post;
import entityClasses.PostStorage;

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
}
