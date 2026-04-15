package entityClasses;
import java.time.LocalDateTime;

/**
 * Represents a reply on a post.
 * Keeps basic info about the reply.
 */
public class Reply {

    private int id;
    private int postId;
    private String body;
    private String author;
    private LocalDateTime timestamp;
    /** The maximum reply body length. */
    public static final int MAX_BODY_LENGTH = 5000;

    /**
     * Creates a new reply.
     * @param id the reply id
     * @param postId the post id
     * @param body the reply body
     * @param author the reply author
     */
    public Reply(int id, int postId, String body, String author) {
        validateCreate(body, author);
        this.id = id;
        this.postId = postId;
        this.body = body;
        this.author = author;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Creates a reply using an existing timestamp from storage.
     * @param id the reply id
     * @param postId the post id
     * @param body the reply body
     * @param author the reply author
     * @param timestamp when the reply was created
     */
    public Reply(int id, int postId, String body, String author, LocalDateTime timestamp) {
        validateCreate(body, author);
        this.id = id;
        this.postId = postId;
        this.body = body;
        this.author = author;
        this.timestamp = timestamp;
    }

    /**
     * Checks values when creating a reply.
     */
    private void validateCreate(String body, String author) {
        if (body == null || body.trim().isEmpty()) {
            throw new IllegalArgumentException("Reply body cannot be empty.");
        }
        if (body.length() > MAX_BODY_LENGTH) {
        	throw new IllegalArgumentException("Reply body exceeds 5000 characters.");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Reply author cannot be empty.");
        }
    }

    /**
     * Updates the reply body.
     * @param newBody the updated body
     */
    public void update(String newBody) {
        validateUpdate(newBody);
        this.body = newBody;
    }

    /**
     * Makes sure updated body isnt empty.
     */
    private void validateUpdate(String newBody) {
        if (newBody == null || newBody.trim().isEmpty()) {
            throw new IllegalArgumentException("Updated reply body cannot be empty.");
        }
        if (newBody.length() > MAX_BODY_LENGTH) {
            throw new IllegalArgumentException("Updated reply body exceeds 5000 characters.");
        }
        
    }
    
    /**
     * Gets the id of the post this reply belongs to.
     * @return the post id
     */
    public int getParentPostId() {
        return postId;
    }

    /**
     * Returns the reply id.
     * @return the reply id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the post id.
     * @return the post id
     */
    public int getPostId() {
        return postId;
    }

    /**
     * Returns the reply text.
     * @return the reply text
     */
    public String getBody() {
        return body;
    }

    /**
     * Returns the username of who wrote this.
     * @return the author user name
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns when this reply was made.
     * @return the reply time
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
