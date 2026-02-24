package guiOneTimePassword;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;
import javafx.scene.control.Alert;

/**
 * <p> Title: ViewOneTimePassword Class. </p>
 * <p> Description: This class provides the user interface for an administrator 
 * to generate and assign a temporary (one-time) password to a selected user. </p>
 */
public class ViewOneTimePassword {
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
	
	/** Label for the page title. */
	protected static Label label_PageTitle = new Label();
	/** Label for user details display. */
	protected static Label label_UserDetails = new Label();
	/** Label prompting the admin to select a user from the list. */
	protected static Label label_SelectUser = new Label("Select a user to be updated:");
	/** Button to navigate to the account update page. */
	protected static Button button_UpdateThisUser = new Button("Account Update");
	/** Button to confirm and save the temporary password change. */
	protected static Button button_confirmPassword = new Button("Confirm Password Change");
	
	/** PasswordField for entering the new temporary password. */
	public static PasswordField TempText_Password1 = new PasswordField();
	/** PasswordField for confirming the new temporary password. */
	protected static PasswordField TempText_Password2 = new PasswordField();
	private static String TempPassword = TempText_Password1.getText();
	
	/** The primary Stage used to display this view. */
	protected static Stage theStage;
	/** The current User interacting with the system. */
	protected static User theUser;
	/** The username of the user currently selected in the ComboBox. */
	protected static String theSelectedUser = "<Select a User>";
	
	private static ViewOneTimePassword theView;
	/** ComboBox used to select a user from the database. */
	protected static ComboBox <String> combobox_SelectUser = new ComboBox <String>();
	
	/** The Pane that holds all the GUI widgets for this view. */
	protected static Pane theRootPane;
	/** The Scene representing the one-time password page. */
	public static Scene oneTimePasswordScene = null; 
	/** Label representing the visual title on the page. */
	protected static Label labelPageTitle = new Label();
	
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	/** List of roles or attributes to be added. */
	protected static List<String> addList = new ArrayList<String>();
	/** List of roles or attributes to be removed. */
	protected static List<String> removeList = new ArrayList<String>();
	
	/** Button to return to the previous admin home page. */
	protected static Button button_Return = new Button("Return");
	/** Button to log out of the current session. */
	protected static Button button_Logout = new Button("Logout");
	/** Button to exit the application. */
	protected static Button button_Quit = new Button("Quit");
	
	/** Information alert confirming a successful password change. */
	protected static Alert displayConfirmation = new Alert(AlertType.INFORMATION);
	/** Error alert shown when passwords do not match. */
	protected static Alert displayError = new Alert(AlertType.INFORMATION);
	/** Error alert shown when password fields are empty. */
	protected static Alert displayError2 = new Alert(AlertType.INFORMATION);
	
	/**
	 * <p> Method: displayOneTimePassword </p>
	 * <p> Description: Main entry point for displaying this view. Initializes the view if 
	 * it doesn't exist and sets the scene on the stage. </p>
	 * @param ps The JavaFX Stage to be used.
	 * @param user The current User object.
	 */
	public static void displayOneTimePassword(Stage ps, User user) {
		theStage = ps;
		theUser = user;
		
		if(theView == null) {
			theView = new ViewOneTimePassword();
		}
		
		combobox_SelectUser.getSelectionModel().select(0);
		
		theStage.setTitle("Create A temporary Password!");
		theStage.setScene(oneTimePasswordScene);
		theStage.show();
		
		ControllerOneTimePassword.repaintWindow();
		ControllerOneTimePassword.doSelectUser();
	}
	
	/**
	 * <p> Constructor: ViewOneTimePassword </p>
	 * <p> Description: Initializes the GUI components, layout, and event handlers 
	 * for the One-Time Password view. </p>
	 */
	public ViewOneTimePassword() {
		theRootPane = new Pane();
		
		oneTimePasswordScene = new Scene(theRootPane, width, height);
		labelPageTitle.setText("Create a Temporary Password");
		setupLabelUI(labelPageTitle, "Arial", 25, width, Pos.CENTER, 10, 10);
		
		setupLabelUI(label_SelectUser, "Arial", 20, 300, Pos.BASELINE_LEFT, 20, 130);
		
		setupComboBoxUI(combobox_SelectUser, "Dialog", 16, 250, 280, 125);
		List<String> userList = theDatabase.getUserList();

		userList.removeIf(u -> u == null || u.trim().isEmpty());

		combobox_SelectUser.setItems(FXCollections.observableArrayList(userList));
		combobox_SelectUser.getSelectionModel().select(0);
		combobox_SelectUser.getSelectionModel().selectedItemProperty()
    	.addListener((@SuppressWarnings("unused") ObservableValue<? extends String> observable, 
    		@SuppressWarnings("unused") String oldvalue, 
    		@SuppressWarnings("unused") String newValue) -> {ControllerOneTimePassword.doSelectUser();});
		
		setupTextUI(TempText_Password1, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 210, true);
		TempText_Password1.setPromptText("Enter Temp Password");
		TempText_Password1.textProperty().addListener((_, _, _)
				-> {ControllerOneTimePassword.setAdminPassword1(); });

		setupTextUI(TempText_Password2, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 260, true);
		TempText_Password2.setPromptText("Enter Temp Password Again");
		TempText_Password2.textProperty().addListener((_, _, _) 
				-> {ControllerOneTimePassword.setAdminPassword2(); });
		
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
		button_Return.setOnAction((_) -> {ControllerOneTimePassword.performReturn(); });
		
		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
		button_Logout.setOnAction((_) -> {ControllerOneTimePassword.performLogout(); });
    
		setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 540);
		button_Quit.setOnAction((_) -> {ControllerOneTimePassword.performQuit(); });
		
		setupButtonUI(button_confirmPassword, "Dialog", 18, 210, Pos.CENTER, 50, 310);
		button_confirmPassword.setOnAction((_) -> {ControllerOneTimePassword.doOneTimePassword();});
		
		displayConfirmation.setTitle("Congrats");
		displayConfirmation.setHeaderText("You have succesfully created a tempoaray password, please log out");
		displayConfirmation.setContentText("One you enter you're new password it will be erased and you will have to create a permentant password");
		
		displayError.setTitle("Error!!");
		displayError.setHeaderText("Passwords do Not Match, Please Confirm They are the Same");
		
		displayError2.setTitle("Error!!");
		displayError2.setHeaderText("Passwords are empty, please enter a valid password");
		
		theRootPane.getChildren().addAll(labelPageTitle, label_SelectUser, combobox_SelectUser, button_Return, button_Logout, button_Quit,
										 TempText_Password1, TempText_Password2, button_confirmPassword);	
		
	}
	
	 private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y) {
	        l.setFont(Font.font(ff, f));
	        l.setMinWidth(w);
	        l.setAlignment(p);
	        l.setLayoutX(x);
	        l.setLayoutY(y);
	    }
	 
	 protected static void setupComboBoxUI(ComboBox <String> c, String ff, double f, double w,
				double x, double y){
			c.setStyle("-fx-font: " + f + " " + ff + ";");
			c.setMinWidth(w);
			c.setLayoutX(x);
			c.setLayoutY(y);
		}
	 	
		protected static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x,
				double y){
			b.setFont(Font.font(ff, f));
			b.setMinWidth(w);
			b.setAlignment(p);
			b.setLayoutX(x);
			b.setLayoutY(y);		
		}
		
		private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, 
				boolean e){
			t.setFont(Font.font(ff, f));
			t.setMinWidth(w);
			t.setMaxWidth(w);
			t.setAlignment(p);
			t.setLayoutX(x);
			t.setLayoutY(y);		
			t.setEditable(e);
		}
}