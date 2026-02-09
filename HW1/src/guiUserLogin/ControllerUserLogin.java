package guiUserLogin;

import database.Database;
import entityClasses.User;
import javafx.stage.Stage;
import guiOneTimePassword.ControllerOneTimePassword;
import guiOneTimePassword.ViewOneTimePassword;

/*******
 * <p> Title: ControllerUserLogin Class. </p>
 * 
 * <p> Description: The Java/FX-based User Login Page.  This class provides the controller
 * actions basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This controller determines if the log in is valid.  If so set up the link to the database, 
 * determines how many roles this user is authorized to play, and the calls one the of the array of
 * role home pages if there is only one role.  If there are more than one role, it setup up and
 * calls the multiple roles dispatch page for the user to determine which role the user wants to
 * play.
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

public class ControllerUserLogin {
	
	/*-********************************************************************************************

	The User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/

	/**
	 * Default constructor is not used.
	 */
	public ControllerUserLogin() {
	}

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	private static Stage theStage;	
	
	private static String username = ViewUserLogin.text_Username.getText();
	
	/**********
	 * <p> Method: public doLogin() </p>
	 * 
	 * <p> Description: This method is called when the user has clicked on the Login button. This
	 * method checks the username and password to see if they are valid.  If so, it then logs that
	 * user in my determining which role to use.
	 * 
	 * The method reaches batch to the view page and to fetch the information needed rather than
	 * passing that information as parameters.
	 * 
	 */	
	/**********
	 * <p> Method: public doLogin() </p>
	 * * <p> Description: This method is called when the user has clicked on the Login button. 
	 * It verifies the credentials against the database and checks if a password reset 
	 * is required.
	 * */	
	protected static void doLogin(Stage ts) {
		theStage = ts;
		
		// 1. Fetch values from the View
		String username = ViewUserLogin.text_Username.getText().trim();
		String password = ViewUserLogin.text_Password.getText();
    	boolean loginResult = false;

    	// 2. Quick check for empty fields to prevent unnecessary database calls
    	if (username.isEmpty() || password.isEmpty()) {
    		ViewUserLogin.alertUsernamePasswordError.setHeaderText("Missing Information");
    		ViewUserLogin.alertUsernamePasswordError.setContentText("Please enter both a username and a password.");
    		ViewUserLogin.alertUsernamePasswordError.showAndWait();
    		return;
    	}
    	
		// 3. Verify the username exists in the database
     	if (theDatabase.getUserAccountDetails(username) == false) {
     		// Use a generic message for security (don't reveal if the user exists)
    		ViewUserLogin.alertUsernamePasswordError.setHeaderText("Login Failed");
    		ViewUserLogin.alertUsernamePasswordError.setContentText("Incorrect username/password. Try again!");
    		ViewUserLogin.alertUsernamePasswordError.showAndWait();
    		return;
    	}
		
		// 4. Verify the password matches
    	String actualPassword = theDatabase.getCurrentPassword();
    	if (password.equals(actualPassword) == false) {
    		ViewUserLogin.alertUsernamePasswordError.setHeaderText("Login Failed");
    		ViewUserLogin.alertUsernamePasswordError.setContentText("Incorrect username/password. Try again!");
    		ViewUserLogin.alertUsernamePasswordError.showAndWait();
    		return;
    	}
    	
    	// 5. Check if this is a temporary password (isOTP)
        if (theDatabase.isPasswordTemporary(username)) {
            // Reconstruct the user object from the database details
            User user = new User(username, password, theDatabase.getCurrentEmailAddress(), 
                                 theDatabase.getCurrentFirstName(), theDatabase.getCurrentMiddleName(), 
                                 theDatabase.getCurrentLastName(), theDatabase.getCurrentPreferredFirstName(), 
                                 theDatabase.getCurrentAdminRole(), theDatabase.getCurrentNewRole1(), 
                                 theDatabase.getCurrentNewRole2());

            // Inform the user they MUST reset
            ViewUserLogin.alertUsernamePasswordError.setTitle("Action Required");
            ViewUserLogin.alertUsernamePasswordError.setHeaderText("One-Time Password Detected");
            ViewUserLogin.alertUsernamePasswordError.setContentText("You must update your password before you can access your home page.");
            ViewUserLogin.alertUsernamePasswordError.showAndWait();

            // Redirect to the update page and exit the login flow
            guiUserUpdate.ViewUserUpdate.displayUserUpdate(theStage, user);
            return; 
        }
		
		// 6. Finalize the User object for a successful login
    	User user = new User(username, password, theDatabase.getCurrentEmailAddress(), 
    			theDatabase.getCurrentFirstName(), theDatabase.getCurrentMiddleName(), 
    			theDatabase.getCurrentLastName(), theDatabase.getCurrentPreferredFirstName(), 
    			theDatabase.getCurrentAdminRole(), theDatabase.getCurrentNewRole1(), 
    			theDatabase.getCurrentNewRole2());
    	
    	// 7. Determine Home Page Redirection
		int numberOfRoles = theDatabase.getNumberOfRoles(user);		

		if (numberOfRoles == 1) {
			// Single Role Path
			if (user.getAdminRole()) {
				loginResult = theDatabase.loginAdmin(user);
				if (loginResult) {
					guiAdminHome.ViewAdminHome.displayAdminHome(theStage, user);
				}
				if(actualPassword.compareTo(ViewOneTimePassword.TempText_Password1.getText()) == 0) {
					theDatabase.updatePassword(username, "");
					user.setPassword("");
				}
			} 
			else if (user.getNewRole1()) {
				loginResult = theDatabase.loginRole1(user);
				if (loginResult) { 
					guiRole1.ViewRole1Home.displayRole1Home(theStage, user);
				}
				if(actualPassword.compareTo(ViewOneTimePassword.TempText_Password1.getText()) == 0) {
					theDatabase.updatePassword(username, ""); 
					user.setPassword("");
				}
			} 
			else if (user.getNewRole2()) {
				loginResult = theDatabase.loginRole2(user);
				if (loginResult) {
					guiRole2.ViewRole2Home.displayRole2Home(theStage, user);
				}
				if(actualPassword.compareTo(ViewOneTimePassword.TempText_Password1.getText()) == 0) {
					theDatabase.updatePassword(username, "");
					user.setPassword("");
				}
			} 
			else {
				System.out.println("***** Error: Authenticated user has no valid roles.");
			}
		} 
		else if (numberOfRoles > 1) {
			// Multiple Roles Path - Let user choose
			guiMultipleRoleDispatch.ViewMultipleRoleDispatch.displayMultipleRoleDispatch(theStage, user);
		}
	}	
		
	/**********
	 * <p> Method: setup() </p>
	 * 
	 * <p> Description: This method is called to reset the page and then populate it with new
	 * content for the new user.</p>
	 * 
	 */
	protected static void doSetupAccount(Stage theStage, String invitationCode) {
		guiNewAccount.ViewNewAccount.displayNewAccount(theStage, invitationCode);
	}

	
	/**********
	 * <p> Method: public performQuit() </p>
	 * 
	 * <p> Description: This method is called when the user has clicked on the Quit button.  Doing
	 * this terminates the execution of the application.  All important data must be stored in the
	 * database, so there is no cleanup required.  (This is important so we can minimize the impact
	 * of crashed.)
	 * 
	 */	
	protected static void performQuit() {
		System.out.println("Perform Quit");
		System.exit(0);
	}	

}
