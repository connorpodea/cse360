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
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;
import guiAddRemoveRoles.ControllerAddRemoveRoles;
import guiFirstAdmin.ControllerFirstAdmin;
import javafx.scene.control.Alert;

public class ViewOneTimePassword{
	
	// creating a new page, when the user clicks button it takes them to the page where they
	// can set up a one time password. 
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
	
	protected static Label label_PageTitle = new Label();
	protected static Label label_UserDetails = new Label();
	protected static Label label_SelectUser = new Label("Select a user to be updated:");
	protected static Button button_UpdateThisUser = new Button("Account Update");
	protected static Button button_confirmPassword = new Button("Confirm Password Change");
	
	
	// text fields, where the user will be prompted to enter a new one time password.
	public static PasswordField TempText_Password1 = new PasswordField();
	protected static PasswordField TempText_Password2 = new PasswordField();
	private static String TempPassword = TempText_Password1.getText();
	
	
	protected static Stage theStage;
	protected static User theUser;
	protected static String theSelectedUser = "<Select a User>";
	
	private static ViewOneTimePassword theView;
	protected static ComboBox <String> combobox_SelectUser = new ComboBox <String>();
	
	
	protected static Pane theRootPane;			// The Pane that holds all the GUI widgets 
	public static Scene oneTimePasswordScene = null; 
	protected static Label labelPageTitle = new Label();
	
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	
	// lists
	protected static List<String> addList = new ArrayList<String>();
	protected static List<String> removeList = new ArrayList<String>();
	
	// buttons to return to main admin page, quit program, or logout:
	protected static Button button_Return = new Button("Return");
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");
	
	// pop up alerts
	protected static Alert displayConfirmation = new Alert(AlertType.INFORMATION);
	protected static Alert displayError = new Alert(AlertType.INFORMATION);
	protected static Alert displayError2 = new Alert(AlertType.INFORMATION);
	
	
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
	
	public ViewOneTimePassword() {
		theRootPane = new Pane();
		
		oneTimePasswordScene = new Scene(theRootPane, width, height);
		labelPageTitle.setText("Create a Temporary Password");
		setupLabelUI(labelPageTitle, "Arial", 25, width, Pos.CENTER, 10, 10);
		
		setupLabelUI(label_SelectUser, "Arial", 20, 300, Pos.BASELINE_LEFT, 20, 130);
		
		//drop down box, will display a list of users currently in the database.
		setupComboBoxUI(combobox_SelectUser, "Dialog", 16, 250, 280, 125);
		List<String> userList = theDatabase.getUserList();


		userList.removeIf(u -> u == null || u.trim().isEmpty());

		combobox_SelectUser.setItems(FXCollections.observableArrayList(userList));
		combobox_SelectUser.getSelectionModel().select(0);
		combobox_SelectUser.getSelectionModel().selectedItemProperty()
    	.addListener((@SuppressWarnings("unused") ObservableValue<? extends String> observable, 
    		@SuppressWarnings("unused") String oldvalue, 
    		@SuppressWarnings("unused") String newValue) -> {ControllerOneTimePassword.doSelectUser();});
		
		// will display the password fields so the user can enter text.
				setupTextUI(TempText_Password1, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 210, 
						true);
				TempText_Password1.setPromptText("Enter Temp Password");
				TempText_Password1.textProperty().addListener((_, _, _)
						-> {ControllerOneTimePassword.setAdminPassword1(); });

				// Establish the text input operand field for the password
				setupTextUI(TempText_Password2, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 260, 
						true);
				TempText_Password2.setPromptText("Enter Temp Password Again");
				TempText_Password2.textProperty().addListener((_, _, _) 
						-> {ControllerOneTimePassword.setAdminPassword2(); });
		
		
		// will be able to return back to the main page.
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
		button_Return.setOnAction((_) -> {ControllerOneTimePassword.performReturn(); });
		
		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
		button_Logout.setOnAction((_) -> {ControllerOneTimePassword.performLogout(); });
    
		setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 540);
		button_Quit.setOnAction((_) -> {ControllerOneTimePassword.performQuit(); });
		
		// button to confirm password change.
		setupButtonUI(button_confirmPassword, "Dialog", 18, 210, Pos.CENTER, 300, 300);
		button_confirmPassword.setOnAction((_) -> {ControllerOneTimePassword.doOneTimePassword();});
		
		// setting up pop up alert confirming the password.
		displayConfirmation.setTitle("Congrats");
		displayConfirmation.setHeaderText("You have succesfully created a tempoaray password, please log out");
		displayConfirmation.setContentText("One you enter you're new password it will be erased and you will have to create a permentant password");
		
		// Display Error message if passwords are not the same.
		displayError.setTitle("Error!!");
		displayError.setHeaderText("Passwords do Not Match, Please Confirm They are the Same");
		
		// display error if empty
		displayError2.setTitle("Error!!");
		displayError2.setHeaderText("Passwords are empty, please enter a valid password");
		
		// making it so that it actually displays the buttons, text, and drop down features.
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