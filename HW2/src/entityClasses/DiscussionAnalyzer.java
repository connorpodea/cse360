package entityClasses;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Collections;

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
     * Counts how many valid replies this user made to posts owned by other students.
     * Self-replies and replies to missing posts are ignored.
     *
     * @param user the replying user
     * @return the number of valid replies to other students
     * @throws IllegalArgumentException if the user is invalid
     */
    public int countRepliesToOtherStudents(User user) {
        String userName = requireValidUser(user);
        int count = 0;

        for (Reply reply : replyStorage.getAllReplies()) {
            if (!userName.equals(reply.getAuthor())) {
                continue;
            }

            Post post = findPostForReply(reply);
            if (post == null || userName.equals(post.getAuthor())) {
                continue;
            }

            count++;
        }

        return count;
    }

    /**
     * Returns whether the user has made at least three valid replies to other students.
     *
     * @param user the replying user
     * @return true when the three-reply requirement is met
     * @throws IllegalArgumentException if the user is invalid
     */
    public boolean hasMetThreeReplyRequirement(User user) {
        return countRepliesToOtherStudents(user) >= 3;
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

            Post post = findPostForReply(reply);
            if (post == null) {
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
     * Returns all posts authored by the specified user.
     *
     * @param user the user to inspect
     * @return the user's posts
     * @throws IllegalArgumentException if the user is invalid
     */
    public List<Post> getPostsByUser(User user) {
        String userName = requireValidUser(user);
        List<Post> posts = new ArrayList<>();

        for (Post post : postStorage.getAllPosts()) {
            if (userName.equals(post.getAuthor())) {
                posts.add(post);
            }
        }

        return posts;
    }

    /**
     * Returns all replies authored by the specified user.
     *
     * @param user the user to inspect
     * @return the user's replies
     * @throws IllegalArgumentException if the user is invalid
     */
    public List<Reply> getRepliesByUser(User user) {
        String userName = requireValidUser(user);
        List<Reply> replies = new ArrayList<>();

        for (Reply reply : replyStorage.getAllReplies()) {
            if (userName.equals(reply.getAuthor())) {
                replies.add(reply);
            }
        }

        return replies;
    }

    /**
     * Counts how many posts were authored by the specified user.
     *
     * @param user the user to inspect
     * @return the number of authored posts
     * @throws IllegalArgumentException if the user is invalid
     */
    public int countPostsByUser(User user) {
        return getPostsByUser(user).size();
    }

    /**
     * Counts how many replies were authored by the specified user.
     *
     * @param user the user to inspect
     * @return the number of authored replies
     * @throws IllegalArgumentException if the user is invalid
     */
    public int countRepliesByUser(User user) {
        return getRepliesByUser(user).size();
    }

    /**
     * Builds a participation summary for the specified user.
     *
     * @param user the user to summarize
     * @return a summary object for instructor/grader review
     * @throws IllegalArgumentException if the user is invalid
     */
    public StudentParticipationSummary summarizeStudent(User user) {
        String userName = requireValidUser(user);
        return new StudentParticipationSummary(
                userName,
                countPostsByUser(user),
                countRepliesByUser(user),
                countRepliesToOtherStudents(user),
                countUniqueStudentsRepliedTo(user),
                hasMetThreeStudentRequirement(user));
    }

    /**
     * Returns all unanswered question posts using the shared unanswered filter logic.
     *
     * @return the unanswered question posts
     */
    public List<Post> getUnansweredQuestionPosts() {
        return Collections.unmodifiableList(
                FilterUnanswered.identifyUnanswered(postStorage, replyStorage));
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

    /**
     * Finds the parent post for a reply.
     *
     * @param reply the reply to inspect
     * @return the parent post, or null when the reply points to a missing post
     */
    private Post findPostForReply(Reply reply) {
        try {
            return postStorage.getPostById(reply.getPostId());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
