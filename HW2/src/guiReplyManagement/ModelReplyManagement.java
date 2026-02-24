package guiReplyManagement;

import entityClasses.Reply;
import entityClasses.ReplyStorage;

/**
 * Model layer for managing replies in memory.
 * Handles creating replies and tracking IDs.
 */
public class ModelReplyManagement {
    
    // Stores replies while the app is running
    private static ReplyStorage replyStorage = new ReplyStorage();

    /**
     * Generates the next available reply ID.
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
     */
    public static entityClasses.Reply createReply(int id, int parentPostId, String body, String author) {
        entityClasses.Reply newReply = new entityClasses.Reply(id, parentPostId, body, author);
        replyStorage.addReply(newReply);
        
        // Controller uses this to save to the database
        return newReply; 
    }
    
    /**
     * Provides access to the reply storage.
     */
    public static ReplyStorage getReplyStorage() {
        return replyStorage; // Shared storage instance
    }
}
