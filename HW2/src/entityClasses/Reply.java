package entityClasses;
import java.time.LocalDateTime;

/**
 * Represents a reply written for a post.
 * This class supports assignment stories where users discuss a post with follow-up messages.
 */
public class Reply {

    private int id;
    private int postId;
    private String body;
    private String author;
    private LocalDateTime timestamp;
    /** Maximum number of characters allowed in a reply body. */
    public static final int MAX_BODY_LENGTH = 5000;

    /**
     * Creates a reply object for a post.
     *
     * @param id the reply id
     * @param postId the parent post id
     * @param body the reply text
     * @param author the username that wrote the reply
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
     *
     * @param newBody the updated reply text
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
     *
     * @return the parent post id
     */
    public int getParentPostId() {
        return postId;
    }

    /**
     * Returns the reply id.
     *
     * @return the reply id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the post id.
     *
     * @return the parent post id
     */
    public int getPostId() {
        return postId;
    }

    /**
     * Returns the reply text.
     *
     * @return the reply body
     */
    public String getBody() {
        return body;
    }

    /**
     * Returns the username of the reply author.
     *
     * @return the reply author username
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns when this reply was made.
     *
     * @return the reply timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
