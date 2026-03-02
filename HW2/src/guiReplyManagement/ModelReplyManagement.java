package guiReplyManagement;

import entityClasses.Reply;
import entityClasses.ReplyStorage;

/**
 * Stores reply data used by the reply screens.
 * This model helps users add replies and keeps the shared reply list for the assignment.
 */
public class ModelReplyManagement {
    
    // Stores replies while the app is running
    private static ReplyStorage replyStorage = new ReplyStorage();
    /**
     * Generates the next available reply id.
     *
     * @return the next reply id value
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
     * Creates a reply and adds it to storage.
     *
     * @param id the reply id
     * @param parentPostId the parent post id
     * @param body the reply body
     * @param author the username that wrote the reply
     * @return the created reply
     */
    public static entityClasses.Reply createReply(int id, int parentPostId, String body, String author) {
        entityClasses.Reply newReply = new entityClasses.Reply(id, parentPostId, body, author);
        replyStorage.addReply(newReply);
        
        // Controller uses this to save to the database
        return newReply; 
    }
    
    /**
     * Returns the shared reply storage.
     *
     * @return the reply storage object
     */
    public static ReplyStorage getReplyStorage() {
        return replyStorage; // Shared storage instance
    }
}
