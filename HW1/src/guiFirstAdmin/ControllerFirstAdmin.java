package guiFirstAdmin;

import java.sql.SQLException;
import database.Database;
import entityClasses.User;
import guiTools.UserNameRecognizer;
import guiUserLogin.ViewUserLogin;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/*******
 * <p> Title: ControllerFirstAdmin Class. </p>
 * 
 * <p> Description: ControllerFirstAdmin class provides the controller actions based on the user's
 *  use of the JavaFX GUI widgets defined by the View class.
 * 
 * This page contains a number of buttons that have not yet been implemented.  WHhen those buttons
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
 *  
 */

public class ControllerFirstAdmin {
	/*-********************************************************************************************

	The controller attributes for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/
	
	private static String adminUsername = "";
	private static String adminPassword1 = "";
	private static String adminPassword2 = "";
	private static String adminEmail = "";
	private static String adminName = "";
	private static String adminFirstName = "";
	private static String adminMiddleName = "";
	private static String adminLastName = "";
	private static String errMessage = "";
	protected static Database theDatabase = applicationMain.FoundationsMain.database;		

	/*-********************************************************************************************

	The User Interface Actions for this page
	
	*/
	
	/**
	 * Default constructor is not used.
	 */
	public ControllerFirstAdmin() {
	}

	/**********
	 * <p> Method: setAdminUsername() </p>
	 * 
	 * <p> Description: This method is called when the user adds text to the username field in the
	 * View.  A private local copy of what was last entered is kept here.</p>
	 * 
	 */
	protected static void setAdminUsername() {
		adminUsername = ViewFirstAdmin.text_AdminUsername.getText();
		errMessage = UserNameRecognizer.checkForValidUserName(adminUsername);	
	}
	
	
	/**********
	 * <p> Method: setAdminPassword1() </p>
	 * 
	 * <p> Description: This method is called when the user adds text to the password 1 field in
	 * the View.  A private local copy of what was last entered is kept here.</p>
	 * 
	 */
	protected static void setAdminPassword1() {
		adminPassword1 = ViewFirstAdmin.text_AdminPassword1.getText();
		ViewFirstAdmin.label_PasswordsDoNotMatch.setText(""); 
	}
	
	
	/**********
	 * <p> Method: setAdminPassword2() </p>
	 * 
	 * <p> Description: This method is called when the user adds text to the password 2 field in
	 * the View.  A private local copy of what was last entered is kept here.</p>
	 * 
	 */
	protected static void setAdminPassword2() {
		adminPassword2 = ViewFirstAdmin.text_AdminPassword2.getText();		
		ViewFirstAdmin.label_PasswordsDoNotMatch.setText("");
	}
	
	protected static void setAdminEmail() {
		adminEmail = ViewFirstAdmin.text_AdminEmail.getText();
	}
	
	protected static void setAdminName() {
		adminName = ViewFirstAdmin.text_AdminName.getText();
	}
	
	/**********
	 * <p> Method: doSetupAdmin() </p>
	 * 
	 * <p> Description: This method is called when the user presses the button to set up the Admin
	 * account.  It start by trying to establish a new user and placing that user into the
	 * database.  If that is successful, we proceed to the UserUpdate page.</p>
	 * 
	 */
	protected static void doSetupAdmin(Stage ps, int r) {
	    
	    // 1. VALIDATE USERNAME FIRST (Fail-Fast)
	    String validationError = UserNameRecognizer.checkForValidUserName(adminUsername);
	    if (validationError != null && !validationError.isEmpty()) {
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Invalid Username");
	        alert.setHeaderText("Username validation failed");
	        alert.setContentText(validationError);
	        try {
	            if (ViewFirstAdmin.theStage != null) alert.initOwner(ViewFirstAdmin.theStage);
	        } catch (Exception ex) { }
	        alert.showAndWait();
	        
	        // Update the UI label as well for consistency
	        ViewFirstAdmin.label_userNameIsInvalid.setText(validationError);
	        return; // Stop execution here
	    }

	    // 2. PARSE THE ADMIN NAME
	    if (adminName != null && !adminName.trim().isEmpty()) {
	        String[] nameParts = adminName.trim().split("\\s+");
	        
	        if (nameParts.length >= 3) {
	            adminFirstName = nameParts[0];
	            adminMiddleName = nameParts[1];
	            adminLastName = nameParts[2];
	        }
	        else if (nameParts.length == 2) {
	            adminFirstName = nameParts[0];
	            adminMiddleName = "";
	            adminLastName = nameParts[1];
	        }
	        else {
	            adminFirstName = adminName;
	            adminMiddleName = "";
	            adminLastName = "";
	        }
	    }

	    // 3. VALIDATE PASSWORDS AND REGISTER
	    if (adminPassword1.equals(adminPassword2) && !adminPassword1.isEmpty()) {
	        try {
	            User user = new User(adminUsername, adminPassword1, adminEmail, 
	                                 adminFirstName, adminMiddleName, adminLastName, 
	                                 "", true, false, false);
	            
	            theDatabase.register(user);
	            ViewUserLogin.displayUserLogin(ps);
	            
	        } catch (SQLException e) {
	            Alert dbAlert = new Alert(AlertType.ERROR);
	            dbAlert.setTitle("Database Error");
	            dbAlert.setHeaderText("Registration Failed");
	            dbAlert.setContentText("Could not register admin: " + e.getMessage());
	            dbAlert.showAndWait();
	        }
	    } else {
	        // Passwords don't match or are empty
	        ViewFirstAdmin.text_AdminPassword1.setText("");
	        ViewFirstAdmin.text_AdminPassword2.setText("");
	        ViewFirstAdmin.label_PasswordsDoNotMatch.setText(
	                "The passwords must match and cannot be empty!");
	    }
	}
	
	
	/**********
	 * <p> Method: performQuit() </p>
	 * 
	 * <p> Description: This method terminates the execution of the program.  It leaves the
	 * database in a state where the normal login page will be displayed when the application is
	 * restarted.</p>
	 * 
	 */
	protected static void performQuit() {
		System.out.println("Perform Quit");
		System.exit(0);
	}	
}

