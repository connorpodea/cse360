package loginRouting;

/**
 * Identifies the GUI destination selected by the login routing logic.
 *
 * <p>This enum lets the login flow describe the next screen in a way that is
 * easy to test without launching JavaFX during unit tests.</p>
 */
public enum LoginDestination {

    /**
     * Route the user to the administrator home page.
     */
    ADMIN_HOME,

    /**
     * Route the user to the student board GUI.
     */
    STUDENT_BOARD,

    /**
     * Route the user to the reviewer board GUI.
     */
    REVIEWER_BOARD,

    /**
     * Route the user to a role-selection page because the account has more
     * than one active role.
     */
    MULTI_ROLE_DISPATCH
}