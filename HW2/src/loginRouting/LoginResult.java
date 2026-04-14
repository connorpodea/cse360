package loginRouting;

/**
 * Stores the outcome of login routing after the application has already
 * determined that a user's credentials are valid.
 *
 * <p>The result intentionally contains only simple data so that the business
 * rule can be tested without creating GUI objects.</p>
 */
public final class LoginResult {

    /**
     * The screen that should be displayed next.
     */
    private final LoginDestination destination;

    /**
     * The user-facing name of the destination GUI.
     */
    private final String destinationDisplayName;

    /**
     * Whether administrator-only controls should be visible on the next screen.
     */
    private final boolean adminOptionsVisible;

    /**
     * Creates an immutable login routing result.
     *
     * @param destination the logical destination selected by the routing rule
     * @param destinationDisplayName the human-readable GUI name
     * @param adminOptionsVisible {@code true} when admin-only controls should
     *        be shown; {@code false} otherwise
     */
    public LoginResult(LoginDestination destination, String destinationDisplayName,
            boolean adminOptionsVisible) {
        if (destination == null) {
            throw new IllegalArgumentException("destination must not be null");
        }
        if (destinationDisplayName == null || destinationDisplayName.trim().isEmpty()) {
            throw new IllegalArgumentException("destinationDisplayName must not be blank");
        }

        this.destination = destination;
        this.destinationDisplayName = destinationDisplayName;
        this.adminOptionsVisible = adminOptionsVisible;
    }

    /**
     * Gets the logical destination for the login request.
     *
     * @return the selected destination
     */
    public LoginDestination getDestination() {
        return destination;
    }

    /**
     * Gets the user-facing name of the GUI that should open.
     *
     * @return the destination GUI name
     */
    public String getDestinationDisplayName() {
        return destinationDisplayName;
    }

    /**
     * Reports whether admin-only options should be visible.
     *
     * @return {@code true} when admin controls should be visible
     */
    public boolean isAdminOptionsVisible() {
        return adminOptionsVisible;
    }
}