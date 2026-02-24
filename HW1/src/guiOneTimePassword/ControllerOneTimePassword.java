package guiOneTimePassword;

import database.Database;

/**
 * Controller class for the One Time Password functionality.
 */
public class ControllerOneTimePassword {
	
	/**
	 * Default constructor for ControllerOneTimePassword.
	 */
	public ControllerOneTimePassword() {
		// Default constructor
	}
	
	// having access to the data base so we can see the users.
	private static Database theDatabase = applicationMain.FoundationsMain.database; 
	
	/** The first password entered by the user. */
	protected static String password1 = "";
	
	/** The second password entered by the user for confirmation. */
	protected static String password2 = "";
	
	/** The current password of the user being modified. */
	protected static String currentUsersPassword = ViewOneTimePassword.TempText_Password1.getText();;

	/**
	 * Selects a user and retrieves their account details from the database.
	 */
	protected static void doSelectUser() {
		ViewOneTimePassword.theSelectedUser = 
				(String) ViewOneTimePassword.combobox_SelectUser.getValue();
		theDatabase.getUserAccountDetails(ViewOneTimePassword.theSelectedUser);
		
		currentUsersPassword = theDatabase.getCurrentPassword();
		
		setupSelectedUser();
		
	}
	
	/**
	 * Updates the visibility of UI elements in the root pane.
	 */
	protected static void repaintWindow() {
		ViewOneTimePassword.theRootPane.getChildren().clear();
		
		if (ViewOneTimePassword.theSelectedUser.compareTo("<Select a User>") == 0) {
			// Only show the request to select a user to be updated and the ComboBox
			ViewOneTimePassword.theRootPane.getChildren().addAll(
		            ViewOneTimePassword.labelPageTitle,
		            ViewOneTimePassword.label_SelectUser,
		            ViewOneTimePassword.combobox_SelectUser,
		            ViewOneTimePassword.button_Return,
		            ViewOneTimePassword.button_Logout,
		            ViewOneTimePassword.button_Quit);
		}
		else {
			ViewOneTimePassword.theRootPane.getChildren().addAll(
		            ViewOneTimePassword.labelPageTitle,
		            ViewOneTimePassword.label_SelectUser,
		            ViewOneTimePassword.combobox_SelectUser,
		            ViewOneTimePassword.button_Return,
		            ViewOneTimePassword.button_Logout,
		            ViewOneTimePassword.button_Quit,
		            ViewOneTimePassword.TempText_Password1,
		            ViewOneTimePassword.TempText_Password2,
		            ViewOneTimePassword.button_confirmPassword);
		}
		
	}
	
	/**
	 * Sets up the roles and permissions view for the selected user.
	 */
	private static void setupSelectedUser() {
		System.out.println("*** Entering setupSelectedUser");
		
		// Create the list of roles that could be added for the currently selected user (e.g., Do
		// not show a role to add that the user already has!)
		ViewOneTimePassword.addList.clear();
		ViewOneTimePassword.addList.add("<Select a role>");
		if (!theDatabase.getCurrentAdminRole())
			ViewOneTimePassword.addList.add("Admin");
		if (!theDatabase.getCurrentNewRole1())
			ViewOneTimePassword.addList.add("Role1");
		if (!theDatabase.getCurrentNewRole2())
			ViewOneTimePassword.addList.add("Role2");

		// Create the list of roles that could be removed for the currently selected user (e.g., Do
		// not show a role to remove that the user does not have!)
		ViewOneTimePassword.removeList.clear();
		ViewOneTimePassword.removeList.add("<Select a role>");
		if (theDatabase.getCurrentAdminRole())
			ViewOneTimePassword.removeList.add("Admin");
		if (theDatabase.getCurrentNewRole1())
			ViewOneTimePassword.removeList.add("Role1");
		if (theDatabase.getCurrentNewRole2())
			ViewOneTimePassword.removeList.add("Role2");
		
		// Create the list or roles that the user currently has with proper use of a comma between
		// items
		boolean notTheFirst = false;
		String theCurrentRoles = "";
		
		// Admin role - It can only be at the head of a list
		if (theDatabase.getCurrentAdminRole()) {
			theCurrentRoles += "Admin";
			notTheFirst = true;
		}
		
		// Roles 1 - It could be at the head of the list or later in the list
		if (theDatabase.getCurrentNewRole1()) {
			if (notTheFirst)
				theCurrentRoles += ", Role1"; 
			else {
				theCurrentRoles += "Role1";
				notTheFirst = true;
			}
		}

		// Roles 2 - It could be at the head of the list or later in the list
		if (theDatabase.getCurrentNewRole2()) {
			if (notTheFirst)
				theCurrentRoles += ", Role2"; 
			else {
				theCurrentRoles += "Role2";
				notTheFirst = true;
			}
		}
		
		repaintWindow();
	}
	
	/**
	 * Updates the first password variable from the UI.
	 */
	protected static void setAdminPassword1() {
		password1 = ViewOneTimePassword.TempText_Password1.getText();
	}
	
	/**
	 * Updates the second password variable from the UI.
	 */
	protected static void setAdminPassword2() {
		password2 = ViewOneTimePassword.TempText_Password2.getText();		
	}
	
	/**
	 * Validates input and updates the password in the database.
	 */
	protected static void doOneTimePassword() {
		if(ViewOneTimePassword.TempText_Password1.getText() == "") {
			ViewOneTimePassword.TempText_Password1.setText("");
			ViewOneTimePassword.TempText_Password2.setText("");
			ViewOneTimePassword.displayError2.showAndWait();
		}
		
		else if(ViewOneTimePassword.TempText_Password1.getText().compareTo(ViewOneTimePassword.TempText_Password2.getText()) == 0 ) {
			theDatabase.updatePassword(ViewOneTimePassword.theSelectedUser, ViewOneTimePassword.TempText_Password1.getText());
			performConfirmation();
			 
			
		}
		else {
			ViewOneTimePassword.TempText_Password1.setText("");
			ViewOneTimePassword.TempText_Password2.setText("");
			ViewOneTimePassword.displayError.showAndWait();
		}
	  
	}
	 
	/**
	 * Returns the user to the Admin Home screen.
	 */
	protected static void performReturn() {
		guiAdminHome.ViewAdminHome.displayAdminHome(ViewOneTimePassword.theStage,
				ViewOneTimePassword.theUser);
	}
	
	/**
	 * Logs out the user and returns to the login screen.
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewOneTimePassword.theStage);
	}
	
	/**
	 * Closes the application.
	 */
	protected static void performQuit() {
		System.exit(0);
	}
	
	/**
	 * Shows a confirmation dialog for successful password updates.
	 */
	protected static void performConfirmation() {
		ViewOneTimePassword.displayConfirmation.showAndWait();
		
	}
	
}