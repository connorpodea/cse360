package guiTools;

import javafx.scene.Scene;
import javafx.stage.Stage;
import entityClasses.User;


/*******
 * <p> Title: GUISingleRoleDispatch Class. </p>
 * 
 * <p> Description: The class dispatches the execution to the appropriate role's home
 * page when the user has only one role.  This is not actually a GUI page... it just dispatches
 * to an actual GUI page for the specified role.
 * 
 * WHen a user has more than one role, a different
 * class, GUIMultipleRoleHomePage, asks the user which of their roles do they want to use,
 * and then it dispatches the user to that role's home page.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-04-20 Initial version
 *  
 */
/**
 * Sends a user to the correct home page when that user has only one role.
 * This helper supports the assignment login flow by opening the correct screen after sign-in.
 */
public class GUISingleRoleDispatch {
	
	/**********************************************************************************************

	Attributes
	
	**********************************************************************************************/
	
	// These are the application values required by the user interface

	
	/** Stores a scene reference used by the single-role dispatch flow. */
	public Scene theViewStudentHomeScene;


	
	/**********************************************************************************************

	Constructors
	
	**********************************************************************************************/

	
	/**
	 * Creates the single-role dispatch helper.
	 */
	public GUISingleRoleDispatch() {
	}

	
	/**
	 * Opens the correct home page for a user with one role.
	 *
	 * @param ps the JavaFX stage used for navigation
	 * @param user the signed-in user
	 */
	public static void doSingleRoleDispatch(Stage ps, User user) {
		System.out.println("************** Just entered single role dispatch page");

		if (user.getAdminRole()) {
			guiAdminHome.ViewAdminHome.displayAdminHome(ps, user);
		} else if (user.getNewRole1()) {
			guiRole1.ViewRole1Home.displayRole1Home(ps, user);
		} else if (user.getNewRole2()) {
			guiRole2.ViewRole2Home.displayRole2Home(ps, user);
		} else {
			// Invalid role
			System.out.println("*** ERROR *** GUISingleRoleDispatch was asked to dispatch to " +
			"a role that is not supported!");
		}
	}
}
