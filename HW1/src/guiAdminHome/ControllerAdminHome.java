package guiAdminHome;

import java.util.List;

import database.Database;

//JavaFX Alert import for validator helper
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

//Import the FSM validator
import guiTools.UserNameRecognizer;

import entityClasses.User;

/*******
 * <p> Title: GUIAdminHomePage Class. </p>
 * 
 * <p> Description: The Java/FX-based Admin Home Page.  This class provides the controller actions
 * basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This page contains a number of buttons that have not yet been implemented.  WHen those buttons
 * are pressed, an alert pops up to tell the user that the function associated with the button has
 * not been implemented. Also, be aware that What has been implemented may not work the way the
 * final product requires and there maybe defects in this code.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-17 Initial version
 * @version 1.01		2025-09-16 Update Javadoc documentation *  
 */

public class ControllerAdminHome {
	
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/
	
	/**
	 * Default constructor is not used.
	 */
	public ControllerAdminHome() {
	}
	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**********
	 * <p> 
	 * 
	 * Title: performInvitation () Method. </p>
	 * 
	 * <p> Description: Protected method to send an email inviting a potential user to establish
	 * an account and a specific role. </p>
	 */
	protected static void performInvitation () {
		// Verify that the email address is valid - If not alert the user and return
		String emailAddress = ViewAdminHome.text_InvitationEmailAddress.getText();
		if (invalidEmailAddress(emailAddress)) {
			return;
		}
		
		// Check to ensure that we are not sending a second message with a new invitation code to
		// the same email address.  
		if (theDatabase.emailaddressHasBeenUsed(emailAddress)) {
			ViewAdminHome.alertEmailError.setContentText(
					"An invitation has already been sent to this email address.");
			ViewAdminHome.alertEmailError.showAndWait();
			return;
		}
		
		// Inform the user that the invitation has been sent and display the invitation code
		String theSelectedRole = (String) ViewAdminHome.combobox_SelectRole.getValue();
		String invitationCode = theDatabase.generateInvitationCode(emailAddress,
				theSelectedRole);
		String msg = "Code: " + invitationCode + " for role " + theSelectedRole + 
				" was sent to: " + emailAddress;
		System.out.println(msg);
		ViewAdminHome.alertEmailSent.setContentText(msg);
		ViewAdminHome.alertEmailSent.showAndWait();
		
		// Update the Admin Home pages status
		ViewAdminHome.text_InvitationEmailAddress.setText("");
		ViewAdminHome.label_NumberOfInvitations.setText("Number of outstanding invitations: " + 
				theDatabase.getNumberOfInvitations());
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: manageInvitations () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void manageInvitations () {
		System.out.println("\n*** WARNING ***: Manage Invitations Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.setTitle("*** WARNING ***");
		ViewAdminHome.alertNotImplemented.setHeaderText("Manage Invitations Issue");
		ViewAdminHome.alertNotImplemented.setContentText("Manage Invitations Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.showAndWait();
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: setOnetimePassword () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void setOnetimePassword () {
		guiOneTimePassword.ViewOneTimePassword.displayOneTimePassword(ViewAdminHome.theStage, ViewAdminHome.theUser);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: deleteUser () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void deleteUser() {
		// Get the list of users
	    java.util.List<String> users = theDatabase.getUserList();
	    if (users == null || users.size() <= 1) { // only <Select a User> or empty
	        ViewAdminHome.alertNotImplemented.setTitle("*** WARNING ***");
	        ViewAdminHome.alertNotImplemented.setHeaderText("Delete User");
	        ViewAdminHome.alertNotImplemented.setContentText("No users available to delete.");
	        ViewAdminHome.alertNotImplemented.showAndWait();
	        return;
	    }

	    // Present a ChoiceDialog to pick which user to delete
	    javafx.scene.control.ChoiceDialog<String> dialog =
	            new javafx.scene.control.ChoiceDialog<>(users.get(0), users);
	    dialog.setTitle("Delete a User");
	    dialog.setHeaderText("Select a user to delete");
	    dialog.setContentText("User:");

	    java.util.Optional<String> result = dialog.showAndWait();
	    if (!result.isPresent()) return;
	    String selectedUser = result.get();

	    if (selectedUser == null || selectedUser.compareTo("<Select a User>") == 0) {
	        // invalid selection
	        javafx.scene.control.Alert a = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
	        a.setTitle("Delete User");
	        a.setHeaderText("No user selected");
	        a.setContentText("Please select a valid user.");
	        a.showAndWait();
	        return;
	    }

	    // Confirm deletion with a Yes/No
	    javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
	    confirm.setTitle("Confirm Delete");
	    confirm.setHeaderText("Are you sure?");
	    confirm.setContentText("Are you sure you want to permanently delete user '" + selectedUser + "'?");
	    java.util.Optional<javafx.scene.control.ButtonType> confirmResult = confirm.showAndWait();
	    if (!confirmResult.isPresent() || confirmResult.get() != javafx.scene.control.ButtonType.OK) {
	        return; // user cancelled
	    }

	    // Attempt the delete via the database (server-side safety checks are in Database.deleteUser)
	    boolean ok = theDatabase.deleteUser(selectedUser);
	    if (ok) {
	        javafx.scene.control.Alert success = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
	        success.setTitle("Delete User");
	        success.setHeaderText("User Deleted");
	        success.setContentText("User '" + selectedUser + "' was deleted.");
	        success.showAndWait();
	    } else {
	        // Provide helpful reason where possible (since deleteUser returns only boolean, give generic message)
	        // If you prefer, enhance deleteUser to return error codes/messages for more specific UI text.
	        javafx.scene.control.Alert failure = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
	        failure.setTitle("Delete User");
	        failure.setHeaderText("Unable to delete user");
	        failure.setContentText("The deletion was blocked. Make sure you are not deleting your own account and at least one admin will remain.");
	        failure.showAndWait();
	    }
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: listUsers () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void listUsers() {
	    List<User> users = theDatabase.getAllUsersForDisplay();
	    System.out.println("Users loaded = " + users.size());
	    ViewAdminHome.displayUserList(users);
	}




	
	/**********
	 * <p> 
	 * 
	 * Title: addRemoveRoles () Method. </p>
	 * 
	 * <p> Description: Protected method that allows an admin to add and remove roles for any of
	 * the users currently in the system.  This is done by invoking the AddRemoveRoles Page. There
	 * is no need to specify the home page for the return as this can only be initiated by and
	 * Admin.</p>
	 */
	protected static void addRemoveRoles() {
		guiAddRemoveRoles.ViewAddRemoveRoles.displayAddRemoveRoles(ViewAdminHome.theStage, 
				ViewAdminHome.theUser);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: invalidEmailAddress () Method. </p>
	 * 
	 * <p> Description: Protected method that is intended to check an email address before it is
	 * used to reduce errors.  The code currently only checks to see that the email address is not
	 * empty.  In the future, a syntactic check must be performed and maybe there is a way to check
	 * if a properly email address is active.</p>
	 * 
	 * @param emailAddress	This String holds what is expected to be an email address
	 */
	protected static boolean invalidEmailAddress(String emailAddress) {
		if (emailAddress.length() == 0) {
			ViewAdminHome.alertEmailError.setContentText(
					"Correct the email address and try again.");
			ViewAdminHome.alertEmailError.showAndWait();
			return true;
		}
		return false;
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performLogout () Method. </p>
	 * 
	 * <p> Description: Protected method that logs this user out of the system and returns to the
	 * login page for future use.</p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewAdminHome.theStage);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performQuit () Method. </p>
	 * 
	 * <p> Description: Protected method that gracefully terminates the execution of the program.
	 * </p>
	 */
	protected static void performQuit() {
		System.exit(0);
	}
	
	/*********************************************************************************
	 * Helper: validateUsername
	 *
	 * A reusable helper method that controllers can call when an admin action requires
	 * validating a username (e.g., before deleting or modifying a user). It uses the shared
	 * UserNameRecognizer FSM to validate the username syntax and displays an Alert with
	 * the helpful message returned by the recognizer when invalid.
	 *
	 * @param candidate the candidate username to validate
	 * @param ownerStage the JavaFX Stage to attach the modal Alert to (may be null)
	 * @return true if valid, false if invalid (and an Alert is shown)
	 *********************************************************************************/
	protected static boolean validateUsername(String candidate, Stage ownerStage) {
		if (candidate == null) candidate = "";
		String validationError = UserNameRecognizer.checkForValidUserName(candidate);
		if (validationError != null && !validationError.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Username");
			alert.setHeaderText("Username validation failed");
			alert.setContentText(validationError);
			try {
				if (ownerStage != null) alert.initOwner(ownerStage);
			} catch (Exception ex) {
				// ignore if unable to set owner
			}
			alert.showAndWait();
			return false;
		}
		return true;
	}
}

