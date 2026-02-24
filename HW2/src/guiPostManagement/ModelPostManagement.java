package guiPostManagement;

import entityClasses.Post;
import entityClasses.PostStorage;

public class ModelPostManagement {
    
    // Reference to the storage class created in entityClasses
    private static PostStorage postStorage = new PostStorage();

    public static Post createPost(int id, String title, String body, String author, String category) {
        Post newPost = new Post(id, title, body, author, category);
        postStorage.addPost(newPost);
        
        // This allows the Controller to 'catch' the post and save it to the Database
        return newPost; 
    }
    
    public static int getNextPostId() {
        int maxId = 0;
        for (entityClasses.Post p : postStorage.getAllPosts()) {
            if (p.getId() > maxId) maxId = p.getId();
        }
        return maxId + 1; // Always gives a brand new ID
    }
    
    public static PostStorage getPostStorage() {
        return postStorage;
    }
}