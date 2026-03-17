package MVCReplyManagement;

import entityClasses.Reply;
import entityClasses.ReplyStorage;

/**
 * Model layer for managing replies in memory.
 * Handles creating replies and tracking IDs.
 */
public class ModelReplyManagement {
    
    // Stores replies while the app is running
    private static ReplyStorage replyStorage = new ReplyStorage();

    /** Creates the model object. */
    public ModelReplyManagement() {}

    /**
     * Generates the next available reply ID.
     * @return the next reply id
     */
    public static int getNextReplyId() {
        // Start IDs at 1 if there are no replies yet
        if (replyStorage.getAllReplies().isEmpty()) {
            return 1;
        }
        int maxId = 0;
        for (entityClasses.Reply r : replyStorage.getAllReplies()) {
            if (r.getId() > maxId) maxId = r.getId();
        }
        // Always return something higher than what already exists
        return maxId + 1;
    }

    /**
     * Creates a new reply and stores it in memory.
     * @param id the reply id
     * @param parentPostId the parent post id
     * @param body the reply body
     * @param author the reply author
     * @return the new reply
     */
    public static entityClasses.Reply createReply(int id, int parentPostId, String body, String author) {
        entityClasses.Reply newReply = new entityClasses.Reply(id, parentPostId, body, author);
        replyStorage.addReply(newReply);
        
        // Controller uses this to save to the database
        return newReply; 
    }
    
    /**
     * Provides access to the reply storage.
     * @return the reply storage
     */
    public static ReplyStorage getReplyStorage() {
        return replyStorage; // Shared storage instance
    }
}
