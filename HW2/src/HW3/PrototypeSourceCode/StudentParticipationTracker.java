package HW3.PrototypeSourceCode;

import java.util.List;

import entityClasses.Post;
import entityClasses.Reply;

/**
 * Prototype class for checking student reply participation.
 * Counts replies made to other students and checks the 3-reply rule.
 */
public class StudentParticipationTracker {

    /**
     * Counts how many replies a student made to other students' posts.
     * Self-replies do not count toward the requirement.
     *
     * @param studentName the student being checked
     * @param posts the posts in the discussion
     * @param replies the replies in the discussion
     * @return the number of valid replies to other students
     */
    public int countRepliesToOtherStudents(String studentName, List<Post> posts, List<Reply> replies) {
        int count = 0;

        for (Reply reply : replies) {
            if (!reply.getAuthor().equals(studentName)) {
                continue;
            }

            // A reply only counts if it is attached to someone else's post.
            for (Post post : posts) {
                if (post.getId() == reply.getPostId() && !post.getAuthor().equals(studentName)) {
                    count++;
                    break;
                }
            }
        }

        return count;
    }

    /**
     * Checks whether a student met the reply requirement.
     *
     * @param studentName the student being checked
     * @param posts the posts in the discussion
     * @param replies the replies in the discussion
     * @return true if the student made at least 3 valid replies
     */
    public boolean hasMetReplyRequirement(String studentName, List<Post> posts, List<Reply> replies) {
        return countRepliesToOtherStudents(studentName, posts, replies) >= 3;
    }
}
