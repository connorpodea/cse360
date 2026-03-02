package entityClasses;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stores posts in memory for the discussion board.
 * This class supports assignment stories that create, list, update, and find posts.
 */
public class PostStorage {

    private final List<Post> posts;

    /**
     * Creates an empty storage list.
     */
    public PostStorage() {
        this.posts = new ArrayList<>();
    }

    /**
     * Adds a post to storage.
     *
     * @param post the post to add
     */
    public void addPost(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Cannot add a null post.");
        }
        posts.add(post);
    }

    /**
     * Returns all stored posts.
     *
     * @return a copy of the stored posts
     */
    public List<Post> getAllPosts() {
        return new ArrayList<>(posts);
    }

    /**
     * Finds a post by id.
     *
     * @param id the post id
     * @return the matching post
     */
    public Post getPostById(int id) {
        return posts.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Post with the given ID does not exist.")
                );
    }

    /**
     * Finds posts that match the given text.
     *
     * @param filter the search text
     * @return the matching posts
     */
    public List<Post> getPostsByFilter(String filter) {
        if (filter == null) {
            throw new IllegalArgumentException("Search filter cannot be null.");
        }

        return posts.stream()
                .filter(p ->
                        p.getTitle().toLowerCase().contains(filter.toLowerCase()) ||
                        p.getBody().toLowerCase().contains(filter.toLowerCase())
                )
                .collect(Collectors.toList());
    }

    /**
     * Removes a post by id.
     *
     * @param id the post id
     */
    public void removePostById(int id) {
        Post postToRemove = posts.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Cannot remove post that does not exist.")
                );

        posts.remove(postToRemove);
    }
    
    /**
     * Loads posts from the database into memory.
     */
    public void loadAllPostsFromDatabase() {
        try {
            // Fetch rows from 'postDB' table
            // For each row: Post p = new Post(id, title, body, author, category, time);
            // this.posts.add(p);
        } catch (Exception e) {
            System.out.println("Error loading posts: " + e.getMessage());
        }
    }
    
    /**
     * Deletes a post by id.
     *
     * @param id the post id
     */
    public void deletePost(int id) {
        posts.removeIf(p -> p.getId() == id);
    }
    
    /**
     * Checks whether a post exists.
     *
     * @param id the post id
     * @return true when the post exists
     */
    public boolean exists(int id) {
        return posts.stream().anyMatch(p -> p.getId() == id);
    }
}
