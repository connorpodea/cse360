package entityClasses;

/**
 * Stores the participation counts used by staff when reviewing a student's discussion work.
 * This keeps the summary data in one place for grading and decision support.
 */
public class StudentParticipationSummary {

    private final String userName;
    private final int postCount;
    private final int replyCount;
    private final int repliesToOtherStudentsCount;
    private final int distinctStudentsAnsweredCount;
    private final boolean metThreeStudentRequirement;

    /**
     * Creates a summary of one student's discussion participation.
     *
     * @param userName the student user name
     * @param postCount the number of authored posts
     * @param replyCount the number of authored replies
     * @param repliesToOtherStudentsCount the number of valid replies to other students
     * @param distinctStudentsAnsweredCount the number of distinct students answered
     * @param metThreeStudentRequirement whether the student met the requirement
     */
    public StudentParticipationSummary(String userName, int postCount, int replyCount,
            int repliesToOtherStudentsCount, int distinctStudentsAnsweredCount,
            boolean metThreeStudentRequirement) {
        this.userName = userName;
        this.postCount = postCount;
        this.replyCount = replyCount;
        this.repliesToOtherStudentsCount = repliesToOtherStudentsCount;
        this.distinctStudentsAnsweredCount = distinctStudentsAnsweredCount;
        this.metThreeStudentRequirement = metThreeStudentRequirement;
    }

    /**
     * @return the student user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return the number of authored posts
     */
    public int getPostCount() {
        return postCount;
    }

    /**
     * @return the number of authored replies
     */
    public int getReplyCount() {
        return replyCount;
    }

    /**
     * @return the number of valid replies to other students
     */
    public int getRepliesToOtherStudentsCount() {
        return repliesToOtherStudentsCount;
    }

    /**
     * @return the number of distinct students answered
     */
    public int getDistinctStudentsAnsweredCount() {
        return distinctStudentsAnsweredCount;
    }

    /**
     * @return true if the student met the requirement
     */
    public boolean hasMetThreeStudentRequirement() {
        return metThreeStudentRequirement;
    }
}
