package entityClasses;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stores replies in memory and lets other classes get them.
 * Handles basic operations on replies.
 */
public class ReplyStorage {

    private List<Reply> replies;

    /**
     * Makes a new storage list.
     */
    public ReplyStorage() {
        this.replies = new ArrayList<>();
    }

    /**
     * Adds a reply to the list.
     * @param reply the reply to add
     */
    public void addReply(Reply reply) {
        if (reply == null) {
            throw new IllegalArgumentException("Cannot add a null reply.");
        }
        replies.add(reply);
    }

    /**
     * Finds a reply by id.
     * @param id the reply id
     * @return the reply
     */
    public Reply getReplyById(int id) {
        return replies.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Reply with the given ID does not exist.")
                );
    }

    /**
     * Updates a reply body.
     * @param id the reply id
     * @param newBody the updated body
     */
    public void updateReply(int id, String newBody) {
        Reply reply = replies.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Cannot update a reply that does not exist.")
                );

        reply.update(newBody);
    }

    /**
     * Deletes a reply by id.
     * @param id the reply id
     */
    public void deleteReply(int id) {
        Reply reply = replies.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Cannot delete reply because it does not exist.")
                );

        replies.remove(reply);
    }

    /**
     * Gets all replies for a post.
     * @param postId the post id
     * @return the replies for the post
     */
    public List<Reply> getRepliesForPost(int postId) {
        return replies.stream()
                .filter(r -> r.getPostId() == postId)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Filters replies by keyword.
     * @param filter the search text
     * @return the matching replies
     */
    public List<Reply> getRepliesByFilter(String filter) {
        if (filter == null) {
            throw new IllegalArgumentException("Search filter cannot be null.");
        }

        return replies.stream()
                .filter(r -> r.getBody().toLowerCase().contains(filter.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Returns all replies (copy of list).
     * @return all replies
     */
    public List<Reply> getAllReplies() {
        return new ArrayList<>(replies);
    }

    /**
     * Checks if a reply exists.
     * @param id the reply id
     * @return true if the reply exists
     */
    public boolean exists(int id) {
        return replies.stream().anyMatch(r -> r.getId() == id);
    }
    
    /**
     * Clears the whole list.
     */
    public void clearAll() {
        replies.clear();
    }

}
