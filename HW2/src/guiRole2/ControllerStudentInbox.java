package guiRole2;

/**
 * Controller for the student inbox page.
 */
public class ControllerStudentInbox {

	/**
	 * Default constructor is not used.
	 */
	public ControllerStudentInbox() {
	}

	/**
	 * Returns the user to the active home page.
	 */
	protected static void performBack() {
		MVCPostManagement.ControllerPostManagement.navigateToActiveHome(
				ViewStudentInbox.theStage, ViewStudentInbox.theUser);
	}
}
