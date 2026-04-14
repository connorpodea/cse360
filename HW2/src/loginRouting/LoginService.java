package loginRouting;

/**
 * Applies the application's login-to-home-screen routing rules.
 *
 * <p>This class isolates the role-based decision from the JavaFX controllers
 * so the requirement can be verified with fast unit tests.</p>
 */
public class LoginService {

    /**
     * Routes a successfully authenticated user to the correct destination.
     *
     * <p>Business rule covered here:
     * "Login as Student -> Opens Student Board GUI, no admin options visible."</p>
     *
     * @param isAdmin {@code true} when the user has the admin role
     * @param isStudent {@code true} when the user has the student role
     * @param isReviewer {@code true} when the user has the reviewer role
     * @return an immutable description of the GUI that should open
     * @throws IllegalArgumentException when the user has no active roles
     */
    public LoginResult routeUser(boolean isAdmin, boolean isStudent, boolean isReviewer) {
        int activeRoleCount = countActiveRoles(isAdmin, isStudent, isReviewer);

        if (activeRoleCount == 0) {
            throw new IllegalArgumentException("A logged-in user must have at least one role.");
        }

        // Multiple active roles must go through a role-selection screen first.
        if (activeRoleCount > 1) {
            return new LoginResult(
                    LoginDestination.MULTI_ROLE_DISPATCH,
                    "Multiple Role Dispatch GUI",
                    false);
        }

        // Exactly one role is active, so route directly to that role's GUI.
        if (isAdmin) {
            return new LoginResult(
                    LoginDestination.ADMIN_HOME,
                    "Admin Home GUI",
                    true);
        }

        if (isStudent) {
            return new LoginResult(
                    LoginDestination.STUDENT_BOARD,
                    "Student Board GUI",
                    false);
        }

        return new LoginResult(
                LoginDestination.REVIEWER_BOARD,
                "Reviewer Board GUI",
                false);
    }

    /**
     * Counts how many roles are active for the current user.
     *
     * @param isAdmin whether the admin role is active
     * @param isStudent whether the student role is active
     * @param isReviewer whether the reviewer role is active
     * @return the number of active roles
     */
    private int countActiveRoles(boolean isAdmin, boolean isStudent, boolean isReviewer) {
        int activeRoleCount = 0;

        // Increment once per enabled role so we can distinguish single-role
        // logins from accounts that need the multi-role dispatch screen.
        if (isAdmin) {
            activeRoleCount++;
        }
        if (isStudent) {
            activeRoleCount++;
        }
        if (isReviewer) {
            activeRoleCount++;
        }

        return activeRoleCount;
    }
}