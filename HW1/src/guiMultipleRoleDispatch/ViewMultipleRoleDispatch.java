package guiMultipleRoleDispatch;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;

/**
 * <p> Title: ViewMultipleRoleDispatch Class. </p>
 * * <p> Description: The Java/FX-based page for users with multiple roles to select 
 * which role they wish to play during the current session. </p>
 * * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * * @author Lynn Robert Carter
 * @version 1.00 2025-08-20 Initial version
 */
public class ViewMultipleRoleDispatch {

	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	private static Label label_PageTitle = new Label("Multiple Role Dispatch Page");
	private static Label label_UserDetails = new Label();
	private static Line line_Separator1 = new Line(20, 95, width-20, 95);
	private static Label label_WhichRole = new Label("Which role do you wish to play:");
	
	/** Dropdown menu allowing the user to select one of their assigned roles. */
	protected static ComboBox <String> combobox_SelectRole = new ComboBox <String>();
	
	private static Button button_PerformRole = new Button("Perform Role");		
	private static Line line_Separator4 = new Line(20, 525, width-20,525);
	private static Button button_Logout = new Button("Logout");
	private static Button button_Quit = new Button("Quit");

	private static ViewMultipleRoleDispatch theView;

	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/** The Stage established by JavaFX for this view. */
	protected static Stage theStage;			
	
	private static Pane theRootPane;			
	
	/** The User currently logged into the application. */
	protected static User theUser;				

	private static Scene theMultipleRoleDispatchScene = null;	

	/**
	 * <p> Method: displayMultipleRoleDispatch </p>
	 * * <p> Description: This method is the entry point for this view. It populates 
	 * the role selection list based on the user's account and sets the scene. </p>
	 * @param ps The JavaFX Stage to be used for this GUI.
	 * @param user The User for whom the roles are being dispatched.
	 */
	public static void displayMultipleRoleDispatch(Stage ps, User user) {
		
		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;
		
		if (theView == null) theView = new ViewMultipleRoleDispatch();

		List<String> list = new ArrayList<String>();	
		theDatabase.getUserAccountDetails(theUser.getUserName());
		
		label_UserDetails.setText("User: " + theUser.getUserName() + "  Select which role");
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.CENTER, 0, 50);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);			

		System.out.println("*** Getting multiple role details for user: " + theUser.getUserName());
		list = new ArrayList<String>();
		list.add("<Select a role>");
		if (theDatabase.getCurrentAdminRole()) list.add("Admin");
		if (theDatabase.getCurrentNewRole1()) list.add("Staff");
		if (theDatabase.getCurrentNewRole2()) list.add("Student");
		combobox_SelectRole.setItems(FXCollections.observableArrayList(list));

		combobox_SelectRole.getSelectionModel().select(0);
		
		theStage.setTitle("CSE 360 Foundation Code: Multiple Role Dispatch");	
		theStage.setScene(theMultipleRoleDispatchScene);		
		theStage.show();										
	}

	/**
	 * Private constructor to initialize all GUI elements, set up layouts, 
	 * and assign event handlers.
	 */
	private ViewMultipleRoleDispatch() {

		theRootPane = new Pane();
		theMultipleRoleDispatchScene = new Scene(theRootPane, width, height);

		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);
		setupLabelUI(label_WhichRole, "Arial", 20, 200, Pos.BASELINE_LEFT, 20, 110);

		setupComboBoxUI(combobox_SelectRole, "Dialog", 16, 100, 305, 105);

		setupButtonUI(button_PerformRole, "Dialog", 16, 100, Pos.CENTER, 495, 105);
		button_PerformRole.setOnAction((_) -> 
		{guiMultipleRoleDispatch.ControllerMultipleRoleDispatch.performRole(); });

		setupButtonUI(button_Logout, "Dialog", 18, 250, Pos.CENTER, 20, 540);
		button_Logout.setOnAction((_) -> 
		{guiMultipleRoleDispatch.ControllerMultipleRoleDispatch.performLogout(); });

		setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 540);
		button_Quit.setOnAction((_) -> 
		{guiMultipleRoleDispatch.ControllerMultipleRoleDispatch.performQuit(); });
		
		theRootPane.getChildren().addAll(
				label_PageTitle,
				label_UserDetails,
				line_Separator1,
				label_WhichRole,
				combobox_SelectRole,
				button_PerformRole,
				line_Separator4, 
				button_Logout,
				button_Quit);
	}

	/**
	 * Helper method to initialize label properties.
	 * @param l Label object
	 * @param ff Font family
	 * @param f Font size
	 * @param w Minimum width
	 * @param p Alignment
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 */
	private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}

	/**
	 * Helper method to initialize button properties.
	 * @param b Button object
	 * @param ff Font family
	 * @param f Font size
	 * @param w Minimum width
	 * @param p Alignment
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 */
	private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}

	/**
	 * Helper method to initialize ComboBox properties.
	 * @param c ComboBox object
	 * @param ff Font family
	 * @param f Font size
	 * @param w Minimum width
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 */
	private void setupComboBoxUI(ComboBox <String> c, String ff, double f, double w, double x, 
			double y){
		c.setStyle("-fx-font: " + f + " " + ff + ";");
		c.setMinWidth(w);
		c.setLayoutX(x);
		c.setLayoutY(y);
	}
}