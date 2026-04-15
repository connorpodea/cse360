package entityClasses;

import java.time.LocalDateTime;

/**
 * Stores one private staff whisper for a student's post.
 */
public class WhisperMessage {

    private final int id;
    private final int postId;
    private final String targetUser;
    private final String postTitle;
    private final String staffAuthor;
    private final String message;
    private final LocalDateTime timestamp;

    /**
     * Creates a whisper message record.
     * @param id the whisper id
     * @param postId the related post id
     * @param targetUser the student receiving the whisper
     * @param postTitle the title of the related post
     * @param staffAuthor the staff member who sent the whisper
     * @param message the whisper text
     * @param timestamp when the whisper was sent
     */
    public WhisperMessage(int id, int postId, String targetUser, String postTitle,
            String staffAuthor, String message, LocalDateTime timestamp) {
        this.id = id;
        this.postId = postId;
        this.targetUser = targetUser;
        this.postTitle = postTitle;
        this.staffAuthor = staffAuthor;
        this.message = message;
        this.timestamp = timestamp;
    }

    /**
     * @return the whisper id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the related post id
     */
    public int getPostId() {
        return postId;
    }

    /**
     * @return the student receiving the whisper
     */
    public String getTargetUser() {
        return targetUser;
    }

    /**
     * @return the related post title
     */
    public String getPostTitle() {
        return postTitle;
    }

    /**
     * @return the staff author
     */
    public String getStaffAuthor() {
        return staffAuthor;
    }

    /**
     * @return the whisper text
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return when the whisper was sent
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
