package entityClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Title: FilterUnanswered
 * </p>
 *
 * <p>
 * Description:
 * Identifies unanswered student questions based on
 * - Category = "Question"
 * - Not deleted
 * - No external replies
 * </p>
 */
public class FilterUnanswered {
	
	/** 
     * Default constructor to satisfy Javadoc.
     */
    public FilterUnanswered() {
        // Not used.
    }

    /**
     * Identifies unanswered posts.
     *
     * @param postStorage storage of posts
     * @param replyStorage storage of replies
     * @return list of unanswered posts
     */
    public static List<Post> identifyUnanswered(
            PostStorage postStorage,
            ReplyStorage replyStorage) {

        if (postStorage == null || replyStorage == null) {
            throw new IllegalArgumentException(
                    "Storage objects cannot be null.");
        }

        List<Post> unanswered = new ArrayList<>();

        for (Post post : postStorage.getAllPosts()) {
            // Requirement: category Mismatch
            if (!isQuestion(post)) {
                continue;
            }
            // Requirement: deleted Post Filter
            if (post.isDeleted()) {
                continue;
            }
            // Requirement: self-Reply Handling
            if (!hasExternalReply(post, replyStorage)) {
                unanswered.add(post);
            }
        }

        return unanswered;
    }

    /**
     * Checks if post is category Question
     * @param post The post to check
     * @return True if it is a question
     */
    private static boolean isQuestion(Post post) {
        return post.getCategory() != null &&
                post.getCategory()
                        .equalsIgnoreCase("Question");
    }

    /**
     * Checks if post has reply from different author.
     * @param post The post to check
     * @param replyStorage The storage containing replies
     * @return True if an external reply exists
     */
    private static boolean hasExternalReply(
            Post post,
            ReplyStorage replyStorage) {

        List<Reply> replies =
                replyStorage.getRepliesForPost(post.getId());

        for (Reply r : replies) {
            if (!r.getAuthor()
                    .equals(post.getAuthor())) {

                return true;
            }
        }
        return false;
    }
}