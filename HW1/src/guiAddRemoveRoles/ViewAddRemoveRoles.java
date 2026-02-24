package guiAddRemoveRoles;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ObservableValue;
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
 * <p> Title: GUIAddRemoveRolesPage Class. </p>
 * <p> Description: The Java/FX-based page for changing the assigned roles to users. </p>
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * @author Lynn Robert Carter
 * @version 1.00 2025-08-20 Initial version
 */
public class ViewAddRemoveRoles {
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	/** Label for the page title. */
	protected static Label label_PageTitle = new Label();
	/** Label displaying details of the current user. */
	protected static Label label_UserDetails = new Label();
	/** Button to navigate to the account update page. */
	protected static Button button_UpdateThisUser = new Button("Account Update");
	/** Line separator for UI sections. */
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);
	/** Label prompting the admin to select a user. */
	protected static Label label_SelectUser = new Label("Select a user to be updated:");
	/** Dropdown menu for selecting a user. */
	protected static ComboBox <String> combobox_SelectUser = new ComboBox <String>();
	/** List of roles available to be added. */
	protected static List<String> addList = new ArrayList<String>();
	/** Button to confirm adding a selected role. */
	protected static Button button_AddRole = new Button("Add This Role");
	/** List of roles available to be removed. */
	protected static List<String> removeList = new ArrayList<String>();
	/** Button to confirm removing a selected role. */
	protected static Button button_RemoveRole = new Button("Remove This Role");
	/** Label indicating the user's current roles. */
	protected static Label label_CurrentRoles = new Label("This user's current roles:");
	/** Label prompting to select a role to add. */
	protected static Label label_SelectRoleToBeAdded = new Label("Select a role to be added:");
	/** Dropdown for choosing a role to add. */
	protected static ComboBox <String> combobox_SelectRoleToAdd = new ComboBox <String>();	
	/** Label prompting to select a role to remove. */
	protected static Label label_SelectRoleToBeRemoved = new Label("Select a role to be removed:");
	/** Dropdown for choosing a role to remove. */
	protected static ComboBox <String> combobox_SelectRoleToRemove = new ComboBox <String>();
	/** Line separator for the bottom UI area. */
	protected static Line line_Separator4 = new Line(20, 525, width-20,525);
	/** Button to return to the previous page. */
	protected static Button button_Return = new Button("Return");
	/** Button to log the user out. */
	protected static Button button_Logout = new Button("Logout");
	/** Button to quit the application. */
	protected static Button button_Quit = new Button("Quit");

	private static ViewAddRemoveRoles theView;	
	private static Database theDatabase = applicationMain.FoundationsMain.database;		

	/** The primary stage for this view. */
	protected static Stage theStage;			
	/** The root pane containing the UI elements. */
	protected static Pane theRootPane;			
	/** The user currently interacting with the view. */
	protected static User theUser;				
	/** The main scene for the add/remove roles page. */
	public static Scene theAddRemoveRolesScene = null;	
	/** The username of the currently selected user in the dropdown. */
	protected static String theSelectedUser = "";	
	/** String representing the role selected for addition. */
	protected static String theAddRole = "";		
	/** String representing the role selected for removal. */
	protected static String theRemoveRole = "";		

	/**
	 * <p> Method: displayAddRemoveRoles </p>
	 * <p> Description: Main entry point for displaying the Add/Remove roles view. </p>
	 * @param ps Specifies the JavaFX Stage
	 * @param user Specifies the User whose roles will be updated
	 */
	public static void displayAddRemoveRoles(Stage ps, User user) {
		theStage = ps;
		theUser = user;
		if (theView == null) theView = new ViewAddRemoveRoles();
		combobox_SelectUser.getSelectionModel().select(0);
		ControllerAddRemoveRoles.repaintTheWindow();
		ControllerAddRemoveRoles.doSelectUser();
	}

	/**
	 * <p> Method: ViewAddRemoveRoles constructor </p>
	 * <p> Description: Initializes all GUI elements. Subsequent uses fill in changeable
	 * fields using the displayAddRemoveRoles method. </p>
	 */
	public ViewAddRemoveRoles() {
		theRootPane = new Pane();
		theAddRemoveRolesScene = new Scene(theRootPane, width, height);
		
		label_PageTitle.setText("Add/Removed Roles Page");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		
		setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
		button_UpdateThisUser.setOnAction((_) -> 
			{guiUserUpdate.ViewUserUpdate.displayUserUpdate(theStage, theUser); });
		
		setupLabelUI(label_SelectUser, "Arial", 20, 300, Pos.BASELINE_LEFT, 20, 130);
		
		setupComboBoxUI(combobox_SelectUser, "Dialog", 16, 250, 280, 125);
		List<String> userList = theDatabase.getUserList();	
		combobox_SelectUser.setItems(FXCollections.observableArrayList(userList));
		combobox_SelectUser.getSelectionModel().select(0);
		combobox_SelectUser.getSelectionModel().selectedItemProperty()
    	.addListener((@SuppressWarnings("unused") ObservableValue<? extends String> observable, 
    		@SuppressWarnings("unused") String oldvalue, 
    		@SuppressWarnings("unused") String newValue) -> {ControllerAddRemoveRoles.doSelectUser();});
		
		setupLabelUI(label_CurrentRoles, "Arial", 16, 300, Pos.BASELINE_LEFT, 50, 170);	
		setupLabelUI(label_SelectRoleToBeAdded, "Arial", 20, 300, Pos.BASELINE_LEFT, 20, 210);
		setupComboBoxUI(combobox_SelectRoleToAdd, "Dialog", 16, 150, 280, 205);
		setupButtonUI(button_AddRole, "Dialog", 16, 150, Pos.CENTER, 460, 205);
		ViewAddRemoveRoles.button_AddRole.setOnAction((_) -> 
			{ControllerAddRemoveRoles.performAddRole(); });
		setupButtonUI(button_RemoveRole, "Dialog", 16, 150, Pos.CENTER, 460, 275);			
		ViewAddRemoveRoles.button_RemoveRole.setOnAction((_) -> 
			{ControllerAddRemoveRoles.performRemoveRole(); });
		setupLabelUI(label_SelectRoleToBeRemoved, "Arial", 20, 300, Pos.BASELINE_LEFT, 20, 280);	
		setupComboBoxUI(combobox_SelectRoleToRemove, "Dialog", 16, 150, 280, 275);	
		
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
		button_Return.setOnAction((_) -> {ControllerAddRemoveRoles.performReturn(); });

		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
		button_Logout.setOnAction((_) -> {ControllerAddRemoveRoles.performLogout(); });
    
		setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 540);
		button_Quit.setOnAction((_) -> {ControllerAddRemoveRoles.performQuit(); });
	}	

	/**
	 * Private local method to initialize the standard fields for a label.
	 * @param l The Label object
	 * @param ff The font family
	 * @param f The font size
	 * @param w The width
	 * @param p The alignment
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 */
	private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x,
			double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}
	
	/**
	 * Protected method to initialize the standard fields for a button.
	 * @param b The Button object
	 * @param ff The font family
	 * @param f The font size
	 * @param w The width
	 * @param p The alignment
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 */
	protected static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x,
			double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}

	/**
	 * Protected method to initialize the standard fields for a ComboBox.
	 * @param c The ComboBox object
	 * @param ff The font family
	 * @param f The font size
	 * @param w The width
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 */
	protected static void setupComboBoxUI(ComboBox <String> c, String ff, double f, double w,
			double x, double y){
		c.setStyle("-fx-font: " + f + " " + ff + ";");
		c.setMinWidth(w);
		c.setLayoutX(x);
		c.setLayoutY(y);
	}
}