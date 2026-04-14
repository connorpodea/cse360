package entityClasses;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Analyzes discussion activity so the instructional team can verify that a student
 * has answered questions from at least three different students.
 *
 * This class supports grading by counting distinct discussion partners rather than
 * total reply count, which prevents duplicate replies to the same student from
 * incorrectly satisfying the requirement.
 */
public class DiscussionAnalyzer {

    private final PostStorage postStorage;
    private final ReplyStorage replyStorage;

    /**
     * Builds an analyzer using the provided storages.
     *
     * @param postStorage the post storage to inspect
     * @param replyStorage the reply storage to inspect
     * @throws IllegalArgumentException if either storage is null
     */
    public DiscussionAnalyzer(PostStorage postStorage, ReplyStorage replyStorage) {
        if (postStorage == null) {
            throw new IllegalArgumentException("Post storage cannot be null.");
        }
        if (replyStorage == null) {
            throw new IllegalArgumentException("Reply storage cannot be null.");
        }
        this.postStorage = postStorage;
        this.replyStorage = replyStorage;
    }

    /**
     * Counts how many distinct post authors this user has replied to.
     * Replies to the user's own posts are ignored because they do not help
     * satisfy the "three different students" requirement.
     *
     * @param user the replying user
     * @return the number of unique authors replied to
     * @throws IllegalArgumentException if the user is invalid
     */
    public int countUniqueStudentsRepliedTo(User user) {
        return getUniquePostAuthorsRepliedTo(user).size();
    }

    /**
     * Determines whether the user has replied to at least three different students.
     *
     * @param user the replying user
     * @return true when the requirement is met; false otherwise
     * @throws IllegalArgumentException if the user is invalid
     */
    public boolean hasMetThreeStudentRequirement(User user) {
        return countUniqueStudentsRepliedTo(user) >= 3;
    }

    /**
     * Returns the posts this user has replied to, without duplicates.
     * Posts authored by the user are excluded.
     *
     * @param user the replying user
     * @return the distinct posts answered by the user
     * @throws IllegalArgumentException if the user is invalid
     */
    public List<Post> getPostsAnsweredByUser(User user) {
        String userName = requireValidUser(user);
        Set<Integer> seenPostIds = new LinkedHashSet<>();
        List<Post> answeredPosts = new ArrayList<>();

        for (Reply reply : replyStorage.getAllReplies()) {
            if (!userName.equals(reply.getAuthor())) {
                continue;
            }

            Post post;
            try {
                post = postStorage.getPostById(reply.getPostId());
            } catch (IllegalArgumentException ex) {
                // Skip orphaned replies so the analyzer stays usable even if
                // the underlying storage contains a reply to a missing post.
                continue;
            }

            if (userName.equals(post.getAuthor())) {
                continue;
            }

            if (seenPostIds.add(post.getId())) {
                answeredPosts.add(post);
            }
        }

        return answeredPosts;
    }

    /**
     * Returns the set of distinct authors whose posts were answered by the user.
     *
     * @param user the replying user
     * @return a set of unique author usernames
     * @throws IllegalArgumentException if the user is invalid
     */
    public Set<String> getUniquePostAuthorsRepliedTo(User user) {
        String userName = requireValidUser(user);
        Set<String> authors = new LinkedHashSet<>();

        for (Reply reply : replyStorage.getAllReplies()) {
            if (!userName.equals(reply.getAuthor())) {
                continue;
            }

            Post post;
            try {
                post = postStorage.getPostById(reply.getPostId());
            } catch (IllegalArgumentException ex) {
                // Ignore replies that point to missing posts so this method
                // still produces useful results for the grader.
                continue;
            }

            if (!userName.equals(post.getAuthor())) {
                // Only count unique authors so duplicate replies
                // to the same student do not incorrectly increase the count.
                // This ensures the requirement is based on distinct students,
                // not total number of replies.
                authors.add(post.getAuthor());
            }
        }

        return authors;
    }

    /**
     * Validates the user object and returns the username.
     *
     * @param user the user to validate
     * @return the username
     * @throws IllegalArgumentException if the user is null or has an empty name
     */
    private String requireValidUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty.");
        }
        return user.getUserName();
    }
}