package guiNewAccount;

import java.sql.SQLException;
import guiTools.UserNameRecognizer;
import database.Database;
import entityClasses.User;
import guiUserUpdate.ViewUserUpdate;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


/*******
 * <p> Title: ControllerNewAccount Class. </p>
 * * <p> Description: The Java/FX-based New Account Page.  This class provides the controller actions
 * to allow the user to establish a new account after responding to an invitation and the use of a
 * one time code.
 * * The controller deals with the user pressing the "User Step" button widget being click.  If also
 * supports the user click on the "Quit" button widget.
 * * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * * @author Lynn Robert Carter
 * * @version 1.00		2025-08-17 Initial version
 * */

public class ControllerNewAccount {
	
	/*-********************************************************************************************

	The User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/
	
	private static String username = "";
	private static String password1 = "";
	private static String password2 = "";
	private static String email = "";
	private static String name = "";
	private static String firstName = "";
	private static String middleName = "";
	private static String lastName = "";
	private static String errMessage = "";
	protected static Database theDatabase = applicationMain.FoundationsMain.database;		


	/**
	 * Default constructor is not used.
	 */
	public ControllerNewAccount() {
	}
	
	protected static void setUsername() {
		username = ViewNewAccount.text_Username.getText(); // Fixed case sensitivity
		errMessage = UserNameRecognizer.checkForValidUserName(username);	
	}
	
	
	/**********
	 * <p> Method: setPassword1() </p>
	 * * <p> Description: This method is called when the user adds text to the password 1 field in
	 * the View.  A private local copy of what was last entered is kept here.</p>
	 * */
	protected static void setPassword1() {
		password1 = ViewNewAccount.text_Password1.getText(); // Fixed name and completion
	}
	
	
	/**********
	 * <p> Method: setPassword2() </p>
	 * * <p> Description: This method is called when the user adds text to the password 2 field in
	 * the View.  A private local copy of what was last entered is kept here.</p>
	 * */
	protected static void setPassword2() {
		password2 = ViewNewAccount.text_Password2.getText(); // Fixed name and completion
	}
	
	protected static void setEmail() {
		email = ViewNewAccount.text_Email.getText(); // Fixed case sensitivity
	}
	
	protected static void setName() {
		name = ViewNewAccount.text_Name.getText(); // Fixed case sensitivity
	}
	
	
	
	/**********
	 * <p> Method: public doCreateUser() </p>
	 * * <p> Description: This method is called when the user has clicked on the User Setup
	 * button.  This method checks the input fields to see that they are valid.  If so, it then
	 * creates the account by adding information to the database.
	 * * The method reaches batch to the view page and to fetch the information needed rather than
	 * passing that information as parameters.
	 * */	
	protected static void doCreateUser() {
	    // 1. Fetch values from the static variables (populated by setUsername, setPassword, etc.)
	    // These are already updated via your "set" methods in the Controller.
	    
	    // 2. USERNAME VALIDATION (The "Gatekeeper")
	    // We check the syntax using the FSM recognizer
	    String validationError = UserNameRecognizer.checkForValidUserName(username);
	    if (validationError != null && !validationError.isEmpty()) {
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Invalid Username");
	        alert.setHeaderText("Username validation failed");
	        alert.setContentText(validationError);
	        // Try to center the alert on the current window
	        if (ViewNewAccount.theStage != null) alert.initOwner(ViewNewAccount.theStage);
	        alert.showAndWait();
	        return; // STOP: Don't process anything else if the name is bad
	    }

	    // 3. PARSE THE FULL NAME
	    // Handle First, Middle, and Last names based on whitespace
	    if (name != null && !name.trim().isEmpty()) {
	        String[] nameParts = name.trim().split("\\s+");
	        if (nameParts.length >= 3) {
	            firstName = nameParts[0];
	            middleName = nameParts[1];
	            lastName = nameParts[2];
	        } else if (nameParts.length == 2) {
	            firstName = nameParts[0];
	            middleName = "";
	            lastName = nameParts[1];
	        } else {
	            firstName = name;
	            middleName = "";
	            lastName = "";
	        }
	    }

	    // 4. PASSWORD VALIDATION & DATABASE ENTRY
	    if (password1.equals(password2) && !password1.isEmpty()) {
	        int roleCode = 0;
	        User user = null;

	        // Determine Role based on the invitation details
	        if (ViewNewAccount.theRole.equals("Admin")) {
	            roleCode = 1;
	            user = new User(username, password1, email, firstName, middleName, lastName, "", true, false, false);
	        } else if (ViewNewAccount.theRole.equals("Role1")) {
	            roleCode = 2;
	            user = new User(username, password1, email, firstName, middleName, lastName, "", false, true, false);
	        } else if (ViewNewAccount.theRole.equals("Role2")) {
	            roleCode = 3;
	            user = new User(username, password1, email, firstName, middleName, lastName, "", false, false, true);
	        }

	        applicationMain.FoundationsMain.activeHomePage = roleCode;

	        try {
	            // Register user in the database
	            theDatabase.register(user);
	            
	            // Cleanup: remove the invitation code used so it can't be reused
	            theDatabase.removeInvitationAfterUse(ViewNewAccount.theInvitationCode);
	            
	            // Sync database state and navigate to User Update page
	            theDatabase.getUserAccountDetails(username);
	            ViewUserUpdate.displayUserUpdate(ViewNewAccount.theStage, user);

	        } catch (SQLException e) {
	            Alert dbAlert = new Alert(AlertType.ERROR);
	            dbAlert.setContentText("Database error: " + e.getMessage());
	            dbAlert.showAndWait();
	            e.printStackTrace();
	        }
	    } else {
	        // Passwords don't match or were empty
	        ViewNewAccount.text_Password1.setText("");
	        ViewNewAccount.text_Password2.setText("");
	        ViewNewAccount.alertUsernamePasswordError.showAndWait();
	    }
	}
	/**********
	 * <p> Method: public performQuit() </p>
	 * * <p> Description: This method is called when the user has clicked on the Quit button.  Doing
	 * this terminates the execution of the application.  All important data must be stored in the
	 * database, so there is no cleanup required.  (This is important so we can minimize the impact
	 * of crashed.)
	 * */	
	protected static void performQuit() {
		System.exit(0);
	}	
}