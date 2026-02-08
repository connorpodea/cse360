package guiUserUpdate;

import entityClasses.User;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

// Import the FSM validator (adjust package if you put the class elsewhere)
import guiTools.UserNameRecognizer;
import database.Database;
import javafx.stage.Stage;

public class ControllerUserUpdate {
	/*-********************************************************************************************

	The Controller for ViewUserUpdate 
	
	**********************************************************************************************/

	/**********
	 * <p> Title: ControllerUserUpdate Class</p>
	 * 
	 * <p> Description: This static class supports the actions initiated by the ViewUserUpdate
	 * class. In this case, there is just one method, no constructors, and no attributes.</p>
	 *
	 */
	
	// attributes
	private static Database db = applicationMain.FoundationsMain.database;

	/*-********************************************************************************************

	The User Interface Actions for this page
	
	**********************************************************************************************/

	
	/**********
	 * <p> Method: public goToUserHomePage(Stage theStage, User theUser) </p>
	 * 
	 * <p> Description: This method is called when the user has clicked on the button to
	 * proceed to the user's home page.
	 * 
	 * @param theStage specifies the JavaFX Stage for next next GUI page and it's methods
	 * 
	 * @param theUser specifies the user so we go to the right page and so the right information
	 */
	protected static void goToUserHomePage(Stage theStage, User theUser) {
		
		
		if (db.isPasswordTemporary(theUser.getUserName())) {
            // This is where you'd call your DB to save the NEW password and set isOTP to 0
            // theDatabase.updatePasswordAndClearOTP(theUser.getUserName(), newPassword);
            
            // 2. Force them to Login page
            guiUserLogin.ViewUserLogin.displayUserLogin(theStage);
            return;
        }
		
		// Get the roles the user selected during login
		
		int theRole = applicationMain.FoundationsMain.activeHomePage;

		// Use that role to proceed to that role's home page
		switch (theRole) {
		case 1:
			guiAdminHome.ViewAdminHome.displayAdminHome(theStage, theUser);
			break;
		case 2:
			guiRole1.ViewRole1Home.displayRole1Home(theStage, theUser);
			break;
		case 3:
			guiRole2.ViewRole2Home.displayRole2Home(theStage, theUser);
			break;
		default: 
			System.out.println("*** ERROR *** UserUpdate goToUserHome has an invalid role: " + 
					theRole);
			System.exit(0);
		}
 	}
	
	/**
	 * Helper: validateUsername
	 * 
	 * A reusable helper method that controllers can call when the user attempts to change
	 * their username on the User Update page (or any other page).  It uses the shared
	 * UserNameRecognizer FSM to validate the username syntax and displays an Alert with
	 * the helpful message returned by the recognizer when invalid.
	 * 
	 * @param newUserName the candidate username to validate
	 * @param ownerStage  the JavaFX Stage to attach the modal Alert to (may be null)
	 * @return true if the username is syntactically valid, false otherwise (and an Alert is shown)
	 */
	protected static boolean validateUsername(String newUserName, Stage ownerStage) {
		if (newUserName == null) newUserName = "";
		String validationError = UserNameRecognizer.checkForValidUserName(newUserName);
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
	
	
